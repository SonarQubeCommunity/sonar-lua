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
package org.sonar.lua.grammar;

import org.junit.Test;
import org.sonar.sslr.parser.LexerlessGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ForStatementTest {

  private LexerlessGrammar g = LuaGrammar.createGrammar();

  @Test
  public void test() {
    assertThat(g.rule(LuaGrammar.FOR_STATEMENT))
        .matches("for index = 1,5 do print(index) end")
        .matches("for index = start,finish,delta do print(index) end")
        .matches("for key,value in pairs(_G) do print(key,value) end");
  }

}
