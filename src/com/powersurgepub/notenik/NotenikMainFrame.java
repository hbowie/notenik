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

  import com.powersurgepub.linktweaker.*;
  import com.powersurgepub.psfiles.*;
  import com.powersurgepub.psdatalib.ui.*;
  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psdatalib.pstags.*;
  import com.powersurgepub.psdatalib.notenik.*;
  import com.powersurgepub.psdatalib.pslist.*;
  import com.powersurgepub.psdatalib.tabdelim.*;
  import com.powersurgepub.pspub.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.urlvalidator.*;
  import com.powersurgepub.xos2.*;
  import java.awt.*;
  import java.awt.event.*;
  import java.io.*;
  import java.net.*;
  import java.text.*;
  import java.util.*;
  import javax.swing.*;
  import javax.swing.event.*;
  import javax.swing.tree.*;

/**
  The Main JFrame window for Notenik.

  @author Herb Bowie
 */
public class NotenikMainFrame 
    extends 
      JFrame
    implements 
      ActionListener,
      AppToBackup,
      TagsChangeAgent,
      FileSpecOpener,
      PublishAssistant,
      URLValidationRegistrar,
      XHandler,
      LinkTweakerApp {

  public static final String PROGRAM_NAME    = "Notenik";
  public static final String PROGRAM_VERSION = "1.00";

  public static final int    CHILD_WINDOW_X_OFFSET = 60;
  public static final int    CHILD_WINDOW_Y_OFFSET = 60;

  public static final        int    ONE_SECOND    = 1000;
  public static final        int    ONE_MINUTE    = ONE_SECOND * 60;
  public static final        int    ONE_HOUR      = ONE_MINUTE * 60;

  public static final String INVALID_URL_TAG = "Invalid URL";

  public static final String URLUNION_FILE_NAME           = "urlunion.html";
  public static final String INDEX_FILE_NAME              = "index.html";
  public static final String FAVORITES_FILE_NAME          = "favorites.html";
  public static final String NETSCAPE_BOOKMARKS_FILE_NAME = "bookmark.html";
  public static final String OUTLINE_FILE_NAME            = "outline.html";
  public static final String SUPPORT_FOLDER_NAME          = "urlunion";

  private             Appster appster;

  private             String  country = "  ";
  private             String  language = "  ";

  private             Home home;
  private             ProgramVersion      programVersion;
  private             XOS                 xos = XOS.getShared();
  private             Trouble             trouble = Trouble.getShared();

  private             File                appFolder;
  private             String              userName;
  private             String              userDirString;
  
  private             StatusBar           statusBar = new StatusBar();

  // About window
  private             AboutWindow         aboutWindow;

  // Publish Window
  private             PublishWindow       publishWindow;

  // Properties Window
  private             CollectionWindow    collectionWindow;
  
  // Replace Window
  private             ReplaceWindow       replaceWindow;

  // Variables used for logging
  private             Logger              logger = Logger.getShared();
  private             LogOutput           logOutput;
  private             LogWindow           logWindow;

  private DateFormat    longDateFormatter
      = new SimpleDateFormat ("EEEE MMMM d, yyyy");
  private DateFormat  backupDateFormatter
      = new SimpleDateFormat ("yyyy-MM-dd-HH-mm");

  private             UserPrefs           userPrefs;
  private             PrefsWindow         prefsWindow;
  private             RecentFiles         recentFiles;
  private             FilePrefs           filePrefs;
  private             WebPrefs            webPrefs;

  // GUI Elements
  private             TextSelector        tagsTextSelector;
  private             TagTreeCellRenderer treeCellRenderer;

  private             NotePositioned      position = null;
  private             boolean             modified = false;
  private             boolean             unsavedChanges = false;
  private             NoteList            noteList = null;
  private             DataDictionary      dict = null;
  private             RecordDefinition    recDef = null;
  private             XFileChooser        fileChooser = new XFileChooser();

  /** File of Notes that is currently open. */
  private             FileSpec            currentFileSpec = null;
  private             File                noteFile = null;
  private             File                currentDirectory;
  private             NoteIO              noteIO = null;
  private             NoteExport          exporter;
  
  private             String              fileName = "";

  public  static final String             FIND = "Find";
  public  static final String             FIND_AGAIN = "Again";

  private             String              lastTextFound = "";
  
  private             Note                foundNote = null;
  
  private             StringBuilder       titleBuilder = new StringBuilder();
  private             int                 titleStart = -1;
  private             StringBuilder       linkBuilder = new StringBuilder();
  private             int                 linkStart = -1;
  private             StringBuilder       tagsBuilder = new StringBuilder();
  private             int                 tagsStart = -1;
  private             StringBuilder       bodyBuilder = new StringBuilder();
  private             int                 bodyStart = -1;

  // Fields used to validate Web Page Notes
  private             javax.swing.Timer   validateURLTimer;
  private             ThreadGroup         webPageGroup;
  private             ArrayList           urlValidators;
  private             ProgressMonitor     progressDialog;
  private             int                 progressMax = 0;
  private             int                 progress = 0;
  private             int                 badPages = 0;

  // Help fields
  private             Tips                tips = null;

  // Written flags
  private             boolean             urlUnionWritten = false;
  private             boolean             favoritesWritten = false;
  private             boolean             netscapeWritten = false;
  private             boolean             outlineWritten = false;
  private             boolean             indexWritten = false;
  
  private             LinkTweaker         linkTweaker;
  private             TweakerPrefs        tweakerPrefs;
  private             LinkLabel           linkLabel;

  /** Creates new form NotenikMainFrame */
  public NotenikMainFrame() {
    appster = new Appster
        ("powersurgepub", "com",
          PROGRAM_NAME, PROGRAM_VERSION,
          language, country,
          this, this);
    home = Home.getShared ();
    programVersion = ProgramVersion.getShared ();
    initComponents();
    linkLabel = new LinkLabel("Link:");
    linkLabel.setLinkTextArea(linkText);
    linkLabel.setFrame(this);
    
    getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);
    WindowMenuManager.getShared(windowMenu);
    currentDirectory = fileChooser.getCurrentDirectory();

    // Set About, Quit and other Handlers in platform-specific ways
    xos.setFileMenu (fileMenu);
    xos.setHelpMenu (helpMenu);
    xos.setXHandler (this);
    xos.setMainWindow (this);
    xos.enablePreferences();

    // Initialize user preferences
    userPrefs = UserPrefs.getShared();
    prefsWindow = new PrefsWindow (this);
    
    webPrefs = prefsWindow.getWebPrefs();
    
    filePrefs = new FilePrefs(this);
    filePrefs.loadFromPrefs();
    prefsWindow.setFilePrefs(filePrefs);
    
    tweakerPrefs = new TweakerPrefs();
    prefsWindow.getPrefsTabs().add(TweakerPrefs.PREFS_TAB_NAME, tweakerPrefs);
    
    exporter = new NoteExport(this);
    
    recentFiles = new RecentFiles();
    
    filePrefs.setRecentFiles(recentFiles);
    recentFiles.registerMenu(openRecentMenu, this);
    
    recentFiles.loadFromPrefs();
    
    if (filePrefs.purgeRecentFilesAtStartup()) {
      recentFiles.purgeInaccessibleFiles();
    }
    
    initRecDef();
    noteList = new NoteList(recDef);
    position = new NotePositioned(recDef);

    // Use special text selector for the tags
    tagsTextSelector = new TextSelector();
    tagsTextSelector.setEditable(true);
    tagsTextSelector.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        tagsActionPerformed(evt);
      }
    });
    java.awt.GridBagConstraints gridBagConstraints;
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    linkPanel.add(tagsTextSelector, gridBagConstraints);
    tagsTextSelector.setValueList(noteList.getTagsList());
    
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.weighty = 0.0;
    gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
    linkPanel.add(linkLabel, gridBagConstraints);

    // Set initial UI prefs
    setBounds (
        userPrefs.getPrefAsInt (FavoritesPrefs.PREFS_LEFT, 100),
        userPrefs.getPrefAsInt (FavoritesPrefs.PREFS_TOP,  100),
        userPrefs.getPrefAsInt (FavoritesPrefs.PREFS_WIDTH, 620),
        userPrefs.getPrefAsInt (FavoritesPrefs.PREFS_HEIGHT, 620));
    CommonPrefs.getShared().setSplitPane(mainSplitPane);
    CommonPrefs.getShared().setMainWindow(this);
    setPreferredCollectionView();

    // Set up Logging
    logWindow = new LogWindow ();
    logOutput = new LogOutputText(logWindow.getTextArea());
    Logger.getShared().setLog (logOutput);
    Logger.getShared().setLogAllData (false);
    Logger.getShared().setLogThreshold (LogEvent.NORMAL);
    WindowMenuManager.getShared().add(logWindow);

    // Get App Folder
    appFolder = home.getAppFolder();
    if (appFolder == null) {
      trouble.report ("The " + home.getProgramName()
          + " Folder could not be found",
          "App Folder Missing");
    } else {
      Logger.getShared().recordEvent (LogEvent.NORMAL,
        "App Folder = " + appFolder.toString(),
        false);
    }

    aboutWindow = new AboutWindow(
      false,   // loadFromDisk, 
      true,    // jxlUsed,
      false,   // pegdownUsed,
      false,   // xerces used
      false,   // saxon used
      "2009"); // copyRightYearFrom
        

    publishWindow = new PublishWindow(this);
    publishWindow.setOnSaveOption(false);
    publishWindow.setStatusBar(statusBar);

    collectionWindow = new CollectionWindow();
    replaceWindow = new ReplaceWindow(this);
    
    linkTweaker = new LinkTweaker(this, prefsWindow.getPrefsTabs());
    linkLabel.setLinkTweaker(linkTweaker);

    // Get System Properties
    userName = System.getProperty ("user.name");
    userDirString = System.getProperty (GlobalConstants.USER_DIR);
    Logger.getShared().recordEvent (LogEvent.NORMAL,
      "User Directory = " + userDirString,
      false);

    // Write some basic data about the run-time environment to the log
    Logger.getShared().recordEvent (LogEvent.NORMAL,
        "Java Virtual Machine = " + System.getProperty("java.vm.name") +
        " version " + System.getProperty("java.vm.version") +
        " from " + StringUtils.removeQuotes(System.getProperty("java.vm.vendor")),
        false);
    if (xos.isRunningOnMacOS()) {
      Logger.getShared().recordEvent (LogEvent.NORMAL,
          "Mac Runtime for Java = " + System.getProperty("mrj.version"),
          false);
    }
    Runtime runtime = Runtime.getRuntime();
    runtime.gc();
    NumberFormat numberFormat = NumberFormat.getInstance();
    Logger.getShared().recordEvent (LogEvent.NORMAL,
        "Available Memory = " + numberFormat.format (Runtime.getRuntime().freeMemory()),
        false);

    // Automatically open the last file opened, if any

    // note = new Note();
    // displayNote();
    // String lastFolderString = userPrefs.getPref (FavoritesPrefs.LAST_FILE, "");
    String lastFolderString = filePrefs.getStartupFilePath();
    if (lastFolderString != null
        && lastFolderString.length() > 0) {
      File lastFolder = new File (lastFolderString);
      if (lastFolder.exists()
          && lastFolder.isDirectory()
          && lastFolder.canRead()) {
        openFile (lastFolder);
        if (prefsWindow.getFavoritesPrefs().isOpenStartup()) {
          launchStartupURLs();
        }
      }
    }

    if (noteList == null || noteList.size() == 0) {
      File defaultDataFolder = Home.getShared().getProgramDefaultDataFolder();
      Home.getShared().ensureProgramDefaultDataFolder();
      if (defaultDataFolder.exists()) {
        openFile (defaultDataFolder);
        if (noteList.size() == 0) {
          addFirstNote();
        }
      } else {
        newFile();
        saveFileAs(defaultDataFolder);
      }
      // displayNote();
    }

    CommonPrefs.getShared().appLaunch();

  }

  public boolean preferencesAvailable() {
    return true;
  }

  /**
   Prepare the data entry screen for a new Note.
   */
  public void newNote() {

    // Capture current category selection, if any
    String selectedTags = "";
    TagsNode tags = (TagsNode)noteTree.getLastSelectedPathComponent();
    if (tags != null) {
      selectedTags = tags.getTagsAsString();
    }
    
    boolean modOK = modIfChanged();

    if (modOK) {
      position = new NotePositioned(recDef);
      position.setIndex (noteList.size());
      fileName = "";
      displayNote();
      tagsTextSelector.setText (selectedTags);
    }
  }

  /**
   Add the first Note for a new collection.
   */
  public void addFirstNote() {
    position = new NotePositioned(recDef);
    position.setIndex (noteList.size());

    Note note = position.getNote();
    note.setTitle("PowerSurge Publishing");
    note.setLink("http://www.powersurgepub.com/");
    note.setTags("Software.Java.Groovy");
    note.setBody("Home to Notenik");

    saveNote(note);
    addNoteToList ();
    noteList.fireTableDataChanged();

    modified = false;
  }
  
  private void saveNote(Note note) {
    try {
      noteIO.save(note, true);
    } catch (IOException e) {
      ioException(e);
    }
  }
  
  /**
   Duplicate the currently displayed event.
   */
  public void duplicateNote() {

    boolean modOK = modIfChanged();

    if (modOK) {
      Note note = position.getNote();
      Note newNote = note.duplicate();
      newNote.setTitle(note.getTitle() + " copy");
      position = new NotePositioned(recDef);
      position.setIndex (noteList.size());
      position.setNote(newNote);
      position.setNewNote(true);
      fileName = "";
      displayNote();
    }
  }

  private void removeNote () {
    if (position.isNewNote()) {
      System.out.println ("New Note -- ignoring delete command");
    } else {
      boolean okToDelete = true;
      if (CommonPrefs.getShared().confirmDeletes()) {
        Object[] options = {"Yes, please",
                            "No, thanks"};
        int option = JOptionPane.showOptionDialog(
          XOS.getShared().getMainWindow(),
          "Really delete Note titled " 
            + position.getNote().getTitle() + "?",
          "Delete Confirmation",
          JOptionPane.YES_NO_OPTION, 
          JOptionPane.QUESTION_MESSAGE, 
          Home.getShared().getIcon(), 
          options, 
          options[0]);
        okToDelete = (option == 0);
      }
      if (okToDelete) {
        noFindInProgress();
        Note noteToDelete = position.getNote();
        String fileToDelete = noteToDelete.getDiskLocation();
        position.setNavigatorToList
            (collectionTabbedPane.getSelectedIndex() == 0);
        position = noteList.remove (position);
        boolean deleted = new File(fileToDelete).delete();
        if (! deleted) {
          trouble.report(
              "Unable to delete note at " + position.getNote().getFileName(), 
              "Delete Failure");
        }
        noteList.fireTableDataChanged();
        positionAndDisplay();
      } // end if user confirmed delete
    } // end if new URL not yet saved
  } // end method removeNote

  private void checkTags() {
    modIfChanged();
    TagsChangeScreen replaceScreen = new TagsChangeScreen
        (this, true, noteList.getTagsList(), this);
    replaceScreen.setLocation (
        this.getX() + CHILD_WINDOW_X_OFFSET,
        this.getY() + CHILD_WINDOW_Y_OFFSET);
    replaceScreen.setVisible (true);
    // setUnsavedChanges (true);
    // catScreen.show();
  }

  /**
   Called from TagsChangeScreen.
   @param from The from String.
   @param to   The to String.
   */
  public void changeAllTags (String from, String to) {

    modIfChanged();
    NotePositioned workNote = new NotePositioned (recDef);
    int mods = 0;
    for (int workIndex = 0; workIndex < noteList.size(); workIndex++) {
      workNote.setNote (noteList.get (workIndex));
      workNote.setIndex (workIndex);
      String before = workNote.getNote().getTags().toString();
      workNote.getNote().getTags().replace (from, to);
      if (! before.equals (workNote.getNote().getTags().toString())) {
        mods++;
        noteList.modify(workNote);
      }
    }

    JOptionPane.showMessageDialog(this,
      String.valueOf (mods)
          + " tags changed",
      "Tags Replacement Results",
      JOptionPane.INFORMATION_MESSAGE);
    displayNote();
  }

  private void flattenTags() {
    modIfChanged();
    NotePositioned workNote = new NotePositioned(recDef);
    for (int workIndex = 0; workIndex < noteList.size(); workIndex++) {
      workNote.setNote (noteList.get (workIndex));
      workNote.getNote().flattenTags();
      noteList.modify(workNote);
    }
    noFindInProgress();
    displayNote();
  }

  private void lowerCaseTags() {
    modIfChanged();
    NotePositioned workNote = new NotePositioned(recDef);
    for (int workIndex = 0; workIndex < noteList.size(); workIndex++) {
      workNote.setNote (noteList.get (workIndex));
      workNote.getNote().lowerCaseTags();
      noteList.modify(workNote);
    }
    noFindInProgress();
  }

