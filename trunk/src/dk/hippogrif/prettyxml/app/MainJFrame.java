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

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.Document;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import dk.hippogrif.prettyxml.*;

/**
 * A GUI application for prettyprinting XML.
 * See {@link dk.hippogrif.prettyxml}.
 *
 * @author  Jesper Goertz
 */
public class MainJFrame extends javax.swing.JFrame implements GParentInterface, PropertyNames {
  
  private static Logger logger = Logger.getLogger(MainJFrame.class.getName());
  private JFileChooser propertyFileChooser = new JFileChooser();
  private JFileChooser inputFileChooser = new JFileChooser();
  private JFileChooser outputFileChooser = new JFileChooser();
  private HTMLDialog help;
  private String version;
  
  /** Creates new form MainJFrame */
  public MainJFrame(String version) {
    this.version = version;
    initComponents();
    propertiesPane.setParent(this);
    initTextFields();
  }
  
  private void initTextFields() {
    DocumentListener listener = new MyDocListener();
    addDocumentListener(inputTextField, listener);
    addDocumentListener(urlTextField, listener);
    addDocumentListener(outputTextField, listener);
  }
  
  private void addDocumentListener(JTextField textField, DocumentListener listener) {
    textField.getDocument().addDocumentListener(listener);
  }
  
  class MyDocListener implements DocumentListener {
    public void changedUpdate(DocumentEvent e) {
      markDirty();
    }
    
    public void insertUpdate(DocumentEvent e) {
      markDirty();
    }
    
    public void removeUpdate(DocumentEvent e) {
      markDirty();
    }
  }
  
  private void prop2ui(Properties prop) {
    propertiesPane.setProperties(prop);
    inputTextField.setText(prop.getProperty(INPUT, ""));
    urlTextField.setText(prop.getProperty(URL, ""));
    outputTextField.setText(prop.getProperty(OUTPUT, ""));
  }
  
  private Properties ui2prop() {
    Properties prop = propertiesPane.getProperties();
    String s;
    if (!(s = inputTextField.getText()).equals("")) prop.setProperty(INPUT,s);
    if (!(s = urlTextField.getText()).equals("")) prop.setProperty(URL,s);
    if (!(s = outputTextField.getText()).equals("")) prop.setProperty(OUTPUT,s);
    return prop;
  }
  
  private void message(String title, Exception e) {
    logger.log(Level.FINE, title, e);
    JOptionPane.showMessageDialog(this, e.getMessage()+"\n["+e.getClass().getName()+"]", title, JOptionPane.ERROR_MESSAGE);
  }
  
  private void message(String title, String message) {
    JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
  }
  
  private void setStatus(File file) {
    filenameTextField.setText(file != null ? file.getPath() : "");
    unmarkDirty();
  }
  
  public void markDirty() {
    saveButton.setEnabled(filenameTextField.getText().length()>0);
  }
  
