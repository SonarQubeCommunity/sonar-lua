package org.sonar.plugins.lua;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.lua.checks.CheckList;
import org.sonar.plugins.lua.Lua;
import org.sonar.squidbridge.annotations.AnnotationBasedRulesDefinition;

public final class LuaRulesDefinition implements RulesDefinition {

  private static final String REPOSITORY_NAME = "SonarQube";

  @Override
  public void define(Context context) {
    NewRepository repository = context
      .createRepository(CheckList.REPOSITORY_KEY, Lua.KEY)
      .setName(REPOSITORY_NAME);

    new AnnotationBasedRulesDefinition(repository, Lua.KEY).addRuleClasses(false, CheckList.getChecks());

    repository.done();
  }
}