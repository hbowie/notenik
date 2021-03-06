/*
 * Copyright 2009 - 2017 Herb Bowie
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
  import com.powersurgepub.psdatalib.psdata.widgets.*;
  import com.powersurgepub.psdatalib.pstags.*;
  import com.powersurgepub.psdatalib.notenik.*;
  import com.powersurgepub.psdatalib.psdata.values.*;
  import com.powersurgepub.psdatalib.pslist.*;
  import com.powersurgepub.psdatalib.script.*;
  import com.powersurgepub.psdatalib.tabdelim.*;
  import com.powersurgepub.psdatalib.textmerge.*;
  import com.powersurgepub.psdatalib.textmerge.input.*;
  import com.powersurgepub.psdatalib.txbio.*;
  import com.powersurgepub.pspub.*;
  import com.powersurgepub.pstextio.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.urlvalidator.*;
  import com.powersurgepub.xos2.*;
  import java.awt.*;
  import java.awt.datatransfer.*;
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
      DateWidgetOwner,
      DisplayWindow,
      TagsChangeAgent,
      FileSpecOpener,
      PublishAssistant,
      ScriptExecutor,
      URLValidationRegistrar,
      XHandler,
      LinkTweakerApp {

  public static final String PROGRAM_NAME    = "Notenik";
  public static final String PROGRAM_VERSION = "3.00";

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
  
  // Replace Window
  private             ReplaceWindow       replaceWindow;
  
  // File Info Window
  private             FileInfoWindow      fileInfoWindow;

  // Variables used for logging
  private             Logger              logger = Logger.getShared();
  private             LogOutput           logOutput;
  private             LogWindow           logWindow;

  private DateFormat    longDateFormatter
      = new SimpleDateFormat ("EEEE MMMM d, yyyy");
  private DateFormat  backupDateFormatter
      = new SimpleDateFormat ("yyyy-MM-dd-HH-mm");
  private    DateFormat       dateFormatter
    = new SimpleDateFormat ("yyyy-MM-dd");

  private             UserPrefs           userPrefs;
  private             GeneralPrefs        generalPrefs;
  private             CollectionPrefs     collectionPrefs;
  private             MasterCollection    masterCollection;
  private             boolean             editingMasterCollection = false;
  private             FilePrefs           filePrefs;
  private             WebPrefs            webPrefs;
  private             Reports             reports;
  
  /** File of Notes that is currently open. */
  private             NoteIO              noteIO = null;
  private             FileSpec            currentFileSpec = null;
  private             File                noteFile = null;
  private             File                currentDirectory;
  private             NoteExport          exporter;
  private             ArrayList<DataWidget> widgets = new ArrayList<DataWidget>();

  // GUI Elements
  private             TextSelector        tagsTextSelector;
  private             TagTreeCellRenderer treeCellRenderer;
  private             DataWidget          linkText = null;
  private             DataWidget          statusWidget = null;
  private             DateWidget          dateWidget = null;
  private             DataWidget          seqWidget = null;
  private             DataWidget          recursWidget = null;
  private             boolean             statusIncluded      = false;
  private             boolean             dateIncluded        = false;
  private             boolean             recursIncluded      = false;
  private             boolean             seqIncluded         = false;

  private             NotePositioned      position = null;
  private             boolean             modified = false;
  private             boolean             unsavedChanges = false;
  
  /** This is the current collection of Notes. */
  private             NoteList            noteList = null;
  
  // The following fields define the fields in the collection. 
  // private             int                 noteType = NoteParms.NOTES_ONLY_TYPE;
  // private             DataDictionary      dict = null;
  // private             RecordDefinition    recDef = null;
  
  private             XFileChooser        fileChooser = new XFileChooser();
  
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
  
  // private             JPanel              notePanel;
  private             LinkLabel           linkLabel;
  
  private             JLabel              lastModDateLabel;
  private             JLabel              lastModDateText;
  
  private             FolderSyncPrefs     folderSyncPrefs;
  private             String              oldTitle = "";
  
  // System ClipBoard fields
  boolean             clipBoardOwned = false;
  Clipboard           clipBoard = null;
  Transferable        clipContents = null;
  
  private             TextMergeHarness    textMerge = null;
  
  private             DisplayTab          displayTab;
  public static final int DISPLAY_TAB_INDEX = 0;
  public static final int CONTENT_TAB_INDEX = 1;
  
  private             NoteSortParm         noteSortParm = new NoteSortParm();
  
  private             String oldSeq = "";

  /** Creates new form NotenikMainFrame */
  public NotenikMainFrame() {
    
    logWindow = new LogWindow ();
    logOutput = new LogOutputText(logWindow.getTextArea());
    Logger.getShared().setLog (logOutput);
    Logger.getShared().setLogAllData (false);
    Logger.getShared().setLogThreshold (LogEvent.NORMAL);
    WindowMenuManager.getShared().add(logWindow);
    WindowMenuManager.getShared().makeVisible(logWindow);
    
    appster = new Appster
        ("powersurgepub", "com",
          PROGRAM_NAME, PROGRAM_VERSION,
          language, country,
          this, this);
    home = Home.getShared ();
    programVersion = ProgramVersion.getShared ();
    initComponents();
    
    noteSortParm.populateMenu(sortMenu);
    
    // textMerge = TextMergeHarness.getShared(noteList);
    reports = new Reports(reportsMenu);
    reports.setScriptExecutor(this);
    
    getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);
    WindowMenuManager.getShared().setWindowMenu(windowMenu);
    currentDirectory = fileChooser.getCurrentDirectory();

    // Set About, Quit and other Handlers in platform-specific ways
    xos.setFileMenu (fileMenu);
    home.setHelpMenu(this, helpMenu);
    xos.setHelpMenu (helpMenu);
    xos.setHelpMenuItem(home.getHelpMenuItem());
    xos.setXHandler (this);
    xos.setMainWindow (this);
    xos.enablePreferences();

    // Initialize user preferences
    userPrefs = UserPrefs.getShared();
    generalPrefs = new GeneralPrefs (this);
    collectionPrefs = new CollectionPrefs(this);
    folderSyncPrefs = collectionPrefs.getFolderSyncPrefs();
    
    webPrefs = generalPrefs.getWebPrefs();
    reports.setWebPrefs(webPrefs);
    
    filePrefs = new FilePrefs(this);
    filePrefs.loadFromPrefs();
    generalPrefs.setFilePrefs(filePrefs);
    
    tweakerPrefs = new TweakerPrefs();
    generalPrefs.getPrefsTabs().add(TweakerPrefs.PREFS_TAB_NAME, tweakerPrefs);
    
    exporter = new NoteExport(this);
    
    masterCollection = new MasterCollection();
    
    filePrefs.setRecentFiles(masterCollection.getRecentFiles());
    masterCollection.registerMenu(openRecentMenu, this);
    
    masterCollection.load();
    
    if (filePrefs.purgeRecentFilesAtStartup()) {
      masterCollection.purgeInaccessibleFiles();
    }
    
    if (masterCollection.hasMasterCollection()) {
      createMasterCollectionMenuItem.setEnabled(false);
      openMasterCollectionMenuItem.setEnabled(true);
    } else {
      createMasterCollectionMenuItem.setEnabled(true);
      openMasterCollectionMenuItem.setEnabled(false);
    }
    
    // initRecDef();
    // noteList = new NoteList(recDef);
    // position = new NotePositioned(recDef);
    // buildNoteTabs();

    // Set initial UI prefs
    setBounds (
        userPrefs.getPrefAsInt (FavoritesPrefs.PREFS_LEFT, 100),
        userPrefs.getPrefAsInt (FavoritesPrefs.PREFS_TOP,  100),
        userPrefs.getPrefAsInt (FavoritesPrefs.PREFS_WIDTH, 620),
        userPrefs.getPrefAsInt (FavoritesPrefs.PREFS_HEIGHT, 620));
    CommonPrefs.getShared().setSplitPane(mainSplitPane);
    CommonPrefs.getShared().setMainWindow(this);
    // setPreferredCollectionView();



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
      true,    // Markdown converter Used,
      true,    // xerces used
      true,    // saxon used
      "2009"); // copyRightYearFrom
        

    publishWindow = new PublishWindow(this);
    publishWindow.setOnSaveOption(false);
    publishWindow.setStatusBar(statusBar);

    replaceWindow = new ReplaceWindow(this);
    
    linkTweaker = new LinkTweaker(this, generalPrefs.getPrefsTabs());
    
    fileInfoWindow = new FileInfoWindow(this);
    
    displayTab = new DisplayTab(generalPrefs.getDisplayPrefs());

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

    // newNote = new Note();
    // displayNote();
    // String lastFolderString = userPrefs.getPref (FavoritesPrefs.LAST_FILE, "");
    FileSpec lastFileSpec = filePrefs.getStartupFileSpec();
    String lastFolderString = filePrefs.getStartupFilePath();
    String lastTitle = "";
    if (lastFolderString != null
        && lastFolderString.length() > 0) {
      File lastFolder = new File (lastFolderString);
      if (goodCollection(lastFolder)) {
        if (lastFileSpec != null) {
          lastTitle = lastFileSpec.getLastTitle();
        }
        openFile (lastFolder, lastTitle, true);
        if (generalPrefs.getFavoritesPrefs().isOpenStartup()) {
          launchStartupURLs();
        }
      }
    }

    if (noteList == null || noteList.size() == 0) {
      File defaultDataFolder = Home.getShared().getProgramDefaultDataFolder();
      Home.getShared().ensureProgramDefaultDataFolder();
      if (defaultDataFolder.exists()) {
        openFile (defaultDataFolder, "", true);
        addFirstNoteIfListEmpty();
      } else {
        userNewFile();
        saveFileAs(defaultDataFolder);
      }
      // displayNote();
    }
    
    JTextField textField = new JTextField();
    EditMenuItemMaker editMenuItemMaker 
        = new EditMenuItemMaker (textField);
    editMenuItemMaker.addCutCopyPaste (editMenu);

    CommonPrefs.getShared().appLaunch();
    
    WindowMenuManager.getShared().hide(logWindow);

  }

  public boolean preferencesAvailable() {
    return true;
  }

  /**
   Prepare the data entry screen for a new Note.
   */
  private void newNote() {

    // Capture current category selection, if any
    String selectedTags = "";
    TagsNode tags = (TagsNode)noteTree.getLastSelectedPathComponent();
    if (tags != null) {
      selectedTags = tags.getTagsAsString();
    }
    
    DataValueSeq newSeq = null;
    if (noteSortParm.getParm() == NoteSortParm.SORT_BY_SEQ_AND_TITLE
        && position != null
        && position.getNote() != null
        && noteList.atEnd(position)) {
      newSeq = new DataValueSeq(position.getNote().getSeq());
      newSeq.increment(false);
    }
    
    boolean modOK = modIfChanged();

    if (modOK) {
      position = new NotePositioned(noteIO.getNoteParms());
      position.setIndex (noteList.size());
      fileName = "";
      boolean seqSet = false;
      if (oldSeq != null && oldSeq.length() > 0) {
        position.getNote().setSeq(oldSeq);
        seqSet = true;
      }
      else
      if (newSeq != null) {
        position.getNote().setSeq(newSeq.toString());
        seqSet = true;
      }
      displayNote();
      tagsTextSelector.setText (selectedTags);
      if (seqSet) {
        seqWidget.setText(position.getNote().getSeq());
      }
      itemTabbedPane.setSelectedIndex(CONTENT_TAB_INDEX);
      oldSeq = "";
    }
  }
  
  /**
   Add one note if the list is empty. 
  */
  private void addFirstNoteIfListEmpty() {
    if (noteList.size() == 0) {
      addFirstNote();
    }
  }

  /**
   Add the first Note for a new collection.
   */
  private void addFirstNote() {
    position = new NotePositioned(noteIO.getRecDef());
    position.setIndex (noteList.size());

    Note note = position.getNote();
    note.setTitle("Notenik.net");
    note.setLink("http://www.notenik.net/");
    note.setTags("Software.Java.Groovy");
    note.setBody("Home to Notenik");

    saveNote(note);
    addNoteToList();
    noteList.fireTableDataChanged();

    modified = false;
  }
  
  /**
   Saves a newNote in its primary location and in its sync folder, if specified. 
  
   @param note The newNote to be saved. 
  */
  protected boolean saveNote(Note note) {
    try {
      noteIO.save(note, true);
      if (folderSyncPrefs.getSync()) {
        noteIO.saveToSyncFolder(
            folderSyncPrefs.getSyncFolder(), 
            folderSyncPrefs.getSyncPrefix(), 
            note);
        note.setSynced(true);
      }
      return true;
    } catch (IOException e) {
      ioException(e);
      return false;
    }
  }
  
  /**
   Duplicate the currently displayed event.
   */
  public void duplicateNote() {

    boolean modOK = modIfChanged();

    if (modOK) {
      Note note = position.getNote();
      Note newNote = new Note(note);
      newNote.setTitle(note.getTitle() + " copy");
      position = new NotePositioned(noteIO.getRecDef());
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
        
        if (deleted && editingMasterCollection) {
          masterCollection.removeRecentFile(noteToDelete.getTitle());
        }
        
        if (folderSyncPrefs.getSync()) {
          File syncFile = noteIO.getSyncFile(
              folderSyncPrefs.getSyncFolder(), 
              folderSyncPrefs.getSyncPrefix(), 
              noteToDelete.getTitle());
          syncFile.delete();
        }
        noteList.fireTableDataChanged();
        positionAndDisplay();
      } // end if user confirmed delete
    } // end if new URL not yet saved
  } // end method removeNote

  private void checkTags() {
    boolean modOK = modIfChanged();

    if (modOK) {
      TagsChangeScreen replaceScreen = new TagsChangeScreen
          (this, true, noteList.getTagsList(), this);
      replaceScreen.setLocation (
          this.getX() + CHILD_WINDOW_X_OFFSET,
          this.getY() + CHILD_WINDOW_Y_OFFSET);
      replaceScreen.setVisible (true);
      // setUnsavedChanges (true);
      // catScreen.show();
    }
  }

  /**
   Called from TagsChangeScreen.
   @param from The from String.
   @param to   The to String.
   */
  public void changeAllTags (String from, String to) {

    boolean modOK = modIfChanged();

    if (modOK) {
      NotePositioned workNote = new NotePositioned (noteIO.getRecDef());
      int mods = 0;
      for (int workIndex = 0; workIndex < noteList.size(); workIndex++) {
        workNote.setNote (noteList.get (workIndex));
        workNote.setIndex (workIndex);
        String before = workNote.getNote().getTags().toString();
        workNote.getNote().getTags().replace (from, to);
        if (! before.equals (workNote.getNote().getTags().toString())) {
          mods++;
          noteList.modify(workNote);
          saveNote(workNote.getNote());
        }
      }

      JOptionPane.showMessageDialog(this,
        String.valueOf (mods)
            + " tags changed",
        "Tags Replacement Results",
        JOptionPane.INFORMATION_MESSAGE);
      displayNote();
    }
  }

  private void flattenTags() {
    boolean modOK = modIfChanged();

    if (modOK) {
      NotePositioned workNote = new NotePositioned(noteIO.getRecDef());
      for (int workIndex = 0; workIndex < noteList.size(); workIndex++) {
        workNote.setNote (noteList.get (workIndex));
        workNote.getNote().flattenTags();
        noteList.modify(workNote);
      }
      noFindInProgress();
      displayNote();
    }
  }

  private void lowerCaseTags() {
    boolean modOK = modIfChanged();

    if (modOK) {
      NotePositioned workNote = new NotePositioned(noteIO.getRecDef());
      for (int workIndex = 0; workIndex < noteList.size(); workIndex++) {
        workNote.setNote (noteList.get (workIndex));
        workNote.getNote().lowerCaseTags();
        noteList.modify(workNote);
      }
      noFindInProgress();
    }
  }

  public int checkTags (String find, String replace) {
    int mods = 0;
    Note next;
    Tags tags;
    String tag;
    for (int i = 0; i < noteList.size(); i++) {
      next = noteList.get(i);
      tags = next.getTags();
      boolean tagsModified = false;
      if (find.equals("")) {
        tags.merge (replace);
        tagsModified = true;
      } else {
        TagsIterator iterator = new TagsIterator (tags);
        while (iterator.hasNextTag() && (! tagsModified)) {
          tag = iterator.nextTag();
          if (tag.equalsIgnoreCase (find)) {
            iterator.removeTag();
            if (replace.length() > 0) {
              tags.merge (replace);
            }
            tagsModified = true;
          }
        } // end while this item has more categories
      } // end if we the find category is not blank
      if (tagsModified) {
        mods++;
        saveNote(next);
        // setUnsavedChanges (true);
      } // end if tags modified
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
        
    boolean modOK = modIfChanged();
    boolean found = false;
    if (modOK) {
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
    } // end if mods ok
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
    boolean modOK = modIfChanged();
    if (modOK) {
      noFindInProgress();
      position.setNavigatorToList (collectionTabbedPane.getSelectedIndex() == 0);
      position = noteList.first (position);
      positionAndDisplay();
    }
  }

  public void priorNote () {
    boolean modOK = modIfChanged();
    if (modOK) {
      noFindInProgress();
      position.setNavigatorToList (collectionTabbedPane.getSelectedIndex() == 0);
      position = noteList.prior (position);
      positionAndDisplay();
    }
  }

  public void nextNote() {
    boolean modOK = modIfChanged();
    if (modOK) {
      noFindInProgress();
      position.setNavigatorToList (collectionTabbedPane.getSelectedIndex() == 0);
      position = noteList.next (position);
      positionAndDisplay();
    }
  }

  public void lastNote() {
    boolean modOK = modIfChanged();
    if (modOK) {
      noFindInProgress();
      position.setNavigatorToList (collectionTabbedPane.getSelectedIndex() == 0);
      position = noteList.last (position);
      positionAndDisplay();
    }
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
      boolean modOK = modIfChanged();
      if (modOK) {
        position = noteList.positionUsingListIndex (selectedRow);
        positionAndDisplay();
      }
    }
  }

  /**
   Respond when user selects a newNote from the tags tree.
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
      boolean modOK = modIfChanged();
      if (modOK) {
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
    }
    else {
      // Do nothing until an item is selected
    }
  }
  
  private void expandAllTags() {
    TreeNode root = (TreeNode) noteTree.getModel().getRoot();
    expandAll(noteTree, new TreePath(root));
  }

  private void expandAll(JTree tree, TreePath parent) {
    TreeNode node = (TreeNode) parent.getLastPathComponent();
    if (node.getChildCount() >= 0) {
      for (Enumeration e = node.children(); e.hasMoreElements();) {
        TreeNode n = (TreeNode) e.nextElement();
        TreePath path = parent.pathByAddingChild(n);
        expandAll(tree, path);
      }
    }
    tree.expandPath(parent);
    // tree.collapsePath(parent);
  }
  
  private void collapseAllTags() {
    TreeNode root = (TreeNode) noteTree.getModel().getRoot();
    TreePath rootPath = new TreePath(root);
    if (root.getChildCount() >= 0) {
      for (Enumeration e = root.children(); e.hasMoreElements();) {
        TreeNode n = (TreeNode) e.nextElement();
        TreePath path = rootPath.pathByAddingChild(n);
        collapseAll(noteTree, path);
      }
    }
  }
  
  private void collapseAll(JTree tree, TreePath parent) {
    TreeNode node = (TreeNode) parent.getLastPathComponent();
    if (node.getChildCount() >= 0) {
      for (Enumeration e = node.children(); e.hasMoreElements();) {
        TreeNode n = (TreeNode) e.nextElement();
        TreePath path = parent.pathByAddingChild(n);
        collapseAll(tree, path);
      }
    }
    tree.collapsePath(parent);
  }
  
  public void displayPrefsUpdated(DisplayPrefs displayPrefs) {
    if (position != null && displayTab != null) {
      buildDisplayTab();
    }
  }

  /**
   Populate both the Display and Edit tabs with data from the current note. 
  */
  public void displayNote () {
    Note note = position.getNote();
    if (note.hasDiskLocation()) {
      reload (note);
    }
    fileName = note.getFileName();
    
    buildDisplayTab();
    
    if (widgets != null && widgets.size() == noteIO.getNumberOfFields()) {
      for (int i = 0; i < noteIO.getNumberOfFields(); i++) {
        DataFieldDefinition fieldDef = noteIO.getRecDef().getDef(i);
        String fieldName = fieldDef.getProperName();
        DataWidget widget = widgets.get(i);
        if (fieldName.equalsIgnoreCase(NoteParms.TITLE_FIELD_NAME)) {
          widget.setText(note.getTitle());
          oldTitle = note.getTitle();
        }
        else
        if (fieldName.equalsIgnoreCase(NoteParms.LINK_FIELD_NAME)) {
          widget.setText(note.getLinkAsString());
        }
        else
        if (fieldName.equalsIgnoreCase(NoteParms.TAGS_FIELD_NAME)) {
          widget.setText(note.getTagsAsString());
        }
        else
        if (fieldName.equalsIgnoreCase(NoteParms.BODY_FIELD_NAME)) {
          widget.setText(note.getBody());
        } 
        else {
          DataField nextField = note.getField(i);
          widget.setText(nextField.getData());
        }

      } // end for each data field
    }
    
    lastModDateText.setText (note.getLastModDate(NoteParms.COMPLETE_FORMAT));
    statusBar.setPosition(position.getIndexForDisplay(), noteList.size());
    modified = false;
    if (currentFileSpec != null) {
      currentFileSpec.setLastTitle(note.getTitle());
    }
    
    if (note.hasInconsistentDiskLocation()) {
      Object[] options = {"Change file name to match title", "Leave it as is"};
      int response = JOptionPane.showOptionDialog(this, 
          "The Note's file name does not match its title", 
          "Title/File Name Mismatch", 
          JOptionPane.OK_CANCEL_OPTION, 
          JOptionPane.WARNING_MESSAGE, 
          null, 
          options, 
          options[0]);
      boolean fixDiskLocation = (response == 0);
      if (fixDiskLocation) {
        System.out.println ("OK, let's fix it!");
        saveNoteAndDeleteOnRename(note);
      }
    }
    
    noteList.fireTableRowsUpdated(position.getIndex(), position.getIndex());
    
  }
  
  /**
   Provide a static, non-editable display of the note on the display tab. 
  */
  private void buildDisplayTab() {
    Note note = position.getNote();
    
    displayTab.startDisplay();
    if (note.hasTags()) {
      displayTab.displayTags(note.getTags());
    }
    
    displayTab.displayTitle(note.getTitle());
    
    if (note.hasLink()) {
      displayTab.displayLink(
          NoteParms.LINK_FIELD_NAME, 
          "", 
          note.getLinkAsString());
    }
    
    if (widgets != null && widgets.size() == noteIO.getNumberOfFields()) {
      for (int i = 0; i < noteIO.getNumberOfFields(); i++) {
        DataFieldDefinition fieldDef = noteIO.getRecDef().getDef(i);
        String fieldName = fieldDef.getProperName();
        DataWidget widget = widgets.get(i);
        if (fieldName.equalsIgnoreCase(NoteParms.TITLE_FIELD_NAME)) {
          // Ignore -- already handled above
        }
        else
        if (fieldName.equalsIgnoreCase(NoteParms.LINK_FIELD_NAME)) {
          // Ignore -- already handled above
        }
        else
        if (fieldName.equalsIgnoreCase(NoteParms.TAGS_FIELD_NAME)) {
          // Ignore -- already handled above
        }
        else
        if (fieldName.equalsIgnoreCase(NoteParms.BODY_FIELD_NAME)) {
          // Ignore -- handled below
        } 
        else {
          DataField nextField = note.getField(i);
          displayTab.displayField(fieldName, nextField.getData());
        }

      } // end for each data field
    
      if (note.hasBody()) {
        displayTab.displayLabelOnly("Body");
        displayTab.displayBody(note.getBody());
      }

      displayTab.displayDivider();
    
    }
    
    displayTab.displayDateAdded(note.getLastModDateStandard());
    
    displayTab.finishDisplay();
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
   User has pressed the OK button to indicate that they are done editing. 
   */
  private void doneEditing() {
    if (position != null && displayTab != null) {
      boolean modOK = modIfChanged();
      if (modOK) {
        positionAndDisplay();
        activateDisplayTab();
      }
    }
  } 
  
  /**
    Changes the active tab to the tab displaying an individual item.
   */
  public void activateDisplayTab () {
    itemTabbedPane.setSelectedIndex (DISPLAY_TAB_INDEX);
    okButton.setEnabled(false);
  }
  
  /**
    Changes the active tab to the tab displaying an individual item.
   */
  public void activateItemTab () {
    itemTabbedPane.setSelectedIndex (CONTENT_TAB_INDEX);
    okButton.setEnabled(true);
  }
  
  /**
   To be called whenever the date is modified by DateWidget.
   */
  public void dateModified (String date) {
    modified = true;
  }
  
  /**
   Apply the recurrence rule to the date.
   
   @param date Date that will be incremented. 
   */
  public String recur (String date) {
    StringDate str = new StringDate();
    str.set(date);
    RecursValue recurs = new RecursValue(getRecurrenceRule());
    return recurs.recur(str);
  }
  
  /**
   Apply the recurrence rule to the date.
   
   @param date Date that will be incremented. 
   */
  public String recur (StringDate date) {
    RecursValue recurs = new RecursValue(getRecurrenceRule());
    return recurs.recur(date);
  }
  
  /**
   Provide a text string describing the recurrence rule, that can
   be used as a tool tip.
   */
  public String getRecurrenceRule() {
    String recurs = "";
    if (canRecur()) {
      if (recursWidget != null) {
        recurs = recursWidget.getText();
      }
      if (recurs.length() > 0) {
        if (position != null) {
          Note testNote = position.getNote();
          if (testNote != null) {
            recurs = testNote.getRecursAsString();
          } // end if we have a note to test
        } // end if we have a position that might contain a note
      } // end if we don't have any recurs data from the recurs widget
    } // end if this list has a recurs field
    return recurs;
  }
  
  /**
   Does this date have an associated rule for recurrence?
   */
  public boolean canRecur() {
    return recursIncluded;
  }
  

  


  /**
   Check to see if the user has changed anything and take appropriate
   actions if so.
   */
  public boolean modIfChanged () {
    
    boolean modOK = true;
    
    Note note = position.getNote();
    
    // Check each field for changes
    for (int i = 0; i < noteIO.getNumberOfFields(); i++) {
      DataFieldDefinition fieldDef = noteIO.getRecDef().getDef(i);
      String fieldName = fieldDef.getProperName();
      if (i < widgets.size()) {
        DataWidget widget = widgets.get(i);
        if (fieldName.equalsIgnoreCase(NoteParms.TITLE_FIELD_NAME)) {
          if (! note.equalsTitle (widget.getText())) {
            oldTitle = note.getTitle();
            note.setTitle (widget.getText());
            modified = true;
          }
        }
        else
        if (fieldName.equalsIgnoreCase(NoteParms.LINK_FIELD_NAME)) {
          if ((widget.getText().equals (note.getLinkAsString()))
              || ((widget.getText().length() == 0) && note.blankLink())) {
            // No change
          } else {
            note.setLink (widget.getText());
            modified = true;
          }
        }
        else
        if (fieldName.equalsIgnoreCase(NoteParms.TAGS_FIELD_NAME)) {
          if (! note.equalsTags (widget.getText())) {
            note.setTags (widget.getText());
            modified = true;
          }
        }
        else
        if (fieldName.equalsIgnoreCase(NoteParms.BODY_FIELD_NAME)) {
          if (! widget.getText().equals (note.getBody())) {
            note.setBody (widget.getText());
            modified = true;
          }
        } 
        else
        if (fieldName.equalsIgnoreCase(NoteParms.SEQ_FIELD_NAME)) {
          if (! widget.getText().equals (note.getSeq())) {
            note.setSeq (widget.getText());
            modified = true;
          }
        } 
        else
        if (fieldName.equalsIgnoreCase(NoteParms.STATUS_FIELD_NAME)) {
          ItemStatus statusValue = new ItemStatus(widget.getText());
          if (note.getStatus().compareTo(statusValue) != 0) {
            note.setStatus (widget.getText());
            modified = true;
          }
        } 
        else
        if (fieldName.equalsIgnoreCase(NoteParms.RECURS_FIELD_NAME)) {
          RecursValue recursValue = new RecursValue(widget.getText());
          if (note.getRecurs().compareTo(recursValue) != 0) {
            note.setRecurs (widget.getText());
            modified = true;
          }
        }  
        else
        if (fieldName.equalsIgnoreCase(NoteParms.DATE_FIELD_NAME)) {
          String newDate = widget.getText();
          if (note.getDateAsString().compareTo(newDate) != 0) {
            note.setDate(newDate);
            modified = true;
          }
        }
        else {
          DataField nextField = note.getField(i);
          if (! widget.getText().equals(nextField.getData())) {
            note.storeField(fieldName, widget.getText());
            modified = true;
          } // end if generic field has been changed
        } // end if generic field
      } // end if we have a widget
    } // end for each field
    
    // If entry has been modified, then let's update if we can
    if (modified) {
      
      // Got to have a title
      String newFileName = note.getFileName();
      if ((! note.hasTitle()) || note.getTitle().length() == 0) {
        Object[] options = {"OK, let me fix it", "Cancel and discard the Note"};
        int response = JOptionPane.showOptionDialog(this, 
            "The Note cannot be saved because the Title field has been left blank", 
            "Data Entry Error", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.WARNING_MESSAGE, 
            null, 
            options, 
            options[0]);
        modOK = (response == 1);
      } 
      else 
      // If we changed the title, then check to see if we have another 
      // Note by the same name
      if ((! newFileName.equals(fileName))
          && noteIO.exists(newFileName)
          && (! newFileName.equalsIgnoreCase(note.getDiskLocationBase()))) {
        trouble.report (this, 
            "A Note already exists with the same Title",
            "Duplicate Found");
        modOK = false;
      } else {
        // Modify newNote on disk
        note.setLastModDateToday();
        saveNoteAndDeleteOnRename(note);
        if (position.isNewNote()) {
          if (note.hasUniqueKey()) {
            addNoteToList ();
          } // end if we have newNote worth adding
        } else {
          noteList.modify(position);
        }
        noteList.fireTableDataChanged();
        
        if (editingMasterCollection) {
          masterCollection.modRecentFile(oldTitle, note.getTitle());
        }
      }
      oldSeq = "";
    } // end if modified
    
    return modOK;
  } // end modIfChanged method
  
  /**
   Save the note, and if it now has a new disk location, 
   delete the file at the old disk location. 
  
   @param note The note to be saved. 
  */
  private void saveNoteAndDeleteOnRename(Note note) {
    String oldDiskLocation = note.getDiskLocation();
    saveNote(note);
    String newDiskLocation = note.getDiskLocation();
    if (! newDiskLocation.equals(oldDiskLocation)) {
      File oldDiskFile = new File (oldDiskLocation);
      oldDiskFile.delete();
      if (folderSyncPrefs.getSync()) {
        File oldSyncFile = noteIO.getSyncFile(
            folderSyncPrefs.getSyncFolder(), 
            folderSyncPrefs.getSyncPrefix(), 
            oldTitle);
        oldSyncFile.delete();
      }
    }
  }
  
  /**
   Try to open the current newNote in the local app for the file type. 
  */
  private void openNote() {
    boolean modOK = modIfChanged();
    boolean ok = modOK;
    Note noteToOpen = null;
    File noteFileToOpen = null;
    String noteTitle = "** Unknown **";
    Desktop desktop = null;
    
    if (position == null) {
      ok = false;
    }
    
    if (ok) {
      noteToOpen = position.getNote();
      if (noteToOpen == null) {
        ok = false;
      } else {
        noteTitle = noteToOpen.getTitle();
      }
    }
    
    if (ok) {
      if (noteToOpen.hasDiskLocation()) {
        noteFileToOpen = new File(noteToOpen.getDiskLocation());
      } else {
        ok = false;
      }
    }
    
    if (ok) {
      if (! Desktop.isDesktopSupported()) {
        ok = false;
      }
    }
    
    if (ok) {
      desktop = Desktop.getDesktop();
      try {
        desktop.open(noteFileToOpen);
      } catch (IOException e) {
        ok = false;
      }
    }
    
    if (! ok) {
      Logger.getShared().recordEvent (LogEvent.MEDIUM,
        "Unable to open note for " + noteTitle,
        false);
    }
  }

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
  @Override
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
    displayGeneralPrefs ();
  }

  public void displayGeneralPrefs () {
    displayAuxiliaryWindow(generalPrefs);
  }
  
  public void displayCollectionPrefs () {
    displayAuxiliaryWindow(collectionPrefs);
  }

  public void setSplit (boolean splitPaneHorizontal) {
    int splitOrientation = JSplitPane.VERTICAL_SPLIT;
    if (splitPaneHorizontal) {
      splitOrientation = JSplitPane.HORIZONTAL_SPLIT;
    }
    mainSplitPane.setOrientation (splitOrientation);
  }

  private void savePrefs () {
    if (FileUtils.isGoodInputDirectory(noteFile)) {
      userPrefs.setPref (FavoritesPrefs.LAST_FILE, noteFile.toString());
    }
    userPrefs.setPref (FavoritesPrefs.PREFS_LEFT, this.getX());
    userPrefs.setPref (FavoritesPrefs.PREFS_TOP, this.getY());
    userPrefs.setPref (FavoritesPrefs.PREFS_WIDTH, this.getWidth());
    userPrefs.setPref (FavoritesPrefs.PREFS_HEIGHT, this.getHeight());
    
    savePreferredCollectionView();
    userPrefs.setPref (CommonPrefs.SPLIT_HORIZONTAL,
        mainSplitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT);
    generalPrefs.savePrefs();
    collectionPrefs.savePrefs();
    if (tips != null) {
      tips.savePrefs();
    }
    boolean prefsOK = userPrefs.savePrefs();
    masterCollection.savePrefs();
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
    boolean modOK = modIfChanged();
    if (modOK) {
      if (goodCollection(inFile)) {
        closeFile();
        openFile (inFile, "", true);
      }
    }
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

    boolean modOK = modIfChanged();
    if (modOK) {
      closeFile();
      savePrefs();
      System.exit(0);
    }
  }

  private void reloadFile() {
    boolean modOK = modIfChanged();
    if (modOK) {
      saveFile();
      NotePositioned savePosition = position;
      if (goodCollection(noteFile)) {
        openFile (noteFile, "", true);
        position = savePosition;
        positionAndDisplay();
      }
    }
  }
  
  private void reloadTaggedOnly() {
    boolean modOK = modIfChanged();
    if (modOK) {
      saveFile();
      NotePositioned savePosition = position;
      if (goodCollection(noteFile)) {
        openFile (noteFile, "", false);
        position = savePosition;
        positionAndDisplay();
      }
    }
  }

  /**
   Let the user choose a folder to open.
   */
  private void userOpenFile() {
    boolean modOK = modIfChanged();
    if (modOK) {
      fileChooser.setDialogTitle ("Open Notes Collection");
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      if (FileUtils.isGoodInputDirectory(noteFile)) {
        fileChooser.setCurrentDirectory (currentDirectory);
      }

      File selectedFile = null;
      selectedFile = fileChooser.showOpenDialog(this);
      if (selectedFile != null) {
        if (FileUtils.isGoodInputDirectory(selectedFile)) {
          closeFile();
          openFile (selectedFile, "", true);
        } else {
          trouble.report ("Trouble opening file " + selectedFile.toString(),
              "File Open Error");
        }
      } // end if user approved a file/folder choice
    }
  } // end method userOpenFile
  
  /**
   Open the Help Notes for Notenik. 
  */
  private void openHelpNotes() {

    boolean modOK = modIfChanged();
    if (modOK) {
      File appFolder = Home.getShared().getAppFolder();
      File helpFolder = new File (appFolder, "help");
      File helpNotes = new File (helpFolder, "notenik-intro");
      if (goodCollection(helpNotes)) {
        closeFile();
        openFile (helpNotes, "Help Notes", true);
        noteSortParm.setParm(NoteSortParm.SORT_BY_SEQ_AND_TITLE);
        firstNote();
      }
    }
  }
  
  private void openFileFromCurrentNote () {
    
    boolean modOK = modIfChanged();
    boolean ok = modOK;
    if (modOK) {
      Note note = null;
      
      if (position == null) {
        ok = false;
      }
      
      if (ok) {
        note = position.getNote();
        if (note == null) {
          ok = false;
        }
      }

      if (ok) {
        File fileToOpen = note.getLinkAsFile();
        if (goodCollection(fileToOpen)) {
          String collectionTitle = note.getTitle();
          closeFile();
          openFile(fileToOpen, collectionTitle, true);
        }
      }
    }
  }
  
  /**
   Check to see if the passed file seems to point to a valid 
   Collection folder. 
  
   @param fileToCheck The file to be checked. 
  
   @return false if file is null, doesn't exist, or isn't a directory,
           or can't be read, or can't be written.         
  */
  public boolean goodCollection(File fileToCheck) {
    return (fileToCheck != null
      && fileToCheck.exists()
      && fileToCheck.isDirectory()
      && fileToCheck.canRead()
      && fileToCheck.canWrite());
  }

  /**
   Open the specified collection and allow the user to view and edit it. 
  
   @param fileToOpen The folder containing the collection to be opened. 
   @param titleToDisplay Any special title to be used for the collection. 
   @param loadUnTagged Load notes without tags, or omit them? 
  */
  private void openFile (
      File fileToOpen, 
      String titleToDisplay, 
      boolean loadUnTagged) {
    
    // closeFile();
    
    if (masterCollection.hasMasterCollection()
        && fileToOpen.equals(masterCollection.getMasterCollectionFolder())) {
      launchButton.setText("Open");
      editingMasterCollection = true;
    } else {
      launchButton.setText("Launch");
      editingMasterCollection = false;
    }
    logNormal("Opening folder " + fileToOpen.toString());
    noteIO = new NoteIO(fileToOpen, NoteParms.NOTES_ONLY_TYPE);
    
    NoteParms templateParms = noteIO.checkForTemplate();
    if (templateParms != null) {
      noteIO = new NoteIO (fileToOpen, templateParms);
    }    
    
    initCollection();
    
    setNoteFile (fileToOpen);
    
    try {
      noteIO.load(noteList, loadUnTagged);
      if (folderSyncPrefs.getSync()) {
        syncWithFolder();
      }
    } catch (IOException e) {
      ioException(e);
    }
    buildNoteTabs();
    addFirstNoteIfListEmpty();
    // buildNoteTabs();
    noteList.fireTableDataChanged();
    if (fileToOpen != null && noteList != null) {
      noteSortParm.setParm(currentFileSpec.getNoteSortParm());
      noteList.sortParmChanged();
    }
    position = new NotePositioned (noteIO.getRecDef());
    setPreferredCollectionView();
    int index = -1;
    if (titleToDisplay != null && titleToDisplay.length() > 0) {
      Note noteToFind = new Note(noteList.getRecDef(), titleToDisplay);
      index = noteList.find(noteToFind);
      position = noteList.positionUsingListIndex(index);
    }
    if (index < 0) {
      position = noteList.first(position);
    }
    positionAndDisplay();
  }
  
  /**
   Purge closed or canceled items. 
  */
  private void purge() {
    
    boolean modOK = modIfChanged();
    if (modOK) {
      noFindInProgress();
      int purged = 0;

      String[] options = {
        "Cancel",
        "Purge and Discard",
        "Purge and Copy"};

      int purgeCancel = 0;
      int purgeDiscard = 1;
      int purgeCopy = 2;

      int option = JOptionPane.showOptionDialog(this,
          "Purge Closed Notes?",
          "Purge Options",
          JOptionPane.YES_NO_CANCEL_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          Home.getShared().getIcon(),
          options,
          options[purgeCopy]);

      File purgeTarget = null;
      String archiveFolderStr = currentFileSpec.getArchiveFolder();
      if (archiveFolderStr != null && archiveFolderStr.length() > 0) {
        File archiveFolder = new File(archiveFolderStr);
        if (archiveFolder.exists() 
            && archiveFolder.isDirectory() 
            && archiveFolder.canWrite()) {
          purgeTarget = archiveFolder;
          fileChooser.setCurrentDirectory(archiveFolder.getParentFile());
          fileChooser.setSelectedFile(archiveFolder);
        }
      }

      NoteIO purgeIO = null;

      if (option == purgeCopy) {
        fileChooser.setDialogTitle ("Select Folder to Hold Purged Notes");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        purgeTarget = fileChooser.showOpenDialog(this);
        if (purgeTarget == null) {
          option = purgeCancel;
        } else {
          if (goodCollection(purgeTarget)) {
            purgeIO = new NoteIO(purgeTarget, NoteParms.DEFINED_TYPE, noteIO.getRecDef());
          } else {
            purgeTarget = null;
            option = purgeCancel;
          }
        } // end if purge target folder not null
      } // end if option 1 was chosen

      if (option == purgeCopy || option == purgeDiscard) {
        Note workNote;
        int workIndex = 0;
        while (workIndex < noteList.size()) {
          workNote = noteList.get (workIndex);
          boolean deleted = false;
          if (workNote.getStatus().isDone()) {
            boolean okToDelete = true;  
            String fileToDelete = workNote.getDiskLocation();         
            if (option == purgeCopy) {
              try {
                purgeIO.save(purgeTarget, workNote, false);
              } catch (IOException e) {
                okToDelete = false;
                System.out.println("I/O Error while attempting to save " 
                    + workNote.getTitle() + " to Archive folder");
              }
            } // end of attempt to copy
            if (okToDelete) {
              deleted = noteList.remove (workNote);
              if (! deleted) {
                System.out.println("Unable to remove " 
                    + workNote.getTitle() + " from note list");
              }
              if (deleted) {
                deleted = new File(fileToDelete).delete();
              }
              if (! deleted) {
                trouble.report(
                    "Unable to delete note at " + fileToDelete, 
                    "Delete Failure");
              }
              if (folderSyncPrefs.getSync()) {
                File syncFile = noteIO.getSyncFile(
                    folderSyncPrefs.getSyncFolder(), 
                    folderSyncPrefs.getSyncPrefix(), 
                    workNote.getTitle());
                syncFile.delete();
              }
              noteList.fireTableDataChanged();
              if (deleted) {
                purged++;
              } 
            } // End if OK to Delete
          } // end if newNote is a candidate for deletion
          if (! deleted) {
            workIndex++;
          }
        } // end of list
      } // end if user chose to proceed with a purge

      if (purged > 0 && option == purgeCopy && purgeTarget != null) {
        currentFileSpec.setArchiveFolder(purgeTarget);
      }

      if (purged > 0) {
        openFile (noteFile, "", true);   
        position.setNavigatorToList (collectionTabbedPane.getSelectedIndex() == 0);
        position = noteList.first (position);
        positionAndDisplay();
      }

      String plural = StringUtils.pluralize("Note", purged);

      JOptionPane.showMessageDialog(this,
          String.valueOf(purged) + " " + plural + " purged successfully",
          "Purge Results",
          JOptionPane.INFORMATION_MESSAGE,
          Home.getShared().getIcon());
      Logger.getShared().recordEvent (LogEvent.NORMAL, String.valueOf(purged) 
          + " " + plural + " purged",
          false);
      statusBar.setStatus(String.valueOf(purged) + " Notes purged");
    }
  } // end of method purge
  
  /**
   This item is done. 
   
   We will either mark it as complete, or bump the date. 
  */
  private void closeNote() {
    Note note = position.getNote();
    if (note.hasRecurs() && note.hasDate()) {
      // Increment Date and leave status alone
      StringDate date = note.getDate();
      String newDate = note.getRecurs().recur(date);
      dateWidget.setText(newDate);
    }
    else
    if (statusIncluded) {
      // Change Status to Closed
      String closedStr = noteIO.getNoteParms().getItemStatusConfig().getClosedString();
      statusWidget.setText(closedStr);
      if (dateIncluded) {
        dateWidget.setText(StringDate.getTodayCommon());
      }
      // newNote.setStatus(ItemStatusConfig.getShared().getClosedString());
    }
    
    if (itemTabbedPane.getSelectedIndex() != CONTENT_TAB_INDEX) {
      
    }
    if (position != null 
        && displayTab != null
        && itemTabbedPane.getSelectedIndex() != CONTENT_TAB_INDEX) {
      modIfChanged();
      positionAndDisplay();
    }
  }
  
  /**
   Let's bump up the seq field for this Note, and all following
   notes until we stop creating duplicate seq fields. 
  */
  private void incrementSeq() {

    if (noteSortParm.getParm() != NoteSortParm.SORT_BY_SEQ_AND_TITLE) {
      JOptionPane.showMessageDialog(this,
          "First Sort by Seq + Title before Incrementing a Seq Value",
          "Sort Error",
          JOptionPane.WARNING_MESSAGE,
          Home.getShared().getIcon());
    } 
    else
    if (position == null
        || position.getNote() == null
        || position.getIndex() < 0) {
      JOptionPane.showMessageDialog(this,
          "First select a Note before Incrementing a Seq Value",
          "Selection Error",
          JOptionPane.WARNING_MESSAGE,
          Home.getShared().getIcon());
    } else {
      oldSeq = position.getNote().getSeq();
      String newSeq = noteList.incrementSeq(
          position, 
          noteIO, 
          folderSyncPrefs.getFolderSyncPrefsData());
      if (seqWidget != null) {
        seqWidget.setText(newSeq);
      }
      
    }
  }
  
  /**
   Build the user interface to view and update one Note. 
  */
  private void buildNoteTabs() {
    
    linkText = null;
    tagsTextSelector = null;
    statusWidget = null;
    dateWidget = null;
    recursWidget = null;
    statusIncluded = false;
    dateIncluded = false;
    recursIncluded = false;
    seqWidget = null;
    seqIncluded = false;
    JPanel notePanel = new JPanel();
    notePanel.setLayout(new GridBagLayout());
    widgets = new ArrayList();
    GridBagger gb = new GridBagger();
		gb.startLayout (notePanel, 2, 99);
		gb.setAllInsets (4);
		gb.setDefaultRowWeight (0.0);
    WidgetWithLabel widgetWithLabel = new WidgetWithLabel();
    for (int i = 0; i < noteIO.getNumberOfFields(); i++) {
      DataFieldDefinition fieldDef = noteIO.getRecDef().getDef(i);
      widgetWithLabel = noteIO.getNoteParms().getWidgetWithLabel(fieldDef, this, gb); 
      switch (fieldDef.getType()) {
        // Special processing for Tags
        case (DataFieldDefinition.TAGS_TYPE):
          tagsTextSelector = (TextSelector)widgetWithLabel.getWidget();
          tagsTextSelector.setValueList(noteList.getTagsList());
          tagsTextSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
              tagsActionPerformed(evt);
            }
          });
          break;
        case (DataFieldDefinition.LINK_TYPE):
          linkText = widgetWithLabel.getWidget();
          linkLabel = (LinkLabel)widgetWithLabel.getLabel();
          linkLabel.setLinkTweaker(linkTweaker);
          break;
        case (DataFieldDefinition.STATUS_TYPE):
          statusWidget = widgetWithLabel.getWidget();
          statusIncluded = true;
          break;
        case (DataFieldDefinition.DATE_TYPE):
          dateWidget = (DateWidget)widgetWithLabel.getWidget();
          dateIncluded = true;
          break;
        case (DataFieldDefinition.RECURS_TYPE):
          recursIncluded = true;
          recursWidget = widgetWithLabel.getWidget();
          break;
        case (DataFieldDefinition.SEQ_TYPE):
          seqWidget = widgetWithLabel.getWidget();
          seqIncluded = true;
          break;
      }
      widgets.add(widgetWithLabel.getWidget());

    } // end for each data field
    
    if (dateIncluded) {
      dateWidget.setOwner(this);
      dateWidget.setFrame(this);
    }
    
    lastModDateLabel = new javax.swing.JLabel();
    lastModDateText = new javax.swing.JLabel();
    lastModDateLabel.setLabelFor(lastModDateText);
    lastModDateLabel.setText("Mod Date:");
    lastModDateText.setText("  ");
    NoteParms.setDefaultLabelConstraints(gb);
    gb.add(lastModDateLabel);
    gb.add(lastModDateText);
    
    itemTabbedPane.removeAll();
    itemTabbedPane.add("Display", displayTab);
    itemTabbedPane.add("Edit", notePanel);
    // mainSplitPane.setRightComponent(notePanel);
    // mainSplitPane.setResizeWeight(0.5);
    
    if (tagsTextSelector != null) {
      tagsTextSelector.setValueList(noteList.getTagsList());
    }

    purgeMenuItem.setEnabled(statusIncluded);
    closeNoteMenuItem.setEnabled(statusIncluded 
        || (dateIncluded && recursIncluded));
   
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

  /** 
   Initialize a new collection to be created, or to be opened. 
  */
  private void initCollection () {
    
    // initRecDef();
    noteList = new NoteList(noteIO.getRecDef());
    noteSortParm.resetToDefaults();
    noteList.setSortParm(noteSortParm);
    position = new NotePositioned(noteIO.getRecDef());
    noteTable.setModel(noteList);
    noteList.setTable(noteTable);
    noteTree.setModel (noteList.getTagsModel().getModel());
    noteTree.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);
    treeCellRenderer = new TagTreeCellRenderer ();
    noteTree.setCellRenderer (treeCellRenderer);
    noteTree.doLayout();
    generalPrefs.getTagsPrefs().setTagsValueList(noteList.getTagsList());
    // setUnsavedChanges(false);
  }
  
  /*
  private void initRecDef() {
    dict = new DataDictionary();
    recDef = new RecordDefinition(dict);
    recDef.addColumn(NoteParms.TITLE_DEF);
    recDef.addColumn(NoteParms.TAGS_DEF);
    recDef.addColumn(NoteParms.LINK_DEF);
    recDef.addColumn(NoteParms.BODY_DEF);
  } */
  
  /**
   Sync the list with a Notational Velocity style folder. 
  
   @return True if everything went OK. 
  */
  public boolean syncWithFolder () {
    
    boolean ok = true;
    StringBuilder msgs = new StringBuilder();
    
    String syncFolderString = folderSyncPrefs.getSyncFolder();
    String syncPrefix = folderSyncPrefs.getSyncPrefix();
    boolean sync = folderSyncPrefs.getSync();
    File syncFolder = null;
    
    // Check to see if we have the info we need to do a sync
    if (syncFolderString == null
        || syncFolderString.length() == 0
        || syncPrefix == null
        || syncPrefix.length() == 0
        || (! sync)) {
      ok = false;
    }
    
    if (ok) {
      syncFolder = new File (syncFolderString);
      if (goodCollection(syncFolder)) {
      } else {
        Trouble.getShared().report(
            this, 
            "Trouble reading from folder: " + syncFolder.toString(), 
            "Problem with Sync Folder");
        ok = false;
      }
    }
    
    int synced = 0;
    int added = 0;
    int addedToSyncFolder = 0;
    
    if (ok) {  
      
      // Now go through the items on the list and mark them all as unsynced
      Note workNote;
      for (int workIndex = 0; workIndex < noteList.size(); workIndex++) {
        workNote = noteList.get (workIndex);
        workNote.setSynced(false);
      }

      // Now match directory entries in the folder with items on the list
      DirectoryReader directoryReader = new DirectoryReader (syncFolder);
      directoryReader.setLog (Logger.getShared());
      try {
        directoryReader.openForInput();
        while (! directoryReader.isAtEnd()) {
          File nextFile = directoryReader.nextFileIn();
          FileName nextFileName = new FileName(nextFile);
          if ((nextFile != null) 
              && (! nextFile.getName().startsWith ("."))
              && nextFile.exists()
              && NoteIO.isInterestedIn(nextFile)
              && nextFile.getName().startsWith(syncPrefix)
              && nextFileName.getBase().length() > syncPrefix.length()) {
            String fileNameBase = nextFileName.getBase();
            String nextTitle 
                = fileNameBase.substring(syncPrefix.length()).trim();
            int i = 0;
            boolean found = false;
            while (i < noteList.size() && (! found)) {
              workNote = noteList.get(i);
              found = (workNote.getTitle().equals(nextTitle));
              if (found) {
                workNote.setSynced(true);
                Date lastModDate = new Date (nextFile.lastModified());
                if (lastModDate.compareTo(workNote.getLastModDate()) > 0) {
                  Note syncNote = noteIO.getNote(nextFile, syncPrefix);
                  msgs.append(
                      "Note updated to match more recent info from sync folder for "
                      + syncNote.getTitle()
                      + "\n");
                  workNote.setTags(syncNote.getTagsAsString());
                  workNote.setLink(syncNote.getLinkAsString());
                  workNote.setBody(syncNote.getBody());
                  noteIO.save(syncNote, true);
                }
                synced++;
              } else {
                i++;
              }
            } // end while looking for a matching newNote
            if ((! found)) {
              // Add new nvAlt newNote to Notenik collection
              Note syncNote = noteIO.getNote(nextFile, syncPrefix);
              syncNote.setLastModDateToday();
              try {
                noteIO.save(syncNote, true);
                position = noteList.add (syncNote);
              } catch (IOException e) {
                ioException(e);
              }
            }
          } // end if file exists, can be read, etc.
        } // end while more files in sync folder
        directoryReader.close();
      } catch (IOException ioe) {
        Trouble.getShared().report(this, 
            "Trouble reading sync folder: " + syncFolder.toString(), 
            "Sync Folder access problems");
        ok = false;
      } // end if caught I/O Error
    }
      
    if (ok) {
      msgs.append(String.valueOf(added) + " "
          + StringUtils.pluralize("item", added)
          + " added\n");
      
      msgs.append(String.valueOf(synced)  + " existing "
          + StringUtils.pluralize("item", synced)
          + " synced\n");
      
      // Now add any unsynced notes to the sync folder
      Note workNote;
      for (int workIndex = 0; workIndex < noteList.size(); workIndex++) {
        workNote = noteList.get(workIndex);
        if (! workNote.isSynced()) {
          workNote.setLastModDateToday();
          saveNote(workNote);
          msgs.append("Added to Sync Folder " + workNote.getTitle() + "\n");
          addedToSyncFolder++;
        }
      } // end of list of notes
      msgs.append(String.valueOf(addedToSyncFolder) + " "
          + StringUtils.pluralize("note", addedToSyncFolder)
          + " added to sync folder\n");
      msgs.append("Folder Sync Completed!\n");
    }
    
    if (ok) {
      logger.recordEvent (LogEvent.NORMAL,
        msgs.toString(),
        false);
    }
    return ok;
      
  }

  private void importFile () {

    fileChooser.setDialogTitle ("Import Notes");
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if (FileUtils.isGoodInputDirectory(currentDirectory)) {
      fileChooser.setCurrentDirectory (currentDirectory);
    }
    File selectedFile = fileChooser.showOpenDialog(this);
    if (selectedFile != null) {
      File importFile = selectedFile;
      currentDirectory = importFile;
      NoteIO importer = new NoteIO (
          importFile, 
          NoteParms.DEFINED_TYPE, 
          noteIO.getRecDef());
      try {
        importer.load(noteList, true);
      } catch (IOException e) {
        ioException(e);
      }
      // setUnsavedChanges(true);
    }
    noteList.fireTableDataChanged();
    firstNote();
  }
  
  private void importXMLFile () {

    fileChooser.setDialogTitle ("Import Notes from XML");
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if (FileUtils.isGoodInputDirectory(currentDirectory)) {
      fileChooser.setCurrentDirectory (currentDirectory);
    }
    File selectedFile = fileChooser.showOpenDialog(this);
    if (selectedFile != null) {
      File importFile = selectedFile;
      NoteImportXML importer = new NoteImportXML(this);
      importer.parse(importFile, noteList);
    }
    noteList.fireTableDataChanged();
    firstNote();
  }
  
  private void importTabDelimited() {
    fileChooser.setDialogTitle("Import Notes from a Tab-Delimited File");
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if (FileUtils.isGoodInputDirectory(currentDirectory)) {
      fileChooser.setCurrentDirectory(currentDirectory);
    }
    File selectedFile = fileChooser.showOpenDialog(this);
    if (selectedFile != null) {
      NoteImportTabDelim importer = new NoteImportTabDelim(this);
      importer.parse(selectedFile, noteList);
    }
    noteList.fireTableDataChanged();
    firstNote();
  }
  
  /**
   Import info about Mac applications. 
  */
  private void importMacAppInfo() {
    
    boolean modOK = modIfChanged();

		if (modOK) {
      fileChooser.setDialogTitle("Import Info about Mac Applications");
      File top = new File ("/");
      fileChooser.setCurrentDirectory (top);
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      File selectedFile = fileChooser.showOpenDialog(this);
      if (selectedFile != null) {
        TextMergeInputMacApps macApps = new TextMergeInputMacApps();
        RecordDefinition recDef = noteList.getRecDef();
        File[] filesInArray = selectedFile.listFiles();
        ArrayList<File> files = new ArrayList<File>();
        for (File fileInArray:filesInArray) {
          files.add(fileInArray);
        }
        int fileIndex = 0;
        while (fileIndex < files.size()) {
          File file = files.get(fileIndex);
          if (macApps.isInterestedIn(file)) {
            TextMergeMacAppReader appReader = new TextMergeMacAppReader(file);
            appReader.retrieveMacAppInfo();
            String appName = appReader.getAppName();
            String fileLink = appReader.getFileLink();
            String tags = appReader.getTags();
            String lastModDate = appReader.getLastModDate();
            String version = appReader.getVersion();
            String minSysVersion = appReader.getMinSysVersion();
            String copyright = appReader.getCopyright();
            addOrUpdateAppInfo(file, appName, fileLink, tags, lastModDate, 
                version, minSysVersion, copyright, true);
          } // end if this seems to be a genuine Mac app
          else
          if (file.isDirectory()
              && (! file.isHidden())
              && (file.canRead())) {
            File[] moreFiles = file.listFiles();
            for (File nestedFile:moreFiles) {
              files.add(nestedFile);
            }
          } // end if a sub-directory
          else
          if (file.getName().endsWith(".jar")) {
            FileName fileName = new FileName(file);
            Date lastMod = new Date (file.lastModified());
            String lastModDate = dateFormatter.format (lastMod);
            addOrUpdateAppInfo(file, fileName.getBase(), fileName.getURLString(),
                "", lastModDate, "", "", "", false);
          }
          fileIndex++;
        } // end for each file in the directory
      } // end if user specified a valid directory
      noteList.fireTableDataChanged();
      firstNote();
    }
  }
  
  private void addOrUpdateAppInfo(
      File file,
      String appName,
      String fileLink,
      String tags,
      String lastModDate,
      String version,
      String minSysVersion,
      String copyright,
      boolean macApp) {

    RecordDefinition recDef = noteList.getRecDef();
    Note appNote = new Note(noteList.getRecDef(), appName);
    StringBuilder body = new StringBuilder();
    int ix = noteList.find(appNote);
    if (ix < 0) {
      // Not found -- add it
      if (fileLink != null && fileLink.length() > 0
          && recDef.contains(NoteParms.LINK_FIELD_NAME)) {
        appNote.setLink(fileLink);
      }
      
      if (tags != null && tags.length() > 0
          && recDef.contains(NoteParms.TAGS_FIELD_NAME)) {
        appNote.setTags(tags);
      }
      
      if (lastModDate != null && lastModDate.length() > 0
          && recDef.contains(NoteParms.DATE_FIELD_NAME)) {
        appNote.setDate(lastModDate);
      } else {
        body.append(
            "Date Last Modified: " + 
            lastModDate + "  "
            + GlobalConstants.LINE_FEED_STRING);
      }
      
      if (version != null && version.length() > 0) {
        if (recDef.contains(NoteParms.SEQ_FIELD_NAME)) {
          appNote.setSeq(version);
        } else {
          body.append(
              "Version: " +
              version + "  " +
              GlobalConstants.LINE_FEED_STRING);
        }
      }
      
      if (minSysVersion != null && minSysVersion.length() > 0) {
        if (recDef.contains(NoteParms.MIN_SYS_VERSION_FIELD_NAME)) {
          appNote.setField(NoteParms.MIN_SYS_VERSION_FIELD_NAME, minSysVersion);
        } else {
          body.append(
              "Minimum System Version: " +
              minSysVersion + "  " +
              GlobalConstants.LINE_FEED_STRING);
        }
      }

      if (copyright != null && copyright.length() > 0) {
        if (recDef.getColumnNumber(NoteParms.BODY_FIELD_NAME) >= 0) {
          body.append("Copyright: " + copyright);
          appNote.setBody(body.toString());
        }
      }

      addImportedNote(appNote);

    } 
    else {
      // Found in table -- update where it makes sense
      appNote = noteList.get(ix);
      if (appNote.isLinkToMacApp() && (! macApp)) {
        // Don't replace a real app entry with a jar file
      } else {
        if (fileLink != null && fileLink.length() > 0
            && recDef.contains(NoteParms.LINK_FIELD_NAME)) {
          appNote.setLink(fileLink);
        }

        if (lastModDate != null && lastModDate.length() > 0
            && recDef.contains(NoteParms.DATE_FIELD_NAME)) {
          appNote.setDate(lastModDate);
        } 

        if (version != null && version.length() > 0) {
          if (recDef.contains(NoteParms.SEQ_FIELD_NAME)) {
            appNote.setSeq(version);
          } 
        }

        if (minSysVersion != null && minSysVersion.length() > 0) {
          if (recDef.contains(NoteParms.MIN_SYS_VERSION_FIELD_NAME)) {
            appNote.setField(NoteParms.MIN_SYS_VERSION_FIELD_NAME, minSysVersion);
          } 
        }

        if (copyright != null && copyright.length() > 0) {
          if (recDef.getColumnNumber(NoteParms.BODY_FIELD_NAME) >= 0) {
            body.append("Copyright: " + copyright);
            appNote.setBody(body.toString());
          }
        }
      } // end if not replacing a real app with a jar file
    } // end if app note already exists
    
  }
  
  private boolean addImportedNote(Note importNote) {
    boolean added = false;
    if ((! importNote.hasTitle()) 
        || importNote.getTitle().length() == 0
        || (! importNote.hasUniqueKey())) {
      // do nothing
    } else {
      importNote.setLastModDateToday();
      saveNote(importNote);
      noteList.add (importNote);
      noteList.fireTableDataChanged();
      added = true;
    }
    return added;
  }
  
  private void ioException(IOException e) {
    Trouble.getShared().report("I/O Exception", "Trouble");
  }

  /**
   Close the current notes collection in an orderly fashion. 
  */
  private void closeFile() {
    if (this.noteFile != null) {
      publishWindow.closeSource();
      if (currentFileSpec != null) {
        currentFileSpec.setNoteSortParm(noteSortParm.getParm());
      }
      filePrefs.handleClose();
    }
  }

  private void saveFile () {
    savePreferredCollectionView();
    if (noteFile == null) {
      userSaveFileAs();
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
      Note nextNote = noteList.get(i);
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
  private void userSaveFileAs () {
    boolean modOK = modIfChanged();
    if (modOK) {
      fileChooser.setDialogTitle ("Save Notes to File");
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      if (FileUtils.isGoodInputDirectory(currentDirectory)) {
        fileChooser.setCurrentDirectory (currentDirectory);
        fileChooser.setSelectedFile (currentDirectory);
      }
      File selectedFile = fileChooser.showSaveDialog (this);
      if(goodCollection(selectedFile)) {
        File chosenFile = selectedFile;
        saveFileAs(chosenFile);
      }
    }
  }
  
  /**
   Allow the user to create a new collection.
  */
  public void userNewFile() {
    boolean modOK = modIfChanged();
    if (modOK) {
      fileChooser.setDialogTitle ("Select Folder for New Note Collection");
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      if (FileUtils.isGoodInputDirectory(currentDirectory)) {
        fileChooser.setCurrentDirectory (currentDirectory);
        fileChooser.setSelectedFile (currentDirectory);
      }
      File selectedFile = fileChooser.showSaveDialog (this);
      if (selectedFile != null) {
        if (goodCollection(selectedFile)) {
          closeFile();
          openFile(selectedFile, "", true);
        } else {
          trouble.report ("Trouble opening new file " + selectedFile.toString(),
              "New File Open Error");
        }
      } // end if user selected a file
    } // end if mods ok
  } // end method userNewFile
  
  private void createMasterCollection() {
    fileChooser.setDialogTitle ("Create Master Collection");
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if (FileUtils.isGoodInputDirectory(currentDirectory)) {
      fileChooser.setCurrentDirectory (currentDirectory);
      fileChooser.setSelectedFile (currentDirectory);
    }
    File selectedFile = fileChooser.showSaveDialog (this);
    if(selectedFile != null) {
      int filesSaved = masterCollection.createMasterCollection(selectedFile);
      if (filesSaved > 0) {
        JOptionPane.showMessageDialog(this,
            String.valueOf(filesSaved) + " Recent Files successfully saved to "
                + GlobalConstants.LINE_FEED
                + selectedFile.toString(),
            "Master Collection Creation Results",
            JOptionPane.INFORMATION_MESSAGE,
            Home.getShared().getIcon());
        createMasterCollectionMenuItem.setEnabled(false);
        openMasterCollectionMenuItem.setEnabled(true);
      } // End if we saved any recent files
    } // End if user selected a file
  } // end method createMasterCollection
  
  private void openMasterCollection() {
    boolean modOK = modIfChanged();
    if (modOK) {
      if (masterCollection.hasMasterCollection()) {
        File selectedFile = masterCollection.getMasterCollectionFolder();
        if (goodCollection(selectedFile)) {
          closeFile();
          openFile (selectedFile, "", true);
        } else {
          trouble.report ("Trouble opening file " + selectedFile.toString(),
              "File Open Error");
        }
      } // end if we have a master collection
    } // end if mod ok
  }
  
  public void openEssentialCollection() {
    boolean modOK = modIfChanged();
    if (modOK) {
      if (filePrefs.hasEssentialFilePath()) {
        File selectedFile = new File(filePrefs.getEssentialFilePath());
        if (goodCollection(selectedFile)) {
          closeFile();
          openFile (selectedFile, "", true);
        } else {
          trouble.report ("Trouble opening file " + selectedFile.toString(),
              "File Open Error");
        }
      } // end if we have an essential file path
    } // end if mod ok
  }
  
  /**
   Generate a template file containing all supported note fields. 
  */
  public void generateTemplate() {
 
    fileChooser.setDialogTitle ("Select Folder for Note Template");
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if (FileUtils.isGoodInputDirectory(currentDirectory)) {
      fileChooser.setCurrentDirectory (currentDirectory);
      fileChooser.setSelectedFile (currentDirectory);
    }
    if (FileUtils.isGoodInputDirectory(noteFile)) {
      fileChooser.setSelectedFile(noteFile);
    }
    File selectedFile = fileChooser.showSaveDialog (this);
    if(selectedFile != null) {
      NoteParms templateParms = new NoteParms(NoteParms.NOTES_EXPANDED_TYPE);
      RecordDefinition recDef = templateParms.getRecDef();
      Note templateNote = new Note(recDef);
      templateNote.setTitle("The unique title for this note");
      templateNote.setTags("One or more tags, separated by commas");
      templateNote.setLink("http://anyurl.com");
      templateNote.setStatus("One of a number of states");
      templateNote.setType("The type of note");
      templateNote.setSeq("Rev Letter or Version Number");
      StringDate today = new StringDate();
      today.set(StringDate.getTodayYMD());
      templateNote.setDate(today);
      templateNote.setRecurs("Every Week");
      templateNote.setAuthor("The Author of the Note");
      templateNote.setRating("5");
      templateNote.setIndex("Index Term");
      templateNote.setTeaser
        ("A brief sample of the note that will make people want to read more");
      templateNote.setBody("The body of the note");
      File templateFile = new File(selectedFile, "template.txt");
      NoteIO templateIO = new NoteIO(selectedFile);
      templateIO.save(templateNote, templateFile, true); 
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
      if (noteIO == null) {
        noteIO = new NoteIO(file);
      } else {
        noteIO.setHomeFolder(file);
      }
      exporter = new NoteExport(this);
      if (noteList != null) {
        noteList.setSource (file);
        noteList.setTitle(noteList.getSource().getName());
      }
      currentFileSpec = masterCollection.addRecentFile (file);
      currentDirectory = file;
      userPrefs.setPref (FavoritesPrefs.LAST_FILE, file.toString());
      FileName fileName = new FileName (file);
      statusBar.setFileName(fileName);
      publishWindow.openSource(currentDirectory);
      reports.setDataFolder(file);
    }

    collectionPrefs.setCollection(currentFileSpec);
  }

  public void displayPublishWindow() {
    displayAuxiliaryWindow(publishWindow);
  }
  
  /**
   If the link points to a file on a file share, then display info 
   about the file. 
  */
  private void displayFileInfo() {
    boolean displayed = false;
    if (position != null) {
      Note note = position.getNote();
      if (note != null) {
        if (note.hasLink()) {
          String link = note.getLinkAsString();
          if (link.startsWith("file:")) {
            fileInfoWindow.setFile(link);
            displayAuxiliaryWindow(fileInfoWindow);
            displayed = true;
          } // end if we have a file
        } // end if we have a link
      } // end if we have a newNote
    } // end if we have a position
    if (! displayed) {
      trouble.report ("No file to display",
          "No File Specified");
    }
  } // end method

  public void displayAuxiliaryWindow(WindowToManage window) {
    window.setLocation(
        this.getX() + 60,
        this.getY() + 60);
    WindowMenuManager.getShared().makeVisible(window);
    window.toFront();
  }
  
  /**
   Method for callback while executing a PSTextMerge script. 
  
   @param operand 
  */
  public void scriptCallback (String operand) {
    pubOperation(reports.getReportsFolder(), operand);
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
    //    generalPrefs.getFavoritesPrefs().getFavoritesTags());

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
    Logger.getShared().recordEvent(LogEvent.NORMAL, "Publishing Favorites", false);
    favoritesWritten = false;
    if (! noteFile.getName().equalsIgnoreCase (FAVORITES_FILE_NAME)) {
      favoritesWritten = exporter.publishFavorites
          (publishTo, noteList, generalPrefs.getFavoritesPrefs());
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
        FileSpec fileSpec = masterCollection.getFileSpec(0);
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
    StringBuilder fileNameWithoutDate = new StringBuilder();
    try {
      backupPath.append(folderForBackups.getCanonicalPath());
    } catch (IOException e) {
      backupPath.append(folderForBackups.getAbsolutePath());
    }
    backupPath.append(File.separator);
    String noteFileName = noteFile.getName();
    if (noteFileName.equalsIgnoreCase("notes")) {
      backupPath.append(noteFile.getParentFile().getName());
      backupPath.append(" ");
      fileNameWithoutDate.append(noteFile.getParentFile().getName());
      fileNameWithoutDate.append(" ");
    }
    backupPath.append(noteFile.getName());
    fileNameWithoutDate.append(noteFile.getName());
    backupPath.append(" ");
    fileNameWithoutDate.append(" ");
    backupPath.append("backup ");
    fileNameWithoutDate.append("backup ");
    backupPath.append(filePrefs.getBackupDate());
    File backupFolder = new File (backupPath.toString());
    backupFolder.mkdir();
    boolean backedUp = FileUtils.copyFolder (noteFile, backupFolder);
    if (backedUp) {
      FileSpec fileSpec = masterCollection.getFileSpec(0);
      filePrefs.saveLastBackupDate
          (fileSpec, masterCollection.getPrefsQualifier(), 0);
      logger.recordEvent (LogEvent.NORMAL,
          "Notes backed up to " + backupFolder.toString(),
            false);
      filePrefs.pruneBackups(folderForBackups, fileNameWithoutDate.toString());
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
      FileSpec fileSpec = masterCollection.getFileSpec(0);
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
  
  private void invokeLinkTweaker() {
    displayAuxiliaryWindow(linkTweaker);
  }
  
  /**
   Get the current link so that it can be tweaked. 
  
   @return The Link to be tweaked. 
  */
  public String getLinkToTweak() {
    if (linkText == null) {
      return "";
    } else {
      return linkText.getText();
    }
  }
  
  /**
   Set a link field to a new value after it has been tweaked. 
  
   @param tweakedLink The link after it has been tweaked. 
   @param linkID      A string identifying the link, in case there are more
                      than one. This would be the text used in the label
                      for the link. 
  */
  public void putTweakedLink (String tweakedLink, String linkID) {
    if (linkText != null
        && tweakedLink.length() > 0) {
      linkText.setText(tweakedLink);
    }
  }
  
  public void setLink(File file) {
    if (linkText != null && file != null) {
      linkText.setText(StringUtils.tweakAnyLink(file.getAbsolutePath(), 
          false, false, false, ""));
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

    boolean modOK = modIfChanged();

    if (modOK) {

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
    }
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
  
  private void generalExport(int exportType) {
    boolean modOK = modIfChanged();
    if (modOK) {
      
      boolean ok = true;
      int exported = 0;
      
      String selectTagsStr 
        = generalPrefs.getTagsPrefs().getSelectTagsAsString();
      String suppressTagsStr 
        = generalPrefs.getTagsPrefs().getSuppressTagsAsString();
      
      File selectedFile;
      fileChooser.setDialogTitle ("Export to " 
          + NoteExport.EXPORT_TYPE[exportType]);
      switch (exportType) {

        case NoteExport.NOTENIK_EXPORT:
          fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
          selectedFile = fileChooser.showSaveDialog(this);
          if (selectedFile == null) {
            // this condition will be handled later
          }
          else
          if (selectedFile.isFile()
              || (! selectedFile.canWrite())) {
            ok = false;
            noValidExportDestination();
          } 
          break;

        case NoteExport.OPML_EXPORT:
          fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
          fileChooser.setCurrentDirectory(noteFile.getParentFile());
          fileChooser.setSelectedFile
            (new File(noteFile.getParentFile(), noteFile.getName() + ".opml"));
          selectedFile = fileChooser.showSaveDialog(this);
          break;
        
        case NoteExport.XML_EXPORT:
          fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
          fileChooser.setCurrentDirectory(noteFile.getParentFile());
          fileChooser.setSelectedFile
            (new File(noteFile.getParentFile(), noteFile.getName() + ".xml"));
          selectedFile = fileChooser.showSaveDialog(this);
          break;
          
        case NoteExport.TABDELIM_EXPORT_MS_LINKS:
          fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
          fileChooser.setCurrentDirectory(noteFile.getParentFile());
          fileChooser.setSelectedFile
            (new File(noteFile.getParentFile(), noteFile.getName() + ".txt"));
          selectedFile = fileChooser.showSaveDialog(this);
          break;

        case NoteExport.TABDELIM_EXPORT:
        default:
          fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
          fileChooser.setCurrentDirectory(noteFile.getParentFile());
          fileChooser.setSelectedFile
            (new File(noteFile.getParentFile(), noteFile.getName() + ".txt"));
          selectedFile = fileChooser.showSaveDialog(this);
      } // end switch for fileChooser setup
      
      if (selectedFile == null) {
        ok = false;
        noValidExportDestination();
      } 
      if (ok) {
        if (exportType == NoteExport.OPML_EXPORT) {
          exported = 
            exporter.OPMLExport(
              selectedFile,
              noteList);
        } else {
          exported = 
            exporter.generalExport(
              selectedFile,
              noteFile,
              noteIO.getRecDef(),
              noteList,
              exportType, 
              selectTagsStr, 
              suppressTagsStr);
        }

        if (ok && exported >= 0) {
          JOptionPane.showMessageDialog(this,
              String.valueOf(exported) + " Notes exported successfully to"
                  + GlobalConstants.LINE_FEED
                  + selectedFile.toString(),
              "Export Results",
              JOptionPane.INFORMATION_MESSAGE,
              Home.getShared().getIcon());
          Logger.getShared().recordEvent (LogEvent.NORMAL, String.valueOf(exported) 
              + " Notes exported to " 
              + selectedFile.toString(),
              false);
          statusBar.setStatus(String.valueOf(exported) + " Notes exported");
        } else {
          Logger.getShared().recordEvent (LogEvent.MEDIUM,
            "Problem exporting Notes to " + selectedFile.toString(),
              false);
          Trouble.getShared().report ("I/O error attempting to export notes to " 
                + selectedFile.toString(),
              "I/O Error");
          statusBar.setStatus("Export problems");
        }
      } // end if prepared to attempt export
    } // end if last mod ok
  } // end method generalExport
  
  private void noValidExportDestination() {
    Trouble.getShared().report ("No valid export destination specified",
        "Export Aborted");
  }
  
  /**
   Copy the current newNote to the system clipboard. 
   */
  private void copyNote() {
    boolean noNoteSelected = true;
    if (position != null) {
      Note note = position.getNote();
      if (note != null) {
        noNoteSelected = false;
        TextLineWriter writer = new ClipboardMaker();
        noteIO.save(note, writer);
      }
    }
    
    if (noNoteSelected) {
      trouble.report ("Select a Note before trying to copy it", 
          "No Note Selected");
    } 
  }
  
  /**
   Paste note from clipboard. 
  */
  private void pasteNote() {

    boolean ok = false;
    boolean modOK = modIfChanged();
    Note newNote = null;
    if (modOK) {
      ok = true;
      String noteText = "";
      TextLineReader reader = new ClipboardReader();
      newNote = noteIO.getNote(reader);
      if (newNote == null 
          || (! newNote.hasTitle()) 
          || (! newNote.hasUniqueKey())
          || (newNote.getTitle().length() == 0)) {
        ok = false;
      }
    }
    
    if (ok) {
      String newFileName = newNote.getFileName();
        if (noteIO.exists(newFileName)) {
          trouble.report (this, 
            "A Note already exists with the same What field",
            "Duplicate Found");
        ok = false;
      }
    }
    
    if (ok) {
      newNote.setLastModDateToday();
      position = new NotePositioned(noteIO.getRecDef());
      position.setIndex (noteList.size());
      position.setNote(newNote);
      position.setNewNote(true);
      fileName = "";
      displayNote();
      saveNote(newNote);
      addNoteToList ();
    }
    
    if (! ok) {
      trouble.report ("Trouble pasting new note from Clipboard",
          "Clipboard Error");
    }
    
  }
  
  public void genHTMLtoClipboard() {
    boolean noNoteSelected = true;
    boolean ok = true;
    Note note = null;
    if (position != null) {
      note = position.getNote();
      if (note != null) {
        noNoteSelected = false;
        NoteExport exporter = new NoteExport(this);
        ok = exporter.bodyToHTMLClipboard(note);
      }
    }
    
    if (noNoteSelected) {
      trouble.report ("Select a Note before trying to generate HTML", 
          "No Note Selected");
    } 
    
    if (ok) {
      logNormal("HTML generated for body of Note " + note.getTitle());
    }
  }
  
  public void genHTMLtoFile() {
    boolean noNoteSelected = true;
    boolean ok = true;
    Note note = null;
    File selectedFile = null;
    if (position != null) {
      note = position.getNote();
      if (note != null) {
        noNoteSelected = false;
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        String htmlFolderStr = currentFileSpec.getHTMLFolder();
        if (htmlFolderStr.length() > 0) {
          File htmlFolder = new File(htmlFolderStr);
          if (htmlFolder.exists()) {
            fileChooser.setCurrentDirectory(htmlFolder);
            FileName fileName = new FileName(note.getDiskLocation());
            File htmlFile = new File(fileName.getBase() + ".html");
            fileChooser.setSelectedFile(htmlFile);
          }
        }
        selectedFile = fileChooser.showSaveDialog(this);
        if (selectedFile == null) {
          ok = false;
          noValidExportDestination();
        }
        NoteExport exporter = new NoteExport(this);
        ok = exporter.bodyToHTMLFile(note, selectedFile);
      }
    }
    
    if (noNoteSelected) {
      trouble.report ("Select a Note before trying to generate HTML", 
          "No Note Selected");
    } 
    
    if (ok) {
      logNormal("HTML generated for body of Note " + note.getTitle());
    }
  }
  
  public void logNormal (String msg) {
    Logger.getShared().recordEvent (LogEvent.NORMAL, msg, false);
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
    okButton = new javax.swing.JButton();
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
    expandAllButton = new javax.swing.JButton();
    collapseAllButton = new javax.swing.JButton();
    itemTabbedPane = new javax.swing.JTabbedPane();
    mainMenuBar = new javax.swing.JMenuBar();
    fileMenu = new javax.swing.JMenu();
    fileNewMenuItem = new javax.swing.JMenuItem();
    generateTemplateMenuItem = new javax.swing.JMenuItem();
    openMenuItem = new javax.swing.JMenuItem();
    openRecentMenu = new javax.swing.JMenu();
    openEssentialMenuItem = new javax.swing.JMenuItem();
    openHelpNotesMenuItem = new javax.swing.JMenuItem();
    openMasterCollectionMenuItem = new javax.swing.JMenuItem();
    fileSaveMenuItem = new javax.swing.JMenuItem();
    saveAllMenuItem = new javax.swing.JMenuItem();
    fileSaveAsMenuItem = new javax.swing.JMenuItem();
    reloadMenuItem = new javax.swing.JMenuItem();
    reloadTaggedMenuItem = new javax.swing.JMenuItem();
    jSeparator12 = new javax.swing.JPopupMenu.Separator();
    createMasterCollectionMenuItem = new javax.swing.JMenuItem();
    jSeparator1 = new javax.swing.JSeparator();
    publishWindowMenuItem = new javax.swing.JMenuItem();
    publishNowMenuItem = new javax.swing.JMenuItem();
    jSeparator4 = new javax.swing.JSeparator();
    fileBackupMenuItem = new javax.swing.JMenuItem();
    jSeparator5 = new javax.swing.JSeparator();
    importMenu = new javax.swing.JMenu();
    importMacAppInfo = new javax.swing.JMenuItem();
    importNotenikMenuItem = new javax.swing.JMenuItem();
    importTabDelimitedMenuItem = new javax.swing.JMenuItem();
    importXMLMenuItem = new javax.swing.JMenuItem();
    exportMenu = new javax.swing.JMenu();
    exportNotenikMenuItem = new javax.swing.JMenuItem();
    exportOPML = new javax.swing.JMenuItem();
    exportTabDelimitedMenuItem = new javax.swing.JMenuItem();
    exportTabDelimitedMSMenuItem = new javax.swing.JMenuItem();
    exportXMLMenuItem = new javax.swing.JMenuItem();
    jSeparator6 = new javax.swing.JPopupMenu.Separator();
    purgeMenuItem = new javax.swing.JMenuItem();
    jSeparator7 = new javax.swing.JPopupMenu.Separator();
    collectionMenu = new javax.swing.JMenu();
    collectionPrefsMenuItem = new javax.swing.JMenuItem();
    jSeparator2 = new javax.swing.JPopupMenu.Separator();
    findMenuItem = new javax.swing.JMenuItem();
    replaceMenuItem = new javax.swing.JMenuItem();
    jSeparator10 = new javax.swing.JPopupMenu.Separator();
    addReplaceMenuItem = new javax.swing.JMenuItem();
    flattenTagsMenuItem = new javax.swing.JMenuItem();
    lowerCaseTagsMenuItem = new javax.swing.JMenuItem();
    jSeparator3 = new javax.swing.JSeparator();
    validateURLsMenuItem = new javax.swing.JMenuItem();
    sortMenu = new javax.swing.JMenu();
    noteMenu = new javax.swing.JMenu();
    newNoteMenuItem = new javax.swing.JMenuItem();
    deleteNoteMenuItem = new javax.swing.JMenuItem();
    jSeparator8 = new javax.swing.JPopupMenu.Separator();
    nextMenuItem = new javax.swing.JMenuItem();
    priorMenuItem = new javax.swing.JMenuItem();
    jSeparator9 = new javax.swing.JPopupMenu.Separator();
    openNoteMenuItem = new javax.swing.JMenuItem();
    getFileInfoMenuItem = new javax.swing.JMenuItem();
    closeNoteMenuItem = new javax.swing.JMenuItem();
    incrementSeqMenuItem = new javax.swing.JMenuItem();
    jSeparator11 = new javax.swing.JPopupMenu.Separator();
    copyNoteMenuItem = new javax.swing.JMenuItem();
    pasteNoteMenuItem = new javax.swing.JMenuItem();
    htmlMenu = new javax.swing.JMenu();
    htmlToClipboardMenuItem = new javax.swing.JMenuItem();
    htmlToFile = new javax.swing.JMenuItem();
    editMenu = new javax.swing.JMenu();
    deleteMenuItem = new javax.swing.JMenuItem();
    escapeMenuItem = new javax.swing.JMenuItem();
    toolsMenu = new javax.swing.JMenu();
    toolsOptionsMenuItem = new javax.swing.JMenuItem();
    toolsLinkTweakerMenuItem = new javax.swing.JMenuItem();
    reportsMenu = new javax.swing.JMenu();
    windowMenu = new javax.swing.JMenu();
    helpMenu = new javax.swing.JMenu();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    mainToolBar.setRollover(true);

    okButton.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
    okButton.setText("OK");
    okButton.setToolTipText("Complete your changes to this item");
    okButton.setFocusable(false);
    okButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    okButton.setMargin(new java.awt.Insets(0, 4, 4, 4));
    okButton.setMaximumSize(new java.awt.Dimension(60, 30));
    okButton.setMinimumSize(new java.awt.Dimension(30, 26));
    okButton.setPreferredSize(new java.awt.Dimension(40, 28));
    okButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okButtonActionPerformed(evt);
      }
    });
    mainToolBar.add(okButton);

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

    treePanel.setLayout(new java.awt.GridBagLayout());

    noteTree.getSelectionModel().setSelectionMode
    (TreeSelectionModel.SINGLE_TREE_SELECTION);
    noteTree.addTreeSelectionListener (new TreeSelectionListener() {
      public void valueChanged (TreeSelectionEvent e) {
        selectBranch();
      }
    });
    treeScrollPane.setViewportView(noteTree);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 0.9;
    gridBagConstraints.weighty = 0.9;
    treePanel.add(treeScrollPane, gridBagConstraints);

    expandAllButton.setText("Expand All");
    expandAllButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        expandAllButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    treePanel.add(expandAllButton, gridBagConstraints);

    collapseAllButton.setText("Collapse All");
    collapseAllButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        collapseAllButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    treePanel.add(collapseAllButton, gridBagConstraints);

    collectionTabbedPane.addTab("Tags", treePanel);

    mainSplitPane.setLeftComponent(collectionTabbedPane);

    itemTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        itemTabbedPaneStateChanged(evt);
      }
    });
    mainSplitPane.setRightComponent(itemTabbedPane);

    getContentPane().add(mainSplitPane, java.awt.BorderLayout.CENTER);

    fileMenu.setText("File");

    fileNewMenuItem.setText("New...");
    fileNewMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        fileNewMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(fileNewMenuItem);

    generateTemplateMenuItem.setText("Generate Template...");
    generateTemplateMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        generateTemplateMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(generateTemplateMenuItem);

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

    openEssentialMenuItem.setAccelerator(KeyStroke.getKeyStroke (KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    openEssentialMenuItem.setText("Open Essential Collection");
    openEssentialMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openEssentialMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(openEssentialMenuItem);

    openHelpNotesMenuItem.setText("Open Help Notes");
    openHelpNotesMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openHelpNotesMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(openHelpNotesMenuItem);

    openMasterCollectionMenuItem.setAccelerator(KeyStroke.getKeyStroke (KeyEvent.VK_M, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    openMasterCollectionMenuItem.setText("Open Master Collection");
    openMasterCollectionMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openMasterCollectionMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(openMasterCollectionMenuItem);

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

    reloadTaggedMenuItem.setText("Reload w/o Untagged");
    reloadTaggedMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        reloadTaggedMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(reloadTaggedMenuItem);
    fileMenu.add(jSeparator12);

    createMasterCollectionMenuItem.setText("Create Master Collection...");
    createMasterCollectionMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        createMasterCollectionMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(createMasterCollectionMenuItem);
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

    importMenu.setText("Import");

    importMacAppInfo.setText("Mac App Info...");
    importMacAppInfo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        importMacAppInfoActionPerformed(evt);
      }
    });
    importMenu.add(importMacAppInfo);

    importNotenikMenuItem.setText("Notenik...");
    importNotenikMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        importNotenikMenuItemActionPerformed(evt);
      }
    });
    importMenu.add(importNotenikMenuItem);

    importTabDelimitedMenuItem.setText("Tab-Delimited...");
    importTabDelimitedMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        importTabDelimitedMenuItemActionPerformed(evt);
      }
    });
    importMenu.add(importTabDelimitedMenuItem);

    importXMLMenuItem.setText("XML...");
    importXMLMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        importXMLMenuItemActionPerformed(evt);
      }
    });
    importMenu.add(importXMLMenuItem);

    fileMenu.add(importMenu);

    exportMenu.setText("Export");

    exportNotenikMenuItem.setText("Notenik");
    exportNotenikMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exportNotenikMenuItemActionPerformed(evt);
      }
    });
    exportMenu.add(exportNotenikMenuItem);

    exportOPML.setText("OPML");
    exportOPML.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exportOPMLActionPerformed(evt);
      }
    });
    exportMenu.add(exportOPML);

    exportTabDelimitedMenuItem.setText("Tab-Delimited");
    exportTabDelimitedMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exportTabDelimitedMenuItemActionPerformed(evt);
      }
    });
    exportMenu.add(exportTabDelimitedMenuItem);

    exportTabDelimitedMSMenuItem.setText("Tab-Delimited for MS Links");
    exportTabDelimitedMSMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exportTabDelimitedMSMenuItemActionPerformed(evt);
      }
    });
    exportMenu.add(exportTabDelimitedMSMenuItem);

    exportXMLMenuItem.setText("XML");
    exportXMLMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exportXMLMenuItemActionPerformed(evt);
      }
    });
    exportMenu.add(exportXMLMenuItem);

    fileMenu.add(exportMenu);
    fileMenu.add(jSeparator6);

    purgeMenuItem.setText("Purge...");
    purgeMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        purgeMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(purgeMenuItem);
    fileMenu.add(jSeparator7);

    mainMenuBar.add(fileMenu);

    collectionMenu.setText("Collection");

    collectionPrefsMenuItem.setText("Collection Preferences");
    collectionPrefsMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        collectionPrefsMenuItemActionPerformed(evt);
      }
    });
    collectionMenu.add(collectionPrefsMenuItem);
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

  sortMenu.setText("Sort");
  mainMenuBar.add(sortMenu);

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
  noteMenu.add(jSeparator8);

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
  noteMenu.add(jSeparator9);

  openNoteMenuItem.setAccelerator(KeyStroke.getKeyStroke (KeyEvent.VK_T, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
  openNoteMenuItem.setText("Text Edit Note");
  openNoteMenuItem.setToolTipText("Open Note using local Text Editor");
  openNoteMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      openNoteMenuItemActionPerformed(evt);
    }
  });
  noteMenu.add(openNoteMenuItem);

  getFileInfoMenuItem.setAccelerator(KeyStroke.getKeyStroke (KeyEvent.VK_G, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
  getFileInfoMenuItem.setText("Get File Info...");
  getFileInfoMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      getFileInfoMenuItemActionPerformed(evt);
    }
  });
  noteMenu.add(getFileInfoMenuItem);

  closeNoteMenuItem.setAccelerator(KeyStroke.getKeyStroke (KeyEvent.VK_K, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
  closeNoteMenuItem.setText("Close Note");
  closeNoteMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      closeNoteMenuItemActionPerformed(evt);
    }
  });
  noteMenu.add(closeNoteMenuItem);

  incrementSeqMenuItem.setText("Increment Seq");
  incrementSeqMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      incrementSeqMenuItemActionPerformed(evt);
    }
  });
  noteMenu.add(incrementSeqMenuItem);
  noteMenu.add(jSeparator11);

  copyNoteMenuItem.setText("Copy Note");
  copyNoteMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      copyNoteMenuItemActionPerformed(evt);
    }
  });
  noteMenu.add(copyNoteMenuItem);

  pasteNoteMenuItem.setText("Paste Note");
  pasteNoteMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      pasteNoteMenuItemActionPerformed(evt);
    }
  });
  noteMenu.add(pasteNoteMenuItem);

  htmlMenu.setText("Gen HTML");

  htmlToClipboardMenuItem.setText("Copy to Clipboard");
  htmlToClipboardMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      htmlToClipboardMenuItemActionPerformed(evt);
    }
  });
  htmlMenu.add(htmlToClipboardMenuItem);

  htmlToFile.setText("Save to File");
  htmlToFile.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      htmlToFileActionPerformed(evt);
    }
  });
  htmlMenu.add(htmlToFile);

  noteMenu.add(htmlMenu);

  mainMenuBar.add(noteMenu);

  editMenu.setText("Edit");

  deleteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_BACK_SPACE, 0));
  deleteMenuItem.setText("Delete");
  deleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      deleteMenuItemActionPerformed(evt);
    }
  });
  editMenu.add(deleteMenuItem);

  escapeMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
  escapeMenuItem.setText("Escape Edit");
  escapeMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      escapeMenuItemActionPerformed(evt);
    }
  });
  editMenu.add(escapeMenuItem);

  mainMenuBar.add(editMenu);

  toolsMenu.setText("Tools");

  toolsOptionsMenuItem.setText("Options...");
  toolsOptionsMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      toolsOptionsMenuItemActionPerformed(evt);
    }
  });
  toolsMenu.add(toolsOptionsMenuItem);

  toolsLinkTweakerMenuItem.setAccelerator(KeyStroke.getKeyStroke (KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
  toolsLinkTweakerMenuItem.setText("Link Tweaker...");
  toolsLinkTweakerMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      toolsLinkTweakerMenuItemActionPerformed(evt);
    }
  });
  toolsMenu.add(toolsLinkTweakerMenuItem);

  mainMenuBar.add(toolsMenu);

  reportsMenu.setText("Reports");
  mainMenuBar.add(reportsMenu);

  windowMenu.setText("Window");
  mainMenuBar.add(windowMenu);

  helpMenu.setText("Help");
  mainMenuBar.add(helpMenu);

  setJMenuBar(mainMenuBar);

  pack();
  }// </editor-fold>//GEN-END:initComponents

