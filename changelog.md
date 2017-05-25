
## Version 2.90 (25 May 2017)

1. **Allow User to Quickly Open one Essential Collection**

	You can specify this Essential Collection in the File Preferences, and then quickly open it from the File menu, or via a handy keyboard shortcut. 


2. **Added a README file**

	Notenik will now generate a README file in each Collection to make sure that someone viewing the folder outside of Notenik will realize that the folder contains notes used by Notenik. 


3. **Added a Way to Specify a Field Type**

	Within a template file, within the data portion of a line, the user may now enclose a special value inside of a pair of less-than greater-than signs to indicate the field type desired, if this is not already discernible from the field name. 


4. **Added an Option for a Master Collection**

	Users can now create a Master Collection, to keep track of all of their other collections. 


5. **Fixed Field Display Bug**

	Fixed a bug that would cause field values to show up next to the wrong labels, both on the Display tab and on the Edit tab. 



## Version 2.80 (2017-05-06)

1. **Increased Maximum Number of Recent Files to 50**

	

2. **Switched Markdown Conversion from Pegdown to Flexmark**

	Switched Markdown processing from Pegdown to Flexmark. 


3. **Added Options to Sort Tasks by Date or by Seq**

	Added two new sort options, both intended for use with task (aka to do) lists. THe first option is to sort by Date. The second new option is to sort by the Seq field, assuming that this is being used to track a task priority. For both of these sorts, completed/canceled tasks will sort to the bottom of the list. 


4. **Added Table Formatting to Markdown Processing**

	Table formatting can now be used in fields that support Markdown formatting. 


5. **Added a Recurs Field**

	Added a new Recurs field that can be used to increment an accompanying Date field by the specified interval. 


6. **Greatly Improved Date Editing**

	The Date field can still be edited as a free-form text field, but can now also be edited using a Calendar editor, to make it easier to pick dates, and to increment dates. 


7. **Hyphens now recognized in the Seq Field**

	Hyphens can now be used as punctuation in the Seq field, in addition to periods. 



## Version 2.70 (2017-04-06)

1. **Added ability to sort by the Seq field**

	Added a Sort menu that allows the user to sort the list of notes by Seq first, and then by Title.



## Version 2.60 (02/13/2017)

1. **Added File Menu Command to Reload Without Untagged Notes**

	This new File Menu item allows the user to reload the current collection, but suppressing any notes that are completely without tags.



## Version 2.50 (02/04/2017)

1. **Added the ability to Import information about Mac Applications**

	This new import function can be used to create a catalog of your applications. Use the Tags to categorize them any way you like. You can also launch each app directly from its Note in Notenik.



## Version 2.40 (12/20/2016)

1. **Refinements to the Reports Feature**

	Several refinements to the Reports function were implemented.



## Version 2.31 (12/16/2016)

1. **Logging Change**

	Created and displayed logging screen at beginning of startup, to help with debugging when main screen never loads. Also removed debugging display of display tab HTML code.



## Version 2.30 (12/11/2016)

1. **Added Display Tab and Display Preferences**

	Added a Display tab to allow the note to be viewed without editing, and a Display Preferences tab to allow the look and feel of the Display tab to be tweaked.



## Version 2.20 (08/15/2016)

1. **Added Index Field**

	Added an Index field that can be used to identify terms under which a note should be indexed.



## Version 2.10 (06/17/2016)

1. **Allow file extension to be specified (md vs txt)**

	Make sure app respects original file extension

The template file can now have any file extension, and that will become the default for the collection. Additionally, when a particular note is read from disk, its current file extension will be honored in following save operations.


2. **Added Reports Menu**

	Added a Reports Menu, which will be automatically populated based on the presence of PSTextMerge script files, sharing an extension of '.tcz', found within a 'reports' folder.



## Version 2.00 (03/16/2016)

1. **Improved Documentation and Bug Fixes**

	Improved the documentation and made sure everything works per the user guide.



## Version 1.80 (11/16/2015)

1. **Added Note Copy/Paste Functionality**

	Added Copy and Paste functions to the Note menu, allowing a note to be copied from one collection to another, by way of the System Clipboard.



## Version 1.70 (08/31/2015)

1. **Added Status Field with Purge Capability**

	A Status field may now be used, by specifying the field contents for a collection through the use of a 'template.txt' file. When a Status field is present, the File menu will include a command to Purge closed notes, optionally copying them to an archive folder before deleting them from the current collection.



## Version 1.60 (03/18/2015)

1. **Added Export for MS Links**

	Added a new export format that combines the Title and the Link into a single field, separated by '#'.



## Version 1.51 (02/24/2015)

1. **Now Remembers Last Note Displayed**

	Instead of always positioning on the first note on the list, Notenik now attempts to re-position itself on the last item displayed when a collection is opened.



## Version 1.50 (02/06/2015)

1. **Added XML Export and Import**

	The File Menu now contains options for exporting to XML, and for importing from the same.



## Version 1.40 (01/29/2015)

1. **Added Get File Info Item under Note Menu**

	Get File Info command pops up a small window with info about a link to a local file or file share, including a list of files with similar names.



## Version 1.31 (01/22/2015)

1. **Added Escape Edit Menu Item**

	Selecting the Escape Edit menu item, or hitting the Escape key, will undo any edits currently in progress against the current note.


2. **Corrected Path Name Separator**

	Corrected path name separator being used to form the path for backups.



## Version 1.30 (01/17/2015)

1. **Added Tags Export Prefs**

	Tags Export Prefs now allow the user to limit an export to certain tags, and/or to suppress certain tags from appearing in exported tags fields.



## Version 1.20 (01/05/2015)

1. **Added Note Menu Item to Text Edit Current Note**

	Added Note Menu Item to open the current note in the preferred local text editor.



## Version 1.10 (10/04/2014)

1. **Added Folder Sync Option**

	Added a new Foldery Sync option available through the application preferences, allowing multiple collections to be synced with a common folder that may be conveniently accessed using nvAlt.



## Version 1.00 (06 May 2017)

1. **Initial Release of Notenik**

	Initial release of Notenik.