public int checkTags (String find, String replace) {
    int mods = 0;
    Note next;
    Tags tags;
    String tag;
    for (int i = 0; i < noteList.size(); i++) {
      next = noteList.get(i);
      tags = next.getTags();
      boolean modified = false;
      if (find.equals("")) {
        tags.merge (replace);
        modified = true;
      } else {
        TagsIterator iterator = new TagsIterator (tags);
        while (iterator.hasNextTag() && (! modified)) {
          tag = iterator.nextTag();
          if (tag.equalsIgnoreCase (find)) {
            iterator.removeTag();
            if (replace.length() > 0) {
              tags.merge (replace);
            }
            modified = true;
          }
        } // end while this item has more categories
      } // end if we the find category is not blank
      if (modified) {
        mods++;
        saveNote(next);
        // setUnsavedChanges (true);
      } // end if modified
    } // end of  items
    return mods;
  } 
  
  /**
   If requested, launch any Note's link that has been tagged with "startup"
  */
  private void launchStartupURLs() {
    Note next;
    Tags tags;
    String tag;
    for (int i = 0; i < noteList.size(); i++) {
      next = noteList.get(i);
      tags = next.getTags();
      TagsIterator iterator = new TagsIterator (tags);
      while (iterator.hasNextTag()) {
        tag = iterator.nextTag();
        if (tag.equalsIgnoreCase("Startup")) {
          openURL(next.getLinkAsString());
        }
      }
    }
  }
  
  private void startReplace() {
    replaceWindow.startReplace(findText.getText());
    displayAuxiliaryWindow(replaceWindow);
  }
  
  /**
    Replace all occurrences of the given text string with a 
    specified replacement.
   
    @param findString    The string we're searching for. 
    @param replaceString The string to replace the find string. 
    @param checkTitle    Should we check the title of the URL item?
    @param checkLink      Should we check the URL of the URL item?
    @param checkTags     Should we check the tags of the URL item?
    @param checkBody Should we check the comments?
    @param caseSensitive Should we do a case-sensitive comparison?
  
  */
  public int replaceAll (String findString, String replaceString, 
      boolean checkTitle, 
      boolean checkLink, 
      boolean checkTags, 
      boolean checkBody,
      boolean caseSensitive) {
    
    int itemsChanged = 0;
    boolean found = true;
    findButton.setText(FIND);
    while (found) {
      found = findNote (
        findButton.getText(),
        findString, 
        checkTitle, 
        checkLink, 
        checkTags,
        checkBody,
        caseSensitive,
        false);
      if (found) {
        boolean replaced = replaceNote(
            findString, 
            replaceString, 
            checkTitle, 
            checkLink, 
            checkTags, 
            checkBody);
        if (replaced) {
          itemsChanged++;
        }
      } // end if another item found
    } // end while more matching Notes found
    
    
    if (itemsChanged == 0) {
      JOptionPane.showMessageDialog(this,
          "No matching Notes found",
          "OK",
          JOptionPane.WARNING_MESSAGE);
      statusBar.setStatus("No Notes found");
    } else {
      JOptionPane.showMessageDialog(this,
        String.valueOf (itemsChanged)
          + " Notes modified",
        "Replacement Results",
        JOptionPane.INFORMATION_MESSAGE);
      statusBar.setStatus(String.valueOf(itemsChanged) + " Notes modified");
    }
    
    return itemsChanged;
    
  } // end replaceAll method

  /**
    Find the next URL item containing the search string, or position the cursor
    on the search string, if it is currently empty. 
  */
  private void findNote () {

    findNote (
      findButton.getText(), 
      findText.getText().trim(), 
      replaceWindow.titleSelected(), 
      replaceWindow.linkSelected(), 
      replaceWindow.tagsSelected(),
      replaceWindow.bodySelected(),
      replaceWindow.caseSensitive(),
      true);
      
    if (findText.getText().trim().length() == 0) {
      findText.grabFocus();
      statusBar.setStatus("Enter a search string");
    }
  }
  
  /**
    Find the specified text string within the list of URL items. This method may
    be called internally, or from the ReplaceWindow. The result will be to 
    position the displays on the item found, or display a message to the user
    that no matching item was found. 
  
    @param findButtonText Either "Find" or "Again", indicating whether we
                          are starting a new search or continuing an 
                          existing one. 
    @param findString  The string we're searching for. 
    @param checkTitle  Should we check the title of the URL item?
    @param checkLink    Should we check the URL of the URL item?
    @param checkTags   Should we check the tags of the URL item?
    @param checkBody Should we check the comments?
    @param caseSensitive Should we do a case-sensitive comparison?
    @param showDialogAtEnd Show a dialog to user when no remaining Notes found?
  */
  public boolean findNote (
      String findButtonText, 
      String findString, 
      boolean checkTitle, 
      boolean checkLink, 
      boolean checkTags,
      boolean checkBody,
      boolean caseSensitive,
      boolean showDialogAtEnd) {
        
    modIfChanged();
    boolean found = false;

    String notFoundMessage;
    if (findString != null && findString.length() > 0) {
      if (findButtonText.equals (FIND)) {
        notFoundMessage = "No Notes Found";
        position.setIndex (-1);
      } else {
        notFoundMessage = "No further Notes Found";
      }
      position.incrementIndex (1);
      String findLower = findString.toLowerCase();
      String findUpper = findString.toUpperCase();
      while (position.hasValidIndex(noteList) && (! found)) {
        Note noteCheck = noteList.get (position.getIndex());
        found = findWithinNote(
            noteCheck,
            findString, 
            checkTitle, 
            checkLink, 
            checkTags,
            checkBody,
            caseSensitive,
            findLower,
            findUpper);
        if (found) {
          foundNote = noteCheck;
        } else {
          position.incrementIndex (1);
        }
      } // while still looking for next match
      if (found) {
        findInProgress();
        lastTextFound = findString;
        position = noteList.positionUsingListIndex (position.getIndex());
        positionAndDisplay();
        statusBar.setStatus("Matching Note found");
      } else {
        JOptionPane.showMessageDialog(this,
            notFoundMessage,
            "Not Found",
            JOptionPane.WARNING_MESSAGE,
            Home.getShared().getIcon());
        noFindInProgress();
        lastTextFound = "";
        statusBar.setStatus(notFoundMessage);
        foundNote = null;
      }
    } // end if we've got a find string
    return found;
  } // end method findNote
  
  /**
    Check for a search string within the given Note Item. 

    @param noteToSearch The Note item to be checked. 
    @param findString  The string we're searching for. 
    @param checkTitle  Should we check the title of the URL item?
    @param checkLink    Should we check the URL of the URL item?
    @param checkTags   Should we check the tags of the URL item?
    @param checkBody Should we check the comments?
    @param caseSensitive Should we do a case-sensitive comparison?
    @param findLower   The search string in all lower case.
    @param findUpper   The search string in all upper case. 
    @return True if an item containing the search string was found. 
  */
  private boolean findWithinNote(
      Note noteToSearch, 
      String findString, 
      boolean checkTitle, 
      boolean checkLink, 
      boolean checkTags,
      boolean checkBody,
      boolean caseSensitive,
      String findLower,
      String findUpper) {
    
    boolean found = false;
    
    if (checkTitle) {
      titleBuilder = new StringBuilder(noteToSearch.getTitle());
      if (caseSensitive) {
        titleStart = titleBuilder.indexOf(findString);
      } else {
        titleStart = StringUtils.indexOfIgnoreCase (findLower, findUpper,
            noteToSearch.getTitle(), 0);
      }
      if (titleStart >= 0) {
        found = true;
      }
    }

    if (checkLink) {
      linkBuilder = new StringBuilder(noteToSearch.getLinkAsString());
      if (caseSensitive) {
        linkStart = linkBuilder.indexOf(findString);
      } else {
        linkStart = StringUtils.indexOfIgnoreCase (findLower, findUpper,
            noteToSearch.getLinkAsString(), 0);
      }
      if (linkStart >= 0) {
        found = true;
      }
    }
    
    if (checkTags) {
      tagsBuilder = new StringBuilder(noteToSearch.getTagsAsString());
      if (caseSensitive) {
        tagsStart = tagsBuilder.indexOf(findString);
      } else {
        tagsStart = StringUtils.indexOfIgnoreCase (findLower, findUpper,
            noteToSearch.getTagsAsString(), 0);
      }
      if (tagsStart >= 0) {
        found = true;
      }
    }

    if (checkBody) {
      bodyBuilder = new StringBuilder(noteToSearch.getBody());
      if (caseSensitive) {
        bodyStart = bodyBuilder.indexOf(findString);
      } else {
        bodyStart = StringUtils.indexOfIgnoreCase (findLower, findUpper,
            noteToSearch.getBody(), 0);
      }
      if (bodyStart >= 0) {
        found = true;
      }
    }
    
    if (found) {
      foundNote = noteToSearch;
    } else {
      foundNote = null;
    }

    return found;
  }
  
  public WebPrefs getWebPrefs() {
    return webPrefs;
  }
  
  public String getLastTextFound() {
    return lastTextFound;
  }
  
  public void noFindInProgress() {
    findButton.setText(FIND);
    replaceWindow.noFindInProgress();
  }
  
  public void findInProgress() {
    findButton.setText(FIND_AGAIN);
    replaceWindow.findInProgress();
  }
  
  public void setFindText(String findString) {
    this.findText.setText(findString);
  }
  
  /**
    Replace the findString in a Note that has already been found. 
  
    @param replaceString The string to replace the found string. 
  */
  public boolean replaceNote(
      String findString,
      String replaceString,
      boolean checkTitle, 
      boolean checkLink, 
      boolean checkTags,
      boolean checkBody) {
    
    boolean replaced = false;
    if (foundNote != null) {
      if (checkTitle && titleStart >= 0) {
        titleBuilder.replace(titleStart, titleStart + findString.length(), 
            replaceString);
        foundNote.setTitle(titleBuilder.toString());
        replaced = true;
      }

      if (checkLink && linkStart >= 0) {
 
        linkBuilder.replace(linkStart, linkStart + findString.length(), 
            replaceString);
        foundNote.setLink(linkBuilder.toString());
        replaced = true;
      }
      
      if (checkTags && tagsStart >= 0) {
        tagsBuilder.replace(tagsStart, tagsStart + findString.length(), 
            replaceString);
        foundNote.setTags(tagsBuilder.toString());
        replaced = true;
      }
      
      if (checkBody && bodyStart >= 0) {
        bodyBuilder.replace(bodyStart, bodyStart + findString.length(), 
            replaceString);
        foundNote.setBody(bodyBuilder.toString());
        replaced = true;
      }
      
      if (replaced) {
        positionAndDisplay();
        statusBar.setStatus("Replacement made");
        saveNote(foundNote);
      }
    }
    return replaced;
  }

  public void firstNote () {
    modIfChanged();
    noFindInProgress();
    position.setNavigatorToList (collectionTabbedPane.getSelectedIndex() == 0);
    position = noteList.first (position);
    positionAndDisplay();
  }

  public void priorNote () {
    modIfChanged();
    noFindInProgress();
    position.setNavigatorToList (collectionTabbedPane.getSelectedIndex() == 0);
    position = noteList.prior (position);
    positionAndDisplay();
  }

  public void nextNote() {
    modIfChanged();
    noFindInProgress();
    position.setNavigatorToList (collectionTabbedPane.getSelectedIndex() == 0);
    position = noteList.next (position);
    positionAndDisplay();
  }

  public void lastNote() {
    modIfChanged();
    noFindInProgress();
    position.setNavigatorToList (collectionTabbedPane.getSelectedIndex() == 0);
    position = noteList.last (position);
    positionAndDisplay();
  }

  private void positionAndDisplay () {
    if (position.getIndex() >= 0
        && position.getIndex() < noteList.size()
        && position.getIndex() != noteTable.getSelectedRow()) {
      noteTable.setRowSelectionInterval
          (position.getIndex(), position.getIndex());
      noteTable.scrollRectToVisible
          ((noteTable.getCellRect(position.getIndex(), 0, false)));
    } 
    if (position.getTagsNode() != null
        && position.getTagsNode()
        != noteTree.getLastSelectedPathComponent()) {
      TreePath path = new TreePath(position.getTagsNode().getPath());
      noteTree.setSelectionPath (path);
      noteTree.scrollPathToVisible (path);
    }
    displayNote ();
  }

  /**
   Respond when the user clicks on a row in the URL list.
   */
  private void selectTableRow () {
    int selectedRow = noteTable.getSelectedRow();
    if (selectedRow >= 0 && selectedRow < noteList.size()) {
      modIfChanged();
      position = noteList.positionUsingListIndex (selectedRow);
      positionAndDisplay();
    }
  }

  /**
   Respond when user selects a note from the tags tree.
   */
  private void selectBranch () {

    TagsNode node = (TagsNode)noteTree.getLastSelectedPathComponent();


    if (node == null) {
      // nothing selected
    }
    else
    if (node == position.getTagsNode()) {
      // If we're already positioned on the selected node, then no
      // need to do anything else (especially since it might set off
      // an endless loop).
    }
    else
    if (node.getNodeType() == TagsNode.ITEM) {
      modIfChanged();
      Note branch = (Note)node.getTaggable();
      int branchIndex = noteList.find (branch);
      if (branchIndex >= 0) {
        position = noteList.positionUsingListIndex (branchIndex);
        position.setTagsNode (node);
        positionAndDisplay();
      } else {
        System.out.println ("Selected a branch from the tree that couldn't be found in the list");
      }
    }
    else {
      // Do nothing until an item is selected
    }
  }

  public void displayNote () {
    Note note = position.getNote();
    if (note.hasDiskLocation()) {
      reload (note);
    }
    fileName = note.getFileName();
    titleText.setText (note.getTitle());
    linkText.setText (note.getLinkAsString());
    tagsTextSelector.setText (note.getTagsAsString());
    commentsText.setText (note.getBody());
    commentsText.setCaretPosition(0);
    lastModDateText.setText (note.getLastModDate(Note.COMPLETE_FORMAT));
    statusBar.setPosition(position.getIndexForDisplay(), noteList.size());
    modified = false;
  }
  
  private void reload (Note note) {
      /* ClubEventReader reader 
          = new ClubEventReader (
              clubEvent.getDiskLocation(), 
              ClubEventReader.PLANNER_TYPE);
      boolean ok = true;
      reader.setClubEventCalc(clubEventCalc);
      try {
        reader.openForInput(clubEvent);
      } catch (java.io.IOException e) {
        ok = false;
        Logger.getShared().recordEvent(LogEvent.MEDIUM, 
            "Trouble reading " + clubEvent.getDiskLocation(), false);
      }
      
      reader.close(); */
  }

  /**
   Check to see if the user has changed anything and take appropriate
   actions if so.
   */
  public boolean modIfChanged () {
    
    boolean modOK = true;

    Note note = position.getNote();
    if (! note.equalsTitle (titleText.getText())) {
      note.setTitle (titleText.getText());
      modified = true;
    }

    if ((linkText.getText().equals (note.getLinkAsString()))
        || ((linkText.getText().length() == 0) && note.blankLink())) {
      // No change
    } else {
      note.setLink (linkText.getText());
      modified = true;
    }

    if (! note.equalsTags (tagsTextSelector.getText())) {
      note.setTags (tagsTextSelector.getText());
      modified = true;
    }

    if (! commentsText.getText().equals (note.getBody())) {
      note.setBody (commentsText.getText());
      modified = true;
    }
    
    
    if (modified) {
      String newFileName = note.getFileName();
      if ((! note.hasTitle()) || note.getTitle().length() == 0) {
        trouble.report (this, 
            "The Title field has been left blank", 
            "Missing Key Field");
        modOK = false;
      } 
      else 
      if ((! newFileName.equals(fileName))
          && noteIO.exists(newFileName)) {
        trouble.report (this, 
            "An event already exists with the same What field",
            "Duplicate Found");
        modOK = false;
      } else {
        // Modify note on disk
        note.setLastModDateToday();
        String oldDiskLocation = note.getDiskLocation();
        saveNote(note);
        String newDiskLocation = note.getDiskLocation();
        if (! newDiskLocation.equals(oldDiskLocation)) {
          File oldDiskFile = new File (oldDiskLocation);
          oldDiskFile.delete();
        }
        if (position.isNewNote()) {
          if (note.hasKey()) {
            addNoteToList ();
          } // end if we have note worth adding
        } else {
          noteList.modify(position);
        }
        noteList.fireTableDataChanged();
      }
    } // end if modified
    
    return modOK;
  } // end modIfChanged method

  private void addNoteToList () {
    position = noteList.add (position.getNote());
    if (position.hasValidIndex (noteList)) {
      positionAndDisplay();
    }
  }

  public void handleOpenApplication() {

  }

  /**
     Standard way to respond to an About Menu Item Selection on a Mac.
   */
  public void handleAbout() {
    displayAuxiliaryWindow(aboutWindow);
  }
  
  /*
   
   This section of the program deals with user preferences.
   
   */
    
  /**
     Standard way to respond to a Preferences Item Selection on a Mac.
   */
  public void handlePreferences() {
    displayPrefs ();
  }

  public void displayPrefs () {
    displayAuxiliaryWindow(prefsWindow);
  }

  public void setSplit (boolean splitPaneHorizontal) {
    int splitOrientation = JSplitPane.VERTICAL_SPLIT;
    if (splitPaneHorizontal) {
      splitOrientation = JSplitPane.HORIZONTAL_SPLIT;
    }
    mainSplitPane.setOrientation (splitOrientation);
  }

  private void savePrefs () {
    if (noteFile != null
        && noteFile.exists()
        && noteFile.isDirectory()
        && noteFile.canRead()) {
      userPrefs.setPref (FavoritesPrefs.LAST_FILE, noteFile.toString());
    }
    userPrefs.setPref (FavoritesPrefs.PREFS_LEFT, this.getX());
    userPrefs.setPref (FavoritesPrefs.PREFS_TOP, this.getY());
    userPrefs.setPref (FavoritesPrefs.PREFS_WIDTH, this.getWidth());
    userPrefs.setPref (FavoritesPrefs.PREFS_HEIGHT, this.getHeight());
    
    savePreferredCollectionView();
    userPrefs.setPref (CommonPrefs.SPLIT_HORIZONTAL,
        mainSplitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT);
    prefsWindow.savePrefs();
    if (tips != null) {
      tips.savePrefs();
    }
    boolean prefsOK = userPrefs.savePrefs();
    recentFiles.savePrefs();
    tweakerPrefs.savePrefs();
  }

  public void handleOpenFile (FileSpec fileSpec) {
    handleOpenFile (new File(fileSpec.getPath()));
  }
  
  /**      
    Standard way to respond to a document being passed to this application on a Mac.
   
    @param inFile File to be processed by this application, generally
                  as a result of a file or directory being dragged
                  onto the application icon.
   */
  public void handleOpenFile (File inFile) {
    openFile (inFile);
  }

  /**
   Open the passed URI. 
   
   @param inURI The URI to open. 
  */
  public void handleOpenURI(URI inURI) {
    // Not supported
  }

  /**
   Standard way to respond to a print request.
   */
  public void handlePrintFile (File printFile) {
    // not supported
  }

  /**
     We're out of here!
   */
  public void handleQuit() {

    closeFile();

    savePrefs();

    System.exit(0);
  }

  private void reloadFile() {
    modIfChanged();
    saveFile();
    NotePositioned savePosition = position;
    if (noteFile != null
        && noteFile.exists()
        && noteFile.isDirectory()
        && noteFile.canRead()) {
      openFile (noteFile);
      position = savePosition;
      positionAndDisplay();
    }
  }

  /**
   Let the user choose a folder to open.
   */
  private void openFile() {
    closeFile();
    fileChooser.setDialogTitle ("Open Notes");
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if (currentDirectory != null
        && currentDirectory.exists()
        && currentDirectory.isDirectory()
        && currentDirectory.canRead()) {
      fileChooser.setCurrentDirectory (currentDirectory);
    }

    File selectedFile = null;
    selectedFile = fileChooser.showOpenDialog(this);
    if (selectedFile != null) {
      if (selectedFile.exists()
          && selectedFile.isDirectory()
          && selectedFile.canRead()) {
        openFile (selectedFile);
      } else {
        trouble.report ("Trouble opening file " + selectedFile.toString(),
            "File Open Error");
      }
    } // end if user approved a file/folder choice
  } // end method openFile

  private void openFile (File fileToOpen) {
    closeFile();
    initCollection();
    setNoteFile (fileToOpen);
    try {
      noteIO.load(noteList);
    } catch (IOException e) {
      ioException(e);
    }
    collectionWindow.newNoteFolder(noteList, noteIO);
    noteList.fireTableDataChanged();
    position = new NotePositioned (recDef);
    setPreferredCollectionView();
    position = noteList.first(position);
    positionAndDisplay();
  }

  private void savePreferredCollectionView () {
    userPrefs.setPref (FavoritesPrefs.LIST_TAB_SELECTED,
        collectionTabbedPane.getSelectedIndex() == 0);
  }

  private void setPreferredCollectionView () {
    boolean listTabSelected =
        userPrefs.getPrefAsBoolean (FavoritesPrefs.LIST_TAB_SELECTED, true);
    if (listTabSelected) {
      collectionTabbedPane.setSelectedComponent (listPanel);
      position.setNavigatorToList(true);
    } else {
      collectionTabbedPane.setSelectedComponent (treePanel);
      position.setNavigatorToList(false);
    }
  }

  private void initCollection () {
    
    initRecDef();
    noteList = new NoteList(recDef);
    position = new NotePositioned(recDef);
    noteTable.setModel(noteList);
    tagsTextSelector.setValueList(noteList.getTagsList());
    noteTree.setModel (noteList.getTagsModel().getModel());
    noteTree.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);
    treeCellRenderer = new TagTreeCellRenderer ();
    noteTree.setCellRenderer (treeCellRenderer);
    noteTree.doLayout();
    // setUnsavedChanges(false);
  }
  
  private void initRecDef() {
    dict = new DataDictionary();
    recDef = new RecordDefinition(dict);
    recDef.addColumn(Note.TITLE_DEF);
    recDef.addColumn(Note.TAGS_DEF);
    recDef.addColumn(Note.LINK_DEF);
    recDef.addColumn(Note.BODY_DEF);
  }

  private void importFile () {

    fileChooser.setDialogTitle ("Import Notes");
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if (currentDirectory != null
        && currentDirectory.exists()
        && currentDirectory.isDirectory()
        && currentDirectory.canRead()) {
      fileChooser.setCurrentDirectory (currentDirectory);
    }
    File selectedFile = fileChooser.showOpenDialog(this);
    if (selectedFile != null) {
      File importFile = selectedFile;
      currentDirectory = importFile;
      NoteIO importer = new NoteIO (recDef, importFile);
      try {
        importer.load(noteList);
      } catch (IOException e) {
        ioException(e);
      }
      // collectionWindow.setList (noteList);
      // setUnsavedChanges(true);
    }
    noteList.fireTableDataChanged();
    firstNote();
  }
  
  private void exportToNoteNik() {
    boolean modOK = modIfChanged();
    int exported = 0;
    if (modOK) {
      fileChooser.setDialogTitle ("Export to NoteNik");
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      File selectedFile = fileChooser.showSaveDialog(this);
      if (selectedFile != null
          && selectedFile.isDirectory()
          && selectedFile.canWrite()) {
        File exportFolder = selectedFile;
        NoteIO exportWriter = new NoteIO(recDef, exportFolder);
        Note workNote;
        try {
          for (int workIndex = 0; workIndex < noteList.size(); workIndex++) {
            workNote = noteList.get (workIndex);
            exportWriter.save (workNote, false);
          }
          JOptionPane.showMessageDialog(this,
                String.valueOf(noteList.size()) + " Notes exported successfully to"
                  + GlobalConstants.LINE_FEED
                  + selectedFile.toString(),
                "Export Results",
                JOptionPane.INFORMATION_MESSAGE,
                Home.getShared().getIcon());
            logger.recordEvent (LogEvent.NORMAL, String.valueOf(noteList.size()) 
                + " Notes exported to " 
                + selectedFile.toString(),
                false);
            statusBar.setStatus(String.valueOf(noteList.size()) 
              + " Notes exported");
        } catch (IOException e) {
          Trouble.getShared().report(this, 
              "I/O Error exporting to NoteNik format", 
              "I/O Error", 
              JOptionPane.ERROR_MESSAGE);
        }
      } // end if user selected a valid folder
    } // end if were able to save the last modified record
  } // end method export to NoteNik
  
  /**
   Export the list of events in tab-delimited format.
   */
  private void exportTabDelim() {
    boolean modOK = modIfChanged();
    int exported = 0;
    if (modOK) {
      fileChooser.setDialogTitle ("Export to Tab-Delimited");
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fileChooser.setCurrentDirectory(noteFile.getParentFile());
      fileChooser.setSelectedFile
          (new File(noteFile.getParentFile(), noteFile.getName() + ".tab"));
      File selectedFile = fileChooser.showSaveDialog (this);
      if (selectedFile != null) {
        TabDelimFile tabs = new TabDelimFile(selectedFile);
        Note workNote;
        try {
          tabs.openForOutput(recDef);
          for (int workIndex = 0; workIndex < noteList.size(); workIndex++) {
            workNote = noteList.get (workIndex);
            if (workNote != null) {
              tabs.nextRecordOut(workNote);
              exported++;
            }
          }
          tabs.close();
          JOptionPane.showMessageDialog(this,
              String.valueOf(exported) + " Notes exported successfully to"
                + GlobalConstants.LINE_FEED
                + selectedFile.toString(),
              "Export Results",
              JOptionPane.INFORMATION_MESSAGE,
              Home.getShared().getIcon());
          logger.recordEvent (LogEvent.NORMAL, String.valueOf(exported) 
              + " Notes exported in tab-delimited format to " 
              + selectedFile.toString(),
              false);
          statusBar.setStatus(String.valueOf(exported) 
            + " Notes exported");
        } catch (java.io.IOException e) {
          logger.recordEvent (LogEvent.MEDIUM,
            "Problem exporting Notes to " + selectedFile.toString(),
              false);
            trouble.report ("I/O error attempting to export notes to " 
                + selectedFile.toString(),
              "I/O Error");
            statusBar.setStatus("Trouble exporting Notes");
        } // end if I/O error
      } // end if user selected an output file
    } // end if were able to save the last modified record
  }
  
  private void ioException(IOException e) {
    Trouble.getShared().report("I/O Exception", "Trouble");
  }

  private void closeFile() {
    if (this.noteFile != null) {
      modIfChanged();
      // checkForUnsavedChanges();
      collectionWindow.close(noteIO);
      publishWindow.closeSource();
      filePrefs.handleClose();
    }
  }

  private void saveFile () {
    savePreferredCollectionView();
    modIfChanged();
    if (noteFile == null) {
      saveFileAs();
    } else {
      try {
        noteIO.save (noteList);
      } catch (IOException e) {
        ioException (e);
      }
      publishWindow.saveSource();
    }
  }
  
  public boolean saveAll() {
    
    boolean saveOK = modIfChanged();
    
    int numberSaved = 0;
    int numberDeleted = 0;
    for (int i = 0; i < noteList.totalSize() && saveOK; i++) {
      Note nextNote = noteList.getUnfiltered(i);
      String oldDiskLocation = nextNote.getDiskLocation();
      try {
        noteIO.save(nextNote, true);
      } catch (IOException e) {
        saveOK = false;
        trouble.report(this, "Trouble saving the item to disk", "I/O Error");
        saveOK = false;
      }
      if (saveOK) {
        numberSaved++;
        String newDiskLocation = nextNote.getDiskLocation();
        if (! newDiskLocation.equals(oldDiskLocation)) {
          File oldDiskFile = new File (oldDiskLocation);
          oldDiskFile.delete();
          numberDeleted++;
        }
      } 
    }
    
    String saveResult;
    if (saveOK) {
      saveResult = "succeeded";
    } else {
      saveResult = "failed";
    }
      logger.recordEvent(LogEvent.NORMAL, 
          "Save All command succeeded, resulting in " 
            + String.valueOf(numberSaved)
            + " saves and "
            + String.valueOf(numberDeleted)
            + " deletes", false);

    
    return saveOK;
  }

  /**
   Save the current collection to a location specified by the user.
   */
  private void saveFileAs () {
    fileChooser.setDialogTitle ("Save Notes to File");
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if (currentDirectory != null
        && currentDirectory.exists()
        && currentDirectory.isDirectory()
        && currentDirectory.canRead()) {
      fileChooser.setCurrentDirectory (currentDirectory);
      fileChooser.setSelectedFile (currentDirectory);
    }
    File selectedFile = fileChooser.showSaveDialog (this);
    if(selectedFile != null) {
      File chosenFile = selectedFile;
      saveFileAs(chosenFile);
    }
  }
  
  public void newFile() {
    closeFile();
    initCollection();
 
    fileChooser.setDialogTitle ("Select Folder for New Note Collection");
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if (currentDirectory != null
        && currentDirectory.exists()
        && currentDirectory.isDirectory()
        && currentDirectory.canRead()) {
      fileChooser.setCurrentDirectory (currentDirectory);
      fileChooser.setSelectedFile (currentDirectory);
    }
    File selectedFile = fileChooser.showSaveDialog (this);
    if(selectedFile != null) {
      File chosenFile = selectedFile;
      saveFileAs(chosenFile);
      // collectionWindow.setList (noteList);
      // collectionWindow.newNoteFolder(noteList, null);
      noteList.fireTableDataChanged();
      // setNoteFile (null);
      // setPreferredCollectionView();
      // newNote();
      addFirstNote();
    }
    
  }
  
  /**
   Save the current collection of Notes to the specified file. 
  
   @param asFile The file to save the noteList to.  
  */
  private void saveFileAs(File asFile) {
    savePreferredCollectionView();
    setNoteFile (asFile);
    try {
      noteIO.save (noteList);
    } catch (IOException e) {
      ioException(e);
    }
    collectionWindow.newNoteFolder(noteList, noteIO);
    publishWindow.saveSource();
  }

  /**
   Save various bits of information about a new Note file that we are
   working with.

   @param file The specific file we are working with that contains a list
   of Notes.

   */
  private void setNoteFile (File file) {
    if (file == null) {
      noteFile = null;
      noteIO = null;
      exporter = null;
      currentFileSpec = null;
      statusBar.setFileName("            ", " ");
    } else {
      noteFile = file;
      noteIO = new NoteIO(recDef, noteFile);
      exporter = new NoteExport(this);
      if (noteList != null) {
        noteList.setSource (file);
      }
      currentFileSpec = recentFiles.addRecentFile (file);
      currentDirectory = file;
      userPrefs.setPref (FavoritesPrefs.LAST_FILE, file.toString());
      FileName fileName = new FileName (file);
      statusBar.setFileName(fileName);
      publishWindow.openSource(currentDirectory);
    }
  }

  public void displayPublishWindow() {
    displayAuxiliaryWindow(publishWindow);
  }

  public void displayAuxiliaryWindow(WindowToManage window) {
    window.setLocation(
        this.getX() + 60,
        this.getY() + 60);
    WindowMenuManager.getShared().makeVisible(window);
  }

  /**
   Any pre-processing to do before PublishWindow starts its publication
   process. In particular, make the source data available to the publication
   script.

   @param publishTo The folder to which we are publishing.
   */
  public void prePub(File publishTo) {
    // File urlsTab = new File (publishTo, "urls.tab");
    // io.exportToTabDelimited(noteList, urlsTab, false, "");
    
    // File favoritesTab = new File (publishTo, "favorites.tab");
    // io.exportToTabDelimited(noteList, favoritesTab, true,
    //    prefsWindow.getFavoritesPrefs().getFavoritesTags());

    urlUnionWritten = false;
    favoritesWritten = false;
    netscapeWritten = false;
    outlineWritten = false;
    indexWritten = false;
  }

  /**
   Perform the requested publishing operation.
   
   @param operand
   */
  public boolean pubOperation(File publishTo, String operand) {
    boolean operationOK = false;
    if (operand.equalsIgnoreCase("urlunion")) {
      operationOK = publishURLUnion(publishTo);
    }
    else
    if (operand.equalsIgnoreCase("favorites")) {
      operationOK = publishFavorites(publishTo);
    }
    else
    if (operand.equalsIgnoreCase("netscape")) {
      operationOK = publishNetscape(publishTo);
    }
    else
    if (operand.equalsIgnoreCase("outline")) {
      operationOK = publishOutline(publishTo);
    }
    else
    if (operand.equalsIgnoreCase("index")) {
      operationOK = publishIndex(publishTo);
    }
    return operationOK;
  }

  /**
   Any post-processing to be done after PublishWindow has completed its
   publication process.

   @param publishTo The folder to which we are publishing.
   */
  public void postPub(File publishTo) {

  }

  private boolean publishURLUnion (File publishTo) {
    urlUnionWritten = false;
    File urlUnionFile = new File (publishTo, URLUNION_FILE_NAME);
    if (! urlUnionFile.toString().equals(noteFile.toString())) {
      exporter.exportToURLUnion (urlUnionFile, noteList);
      urlUnionWritten = true;
    }
    return urlUnionWritten;
  }

  private boolean publishFavorites (File publishTo) {
    
    // Publish selected favorites
    favoritesWritten = false;
    if (! noteFile.getName().equalsIgnoreCase (FAVORITES_FILE_NAME)) {
      favoritesWritten = exporter.publishFavorites
          (publishTo, noteList, prefsWindow.getFavoritesPrefs());
    }
    return favoritesWritten;
  }

  private boolean publishNetscape (File publishTo) {
    // Publish in Netscape bookmarks format
    netscapeWritten = false;
    if (! noteFile.getName().equalsIgnoreCase (NETSCAPE_BOOKMARKS_FILE_NAME)) {
      File netscapeFile = new File (publishTo,
        NETSCAPE_BOOKMARKS_FILE_NAME);
      exporter.publishNetscape (netscapeFile, noteList);
      netscapeWritten = true;
    }
    return netscapeWritten;
  }

  private boolean publishOutline (File publishTo) {
    // Publish in outline form using dynamic html
    outlineWritten = false;
    if (! noteFile.getName().equalsIgnoreCase (OUTLINE_FILE_NAME)) {
      File dynamicHTMLFile = new File (publishTo, OUTLINE_FILE_NAME);
      exporter.publishOutline(dynamicHTMLFile, noteList);
      outlineWritten = true;
    }
    return outlineWritten;
  }

  private boolean publishIndex (File publishTo) {
    // Publish index file pointing to other files
    indexWritten = false;
    if (! noteFile.getName().equalsIgnoreCase (INDEX_FILE_NAME)) {
      File indexFile = new File (publishTo, INDEX_FILE_NAME);
      exporter.publishIndex(indexFile, noteFile,
         favoritesWritten, FAVORITES_FILE_NAME,
         netscapeWritten, NETSCAPE_BOOKMARKS_FILE_NAME,
         outlineWritten, OUTLINE_FILE_NAME);
      indexWritten = true;
    }
    return indexWritten;
  }
  
  /**
   Backup without prompting the user. 
  
   @return True if backup was successful. 
  */
  public boolean backupWithoutPrompt() {

    boolean backedUp = false;
    
    if (noteFile != null && noteFile.exists()) {
      FileName urlFileName = new FileName (noteFile);
      File backupFolder = getBackupFolder();
      String backupFileName 
          = filePrefs.getBackupFileName(noteFile, urlFileName.getExt());
      File backupFile = new File 
          (backupFolder, backupFileName);
      backedUp = backup (backupFile);
    }

    return backedUp;
    
  }
  
  /**
   Prompt the user for a backup location. 

   @return True if backup was successful.
  */
  public boolean promptForBackup() {
    boolean modOK = modIfChanged();
    boolean backedUp = false;

		if (modOK) {
      fileChooser.setDialogTitle ("Make Backup of Notenik Folder");
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      FileName noteFileName = new FileName (home.getUserHome());
      if (noteFile != null && noteFile.exists()) {
        noteFileName = new FileName (noteFile);
      }
      File backupFolder = getBackupFolder();
      fileChooser.setCurrentDirectory (backupFolder);
      File selectedFile = fileChooser.showSaveDialog (this);
      if (selectedFile != null) {
        File backupFile = selectedFile;
        backedUp = backup (backupFile);
        FileSpec fileSpec = recentFiles.get(0);
        fileSpec.setBackupFolder(backupFile);
        if (backedUp) {
          JOptionPane.showMessageDialog(this,
              "Backup completed successfully",
              "Backup Results",
              JOptionPane.INFORMATION_MESSAGE,
              Home.getShared().getIcon());
        } // end if backed up successfully
      } // end if the user selected a backup location
    } // end if modIfChanged had no problems

    return backedUp;

  }
  
  /**
   Backup the data store to the indicated location. 
  
   @param backupFile The backup file to be used. 
  
   @return 
  */
  public boolean backup(File folderForBackups) {
    
    StringBuilder backupPath = new StringBuilder();
    try {
      backupPath.append(folderForBackups.getCanonicalPath());
    } catch (IOException e) {
      backupPath.append(folderForBackups.getAbsolutePath());
    }
    backupPath.append(xos.getPathSeparator());
    backupPath.append(noteFile.getName());
    backupPath.append(" ");
    backupPath.append("backup ");
    backupPath.append(filePrefs.getBackupDate());
    File backupFolder = new File (backupPath.toString());
    backupFolder.mkdir();
    boolean backedUp = FileUtils.copyFolder (noteFile, backupFolder);
    if (backedUp) {
      FileSpec fileSpec = recentFiles.get(0);
      filePrefs.saveLastBackupDate
          (fileSpec, recentFiles.getPrefsQualifier(), 0);
      logger.recordEvent (LogEvent.NORMAL,
          "Notes backed up to " + backupFolder.toString(),
            false);
    } else {
      logger.recordEvent (LogEvent.MEDIUM,
          "Problem backing up Notes to " + backupFolder.toString(),
            false);
    }
    return backedUp;
  }
  
  /**
   Return the presumptive folder to be used for backups. 
  
   @return The folder we think the user wishes to use for backups,
           based on his past choices, or on the application defaults.
  */
  private File getBackupFolder() {
    File backupFolder = home.getUserHome();
    if (noteFile != null && noteFile.exists()) {    
      FileSpec fileSpec = recentFiles.get(0);
      String backupFolderStr = fileSpec.getBackupFolder();
      File defaultBackupFolder = new File (fileSpec.getFolder(), "backups");
      if (backupFolderStr == null
          || backupFolderStr.length() < 2) {
        backupFolder = defaultBackupFolder;
      } else {
        backupFolder = new File (backupFolderStr);
        if (backupFolder.exists()
            && backupFolder.canWrite()) {
          // leave as-is
        } else {
          backupFolder = defaultBackupFolder;
        }
      }
    }
    return backupFolder;
  }
  
  private void tweakURL() {
    if (linkText.getText().length() > 0) {
      linkTweaker.setLink(linkText.getText());
    }
    displayAuxiliaryWindow(linkTweaker);
  }
  
  public void setTweakedLink (String tweakedLink, String linkID) {
    if (tweakedLink.length() > 0) {
      linkText.setText(tweakedLink);
    }
  }

  public void openURL (File file) {
    appster.openURL(file);
  }

  public void openURL (String url) {
    appster.openURL(url);
  }

  private void tagsActionPerformed (java.awt.event.ActionEvent evt) {
    
  }

  /**
    Validate Notes.
   */
  public void validateURLs () {

    modIfChanged();

    // Make sure user is ready to proceed
    Object[] options = { "Continue", "Cancel" };
    int userOption = JOptionPane.showOptionDialog(this,
        "Please ensure your Internet connection is active",
        "Validate Web Pages",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.WARNING_MESSAGE,
        null, options, options[0]);

    // If User is ready, then proceed
    if (userOption == 0) {

      // Prepare Auxiliary List to track invalid Notes
      webPageGroup = new ThreadGroup("WebPage threads");
      urlValidators = new ArrayList();

      // Go through sorted items looking for Web Pages
      Note workNote;
      String address;
      URLValidator validator;
      for (int workIndex = 0; workIndex < noteList.size(); workIndex++) {
        workNote = noteList.get (workIndex);
        address = workNote.getURLasString();
        if (address.length() > 0) {
          validator = new URLValidator (webPageGroup, workNote, workIndex, this);
          urlValidators.add (validator);
        }
      } // end of list

      // Prepare dialog to show validation progress
      progress = 0;
      progressMax = urlValidators.size();
      progressDialog = new ProgressMonitor (this,
          "Validating "
              + String.valueOf (progressMax)
              + " Links...",
          "                                                  ", // Status Note
          0,              // lower bound of range
          progressMax     // upper bound of range
          );
      progressDialog.setProgress(0);
      progressDialog.setMillisToDecideToPopup(500);
      progressDialog.setMillisToPopup(500);

      // Now start threads to check Web pages
      badPages = 0;
      for (int i = 0; i < urlValidators.size(); i++) {
        validator = (URLValidator)urlValidators.get(i);
        validator.start();
      } // end for each page being validated

      // Start timer to give the user a chance to cancel
      if (validateURLTimer == null) {
        validateURLTimer = new javax.swing.Timer (ONE_SECOND, this);
      } else {
        validateURLTimer.setDelay (ONE_SECOND);
      }
      validateURLTimer.start();
    } // continue rather than cancel
  } // end validateURLs method

  /**
    Record the results each time a WebPage checks in to report that
    its URL validation process has been complete.

    @param item   The ToDoItem whose Web Page URL was being validated.
    @param valid  True if the URL was found to be valid.
   */
  public synchronized void registerURLValidationResult
      (ItemWithURL item, boolean valid) {
    progress++;
    progressDialog.setProgress (progress);
    progressDialog.setNote ("Validation complete for "
        + String.valueOf (progress));
    if (! valid) {
      badPages++;
    }
    if (progress >= progressMax) {
      validateURLAllDone();
    } // end if all pages checked
  } // end method validateURLPageDone

  /**
    Handle GUI events, including the firing of various timers.

    @param event The GUI event that fired the action.
   */
  public void actionPerformed (ActionEvent event) {
    Object source = event.getSource();

    // URL Validation Timer
    if (source == validateURLTimer) {
      if (progressDialog.isCanceled()) {
        URLValidator validator;
        for (int i = 0; i < urlValidators.size(); i++) {
          validator = (URLValidator)urlValidators.get(i);
          if (! validator.isValidationComplete()) {
            Logger.getShared().recordEvent (new LogEvent (LogEvent.MEDIUM,
                "URL Validation incomplete for "
                + validator.toString(),
                false));
            validator.interrupt();
          }
        } // end for each page being validated
        validateURLAllDone();
      }
    }

  } // end method

  /**
    Shut down the URL Validation process and report the results.
   */
  private void validateURLAllDone () {
    if (validateURLTimer != null
        && validateURLTimer.isRunning()) {
      validateURLTimer.stop();
    }

    // Add "Invalid URL" tags to invalid URL items
    if (badPages > 0) {
      URLValidator validator;
      NotePositioned workNote;
      for (int i = 0; i < urlValidators.size(); i++) {
        validator = (URLValidator)urlValidators.get(i);
        if (! validator.isValidURL()) {
          workNote = noteList.positionUsingListIndex (validator.getIndex());
          if (workNote.getNote().equals (validator.getItemWithURL())) {
            workNote.getNote().getTags().merge (INVALID_URL_TAG);
            noteList.modify(workNote);
            saveNote(workNote.getNote());
          } // end if we have the right URL
        } // end if URL wasn't validated
      } // end for each page being validated
      noteList.fireTableDataChanged();
    } // end if any bad pages found

    // Close progress dialog and show user the final results
    progressDialog.close();
    JOptionPane.showMessageDialog(this,
      String.valueOf (badPages)
          + " Invalid URL(s) Found out of "
          + String.valueOf (urlValidators.size()),
      "URL Validation Results",
      JOptionPane.INFORMATION_MESSAGE);

  } // end method

  public XFileChooser getFileChooser () {
    return fileChooser;
  }

  public File getCurrentDirectory () {
    return currentDirectory;
  }

  /**
   Show the tips.
   */
  public void showTips () {

    if (tips == null) {
      tips = new Tips ();
      tips.noTipsAtStartupOption();
    }
    tips.setVisible (true);
    tips.toFront();
  }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    mainToolBar = new javax.swing.JToolBar();
    urlOKButton = new javax.swing.JButton();
    urlNewButton = new javax.swing.JButton();
    urlDeleteButton = new javax.swing.JButton();
    urlFirstButton = new javax.swing.JButton();
    urlPriorButton = new javax.swing.JButton();
    urlNextButton = new javax.swing.JButton();
    urlLastButton = new javax.swing.JButton();
    launchButton = new javax.swing.JButton();
    findText = new javax.swing.JTextField();
    findButton = new javax.swing.JButton();
    mainSplitPane = new javax.swing.JSplitPane();
    collectionTabbedPane = new javax.swing.JTabbedPane();
    listPanel = new javax.swing.JPanel();
    tableScrollPane = new javax.swing.JScrollPane();
    noteTable = new javax.swing.JTable();
    treePanel = new javax.swing.JPanel();
    treeScrollPane = new javax.swing.JScrollPane();
    noteTree = new javax.swing.JTree();
    linkPanel = new javax.swing.JPanel();
    titleLabel = new javax.swing.JLabel();
    titleText = new javax.swing.JTextField();
    urlScrollPane = new javax.swing.JScrollPane();
    linkText = new javax.swing.JTextArea();
    tagsLabel = new javax.swing.JLabel();
    commentsLabel = new javax.swing.JLabel();
    commentsScrollPane = new javax.swing.JScrollPane();
    commentsText = new javax.swing.JTextArea();
    lastModDateLabel = new javax.swing.JLabel();
    lastModDateText = new javax.swing.JLabel();
    mainMenuBar = new javax.swing.JMenuBar();
    fileMenu = new javax.swing.JMenu();
    fileNewMenuItem = new javax.swing.JMenuItem();
    openMenuItem = new javax.swing.JMenuItem();
    openRecentMenu = new javax.swing.JMenu();
    fileSaveMenuItem = new javax.swing.JMenuItem();
    saveAllMenuItem = new javax.swing.JMenuItem();
    fileSaveAsMenuItem = new javax.swing.JMenuItem();
    reloadMenuItem = new javax.swing.JMenuItem();
    jSeparator1 = new javax.swing.JSeparator();
    publishWindowMenuItem = new javax.swing.JMenuItem();
    publishNowMenuItem = new javax.swing.JMenuItem();
    jSeparator4 = new javax.swing.JSeparator();
    fileBackupMenuItem = new javax.swing.JMenuItem();
    jSeparator5 = new javax.swing.JSeparator();
    importMenuItem = new javax.swing.JMenuItem();
    exportMenu = new javax.swing.JMenu();
    exportNoteNikMenuItem = new javax.swing.JMenuItem();
    exportTabDelimitedMenuItem = new javax.swing.JMenuItem();
    editMenu = new javax.swing.JMenu();
    deleteMenuItem = new javax.swing.JMenuItem();
    collectionMenu = new javax.swing.JMenu();
    propertiesMenuItem = new javax.swing.JMenuItem();
    jSeparator2 = new javax.swing.JPopupMenu.Separator();
    findMenuItem = new javax.swing.JMenuItem();
    replaceMenuItem = new javax.swing.JMenuItem();
    jSeparator10 = new javax.swing.JPopupMenu.Separator();
    addReplaceMenuItem = new javax.swing.JMenuItem();
    flattenTagsMenuItem = new javax.swing.JMenuItem();
    lowerCaseTagsMenuItem = new javax.swing.JMenuItem();
    jSeparator3 = new javax.swing.JSeparator();
    validateURLsMenuItem = new javax.swing.JMenuItem();
    noteMenu = new javax.swing.JMenu();
    newNoteMenuItem = new javax.swing.JMenuItem();
    deleteNoteMenuItem = new javax.swing.JMenuItem();
    nextMenuItem = new javax.swing.JMenuItem();
    priorMenuItem = new javax.swing.JMenuItem();
    toolsMenu = new javax.swing.JMenu();
    toolsOptionsMenuItem = new javax.swing.JMenuItem();
    windowMenu = new javax.swing.JMenu();
    helpMenu = new javax.swing.JMenu();
    helpHistoryMenuItem = new javax.swing.JMenuItem();
    userGuideMenuItem = new javax.swing.JMenuItem();
    jSeparator7 = new javax.swing.JSeparator();
    helpSoftwareUpdatesMenuItem = new javax.swing.JMenuItem();
    webMenuItem = new javax.swing.JMenuItem();
    submitFeedbackMenuItem = new javax.swing.JMenuItem();
    jSeparator8 = new javax.swing.JSeparator();
    helpReduceWindowSizeMenuItem = new javax.swing.JMenuItem();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    mainToolBar.setRollover(true);

    urlOKButton.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
    urlOKButton.setText("OK");
    urlOKButton.setToolTipText("Complete your changes to this item");
    urlOKButton.setFocusable(false);
    urlOKButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    urlOKButton.setMargin(new java.awt.Insets(0, 4, 4, 4));
    urlOKButton.setMaximumSize(new java.awt.Dimension(60, 30));
    urlOKButton.setMinimumSize(new java.awt.Dimension(30, 26));
    urlOKButton.setPreferredSize(new java.awt.Dimension(40, 28));
    urlOKButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    urlOKButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        urlOKButtonActionPerformed(evt);
      }
    });
    mainToolBar.add(urlOKButton);

    urlNewButton.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
    urlNewButton.setText("+");
    urlNewButton.setToolTipText("Add a new item");
    urlNewButton.setFocusable(false);
    urlNewButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    urlNewButton.setMargin(new java.awt.Insets(0, 4, 4, 4));
    urlNewButton.setMaximumSize(new java.awt.Dimension(60, 30));
    urlNewButton.setMinimumSize(new java.awt.Dimension(30, 26));
    urlNewButton.setPreferredSize(new java.awt.Dimension(40, 28));
    urlNewButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    urlNewButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        urlNewButtonActionPerformed(evt);
      }
    });
    mainToolBar.add(urlNewButton);

    urlDeleteButton.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
    urlDeleteButton.setText("-");
    urlDeleteButton.setToolTipText("Delete this item");
    urlDeleteButton.setFocusable(false);
    urlDeleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    urlDeleteButton.setMargin(new java.awt.Insets(0, 4, 4, 4));
    urlDeleteButton.setMaximumSize(new java.awt.Dimension(60, 30));
    urlDeleteButton.setMinimumSize(new java.awt.Dimension(30, 26));
    urlDeleteButton.setPreferredSize(new java.awt.Dimension(40, 28));
    urlDeleteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    urlDeleteButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        urlDeleteButtonActionPerformed(evt);
      }
    });
    mainToolBar.add(urlDeleteButton);

    urlFirstButton.setText("<<");
    urlFirstButton.setToolTipText("Return to beginning of list");
    urlFirstButton.setFocusable(false);
    urlFirstButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    urlFirstButton.setMaximumSize(new java.awt.Dimension(60, 30));
    urlFirstButton.setMinimumSize(new java.awt.Dimension(30, 26));
    urlFirstButton.setPreferredSize(new java.awt.Dimension(40, 28));
    urlFirstButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    urlFirstButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        urlFirstButtonAction(evt);
      }
    });
    mainToolBar.add(urlFirstButton);

    urlPriorButton.setText("<");
    urlPriorButton.setToolTipText("Return to prior item in list");
    urlPriorButton.setFocusable(false);
    urlPriorButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    urlPriorButton.setMaximumSize(new java.awt.Dimension(60, 30));
    urlPriorButton.setMinimumSize(new java.awt.Dimension(30, 26));
    urlPriorButton.setPreferredSize(new java.awt.Dimension(40, 28));
    urlPriorButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    urlPriorButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        urlPriorButtonAction(evt);
      }
    });
    mainToolBar.add(urlPriorButton);

    urlNextButton.setText(">");
    urlNextButton.setToolTipText("Advance to next item in list");
    urlNextButton.setFocusable(false);
    urlNextButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    urlNextButton.setMaximumSize(new java.awt.Dimension(60, 30));
    urlNextButton.setMinimumSize(new java.awt.Dimension(30, 26));
    urlNextButton.setPreferredSize(new java.awt.Dimension(40, 28));
    urlNextButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    urlNextButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        urlNextButtonAction(evt);
      }
    });
    mainToolBar.add(urlNextButton);

    urlLastButton.setText(">>");
    urlLastButton.setToolTipText("Go to end of list");
    urlLastButton.setFocusable(false);
    urlLastButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    urlLastButton.setMaximumSize(new java.awt.Dimension(60, 30));
    urlLastButton.setMinimumSize(new java.awt.Dimension(30, 26));
    urlLastButton.setPreferredSize(new java.awt.Dimension(40, 28));
    urlLastButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    urlLastButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        urlLastButtonAction(evt);
      }
    });
    mainToolBar.add(urlLastButton);

    launchButton.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
    launchButton.setText("Launch");
    launchButton.setToolTipText("Open the URL in your Web browser");
    launchButton.setFocusable(false);
    launchButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    launchButton.setMargin(new java.awt.Insets(0, 4, 4, 4));
    launchButton.setMaximumSize(new java.awt.Dimension(80, 30));
    launchButton.setMinimumSize(new java.awt.Dimension(30, 26));
    launchButton.setPreferredSize(new java.awt.Dimension(40, 28));
    launchButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    launchButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        launchButtonActionPerformed(evt);
      }
    });
    mainToolBar.add(launchButton);

    findText.setMargin(new java.awt.Insets(4, 4, 4, 4));
    findText.setMaximumSize(new java.awt.Dimension(240, 30));
    findText.setMinimumSize(new java.awt.Dimension(40, 26));
    findText.setPreferredSize(new java.awt.Dimension(120, 28));
    findText.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        findTextActionPerformed(evt);
      }
    });
    findText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        findTextKeyTyped(evt);
      }
    });
    mainToolBar.add(findText);

    findButton.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
    findButton.setText("Find");
    findButton.setToolTipText("Search for the text entered to the left");
    findButton.setFocusable(false);
    findButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    findButton.setMaximumSize(new java.awt.Dimension(72, 30));
    findButton.setMinimumSize(new java.awt.Dimension(48, 26));
    findButton.setPreferredSize(new java.awt.Dimension(60, 28));
    findButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    findButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        findButtonActionPerformed(evt);
      }
    });
    mainToolBar.add(findButton);

    getContentPane().add(mainToolBar, java.awt.BorderLayout.NORTH);

    mainSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

    listPanel.setLayout(new java.awt.BorderLayout());

    noteTable.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null}
      },
      new String [] {
        "Title 1", "Title 2", "Title 3", "Title 4"
      }
    ));
    noteTable.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        noteTableMouseClicked(evt);
      }
    });
    noteTable.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        noteTableKeyReleased(evt);
      }
    });
    tableScrollPane.setViewportView(noteTable);

    listPanel.add(tableScrollPane, java.awt.BorderLayout.CENTER);

    collectionTabbedPane.addTab("List", listPanel);

    treePanel.setLayout(new java.awt.BorderLayout());

    noteTree.getSelectionModel().setSelectionMode
    (TreeSelectionModel.SINGLE_TREE_SELECTION);
    noteTree.addTreeSelectionListener (new TreeSelectionListener() {
      public void valueChanged (TreeSelectionEvent e) {
        selectBranch();
      }
    });
    treeScrollPane.setViewportView(noteTree);

    treePanel.add(treeScrollPane, java.awt.BorderLayout.CENTER);

    collectionTabbedPane.addTab("Tags", treePanel);

    mainSplitPane.setLeftComponent(collectionTabbedPane);

    linkPanel.setLayout(new java.awt.GridBagLayout());

    titleLabel.setLabelFor(titleText);
    titleLabel.setText("Title:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
    linkPanel.add(titleLabel, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    linkPanel.add(titleText, gridBagConstraints);

    linkText.setColumns(20);
    linkText.setLineWrap(true);
    linkText.setRows(2);
    urlScrollPane.setViewportView(linkText);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
    linkPanel.add(urlScrollPane, gridBagConstraints);

    tagsLabel.setText("Tags:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
    linkPanel.add(tagsLabel, gridBagConstraints);

    commentsLabel.setLabelFor(commentsText);
    commentsLabel.setText("Body:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
    linkPanel.add(commentsLabel, gridBagConstraints);

    commentsText.setColumns(20);
    commentsText.setLineWrap(true);
    commentsText.setRows(5);
    commentsText.setWrapStyleWord(true);
    commentsScrollPane.setViewportView(commentsText);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 0.8;
    gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
    linkPanel.add(commentsScrollPane, gridBagConstraints);

    lastModDateLabel.setLabelFor(commentsText);
    lastModDateLabel.setText("Mod Date:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
    linkPanel.add(lastModDateLabel, gridBagConstraints);

    lastModDateText.setLabelFor(commentsText);
    lastModDateText.setText("  ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
    linkPanel.add(lastModDateText, gridBagConstraints);

    mainSplitPane.setRightComponent(linkPanel);

    getContentPane().add(mainSplitPane, java.awt.BorderLayout.CENTER);

    fileMenu.setText("File");

    fileNewMenuItem.setText("New...");
    fileNewMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        fileNewMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(fileNewMenuItem);

    openMenuItem.setAccelerator(KeyStroke.getKeyStroke (KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    openMenuItem.setText("Open...");
    openMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(openMenuItem);

    openRecentMenu.setText("Open Recent");
    fileMenu.add(openRecentMenu);

    fileSaveMenuItem.setAccelerator(KeyStroke.getKeyStroke (KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    fileSaveMenuItem.setText("Save");
    fileSaveMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        fileSaveMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(fileSaveMenuItem);

    saveAllMenuItem.setText("Save All");
    saveAllMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveAllMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(saveAllMenuItem);

    fileSaveAsMenuItem.setText("Save As...");
    fileSaveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        fileSaveAsMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(fileSaveAsMenuItem);

    reloadMenuItem.setText("Reload");
    reloadMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        reloadMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(reloadMenuItem);
    fileMenu.add(jSeparator1);

    publishWindowMenuItem.setAccelerator(KeyStroke.getKeyStroke (KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    publishWindowMenuItem.setText("Publish...");
    publishWindowMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        publishWindowMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(publishWindowMenuItem);

    publishNowMenuItem.setText("Publish Now");
    publishNowMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        publishNowMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(publishNowMenuItem);
    fileMenu.add(jSeparator4);

    fileBackupMenuItem.setAccelerator(KeyStroke.getKeyStroke (KeyEvent.VK_B, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    fileBackupMenuItem.setText("Backup...");
    fileBackupMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        fileBackupMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(fileBackupMenuItem);
    fileMenu.add(jSeparator5);

    importMenuItem.setText("Import...");
    importMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        importMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(importMenuItem);

    exportMenu.setText("Export");

    exportNoteNikMenuItem.setText("NoteNik...");
    exportNoteNikMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exportNoteNikMenuItemActionPerformed(evt);
      }
    });
    exportMenu.add(exportNoteNikMenuItem);

    exportTabDelimitedMenuItem.setText("Tab-Delimited...");
    exportTabDelimitedMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exportTabDelimitedMenuItemActionPerformed(evt);
      }
    });
    exportMenu.add(exportTabDelimitedMenuItem);

    fileMenu.add(exportMenu);

    mainMenuBar.add(fileMenu);

    editMenu.setText("Edit");

    deleteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_BACK_SPACE, 0));
    deleteMenuItem.setText("Delete");
    deleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        deleteMenuItemActionPerformed(evt);
      }
    });
    editMenu.add(deleteMenuItem);

    mainMenuBar.add(editMenu);

    collectionMenu.setText("Collection");

    propertiesMenuItem.setAccelerator(KeyStroke.getKeyStroke (KeyEvent.VK_I, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    propertiesMenuItem.setText("Get Info");
    propertiesMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        propertiesMenuItemActionPerformed(evt);
      }
    });
    collectionMenu.add(propertiesMenuItem);
    collectionMenu.add(jSeparator2);

    findMenuItem.setText("Find");
    findMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_F,
      Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
  findMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      findMenuItemActionPerformed(evt);
    }
  });
  collectionMenu.add(findMenuItem);

  replaceMenuItem.setText("Replace...");
  replaceMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_R,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
replaceMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    replaceMenuItemActionPerformed(evt);
  }
  });
  collectionMenu.add(replaceMenuItem);
  collectionMenu.add(jSeparator10);

  addReplaceMenuItem.setText("Add/Replace Tag...");
  addReplaceMenuItem.setToolTipText("Add/Replace a tag on all items on which it occurs");
  addReplaceMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      addReplaceMenuItemActionPerformed(evt);
    }
  });
  collectionMenu.add(addReplaceMenuItem);

  flattenTagsMenuItem.setText("Flatten Levels");
  flattenTagsMenuItem.setToolTipText("Remove levels from all tags for each URL, making each level a separate tag, and eliminating any duplicates. ");
  flattenTagsMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      flattenTagsMenuItemActionPerformed(evt);
    }
  });
  collectionMenu.add(flattenTagsMenuItem);

  lowerCaseTagsMenuItem.setText("Lower Case");
  lowerCaseTagsMenuItem.setToolTipText("Change all capital letters in tags to lower case");
  lowerCaseTagsMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      lowerCaseTagsMenuItemActionPerformed(evt);
    }
  });
  collectionMenu.add(lowerCaseTagsMenuItem);
  collectionMenu.add(jSeparator3);

  validateURLsMenuItem.setText("Validate Links...");
  validateURLsMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      validateURLsMenuItemActionPerformed(evt);
    }
  });
  collectionMenu.add(validateURLsMenuItem);

  mainMenuBar.add(collectionMenu);

  noteMenu.setText("Note");

  newNoteMenuItem.setAccelerator(KeyStroke.getKeyStroke (KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
  newNoteMenuItem.setText("New Note");
  newNoteMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      newNoteMenuItemActionPerformed(evt);
    }
  });
  noteMenu.add(newNoteMenuItem);

  deleteNoteMenuItem.setText("Delete Note");
  deleteNoteMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      deleteNoteMenuItemActionPerformed(evt);
    }
  });
  noteMenu.add(deleteNoteMenuItem);

  nextMenuItem.setAccelerator(KeyStroke.getKeyStroke (KeyEvent.VK_CLOSE_BRACKET, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
  nextMenuItem.setText("Go to Next Note");
  nextMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      nextMenuItemActionPerformed(evt);
    }
  });
  noteMenu.add(nextMenuItem);

  priorMenuItem.setAccelerator(KeyStroke.getKeyStroke (KeyEvent.VK_OPEN_BRACKET, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
  priorMenuItem.setText("Go to Previous Note");
  priorMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      priorMenuItemActionPerformed(evt);
    }
  });
  noteMenu.add(priorMenuItem);

  mainMenuBar.add(noteMenu);

  toolsMenu.setText("Tools");

  toolsOptionsMenuItem.setText("Options...");
  toolsOptionsMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      toolsOptionsMenuItemActionPerformed(evt);
    }
  });
  toolsMenu.add(toolsOptionsMenuItem);

  mainMenuBar.add(toolsMenu);

  windowMenu.setText("Window");
  mainMenuBar.add(windowMenu);

  helpMenu.setText("Help");

  helpHistoryMenuItem.setText("Program History");
  helpHistoryMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      helpHistoryMenuItemActionPerformed(evt);
    }
  });
  helpMenu.add(helpHistoryMenuItem);

  userGuideMenuItem.setText("User Guide");
  userGuideMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      userGuideMenuItemActionPerformed(evt);
    }
  });
  helpMenu.add(userGuideMenuItem);
  helpMenu.add(jSeparator7);

  helpSoftwareUpdatesMenuItem.setText("Check for Updates...");
  helpSoftwareUpdatesMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      helpSoftwareUpdatesMenuItemActionPerformed(evt);
    }
  });
  helpMenu.add(helpSoftwareUpdatesMenuItem);

  webMenuItem.setText("URL Union Home Page");
  webMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      webMenuItemActionPerformed(evt);
    }
  });
  helpMenu.add(webMenuItem);

  submitFeedbackMenuItem.setText("Submit Feedback");
  submitFeedbackMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      submitFeedbackMenuItemActionPerformed(evt);
    }
  });
  helpMenu.add(submitFeedbackMenuItem);
  helpMenu.add(jSeparator8);

  helpReduceWindowSizeMenuItem.setText("Reduce Window Size");
  helpReduceWindowSizeMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_W,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
helpReduceWindowSizeMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    helpReduceWindowSizeMenuItemActionPerformed(evt);
  }
  });
  helpMenu.add(helpReduceWindowSizeMenuItem);

  mainMenuBar.add(helpMenu);

  setJMenuBar(mainMenuBar);

  pack();
  }// </editor-fold>//GEN-END:initComponents

private void launchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_launchButtonActionPerformed
  openURL (linkText.getText());
}//GEN-LAST:event_launchButtonActionPerformed

private void fileNewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileNewMenuItemActionPerformed
  newFile();
}//GEN-LAST:event_fileNewMenuItemActionPerformed

private void urlNewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlNewButtonActionPerformed
  newNote();
}//GEN-LAST:event_urlNewButtonActionPerformed

private void urlDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlDeleteButtonActionPerformed
  removeNote();
}//GEN-LAST:event_urlDeleteButtonActionPerformed

private void urlFirstButtonAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlFirstButtonAction
  firstNote();
}//GEN-LAST:event_urlFirstButtonAction

private void urlPriorButtonAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlPriorButtonAction
  priorNote();
}//GEN-LAST:event_urlPriorButtonAction

private void urlNextButtonAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlNextButtonAction
  nextNote();
}//GEN-LAST:event_urlNextButtonAction

private void urlLastButtonAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlLastButtonAction
  lastNote();
}//GEN-LAST:event_urlLastButtonAction

