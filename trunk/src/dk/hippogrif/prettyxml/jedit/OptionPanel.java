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
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.gjt.sp.jedit.OptionPane;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.browser.VFSBrowser;

/**
 * jedit options for prettyxml plugin. 
 * Properties are handled by {@link dk.hippogrif.prettyxml.PropertiesPane}.
 */
public class OptionPanel extends javax.swing.JPanel implements OptionPane {
  final static private String PREFIX = "prettyxml.";
  private String version;
  private Properties defaultProperties;
  
  /** Creates new form OptionPanel */
  public OptionPanel() {
    initComponents();
  }

  public Component getComponent() {
    return this;    
  }
  
  public String getName() {
    return "prettyxml";
  }
  
  public void init() {
    version = jEdit.getProperty(PREFIX + "version");
    propertiesPane.setProperties(initProperties());
  }
  
  static Properties initProperties() {
    Properties prop = new Properties();
    for (int i=0; i<PrettyPrint.BASIC_KEYS.length; i++) {
      String key = PrettyPrint.BASIC_KEYS[i];
      String value = jEdit.getProperty(PREFIX + key);
      if (value != null) {
        prop.setProperty(key, value);
      }
    }
    return prop;
  }
  
  public void save() {
    saveProperties(propertiesPane.getProperties());
  }
  
  static void saveProperties(Properties prop) {
    for (Enumeration e = prop.propertyNames() ; e.hasMoreElements() ;) {
      String key = (String)e.nextElement();
      jEdit.setProperty(PREFIX + key, prop.getProperty(key));
    }
  }
          
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
  private void initComponents() {
    propertiesPane = new dk.hippogrif.prettyxml.PropertiesPane();
    buttonPanel = new javax.swing.JPanel();
    selectButton = new javax.swing.JButton();
    openButton = new javax.swing.JButton();
    saveButton = new javax.swing.JButton();

    setLayout(new java.awt.BorderLayout());

    add(propertiesPane, java.awt.BorderLayout.CENTER);

    selectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dk/hippogrif/prettyxml/image/Bookmarks16.gif")));
    selectButton.setToolTipText("select predefined setting");
    selectButton.setPreferredSize(new java.awt.Dimension(22, 22));
    selectButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        selectButtonActionPerformed(evt);
      }
    });

    buttonPanel.add(selectButton);

    openButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dk/hippogrif/prettyxml/image/Open16.gif")));
    openButton.setToolTipText("load properties from file");
    openButton.setPreferredSize(new java.awt.Dimension(22, 22));
    openButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openButtonActionPerformed(evt);
      }
    });

    buttonPanel.add(openButton);

    saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dk/hippogrif/prettyxml/image/SaveAs16.gif")));
    saveButton.setToolTipText("store properties in file");
    saveButton.setPreferredSize(new java.awt.Dimension(22, 22));
    saveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveButtonActionPerformed(evt);
      }
    });

    buttonPanel.add(saveButton);

    add(buttonPanel, java.awt.BorderLayout.NORTH);

  }
  // </editor-fold>//GEN-END:initComponents

  private String getParent(File file) {
    String parent = file.getParent();
    return (parent != null) ? parent + "/" : null;
  }
  
  private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
    String dir = jEdit.getProperty(PREFIX + "dir");
    String as[] = GUIUtilities.showVFSFileDialog(null, dir, VFSBrowser.SAVE_DIALOG, false);
    if(as != null) {
      String path = as[0];
      try {
        File file = new File(path);
        jEdit.setProperty(PREFIX + "dir", getParent(file));
        PrettyPrint.storeProperties(file, propertiesPane.getProperties());
        GUIUtilities.message(this, "store-done", new Object[]{path});
      } catch (IOException e) {
        GUIUtilities.error(this, "store-error", new Object[]{path, e.getMessage()});
      }
    }
  }//GEN-LAST:event_saveButtonActionPerformed

  private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
    String dir = jEdit.getProperty(PREFIX + "dir");
    String as[] = GUIUtilities.showVFSFileDialog(null, dir, VFSBrowser.OPEN_DIALOG, false);
    if(as != null) {
      String path = as[0];
      try {
        File file = new File(path);
        jEdit.setProperty(PREFIX + "dir", getParent(file));
        Properties prop = PrettyPrint.loadProperties(file);
        PrettyPrint.checkProperties(prop, false);
        propertiesPane.setProperties(prop);
        GUIUtilities.message(this, "load-done", new Object[]{path});
      } catch (IOException e) {
        GUIUtilities.error(this, "load-error", new Object[]{path, e.getMessage()});
      } catch (Exception e) {
        GUIUtilities.error(this, "load-property-error", new Object[]{path, e.getMessage()});
      }
    }
  }//GEN-LAST:event_openButtonActionPerformed

  private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
    String s = (String)JOptionPane.showInputDialog(
            this,
            "Select a setting",
            "Properties settings",
            JOptionPane.PLAIN_MESSAGE,
            null,
            PrettyPrint.getSettings(),
            PrettyPrint.getSettings()[0]);
    if (s != null) {
      propertiesPane.setProperties(PrettyPrint.getSetting(s));
    }
  }//GEN-LAST:event_selectButtonActionPerformed
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel buttonPanel;
  private javax.swing.JButton openButton;
  private dk.hippogrif.prettyxml.PropertiesPane propertiesPane;
  private javax.swing.JButton saveButton;
  private javax.swing.JButton selectButton;
  // End of variables declaration//GEN-END:variables
  
}