private void launchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_launchButtonActionPerformed
  if (editingMasterCollection) {
    openFileFromCurrentNote();
  } else {
    openURL (linkText.getText());
  }
}//GEN-LAST:event_launchButtonActionPerformed

private void fileNewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileNewMenuItemActionPerformed
  userNewFile();
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
  userSaveFileAs ();
}//GEN-LAST:event_fileSaveAsMenuItemActionPerformed

private void fileSaveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSaveMenuItemActionPerformed
    boolean modOK = modIfChanged();
    if (modOK) {
      positionAndDisplay();
    }
}//GEN-LAST:event_fileSaveMenuItemActionPerformed

private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
  if (editingMasterCollection) {
    openFileFromCurrentNote();
  } else {
    userOpenFile();
  }
}//GEN-LAST:event_openMenuItemActionPerformed

private void importNotenikMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importNotenikMenuItemActionPerformed
  importFile();
}//GEN-LAST:event_importNotenikMenuItemActionPerformed

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

private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
  doneEditing();
}//GEN-LAST:event_okButtonActionPerformed

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

private void toolsOptionsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolsOptionsMenuItemActionPerformed
  this.handlePreferences();
}//GEN-LAST:event_toolsOptionsMenuItemActionPerformed

private void publishWindowMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_publishWindowMenuItemActionPerformed
  displayPublishWindow();
}//GEN-LAST:event_publishWindowMenuItemActionPerformed

  private void publishNowMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_publishNowMenuItemActionPerformed
    publishWindow.publishNow();
  }//GEN-LAST:event_publishNowMenuItemActionPerformed

  private void replaceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceMenuItemActionPerformed
    startReplace();
  }//GEN-LAST:event_replaceMenuItemActionPerformed

  private void exportNotenikMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportNotenikMenuItemActionPerformed
    generalExport(NoteExport.NOTENIK_EXPORT);
  }//GEN-LAST:event_exportNotenikMenuItemActionPerformed

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
    generalExport(NoteExport.TABDELIM_EXPORT);
  }//GEN-LAST:event_exportTabDelimitedMenuItemActionPerformed

  private void openNoteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openNoteMenuItemActionPerformed
    openNote();
  }//GEN-LAST:event_openNoteMenuItemActionPerformed

  private void toolsLinkTweakerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolsLinkTweakerMenuItemActionPerformed
    invokeLinkTweaker();
  }//GEN-LAST:event_toolsLinkTweakerMenuItemActionPerformed

  private void escapeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_escapeMenuItemActionPerformed
    positionAndDisplay();
  }//GEN-LAST:event_escapeMenuItemActionPerformed

  private void getFileInfoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getFileInfoMenuItemActionPerformed
    displayFileInfo();
  }//GEN-LAST:event_getFileInfoMenuItemActionPerformed

  private void exportXMLMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportXMLMenuItemActionPerformed
    generalExport(NoteExport.XML_EXPORT);
  }//GEN-LAST:event_exportXMLMenuItemActionPerformed

  private void importXMLMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importXMLMenuItemActionPerformed
    importXMLFile();
  }//GEN-LAST:event_importXMLMenuItemActionPerformed

  private void exportTabDelimitedMSMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportTabDelimitedMSMenuItemActionPerformed
    generalExport(NoteExport.TABDELIM_EXPORT_MS_LINKS);
  }//GEN-LAST:event_exportTabDelimitedMSMenuItemActionPerformed

  private void purgeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purgeMenuItemActionPerformed
    if (statusIncluded) {
      purge();
    }
  }//GEN-LAST:event_purgeMenuItemActionPerformed

  private void closeNoteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeNoteMenuItemActionPerformed
    if (statusIncluded || (dateIncluded && recursIncluded)) {
      closeNote();
    }
  }//GEN-LAST:event_closeNoteMenuItemActionPerformed

  private void copyNoteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyNoteMenuItemActionPerformed
    copyNote();
  }//GEN-LAST:event_copyNoteMenuItemActionPerformed

  private void pasteNoteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteNoteMenuItemActionPerformed
    pasteNote();
  }//GEN-LAST:event_pasteNoteMenuItemActionPerformed

  private void generateTemplateMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateTemplateMenuItemActionPerformed
    generateTemplate();
  }//GEN-LAST:event_generateTemplateMenuItemActionPerformed

  private void exportOPMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportOPMLActionPerformed
    generalExport(NoteExport.OPML_EXPORT);
  }//GEN-LAST:event_exportOPMLActionPerformed

  private void htmlToClipboardMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_htmlToClipboardMenuItemActionPerformed
    genHTMLtoClipboard();
  }//GEN-LAST:event_htmlToClipboardMenuItemActionPerformed

  private void htmlToFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_htmlToFileActionPerformed
    genHTMLtoFile();
  }//GEN-LAST:event_htmlToFileActionPerformed

  private void collectionPrefsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_collectionPrefsMenuItemActionPerformed
    displayAuxiliaryWindow(collectionPrefs);
  }//GEN-LAST:event_collectionPrefsMenuItemActionPerformed

  private void importTabDelimitedMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importTabDelimitedMenuItemActionPerformed
    importTabDelimited();
  }//GEN-LAST:event_importTabDelimitedMenuItemActionPerformed

  private void itemTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_itemTabbedPaneStateChanged
    int index = itemTabbedPane.getSelectedIndex();
    if (index == DISPLAY_TAB_INDEX) {
      if (position != null 
          && displayTab != null 
          && widgets != null 
          && (! widgets.isEmpty())) {
        boolean modOK = modIfChanged();
        if (modOK) {
          positionAndDisplay();
        }
      }
      okButton.setEnabled(false);
    } else {
      okButton.setEnabled(true);
    }
  }//GEN-LAST:event_itemTabbedPaneStateChanged

  private void importMacAppInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importMacAppInfoActionPerformed
    importMacAppInfo();
  }//GEN-LAST:event_importMacAppInfoActionPerformed

  private void reloadTaggedMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadTaggedMenuItemActionPerformed
    reloadTaggedOnly();
  }//GEN-LAST:event_reloadTaggedMenuItemActionPerformed

  private void incrementSeqMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_incrementSeqMenuItemActionPerformed
    if (seqIncluded) {
      incrementSeq();
    }
  }//GEN-LAST:event_incrementSeqMenuItemActionPerformed

  private void openHelpNotesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openHelpNotesMenuItemActionPerformed
    openHelpNotes();
  }//GEN-LAST:event_openHelpNotesMenuItemActionPerformed

  private void expandAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expandAllButtonActionPerformed
    expandAllTags();
  }//GEN-LAST:event_expandAllButtonActionPerformed

  private void collapseAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_collapseAllButtonActionPerformed
    collapseAllTags();
  }//GEN-LAST:event_collapseAllButtonActionPerformed

  private void createMasterCollectionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createMasterCollectionMenuItemActionPerformed
    createMasterCollection();
  }//GEN-LAST:event_createMasterCollectionMenuItemActionPerformed

  private void openMasterCollectionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMasterCollectionMenuItemActionPerformed
    openMasterCollection();
  }//GEN-LAST:event_openMasterCollectionMenuItemActionPerformed

  private void openEssentialMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openEssentialMenuItemActionPerformed
    openEssentialCollection();
  }//GEN-LAST:event_openEssentialMenuItemActionPerformed



  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JMenuItem addReplaceMenuItem;
  private javax.swing.JMenuItem closeNoteMenuItem;
  private javax.swing.JButton collapseAllButton;
  private javax.swing.JMenu collectionMenu;
  private javax.swing.JMenuItem collectionPrefsMenuItem;
  private javax.swing.JTabbedPane collectionTabbedPane;
  private javax.swing.JMenuItem copyNoteMenuItem;
  private javax.swing.JMenuItem createMasterCollectionMenuItem;
  private javax.swing.JMenuItem deleteMenuItem;
  private javax.swing.JMenuItem deleteNoteMenuItem;
  private javax.swing.JMenu editMenu;
  private javax.swing.JMenuItem escapeMenuItem;
  private javax.swing.JButton expandAllButton;
  private javax.swing.JMenu exportMenu;
  private javax.swing.JMenuItem exportNotenikMenuItem;
  private javax.swing.JMenuItem exportOPML;
  private javax.swing.JMenuItem exportTabDelimitedMSMenuItem;
  private javax.swing.JMenuItem exportTabDelimitedMenuItem;
  private javax.swing.JMenuItem exportXMLMenuItem;
  private javax.swing.JMenuItem fileBackupMenuItem;
  private javax.swing.JMenu fileMenu;
  private javax.swing.JMenuItem fileNewMenuItem;
  private javax.swing.JMenuItem fileSaveAsMenuItem;
  private javax.swing.JMenuItem fileSaveMenuItem;
  private javax.swing.JButton findButton;
  private javax.swing.JMenuItem findMenuItem;
  private javax.swing.JTextField findText;
  private javax.swing.JMenuItem flattenTagsMenuItem;
  private javax.swing.JMenuItem generateTemplateMenuItem;
  private javax.swing.JMenuItem getFileInfoMenuItem;
  private javax.swing.JMenu helpMenu;
  private javax.swing.JMenu htmlMenu;
  private javax.swing.JMenuItem htmlToClipboardMenuItem;
  private javax.swing.JMenuItem htmlToFile;
  private javax.swing.JMenuItem importMacAppInfo;
  private javax.swing.JMenu importMenu;
  private javax.swing.JMenuItem importNotenikMenuItem;
  private javax.swing.JMenuItem importTabDelimitedMenuItem;
  private javax.swing.JMenuItem importXMLMenuItem;
  private javax.swing.JMenuItem incrementSeqMenuItem;
  private javax.swing.JTabbedPane itemTabbedPane;
  private javax.swing.JSeparator jSeparator1;
  private javax.swing.JPopupMenu.Separator jSeparator10;
  private javax.swing.JPopupMenu.Separator jSeparator11;
  private javax.swing.JPopupMenu.Separator jSeparator12;
  private javax.swing.JPopupMenu.Separator jSeparator2;
  private javax.swing.JSeparator jSeparator3;
  private javax.swing.JSeparator jSeparator4;
  private javax.swing.JSeparator jSeparator5;
  private javax.swing.JPopupMenu.Separator jSeparator6;
  private javax.swing.JPopupMenu.Separator jSeparator7;
  private javax.swing.JPopupMenu.Separator jSeparator8;
  private javax.swing.JPopupMenu.Separator jSeparator9;
  private javax.swing.JButton launchButton;
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
  private javax.swing.JButton okButton;
  private javax.swing.JMenuItem openEssentialMenuItem;
  private javax.swing.JMenuItem openHelpNotesMenuItem;
  private javax.swing.JMenuItem openMasterCollectionMenuItem;
  private javax.swing.JMenuItem openMenuItem;
  private javax.swing.JMenuItem openNoteMenuItem;
  private javax.swing.JMenu openRecentMenu;
  private javax.swing.JMenuItem pasteNoteMenuItem;
  private javax.swing.JMenuItem priorMenuItem;
  private javax.swing.JMenuItem publishNowMenuItem;
  private javax.swing.JMenuItem publishWindowMenuItem;
  private javax.swing.JMenuItem purgeMenuItem;
  private javax.swing.JMenuItem reloadMenuItem;
  private javax.swing.JMenuItem reloadTaggedMenuItem;
  private javax.swing.JMenuItem replaceMenuItem;
  private javax.swing.JMenu reportsMenu;
  private javax.swing.JMenuItem saveAllMenuItem;
  private javax.swing.JMenu sortMenu;
  private javax.swing.JScrollPane tableScrollPane;
  private javax.swing.JMenuItem toolsLinkTweakerMenuItem;
  private javax.swing.JMenu toolsMenu;
  private javax.swing.JMenuItem toolsOptionsMenuItem;
  private javax.swing.JPanel treePanel;
  private javax.swing.JScrollPane treeScrollPane;
  private javax.swing.JButton urlDeleteButton;
  private javax.swing.JButton urlFirstButton;
  private javax.swing.JButton urlLastButton;
  private javax.swing.JButton urlNewButton;
  private javax.swing.JButton urlNextButton;
  private javax.swing.JButton urlPriorButton;
  private javax.swing.JMenuItem validateURLsMenuItem;
  private javax.swing.JMenu windowMenu;
  // End of variables declaration//GEN-END:variables

}
