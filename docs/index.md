<h1 id="notenik-user-guide">Notenik User Guide</h1>


Version: 2.00

<h2 id="table-of-contents">Table of Contents</h2>

<div id="toc">
  <ul>
    <li>
      <a href="#note-taking">Note Taking</a>
    </li>
    <li>
      <a href="#navigate-and-maintain-your-notes">Navigate and Maintain your Notes</a>
    </li>
    <li>
      <a href="#organize-your-notes-into-collections">Organize Your Notes into Collections</a>
    </li>
    <li>
      <a href="#sync-to-the-cloud-and-to-other-devices">Sync to the Cloud and to other Devices</a>
    </li>
    <li>
      <a href="#edit-your-notes-with-any-text-editor">Edit Your Notes with any Text Editor</a>
    </li>
    <li>
      <a href="#add-a-link-to-each-note">Add a Link to each Note</a>
    </li>
    <li>
      <a href="#organize-your-notes-with-tags">Organize Your Notes with Tags</a>
    </li>
    <li>
      <a href="#add-other-fields">Add Other Fields</a>
    </li>
    <li>
      <a href="#create-backups">Create Backups</a>
    </li>
    <li>
      <a href="#sync-multiple-collections-to-a-common-folder">Sync Multiple Collections to a Common Folder</a>
    </li>
    <li>
      <a href="#export-your-notes">Export Your Notes</a>
      <ul>
        <li>
          <a href="#export-to-notenik">Export to Notenik</a>
        </li>
        <li>
          <a href="#export-to-opml">Export to OPML</a>
        </li>
        <li>
          <a href="#export-to-tabdelimited">Export to Tab-Delimited</a>
        </li>
        <li>
          <a href="#export-to-tabdelimited-for-ms-links">Export to Tab-Delimited for MS Links</a>
        </li>
        <li>
          <a href="#export-to-xml">Export to XML</a>
        </li>
        <li>
          <a href="#publish-your-notes">Publish Your Notes</a>
        </li>
      </ul>

    </li>
    <li>
      <a href="#publish-a-favorites-page">Publish a Favorites Page</a>
    </li>
    <li>
      <a href="#findreplace-and-other-operations-on-collections">Find/Replace and Other Operations on Collections</a>
    </li>
    <li>
      <a href="#modify-the-look-and-feel">Modify the Look and Feel</a>
    </li>
    <li>
      <a href="#system-requirements">System Requirements</a>
    </li>
    <li>
      <a href="#installation">Installation</a>
    </li>
    <li>
      <a href="#rights">Rights</a>
    </li>
  </ul>

</div>


<h2 id="note-taking">Note Taking</h2>


Notenik is a desktop application for taking notes. Each Note is stored on disk as a plain text file, in an easy-to read format. That sounds pretty simple, doesn't it?

But that's just the start.

What else can you do with Notenik?

Read on....

<h2 id="navigate-and-maintain-your-notes">Navigate and Maintain your Notes</h2>


A toolbar with multiple buttons appears at the top of the user interface.

* **OK** -- Indicates that you have completed adding/editing the fields for the current Note.
* **+** -- Clear the data fields and prepare to add a new Note to the collection.
* **-** -- Delete the current Note.
* **&lt;&lt;** -- Display the first Note in the collection.
* **&lt;** -- Display the next Note in the collection.
* **&gt;** -- Display the next Note in the collection.
* **&gt;&gt;** -- Display the last Note in the collection.
* **Launch** -- Launch the link from thee current Note in your Web browser. (This may also be accomplished by selecting Launch from the dropdown that appears just to the left of the URL itself.)
* **Find** -- Looks for the text entered in the field just to the left of this button, and displays the first Note containing this text in any field, ignoring case. After finding the first occurrence, this button's text changes to **Again**, to allow you to search again for the next Note containing the specified text.

<h2 id="organize-your-notes-into-collections">Organize Your Notes into Collections</h2>


Each Collection of Notes is stored within its own folder (aka directory) on disk. Create as many different Collections as you would like. The File menu lets you work with Collections.

Each Note must have a unique title within its Collection. Each note's file name is based on its title, and each note's file name must also be unique within its folder.

One of the options on the File menu is to select from a list of recently opened Collections. You can adjust the number of Collections available on the Files tab within the Notenik Preferences. This is also the place where you can specify whether you would like Notenik to open the most recent Collection used when it starts up, or always open one specific Collection.