private void fileSaveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSaveAsMenuItemActionPerformed
  saveFileAs ();
}//GEN-LAST:event_fileSaveAsMenuItemActionPerformed

private void fileSaveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSaveMenuItemActionPerformed
  saveFile();
}//GEN-LAST:event_fileSaveMenuItemActionPerformed

private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
  openFile();
}//GEN-LAST:event_openMenuItemActionPerformed

private void importMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importMenuItemActionPerformed
  importFile();
}//GEN-LAST:event_importMenuItemActionPerformed

private void noteTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_noteTableMouseClicked
  selectTableRow();
}//GEN-LAST:event_noteTableMouseClicked

private void noteTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_noteTableKeyReleased
  selectTableRow();
}//GEN-LAST:event_noteTableKeyReleased

private void fileBackupMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileBackupMenuItemActionPerformed
  if (noteFile != null 
      && noteFile.exists()
      && noteList != null
      && noteList.size() > 0) {
    promptForBackup();
  } else {
    trouble.report(
        this, 
        "Open a Notes folder before attempting a backup", 
        "Backup Error", 
        JOptionPane.ERROR_MESSAGE);
  }
}//GEN-LAST:event_fileBackupMenuItemActionPerformed

private void findTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findTextActionPerformed
    findNote();
}//GEN-LAST:event_findTextActionPerformed

