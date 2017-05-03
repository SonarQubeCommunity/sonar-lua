/*
 * SonarQube Lua Plugin
 * Copyright (C) 2016 SonarSource SA
 * mailto: fati.ahmadi66 AT gmail DOT com
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
import org.sonar.lua.api.LuaMetric;
import org.sonar.lua.checks.utils.LuaCheck;
import org.sonar.lua.checks.utils.Tags;
import org.sonar.lua.grammar.LuaGrammar;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleLinearWithOffsetRemediation;
import org.sonar.squidbridge.api.SourceFunction;
import org.sonar.squidbridge.checks.ChecksHelper;

@Rule(key = "MethodComplexity",
name = "Methods should not be too complex",
priority = Priority.MAJOR, 
tags = Tags.BRAIN_OVERLOAD)
@ActivatedByDefault
@SqaleLinearWithOffsetRemediation(coeff = "1min", offset = "10min", effortToFixDescription = "per complexity point above the threshold")
public class MethodComplexityCheck extends LuaCheck {
	public static final String CHECK_KEY = "MethodComplexity";
	private static final int DEFAULT_MAXIMUM_METHOD_COMPLEXITY_THRESHOLD = 10;

	@RuleProperty(key = "maximumMethodComplexityThreshold",
			description = "The maximum authorized complexity.", defaultValue = ""
			+ DEFAULT_MAXIMUM_METHOD_COMPLEXITY_THRESHOLD)
	private int maximumMethodComplexityThreshold = DEFAULT_MAXIMUM_METHOD_COMPLEXITY_THRESHOLD;


	@Override
	public void init() {
		subscribeTo(LuaGrammar.FUNCSTAT);
		
	}

	@Override
	public void leaveNode(AstNode node) {
		SourceFunction function = (SourceFunction) getContext()
				.peekSourceCode();
	
		int functionComplexity = ChecksHelper.getRecursiveMeasureInt(function,
				LuaMetric.COMPLEXITY);
		if (functionComplexity > maximumMethodComplexityThreshold) {
			String message = String
					.format("Method has a complexity of %s which is greater than %s authorized.",
							functionComplexity,
							maximumMethodComplexityThreshold);
			createIssueWithCost(message, node, (double) functionComplexity
					- maximumMethodComplexityThreshold);
		}
	}


	public void setMaximumFunctionComplexityThreshold(int threshold) {
		this.maximumMethodComplexityThreshold = threshold;
	}

}
