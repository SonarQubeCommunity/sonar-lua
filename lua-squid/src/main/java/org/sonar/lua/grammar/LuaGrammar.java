/*
 * Sonar Lua
 * Copyright (C) 2013 SonarSource
 * dev@sonar.codehaus.org
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.lua.grammar;

import com.sonar.sslr.api.GenericTokenType;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;
import org.sonar.sslr.parser.LexerlessGrammar;

/**
 * http://www.lua.org/manual/5.1/manual.html
 */
public enum LuaGrammar implements GrammarRuleKey {

  CHUNK,
  BLOCK,

  STATEMENT,
  DO_STATEMENT,
  WHILE_STATEMENT,
  REPEAT_STATEMENT,
  IF_STATEMENT,
  FOR_STATEMENT,

  LASTSTAT,
  FUNCNAME,
  FUNCSTAT,
  VARLIST,
  VAR,
  NAMELIST,
  EXPLIST,
  EXP,
  PREFIXEXP,
  FUNCTIONCALL,
  ARGS,
  FUNCTION,
  FUNCBODY,
  PARLIST,
  TABLECONSTRUCTOR,
  FIELDLIST,
  FIELD,
  FIELDSEP,
  BINOP,
  UNOP,

  NAME,
  NUMBER,
  STRING,

  VALUE,
  PREFIX,
  SUFFIX,
  INDEX,
  CALL,

  KEYWORD,
  COMMENT,
  SPACING,
  ROOT,
  LONGSTRING,
  SHEBANG;

  public static enum Punctuator implements GrammarRuleKey {
    PLUS("+"),
    MINUS("-"),
    MUL("*"),
    DIV("/"),
    MOD("%"),
    CARET("^"),
    HASH("#"),
    EQEQ("=="),
    TILDA_EQ("~="),
    LE("<="),
    GE(">="),
    LT("<"),
    GT(">"),
    EQ("="),
    LPARENTHESES("("),
    RPARENTHESES(")"),
    LCURLYBRACKET("{"),
    RCURLYBRACKET("}"),
    LBRACKET("["),
    RBRACKET("]"),
    SEMICOLON(";"),
    COLON(":"),
    COMMA(","),
    DOT("."),
    DOTDOT(".."),
    ELLIPSIS("...");

    private String value;

    private Punctuator(String value) {
      this.value = value;
    }
  }

  public static enum Keyword implements GrammarRuleKey {
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

    private Keyword(String value) {
      this.value = value;
    }

  }

