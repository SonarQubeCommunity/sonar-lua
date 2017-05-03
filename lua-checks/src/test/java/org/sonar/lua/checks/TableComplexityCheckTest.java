/*
 * SonarQube Lua Plugin
 * Copyright (C) 2016 
 * mailto:fati.ahmadi AT gmail DOT com
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
import org.sonar.squidbridge.api.SourceFunction;

import java.io.File;

public class TableComplexityCheckTest {

  @Test
  public void test() {
    TableComplexityCheck check = new TableComplexityCheck();
    check.setMaximumTableComplexityThreshold(0);

    SourceFile file = LuaAstScanner.scanSingleFile(new File("src/test/resources/checks/tableComplexity.lua"), check);

    CheckMessagesVerifier.verify(file.getCheckMessages())
       .next().atLine(1).withMessage("Table has a complexity of 1 which is greater than 0 authorized.")
       
        .noMore();
  }

}
