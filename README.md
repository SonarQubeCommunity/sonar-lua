Sonar Lua
==========

## Description
This plugin enables analysis of:
 * Lua files 5.1
 * within [SonarQube](http://www.sonarqube.org). It:
 * Performs more than [15 checks]

 
##Steps to Analyze a Lua Project

* Install SonarQube Server.5.6+

* Install SonarQube Scanner.

* Install Lua Plugin (see Installing a Plugin for more details).

* Create a sonar-project.properties file at the root of your project.

* Run sonar-scanner command from the project root dir.

* Follow the link provided at the end of the 
* analysis to browse your project's quality in SonarQube UI


## The metrics:
 * Computes the following metrics: 
 
  LINES_OF_CODE,
  
  LINES,
  
  FILES,
  
  COMMENT_LINES,
  
  FUNCTIONS,
  
  STATEMENTS,
  
  TABLECONSTRUCTORS,
  
  COMPLEXITY;

### Complexity
The following elements increment the complexity by one:

 FUNCTION,
 FUNCSTAT,  
 FUNCTIONCALL,
 WHILE_STATEMENT,
 FOR_STATEMENT,
 IF_STATEMENT,
 DO_STATEMENT,
 TABLECONSTRUCTOR
       
## Checks
XPathCheck
FunctionComplexityCheck.
FunctionWithTooManyParametersCheck.
TableComplexityCheck
NestedControlFlowDepthCheck
LineLengthCheck
FileComplexityCheck.
TableWithTooManyFieldsCheck.
FunctionCallComplexityCheck

