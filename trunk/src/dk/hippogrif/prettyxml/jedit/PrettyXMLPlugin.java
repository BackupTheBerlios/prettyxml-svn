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

package dk.hippogrif.prettyxml.jedit;

import dk.hippogrif.prettyxml.PrettyPrint;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Properties;
import jdiff.DualDiff;
import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.EditPlugin;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.Mode;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.browser.VFSBrowser;
import org.gjt.sp.jedit.io.VFSManager;
import org.gjt.sp.util.Log;

/**
 * Plugin for jEdit.
 */
public class PrettyXMLPlugin extends EditPlugin {
  
  private String version;
  
  /**
   * Sets default options if error in saved properties.
   */
  public void start() {
    version = PrettyPrint.getVersion();
    jEdit.setTemporaryProperty("prettyxml.version", version);
    try {
      Properties prop = OptionPanel.initProperties();
      PrettyPrint.checkProperties(prop, false);
    } catch (Exception e) {
      Log.log(Log.WARNING, this, "using default prettyxml options because of error in user setting properties:\n" + e.getMessage());
      OptionPanel.saveProperties(PrettyPrint.getDefaultSetting());
    }
  }
  
  /**
   * Prettyprint current buffer.
   *
   * @param view holds current view and buffer
   * @param newBuffer to print in same or new buffer
   * @param prop holds prettyprint properties to use or null to use jedit options
   */
  public static boolean prettyPrint(View view, boolean newBuffer, Properties prop) {
    boolean result = false;
    PrettyXMLPlugin logid = new PrettyXMLPlugin();
    Buffer buffer = view.getBuffer();
    buffer.writeLock();
    try {
      String s = buffer.getText(0, buffer.getLength());
      if (prop == null) {
        prop = OptionPanel.initProperties();
      }
      PrettyPrint.checkProperties(prop, false);
      String linesep = prop.getProperty(PrettyPrint.LINE_SEPARATOR);
      prop.setProperty(PrettyPrint.LINE_SEPARATOR, "\n");
      s = PrettyPrint.execute(prop, s);
      if (newBuffer) {
        Mode mode = buffer.getMode();
        buffer.writeUnlock();
        buffer = jEdit.newFile(view);
        buffer.writeLock();
        buffer.setMode(mode);
        buffer.insert(0, s.substring(0, s.length()-1));
      } else {
        buffer.beginCompoundEdit();
        buffer.remove(0, buffer.getLength());
        buffer.removeAllMarkers();
        buffer.insert(0, s.substring(0, s.length()-1));
        buffer.endCompoundEdit();
      }
      buffer.setStringProperty(buffer.LINESEP, linesep);
      buffer.setStringProperty(buffer.ENCODING, prop.getProperty(PrettyPrint.ENCODING));
      result = true;
    } catch (Exception e) {
      String s = e.getMessage();
      if (s == null) {
        s = e.getClass().getName() + "\nsee activity.log for stack trace";
        Log.log(Log.ERROR, logid, getStackTrace(e));
      }
      GUIUtilities.error(view, "prettyprint-error", new Object[]{s});
    } finally {
      buffer.writeUnlock();
    }
    return result;
  }
  
  /**
   * Diff two files which are opened, prettyprinted and diffed in current view with JDiffPlugin.
   *
   * @param view holds current view
   * @param prop holds prettyprint properties to use or null to use jedit options
   */
  public static boolean diff(View view, Properties prop) {
    boolean result = false;
      if (jEdit.getPlugin("jdiff.JDiffPlugin") == null) {
        GUIUtilities.error(view, "prettyprint-no-jdiff", null);
        return result;
      }
    view.unsplit();
    String dir = jEdit.getProperty("prettyxml.userdir");
    if (dir == null) {
      dir = System.getProperty("user.home");
    }
    String[] sa = GUIUtilities.showVFSFileDialog(view,dir,VFSBrowser.OPEN_DIALOG,false);
    if (sa == null || sa[0] == null) return result;
    Buffer buf1 = jEdit.openFile(view, sa[0]);
    view.splitVertically();
    dir = new File(sa[0]).getParent()+"/";
    sa = GUIUtilities.showVFSFileDialog(view,dir,VFSBrowser.OPEN_DIALOG,false);
    if (sa == null || sa[0] == null) return result;
    jEdit.setProperty("prettyxml.userdir", new File(sa[0]).getParent()+"/");
    Buffer buf2 = jEdit.openFile(view, sa[0]);
    view.showWaitCursor();
    try {
      view.goToBuffer(buf1);
      VFSManager.waitForRequests();
      if (!dk.hippogrif.prettyxml.jedit.PrettyXMLPlugin.prettyPrint(view, true, prop)) return result;
      view.goToBuffer(buf2);
      if (!dk.hippogrif.prettyxml.jedit.PrettyXMLPlugin.prettyPrint(view, true, prop)) return result;
      DualDiff.toggleFor(view);
      result = true;
    } finally {
      view.hideWaitCursor();
    }
    return result;
  }
  
  public static String getStackTrace(Throwable e) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    e.printStackTrace(ps);
    ps.close();
    return baos.toString();
  }
}
