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

/**
 * A GUI application for prettyprinting XML.
 * See {@link dk.hippogrif.prettyxml}.
 *
 * @author  Jesper Goertz
 */
public class MainJFrame extends javax.swing.JFrame {
  
  private static Logger logger = Logger.getLogger("dk.hippogrif.prettyxml.MainJFrame");
  private JFileChooser propertyFileChooser = new JFileChooser();
  private JFileChooser inputFileChooser = new JFileChooser();
  private JFileChooser outputFileChooser = new JFileChooser();
  private JFileChooser transformFileChooser = new JFileChooser();
  private HTMLDialog help;
  private String version;
  
  /** Creates new form MainJFrame */
  public MainJFrame(String version) {
    this.version = version;
    initComponents();
    initTextFields();
  }
  
  private void initTextFields() {
    DocumentListener listener = new MyDocListener();
    addDocumentListener(transformTextField, listener);
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
  
  private void reset() {
    encodingComboBox.setSelectedItem("UTF-8");
    expandEmptyElementsCheckBox.setSelected(false);
    indentSpinner.setValue(new Integer(2));
    lineSeparatorComboBox.setSelectedItem("\r\n");
    omitDeclarationCheckBox.setSelected(false);
    omitEncodingCheckBox.setSelected(false);
    textModeComboBox.setSelectedItem("TRIM");
    indentAttributesCheckBox.setSelected(false);
    sortAttributesCheckBox.setSelected(false);
    transformTextField.setText("");
    inputTextField.setText("");
    urlTextField.setText("");
    outputTextField.setText("");
  }
  
  private void prop2ui(Properties prop) {
    encodingComboBox.setSelectedItem(prop.getProperty("encoding", "UTF-8"));
    expandEmptyElementsCheckBox.setSelected("TRUE".equals(prop.getProperty("expandEmptyElements")));
    indentSpinner.setValue(new Integer(prop.getProperty("indent", "0")));
    String s = prop.getProperty("lineSeparator");
    if ("\r".equals(s)) s = "\\r"; else if ("\n".equals(s)) s = "\\n"; else s = "\\r\\n";
    lineSeparatorComboBox.setSelectedItem(s);
    omitDeclarationCheckBox.setSelected("TRUE".equals(prop.getProperty("omitDeclaration")));
    omitEncodingCheckBox.setSelected("TRUE".equals(prop.getProperty("omitEncoding")));
    textModeComboBox.setSelectedItem(prop.getProperty("textMode", "PRESERVE"));
    indentAttributesCheckBox.setSelected("TRUE".equals(prop.getProperty("indentAttributes")));
    sortAttributesCheckBox.setSelected("TRUE".equals(prop.getProperty("sortAttributes")));
    transformTextField.setText(prop.getProperty("transform", ""));
    inputTextField.setText(prop.getProperty("input", ""));
    urlTextField.setText(prop.getProperty("url", ""));
    outputTextField.setText(prop.getProperty("output", ""));
  }
  
  private Properties ui2prop() {
    Properties prop = new Properties();
    prop.setProperty("encoding",(String)encodingComboBox.getSelectedItem());
    if (expandEmptyElementsCheckBox.isSelected()) prop.setProperty("expandEmptyElements","TRUE");
    Integer integer = (Integer)indentSpinner.getValue();
    if (integer.intValue() != 0) prop.setProperty("indent",integer.toString());
    String s = (String)lineSeparatorComboBox.getSelectedItem();
    if ("\\r".equals(s)) s = "\r"; else if ("\\n".equals(s)) s = "\n"; else s = "\r\n";
    prop.setProperty("lineSeparator",s);
    if (omitDeclarationCheckBox.isSelected()) prop.setProperty("omitDeclaration","TRUE");
    if (omitEncodingCheckBox.isSelected()) prop.setProperty("omitEncoding","TRUE");
    prop.setProperty("textMode",(String)textModeComboBox.getSelectedItem());
    if (indentAttributesCheckBox.isSelected()) prop.setProperty("indentAttributes","TRUE");
    if (sortAttributesCheckBox.isSelected()) prop.setProperty("sortAttributes","TRUE");
    if (!(s = transformTextField.getText()).equals("")) prop.setProperty("transform",s);
    if (!(s = inputTextField.getText()).equals("")) prop.setProperty("input",s);
    if (!(s = urlTextField.getText()).equals("")) prop.setProperty("url",s);
    if (!(s = outputTextField.getText()).equals("")) prop.setProperty("output",s);
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
  
  private void markDirty() {
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
    resetButton = new javax.swing.JButton();
    openButton = new javax.swing.JButton();
    saveButton = new javax.swing.JButton();
    saveAsButton = new javax.swing.JButton();
    filenameTextField = new javax.swing.JTextField();
    executeButton = new javax.swing.JButton();
    helpButton = new javax.swing.JButton();
    propertiesPanel = new javax.swing.JPanel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    jLabel8 = new javax.swing.JLabel();
    jLabel9 = new javax.swing.JLabel();
    jLabel10 = new javax.swing.JLabel();
    jLabel11 = new javax.swing.JLabel();
    jLabel12 = new javax.swing.JLabel();
    jLabel13 = new javax.swing.JLabel();
    jLabel14 = new javax.swing.JLabel();
    encodingComboBox = new javax.swing.JComboBox();
    expandEmptyElementsCheckBox = new javax.swing.JCheckBox();
    indentSpinner = new javax.swing.JSpinner();
    lineSeparatorComboBox = new javax.swing.JComboBox();
    omitDeclarationCheckBox = new javax.swing.JCheckBox();
    omitEncodingCheckBox = new javax.swing.JCheckBox();
    textModeComboBox = new javax.swing.JComboBox();
    indentAttributesCheckBox = new javax.swing.JCheckBox();
    sortAttributesCheckBox = new javax.swing.JCheckBox();
    transformTextField = new javax.swing.JTextField();
    inputTextField = new javax.swing.JTextField();
    urlTextField = new javax.swing.JTextField();
    outputTextField = new javax.swing.JTextField();
    transformButton = new javax.swing.JButton();
    inputButton = new javax.swing.JButton();
    outputButton = new javax.swing.JButton();
    jPanel1 = new javax.swing.JPanel();
    jPanel2 = new javax.swing.JPanel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("prettyxml "+version);
    buttonPanel.setLayout(new java.awt.GridBagLayout());

    resetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dk/hippogrif/prettyxml/image/New16.gif")));
    resetButton.setToolTipText("new");
    resetButton.setBorder(new javax.swing.border.EtchedBorder());
    resetButton.setPreferredSize(new java.awt.Dimension(20, 20));
    resetButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        resetButtonActionPerformed(evt);
      }
    });

