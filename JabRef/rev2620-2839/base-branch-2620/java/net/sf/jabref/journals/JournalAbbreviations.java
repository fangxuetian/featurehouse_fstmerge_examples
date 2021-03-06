package net.sf.jabref.journals;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.EntryEditor;
import net.sf.jabref.FieldEditor;
import net.sf.jabref.Globals;
import net.sf.jabref.undo.UndoableFieldChange;
import net.sf.jabref.util.CaseChanger;


public class JournalAbbreviations {

    static String TOOLTIPTEXT = "<HTML>"+Globals.lang("Switches between full and abbreviated journal name "
        +"if the journal name is known.")
        +"<BR>"+Globals.lang("To set up, go to <B>Tools -> Manage journal abbreviations</B>")+".</HTML>";
    TreeMap<String, String> fullNameKeyed = new TreeMap<String, String>();
    HashMap<String, String> abbrNameKeyed = new HashMap<String, String>();
    HashMap<String, String> abbrNoDotsToAbbr = new HashMap<String, String>();
    TreeMap<String, String> all = new TreeMap<String, String>();
    CaseChanger caseChanger = new CaseChanger();

    public JournalAbbreviations() {
        
    }

    public JournalAbbreviations(String resource) {
        readJournalList(resource);
    }

    public JournalAbbreviations(File file) throws FileNotFoundException {
        readJournalList(file);
    }

    
    public Iterator<String> fullNameIterator() {
        return fullNameKeyed.keySet().iterator();
    }

    public boolean isKnownName(String journalName) {
        String s = journalName.toLowerCase();
        return ((fullNameKeyed.get(s) != null) || (abbrNameKeyed.get(s) != null) || (abbrNoDotsToAbbr.get(s) != null));
    }

    public boolean isAbbreviatedName(String journalName) {
        String s = journalName.toLowerCase();
        return ((abbrNameKeyed.get(s) != null) || (abbrNoDotsToAbbr.get(s) != null));
    }

    public String dotsToNodots(String name) {
        return name.replaceAll("\\.", " ").replaceAll("  ", " ").trim();
    }

    
    public String getAbbreviatedName(String journalName, boolean withDots) {
        String s = journalName.toLowerCase();
        String abbr;
        if (fullNameKeyed.containsKey(s)) {
            abbr = fullNameKeyed.get(s);
        }
        else if (abbrNameKeyed.containsKey(s)) {
            abbr = journalName;
        }
        else if (abbrNoDotsToAbbr.containsKey(s)) {
            abbr = abbrNoDotsToAbbr.get(s);
        } else
            return null;

        if (!withDots) {
            abbr = dotsToNodots(abbr);
        }

        return abbr;
    }

    
    public String getFullName(String journalName) {
        
        String s = getAbbreviatedName(journalName, true);
	if (s != null) 
	    s = s.toLowerCase();
	else
	    return null;
        Object o = abbrNameKeyed.get(s);
        if (o == null) {
            if (fullNameKeyed.containsKey(s))
                o = s;
            else
                return null;
        }
        s = (String)o;
        return s;
    }

    public void readJournalList(String resourceFileName) {
        URL url = JournalAbbreviations.class.getResource(resourceFileName);
        try {
            readJournalList(new InputStreamReader(url.openStream()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readJournalList(File file) throws FileNotFoundException {
        readJournalList(new FileReader(file));
    }

    
    public void readJournalList(Reader in) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(in);
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                
                if (line.startsWith("#"))
                    continue;
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String fullName = parts[0].trim();
                    String fullNameLC = fullName.toLowerCase();
                    String abbrName = parts[1].trim();
                    if (abbrName.indexOf(';') >= 0) {
                        String[] restParts = abbrName.split(";");
                        abbrName = restParts[0];
                    }
                    String abbrNameLC = abbrName.toLowerCase();
                    String abbrNoDots = dotsToNodots(abbrName);
                    String abbrNoDotsLC = abbrNoDots.toLowerCase();
                    
                    if ((fullName.length()>0) && (abbrName.length()>0)) {
                        
                        fullNameKeyed.put(fullNameLC, abbrName);
                        abbrNameKeyed.put(abbrNameLC, fullName);
                        abbrNoDotsToAbbr.put(abbrNoDotsLC, abbrName);
                        all.put(fullName, abbrName);
                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException ex2) {
                ex2.printStackTrace();
            }
        }
    }

    
    public boolean abbreviate(BibtexEntry entry, String fieldName, CompoundEdit ce, boolean withDots) {
        Object o = entry.getField(fieldName);
        if (o == null)
            return false;
        String text = (String)o;
        if (isKnownName(text) && !isAbbreviatedName(text)) {
            String newText = getAbbreviatedName(text, withDots);
            if (newText == null)
                return false;
            entry.setField(fieldName, newText);
            ce.addEdit(new UndoableFieldChange(entry, fieldName, text, newText));
            return true;
        } else {
            String unabbr = getFullName(text);
            if (unabbr != null) {
                String newText = getAbbreviatedName(unabbr, withDots);
                if (newText == null)
                    return false;
                entry.setField(fieldName, newText);
                ce.addEdit(new UndoableFieldChange(entry, fieldName, text, newText));
                return true;
            } else
                return false;
        }
    }

    
    public boolean unabbreviate(BibtexEntry entry, String fieldName, CompoundEdit ce) {
        Object o = entry.getField(fieldName);
        if (o == null)
            return false;
        String text = (String)o;
        if (isKnownName(text) && isAbbreviatedName(text)) {
            String newText = getFullName(text);
            if (newText == null)
                return false;
            entry.setField(fieldName, newText);
            ce.addEdit(new UndoableFieldChange(entry, fieldName, text, newText));
            return true;
        } else
            return false;
    }


    public Map<String, String> getJournals() {
        return Collections.unmodifiableMap(all);
    }

    
    public static JComponent getNameSwitcher(final EntryEditor entryEditor, final FieldEditor editor,
                                      final UndoManager undoManager) {
        JButton button = new JButton(Globals.lang("Toggle abbreviation"));
        button.setToolTipText(TOOLTIPTEXT);
        button.addActionListener(new ActionListener() {
            boolean withDots = true;
            public void actionPerformed(ActionEvent actionEvent) {
                String text = editor.getText();
                if (Globals.journalAbbrev.isKnownName(text)) {
                    String s;
                    if (Globals.journalAbbrev.isAbbreviatedName(text)) {
                        
                        if (!withDots) {
                            s = Globals.journalAbbrev.getAbbreviatedName(text, false);
                            withDots = true;
                        } else {
                            s = Globals.journalAbbrev.getFullName(text);
                        }
                    }
                    else {
                        s = Globals.journalAbbrev.getAbbreviatedName(text, true);
                        withDots = false;
                    }

                    if (s != null) {
                        editor.setText(s);
                        entryEditor.storeFieldAction.actionPerformed(new ActionEvent(editor, 0, ""));
                        undoManager.addEdit(new UndoableFieldChange(entryEditor.getEntry(), editor.getFieldName(),
                                text, s));
                    }
                }
            }
        });

        return button;
    }

    public TableModel getTableModel() {
        Object[][] cells = new Object[fullNameKeyed.size()][2];
        int row = 0;
        for (Iterator<String> i=fullNameIterator(); i.hasNext();) {
            String name = i.next();
            cells[row][0] = getFullName(name);
            cells[row][1] = getAbbreviatedName(name, true);
            row++;
        }
        DefaultTableModel tableModel = new DefaultTableModel(cells, new Object[] {Globals.lang("Full name"),
            Globals.lang("Abbreviation")}) {

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        return tableModel;
    }

}

