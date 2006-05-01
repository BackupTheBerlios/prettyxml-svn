@echo off
echo prettyprints a weblogic petstore configuration file
echo ---------------------------------------------------
if "%PRETTYXML_HOME%" == "" goto no_home

call %PRETTYXML_HOME%\prettyxml -p %PRETTYXML_HOME%\samples\config.properties -i %PRETTYXML_HOME%\samples\config.xml

goto :end

:no_home
echo please set PRETTYXML_HOME

:end
