









package net.sf.jabref;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.undo.UndoableEdit;

import net.sf.jabref.export.layout.LayoutEntry;
import net.sf.jabref.export.layout.LayoutFormatter;
import net.sf.jabref.external.ExternalFileType;
import net.sf.jabref.external.ExternalFileTypeEntryEditor;
import net.sf.jabref.external.UnknownExternalFileType;
import net.sf.jabref.groups.AbstractGroup;
import net.sf.jabref.groups.KeywordGroup;
import net.sf.jabref.gui.AutoCompleter;
import net.sf.jabref.gui.FileListEntry;
import net.sf.jabref.gui.FileListEntryEditor;
import net.sf.jabref.gui.FileListTableModel;
import net.sf.jabref.imports.CiteSeerFetcher;
import net.sf.jabref.undo.NamedCompound;
import net.sf.jabref.undo.UndoableFieldChange;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;


public class Util {

	
	private static SimpleDateFormat dateFormatter = null;

	
	public static Color fieldsCol = new Color(180, 180, 200);

	
	final static int TYPE_MISMATCH = -1, NOT_EQUAL = 0, EQUAL = 1, EMPTY_IN_ONE = 2,
		EMPTY_IN_TWO = 3;

	final static NumberFormat idFormat;

	static {
		idFormat = NumberFormat.getInstance();
		idFormat.setMinimumIntegerDigits(8);
		idFormat.setGroupingUsed(false);
	}

	public static void bool(boolean b) {
		if (b)
			System.out.println("true");
		else
			System.out.println("false");
	}

	public static void pr(String s) {
		System.out.println(s);
	}

	public static void pr_(String s) {
		System.out.print(s);
	}

	public static String nCase(String s) {
		
		
		if (s.length() > 1)
			return s.substring(0, 1).toUpperCase() + s.substring(1, s.length()).toLowerCase();
		else
			return s.toUpperCase();

	}

	public static String checkName(String s) {
		
		if (s.length() < 4 || !s.substring(s.length() - 4).equalsIgnoreCase(".bib")) {
			return s + ".bib";
		}
		return s;
	}

	private static int idCounter = 0;

