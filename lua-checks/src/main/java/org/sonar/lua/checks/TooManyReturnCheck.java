/*
 * SonarQube Lua Plugin
 * Copyright (C) 2016-2016 SonarSource SA
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
package org.sonar.lua.checks;

import com.sonar.sslr.api.AstNode;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.lua.grammar.LuaGrammar;
import org.sonar.lua.checks.Tags;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;

@Rule(
  key = "S1142",
  name = "Functions should not contain too many return statements",
  priority = Priority.MAJOR,
  tags = Tags.BRAIN_OVERLOAD)
@ActivatedByDefault
@SqaleConstantRemediation("20min")
public class TooManyReturnCheck extends SquidCheck<LexerlessGrammar> {

  private static final int DEFAULT = 3;
  private final Deque<Integer> returnStatementCounter = new ArrayDeque<>();

  @RuleProperty(
    key = "max",
    description = "Maximum allowed return statements per function",
    defaultValue = "" + DEFAULT)
  int max = DEFAULT;


  @Override
  public void init() {
    subscribeTo(LuaGrammar.FUNCTION, LuaGrammar.FUNCSTAT,LuaGrammar.Keyword.RETURN);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    returnStatementCounter.clear();
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(LuaGrammar.Keyword.RETURN)) {
      setReturnStatementCounter(getReturnStatementCounter() + 1);
    } else {
      returnStatementCounter.push(0);
    }

  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (astNode.is(LuaGrammar.FUNCTION)||astNode.is(LuaGrammar.FUNCSTAT)) {
      if (getReturnStatementCounter() > max) {
        getContext().createLineViolation(this, "Reduce the number of returns of this function {0,number,integer}, down to the maximum allowed {1,number,integer}.",
          astNode, getReturnStatementCounter(), max);
      }
      returnStatementCounter.pop();
    }

  }

  private int getReturnStatementCounter() {
    return returnStatementCounter.peek();
  }

  private void setReturnStatementCounter(int value) {
    returnStatementCounter.pop();
    returnStatementCounter.push(value);
  }
}