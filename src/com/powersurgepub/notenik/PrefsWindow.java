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

  import com.powersurgepub.psdatalib.ui.*;
  import com.powersurgepub.psfiles.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.xos2.*;
  import javax.swing.*;

/**
 *
 * @author  hbowie
 */
public class PrefsWindow 
  extends javax.swing.JFrame
    implements WindowToManage {
  
  private NotenikMainFrame      mainFrame;

  private XOS               xos = XOS.getShared();
  
  private ProgramVersion    programVersion = ProgramVersion.getShared();
  
  private boolean           setupComplete = false;
  
  private CommonPrefs       commonPrefs;
  private WebPrefs          webPrefs;
  private FavoritesPrefs    favoritesPrefs;
  private FilePrefs         filePrefs = null;
  private FolderSyncPrefs   folderSyncPrefs;
  
  /** Creates new form PrefsWindow */
  public PrefsWindow(NotenikMainFrame mainFrame) {

    this.mainFrame = mainFrame;
    initComponents();
    
    this.setTitle (Home.getShared().getProgramName() + " Preferences");
    this.setBounds (100, 100, 600, 400);

    commonPrefs = CommonPrefs.getShared();
    prefsTabs.addTab("General", commonPrefs);
    
    webPrefs = new WebPrefs (mainFrame);
    prefsTabs.addTab("Web", webPrefs);
    
    favoritesPrefs = new FavoritesPrefs (mainFrame);
    prefsTabs.addTab("Favorites", favoritesPrefs);
    
    folderSyncPrefs = new FolderSyncPrefs (mainFrame);
    prefsTabs.addTab("Folder Sync", folderSyncPrefs);
    
    setupComplete = true;
  }
  
  public void setFilePrefs(FilePrefs filePrefs) {
    this.filePrefs = filePrefs;
    prefsTabs.addTab("Files", filePrefs);
  }
  
  /**
   Keep track of the current file spec. 
  
   @param currentSpec 
  */
  public void setCollection (FileSpec currentSpec) {
    if (folderSyncPrefs != null) {
      folderSyncPrefs.setCollection(currentSpec);
    }
  }

  public void savePrefs() {
    commonPrefs.savePrefs();
    webPrefs.savePrefs();
    favoritesPrefs.savePrefs();
    if (filePrefs != null) {
      filePrefs.savePrefs();
    }
    folderSyncPrefs.savePrefs();
  }
  
  public WebPrefs getWebPrefs() {
    return webPrefs;
  }

  public FavoritesPrefs getFavoritesPrefs() {
    return favoritesPrefs;
  }
  
  public FolderSyncPrefs getFolderSyncPrefs() {
    return folderSyncPrefs;
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

    prefsTabs = new javax.swing.JTabbedPane();

    setMinimumSize(new java.awt.Dimension(400, 300));
    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentHidden(java.awt.event.ComponentEvent evt) {
        formComponentHidden(evt);
      }
    });
    getContentPane().add(prefsTabs, java.awt.BorderLayout.CENTER);

    pack();
  }// </editor-fold>//GEN-END:initComponents

private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
  WindowMenuManager.getShared().hide(this);
}//GEN-LAST:event_formComponentHidden
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTabbedPane prefsTabs;
  // End of variables declaration//GEN-END:variables
  
}
