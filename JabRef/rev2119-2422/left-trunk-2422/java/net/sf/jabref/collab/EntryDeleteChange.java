package net.sf.jabref.collab;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import net.sf.jabref.*;
import net.sf.jabref.undo.NamedCompound;
import net.sf.jabref.undo.UndoableRemoveEntry;

public class EntryDeleteChange extends Change {

  BibtexEntry memEntry, tmpEntry, diskEntry;
  boolean isModifiedLocally;
  double matchWithTmp;
  PreviewPanel pp;
  JScrollPane sp;

  public EntryDeleteChange(BibtexEntry memEntry, BibtexEntry tmpEntry) {
    super("Deleted entry");
    this.memEntry = memEntry;
    this.tmpEntry = tmpEntry;

    
    
    matchWithTmp = Util.compareEntriesStrictly(memEntry, tmpEntry);

    
    isModifiedLocally = !(matchWithTmp > 1);

    
    

    pp = new PreviewPanel(null, memEntry, null, new MetaData(), Globals.prefs.get("preview0"));
    sp = new JScrollPane(pp);
  }

  public void makeChange(BasePanel panel, NamedCompound undoEdit) {
    panel.database().removeEntry(memEntry.getId());
    undoEdit.addEdit(new UndoableRemoveEntry(panel.database(), memEntry, panel));
  }

  JComponent description() {
    return sp;
  }
}
