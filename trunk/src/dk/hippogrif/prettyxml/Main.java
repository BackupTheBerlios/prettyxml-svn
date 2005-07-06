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

import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.jdom.transform.XSLTransformer;

/**
 * A command line application for prettyprinting an xml file. <p/>
 * <code>Main</code> takes the following optional arguments
 * <p/><pre>
 *   -a              indent attributes
 *   -h              help
 *   -i file         input file
 *   -n no           no of spaces to indent, default 2
 *   -o file         output file
 *   -p file         property file holding output format and options
 *   -s              sort attributes on name
 *   -t files        an xslt pipeline of one or more stylesheets separated by ;
 *   -u url          input url
 *   -v              version </pre>
 *
 * The property file may hold the following {@link org.jdom.output.Format Format} properties and prettyxml options<pre>
 *   format = COMPACT | RAW | PRETTY (default)
 *   encoding = string, e.g., UTF-8, ISO-8859-1
 *   expandEmptyElements = TRUE | FALSE
 *   indent = no of spaces to indent
 *   lineSeparator = string, i.e., \r, \n or \r\n (default)
 *   omitDeclaration = TRUE | FALSE
 *   omitEncoding = TRUE | FALSE
 *   textMode = NORMALIZE | TRIM | TRIM_FULL_WHITE | PRESERVE
 *   indentAttributes = TRUE | FALSE
 *   sortAttributes = TRUE | FALSE
 *   transform = an xslt pipeline of one or more stylesheets separated by ;
 *   input = input file
 *   url = input url
 *   output = output file </pre>
 *
 * Use the xslt pipeline to sort elements or filter nodes -<br/>
 * stylesheets are located first as files then as resources on the classpath.<br/>
 * Standard input is used if no file or url is specified.<br/>
 * Standard output is used if no file is specified.<br/>
 *
 * @author Jesper Goertz
 */
public class Main {
  
  private static String version;
  
  private static Options options;
  private static List optionList;
  
  public static String getVersion() {
    return version;
  }

  /**
   * init options for resetting options between (test) uses, 
   * because commons-cli-1.0 from maven stores state in them
   * whereas the original 1.0 does not
   */  
  public static void initOptions() {
    options = mkOptions();
    optionList = sortOptions(options);
  }

  private static void getConfiguration() throws Exception {
    InputStream is = null;
    try {
      is = new Main().getClass().getResourceAsStream("/prettyxml.properties");
      Properties prop = new Properties();
      prop.load(is);
      version = (String)prop.getProperty("version", "");
      if (version.equals("")) {
        throw new Exception("unknown version of prettyxml");
      }
    } finally {
      IOUtils.closeQuietly(is);
    }
  }
  
  private static List sortOptions(Options options) {
    ArrayList list = new ArrayList(options.getOptions());
    Collections.sort(list, new Comparator() {
      public int compare(Object o1, Object o2) {
        return ((Option)o1).getOpt().compareTo(((Option)o2).getOpt());
      }
    });
    return list;
  }
  
  private static Options mkOptions() {
    Options options = new Options();
    options.addOption("h", false, "help");
    options.addOption("v", false, "version");
    options.addOption("s", false, "sort attributes on name");
    options.addOption("a", false, "indent attributes");
    Option option;
    option = new Option("n", true, "no of spaces to indent, default 2");
    option.setArgName("no");
    options.addOption(option);
    option = new Option("p", true, "property file holding output format and options");
    option.setArgName("file");
    options.addOption(option);
    option = new Option("t", true, "an xslt pipeline of one or more stylesheets separated by ;");
    option.setArgName("files");
    options.addOption(option);
    option = new Option("i", true, "input file");
    option.setArgName("file");
    options.addOption(option);
    option = new Option("u", true, "input URL");
    option.setArgName("url");
    options.addOption(option);
    option = new Option("o", true, "output file");
    option.setArgName("file");
    options.addOption(option);
    return options;
  }
  
  private static void help(PrintStream ps) {
    ps.println("prettyxml prettyprints an xml file");
    ps.println("");
    usage(ps);
    ps.println("");
    ps.println("the property file may hold the following properties:");
    ps.println("  format = COMPACT | RAW | PRETTY (default)");
    ps.println("  encoding = string, e.g., UTF-8, ISO-8859-1");
    ps.println("  expandEmptyElements = TRUE | FALSE");
    ps.println("  indent = no of spaces to indent");
    ps.println("  lineSeparator = string, i.e., \\r, \\n or \\r\\n (default)");
    ps.println("  omitDeclaration = TRUE | FALSE");
    ps.println("  omitEncoding = TRUE | FALSE");
    ps.println("  textMode = NORMALIZE | TRIM | TRIM_FULL_WHITE | PRESERVE");
    ps.println("  indentAttributes = TRUE | FALSE");
    ps.println("  sortAttributes = TRUE | FALSE");
    ps.println("  transform = an xslt pipeline of one or more stylesheets separated by ;");
    ps.println("  input = input file");
    ps.println("  url = input url");
    ps.println("  output = output file");
    ps.println("");
    ps.println("use the xslt pipeline to sort elements or filter nodes -");
    ps.println("stylesheets are located first as files then as resources on the classpath.");
    ps.println("standard input is used if no file or url is specified");
    ps.println("standard output is used if no file is specified");
  }
  
  private static void version(PrintStream ps) {
    ps.println("prettyxml version " + version);
    ps.println("Copyright (C) 2005 Jesper Goertz");
    ps.println("All Rights Reserved, http://hippogrif.dk/sw/prettyxml");
    ps.println("prettyxml comes with ABSOLUTELY NO WARRANTY");
    ps.println("this is free software under GNU GENERAL PUBLIC LICENSE Version 2");
  }
  
