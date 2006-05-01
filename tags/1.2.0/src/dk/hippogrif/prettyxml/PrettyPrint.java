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
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.jdom.transform.XSLTransformer;
import org.jdom.transform.XSLTransformException;

/**
 * Prettyprints XML based on JDOM 1.0 according to a set of properties
 * specifying format and options.
 * See {@link dk.hippogrif.prettyxml}.
 *
 * @author Jesper Goertz
 */
public class PrettyPrint implements PropertyNames {
  private static Logger logger = Logger.getLogger(PrettyPrint.class.getName());
  private static String[] encodings;
  private static String[] settings;
  private static HashMap setting;
  private static String[] transformations;
  private static HashMap transformation;
  
  /**
   * Accepted properties (basic and extended).
   */
  static ArrayList keys = initKeys();
  
  private static String version = loadConfiguration("prettyxml.properties");

  private static ArrayList initKeys() {
    ArrayList keys = new ArrayList(Arrays.asList(BASIC_KEYS));
    if (keys == null) throw new RuntimeException("basic keys null");
    keys.addAll(Arrays.asList(EXTENDED_KEYS));
    if (keys == null) throw new RuntimeException("added keys null");
    return keys;
  }
  
  /**
   * Get prettyxml version.
   */
  public static String getVersion() {
    return version;
  }
  
  class MyLevel extends Level {
    public MyLevel(int level) {
      super("prettyxml", level);
    }
  }
  
  /**
   * Get wellknown encodings.
   **/
  public static String[] getEncodings() {
    return encodings;
  }
  
  /**
   * Get names of wellknown properties settings.
   **/
  public static String[] getSettings() {
    return settings;
  }
  
  /**
   * Get wellknown properties setting.
   **/
  public static Properties getSetting(String name) {
    return (Properties)setting.get(name);
  }
  
  /**
   * Get default properties setting - the first in configured settings.
   **/
  public static Properties getDefaultSetting() {
    return (Properties)setting.get(settings[0]);
  }
  
  /**
   * Get names of wellknown transformations.
   **/
  public static String[] getTransformations() {
    return transformations;
  }
  
  /**
   * Get wellknown transformation.
   **/
  public static XSLTransformer getTransformation(String name) {
    return (XSLTransformer)transformation.get(name);
  }
  
  static String loadConfiguration(String resource) {
    try {
      Properties prop = loadPropertiesResource(resource);
      
      // logging
      String loggingLevel = prop.getProperty("logging.Level");
      if (loggingLevel != null) {
        Level level = Level.parse(loggingLevel);
        Logger l = Logger.getLogger("dk.hippogrif.prettyxml");
        l.setLevel(level);
        if ("ConsoleHandler".equals(prop.getProperty("logging.Handler"))) {
          ConsoleHandler h = new ConsoleHandler();
          h.setLevel(level);
          l.addHandler(h);
        } else if ("FileHandler".equals(prop.getProperty("logging.Handler"))) {
          FileHandler h = new FileHandler(System.getProperty("user.home")+"/prettyxml.log");
          h.setLevel(level);
          l.addHandler(h);
        }
        logger.config("logging.Level="+loggingLevel);
      }
      
      // version
      version = prop.getProperty("version", "");
      logger.config("version="+version);
      
      // wellknown encodings
      String s = prop.getProperty("encodings");
      if (s == null) {
        throw new Exception("encodings missing in prettyxml.properties");
      }
      encodings = s.split(";");
      
      // wellknown property settings
      s = prop.getProperty("settings");
      if (s == null) {
        throw new Exception("settings missing in prettyxml.properties");
      }
      settings = s.split(";");
      setting = new HashMap();
      for (int i=0; i<settings.length;i++) {
        String name = settings[i];
        Properties props = loadPropertiesResource(name+".properties");
        checkProperties(props, false);
        setting.put(name, props);
      }
      
      // wellknown transformations
      s = prop.getProperty("transformations");
      if (s == null) {
        throw new Exception("transformations missing in prettyxml.properties");
      }
      transformations = s.split(";");
      transformation = new HashMap();
      for (int i=0; i<transformations.length;i++) {
        String name = transformations[i];
        transformation.put(name, mkTransformerResource(name+".xslt"));
      }
    } catch(Exception e) {
      throw new RuntimeException(e);
    }
    return version;
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
   * Checks known properties -
   * present boolean properties are set to either "true" or "false",
   * indent=0 is removed,
   * string properties are trimmed and empty properties removed -
   * however, TEXT_MODE is checked when format is initialized
   * and ENCODING when used.
   *
   * @param prop holds the properties
   * @param extended to include in/out properties
   * @throws Exception if property error
   */
  public static void checkProperties(Properties prop, boolean extended) throws Exception {
    for (Enumeration elements = prop.propertyNames(); elements.hasMoreElements(); ) {
      String s = (String)elements.nextElement();
      if (!keys.contains(s)) {
        prop.remove(s);
      }
    }
    checkBoolean(EXPAND_EMPTY_ELEMENTS, prop);
    checkBoolean(OMIT_DECLARATION, prop);
    checkBoolean(OMIT_ENCODING, prop);
    checkBoolean(INDENT_ATTRIBUTES, prop);
    checkBoolean(SORT_ATTRIBUTES, prop);
    if (prop.containsKey(INDENT)) {
      try {
        int i = Integer.parseInt(prop.getProperty(INDENT));
        if (i == 0) {
          prop.remove(INDENT);
        } else if (i < 1 || i > 99) {
          throw new Exception(INDENT+" must be an integer >= 0 and < 100");
        }
      } catch (NumberFormatException e) {
        throw new Exception(INDENT+" must be an integer >= 0 and < 100");
      }
    }
    if (prop.containsKey(LINE_SEPARATOR)) {
      String s = prop.getProperty(LINE_SEPARATOR);
      boolean err = true;
      for (int i=0; i<LINE_SEPARATORS.length; i++) {
        if (LINE_SEPARATORS[i].equals(s)) {
          err = false;
          break;
        }
      }
      if (err) {
        throw new Exception(LINE_SEPARATOR+" must be \\r, \\n or \\r\\n");
      }
    }
    checkString(TRANSFORM, prop);
    if (extended) {
      checkString(INPUT, prop);
      checkString(URL, prop);
      checkString(OUTPUT, prop);
      if (prop.containsKey(INPUT) && prop.containsKey(URL)) {
        throw new Exception("do not use "+INPUT+" and "+URL+" at the same time");
      }
    } else {
      prop.remove(INPUT);
      prop.remove(URL);
      prop.remove(OUTPUT);
    }
  }
  
  /**
   * Load properties from file or as resource in classpath if file not found.
   *
   * @param name of properties file or resource
   * @throws IOException if io error on reading file
   * @throws Exception if resource not found
   */
  public static Properties loadProperties(String name) throws Exception {
    File file = new File(name);
    if (file.isFile()) {
      return loadProperties(file);
    }
    return loadPropertiesResource(name);
  }
  
  /**
   * Load properties as resource in classpath.
   *
   * @param name of resource holding properties
   * @throws Exception if resource not found
   */
  public static Properties loadPropertiesResource(String name) throws Exception {
    InputStream is = null;
    try {
      is = PrettyPrint.class.getResourceAsStream("/"+name);
      if (is == null) {
        throw new Exception("cannot find properties "+name);
      }
      Properties prop = new Properties();
      prop.load(is);
      return prop;
    } finally {
      IOUtils.closeQuietly(is);
    }
  }
  
  /**
   * Load properties from file.
   *
   * @param file holding properties
   * @throws IOException on io error
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
   *
   * @param file to store the properties in
   * @param prop the properties to store
   * @throws IOException on io error
   */
  public static void storeProperties(File file, Properties prop) throws IOException {
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
      if ("false".equalsIgnoreCase(s)) {
        prop.setProperty(key, "false");
      } else if ("true".equalsIgnoreCase(s)) {
        prop.setProperty(key, "true");
      } else {
        throw new Exception("value of property "+key+" must be true or false, was: "+s);
      }
    }
  }
  
