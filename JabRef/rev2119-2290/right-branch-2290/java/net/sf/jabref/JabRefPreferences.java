

package net.sf.jabref;

import java.io.*;
import javax.swing.*;

import net.sf.jabref.labelPattern.DefaultLabelPatterns;
import net.sf.jabref.labelPattern.LabelPattern;
import net.sf.jabref.export.CustomExportList;
import net.sf.jabref.imports.CustomImportList;
import java.awt.*;
import java.util.prefs.*;
import java.util.*;
import java.util.List;
import java.awt.event.*;
import net.sf.jabref.export.ExportComparator;
import net.sf.jabref.external.ExternalFileType;
import net.sf.jabref.external.UnknownExternalFileType;

public class JabRefPreferences {

    public final String
        CUSTOM_TYPE_NAME = "customTypeName_",
        CUSTOM_TYPE_REQ = "customTypeReq_",
        CUSTOM_TYPE_OPT = "customTypeOpt_",
        CUSTOM_TAB_NAME = "customTabName_",
        CUSTOM_TAB_FIELDS = "customTabFields_";

    public String WRAPPED_USERNAME;

    Preferences prefs;
    public HashMap defaults = new HashMap(),
        keyBinds = new HashMap(),
        defKeyBinds = new HashMap();
    private HashSet putBracesAroundCapitalsFields = new HashSet(4);
    private HashSet nonWrappableFields = new HashSet(4);
    private static final LabelPattern KEY_PATTERN = new DefaultLabelPatterns();
    private static LabelPattern keyPattern;

    
    public CustomExportList customExports;

    
    public CustomImportList customImports;

    
    private EntryEditorTabList tabList = null;

    
    private TreeSet externalFileTypes = new TreeSet();

    
    private static JabRefPreferences singleton = null;