<h2 id="sync-to-the-cloud-and-to-other-devices">Sync to the Cloud and to other Devices</h2>


Since your Notes are stored as plain text files within folders, they can be easily synced between devices using Dropbox, iCloud Drive, or the sync service of your choice.

<h2 id="edit-your-notes-with-any-text-editor">Edit Your Notes with any Text Editor</h2>


Since your Notes are stored as plain text files, in an easy-to-read format, they can easily be edited using any text editor on any device.

<h2 id="add-a-link-to-each-note">Add a Link to each Note</h2>


Each Note can have a Link field associated with it, to store a hyperlink (aka URL). This is an easy way to bookmark websites that you'd like to refer to later, or simply to attach a Link to a Note.

<h2 id="organize-your-notes-with-tags">Organize Your Notes with Tags</h2>


Each note can have a Tags field associated with it.

Multiple Tags can be assigned to each Note, and each Tag can consist of multiple levels. Use a period to nest one Tag within another. Use a comma to separate multiple tags.

Use the Tags tab in the main window to see your Notes Collection organized by its Tags. By adding multiple levels to your Tags, you can effectively organize your Notes into an outline.

Tags are stored internally within a Note, so they sync easily along with the rest of the contents of each Note.

The "Favorites" tag by default identifies Notes you wish to appear in your favorites file (see the **Publish** section below). The "Startup" tag identifies Notes whose links you wish to have launched in your favorite Web Browser whenever Notenik launches, (so long as this is requested on the Favorites tab within the Notenik Preferences).

<h2 id="add-other-fields">Add Other Fields</h2>


A basic Note consists of a Title, a Tags line, a Link, plus the actual Body of the Note.

