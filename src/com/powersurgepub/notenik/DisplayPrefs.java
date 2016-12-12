/*
 * Copyright 2003 - 2013 Herb Bowie
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

  import com.powersurgepub.psutils.*;
  import com.powersurgepub.xos2.*;
  import java.awt.*;
  import javax.swing.*;

/**
 * General user preferences. 
 */
public class DisplayPrefs extends javax.swing.JPanel {
  
  public static final String DISPLAY_BACKGROUND_COLOR_KEY  = "displaybackcolor";
  public static final String DISPLAY_TEXT_COLOR_KEY        = "displaytextcolor";
  public static final String DISPLAY_NORMAL_FONT_SIZE_KEY  = "displayfontsize";
  public static final String DISPLAY_FONT_NAME_KEY         = "displayfontname";
  public static final String DISPLAY_METHOD_KEY            = "displaymethod";
  public static final String DISPLAY_SECONDS_KEY           = "displayseconds";
  public static final String DISPLAY_TITLE_KEY             = "displaytitle";
  public static final String DISPLAY_SOURCE_KEY            = "displaysource";
  public static final String DISPLAY_TYPE_KEY              = "displaytype";
  public static final String DISPLAY_ADDED_KEY             = "displayadded";
  public static final String DISPLAY_ID_KEY                = "displayid";
  
  private DisplayWindow     displayWindow = null;
  
  private XOS               xos = XOS.getShared();
  
  private ProgramVersion    programVersion = ProgramVersion.getShared();
  
  private boolean           setupComplete = false;
  
  private String[]          fontList;
  
  private   Color           displayBackgroundColor   = new Color (255, 255, 255);
  private   Color           displayTextColor         = new Color (0, 0, 0);
  private   String          displayFont              = "Verdana";
  private   int             displayNormalFontSize    = 3;
  private   int             displayBigFontSize       = 4;
  
  private   boolean         displayTitle            = true;
  private   boolean         displaySource           = true;
  private   boolean         displayType             = true;
  private   boolean         displayAdded            = true;
  private   boolean         displayID               = true;
  
  /** Creates new form DisplayPrefs */
  public DisplayPrefs(DisplayWindow displayWindow) {
    this.displayWindow = displayWindow;
    initComponents();
    
    displayTextColor = StringUtils.hexStringToColor 
        (UserPrefs.getShared().getPref (DISPLAY_TEXT_COLOR_KEY, "000000"));
    
    displayBackgroundColor = StringUtils.hexStringToColor 
        (UserPrefs.getShared().getPref (DISPLAY_BACKGROUND_COLOR_KEY, "FFFFFF"));
    
    displayNormalFontSize 
        = UserPrefs.getShared().getPrefAsInt (DISPLAY_NORMAL_FONT_SIZE_KEY,  3);
    displayFontSizeSlider.setValue(displayNormalFontSize);
    if (displayNormalFontSize < 7) {
      displayBigFontSize = displayNormalFontSize + 1;
    } else {
      displayBigFontSize = 7;
    }

    displayFont = UserPrefs.getShared().getPref (DISPLAY_FONT_NAME_KEY, "Verdana");
    fontList 
        = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    for (int i = 0; i < fontList.length; i++) {
      displayFontComboBox.addItem (fontList [i]);
      if (displayFont.equals (fontList [i])) {
        displayFontComboBox.setSelectedItem (fontList [i]);
      }
    }

    displaySampleText();
    
    displayTitle
        = UserPrefs.getShared().getPrefAsBoolean (DISPLAY_TITLE_KEY, true);
    displayTitleCheckBox.setSelected (displayTitle);
    
    displaySource
        = UserPrefs.getShared().getPrefAsBoolean (DISPLAY_SOURCE_KEY, true);
    displaySourceCheckBox.setSelected (displaySource);
    
    displayType
        = UserPrefs.getShared().getPrefAsBoolean (DISPLAY_TYPE_KEY, true);
    displayTypeCheckBox.setSelected (displayType);
    
    displayAdded
        = UserPrefs.getShared().getPrefAsBoolean (DISPLAY_ADDED_KEY, true);
    displayAddedCheckBox.setSelected (displayAdded);
    
    displayID
        = UserPrefs.getShared().getPrefAsBoolean (DISPLAY_ID_KEY, true);
    displayIDCheckBox.setSelected (displayID);
    
    setupComplete = true;
  }
  