private void findTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_findTextKeyTyped
    if (! findText.getText().equals (lastTextFound)) {
      noFindInProgress();
    }
}//GEN-LAST:event_findTextKeyTyped

private void findButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findButtonActionPerformed
  findNote();
}//GEN-LAST:event_findButtonActionPerformed

private void findMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findMenuItemActionPerformed
  findNote();
}//GEN-LAST:event_findMenuItemActionPerformed

private void urlOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlOKButtonActionPerformed
    modIfChanged();
    positionAndDisplay();
}//GEN-LAST:event_urlOKButtonActionPerformed

private void flattenTagsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flattenTagsMenuItemActionPerformed
  flattenTags();
}//GEN-LAST:event_flattenTagsMenuItemActionPerformed

private void lowerCaseTagsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lowerCaseTagsMenuItemActionPerformed
  lowerCaseTags();
}//GEN-LAST:event_lowerCaseTagsMenuItemActionPerformed

private void addReplaceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addReplaceMenuItemActionPerformed
  checkTags();
}//GEN-LAST:event_addReplaceMenuItemActionPerformed

private void validateURLsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validateURLsMenuItemActionPerformed
  validateURLs();
}//GEN-LAST:event_validateURLsMenuItemActionPerformed

private void propertiesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_propertiesMenuItemActionPerformed
  // setUnsavedChanges (true);
  displayAuxiliaryWindow(collectionWindow);
}//GEN-LAST:event_propertiesMenuItemActionPerformed

