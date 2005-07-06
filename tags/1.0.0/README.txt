Description
===========

prettyxml is a free java utility for prettyprinting xml with attribute
sorting and indentation.

prettyxml is a command-line application based on JDOM. It offers various
format options and supports element sorting and node filtering via 
stylesheet transformations.

Copyright (C) 2005 Jesper Goertz
All Rights Reserved


Requirements
============

A Java Runtime Environment is required (JRE 1.4.2+).

Download the latest version from http://java.sun.com/j2se


How to use
==========

First set the environment variable PRETTYXML_HOME to the directory
where you unpacked the distribution file, e.g.,
  set PRETTYXML_HOME=c:\prettyxml  (on Windows)
or
  export PRETTYXML_HOME=/opt/prettyxml  (on Unix)

Run prettyxml with
  %PRETTYXML_HOME%\prettyxml options  (Windows)
or
  $PRETTYXML_HOME/prettyxml options  (Unix)

Say "prettyxml -v" to get the version and "prettyxml -h" to get help.

Transformation stylesheets are first located as files then as resources
on the classpath - sort-attributes.xslt and sort-elements.xslt are built-in.


Examples
========

The directory samples contains a few examples on use, run the bat or sh files.


Files
=====

LICENSE.txt       The License file (GPL)
README.txt        An english readme (this file)
apidoc            directory holding javadoc for prettyxml
lib               directory holding jar files
samples           directory holding examples
prettyxml         Unix script to run prettyxml
prettyxml.bat     Windows script to run prettyxml


License
=======

please see LICENSE.txt

This product includes software developed by the JDOM org (http://jdom.org)
and by The Apache Software Foundation (http://www.apache.org/).

The licenses for the re-distributed jdom and jakarta-commons code can be 
found in the respective jar files.


Contact
=======

Get the latest version from http://hippogrif.dk/sw/prettyxml
where you will also find detailed contact information.
