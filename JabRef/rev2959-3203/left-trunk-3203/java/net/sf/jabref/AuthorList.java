package net.sf.jabref;

import net.sf.jabref.export.layout.format.CreateDocBookAuthors;

import java.util.Vector;
import java.util.WeakHashMap;


public class AuthorList {

	private Vector<Author> authors; 

	
	
	private String authorsNatbib = null, authorsFirstFirstAnds = null,
		authorsAlph = null;

	private String[] authorsFirstFirst = new String[4], authorsLastOnly = new String[2],
	authorLastFirstAnds = new String[2], 
	authorsLastFirst = new String[4],
    authorsLastFirstFirstLast = new String[2];


	

	private String orig; 

	
	private int token_start; 

	private int token_end; 

	
	private int token_abbr; 

	

	private char token_term; 

	private boolean token_case; 

	

	
	
	
	private Vector<Object> tokens;

	private static final int TOKEN_GROUP_LENGTH = 4; 

	

	
	private static final int OFFSET_TOKEN = 0; 

	private static final int OFFSET_TOKEN_ABBR = 1; 

	

	private static final int OFFSET_TOKEN_TERM = 2; 

	
	

	
	
	
	
	
	int von_start, 
		last_start, 
		
		comma_first, 
		comma_second; 

	

	
	private static final int TOKEN_EOF = 0;

	private static final int TOKEN_AND = 1;

	private static final int TOKEN_COMMA = 2;

	private static final int TOKEN_WORD = 3;

	
	private static final java.util.HashSet<String> tex_names = new java.util.HashSet<String>();
	
	static {
		tex_names.add("aa");
		tex_names.add("ae");
		tex_names.add("l");
		tex_names.add("o");
		tex_names.add("oe");
		tex_names.add("i");
		tex_names.add("AA");
		tex_names.add("AE");
		tex_names.add("L");
		tex_names.add("O");
		tex_names.add("OE");
		tex_names.add("j");
	}