  public static LexerlessGrammar createGrammar() {
    LexerlessGrammarBuilder b = LexerlessGrammarBuilder.create();

    lexical(b);

    b.rule(SHEBANG).is("#!", b.regexp("[^\\n\\r]*+")).skip();

    b.rule(ROOT).is(b.optional(SHEBANG), SPACING, CHUNK, SPACING, b.token(GenericTokenType.EOF, b.endOfInput()));

    b.rule(CHUNK).is(b.zeroOrMore(STATEMENT, b.optional(Punctuator.SEMICOLON)), b.optional(LASTSTAT, b.optional(Punctuator.SEMICOLON)));

    b.rule(BLOCK).is(CHUNK);

    b.rule(STATEMENT).is(b.firstOf(
        DO_STATEMENT,
        WHILE_STATEMENT,
        REPEAT_STATEMENT,
        IF_STATEMENT,
        FOR_STATEMENT,
        b.sequence(Keyword.FUNCTION, FUNCNAME, FUNCBODY),
        b.sequence(Keyword.LOCAL, Keyword.FUNCTION, NAME, FUNCBODY),
        b.sequence(Keyword.LOCAL, NAMELIST, b.optional(Punctuator.EQ, EXPLIST)),
        b.sequence(VARLIST, Punctuator.EQ, EXPLIST),
        FUNCTIONCALL));
    b.rule(DO_STATEMENT).is(Keyword.DO, BLOCK, Keyword.END);
    b.rule(WHILE_STATEMENT).is(Keyword.WHILE, EXP, Keyword.DO, BLOCK, Keyword.END);
    b.rule(REPEAT_STATEMENT).is(Keyword.REPEAT, BLOCK, Keyword.UNTIL, EXP);
    b.rule(IF_STATEMENT).is(Keyword.IF, EXP, Keyword.THEN, BLOCK, b.zeroOrMore(Keyword.ELSEIF, EXP, Keyword.THEN, BLOCK), b.optional(Keyword.ELSE, BLOCK), Keyword.END);
    b.rule(FOR_STATEMENT).is(b.firstOf(
        b.sequence(Keyword.FOR, NAME, Punctuator.EQ, EXP, Punctuator.COMMA, EXP, b.optional(Punctuator.COMMA, EXP), Keyword.DO, BLOCK, Keyword.END),
        b.sequence(Keyword.FOR, NAMELIST, Keyword.IN, EXPLIST, Keyword.DO, BLOCK, Keyword.END)));

    b.rule(LASTSTAT).is(b.firstOf(
        b.sequence(Keyword.RETURN, b.optional(EXPLIST)),
        Keyword.BREAK));

    b.rule(FUNCNAME).is(NAME, b.zeroOrMore(Punctuator.DOT, NAME), b.optional(Punctuator.COLON, NAME));

    b.rule(VARLIST).is(VAR, b.zeroOrMore(Punctuator.COMMA, VAR));

    b.rule(NAMELIST).is(NAME, b.zeroOrMore(Punctuator.COMMA, NAME));

    b.rule(VALUE).is(b.firstOf(
        Keyword.NIL,
        Keyword.FALSE,
        Keyword.TRUE,
        NUMBER,
        STRING,
        Punctuator.ELLIPSIS,
        FUNCTION,
        TABLECONSTRUCTOR,
        FUNCTIONCALL,
        VAR,
        b.sequence(Punctuator.LPARENTHESES, EXP, Punctuator.RPARENTHESES)));
    b.rule(EXP).is(b.firstOf(
        b.sequence(UNOP, EXP),
        b.sequence(VALUE, b.optional(BINOP, EXP))));
    b.rule(PREFIX).is(b.firstOf(
        b.sequence(Punctuator.LPARENTHESES, EXP, Punctuator.RPARENTHESES),
        NAME));
    b.rule(INDEX).is(b.firstOf(
        b.sequence(Punctuator.LBRACKET, EXP, Punctuator.RBRACKET),
        b.sequence(Punctuator.DOT, NAME)));
    b.rule(CALL).is(b.firstOf(
        ARGS,
        b.sequence(Punctuator.COLON, NAME, ARGS)));
    b.rule(SUFFIX).is(b.firstOf(
        CALL,
        INDEX));
    b.rule(VAR).is(b.firstOf(
        b.sequence(PREFIX, b.zeroOrMore(SUFFIX, b.next(SUFFIX)), INDEX),
        NAME));
    b.rule(FUNCTIONCALL).is(PREFIX, b.zeroOrMore(SUFFIX, b.next(SUFFIX)), CALL);

    b.rule(EXPLIST).is(EXP, b.zeroOrMore(Punctuator.COMMA, EXP));

    b.rule(ARGS).is(b.firstOf(
        b.sequence(Punctuator.LPARENTHESES, b.optional(EXPLIST), Punctuator.RPARENTHESES),
        TABLECONSTRUCTOR,
        STRING));

    b.rule(FUNCTION).is(Keyword.FUNCTION, FUNCBODY);

    b.rule(FUNCBODY).is(Punctuator.LPARENTHESES, b.optional(PARLIST), Punctuator.RPARENTHESES, BLOCK, Keyword.END);

    b.rule(PARLIST).is(b.firstOf(
        b.sequence(NAMELIST, b.optional(Punctuator.COMMA, Punctuator.ELLIPSIS)),
        Punctuator.ELLIPSIS));

    b.rule(TABLECONSTRUCTOR).is(Punctuator.LCURLYBRACKET, b.optional(FIELDLIST), Punctuator.RCURLYBRACKET);

    b.rule(FIELDLIST).is(FIELD, b.zeroOrMore(FIELDSEP, FIELD), b.optional(FIELDSEP));

    b.rule(FIELD).is(b.firstOf(
        b.sequence(Punctuator.LBRACKET, EXP, Punctuator.RBRACKET, Punctuator.EQ, EXP),
        b.sequence(NAME, Punctuator.EQ, EXP),
        EXP));

    b.rule(FIELDSEP).is(b.firstOf(Punctuator.COMMA, Punctuator.SEMICOLON));

    b.rule(BINOP).is(b.firstOf(
        Punctuator.PLUS, Punctuator.MINUS, Punctuator.MUL, Punctuator.DIV, Punctuator.CARET, Punctuator.MOD, Punctuator.DOTDOT,
        Punctuator.LE, Punctuator.LT, Punctuator.GE, Punctuator.GT, Punctuator.EQEQ, Punctuator.EQ, Punctuator.TILDA_EQ,
        Keyword.AND, Keyword.OR));

    b.rule(UNOP).is(b.firstOf(Punctuator.MINUS, Keyword.NOT, Punctuator.HASH));

    b.setRootRule(ROOT);

    return b.build();
  }

