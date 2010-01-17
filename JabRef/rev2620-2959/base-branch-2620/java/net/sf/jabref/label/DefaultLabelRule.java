
package net.sf.jabref.label;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.BibtexFields;

public class DefaultLabelRule implements LabelRule {


    
    public String applyRule(BibtexEntry oldEntry){
        return oldEntry.getField(BibtexFields.KEY_FIELD);
    }


}



