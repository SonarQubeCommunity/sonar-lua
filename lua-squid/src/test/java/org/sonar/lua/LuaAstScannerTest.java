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
package org.sonar.lua;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import org.junit.Ignore;
import org.junit.Test;
import org.sonar.lua.LuaConfiguration;
import org.sonar.lua.api.LuaMetric;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceProject;
import org.sonar.squidbridge.indexer.QueryByType;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.io.File;
import java.util.Collections;

import static org.fest.assertions.Assertions.assertThat;
public class LuaAstScannerTest {
	 

	 @Test
	  public void files() {
	    AstScanner<LexerlessGrammar> scanner = LuaAstScanner.create(new LuaConfiguration(Charsets.UTF_8), Collections.emptyList());
	    scanner.scanFiles(ImmutableList.of(new File("src/test/resources/metrics/comments.lua"),
	      new File("src/test/resources/metrics/lines.lua")));
	    SourceProject project = (SourceProject) scanner.getIndex().search(new QueryByType(SourceProject.class)).iterator().next();
	    assertThat(project.getInt(LuaMetric.FILES)).isEqualTo(2);
	  }
	
	 @Test
	  public void lines() {
	    SourceFile file = LuaAstScanner.scanSingleFile(new File("src/test/resources/metrics/lines.lua"));
	    assertThat(file.getInt(LuaMetric.LINES)).isEqualTo(4);
	  }
	 
		 
	 @Test
	  public void functionCall() {
	    SourceFile file = LuaAstScanner.scanSingleFile(new File("src/test/resources/metrics/functionCall.lua"));
	    assertThat(file.getInt(LuaMetric.FUNCTIONCALL)).isEqualTo(3);
	  }
	 @Test
	  public void lines_of_code() {
	    SourceFile file = LuaAstScanner.scanSingleFile(new File("src/test/resources/metrics/line_of_code.lua"));
	    assertThat(file.getInt(LuaMetric.LINES_OF_CODE)).isEqualTo(15);
	  } 
	 @Test
	  public void TableConstructors() {
	    SourceFile file = LuaAstScanner.scanSingleFile(new File("src/test/resources/metrics/tableConstructor.lua"));
	    assertThat(file.getInt(LuaMetric.TABLECONSTRUCTORS)).isEqualTo(4);
	  } 
	 @Test
	  public void functions() {
	    SourceFile file = LuaAstScanner.scanSingleFile(new File("src/test/resources/metrics/function.lua"));
	    assertThat(file.getInt(LuaMetric.FUNCTIONS)).isEqualTo(6);
	  }

	 @Test
	  public void statments() {
	    SourceFile file = LuaAstScanner.scanSingleFile(new File("src/test/resources/metrics/statement.lua"));
	    assertThat(file.getInt(LuaMetric.STATEMENTS)).isEqualTo(3);
	  } 
	
	 @Test
	  public void complexity() {
	    SourceFile file = LuaAstScanner.scanSingleFile(new File("src/test/resources/metrics/complexity.lua"));
	    assertThat(file.getInt(LuaMetric.COMPLEXITY)).isEqualTo(4);
	  }
}
