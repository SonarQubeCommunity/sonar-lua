/*
 * SonarQube Lua Plugin
 * Copyright (C) 2016
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
package org.sonar.lua.metrics;
import com.sonar.sslr.api.AstNode;
import org.sonar.lua.api.LuaMetric;
import org.sonar.lua.grammar.LuaGrammar;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.sslr.parser.LexerlessGrammar;

public class ComplexityVisitor extends SquidAstVisitor<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(
      // Entry points
      LuaGrammar.FUNCTION,
      LuaGrammar.FUNCSTAT,
      LuaGrammar.LOCALFUNCSTAT,
     
      // Branching nodes
      LuaGrammar.Keyword.AND,
      LuaGrammar.Keyword.OR,
      LuaGrammar.WHILE_STATEMENT,
      LuaGrammar.FOR_STATEMENT,
      LuaGrammar.IF_STATEMENT,
      LuaGrammar.DO_STATEMENT,
      LuaGrammar.REPEAT_STATEMENT,
      LuaGrammar.Keyword.ELSEIF,
      LuaGrammar.TAILCALL,
      LuaGrammar.Keyword.BREAK
      
      );
     
  }
    @Override
    public void visitNode(AstNode astNode) {
      getContext().peekSourceCode().add(LuaMetric.COMPLEXITY, 1);
    }

  

  }
