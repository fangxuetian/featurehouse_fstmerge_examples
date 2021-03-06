package net.sf.jabref;

import java.util.Comparator;
import java.util.List;


public class FieldComparatorStack<T> implements Comparator<T> {

    List<? extends Comparator<? super T>> comparators;

    public FieldComparatorStack(List<? extends Comparator<? super T>> comparators) {
        this.comparators = comparators;
    }

    public int compare(T o1, T o2) {
    	for (Comparator<? super T> comp : comparators){
    		int res = comp.compare(o1, o2);
            if (res != 0)
                return res;
        }
        return 0;
    }
}