private void reloadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadMenuItemActionPerformed
  reloadFile();
}//GEN-LAST:event_reloadMenuItemActionPerformed

private void deleteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteMenuItemActionPerformed
  removeNote();
}//GEN-LAST:event_deleteMenuItemActionPerformed

private void nextMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextMenuItemActionPerformed
  nextNote();
}//GEN-LAST:event_nextMenuItemActionPerformed

private void priorMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priorMenuItemActionPerformed
  priorNote();
}//GEN-LAST:event_priorMenuItemActionPerformed

private void helpSoftwareUpdatesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpSoftwareUpdatesMenuItemActionPerformed
  programVersion.informUserIfNewer();
  programVersion.informUserIfLatest();
}//GEN-LAST:event_helpSoftwareUpdatesMenuItemActionPerformed

private void submitFeedbackMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitFeedbackMenuItemActionPerformed
  openURL ("mailto:support@powersurgepub.com?subject=Notenik Feedback");
}//GEN-LAST:event_submitFeedbackMenuItemActionPerformed

private void userGuideMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userGuideMenuItemActionPerformed
  File userGuideFile = new File (appFolder, "urlunion.html");
  try {
    URI userGuideURI = userGuideFile.toURI();
    URL userGuideURL = userGuideURI.toURL();
    openURL(userGuideURL.toString());
  }
  catch (MalformedURLException e) {
    trouble.report("Trouble opening the User Guide", "User Guide Problem");
  }
}//GEN-LAST:event_userGuideMenuItemActionPerformed

