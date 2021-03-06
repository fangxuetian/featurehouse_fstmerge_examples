package net.sf.jabref;

import java.util.Comparator;


public class IdComparator implements Comparator<BibtexEntry> {

    public int compare(BibtexEntry one, BibtexEntry two) {
        return one.getId().compareTo(two.getId());
    }
}
