<!-- Generated using template product-user-guide-template.mdtoc -->
<!-- Generated using template product-user-guide-template.md -->
<h1 id="notenik-user-guide">Notenik User Guide</h1>


<h2 id="table-of-contents">Table of Contents</h2>

<div id="toc">
  <ul>
    <li>
      <a href="#introduction">Introduction</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li>
          <a href="#system-requirements">System Requirements</a>
        </li>
        <li>
          <a href="#rights">Rights</a>
        </li>
        <li>
          <a href="#installation">Installation</a>
        </li>
      </ul>

    </li>
    <li>
      <a href="#data-fields">Data Fields</a>
    </li>
    <li>
      <a href="#file-operations">File Operations</a>
      <ul>
        <li>
          <a href="#creating-a-new-collection">Creating a New Collection</a>
        </li>
        <li>
          <a href="#saving-a-collection">Saving a Collection</a>
        </li>
        <li>
          <a href="#opening-recent-files">Opening Recent Files</a>
        </li>
      </ul>

    </li>
    <li>
      <a href="#user-interface">User Interface</a>
      <ul>
        <li>
          <a href="#the-tool-bar">The Tool Bar</a>
        </li>
        <li>
          <a href="#main-window">Main Window</a>
        </li>
        <li>
          <a href="#publish">Publish</a>
        </li>
      </ul>

    </li>
    <li>
      <a href="#preferences">Preferences</a>
      <ul>
        <li>
          <a href="#general-prefs">General Prefs</a>
        </li>
        <li>
          <a href="#favorites-prefs">Favorites Prefs</a>
        </li>
        <li>
          <a href="#tags-export-prefs">Tags Export Prefs</a>
        </li>
        <li>
          <a href="#folder-sync-prefs">Folder Sync Prefs</a>
        </li>
      </ul>

    </li>
    <li>
      <a href="#help">Help</a>
    </li>
  </ul>

</div>


<h2 id="introduction">Introduction</h2>


Notenik is a desktop software program to help a single user maintain multiple collections of notes.

You may well ask why the world needs yet another note-taking app and, in truth, I'm not sure it does. However, whether it wants or needs one or not, it has one, and so I will try to explain why I've have labored to give birth to such a thing at this late date. 

Here were my design goals for the app. 

1. **Each note stored as a plain text file.** This ensures that the notes can be edited on any device, by any text editor, and allows the notes to be painlessly synced between devices using a service such as Dropbox.

2. **Ability to handle multiple collections of notes.** I really don't want to be limited to a single collection. With Notenik, create as many folders of notes as you like. 

3. **Embedded, platform-independent tags.** I want to be able to tag my notes, and see them organized by tags, but I want the tags to move with the notes when the notes get synced between devices, and I want the tags to be editable with any text editor that can be used to edit the notes themselves. 

4. **Bookmarks too.** Add a URL to a note, and it becomes a bookmark. So now I can create a separate folder just for bookmarks, and organize them by tags, all with the same little app. 

5. **A file format that is simple to read and simple to edit.** No XML, no HTML, just some flexible, quasi-markdown formatting. 

Make sense?

If so, read on. 

If not, just move along -- this is not the app you were looking for. 


<h2 id="getting-started">Getting Started</h2>


<h3 id="system-requirements">System Requirements</h3>


