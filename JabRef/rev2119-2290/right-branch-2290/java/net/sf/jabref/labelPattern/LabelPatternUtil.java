
package net.sf.jabref.labelPattern;

import java.util.ArrayList;
import java.util.StringTokenizer;

import net.sf.jabref.*;
import net.sf.jabref.export.layout.format.RemoveLatexCommands;



public class LabelPatternUtil {

  
  private static String CHARS = "abcdefghijklmnopqrstuvwxyz";

  public static ArrayList DEFAULT_LABELPATTERN;
  static {
      updateDefaultPattern();
  }
  

  private static BibtexDatabase _db;

  public static void updateDefaultPattern() {
      DEFAULT_LABELPATTERN = split(Globals.prefs.get("defaultLabelPattern"));
  }

  
  public static ArrayList split(String labelPattern) {
    
    ArrayList _alist = new ArrayList();

    
    _alist.add(labelPattern);

    
    StringTokenizer tok = new StringTokenizer(labelPattern, "[]", true);
    while (tok.hasMoreTokens()) {
      _alist.add(tok.nextToken());

    }
    return _alist;

    
  }

  
  public static BibtexEntry makeLabel(LabelPattern table,
                                      BibtexDatabase database,
                                      BibtexEntry _entry) {
    _db = database;
    ArrayList _al;
    String _spacer, _label;
    StringBuffer _sb = new StringBuffer();
    boolean forceUpper = false, forceLower = false;

    try {
      
      String _type = _entry.getType().getName().toLowerCase();
      
      _al = table.getValue(_type);
      int _alSize = _al.size();
      boolean field = false;
      for (int i = 1; i < _alSize; i++) {
        String val = _al.get(i).toString();
        if (val.equals("[")) {
          field = true;
        }
        else if (val.equals("]")) {
          field = false;
        }
        else if (field) {
            
            
            
        String[] parts = val.split(":");
        val = parts[0];
        
            
                
                
            
            StringBuffer _sbvalue = new StringBuffer();

            try {

               if (val.startsWith("auth") || val.startsWith("pureauth")) {

                  
                  
                  
                  
                  
                  

                  String authString;
                  if(val.startsWith("pure")) {
                    
                    val = val.substring(4);
                    System.out.println("val is now "+val);
                    authString = _entry.getField("author").toString(); 
                    System.out.println("Got authString " + authString);
                  } else {
                    if (_entry.getField("author") == null || _entry.getField("author").toString().equals("")) {
                      authString = _entry.getField("editor").toString();
                    } else {
                      authString = _entry.getField("author").toString();
                    }
                  }

                  
                  if (val.equals("auth")) {
                    _sbvalue.append(firstAuthor(authString));
                  }
                  else if (val.equals("authors")) {
                    _sbvalue.append(allAuthors(authString));
                  }
                  else if (val.equals("authorIni")) {
                    _sbvalue.append(oneAuthorPlusIni(authString));
                  }
                  else if (val.matches("authIni[\\d]+")) {
                    int num = Integer.parseInt(val.substring(7));
                                        _sbvalue.append(authIniN(authString,num));
                  }
                  else if (val.equals("auth.auth.ea")) {
                    _sbvalue.append(authAuthEa(authString));
                  }
                  else if (val.equals("auth.etal")) {
                    _sbvalue.append(authEtal(authString));
                  }

                  else if (val.equals("authshort")) {
                    _sbvalue.append(authshort(authString));
                  }
                  else if (val.matches("auth[\\d]+_[\\d]+")) {
                    String[] nums = val.substring(4).split("_");
                    _sbvalue.append(authN_M(authString,
                                            Integer.parseInt(nums[0]),
                                            Integer.parseInt(nums[1]) - 1));
                  }
                  
                  else if (val.matches("auth\\d+")) {
                    int num = Integer.parseInt(val.substring(4));
                    String fa = firstAuthor(authString);
                    if ( num > fa.length() )
                      num = fa.length();
                    _sbvalue.append(fa.substring(0,num));
                  }
                  else if (val.matches("authors\\d+")) {
                    _sbvalue.append(NAuthors(authString,Integer.parseInt(val.substring(7))));
                  }

                  else {
                    
                    _sbvalue.append(_entry.getField(val).toString());
                  }
                }
                else if (val.startsWith("ed")) {
                  
                  if (val.equals("edtr")) {
                    _sbvalue.append(firstAuthor(_entry.getField("editor").toString()));
                  }
                  else if (val.equals("editors")) {
                    _sbvalue.append(allAuthors(_entry.getField("editor").toString()));
                  }
                  else if (val.equals("editorIni")) {
                      _sbvalue.append(oneAuthorPlusIni(_entry.getField("editor").toString()));
                  }
                  else if (val.matches("edtrIni[\\d]+")) {
                    int num = Integer.parseInt(val.substring(7));
                                        _sbvalue.append(authIniN(_entry.getField("editor").toString(),num));
                  }
                  else if (val.matches("edtr[\\d]+_[\\d]+")) {
                    String[] nums = val.substring(4).split("_");
                    _sbvalue.append(authN_M(_entry.getField("editor").toString(), Integer.parseInt(nums[0]),
                                            Integer.parseInt(nums[1])-1));
                  }
                  else if (val.equals("edtr.edtr.ea")) {
                    _sbvalue.append(authAuthEa(_entry.getField("editor").toString()));
                  }
                  else if (val.equals("edtrshort")) {
                    _sbvalue.append(authshort(_entry.getField("editor").toString()));
                  }
                  
                  else if (val.matches("edtr\\d+")) {
                    int num = Integer.parseInt(val.substring(4));
                    String fa = firstAuthor(_entry.getField("editor").toString());
                    if ( num > fa.length() )
                      num = fa.length();
                    _sbvalue.append(fa.substring(0,num));
                  }
                  else {
                    
                    _sbvalue.append(_entry.getField(val).toString());
                  }
                }
                else if (val.equals("firstpage")) {
                  _sbvalue.append(firstPage(_entry.getField("pages").toString()));
                }
                else if (val.equals("lastpage")) {
                  _sbvalue.append(lastPage(_entry.getField("pages").toString()));
                }
                else if (val.equals("shorttitle")) {
                  _sbvalue.append(getTitleWords(3, _entry));
                }
                else if (val.equals("shortyear")) {
                  String ss = _entry.getField("year").toString();
                  if (ss.startsWith("in") || ss.startsWith("sub")) {
                    _sbvalue.append("IP");
                  }
                  else if (ss.length() > 2) {
                    _sbvalue.append(ss.substring(ss.length() - 2));
                  }
                  else {
                    _sbvalue.append(ss);
                  }
                }

                else if(val.equals("veryshorttitle")) {
                  _sbvalue.append(getTitleWords(1, _entry));
                }

               else if (val.matches("keyword\\d+")) {
                    int num = Integer.parseInt(val.substring(7));
                    String kw = _entry.getField("keywords").toString();
                    if (kw != null) {
                        String[] keywords = kw.split("[,;]\\s*");
                        if ((num > 0) && (num < keywords.length))
                            _sbvalue.append(keywords[num-1].trim());
                    }
               }

                
                else {
                  _sbvalue.append(_entry.getField(val).toString());
                }
            }
            catch (NullPointerException ex) {
                    
            }
            
        if (parts.length > 1) for (int j=1; j<parts.length; j++) {
            String modifier = parts[j];

            if(modifier.equals("lower")) {
                String tmp = _sbvalue.toString().toLowerCase();
                _sbvalue = new StringBuffer(tmp);
                    }
            else if (modifier.equals("abbr")) {
                
                
                StringBuffer abbr = new StringBuffer();
                String[] words = _sbvalue.toString().replaceAll("[\\{\\}]","")
                        .split("[ \r\n]");
                for (int word=0; word<words.length; word++)
                    if (words[word].length() > 0)
                        abbr.append(words[word].charAt(0));
                _sbvalue = abbr;
            }
            else {
                        Globals.logger("Key generator warning: unknown modifier '"+modifier+"'.");
                    }
            }

        _sb.append(_sbvalue);


        }
        else {
          _sb.append(val);
        }
      }
    }

    catch (Exception e) {
      System.err.println(e);
    }

    

    
    _label = Util.checkLegalKey(_sb.toString());

    
    
    String regex = Globals.prefs.get("KeyPatternRegex");
    if ((regex != null) && (regex.trim().length() > 0)) {
        String replacement = Globals.prefs.get("KeyPatternReplacement");
        _label = _label.replaceAll(regex, replacement);
    }

    if (forceUpper) {
      _label = _label.toUpperCase();
    }
    if (forceLower) {
      _label = _label.toLowerCase();
    }


    String oldKey = _entry.getCiteKey();
    int occurences = _db.getNumberOfKeyOccurences(_label);
    if ((oldKey != null) && oldKey.equals(_label))
        occurences--; 

    
    

    if (occurences == 0) {
        
        if (!_label.equals(oldKey))
            _db.setCiteKeyForEntry(_entry.getId(), _label);
        
    }
    else {

        
        int number = 0;

        String moddedKey = _label+getAddition(number);
        occurences = _db.getNumberOfKeyOccurences(moddedKey);
        if ((oldKey != null) && oldKey.equals(moddedKey))
            occurences--;
        while (occurences > 0) {
            number++;
            moddedKey = _label+getAddition(number);

            occurences = _db.getNumberOfKeyOccurences(moddedKey);
            if ((oldKey != null) && oldKey.equals(moddedKey))
                occurences--;
        }

        

        if (!moddedKey.equals(oldKey))  {
            _db.setCiteKeyForEntry(_entry.getId(), moddedKey);
        }
    }
    
    return _entry;
    

  }

    
    private static String getAddition(int number) {
        String s = "";
        if (number >= CHARS.length()) {
            int lastChar = number % CHARS.length();
            return getAddition(number/CHARS.length()-1) + CHARS.substring(lastChar, lastChar+1);
        } else
            return CHARS.substring(number, number+1);
    }


