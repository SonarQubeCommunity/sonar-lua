package org.sonar.lua.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.squidbridge.annotations.NoSqale;
import org.sonar.squidbridge.annotations.RuleTemplate;
import org.sonar.squidbridge.checks.AbstractXPathCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "XPath",
  name = "XPath rule",
  priority = Priority.MAJOR)
@RuleTemplate
@NoSqale
public class XPathCheck extends AbstractXPathCheck<LexerlessGrammar> {

  private static final String DEFAULT_XPATH_QUERY = "";
  private static final String DEFAULT_MESSAGE = "The XPath expression matches this piece of code";

  @RuleProperty(
    key = "xpathQuery",
    description = "The XPath query",
    defaultValue = "" + DEFAULT_XPATH_QUERY,
    type = "TEXT")
  public String xpathQuery = DEFAULT_XPATH_QUERY;

  @RuleProperty(
    key = "message",
    description = "The issue message",
    defaultValue = "" + DEFAULT_MESSAGE)
  public String message = DEFAULT_MESSAGE;

  @Override
  public String getXPathQuery() {
    return xpathQuery;
  }

  @Override
  public String getMessage() {
    return message;
  }

}