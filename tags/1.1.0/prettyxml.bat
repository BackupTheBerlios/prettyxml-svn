@echo off
if "%PRETTYXML_HOME%" == "" goto no_home
if "%JAVA_HOME%" == "" goto no_java_home
set JAVA_COMMAND="%JAVA_HOME%/bin/java"
goto doit
:no_java_home
set JAVA_COMMAND=java
:doit
%JAVA_COMMAND% -jar %PRETTYXML_HOME%\lib\prettyxml.jar %*
goto :end
:no_home
echo please set PRETTYXML_HOME
:end
