package org.sonar.lua.checks;
import com.sonar.sslr.api.AstNode;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.lua.grammar.LuaGrammar;
import org.sonar.lua.checks.utils.Function;
import org.sonar.lua.checks.Tags;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Pattern;

@Rule(
  key = "S100",
  name = "Function names should comply with a naming convention",
  tags = Tags.CONVENTION,
  priority = Priority.MINOR)
@ActivatedByDefault
@SqaleConstantRemediation("5min")
public class FunctionNameCheck extends SquidCheck<LexerlessGrammar> {


  private static final String DEFAULT = "^[a-z][a-zA-Z0-9]*$";
  private Pattern pattern = null;
  private Deque<String> classes = new ArrayDeque<>();

  @RuleProperty(
    key = "format",
    description = "Regular expression used to check the function names against",
    defaultValue = DEFAULT)
  String format = DEFAULT;


  @Override
  public void init() {
    subscribeTo(
      LuaGrammar.FUNCSTAT);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    if (pattern == null) {
      pattern = Pattern.compile(format);
    }
    classes.clear();
  }

  @Override
  public void visitNode(AstNode astNode) {
    
      String functionName = Function.getName(astNode);

      if (!isConstructor(astNode) && !pattern.matcher(functionName).matches()) {
        getContext().createLineViolation(this, "Rename this \"{0}\" function to match the regular expression {1}", astNode, functionName, format);
      }
    }

  private boolean isConstructor(AstNode functionNode) {
    return !classes.isEmpty() && Function.isConstructor(functionNode, classes.peek());
  }
}