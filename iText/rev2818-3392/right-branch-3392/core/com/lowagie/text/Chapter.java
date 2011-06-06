

package com.lowagie.text;

import java.util.ArrayList;



public class Chapter extends Section {
    
    
    private static final long serialVersionUID = 1791000695779357361L;
    
    
    public Chapter(int number) {
        super(null, 1);
        numbers = new ArrayList<Integer>();
        numbers.add(new Integer(number));
        triggerNewPage = true;
    }
    
    
    
    public Chapter(Paragraph title, int number) {
        super(title, 1);
        numbers = new ArrayList<Integer>();
        numbers.add(new Integer(number));
        triggerNewPage = true;
    }
    
    
    public Chapter(String title, int number) {
        this(new Paragraph(title), number);
    }
    
    
    
    
    public int type() {
        return Element.CHAPTER;
    }

    
    public boolean isNestable() {
        return false;
    }

}