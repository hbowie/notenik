/*
 * Copyright 2014 - 2017 Herb Bowie
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
  import com.powersurgepub.psfiles.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.xos2.*;
  import java.io.*;
  import javax.swing.*;

/**
 Display and save user prefs for syncing with a Notational Velocity style folder. 

 @author Herb Bowie
 */
public class FolderSyncPrefs 
    extends javax.swing.JPanel {
  
  public static final String SYNC_FOLDER = "sync-folder";
  
  private NotenikMainFrame      main;
  
  private FolderSyncPrefsData folderSyncPrefsData = new FolderSyncPrefsData();
  
  private FileSpec              collection = null;

  /**
   Creates new form FolderSyncPrefs
   */
  public FolderSyncPrefs(NotenikMainFrame main) {
    this.main = main;
    initComponents();
    syncFolderTextField.setText(folderSyncPrefsData.getSyncFolder());
  }
  
  public void setFolderSyncPrefsData (FolderSyncPrefsData folderSyncPrefsData) {
    this.folderSyncPrefsData = folderSyncPrefsData;
    syncFolderTextField.setText(folderSyncPrefsData.getSyncFolder());
  }
  
  public FolderSyncPrefsData getFolderSyncPrefsData() {
    return folderSyncPrefsData;
  }
  
  public boolean getSync() {
    return folderSyncPrefsData.getSync();
  }
  
  public String getSyncFolder() {
    return folderSyncPrefsData.getSyncFolder();
  }
  
  public String getSyncPrefix() {
    return folderSyncPrefsData.getSyncPrefix();
  }
  
  public void savePrefs() {
    folderSyncPrefsData.savePrefs();
  }
  
  public void setCollection(FileSpec collection) {
    this.collection = collection;
    folderSyncPrefsData.setCollection(collection);
    if (collection != null) {
      collectionTextField.setText(collection.getPath());
      prefixTextField.setText(folderSyncPrefsData.getSyncPrefix());
      String collectionPrefix = folderSyncPrefsData.getSyncPrefix();
      if (collectionPrefix.length() > 0) {
        prefixTextField.setText(collection.getSyncPrefix());
      }
    } 
    syncCheckBox.setSelected(folderSyncPrefsData.getSync());
  }
  
  private void setSyncFolder (File folder) {
    try {
      String folderString = folder.getCanonicalPath();
      setSyncFolder(folderString);
    } catch (java.io.IOException e) {
      setSyncFolder(folder.toString());
    }
    
  }
  
  private void setSyncFolder (String folderString) {
    syncFolderTextField.setText(folderString);
    folderSyncPrefsData.setSyncFolder(folderString);
  }

  /**
   This method is called from within the constructor to initialize the form.
   WARNING: Do NOT modify this code. The content of this method is always
   regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    folderLabel = new javax.swing.JLabel();
    folderBrowseButton = new javax.swing.JButton();
    syncFolderTextField = new javax.swing.JTextField();
    collectionLabel = new javax.swing.JLabel();
    collectionTextField = new javax.swing.JLabel();
    prefixLabel = new javax.swing.JLabel();
    prefixTextField = new javax.swing.JTextField();
    syncLabel = new javax.swing.JLabel();
    syncCheckBox = new javax.swing.JCheckBox();
    saveButton = new javax.swing.JButton();

    setLayout(new java.awt.GridBagLayout());

    folderLabel.setText("Folder:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
    add(folderLabel, gridBagConstraints);

    folderBrowseButton.setText("Browse...");
    folderBrowseButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        folderBrowseButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(folderBrowseButton, gridBagConstraints);

    syncFolderTextField.setColumns(60);
    syncFolderTextField.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        syncFolderTextFieldActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(syncFolderTextField, gridBagConstraints);

    collectionLabel.setText("Collection:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(12, 8, 4, 4);
    add(collectionLabel, gridBagConstraints);

    collectionTextField.setText(" ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
    add(collectionTextField, gridBagConstraints);

    prefixLabel.setText("File Name Prefix for this Collection:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(12, 8, 4, 4);
    add(prefixLabel, gridBagConstraints);

    prefixTextField.setColumns(60);
    prefixTextField.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        prefixTextFieldActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(prefixTextField, gridBagConstraints);

    syncLabel.setText("Sync this Collection?");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 9;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(12, 8, 4, 4);
    add(syncLabel, gridBagConstraints);

    syncCheckBox.setText("Yes");
    syncCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        syncCheckBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    add(syncCheckBox, gridBagConstraints);

    saveButton.setText("Save");
    saveButton.setMinimumSize(new java.awt.Dimension(100, 29));
    saveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 12;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(12, 4, 4, 4);
    add(saveButton, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void folderBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_folderBrowseButtonActionPerformed
    XFileChooser chooser = new XFileChooser();
    chooser.setDialogTitle("Specify Common Folder for Synchronization");
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    File chosen = chooser.showOpenDialog(main);
    if (chosen != null) {
      setSyncFolder(chosen);
    } // end if user chose a folder
  }//GEN-LAST:event_folderBrowseButtonActionPerformed

  private void syncFolderTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syncFolderTextFieldActionPerformed
    folderSyncPrefsData.setSyncFolder(syncFolderTextField.getText());
  }//GEN-LAST:event_syncFolderTextFieldActionPerformed

  private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
    folderSyncPrefsData.setSyncFolder(syncFolderTextField.getText());
    folderSyncPrefsData.setSync(syncCheckBox.isSelected());
    collection.setSync(syncCheckBox.isSelected());
    folderSyncPrefsData.setSyncPrefix(prefixTextField.getText());
    collection.setSyncPrefix(prefixTextField.getText());
    if ((! folderSyncPrefsData.getSync())
        && syncCheckBox.isSelected()) {
      main.syncWithFolder();
      folderSyncPrefsData.setSync(true);
    }
  }//GEN-LAST:event_saveButtonActionPerformed

  private void prefixTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prefixTextFieldActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_prefixTextFieldActionPerformed

  private void syncCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syncCheckBoxActionPerformed
    collection.setSync (syncCheckBox.isSelected());
  }//GEN-LAST:event_syncCheckBoxActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel collectionLabel;
  private javax.swing.JLabel collectionTextField;
  private javax.swing.JButton folderBrowseButton;
  private javax.swing.JLabel folderLabel;
  private javax.swing.JLabel prefixLabel;
  private javax.swing.JTextField prefixTextField;
  private javax.swing.JButton saveButton;
  private javax.swing.JCheckBox syncCheckBox;
  private javax.swing.JTextField syncFolderTextField;
  private javax.swing.JLabel syncLabel;
  // End of variables declaration//GEN-END:variables
}
