package org.sonar.plugins.lua.cobertura;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.plugins.lua.LuaPlugin;
import org.sonar.plugins.lua.Lua;

import java.io.File;

public class CoberturaSensor implements Sensor {

  private static final Logger LOGGER = LoggerFactory.getLogger(CoberturaSensor.class);

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor
      .name("Lua Cobertura")
      .onlyOnFileType(InputFile.Type.MAIN)
      .onlyOnLanguage(Lua.KEY);
  }

  @Override
  public void execute(SensorContext context) {
    String reportPath = context.settings().getString(LuaPlugin.COBERTURA_REPORT_PATH);

    if (reportPath != null) {
      File xmlFile = getIOFile(context.fileSystem(), reportPath);

      if (xmlFile.exists()) {
        LOGGER.info("Analyzing Cobertura report: " + reportPath);
        CoberturaReportParser.parseReport(xmlFile, context);
      } else {
        LOGGER.info("Cobertura xml report not found: " + reportPath);
      }
    } else {
      LOGGER.info("No Cobertura report provided (see '" + LuaPlugin.COBERTURA_REPORT_PATH + "' property)");
    }
  }

  /**
   * Returns a java.io.File for the given path.
   * If path is not absolute, returns a File with module base directory as parent path.
   */
  private static File getIOFile(FileSystem fileSystem, String path) {
    File file = new File(path);
    if (!file.isAbsolute()) {
      file = new File(fileSystem.baseDir(), path);
    }

    return file;
  }

}