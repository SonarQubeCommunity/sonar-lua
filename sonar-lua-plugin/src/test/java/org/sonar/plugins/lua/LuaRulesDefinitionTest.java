package org.sonar.plugins.lua;
import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.lua.checks.CheckList;

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
    RulesDefinition.Rule functionComplexityRule = repository.rule("FunctionComplexity");
    assertThat(functionComplexityRule).isNotNull();
    assertThat(functionComplexityRule.name()).isEqualTo("Functions should not be too complex");
   

    for (RulesDefinition.Rule rule : repository.rules()) {
      for (RulesDefinition.Param param : rule.params()) {
        assertThat(param.description()).as("description for " + param.key() + " of " + rule.key()).isNotEmpty();
      }
    }
  }
}