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
package org.sonar.lua.api;



import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

public enum LuaKeyword implements TokenType{
	
    AND("and"),
    BREAK("break"),
    DO("do"),
    ELSEIF("elseif"),
    ELSE("else"),
    END("end"),
    FALSE("false"),
    FOR("for"),
    FUNCTION("function"),
    IF("if"),
    IN("in"),
    LOCAL("local"),
    NIL("nil"),
    NOT("not"),
    OR("or"),
    REPEAT("repeat"),
    RETURN("return"),
    THEN("then"),
    TRUE("true"),
    UNTIL("until"),
    WHILE("while");

    private String value;

    private LuaKeyword(String word) {
      this.value = word;
    }

	@Override
	public String getName() {
		
		return name();
	}

	@Override
	public String getValue() {
		
		return value;
	}

	@Override
	public boolean hasToBeSkippedFromAst(AstNode node) {
		
		return false;
	}
	public static String[] keywordValues() {
	    LuaKeyword[] keywordsEnum = LuaKeyword.values();
	    String[] keywords = new String[keywordsEnum.length];
	    for (int i = 0; i < keywords.length; i++) {
	      keywords[i] = keywordsEnum[i].getValue();
	    }
	    return keywords;
	  }
  }
