/*
 * SonarQube Lua Plugin
 * Copyright (C) 2013-2016-2016 SonarSource SA
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
package org.sonar.plugins.lua;

import com.google.common.collect.ImmutableList;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.ce.measure.RangeDistributionBuilder;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.lua.LuaAstScanner;
import org.sonar.lua.LuaConfiguration;
import org.sonar.lua.api.LuaMetric;
import org.sonar.lua.checks.CheckList;

import org.sonar.lua.metrics.FileLinesVisitor;
import org.sonar.plugins.lua.core.Lua;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.api.CheckMessage;
import org.sonar.squidbridge.api.SourceClass;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceFunction;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.squidbridge.indexer.QueryByParent;
import org.sonar.squidbridge.indexer.QueryByType;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class LuaSquidSensor implements Sensor {

  private static final Number[] FUNCTIONS_DISTRIB_BOTTOM_LIMITS = {1, 2, 4, 6, 8, 10, 12};
  private static final Number[] FILES_DISTRIB_BOTTOM_LIMITS = {0, 5, 10, 20, 30, 60, 90};

  private final Checks<SquidCheck<LexerlessGrammar>> checks;
  private final FileLinesContextFactory fileLinesContextFactory;

  private AstScanner<LexerlessGrammar> scanner;

  public LuaSquidSensor(CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory) {
    this.checks = checkFactory
      .<SquidCheck<LexerlessGrammar>>create(CheckList.REPOSITORY_KEY)
      .addAnnotatedChecks((Iterable) CheckList.getChecks());
    this.fileLinesContextFactory = fileLinesContextFactory;
  }

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor
      .name("Lua")
      .onlyOnFileType(InputFile.Type.MAIN)
      .onlyOnLanguage(Lua.KEY);
  }

  @Override
  public void execute(SensorContext context) {
    FileSystem fileSystem = context.fileSystem();
    FilePredicates predicates = fileSystem.predicates();
    List<SquidAstVisitor<LexerlessGrammar>> visitors = new ArrayList<>(checks.all());
    visitors.add(new FileLinesVisitor(fileLinesContextFactory, fileSystem));
    LuaConfiguration configuration = new LuaConfiguration(fileSystem.encoding());

    scanner = LuaAstScanner.create(configuration, visitors);

    Iterable<java.io.File> files = fileSystem.files(
      predicates.and(
        predicates.hasType(InputFile.Type.MAIN),
        predicates.hasLanguage(Lua.KEY),
        inputFile -> !inputFile.absolutePath().endsWith("mxml")
      ));
    scanner.scanFiles(ImmutableList.copyOf(files));

    Collection<SourceCode> squidSourceFiles = scanner.getIndex().search(new QueryByType(SourceFile.class));
    save(context, squidSourceFiles);
  }

  private void save(SensorContext context, Collection<SourceCode> squidSourceFiles) {
    FileSystem fileSystem = context.fileSystem();
    for (SourceCode squidSourceFile : squidSourceFiles) {
      SourceFile squidFile = (SourceFile) squidSourceFile;

      InputFile inputFile = fileSystem.inputFile(fileSystem.predicates().hasPath(squidFile.getKey()));

      saveClassComplexity(context, inputFile, squidFile);
      saveMeasures(context, inputFile, squidFile);
      saveFunctionsComplexityDistribution(context, inputFile, squidFile);
      saveFilesComplexityDistribution(context, inputFile, squidFile);
      saveViolations(context, inputFile, squidFile);
    }
  }

  private static void saveMeasures(SensorContext context, InputFile inputFile, SourceFile squidFile) {
    context.<Integer>newMeasure()
      .on(inputFile)
      .forMetric(CoreMetrics.NCLOC)
      .withValue(squidFile.getInt(LuaMetric.LINES_OF_CODE))
      .save();
    context.<Integer>newMeasure()
    .on(inputFile)
    .forMetric(CoreMetrics.CLASSES)
    .withValue(squidFile.getInt(LuaMetric.TABLECONSTRUCTORS))
    .save();
    context.<Integer>newMeasure()
      .on(inputFile)
      .forMetric(CoreMetrics.COMMENT_LINES)
      .withValue(squidFile.getInt(LuaMetric.COMMENT_LINES))
      .save();
    
    context.<Integer>newMeasure()
      .on(inputFile)
      .forMetric(CoreMetrics.FUNCTIONS)
      .withValue(squidFile.getInt(LuaMetric.FUNCTIONS))
      .save();
    context.<Integer>newMeasure()
      .on(inputFile)
      .forMetric(CoreMetrics.STATEMENTS)
      .withValue(squidFile.getInt(LuaMetric.STATEMENTS))
      .save();
    context.<Integer>newMeasure()
      .on(inputFile)
      .forMetric(CoreMetrics.COMPLEXITY)
      .withValue(squidFile.getInt(LuaMetric.COMPLEXITY))
      .save();
  }

  private void saveClassComplexity(SensorContext context, InputFile inputFile, SourceFile squidFile) {
    Collection<SourceCode> classes = scanner.getIndex().search(new QueryByParent(squidFile), new QueryByType(SourceClass.class));
    int complexityInClasses = 0;
    for (SourceCode squidClass : classes) {
      int classComplexity = squidClass.getInt(LuaMetric.COMPLEXITY);
      complexityInClasses += classComplexity;
    }
    context.<Integer>newMeasure()
      .on(inputFile)
      .forMetric(CoreMetrics.COMPLEXITY_IN_CLASSES)
      .withValue(complexityInClasses)
      .save();
  }

  private void saveFunctionsComplexityDistribution(SensorContext context, InputFile inputFile, SourceFile squidFile) {
    Collection<SourceCode> squidFunctionsInFile = scanner.getIndex().search(new QueryByParent(squidFile), new QueryByType(SourceFunction.class));
    RangeDistributionBuilder complexityDistribution = new RangeDistributionBuilder(FUNCTIONS_DISTRIB_BOTTOM_LIMITS);
    for (SourceCode squidFunction : squidFunctionsInFile) {
      complexityDistribution.add(squidFunction.getDouble(LuaMetric.COMPLEXITY));
    }
    context.<String>newMeasure()
      .on(inputFile)
      .forMetric(CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION)
      .withValue(complexityDistribution.build())
      .save();
  }

  private static void saveFilesComplexityDistribution(SensorContext context, InputFile inputFile, SourceFile squidFile) {
    String distribution = new RangeDistributionBuilder(FILES_DISTRIB_BOTTOM_LIMITS)
      .add(squidFile.getDouble(LuaMetric.COMPLEXITY))
      .build();
    context.<String>newMeasure()
      .on(inputFile)
      .forMetric(CoreMetrics.FILE_COMPLEXITY_DISTRIBUTION)
      .withValue(distribution)
      .save();
  }

  private void saveViolations(SensorContext context, InputFile inputFile, SourceFile squidFile) {
    Collection<CheckMessage> messages = squidFile.getCheckMessages();
    if (messages != null) {

      for (CheckMessage message : messages) {
        RuleKey ruleKey = checks.ruleKey((SquidCheck<LexerlessGrammar>) message.getCheck());
        NewIssue newIssue = context.newIssue()
          .forRule(ruleKey)
          .gap(message.getCost());
        Integer line = message.getLine();
        NewIssueLocation location = newIssue.newLocation()
          .on(inputFile)
          .message(message.getText(Locale.ENGLISH));
        if (line != null) {
          location.at(inputFile.selectLine(line));
        }
        newIssue.at(location);
        newIssue.save();
      }
    }
  }

}
