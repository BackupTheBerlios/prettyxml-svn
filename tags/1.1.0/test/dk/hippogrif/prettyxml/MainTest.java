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
   * Test of go method, of class dk.hippogrif.prettyxml.Main.
   */
  public void testGo() {
    System.out.println("testGo");
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