	static WeakHashMap<String, AuthorList> authorCache = new WeakHashMap<String, AuthorList>();

	
	protected AuthorList(String bibtex_authors) {
		authors = new Vector<Author>(5); 
		orig = bibtex_authors; 
		token_start = 0;
		token_end = 0; 
		while (token_start < orig.length()) {
			Author author = getAuthor();
			if (author != null)
				authors.add(author);
		}
		
		orig = null;
		tokens = null;
	}

	
	public static AuthorList getAuthorList(String authors) {
		AuthorList authorList = authorCache.get(authors);
		if (authorList == null) {
			authorList = new AuthorList(authors);
			authorCache.put(authors, authorList);
		}
		return authorList;
	}

	
	public static String fixAuthor_firstNameFirstCommas(String authors, boolean abbr,
		boolean oxfordComma) {
		return getAuthorList(authors).getAuthorsFirstFirst(abbr, oxfordComma);
	}

	
	public static String fixAuthor_firstNameFirst(String authors) {
		return getAuthorList(authors).getAuthorsFirstFirstAnds();
	}

	
	public static String fixAuthor_lastNameFirstCommas(String authors, boolean abbr,
		boolean oxfordComma) {
		return getAuthorList(authors).getAuthorsLastFirst(abbr, oxfordComma);
	}

	
	public static String fixAuthor_lastNameFirst(String authors) {
		return getAuthorList(authors).getAuthorsLastFirstAnds(false);
	}
	
	
	public static String fixAuthor_lastNameFirst(String authors, boolean abbreviate) {
		return getAuthorList(authors).getAuthorsLastFirstAnds(abbreviate);
	}

	
	public static String fixAuthor_lastNameOnlyCommas(String authors, boolean oxfordComma) {
		return getAuthorList(authors).getAuthorsLastOnly(oxfordComma);
	}

	
	public static String fixAuthorForAlphabetization(String authors) {
		return getAuthorList(authors).getAuthorsForAlphabetization();
	}

	
	public static String fixAuthor_Natbib(String authors) {
		return AuthorList.getAuthorList(authors).getAuthorsNatbib();
	}

	
	private Author getAuthor() {

		tokens = new Vector<Object>(); 
		von_start = -1;
		last_start = -1;
		comma_first = -1;
		comma_second = -1;

		
		token_loop: while (true) {
			int token = getToken();
			cases: switch (token) {
			case TOKEN_EOF:
			case TOKEN_AND:
				break token_loop;
			case TOKEN_COMMA:
				if (comma_first < 0)
					comma_first = tokens.size();
				else if (comma_second < 0)
					comma_second = tokens.size();
				break cases;
			case TOKEN_WORD:
				tokens.add(orig.substring(token_start, token_end));
				tokens.add(orig.substring(token_start, token_abbr));
				tokens.add(new Character(token_term));
				tokens.add(Boolean.valueOf(token_case));
				if (comma_first >= 0)
					break cases;
				if (last_start >= 0)
					break cases;
				if (von_start < 0) {
					if (!token_case) {
						von_start = tokens.size() - TOKEN_GROUP_LENGTH;
						break cases;
					}
				} else if (last_start < 0 && token_case) {
					last_start = tokens.size() - TOKEN_GROUP_LENGTH;
					break cases;
				}
			}
		}

		
		
		if (tokens.size() == 0)
			return null; 


		
		int first_part_start = -1, von_part_start = -1, last_part_start = -1, jr_part_start = -1;
		int first_part_end = 0, von_part_end = 0, last_part_end = 0, jr_part_end = 0;
        boolean jrAsFirstname = false;
		if (comma_first < 0) { 
			if (von_start < 0) { 
				last_part_end = tokens.size();
				last_part_start = tokens.size() - TOKEN_GROUP_LENGTH;
				int index = tokens.size() - 2 * TOKEN_GROUP_LENGTH + OFFSET_TOKEN_TERM;
				if (index > 0) {
					Character ch = (Character)tokens.elementAt(index);
					if (ch.charValue() == '-')
						last_part_start -= TOKEN_GROUP_LENGTH;
				}
				first_part_end = last_part_start;
				if (first_part_end > 0) {
					first_part_start = 0;
				}
			} else { 
				if (last_start >= 0) {
					last_part_end = tokens.size();
					last_part_start = last_start;
					von_part_end = last_part_start;
				} else {
					von_part_end = tokens.size();
				}
				von_part_start = von_start;
				first_part_end = von_part_start;
				if (first_part_end > 0)
					first_part_start = 0;
			}
		} else { 
			
			first_part_end = tokens.size();
			if (comma_second < 0) { 
				if (comma_first < first_part_end) {
                    first_part_start = comma_first;
                    
                    
                }
			} else { 
				if (comma_second < first_part_end)
					first_part_start = comma_second;
				jr_part_end = comma_second;
				if (comma_first < jr_part_end)
					jr_part_start = comma_first;
			}
			if (von_start != 0) { 
				last_part_end = comma_first;
				if (last_part_end > 0)
					last_part_start = 0;
			} else { 
				if (last_start < 0) {
					von_part_end = comma_first;
				} else {
					last_part_end = comma_first;
					last_part_start = last_start;
					von_part_end = last_part_start;
				}
				von_part_start = 0;
			}
		}

        if ((first_part_start == -1) && (last_part_start == -1) && (von_part_start != -1)) {
            
            
            
            
            last_part_start = von_part_start;
            last_part_end = von_part_end;
            von_part_start = -1;
            von_part_end = -1;
        }
        if (jrAsFirstname) {
            
            
            
        }

		
		return new Author((first_part_start < 0 ? null : concatTokens(first_part_start,
			first_part_end, OFFSET_TOKEN, false)), (first_part_start < 0 ? null : concatTokens(
			first_part_start, first_part_end, OFFSET_TOKEN_ABBR, true)), (von_part_start < 0 ? null
			: concatTokens(von_part_start, von_part_end, OFFSET_TOKEN, false)),
			(last_part_start < 0 ? null : concatTokens(last_part_start, last_part_end,
				OFFSET_TOKEN, false)), (jr_part_start < 0 ? null : concatTokens(jr_part_start,
				jr_part_end, OFFSET_TOKEN, false)));
	}

	
	private String concatTokens(int start, int end, int offset, boolean dot_after) {
		StringBuffer res = new StringBuffer();
		
		res.append((String) tokens.get(start + offset));
		if (dot_after)
			res.append('.');
		start += TOKEN_GROUP_LENGTH;
		while (start < end) {
			res.append(tokens.get(start - TOKEN_GROUP_LENGTH + OFFSET_TOKEN_TERM));
			res.append((String) tokens.get(start + offset));
			if (dot_after)
				res.append('.');
			start += TOKEN_GROUP_LENGTH;
		}
		return res.toString();
	}

	
	private int getToken() {
		token_start = token_end;
		while (token_start < orig.length()) {
			char c = orig.charAt(token_start);
			if (!(c == '~' || c == '-' || Character.isWhitespace(c)))
				break;
			token_start++;
		}
		token_end = token_start;
		if (token_start >= orig.length())
			return TOKEN_EOF;
		if (orig.charAt(token_start) == ',') {
			token_end++;
			return TOKEN_COMMA;
		}
		token_abbr = -1;
		token_term = ' ';
		token_case = true;
		int braces_level = 0;
		int current_backslash = -1;
		boolean first_letter_is_found = false;
		while (token_end < orig.length()) {
			char c = orig.charAt(token_end);
			if (c == '{') {
				braces_level++;
			}
			if (braces_level > 0)
				if (c == '}')
					braces_level--;
			if (first_letter_is_found && token_abbr < 0 && braces_level == 0)
				token_abbr = token_end;
			if (!first_letter_is_found && current_backslash < 0 && Character.isLetter(c)) {
				token_case = Character.isUpperCase(c);
				first_letter_is_found = true;
			}
			if (current_backslash >= 0 && !Character.isLetter(c)) {
				if (!first_letter_is_found) {
					String tex_cmd_name = orig.substring(current_backslash + 1, token_end);
					if (tex_names.contains(tex_cmd_name)) {
						token_case = Character.isUpperCase(tex_cmd_name.charAt(0));
						first_letter_is_found = true;
					}
				}
				current_backslash = -1;
			}
			if (c == '\\')
				current_backslash = token_end;
			if (braces_level == 0)
				if (c == ',' || c == '~' || c=='-' || Character.isWhitespace(c))
					break;
			
			
			
			
			
			token_end++;
		}
		if (token_abbr < 0)
			token_abbr = token_end;
		if (token_end < orig.length() && orig.charAt(token_end) == '-')
			token_term = '-';
		if (orig.substring(token_start, token_end).equalsIgnoreCase("and"))
			return TOKEN_AND;
		else
			return TOKEN_WORD;
	}

	
	public int size() {
		return authors.size();
	}

	
	public Author getAuthor(int i) {
		return authors.get(i);
	}

	
	public String getAuthorsNatbib() {
		
		if (authorsNatbib != null)
			return authorsNatbib;

		StringBuffer res = new StringBuffer();
		if (size() > 0) {
			res.append(getAuthor(0).getLastOnly());
			if (size() == 2) {
				res.append(" and ");
				res.append(getAuthor(1).getLastOnly());
			} else if (size() > 2) {
				res.append(" et al.");
			}
		}
		authorsNatbib = res.toString();
		return authorsNatbib;
	}

	
	public String getAuthorsLastOnly(boolean oxfordComma) {
		int abbrInt = (oxfordComma ? 0 : 1);

		
		if (authorsLastOnly[abbrInt] != null)
			return authorsLastOnly[abbrInt];

		StringBuffer res = new StringBuffer();
		if (size() > 0) {
			res.append(getAuthor(0).getLastOnly());
			int i = 1;
			while (i < size() - 1) {
				res.append(", ");
				res.append(getAuthor(i).getLastOnly());
				i++;
			}
			if (size() > 2 && oxfordComma)
				res.append(",");
			if (size() > 1) {
				res.append(" and ");
				res.append(getAuthor(i).getLastOnly());
			}
		}
		authorsLastOnly[abbrInt] = res.toString();
		return authorsLastOnly[abbrInt];
	}

	
	public String getAuthorsLastFirst(boolean abbreviate, boolean oxfordComma) {
		int abbrInt = (abbreviate ? 0 : 1);
		abbrInt += (oxfordComma ? 0 : 2);

		
		if (authorsLastFirst[abbrInt] != null)
			return authorsLastFirst[abbrInt];

		StringBuffer res = new StringBuffer();
		if (size() > 0) {
			res.append(getAuthor(0).getLastFirst(abbreviate));
			int i = 1;
			while (i < size() - 1) {
				res.append(", ");
				res.append(getAuthor(i).getLastFirst(abbreviate));
				i++;
			}
			if (size() > 2 && oxfordComma)
				res.append(",");
			if (size() > 1) {
				res.append(" and ");
				res.append(getAuthor(i).getLastFirst(abbreviate));
			}
		}
		authorsLastFirst[abbrInt] = res.toString();
		return authorsLastFirst[abbrInt];
	}
	
