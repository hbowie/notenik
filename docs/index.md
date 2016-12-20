<!-- Generated using template product-user-guide-template.mdtoc -->
<!-- Generated using template notenik.md -->
<h1 id="notenik">Notenik</h1>


Version: 2.40, released 20 Dec 2016 <[details](http://www.powersurgepub.com/products/notenik/versions.html)>


<h2 id="table-of-contents">Table of Contents</h2>

<div id="toc">
  <ul>
    <li>
      <a href="#introduction">Introduction</a>
    </li>
    <li>
      <a href="#principles">Principles</a>
    </li>
    <li>
      <a href="#data-format">Data Format</a>
    </li>
    <li>
      <a href="#example">Example</a>
    </li>
    <li>
      <a href="#standard-fields">Standard Fields</a>
      <ul>
        <li>
          <a href="#title">Title</a>
        </li>
        <li>
          <a href="#tags">Tags</a>
        </li>
        <li>
          <a href="#link">Link</a>
        </li>
        <li>
          <a href="#author">Author</a>
        </li>
        <li>
          <a href="#rating">Rating</a>
        </li>
        <li>
          <a href="#type">Type</a>
        </li>
        <li>
          <a href="#status">Status</a>
        </li>
        <li>
          <a href="#seq">Seq</a>
        </li>
        <li>
          <a href="#date">Date</a>
        </li>
        <li>
          <a href="#index">Index</a>
        </li>
        <li>
          <a href="#teaser">Teaser</a>
        </li>
        <li>
          <a href="#body">Body</a>
        </li>
        <li>
          <a href="#mod-date">Mod Date</a>
        </li>
        <li>
          <a href="#file-size">File Size</a>
        </li>
      </ul>

    </li>
    <li>
      <a href="#tables-rows-and-columns">Tables, Rows and Columns</a>
    </li>
    <li>
      <a href="#notenik-app">Notenik App</a>
      <ul>
        <li>
          <a href="#navigate-and-maintain-your-notes">Navigate and Maintain your Notes</a>
        </li>
        <li>
          <a href="#organize-your-notes-into-collections">Organize Your Notes into Collections</a>
        </li>
        <li>
          <a href="#use-multiple-fields-within-your-notes">Use Multiple Fields Within Your Notes</a>
        </li>
        <li>
          <a href="#edit-your-notes-with-any-text-editor">Edit Your Notes with any Text Editor</a>
        </li>
        <li>
          <a href="#use-markdown-in-the-body-of-your-notes">Use Markdown in the Body of your Notes</a>
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
          <a href="#importexport-your-notes">Import/Export Your Notes</a>
        </li>
        <li>
          <a href="#publish-your-notes">Publish Your Notes</a>
        </li>
        <li>
          <a href="#publish-a-favorites-page">Publish a Favorites Page</a>
        </li>
        <li>
          <a href="#reports">Reports</a>
        </li>
        <li>
          <a href="#purge-closed-notes">Purge Closed Notes</a>
        </li>
        <li>
          <a href="#findreplace-and-other-operations-on-collections">Find/Replace and Other Operations on Collections</a>
        </li>
        <li>
          <a href="#tailor-the-appearance-of-the-note-display">Tailor the Appearance of the Note Display</a>
        </li>
        <li>
          <a href="#modify-the-look-and-feel">Modify the Look and Feel</a>
        </li>
      </ul>

    </li>
    <li>
      <a href="#rights">Rights</a>
    </li>
    <li>
      <a href="#source-code">Source Code</a>
    </li>
    <li>
      <a href="#downloads-and-links">Downloads and Links</a>
    </li>
  </ul>

</div>



<h2 id="introduction">Introduction</h2>


Notenik is a system for recording, collecting and referencing notes.

Notenik is really three things:

1. A data format;
2. An application that enables users to create, organize and access notes in this format;
3. A set of related tools that can do things with notes formatted following Notenik conventions.

<h2 id="principles">Principles</h2>


The Notenik design is based on the following principles.

* A Note is something you record using a mixture of letters, numbers and punctuation.
* A Note may contain one or more Fields, each identified by its own Label.
* At its most basic, a Note has a Title Field and a Body Field.
* Notes should be extensible, so that they can be used in a variety of ways.
* Each Note is part of a Collection.
* Notes can be organized within a Collection using Tags.
* Each User may have one or more Collections.
* Collections of Notes should be open, easily accessible, and portable: they should be accessible using a variety of tools, on a variety of platforms, with the ability to easily sync them across devices.

If this set of principles sounds appealing to you, then Notenik may be just what you've been looking for.

<h2 id="data-format">Data Format</h2>


Each Note is stored as a separate text file, in the [UTF-8](https://en.wikipedia.org/wiki/UTF-8) format, capable of being read and modified by any text editor, on almost any computer system in the world.

A note file may be stored with any of the following file extensions:

* .markdown
* .md
* .mdown
* .mdtext
* .mkdown
* .nnk
* .notenik
* .text
* .txt

A Collection of Notes is stored as a folder (aka directory). A collection resides on a local disk or file share, but may be synced to the cloud and to other devices and users via a service such as [Dropbox](http://www.dropbox.com) or [iCloud](http://www.apple.com/icloud/icloud-drive/).

Each Note contains a number of Fields.

At its most basic, a note consists of a Title Field and a Body Field.

Each Field in a Note consists of the Field Label, followed by a colon and one or more spaces, followed by the Field Value.

A Field Label must always start at the beginning of a line. A Field Label may not consist of more than 48 characters, and may not contain a comma (',').

The Field Value may be specified on the same line, and/or on one or more following lines.

The Body Field, if present, will always be the last Field in a Note, since all following text will be assumed to be part of the Body (even if it contains strings of text that might otherwise appear to be additional Field Labels).

Each Note must have a Title that is unique within its Collection. Each Note's file name is based on its Title, and each Note's file name must also be unique within its folder.

Each Field Label may be considered to have a *proper form* (including capitalization, spaces and punctuation), and a *common form* (the proper form without capitalization, whitespace or punctuation). The common form is considered to be the key identifier for the Field, so that any variations of the Label that include the same letters and digits in the same sequence will be considered equivalent.

The Body of a Note may be formatted using the [Markdown](https://daringfireball.net/projects/markdown/) syntax. And the Fields within each Note are generally formatted following the [MultiMarkdown Metadata](http://fletcher.github.io/MultiMarkdown-4/metadata.html) syntax.

<h2 id="example">Example</h2>


The Notenik data format is intended be easily read and edited by humans, using any text editor. Following is an example of a Note.

    Title:  The unique title for this note

    Author: The author of the Note

    Date:   2016-12-13

    Status: 0 - Suggested

    Type:   The type of note

    Seq:    Rev Letter or Version Number

    Tags:   One or more tags, separated by commas

    Link:   http://notenik.com

    Rating: 5

    Index:  Index Term 1;

    Teaser:

    A brief sample of the note

    Body:

    The substance of the note


By default, Notenik shows only four Fields for a Note: Title, Link, Tags and Body. However, this default may be altered by placing a file named 'template.txt' or 'template.md' within a Collection's folder. Such a file should be in the normal Notenik format, as depicted above, although the Field Labels specified need not have any accompanying values. When such a template file is found, the field names found in this file will be used as the fields to be displayed and maintained for that Collection. This file should be created using a text editor -- not using the Notenik app itself for this purpose. The file extension used for the template file will be used as the file extension for Notes subsequently created within the Collection.

<h2 id="standard-fields">Standard Fields</h2>


The following Field Labels are typically expected to be used within Notes, and often have some special rules associated with them. Note that not all Collections need use all of these fields, and not all Notes within a Collection need specify all of the Fields used within the Collection. Other fields may be specified, and will be treated as simple text fields.

<h3 id="title">Title</h3>


Each Note in a Collection must have a unique Title. If a Title is not specified within a Note file, then the file name (without the extension) will be used as the Title of the Note.

<h3 id="tags">Tags</h3>


Tags may be used to group related notes into categories. One or more tags may be associated with each note, and each tag may contain one or more sub-tags. A period or a slash may be used to separate one level of a tag from the next level, with the period being preferred. A comma or a semi-colon may be used to separate one tag from another, with the comma being preferred.

The "Favorites" tag may be used to identify favored notes within a collection, and these notes will appear on the Favorites Report. The "Startup" tag may be used to identify notes whose links you wish to have opened by the application when it first starts.

<h3 id="link">Link</h3>


A Hyperlink (aka URL) that can be opened by a Web browser. Notice that a Collection of Notes with Links is essentially a set of Web bookmarks.

<h3 id="author">Author</h3>


The author(s) of the Note.

<h3 id="rating">Rating</h3>


Your rating of the note, on a scale of one to five.

<h3 id="type">Type</h3>


The type of note. Any values may be used to distinguish between different types of notes within a collection.

<h3 id="status">Status</h3>


The state of the note, indicating its degree of completion. Status values are usually selected from the following standard list. Note that each status may be represented by a single digit and/or an associated label. The digits serve to place the values into an approximate life cycle sequence.

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

The labels may be modified by placing a series of integer + label pairs in the data area of the relevant template file, with separating punctuation. The template line might look something like this:

	Status: 1 - Idea; 4 - In Work; 9 - Published;

<h3 id="seq">Seq</h3>


A revision letter or version number or any other value used to keep notes in a prescribed sequence.

<h3 id="date">Date</h3>


The date of the note, such as the date the note was officially published, or a due date for the note. The date may be expressed in any of a number of common formats. It may also be a partial date, such as a year, or a year and a month. It may or may not contain a specific time of day.

<h3 id="index">Index</h3>


One or more terms under which this note should appear in an index of the collection. Multiple terms can be placed on separate Index lines within the note, or can be entered as one field by using a semi-colon (';') to end each term entry. A URL to be associated with the term can be placed in parentheses following the term. If the term should reference a specific anchor within the note, then the anchor can be specified by preceding it with the usual pound sign ('#').

<h3 id="teaser">Teaser</h3>


An excerpt from the note used as a teaser in a list of notes. The teaser may be formatted using Markdown.

<h3 id="body">Body</h3>


The Body of the Note; in other words, an indicator that the document portion of the Note follows. In this case the Field Value is expected on following lines. The Body Field will always be treated as the final field in a Note, to avoid having portions of the document inadvertently treated as Fields.

Note that, if the 'Body:' Label is omitted, then Notenik will by default consider any block of text following other fields to constitute the Body of the Note.

The Body of a Note may be formatted using the [Markdown](https://daringfireball.net/projects/markdown/) syntax.

<h3 id="mod-date">Mod Date</h3>


This is the last modification of the Note, as maintained by the file system (not specified within the contents of the Note, but available as Note metadata).

<h3 id="file-size">File Size</h3>


This is the size of the file, in characters, as maintained by the file system (not specified within the contents of the note, but available as metadata).

<h2 id="tables-rows-and-columns">Tables, Rows and Columns</h2>


Each Collection may be thought of as a Table, with each Note in the Collection treated as a Row, and each Field in the collection represented as a Column, with the Field Labels used as Column Headings.

When considered in this way, each Collection may be Viewed in a grid, Sorted on one or more columns, Filtered using one or more criteria, Output in one or more formats, and used to generate things like web pages through the use of Templates.

In fact, all of this and more can be accomplished through the use of the [PSTextMerge](http://www.powersurgepub.com/products/pstextmerge/index.html) application.

<h2 id="notenik-app">Notenik App</h2>


The Notenik App looks like this.

![Notenik Screenshot](http://www.powersurgepub.com/screenshots/notenik.png "Notenik Screenshot")

The Notenik application can perform the following functions.

<h3 id="navigate-and-maintain-your-notes">Navigate and Maintain your Notes</h3>


A toolbar with multiple buttons appears at the top of the user interface.

* **OK** -- Indicates that you have completed adding/editing the fields for the current Note.
* **+** -- Clear the data fields and prepare to add a new Note to the collection.
* **-** -- Delete the current Note.
* **&lt;&lt;** -- Display the first Note in the collection.
* **&lt;** -- Display the next Note in the collection.
* **&gt;** -- Display the next Note in the collection.
* **&gt;&gt;** -- Display the last Note in the collection.
* **Launch** -- Launch the link from the current Note in your Web browser. (This may also be accomplished by selecting Launch from the dropdown that appears just to the left of the URL itself.)
* **Find** -- Looks for the text entered in the field just to the left of this button, and displays the first Note containing this text in any field, ignoring case. After finding the first occurrence, this button's text changes to **Again**, to allow you to search again for the next Note containing the specified text.

<h3 id="organize-your-notes-into-collections">Organize Your Notes into Collections</h3>


Each Collection of Notes is stored within its own folder (aka directory) on disk. Create as many different Collections as you would like. The File menu lets you work with Collections.

Each Note must have a unique title within its Collection. Each note's file name is based on its title, and each note's file name must also be unique within its folder.

One of the options on the File menu is to select from a list of recently opened Collections. You can adjust the number of Collections available on the Files tab within the Notenik Preferences. This is also the place where you can specify whether you would like Notenik to open the most recently used Collection used when it starts up, or always open one specific Collection.

<h3 id="use-multiple-fields-within-your-notes">Use Multiple Fields Within Your Notes</h3>


Each note contains a number of fields. At its most basic, a note consists of a Title field and a Body field. By default, a Tags field and a Link field will also be included in every Note. Use a Template file to specify other fields to be used within a particular Collection.

<h3 id="edit-your-notes-with-any-text-editor">Edit Your Notes with any Text Editor</h3>


Since your Notes are stored as plain text files, in an easy-to-read format, they can easily be edited using any text editor on any device. In fact, from within Notenik, you can select Text Edit Note from the Note menu to have the current Note opened in your preferred text editor.

<h3 id="use-markdown-in-the-body-of-your-notes">Use Markdown in the Body of your Notes</h3>


You are encouraged to use [Markdown][] syntax when creating the Body of your Notes. In fact, there is an option under the Note menu to generate HTML from the Body of a Note, and to either copy it to your system clipboard, in preparation for pasting the HTML somewhere else, or to store it in a file. You can even specify a preferred HTML folder for each of your Collections, in your Collection Preferences.

<h3 id="organize-your-notes-with-tags">Organize Your Notes with Tags</h3>


Use the Tags tab in the main window to see your Notes Collection organized by its Tags. By adding multiple levels to your Tags, you can effectively organize your Notes into an outline.

The "Favorites" tag by default identifies Notes you wish to appear in your favorites file (see the **Publish** section below). The "Startup" tag identifies Notes whose links you wish to have launched in your favorite Web Browser whenever Notenik launches, (so long as this is requested on the Favorites tab within the Notenik Preferences).

<h3 id="add-other-fields">Add Other Fields</h3>


A basic Note consists of a Title, a Tags line, a Link, plus the actual Body of the Note.

However, other fields can be added by providing a template file for a collection, named 'template.txt' (or 'template.md' if you'd prefer Notenik to use the '.md' file extension for notes in this collection). The template is in the same format as a Note, but with the specification of additional fields. (There's a command on the File menu that can be used to generate a template, which you can then modify with any text editor.) Notenik will then configure its UI dynamically to handle the fields specified in the template for that collection.

<h3 id="create-backups">Create Backups</h3>


When you create a backup of a Collection, Notenik creates an exact copy of the current folder of Notes, and gives the new folder a name starting with the name of your source folder, and ending with the current date and time.

Additionally, on the Files tab of the Notenik preferences, you can specify whether you want Notenik to backup automatically, or to occasionally (on a weekly basis) prompt you to create a backup. And if you would like Notenik to automatically delete older backups, then you can specify the maximum number of backups to keep for any Collection.

<h3 id="sync-multiple-collections-to-a-common-folder">Sync Multiple Collections to a Common Folder</h3>


The Folder Sync tab on the Collection preferences allows the user to identify a common folder to which several different Notenik collections can be synced.

Such a common folder could then be conveniently accessed using [nvAlt][], for example.

Each collection can have a different prefix assigned, and that prefix will then be used to keep the notes from the different collections separately identified within the common folder. The prefix will default to the folder name for the collection, with a trailing 's' removed if one is found, and with a dash added as a separator. A folder name of 'Bookmarks', for example, would result in a prefix of 'Bookmark - ' being appended to the front of each note as it is stored in the common folder.

The logic for the syncing works as follows.

A sweep of the entire common folder will be performed whenever syncing is first turned on for a collection, and henceforth whenever a collection with syncing already on is opened.

The sweep sync includes the following logic.

* For any common folder notes with a matching prefix, where the corresponding note does not already exist within the Notenik collection, the note will be added to the Notenik collection.

* For any Notenik notes where a matching nvAlt note is not found, the note will be added to the nvAlt folder.

* For any Notenik notes where a matching nvAlt note has been updated more recently than the matching Notenik note, the Notenik note will be updated to match the nvAlt note.

Once folder sync has been turned on for a collection, then every time that Notenik makes an update to any note within that collection, a parallel update will be made to the corresponding note within the common folder.

<h3 id="importexport-your-notes">Import/Export Your Notes</h3>


Notenik allows you to export a Collection of Notes in any of several different formats.

You may filter the notes to be exported, for any of these formats, by adjusting the entries in the Tags Export preferences. You may specify one or more tags to be selected, so that only notes containing those tags will be exported. You may also suppress one or more tags, meaning that exported notes will have those tags removed from the resulting output.

For example, if you have a collection of blog entries stored as a Collection of Notes, and you have multiple blogs to which they are published, you can specify tags for the relevant blogs for each note, and then select only those notes when publishing a particular blog (and suppress the tags for the other blogs).

If you leave the Tags to Select field blank, then all Notes will be exported.

Under the File menu you will find options to Import and Export your Notes.

<h4 id="importexport-in-notenik-format">Import/Export in Notenik Format</h4>


You can import and export your Notes in this collection from/to a folder in the same Notenik format.

<h4 id="export-to-opml">Export to OPML</h4>


The Notes in this collection will be exported to an XML-based Outline format that can be opened by an app such as OmniOutliner. The Tags in the collection will be used to create the outline.

Note that, for XML formats, the resulting file may contain invalid characters if those are present in your Notes.

<h4 id="importexport-in-tabdelimited-format">Import/Export in Tab-Delimited Format</h4>


Each Note will be represented as one row/line, and each field will be represented in a separate column. This format is suitable for import into MS Excel, for example.

<h4 id="export-to-tabdelimited-for-ms-links">Export to Tab-Delimited for MS Links</h4>


Similar to tab-delimited, but the Title and link are concatenated into a single field, with a '#' character separating the title from the link. Some Microsoft apps can use this format to import each link into a special field that combines the title and the hyperlink into a single field.

<h4 id="importexport-in-xml-format">Import/Export in XML Format</h4>


Your Notes will be represented in an XML format with each field represented as a separate tag.

Note that, for XML formats, an exported file may contain invalid characters if those are present in your Notes.

<h3 id="publish-your-notes">Publish Your Notes</h3>


The publish option allows you to easily publish your Notes in a variety of useful formats. For example, you can easily publish your notes as a series of web pages.

To begin the publication process, select the Publish command from the File menu.

You will then see a window with the following fields available to you.

**Publish to**: You may use the Browse button above and to the right to select a folder on your computer to which you wish to publish your Notes. You may also enter or modify the path directly in the text box. When modifying this field, you will be prompted to specify whether you wish to update the existing publication location, or add a new one. By specifying that you wish to add a new one, you may create multiple publications, and then later select the publication of interest by using the drop-down arrow to the right of this field.

**Equivalent URL**: If the folder to which you are publishing will be addressable from the World Wide Web, then enter its Web address here.

**Templates**: This is the address of a folder containing one or more publishing templates. This will default to the location of the templates provided along with the application executable. You may use the Browse button above and to the right to pick a different location, if you have your own templates you wish to use for publishing.

**Select**: Use the drop-down list to select the template you wish to use.

* **Favorites Plus**: This template will produce the following files and formats.

	1. index.html -- This file is an index file with links to the other files. You can browse this locally by selecting **Browse local index** from the **File** menu.
	2. favorites.html -- This file tries to arrange all of the Notes you have tagged as "Favorites" into a four-column format that will fit on a single page.
	3. bookmark.html -- This file formats your URLs in the time-honored Netscape bookmarks format, suitable for import into almost any Web browser or URL manager.
	4. outline.html -- This is a dynamic html file that organizes your URLs within your tags, allowing you to reveal/disclose selected tags.

**Apply**: Press this button to apply the selected template. This will copy the contents of the template folder to the location specified above as the Publish to location.

**Publish Script**: Specify the location of the script to be used. The [PSTextMerge][] templating system is the primary scripting language used for publishing. A PSTextMerge script will usually end with a '.tcz' file extension.

**Publish when**: You may specify publication 'On Close' (whenever you Quit the application or close a Collection), or 'On Demand'.

**Publish Now**: Press this button to publish to the currently displayed location. Note that, if you've specified 'On Demand', then this is the only time that publication will occur.

**View**: Select the local file location or the equivalent URL location.

**View Now**: Press this button to view the resulting Web site in your Web browser.

<h3 id="publish-a-favorites-page">Publish a Favorites Page</h3>


You can easily publish a single web page containing your favorite links arranged in a series of columns and rows. Use the Favorites tab on the Notenik preferences to specify the number of columns to appear, and the maximum number of rows to include in a single column.

<h3 id="reports">Reports</h3>


The Reports menu allows you to select from a list of reports, and then generate the report of your choice. Reports are generally HTML web pages generated from PSTextMerge scripts.

Any number of report scripts and templates may be placed within a folder named 'reports' that appears inside each folder of notes.

If a 'reports' folder has not yet been created and populated for a particular Collection, then a set of standard reports will be loaded from the application's resources folder.

Reports are generally a simpler and more straightforward approach to the Publish function described above.

<h3 id="purge-closed-notes">Purge Closed Notes</h3>


Under the File menu you'll find an option to Purge closed notes. Notes with a Status of Canceled or Closed will be purged. You may choose to discard the purged notes, or to copy them to an archive folder.

<h3 id="findreplace-and-other-operations-on-collections">Find/Replace and Other Operations on Collections</h3>


The following operations are available from the Collection menu.

* Search for any text string using the Find box in the Toolbar.

* Enter a replacement value for a search string and perform a mass replace operation.

* Replace one Tag with another.

* Flatten the levels of all your Tags, making each level a separate Tag.

* Change all your tags to lower case letters.

* Validate all the links in your collection.

<h3 id="tailor-the-appearance-of-the-note-display">Tailor the Appearance of the Note Display</h3>


The Display tab in the Notenik preferences allows you to change the font, font size, and foreground and background colors used for the Display of each Note.

<h3 id="modify-the-look-and-feel">Modify the Look and Feel</h3>


The Notenik General preferences contain a number of options for modifying the program's look and feel. Feel free to experiment with these to find your favorite configuration. Some options may require you to quit and re-launch Notenik before the changes will take effect.

**SplitPane: Horizontal Split?**: Check the box to have the **List** and **Tags** appear on the left of the main screen, rather than the top.

**Deletion: Confirm Deletes?**: Check the box to have a confirmation dialog shown whenever you attempt to delete the selected Note.

**Software Updates: Check Automatically?**: Check the box to have Notenik check for newer versions whenever it launches.

**Check Now**: Click this button to check for a new version immediately.

**File Chooser**: If running on a Mac, you may wish to select AWT rather than Swing, to make your Open and Save dialogs appear more Mac-like. However, Swing dialogs may still appear to handle options that can't be handled by the native AWT chooser.

**Look and Feel**: Select from one of the available options to change the overall look and feel of the application.

**Menu Location**: If running on a Mac, you may wish to have the menus appear at the top of the screen, rather than at the top of the window.

<h2 id="rights">Rights</h2>


Notenik is free and open source software.

Notenik Software, Specifications and Documentation are all Copyright 2009 - 2016 by [Herb Bowie](http://www.herbbowie.com/).

Licensed under the Apache License, Version 2.0 (the "License"); you may not use Notenik except in compliance with the License. You may obtain a copy of the License at [www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0). Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

<h2 id="source-code">Source Code</h2>


Notenik software is written in [Java][javanet] using the [Netbeans IDE][netbeans].

Source code for the Notenik app is available at [github.com/hbowie/notenik](https://github.com/hbowie/notenik).

Java classes for Notenik data access are available at [github.com/hbowie/psdatalib](https://github.com/hbowie/psdatalib) in the 'com.powersurgepub.psdatalib.notenik' package.

<h2 id="downloads-and-links">Downloads and Links</h2>


The Notenik app can be downloaded in either of two forms. The first is packaged as a typical Mac App, and can only be run on macOS (formerly known as OS X). The second contains a folder full of jar files, and can be run on pretty much any operating system. Double-click on the appropriate jar file to launch the corresponding app.

Because Notenik may be run on multiple platforms, it may look slightly different on different operating systems, and will obey slightly different conventions (using the CMD key on a Mac, vs. an ALT key on a PC, for example).

Note that you may receive various warnings that the software is written in Java and/or that the application code has not been signed. So long as you have downloaded the software from [Notenik.com](http://www.notenik.com) or [PowerSurgePub.com](http://www.powersurgepub.com) then you should be safe to ignore such warnings and proceed to happily use the software.

If you see a message saying you need Java, then you can visit [Java.com](http://www.java.com) to download a recent version for most operating systems.

* [Notenik Mac App](http://www.notenik.net/notenik.dmg)
* [PowerSurge Publishing Omni Pack](http://www.powersurgepub.com/downloads/pspub-omni-pack.zip)
* [Markdown Syntax](https://daringfireball.net/projects/markdown/syntax)


[javacom]:      http://www.java.com
[javanet]:      http://www.java.net
[netbeans]:		https://netbeans.org
[pspub]:        http://www.powersurgepub.com/
[downloads]:    http://www.powersurgepub.com/downloads.html
[osd]:		    http://opensource.org/osd
[gnu]:          http://www.gnu.org/licenses/
[apache]:	    http://www.apache.org/licenses/LICENSE-2.0.html
[markdown]:		http://daringfireball.net/projects/markdown/
[multimarkdown]:  http://fletcher.github.com/peg-multimarkdown/
[wikiq]:     	http://www.wikiquote.org
[support]:   	mailto:support@powersurgepub.com
[fortune]:   	http://en.wikipedia.org/wiki/Fortune_(Unix)
[opml]:      	http://en.wikipedia.org/wiki/OPML
[textile]:   	http://en.wikipedia.org/wiki/Textile_(markup_language)
[pw]:        	http://www.portablewisdom.org
[store]:     	http://www.powersurgepub.com/store.html
[pegdown]:   	https://github.com/sirthias/pegdown/blob/master/LICENSE
[parboiled]: 	https://github.com/sirthias/parboiled/blob/master/LICENSE
[Mathias]:   	https://github.com/sirthias
[template]:     http://www.powersurgepub.com/products/pstextmerge/user-guide.html#template-file-format
[mozilla]:   	http://www.mozilla.org/MPL/2.0/
[nvalt]:    	http://brettterpstra.com/projects/nvalt/
[pstextmerge]: 	http://www.powersurgepub.com/products/pstextmerge/index.html

