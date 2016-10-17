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

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.lua.checks.CheckList;
import org.sonar.plugins.lua.LuaRulesDefinition;

import static org.fest.assertions.Assertions.assertThat;

public class LuaRulesDefinitionTest {

  @Test
  public void test() {
    LuaRulesDefinition rulesDefinition = new LuaRulesDefinition();
    RulesDefinition.Context context = new RulesDefinition.Context();
    rulesDefinition.define(context);
    RulesDefinition.Repository repository = context.repository("lua");

    assertThat(repository.name()).isEqualTo("SonarQube");
    assertThat(repository.language()).isEqualTo("lua");
    assertThat(repository.rules()).hasSize(CheckList.getChecks().size());

    for (RulesDefinition.Rule rule : repository.rules()) {
        for (RulesDefinition.Param param : rule.params()) {
          assertThat(param.description()).as("description for " + param.key() + " of " + rule.key()).isNotEmpty();
        }
      }
    
  }
}
