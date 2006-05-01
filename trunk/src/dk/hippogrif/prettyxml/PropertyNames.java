/*
    Copyright (C) 2005 Jesper Goertz
    All Rights Reserved, http://hippogrif.dk/sw/prettyxml
 
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package dk.hippogrif.prettyxml;

/**
 * Properties for prettyxml.
 */
public interface PropertyNames {
  /**
   * Encoding of XML document, e.g., "UTF-8" or "ISO-8859-1"
   */
  String ENCODING = "encoding";
  /**
   * TRUE or FALSE
   */
  String EXPAND_EMPTY_ELEMENTS = "expandEmptyElements";
  /**
   * Integer between 1 and 99 incl
   */
  String INDENT = "indent";
  /**
   * "\\r", "\\n" or "\\r\\n"
   */
  String LINE_SEPARATOR = "lineSeparator";
  /**
   * "\\r", "\\n" or "\\r\\n"
   */
  String[] LINE_SEPARATORS = new String[]{"\r\n", "\r", "\n"};
  /**
   * TRUE or FALSE
   */
  String OMIT_DECLARATION = "omitDeclaration";
  /**
   * TRUE or FALSE
   */
  String OMIT_ENCODING = "omitEncoding";
  /**
   * "NORMALIZE", "TRIM", "TRIM_FULL_WHITE" or "PRESERVE"
   */
  String TEXT_MODE = "textMode";
  /**
   * "NORMALIZE", "TRIM", "TRIM_FULL_WHITE", "PRESERVE"
   */
  String[] TEXT_MODES = new String[]{"PRESERVE", "NORMALIZE", "TRIM", "TRIM_FULL_WHITE"};
  /**
   * TRUE or FALSE
   */
  String SORT_ATTRIBUTES = "sortAttributes";
  /**
   * TRUE or FALSE
   */
  String INDENT_ATTRIBUTES = "indentAttributes";
  /**
   * Stylesheet names separated by ";"
   */
  String TRANSFORM = "transform";
  /**
   * File name
   */
  String INPUT = "input";
  /**
   * Universal resource location
   */
  String URL = "url";
  /**
   * File name
   */
  String OUTPUT = "output";
  
  /**
   * Basic properties for specifying format and handling.
   */
  String[] BASIC_KEYS = new String[]{ENCODING, EXPAND_EMPTY_ELEMENTS, INDENT, LINE_SEPARATOR, OMIT_DECLARATION, OMIT_ENCODING, TEXT_MODE, SORT_ATTRIBUTES, INDENT_ATTRIBUTES, TRANSFORM};
  /**
   * Extended properties for specifying document location.
   */
  String[] EXTENDED_KEYS = new String[]{INPUT, URL, OUTPUT};
  
}
