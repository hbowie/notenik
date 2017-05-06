<!-- Generated using template product-user-guide-template.mdtoc -->
<!-- Generated using template notenik.md -->
<h1 id="notenik">Notenik</h1>


<h2 id="introduction-to-notenik">Introduction to Notenik</h2>


Let me introduce you to [Notenik](http://notenik.net), a simple but powerful system for taking, collecting and referencing notes.

This introduction describes Notenik [Version 2.80](http://www.powersurgepub.com/products/notenik/versions.html).

<h2 id="table-of-contents">Table of Contents</h2>

<div id="toc">
  <ul>
    <li>
      <a href="#introduction-to-notenik">Introduction to Notenik</a>
    </li>
    <li>
      <a href="#motivations">Motivations</a>
    </li>
    <li>
      <a href="#the-notenik-application">The Notenik Application</a>
    </li>
    <li>
      <a href="#help-using-notenik">Help Using Notenik</a>
    </li>
    <li>
      <a href="#each-note-has-a-title-and-a-body">Each Note has a Title and a Body</a>
    </li>
    <li>
      <a href="#each-note-is-its-own-text-file">Each Note is its own Text File</a>
    </li>
    <li>
      <a href="#notes-are-collected-into-folders">Notes are Collected into Folders</a>
    </li>
    <li>
      <a href="#add-delete-and-update-your-notes">Add, Delete and Update Your Notes</a>
    </li>
    <li>
      <a href="#view-your-notes-as-a-list">View Your Notes as a List</a>
    </li>
    <li>
      <a href="#sync-your-notes">Sync Your Notes</a>
    </li>
    <li>
      <a href="#format-the-body-of-a-note-using-markdown">Format the Body of a Note using Markdown</a>
    </li>
    <li>
      <a href="#add-other-fields-to-your-notes">Add Other Fields to Your Notes</a>
    </li>
    <li>
      <a href="#organize-your-notes-with-tags">Organize Your Notes with Tags</a>
    </li>
    <li>
      <a href="#add-a-link-field-to-make-a-bookmark">Add a Link Field to Make a Bookmark</a>
    </li>
    <li>
      <a href="#override-default-fields-with-a-template-file">Override Default Fields with a Template file</a>
    </li>
    <li>
      <a href="#a-note-file-template">A Note File Template</a>
    </li>
    <li>
      <a href="#sequence-your-notes">Sequence Your Notes</a>
    </li>
    <li>
      <a href="#track-the-status-of-your-notes">Track the Status of Your Notes</a>
    </li>
    <li>
      <a href="#date-your-notes">Date Your Notes</a>
    </li>
    <li>
      <a href="#specify-date-recurs-rules">Specify Date Recurs Rules</a>
    </li>
    <li>
      <a href="#track-your-tasks">Track Your Tasks</a>
    </li>
    <li>
      <a href="#index-your-notes">Index Your Notes</a>
    </li>
    <li>
      <a href="#find-one-or-more-notes-containing-specified-text">Find One or More Notes Containing Specified Text</a>
    </li>
    <li>
      <a href="#use-other-special-fields">Use Other Special Fields</a>
    </li>
    <li>
      <a href="#adjust-notenik-to-suit-your-preferences">Adjust Notenik to Suit Your Preferences</a>
    </li>
    <li>
      <a href="#tables-rows-and-columns">Tables, Rows and Columns</a>
    </li>
    <li>
      <a href="#run-reports">Run Reports</a>
    </li>
    <li>
      <a href="#publish-your-notes">Publish Your Notes</a>
    </li>
    <li>
      <a href="#publish-a-favorites-page">Publish a Favorites Page</a>
    </li>
    <li>
      <a href="#create-backups">Create Backups</a>
    </li>
    <li>
      <a href="#import-and-export-your-notes">Import and Export Your Notes</a>
    </li>
    <li>
      <a href="#sync-multiple-collections-to-a-common-folder">Sync Multiple Collections to a Common Folder</a>
    </li>
    <li>
      <a href="#view-the-log">View the Log</a>
    </li>
    <li>
      <a href="#rights">Rights</a>
    </li>
    <li>
      <a href="#support">Support</a>
    </li>
  </ul>

</div>



<h2 id="motivations">Motivations</h2>


I'll start off by explaining my primary motivations behind the creation of Notenik.

1. I love notes. I like being able to write down some words -- a few or a bunch -- about something that interests me and then refer back to them later.

2. I like the simplicity and flexibility of notes.

3. I want to store my notes in an open, non-proprietary format that I can easily write, read and understand, but also in a format that can be usefully manipulated by a variety of software tools.

4. I want my notes to be portable. I want to be able to easily move them around from one device to another, from one platform to another, without losing anything in the process.

5. I want my notes to be durable. I don't want to have to worry about not being able to read them a few years down the road because some piece of software is no longer available.

6. I want some basic application software for working with my notes to be free and open-source and available on as many platforms as possible.

7. I want to be able to organize my notes in a variety of useful ways.

8. I want to be able to usefully extend this basic idea of a note in as many ways as possible, while still remaining true to all of my motivations above.

I'm sharing these motivations upfront because, if these same ideas appeal to you, then there's a good chance it will be worth your while to read a bit further. On the other hand, if you have different sorts of interests, then it's probably best to just stop now and move on.

<h2 id="the-notenik-application">The Notenik Application</h2>


The Notenik App looks like this.

![Notenik Screenshot](http://www.powersurgepub.com/screenshots/notenik.png "Notenik Screenshot")

Notenik software is written in [Java](http://www.java.net) using the [Netbeans IDE](https://netbeans.org). Source can be found on [GitHub](https://github.com/hbowie/notenik). Executables can be downloaded in either of two forms. The first is packaged as a typical Mac Application, and can only be run on macOS (formerly known as OS X). The second contains a folder full of jar files, and can be run on Windows and on Linux, as well as on macOS. Double-click on the 'notenik.jar' file to launch the application when using this second form.

Although the Notenik application can be run on other platforms, my preference is for the Mac, and so the instructions that follow will primarily focus on the Mac user interface. Users on other platforms should be able to easily adjust, where necessary, for their systems.

You may receive various warnings that the software is written in Java and/or that the application code has not been signed. So long as you have downloaded the software from [Notenik.com](http://www.notenik.com) or [PowerSurgePub.com](http://www.powersurgepub.com) then you should be safe to ignore such warnings and proceed to happily use the software.

If you see a message saying you need Java, then you can visit [Java.com](http://www.java.com) to download a recent version for most operating systems.

If you'd like to learn more about Notenik, then it's probably best to download the application now and get it launched, so that you can follow along with the examples given below.

* [Notenik Mac App](http://www.notenik.net/notenik.dmg)
* [PowerSurge Publishing Omni Pack](http://www.powersurgepub.com/downloads/pspub-omni-pack.zip)

From this point on, I'll introduce one concept at a time.

<h2 id="help-using-notenik">Help Using Notenik</h2>


You're already reading this intro, so obviously I don't need to tell you how to find this.

But here's a few other help items that are available, including some that may not be so obvious.

This User Guide is available as a web page from the [Notenik](http://www.notenik.net) site, from the [PowerSurge Publishing](http://www.powersurgepub.com) site, and from the Notenik Help Menu.

But the same content is also available as a Collection of Help Notes, by selecting the corresponding Open Command from Notenik's File menu. You may find it a little easier to read and navigate in this form, plus you can practice using Notenik as you read the Intro!

The Help Menu also provides access to a few other helpful items that you can explore on your own. You may wish to memorize the keyboard shortcut to Reduce the Window Size, since this can be handy if your Notenik window ends up missing because you've shifted your Monitor setup since the last time you used it.

<h2 id="each-note-has-a-title-and-a-body">Each Note has a Title and a Body</h2>


In its most basic form, a Note consists of a Title and a Body.

A Title consists of a few words telling you what the Note is about.

The Body consists of the text of the Note, containing as many words as you like.

If no Title is specified within a Note file, then the file name (without the extension) will be used as the Title of the Note.

<h2 id="each-note-is-its-own-text-file">Each Note is its own Text File</h2>


Each Note is stored as a separate text file, in the [UTF-8](https://en.wikipedia.org/wiki/UTF-8) format, capable of being read and modified by any text editor, on almost any computer system in the world.

The file name is generally identical to the Note's title, with the exception of a few occasional tweaks to avoid running afoul of various common operating system limitations.

Any of the following file extensions may be used:

* .markdown
* .md
* .mdown
* .mdtext
* .mkdown
* .nnk
* .notenik
* .text
* .txt

The two preferred extensions are '.txt' (short for text) and '.md' (short for markdown -- more on this later).

Use the Text Edit Note command on the Note menu to open the currently displayed Note in your default text editor.

<h2 id="notes-are-collected-into-folders">Notes are Collected into Folders</h2>


Each Note is part of a Collection, and each Collection is stored in its own folder (aka directory). Each Note within a Collection must have its own unique Title.

You can organize your Notes into as many Collections as you would like, and store each one whever you would like.

The Notenik application can only open one Collection at a time, but you can easily switch from one to another using the Open Recent command on the File menu.

You can adjust the number of Recent Files to retain using the File Tab in the Notenik preferences. This is also the place where you can specify whether you would like Notenik to open the most recently used Collection used when it starts up, or always open one specific Collection.

You might want to use the New command on the File menu to create your first Collection now, if you don't already have one created.

<h2 id="add-delete-and-update-your-notes">Add, Delete and Update Your Notes</h2>


The Display tab shows you what your Note looks like.

The Edit tab lets you make changes to the Field Values of a Note.

Use the '+' (plus) sign on the Toolbar to clear the Field Values and prepare to Add a new Note.

Use the '-' (minus) sign on the Toolbar to delete the currently selected Note.

Click on the OK button on the Toolbar, or click on the Display tab, to complete and save your latest entries on the Edit tab.

<h2 id="view-your-notes-as-a-list">View Your Notes as a List</h2>


The first half of the Notenik display shows all the Notes in your current Collection as a simple list, sorted by Title.

You can navigate through this list using some of the buttons on the Toolbar:

<ul>
<li>&lt;&lt; Takes you to the first Note.</li>
<li>&gt;&gt; Takes you to the last note. </li>
<li>&lt;&nbsp; Takes you to the prior note.</li>
<li>&gt;&nbsp; Takes you to the next note. </li>
</ul>

Some of the same navigational commands are available under the Note menu, with handy keyboard shortcuts noted the right.

Feel free to use the Notenik General Preferences to change the Notenik display from a horizontal split to a vertical split, or vice-versa. Sometimes it's more useful to have the list of Notes on the left, while at other times it may be more useful to have it appear at the top of the window.

<h2 id="sync-your-notes">Sync Your Notes</h2>


Since a Collection of Notes lives on your local storage as a folder full of text files, it can be easily synced to the cloud and/or to other devices via a service such as [Dropbox](http://www.dropbox.com) or [iCloud](http://www.apple.com/icloud/icloud-drive/).

<h2 id="format-the-body-of-a-note-using-markdown">Format the Body of a Note using Markdown</h2>


[Markdown](http://daringfireball.net/projects/markdown/) is a simple syntax for formatting plain text files so that they can be easily read and written by humans, but also can easily be converted into HTML for use on the Web. If you'd like, you can use the Markdown syntax for formatting the body of each note. But it's not required.

When you view a Note on the Display tab, you will see the Body Field Value converted to HTML using a Markdown parser. If you haven't used any special Markdown formatting, then the text will simply appear as you entered it.

In fact, there is an option under the Note menu to generate HTML from the Body of a Note, and to either copy it to your system clipboard, in preparation for pasting the HTML somewhere else, or to store it in a file. You can even specify a preferred HTML folder for each of your Collections, in your Collection Preferences.

Notenik uses the [Flexmark](https://github.com/vsch/flexmark-java) parser to convert from Markdown to HTML, and includes the Typographic, Tables and Definitions extensions.

<h2 id="add-other-fields-to-your-notes">Add Other Fields to Your Notes</h2>


I've said that, at their most basic, each Note consists of a Title Field and a Body Field. But each Note can actually contain any number of fields.

Each Field in a Note consists of the Field's Label, followed by a colon and one or more spaces, followed by the Field's Value.

In other words, something like this:

	Title: This is a Sample Note

Field Labels must follow a few rules. A Field Label must always start at the beginning of a line. A Field Label may not consist of more than 48 characters, and may not contain a comma (',').

The Field Value may be specified on the same line, and/or on one or more following lines.

The Body Field, if present, will always be the last Field in a Note, since all following text will be assumed to be part of the Body (even if it contains strings of text that might otherwise appear to be additional Field Labels).

Each Field Label may be considered to have a *proper form* (including capitalization, spaces and punctuation), and a *common form* (the proper form without capitalization, whitespace or punctuation). The common form is considered to be the key identifier for the Field, so that any variations of the Label that include the same letters and digits in the same sequence will be considered equivalent.

The Notenik approach to identifying fields within a Note is very similar to the [Multimarkdown metadata syntax](http://fletcher.github.io/MultiMarkdown-4/metadata.html), and also similar to [YAML](https://en.wikipedia.org/wiki/YAML).

<h2 id="organize-your-notes-with-tags">Organize Your Notes with Tags</h2>


The Tags Field offers another way to organize the Notes within a Collection.

Tags may be used to group related notes into categories. One or more tags may be associated with each note, and each tag may contain one or more sub-tags. A period or a slash may be used to separate one level of a tag from the next level, with the period being preferred. A comma or a semi-colon may be used to separate one tag from another, with the comma being preferred.

Click on the Tags tab to see your Notes organized by tags, instead of appearing in a straight List. If a Note has multiple Tags assigned, then it will appear multiple times on the Tags tab, once for each Tag. By adding multiple levels to your Tags, you can effectively organize your Notes into an outline.

The "Favorites" tag may be used to identify favored notes within a collection, and these notes will appear on the Favorites Report.

The "Startup" tag may be used to identify notes whose links you wish to have opened by the application when it first starts, so long as you check this option on the Favorites tab of the Notenik preferences.

Note the the Collections Menu contains a few nice options for making mass changes to the Tags in a Collection.

* Replace one Tag with another.

* Flatten the levels of all your Tags, making each level a separate Tag.

* Change all your tags to lower case letters.

There's also an option under the File menu to Reload the current Collection without any untagged items. This doesn't permanently delete the items with blank tags -- it just temporarily hides them from view.

<h2 id="add-a-link-field-to-make-a-bookmark">Add a Link Field to Make a Bookmark</h2>


A Link Field within Notenik is intended to hold a URL: a hyperlink to a location on the Web (or to a local file, if you're prefer to use it that way).

You can easily Launch a link from within Notenik, either by using the dropdown to the left of the Link value, or by clicking on the Launch button in the Toolbar.

The Link Label dropdown also provides a couple of other options. Use the Tweak option to "tweak" your URL in some potentially useful ways; use the Disk File option to select a local file on your computer, to build a Link pointing to that file.

Adding a Link to a Note doesn't necessarily reduce the Note to a simple Bookmark, but this is certainly one way to use a Collection of Notes.

Look under the Collection menu for an option to Validate Links. Notenik will check all your Link Values to make sure they're accessible, and will add an 'Invalid URL' tag to any Notes that are unresponsive.

<h2 id="override-default-fields-with-a-template-file">Override Default Fields with a Template file</h2>


By default, Notenik shows only four Fields for a Note: Title, Link, Tags and Body. However, this default may be altered by placing a file named 'template.txt' or 'template.md' within a Collection's folder. Such a file should be in the normal Notenik format, although the Field Labels specified need not have any accompanying values. When such a template file is found, the field names found in this file will be used as the fields to be displayed and maintained for that Collection, overriding the default four.

You may use the Generate Template command on the File menu to create a template file. You can then use any text editor to modify the sample template file to reflect the fields you desire for that Collection.

The file extension used for the template file will be used as the file extension for Notes subsequently created within the Collection.

<h2 id="a-note-file-template">A Note File Template</h2>


Following is a sample Note file, showing all of the Fields that Notenik treats as special in some way. Feel free to copy and paste to create a template file, as described in the previous section.

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

<h2 id="sequence-your-notes">Sequence Your Notes</h2>


Add a Seq Field to a Collection (using a template file) in order to specify a sequence number, revision letter, version number or priority to be associated with each Note.

A Seq field may contain letters, digits and one or more periods (aka decimal points) or hyphens.

You may wish to assign a unique Seq value to each Note in a Collection, but Notenik does not require this (in other words, it does allow duplicate Seq values to be assigned to different Notes).

If you'd like to see your Notes listed in sequence by their assigned number, you can use the Sort Menu to change the sequence of the displayed list from Title to Seq + Title.

If you want to insert a new note with a Seq Value already assigned to another Note, then first select the other Note, then use the Increment Seq command on the Note menu to increment the Seq field of the existing note, as well as following notes that might otherwise cause duplicate Seq values.

To add a new Note with the next available Seq value, first select the last note in the list (the one with the highest seq field) and then add a New Note in the normal manner.

<h2 id="track-the-status-of-your-notes">Track the Status of Your Notes</h2>


Add an optional Status Field to your Collection, indicating each Note's degree of completion, and you have a basic To Do List.

Status values are usually selected from the following standard list. Note that each status may be represented by a single digit and/or an associated label. The digits serve to place the values into an approximate life cycle sequence.

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

The labels may be modified by placing a series of integer + label pairs in the Value area of the relevant template file, with separating punctuation. Such a template line might look something like this:

	Status: 1 - Idea; 4 - In Work; 9 - Published;

Look under the File menu for an option to Purge Notes that have been Canceled or Completed. You'll be given the option of discarding the purged Notes, or of copying them to another location.

<h2 id="date-your-notes">Date Your Notes</h2>


Add a Date field to a Collection in order to track the date each note was officially published, or a due date for each note. A date may be expressed in any of a number of common formats. It may also be a partial date, such as a year, or a year and a month. It may or may not contain a specific time of day.

Note that the Date field has several helpful editing tools. You can enter a free-form date yourself, or you can use a Calendar widget to pick dates from a Calendar. You can use the Today button to set the date to Today's date, and you can use the Recurs button to apply the Recurs rule, if one has been supplied.

<h2 id="specify-date-recurs-rules">Specify Date Recurs Rules</h2>


Add a Recurs field to a Collection to cause a Date for a Note to recur on a regular basis.

Specify a recurs rule using normal English, such as "Every 3 months," "Every Tuesday" or "Every Year."

Use the Recurs button on the Date editing row to apply the rule to the current Date associated with the selected Note.

<h2 id="track-your-tasks">Track Your Tasks</h2>


Add a Status field, a Seq field, a Date field and a Recurs field to a Collection, and you have all the elements of a personal task management system.

A Collection such as this can use the Date field to track due dates, and/or the Seq field to track priorities. If desired, use the Tags field to group tasks by context and/or by project.

If you use the Close Note option under the Note menu, then you can cause the Due Date to recur (if a Recurs field is available), or the Status field to show the task as Completed, if it is not eligible to recur.

The Sort menu contains two options specific to task tracking. The first option sorts all the Notes in a list by Date and then Seq, while the second option sorts all tasks by Seq and then Date. In both cases, completed tasks sort to the bottom of the list.

And don't forget the Purge Option under the File menu, which will allow you to purge Cancelled and Completed Notes from a Collection.

<h2 id="index-your-notes">Index Your Notes</h2>


An Index Field can contain one or more terms under which this note should appear in an index of the collection. Multiple terms can be placed on separate Index lines within the note, or can be entered as one field by using a semi-colon (';') to end each term entry. A URL to be associated with the term can be placed in parentheses following the term. If the term should reference a specific anchor within the note, then the anchor can be specified by preceding it with the usual pound sign ('#').

See the [Alphabetical Index](http://www.softdevbigideas.com/alphabetical-index.html) to Software Development Big Ideas for an example of an Index that can be generated in this manner.

[PSTextMerge](http://www.powersurgepub.com/products/pstextmerge/index.html) has a special Type of Data Source that will read a Notenik Index, and can then be used to generate an index page such as the example just cited.

<h2 id="find-one-or-more-notes-containing-specified-text">Find One or More Notes Containing Specified Text</h2>


The Find button on the Toolbar will search for the text specified in the blank space just to its left, and display the first Note containing this text in any field. After finding the first occurrence, this button's text changes to 'Again', to allow you to search again for the next Note containing the specified text.

The Find function is also available on the Collection Menu.

There you'll also find a Replace function that will let you Search for one particular string of text, and then systematically replace it with another.

<h2 id="use-other-special-fields">Use Other Special Fields</h2>


You can use whatever Field Labels you want within a Collection, but there are a few other Field Labels that have some special Notenik logic associated with them.

* Author -- The author(s) of the Note.

* Rating -- Your rating of the note, on a scale of one to five.

* Type -- The type of note. Any values may be used to distinguish between different types of notes within a collection.

* Teaser -- An excerpt from the note used as a teaser in a list of notes. The teaser may be formatted using Markdown.

<h2 id="adjust-notenik-to-suit-your-preferences">Adjust Notenik to Suit Your Preferences</h2>


Notenik's application Preferences allow you to adjust its appearance and functionality in a number of ways.

The Display tab in the Notenik preferences allows you to change the font, font size, and foreground and background colors used for the Display of each Note.

The Notenik General preferences contain a number of options for modifying the program's look and feel. Feel free to experiment with these to find your favorite configuration. Some options may require you to quit and re-launch Notenik before the changes will take effect.

**SplitPane: Horizontal Split?**: Check the box to have the **List** and **Tags** appear on the left of the main screen, rather than the top.

**Deletion: Confirm Deletes?**: Check the box to have a confirmation dialog shown whenever you attempt to delete the selected Note.

**Software Updates: Check Automatically?**: Check the box to have Notenik check for newer versions whenever it launches.

**Check Now**: Click this button to check for a new version immediately.

**File Chooser**: If running on a Mac, you may wish to select AWT rather than Swing, to make your Open and Save dialogs appear more Mac-like. However, Swing dialogs may still appear to handle options that can't be handled by the native AWT chooser.

**Look and Feel**: Select from one of the available options to change the overall look and feel of the application.

**Menu Location**: If running on a Mac, you may wish to have the menus appear at the top of the screen, rather than at the top of the window.

<h2 id="tables-rows-and-columns">Tables, Rows and Columns</h2>


Each Collection may be thought of as a Table, with each Note in the Collection treated as a Row, and each Field in the collection represented as a Column, with the Field Labels used as Column Headings.

When considered in this way, each Collection may be Viewed in a grid, Sorted on one or more columns, Filtered using one or more criteria, Output in one or more formats, and used to generate things like web pages through the use of Templates.

In fact, all of this and more can be accomplished through the [PSTextMerge](http://www.powersurgepub.com/products/pstextmerge/index.html) application.

You can read and process any Notes Collection by downloading and executing PSTextMerge, but much of the power of this application is also built right in to Notenik, as we'll see in the next couple of sections.

<h2 id="run-reports">Run Reports</h2>


The Reports menu allows you to select from a list of reports, and then generate the report of your choice. Reports are generally HTML web pages generated from [PSTextMerge](http://www.powersurgepub.com/products/pstextmerge/user-guide.html) scripts.

Any number of report scripts and templates may be placed within a folder named 'reports' that appears inside each folder of notes.

If a 'reports' folder has not yet been created and populated for a particular Collection, then a set of standard reports will be loaded from the application's resources folder.

Reports are generally a simpler and more straightforward alternative to the Publish function described below.

<h2 id="publish-your-notes">Publish Your Notes</h2>


The Publish option allows you to easily publish your Notes in a variety of useful formats, using the power of [PSTextMerge](http://www.powersurgepub.com/products/pstextmerge/user-guide.html). For example, you can easily publish your notes as a series of web pages.

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

**Publish Script**: Specify the location of the script to be used. The [PSTextMerge](http://www.powersurgepub.com/products/pstextmerge/user-guide.html) templating system is the primary scripting language used for publishing. A PSTextMerge script will usually end with a '.tcz' file extension.

**Publish when**: You may specify publication 'On Close' (whenever you Quit the application or close a Collection), or 'On Demand'.

**Publish Now**: Press this button to publish to the currently displayed location. Note that, if you've specified 'On Demand', then this is the only time that publication will occur.

**View**: Select the local file location or the equivalent URL location.

**View Now**: Press this button to view the resulting Web site in your Web browser.

<h2 id="publish-a-favorites-page">Publish a Favorites Page</h2>


As mentioned above, you may wish to use Notenik to keep track of your Web Bookarks. If so, you can Tag your favorite bookmarks with the tag 'Favorite', then publish all of your Favorite Bookmarks as a single Web page that you can then set as your home page within your Web browser.

The Notenik Favorites page is designed to display your most important bookmarks on a single page, in a series of rows and columns. These links are grouped by the 'Favorites' sub-categories specified.

Use the Favorites tab on the Notenik preferences to specify the number of columns to appear, and the maximum number of rows to include in a single column.

See this [Favorites Sampler](http://www.notenik.com/favorites/favorites.html) for an example of what a Favorites page might look like.

<h2 id="create-backups">Create Backups</h2>


Since your Notenik Collections are just folders full of text files, and they stay wherever you put them, whatever backup scheme(s) you may already be using for the rest of your data will likely also be backing up your Notes.

However Notenik also has a special backup mechanism you may choose to use.

Use the Backup command on the File menu to create a backup of your current Collection.

When you create a backup of a Collection, Notenik creates an exact copy of the current folder of Notes, and gives the new folder a name starting with the name of your source folder, and ending with the current date and time.

Additionally, on the Files tab of the Notenik preferences, you can specify whether you want Notenik to backup automatically, or to occasionally (on a weekly basis) prompt you to create a backup. And if you would like Notenik to automatically delete older backups, then you can specify the maximum number of backups to keep for any Collection.

<h2 id="import-and-export-your-notes">Import and Export Your Notes</h2>


You can import and export a Collection in a variety of formats, using commands found under the File menu.

You may filter the notes to be exported, for any of these formats, by adjusting the entries in the Tags Export preferences. You may specify one or more Tags to be selected, so that only notes containing those Tags will be exported. You may also suppress one or more Tags, meaning that exported notes will have those Tags removed from the resulting output.

For example, if you have a collection of blog entries stored as a Collection of Notes, and you have multiple blogs to which they are published, you can specify Tags for the relevant blogs for each note, and then select only those Notes when publishing a particular blog (and suppress the Tags for the other blogs).

If you leave the Tags to Select field blank, then all Notes will be exported.

Following are the supported formats.

* Import/Export in Notenik Format

	You can import and export your Notes in the current collection from/to a folder in the same Notenik format.

* Export to [OPML](http://en.wikipedia.org/wiki/OPML)

	The Notes in the current collection will be exported to an XML-based Outline format that can be opened by an app such as OmniOutliner. The Tags in the collection will be used to create the outline.

	Note that, for XML formats, the resulting file may contain invalid characters if those are present in your Notes.

* Import/Export in Tab-Delimited Format

	Each Note will be represented as one row/line, and each field will be represented in a separate column. This format is suitable for import into MS Excel, for example.

* Export to Tab-Delimited for MS Links

	Similar to tab-delimited, but the Title and link are concatenated into a single field, with a '#' character separating the title from the link. Some Microsoft apps can use this format to import each link into a special field that combines the title and the hyperlink into a single field.

* Import/Export in XML Format

	Your Notes will be represented in an XML format with each field represented as a separate SML tag.

	Note that, for XML formats, an exported file may contain invalid characters if those are present in your Notes.

* Import Mac App Info

	When running on a Mac, if you choose this option, and then select your Applications folder, Notenik will import one Note for each application found in the folder. If duplicates are found then the existing notes will be updated rather than adding new notes. You will get the best results by first specifying a 'template.txt' file for the Collection in the following format.

		Title:  The unique title for this note

		Tags:   One or more tags, separated by commas

		Link:   http://anyurl.com

		Date:   2017-01-22

		Seq:    Rev Letter or Version Number

		Minimum System Version:

		Body:
 
		The body of the note

	After the import, you can use the Tags field to organize the applications in any way you like, and clicking on the Link field will launch the selected application.

<h2 id="sync-multiple-collections-to-a-common-folder">Sync Multiple Collections to a Common Folder</h2>


The Folder Sync tab in the Collection preferences, on the Collection menu, allows the user to identify a common folder to which several different Notenik collections can be synced.

Such a common folder could then be conveniently accessed using [nvAlt](http://brettterpstra.com/projects/nvalt/), for example.

Each collection can have a different prefix assigned, and that prefix will then be used to keep the notes from the different collections separately identified within the common folder. The prefix will default to the folder name for the collection, with a trailing 's' removed if one is found, and with a dash added as a separator. A folder name of 'Bookmarks', for example, would result in a prefix of 'Bookmark - ' being appended to the front of each note as it is stored in the common folder.

The logic for the syncing works as follows.

A sweep of the entire common folder will be performed whenever syncing is first turned on for a collection, and henceforth whenever a collection with syncing already on is opened.

The sweep sync includes the following logic.

* For any common folder notes with a matching prefix, where the corresponding note does not already exist within the Notenik collection, the note will be added to the Notenik collection.

* For any Notenik notes where a matching nvAlt note is not found, the note will be added to the nvAlt folder.

* For any Notenik notes where a matching nvAlt note has been updated more recently than the matching Notenik note, the Notenik note will be updated to match the nvAlt note.

Once folder sync has been turned on for a collection, then every time that Notenik makes an update to any note within that collection, a parallel update will be made to the corresponding note within the common folder.

<h2 id="view-the-log">View the Log</h2>


On the Window Menu, you'll find an entry for the Log window.

You'll also briefly see the Log window when you first launch Notenik.

You can usually ignore the Log, but if Notenik is not behaving as you think it should, a look at the Log window can sometimes help determine what is going on.

<h2 id="rights">Rights</h2>


Notenik is free and [open source](http://opensource.org/osd) software.

Notenik Software, Specifications and Documentation are all Copyright 2009 - 2017 by [Herb Bowie](http://www.herbbowie.com/).

Licensed under the Apache License, Version 2.0 (the "License"); you may not use Notenik except in compliance with the License. You may obtain a copy of the License at [www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0). Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

Notenik also incorporates the following open-source libraries.

* JExcelAPI Copyright 2002 Andrew Khan, used under the terms of the GNU Lesser General Public License
 
* Flexmark Copyright 2016 by Vladimir Schneider, used under the terms of the BSD 2-clause license.
 
* Xerces Copyright 1999-2012 The Apache Software Foundation, used under the terms of the Apache License, Version 2.0
 
 
* Saxon Copyright Michael H. Kay, used under the terms of the Mozilla Public License, Version 1.0

<h2 id="support">Support</h2>


If you have questions about Notenik, bug reports, or requests for enhancements, please shoot me a note at [support@powersurgepub.com](mailto:support@powersurgepub.com) and I'll try to get back to you as quickly as I can.


