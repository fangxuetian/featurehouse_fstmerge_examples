
package net.sf.jabref;

import net.sf.jabref.gui.AutoCompleteListener;

import java.awt.*;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.UndoableEditListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;
import javax.swing.undo.CannotUndoException;


public class FieldTextArea extends JTextArea implements FieldEditor {

	Dimension PREFERRED_SIZE;

	JScrollPane sp;

	FieldNameLabel label;

	String fieldName;

	final static Pattern bull = Pattern.compile("\\s*[-\\*]+.*");

	final static Pattern indent = Pattern.compile("\\s+.*");

    private AutoCompleteListener autoCompleteListener = null;

    

	public FieldTextArea(String fieldName_, String content) {
		super(content);

        
        

        updateFont();

		
		
		addFocusListener(Globals.focusListener);
		addFocusListener(new FieldEditorFocusListener());
		sp = new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setMinimumSize(new Dimension(200, 1));

		setLineWrap(true);
		setWrapStyleWord(true);
		fieldName = fieldName_;

		label = new FieldNameLabel(" " + Util.nCase(fieldName) + " ");
		setBackground(GUIGlobals.validFieldBackground);

        

		FieldTextMenu popMenu = new FieldTextMenu(this);
		this.addMouseListener(popMenu);
		label.addMouseListener(popMenu);
	}

	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String newName) {
		fieldName = newName;
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabelColor(Color c) {
		label.setForeground(c);
	}

	public JComponent getPane() {
		return sp;
	}

	public JComponent getTextComponent() {
		return this;
	}


    public void updateFont() {
        setFont(GUIGlobals.CURRENTFONT);
    }

    public void paste(String textToInsert) {
		int sel = getSelectionEnd() - getSelectionStart();
		if (sel > 0) 
			replaceSelection(textToInsert);
		else {
			int cPos = this.getCaretPosition();
			this.insert(textToInsert, cPos);
		}
	}


    public boolean hasUndoInformation() {
        return false;
    }

    public void undo() {
        

    }

    public boolean hasRedoInformation() {
        return false;
    }

    public void redo() {
        

    }

    public void addUndoableEditListener(UndoableEditListener listener) {
        getDocument().addUndoableEditListener(listener);
    }

    public void setAutoCompleteListener(AutoCompleteListener listener) {
        autoCompleteListener = listener;
    }

    public void clearAutoCompleteSuggestion() {
        if (autoCompleteListener != null) {
            autoCompleteListener.clearCurrentSuggestion(this);
        }
    }
}

