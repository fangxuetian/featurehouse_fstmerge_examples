package net.sf.jabref.export.layout.format;

import net.sf.jabref.export.layout.AbstractParamLayoutFormatter;


public class Replace extends AbstractParamLayoutFormatter {

    private String regex = null, replaceWith = null;


    public void setArgument(String arg) {
        String[] parts = parseArgument(arg);

        if (parts.length < 2)
            return; 
        regex = parts[0];
        replaceWith = parts[1];

    }

    public String format(String fieldText) {
        if (regex == null)
            return fieldText; 
        return fieldText.replaceAll(regex, replaceWith);
    }
}
