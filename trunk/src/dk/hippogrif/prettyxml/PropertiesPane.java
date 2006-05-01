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
import javax.swing.JPanel;
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

/**
 * A GUI pane for setting prettyxml properties. 
 * We get known encodings and transformations from configuration.
 * Parent may want call on property modification with GParentInterface.markDirty().
 * 
 * See {@link dk.hippogrif.prettyxml}.
 */
public class PropertiesPane extends JPanel implements PropertyNames {
  
  private static Logger logger = Logger.getLogger(PropertiesPane.class.getName());
  private String[] encodings = PrettyPrint.getEncodings();
  private GParentInterface parent;
  
  /** Construct pane */
  public PropertiesPane() {
    initComponents();
  }
  
  /** set parent (optional) for dirty notification */
  public void setParent(GParentInterface p) {
    parent = p;
  }
  
  String linesep2display(String s) {
    if ("\r".equals(s)) s = "\\r"; else if ("\n".equals(s)) s = "\\n"; else s = "\\r\\n";
    return s;
  }
  
  String display2linesep(String s) {
    if ("\\r".equals(s)) s = "\r"; else if ("\\n".equals(s)) s = "\n"; else s = "\r\n";
    return s;
  }
  
  /** Get properties from GUI */
  public Properties getProperties() {
    Properties prop = new Properties();
    prop.setProperty(ENCODING, (String)encodingComboBox.getSelectedItem());
    prop.setProperty(EXPAND_EMPTY_ELEMENTS, Boolean.toString(expandEmptyElementsCheckBox.isSelected()));
    prop.setProperty(INDENT, ((Integer)indentSpinner.getValue()).toString());
    prop.setProperty(LINE_SEPARATOR, display2linesep((String)lineSeparatorComboBox.getSelectedItem()));
    prop.setProperty(OMIT_DECLARATION, Boolean.toString(omitDeclarationCheckBox.isSelected()));
    prop.setProperty(OMIT_ENCODING, Boolean.toString(omitEncodingCheckBox.isSelected()));
    prop.setProperty(TEXT_MODE, (String)textModeComboBox.getSelectedItem());
    prop.setProperty(INDENT_ATTRIBUTES, Boolean.toString(indentAttributesCheckBox.isSelected()));
    prop.setProperty(SORT_ATTRIBUTES, Boolean.toString(sortAttributesCheckBox.isSelected()));
    prop.setProperty(TRANSFORM, transformsPane.getTransformations());
    return prop;
  }
  
  /** Set properties in GUI */
  public void setProperties(Properties prop) {
    encodingComboBox.setSelectedItem(prop.getProperty(ENCODING, "UTF-8"));
    expandEmptyElementsCheckBox.setSelected(Boolean.valueOf(prop.getProperty(EXPAND_EMPTY_ELEMENTS, "false")).booleanValue());
    indentSpinner.setValue(Integer.valueOf(prop.getProperty(INDENT, "0")));
    lineSeparatorComboBox.setSelectedItem(linesep2display(prop.getProperty(LINE_SEPARATOR, LINE_SEPARATORS[0])));
    omitDeclarationCheckBox.setSelected(Boolean.valueOf(prop.getProperty(OMIT_DECLARATION, "false")).booleanValue());
    omitEncodingCheckBox.setSelected(Boolean.valueOf(prop.getProperty(OMIT_ENCODING, "false")).booleanValue());
    textModeComboBox.setSelectedItem(prop.getProperty(TEXT_MODE, TEXT_MODES[0]));
    indentAttributesCheckBox.setSelected(Boolean.valueOf(prop.getProperty(INDENT_ATTRIBUTES, "false")).booleanValue());
    sortAttributesCheckBox.setSelected(Boolean.valueOf(prop.getProperty(SORT_ATTRIBUTES, "false")).booleanValue());
    transformsPane.setTransformations(prop.getProperty(TRANSFORM));
  }
  
