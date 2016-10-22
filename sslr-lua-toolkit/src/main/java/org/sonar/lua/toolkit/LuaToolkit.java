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
package org.sonar.lua.toolkit;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;

import org.sonar.colorizer.CDocTokenizer;
import org.sonar.colorizer.CppDocTokenizer;
import org.sonar.colorizer.JavadocTokenizer;
import org.sonar.colorizer.KeywordsTokenizer;
import org.sonar.colorizer.StringTokenizer;
import org.sonar.colorizer.Tokenizer;

import org.sonar.lua.api.LuaKeyword;
import org.sonar.lua.parser.LuaParser;
import org.sonar.lua.LuaConfiguration;
import org.sonar.lua.parser.LuaParser;
import org.sonar.sslr.toolkit.Toolkit;

import java.nio.charset.Charset;
import java.util.List;

public final class LuaToolkit {

  private LuaToolkit() {
  }

  public static void main(String[] args) {
    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "SSDK");
    new Toolkit(LuaParser.create(new LuaConfiguration(Charset.defaultCharset())), getTokenizers(), "SSLR Lua Toolkit").run();
  }

  @VisibleForTesting
  static List<Tokenizer> getTokenizers() {
    return ImmutableList.of(
        new StringTokenizer("<span class=\"s\">", "</span>"),
        new CDocTokenizer("<span class=\"cd\">", "</span>"),
        new JavadocTokenizer("<span class=\"cppd\">", "</span>"),
        new CppDocTokenizer("<span class=\"cppd\">", "</span>"),
        new KeywordsTokenizer("<span class=\"k\">", "</span>", LuaKeyword.keywordValues()));
  }

}