  public void displaySampleText () {
    StringBuffer text = new StringBuffer();
    text.append ("<html>");
    text.append ("<body bgcolor=\"#" 
        + StringUtils.colorToHexString(displayBackgroundColor)
        + "\" text=\"#"
        + StringUtils.colorToHexString(displayTextColor)
        + "\" link=\"#"
        + StringUtils.colorToHexString(displayTextColor)
        + "\" alink=\"#"
        + StringUtils.colorToHexString(displayTextColor)
        + "\" vlink=\"#"
        + StringUtils.colorToHexString(displayTextColor)
        + "\">");
    
    text.append ("<p><font size="
        + String.valueOf (displayNormalFontSize)
        + " face=\""
        + displayFont
        + ", Verdana, Arial, sans-serif\">" 
        + "&#8220;There is nothing worse than a brilliant image of a fuzzy concept.&#8221;"
        + "</font></p>");
    
    text.append ("</body>");
    text.append ("</html>");
    displayTextSample.setText (text.toString());
  }  
  
  public void savePrefs() {
    
  }
  
  public Color getDisplayBackgroundColor() {
    return displayBackgroundColor;
  }
  
  public Color getDisplayTextColor() {
    return displayTextColor;
  }
  
  public String getDisplayFont() {
    return displayFont;
  }
  
  public int getDisplayNormalFontSize() {
    return displayNormalFontSize;
  }
  
  public int getDisplayBigFontSize() {
    return displayBigFontSize;
  }
  
  public boolean getDisplayTitle() {
    return displayTitle;
  }
  
  public boolean getDisplaySource() {
    return displaySource;
  }
  
  public boolean getDisplayType() {
    return displayType;
  }
  
  public boolean getDisplayAdded() {
    return displayAdded;
  }
  
  public boolean getDisplayID() {
    return displayID;
  }

  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    displayBackgroundColorLabel = new javax.swing.JLabel();
    displayBackgroundColorButton = new javax.swing.JButton();
    displayTextColorLabel = new javax.swing.JLabel();
    displayTextColorButton = new javax.swing.JButton();
    displayFontSizeLabel = new javax.swing.JLabel();
    displayFontSizeSlider = new javax.swing.JSlider();
    displayTextSample = new javax.swing.JEditorPane();
    displayFontLabel = new javax.swing.JLabel();
    displayFontComboBox = new javax.swing.JComboBox();
    displayTitleCheckBox = new javax.swing.JCheckBox();
    displayFieldsLabel = new javax.swing.JLabel();
    displaySourceCheckBox = new javax.swing.JCheckBox();
    displayTypeCheckBox = new javax.swing.JCheckBox();
    displayAddedCheckBox = new javax.swing.JCheckBox();
    displayIDCheckBox = new javax.swing.JCheckBox();

    setLayout(new java.awt.GridBagLayout());

    displayBackgroundColorLabel.setText("Display Background:");
    displayBackgroundColorLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayBackgroundColorLabel, gridBagConstraints);

    displayBackgroundColorButton.setText("Select...");
    displayBackgroundColorButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        displayBackgroundColorButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayBackgroundColorButton, gridBagConstraints);

    displayTextColorLabel.setText("Display Text:");
    displayTextColorLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayTextColorLabel, gridBagConstraints);

    displayTextColorButton.setText("Select...");
    displayTextColorButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        displayTextColorButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayTextColorButton, gridBagConstraints);

    displayFontSizeLabel.setText("Display Font Size:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 2, 2, 2);
    add(displayFontSizeLabel, gridBagConstraints);

    displayFontSizeSlider.setMajorTickSpacing(1);
    displayFontSizeSlider.setMaximum(7);
    displayFontSizeSlider.setMinimum(1);
    displayFontSizeSlider.setPaintLabels(true);
    displayFontSizeSlider.setPaintTicks(true);
    displayFontSizeSlider.setSnapToTicks(true);
    displayFontSizeSlider.setValue(3);
    displayFontSizeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        displayFontSizeSliderStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayFontSizeSlider, gridBagConstraints);

    displayTextSample.setEditable(false);
    displayTextSample.setContentType("text/html"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayTextSample, gridBagConstraints);

    displayFontLabel.setText("Display Font:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayFontLabel, gridBagConstraints);

    displayFontComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        displayFontComboBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayFontComboBox, gridBagConstraints);

    displayTitleCheckBox.setText("Display Title?");
    displayTitleCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        displayTitleCheckBoxItemStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 9;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayTitleCheckBox, gridBagConstraints);

    displayFieldsLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    displayFieldsLabel.setText("Optional Fields");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 9;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayFieldsLabel, gridBagConstraints);

    displaySourceCheckBox.setText("Display Source?");
    displaySourceCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        displaySourceCheckBoxItemStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displaySourceCheckBox, gridBagConstraints);

    displayTypeCheckBox.setText("Display Source Type?");
    displayTypeCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        displayTypeCheckBoxItemStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 11;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayTypeCheckBox, gridBagConstraints);

    displayAddedCheckBox.setText("Display Added Date and Time?");
    displayAddedCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        displayAddedCheckBoxItemStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 12;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayAddedCheckBox, gridBagConstraints);

    displayIDCheckBox.setText("Display ID Number?");
    displayIDCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        displayIDCheckBoxItemStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 13;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayIDCheckBox, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void displayFontComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayFontComboBoxActionPerformed
    if (setupComplete) {
      displayFont = (String)displayFontComboBox.getSelectedItem();
      UserPrefs.getShared().setPref(DISPLAY_FONT_NAME_KEY, displayFont);
      displaySampleText();
      displayWindow.displayPrefsUpdated(this);
    }
  }//GEN-LAST:event_displayFontComboBoxActionPerformed

  private void displayFontSizeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_displayFontSizeSliderStateChanged
    if (! displayFontSizeSlider.getValueIsAdjusting()) {
      displayNormalFontSize = displayFontSizeSlider.getValue();
      UserPrefs.getShared().setPref(DISPLAY_NORMAL_FONT_SIZE_KEY, displayNormalFontSize);
      displaySampleText();
      displayWindow.displayPrefsUpdated(this);
    }
  }//GEN-LAST:event_displayFontSizeSliderStateChanged

  private void displayTextColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayTextColorButtonActionPerformed
    Color oldColor = displayTextColor;
    displayTextColor = JColorChooser.showDialog(
        this,
        "Choose Text Color for Display Tab",
        oldColor);
    UserPrefs.getShared().setPref(DISPLAY_TEXT_COLOR_KEY,
        StringUtils.colorToHexString(displayTextColor));
    displaySampleText();
    displayWindow.displayPrefsUpdated(this);
  }//GEN-LAST:event_displayTextColorButtonActionPerformed

  private void displayBackgroundColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayBackgroundColorButtonActionPerformed
    Color oldColor = displayBackgroundColor;
    displayBackgroundColor = JColorChooser.showDialog(
        this,
        "Choose Background Color for Display Tab",
        oldColor);
    UserPrefs.getShared().setPref(DISPLAY_BACKGROUND_COLOR_KEY,
        StringUtils.colorToHexString(displayBackgroundColor));
    displaySampleText();
    displayWindow.displayPrefsUpdated(this);
  }//GEN-LAST:event_displayBackgroundColorButtonActionPerformed

