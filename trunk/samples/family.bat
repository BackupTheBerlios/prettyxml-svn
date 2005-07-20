@echo off
echo shows 4 different views of Eliotte Rusty Harolds family example
if "%PRETTYXML_HOME%" == "" goto no_home

echo --- default prettyprint ---
call %PRETTYXML_HOME%\prettyxml -u http://www.cafeconleche.org/books/xml/examples/07/family.xml

echo --- prettyprint with attribute indent ---
call %PRETTYXML_HOME%\prettyxml -a -u http://www.cafeconleche.org/books/xml/examples/07/family.xml

echo --- prettyprint with element sort on attributes ---
call %PRETTYXML_HOME%\prettyxml -t sort-attributes.xslt;sort-elements.xslt -u http://www.cafeconleche.org/books/xml/examples/07/family.xml

echo --- prettyprint with 4 space indentation, attribute indent and sort on text ---
call %PRETTYXML_HOME%\prettyxml -n 4 -a -t %PRETTYXML_HOME%\samples\sort-text.xslt -u http://www.cafeconleche.org/books/xml/examples/07/family.xml
goto :end

:no_home
echo please set PRETTYXML_HOME

:end
