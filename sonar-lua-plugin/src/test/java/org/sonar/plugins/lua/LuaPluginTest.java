package org.sonar.plugins.lua;
import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.utils.Version;

import static org.fest.assertions.Assertions.assertThat;

public class LuaPluginTest {

  @Test
  public void testGetExtensions() throws Exception {
    Plugin.Context context = new Plugin.Context(Version.create(5, 6));
    new LuaPlugin().define(context);
    assertThat(context.getExtensions()).isNotEmpty();
  }

}