	public String toString(){
		return getAuthorsLastFirstAnds(false);
	}

	
	public String getAuthorsLastFirstAnds(boolean abbreviate) {
		int abbrInt = (abbreviate ? 0 : 1);
		
		if (authorLastFirstAnds[abbrInt] != null)
			return authorLastFirstAnds[abbrInt];

		StringBuffer res = new StringBuffer();
		if (size() > 0) {
			res.append(getAuthor(0).getLastFirst(abbreviate));
			for (int i = 1; i < size(); i++) {
				res.append(" and ");
				res.append(getAuthor(i).getLastFirst(abbreviate));
			}
		}

		authorLastFirstAnds[abbrInt] = res.toString();
		return authorLastFirstAnds[abbrInt];
	}

	public String getAuthorsLastFirstFirstLastAnds(boolean abbreviate) {
		int abbrInt = (abbreviate ? 0 : 1);
		
		if (authorsLastFirstFirstLast[abbrInt] != null)
			return authorsLastFirstFirstLast[abbrInt];

		StringBuffer res = new StringBuffer();
		if (size() > 0) {
            res.append(getAuthor(0).getLastFirst(abbreviate));
			for (int i = 1; i < size(); i++) {
				res.append(" and ");
				res.append(getAuthor(i).getFirstLast(abbreviate));
			}
		}

		authorsLastFirstFirstLast[abbrInt] = res.toString();
		return authorsLastFirstFirstLast[abbrInt];
	}    

	
	public String getAuthorsFirstFirst(boolean abbr, boolean oxfordComma) {

		int abbrInt = (abbr ? 0 : 1);
		abbrInt += (oxfordComma ? 0 : 2);

		
		if (authorsFirstFirst[abbrInt] != null)
			return authorsFirstFirst[abbrInt];

		StringBuffer res = new StringBuffer();
		if (size() > 0) {
			res.append(getAuthor(0).getFirstLast(abbr));
			int i = 1;
			while (i < size() - 1) {
				res.append(", ");
				res.append(getAuthor(i).getFirstLast(abbr));
				i++;
			}
			if (size() > 2 && oxfordComma)
				res.append(",");
			if (size() > 1) {
				res.append(" and ");
				res.append(getAuthor(i).getFirstLast(abbr));
			}
		}
		authorsFirstFirst[abbrInt] = res.toString();
		return authorsFirstFirst[abbrInt];
	}
	
	
	public boolean equals(Object o) {
		if (!(o instanceof AuthorList)) {
			return false;
		}
		AuthorList a = (AuthorList) o;
		
		return this.authors.equals(a.authors);
	}
	
	
	public String getAuthorsFirstFirstAnds() {
		
		if (authorsFirstFirstAnds != null)
			return authorsFirstFirstAnds;

		StringBuffer res = new StringBuffer();
		if (size() > 0) {
			res.append(getAuthor(0).getFirstLast(false));
			for (int i = 1; i < size(); i++) {
				res.append(" and ");
				res.append(getAuthor(i).getFirstLast(false));
			}
		}
		authorsFirstFirstAnds = res.toString();
		return authorsFirstFirstAnds;
	}

	
	public String getAuthorsForAlphabetization() {
		if (authorsAlph != null)
			return authorsAlph;

		StringBuffer res = new StringBuffer();
		if (size() > 0) {
			res.append(getAuthor(0).getNameForAlphabetization());
			for (int i = 1; i < size(); i++) {
				res.append(" and ");
				res.append(getAuthor(i).getNameForAlphabetization());
			}
		}
		authorsAlph = res.toString();
		return authorsAlph;
	}

	
	public static class Author {
		
