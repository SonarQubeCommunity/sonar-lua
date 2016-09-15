package org.sonar.lua.checks;
import org.junit.Test;
import org.sonar.lua.LuaAstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

import java.io.File;

public class FunctionNameCheckTest {

  private FunctionNameCheck check = new FunctionNameCheck();

  @Test
  public void defaultFormat() {
    SourceFile file = LuaAstScanner.scanSingleFile(new File("src/test/resources/checks/functionName.lua"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())

    .next().atLine(6).withMessage("Rename this \"Add\" function to match the regular expression " + check.format)
    .next().atLine(10).withMessage("Rename this \"_sara\" function to match the regular expression " + check.format)
    .next().atLine(15).withMessage("Rename this \"d_Test\" function to match the regular expression " + check.format)
    .noMore();
  }

  @Test
  public void custom() {
    check.format = "^[A-Z][a-zA-Z0-9]*$";
    SourceFile file = LuaAstScanner.scanSingleFile(new File("src/test/resources/checks/functionName.lua"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(1).withMessage("Rename this \"add\" function to match the regular expression " + check.format)
      .next().atLine(10).withMessage("Rename this \"_sara\" function to match the regular expression " + check.format)
      .next().atLine(15).withMessage("Rename this \"d_Test\" function to match the regular expression " + check.format)
      .noMore();
  }
}