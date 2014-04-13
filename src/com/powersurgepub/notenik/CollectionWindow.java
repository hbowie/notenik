/*
 * Copyright 2009 - 2014 Herb Bowie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.powersurgepub.notenik;

  import com.powersurgepub.psdatalib.notenik.*;
  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.xos2.*;
  import java.awt.event.*;
  import java.io.*;
  import javax.swing.*;

/**
   Collect metadata about the Notes Collection. 

 */
public class CollectionWindow
    extends javax.swing.JFrame
      implements 
        WindowListener,
        WindowToManage {
  
  public static final String PARMS_TITLE        = "Collection Parms";
  public static final String SECONDARY_LOCATION = "Secondary Location";
  public static final String SECONDARY_PREFIX   = "Secondary Prefix";
  public static final String IO_STYLE           = "IO Style";
  public static final String IO_EXPLICIT        = "Explicit";
  public static final String IO_IMPLICIT        = "Implicit";
  public static final String IO_IMPLICIT_UNDERLINES = "Underlines";
  public static final String IO_IMPLICIT_FILENAME = "Filename";
  
  public static final DataFieldDefinition SECONDARY_LOCATION_DEF 
      = new DataFieldDefinition(SECONDARY_LOCATION);
  public static final DataFieldDefinition SECONDARY_PREFIX_DEF 
      = new DataFieldDefinition(SECONDARY_PREFIX);
  
  private NoteList noteList;
  
  private NoteIO io = null;
  
  private File source = null;
  
  private Note parmsNote = new Note(PARMS_TITLE);
  
  private boolean ioStyleSet = false;
  
  /** Creates new form CollectionWindow */
  public CollectionWindow() {
    initComponents();
    titleText.setText ("Notes");
    ioStyleComboBox.removeAll();
    ioStyleComboBox.addItem(IO_EXPLICIT);
    ioStyleComboBox.addItem(IO_IMPLICIT);
    ioStyleComboBox.addItem(IO_IMPLICIT_UNDERLINES);
    ioStyleComboBox.addItem(IO_IMPLICIT_FILENAME);
    
    // this.setBounds (100, 100, 600, 540);
  }
  
  public void newNoteFolder (NoteList noteList, NoteIO io) {
    this.noteList = noteList;
    this.io = io;
    source = noteList.getSource();
    parmsNote = new Note(PARMS_TITLE);
    if (io == null) {
      source = null;
    }
    if (source == null) {
      secondaryLocationPrefixText.setText("");
      fileNameText.setText("");
    } else {
      fileNameText.setText(source.getAbsolutePath());
      fileNameText.setText(noteList.getSource().getAbsolutePath());
      titleText.setText (noteList.getSource().getName());
      if (io.exists(parmsNote)) {
        try {
          parmsNote = io.getNote(parmsNote.getFileName());
          // DataField secondaryLocationField = parmsNote.getField(SECONDARY_LOCATION);
          // String secondaryLocationData = secondaryLocationField.getData();
          secondaryLocationText.setText
              (parmsNote.getField(SECONDARY_LOCATION).getData());
          secondaryLocationPrefixText.setText
              (parmsNote.getField(SECONDARY_PREFIX).getData());
          String ioStyle = parmsNote.getField(IO_STYLE).getData();
          if (ioStyle != null
              && ioStyle.length() > 0) {
            int i = 0;
            boolean found = false;
            while (i < ioStyleComboBox.getItemCount()
                && (! found)) {
              String comboBoxItem = (String)ioStyleComboBox.getItemAt(i);
              if (comboBoxItem.equalsIgnoreCase(ioStyle)) {
                ioStyleComboBox.setSelectedIndex(i);
                found = true;
              } // end if match
            } // end while looking for match
          } // end if we have an I/O Style value
        } catch (IOException e) {
          System.out.println("I/O Exception attempting to retrieve " + PARMS_TITLE);
        }
      }
      if (secondaryLocationPrefixText.getText().length() == 0) {
        StringBuilder prefix = new StringBuilder(source.getName());
        if (prefix.charAt(prefix.length() - 1) == 's') {
          prefix.deleteCharAt(prefix.length() - 1);
        }
        prefix.append(" - ");
        secondaryLocationPrefixText.setText(prefix.toString());
      }
    }
  }
  
  private void setSecondaryLocation(String secondaryLocationText) {
    this.secondaryLocationText.setText(secondaryLocationText);
  }

  private void setSecondaryLocation(File secondaryLocation) {
    try {
      secondaryLocationText.setText(secondaryLocation.getCanonicalPath());
    } catch (java.io.IOException e) {
      secondaryLocationText.setText(secondaryLocation.getAbsolutePath());
    }
  }

  private String getSecondaryLocationText() {
    return secondaryLocationText.getText();
  }

  private File getSecondaryLocation() {
    return new File(secondaryLocationText.getText());
  }
  
  public void close(NoteIO noteIO) {
    saveParms(noteIO);
  }
  
  /**
   Save all the parameters modified by this program. 
  
   @param noteIO The I/O module to use. 
  */
  public void saveParms (NoteIO noteIO) {
    parmsNote = new Note(PARMS_TITLE);
    parmsNote.addField
        (SECONDARY_LOCATION_DEF, secondaryLocationText.getText());
    parmsNote.addField
        (SECONDARY_PREFIX_DEF, secondaryLocationPrefixText.getText());
    try {
      noteIO.save(parmsNote, true);
    } catch (IOException e) {
      Trouble.getShared().report
          ("I/O Error Saving Collection Parms", "I/O Error");
    }
  }

  public void windowOpened      (WindowEvent e) {}
  public void windowClosing     (WindowEvent e) {}
  public void windowClosed      (WindowEvent e) {
    if (titleText.getText().length() > 0) {
      noteList.setTitle (titleText.getText());
    }
  }
  public void windowIconified   (WindowEvent e) {}
  public void windowDeiconified (WindowEvent e) {}
  public void windowActivated   (WindowEvent e) {}
  public void windowDeactivated (WindowEvent e) {}
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    fileNameLabel = new javax.swing.JLabel();
    fileNameText = new javax.swing.JLabel();
    titleLabel = new javax.swing.JLabel();
    titleText = new javax.swing.JTextField();
    filler = new javax.swing.JLabel();
    ioStyleLabel = new javax.swing.JLabel();
    ioStyleComboBox = new javax.swing.JComboBox();
    secondaryLocationPanel = new javax.swing.JPanel();
    secondaryLocationLabel = new javax.swing.JLabel();
    secondaryLocationText = new javax.swing.JTextField();
    secondaryLocationBrowseButton = new javax.swing.JButton();
    filler3 = new javax.swing.JLabel();
    secondaryLocationPrefixLabel = new javax.swing.JLabel();
    secondaryLocationPrefixText = new javax.swing.JTextField();
    cancelButton = new javax.swing.JButton();
    filler1 = new javax.swing.JLabel();
    okButton = new javax.swing.JButton();

    setMinimumSize(new java.awt.Dimension(400, 200));
    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentHidden(java.awt.event.ComponentEvent evt) {
        formComponentHidden(evt);
      }
    });
    getContentPane().setLayout(new java.awt.GridBagLayout());

    fileNameLabel.setText("File Name:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(8, 10, 8, 4);
    getContentPane().add(fileNameLabel, gridBagConstraints);

    fileNameText.setText(" ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(8, 10, 8, 10);
    getContentPane().add(fileNameText, gridBagConstraints);

    titleLabel.setText("Title:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
    getContentPane().add(titleLabel, gridBagConstraints);

    titleText.setToolTipText("Title of this Wisdom collection.");
    titleText.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        titleTextActionPerformed(evt);
      }
    });
    titleText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        titleTextFocusLost(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
    getContentPane().add(titleText, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(filler, gridBagConstraints);

    ioStyleLabel.setText("I/O Style:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
    getContentPane().add(ioStyleLabel, gridBagConstraints);

    ioStyleComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(ioStyleComboBox, gridBagConstraints);

    secondaryLocationPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    secondaryLocationPanel.setLayout(new java.awt.GridBagLayout());

    secondaryLocationLabel.setText("Secondary Location:");
    secondaryLocationLabel.setMaximumSize(new java.awt.Dimension(150, 25));
    secondaryLocationLabel.setMinimumSize(new java.awt.Dimension(100, 19));
    secondaryLocationLabel.setPreferredSize(new java.awt.Dimension(120, 19));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.4;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    secondaryLocationPanel.add(secondaryLocationLabel, gridBagConstraints);

    secondaryLocationText.setColumns(50);
    secondaryLocationText.setToolTipText("Folder to which data will be published");
    secondaryLocationText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        secondaryLocationTextFocusLost(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    secondaryLocationPanel.add(secondaryLocationText, gridBagConstraints);

    secondaryLocationBrowseButton.setText("Browse...");
    secondaryLocationBrowseButton.setMaximumSize(new java.awt.Dimension(150, 35));
    secondaryLocationBrowseButton.setMinimumSize(new java.awt.Dimension(130, 29));
    secondaryLocationBrowseButton.setPreferredSize(new java.awt.Dimension(130, 29));
    secondaryLocationBrowseButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        secondaryLocationBrowseButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    secondaryLocationPanel.add(secondaryLocationBrowseButton, gridBagConstraints);

    filler3.setText(" ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.6;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    secondaryLocationPanel.add(filler3, gridBagConstraints);

    secondaryLocationPrefixLabel.setText("File Name Prefix:");
    secondaryLocationPrefixLabel.setMaximumSize(new java.awt.Dimension(150, 25));
    secondaryLocationPrefixLabel.setMinimumSize(new java.awt.Dimension(100, 19));
    secondaryLocationPrefixLabel.setPreferredSize(new java.awt.Dimension(120, 19));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.4;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    secondaryLocationPanel.add(secondaryLocationPrefixLabel, gridBagConstraints);

    secondaryLocationPrefixText.setColumns(50);
    secondaryLocationPrefixText.setToolTipText("Folder to which data will be published");
    secondaryLocationPrefixText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        secondaryLocationPrefixTextFocusLost(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    secondaryLocationPanel.add(secondaryLocationPrefixText, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(secondaryLocationPanel, gridBagConstraints);

    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    getContentPane().add(cancelButton, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(filler1, gridBagConstraints);

    okButton.setText("OK");
    okButton.setMaximumSize(new java.awt.Dimension(86, 29));
    okButton.setMinimumSize(new java.awt.Dimension(86, 29));
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 7;
    getContentPane().add(okButton, gridBagConstraints);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void titleTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_titleTextFocusLost
    if (titleText.getText().length() > 0) {
      noteList.setTitle (titleText.getText());
    }
  }//GEN-LAST:event_titleTextFocusLost

  private void titleTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titleTextActionPerformed
    if (titleText.getText().length() > 0) {
      noteList.setTitle (titleText.getText());
    }
  }//GEN-LAST:event_titleTextActionPerformed

private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
  if (titleText.getText().length() > 0) {
    noteList.setTitle (titleText.getText());
  }
  this.setVisible(false);
}//GEN-LAST:event_okButtonActionPerformed

private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
  WindowMenuManager.getShared().hide(this);
}//GEN-LAST:event_formComponentHidden

  private void secondaryLocationTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_secondaryLocationTextFocusLost

  }//GEN-LAST:event_secondaryLocationTextFocusLost

  private void secondaryLocationBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_secondaryLocationBrowseButtonActionPerformed
    XFileChooser chooser = new XFileChooser();
    chooser.setDialogTitle("Locate Secondary Folder");
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    File chosen = chooser.showOpenDialog(this);
    if (chosen != null) {
      setSecondaryLocation(chosen);
    }
  }//GEN-LAST:event_secondaryLocationBrowseButtonActionPerformed

  private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cancelButtonActionPerformed

  private void secondaryLocationPrefixTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_secondaryLocationPrefixTextFocusLost
    // TODO add your handling code here:
  }//GEN-LAST:event_secondaryLocationPrefixTextFocusLost
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton cancelButton;
  private javax.swing.JLabel fileNameLabel;
  private javax.swing.JLabel fileNameText;
  private javax.swing.JLabel filler;
  private javax.swing.JLabel filler1;
  private javax.swing.JLabel filler3;
  private javax.swing.JComboBox ioStyleComboBox;
  private javax.swing.JLabel ioStyleLabel;
  private javax.swing.JButton okButton;
  private javax.swing.JButton secondaryLocationBrowseButton;
  private javax.swing.JLabel secondaryLocationLabel;
  private javax.swing.JPanel secondaryLocationPanel;
  private javax.swing.JLabel secondaryLocationPrefixLabel;
  private javax.swing.JTextField secondaryLocationPrefixText;
  private javax.swing.JTextField secondaryLocationText;
  private javax.swing.JLabel titleLabel;
  private javax.swing.JTextField titleText;
  // End of variables declaration//GEN-END:variables
  
}