  private static void lexical(LexerlessGrammarBuilder b) {
    b.rule(LONGSTRING).is(b.regexp("\\[(=*+)\\[((?!\\]\\1\\])[\\s\\S])*+\\]\\1\\]"));

    b.rule(COMMENT).is(b.firstOf(
        b.sequence("--", LONGSTRING),
        b.sequence("--", b.regexp("[^\\n\\r]*+"))));

    b.rule(SPACING).is(
        b.skippedTrivia(b.regexp("\\s*+")),
        b.zeroOrMore(
            b.commentTrivia(COMMENT),
            b.skippedTrivia(b.regexp("\\s*+")))).skip();

    b.rule(NAME).is(
        b.nextNot(KEYWORD),
        b.regexp("\\p{javaJavaIdentifierStart}++\\p{javaJavaIdentifierPart}*+"),
        SPACING);

    String exp = "(?:[Ee][+-]?+[0-9]++)";
    b.rule(NUMBER).is(b.regexp("(?:" +
        "0x[0-9a-fA-F]++" +
        "|" + "[0-9]++" + "(?:\\.[0-9]++)?+" + exp + "?+" +
        "|" + "\\.[0-9]++" + exp + "?+" +
        ")"), SPACING);

    b.rule(STRING).is(
        b.firstOf(
            b.regexp("\"([^\"\\\\]*+(\\\\[\\s\\S])?+)*+\""),
            b.regexp("'([^'\\\\]*+(\\\\[\\s\\S])?+)*+'"),
            LONGSTRING),
        SPACING);

    String[] keywordValues = new String[Keyword.values().length];
    for (int i = 0; i < keywordValues.length; i++) {
      Keyword keyword = Keyword.values()[i];
      b.rule(keyword).is(keyword.value, SPACING);
      keywordValues[i] = keyword.value;
    }
    b.rule(KEYWORD).is(firstOfRules(b, keywordValues), b.nextNot(b.regexp("\\p{javaJavaIdentifierPart}")));

    for (Punctuator punctuator : Punctuator.values()) {
      b.rule(punctuator).is(punctuator.value, SPACING);
    }
  }

  private static Object firstOfRules(LexerlessGrammarBuilder b, Object[] rules) {
    Object[] rest = new Object[rules.length - 2];
    System.arraycopy(rules, 2, rest, 0, rules.length - 2);
    return b.firstOf(rules[0], rules[1], rest);
  }

}
