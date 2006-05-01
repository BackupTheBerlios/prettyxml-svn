Description
===========

prettyxml is a free java utility for prettyprinting xml with attribute
sorting and indentation.

prettyxml is a command-line and GUI application based on JDOM. 
It offers various format options and supports element sorting and node 
filtering via stylesheet transformations.

Copyright (C) 2005 Jesper Goertz
All Rights Reserved


Requirements
============

A Java Runtime Environment is required (JRE 1.4.2+).

Download the latest version from http://java.sun.com/j2se


How to use
==========

Unpack the distributed zip file in a directory of your choice. 

To invoke the GUI simply run java -jar prettyxml.jar 
or use the command script as described in the following.

use command script
------------------

First set the environment variable PRETTYXML_HOME to the directory
where you unpacked the distribution file, e.g.,
  set PRETTYXML_HOME=c:\prettyxml  (on Windows)
or
  export PRETTYXML_HOME=/opt/prettyxml  (on Unix)

Run prettyxml with
  java -jar %PRETTYXML_HOME%/lib/prettyxml.jar options
or use one of the scripts
  %PRETTYXML_HOME%\prettyxml options  (Windows)
or
  $PRETTYXML_HOME/prettyxml options  (Unix)

Say "prettyxml -v" to get the version and "prettyxml -h" to get help.

If invoked without options a GUI is activated for format and option setting.

built-in settings and stylesheets
---------------------------------

The following settings are built-in

  pretty    JDOM format: trimmed whitespace and 2 space indentation
  raw       JDOM format: preserved whitespace, no indentation
  compact   JDOM format: whitespace normalization (oneliner+declaration)
  indented  trimmed whitespace with element and attribute indentation
            and sorted attributes
  sorted    as indented but with elements sorted, useful for line diff

The following transformation stylesheets are built-in

  sort-attributes   sort of attributes (needed for sort of elements) 
                    by name ascending</li>
  sort-elements     sort of elements by name ascending and
                    by (first 6) attributes and then text content

Other settings and transformation stylesheets are first located as files 
then as resources on the classpath.

You may place your own properties files and stylesheets in lib to be found 
as resources.

Examples
========

The directory samples contains a few examples on use, run the bat or sh files.


Files
=====

LICENSE.txt       the License file (GPL)
README.txt        an english readme (this file)
HISTORY.txt       release history
apidoc            directory holding javadoc for prettyxml
lib               directory holding jar files and stylesheets
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
