/*
 * Copyright 1999 - 2015 Herb Bowie
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
  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psutils.*;
  import java.io.*;
  import java.util.*;
  import org.xml.sax.*;
  import org.xml.sax.helpers.*;

/**
 Imports notes from an XML file. 

 @author Herb Bowie
 */
public class NoteImportXML
    extends DefaultHandler {
  
  private     File                importFile;
  private     NoteList            noteList;
  
  private     NotenikMainFrame    mainFrame;
  
  private     boolean             notenikStarted = false;
  private     boolean             noteStarted = false;
  
  private     Note                workNote;
  
  private     String              fieldName = "";

  private     XMLReader           parser;
  
  private     DataDictionary      dict;
  private     RecordDefinition    recDef;
  private     int                 elementLevel = -1;
  
  private     ArrayList<String>        fieldNames;
  private     ArrayList<StringBuilder> chars;
  
  private     boolean             ok = true;
  private     int                 imported = 0;
  
  /** Log used to record events. */
  private     Logger              log = Logger.getShared();
  
  /** Creates a new instance of XMLParser2 */
  public NoteImportXML(NotenikMainFrame mainFrame) {
    this.mainFrame = mainFrame;
  }
  
  /**
     Sets a logger to be used for logging operations.
    
     @param log Logger instance.
   */
  public void setLog (Logger log) {
    this.log = log;
  }
  
  public int parse (
      File importFile,
      NoteList noteList) {
    
    this.importFile = importFile;
    this.noteList = noteList;
    ok = true;
    imported = 0;
    fieldNames = new ArrayList<String>();
    chars = new ArrayList<StringBuilder>();

    try {
      parser = XMLReaderFactory.createXMLReader();
    } catch (SAXException e) {
      log.recordEvent (LogEvent.MINOR, 
          "Generic SAX Parser Not Found",
          false);
      try {
        parser = XMLReaderFactory.createXMLReader
            ("org.apache.xerces.parsers.SAXParser");
      } catch (SAXException eex) {
        log.recordEvent (LogEvent.MEDIUM, 
            "Xerces SAX Parser Not Found",
            false);
        ok = false;
      }
    }
    if (ok) {
      parser.setContentHandler (this);
      if (! this.importFile.exists()) {
        ok = false;
        log.recordEvent (LogEvent.MEDIUM, 
            "XML File " + importFile.toString() + " cannot be found",
            false);
      }
    }
    if (ok) {
      if (! this.importFile.canRead()) {
        ok = false;
        log.recordEvent (LogEvent.MEDIUM, 
            "XML File " + importFile.toString() + " cannot be read",
            false);       
      }
    }
    if (ok) {
      if (this.importFile.isDirectory()) {
        ok = false;
        log.recordEvent (LogEvent.MEDIUM, 
            "Directory found instead of XML file at " + importFile.toString(),
            false); 
      } // end if passed String identified a directory
      else 
      if (this.importFile.isFile()) {
        parseXMLFile (this.importFile);
      }
    } // end if everything still OK
    if (! ok) {
      imported = -1;
    }
    return imported;
  }  
  
  private void parseXMLFile (File xmlFile) {
    try {
      parser.parse (xmlFile.toURI().toString());
    } 
    catch (SAXException saxe) {
        log.recordEvent (LogEvent.MEDIUM, 
            "Encountered SAX error while reading XML file " + xmlFile.toString() 
            + saxe.toString(),
            false);   
        ok = false;
    } 
    catch (java.io.IOException ioe) {
        log.recordEvent (LogEvent.MEDIUM, 
            "Encountered I/O error while reading XML file " + xmlFile.toString() 
            + ioe.toString(),
            false);  
        ok = false;
    }
  }
  
  /**
   Process the start of a new XML element identified by the parser. 
  
   @param namespaceURI
   @param localName This is the name of the field. 
   @param qualifiedName
   @param attributes 
  */
  public void startElement (
      String namespaceURI,
      String localName,
      String qualifiedName,
      Attributes attributes) {
    // System.out.println ("startElement " + localName);
    StringBuilder str = new StringBuilder();
    fieldName = localName;
    if (localName.equalsIgnoreCase(NoteExport.NOTENIK)) {
      notenikStarted = true;
    }
    else
    if (notenikStarted 
        && localName.equalsIgnoreCase(NoteExport.NOTE)) {
      noteStarted = true;
      elementLevel = 0;
      workNote = new Note(noteList.getRecDef());
      storeField (elementLevel, fieldName, str);
    }
    else
    if (noteStarted) {
      elementLevel++;
      storeField (elementLevel, fieldName, str);
    }
  } // end method
  
  
  /**
   Collect any attributes attached to the start of the element. 
  
   @param attributes
   @param field 
  */
  private void harvestAttributes (Attributes attributes, DataField field) {
    for (int i = 0; i < attributes.getLength(); i++) {
      String name = attributes.getLocalName (i);
      DataFieldDefinition def = dict.getDef (name);
      if (def == null) {
        def = new DataFieldDefinition (name);
        dict.putDef (def);
      }
      // System.out.println ("  Attribute " + name + " = " + attributes.getValue (i));
      DataField attr = new DataField (def, attributes.getValue (i));
      field.addField (attr);
    }
  }
  
  /**
   Process a character string passed by the parser. 
  
   @param ch      An array of characters.
   @param start   The starting position within the array. 
   @param length  The number of characters to obtain. 
  */
  public void characters (char [] ch, int start, int length) {
    if (elementLevel >= 0 && elementLevel < chars.size()) {
      StringBuilder str = chars.get (elementLevel);
      str.append (ch, start, length);
    }
  }
  
  /**
   If it's ignorable, then let's ignore it. 
  
   @param ch
   @param start
   @param length 
  */
  public void ignorableWhitespace (char [] ch, int start, int length) {
    
  }
  
  public void endElement (
      String namespaceURI,
      String localName,
      String qualifiedName) {
    
    if (localName.equalsIgnoreCase(NoteExport.NOTENIK)) {
      notenikStarted = false;
      elementLevel = -1;
    }
    else
    if (notenikStarted 
        && localName.equalsIgnoreCase(NoteExport.NOTE)) {
      noteList.add(workNote);
      mainFrame.saveNote(workNote);
      noteStarted = false;
      elementLevel = 0;
    }
    else
    if (noteStarted) {
      String str = chars.get (elementLevel).toString().trim();
      workNote.setField(localName, str.toString());
      elementLevel--;
    }
  } // end method
  
  /**
   Store a field name and its associated characters content. 
  
   @param level The depth within the parsed XML document. 
   @param field The name of the field. 
   @param str   The associated characters content. 
  */
  private void storeField (int level, String field, StringBuilder str) {
    if (fieldNames.size() > level) {
      fieldNames.set (level, field);
      chars.set (level, str);
    } else {
      fieldNames.add (field);
      chars.add (level, str);
    }
  } // end method
  
}