  private static void usage(PrintStream ps) {
    ps.println("usage: prettyxml option+");
    optionUsage(ps);
  }
  
  private static void optionUsage(PrintStream ps) {
    for (Iterator iter = optionList.iterator(); iter.hasNext(); ) {
      Option option = (Option) iter.next();
      ps.print("  -" + option.getOpt());
      String spaces = "              ";
      int i = 0;
      if (option.hasArg()) {
        ps.print(" " + option.getArgName());
        i = option.getArgName().length() + 1;
      }
      ps.print(spaces.substring(i));
      ps.println(option.getDescription());
    }
  }
  
  public static CommandLine parse(String[] args) {
    CommandLineParser parser = new BasicParser();
    CommandLine cmd = null;
    try {
      cmd = parser.parse(options, args);
    } catch (ParseException pe) { }
    return cmd;
  }
  
  public static CommandLine getCmdLine(String[] args) {
    initOptions();
    CommandLine cmd = parse(args);
    if (args.length == 0 || cmd == null || cmd.getArgs().length > 0) {
      usage(System.out);
      cmd = null;
    } else if (cmd.hasOption("h")) {
      help(System.out);
      cmd = null;
    } else if (cmd.hasOption("v")) {
      version(System.out);
      cmd = null;
    }
    return cmd;
  }
  
  public static void setIndentation(String no, Format format) throws Exception {
    int n = Integer.parseInt(no);
    if (n > 0) {
      StringBuffer sb = new StringBuffer(n);
      for (int i=0; i<n; i++) {
        sb.append(' ');
      }
      format.setIndent(sb.toString());
    } else throw new Exception("-n indentation must be > 0, was "+n);
  }
  
  public static Format initFormat(Properties prop) throws Exception {
    Format format = Format.getPrettyFormat();
    String p;
    if ((p = prop.getProperty("format")) != null) {
      if (p.equals("COMPACT")) {
        format = Format.getCompactFormat();
      } else if (p.equals("RAW")) {
        format = Format.getRawFormat();
      } else if (!p.equals("PRETTY")) {
        throw new Exception("invalid format in properties file:"+p);
      }
    }
    if ((p = prop.getProperty("encoding")) != null) {
      format.setEncoding(p);
    }
    if ((p = prop.getProperty("expandEmptyElements")) != null) {
      format.setExpandEmptyElements(Boolean.valueOf(p).booleanValue());
    }
    if ((p = prop.getProperty("indent")) != null) {
      setIndentation(p, format);
    }
    if ((p = prop.getProperty("lineSeparator")) != null) {
      format.setLineSeparator(p);
    }
    if ((p = prop.getProperty("omitDeclaration")) != null) {
      format.setOmitDeclaration(Boolean.valueOf(p).booleanValue());
    }
    if ((p = prop.getProperty("omitEncoding")) != null) {
      format.setOmitEncoding(Boolean.valueOf(p).booleanValue());
    }
    if ((p = prop.getProperty("textMode")) != null) {
      if (p.equals("NORMALIZE")) {
        format.setTextMode(Format.TextMode.NORMALIZE);
      } else if (p.equals("TRIM")) {
        format.setTextMode(Format.TextMode.TRIM);
      } else if (p.equals("TRIM_FULL_WHITE")) {
        format.setTextMode(Format.TextMode.TRIM_FULL_WHITE);
      } else if (!p.equals("PRESERVE")) {
        throw new Exception("invalid textMode in properties file: "+p);
      }
    }
    return format;
  }
  
  final public static List keys = Arrays.asList(new String[]{"format","encoding","expandEmptyElements","indent","lineSeparator","omitDeclaration","omitEncoding","textMode","sortAttributes","indentAttributes","transform","input","url","output"});
  
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
    if (prop.containsKey("input") && prop.containsKey("url")) {
      throw new Exception("do not use input and url at the same time");
    }
  }
  
  public static Properties readFormat(String filename) throws Exception {
    Properties prop = new Properties();
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(filename);
      prop.load(fis);
    } finally {
      IOUtils.closeQuietly(fis);
    }
    return prop;
  }
  
  public static void checkBoolean(String key, Properties prop) throws Exception {
    String s = prop.getProperty(key);
    if (s != null) {
      if ("FALSE".equalsIgnoreCase(s)) {
        prop.remove(key);
      } else if (!("TRUE".equalsIgnoreCase(s))) {
        throw new Exception("value of property "+key+" must be TRUE or FALSE, was: "+s);
      }
    }
  }
  
  public static Properties getProperties(CommandLine cmd) throws Exception {
    Properties prop = new Properties();
    if (cmd.hasOption("p")) {
      prop = readFormat(cmd.getOptionValue("p"));
    }
    if (cmd.hasOption("n")) {
      prop.put("indent", cmd.getOptionValue("n"));
    }
    if (cmd.hasOption("a")) {
      prop.put("indentAttributes", "TRUE");
    }
    if (cmd.hasOption("s")) {
      prop.put("sortAttributes", "TRUE");
    }
    if (cmd.hasOption("t")) {
      prop.put("transform", cmd.getOptionValue("t"));
    }
    if (cmd.hasOption("i")) {
      prop.put("input", cmd.getOptionValue("i"));
    }
    if (cmd.hasOption("o")) {
      prop.put("output", cmd.getOptionValue("o"));
    }
    if (cmd.hasOption("u")) {
      prop.put("url", cmd.getOptionValue("u"));
    }
    checkProperties(prop);
    return prop;
  }
  
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
  
  public static void main(String[] args) {
    try {
      go(args);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
  
  public static void go(String[] args) throws Exception {
    getConfiguration();
    CommandLine cmd = getCmdLine(args);
    if (cmd == null) {
      return;
    }
    Properties prop = getProperties(cmd);
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
