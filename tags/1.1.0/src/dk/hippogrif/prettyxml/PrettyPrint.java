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

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.jdom.transform.XSLTransformer;

/**
 * Prettyprints XML based on JDOM 1.0 according to a set of properties
 * specifying format and options.
 * See {@link dk.hippogrif.prettyxml}.
 *
 * @author Jesper Goertz
 */
public class PrettyPrint {
  private Logger logger = Logger.getLogger(getClass().getName());
  
  /**
   * Encoding of XML document, e.g., "UTF-8" or "ISO-8859-1"
   */
  public final static String ENCODING = "encoding";
  /**
   * TRUE or FALSE
   */
  public final static String EXPAND_EMPTY_ELEMENTS = "expandEmptyElements";
  /**
   * Integer between 1 and 99 incl
   */
  public final static String INDENT = "indent";
  /**
   * "\\r", "\\n" or "\\r\\n"
   */
  public final static String LINE_SEPARATOR = "lineSeparator";
  /**
   * TRUE or FALSE
   */
  public final static String OMIT_DECLARATION = "omitDeclaration";
  /**
   * TRUE or FALSE
   */
  public final static String OMIT_ENCODING = "omitEncoding";
  /**
   * "NORMALIZE", "TRIM", "TRIM_FULL_WHITE" or "PRESERVE"
   */
  public final static String TEXT_MODE = "textMode";
  /**
   * TRUE or FALSE
   */
  public final static String SORT_ATTRIBUTES = "sortAttributes";
  /**
   * TRUE or FALSE
   */
  public final static String INDENT_ATTRIBUTES = "indentAttributes";
  /**
   * Stylesheet names separated by ";"
   */
  public final static String TRANSFORM = "transform";
  /**
   * File name
   */
  public final static String INPUT = "input";
  /**
   * Universal resource location
   */
  public final static String URL = "url";
  /**
   * File name
   */
  public final static String OUTPUT = "output";
  
  /**
   * Accepted properties.
   */
  final static List keys = Arrays.asList(new String[]{ENCODING, EXPAND_EMPTY_ELEMENTS, INDENT, LINE_SEPARATOR, OMIT_DECLARATION, OMIT_ENCODING, TEXT_MODE, SORT_ATTRIBUTES, INDENT_ATTRIBUTES, TRANSFORM, INPUT, URL, OUTPUT});
  
  private String version;
  
  /**
   * Construct and load configuration from resource "/prettyxml.properties".
   *
   * @throws Exception if version not configured
   */
  public PrettyPrint() throws Exception {
    loadConfiguration("/prettyxml.properties");
  }
  
  /**
   * Get prettyxml version.
   */
  public String getVersion() {
    return version;
  }
  
  class MyLevel extends Level {
    public MyLevel(int level) {
      super("prettyxml", level);
    }
  }
  
  void loadConfiguration(String resource) throws Exception {
    InputStream is = null;
    try {
      is = new Main().getClass().getResourceAsStream(resource);
      if (is == null) {
        throw new Exception("unknown resource: "+resource);
      }
      Properties prop = new Properties();
      prop.load(is);
      String loggingLevel = prop.getProperty("logging.Level");
      if (loggingLevel != null) {
        Level level = Level.parse(loggingLevel);
        Logger l = Logger.getLogger("dk.hippogrif.prettyxml");
        l.setLevel(level);
        if ("ConsoleHandler".equals(prop.getProperty("logging.Handler"))) {
          ConsoleHandler h = new ConsoleHandler();
          h.setLevel(level);
          l.addHandler(h);
        }
        logger.config("logging.Level="+loggingLevel);
      }
      version = prop.getProperty("version", "");
      logger.config("version="+version);
      if (version.equals("")) {
        throw new Exception("unknown version of prettyxml");
      }
    } finally {
      IOUtils.closeQuietly(is);
    }
  }
  
  static void setIndentation(String no, Format format) {
    int n = Integer.parseInt(no);
    if (n > 0) {
      StringBuffer sb = new StringBuffer(n);
      for (int i=0; i<n; i++) {
        sb.append(' ');
      }
      format.setIndent(sb.toString());
    }
  }
  
