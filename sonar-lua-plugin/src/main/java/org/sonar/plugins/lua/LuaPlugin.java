package org.sonar.plugins.lua;

import java.util.List;

import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;
import org.sonar.plugins.lua.Lua;
import org.sonar.plugins.lua.cobertura.CoberturaSensor;



public class LuaPlugin implements Plugin {

  public static final String FILE_SUFFIXES_KEY = "sonar.lua.file.suffixes";
  public static final String COBERTURA_REPORT_PATH = "sonar.lua.cobertura.reportPath";

  @Override
  public void define(Context context) {
    context.addExtensions(
      Lua.class,

      LuaSquidSensor.class,
      CoberturaSensor.class,

      LuaRulesDefinition.class,
      LuaProfile.class,

      PropertyDefinition.builder(FILE_SUFFIXES_KEY)
        .defaultValue(Lua.DEFAULT_FILE_SUFFIXES)
        .name("File suffixes")
        .description("Comma-separated list of suffixes for files to analyze. To not filter, leave the list empty.")
        .onQualifiers(Qualifiers.MODULE, Qualifiers.PROJECT)
        .build(),

      PropertyDefinition.builder(COBERTURA_REPORT_PATH)
        .name("Cobertura xml report path")
        .description("Path to the Cobertura coverage report file. The path may be either absolute or relative to the project base directory.")
        .onQualifiers(Qualifiers.MODULE, Qualifiers.PROJECT)
        .build()
    );
  }
}