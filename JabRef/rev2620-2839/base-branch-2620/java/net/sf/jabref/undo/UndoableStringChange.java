

package net.sf.jabref.undo;

import javax.swing.undo.AbstractUndoableEdit;

import net.sf.jabref.BasePanel;
import net.sf.jabref.BibtexString;
import net.sf.jabref.Globals;

public class UndoableStringChange extends AbstractUndoableEdit {

    private BibtexString string;
    private String oldValue, newValue;
    private boolean nameChange;
    private BasePanel panel;
    

    public UndoableStringChange(BasePanel panel,
				BibtexString string, boolean nameChange,
				String oldValue, String newValue) {
	this.string = string;
	this.oldValue = oldValue;
	this.newValue = newValue;
	this.nameChange = nameChange;
	this.panel = panel;
    }

    public String getUndoPresentationName() {
	return Globals.lang("Undo")+": "
	    +Globals.lang(nameChange ? "change string name" : "change string content");
    }

    public String getRedoPresentationName() {
	return Globals.lang("Redo")+": "
	    +Globals.lang(nameChange ? "change string name" : "change string content");
    }

    public void undo() {
	super.undo();
	
	

	panel.assureStringDialogNotEditing();

	if (nameChange)
	    string.setName(oldValue);
	else
	    string.setContent(oldValue);

	panel.updateStringDialog();
    }

    public void redo() {
	super.redo();

	

	panel.assureStringDialogNotEditing();
	if (nameChange)
	    string.setName(newValue);
	else
	    string.setContent(newValue);

	panel.updateStringDialog();
    }



}