		private final String first_part;

		private final String first_abbr;

		private final String von_part;

		private final String last_part;

		private final String jr_part;

		
		public boolean equals(Object o) {
			if (!(o instanceof Author)) {
				return false;
			}
			Author a = (Author) o;
			return Util.equals(first_part, a.first_part)
					&& Util.equals(first_abbr, a.first_abbr)
					&& Util.equals(von_part, a.von_part)
					&& Util.equals(last_part, a.last_part)
					&& Util.equals(jr_part, a.jr_part);
		}
		
		
		public Author(String first, String firstabbr, String von, String last, String jr) {
			first_part = first;
			first_abbr = firstabbr;
			von_part = von;
			last_part = last;
			jr_part = jr;
		}

		
		public String getFirst() {
			return first_part;
		}

		
		public String getFirstAbbr() {
			return first_abbr;
		}

		
		public String getVon() {
			return von_part;
		}

		
		public String getLast() {
            return last_part;
		}

		
		public String getJr() {
			return jr_part;
		}

		
		public String getLastOnly() {
			if (von_part == null) {
				return (last_part == null ? "" : last_part);
			} else {
				return (last_part == null ? von_part : von_part + " " + last_part);
			}
		}

		
		public String getLastFirst(boolean abbr) {
			String res = getLastOnly();
			if (jr_part != null)
				res += ", " + jr_part;
			if (abbr) {
				if (first_abbr != null)
					res += ", " + first_abbr;
			} else {
				if (first_part != null)
					res += ", " + first_part;
			}
			return res;
		}

		
		public String getFirstLast(boolean abbr) {
			String res = getLastOnly();
			if (abbr) {
				res = (first_abbr == null ? "" : first_abbr + " ") + res;
			} else {
				res = (first_part == null ? "" : first_part + " ") + res;
			}
			if (jr_part != null)
				res += ", " + jr_part;
			return res;
		}

		
		public String getNameForAlphabetization() {
			StringBuffer res = new StringBuffer();
			if (last_part != null)
				res.append(last_part);
			if (jr_part != null) {
				res.append(", ");
				res.append(jr_part);
			}
			if (first_abbr != null) {
				res.append(", ");
				res.append(first_abbr);
			}
			while ((res.length() > 0) && (res.charAt(0) == '{'))
				res.deleteCharAt(0);
			return res.toString();
		}
	}


    public static void main(String[] args) {
        
        String s = "Olaf von Nilsen, Jr.";
        AuthorList al = AuthorList.getAuthorList(s);
        for (int i=0; i<al.size(); i++) {
            Author a = al.getAuthor(i);
            System.out.println((i+1)+": first = '"+a.getFirst()+"'");
            System.out.println((i+1)+": last = '"+a.getLast()+"'");
            System.out.println((i+1)+": jr = '"+a.getJr()+"'");
            System.out.println((i+1)+": von = '"+a.getVon()+"'");
        }

        System.out.println((new CreateDocBookAuthors()).format(s));
    }
}
