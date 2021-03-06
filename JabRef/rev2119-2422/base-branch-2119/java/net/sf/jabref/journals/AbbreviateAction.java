package net.sf.jabref.journals;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.Globals;
import net.sf.jabref.BasePanel;
import net.sf.jabref.AbstractWorker;
import net.sf.jabref.undo.NamedCompound;


public class AbbreviateAction extends AbstractWorker {
    BasePanel panel;
    String message = "";
    boolean iso;

    public AbbreviateAction(BasePanel panel, boolean iso) {
        this.panel = panel;
        this.iso = iso;
    }


    public void init() {
        
        panel.output("Abbreviating...");
    }

    public void run() {
        


        BibtexEntry[] entries = panel.getSelectedEntries();
        if (entries == null)
            return;
        NamedCompound ce = new NamedCompound("Abbreviate journal names");
        int count = 0;
        for (int i = 0; i < entries.length; i++) {
            if (Globals.journalAbbrev.abbreviate(entries[i], "journal", ce, iso))
                count++;
        }
        if (count > 0) {
            ce.end();
            panel.undoManager.addEdit(ce);
            panel.markBaseChanged();
            message = Globals.lang("Abbreviated %0 journal names.", String.valueOf(count));
        } else {
            message = Globals.lang("No journal names could be abbreviated.");
        }
    }

    public void update() {
        panel.output(message);
    }
}
