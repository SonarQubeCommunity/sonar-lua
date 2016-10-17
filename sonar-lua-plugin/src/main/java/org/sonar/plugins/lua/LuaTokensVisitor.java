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

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.TokenType;
import com.sonar.sslr.api.Trivia;
import com.sonar.sslr.impl.Lexer;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;

import org.sonar.lua.api.LuaKeyword;
import org.sonar.lua.api.LuaTokenType;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class LuaTokensVisitor extends SquidAstVisitor<LexerlessGrammar> {

  private static final String NORMALIZED_CHARACTER_LITERAL = "$CHARS";
  private static final String NORMALIZED_NUMERIC_LITERAL = "$NUMBER";
  private static final Set<LuaKeyword> KEYWORDS = Sets.immutableEnumSet(Arrays.asList(LuaKeyword.values()));

  private final SensorContext context;
  private final Lexer lexer;

  public LuaTokensVisitor(SensorContext context, Lexer lexer) {
    this.context = context;
    this.lexer = lexer;
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    NewHighlighting highlighting = context.newHighlighting();
    File file = getContext().getFile();
    InputFile inputFile = context.fileSystem().inputFile(context.fileSystem().predicates().is(file));
    highlighting.onFile(inputFile);

    NewCpdTokens cpdTokens = context.newCpdTokens();
    cpdTokens.onFile(inputFile);

    Iterator<Token> iterator = lexer.lex(file).iterator();
   

    while (iterator.hasNext()) {
      Token token = iterator.next();
      TokenType tokenType = token.getType();
      
         if (!tokenType.equals(GenericTokenType.EOF)) {
        TokenLocation tokenLocation = new TokenLocation(token);
        cpdTokens.addToken(tokenLocation.startLine(), tokenLocation.startCharacter(), tokenLocation.endLine(), tokenLocation.endCharacter(), getTokenImage(token));
      }
      if (tokenType.equals(LuaTokenType.NUMBER)) {
        highlight(highlighting, token, TypeOfText.CONSTANT);
      } else if (tokenType.equals(GenericTokenType.LITERAL)) {
        highlight(highlighting, token, TypeOfText.STRING);
      } else if (KEYWORDS.contains(tokenType)) {
        highlight(highlighting, token, TypeOfText.KEYWORD);
      }
      for (Trivia trivia : token.getTrivia()) {
        highlight(highlighting, trivia.getToken(), TypeOfText.COMMENT);
      }
    }

    highlighting.save();
    cpdTokens.save();
  }

  private static String getTokenImage(Token token) {
    if (token.getType().equals(GenericTokenType.LITERAL)) {
      return NORMALIZED_CHARACTER_LITERAL;
    } else if (token.getType().equals(LuaTokenType.NUMBER)) {
      return NORMALIZED_NUMERIC_LITERAL;
    }
    return token.getValue();
  }

  private static void highlight(NewHighlighting highlighting, Token token, TypeOfText typeOfText) {
    TokenLocation tokenLocation = new TokenLocation(token);
    highlighting.highlight(tokenLocation.startLine(), tokenLocation.startCharacter(), tokenLocation.endLine(), tokenLocation.endCharacter(), typeOfText);
  }

}