    buttonPanel.add(resetButton, new java.awt.GridBagConstraints());

    openButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dk/hippogrif/prettyxml/image/Open16.gif")));
    openButton.setToolTipText("open");
    openButton.setBorder(new javax.swing.border.EtchedBorder());
    openButton.setPreferredSize(new java.awt.Dimension(20, 20));
    openButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openButtonActionPerformed(evt);
      }
    });

    buttonPanel.add(openButton, new java.awt.GridBagConstraints());

    saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dk/hippogrif/prettyxml/image/Save16.gif")));
    saveButton.setToolTipText("save");
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
    saveAsButton.setToolTipText("save as");
    saveAsButton.setBorder(new javax.swing.border.EtchedBorder());
    saveAsButton.setPreferredSize(new java.awt.Dimension(20, 20));
    saveAsButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveAsButtonActionPerformed(evt);
      }
    });

    buttonPanel.add(saveAsButton, new java.awt.GridBagConstraints());

    filenameTextField.setBackground(new java.awt.Color(255, 255, 255));
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

    propertiesPanel.setMinimumSize(new java.awt.Dimension(400, 300));
    propertiesPanel.setPreferredSize(new java.awt.Dimension(400, 300));
    jLabel2.setText("encoding:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel2, gridBagConstraints);

    jLabel3.setText("expandEmptyElements:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel3, gridBagConstraints);

    jLabel4.setText("indent:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel4, gridBagConstraints);

    jLabel5.setText("lineSeparator:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel5, gridBagConstraints);

    jLabel6.setText("omitDeclaration:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel6, gridBagConstraints);

    jLabel7.setText("omitEncoding:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel7, gridBagConstraints);

    jLabel8.setText("textMode:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel8, gridBagConstraints);

    jLabel9.setText("indentAttributes:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel9, gridBagConstraints);

    jLabel10.setText("sortAttributes:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 9;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel10, gridBagConstraints);

    jLabel11.setText("transform:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel11, gridBagConstraints);

    jLabel12.setText("input:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 11;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel12, gridBagConstraints);

    jLabel13.setText("url:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 12;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel13, gridBagConstraints);

    jLabel14.setText("output:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 13;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    propertiesPanel.add(jLabel14, gridBagConstraints);

    encodingComboBox.setEditable(true);
    encodingComboBox.setMaximumRowCount(6);
    encodingComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "UTF-8", "ISO-8859-1" }));
    encodingComboBox.setToolTipText("encoding of the xml output");
    encodingComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        encodingComboBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    propertiesPanel.add(encodingComboBox, gridBagConstraints);

    expandEmptyElementsCheckBox.setToolTipText("expansion of empty elements");
    expandEmptyElementsCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        expandEmptyElementsCheckBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    propertiesPanel.add(expandEmptyElementsCheckBox, gridBagConstraints);

    indentSpinner.setModel(new SpinnerNumberModel(2, 0, 99, 1));
    indentSpinner.setToolTipText("no of spaces to indent (elements) - 0 means no indentation");
    indentSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        indentSpinnerStateChanged(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    propertiesPanel.add(indentSpinner, gridBagConstraints);

    lineSeparatorComboBox.setMaximumRowCount(3);
    lineSeparatorComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "\\r\\n", "\\r", "\\n" }));
    lineSeparatorComboBox.setToolTipText("the newline separator -  DOS, Mac or Unix style");
    lineSeparatorComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        lineSeparatorComboBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    propertiesPanel.add(lineSeparatorComboBox, gridBagConstraints);

    omitDeclarationCheckBox.setToolTipText("omit XML declaration");
    omitDeclarationCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        omitDeclarationCheckBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    propertiesPanel.add(omitDeclarationCheckBox, gridBagConstraints);

    omitEncodingCheckBox.setToolTipText("omit encoding in XML declaration");
    omitEncodingCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        omitEncodingCheckBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    propertiesPanel.add(omitEncodingCheckBox, gridBagConstraints);

    textModeComboBox.setMaximumRowCount(4);
    textModeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NORMALIZE", "PRESERVE", "TRIM", "TRIM_FULL_WHITE" }));
    textModeComboBox.setSelectedIndex(2);
    textModeComboBox.setToolTipText("how to output text");
    textModeComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        textModeComboBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    propertiesPanel.add(textModeComboBox, gridBagConstraints);

    indentAttributesCheckBox.setToolTipText("indent attributes according to indent");
    indentAttributesCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        indentAttributesCheckBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    propertiesPanel.add(indentAttributesCheckBox, gridBagConstraints);

    sortAttributesCheckBox.setToolTipText("sort attributes on name");
    sortAttributesCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sortAttributesCheckBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    propertiesPanel.add(sortAttributesCheckBox, gridBagConstraints);

    transformTextField.setToolTipText("pipeline of transformation stylesheets separated by ;");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    propertiesPanel.add(transformTextField, gridBagConstraints);

    inputTextField.setToolTipText("input xml file - stdin is used if neither input file nor url is specified");
    inputTextField.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        inputTextFieldFocusLost(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
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
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    propertiesPanel.add(urlTextField, gridBagConstraints);

    outputTextField.setToolTipText("output file - stdout is used if no file is specified");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    propertiesPanel.add(outputTextField, gridBagConstraints);

    transformButton.setText("...");
    transformButton.setToolTipText("choose file and append path");
    transformButton.setMaximumSize(new java.awt.Dimension(20, 20));
    transformButton.setMinimumSize(new java.awt.Dimension(20, 20));
    transformButton.setPreferredSize(new java.awt.Dimension(20, 20));
    transformButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        transformButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
    propertiesPanel.add(transformButton, gridBagConstraints);

    inputButton.setText("...");
    inputButton.setToolTipText("choose file");
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
    gridBagConstraints.gridy = 11;
    gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
    propertiesPanel.add(inputButton, gridBagConstraints);

    outputButton.setText("...");
    outputButton.setToolTipText("choose file");
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
    gridBagConstraints.gridy = 13;
    gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
    propertiesPanel.add(outputButton, gridBagConstraints);

    getContentPane().add(propertiesPanel, java.awt.BorderLayout.CENTER);

    jPanel1.setMinimumSize(new java.awt.Dimension(5, 10));
    getContentPane().add(jPanel1, java.awt.BorderLayout.EAST);

    jPanel2.setMinimumSize(new java.awt.Dimension(5, 10));
    getContentPane().add(jPanel2, java.awt.BorderLayout.WEST);

    pack();
  }
  // </editor-fold>//GEN-END:initComponents
  
  private void urlTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_urlTextFieldFocusLost
    if (urlTextField.getText().length() > 0) inputTextField.setText("");
  }//GEN-LAST:event_urlTextFieldFocusLost
  
  private void inputTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputTextFieldFocusLost
    if (inputTextField.getText().length() > 0) urlTextField.setText("");
  }//GEN-LAST:event_inputTextFieldFocusLost
  
  private void helpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpButtonActionPerformed
    if (help == null) {
      help = new HTMLDialog(this, this.getTitle()+" Help", this.getClass().getResource("package.html"), 30, new Dimension(400, 400));
    }
    help.show();
  }//GEN-LAST:event_helpButtonActionPerformed
  
  private void indentSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_indentSpinnerStateChanged
    markDirty();
  }//GEN-LAST:event_indentSpinnerStateChanged
  
  private void sortAttributesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortAttributesCheckBoxActionPerformed
    markDirty();
  }//GEN-LAST:event_sortAttributesCheckBoxActionPerformed
  
  private void indentAttributesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_indentAttributesCheckBoxActionPerformed
    markDirty();
  }//GEN-LAST:event_indentAttributesCheckBoxActionPerformed
  
  private void textModeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textModeComboBoxActionPerformed
    markDirty();
  }//GEN-LAST:event_textModeComboBoxActionPerformed
  
  private void omitEncodingCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_omitEncodingCheckBoxActionPerformed
    markDirty();
  }//GEN-LAST:event_omitEncodingCheckBoxActionPerformed
  
  private void omitDeclarationCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_omitDeclarationCheckBoxActionPerformed
    markDirty();
  }//GEN-LAST:event_omitDeclarationCheckBoxActionPerformed
  
  private void lineSeparatorComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lineSeparatorComboBoxActionPerformed
    markDirty();
  }//GEN-LAST:event_lineSeparatorComboBoxActionPerformed
  
  private void expandEmptyElementsCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expandEmptyElementsCheckBoxActionPerformed
    markDirty();
  }//GEN-LAST:event_expandEmptyElementsCheckBoxActionPerformed
  
  private void encodingComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encodingComboBoxActionPerformed
    markDirty();
  }//GEN-LAST:event_encodingComboBoxActionPerformed
  
  private void transformButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transformButtonActionPerformed
    int returnVal = transformFileChooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      String s = transformTextField.getText().trim();
      if (s.length()>0) s = s+";";
      transformTextField.setText(s+transformFileChooser.getSelectedFile().getPath());
      markDirty();
    }
  }//GEN-LAST:event_transformButtonActionPerformed
  
  private void outputButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputButtonActionPerformed
    int returnVal = outputFileChooser.showSaveDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      outputTextField.setText(outputFileChooser.getSelectedFile().getPath());
      markDirty();
    }
  }//GEN-LAST:event_outputButtonActionPerformed
  
  private void inputButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputButtonActionPerformed
    int returnVal = inputFileChooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      inputTextField.setText(inputFileChooser.getSelectedFile().getPath());
      markDirty();
      urlTextField.setText("");
    }
  }//GEN-LAST:event_inputButtonActionPerformed
  
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
      int returnVal = propertyFileChooser.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = propertyFileChooser.getSelectedFile();
        Properties prop = ui2prop();
        PrettyPrint.storeProperties(file, prop, version);
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
      PrettyPrint.storeProperties(file, prop, version);
      unmarkDirty();
      message("Save", "done");
    } catch (Exception e) {
      message("Save", e);
    }
  }//GEN-LAST:event_saveButtonActionPerformed
  
  private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
    reset();
    setStatus(null);
  }//GEN-LAST:event_resetButtonActionPerformed
  
  private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
    try {
      int returnVal = propertyFileChooser.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = propertyFileChooser.getSelectedFile();
        Properties prop = PrettyPrint.loadProperties(file);
        PrettyPrint.checkProperties(prop);
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
  private javax.swing.JComboBox encodingComboBox;
  private javax.swing.JButton executeButton;
  private javax.swing.JCheckBox expandEmptyElementsCheckBox;
  private javax.swing.JTextField filenameTextField;
  private javax.swing.JButton helpButton;
  private javax.swing.JCheckBox indentAttributesCheckBox;
  private javax.swing.JSpinner indentSpinner;
  private javax.swing.JButton inputButton;
  private javax.swing.JTextField inputTextField;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel11;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel14;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JScrollBar jScrollBar1;
  private javax.swing.JComboBox lineSeparatorComboBox;
  private javax.swing.JCheckBox omitDeclarationCheckBox;
  private javax.swing.JCheckBox omitEncodingCheckBox;
  private javax.swing.JButton openButton;
  private javax.swing.JButton outputButton;
  private javax.swing.JTextField outputTextField;
  private javax.swing.JPanel propertiesPanel;
  private javax.swing.JButton resetButton;
  private javax.swing.JButton saveAsButton;
  private javax.swing.JButton saveButton;
  private javax.swing.JCheckBox sortAttributesCheckBox;
  private javax.swing.JComboBox textModeComboBox;
  private javax.swing.JButton transformButton;
  private javax.swing.JTextField transformTextField;
  private javax.swing.JTextField urlTextField;
  // End of variables declaration//GEN-END:variables
  
}
