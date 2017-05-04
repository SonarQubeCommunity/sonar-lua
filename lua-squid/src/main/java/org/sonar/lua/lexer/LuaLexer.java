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
package org.sonar.lua.lexer;

import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.channel.BlackHoleChannel;
import com.sonar.sslr.impl.channel.IdentifierAndKeywordChannel;
import com.sonar.sslr.impl.channel.PunctuatorChannel;
import com.sonar.sslr.impl.channel.UnknownCharacterChannel;
import org.sonar.lua.LuaConfiguration;
import org.sonar.lua.api.LuaKeyword;
import org.sonar.lua.api.LuaPunctuator;
import org.sonar.lua.api.LuaTokenType;
import org.sonar.sslr.channel.Channel;
import org.sonar.sslr.channel.CodeReader;

import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.commentRegexp;
import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.regexp;

public final class LuaLexer {

  private static final String EXP = "([Ee][+-]?+[0-9_]++)";

  private LuaLexer() {
  }

  public static Lexer create(LuaConfiguration conf) {
    return Lexer.builder()
      .withCharset(conf.getCharset())

      .withFailIfNoChannelToConsumeOneCharacter(true)

      .withChannel(new BomCharacterChannel())
      .withChannel(new BlackHoleChannel("\\s++"))

      // Comments
      .withChannel(commentRegexp("//[^\\n\\r]*+"))
      .withChannel(commentRegexp("/\\*[\\s\\S]*?\\*/"))

      // String Literals
      .withChannel(regexp(GenericTokenType.LITERAL, "\"([^\"\\\\]*+(\\\\[\\s\\S])?+)*+\""))
      .withChannel(regexp(GenericTokenType.LITERAL, "\'([^\'\\\\]*+(\\\\[\\s\\S])?+)*+\'"))

      // Regular Expression Literal
      .withChannel(new LuaRegularExpressionLiteralChannel())

      // Numbers
      .withChannel(regexp(LuaTokenType.NUMERIC_LITERAL, "0[xX][0-9a-fA-F]++"))
      .withChannel(regexp(LuaTokenType.NUMERIC_LITERAL, "[0-9]++\\.([0-9]++)?+" + EXP + "?+"))
      .withChannel(regexp(LuaTokenType.NUMERIC_LITERAL, "\\.[0-9]++" + EXP + "?+"))
      .withChannel(regexp(LuaTokenType.NUMERIC_LITERAL, "[0-9]++" + EXP + "?+"))

      .withChannel(new IdentifierAndKeywordChannel("\\p{javaJavaIdentifierStart}++\\p{javaJavaIdentifierPart}*+", true, LuaKeyword.values()))
      .withChannel(new PunctuatorChannel(LuaPunctuator.values()))

      .withChannel(new UnknownCharacterChannel())

      .build();
  }

  private static class BomCharacterChannel extends Channel<Lexer> {

    @Override
    public boolean consume(CodeReader code, Lexer lexer) {
      if (code.peek() == '\ufeff') {
        code.pop();
        code.setColumnPosition(code.getColumnPosition() - 1);
        return true;
      } else {
        return false;
      }
    }
  }

}