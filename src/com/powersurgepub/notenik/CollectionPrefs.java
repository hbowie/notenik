/*
 * Copyright 2009 - 2016 Herb Bowie
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

  import com.powersurgepub.psdatalib.pstags.*;
  import com.powersurgepub.psfiles.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.xos2.*;
  import javax.swing.*;

/**
 * Preferences for a particular collection. 
 *
 * @author  Herb Bowie
 */
public class CollectionPrefs 
  extends javax.swing.JFrame
    implements WindowToManage {
  
  private NotenikMainFrame  mainFrame;

  private XOS               xos = XOS.getShared();
  
  private ProgramVersion    programVersion = ProgramVersion.getShared();
  
  private boolean           setupComplete = false;
  
  private WebPrefs          webPrefs;
  private FolderSyncPrefs   folderSyncPrefs;
  private HTMLPrefs         htmlPrefs;
  
  /** Creates new form PrefsWindow */
  public CollectionPrefs(NotenikMainFrame mainFrame) {

    this.mainFrame = mainFrame;
    initComponents();
    
    this.setTitle (Home.getShared().getProgramName() + " Collection Preferences");
    this.setBounds (100, 100, 600, 400);
    
    webPrefs = new WebPrefs (mainFrame);
    prefsTabs.addTab("Web", webPrefs);
    
    folderSyncPrefs = new FolderSyncPrefs (mainFrame);
    prefsTabs.addTab("Folder Sync", folderSyncPrefs);
    
    htmlPrefs = new HTMLPrefs(mainFrame);
    prefsTabs.addTab ("HTML Gen", htmlPrefs);
    
    setupComplete = true;
  }
  
  /**
   Keep track of the current file spec. 
  
   @param currentSpec 
  */
  public void setCollection (FileSpec currentSpec) {
    collectionNameLabel.setText(("Collection: " + currentSpec.getPath()));
    if (folderSyncPrefs != null) {
      folderSyncPrefs.setCollection(currentSpec);
    }
    if (htmlPrefs != null) {
      htmlPrefs.setCollection(currentSpec);
    }
  }

  public void savePrefs() {
    webPrefs.savePrefs();
    folderSyncPrefs.savePrefs();
    htmlPrefs.savePrefs();
  }
  
  public WebPrefs getWebPrefs() {
    return webPrefs;
  }
  
  public FolderSyncPrefs getFolderSyncPrefs() {
    return folderSyncPrefs;
  }
  
  public HTMLPrefs getHTMLPrefs() {
    return htmlPrefs;
  }
  
  public JTabbedPane getPrefsTabs() {
    return prefsTabs;
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    collectionNameLabel = new javax.swing.JLabel();
    prefsTabs = new javax.swing.JTabbedPane();

    setBounds(new java.awt.Rectangle(66, 23, 600, 400));
    setMaximumSize(new java.awt.Dimension(1200, 900));
    setMinimumSize(new java.awt.Dimension(600, 400));
    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentHidden(java.awt.event.ComponentEvent evt) {
        formComponentHidden(evt);
      }
    });
    getContentPane().setLayout(new java.awt.GridBagLayout());

    collectionNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    collectionNameLabel.setText("Collection: ");
    collectionNameLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    collectionNameLabel.setMaximumSize(new java.awt.Dimension(800, 16));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
    getContentPane().add(collectionNameLabel, gridBagConstraints);

    prefsTabs.setMaximumSize(new java.awt.Dimension(1200, 800));
    prefsTabs.setMinimumSize(new java.awt.Dimension(800, 600));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    getContentPane().add(prefsTabs, gridBagConstraints);

    pack();
  }// </editor-fold>//GEN-END:initComponents

private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
  WindowMenuManager.getShared().hide(this);
}//GEN-LAST:event_formComponentHidden
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel collectionNameLabel;
  private javax.swing.JTabbedPane prefsTabs;
  // End of variables declaration//GEN-END:variables
  
}
