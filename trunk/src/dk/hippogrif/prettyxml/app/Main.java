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

package dk.hippogrif.prettyxml.app;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;
import dk.hippogrif.prettyxml.*;

/**
 * A command line application for prettyprinting an xml file.
 * If no options are present the GUI application is invoked.
 * See {@link dk.hippogrif.prettyxml}.
 *
 * @author Jesper Goertz
 * @see MainJFrame
 */
public class Main {
  
  private static Logger logger = Logger.getLogger("dk.hippogrif.prettyxml.Main");
  private static String version;
  private static Options options;
  private static List optionList;
  
  /**
   * init options for resetting options between (test) uses,
   * because commons-cli-1.0 from maven stores state in them
   * whereas the original 1.0 does not
   */
  static void initOptions() {
    options = mkOptions();
    optionList = sortOptions(options);
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
    ps.println("  encoding = string, e.g., UTF-8 (default), ISO-8859-1");
    ps.println("  expandEmptyElements = TRUE | FALSE (default)");
    ps.println("  indent = no of spaces to indent (min 1, max 99)");
    ps.println("  lineSeparator = string, i.e., \\r, \\n or \\r\\n (default)");
    ps.println("  omitDeclaration = TRUE | FALSE (default)");
    ps.println("  omitEncoding = TRUE | FALSE (default)");
    ps.println("  textMode = NORMALIZE | TRIM | TRIM_FULL_WHITE | PRESERVE (default)");
    ps.println("  indentAttributes = TRUE | FALSE (default)");
    ps.println("  sortAttributes = TRUE | FALSE (default)");
    ps.println("  transform = an xslt pipeline of one or more stylesheets separated by ;");
    ps.println("  input = input file");
    ps.println("  url = input url");
    ps.println("  output = output file");
    ps.println("");
    ps.println("use the xslt pipeline to sort elements or filter nodes");
    ps.println("standard input is used if no file or url is specified");
    ps.println("standard output is used if no file is specified");
    ps.println("property file and transformation stylesheets are located in this order:");
    ps.println("  built-in, file, classpath resource");
    ps.print("built-in property files:");
    String[] sa = PrettyPrint.getSettings();
    for (int i=0; i<sa.length; i++) {
      ps.print(" "+sa[i]);
    }
    ps.println("");
    ps.print("built-in transformation stylesheets:");
    sa = PrettyPrint.getTransformations();
    for (int i=0; i<sa.length; i++) {
      ps.print(" "+sa[i]);
    }
    ps.println("");
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
  
  static CommandLine parse(String[] args) {
    CommandLineParser parser = new BasicParser();
    CommandLine cmd = null;
    try {
      cmd = parser.parse(options, args);
    } catch (ParseException pe) {
      logger.log(Level.FINE, "parse", pe);
    }
    return cmd;
  }
  
  static CommandLine getCmdLine(String[] args) {
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
  
  static Properties getProperties(CommandLine cmd) throws Exception {
    Properties prop = new Properties();
    if (cmd.hasOption("p")) {
      String pname = cmd.getOptionValue("p");
      prop = PrettyPrint.getSetting(pname);
      if (prop == null) {
        prop = PrettyPrint.loadProperties(new File(pname));
      }
    } else {
      // default Pretty format
      prop.put(PrettyPrint.INDENT, "2");
      prop.put(PrettyPrint.TEXT_MODE, "TRIM");
    }
    if (cmd.hasOption("n")) {
      prop.put(PrettyPrint.INDENT, cmd.getOptionValue("n"));
    }
    if (cmd.hasOption("a")) {
      prop.put(PrettyPrint.INDENT_ATTRIBUTES, "TRUE");
    }
    if (cmd.hasOption("s")) {
      prop.put(PrettyPrint.SORT_ATTRIBUTES, "TRUE");
    }
    if (cmd.hasOption("t")) {
      prop.put(PrettyPrint.TRANSFORM, cmd.getOptionValue("t"));
    }
    if (cmd.hasOption("i")) {
      prop.put(PrettyPrint.INPUT, cmd.getOptionValue("i"));
    }
    if (cmd.hasOption("o")) {
      prop.put(PrettyPrint.OUTPUT, cmd.getOptionValue("o"));
    }
    if (cmd.hasOption("u")) {
      prop.put(PrettyPrint.URL, cmd.getOptionValue("u"));
    }
    PrettyPrint.checkProperties(prop, true);
    return prop;
  }
  
  /**
   * Application using commandline options or running GUI if no options.
   */
  public static void main(String[] args) {
    try {
      PrettyPrint prettyPrint = new PrettyPrint();
      version = prettyPrint.getVersion();
      if (args.length == 0) {
        MainJFrame.run(version);
      } else {
        go(args);
      }
    } catch (Exception e) {
      logger.log(Level.FINE, "main", e);
      System.err.println(e.getMessage());
    }
  }
  
  static void go(String[] args) throws Exception {
    CommandLine cmd = getCmdLine(args);
    if (cmd == null) {
      return;
    }
    Properties prop = getProperties(cmd);
    PrettyPrint.execute(prop);
  }
  
}
