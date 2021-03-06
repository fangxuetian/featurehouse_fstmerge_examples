

package net.sf.jabref.groups;

import java.util.Map;

import javax.swing.undo.AbstractUndoableEdit;

import net.sf.jabref.BibtexDatabase;
import net.sf.jabref.BibtexEntry;
import net.sf.jabref.Globals;
import net.sf.jabref.SearchRule;


public class AllEntriesGroup extends AbstractGroup implements SearchRule {
    public static final String ID = "AllEntriesGroup:";

    public AllEntriesGroup() {
        super(Globals.lang("All Entries"), AbstractGroup.INDEPENDENT);
    }
    
    public static AbstractGroup fromString(String s, BibtexDatabase db, int version) throws Exception {
        if (!s.startsWith(ID))
            throw new Exception(
                    "Internal error: AllEntriesGroup cannot be created from \""
                            + s + "\". "
                            + "Please report this on www.sf.net/projects/jabref");
        switch (version) {
        case 0:
        case 1:
        case 2:
        case 3:
            return new AllEntriesGroup();
        default:
            throw new UnsupportedVersionException("AllEntriesGroup", version); 
        }
    }

    public SearchRule getSearchRule() {
        return this;
    }

    public boolean supportsAdd() {
        return false;
    }

    public boolean supportsRemove() {
        return false;
    }

    public AbstractUndoableEdit add(BibtexEntry[] entries) {
        
        return null;
    }

    public AbstractUndoableEdit remove(BibtexEntry[] entries) {
        
        return null;
    }

    public boolean contains(Map<String, String> searchOptions, BibtexEntry entry) {
        return true; 
    }

    public AbstractGroup deepCopy() {
        return new AllEntriesGroup();
    }

    public int applyRule(Map<String, String> searchStrings, BibtexEntry bibtexEntry) {
        return 1; 
    }

    public boolean equals(Object o) {
        return o instanceof AllEntriesGroup;
    }

    public String toString() {
        return ID;
    }

    public boolean contains(BibtexEntry entry) {
        return true;
    }
    
    public boolean isDynamic() {
    	
    	return false;
    }

	public String getDescription() {
		return "This group contains all entries. It cannot be edited or removed.";
		
	}
	
	public String getShortDescription() {
		return Globals.lang("<b>All Entries</b> (this group cannot be edited or removed)");
	}

    public String getTypeId() {
        return ID;
    }
}
