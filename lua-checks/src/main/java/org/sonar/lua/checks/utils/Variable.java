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
package org.sonar.lua.checks.utils;



import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.sonar.sslr.api.AstNode;
import org.sonar.lua.grammar.LuaGrammar;


import java.util.List;

public final class Variable {

  private Variable() {
  }

  public static String getName(AstNode varDeclStatement) {
    Preconditions.checkArgument(varDeclStatement.is(LuaGrammar.STATEMENT));
    return varDeclStatement
      .getFirstChild(LuaGrammar.VARLIST)
      .getFirstChild(LuaGrammar.VAR)
      .getFirstChild(LuaGrammar.NAME).getTokenValue();
  }
      
  

  public static boolean isVariable(AstNode directive) {
    Preconditions.checkArgument(directive.is(LuaGrammar.EXP));
    if (directive.getFirstChild(LuaGrammar.VALUE) != null) {
      AstNode variableDecStmt = directive.getFirstChild(LuaGrammar.VAR).getFirstChild(LuaGrammar.NAME);

      if (variableDecStmt != null) {
        return variableDecStmt
          .getFirstChild(LuaGrammar.NAME)
         
          .getFirstChild().is(LuaGrammar.Keyword.LOCAL);
      }
    }
    return false;
  }

  

  public static List<AstNode> getDeclaredIdentifiers(AstNode varDeclStatement) {
    Preconditions.checkArgument(varDeclStatement.is(LuaGrammar.STATEMENT));
    List<AstNode> identifiers = Lists.newArrayList();
    if (varDeclStatement.is(LuaGrammar.STATEMENT)) {
      AstNode varBindingList = varDeclStatement
        .getFirstChild(LuaGrammar.VARLIST)
        .getFirstChild(LuaGrammar.VAR);

      for (AstNode varBinding : varBindingList.getChildren(LuaGrammar.NAME)) {
        identifiers.add(varBinding.getFirstChild(LuaGrammar.NAME));
      }
    }
    return identifiers;
  }
}
