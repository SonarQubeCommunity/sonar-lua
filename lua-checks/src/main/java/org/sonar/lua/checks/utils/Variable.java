package org.sonar.lua.checks.utils;



import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.sonar.sslr.api.AstNode;
import org.sonar.lua.grammar.LuaGrammar;


import java.util.List;

public final class Variable {

  private Variable() {
  }

  public static String getName(AstNode varDeclStatement) {
    Preconditions.checkArgument(varDeclStatement.is(LuaGrammar.STATEMENT));
    return varDeclStatement
      .getFirstChild(LuaGrammar.VARLIST)
      .getFirstChild(LuaGrammar.VAR)
      .getFirstChild(LuaGrammar.NAME).getTokenValue();
  }
      
  

  public static boolean isVariable(AstNode directive) {
    Preconditions.checkArgument(directive.is(LuaGrammar.EXP));
    if (directive.getFirstChild(LuaGrammar.VALUE) != null) {
      AstNode variableDecStmt = directive.getFirstChild(LuaGrammar.VAR).getFirstChild(LuaGrammar.NAME);

      if (variableDecStmt != null) {
        return variableDecStmt
          .getFirstChild(LuaGrammar.NAME)
         
          .getFirstChild().is(LuaGrammar.Keyword.LOCAL);
      }
    }
    return false;
  }

  

  public static List<AstNode> getDeclaredIdentifiers(AstNode varDeclStatement) {
    Preconditions.checkArgument(varDeclStatement.is(LuaGrammar.STATEMENT));
    List<AstNode> identifiers = Lists.newArrayList();
    if (varDeclStatement.is(LuaGrammar.STATEMENT)) {
      AstNode varBindingList = varDeclStatement
        .getFirstChild(LuaGrammar.VARLIST)
        .getFirstChild(LuaGrammar.VAR);

      for (AstNode varBinding : varBindingList.getChildren(LuaGrammar.NAME)) {
        identifiers.add(varBinding.getFirstChild(LuaGrammar.NAME));
      }
    }
    return identifiers;
  }

}