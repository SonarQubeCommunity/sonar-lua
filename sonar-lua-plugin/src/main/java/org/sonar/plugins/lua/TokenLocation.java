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
package org.sonar.plugins.lua;

import com.sonar.sslr.api.Token;

public class TokenLocation {

  private final int startLine;
  private final int startCharacter;
  private final int endLine;
  private final int endCharacter;

  public TokenLocation(Token token) {
    this.startLine = token.getLine();
    this.startCharacter = token.getColumn();
    final String[] lines = token.getOriginalValue().split("\r\n|\n|\r", -1);
    if (lines.length > 1) {
      this.endLine = token.getLine() + lines.length - 1;
      this.endCharacter = lines[lines.length - 1].length();
    } else {
      this.endLine = startLine;
      this.endCharacter = startCharacter + token.getOriginalValue().length();
    }
  }

  public int startLine() {
    return startLine;
  }

  public int startCharacter() {
    return startCharacter;
  }

  public int endLine() {
    return endLine;
  }

  public int endCharacter() {
    return endCharacter;
  }

}
