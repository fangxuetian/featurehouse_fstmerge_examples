
package net.sourceforge.pmd.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class StringUtil {

	public static final String[] EMPTY_STRINGS = new String[0];
    private static final boolean SUPPORTS_UTF8 = System.getProperty("net.sourceforge.pmd.supportUTF8", "no").equals("yes");
    private static final String[] ENTITIES;

    static {
        ENTITIES = new String[256 - 126];
        for (int i = 126; i <= 255; i++) {
            ENTITIES[i - 126] = "&#" + i + ';';
        }
    }

    private StringUtil() {}
    
    
    public static boolean isEmpty(String value) {
    	
    	if (value == null || "".equals(value)) {
    		return true;
    	}
    	
    	for (int i=0; i<value.length(); i++) {
    		if (!Character.isWhitespace(value.charAt(i))) {
    		    return false;
    		}
    	}
    	
    	return true;
    }
    
    public static boolean isNotEmpty(String value) {
    	return !isEmpty(value);
    }
    
	public static boolean areSemanticEquals(String a, String b) {
		
		if (a==null) { return isEmpty(b); }
		if (b==null) { return isEmpty(a); }
		
		return a.equals(b);
	}
    
    public static String replaceString(final String original, char oldChar, final String newString) {
	int index = original.indexOf(oldChar);
	if (index < 0) {
	    return original;
	} else {
	    final String replace = newString == null ? "" : newString;
	    final StringBuilder buf = new StringBuilder(Math.max(16, original.length() + replace.length()));
	    int last = 0;
	    while (index != -1) {
		buf.append(original.substring(last, index));
		buf.append(replace);
		last = index + 1;
		index = original.indexOf(oldChar, last);
	    }
	    buf.append(original.substring(last));
	    return buf.toString();
	}
    }

    public static String replaceString(final String original, final String oldString, final String newString) {
	int index = original.indexOf(oldString);
	if (index < 0) {
	    return original;
	} else {
	    final String replace = newString == null ? "" : newString;
	    final StringBuilder buf = new StringBuilder(Math.max(16, original.length() + replace.length()));
	    int last = 0;
	    while (index != -1) {
		buf.append(original.substring(last, index));
		buf.append(replace);
		last = index + oldString.length();
		index = original.indexOf(oldString, last);
	    }
	    buf.append(original.substring(last));
	    return buf.toString();
	}
    }

    
    public static void appendXmlEscaped(StringBuffer buf, String src) {
        appendXmlEscaped(buf, src, SUPPORTS_UTF8);
    }

    public static String htmlEncode(String string) {
        String encoded = replaceString(string, '&', "&amp;");
        encoded = replaceString(encoded, '<', "&lt;");
        return replaceString(encoded, '>', "&gt;");
    }
    
    
    
    private static void appendXmlEscaped(StringBuffer buf, String src, boolean supportUTF8) {
        char c;
        for (int i = 0; i < src.length(); i++) {
            c = src.charAt(i);
            if (c > '~') {
                if (!supportUTF8) {
                    if (c <= 255) {
                        buf.append(ENTITIES[c - 126]);
                    } else {
                        buf.append("&u").append(Integer.toHexString(c)).append(';');
                    }
                } else {
                    buf.append(c);
                }
            } else if (c == '&') {
                buf.append("&amp;");
            } else if (c == '"') {
                buf.append("&quot;");
            } else if (c == '<') {
                buf.append("&lt;");
            } else if (c == '>') {
                buf.append("&gt;");
            } else {
                buf.append(c);
            }
        }
    }

	
	public static String[] substringsOf(String source, char delimiter) {

		if (source == null || source.length() == 0) {
            return EMPTY_STRINGS;
        }
		
		int delimiterCount = 0;
		int length = source.length();
		char[] chars = source.toCharArray();

		for (int i=0; i<length; i++) {
			if (chars[i] == delimiter) {
			    delimiterCount++;
			}
			}

		if (delimiterCount == 0) {
		    return new String[] { source };
		}

		String results[] = new String[delimiterCount+1];

		int i = 0;
		int offset = 0;

		while (offset <= length) {
			int pos = source.indexOf(delimiter, offset);
			if (pos < 0) {
			    pos = length;
			}
			results[i++] = pos == offset ? "" : source.substring(offset, pos);
			offset = pos + 1;
			}

		return results;
	}
	
	
	  public static String[] substringsOf(String str, String separator) {
		  
	        if (str == null || str.length() == 0) {
	            return EMPTY_STRINGS;
	        }

	        int index = str.indexOf(separator);
	        if (index == -1) {
	            return new String[]{str};
	        }

	        List<String> list = new ArrayList<String>();
	        int currPos = 0;
	        int len = separator.length();
	        while (index != -1) {
	            list.add(str.substring(currPos, index));
	            currPos = index + len;
	            index = str.indexOf(separator, currPos);
	        }
	        list.add(str.substring(currPos));
	        return list.toArray(new String[list.size()]);
	    }
	
	
	
	public static void asStringOn(StringBuffer sb, Iterator<?> iter, String separator) {
		
	    if (!iter.hasNext()) { return;  }
	    
	    sb.append(iter.next());
	    
	    while (iter.hasNext()) {
	    	sb.append(separator);
	        sb.append(iter.next());
	    }
	}
	
	public static int lengthOfShortestIn(String[] strings) {
		
	    if (CollectionUtil.isEmpty(strings)) { return 0; }
	    
		int minLength = Integer.MAX_VALUE;
		
		for (int i=0; i<strings.length; i++) {
			if (strings[i] == null) {
			    return 0;
			}
			minLength = Math.min(minLength, strings[i].length());
		}
		
		return minLength;
	}
	
	
	public static int maxCommonLeadingWhitespaceForAll(String[] strings) {
		
		int shortest = lengthOfShortestIn(strings);
		if (shortest == 0) {
		    return 0;
		}
		
		char[] matches = new char[shortest];
		
		String str;
		for (int m=0; m<matches.length; m++) {
			matches[m] = strings[0].charAt(m);
			if (!Character.isWhitespace(matches[m])) {
			    return m;
			}
			for (int i=0; i<strings.length; i++) {
				str = strings[i];
				if (str.charAt(m) != matches[m]) {
				    return m; 
				}
				}
		}
		
		return shortest;
	}
	
	
	public static String[] trimStartOn(String[] strings, int trimDepth) {
		
		if (trimDepth == 0) {
		    return strings;
		}
		
		String[] results = new String[strings.length];
		for (int i=0; i<strings.length; i++) {
			results[i] = strings[i].substring(trimDepth);
		}
		return results;
   }
	
    
    public static String lpad(String s, int length) {
         String res = s;
         if (length - s.length() > 0) {
             char [] arr = new char[length - s.length()];
             java.util.Arrays.fill(arr, ' ');
             res = new StringBuffer(length).append(arr).append(s).toString();
         }
         return res;
    }
    
    
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    public static boolean isSame(String s1, String s2, boolean trim, boolean ignoreCase, boolean standardizeWhitespace) {
		if (s1 == s2) {
			return true;
		} else if (s1 == null || s2 == null) {
			return false;
		} else {
			if (trim) {
				s1 = s1.trim();
				s2 = s2.trim();
			}
			if (standardizeWhitespace) {
				
				s1 = s1.replaceAll("\\s+", " ");
				s2 = s2.replaceAll("\\s+", " ");
			}
			return ignoreCase ? s1.equalsIgnoreCase(s2) : s1.equals(s2);
		}
    }
    
	
	public static String asString(Object[] items, String separator) {
		
		if (items == null || items.length == 0) { return ""; }
		if (items.length == 1) { return items[0].toString(); }
		
		StringBuilder sb = new StringBuilder(items[0].toString());
		for (int i=1; i<items.length; i++) {
			sb.append(separator).append(items[i]);
		}
		
		return sb.toString();
	}
}
