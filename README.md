SonarQube Lua Plugin
====================

## Description
This plugin enables analysis of Lua project within SonarQube:

 
##Steps to Analyze a Lua Project

* Install SonarQube Server (http://docs.sonarqube.org/display/SONAR/Setup+and+Upgrade)

* Install SonarQube Scanner see(http://docs.sonarqube.org/display/SONAR/Setup+and+Upgrade).

* Download Lua Plugin see "release".

* Install Lua Plugin see( http://docs.sonarqube.org/display/SONAR/Installing+a+Plugin)

* Create a sonar-project.properties file at the root of your project.

* Run sonar-scanner command from the project root dir.

* Follow the link provided at the end of the 
 analysis to browse your project's quality in SonarQube UI


## The metrics:
 * Computes the following metrics: 
 
  * LINES_OF_CODE,
  
  * LINES,
  
  * FILES,
  
  * COMMENT_LINES,
  
  * FUNCTIONS,
  
  * STATEMENTS,
  
  * TABLECONSTRUCTORS,
  
  * COMPLEXITY;

### Complexity
The following elements increment the complexity by one:

 * FUNCTION,
 
 * FUNCSTAT,  
 
 * FUNCTIONCALL,
 
 * WHILE_STATEMENT,
 
 * FOR_STATEMENT,
 
 * IF_STATEMENT,
 
 * DO_STATEMENT,
 
 * TABLECONSTRUCTOR
       
## Rules

 * XPathCheck

 * FunctionComplexityCheck.

 * FunctionWithTooManyParametersCheck.

 * TableComplexityCheck

 * NestedControlFlowDepthCheck

 * LineLengthCheck

 * FileComplexityCheck.

 * TableWithTooManyFieldsCheck.

 * FunctionCallComplexityCheck

