// $Id: BibtexTextPrefix.java,v 1.1 2010-01-15 13:12:46 apel Exp $
package net.sf.jabref.bst;


/**
 * The |built_in| function {\.{text.prefix\$}} pops the top two literals (the
 * integer literal |pop_lit1| and a string literal, in that order). It pushes
 * the substring of the (at most) |pop_lit1| consecutive text characters
 * starting from the beginning of the string. This function is similar to
 * {\.{substring\$}}, but this one considers an accented character (or more
 * precisely, a ``special character''$\!$, even if it's missing its matching
 * |right_brace|) to be a single text character (rather than however many
 * |ASCII_code| characters it actually comprises), and this function doesn't
 * consider braces to be text characters; furthermore, this function appends any
 * needed matching |right_brace|s. If any of the types is incorrect, it
 * complains and pushes the null string.
 * 
 * @author $Author: apel $
 * @version $Revision: 1.1 $ ($Date: 2010-01-15 13:12:46 $)
 * 
 */
public class BibtexTextPrefix {

	/**
	 * 
	 * @param numOfChars
	 * @param toPrefix
	 * @param warn may-be-null
	 * @return
	 */
	public static String textPrefix(int numOfChars, String toPrefix, Warn warn) {

		StringBuffer sb = new StringBuffer();

		char[] cs = toPrefix.toCharArray();
		int n = cs.length;
		int i = 0;

		int braceLevel = 0;

		while (i < n && numOfChars > 0) {
			char c = cs[i];
			i++;
			if (c == '{') {
				braceLevel++;
				if (braceLevel == 1 && i < n && (cs[i] == '\\')) {
					i++; // skip backslash
					while (i < n && braceLevel > 0) {
						if (cs[i] == '}') {
							braceLevel--;
						} else if (cs[i] == '{') {
							braceLevel++;
						}
						i++;
					}
					numOfChars--;
				}
			} else if (c == '}') {
				if (braceLevel > 0) {
					braceLevel--;
				} else {
					if (warn != null)
						warn.warn("Unbalanced brace in string for purify$: " + toPrefix);
				}
			} else {
				numOfChars--;
			}
			
		}
		sb.append(toPrefix.substring(0, i));
		while (braceLevel > 0){
			sb.append('}');
			braceLevel--;
		}

		return sb.toString();
	}
}
