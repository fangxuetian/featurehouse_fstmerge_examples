

package edu.rice.cs.drjava.ui.config;

import javax.swing.*;
import javax.swing.event.*;

import edu.rice.cs.drjava.*;
import edu.rice.cs.drjava.config.*;
import edu.rice.cs.util.swing.SwingFrame;


public class StringOptionComponent extends OptionComponent<String,JTextField> {
  private JTextField _jtf;

  public StringOptionComponent(StringOption opt, String text, SwingFrame parent) {
    super(opt, text, parent);
    _jtf = new JTextField();
    _jtf.setText(_option.format(DrJava.getConfig().getSetting(_option)));
    _jtf.getDocument().addDocumentListener(new DocumentListener() {
      public void insertUpdate(DocumentEvent e) { notifyChangeListeners(); }
      public void removeUpdate(DocumentEvent e) { notifyChangeListeners(); }
      public void changedUpdate(DocumentEvent e) { notifyChangeListeners(); }
    });
    setComponent(_jtf);
  }

  
  public StringOptionComponent (StringOption opt, String text, SwingFrame parent, String description) {
    this(opt, text, parent);
    setDescription(description);
  }

  
  public void setDescription(String description) {
    _jtf.setToolTipText(description);
    _label.setToolTipText(description);
  }

  
  public boolean updateConfig() {
    String oldValue = DrJava.getConfig().getSetting(_option);
    String newValue = _option.parse(_jtf.getText().trim());

    if (! oldValue.equals(newValue)) { DrJava.getConfig().setSetting(_option, newValue); }
    return true;
  }

  
  public void setValue(String value) { _jtf.setText(value); }
}