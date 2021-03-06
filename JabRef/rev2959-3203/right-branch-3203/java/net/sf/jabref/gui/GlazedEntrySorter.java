
package net.sf.jabref.gui;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.DatabaseChangeEvent;
import net.sf.jabref.DatabaseChangeListener;
import net.sf.jabref.IdComparator;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

public class GlazedEntrySorter implements DatabaseChangeListener {

	EventList<BibtexEntry> list;

    String[] idArray;
    BibtexEntry[] entryArray;
    
    public GlazedEntrySorter(Map<String, BibtexEntry> entries) {
        list = new BasicEventList<BibtexEntry>();
        list.getReadWriteLock().writeLock().lock();
        Set<String> keySet = entries.keySet();
        if (keySet != null) {
            Iterator<String> i = keySet.iterator();
            while (i.hasNext()) {
                list.add(entries.get(i.next()));
            }
        }

        
        
        Collections.sort(list, new IdComparator());
        
        list.getReadWriteLock().writeLock().unlock();

    }

    public EventList<BibtexEntry> getTheList() {
        return list;
    }

    public void databaseChanged(DatabaseChangeEvent e) {
        list.getReadWriteLock().writeLock().lock();
        if (e.getType() == DatabaseChangeEvent.ADDED_ENTRY) {
            list.add(e.getEntry());
        } else if (e.getType() == DatabaseChangeEvent.REMOVED_ENTRY) {
            list.remove(e.getEntry());
        } else if (e.getType() == DatabaseChangeEvent.CHANGED_ENTRY) {
            int index = list.indexOf(e.getEntry());
            list.set(index, e.getEntry());
        }
        list.getReadWriteLock().writeLock().unlock();

    }


}
