/*
 * PrettyPrintTest.java
 * JUnit based test
 *
 * Created on 19. juli 2005, 22:42
 */

package dk.hippogrif.prettyxml;

import junit.framework.*;
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
 *
 * @author jgortz
 */
public class PrettyPrintTest extends TestCase {
  
  private String testdir, tmpdir;
  
  protected String getDir(String property) throws Exception {
    String name = "dk.hippogrif.prettyxml.app.MainTest."+property;
    String dir = System.getProperty(name);
    if (dir == null || !new File(dir).isDirectory()) {
      throw new Exception("cannot find dir "+name+"="+dir);
    }
    return dir;
  }
  
  public PrettyPrintTest(String testName) {
    super(testName);
  }
  
  protected void setUp() throws Exception {
    testdir = getDir("dir");
    tmpdir = getDir("tmp");
  }
  
  protected void tearDown() throws Exception {
  }
  
  public static Test suite() {
    TestSuite suite = new TestSuite(PrettyPrintTest.class);
    
    return suite;
  }
  
  /**
   * Test of getVersion method, of class dk.hippogrif.prettyxml.PrettyPrint.
   */
  public void testGetVersion() {
    System.out.println("testGetVersion");
    try {
      PrettyPrint pp = new PrettyPrint();
      assertNotNull(pp.getVersion());
    } catch (Exception e) {
      fail(e.toString());
    }
  }
  
  /**
   * Test of loadConfiguration method, of class dk.hippogrif.prettyxml.PrettyPrint.
   */
  public void testLoadConfiguration() {
    System.out.println("testLoadConfiguration");
    try {
      PrettyPrint pp = new PrettyPrint();
      pp.loadConfiguration("prettyxml.properties");
      assertNotNull(pp.getVersion());
    } catch (Exception e) {
      fail(e.toString());
    }
  }
  
  /**
   * Test of setIndentation method, of class dk.hippogrif.prettyxml.PrettyPrint.
   */
  public void testSetIndentation() {
    System.out.println("testSetIndentation");
    Format format = Format.getRawFormat();
    try {
      PrettyPrint.setIndentation("notanumber", format);
      fail("notanumber");
    } catch (Exception e) {}
    try {
      PrettyPrint.setIndentation("0", format);
      assertNull(format.getIndent());
      PrettyPrint.setIndentation("1", format);
      assertTrue(format.getIndent().equals(" "));
      PrettyPrint.setIndentation("8", format);
      assertTrue(format.getIndent().equals("        "));
    } catch (Exception e) {
      fail(e.toString());
    }
  }
  
  /**
   * Test of initFormat method, of class dk.hippogrif.prettyxml.PrettyPrint.
   */
  public void testInitFormat() {
    System.out.println("testInitFormat");
    Properties prop = new Properties();
    Format format;
    try {
      format = PrettyPrint.initFormat(prop);
      assertTrue(format.getTextMode().equals(Format.TextMode.PRESERVE));
      prop.setProperty("format", "RAW");
      prop.setProperty("encoding", "UTF-16");
      prop.setProperty("expandEmptyElements", "TRUE");
      prop.setProperty("indent", "3");
      prop.setProperty("lineSeparator", "\r");
      prop.setProperty("omitDeclaration", "TRUE");
      prop.setProperty("omitEncoding", "TRUE");
      prop.setProperty("textMode", "NORMALIZE");
      format = PrettyPrint.initFormat(prop);
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
      format = PrettyPrint.initFormat(prop);
      assertTrue(!format.getExpandEmptyElements());
      assertTrue(!format.getOmitDeclaration());
      assertTrue(!format.getOmitEncoding());
      assertTrue(format.getTextMode().equals(Format.TextMode.TRIM));
      prop.clear();
      prop.setProperty("format", "RAW");
      prop.setProperty("textMode", "TRIM_FULL_WHITE");
      format = PrettyPrint.initFormat(prop);
      assertTrue(format.getTextMode().equals(Format.TextMode.TRIM_FULL_WHITE));
      prop.clear();
      prop.setProperty("format", "RAW");
      prop.setProperty("textMode", "PRESERVE");
      format = PrettyPrint.initFormat(prop);
      assertTrue(format.getTextMode().equals(Format.TextMode.PRESERVE));
    } catch (Exception e) {
      fail(e.toString());
    }
    try {
      prop.clear();
      prop.setProperty("textMode", "unknown");
      format = PrettyPrint.initFormat(prop);
      fail("unknown textMode");
    } catch (Exception e) {}
  }
  
