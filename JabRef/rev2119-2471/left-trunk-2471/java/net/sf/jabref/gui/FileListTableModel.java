package net.sf.jabref.gui;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import net.sf.jabref.Globals;


public class FileListTableModel extends AbstractTableModel {

    private final ArrayList<FileListEntry> list = new ArrayList<FileListEntry>();

    public FileListTableModel() {
    }

    public int getRowCount() {
        synchronized (list) {
            return list.size();
        }
    }

    public int getColumnCount() {
        return 3;
    }

    public Class<String> getColumnClass(int columnIndex) {
        return String.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        synchronized (list) {
            FileListEntry entry = list.get(rowIndex);
            switch (columnIndex) {
                case 0: return entry.getDescription();
                case 1: return entry.getLink();
                default: return entry.getType() != null ?
                        entry.getType().getName() : "";
            }
        }
    }

    public FileListEntry getEntry(int index) {
        synchronized (list) {
            return list.get(index);
        }
    }

    public void removeEntry(int index) {
        synchronized (list) {
            list.remove(index);
            fireTableRowsDeleted(index, index);
        }

    }

    
    public void addEntry(final int index, final FileListEntry entry) {
        synchronized (list) {
            list.add(index, entry);
            if (!SwingUtilities.isEventDispatchThread()) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        fireTableRowsInserted(index, index);
                    }
                });
            } else
                fireTableRowsInserted(index, index);
        }

    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    
    public void setContent(String value) {
        setContent(value, false);
    }

    private FileListEntry setContent(String value, boolean firstOnly) {
        if (value == null)
            value = "";
        ArrayList<FileListEntry> newList = new ArrayList<FileListEntry>();
        StringBuilder sb = new StringBuilder();
        ArrayList<String> thisEntry = new ArrayList<String>();
        boolean inXmlChar = false;
        boolean escaped = false;
        for (int i=0; i<value.length(); i++) {
            char c = value.charAt(i);
            if (!escaped && (c == '\\')) {
                escaped = true;
                continue;
            }
            
            
            else if (!escaped && (c == '&') && !inXmlChar) {
                sb.append(c);
                if ((value.length() > i+1) && (value.charAt(i+1) == '#'))
                    inXmlChar = true;
            }
            
            else if (!escaped && inXmlChar && (c == ';')) {
                sb.append(c);
                inXmlChar = false;
            }
            else if (!escaped && (c == ':')) {
                thisEntry.add(sb.toString());
                sb = new StringBuilder();
            }
            else if (!escaped && (c == ';') && !inXmlChar) {
                thisEntry.add(sb.toString());
                sb = new StringBuilder();
                if (firstOnly)
                    return decodeEntry(thisEntry);
                else {
                    newList.add(decodeEntry(thisEntry));
                    thisEntry.clear();
                }
            }
            else sb.append(c);
            escaped = false;
        }
        if (sb.length() > 0)
            thisEntry.add(sb.toString());
        if (thisEntry.size() > 0) {
            if (firstOnly)
                return decodeEntry(thisEntry);
            else
                newList.add(decodeEntry(thisEntry));
        }
          
        synchronized (list) {
            list.clear();
            list.addAll(newList);
        }
        fireTableChanged(new TableModelEvent(this));
        return null;
    }


    private FileListEntry decodeEntry(ArrayList<String> contents) {
        return new FileListEntry(getElementIfAvailable(contents, 0),
                getElementIfAvailable(contents, 1),
                Globals.prefs.getExternalFileTypeByName
                        (getElementIfAvailable(contents, 2)));
    }



    
    public static JLabel getFirstLabel(String content) {
        FileListTableModel tm = new FileListTableModel();
        FileListEntry entry = tm.setContent(content, true);
        if (entry == null || entry.getType()==null )
            return null;
        return entry.getType().getIconLabel();
    }

    



    private String getElementIfAvailable(ArrayList<String> contents, int index) {
        if (index < contents.size())
            return contents.get(index);
        else return "";
    }

    
    public String getStringRepresentation() {
        StringBuilder sb = new StringBuilder();
        for (Iterator<FileListEntry> iterator = list.iterator(); iterator.hasNext();) {
            FileListEntry entry = iterator.next();
            sb.append(encodeEntry(entry));
            if (iterator.hasNext())
                sb.append(';');
        }
        return sb.toString();
    }

    
    public String getToolTipHTMLRepresentation() {
        StringBuilder sb = new StringBuilder("<html>");
        for (Iterator<FileListEntry> iterator = list.iterator(); iterator.hasNext();) {
            FileListEntry entry = iterator.next();
            sb.append(entry.getDescription()).append(" (").append(entry.getLink()).append(')');
            if (iterator.hasNext())
                sb.append("<br>");
        }
        return sb.append("</html>").toString();
    }

    private String encodeEntry(FileListEntry entry) {
        StringBuilder sb = new StringBuilder();
        sb.append(encodeString(entry.getDescription()));
        sb.append(':');
        sb.append(encodeString(entry.getLink()));
        sb.append(':');
        sb.append(encodeString(entry.getType() != null ? entry.getType().getName() : ""));
        return sb.toString();
    }

    private String encodeString(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if ((c == ';') || (c == ':') || (c == '\\'))
                sb.append('\\');
            sb.append(c);
        }
        return sb.toString();
    }

    public void print() {
        System.out.println("----");
        for (Iterator<FileListEntry> iterator = list.iterator(); iterator.hasNext();) {
            FileListEntry fileListEntry = iterator.next();
            System.out.println(fileListEntry);
        }
        System.out.println("----");
    }

   
}