private void displayTitleCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_displayTitleCheckBoxItemStateChanged
  if (setupComplete) {
    displayTitle = displayTitleCheckBox.isSelected();
    UserPrefs.getShared().setPref (DISPLAY_TITLE_KEY, displayTitle);
  }
}//GEN-LAST:event_displayTitleCheckBoxItemStateChanged

private void displaySourceCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_displaySourceCheckBoxItemStateChanged
  if (setupComplete) {
    displaySource = displaySourceCheckBox.isSelected();
    UserPrefs.getShared().setPref (DISPLAY_SOURCE_KEY, displaySource);
  }
}//GEN-LAST:event_displaySourceCheckBoxItemStateChanged

private void displayTypeCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_displayTypeCheckBoxItemStateChanged
  if (setupComplete) {
    displayType = displayTypeCheckBox.isSelected();
    UserPrefs.getShared().setPref (DISPLAY_TYPE_KEY, displayType);
  }
}//GEN-LAST:event_displayTypeCheckBoxItemStateChanged

private void displayAddedCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_displayAddedCheckBoxItemStateChanged
  if (setupComplete) {
    displayAdded = displayAddedCheckBox.isSelected();
    UserPrefs.getShared().setPref (DISPLAY_ADDED_KEY, displayAdded);
  }
}//GEN-LAST:event_displayAddedCheckBoxItemStateChanged

private void displayIDCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_displayIDCheckBoxItemStateChanged
  if (setupComplete) {
    displayID = displayIDCheckBox.isSelected();
    UserPrefs.getShared().setPref (DISPLAY_ID_KEY, displayID);
  }
}//GEN-LAST:event_displayIDCheckBoxItemStateChanged
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JCheckBox displayAddedCheckBox;
  private javax.swing.JButton displayBackgroundColorButton;
  private javax.swing.JLabel displayBackgroundColorLabel;
  private javax.swing.JLabel displayFieldsLabel;
  private javax.swing.JComboBox displayFontComboBox;
  private javax.swing.JLabel displayFontLabel;
  private javax.swing.JLabel displayFontSizeLabel;
  private javax.swing.JSlider displayFontSizeSlider;
  private javax.swing.JCheckBox displayIDCheckBox;
  private javax.swing.JCheckBox displaySourceCheckBox;
  private javax.swing.JButton displayTextColorButton;
  private javax.swing.JLabel displayTextColorLabel;
  private javax.swing.JEditorPane displayTextSample;
  private javax.swing.JCheckBox displayTitleCheckBox;
  private javax.swing.JCheckBox displayTypeCheckBox;
  // End of variables declaration//GEN-END:variables
  
}