	public synchronized static String createNeutralId() {
		return idFormat.format(idCounter++);
	}

	
	public static void placeDialog(java.awt.Dialog diag, java.awt.Container win) {
		Dimension ds = diag.getSize(), df = win.getSize();
		Point pf = win.getLocation();
		diag.setLocation(new Point(Math.max(0, pf.x + (df.width - ds.width) / 2), Math.max(0, pf.y
			+ (df.height - ds.height) / 2)));

	}

	
	public static String parseField(String content) {
		
		if (content.length() == 0)
			return content;
		
		String[] strings = content.split("#");
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < strings.length; i++){
			String s = strings[i].trim();
			if (s.length() > 0){
				char c = s.charAt(0);
				
				if (c == '{' || c == '"'){
					result.append(shaveString(strings[i]));	
				} else {
					
					
					String s2 = shaveString(s);
					try {
						Integer.parseInt(s2);
						
						result.append(s2);
					} catch (NumberFormatException ex) {
						
						result.append("#").append(s2).append("#");
					}
				}
			}
		}
		return result.toString();
	}

	
	public static String getPublicationDate(BibtexEntry entry) {

		Object o = entry.getField("year");
		if (o == null)
			return null;

		String year = toFourDigitYear(o.toString());

		o = entry.getField("month");
		if (o != null) {
			int month = Util.getMonthNumber(o.toString());
			if (month != -1) {
				return year + "-" + (month + 1 < 10 ? "0" : "") + (month + 1);
			}
		}
		return year;
	}

	public static String shaveString(String s) {
		
		
		
		if (s == null)
			return null;
		char ch, ch2;
		int beg = 0, end = s.length();
		
		boolean begok = false, endok = false;
		while (!begok) {
			if (beg < s.length()) {
				ch = s.charAt(beg);
				if (Character.isWhitespace(ch))
					beg++;
				else
					begok = true;
			} else
				begok = true;

		}
		while (!endok) {
			if (end > beg + 1) {
				ch = s.charAt(end - 1);
				if (Character.isWhitespace(ch))
					end--;
				else
					endok = true;
			} else
				endok = true;
		}

		if (end > beg + 1) {
			ch = s.charAt(beg);
			ch2 = s.charAt(end - 1);
			if (((ch == '{') && (ch2 == '}')) || ((ch == '"') && (ch2 == '"'))) {
				beg++;
				end--;
			}
		}
		s = s.substring(beg, end);
		return s;
	}

	
	public static String checkLegalKey(String key) {
		if (key == null)
			return null;
		StringBuffer newKey = new StringBuffer();
		for (int i = 0; i < key.length(); i++) {
			char c = key.charAt(i);
			if (!Character.isWhitespace(c) && (c != '#') && (c != '{') && (c != '\\') && (c != '"')
				&& (c != '}') && (c != '~') && (c != ',') && (c != '^'))
				newKey.append(c);
		}

		
		
		String newKeyS = replaceSpecialCharacters(newKey.toString());

		return newKeyS;
	}

	
	public static String replaceSpecialCharacters(String s) {
		for (Iterator i = Globals.UNICODE_CHARS.keySet().iterator(); i.hasNext();) {
			String chr = (String) i.next(), replacer = (String) Globals.UNICODE_CHARS.get(chr);
			
			s = s.replaceAll(chr, replacer);
		}
		return s;
	}

	static public String _wrap2(String in, int wrapAmount) {
		
		
		
		
		
		StringBuffer out = new StringBuffer(in.replaceAll("[ \\t\\r]+", " "));

		int p = in.length() - wrapAmount;
		int lastInserted = -1;
		while (p > 0) {
			p = out.lastIndexOf(" ", p);
			if (p <= 0 || p <= 20)
				break;
			int lbreak = out.indexOf("\n", p);
			System.out.println(lbreak + " " + lastInserted);
			if ((lbreak > p) && ((lastInserted >= 0) && (lbreak < lastInserted))) {
				p = lbreak - wrapAmount;
			} else {
				out.insert(p, "\n\t");
				lastInserted = p;
				p -= wrapAmount;
			}
		}
		return out.toString();
	}

	static public String wrap2(String in, int wrapAmount) {
		return net.sf.jabref.imports.FieldContentParser.wrap(in, wrapAmount);
	}

	static public String __wrap2(String in, int wrapAmount) {
		
		
		
		StringBuffer out = new StringBuffer(in.replaceAll("[ \\t\\r]+", " "));

		int p = 0;
		
		while (p < out.length()) {
			int q = out.indexOf(" ", p + wrapAmount);
			if ((q < 0) || (q >= out.length()))
				break;
			int lbreak = out.indexOf("\n", p);
			
			if ((lbreak > p) && (lbreak < q)) {
				p = lbreak + 1;
				int piv = lbreak + 1;
				if ((out.length() > piv) && !(out.charAt(piv) == '\t'))
					out.insert(piv, "\n\t");

			} else {
				
				out.deleteCharAt(q);
				out.insert(q, "\n\t");
				p = q + 1;
			}
		}
		return out.toString();
	}

	public static HashSet findDeliminatedWordsInField(BibtexDatabase db, String field,
		String deliminator) {
		HashSet res = new HashSet();
		Iterator i = db.getKeySet().iterator();
		while (i.hasNext()) {
			BibtexEntry be = db.getEntryById(i.next().toString());
			Object o = be.getField(field);
			if (o != null) {
				String fieldValue = o.toString().trim();
				StringTokenizer tok = new StringTokenizer(fieldValue, deliminator);
				while (tok.hasMoreTokens())
					res.add(tok.nextToken().trim());
			}
		}
		return res;
	}

	
	public static HashSet findAllWordsInField(BibtexDatabase db, String field, String remove) {
		HashSet res = new HashSet();
		StringTokenizer tok;
		Iterator i = db.getKeySet().iterator();
		while (i.hasNext()) {
			BibtexEntry be = db.getEntryById(i.next().toString());
			Object o = be.getField(field);
			if (o != null) {
				tok = new StringTokenizer(o.toString(), remove, false);
				while (tok.hasMoreTokens())
					res.add(tok.nextToken());
			}
		}
		return res;
	}

	
	public static String stringArrayToDelimited(String[] strs, String delimiter) {
		if ((strs == null) || (strs.length == 0))
			return "";
		if (strs.length == 1)
			return strs[0];
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strs.length - 1; i++) {
			sb.append(strs[i]);
			sb.append(delimiter);
		}
		sb.append(strs[strs.length - 1]);
		return sb.toString();
	}

	
	public static String[] delimToStringArray(String names, String delimiter) {
		if (names == null)
			return null;
		return names.split(delimiter);
	}

	
	public static void openExternalViewer(MetaData metaData, String link, String fieldName)
		throws IOException {

		if (fieldName.equals("ps") || fieldName.equals("pdf")) {

            
			String dir = metaData.getFileDirectory(fieldName);

			File file = expandFilename(link, new String[] { dir, "." });

			
			if ((file == null) || !file.exists()) {
				throw new IOException(Globals.lang("File not found") + " (" + fieldName + "): '"
					+ link + "'.");
			}
			link = file.getCanonicalPath();

			
			String[] split = file.getName().split("\\.");
			if (split.length >= 2) {
				if (split[split.length - 1].equalsIgnoreCase("pdf"))
					fieldName = "pdf";
				else if (split[split.length - 1].equalsIgnoreCase("ps")
					|| (split.length >= 3 && split[split.length - 2].equalsIgnoreCase("ps")))
					fieldName = "ps";
			}

            
            ExternalFileType type = Globals.prefs.getExternalFileTypeByExt(fieldName);

        } else if (fieldName.equals("doi")) {
			fieldName = "url";
			
			if (!link.startsWith("http://")) {
				link = Globals.DOI_LOOKUP_PREFIX + link;
			}
		} else if (fieldName.equals("citeseerurl")) {
			fieldName = "url";

			String canonicalLink = CiteSeerFetcher.generateCanonicalURL(link);
			if (canonicalLink != null)
				link = canonicalLink;
		}

		String cmdArray[] = new String[2];
		if (fieldName.equals("url")) { 
			try {

				
				
				if (link.startsWith("\\u{") && link.endsWith("}"))
					link = link.substring(5, link.length() - 1);

				link = sanitizeUrl(link);

				if (Globals.ON_MAC) {
					String[] cmd = { "/usr/bin/open", "-a", Globals.prefs.get("htmlviewer"), link };
					Process child = Runtime.getRuntime().exec(cmd);
				} else if (Globals.ON_WIN) {
					openFileOnWindows(link, false);
					
				} else {
					cmdArray[0] = Globals.prefs.get("htmlviewer");
					cmdArray[1] = link;
					Process child = Runtime.getRuntime().exec(cmdArray);
				}

			} catch (IOException e) {
				System.err.println("An error occured on the command: "
					+ Globals.prefs.get("htmlviewer") + " " + link);
			}
		} else if (fieldName.equals("ps")) {
			try {
				if (Globals.ON_MAC) {
                    ExternalFileType type = Globals.prefs.getExternalFileTypeByExt("ps");
                    String viewer = type != null ? type.getOpenWith() : Globals.prefs.get("psviewer");
                    String[] cmd = (viewer != null) && (viewer.length() > 0) ?
                            new String[] { "/usr/bin/open", "-a", viewer, link } :
                            new String[] { "/usr/bin/open", link };
                    Process child = Runtime.getRuntime().exec(cmd);
				} else if (Globals.ON_WIN) {
					openFileOnWindows(link, true);
					
				} else {
                    ExternalFileType type = Globals.prefs.getExternalFileTypeByExt("ps");
                    String viewer = type != null ? type.getOpenWith() : Globals.prefs.get("psviewer");
                    cmdArray[0] = viewer;
					cmdArray[1] = link;
					Process child = Runtime.getRuntime().exec(cmdArray);
				}
			} catch (IOException e) {
				System.err.println("An error occured on the command: "
					+ Globals.prefs.get("psviewer") + " " + link);
			}
		} else if (fieldName.equals("pdf")) {
			try {
				if (Globals.ON_MAC) {
                    ExternalFileType type = Globals.prefs.getExternalFileTypeByExt("pdf");
                    String viewer = type != null ? type.getOpenWith() : Globals.prefs.get("pdfviewer");
                    String[] cmd = (viewer != null) && (viewer.length() > 0) ?
                            new String[] { "/usr/bin/open", "-a", viewer, link } :
                            new String[] { "/usr/bin/open", link };
                    Process child = Runtime.getRuntime().exec(cmd);
				} else if (Globals.ON_WIN) {
					openFileOnWindows(link, true);
					
				} else {
                    ExternalFileType type = Globals.prefs.getExternalFileTypeByExt("pdf");
                    String viewer = type != null ? type.getOpenWith() : Globals.prefs.get("pdfviewer");
                    cmdArray[0] = viewer;
					cmdArray[1] = link;
					
					
					Process child = Runtime.getRuntime().exec(cmdArray);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("An error occured on the command: "
					+ Globals.prefs.get("pdfviewer") + " #" + link);
				System.err.println(e.getMessage());
			}
		} else {
			System.err
				.println("Message: currently only PDF, PS and HTML files can be opened by double clicking");
			
		}
	}

	
	public static void openFileOnWindows(String link, boolean localFile) throws IOException {
		
		link = link.replaceAll("&", "\"&\"").replaceAll(" ", "\" \"");

		
		
		String cmd;
		if (Globals.osName.startsWith("Windows 9")) {
			cmd = "command.com /c start " + link;
		} else {
			cmd = "cmd.exe /c start " + link;
		}

        Runtime.getRuntime().exec(cmd);
	}

    
    public static void openFileWithApplicationOnWindows(String link, String application)
        throws IOException {

        link = link.replaceAll("&", "\"&\"").replaceAll(" ", "\" \"");

		Runtime.getRuntime().exec(application + " " + link);
    }

    
	public static void openExternalFileAnyFormat(MetaData metaData, String link,
                                                 ExternalFileType fileType) throws IOException {


        boolean httpLink = link.toLowerCase().startsWith("http");

        
		File file = new File(link);

		
		String name = file.getName();
		int pos = name.lastIndexOf('.');
		String extension = ((pos >= 0) && (pos < name.length() - 1)) ? name.substring(pos + 1)
			.trim().toLowerCase() : null;

		

		
		String dir = metaData.getFileDirectory(extension);
        
        String fileDir = metaData.getFileDirectory(GUIGlobals.FILE_FIELD);

        
        String[] dirs;
        if (metaData.getFile() != null) {
            String databaseDir = metaData.getFile().getParent();
            dirs = new String[] { dir, fileDir, databaseDir };
        }
        else
            dirs = new String[] { dir, fileDir };

        if (!httpLink) {
            File tmp = expandFilename(link, dirs);
            if (tmp != null)
                file = tmp;
        }

        
		if ((httpLink || file.exists()) && (fileType != null)) {
			
			try {
                String filePath = httpLink ? link : file.getPath();
                if (Globals.ON_MAC) {
		    
                    String[] cmd = ((fileType.getOpenWith() != null) && (fileType.getOpenWith().length() > 0)) ?
                            new String[] { "/usr/bin/open", "-a", fileType.getOpenWith(), filePath } :
                            new String[] { "/usr/bin/open", filePath };
		    Runtime.getRuntime().exec(cmd);		
		} else if (Globals.ON_WIN) {
                    if ((fileType.getOpenWith() != null) && (fileType.getOpenWith().length() > 0)) {
                        
                        openFileWithApplicationOnWindows(filePath, fileType.getOpenWith());
                    } else
                        openFileOnWindows(filePath, true);
				} else {
                    String[] cmdArray = new String[] { fileType.getOpenWith(), filePath };
					Runtime.getRuntime().exec(cmdArray);
				}
			} catch (IOException e) {
                throw e;
                
			}

		} else {
			
			

			
			
			if (link.startsWith("\\u{") && link.endsWith("}"))
				link = link.substring(5, link.length() - 1);

			if (link.startsWith("doi:"))
				link = Globals.DOI_LOOKUP_PREFIX + link;

			link = sanitizeUrl(link);

			if (Globals.ON_MAC) {
				String[] cmd = { "/usr/bin/open", "-a", Globals.prefs.get("htmlviewer"), link };
				Runtime.getRuntime().exec(cmd);
			} else if (Globals.ON_WIN) {
				openFileOnWindows(link, false);
			} else {
				String[] cmdArray = new String[] { Globals.prefs.get("htmlviewer"), link };
				Runtime.getRuntime().exec(cmdArray);
			}

		}
	}

public static void openExternalFileUnknown(JabRefFrame frame, BibtexEntry entry, MetaData metaData,
                                           String link, UnknownExternalFileType fileType) throws IOException {

    String cancelMessage = Globals.lang("Unable to open file.");
    String[] options = new String[] {Globals.lang("Define '%0'", fileType.getName()),
            Globals.lang("Change file type"), Globals.lang("Cancel")};
    String defOption = options[0];
    int answer = JOptionPane.showOptionDialog(frame, Globals.lang("This external link is of the type '%0', which is undefined. What do you want to do?",
            fileType.getName()),
            Globals.lang("Undefined file type"), JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, options, defOption);
    if (answer == JOptionPane.CANCEL_OPTION) {
        frame.output(cancelMessage);
        return;
    }
    else if (answer == JOptionPane.YES_OPTION) {
        
        ExternalFileType newType = new ExternalFileType(fileType.getName(), "", "", "new");
        ExternalFileTypeEntryEditor editor = new ExternalFileTypeEntryEditor(frame, newType);
        editor.setVisible(true);
        if (editor.okPressed()) {
            
            List<ExternalFileType> fileTypes = new ArrayList<ExternalFileType>();
            ExternalFileType[] oldTypes = Globals.prefs.getExternalFileTypeSelection();
            for (int i = 0; i < oldTypes.length; i++) {
                fileTypes.add(oldTypes[i]);
            }
            fileTypes.add(newType);
            Collections.sort(fileTypes);
            Globals.prefs.setExternalFileTypes(fileTypes);
            
            openExternalFileAnyFormat(metaData, link, newType);
        } else {
            
            frame.output(cancelMessage);
            return;
        }
    }
    else {
        
        
        FileListTableModel tModel = new FileListTableModel();
        String oldValue = (String)entry.getField(GUIGlobals.FILE_FIELD);
        tModel.setContent(oldValue);
        FileListEntry flEntry = null;
        
        for (int i=0; i<tModel.getRowCount(); i++) {
            FileListEntry iEntry = tModel.getEntry(i);
            if (iEntry.getLink().equals(link)) {
                flEntry = iEntry;
                break;
            }
        }
        if (flEntry == null) {
            
            throw new RuntimeException("Could not find the file list entry "+link+" in "+entry.toString());
        }

        FileListEntryEditor editor = new FileListEntryEditor(frame, flEntry, false, true, metaData);
        editor.setVisible(true);
        if (editor.okPressed()) {
            
            String newValue = tModel.getStringRepresentation();
            UndoableFieldChange ce = new UndoableFieldChange(entry, GUIGlobals.FILE_FIELD,
                    oldValue, newValue);
            entry.setField(GUIGlobals.FILE_FIELD, newValue);
            frame.basePanel().undoManager.addEdit(ce);
            frame.basePanel().markBaseChanged();
            
            openExternalFileAnyFormat(metaData, flEntry.getLink(), flEntry.getType());
        } else {
            
            frame.output(cancelMessage);
            return;
        }
    }
}
    
	public static String sanitizeUrl(String link) {

		link = link.replaceAll("\\+", "%2B");

		try {
			link = URLDecoder.decode(link, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}

		
		try {
			return new URI(null, link, null).toASCIIString();
		} catch (URISyntaxException e) {
			return link;
		}
	}

	
	public static String findPdf(String key, String extension, String directory, OpenFileFilter off) {
		

		
		if (!directory.endsWith(System.getProperty("file.separator")))
			directory += System.getProperty("file.separator");
		String found = findInDir(key, directory, off);
		if (found != null)
			return found.substring(directory.length());
		else
			return null;
	}

	public static Map<BibtexEntry, List<File>> findAssociatedFiles(Collection<BibtexEntry> entries, Collection<String> extensions, Collection<File> directories){
		HashMap<BibtexEntry, List<File>> result = new HashMap<BibtexEntry, List<File>>();
	
		
		Set<File> filesWithExtension = findFiles(extensions, directories);
		
		
		for (BibtexEntry entry : entries){
			result.put(entry, new ArrayList<File>());
		}

        boolean exactOnly = Globals.prefs.getBoolean("autolinkExactKeyOnly");
        
		nextFile:
		for (File file : filesWithExtension){
			
			String name = file.getName();
            int dot = name.lastIndexOf('.');
            
            for (BibtexEntry entry : entries){
                String citeKey = entry.getCiteKey();
                if ((citeKey != null) && (citeKey.length() > 0)) {
                    if (dot > 0) {
                        if (name.substring(0, dot).equals(citeKey)) {
                            result.get(entry).add(file);
                            continue nextFile;
                        }
                    }
                }
            }
            
            
            if (!exactOnly) {
                for (BibtexEntry entry : entries){
                    String citeKey = entry.getCiteKey();
                    if ((citeKey != null) && (citeKey.length() > 0)) {
                        if (name.startsWith(citeKey)){
                            result.get(entry).add(file);
                            continue nextFile;
                        }
                    }
                }
            }
		}
		
		return result;
	}
	
	public static Set<File> findFiles(Collection<String> extensions, Collection<File> directories) {
		Set<File> result = new HashSet<File>();
		
		for (File directory : directories){
			result.addAll(findFiles(extensions, directory));
		}
		
		return result;
	}

	private static Collection<? extends File> findFiles(Collection<String> extensions, File directory) {
		Set<File> result = new HashSet<File>();
		
		File[] children = directory.listFiles();
		if (children == null)
			return result; 

		for (File child : children){
			if (child.isDirectory()) {
				result.addAll(findFiles(extensions, child));
			} else {
				
				String extension = getFileExtension(child);
					
				if (extension != null){
					if (extensions.contains(extension)){
						result.add(child);
					}
				}
			}
		}
		
		return result;
	}

	
	public static String getFileExtension(File file) {
		String name = file.getName();
		int pos = name.lastIndexOf('.');
		String extension = ((pos >= 0) && (pos < name.length() - 1)) ? name.substring(pos + 1)
			.trim().toLowerCase() : null;
		return extension;
	}

	
	public static String findPdf(BibtexEntry entry, String extension, String directory) {
		return findPdf(entry, extension, new String[] { directory });
	}

	
	public static String findPdf(BibtexEntry entry, String extension, String[] directories) {

		String regularExpression;
		if (Globals.prefs.getBoolean(JabRefPreferences.USE_REG_EXP_SEARCH_KEY)) {
			regularExpression = Globals.prefs.get(JabRefPreferences.REG_EXP_SEARCH_EXPRESSION_KEY);
		} else {
			regularExpression = Globals.prefs
				.get(JabRefPreferences.DEFAULT_REG_EXP_SEARCH_EXPRESSION_KEY);
		}
		regularExpression = regularExpression.replaceAll("\\[extension\\]", extension);

		return findFile(entry, null, directories, regularExpression, true);
	}

    
    public static String findFile(BibtexEntry entry, ExternalFileType fileType, List extraDirs) {

        List dirs = new ArrayList();
        dirs.addAll(extraDirs);
        if (Globals.prefs.hasKey(fileType.getExtension()+"Directory")) {
            dirs.add(Globals.prefs.get(fileType.getExtension()+"Directory"));
        }
        String [] directories = (String[])dirs.toArray(new String[dirs.size()]);
        return findPdf(entry, fileType.getExtension(), directories);
    }

    
	public static String findFile(BibtexEntry entry, BibtexDatabase database, String[] directory,
		String file, boolean relative) {

		for (int i = 0; i < directory.length; i++) {
			String result = findFile(entry, database, directory[i], file, relative);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	
	public static String stripBrackets(String s) {
		int beginIndex = (s.startsWith("[") ? 1 : 0);
		int endIndex = (s.endsWith("]") ? s.length() - 1 : s.length());
		return s.substring(beginIndex, endIndex);
	}

	public static ArrayList<String[]> parseMethodsCalls(String calls) throws RuntimeException {

		ArrayList<String[]> result = new ArrayList<String[]>();

		char[] c = calls.toCharArray();

		int i = 0;

		while (i < c.length) {

			int start = i;
			if (Character.isJavaIdentifierStart(c[i])) {
				i++;
				while (i < c.length && (Character.isJavaIdentifierPart(c[i]) || c[i] == '.')) {
					i++;
				}
				if (i < c.length && c[i] == '(') {

					String method = calls.substring(start, i);

					
					i++;
					if (i < c.length){
						if (c[i] == '"'){
							

							
							i++;

							int startParam = i;
							i++;
		                    boolean escaped = false;
							while (i + 1 < c.length &&
                                    !(!escaped && c[i] == '"' && c[i + 1] == ')')) {
                                if (c[i] == '\\') {
                                    escaped = !escaped;
                                }
                                else
                                    escaped = false;
                                i++;

                            }

							String param = calls.substring(startParam, i);
                            result.add(new String[] { method, param });
						} else {
							

							int startParam = i;

							while (i < c.length && c[i] != ')') {
								i++;
							}

							String param = calls.substring(startParam, i);

							result.add(new String[] { method, param });


						}
					} else {
						
						result.add(new String[] { method });
					}
				} else {
					String method = calls.substring(start, i);
					result.add(new String[] { method });
				}
			}
			i++;
		}

		return result;
	}

	
	public static String getFieldAndFormat(String fieldAndFormat, BibtexEntry entry,
		BibtexDatabase database) {

		fieldAndFormat = stripBrackets(fieldAndFormat);

		int colon = fieldAndFormat.indexOf(':');

		String beforeColon, afterColon;
		if (colon == -1) {
			beforeColon = fieldAndFormat;
			afterColon = null;
		} else {
			beforeColon = fieldAndFormat.substring(0, colon);
			afterColon = fieldAndFormat.substring(colon + 1);
		}
		beforeColon = beforeColon.trim();

		if (beforeColon.length() == 0) {
			return null;
		}

		String fieldValue = BibtexDatabase.getResolvedField(beforeColon, entry, database);

		if (fieldValue == null)
			return null;

		if (afterColon == null || afterColon.length() == 0)
			return fieldValue;

		try {
			LayoutFormatter[] formatters = LayoutEntry.getOptionalLayout(afterColon, "");
			for (int i = 0; i < formatters.length; i++) {
				fieldValue = formatters[i].format(fieldValue);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return fieldValue;
	}

	
	public static String findFile(BibtexEntry entry, BibtexDatabase database, String file) {
		return findFile(entry, database, (String) null, file, false);
	}

	
	public static String findFile(BibtexEntry entry, BibtexDatabase database, String directory,
		String file, boolean relative) {

		File root;
		if (directory == null) {
			root = new File(".");
		} else {
			root = new File(directory);
		}
		if (!root.exists())
			return null;

		String found = findFile(entry, database, root, file);

		if (directory == null || !relative) {
			return found;
		}

		if (found != null) {
			try {
				
                
                
                String tmp = found.substring(root.getCanonicalPath().length());
                if ((tmp.length() > 1) && (tmp.charAt(0) == File.separatorChar))
                    tmp = tmp.substring(1);
                return tmp;
                
			} catch (IOException e) {
				return null;
			}
		}
		return null;
	}

	
	protected static String findFile(BibtexEntry entry, BibtexDatabase database, File directory,
		String file) {

		if (file.startsWith("/")) {
			directory = new File(".");
			file = file.substring(1);
		}

		
		Matcher m = Pattern.compile("([^\\\\])\\\\([^\\\\])").matcher(file);
		StringBuffer s = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(s, m.group(1) + "/" + m.group(2));
		}
		m.appendTail(s);
		file = s.toString();
		String[] fileParts = file.split("/");

		if (fileParts.length == 0)
			return null;

		if (fileParts.length > 1) {

			for (int i = 0; i < fileParts.length - 1; i++) {

				String dirToProcess = fileParts[i];

				dirToProcess = expandBrackets(dirToProcess, entry, database);

				if (dirToProcess.matches("^.:$")) { 
					directory = new File(dirToProcess + "/");
					continue;
				}
				if (dirToProcess.equals(".")) { 
					continue;
				}
				if (dirToProcess.equals("..")) {
					directory = new File(directory.getParent());
					continue;
				}
				if (dirToProcess.equals("*")) { 

					File[] subDirs = directory.listFiles();
					if (subDirs == null)
						return null; 

					String restOfFileString = join(fileParts, "/", i + 1, fileParts.length);

					for (int sub = 0; sub < subDirs.length; sub++) {
						if (subDirs[sub].isDirectory()) {
							String result = findFile(entry, database, subDirs[sub],
								restOfFileString);
							if (result != null)
								return result;
						}
					}
					return null;
				}
				
				if (dirToProcess.equals("**")) {
					List toDo = new LinkedList();
					toDo.add(directory);

					String restOfFileString = join(fileParts, "/", i + 1, fileParts.length);

					
					
					String result = findFile(entry, database, directory, restOfFileString);
					if (result != null)
						return result;

					while (!toDo.isEmpty()) {

						
						File[] subDirs = ((File) toDo.remove(0)).listFiles();
						if (subDirs == null) 
							continue;

						toDo.addAll(Arrays.asList(subDirs));

						for (int sub = 0; sub < subDirs.length; sub++) {
							if (!subDirs[sub].isDirectory())
								continue;
							result = findFile(entry, database, subDirs[sub], restOfFileString);
							if (result != null)
								return result;
						}
					}
					
					return null;
				}

				final Pattern toMatch = Pattern
					.compile(dirToProcess.replaceAll("\\\\\\\\", "\\\\"));

				File[] matches = directory.listFiles(new FilenameFilter() {
					public boolean accept(File arg0, String arg1) {
						return toMatch.matcher(arg1).matches();
					}
				});
				if (matches == null || matches.length == 0)
					return null;

				directory = matches[0];

				if (!directory.exists())
					return null;

			} 
		}
		
		String filenameToLookFor = expandBrackets(fileParts[fileParts.length - 1], entry, database);

		final Pattern toMatch = Pattern.compile("^"
			+ filenameToLookFor.replaceAll("\\\\\\\\", "\\\\") + "$");

		File[] matches = directory.listFiles(new FilenameFilter() {
			public boolean accept(File arg0, String arg1) {
				return toMatch.matcher(arg1).matches();
			}
		});
		if (matches == null || matches.length == 0)
			return null;

		try {
			return matches[0].getCanonicalPath();
		} catch (IOException e) {
			return null;
		}
	}

	static Pattern squareBracketsPattern = Pattern.compile("\\[.*?\\]");

	
	public static String expandBrackets(String bracketString, BibtexEntry entry,
		BibtexDatabase database) {
		Matcher m = squareBracketsPattern.matcher(bracketString);
		StringBuffer s = new StringBuffer();
		while (m.find()) {
			String replacement = getFieldAndFormat(m.group(), entry, database);
			if (replacement == null)
				replacement = "";
			m.appendReplacement(s, replacement);
		}
		m.appendTail(s);

		return s.toString();
	}

	
	public static String join(String[] strings, String separator, int from, int to) {
		if (strings.length == 0 || from >= to)
			return "";

		StringBuffer sb = new StringBuffer();
		for (int i = from; i < to - 1; i++) {
			sb.append(strings[i]).append(separator);
		}
		return sb.append(strings[to - 1]).toString();
	}

	
	public static File expandFilename(String name, String[] dir) {

		for (int i = 0; i < dir.length; i++) {
            if (dir[i] != null) {
                File result = expandFilename(name, dir[i]);
                if (result != null) {
                    return result;
                }
            }
        }

		return null;
	}

	
	public static File expandFilename(String name, String dir) {
		
		File file = null;
		if (name == null || name.length() == 0)
			return null;
		else {
			file = new File(name);
		}

		if (file != null) {
			if (!file.exists() && (dir != null)) {
				if (dir.endsWith(System.getProperty("file.separator")))
					name = dir + name;
				else
					name = dir + System.getProperty("file.separator") + name;

				
				

				file = new File(name);

                if (file.exists())
					return file;
				
				if (Globals.ON_WIN) {
					
					
					try {
						name = name.replaceAll("/", "\\\\");
					} catch (java.lang.StringIndexOutOfBoundsException exc) {
						System.err.println("An internal Java error was caused by the entry " + "\""
							+ name + "\"");
					}
				} else
					name = name.replaceAll("\\\\", "/");
				
				file = new File(name);
				if (!file.exists())
					file = null;
			}
		}
		return file;
	}

	private static String findInDir(String key, String dir, OpenFileFilter off) {
		File f = new File(dir);
		File[] all = f.listFiles();
		if (all == null)
			return null; 
		

		int numFiles = all.length;

		for (int i = 0; i < numFiles; i++) {
			File curFile = all[i];

			if (curFile.isFile()) {
				String name = curFile.getName();
				if (name.startsWith(key + ".") && off.accept(name))
					return curFile.getPath();

			} else if (curFile.isDirectory()) {
				String found = findInDir(key, curFile.getPath(), off);
				if (found != null)
					return found;
			}
		}
		return null;
	}

	
	public static boolean isDuplicate(BibtexEntry one, BibtexEntry two, float threshold) {

		
		if (one.getType() != two.getType())
			return false;

		
		String[] fields = one.getType().getRequiredFields();

        float req;
        if (fields == null)
			req = 1;
        else
            req = compareFieldSet(fields, one, two);
		fields = one.getType().getOptionalFields();

		if (fields != null) {
			float opt = compareFieldSet(fields, one, two);
			return (2 * req + opt) / 3 >= threshold;
		} else {
			return (req >= threshold);
		}
	}

	
	public static BibtexEntry containsDuplicate(BibtexDatabase database, BibtexEntry entry) {
		Collection entries = database.getEntries();
		for (Iterator i = entries.iterator(); i.hasNext();) {
			BibtexEntry other = (BibtexEntry) i.next();
			if (isDuplicate(entry, other, Globals.duplicateThreshold))
				return other; 
		}
		return null; 
	}

	private static float compareFieldSet(String[] fields, BibtexEntry one, BibtexEntry two) {
		int res = 0;
		for (int i = 0; i < fields.length; i++) {
			
			if (compareSingleField(fields[i], one, two) == EQUAL) {
				res++;
				
			}
		}
		return ((float) res) / ((float) fields.length);
	}

	private static int compareSingleField(String field, BibtexEntry one, BibtexEntry two) {
		String s1 = (String) one.getField(field), s2 = (String) two.getField(field);
		if (s1 == null) {
			if (s2 == null)
				return EQUAL;
			else
				return EMPTY_IN_ONE;
		} else if (s2 == null)
			return EMPTY_IN_TWO;
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		
		if (field.equals("author") || field.equals("editor")) {
			
			
			String[] aus1 = AuthorList.fixAuthor_lastNameFirst(s1).split(" and "), aus2 = AuthorList
				.fixAuthor_lastNameFirst(s2).split(" and "), au1 = aus1[0].split(","), au2 = aus2[0]
				.split(",");

			
			if ((aus1.length > 0) && (aus1.length == aus2.length)
				&& au1[0].trim().equals(au2[0].trim()))
				return EQUAL;
			else
				return NOT_EQUAL;
		} else {
			if (s1.trim().equals(s2.trim()))
				return EQUAL;
			else
				return NOT_EQUAL;
		}

	}

	public static double compareEntriesStrictly(BibtexEntry one, BibtexEntry two) {
		HashSet allFields = new HashSet();
		Object[] o = one.getAllFields();
		for (int i = 0; i < o.length; i++)
			allFields.add(o[i]);
		o = two.getAllFields();
		for (int i = 0; i < o.length; i++)
			allFields.add(o[i]);
		int score = 0;
		for (Iterator fld = allFields.iterator(); fld.hasNext();) {
			String field = (String) fld.next();
			Object en = one.getField(field), to = two.getField(field);
			if ((en != null) && (to != null) && (en.equals(to)))
				score++;
			else if ((en == null) && (to == null))
				score++;
		}
		if (score == allFields.size())
			return 1.01; 
		
		
		else
			return ((double) score) / allFields.size();
	}

    
    public static void updateCompletersForEntry(HashMap autoCompleters,
                                                BibtexEntry be) {

        for (Iterator j = autoCompleters.keySet().iterator(); j.hasNext();) {
            String field = (String) j.next();
            AutoCompleter comp = (AutoCompleter) autoCompleters.get(field);
            comp.addAll(be.getField(field));
        }
    }


	
	public static void setAutomaticFields(List bibs, boolean overwriteOwner,
             boolean overwriteTimestamp) {
		String defaultOwner = Globals.prefs.get("defaultOwner");
		String timestamp = easyDateFormat();
		boolean globalSetOwner = Globals.prefs.getBoolean("useOwner"),
                globalSetTimeStamp = Globals.prefs.getBoolean("useTimeStamp");
		String timeStampField = Globals.prefs.get("timeStampField");
		
		for (int i = 0; i < bibs.size(); i++) {
			
			BibtexEntry curEntry = (BibtexEntry) bibs.get(i);
            boolean setOwner = globalSetOwner &&
                (overwriteOwner || (curEntry.getField(BibtexFields.OWNER)==null));
            boolean setTimeStamp = globalSetTimeStamp &&
                (overwriteTimestamp || (curEntry.getField(timeStampField)==null));
            setAutomaticFields(curEntry, setOwner, defaultOwner, setTimeStamp, timeStampField,
				timestamp);

		}

	}

	
	public static void setAutomaticFields(BibtexEntry entry, boolean overwriteOwner,
                                          boolean overwriteTimestamp) {
		String defaultOwner = Globals.prefs.get("defaultOwner");
		String timestamp = easyDateFormat();
        String timeStampField = Globals.prefs.get("timeStampField");
        boolean setOwner = Globals.prefs.getBoolean("useOwner") &&
            (overwriteOwner || (entry.getField(BibtexFields.OWNER)==null));
        boolean setTimeStamp = Globals.prefs.getBoolean("useTimeStamp") &&
            (overwriteTimestamp || (entry.getField(timeStampField)==null));

		setAutomaticFields(entry, setOwner, defaultOwner, setTimeStamp, timeStampField, timestamp);
	}

	private static void setAutomaticFields(BibtexEntry entry, boolean setOwner, String owner,
		boolean setTimeStamp, String timeStampField, String timeStamp) {

		
		if (setOwner) {
			
			
			
			
			entry.setField(BibtexFields.OWNER, owner);
			
		}

		if (setTimeStamp)
			entry.setField(timeStampField, timeStamp);
	}

	
	public static boolean copyFile(File source, File dest, boolean deleteIfExists)
		throws IOException {

		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			
			if (dest.exists()) {
				if (!deleteIfExists)
					return false;
				
			}

			in = new BufferedInputStream(new FileInputStream(source));
			out = new BufferedOutputStream(new FileOutputStream(dest));
			int el;
			
			while ((el = in.read()) >= 0) {
				out.write(el);
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
			if (in != null)
				in.close();
		}
		return true;
	}

	
	public static void performCompatibilityUpdate() {

		
		
		String genFields = Globals.prefs.get("generalFields");
		
		if (genFields.indexOf("abstract") >= 0) {
			
			String newGen;
			if (genFields.equals("abstract"))
				newGen = "";
			else if (genFields.indexOf(";abstract;") >= 0) {
				newGen = genFields.replaceAll(";abstract;", ";");
			} else if (genFields.indexOf("abstract;") == 0) {
				newGen = genFields.replaceAll("abstract;", "");
			} else if (genFields.indexOf(";abstract") == genFields.length() - 9) {
				newGen = genFields.replaceAll(";abstract", "");
			} else
				newGen = genFields;
			
			Globals.prefs.put("generalFields", newGen);
		}

	}

    
    public static NamedCompound upgradePdfPsToFile(BibtexDatabase database, String[] fields) {
        NamedCompound ce = new NamedCompound(Globals.lang("Move external links to 'file' field"));
        for (Iterator i = database.getEntryMap().keySet().iterator(); i.hasNext();) {
            BibtexEntry entry = (BibtexEntry) database.getEntryMap().get(i.next());
            FileListTableModel tableModel = new FileListTableModel();
            
            Object oldFileContent = entry.getField(GUIGlobals.FILE_FIELD);
            if (oldFileContent != null) {
                tableModel.setContent((String) oldFileContent);
            }
            int oldRowCount = tableModel.getRowCount();
            for (int j = 0; j < fields.length; j++) {
                Object o = entry.getField(fields[j]);
                if (o != null) {
                    String s = (String) o;
                    if (s.trim().length() > 0) {
                        File f = new File(s);
                        String extension = "";
                        if ((s.lastIndexOf('.') >= 0) && (s.lastIndexOf('.') < s.length() - 1)) {
                            extension = s.substring(s.lastIndexOf('.') + 1);
                        }
                        FileListEntry flEntry = new FileListEntry(f.getName(), s,
                                Globals.prefs.getExternalFileTypeByExt(fields[j]));
                        tableModel.addEntry(tableModel.getRowCount(), flEntry);
                        
                        entry.clearField(fields[j]);
                        ce.addEdit(new UndoableFieldChange(entry, fields[j], o, null));
                    }
                }
            }
            if (tableModel.getRowCount() != oldRowCount) {
                String newValue = tableModel.getStringRepresentation();
                entry.setField(GUIGlobals.FILE_FIELD, newValue);
                ce.addEdit(new UndoableFieldChange(entry, GUIGlobals.FILE_FIELD, oldFileContent, newValue));
            }
        }
        ce.end();
        return ce;
    }

    

	
	public static String getCorrectFileName(String orgName, String defaultExtension) {
		if (orgName == null)
			return "";

		String back = orgName;
		int t = orgName.indexOf(".", 1); 
		if (t < 1)
			back = back + "." + defaultExtension;

		return back;
	}

	
	public static String quoteForHTML(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); ++i) {
			sb.append("&#" + (int) s.charAt(i) + ";");
		}
		return sb.toString();
	}

	public static String quote(String s, String specials, char quoteChar) {
		return quote(s, specials, quoteChar, 0);
	}

	
	public static String quote(String s, String specials, char quoteChar, int linewrap) {
		StringBuffer sb = new StringBuffer();
		char c;
		int linelength = 0;
		boolean isSpecial;
		for (int i = 0; i < s.length(); ++i) {
			c = s.charAt(i);
			isSpecial = specials.indexOf(c) >= 0 || c == quoteChar;
			
			if (linewrap > 0
				&& (++linelength >= linewrap || (isSpecial && linelength >= linewrap - 1))) {
				sb.append(quoteChar);
				sb.append('\n');
				linelength = 0;
			}
			if (isSpecial) {
				sb.append(quoteChar);
				++linelength;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	
	public static String unquote(String s, char quoteChar) {
		StringBuffer sb = new StringBuffer();
		char c;
		boolean quoted = false;
		for (int i = 0; i < s.length(); ++i) {
			c = s.charAt(i);
			if (quoted) { 
				if (c != '\n') 
					sb.append(c);
				quoted = false;
			} else if (c != quoteChar) {
				sb.append(c);
			} else { 
				quoted = true;
			}
		}
		return sb.toString();
	}

	
	public static String quoteMeta(String s) {
		
		
		int i = s.length() - 1;
		StringBuffer bs = new StringBuffer("");
		while ((i >= 0) && (s.charAt(i) == '\\')) {
			--i;
			bs.append("\\\\");
		}
		s = s.substring(0, i + 1);
		return "\\Q" + s.replaceAll("\\\\E", "\\\\E\\\\\\\\E\\\\Q") + "\\E" + bs.toString();
	}

	
	public static String sortWordsAndRemoveDuplicates(String text) {

		String[] words = text.split(", ");
		SortedSet set = new TreeSet();
		for (int i = 0; i < words.length; i++)
			set.add(words[i]);
		StringBuffer sb = new StringBuffer();
		for (Iterator i = set.iterator(); i.hasNext();) {
			sb.append(i.next());
			sb.append(", ");
		}
		if (sb.length() > 2)
			sb.delete(sb.length() - 2, sb.length());
		String result = sb.toString();
		return result.length() > 2 ? result : "";
	}

	
	public static boolean warnAssignmentSideEffects(AbstractGroup[] groups, BibtexEntry[] entries,
		BibtexDatabase db, Component parent) {
		Vector affectedFields = new Vector();
		for (int k = 0; k < groups.length; ++k) {
			if (groups[k] instanceof KeywordGroup) {
				KeywordGroup kg = (KeywordGroup) groups[k];
				String field = kg.getSearchField().toLowerCase();
				if (field.equals("keywords"))
					continue; 
				for (int i = 0, len = BibtexFields.numberOfPublicFields(); i < len; ++i) {
					if (field.equals(BibtexFields.getFieldName(i))) {
						affectedFields.add(field);
						break;
					}
				}
			}
		}
		if (affectedFields.size() == 0)
			return true; 

		
		StringBuffer message = 
		new StringBuffer("This action will modify the following field(s)\n"
			+ "in at least one entry each:\n");
		for (int i = 0; i < affectedFields.size(); ++i)
			message.append(affectedFields.elementAt(i)).append("\n");
		message.append("This could cause undesired changes to "
			+ "your entries, so it is\nrecommended that you change the grouping field "
			+ "in your group\ndefinition to \"keywords\" or a non-standard name."
			+ "\n\nDo you still want to continue?");
		int choice = JOptionPane.showConfirmDialog(parent, message, Globals.lang("Warning"),
			JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		return choice != JOptionPane.NO_OPTION;

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

	
	
	
	
	static Pattern titleCapitalPattern = Pattern.compile("[A-Z]+");

	
	public static String putBracesAroundCapitals(String s) {

		boolean inString = false, isBracing = false, escaped = false;
		int inBrace = 0;
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			
			int c = s.charAt(i);
			if (c == '{')
				inBrace++;
			else if (c == '}')
				inBrace--;
			else if (!escaped && (c == '#'))
				inString = !inString;

			
			if ((inBrace == 0) && !isBracing && !inString && Character.isLetter((char) c)
				&& Character.isUpperCase((char) c)) {

				buf.append('{');
				isBracing = true;
			}

			
			if (isBracing && !(Character.isLetter((char) c) && Character.isUpperCase((char) c))) {

				buf.append('}');
				isBracing = false;
			}

			
			buf.append((char) c);

			
			if ((c == '\\') && !escaped)
				escaped = true;
			else
				escaped = false;

		}
		
		if (isBracing)
			buf.append('}');

		return buf.toString();

		
	}

	static Pattern bracedTitleCapitalPattern = Pattern.compile("\\{[A-Z]+\\}");

	
	public static String removeBracesAroundCapitals(String s) {
		String previous = s;
		while ((s = removeSingleBracesAroundCapitals(s)).length() < previous.length()) {
			previous = s;
		}
		return s;
	}

	
	public static String removeSingleBracesAroundCapitals(String s) {
		Matcher mcr = bracedTitleCapitalPattern.matcher(s);
		StringBuffer buf = new StringBuffer();
		while (mcr.find()) {
			String replaceStr = mcr.group();
			mcr.appendReplacement(buf, replaceStr.substring(1, replaceStr.length() - 1));
		}
		mcr.appendTail(buf);
		return buf.toString();
	}

	
	public static OpenFileFilter getFileFilterForField(String fieldName) {
		String s = BibtexFields.getFieldExtras(fieldName);
		final String ext = "." + fieldName.toLowerCase();
		final OpenFileFilter off;
		if (s.equals("browseDocZip"))
			off = new OpenFileFilter(new String[] { ext, ext + ".gz", ext + ".bz2" });
		else
			off = new OpenFileFilter(new String[] { ext });
		return off;
	}

	
	public static void showQuickErrorDialog(JFrame parent, String title, Exception e) {
		
		final JPanel pan = new JPanel(), details = new JPanel();
		final CardLayout crd = new CardLayout();
		pan.setLayout(crd);
		final JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Sans-Serif", Font.PLAIN, 10));
		textArea.setEditable(false);
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		textArea.setText(writer.toString());
		JLabel lab = new JLabel(e.getMessage());
		JButton flip = new JButton(Globals.lang("Details"));

		FormLayout layout = new FormLayout("left:pref", "");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.append(lab);
		builder.nextLine();
		builder.append(Box.createVerticalGlue());
		builder.nextLine();
		builder.append(flip);
		final JPanel simple = builder.getPanel();

		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(350, 150));
		details.setLayout(new BorderLayout());
		details.add(scrollPane, BorderLayout.CENTER);

		flip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				crd.show(pan, "details");
			}
		});
		pan.add(simple, "simple");
		pan.add(details, "details");
		
		JOptionPane.showMessageDialog(parent, pan, title, JOptionPane.ERROR_MESSAGE);
	}

	public static String wrapHTML(String s, final int lineWidth) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer tok = new StringTokenizer(s);
		int charsLeft = lineWidth;
		while (tok.hasMoreTokens()) {
			String word = tok.nextToken();
			if (charsLeft == lineWidth) { 
				sb.append(word);
				charsLeft -= word.length();
				if (charsLeft <= 0) {
					sb.append("<br>\n");
					charsLeft = lineWidth;
				}
			} else { 
				if (charsLeft < word.length() + 1) {
					sb.append("<br>\n");
					sb.append(word);
					if (word.length() >= lineWidth - 1) {
						sb.append("<br>\n");
						charsLeft = lineWidth;
					} else {
						sb.append(" ");
						charsLeft = lineWidth - word.length() - 1;
					}
				} else {
					sb.append(' ').append(word);
					charsLeft -= word.length() + 1;
				}
			}
		}
		return sb.toString();
	}

	
	public static String easyDateFormat() {
		
		return easyDateFormat(new Date());
	}

	
	public static String easyDateFormat(Date date) {
		
		if (dateFormatter == null) {
			String format = Globals.prefs.get("timeStampFormat");
			dateFormatter = new SimpleDateFormat(format);
		}
		return dateFormatter.format(date);
	}

	public static void markEntry(BibtexEntry be, NamedCompound ce) {
		Object o = be.getField(BibtexFields.MARKED);
		if ((o != null) && (o.toString().indexOf(Globals.prefs.WRAPPED_USERNAME) >= 0))
			return;
		String newValue;
		if (o == null) {
			newValue = Globals.prefs.WRAPPED_USERNAME;
		} else {
			StringBuffer sb = new StringBuffer(o.toString());
			
			sb.append(Globals.prefs.WRAPPED_USERNAME);
			newValue = sb.toString();
		}
		ce.addEdit(new UndoableFieldChange(be, BibtexFields.MARKED, be
			.getField(BibtexFields.MARKED), newValue));
		be.setField(BibtexFields.MARKED, newValue);
	}

	public static void unmarkEntry(BibtexEntry be, BibtexDatabase database, NamedCompound ce) {
		Object o = be.getField(BibtexFields.MARKED);
		if (o != null) {
			String s = o.toString();
			if (s.equals("0")) {
				unmarkOldStyle(be, database, ce);
				return;
			}

			int piv = 0, hit;
			StringBuffer sb = new StringBuffer();
			while ((hit = s.indexOf(Globals.prefs.WRAPPED_USERNAME, piv)) >= 0) {
				if (hit > 0)
					sb.append(s.substring(piv, hit));
				piv = hit + Globals.prefs.WRAPPED_USERNAME.length();
			}
			if (piv < s.length() - 1) {
				sb.append(s.substring(piv));
			}
			String newVal = sb.length() > 0 ? sb.toString() : null;
			ce.addEdit(new UndoableFieldChange(be, BibtexFields.MARKED, be
				.getField(BibtexFields.MARKED), newVal));
			be.setField(BibtexFields.MARKED, newVal);
		}
	}

	
	private static void unmarkOldStyle(BibtexEntry be, BibtexDatabase database, NamedCompound ce) {
		TreeSet owners = new TreeSet();
		for (Iterator i = database.getEntries().iterator(); i.hasNext();) {
			BibtexEntry entry = (BibtexEntry) i.next();
			Object o = entry.getField(BibtexFields.OWNER);
			if (o != null)
				owners.add(o);
			
		}
		owners.remove(Globals.prefs.get("defaultOwner"));
		StringBuffer sb = new StringBuffer();
		for (Iterator i = owners.iterator(); i.hasNext();) {
			sb.append('[');
			sb.append(i.next().toString());
			sb.append(']');
		}
		String newVal = sb.toString();
		if (newVal.length() == 0)
			newVal = null;
		ce.addEdit(new UndoableFieldChange(be, BibtexFields.MARKED, be
			.getField(BibtexFields.MARKED), newVal));
		be.setField(BibtexFields.MARKED, newVal);

	}

	public static boolean isMarked(BibtexEntry be) {
		Object fieldVal = be.getField(BibtexFields.MARKED);
		if (fieldVal == null)
			return false;
		String s = (String) fieldVal;
		return (s.equals("0") || (s.indexOf(Globals.prefs.WRAPPED_USERNAME) >= 0));
	}

	
	public static UndoableEdit massSetField(Collection entries, String field, String text,
		boolean overwriteValues) {

		NamedCompound ce = new NamedCompound(Globals.lang("Set field"));
		for (Iterator i = entries.iterator(); i.hasNext();) {
			BibtexEntry entry = (BibtexEntry) i.next();
			Object oldVal = entry.getField(field);
			
			
			
			if (!overwriteValues && (oldVal != null) && (((String) oldVal).length() > 0))
				continue;
			if (text != null)
				entry.setField(field, text);
			else
				entry.clearField(field);
			ce.addEdit(new UndoableFieldChange(entry, field, oldVal, text));
		}
		ce.end();
		return ce;
	}

	
	public static List findEncodingsForString(String characters) {
		List encodings = new ArrayList();
		for (int i = 0; i < Globals.ENCODINGS.length; i++) {
			CharsetEncoder encoder = Charset.forName(Globals.ENCODINGS[i]).newEncoder();
			if (encoder.canEncode(characters))
				encodings.add(Globals.ENCODINGS[i]);
		}
		return encodings;
	}

	
	public static String toFourDigitYear(String year) {
		if (thisYear == 0) {
			thisYear = Calendar.getInstance().get(Calendar.YEAR);
		}
		return toFourDigitYear(year, thisYear);
	}

	public static int thisYear;

	
	public static String toFourDigitYear(String year, int thisYear) {
		if (year.length() != 2)
			return year;
		try {
			int thisYearTwoDigits = thisYear % 100;
			int thisCentury = thisYear - thisYearTwoDigits;

			int yearNumber = Integer.parseInt(year);

			if (yearNumber == thisYearTwoDigits) {
				return String.valueOf(thisYear);
			}
			
			
			if ((yearNumber + 100 - thisYearTwoDigits) % 100 > 30) {
				if (yearNumber < thisYearTwoDigits) {
					return String.valueOf(thisCentury + yearNumber);
				} else {
					return String.valueOf(thisCentury - 100 + yearNumber);
				}
			} else {
				if (yearNumber < thisYearTwoDigits) {
					return String.valueOf(thisCentury + 100 + yearNumber);
				} else {
					return String.valueOf(thisCentury + yearNumber);
				}
			}
		} catch (NumberFormatException e) {
			return year;
		}
	}

	
	public static int getMonthNumber(String month) {

		month = month.replaceAll("#", "").toLowerCase();

		for (int i = 0; i < Globals.MONTHS.length; i++) {
			if (month.startsWith(Globals.MONTHS[i])) {
				return i;
			}
		}

		try {
			return Integer.parseInt(month) - 1;
		} catch (NumberFormatException e) {
		}
		return -1;
	}


    
    public static String encodeStringArray(String[][] values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            sb.append(encodeStringArray(values[i]));
            if (i < values.length-1)
                sb.append(';');
        }
        return sb.toString();
    }

    
    public static String encodeStringArray(String[] entry) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < entry.length; i++) {
            sb.append(encodeString(entry[i]));
            if (i < entry.length-1)
                sb.append(':');

        }
        return sb.toString();
    }

    
    public static String[][] decodeStringDoubleArray(String value) {
        ArrayList<ArrayList<String>> newList = new ArrayList<ArrayList<String>>();
        StringBuilder sb = new StringBuilder();
        ArrayList<String> thisEntry = new ArrayList<String>();
        boolean escaped = false;
        for (int i=0; i<value.length(); i++) {
            char c = value.charAt(i);
            if (!escaped && (c == '\\')) {
                escaped = true;
                continue;
            }
            else if (!escaped && (c == ':')) {
                thisEntry.add(sb.toString());
                sb = new StringBuilder();
            }
            else if (!escaped && (c == ';')) {
                thisEntry.add(sb.toString());
                sb = new StringBuilder();
                newList.add(thisEntry);
                thisEntry = new ArrayList<String>();
            }
            else sb.append(c);
            escaped = false;
        }
        if (sb.length() > 0)
            thisEntry.add(sb.toString());
        if (thisEntry.size() > 0)
            newList.add(thisEntry);

        
        String[][] res = new String[newList.size()][];
        for (int i = 0; i < res.length; i++) {
            res[i] = new String[newList.get(i).size()];
            for (int j = 0; j < res[i].length; j++) {
                res[i][j] = newList.get(i).get(j);
            }
        }
        return res;
    }

    private static String encodeString(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if ((c == ';') || (c == ':') || (c == '\\'))
                sb.append('\\');
            sb.append(c);
        }
        return sb.toString();
    }

    
	public static boolean equals(Object one, Object two) {
		return one == null ? two == null : one.equals(two);
	}

}
