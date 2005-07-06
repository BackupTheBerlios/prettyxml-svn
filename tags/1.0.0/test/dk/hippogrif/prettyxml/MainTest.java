/*
 * MainTest.java
 * JUnit based test
 */

package dk.hippogrif.prettyxml;

import junit.framework.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.jdom.transform.XSLTransformer;

/**
 *
 * @author jgortz
 */
public class MainTest extends TestCase {
  
  public MainTest(String testName) {
    super(testName);
  }
  
  private String testdir, tmpdir;

  protected String getDir(String property) throws Exception {
    String name = "dk.hippogrif.prettyxml.MainTest."+property;
    String dir = System.getProperty(name);
    if (dir == null || !new File(dir).isDirectory()) {
      throw new Exception("cannot find dir "+name+"="+dir);
    }
    return dir;
  }
  
  protected void setUp() throws Exception {
    testdir = getDir("dir");
    tmpdir = getDir("tmp");
  }
  
  protected void tearDown() throws Exception {
  }
  
  public static Test suite() {
    TestSuite suite = new TestSuite(MainTest.class);
    
    return suite;
  }
  
  /**
   * Test of getVersion method, of class dk.hippogrif.prettyxml.Main.
   */
  public void testGetVersion() {
    System.out.println("testGetVersion");
    assertNull(Main.getVersion());
  }
  
  /**
   * Test of getCmdLine method, of class dk.hippogrif.prettyxml.Main.
   */
  public void testGetCmdLine() {
    System.out.println("testGetCmdLine");
    CommandLine cmd = Main.getCmdLine(new String[0]);
    assertNull(cmd);
    cmd = Main.getCmdLine(new String[]{"x"});
    assertNull(cmd);
    cmd = Main.getCmdLine(new String[]{"-v"});
    assertNull(cmd);
    cmd = Main.getCmdLine(new String[]{"-h"});
    assertNull(cmd);
    cmd = Main.getCmdLine(new String[]{"-p","ccc"});
    assertNotNull(cmd);
    cmd = Main.getCmdLine(new String[]{"-s","-u","ccc"});
    assertNotNull(cmd);
    cmd = Main.getCmdLine(new String[]{"-a","-i","file"});
    assertNotNull(cmd);
    assertTrue(cmd.hasOption("a") && cmd.hasOption("i"));
  }
  
  /**
   * Test of setIndentation method, of class dk.hippogrif.prettyxml.Main.
   */
  public void testSetIndentation() {
    System.out.println("testSetIndentation");
    Format format = Format.getRawFormat();
    try {
      Main.setIndentation("notanumber", format);
      fail("notanumber");
    } catch (Exception e) {}
    try {
      Main.setIndentation("0", format);
      fail("0");
    } catch (Exception e) {}
    try {
      Main.setIndentation("1", format);
      assertTrue(format.getIndent().equals(" "));
      Main.setIndentation("8", format);
      assertTrue(format.getIndent().equals("        "));
    } catch (Exception e) {
      fail(e.toString());
    }
  }
  
