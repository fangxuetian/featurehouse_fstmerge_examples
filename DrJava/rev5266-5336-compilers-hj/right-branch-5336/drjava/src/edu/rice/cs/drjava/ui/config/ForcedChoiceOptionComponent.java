

package edu.rice.cs.drjava.ui.config;

import javax.swing.*;
import edu.rice.cs.drjava.config.*;
import edu.rice.cs.drjava.*;
import edu.rice.cs.util.swing.SwingFrame;

import java.awt.event.*;
import java.util.Iterator;


public class ForcedChoiceOptionComponent extends OptionComponent<String,JComboBox> {
  private volatile JComboBox _comboBox;

  
  public ForcedChoiceOptionComponent(ForcedChoiceOption option, String labelText, SwingFrame parent) {
    super(option, labelText, parent);

    
    Iterator<String> values = option.getLegalValues();
    _comboBox = new JComboBox();
    
    _comboBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) { notifyChangeListeners(); }
    });

    while(values.hasNext()) {
      String currValue = values.next();
      _comboBox.addItem(currValue);
    }

    resetToCurrent(DrJava.getConfig().getSetting(_option));
    setComponent(_comboBox);
  }

  
  public ForcedChoiceOptionComponent(ForcedChoiceOption option, String labelText, SwingFrame parent, String description) {
    this(option, labelText, parent);
    setDescription(description);
  }

  
  public void setDescription(String description) {
    _comboBox.setToolTipText(description);
    _label.setToolTipText(description);
  }

  
  public void resetToCurrent(String current) {

    _comboBox.setSelectedItem(current);
    











  }

  
  public boolean updateConfig() {
    String oldValue = DrJava.getConfig().getSetting(_option);
    String newValue = _comboBox.getSelectedItem().toString();

    if (! newValue.equals(oldValue)) { DrJava.getConfig().setSetting(_option, newValue); }

    return true;
  }
  
  
  public String getCurrentComboBoxValue() {
    return _comboBox.getSelectedItem().toString();
  }

  
  public void setValue(String value) { resetToCurrent(value); }
}