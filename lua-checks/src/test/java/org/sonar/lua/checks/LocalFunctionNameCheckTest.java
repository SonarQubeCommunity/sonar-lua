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
import org.junit.Test;
import org.sonar.lua.LuaAstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

import java.io.File;

public class LocalFunctionNameCheckTest {

  private LocalFunctionNameCheck check = new LocalFunctionNameCheck();

  @Test
  public void defaultFormat() {
    SourceFile file = LuaAstScanner.scanSingleFile(new File("src/test/resources/checks/localFunctionName.lua"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())

   //.next().atLine(1).withMessage("Rename this \"SOMETHING\" function to match the regular expression " + check.format)
    .next().atLine(1).withMessage("Rename this \"_Test\" function to match the regular expression " + check.format)
   // .next().atLine(13).withMessage("Rename this \"d_Test\" function to match the regular expression " + check.format)
    .noMore();
  }

  @Test
  public void custom() {
    check.format = "^[a-z][a-z_A-Z0-9]*$";
   


    SourceFile file = LuaAstScanner.scanSingleFile(new File("src/test/resources/checks/localFunctionName.lua"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
    //.next().atLine(1).withMessage("Rename this \"SOMETHING\" function to match the regular expression " + check.format)
      .next().atLine(1).withMessage("Rename this \"_Test\" function to match the regular expression " + check.format)
     // .next().atLine(13).withMessage("Rename this \"d_Test\" function to match the regular expression " + check.format)
      .noMore();
  }
}