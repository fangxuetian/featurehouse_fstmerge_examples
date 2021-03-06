
package net.sf.jabref.undo;

import javax.swing.undo.AbstractUndoableEdit;

import net.sf.jabref.*;

public class UndoableInsertString extends AbstractUndoableEdit {
    
    private BibtexDatabase base;
    private BasePanel panel;
    private BibtexString string;

    public UndoableInsertString(BasePanel panel, BibtexDatabase base,
				BibtexString string) {
	this.base = base;
	this.panel = panel;
	this.string = string;
    }

    public String getUndoPresentationName() {
	return Globals.lang("Undo")+": "+Globals.lang("insert string ");
    }

    public String getRedoPresentationName() {
	return Globals.lang("Redo")+": "+Globals.lang("insert string ");
    }

    public void undo() {
	super.undo();
	
	
	base.removeString(string.getId());
	panel.updateStringDialog();
    }

    public void redo() {
	super.redo();

	
	try {
	    base.addString(string);
	} catch (KeyCollisionException ex) {
	    ex.printStackTrace();
	}

	panel.updateStringDialog();
    }



}
