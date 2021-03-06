package net.sf.jabref.search;

import net.sf.jabref.BibtexEntry;
import ca.odell.glazedlists.matchers.Matcher;


public class SearchMatcher implements Matcher<BibtexEntry> {

	public static SearchMatcher INSTANCE = new SearchMatcher();

	public boolean matches(BibtexEntry entry) {
		return entry.isSearchHit();
	}
}
