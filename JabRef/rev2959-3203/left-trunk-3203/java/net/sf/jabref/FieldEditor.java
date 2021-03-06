

package net.sf.jabref;

import net.sf.jabref.gui.AutoCompleteListener;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.UndoableEditListener;



public interface FieldEditor {

	public String getFieldName();

	
	public JComponent getPane();

	
	public JComponent getTextComponent();

	public JLabel getLabel();

	public void setLabelColor(Color c);

	public void setBackground(Color c);

	public String getText();

	
	public void setText(String newText);

	public void append(String text);

	public Container getParent();

	public void requestFocus();

	public void setEnabled(boolean enabled);

    public void updateFont();
    
	public void paste(String textToInsert);

	
	public String getSelectedText();


    public boolean hasUndoInformation();

    public void undo();

    public boolean hasRedoInformation();

    public void redo();

    public void addUndoableEditListener(UndoableEditListener listener);

    public void setAutoCompleteListener(AutoCompleteListener listener);

    public void clearAutoCompleteSuggestion();
}
