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
package org.sonar.lua.checks.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.sonar.sslr.api.AstNode;
import org.sonar.lua.grammar.LuaGrammar;
import org.sonar.lua.grammar.LuaGrammar.Keyword;

import java.util.List;

public class Function {
    
    private Function() {
    }
    
    public static String getName(AstNode functionDef) {
        Preconditions.checkArgument(functionDef.is(LuaGrammar.LOCALFUNCSTAT));
        return functionDef.getFirstChild(LuaGrammar.NAME).getTokenValue();
        
    }
    
    public static String getLocalName(AstNode functionDef) {
        Preconditions.checkArgument(functionDef.is(LuaGrammar.LOCALFUNCSTAT));
        return functionDef.getFirstChild(LuaGrammar.NAME).getTokenValue();
        
    }
    
    public static boolean isEmptyConstructor(AstNode functionDef, String className) {
        Preconditions.checkArgument(functionDef.is(LuaGrammar.LOCALFUNCSTAT));
        AstNode functionBlock = functionDef.getFirstChild(LuaGrammar.FUNCBODY).getFirstChild(LuaGrammar.BLOCK);
        
        return isConstructor(functionDef, className)
        && (functionBlock == null || functionBlock.getFirstChild(LuaGrammar.CHUNK).getChildren().isEmpty());
    }
    
    public static boolean isConstructor(AstNode functionDef, String className) {
        Preconditions.checkArgument(functionDef.is(LuaGrammar.LOCALFUNCSTAT));
        return functionDef.getFirstChild(LuaGrammar.NAME).getNumberOfChildren() == 1
        && functionDef.getFirstChild(LuaGrammar.NAME).getFirstChild().getTokenValue().equals(className);
    }
    
    
}


