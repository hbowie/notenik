/*
 * Copyright 2003 - 2016 Herb Bowie
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

  import com.powersurgepub.psdatalib.psdata.values.*;
  import com.powersurgepub.psdatalib.markup.*;
  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psdatalib.pstags.*;
	import com.powersurgepub.psutils.*;
  import java.util.*;
  import javax.swing.event.*;
  import javax.swing.text.html.*;
  import java.text.*;

/**
   A panel to display a single note. <p>
 */
public class DisplayTab 
    extends javax.swing.JPanel 
      implements HyperlinkListener {
  
  private String startCategoryParagraph 
      = "<p><font size=3 face=\"Lucida Grande, Verdana, Arial, sans-serif\"><em>";
  private String horizontalRule = "<hr>";
  private String startTitleParagraph 
      = "<p><font size=4 face=\"Lucida Grande, Verdana, Arial, sans-serif\"><b>";
  private String startNormalParagraph 
      = "<p><font size=3 face=\"Lucida Grande, Verdana, Arial, sans-serif\">";
  private String endCategoryParagraph
      = "</em></font></p>";
  private String endTitleParagraph 
      = "</b></font></p>";
  private String endNormalParagraph
      = "</font></p>";
  
  private Trouble trouble = Trouble.getShared();
  
  private DisplayPrefs displayPrefs;
  
  String endSpecialTag = "";
  
  private StringBuffer text;
  
  private MdToHTML mdToHTML;
  
  private DateFormat dateFormat = new SimpleDateFormat ("EEEE MMMM d, yyyy");
  
  /** Creates new form DisplayTab */
  public DisplayTab(DisplayPrefs displayPrefs) {
    this.displayPrefs = displayPrefs;
    initComponents();
    displayEditorPane.addHyperlinkListener (this);
    HTMLEditorKit kit = (HTMLEditorKit) displayEditorPane.getEditorKit();
    StyleSheet styles = kit.getStyleSheet();
    Enumeration rules = styles.getStyleNames();
    mdToHTML = MdToHTML.getShared();
   	/* while (rules.hasMoreElements()) {
   	    String name = (String) rules.nextElement();
   	    Style rule = styles.getStyle(name);
   	    Logger.getShared().recordEvent (LogEvent.NORMAL, 
          "Display Stylesheet rule " + rule.toString(),
          false);
   	} */
    // kit.setStyleSheet(styles);
  }
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param store Disk Store object for the file.
   */
  public void filePrep () {
    // No file information used on the About Tab
  }
  
  public void initItems() {

  }
  
  public void startDisplay() {
    text = new StringBuffer();
    text.append ("<html>");
    text.append ("<body bgcolor=\"#" 
        + StringUtils.colorToHexString(displayPrefs.getDisplayBackgroundColor())
        + "\" text=\"#"
        + StringUtils.colorToHexString(displayPrefs.getDisplayTextColor())
        + "\" link=\"#"
        + StringUtils.colorToHexString(displayPrefs.getDisplayTextColor())
        + "\" alink=\"#"
        + StringUtils.colorToHexString(displayPrefs.getDisplayTextColor())
        + "\" vlink=\"#"
        + StringUtils.colorToHexString(displayPrefs.getDisplayTextColor())
        + "\">");
  }
  
  public void displayTags(Tags tags) {
    String tagsString = "";
    if (tags.hasData()) {
      tagsString = tags.toString();
    }
    appendParagraph ("em", 0, "", "", tagsString);
  }
  
  public void displayTitle(String title) {
    // Display Title in bold and increased size
    if (displayPrefs.getDisplayTitle()) {
      appendParagraph ("b", 1, "", "", title);
    }
  }
  
  public void displayBody(String body) {
    // Display Body, if there is any
    String bodyHTML = mdToHTML.markdownToHtml(body);
    if (body.length() > 0) {
      startFont(0);
      int i = 0;
      while ( i < bodyHTML.length()) {
        int j = bodyHTML.indexOf("<h", i);
        if (j < 0) { j = bodyHTML.length(); }
        if (j > i) {
          text.append(bodyHTML.substring(i, j));
          i = j+1;
        }
        endFont();
        if (j < bodyHTML.length()) {
          int k = bodyHTML.indexOf(">", j+2);
          if (k > 0) {
            int l = bodyHTML.indexOf("</h", k+1);
            if (l > 0) {
              int m = bodyHTML.indexOf(">", l+3);
              if (m > 0) {
                text.append(bodyHTML.substring(j, k+1));
                startFontFace();
                text.append(bodyHTML.substring(k+1, l));
                endFont();
                text.append(bodyHTML.substring(l, m+1));
                startFont(0);
                i = m + 1;
              } // end if we found another complete heading
            } // end if we found start of heading close
          } // end if we found close for heading tag
        } // end if we found the beginning of another heading tag
      } // end while scanning for headings
      // text.append(bodyHTML);
      // appendParagraph ("", 0, "", "", description);
      endFont();
    }
  }
  
  /**
   Display a horizontal rule to serve as a divider
  */
  public void displayDivider() {
    
    text.append ("<br><hr>");
  }
  
  public void displayAuthor(Author author) {
    // Display Author, if any
    String authorCompleteName = author.getCompleteName();
    if (authorCompleteName.length() > 0) {
      int numberOfAuthors = author.getNumberOfAuthors();
      if (author.isCompound()) {
        startParagraph ("", 0, "Authors");
        for (int i = 0; i < numberOfAuthors; i++) {
          Author nextAuthor = author.getAuthor (i);
          appendItem (
              nextAuthor.getALink(), 
              nextAuthor.getCompleteName(),  
              nextAuthor.getWikiquoteLink(),
              "Wikiquote",
              i, 
              numberOfAuthors);
        }
      } else {
        startParagraph ("", 0, "Author");
        appendItem (
            author.getALink(), 
            authorCompleteName,  
            author.getWikiquoteLink(),
            "Wikiquote",
            0, 1);
      }
      String authorInfo = author.getAuthorInfo();
      if (authorInfo.length() > 0) {
        appendItem ("", ", " + authorInfo, 0, 1);
      }
      endParagraph();
    }
  }
  
  /**
   Display source of text, if any. 
  
   @param source The source of the text. 
  */
  public void displaySource(WisdomSource sourceObj, String pages) {
    // Display Source, if any
    if (displayPrefs.getDisplaySource()) {
      String sourceType = sourceObj.getTypeLabel();
      String source = sourceObj.toString();
      String url = sourceObj.getALink();
      String minorTitle = sourceObj.getMinorTitle();
      if ((source.length() > 0 
            && (! source.equalsIgnoreCase (WisdomSource.UNKNOWN)))
          || (minorTitle.length() > 0)) {

        startParagraph ("", 0, "Source");

        if (displayPrefs.getDisplayType()
            && sourceType.length() > 0
            && (! sourceType.equalsIgnoreCase(WisdomSource.UNKNOWN))) {
          text.append ("the " + sourceObj.getTypeLabel().toLowerCase() + ", ");
        }

        if (source.length() > 0 
            && (! source.equalsIgnoreCase (WisdomSource.UNKNOWN))) {
          text.append("<cite>");
          appendItem (url, source, 0, 1);
          text.append("</cite>");
          if (minorTitle.length() > 0) {
            text.append(", ");
          }
        }
        if (minorTitle.length() > 0) {
          text.append("\"" + minorTitle + "\"");
        }
        if (pages.length() > 0) {
          StringBuffer pagesLabel = new StringBuffer("page");
          if (pages.indexOf("-") > 0) {
            pagesLabel.append("s");
          }
          text.append(", " + pagesLabel + " " + pages);
        }

        endParagraph();

        /*
        if (url.length() > 0) {
          appendParagraph ("", 0, url, "Source", source);
        } else {
          appendParagraph ("", 0, "", "Source", source);
        }
        if (td.displayType) {
          appendParagraph ("", 0, "",  "Type", item.getSourceTypeLabel());
        }
        if (item.getPages().length() > 0) {
          appendParagraph ("", 0, "", "Page(s)", item.getPages());
        }
         */
        StringBuffer publisher = new StringBuffer(sourceObj.getCity());
        if (publisher.length() > 0) {
          publisher.append (": ");
        }
        publisher.append (sourceObj.getPublisher());
        if (publisher.length() > 0) {
          appendParagraph ("", 0, "", "Publisher", publisher.toString());
        }
        
      }

      /*
      if (minorTitle.length() > 0) {
        appendParagraph ("", 0, "", "Minor Title", minorTitle);
      }
       */
    }
  }
  
  /**
   Display copyright, or other rights, and related info. 
  
   @param rights The rights owned, such as Copyright. 
   @param year   The year (at a minimum) of first publication of the work.
   @param rightsOwner The name of the person or company owning the rights. 
  */
  public void displayRights(String rights, String year, String rightsOwner) {
    // Display Rights / Publication Year
    String yearRightsLabel = "First Published";
    StringBuffer yearRights = new StringBuffer();

    if (rights.length() > 0
        && rights.startsWith ("Copyright")) {
      yearRights.append ("Copyright &copy;");
    } else {
      yearRights.append (rights);
    }
    
    if (yearRights.length() > 0) {
      yearRightsLabel = "Rights";
    }
    
    if (! year.equals("")) {
      if (yearRights.length() > 0) {
        yearRights.append (" ");
      }
      yearRights.append (year);
    }
    
    if (rightsOwner.length() > 0) {
      if (yearRights.length() > 0) {
        yearRights.append (" by ");
      }
      yearRights.append (rightsOwner);
      yearRightsLabel = "Rights";
    }
    if (yearRights.length() > 0) {
      appendParagraph ("", 0, "", yearRightsLabel, yearRights.toString());
    }
  }
  
  /**
   Display the priority / rating if anything unusual. 
  
   @param priority An intereger / priority in the range 1 - 5. 
   @param label    The display value. 
  */
  public void displayRating(int priority, String label) {

    if (priority != 3) {
      appendParagraph ("", 0, "", "Rating", 
          String.valueOf (priority) + " " + label);
    }
  }
  
  public void finishDisplay() {
    text.append ("</body>");
    text.append ("</html>");
    // Logger.getShared().recordEvent(LogEvent.NORMAL, text.toString(), false);
    displayEditorPane.setText (text.toString());
    displayEditorPane.setCaretPosition(0);
  }
  
  public void displayDateAdded(String dateAdded) {
    // Display Date Added
    if (displayPrefs.getDisplayAdded()) {
      appendParagraph ("", 0, "", "Added", 
          dateAdded);
    }
  }
  
  /**
   Display Item ID.
  
   @param id A number uniquely identifying the item being displayed. 
  */
  public void displayItemID (int id) {
        // Display Item ID
    if (displayPrefs.getDisplayID()) {
      appendParagraph ("", 0,
          // item.getItemIDLink(td.collectionWindow),
          "", "ID",
          String.valueOf (id));
    }
  }
  
  public void displayField(String label, String value) {
    appendParagraph("", 0, "", label, value);
  }
  
  public void displayLink(String label, String value, String link) {
    String val = value;
    if (val.length() == 0) {
      if (link.startsWith("http://")) {
        val = link.substring(7);
      }
      else
      if (link.startsWith("https://")) {
        val = link.substring(8);
      }
      else
      if (link.startsWith("mailto:")) {
        val = link.substring(7);
      } else {
        val = link;
      }
    }
    appendParagraph ("", 0, link, label, val);
  }
  
  public void displayLabelOnly(
      String label) {
    
    startParagraph("", 0, label);
    endParagraph();
  }
  
  public void appendParagraph (
      String specialTag, 
      int fontVariance, 
      String href,
      String label, 
      String body) {
    
    startParagraph (specialTag, fontVariance, label);

    appendItem (href, body, 0, 1);
    
    endParagraph();
  }
  
  public void startParagraph (
      String specialTag, 
      int fontVariance, 
      String label) {

    String startSpecialTag = "";
    endSpecialTag = "";
    if (specialTag.length() > 0) {
      startSpecialTag = "<" + specialTag + ">";
      endSpecialTag = "</" + specialTag + ">";
    }
    String intro = "";
    if (label.length() > 0) {
      intro = label + ": ";
    }
    text.append ("<p>");
    startFont(fontVariance);
    text.append (
        startSpecialTag +
        intro);
  }

  public void startFont (int fontVariance) {
    int fontSize = displayPrefs.getDisplayNormalFontSize() + fontVariance;
    if (fontSize < 1) {
      fontSize = 1;
    }
    if (fontSize > 7) {
      fontSize = 7;
    }
    text.append (
        "<font size=" +
        String.valueOf (fontSize) +
        " face=\"" +
        displayPrefs.getDisplayFont() +
        ", Verdana, Arial, sans-serif\">");
  }
  
  public void startFontFace() {
    text.append (
        "<font face=\"" +
        displayPrefs.getDisplayFont() +
        ", Verdana, Arial, sans-serif\">");
  }

  public void appendItem (
      String href, 
      String body,
      int listPosition,
      int listLength) {
    
    appendItem (href, body, "", "", listPosition, listLength);
  }
  
  public void appendItem (
      String href, 
      String body,
      String parenHref,
      String parenthetical,
      int listPosition,
      int listLength) {
    
    String startLink = "";
    String endLink = ""; 
    String listSuffix = "";
    if (href != null && href.length() > 0) {
      startLink = "<a href=\"" + href + "\">";
      endLink = "</a>";
    }
    if (listLength > 1) {
      int listEndProximity = listLength - listPosition - 1;
      if (listEndProximity == 1) {
        listSuffix = " and ";
      }
      else
      if (listEndProximity > 1) {
        listSuffix = ", ";
      }
    }
    String parenPrefix = "";
    String parenSuffix = "";
    String parenStartLink = "";
    String parenEndLink = "";
    if (parenthetical.length() > 0) {
      parenPrefix = " (";
      parenSuffix = ")";
      if (parenHref.length() > 0) {
        parenStartLink = "<a href=\"" + parenHref + "\">";
        parenEndLink = "</a>";
      }
    }
    text.append (
        startLink +
        body +
        endLink +
        parenPrefix +
        parenStartLink +
        parenthetical +
        parenEndLink +
        parenSuffix +
        listSuffix);
  }
  
  public void endParagraph () {
    text.append (endSpecialTag);
    endFont();
    text.append ("</p>");
  }

  public void endFont () {
    text.append ("</font>");
  }
  
  public void hyperlinkUpdate (HyperlinkEvent e) {
    HyperlinkEvent.EventType type = e.getEventType();
    if (type == HyperlinkEvent.EventType.ACTIVATED) {
      Home.getShared().openURL(e.getURL());
    }
  }
  
  /**
   Modifies the td.item if anything on the screen changed. 
   
   @return True if any of the data changed on this tab. 
   */
  public boolean modIfChanged () {
    return false;
  } // end method
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    displayScrollPane = new javax.swing.JScrollPane();
    displayEditorPane = new javax.swing.JEditorPane();

    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent evt) {
        formComponentShown(evt);
      }
    });
    addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        formFocusLost(evt);
      }
    });
    setLayout(new java.awt.GridBagLayout());

    displayScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    displayScrollPane.setViewportBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

    displayEditorPane.setEditable(false);
    displayEditorPane.setContentType("text/html"); // NOI18N
    displayEditorPane.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        displayEditorPaneMouseClicked(evt);
      }
    });
    displayScrollPane.setViewportView(displayEditorPane);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(displayScrollPane, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost
    // Does nothing useful
  }//GEN-LAST:event_formFocusLost

  private void displayEditorPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_displayEditorPaneMouseClicked
    // td.activateItemTab();
  }//GEN-LAST:event_displayEditorPaneMouseClicked

  private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
    // showThisTab();
    // td.endRotation();
    // aboutJavaText.requestFocus();
  }//GEN-LAST:event_formComponentShown
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JEditorPane displayEditorPane;
  private javax.swing.JScrollPane displayScrollPane;
  // End of variables declaration//GEN-END:variables
  
}