  static Format initFormat(Properties prop) throws Exception {
    Format format = Format.getRawFormat();
    String p;
    if ((p = prop.getProperty(ENCODING)) != null) {
      format.setEncoding(p);
    }
    if ((p = prop.getProperty(EXPAND_EMPTY_ELEMENTS)) != null) {
      format.setExpandEmptyElements(Boolean.valueOf(p).booleanValue());
    }
    if ((p = prop.getProperty(INDENT)) != null) {
      setIndentation(p, format);
    }
    if ((p = prop.getProperty(LINE_SEPARATOR)) != null) {
      format.setLineSeparator(p);
    }
    if ((p = prop.getProperty(OMIT_DECLARATION)) != null) {
      format.setOmitDeclaration(Boolean.valueOf(p).booleanValue());
    }
    if ((p = prop.getProperty(OMIT_ENCODING)) != null) {
      format.setOmitEncoding(Boolean.valueOf(p).booleanValue());
    }
    if ((p = prop.getProperty(TEXT_MODE)) != null) {
      if (p.equals("NORMALIZE")) {
        format.setTextMode(Format.TextMode.NORMALIZE);
      } else if (p.equals("TRIM")) {
        format.setTextMode(Format.TextMode.TRIM);
      } else if (p.equals("TRIM_FULL_WHITE")) {
        format.setTextMode(Format.TextMode.TRIM_FULL_WHITE);
      } else if (!p.equals("PRESERVE")) {
        throw new Exception("invalid "+TEXT_MODE+" in properties file: "+p);
      }
    }
    return format;
  }
  
  /**
   * Checks properties are known with valid syntax - false properties are removed.
   *
   * @throws Exception if property error
   */
  public static void checkProperties(Properties prop) throws Exception {
    for (Enumeration elements = prop.propertyNames(); elements.hasMoreElements(); ) {
      String s = (String)elements.nextElement();
      if (!keys.contains(s)) {
        throw new Exception("unknown property in properties file: "+s);
      }
    }
    checkBoolean("expandEmptyElements", prop);
    checkBoolean("omitDeclaration", prop);
    checkBoolean("omitEncoding", prop);
    checkBoolean("indentAttributes", prop);
    checkBoolean("sortAttributes", prop);
    if (prop.containsKey("indent")) {
      try {
        int i = Integer.parseInt(prop.getProperty("indent"));
        if (i < 1 || i > 99) {
          throw new Exception("indent must be an integer > 0 and < 100");
        }
      } catch (NumberFormatException e) {
        throw new Exception("indent must be an integer > 0 and < 100");
      }
    }
    if (prop.containsKey("input") && prop.containsKey("url")) {
      throw new Exception("do not use input and url at the same time");
    }
  }
  
  /**
   * Load properties from file.
   */
  public static Properties loadProperties(File file) throws IOException {
    Properties prop = new Properties();
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
      prop.load(fis);
    } finally {
      IOUtils.closeQuietly(fis);
    }
    return prop;
  }
  
  /**
   * Store properties in file with version in header.
   */
  public static void storeProperties(File file, Properties prop, String version) throws IOException {
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(file);
      prop.store(fos, "prettyxml "+version);
    } finally {
      IOUtils.closeQuietly(fos);
    }
  }
  
  static void checkBoolean(String key, Properties prop) throws Exception {
    String s = prop.getProperty(key);
    if (s != null) {
      if ("FALSE".equalsIgnoreCase(s)) {
        prop.remove(key);
      } else if (!("TRUE".equalsIgnoreCase(s))) {
        throw new Exception("value of property "+key+" must be TRUE or FALSE, was: "+s);
      }
    }
  }
  
  /**
   * Construct an XSL transformer.
   *
   * @param name of stylesheet file or resource
   * @throws Exception if stylesheet not found
   */
  public static XSLTransformer mkTransformer(String name) throws Exception {
    File file = new File(name);
    if (file.isFile()) {
      return new XSLTransformer(file);
    }
    InputStream is = null;
    try {
      is = new Main().getClass().getResourceAsStream("/"+name);
      if (is == null) {
        throw new Exception("cannot find stylesheet "+name);
      }
      return new XSLTransformer(is);
    } finally {
      IOUtils.closeQuietly(is);
    }
  }
  
  /**
   * Do the prettyprint according to properties.
   *
   * @throws Exception if something goes wrong
   */
  public static void execute(Properties prop) throws Exception {
    Format format = initFormat(prop);
    PrettyXMLOutputter outp = new PrettyXMLOutputter(format);
    outp.setSortAttributes(prop.containsKey("sortAttributes"));
    outp.setIndentAttributes(prop.containsKey("indentAttributes"));
    SAXBuilder builder = new SAXBuilder();
    Document doc;
    if (prop.containsKey("input")) {
      doc = builder.build(new File(prop.getProperty("input")));
    } else if (prop.containsKey("url")) {
      doc = builder.build(new URL(prop.getProperty("url")));
    } else {
      doc = builder.build(System.in);
    }
    if (prop.containsKey("transform")) {
      String[] sa = prop.getProperty("transform").split(";");
      for (int i=0; i<sa.length; i++) {
        XSLTransformer transformer = mkTransformer(sa[i].trim());
        doc = transformer.transform(doc);
      }
    }
    if (prop.containsKey("output")) {
      FileOutputStream fos = null;
      try {
        fos = new FileOutputStream(new File(prop.getProperty("output")));
        outp.output(doc, fos);
      } finally {
        IOUtils.closeQuietly(fos);
      }
    } else {
      outp.output(doc, System.out);
    }
  }
  
}
