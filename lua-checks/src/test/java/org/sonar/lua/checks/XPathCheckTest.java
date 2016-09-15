package org.sonar.lua.checks;

import org.junit.Test;
import org.sonar.lua.LuaAstScanner;

import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

import java.io.File;

public class XPathCheckTest {

  @Test
  public void check() {
    XPathCheck check = new XPathCheck();
    check.xpathQuery = "//IDENTIFIER[string-length(@tokenValue) >= 10]";
    check.message = "Avoid identifiers which are too long!";

    SourceFile file = LuaAstScanner.scanSingleFile(new File("src/test/resources/checks/xPath.lua"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
        .next().atLine(2).withMessage("Avoid identifiers which are too long!")
        .noMore();
  }

}