    public static JabRefPreferences getInstance() {
		if (singleton == null)
			singleton = new JabRefPreferences();
		return singleton;
	}

    
    private JabRefPreferences() {

        prefs = Preferences.userNodeForPackage(JabRef.class);
        
        
        if (Globals.osName.equals(Globals.MAC)) {
			defaults.put("pdfviewer", "/Applications/Preview.app");
			defaults.put("psviewer", "/Applications/Preview.app");
			defaults.put("htmlviewer", "/Applications/Safari.app");
		} else if (Globals.osName.toLowerCase().startsWith("windows")) {
			defaults.put("pdfviewer", "cmd.exe /c start /b");
			defaults.put("psviewer", "cmd.exe /c start /b");
			defaults.put("htmlviewer", "cmd.exe /c start /b");
			defaults.put("lookAndFeel", "com.jgoodies.plaf.windows.ExtWindowsLookAndFeel");
            defaults.put("winEdtPath", "C:\\Program Files\\WinEdt Team\\WinEdt\\WinEdt.exe");
            defaults.put("latexEditorPath", "C:\\Program Files\\LEd\\LEd.exe");
        } else {
			defaults.put("pdfviewer", "acroread");
			defaults.put("psviewer", "gv");
			defaults.put("htmlviewer", "mozilla");
			defaults.put("lookAndFeel", "com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
		}
        defaults.put("useDefaultLookAndFeel", Boolean.TRUE);
        defaults.put("lyxpipe", System.getProperty("user.home")+File.separator+".lyx/lyxpipe");
        defaults.put("vim", "vim");
        defaults.put("vimServer", "vim");
        defaults.put("posX", new Integer(0));
        defaults.put("posY", new Integer(0));
        defaults.put("sizeX", new Integer(840));
        defaults.put("sizeY", new Integer(680));
        defaults.put("rememberWindowLocation", Boolean.TRUE);
        defaults.put("autoResizeMode", new Integer(JTable.AUTO_RESIZE_OFF));
        defaults.put("tableColorCodesOn", Boolean.TRUE);
        defaults.put("namesAsIs", Boolean.FALSE);
        defaults.put("namesFf", Boolean.FALSE);
        defaults.put("namesLf", Boolean.FALSE);
        defaults.put("namesNatbib", Boolean.TRUE);
        defaults.put("abbrAuthorNames", Boolean.TRUE);
        defaults.put("namesLastOnly", Boolean.TRUE);
        defaults.put("language", "en");
        defaults.put("showShort", Boolean.TRUE);
        defaults.put("priSort", "author");
        defaults.put("priDescending", Boolean.FALSE);
        defaults.put("priBinary", Boolean.FALSE);
        defaults.put("secSort", "year");
        defaults.put("secDescending", Boolean.TRUE);
        defaults.put("terSort", "author");
        defaults.put("terDescending", Boolean.FALSE);
        defaults.put("columnNames", "entrytype;author;title;year;journal;owner;timestamp;bibtexkey");
        defaults.put("columnWidths","75;280;400;60;100;100;100;100");
        defaults.put("xmpPrivacyFilters", "pdf;timestamp;keywords;owner;note;review");
        defaults.put("useXmpPrivacyFilter", Boolean.FALSE);
        defaults.put("numberColWidth",new Integer(GUIGlobals.NUMBER_COL_LENGTH));
        defaults.put("workingDirectory", System.getProperty("user.home"));
        defaults.put("exportWorkingDirectory", System.getProperty("user.home"));
        defaults.put("importWorkingDirectory", System.getProperty("user.home"));
        defaults.put("fileWorkingDirectory", System.getProperty("user.home"));
        defaults.put("autoOpenForm", Boolean.TRUE);
        defaults.put("entryTypeFormHeightFactor", new Integer(1));
        defaults.put("entryTypeFormWidth", new Integer(1));
        defaults.put("backup", Boolean.TRUE);
        defaults.put("openLastEdited", Boolean.TRUE);
        defaults.put("lastEdited", null);
        defaults.put("stringsPosX", new Integer(0));
        defaults.put("stringsPosY", new Integer(0));
        defaults.put("stringsSizeX", new Integer(600));
        defaults.put("stringsSizeY", new Integer(400));
        defaults.put("defaultShowSource", Boolean.FALSE);
        defaults.put("showSource", Boolean.TRUE);
        defaults.put("defaultAutoSort", Boolean.FALSE);
        defaults.put("enableSourceEditing", Boolean.TRUE);
        defaults.put("caseSensitiveSearch", Boolean.FALSE);
        defaults.put("searchReq", Boolean.TRUE);
        defaults.put("searchOpt", Boolean.TRUE);
        defaults.put("searchGen", Boolean.TRUE);
        defaults.put("searchAll", Boolean.FALSE);
        defaults.put("incrementS", Boolean.FALSE);
        defaults.put("saveInStandardOrder", Boolean.TRUE);
        defaults.put("saveInOriginalOrder", Boolean.FALSE);
        defaults.put("selectS", Boolean.FALSE);
        defaults.put("regExpSearch", Boolean.TRUE);
        defaults.put("searchPanePosX", new Integer(0));
        defaults.put("searchPanePosY", new Integer(0));
        defaults.put("autoComplete", Boolean.TRUE);
        defaults.put("autoCompleteFields", "author;editor;title;journal;publisher;keywords");
        defaults.put("groupSelectorVisible", Boolean.TRUE);
        defaults.put("groupFloatSelections", Boolean.TRUE);
        defaults.put("groupIntersectSelections", Boolean.TRUE);
        defaults.put("groupInvertSelections", Boolean.FALSE);
        defaults.put("groupShowOverlapping", Boolean.FALSE);
        defaults.put("groupSelectMatches", Boolean.FALSE);
        defaults.put("groupsDefaultField", "keywords");
        defaults.put("groupShowIcons", Boolean.TRUE);
        defaults.put("groupShowDynamic", Boolean.TRUE);
        defaults.put("groupExpandTree", Boolean.TRUE);
        defaults.put("groupAutoShow", Boolean.TRUE);
        defaults.put("groupAutoHide", Boolean.TRUE);
        defaults.put("groupKeywordSeparator", ", ");
        defaults.put("highlightGroupsMatchingAny", Boolean.FALSE);
        defaults.put("highlightGroupsMatchingAll", Boolean.FALSE);
        defaults.put("searchPanelVisible", Boolean.FALSE);
        defaults.put("defaultEncoding", System.getProperty("file.encoding"));
        defaults.put("groupsVisibleRows", new Integer(8));
        defaults.put("defaultOwner", System.getProperty("user.name"));
        defaults.put("preserveFieldFormatting", Boolean.FALSE);
    
        defaults.put("generalFields", "crossref;keywords;file;doi;url;urldate;citeseerurl;"+
                     "pdf;comment;owner");

        defaults.put("useCustomIconTheme", Boolean.FALSE);
        defaults.put("customIconThemeFile", "/home/alver/div/crystaltheme_16/Icons.properties");

    
    defaults.put(CUSTOM_TAB_NAME+"_def0", Globals.lang("General"));
        defaults.put(CUSTOM_TAB_FIELDS+"_def0", "crossref;keywords;file;doi;url;citeseerurl;"+
                     "comment;owner;timestamp");

    
        defaults.put(CUSTOM_TAB_FIELDS+"_def1", "abstract");
    defaults.put(CUSTOM_TAB_NAME+"_def1", Globals.lang("Abstract"));

  
        defaults.put(CUSTOM_TAB_FIELDS+"_def2", "review");
    defaults.put(CUSTOM_TAB_NAME+"_def2", Globals.lang("Review"));

        
        defaults.put("historySize", new Integer(8));
        defaults.put("fontFamily", "Times");
        defaults.put("fontStyle", new Integer(java.awt.Font.PLAIN));
        defaults.put("fontSize", new Integer(12));
        defaults.put("overrideDefaultFonts", Boolean.FALSE);
        defaults.put("menuFontFamily", "Times");
        defaults.put("menuFontStyle", new Integer(java.awt.Font.PLAIN));
        defaults.put("menuFontSize", new Integer(11));
        
        defaults.put("tableBackground", "255:255:255");
        defaults.put("tableReqFieldBackground", "230:235:255");
        defaults.put("tableOptFieldBackground", "230:255:230");
        defaults.put("tableText", "0:0:0");
        defaults.put("gridColor", "210:210:210");
        defaults.put("grayedOutBackground", "210:210:210");
        defaults.put("grayedOutText", "40:40:40");
        defaults.put("veryGrayedOutBackground", "180:180:180");
        defaults.put("veryGrayedOutText", "40:40:40");
        defaults.put("markedEntryBackground", "255:255:180");
        defaults.put("incompleteEntryBackground", "250:175:175");

        defaults.put("antialias", Boolean.FALSE);
        defaults.put("ctrlClick", Boolean.FALSE);
        defaults.put("disableOnMultipleSelection", Boolean.FALSE);
        defaults.put("pdfColumn", Boolean.FALSE);
        defaults.put("urlColumn", Boolean.TRUE);
        defaults.put("fileColumn", Boolean.TRUE);
        defaults.put("citeseerColumn", Boolean.FALSE);
        defaults.put("useOwner", Boolean.TRUE);
        defaults.put("overwriteOwner", Boolean.FALSE);
        defaults.put("allowTableEditing", Boolean.FALSE);
        defaults.put("dialogWarningForDuplicateKey", Boolean.TRUE);
        defaults.put("dialogWarningForEmptyKey", Boolean.TRUE);
        defaults.put("displayKeyWarningDialogAtStartup", Boolean.TRUE);
        defaults.put("avoidOverwritingKey", Boolean.FALSE);
        defaults.put("warnBeforeOverwritingKey", Boolean.TRUE);
        defaults.put("confirmDelete", Boolean.TRUE);
        defaults.put("grayOutNonHits", Boolean.TRUE);
        defaults.put("defaultLabelPattern", "[auth][year]");
        defaults.put("previewEnabled", Boolean.TRUE);
        defaults.put("preview0", "<font face=\"arial\">"
                     +"<b><i>\\bibtextype</i><a name=\"\\bibtexkey\">\\begin{bibtexkey} (\\bibtexkey)</a>"
                     +"\\end{bibtexkey}</b><br>__NEWLINE__"
                     +"\\begin{author} \\format[AuthorLastFirst,HTMLChars,AuthorAbbreviator,AuthorAndsReplacer]{\\author}<BR>\\end{author}__NEWLINE__"
                     +"\\begin{editor} \\format[AuthorLastFirst,HTMLChars,AuthorAbbreviator,AuthorAndsReplacer]{\\editor} <i>(ed.)</i><BR>\\end{editor}__NEWLINE__"
                     +"\\begin{title} \\format[HTMLChars]{\\title} \\end{title}<BR>__NEWLINE__"
                     +"\\begin{chapter} \\format[HTMLChars]{\\chapter}<BR>\\end{chapter}__NEWLINE__"
                     +"\\begin{journal} <em>\\format[HTMLChars]{\\journal}, </em>\\end{journal}__NEWLINE__"
                     
                     +"\\begin{booktitle} <em>\\format[HTMLChars]{\\booktitle}, </em>\\end{booktitle}__NEWLINE__"
                     +"\\begin{school} <em>\\format[HTMLChars]{\\school}, </em>\\end{school}__NEWLINE__"
                     +"\\begin{institution} <em>\\format[HTMLChars]{\\institution}, </em>\\end{institution}__NEWLINE__"
                     +"\\begin{publisher} <em>\\format[HTMLChars]{\\publisher}, </em>\\end{publisher}__NEWLINE__"
                     +"\\begin{year}<b>\\year</b>\\end{year}\\begin{volume}<i>, \\volume</i>\\end{volume}"
                     +"\\begin{pages}, \\format[FormatPagesForHTML]{\\pages} \\end{pages}"
                     +"</dd>__NEWLINE__<p></p></font>");

        defaults.put("preview1", "<font face=\"arial\">"
                     +"<b><i>\\bibtextype</i><a name=\"\\bibtexkey\">\\begin{bibtexkey} (\\bibtexkey)</a>"
                     +"\\end{bibtexkey}</b><br>__NEWLINE__"
                     +"\\begin{author} \\format[AuthorLastFirst,HTMLChars,AuthorAbbreviator,AuthorAndsReplacer]{\\author}<BR>\\end{author}__NEWLINE__"
                     +"\\begin{editor} \\format[AuthorLastFirst,HTMLChars,AuthorAbbreviator,AuthorAndsReplacer]{\\editor} <i>(ed.)</i><BR>\\end{editor}__NEWLINE__"
                     +"\\begin{title} \\format[HTMLChars]{\\title} \\end{title}<BR>__NEWLINE__"
                     +"\\begin{chapter} \\format[HTMLChars]{\\chapter}<BR>\\end{chapter}__NEWLINE__"
                     +"\\begin{journal} <em>\\format[HTMLChars]{\\journal}, </em>\\end{journal}__NEWLINE__"
                     
                     +"\\begin{booktitle} <em>\\format[HTMLChars]{\\booktitle}, </em>\\end{booktitle}__NEWLINE__"
                     +"\\begin{school} <em>\\format[HTMLChars]{\\school}, </em>\\end{school}__NEWLINE__"
                     +"\\begin{institution} <em>\\format[HTMLChars]{\\institution}, </em>\\end{institution}__NEWLINE__"
                     +"\\begin{publisher} <em>\\format[HTMLChars]{\\publisher}, </em>\\end{publisher}__NEWLINE__"
                     +"\\begin{year}<b>\\year</b>\\end{year}\\begin{volume}<i>, \\volume</i>\\end{volume}"
                     +"\\begin{pages}, \\format[FormatPagesForHTML]{\\pages} \\end{pages}__NEWLINE__"
                     +"\\begin{abstract}<BR><BR><b>Abstract: </b> \\format[HTMLChars]{\\abstract} \\end{abstract}__NEWLINE__"
                     +"\\begin{review}<BR><BR><b>Review: </b> \\format[HTMLChars]{\\review} \\end{review}"
                     +"</dd>__NEWLINE__<p></p></font>");
        defaults.put("autoDoubleBraces", Boolean.FALSE);
        defaults.put("doNotResolveStringsFor", "url");
        defaults.put("resolveStringsAllFields", Boolean.FALSE);
        defaults.put("putBracesAroundCapitals","");
        defaults.put("nonWrappableFields", "pdf;ps;url;doi");
        defaults.put("useImportInspectionDialog", Boolean.TRUE);
        defaults.put("useImportInspectionDialogForSingle", Boolean.FALSE);
        defaults.put("generateKeysAfterInspection", Boolean.TRUE);
        defaults.put("warnAboutDuplicatesInInspection", Boolean.TRUE);
        defaults.put("useTimeStamp", Boolean.TRUE);
        defaults.put("overwriteTimeStamp", Boolean.FALSE);
        defaults.put("timeStampFormat", "yyyy.MM.dd");

        defaults.put("timeStampField", BibtexFields.TIMESTAMP);
        defaults.put("generateKeysBeforeSaving", Boolean.FALSE);

        defaults.put("useRemoteServer", Boolean.FALSE);
        defaults.put("remoteServerPort", new Integer(6050));

        defaults.put("personalJournalList", null);
        defaults.put("externalJournalLists", null);
        defaults.put("citeCommand", "cite");
        defaults.put("floatMarkedEntries", Boolean.TRUE);

        defaults.put("useNativeFileDialogOnMac", Boolean.FALSE);

        defaults.put("lastUsedExport", null);
        defaults.put("sidePaneWidth", new Integer(-1));

        defaults.put("importInspectionDialogWidth", new Integer(650));
        defaults.put("importInspectionDialogHeight", new Integer(650));

        defaults.put("showFileLinksUpgradeWarning", Boolean.TRUE);

        

        
        
        
        
        

        

        restoreKeyBindings();

        customExports = new CustomExportList(this, new ExportComparator());
        customImports = new CustomImportList(this);

        
        updateSpecialFieldHandling();
        WRAPPED_USERNAME = "["+get("defaultOwner")+"]";

        String defaultExpression = "**/.*[bibtexkey].*\\\\.[extension]";
        defaults.put(DEFAULT_REG_EXP_SEARCH_EXPRESSION_KEY, defaultExpression);
        defaults.put(REG_EXP_SEARCH_EXPRESSION_KEY, defaultExpression);
        defaults.put(USE_REG_EXP_SEARCH_KEY, Boolean.FALSE);
    }
    
    public static final String DEFAULT_REG_EXP_SEARCH_EXPRESSION_KEY = "defaultRegExpSearchExpression";
    public static final String REG_EXP_SEARCH_EXPRESSION_KEY = "regExpSearchExpression";
    public static final String USE_REG_EXP_SEARCH_KEY = "useRegExpSearch";
    
    public boolean putBracesAroundCapitals(String fieldName) {
        return putBracesAroundCapitalsFields.contains(fieldName);
    }

    public void updateSpecialFieldHandling() {
        putBracesAroundCapitalsFields.clear();
        String fieldString = get("putBracesAroundCapitals");
        if (fieldString.length() > 0) {
            String[] fields = fieldString.split(";");
            for (int i=0; i<fields.length; i++)
                putBracesAroundCapitalsFields.add(fields[i]);
        }
        nonWrappableFields.clear();
        fieldString = get("nonWrappableFields");
        if (fieldString.length() > 0) {
            String[] fields = fieldString.split(";");
            for (int i=0; i<fields.length; i++)
                nonWrappableFields.add(fields[i]);
        }

    }

    
    public boolean hasKey(String key) {
        return prefs.get(key, null) != null;
    }

    public String get(String key) {
        return prefs.get(key, (String)defaults.get(key));
    }

    public String get(String key, String def) {
        return prefs.get(key, def);
    }

    public boolean getBoolean(String key) {
        return prefs.getBoolean(key, ((Boolean)defaults.get(key)).booleanValue());
    }

    public double getDouble(String key) {
        return prefs.getDouble(key, ((Double)defaults.get(key)).doubleValue());
    }

    public int getInt(String key) {
        return prefs.getInt(key, ((Integer)defaults.get(key)).intValue());
    }

    public byte[] getByteArray(String key) {
        return prefs.getByteArray(key, (byte[])defaults.get(key));
    }

    public void put(String key, String value) {
        prefs.put(key, value);
    }

    public void putBoolean(String key, boolean value) {
        prefs.putBoolean(key, value);
    }

    public void putDouble(String key, double value) {
        prefs.putDouble(key, value);
    }

    public void putInt(String key, int value) {
        prefs.putInt(key, value);
    }

    public void putByteArray(String key, byte[] value) {
        prefs.putByteArray(key, value);
    }

    public void remove(String key) {
        prefs.remove(key);
    }

    
    public void putStringArray(String key, String[] value) {
        if (value == null) {
            remove(key);
            return;
        }

        if (value.length > 0) {
            StringBuffer linked = new StringBuffer();
            for (int i=0; i<value.length-1; i++) {
                linked.append(makeEscape(value[i]));
                linked.append(";");
            }
            linked.append(makeEscape(value[value.length-1]));
            put(key, linked.toString());
        } else {
            put(key, "");
        }
    }

    
    public String[] getStringArray(String key) {
        String names = get(key);
        if (names == null)
            return null;

        StringReader rd = new StringReader(names);
        Vector arr = new Vector();
        String rs;
        try {
            while ((rs = getNextUnit(rd)) != null) {
                arr.add(rs);
            }
        } catch (IOException ex) {}
        String[] res = new String[arr.size()];
        for (int i=0; i<res.length; i++)
            res[i] = (String)arr.elementAt(i);

        return res;
    }

    
    public Color getColor(String key) {
        String value = get(key);
        int[] rgb = getRgb(value);
        return new Color(rgb[0], rgb[1], rgb[2]);
    }

    public Color getDefaultColor(String key) {
        String value = (String)defaults.get(key);
        int[] rgb = getRgb(value);
        return new Color(rgb[0], rgb[1], rgb[2]);
    }

    
    public void putColor(String key, Color color) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf(color.getRed()));
        sb.append(':');
        sb.append(String.valueOf(color.getGreen()));
        sb.append(':');
        sb.append(String.valueOf(color.getBlue()));
        put(key, sb.toString());
    }

    
    public int[] getRgb(String value) {
        String[] elements = value.split(":");
        int[] values = new int[3];
        values[0] = Integer.parseInt(elements[0]);
        values[1] = Integer.parseInt(elements[1]);
        values[2] = Integer.parseInt(elements[2]);
        return values;
    }

    
    public KeyStroke getKey(String bindName) {

        String s = (String)keyBinds.get(bindName);
        
        
        
        
        if (s == null) {
            s = (String)defKeyBinds.get(bindName);
            
            
            
            keyBinds.put(bindName, s);
        }
        if (s == null) {
          Globals.logger("Could not get key binding for \"" + bindName + "\"");
          
        }

        if (Globals.ON_MAC)
          return getKeyForMac(KeyStroke.getKeyStroke(s));
        else
          return KeyStroke.getKeyStroke(s);
    }

    
    private KeyStroke getKeyForMac(KeyStroke ks) {
      if (ks == null) return null;
      int keyCode = ks.getKeyCode();
      if ((ks.getModifiers() & KeyEvent.CTRL_MASK) == 0) {
        return ks;
      }
      else {
        if ((ks.getModifiers() & KeyEvent.SHIFT_MASK) != 0) {
          return KeyStroke.getKeyStroke(keyCode, Globals.SHORTCUT_MASK+KeyEvent.SHIFT_MASK);
        }
        return KeyStroke.getKeyStroke(keyCode, Globals.SHORTCUT_MASK);
      }
    }

    
    public HashMap getKeyBindings() {
        return keyBinds;
    }

    
    public HashMap getDefaultKeys() {
        return defKeyBinds;
    }

    public void flush() {
    try {
        prefs.flush();
    } catch (BackingStoreException ex) {
        ex.printStackTrace();
    }
    }

    
    public void setNewKeyBindings(HashMap newBindings) {
        if (!newBindings.equals(keyBinds)) {
            
            String[] bindNames = new String[newBindings.size()],
                bindings = new String[newBindings.size()];
            int index = 0;
            for (Iterator i=newBindings.keySet().iterator();
                 i.hasNext();) {
                String nm = (String)i.next();
                String bnd = (String)newBindings.get(nm);
                bindNames[index] = nm;
                bindings[index] = bnd;
                index++;
            }
            putStringArray("bindNames", bindNames);
            putStringArray("bindings", bindings);
            keyBinds = newBindings;
        }
    }


        public LabelPattern getKeyPattern(){

            keyPattern = new LabelPattern(KEY_PATTERN);
            Preferences pre = Preferences.userNodeForPackage
                (net.sf.jabref.labelPattern.LabelPattern.class);
            try {
                String[] keys = pre.keys();
            if (keys.length > 0) for (int i=0; i<keys.length; i++)
                keyPattern.addLabelPattern(keys[i], pre.get(keys[i], null));
            } catch (BackingStoreException ex) {
                Globals.logger("BackingStoreException in JabRefPreferences.getKeyPattern");
            }

            
            
            
            

            return keyPattern;
        }

        public void putKeyPattern(LabelPattern pattern){
            keyPattern = pattern;
            LabelPattern parent = pattern.getParent();
            if (parent == null)
                return;

            
            Preferences pre = Preferences.userNodeForPackage
                (net.sf.jabref.labelPattern.LabelPattern.class);
            try {
                pre.clear(); 
            } catch (BackingStoreException ex) {
                Globals.logger("BackingStoreException in JabRefPreferences.putKeyPattern");
            }

            Iterator i = pattern.keySet().iterator();
            while (i.hasNext()) {
                String s = (String)i.next();
                if (!(pattern.get(s)).equals(parent.get(s)))
                    pre.put(s, pattern.getValue(s).get(0).toString());
            }
        }

    private void restoreKeyBindings() {
        
        defineDefaultKeyBindings();

        
        String[] bindNames = getStringArray("bindNames"),
            bindings = getStringArray("bindings");

        
        if ((bindNames == null) || (bindings == null)
            || (bindNames.length != bindings.length)) {
            
            setDefaultKeyBindings();
            return;
        }

        for (int i=0; i<bindNames.length; i++)
            keyBinds.put(bindNames[i], bindings[i]);
    }

    private void setDefaultKeyBindings() {
        keyBinds = defKeyBinds;
    }

    private void defineDefaultKeyBindings() {
        defKeyBinds.put("Push to application","ctrl L");
      defKeyBinds.put("Push to LyX","ctrl L");
      defKeyBinds.put("Push to WinEdt","ctrl shift W");
      defKeyBinds.put("Push to Emacs","ctrl shift E");
        defKeyBinds.put("Quit JabRef", "ctrl Q");
        defKeyBinds.put("Open database", "ctrl O");
        defKeyBinds.put("Save database", "ctrl S");
        defKeyBinds.put("Save database as ...", "ctrl shift S");
        defKeyBinds.put("Save all", "ctrl alt S");
        defKeyBinds.put("Close database", "ctrl W");
        defKeyBinds.put("New entry", "ctrl N");
        defKeyBinds.put("Cut", "ctrl X");
        defKeyBinds.put("Copy", "ctrl C");
        defKeyBinds.put("Paste", "ctrl V");
        defKeyBinds.put("Undo", "ctrl Z");
        defKeyBinds.put("Redo", "ctrl Y");
        defKeyBinds.put("Help", "F1");
        defKeyBinds.put("New article", "ctrl shift A");
        defKeyBinds.put("New book", "ctrl shift B");
        defKeyBinds.put("New phdthesis", "ctrl shift T");
        defKeyBinds.put("New inbook", "ctrl shift I");
        defKeyBinds.put("New mastersthesis", "ctrl shift M");
        defKeyBinds.put("New proceedings", "ctrl shift P");
        defKeyBinds.put("New unpublished", "ctrl shift U");
        defKeyBinds.put("Edit strings", "ctrl T");
        defKeyBinds.put("Edit preamble", "ctrl P");
        defKeyBinds.put("Select all", "ctrl A");
        defKeyBinds.put("Toggle groups interface", "ctrl shift G");
        defKeyBinds.put("Autogenerate BibTeX keys", "ctrl G");
        defKeyBinds.put("Search", "ctrl F");
        defKeyBinds.put("Incremental search", "ctrl shift F");
        defKeyBinds.put("Repeat incremental search", "ctrl shift F");
        defKeyBinds.put("Close dialog", "ESCAPE");
        defKeyBinds.put("Close entry editor", "ESCAPE");
        defKeyBinds.put("Close preamble editor", "ESCAPE");
        defKeyBinds.put("Back, help dialog", "LEFT");
        defKeyBinds.put("Forward, help dialog", "RIGHT");
        defKeyBinds.put("Preamble editor, store changes", "alt S");
        defKeyBinds.put("Clear search", "ESCAPE");
        defKeyBinds.put("Entry editor, next panel", "ctrl TAB");
        defKeyBinds.put("Entry editor, previous panel", "ctrl shift TAB");
        defKeyBinds.put("Entry editor, next panel 2", "ctrl PLUS");
        defKeyBinds.put("Entry editor, previous panel 2", "ctrl MINUS");
        defKeyBinds.put("Entry editor, next entry", "ctrl shift DOWN");
        defKeyBinds.put("Entry editor, previous entry", "ctrl shift UP");
        defKeyBinds.put("Entry editor, store field", "alt S");
        defKeyBinds.put("String dialog, add string", "ctrl N");
        defKeyBinds.put("String dialog, remove string", "shift DELETE");
        defKeyBinds.put("String dialog, move string up", "ctrl UP");
        defKeyBinds.put("String dialog, move string down", "ctrl DOWN");
        defKeyBinds.put("Save session", "F11");
        defKeyBinds.put("Load session", "F12");
        defKeyBinds.put("Copy \\cite{BibTeX key}", "ctrl K");
        defKeyBinds.put("Next tab", "ctrl PAGE_DOWN");
        defKeyBinds.put("Previous tab", "ctrl PAGE_UP");
        defKeyBinds.put("Replace string", "ctrl R");
        defKeyBinds.put("Delete", "DELETE");
        defKeyBinds.put("Open file", "F4");
        defKeyBinds.put("Open PDF or PS", "shift F5");
        defKeyBinds.put("Open URL or DOI", "F3");
        defKeyBinds.put("Toggle entry preview", "ctrl F9");
        defKeyBinds.put("Switch preview layout", "F9");
        defKeyBinds.put("Edit entry", "ctrl E");
        defKeyBinds.put("Mark entries", "ctrl M");
        defKeyBinds.put("Unmark entries", "ctrl shift M");
        defKeyBinds.put("Fetch Medline", "F5");
        defKeyBinds.put("Fetch CiteSeer", "F6");
        defKeyBinds.put("New from plain text", "ctrl shift N");
        defKeyBinds.put("Import Fields from CiteSeer", "ctrl shift C");
        defKeyBinds.put("Fetch citations from CiteSeer", "F7");
        defKeyBinds.put("Synchronize PDF", "shift F4");
        defKeyBinds.put("Synchronize PS", "ctrl F4");
        defKeyBinds.put("Abbreviate", "ctrl alt A");
        defKeyBinds.put("Unabbreviate", "ctrl alt shift A");
        defKeyBinds.put("Search IEEXplore", "F8");
        defKeyBinds.put("Fetch ArXiv.org", "shift F8");
        defKeyBinds.put("Write XMP", "ctrl F4");

        
    }

    private String getNextUnit(Reader data) throws IOException {
        int c;
        boolean escape = false, done = false;
        StringBuffer res = new StringBuffer();
        while (!done && ((c = data.read()) != -1)) {
            if (c == '\\') {
                if (!escape)
                    escape = true;
                else {
                    escape = false;
                    res.append('\\');
                }
            } else {
                if (c == ';') {
                    if (!escape)
                        done = true;
                    else
                        res.append(';');
                } else {
                    res.append((char)c);
                }
                escape = false;
            }
        }
        if (res.length() > 0)
            return res.toString();
        else
            return null;
    }

    private String makeEscape(String s) {
        StringBuffer sb = new StringBuffer();
        int c;
        for (int i=0; i<s.length(); i++) {
            c = s.charAt(i);
            if ((c == '\\') || (c == ';'))
                sb.append('\\');
            sb.append((char)c);
        }
        return sb.toString();
    }

    
    public void storeCustomEntryType(CustomEntryType tp, int number) {
        String nr = ""+number;
        put(CUSTOM_TYPE_NAME+nr, tp.getName());
        putStringArray(CUSTOM_TYPE_REQ+nr, tp.getRequiredFields());
        putStringArray(CUSTOM_TYPE_OPT+nr, tp.getOptionalFields());

    }

    
    public CustomEntryType getCustomEntryType(int number) {
        String nr = ""+number;
        String
            name = get(CUSTOM_TYPE_NAME+nr);
        String[]
            req = getStringArray(CUSTOM_TYPE_REQ+nr),
            opt = getStringArray(CUSTOM_TYPE_OPT+nr);
        if (name == null)
            return null;
        else return new CustomEntryType
            (Util.nCase(name), req, opt);


    }

    
    public void updateExternalFileTypes() {
        if (prefs.get("externalFileTypes", null) == null) {
            externalFileTypes.clear();
            List<ExternalFileType> list = getDefaultExternalFileTypes();
            externalFileTypes.addAll(list);
        }
        else {
            String[][] vals = Util.decodeStringDoubleArray(prefs.get("externalFileTypes", ""));
            for (int i = 0; i < vals.length; i++) {
                ExternalFileType type = new ExternalFileType(vals[i]);
                externalFileTypes.add(type);
            }
        }
    }


    public List<ExternalFileType> getDefaultExternalFileTypes() {
        List<ExternalFileType> list = new ArrayList<ExternalFileType>();
        list.add(new ExternalFileType("PDF", "pdf", get("pdfviewer"), "pdfSmall"));
        list.add(new ExternalFileType("PostScript", "ps", get("psviewer"), "psSmall"));
        list.add(new ExternalFileType("Word file", "doc", "oowriter", "openoffice"));
        list.add(new ExternalFileType("OpenDocument text", "odt", "oowriter", "openoffice"));
        list.add(new ExternalFileType("PowerPoint file", "ppt", "ooimpress", "openoffice"));
        list.add(new ExternalFileType("OpenDocument presentation", "odp", "ooimpress", "openoffice"));
        list.add(new ExternalFileType("PNG image", "png", "gimp", "picture"));
        list.add(new ExternalFileType("GIF image", "gif", "gimp", "picture"));
        list.add(new ExternalFileType("JPG image", "jpg", "gimp", "picture"));
        list.add(new ExternalFileType("Text file", "txt", "emacs", "emacs"));
        ExternalFileType tp = new ExternalFileType("URL", "html", "firefox", "www");
        list.add(tp);

        
        
        if (Globals.ON_WIN) {
            for (Iterator<ExternalFileType> iterator = list.iterator(); iterator.hasNext();) {
                ExternalFileType type = iterator.next();
                type.setOpenWith("");
            }
        }

        return list;
    }

    public ExternalFileType[] getExternalFileTypeSelection() {
        return (ExternalFileType[])externalFileTypes.toArray
                (new ExternalFileType[externalFileTypes.size()]);
    }

    
    public ExternalFileType getExternalFileTypeByName(String name) {
        for (Iterator iterator = externalFileTypes.iterator(); iterator.hasNext();) {
            ExternalFileType type = (ExternalFileType) iterator.next();
            if (type.getName().equals(name))
                return type;
        }
        
        return new UnknownExternalFileType(name);
    }

    
    public ExternalFileType getExternalFileTypeByExt(String extension) {
        for (Iterator iterator = externalFileTypes.iterator(); iterator.hasNext();) {
            ExternalFileType type = (ExternalFileType) iterator.next();
            if (type.getExtension().equals(extension))
                return type;
        }
        return null;
    }


    
    public void setExternalFileTypes(List<ExternalFileType> types) {
        externalFileTypes.clear();
        for (Iterator<ExternalFileType> iterator = types.iterator(); iterator.hasNext();) {
            externalFileTypes.add(iterator.next());
        }
        String[][] array = new String[externalFileTypes.size()][];
        int i=0;
        for (Iterator iterator = externalFileTypes.iterator(); iterator.hasNext();) {
            ExternalFileType type = (ExternalFileType) iterator.next();
            array[i] = type.getStringArrayRepresentation();
            i++;
        }
        
        put("externalFileTypes", Util.encodeStringArray(array));
    }

    

    
    public void purgeCustomEntryTypes(int number) {
    purgeSeries(CUSTOM_TYPE_NAME, number);
    purgeSeries(CUSTOM_TYPE_REQ, number);
    purgeSeries(CUSTOM_TYPE_OPT, number);

        
    }

    
    public void purgeSeries(String prefix, int number) {
        while (get(prefix+number) != null) {
            remove(prefix+number);
            number++;
        }
    }

    public EntryEditorTabList getEntryEditorTabList() {
    if (tabList == null)
        updateEntryEditorTabList();
    return tabList;
    }

    public void updateEntryEditorTabList() {
    tabList = new EntryEditorTabList();
    }

    
    public void exportPreferences(String filename) throws IOException {
      File f = new File(filename);
      OutputStream os = new FileOutputStream(f);
      try {
        prefs.exportSubtree(os);
      } catch (BackingStoreException ex) {
        throw new IOException(ex.getMessage());
      }
    }

      
      public void importPreferences(String filename) throws IOException {
        File f = new File(filename);
        InputStream is = new FileInputStream(f);
        try {
          Preferences.importPreferences(is);
        } catch (InvalidPreferencesFormatException ex) {
          throw new IOException(ex.getMessage());
        }
      }

    
    public boolean isNonWrappableField(String fieldName) {
        return nonWrappableFields.contains(fieldName);
    }
}