  private void markDirty() {
    if (parent != null) {
      parent.markDirty();
    }
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

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
    encodingComboBox = new javax.swing.JComboBox();
    expandEmptyElementsCheckBox = new javax.swing.JCheckBox();
    indentSpinner = new javax.swing.JSpinner();
    lineSeparatorComboBox = new javax.swing.JComboBox();
    omitDeclarationCheckBox = new javax.swing.JCheckBox();
    omitEncodingCheckBox = new javax.swing.JCheckBox();
    textModeComboBox = new javax.swing.JComboBox();
    indentAttributesCheckBox = new javax.swing.JCheckBox();
    sortAttributesCheckBox = new javax.swing.JCheckBox();
    transformsPane = new dk.hippogrif.prettyxml.TransformsPane();

    setLayout(new java.awt.GridBagLayout());

    setMinimumSize(new java.awt.Dimension(270, 200));
    setPreferredSize(new java.awt.Dimension(270, 200));
    jLabel2.setText("encoding:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    add(jLabel2, gridBagConstraints);

    jLabel3.setText("expandEmptyElements:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    add(jLabel3, gridBagConstraints);

    jLabel4.setText("indent:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    add(jLabel4, gridBagConstraints);

    jLabel5.setText("lineSeparator:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    add(jLabel5, gridBagConstraints);

    jLabel6.setText("omitDeclaration:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    add(jLabel6, gridBagConstraints);

    jLabel7.setText("omitEncoding:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    add(jLabel7, gridBagConstraints);

    jLabel8.setText("textMode:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    add(jLabel8, gridBagConstraints);

    jLabel9.setText("indentAttributes:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    add(jLabel9, gridBagConstraints);

    jLabel10.setText("sortAttributes:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 9;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    add(jLabel10, gridBagConstraints);

    jLabel11.setText("transform:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
    add(jLabel11, gridBagConstraints);

    encodingComboBox.setEditable(true);
    encodingComboBox.setMaximumRowCount(6);
    encodingComboBox.setModel(new javax.swing.DefaultComboBoxModel(encodings));
    encodingComboBox.setToolTipText("encoding of the xml output");
    encodingComboBox.setMaximumSize(new java.awt.Dimension(32767, 22));
    encodingComboBox.setPreferredSize(new java.awt.Dimension(150, 20));
    encodingComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        encodingComboBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    add(encodingComboBox, gridBagConstraints);

    expandEmptyElementsCheckBox.setToolTipText("expansion of empty elements");
    expandEmptyElementsCheckBox.setPreferredSize(new java.awt.Dimension(20, 20));
    expandEmptyElementsCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        expandEmptyElementsCheckBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    add(expandEmptyElementsCheckBox, gridBagConstraints);

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
    add(indentSpinner, gridBagConstraints);

    lineSeparatorComboBox.setMaximumRowCount(3);
    lineSeparatorComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "\\r\\n", "\\r", "\\n" }));
    lineSeparatorComboBox.setToolTipText("the newline separator -  DOS, Mac or Unix style");
    lineSeparatorComboBox.setPreferredSize(new java.awt.Dimension(55, 20));
    lineSeparatorComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        lineSeparatorComboBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    add(lineSeparatorComboBox, gridBagConstraints);

    omitDeclarationCheckBox.setToolTipText("omit XML declaration");
    omitDeclarationCheckBox.setPreferredSize(new java.awt.Dimension(20, 20));
    omitDeclarationCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        omitDeclarationCheckBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    add(omitDeclarationCheckBox, gridBagConstraints);

    omitEncodingCheckBox.setToolTipText("omit encoding in XML declaration");
    omitEncodingCheckBox.setPreferredSize(new java.awt.Dimension(20, 20));
    omitEncodingCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        omitEncodingCheckBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    add(omitEncodingCheckBox, gridBagConstraints);

    textModeComboBox.setMaximumRowCount(4);
    textModeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NORMALIZE", "PRESERVE", "TRIM", "TRIM_FULL_WHITE" }));
    textModeComboBox.setSelectedIndex(2);
    textModeComboBox.setToolTipText("how to output text");
    textModeComboBox.setPreferredSize(new java.awt.Dimension(150, 20));
    textModeComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        textModeComboBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    add(textModeComboBox, gridBagConstraints);

    indentAttributesCheckBox.setToolTipText("indent attributes according to indent");
    indentAttributesCheckBox.setPreferredSize(new java.awt.Dimension(20, 20));
    indentAttributesCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        indentAttributesCheckBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    add(indentAttributesCheckBox, gridBagConstraints);

    sortAttributesCheckBox.setToolTipText("sort attributes on name");
    sortAttributesCheckBox.setPreferredSize(new java.awt.Dimension(20, 20));
    sortAttributesCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sortAttributesCheckBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    add(sortAttributesCheckBox, gridBagConstraints);

    transformsPane.setBorder(new javax.swing.border.EtchedBorder());
    transformsPane.setToolTipText("XSL Transformations");
    transformsPane.setPreferredSize(new java.awt.Dimension(0, 0));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.gridheight = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(transformsPane, gridBagConstraints);

  }
  // </editor-fold>//GEN-END:initComponents
  
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
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox encodingComboBox;
  private javax.swing.JCheckBox expandEmptyElementsCheckBox;
  private javax.swing.JCheckBox indentAttributesCheckBox;
  private javax.swing.JSpinner indentSpinner;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel11;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JComboBox lineSeparatorComboBox;
  private javax.swing.JCheckBox omitDeclarationCheckBox;
  private javax.swing.JCheckBox omitEncodingCheckBox;
  private javax.swing.JCheckBox sortAttributesCheckBox;
  private javax.swing.JComboBox textModeComboBox;
  private dk.hippogrif.prettyxml.TransformsPane transformsPane;
  // End of variables declaration//GEN-END:variables
  
}