However, other fields can be added by providing a template file for a collection, named 'template.txt'. The template is in the same format as a Note, but with the specification of additional fields. (There's a command on the File menu that can be used to generate a template, which you can then modify with any text editor.) Notenik will then configure its UI dynamically to handle the fields specified in the template for that collection.

Following is a complete list of all the fields that can be used within a note.

**Title**: Each note in a collection must have a unique title. If the contents of the note file does not contain a title field, then the file name (without the extension) will be used as the title of the note.

**Tags**: Tags may be used to group related notes into categories. One or more tags may be associated with each note, and each tag may contain one or more sub-tags. A period or a slash may be used to separate one level of a tag from the next level, with the period being preferred. A comma or a semi-colon may be used to separate one tag from another, with the comma being preferred.

The "Favorites" tag may be used to identify favored notes within a collection. The "Startup" tag may be used to identify notes whose links you wish to have opened by an application when it first starts.

**Link**: A Hyperlink (aka URL).

**Author**: The author(s) of the note.

**Rating**: Your rating of the note, on a scale of one to five.

**Type**: The type of note.

**Status**: The state of the note, indicating its degree of completion. Status values are usually selected from the following standard list. Note that each status may be represented by a single digit and/or an associated label. The digits serve to place the values into an approximate life cycle sequence.

* 0 - Suggested
* 1 - Proposed
* 2 - Approved
* 3 - Planned
* 4 - Active
* 5 - Held
* 6 - Completed
* 7 - Pending Recurs
* 8 - Canceled
* 9 - Closed

**Date**: The date of the note, such as the date the note was officially published, or a due date for the note. The date may be expressed in any of a number of common formats. It may also be a partial date, such as a year, or a year and a month. It may or may not contain a specific time of day.

**Teaser**: An excerpt from the note used as a teaser in a list of notes. The teaser may be formatted using Markdown.

**Body**: The body of the note; in other words, an indicator that the document portion of the note follows. In this case the field value is expected on following lines. The Body field will always be treated as the final field in a note, to avoid having portions of the document inadvertently treated as fields.

In addition, the following two fields are derived from file system data about the file.

**Mod Date**: This is the last modification of the note, as maintained by the file system (not specified within the contents of the note, but available as note metadata).

**File Size**: This is the size of the file, in characters, as maintained by the file system (not specified within the contents of the note, but available as metadata).

<h2 id="create-backups">Create Backups</h2>


When you create a backup of a Collection, Notenik creates an exact copy of the current folder of Notes, and gives the new folder a name starting with the name of your source folder, and ending with the current date and time.

Additionally, on the Files tab of the Notenik preferences, you can specify whether you want Notenik to backup automatically, or to occasionally (on a weekly basis) prompt you to create a backup. And if you would like Notenik to automatically delete older backups, then you can specify the maximum number of backups to keep for any Collection.

<h2 id="sync-multiple-collections-to-a-common-folder">Sync Multiple Collections to a Common Folder</h2>


The Folder Sync tab on the Collection preferences allows the user to identify a common folder to which several different Notenik collections can be synced.

Such a common folder could then be conveniently accessed using [nvAlt](http://brettterpstra.com/projects/nvalt/), for example.

Each collection can have a different prefix assigned, and that prefix will then be used to keep the notes from the different collections separately identified within the common folder. The prefix will default to the folder name for the collection, with a trailing 's' removed if one is found, and with a dash added as a separator. A folder name of 'Bookmarks', for example, would result in a prefix of 'Bookmark - ' being appended to the front of each note as it is stored in the common folder.

The logic for the syncing works as follows.

A sweep of the entire common folder will be performed whenever syncing is first turned on for a collection, and henceforth whenever a collection with syncing already on is opened.

The sweep sync includes the following logic.

* For any common folder notes with a matching prefix, where the corresponding note does not already exist within the Notenik collection, the note will be added to the Notenik collection.

* For any Notenik notes where a matching nvAlt note is not found, the note will be added to the nvAlt folder.

* For any Notenik notes where a matching nvAlt note has been updated more recently than the matching Notenik note, the Notenik note will be updated to match the nvAlt note.

Once folder sync has been turned on for a collection, then every time that Notenik makes an update to any note within that collection, a parallel update will be made to the corresponding note within the common folder.

<h2 id="export-your-notes">Export Your Notes</h2>


Notenik allows you to export a Collection of Notes in any of several different formats.

You may filter the notes to be exported, for any of these formats, by adjusting the entries in the Tags Export preferences. You may specify one or more tags to be selected, so that only notes containing those tags will be exported. You may also suppress one or more tags, meaning that exported notes will have those tags removed from the resulting output.

For example, if you have a collection of blog entries stored as a Collection of Notes, and you have multiple blogs to which they are published, you can specify tags for the relevant blogs for each note, and then select only those notes when publishing a particular blog (and suppress the tags for the other blogs).

If you leave the Tags to Select field blank, then all Notes will be exported.

<h3 id="export-to-notenik">Export to Notenik</h3>


The Notes in this collection will be exported to a new folder in the same Notenik format.

<h3 id="export-to-opml">Export to OPML</h3>


The Notes in this collection will be exported to an XML-based Outline format that can be opened by an app such as OmniOutliner. The Tags in the collection will be used to create the outline.

Note that, for XML formats, the resulting file may contain invalid characters if those are present in your Notes.

<h3 id="export-to-tabdelimited">Export to Tab-Delimited</h3>


Each Note will be exported to one row/line, and each field will be exported to a separate column. This format is suitable for import into MS Excel, for example.

<h3 id="export-to-tabdelimited-for-ms-links">Export to Tab-Delimited for MS Links</h3>


Similar to tab-delimited, but the Title and link are concatenated into a single field, with a '#' character separating the title from the link. Some Microsoft apps can use this format to import each link into a special field that combines the title and the hyperlink into a single field.

<h3 id="export-to-xml">Export to XML</h3>


Your Notes will be exported to an XML format with each field represented as a separate tag.

Note that, for XML formats, the resulting file may contain invalid characters if those are present in your Notes.

<h3 id="publish-your-notes">Publish Your Notes</h3>


The publish option allows you to easily publish your Notes in a variety of useful formats. For example, you can easily publish your notes as a series of web pages.

To begin the publication process, select the Publish command from the File menu.

You will then see a window with the following fields available to you.

**Publish to**: You may use the Browse button above and to the right to select a folder on your computer to which you wish to publish your Notes. You may also enter or modify the path directly in the text box. When modifying this field, you will be prompted to specify whether you wish to update the existing publication location, or add a new one. By specifying that you wish to add a new one, you may create multiple publications, and then later select the publication of interest by using the drop-down arrow to the right of this field.

**Equivalent URL**: If the folder to which you are publishing will be addressable from the World Wide Web, then enter its Web address here.

**Templates**: This is the address of a folder containing one or more publishing templates. This will default to the location of the templates provided along with the application executable. You may use the Browse button above and to the right to pick a different location, if you have your own templates you wish to use for publishing.

**Select**: Use the drop-down list to select the template you wish to use.

	**Favorites Plus**: This template will produce the following files and formats.

	1. index.html -- This file is an index file with links to the other files. You can browse this locally by selecting **Browse local index** from the **File** menu.
	2. favorites.html -- This file tries to arrange all of the Notes you have tagged as "Favorites" into a four-column format that will fit on a single page.
	3. bookmark.html -- This file formats your URLs in the time-honored Netscape bookmarks format, suitable for import into almost any Web browser or URL manager.
	4. outline.html -- This is a dynamic html file that organizes your URLs within your tags, allowing you to reveal/disclose selected tags.

**Apply**: Press this button to apply the selected template. This will copy the contents of the template folder to the location specified above as the Publish to location.

**Publish Script**: Specify the location of the script to be used. The PSTextMerge templating system is the primary scripting language used for publishing. A PSTextMerge script will usually end with a '.tcz' file extension.

**Publish when**: You may specify publication 'On Close' (whenever you Quit the application or close a Collection), or 'On Demand'.

**Publish Now**: Press this button to publish to the currently displayed location. Note that, if you've specified 'On Demand', then this is the only time that publication will occur.

**View**: Select the local file location or the equivalent URL location.

**View Now**: Press this button to view the resulting Web site in your Web browser.

<h2 id="publish-a-favorites-page">Publish a Favorites Page</h2>


You can easily publish a single web page containing your favorite links arranged in a series of columns and rows. Use the Favorites tab on the Notenik preferences to specify the number of columns to appear, and the maximum number of rows to include in a single column.

<h2 id="findreplace-and-other-operations-on-collections">Find/Replace and Other Operations on Collections</h2>


The following operations are available from the Collection menu.

* Search for any text string using the Find box in the Toolbar.

* Enter a replacement value for a search string and perform a mass replace operation.

* Replace one Tag with another.

* Flatten the levels of all your Tags, making each level a separate Tag.

* Change all your tags to lower case letters.

* Validate all the links in your collection.

<h2 id="modify-the-look-and-feel">Modify the Look and Feel</h2>


The Notenik General preferences contain a number of options for modifying the program's look and feel. Feel free to experiment with these to find your favorite configuration. Some options may require you to quit and re-launch Notenik before the changes will take effect.

**SplitPane: Horizontal Split?**: Check the box to have the **List** and **Tags** appear on the left of the main screen, rather than the top.

**Deletion: Confirm Deletes?**: Check the box to have a confirmation dialog shown whenever you attempt to delete the selected Note.

**Software Updates: Check Automatically?**: Check the box to have Notenik check for newer versions whenever it launches.

**Check Now**: Click this button to check for a new version immediately.

**File Chooser**: If running on a Mac, you may wish to select AWT rather than Swing, to make your Open and Save dialogs appear more Mac-like. However, Swing dialogs may still appear to handle options that can't be handled by the native AWT chooser.

**Look and Feel**: Select from one of the available options to change the overall look and feel of the application.

**Menu Location**: If running on a Mac, you may wish to have the menus appear at the top of the screen, rather than at the top of the window.

<h2 id="system-requirements">System Requirements</h2>


Notenik is written in Java and can run on any reasonably modern operating system, including Mac OS X, Windows and Linux. Notenik requires a Java Runtime Environment (JRE), also known as a Java Virtual Machine (JVM). The version of this JRE/JVM must be at least 6. Visit [www.java.com](http://www.java.com) to download a recent version for most operating systems. Installation happens a bit differently under Mac OS X, but generally will occur fairly automatically when you try to launch a Java app for the first time.

Because Notenik may be run on multiple platforms, it may look slightly different on different operating systems, and will obey slightly different conventions (using the CMD key on a Mac, vs. an ALT key on a PC, for example).

<h2 id="installation">Installation</h2>


Download the latest version from [PowerSurgePub.com](http://www.powersurgepub.com/downloads.html). Decompress the downloaded file. Drag the resulting file or folder into the location where you normally store your applications. Double-click on the jar file (or the application, if you've downloaded the Mac app) to launch.

<h2 id="rights">Rights</h2>


Notenik Copyright 2013 - 2016 by Herb Bowie

Notenik is [open source software](http://opensource.org/osd). Source code is available at [GitHub](http://github.com/hbowie/notenik).

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

  [www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

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

