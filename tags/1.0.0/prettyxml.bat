@echo off
if "%PRETTYXML_HOME%" == "" goto no_home
if "%JAVA_HOME%" == "" goto no_java_home
set JAVA_COMMAND="%JAVA_HOME%/bin/java"
goto doit
:no_java_home
set JAVA_COMMAND=java
:doit
%JAVA_COMMAND% -cp %PRETTYXML_HOME%\lib\prettyxml.jar;%PRETTYXML_HOME%\lib\jdom-1.0.jar;%PRETTYXML_HOME%\lib\commons-cli-1.0.jar;%PRETTYXML_HOME%\lib\commons-io-1.0.jar dk.hippogrif.prettyxml.Main %*
goto :end
:no_home
echo please set PRETTYXML_HOME
:end
