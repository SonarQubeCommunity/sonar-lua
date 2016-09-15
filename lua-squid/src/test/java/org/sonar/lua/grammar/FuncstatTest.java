package org.sonar.lua.grammar;

import org.junit.Test;
import org.sonar.sslr.parser.LexerlessGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class FuncstatTest {

  private LexerlessGrammar g = LuaGrammar.createGrammar();

  @Test
  public void test() {
    assertThat(g.rule(LuaGrammar.FUNCSTAT))
        .matches("function fati () end");
  }

}
