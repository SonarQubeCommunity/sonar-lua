/*
 * SonarQube Lua Plugin
 * Copyright (C) 2013-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.sonar.plugins.lua.cobertura;
import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.in.SMInputCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.coverage.CoverageType;
import org.sonar.api.batch.sensor.coverage.NewCoverage;
import org.sonar.api.utils.ParsingUtils;
import org.sonar.api.utils.StaxParser;
import org.sonar.plugins.lua.Lua;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CoberturaReportParser {

  private static final Logger LOG = LoggerFactory.getLogger(CoberturaReportParser.class);

  private CoberturaReportParser() {
  }

  /**
   * Parse a Cobertura xml report and create measures accordingly
   */
  public static void parseReport(File xmlFile, final SensorContext context) {
    try {
      StaxParser parser = new StaxParser(rootCursor -> {
        rootCursor.advance();
        collectPackageMeasures(rootCursor.descendantElementCursor("package"), context);
      });
      parser.parse(xmlFile);
    } catch (XMLStreamException e) {
      throw new IllegalStateException(e);
    }
  }

  private static void collectPackageMeasures(SMInputCursor pack, SensorContext context) throws XMLStreamException {
    while (pack.getNext() != null) {
      collectFileMeasures(context, pack.descendantElementCursor("class"));
    }
  }

  private static void collectFileMeasures(SensorContext context, SMInputCursor clazz) throws XMLStreamException {
    FileSystem fileSystem = context.fileSystem();
    FilePredicates predicates = fileSystem.predicates();
    Map<String, InputFile> inputFileByFilename = new HashMap<>();
    while (clazz.getNext() != null) {
      String fileName = clazz.getAttrValue("filename");

      InputFile inputFile;
   // mxml files are not supported by the plugin
      if (inputFileByFilename.containsKey(fileName)) {
        inputFile = inputFileByFilename.get(fileName);
      } else {
        String key = fileName.startsWith(File.separator) ? fileName : (File.separator + fileName);
        inputFile = fileSystem.inputFile(predicates.and(
          predicates.matchesPathPattern("file:**" + key.replace(File.separator, "/")),
          predicates.hasType(InputFile.Type.MAIN),
          predicates.hasLanguage(Lua.KEY)));
        inputFileByFilename.put(fileName, inputFile);
        if (inputFile == null && !fileName.endsWith(".mxml")) {
          LOG.warn("Cannot save coverage result for file: {}, because resource not found.", fileName);
        }
      }
      if (inputFile != null) {
        collectFileData(
          clazz,
          context.newCoverage()
            .onFile(inputFile)
            .ofType(CoverageType.UNIT)
        );
      } else {
   
        SMInputCursor line = clazz.childElementCursor("lines").advance().childElementCursor("line");
        while (line.getNext() != null) {
          // advance
        }
      }
    }}
  

  private static void collectFileData(SMInputCursor clazz, NewCoverage newCoverage) throws XMLStreamException {
    SMInputCursor line = clazz.childElementCursor("lines").advance().childElementCursor("line");
    while (line.getNext() != null) {
      int lineId = Integer.parseInt(line.getAttrValue("number"));
      try {
        newCoverage.lineHits(lineId, (int) ParsingUtils.parseNumber(line.getAttrValue("hits"), Locale.ENGLISH));
      } catch (ParseException e) {
        throw new IllegalStateException(e);
      }

      String isBranch = line.getAttrValue("branch");
      String text = line.getAttrValue("condition-coverage");
      if (StringUtils.equals(isBranch, "true") && StringUtils.isNotBlank(text)) {
        String[] conditions = StringUtils.split(StringUtils.substringBetween(text, "(", ")"), "/");
        newCoverage.conditions(lineId, Integer.parseInt(conditions[1]), Integer.parseInt(conditions[0]));
      }
    }
    newCoverage.save();
  }
}
