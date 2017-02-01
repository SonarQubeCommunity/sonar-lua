/*
 * SonarQube Lua Plugin
 * Copyright (C) 2013-2016 Fati Ahmadi
 * mailto:fati.ahmadi66 AT gmail.com
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
package org.sonar.lua.checks;

import com.google.common.collect.ImmutableList;

import java.util.List;

public final class CheckList {

  public static final String REPOSITORY_KEY = "lua";

  public static final String SONAR_WAY_PROFILE = "Sonar way";

  private CheckList() {
  }

  public static List<Class> getChecks() {
    return ImmutableList.<Class>of(
      
      XPathCheck.class,
      FunctionComplexityCheck.class,
      FunctionWithTooManyParametersCheck.class,
    
      TableComplexityCheck.class,
      NestedControlFlowDepthCheck.class,
      LineLengthCheck.class,
      FileComplexityCheck.class,
 
      TableWithTooManyFieldsCheck.class,
      FunctionCallComplexityCheck.class
     );
  }

}