    static String getTitleWords(int number, BibtexEntry _entry) {
    String ss = (new RemoveLatexCommands()).format(_entry.getField("title").toString());
    StringBuffer _sbvalue = new StringBuffer(),
        current;
    int piv=0, words = 0;

    
    
    mainl: while ((piv < ss.length()) && (words < number)) {
      current = new StringBuffer();
      
      while ((piv<ss.length()) && !Character.isWhitespace(ss.charAt(piv))) {
        current.append(ss.charAt(piv));
        piv++;
        
      }
      piv++;
      
      String word = current.toString().trim();
      if (word.length() == 0)
        continue mainl;
      for(int _i=0; _i< Globals.SKIP_WORDS.length; _i++) {
        if (word.equalsIgnoreCase(Globals.SKIP_WORDS[_i])) {
          continue mainl;
        }
      }

      
      if (_sbvalue.length() > 0)
        _sbvalue.append(" ");
      _sbvalue.append(word);
      words++;
    }

    return _sbvalue.toString();
  }


  
  public static boolean isLabelUnique(String label) {
    boolean _isUnique = true;
    BibtexEntry _entry;
    int _dbSize = _db.getEntryCount();
    
    
    
    
    

    for (int i = 0; i < _dbSize; i++) {
      _entry = _db.getEntryById(String.valueOf(i));

      
      
      if (_entry.getField(BibtexFields.KEY_FIELD).equals(label)) {
        _isUnique = false;
        break;
      }
    }

    return _isUnique;

  }

  
  private static String firstAuthor(String authorField) {
    String author = "";
    
    
      String[] tokens = AuthorList.fixAuthorForAlphabetization(authorField).split("\\band\\b");
    if (tokens.length > 0) { 
      String[] firstAuthor = tokens[0].replaceAll("\\s+", " ").split(" ");
      author += firstAuthor[0];

    }
    return author;
  }

  
  private static String allAuthors(String authorField) {
    String author = "";
    
    String[] tokens = AuthorList.fixAuthorForAlphabetization(authorField).split("\\band\\b");
    int i = 0;
    while (tokens.length > i) {
      
      String[] firstAuthor = tokens[i].replaceAll("\\s+", " ").trim().split(" ");
      
      author += firstAuthor[0];
      i++;
    }
    return author;
  }

  
  private static String NAuthors(String authorField, int n) {
            String author = "";
            
            String[] tokens = AuthorList.fixAuthorForAlphabetization(authorField).split("\\band\\b");
            int i = 0;
            while (tokens.length > i && i < n) {
              
              String[] firstAuthor = tokens[i].replaceAll("\\s+", " ").trim().split(" ");
              
              author += firstAuthor[0];
              i++;
            }
            if (tokens.length <= n) return author;
            return author += "EtAl";
  }

  
  private static String oneAuthorPlusIni(String authorField) {
    final int CHARS_OF_FIRST = 5;
    authorField = AuthorList.fixAuthorForAlphabetization(authorField);
    String author = "";
    
    String[] tokens = authorField.split("\\band\\b");
    int i = 1;
    if (tokens.length == 0) {
      return author;
    }
    String[] firstAuthor = tokens[0].replaceAll("\\s+", " ").split(" ");
    author = firstAuthor[0].substring(0,
                                      (int) Math.min(CHARS_OF_FIRST,
        firstAuthor[0].length()));
    while (tokens.length > i) {
      
      author += tokens[i].trim().charAt(0);
      i++;
    }
    return author;

  }

  
  private static String authAuthEa(String authorField) {
    authorField = AuthorList.fixAuthorForAlphabetization(authorField);
    StringBuffer author = new StringBuffer();

    String[] tokens = authorField.split("\\band\\b");
    if (tokens.length == 0) {
      return "";
    }
    author.append((tokens[0].split(","))[0]);
    if (tokens.length >= 2)
        author.append(".").append((tokens[1].split(","))[0]);
    if (tokens.length > 2)
      author.append(".ea");

    return author.toString();
  }

  
  private static String authEtal(String authorField) {
    authorField = AuthorList.fixAuthorForAlphabetization(authorField);
    StringBuffer author = new StringBuffer();

    String[] tokens = authorField.split("\\band\\b");
    if (tokens.length == 0) {
      return "";
    }
    author.append((tokens[0].split(","))[0]);
    if (tokens.length == 2)
        author.append(".").append((tokens[1].split(","))[0]);
    else if (tokens.length > 2)
      author.append(".etal");

    return author.toString();
  }

  
  private static String authN_M(String authorField, int n, int m) {
    authorField = AuthorList.fixAuthorForAlphabetization(authorField);
    StringBuffer author = new StringBuffer();

    String[] tokens = authorField.split("\\band\\b");
    if ((tokens.length <= m) || (n<0) || (m<0)) {
      return "";
    }
    String lastName = (tokens[m].split(","))[0].trim();
    
    if (lastName.length() <= n)
      return lastName;
    else
      return lastName.substring(0, n);
  }

  
  private static String authshort(String authorField) {
    authorField = AuthorList.fixAuthorForAlphabetization(authorField);
    StringBuffer author = new StringBuffer();
    String[] tokens = authorField.split("\\band\\b");
    int i = 0;

    if (tokens.length == 1) {

      author.append(authN_M(authorField,authorField.length(),0));

    } else if (tokens.length >= 2) {

      while (tokens.length > i && i<3) {
        author.append(authN_M(authorField,1,i));
        i++;
      }

      if (tokens.length > 3)
        author.append("+");

    }

    return author.toString();
  }

  
  private static String authIniN(String authorField, int n) {
    authorField = AuthorList.fixAuthorForAlphabetization(authorField);
    StringBuffer author = new StringBuffer();
    String[] tokens = authorField.split("\\band\\b");
    int i = 0;
    int charsAll = n / tokens.length;

    if (tokens.length == 0) {
      return author.toString();
    }

    while (tokens.length > i) {
      if ( i < (n % tokens.length) ) {
        author.append(authN_M(authorField,charsAll+1,i));
      } else {
        author.append(authN_M(authorField,charsAll,i));
      }
      i++;
    }

    if (author.length() <= n)
      return author.toString();
    else
      return author.toString().substring(0, n);
  }


  
  private static String firstPage(String pages) {
    String[] _pages = pages.split("-");
    return _pages[0];
  }

  
  private static String lastPage(String pages) {
    String[] _pages = pages.split("-");
    return _pages[1];
  }

}
