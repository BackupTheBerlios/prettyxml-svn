#!/bin/sh
echo shows 4 different views of Eliotte Rusty Harolds family example
if [ "$PRETTYXML_HOME" = "" ] ; then echo "please set PRETTYXML_HOME"; exit 1 ; fi 

echo --- default prettyprint ---
"$PRETTYXML_HOME"/prettyxml -u http://www.cafeconleche.org/books/xml/examples/07/family.xml

echo --- prettyprint with attribute indent ---
"$PRETTYXML_HOME"/prettyxml -a -u http://www.cafeconleche.org/books/xml/examples/07/family.xml

echo --- prettyprint with element sort on attributes ---
"$PRETTYXML_HOME"/prettyxml -t sort-attributes.xslt;sort-elements.xslt -u http://www.cafeconleche.org/books/xml/examples/07/family.xml

echo --- prettyprint with 4 space indentation, attribute indent and sort on text ---
"$PRETTYXML_HOME"/prettyxml -n 4 -a -t "$PRETTYXML_HOME"/samples/sort-text.xslt -u http://www.cafeconleche.org/books/xml/examples/07/family.xml