  /**
   * Test of initFormat method, of class dk.hippogrif.prettyxml.Main.
   */
  public void testInitFormat() {
    System.out.println("testInitFormat");
    Properties prop = new Properties();
    Format format;
    try {
      format = Main.initFormat(prop);
      assertTrue(format.getTextMode().equals(Format.TextMode.TRIM));
      prop.setProperty("format", "PRETTY");
      format = Main.initFormat(prop);
      assertTrue(format.getTextMode().equals(Format.TextMode.TRIM));
      prop.setProperty("format", "COMPACT");
      format = Main.initFormat(prop);
      assertTrue(format.getTextMode()==Format.TextMode.NORMALIZE);
      prop.setProperty("format", "RAW");
      format = Main.initFormat(prop);
      assertTrue(format.getTextMode()==Format.TextMode.PRESERVE);
      prop.setProperty("format", "RAW");
      prop.setProperty("encoding", "UTF-16");
      prop.setProperty("expandEmptyElements", "TRUE");
      prop.setProperty("indent", "3");
      prop.setProperty("lineSeparator", "\r");
      prop.setProperty("omitDeclaration", "TRUE");
      prop.setProperty("omitEncoding", "TRUE");
      prop.setProperty("textMode", "NORMALIZE");
      format = Main.initFormat(prop);
      assertTrue(format.getEncoding().equals("UTF-16"));
      assertTrue(format.getExpandEmptyElements());
      assertTrue(format.getIndent().equals("   "));
      assertTrue(format.getLineSeparator().equals("\r"));
      assertTrue(format.getOmitDeclaration());
      assertTrue(format.getOmitEncoding());
      assertTrue(format.getTextMode()==Format.TextMode.NORMALIZE);
      prop.clear();
      prop.setProperty("format", "RAW");
      prop.setProperty("expandEmptyElements", "FALSE");
      prop.setProperty("omitDeclaration", "FALSE");
      prop.setProperty("omitEncoding", "FALSE");
      prop.setProperty("textMode", "TRIM");
      format = Main.initFormat(prop);
      assertTrue(!format.getExpandEmptyElements());
      assertTrue(!format.getOmitDeclaration());
      assertTrue(!format.getOmitEncoding());
      assertTrue(format.getTextMode().equals(Format.TextMode.TRIM));
      prop.clear();
      prop.setProperty("format", "RAW");
      prop.setProperty("textMode", "TRIM_FULL_WHITE");
      format = Main.initFormat(prop);
      assertTrue(format.getTextMode().equals(Format.TextMode.TRIM_FULL_WHITE));
      prop.clear();
      prop.setProperty("format", "RAW");
      prop.setProperty("textMode", "PRESERVE");
      format = Main.initFormat(prop);
      assertTrue(format.getTextMode().equals(Format.TextMode.PRESERVE));
    } catch (Exception e) {
      fail(e.toString());
    }
    try {
      prop.clear();
      prop.setProperty("textMode", "unknown");
      format = Main.initFormat(prop);
      fail("unknown textMode");
    } catch (Exception e) {}
    try {
      prop.clear();
      prop.setProperty("format", "unknown");
      format = Main.initFormat(prop);
      fail("unknown format");
    } catch (Exception e) {}
  }
  
  /**
   * Test of readFormat method, of class dk.hippogrif.prettyxml.Main.
   */
  public void testCheckProperties() {
    System.out.println("testCheckProperties");
    Properties prop = new Properties();;
    try {
      Main.checkProperties(prop);
    } catch (Exception e) {
      fail(e.toString());
    }
    try {
      prop.setProperty("format","x");
      prop.setProperty("encoding","x");
      prop.setProperty("expandEmptyElements","true");
      prop.setProperty("indent","x");
      prop.setProperty("lineSeparator","x");
      prop.setProperty("omitDeclaration","false");
      prop.setProperty("omitEncoding","True");
      prop.setProperty("textMode","x");
      prop.setProperty("indentAttributes","False");
      prop.setProperty("sortAttributes","TRUE");
      prop.setProperty("transform","x");
      prop.setProperty("input","x");
      prop.setProperty("output","x");
      assertTrue(Main.keys.size()-1 == prop.size());
      Main.checkProperties(prop);
      assertTrue(11 == prop.size());
    } catch (Exception e) {
      fail(e.toString());
    }
    try {
      prop.clear();
      prop.setProperty("url","x");
      Main.checkProperties(prop);
    } catch (Exception e) {
      fail(e.toString());
    }
    try {
      prop.clear();
      prop.setProperty("x","y");
      Main.checkProperties(prop);
      fail("unknown property");
    } catch (Exception e) {}
    try {
      prop.clear();
      prop.setProperty("input","aaa");
      prop.setProperty("url","bbb");
      Main.checkProperties(prop);
      fail("input and url");
    } catch (Exception e) {}
    try {
      prop.clear();
      prop.setProperty("omitEncoding","y");
      Main.checkProperties(prop);
      fail("bad boolean");
    } catch (Exception e) {}
  }
  
  
  /**
   * Test of readFormat method, of class dk.hippogrif.prettyxml.Main.
   */
  public void testReadFormat() {
    System.out.println("testReadFormat");
    try {
      Main.readFormat(testdir+"/ex.properties");
    } catch (Exception e) {
      fail(e.toString());
    }
  }
  
