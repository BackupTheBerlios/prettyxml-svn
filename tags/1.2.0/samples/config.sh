#!/bin/sh
echo prettyprints a weblogic petstore configuration file
echo ---------------------------------------------------
if [ "$PRETTYXML_HOME" = "" ] ; then echo "please set PRETTYXML_HOME"; exit 1 ; fi 

"$PRETTYXML_HOME"/prettyxml -p "$PRETTYXML_HOME"/samples/config.properties -i "$PRETTYXML_HOME"/samples/config.xml

