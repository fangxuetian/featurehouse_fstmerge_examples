
package net.sf.jabref.export.layout.format;

import net.sf.jabref.export.layout.LayoutFormatter;
import net.sf.jabref.AuthorList;


public class AuthorLastFirstAbbrCommas implements LayoutFormatter {

    public String format(String fieldText) {
        return AuthorList.fixAuthor_lastNameFirstCommas(fieldText, true, false);
    }

}