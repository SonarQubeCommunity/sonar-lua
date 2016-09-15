package org.sonar.lua.checks.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.sonar.sslr.api.AstNode;
import org.sonar.lua.grammar.LuaGrammar;
import org.sonar.lua.grammar.LuaGrammar.Keyword;

import java.util.List;

public class Function {

  private Function() {
  }

  public static String getName(AstNode functionDef) {
    Preconditions.checkArgument(functionDef.is(LuaGrammar.FUNCSTAT));
    return functionDef.getFirstChild(LuaGrammar.FUNCNAME).getFirstChild(LuaGrammar.NAME).getTokenValue();
  }

  

  public static boolean isEmptyConstructor(AstNode functionDef, String className) {
    Preconditions.checkArgument(functionDef.is(LuaGrammar.FUNCSTAT));
    AstNode functionBlock = functionDef.getFirstChild(LuaGrammar.FUNCBODY).getFirstChild(LuaGrammar.BLOCK);

    return isConstructor(functionDef, className)
      && (functionBlock == null || functionBlock.getFirstChild(LuaGrammar.CHUNK).getChildren().isEmpty());
  }

  public static boolean isConstructor(AstNode functionDef, String className) {
    Preconditions.checkArgument(functionDef.is(LuaGrammar.FUNCSTAT));
    return functionDef.getFirstChild(LuaGrammar.FUNCNAME).getNumberOfChildren() == 1
      && functionDef.getFirstChild(LuaGrammar.NAME).getFirstChild().getTokenValue().equals(className);
  }


  public static List<AstNode> getParametersIdentifiers(AstNode functionDef) {
    Preconditions.checkArgument(functionDef.is(LuaGrammar.FUNCTION, LuaGrammar.FUNCSTAT));
    List<AstNode> paramIdentifier = Lists.newArrayList();
    AstNode parameters = functionDef
      .getFirstChild(LuaGrammar.FUNCBODY)
      .getFirstChild(LuaGrammar.PARLIST)
      .getFirstChild(LuaGrammar.NAMELIST);

    if (parameters != null) {
      for (AstNode parameter : parameters.getChildren(LuaGrammar.NAME)) {
        if (parameter.getFirstChild(LuaGrammar.NAME) != null) {
          paramIdentifier.add(parameter.getFirstChild(LuaGrammar.NAME));
        }
      }
    }
    return paramIdentifier;
  }

  
}