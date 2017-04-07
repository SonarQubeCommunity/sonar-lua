/*
 * SonarQube Lua Plugin
 * Copyright (C) 2016 
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
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleLinearWithOffsetRemediation;
import org.sonar.squidbridge.api.SourceClass;
import org.sonar.squidbridge.checks.ChecksHelper;
import org.sonar.lua.api.LuaMetric;
import org.sonar.lua.checks.utils.LuaCheck;
import org.sonar.lua.checks.utils.Tags;
import org.sonar.lua.grammar.LuaGrammar;

@Rule(
  key = "TableComplexity",
  name = "Tables should not be too complex",
  priority = Priority.MAJOR,
  tags = Tags.BRAIN_OVERLOAD)
@ActivatedByDefault
@SqaleLinearWithOffsetRemediation(coeff = "1min", offset = "10min", effortToFixDescription = "per complexity point above the threshold")

public class TableComplexityCheck extends LuaCheck {

  private static final int DEFAULT_MAXIMUM_TABLE_COMPLEXITY_THRESHOLD = 10;

  @RuleProperty(
    key = "maximumTableComplexityThreshold",
    description = "The maximum authorized complexity.",
    defaultValue = "" + DEFAULT_MAXIMUM_TABLE_COMPLEXITY_THRESHOLD)
  private int maximumTableComplexityThreshold = DEFAULT_MAXIMUM_TABLE_COMPLEXITY_THRESHOLD;
  @Override
  public void init() {
    subscribeTo(LuaGrammar.TABLECONSTRUCTOR );
    //subscribeTo(LuaGrammar.Keyword.FUNCTION);
  }

  @Override
  public void leaveNode(AstNode node) {
	  SourceClass table = (SourceClass) getContext().peekSourceCode();
 
    int tableComplexity = ChecksHelper.getRecursiveMeasureInt(table, LuaMetric.COMPLEXITY);
    if (tableComplexity > maximumTableComplexityThreshold) {
      String message = String.format("Table has a complexity of %s which is greater than %s authorized.", tableComplexity, maximumTableComplexityThreshold);
      createIssueWithCost(message, node, (double)tableComplexity - maximumTableComplexityThreshold);
    }
  }

 

  public void setMaximumTableComplexityThreshold(int threshold) {
    this.maximumTableComplexityThreshold = threshold;
  }

}