  private void unmarkDirty() {
    saveButton.setEnabled(false);
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jScrollBar1 = new javax.swing.JScrollBar();
    buttonPanel = new javax.swing.JPanel();
    selectButton = new javax.swing.JButton();
    openButton = new javax.swing.JButton();
    saveButton = new javax.swing.JButton();
    saveAsButton = new javax.swing.JButton();
    filenameTextField = new javax.swing.JTextField();
    executeButton = new javax.swing.JButton();
    helpButton = new javax.swing.JButton();
    propertiesPanel = new javax.swing.JPanel();
    jLabel4 = new javax.swing.JLabel();
    jLabel12 = new javax.swing.JLabel();
    jLabel13 = new javax.swing.JLabel();
    jLabel14 = new javax.swing.JLabel();
    inputTextField = new javax.swing.JTextField();
    urlTextField = new javax.swing.JTextField();
    outputTextField = new javax.swing.JTextField();
    inputButton = new javax.swing.JButton();
    outputButton = new javax.swing.JButton();
    propertiesPane = new dk.hippogrif.prettyxml.PropertiesPane();
    jPanel1 = new javax.swing.JPanel();
    jPanel2 = new javax.swing.JPanel();
    jPanel3 = new javax.swing.JPanel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("prettyxml "+version);
    buttonPanel.setLayout(new java.awt.GridBagLayout());

    selectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dk/hippogrif/prettyxml/image/Bookmarks16.gif")));
    selectButton.setToolTipText("select predefined setting");
    selectButton.setBorder(new javax.swing.border.EtchedBorder());
    selectButton.setMaximumSize(new java.awt.Dimension(20, 20));
    selectButton.setMinimumSize(new java.awt.Dimension(20, 20));
    selectButton.setPreferredSize(new java.awt.Dimension(20, 20));
    selectButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        selectButtonActionPerformed(evt);
      }
    });

    buttonPanel.add(selectButton, new java.awt.GridBagConstraints());

    openButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dk/hippogrif/prettyxml/image/Open16.gif")));
    openButton.setToolTipText("load setting from file");
    openButton.setBorder(new javax.swing.border.EtchedBorder());
    openButton.setPreferredSize(new java.awt.Dimension(20, 20));
    openButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openButtonActionPerformed(evt);
      }
    });

    buttonPanel.add(openButton, new java.awt.GridBagConstraints());

    saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dk/hippogrif/prettyxml/image/Save16.gif")));
    saveButton.setToolTipText("save setting");
    saveButton.setBorder(new javax.swing.border.EtchedBorder());
    saveButton.setEnabled(false);
    saveButton.setPreferredSize(new java.awt.Dimension(20, 20));
    saveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveButtonActionPerformed(evt);
      }
    });

    buttonPanel.add(saveButton, new java.awt.GridBagConstraints());

    saveAsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dk/hippogrif/prettyxml/image/SaveAs16.gif")));
    saveAsButton.setToolTipText("save setting as file");
    saveAsButton.setBorder(new javax.swing.border.EtchedBorder());
    saveAsButton.setPreferredSize(new java.awt.Dimension(20, 20));
    saveAsButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveAsButtonActionPerformed(evt);
      }
    });

    buttonPanel.add(saveAsButton, new java.awt.GridBagConstraints());

    filenameTextField.setEditable(false);
    filenameTextField.setToolTipText("property file");
    filenameTextField.setBorder(new javax.swing.border.EtchedBorder());
    filenameTextField.setMargin(new java.awt.Insets(1, 10, 1, 1));
    filenameTextField.setMinimumSize(new java.awt.Dimension(200, 20));
    filenameTextField.setPreferredSize(new java.awt.Dimension(200, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    buttonPanel.add(filenameTextField, gridBagConstraints);

    executeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dk/hippogrif/prettyxml/image/Print16.gif")));
    executeButton.setToolTipText("prettyprint");
    executeButton.setBorder(new javax.swing.border.EtchedBorder());
    executeButton.setPreferredSize(new java.awt.Dimension(20, 20));
    executeButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        executeButtonActionPerformed(evt);
      }
    });

    buttonPanel.add(executeButton, new java.awt.GridBagConstraints());

    helpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dk/hippogrif/prettyxml/image/Help16.gif")));
    helpButton.setToolTipText("help");
    helpButton.setBorder(new javax.swing.border.EtchedBorder());
    helpButton.setPreferredSize(new java.awt.Dimension(20, 20));
    helpButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        helpButtonActionPerformed(evt);
      }
    });

    buttonPanel.add(helpButton, new java.awt.GridBagConstraints());

    getContentPane().add(buttonPanel, java.awt.BorderLayout.NORTH);

    propertiesPanel.setLayout(new java.awt.GridBagLayout());

    propertiesPanel.setMinimumSize(new java.awt.Dimension(400, 330));
    propertiesPanel.setPreferredSize(new java.awt.Dimension(400, 330));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel4, gridBagConstraints);

    jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel12.setText("input:");
    jLabel12.setPreferredSize(new java.awt.Dimension(110, 14));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 15;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel12, gridBagConstraints);

    jLabel13.setText("url:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 16;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel13, gridBagConstraints);

    jLabel14.setText("output:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 17;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel14, gridBagConstraints);

    inputTextField.setToolTipText("input xml file - stdin is used if neither input file nor url is specified");
    inputTextField.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        inputTextFieldFocusLost(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 15;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    propertiesPanel.add(inputTextField, gridBagConstraints);

    urlTextField.setToolTipText("input url - stdin is used if neither input file nor url is specified");
    urlTextField.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        urlTextFieldFocusLost(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 16;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    propertiesPanel.add(urlTextField, gridBagConstraints);

    outputTextField.setToolTipText("output file - stdout is used if no file is specified");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 17;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    propertiesPanel.add(outputTextField, gridBagConstraints);

    inputButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dk/hippogrif/prettyxml/image/Open16.gif")));
    inputButton.setToolTipText("choose file");
    inputButton.setBorder(null);
    inputButton.setMaximumSize(new java.awt.Dimension(20, 20));
    inputButton.setMinimumSize(new java.awt.Dimension(20, 20));
    inputButton.setPreferredSize(new java.awt.Dimension(20, 20));
    inputButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        inputButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 15;
    gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
    propertiesPanel.add(inputButton, gridBagConstraints);

    outputButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dk/hippogrif/prettyxml/image/Open16.gif")));
    outputButton.setToolTipText("choose file");
    outputButton.setBorder(null);
    outputButton.setMaximumSize(new java.awt.Dimension(20, 20));
    outputButton.setMinimumSize(new java.awt.Dimension(20, 20));
    outputButton.setPreferredSize(new java.awt.Dimension(20, 20));
    outputButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        outputButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 17;
    gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
    propertiesPanel.add(outputButton, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
    propertiesPanel.add(propertiesPane, gridBagConstraints);

    getContentPane().add(propertiesPanel, java.awt.BorderLayout.CENTER);

    jPanel1.setMinimumSize(new java.awt.Dimension(5, 10));
    getContentPane().add(jPanel1, java.awt.BorderLayout.EAST);

    jPanel2.setMinimumSize(new java.awt.Dimension(5, 10));
    getContentPane().add(jPanel2, java.awt.BorderLayout.WEST);

    getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

    pack();
  }
  // </editor-fold>//GEN-END:initComponents

  private void inputTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputTextFieldFocusLost
    if (inputTextField.getText().length() > 0) urlTextField.setText("");
  }//GEN-LAST:event_inputTextFieldFocusLost

  private void urlTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_urlTextFieldFocusLost
    if (urlTextField.getText().length() > 0) inputTextField.setText("");
  }//GEN-LAST:event_urlTextFieldFocusLost

  private void inputButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputButtonActionPerformed
    int returnVal = inputFileChooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      inputTextField.setText(inputFileChooser.getSelectedFile().getPath());
      markDirty();
      urlTextField.setText("");
    }
  }//GEN-LAST:event_inputButtonActionPerformed

  private void outputButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputButtonActionPerformed
    int returnVal = outputFileChooser.showSaveDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      outputTextField.setText(outputFileChooser.getSelectedFile().getPath());
      markDirty();
    }
  }//GEN-LAST:event_outputButtonActionPerformed
  
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
      prop2ui(PrettyPrint.getSetting(s));
      setStatus(null);
    }
  }//GEN-LAST:event_selectButtonActionPerformed
      
  private void helpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpButtonActionPerformed
    if (help == null) {
      help = new HTMLDialog(this, this.getTitle()+" Help", this.getClass().getResource("help.html"), 30, new Dimension(400, 400));
    }
    help.show();
  }//GEN-LAST:event_helpButtonActionPerformed
                        
  private void executeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executeButtonActionPerformed
    try {
      Properties prop = ui2prop();
      PrettyPrint.execute(prop);
      message("Execute", "done");
    } catch (Exception e) {
      message("Execute", e);
    }
  }//GEN-LAST:event_executeButtonActionPerformed
  
  private void saveAsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsButtonActionPerformed
    try {
      int returnVal = propertyFileChooser.showSaveDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = propertyFileChooser.getSelectedFile();
        Properties prop = ui2prop();
        PrettyPrint.storeProperties(file, prop);
        setStatus(file);
        message("SaveAs", "done");
      }
    } catch (Exception e) {
      message("SaveAs", e);
    }
  }//GEN-LAST:event_saveAsButtonActionPerformed
  
  private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
    try {
      File file = new File(filenameTextField.getText());
      Properties prop = ui2prop();
      PrettyPrint.storeProperties(file, prop);
      unmarkDirty();
      message("Save", "done");
    } catch (Exception e) {
      message("Save", e);
    }
  }//GEN-LAST:event_saveButtonActionPerformed
    
  private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
    try {
      int returnVal = propertyFileChooser.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = propertyFileChooser.getSelectedFile();
        Properties prop = PrettyPrint.loadProperties(file);
        PrettyPrint.checkProperties(prop, true);
        prop2ui(prop);
        setStatus(file);
      }
    } catch (Exception e) {
      message("Open", e);
    }
  }//GEN-LAST:event_openButtonActionPerformed
  
  /**
   * @param version of prettyxml
   */
  public static void run(final String version) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new MainJFrame(version).setVisible(true);
      }
    });
  }
  
  /**
   * @param args are ignored
   */
  public static void main(String[] args) {
    try {
      PrettyPrint prettyPrint = new PrettyPrint();
      run(prettyPrint.getVersion());
    } catch (Exception e) {
      logger.log(Level.FINE, "main", e);
      System.err.println(e.getMessage());
    }
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel buttonPanel;
  private javax.swing.JButton executeButton;
  private javax.swing.JTextField filenameTextField;
  private javax.swing.JButton helpButton;
  private javax.swing.JButton inputButton;
  private javax.swing.JTextField inputTextField;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel14;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JScrollBar jScrollBar1;
  private javax.swing.JButton openButton;
  private javax.swing.JButton outputButton;
  private javax.swing.JTextField outputTextField;
  private dk.hippogrif.prettyxml.PropertiesPane propertiesPane;
  private javax.swing.JPanel propertiesPanel;
  private javax.swing.JButton saveAsButton;
  private javax.swing.JButton saveButton;
  private javax.swing.JButton selectButton;
  private javax.swing.JTextField urlTextField;
  // End of variables declaration//GEN-END:variables
  
}