Notenik is written in Java and can run on any reasonably modern operating system, including Mac OS X, Windows and Linux. Notenik requires a Java Runtime Environment (JRE), also known as a Java Virtual Machine (JVM). The version of this JRE/JVM must be at least 6. Visit [www.java.com](http://www.java.com) to download a recent version for most operating systems. Installation happens a bit differently under Mac OS X, but generally will occur fairly automatically when you try to launch a Java app for the first time.

Because Notenik may be run on multiple platforms, it may look slightly different on different operating systems, and will obey slightly different conventions (using the CMD key on a Mac, vs. an ALT key on a PC, for example).

<h3 id="rights">Rights</h3>


Notenik Copyright 2013 - 2014 by Herb Bowie

Notenik is [open source software](http://opensource.org/osd). Source code is available at [GitHub](http://github.com/hbowie/notenik).

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

  [www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.


<h3 id="installation">Installation</h3>


Download the latest version from [PowerSurgePub.com](http://www.powersurgepub.com/downloads.html). Decompress the downloaded file. Drag the resulting file or folder into the location where you normally store your applications. Double-click on the jar file (or the application, if you've downloaded the Mac app) to launch.


<h2 id="data-fields">Data Fields</h2>


Notenik works with collections of Notes.

Notenik maintains the following fields for each Note.

Title
:    A brief title for the note. Each note in a collection must have a unique title.

Tags
:    Use a period to nest one tag within another. Use a comma to separate multiple tags. The "Favorites" tag by default identifies Notes you wish to appear in your favorites file (see the **Publish** section below). The "Startup" tag identifies Notes you wish to have launched in your favorite Web Browser whenever Notenik launches, so long as this is requested in your Preferences.

Link
:    A Hyperlink (aka URL).

Body
:    The body of the note. This may be left blank, if the title says it all. Use [Markdown](http://daringfireball.net/projects/markdown/) formatting within the body, if desired.

<h2 id="file-operations">File Operations</h2>


File operations may be accessed via the File menu.

<h3 id="creating-a-new-collection">Creating a New Collection</h3>


The first time you launch Notenik, or after selecting **New** from the **File** menu, you will see a fresh collection of Notes, containing only a single entry, for PowerSurge Publishing. New users will  have their first set of Notes saved automatically for them in a default location, in a "Notenik" folder within their Documents folder.

<h3 id="saving-a-collection">Saving a Collection</h3>


Several **Save** commands are available from the **File** menu. **Save As** will allow you to specify a new folder for the current collection of Notes. **Save** will prompt you for a folder, if one hasn't already been specified, and then will save any unsaved Notes to disk. **Save All** will force all Notes in the collection to be saved to disk.

<h3 id="opening-recent-files">Opening Recent Files</h3>


On subsequent launches, Notenik will automatically open the last Notenik collection you used. You may also open a recent file by selecting **Open Recent** from the **File** menu.

<h2 id="user-interface">User Interface</h2>



<h3 id="the-tool-bar">The Tool Bar</h3>


A toolbar with multiple buttons appears at the top of the user interface.

* **OK** -- Indicates that you have completed adding/editing the fields for the current Note.
* **+** -- Clear the data fields and prepare to add a new Note to the collection.
* **-** -- Delete the current Note.
* **&lt;&lt;** -- Display the first Note in the collection.
* **&lt;** -- Display the next Note in the collection.
* **&gt;** -- Display the next Note in the collection.
* **&gt;&gt;** -- Display the last Note in the collection.
* **Launch** -- Launch the current Note in your Web browser. (This may also be accomplished by clicking the arrow that appears just to the left of the URL itself.)
* **Find** -- Looks for the text entered in the field just to the left of this button, and displays the first Note containing this text in any field, ignoring case. After finding the first occurrence, this button's text changes to **Again**, to allow you to search again for the next Note containing the specified text.

<h3 id="main-window">Main Window</h3>


The main window contains three different panes.

<h4 id="the-list">The List</h4>


On the first half of the main window, you'll see two tabs. The first of these displays the **List**. This is just a simple list of all your Notes. You can rearrange/resize columns. You can't sort by other columns. Click on a row to select that Note for display on the other half of the main window. Use the entries on the **View** menu to select a different sorting/filtering option. Use the **View Preferences** to modify your view options.

<h4 id="tags">Tags</h4>


The second Tab on the first half of the main window displays the **Tags**. This is an indented list of all your Tags, with Notes appearing under as many Tags as have been assigned to them, and with Notes with no Tags displaying at the very top. Click to the left of a Tag to expand it, showing Notes and/or sub-tags contained within it.

Note that Tags that were once used, but that are used no more, will stick around until you close the Notenik file and re-open it. If you wish, you may accelerate this process by selecting **Reload** from the **File** menu.

<h4 id="details">Details</h4>


The detailed data for the currently selected Note appears on the second half of the main window.


<h3 id="publish">Publish</h3>


The publish option allows you to easily publish your Notes in a variety of useful formats.

To begin the publication process, select the **Publish...** command from the **File** menu.

You will then see a window with the following fields available to you.

Publish to
:    You may use the Browse button above and to the right to select a folder on your computer to which you wish to publish your Notes. You may also enter or modify the path directly in the text box. When modifying this field, you will be prompted to specify whether you wish to update the existing publication location, or add a new one. By specifying that you wish to add a new one, you may create multiple publications, and then later select the publication of interest by using the drop-down arrow to the right of this field.

Equivalent URL
:    If the folder to which you are publishing will be addressable from the World-Wide Web, then enter its Web address here.

Templates
:    This is the address of a folder containing one or more publishing templates. This will default to the location of the templates provided along with the application executable. You may use the Browse button above and to the right to pick a different location, if you have your own templates you wish to use for publishing.

Select
:    Use the drop-down list to select the template you wish to use.

	**Favorites Plus**: This template will produce the following files and formats.

	1. index.html -- This file is an index file with links to the other files. You can browse this locally by selecting **Browse local index** from the **File** menu.
	 2. favorites.html -- This file tries to arrange all of the Notes you have tagged as "Favorites" into a four-column format that will fit on a single page.
	 3. bookmark.html -- This file formats your URLs in the time-honored Netscape bookmarks format, suitable for import into almost any Web browser or URL manager.
	 4. outline.html -- This is a dynamic html file that organizes your URLs within your tags, allowing you to reveal/disclose selected tags.

Apply
:    Press this button to apply the selected template. This will copy the contents of the template folder to the location specified above as the Publish to location.

Publish Script
:    Specify the location of the script to be used. The PSTextMerge templating system is the primary scripting language used for publishing. A PSTextMerge script will usually end with a '.tcz' file extension.

Publish when
:    You may specify publication 'On Close' (whenever you Quit the application or close a data collection), 'On Save' (whenever you save the data collection to disk), or 'On Demand'.

Publish Now
:    Press this button to publish to the currently displayed location. Note that, if you've specified 'On Demand', then this is the only time that publication will occur.

View
:    Select the local file location or the equivalent URL location.

View Now
:    Press this button to view the resulting Web site in your Web browser.

<h2 id="preferences">Preferences</h2>


The following preference tabs are available.

<h3 id="general-prefs">General Prefs</h3>


The program's General Preferences contain a number of options for modifying the program's look and feel. Feel free to experiment with these to find your favorite configuration. Some options may require you to quit and re-launch Notenik before the changes will take effect.

SplitPane: Horizontal Split?
:    Check the box to have the **List** and **Tags** appear on the left of the main screen, rather than the top.

Deletion: Confirm Deletes?
:    Check the box to have a confirmation dialog shown whenever you attempt to delete the selected Note.

Software Updates: Check Automatically?
:    Check the box to have Notenik check for newer versions whenever it launches.

Check Now
:    Click this button to check for a new version immediately.

File Chooser
:    If running on a Mac, you may wish to select AWT rather than Swing, to make your Open and Save dialogs appear more Mac-like. However, Swing dialogs may still appear to handle options that can't be handled by the native AWT chooser.

Look and Feel
:    Select from one of the available options to change the overall look and feel of the application.

Menu Location
:    If running on a Mac, you may wish to have the menus appear at the top of the screen, rather than at the top of the window.

<h3 id="favorites-prefs">Favorites Prefs</h3>


Open Startup Tags at Program Launch?
:    Indicate whether you want URLs tagged with "Startup" launched within your Web browser whenever URL Union starts.

Favorites Tags
:    Specify the tags that you'd like Favorites pages to be generated for. The default is 'Favorites', but you may specify whatever you'd like here, separating separate tags with commas. Each tag identified here will have a separate page generated with a name matching the tag.

Home Link
:    Specify the desired link from the Favorites page to a Home page.

Favorites Columns
:    Specify the number of columns you wish to appear on the Favorites page.

Favorites Rows
:    Specify the maximum number of rows you wish to appear on the Favorites page.

<h3 id="tags-export-prefs">Tags Export Prefs</h3>


Tags to Select
:    Leave this blank to select all tags on any export, including a data export performed as part of a Publish process. Specifying one or more tags here will limit the content of the export to items containing at least one of those tags.

Tags to Suppress
:    Any tags specified here will be removed from all tags fields appearing on exports. This may be useful to suppress tags used for selection at Publish time, as opposed to tags that will appear in the eventual output being created.


<h3 id="folder-sync-prefs">Folder Sync Prefs</h3>


The Folder Sync application prefs pane allow the user to identify a common folder to which several different Notenik collections can be synced.

The common folder may then be conveniently accessed using nvAlt.

Each collection can have a different prefix assigned, and that prefix will then be used to keep the notes from the different collections separately identified within the common nvAlt folder. The prefix will default to the folder name for the collection, with a trailing 's' removed if one is found, and with a dash added as a separator. A folder name of 'Bookmarks', for example, would result in a prefix of 'Bookmark - ' being appended to the front of each note as it is stored in the common folder.

The logic for the syncing works as follows.

A sweep of the entire common folder will be performed whenever syncing is first turned on for a collection, and henceforth whenever a collection with syncing already on is opened.

The sweep sync includes the following logic.

* For any nvAlt notes with a matching prefix, where the corresponding note does not already exist within the Notenik collection, the note will be added to the Notenik collection.

* For any Notenik notes where a matching nvAlt note is not found, the note will be added to the nvAlt folder.

* For any Notenik notes where a matching nvAlt note has been updated more recently than the matching Notenik note, the Notenik note will be updated to match the nvAlt note.

Once folder sync has been turned on for a collection, then every time that Notenik makes an update to any note within that collection, a parallel update will be made to the corresponding note within the common folder.


<h2 id="help">Help</h2>


The following commands are available. Note that the first two commands open local documentation installed with your application, while the next group of commands will access the Internet and access the latest program documentation, where applicable.

* **Program History** -- Opens the program's version history in your preferred Web browser.

* **User Guide** -- Opens the program's user guide in your preferred Web browser.

* **Check for Updates** -- Checks the PowerSurgePub web site to see if you're running the latest version of the application.

* **Notenik Home Page** -- Open's the Notenik product page on the World-Wide Web.

* **Reduce Window Size** -- Restores the main Notenik window to its default size and location. Note that this command has a shortcut so that it may be executed even when the Notenik window is not visible. This command may sometimes prove useful if you use multiple monitors, but occasionally in different configurations. On Windows in particular, this sometimes results in Notenik opening on a monitor that is no longer present, making it difficult to see.



[java]:       http://www.java.com/
[pspub]:      http://www.powersurgepub.com/
[downloads]:  http://www.powersurgepub.com/downloads.html
[osd]:		  http://opensource.org/osd
[gnu]:        http://www.gnu.org/licenses/
[apache]:	     http://www.apache.org/licenses/LICENSE-2.0.html
[markdown]:		http://daringfireball.net/projects/markdown/
[multimarkdown]:  http://fletcher.github.com/peg-multimarkdown/

[wikiq]:     http://www.wikiquote.org
[support]:   mailto:support@powersurgepub.com
[fortune]:   http://en.wikipedia.org/wiki/Fortune_(Unix)
[opml]:      http://en.wikipedia.org/wiki/OPML
[textile]:   http://en.wikipedia.org/wiki/Textile_(markup_language)
[pw]:        http://www.portablewisdom.org

[store]:     http://www.powersurgepub.com/store.html

[pegdown]:   https://github.com/sirthias/pegdown/blob/master/LICENSE
[parboiled]: https://github.com/sirthias/parboiled/blob/master/LICENSE
[Mathias]:   https://github.com/sirthias

[club]:         clubplanner.html
[filedir]:      filedir.html
[metamarkdown]: metamarkdown.html
[template]:     template.html

[mozilla]:    http://www.mozilla.org/MPL/2.0/