  static void checkString(String key, Properties prop) {
    String s = prop.getProperty(key);
    if (s != null) {
      s = s.trim();
      if (s.length() == 0) {
        prop.remove(key);
      } else {
        prop.setProperty(key, s);
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
    if (transformation.containsKey(name)) {
      return (XSLTransformer)transformation.get(name);
    }
    File file = new File(name);
    if (file.isFile()) {
      return new XSLTransformer(file);
    }
    return mkTransformerResource(name);
  }
  
  private static XSLTransformer mkTransformerResource(String name) throws Exception {
    InputStream is = null;
    try {
      is = PrettyPrint.class.getResourceAsStream("/"+name);
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
    execute(prop, null);
  }
  
  /**
   * Do the prettyprint according to properties.
   *
   * @param input holds xml document as text if present
   * @return prettyprinted document as text if input param present
   * @throws Exception if something goes wrong
   */
  public static String execute(Properties prop, String input) throws Exception {
    try {
      checkProperties(prop, true);
      logger.log(Level.FINEST, "input="+input+" properties="+prop);
      Format format = initFormat(prop);
      PrettyXMLOutputter outp = new PrettyXMLOutputter(format);
      outp.setSortAttributes(prop.containsKey(SORT_ATTRIBUTES));
      outp.setIndentAttributes(prop.containsKey(INDENT_ATTRIBUTES));
      SAXBuilder builder = new SAXBuilder();
      Document doc;
      if (input != null) {
        doc = builder.build(new StringReader(input));
      } else if (prop.containsKey(INPUT)) {
        doc = builder.build(new File(prop.getProperty(INPUT)));
      } else if (prop.containsKey(URL)) {
        doc = builder.build(new URL(prop.getProperty(URL)));
      } else {
        doc = builder.build(System.in);
      }
      if (prop.containsKey(TRANSFORM)) {
        String[] sa = prop.getProperty(TRANSFORM).split(";");
        for (int i=0; i<sa.length; i++) {
          XSLTransformer transformer = mkTransformer(sa[i].trim());
          doc = transformer.transform(doc);
        }
      }
      if (prop.containsKey(OUTPUT)) {
        FileOutputStream fos = null;
        try {
          fos = new FileOutputStream(new File(prop.getProperty(OUTPUT)));
          outp.output(doc, fos);
        } finally {
          IOUtils.closeQuietly(fos);
        }
      } else if (input != null) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        outp.output(doc, baos);
        return baos.toString(prop.getProperty(ENCODING, "UTF-8"));
      } else {
        outp.output(doc, System.out);
      }
      return null;
    } catch (Exception e) {
      logger.log(Level.FINER, "properties="+prop, e);
      throw e;
    }
  }
  
}
