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
package org.sonar.lua.checks;

import java.io.File;

import org.junit.Test;
import org.sonar.lua.LuaAstScanner;

import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

public class FileComplexityCheckTest {

  private FileComplexityCheck check = new FileComplexityCheck();

  @Test
  public void test() {
    check.setMaximumFileComplexityThreshold(0);

    SourceFile file = LuaAstScanner.scanSingleFile(new File("src/test/resources/checks/fileComplexity.lua"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
        .next().withMessage("File has a complexity of 1 which is greater than 0"+ " authorized.")
        .noMore();
  }

  @Test
  public void defaults() {
    SourceFile file = LuaAstScanner.scanSingleFile(new File("src/test/resources/checks/fileComplexity.lua"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
        .noMore();
  }

}