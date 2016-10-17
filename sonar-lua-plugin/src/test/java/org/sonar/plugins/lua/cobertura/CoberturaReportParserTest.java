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
package org.sonar.plugins.lua.cobertura;

import org.junit.Ignore;
import org.junit.Test;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.test.TestUtils;

import java.io.File;

public class CoberturaReportParserTest {


  @Test(expected = IllegalStateException.class)
  public void invalidXmlFile() throws Exception {
    CoberturaReportParser.parseReport(
      TestUtils.getResource("org/sonar/plugins/lua/cobertura/coverage-invalid.xml"),
      SensorContextTester.create(new File("."))
    );
  }
}
