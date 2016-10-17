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
package org.sonar.lua.checks;

import com.sonar.sslr.api.AstNode;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import org.sonar.lua.checks.utils.Tags;
import org.sonar.lua.grammar.LuaGrammar;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S107",
  name = "Functions should not have too many parameters",
  priority = Priority.MAJOR,
  tags = {Tags.BRAIN_OVERLOAD})
@ActivatedByDefault
@SqaleConstantRemediation("20min")
public class FunctionWithTooManyParametersCheck extends SquidCheck<LexerlessGrammar> {

  private static final int DEFAULT = 7;
  @RuleProperty(
    key = "max",
    description = "Maximum authorized number of parameters",
    defaultValue = "" + DEFAULT)
  int max = DEFAULT;


  @Override
  public void init() {
    subscribeTo(LuaGrammar.NAMELIST);
  }

  @Override
  public void visitNode(AstNode astNode) {
    int nbParameters = astNode.getChildren(LuaGrammar.NAME, LuaGrammar.Punctuator.ELLIPSIS).size();
 
    if (nbParameters > max  ) {
      getContext().createLineViolation(this, "This function has {0,number,integer} parameters, which is greater than the {1,number,integer} authorized.",
        astNode, nbParameters, max);
    }
   
  }

}
