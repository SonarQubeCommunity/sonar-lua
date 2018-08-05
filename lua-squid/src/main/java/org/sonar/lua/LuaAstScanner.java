/*
 * SonarQube Lua Plugin
 * Copyright (C) 2016 
 * mailto:fati.ahmadi66 AT gmail DOT com
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
package org.sonar.lua;

import org.sonar.lua.SourceFuncCall;
import com.google.common.base.Charsets;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.impl.Parser;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.lua.api.LuaMetric;
import org.sonar.lua.grammar.LuaGrammar;
import org.sonar.lua.metrics.ComplexityVisitor;
import org.sonar.lua.parser.LuaParser;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.ProgressAstScanner;
import org.sonar.squidbridge.SourceCodeBuilderCallback;
import org.sonar.squidbridge.SourceCodeBuilderVisitor;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.SquidAstVisitorContextImpl;
import org.sonar.squidbridge.api.SourceClass;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceFunction;
import org.sonar.squidbridge.api.SourceProject;
import org.sonar.squidbridge.indexer.QueryByType;
import org.sonar.squidbridge.metrics.CommentsVisitor;
import org.sonar.squidbridge.metrics.CounterVisitor;
import org.sonar.squidbridge.metrics.LinesVisitor;
import org.sonar.squidbridge.metrics.LinesOfCodeVisitor;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;



public final class LuaAstScanner {

  private LuaAstScanner() {
  }

   /**
   * Helper method for testing checks without having to deploy them on a Sonar instance.
   */
  public static SourceFile scanSingleFile(File file, SquidAstVisitor<LexerlessGrammar>... visitors) {
    if (!file.isFile()) {
      throw new IllegalArgumentException("File '" + file + "' not found.");
    }

    AstScanner<LexerlessGrammar> scanner = create(new LuaConfiguration(Charsets.UTF_8), Arrays.asList(visitors));
    scanner.scanFile(file);
    Collection<SourceCode> sources = scanner.getIndex().search(new QueryByType(SourceFile.class));
    if (sources.size() != 1) {
      throw new IllegalStateException("Only one SourceFile was expected whereas " + sources.size() + " has been returned.");
    }
    return (SourceFile) sources.iterator().next();
  }

  public static AstScanner<LexerlessGrammar> create(LuaConfiguration conf, List<SquidAstVisitor<LexerlessGrammar>> visitors) {
    final SquidAstVisitorContextImpl<LexerlessGrammar> context = new SquidAstVisitorContextImpl<>(new SourceProject("Lua Project"));
    final Parser<LexerlessGrammar> parser = LuaParser.create(conf);

    AstScanner.Builder<LexerlessGrammar> builder = new ProgressAstScanner.Builder(context).setBaseParser(parser);

    /* Metrics */
    builder.withMetrics(LuaMetric.values()); 
    /* Files */
    builder.setFilesMetric(LuaMetric.FILES);
    
    /* Comments */
    builder.setCommentAnalyser(new LuaCommentAnalyser()); 
    

 
//*table constructor*/
    builder.withSquidAstVisitor(new SourceCodeBuilderVisitor<LexerlessGrammar>(new SourceCodeBuilderCallback() {
        private int seq = 0;

        @Override
        public SourceCode createSourceCode(SourceCode parentSourceCode, AstNode astNode) {
          seq++;
          SourceClass cls = new SourceClass("table:" + seq);
          cls.setStartAtLine(astNode.getTokenLine());
          return cls;
        }
      },LuaGrammar.TABLECONSTRUCTOR));

      builder.withSquidAstVisitor(CounterVisitor.<LexerlessGrammar>builder()
        .setMetricDef(LuaMetric.TABLECONSTRUCTORS)
        .subscribeTo(LuaGrammar.TABLECONSTRUCTOR)
        .build());
    
    /* Functions*/
    
      

      /* Functions*/
      

      builder.withSquidAstVisitor(new SourceCodeBuilderVisitor<LexerlessGrammar>(new SourceCodeBuilderCallback() {
        private int seq = 0;

        @Override
        public SourceCode createSourceCode(SourceCode parentSourceCode, AstNode astNode) {
          seq++;
         
          SourceFunction function = new SourceFunction("function" + seq);
          function.setStartAtLine(astNode.getTokenLine());
          return function;
        }
      }, LuaGrammar.FUNCTION,LuaGrammar.FUNCSTAT,LuaGrammar.LOCALFUNCSTAT));

      builder.withSquidAstVisitor(CounterVisitor.<LexerlessGrammar>builder()
        .setMetricDef(LuaMetric.FUNCTIONS)
        .subscribeTo(LuaGrammar.FUNCTION,LuaGrammar.FUNCSTAT,LuaGrammar.LOCALFUNCSTAT)
        .build());

      
      /*FanctionCall */
      
      builder.withSquidAstVisitor(new SourceCodeBuilderVisitor<LexerlessGrammar>(new SourceCodeBuilderCallback() {
          private int seq = 0;
          
          @Override
          public SourceCode createSourceCode(SourceCode parentSourceCode, AstNode astNode) {
              seq++;
              
              SourceFuncCall functionCall = new SourceFuncCall("functionCall" + seq);
              functionCall.setStartAtLine(astNode.getTokenLine());
              return functionCall;
          }
      },LuaGrammar.FUNCTIONCALL));
      
      builder.withSquidAstVisitor(CounterVisitor.<LexerlessGrammar>builder()
                                  .setMetricDef(LuaMetric.FUNCTIONCALL)
                                  .subscribeTo(LuaGrammar.FUNCTIONCALL)
                                  .build());


    /* Metrics */
    // Lines of code and comments
    builder.withSquidAstVisitor(new LinesVisitor<LexerlessGrammar>(LuaMetric.LINES));
    builder.withSquidAstVisitor(new LinesOfCodeVisitor<LexerlessGrammar>(LuaMetric.LINES_OF_CODE));
    builder.withSquidAstVisitor(CommentsVisitor.<LexerlessGrammar>builder().withCommentMetric(LuaMetric.COMMENT_LINES)
      .withNoSonar(true)
      .withIgnoreHeaderComment(conf.getIgnoreHeaderComments())
      .build());

    // Statements
    builder.withSquidAstVisitor(CounterVisitor.<LexerlessGrammar>builder()
      .setMetricDef(LuaMetric.STATEMENTS)
      .subscribeTo(
    		 
          LuaGrammar.IF_STATEMENT,
          LuaGrammar.WHILE_STATEMENT,
          LuaGrammar.FOR_STATEMENT,
          LuaGrammar.DO_STATEMENT,
          LuaGrammar.WHILE_STATEMENT,
          LuaGrammar.REPEAT_STATEMENT,
        
          LuaGrammar.Keyword.AND,
  
          LuaGrammar.Keyword.OR
       
          
 
        ).build());
      
    /* END calculation metric*/
    builder.withSquidAstVisitor(new ComplexityVisitor());
    
    /* External visitors (typically Check ones) */
    for (SquidAstVisitor<LexerlessGrammar> visitor : visitors) {
      if (visitor instanceof CharsetAwareVisitor) {
        ((CharsetAwareVisitor) visitor).setCharset(conf.getCharset());
      }
      builder.withSquidAstVisitor(visitor);
    }

    return builder.build();
  }

}