private void webMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webMenuItemActionPerformed
  openURL ("http://www.powersurgepub.com/products/urlunion.html");
}//GEN-LAST:event_webMenuItemActionPerformed

private void toolsOptionsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolsOptionsMenuItemActionPerformed
  this.handlePreferences();
}//GEN-LAST:event_toolsOptionsMenuItemActionPerformed

private void helpReduceWindowSizeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpReduceWindowSizeMenuItemActionPerformed
    setBounds(100, 100, 800, 600);
    // pack();
}//GEN-LAST:event_helpReduceWindowSizeMenuItemActionPerformed

private void publishWindowMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_publishWindowMenuItemActionPerformed
  displayPublishWindow();
}//GEN-LAST:event_publishWindowMenuItemActionPerformed

private void helpHistoryMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpHistoryMenuItemActionPerformed
  File historyFile = new File (appFolder, "versions.html");
  try {
    URI historyURI = historyFile.toURI();
    URL historyURL = historyURI.toURL();
    openURL(historyURL.toString());
  }
  catch (MalformedURLException e) {
    trouble.report("Trouble opening the Program History", "Program History Problem");
  }
}//GEN-LAST:event_helpHistoryMenuItemActionPerformed

  private void publishNowMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_publishNowMenuItemActionPerformed
    publishWindow.publishNow();
  }//GEN-LAST:event_publishNowMenuItemActionPerformed

  private void replaceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceMenuItemActionPerformed
    startReplace();
  }//GEN-LAST:event_replaceMenuItemActionPerformed

  private void exportNoteNikMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportNoteNikMenuItemActionPerformed
    exportToNoteNik();
  }//GEN-LAST:event_exportNoteNikMenuItemActionPerformed

  private void saveAllMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAllMenuItemActionPerformed
    saveAll();
  }//GEN-LAST:event_saveAllMenuItemActionPerformed

  private void newNoteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newNoteMenuItemActionPerformed
    newNote();
  }//GEN-LAST:event_newNoteMenuItemActionPerformed

  private void deleteNoteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteNoteMenuItemActionPerformed
    removeNote();
  }//GEN-LAST:event_deleteNoteMenuItemActionPerformed

  private void exportTabDelimitedMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportTabDelimitedMenuItemActionPerformed
    exportTabDelim();
  }//GEN-LAST:event_exportTabDelimitedMenuItemActionPerformed



  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JMenuItem addReplaceMenuItem;
  private javax.swing.JMenu collectionMenu;
  private javax.swing.JTabbedPane collectionTabbedPane;
  private javax.swing.JLabel commentsLabel;
  private javax.swing.JScrollPane commentsScrollPane;
  private javax.swing.JTextArea commentsText;
  private javax.swing.JMenuItem deleteMenuItem;
  private javax.swing.JMenuItem deleteNoteMenuItem;
  private javax.swing.JMenu editMenu;
  private javax.swing.JMenu exportMenu;
  private javax.swing.JMenuItem exportNoteNikMenuItem;
  private javax.swing.JMenuItem exportTabDelimitedMenuItem;
  private javax.swing.JMenuItem fileBackupMenuItem;
  private javax.swing.JMenu fileMenu;
  private javax.swing.JMenuItem fileNewMenuItem;
  private javax.swing.JMenuItem fileSaveAsMenuItem;
  private javax.swing.JMenuItem fileSaveMenuItem;
  private javax.swing.JButton findButton;
  private javax.swing.JMenuItem findMenuItem;
  private javax.swing.JTextField findText;
  private javax.swing.JMenuItem flattenTagsMenuItem;
  private javax.swing.JMenuItem helpHistoryMenuItem;
  private javax.swing.JMenu helpMenu;
  private javax.swing.JMenuItem helpReduceWindowSizeMenuItem;
  private javax.swing.JMenuItem helpSoftwareUpdatesMenuItem;
  private javax.swing.JMenuItem importMenuItem;
  private javax.swing.JSeparator jSeparator1;
  private javax.swing.JPopupMenu.Separator jSeparator10;
  private javax.swing.JPopupMenu.Separator jSeparator2;
  private javax.swing.JSeparator jSeparator3;
  private javax.swing.JSeparator jSeparator4;
  private javax.swing.JSeparator jSeparator5;
  private javax.swing.JSeparator jSeparator7;
  private javax.swing.JSeparator jSeparator8;
  private javax.swing.JLabel lastModDateLabel;
  private javax.swing.JLabel lastModDateText;
  private javax.swing.JButton launchButton;
  private javax.swing.JPanel linkPanel;
  private javax.swing.JTextArea linkText;
  private javax.swing.JPanel listPanel;
  private javax.swing.JMenuItem lowerCaseTagsMenuItem;
  private javax.swing.JMenuBar mainMenuBar;
  private javax.swing.JSplitPane mainSplitPane;
  private javax.swing.JToolBar mainToolBar;
  private javax.swing.JMenuItem newNoteMenuItem;
  private javax.swing.JMenuItem nextMenuItem;
  private javax.swing.JMenu noteMenu;
  private javax.swing.JTable noteTable;
  private javax.swing.JTree noteTree;
  private javax.swing.JMenuItem openMenuItem;
  private javax.swing.JMenu openRecentMenu;
  private javax.swing.JMenuItem priorMenuItem;
  private javax.swing.JMenuItem propertiesMenuItem;
  private javax.swing.JMenuItem publishNowMenuItem;
  private javax.swing.JMenuItem publishWindowMenuItem;
  private javax.swing.JMenuItem reloadMenuItem;
  private javax.swing.JMenuItem replaceMenuItem;
  private javax.swing.JMenuItem saveAllMenuItem;
  private javax.swing.JMenuItem submitFeedbackMenuItem;
  private javax.swing.JScrollPane tableScrollPane;
  private javax.swing.JLabel tagsLabel;
  private javax.swing.JLabel titleLabel;
  private javax.swing.JTextField titleText;
  private javax.swing.JMenu toolsMenu;
  private javax.swing.JMenuItem toolsOptionsMenuItem;
  private javax.swing.JPanel treePanel;
  private javax.swing.JScrollPane treeScrollPane;
  private javax.swing.JButton urlDeleteButton;
  private javax.swing.JButton urlFirstButton;
  private javax.swing.JButton urlLastButton;
  private javax.swing.JButton urlNewButton;
  private javax.swing.JButton urlNextButton;
  private javax.swing.JButton urlOKButton;
  private javax.swing.JButton urlPriorButton;
  private javax.swing.JScrollPane urlScrollPane;
  private javax.swing.JMenuItem userGuideMenuItem;
  private javax.swing.JMenuItem validateURLsMenuItem;
  private javax.swing.JMenuItem webMenuItem;
  private javax.swing.JMenu windowMenu;
  // End of variables declaration//GEN-END:variables

}