  /**
   * Test of checkBoolean method, of class dk.hippogrif.prettyxml.Main.
   */
  public void testCheckBoolean() {
    System.out.println("testCheckBoolean");
    Properties prop = new Properties();
    prop.setProperty("f", "faLSe");
    prop.setProperty("t", "TRue");
    prop.setProperty("w", "what");
    try {
      Main.checkBoolean("notkey", prop);
      assertTrue(prop.size() == 3);
      Main.checkBoolean("f", prop);
      assertFalse(prop.containsKey("f"));
      Main.checkBoolean("t", prop);
      assertTrue(prop.containsKey("t"));
      Main.checkBoolean("w", prop);
      fail("what");
    } catch (Exception e) {}
  }
  
  /**
   * Test of getProperties method, of class dk.hippogrif.prettyxml.Main.
   */
  public void testGetProperties() {
    System.out.println("testGetProperties");
    CommandLine cmd;
    Properties prop;
    try {
      cmd = Main.getCmdLine(new String[]{"-n","5","-a","-t","xxx;yyy","-i","aaa","-o","bbb"});
      prop = Main.getProperties(cmd);
      assertTrue("5".equals(prop.getProperty("indent")));
      assertTrue("TRUE".equals(prop.getProperty("indentAttributes")));
      assertTrue("xxx;yyy".equals(prop.getProperty("transform")));
      assertTrue("aaa".equals(prop.getProperty("input")));
      assertTrue("bbb".equals(prop.getProperty("output")));
      cmd = Main.getCmdLine(new String[]{"-s","-u","ccc"});
      prop = Main.getProperties(cmd);
      assertTrue("TRUE".equals(prop.getProperty("sortAttributes")));
      assertTrue("ccc".equals(prop.getProperty("url")));
    } catch (Exception e) {
      fail(e.toString());
    }
    try {
      String propertyFile = testdir+"/ex.properties";
      cmd = Main.getCmdLine(new String[]{"-p",propertyFile});
      prop = Main.getProperties(cmd);
      assertTrue("3".equals(prop.getProperty("indent")));
      assertTrue("TRUE".equals(prop.getProperty("indentAttributes")));
      cmd = Main.getCmdLine(new String[]{"-n","4","-p",propertyFile});
      prop = Main.getProperties(cmd);
      assertTrue("4".equals(prop.getProperty("indent")));
      assertTrue("TRUE".equals(prop.getProperty("indentAttributes")));
    } catch (Exception e) {
      fail(e.toString());
    }
  }
  
  /**
   * Test of readFormat method, of class dk.hippogrif.prettyxml.Main.
   */
  public void testMkTransformer() {
    System.out.println("testMkXSLTransformer");
    try {
      Main.mkTransformer(testdir+"/sort-elements.xslt");
      Main.mkTransformer("sort-attributes.xslt");
    } catch (Exception e) {
      fail(e.toString());
    }
    try {
      Main.mkTransformer("unknown.xslt");
      fail("unknown xslt");
    } catch (Exception e) {}
  }
  
  /**
   * Test of main method, of class dk.hippogrif.prettyxml.Main.
   */
  public void testGo() {
    System.out.println("testMain");
    try {
      Main.go(new String[]{"-a","-s","-i",testdir+"/in1.xml","-o",tmpdir+"/tmp1.xml"});
      assertTrue(FileUtils.contentEquals(new File(testdir+"/out1.xml"), new File(tmpdir+"/tmp1.xml")));
      Main.go(new String[]{"-a","-u","http://www.cafeconleche.org/books/xml/examples/07/family.xml","-o",tmpdir+"/tmp2.xml"});
      assertTrue(FileUtils.contentEquals(new File(testdir+"/family.xml"), new File(tmpdir+"/tmp2.xml")));
      
    } catch (Exception e) {
      fail(e.toString());
    }
  }
  
}
