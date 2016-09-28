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
import org.sonar.lua.api.LuaMetric;
import org.sonar.lua.checks.utils.LuaCheck;
import org.sonar.lua.checks.Tags;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleLinearWithOffsetRemediation;
import org.sonar.squidbridge.api.SourceClass;
import org.sonar.squidbridge.api.SourceFunction;
import org.sonar.squidbridge.checks.ChecksHelper;

@Rule(
  key = "FunctionCaLLComplexity",
  name = "FunctionCall should not be too complex",
  priority = Priority.MAJOR,
  tags = Tags.BRAIN_OVERLOAD)
@ActivatedByDefault
@SqaleLinearWithOffsetRemediation(coeff = "1min", offset = "10min", effortToFixDescription = "per complexity point above the threshold")
public class FunctionCallComplexityCheck extends LuaCheck {

  private static final int DEFAULT_MAXIMUM_FUNCTIONCALL_COMPLEXITY_THRESHOLD = 5;

  @RuleProperty(
    key = "maximumFunctionComplexityThreshold",
    description = "The maximum authorized complexity.",
    defaultValue = "" + DEFAULT_MAXIMUM_FUNCTIONCALL_COMPLEXITY_THRESHOLD)
  private int maximumFunctionCallComplexityThreshold = DEFAULT_MAXIMUM_FUNCTIONCALL_COMPLEXITY_THRESHOLD;

  @Override
  public void init() {
    subscribeTo(LuaGrammar.FUNCTIONCALL);
   
  }

  @Override
  public void leaveNode(AstNode node) {
	  SourceFunction function = (SourceFunction) getContext().peekSourceCode();

    int functionComplexity = ChecksHelper.getRecursiveMeasureInt(function, LuaMetric.COMPLEXITY);
    if (functionComplexity > maximumFunctionCallComplexityThreshold) {
      String message = String.format("FunctionCall has a complexity of %s which is greater than %s authorized.", functionComplexity, maximumFunctionCallComplexityThreshold);
      createIssueWithCost(message, node, (double)functionComplexity - maximumFunctionCallComplexityThreshold);
    }
  }

  public void setMaximumFunctionCallComplexityThreshold(int threshold) {
    this.maximumFunctionCallComplexityThreshold = threshold;
  }

}