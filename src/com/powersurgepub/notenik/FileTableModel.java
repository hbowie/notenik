/*
 * Copyright 2015 - 2015 Herb Bowie
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

  import java.io.*;
  import java.text.*;
  import java.util.*;
  import javax.swing.table.*;

/**
 A Table Model containing a list of files. 

 @author Herb Bowie
 */
public class FileTableModel 
    extends AbstractTableModel{
  
  public static final int NUMBER_OF_COLUMNS = 3;
  
  private SimpleDateFormat format 
      = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
  
  private ArrayList<File> files;
  
  public FileTableModel() {
    files = new ArrayList<File>();
  }
  
  public void add(File anotherFile) {
    files.add(anotherFile);
  }
  
  public File get(int rowIndex) {
    if (rowIndex >= 0 && rowIndex < files.size()) {
      return files.get(rowIndex);
    } else {
      return null;
    }
  }
  
  /**
   How many rows are in the table?
  
   @return Number of rows. 
  */
  public int getRowCount() {
    return files.size();
  }
  
  /**
   How many columns are in the table?
  
   @return Number of columns. 
  */
  public int getColumnCount() {
    return NUMBER_OF_COLUMNS;
  }
  
  public String getColumnName(int columnIndex) {
    if (columnIndex >= 0 && columnIndex < NUMBER_OF_COLUMNS) {
      switch (columnIndex) {
        case 0:
          return "File Name";
        case 1:
          return "Last Mod Date";
        case 2:
          return "Exists?";
        default:
          return null;
      } // end switch
    } else {
      return "";
    }
  }
  
  /** 
   Return a value from the table. 
  
   @param rowIndex    Desired row index. 
   @param columnIndex Desired column index.
  
   @return Desired value, or null if either index is out of bounds. 
  */
  public String getValueAt (int rowIndex, int columnIndex) {
    if (rowIndex >= 0 && rowIndex < files.size()
        && columnIndex >= 0 && columnIndex < NUMBER_OF_COLUMNS) {
      File file = files.get(rowIndex);
      switch (columnIndex) {
        case 0:
          return file.getName();
        case 1:
          if (file != null && file.exists()) {
            Date lastMod = new Date(file.lastModified());
            return format.format(lastMod); 
          } else {
            return " ";
          }
        case 2:
          return String.valueOf(file.exists());
        default:
          return null;
      } // end switch
    } else {
      return null;
    }
  } // end method getValueAt

}
