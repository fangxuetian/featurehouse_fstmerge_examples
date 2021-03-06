

package net.sf.jabref.groups;

import java.util.*;

import javax.swing.undo.AbstractUndoableEdit;

import net.sf.jabref.*;
import net.sf.jabref.util.QuotedStringTokenizer;


public class ExplicitGroup extends AbstractGroup implements SearchRule {
    public static final String ID = "ExplicitGroup:";

    private final Set<BibtexEntry> m_entries;

    public ExplicitGroup(String name, int context) {
        super(name, context);
        m_entries = new HashSet<BibtexEntry>();
    }

    public static AbstractGroup fromString(String s, BibtexDatabase db,
            int version) throws Exception {
        if (!s.startsWith(ID))
            throw new Exception(
                    "Internal error: ExplicitGroup cannot be created from \""
                            + s
                            + "\". "
                            + "Please report this on www.sf.net/projects/jabref");
        QuotedStringTokenizer tok = new QuotedStringTokenizer(s.substring(ID
                .length()), SEPARATOR, QUOTE_CHAR);
        switch (version) {
        case 0:
        case 1:
        case 2: {
            ExplicitGroup newGroup = new ExplicitGroup(tok.nextToken(),
                    AbstractGroup.INDEPENDENT);
            newGroup.addEntries(tok, db);
            return newGroup;
        }
        case 3: {
            String name = tok.nextToken();
            int context = Integer.parseInt(tok.nextToken());
            ExplicitGroup newGroup = new ExplicitGroup(name, context);
            newGroup.addEntries(tok, db);
            return newGroup;
        }
        default:
            throw new UnsupportedVersionException("ExplicitGroup", version);
        }
    }

    
    protected void addEntries(QuotedStringTokenizer tok, BibtexDatabase db) {
        BibtexEntry[] entries;
        while (tok.hasMoreTokens()) {
            entries = db.getEntriesByKey(Util.unquote(tok.nextToken(),
                    QUOTE_CHAR));
            for (int i = 0; i < entries.length; ++i)
                m_entries.add(entries[i]);
        }
    }

    public SearchRule getSearchRule() {
        return this;
    }

    public boolean supportsAdd() {
        return true;
    }

    public boolean supportsRemove() {
        return true;
    }

    public AbstractUndoableEdit add(BibtexEntry[] entries) {
        if (entries.length == 0)
            return null; 

        HashSet<BibtexEntry> entriesBeforeEdit = new HashSet<BibtexEntry>(m_entries);
        for (int i = 0; i < entries.length; ++i)
            m_entries.add(entries[i]);

        return new UndoableChangeAssignment(entriesBeforeEdit, m_entries);
    }

    public boolean addEntry(BibtexEntry entry) {
        return m_entries.add(entry);
    }

    public AbstractUndoableEdit remove(BibtexEntry[] entries) {
        if (entries.length == 0)
            return null; 

        HashSet<BibtexEntry> entriesBeforeEdit = new HashSet<BibtexEntry>(m_entries);
        for (int i = 0; i < entries.length; ++i)
            m_entries.remove(entries[i]);

        return new UndoableChangeAssignment(entriesBeforeEdit, m_entries);
    }

    public boolean removeEntry(BibtexEntry entry) {
        return m_entries.remove(entry);
    }

    public boolean contains(BibtexEntry entry) {
        return m_entries.contains(entry);
    }

    public boolean contains(Map<String, String> searchOptions, BibtexEntry entry) {
        return contains(entry);
    }

    public int applyRule(Map<String, String> searchStrings, BibtexEntry bibtexEntry) {
        return contains(searchStrings, bibtexEntry) ? 1 : 0;
    }

    public AbstractGroup deepCopy() {
        ExplicitGroup copy = new ExplicitGroup(m_name, m_context);
        copy.m_entries.addAll(m_entries);
        return copy;
    }

    public boolean equals(Object o) {
        if (!(o instanceof ExplicitGroup))
            return false;
        ExplicitGroup other = (ExplicitGroup) o;
        
        if (m_entries.size() != other.m_entries.size())
            return false; 
        HashSet<String> keys = new HashSet<String>();
        BibtexEntry entry;
        String key;
        
        for (Iterator<BibtexEntry> it = m_entries.iterator(); it.hasNext(); ) {
            entry = it.next();
            key = entry.getCiteKey();
            if (key != null)
                keys.add(key);
        }
        for (Iterator<BibtexEntry> it = other.m_entries.iterator(); it.hasNext(); ) {
            entry = it.next();
            key = entry.getCiteKey();
            if (key != null)
                if (!keys.remove(key))
                    return false;
        }
        if (!keys.isEmpty())
            return false;
        return other.m_name.equals(m_name)
                && other.getHierarchicalContext() == getHierarchicalContext();
    }

    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(ID).append(Util.quote(m_name, SEPARATOR, QUOTE_CHAR)).append(SEPARATOR).append(m_context).append(SEPARATOR);
        String s;
        
        Set<String> sortedKeys = new TreeSet<String>();
        for (Iterator<BibtexEntry> it = m_entries.iterator(); it.hasNext();) {
            s = it.next().getCiteKey();
            if (s != null && !s.equals("")) 
                sortedKeys.add(s);
        }
        for (Iterator<String> it = sortedKeys.iterator(); it.hasNext();) {
            sb.append(Util.quote(it.next(), SEPARATOR, QUOTE_CHAR)).append(SEPARATOR);
        }
        return sb.toString();
    }

    
    public void clearAssignments() {
        m_entries.clear();
    }

    public boolean isDynamic() {
        return false;
    }

    public String getDescription() {
        return getDescriptionForPreview();
    }

    public static String getDescriptionForPreview() {
        return Globals
                .lang("This group contains entries based on manual assignment. "
                        + "Entries can be assigned to this group by selecting them "
                        + "then using either drag and drop or the context menu. "
                        + "Entries can be removed from this group by selecting them "
                        + "then using the context menu. Every entry assigned to this group "
                        + "must have a unique key. The key may be changed at any time "
                        + "as long as it remains unique.");
    }

    public String getShortDescription() {
        StringBuffer sb = new StringBuffer();
        sb.append("<b>").append(getName()).append(Globals.lang("</b> - static group"));
        switch (getHierarchicalContext()) {
        case AbstractGroup.INCLUDING:
            sb.append(Globals.lang(", includes subgroups"));
            break;
        case AbstractGroup.REFINING:
            sb.append(Globals.lang(", refines supergroup"));
            break;
        default:
            break;
        }
        return sb.toString();
    }
    
    
        public void refreshForNewDatabase(BibtexDatabase db) {
            Set<BibtexEntry> newSet = new HashSet<BibtexEntry>();
            for (Iterator<BibtexEntry> i=m_entries.iterator(); i.hasNext();) {
                BibtexEntry entry = i.next();
                BibtexEntry sameEntry = db.getEntryByKey(entry.getCiteKey());
                
                newSet.add(sameEntry);
            }
            m_entries.clear();
            m_entries.addAll(newSet);
        }

		public Set<BibtexEntry> getEntries(){
			return m_entries;
		}

    public String getTypeId() {
        return ID;
    }
}
