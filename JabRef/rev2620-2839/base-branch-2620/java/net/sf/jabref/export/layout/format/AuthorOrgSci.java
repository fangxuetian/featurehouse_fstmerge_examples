package net.sf.jabref.export.layout.format;

import net.sf.jabref.AuthorList;
import net.sf.jabref.AuthorList.Author;
import net.sf.jabref.export.layout.LayoutFormatter;


public class AuthorOrgSci implements LayoutFormatter {

	public String format(String fieldText) {
		AuthorList a = AuthorList.getAuthorList(fieldText);
		if (a.size() == 0) {
			return fieldText;
		}
		Author first = a.getAuthor(0);
		StringBuffer sb = new StringBuffer();
		sb.append(first.getLastFirst(true));
		for (int i = 1; i < a.size(); i++) {
			sb.append(", ").append(a.getAuthor(i).getFirstLast(true));
		}
		return sb.toString();
	}
}