  /**
   * Test of checkProperties method, of class dk.hippogrif.prettyxml.PrettyPrint.
   */
  public void testCheckProperties() {
    System.out.println("testCheckProperties");
    Properties prop = new Properties();;
    try {
      PrettyPrint.checkProperties(prop, true);
    } catch (Exception e) {
      fail(e.toString());
    }
    try {
      prop.setProperty("encoding","x");
      prop.setProperty("expandEmptyElements","true");
      prop.setProperty("indent","1");
      prop.setProperty("lineSeparator","\n");
      prop.setProperty("omitDeclaration","false");
      prop.setProperty("omitEncoding","True");
      prop.setProperty("textMode","x");
      prop.setProperty("indentAttributes","False");
      prop.setProperty("sortAttributes","TRUE");
      prop.setProperty("transform","x");
      prop.setProperty("input","x");
      prop.setProperty("output","x");
      assertTrue(PrettyPrint.keys.size()-1 == prop.size());
      PrettyPrint.checkProperties(prop, true);
      assertTrue(12 == prop.size());
    } catch (Exception e) {
      fail(e.toString());
    }
    try {
      prop.clear();
      prop.setProperty("url","x");
      PrettyPrint.checkProperties(prop, true);
    } catch (Exception e) {
      fail(e.toString());
    }
    try {
      prop.clear();
      prop.setProperty("indent","0");
      PrettyPrint.checkProperties(prop, true);
      assertFalse(prop.containsKey("indent"));
    } catch (Exception e) {}
    try {
      prop.clear();
      prop.setProperty("indent","100");
      PrettyPrint.checkProperties(prop, true);
      fail("indent 100");
    } catch (Exception e) {}
    try {
      prop.clear();
      prop.setProperty("indent","x");
      PrettyPrint.checkProperties(prop, true);
      fail("indent not integer");
    } catch (Exception e) {}
    try {
      prop.clear();
      prop.setProperty("x","y");
      PrettyPrint.checkProperties(prop, true);
      assertFalse(prop.containsKey("x"));
    } catch (Exception e) {}
    try {
      prop.clear();
      prop.setProperty("input","aaa");
      prop.setProperty("url","bbb");
      PrettyPrint.checkProperties(prop, true);
      fail("input and url");
    } catch (Exception e) {}
    try {
      prop.clear();
      prop.setProperty("omitEncoding","y");
      PrettyPrint.checkProperties(prop, true);
      fail("bad boolean");
    } catch (Exception e) {}
  }
  
  /**
   * Test of loadProperties method, of class dk.hippogrif.prettyxml.PrettyPrint.
   */
  public void testLoadProperties() {
    System.out.println("testLoadProperties");
    try {
      PrettyPrint.loadProperties(new File(testdir+"/ex.properties"));
    } catch (Exception e) {
      fail(e.toString());
    }
  }
  
  /**
   * Test of storeProperties method, of class dk.hippogrif.prettyxml.PrettyPrint.
   */
  public void testStoreProperties() {
    System.out.println("testStoreProperties");
    try {
      Properties prop = PrettyPrint.loadProperties(new File(testdir+"/ex.properties"));
      PrettyPrint.storeProperties(new File(tmpdir+"/ex.properties"), prop);
      Properties tmpprop = PrettyPrint.loadProperties(new File(tmpdir+"/ex.properties"));
      assertTrue(prop.equals(tmpprop));
    } catch (Exception e) {
      fail(e.toString());
    }
  }
  
  /**
   * Test of checkBoolean method, of class dk.hippogrif.prettyxml.PrettyPrint.
   */
  public void testCheckBoolean() {
    System.out.println("testCheckBoolean");
    Properties prop = new Properties();
    prop.setProperty("f", "faLSe");
    prop.setProperty("t", "TRue");
    prop.setProperty("w", "what");
    try {
      PrettyPrint.checkBoolean("notkey", prop);
      assertTrue(prop.size() == 3);
      PrettyPrint.checkBoolean("f", prop);
      assertTrue(prop.containsKey("f"));
      PrettyPrint.checkBoolean("t", prop);
      assertTrue(prop.containsKey("t"));
      PrettyPrint.checkBoolean("w", prop);
      fail("what");
    } catch (Exception e) {}
  }
  
  /**
   * Test of mkTransformer method, of class dk.hippogrif.prettyxml.PrettyPrint.
   */
  public void testMkTransformer() {
    System.out.println("testMkXSLTransformer");
    try {
      PrettyPrint.mkTransformer(testdir+"/sort-elements.xslt");
      PrettyPrint.mkTransformer("sort-attributes.xslt");
    } catch (Exception e) {
      fail(e.toString());
    }
    try {
      PrettyPrint.mkTransformer("unknown.xslt");
      fail("unknown xslt");
    } catch (Exception e) {}
  }
  
  /**
   * Test of execute method, of class dk.hippogrif.prettyxml.PrettyPrint.
   */
  public void testExecute() {
    System.out.println("testExecute");
    System.out.println("tested by MainTest.testGo");
  }
  
}
