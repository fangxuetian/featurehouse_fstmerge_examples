package net.sf.jabref.gui;

import net.sf.jabref.*;
import net.sf.jabref.undo.NamedCompound;
import net.sf.jabref.undo.UndoableFieldChange;
import net.sf.jabref.external.ExternalFileType;
import net.sf.jabref.external.DownloadExternalFile;
import net.sf.jabref.external.UnknownExternalFileType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.io.File;
import java.io.IOException;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Created by Morten O. Alver 2007.02.22
 */
public class FileListEditor extends JTable implements FieldEditor,
        DownloadExternalFile.DownloadCallback {

    FieldNameLabel label;
    FileListEntryEditor editor = null;
    private JabRefFrame frame;
    private MetaData metaData;
    private String fieldName;
    private EntryEditor entryEditor;
    private JPanel panel;
    private FileListTableModel tableModel;
    private JScrollPane sPane;
    private JButton add, remove, up, down, auto, download;

    public FileListEditor(JabRefFrame frame, MetaData metaData, String fieldName, String content,
                          EntryEditor entryEditor) {
        this.frame = frame;
        this.metaData = metaData;
        this.fieldName = fieldName;
        this.entryEditor = entryEditor;
        label = new FieldNameLabel(" " + Util.nCase(fieldName) + " ");
        tableModel = new FileListTableModel();
        setText(content);
        setModel(tableModel);
        sPane = new JScrollPane(this);
        setTableHeader(null);
        addMouseListener(new TableClickListener());

        add = new JButton(GUIGlobals.getImage("add"));
        remove = new JButton(GUIGlobals.getImage("remove"));
        up = new JButton(GUIGlobals.getImage("up"));
        down = new JButton(GUIGlobals.getImage("down"));
        auto = new JButton(Globals.lang("Auto"));
        download = new JButton(Globals.lang("Download"));
        add.setMargin(new Insets(0,0,0,0));
        remove.setMargin(new Insets(0,0,0,0));
        up.setMargin(new Insets(0,0,0,0));
        down.setMargin(new Insets(0,0,0,0));
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addEntry();
            }
        });
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeEntries();
            }
        });
        up.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveEntry(-1);
            }
        });
        down.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveEntry(1);
            }
        });
        auto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                autoSetLinks();
            }
        });
        download.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                downloadFile();
            }
        });
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout
                ("fill:pref,1dlu,fill:pref,1dlu,fill:pref", "fill:pref,fill:pref"));
        builder.append(up);
        builder.append(add);
        builder.append(auto);
        builder.append(down);
        builder.append(remove);
        builder.append(download);        
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(sPane, BorderLayout.CENTER);
        panel.add(builder.getPanel(), BorderLayout.EAST);

        // Add an input/action pair for deleting entries:
        getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "delete");
        getActionMap().put("delete", new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                int row = getSelectedRow();
                removeEntries();
                row = Math.min(row, getRowCount()-1);
                if (row >= 0)
                    setRowSelectionInterval(row, row);
            }
        });

        // Add an input/action pair for inserting an entry:
        getInputMap().put(KeyStroke.getKeyStroke("INSERT"), "insert");
        getActionMap().put("insert", new AbstractAction() {

            public void actionPerformed(ActionEvent actionEvent) {
                addEntry();
            }
        });
    }



    public String getFieldName() {
        return fieldName;
    }

    /*
      * Returns the component to be added to a container. Might be a JScrollPane
    * or the component itself.
    */
    public JComponent getPane() {
        return panel;
    }

    /*
     * Returns the text component itself.
    */
    public JComponent getTextComponent() {
        return this;
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabelColor(Color c) {
        label.setForeground(c);
    }

    public String getText() {
        return tableModel.getStringRepresentation();
    }

    public void setText(String newText) {
        tableModel.setContent(newText);
    }


    public void append(String text) {

    }

    public void updateFont() {

    }

    public void paste(String textToInsert) {

    }

    public String getSelectedText() {
        return null;
    }

    private void addEntry() {
        int row = getSelectedRow();
        if (row == -1)
            row = 0;
        FileListEntry entry = new FileListEntry("", "", null);
        if (editListEntry(entry))
            tableModel.addEntry(row, entry);
        entryEditor.updateField(this);
    }

    private void removeEntries() {
        int[] rows = getSelectedRows();
        if (rows != null)
            for (int i = rows.length-1; i>=0; i--) {
                tableModel.removeEntry(rows[i]);
            }
        entryEditor.updateField(this);
    }

    private void moveEntry(int i) {
        int[] sel = getSelectedRows();
        if ((sel.length != 1) || (tableModel.getRowCount() < 2))
            return;
        int toIdx = sel[0]+i;
        if (toIdx >= tableModel.getRowCount())
            toIdx -= tableModel.getRowCount();
        if (toIdx < 0)
            toIdx += tableModel.getRowCount();
        FileListEntry entry = tableModel.getEntry(sel[0]);
        tableModel.removeEntry(sel[0]);
        tableModel.addEntry(toIdx, entry);
        entryEditor.updateField(this);
        setRowSelectionInterval(toIdx, toIdx);
    }

    private boolean editListEntry(FileListEntry entry) {
        if (editor == null) {
            editor = new FileListEntryEditor(frame, entry, false, metaData);
        }
        else
            editor.setEntry(entry);
        editor.setVisible(true);
        if (editor.okPressed())
            tableModel.fireTableDataChanged();
        entryEditor.updateField(this);
        return editor.okPressed();
    }

    private void autoSetLinks() {
        auto.setEnabled(false);
        BibtexEntry entry = entryEditor.getEntry();
        int tableSize = tableModel.getRowCount();
        JDialog diag = new JDialog(frame, true);
        autoSetLinks(entry, tableModel, metaData, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                auto.setEnabled(true);
                if (e.getID() > 0) {
                    entryEditor.updateField(FileListEditor.this);
                    frame.output(Globals.lang("Finished autosetting external links."));
                }
                else frame.output(Globals.lang("Finished autosetting external links.")
                    +" "+Globals.lang("No files found."));
            }
        }, diag);

    }

    /**
     * Automatically add links for this set of entries, based on the globally stored list of
     * external file types. The entries are modified, and corresponding UndoEdit elements
     * added to the NamedCompound given as argument. Furthermore, all entries which are modified
     * are added to the Set of entries given as an argument.
     *
     * The entries' bibtex keys must have been set - entries lacking key are ignored.
     * The operation is done in a new thread, which is returned for the caller to wait for
     * if needed.
     *
     * @param entries A collection of BibtexEntry objects to find links for.
     * @param ce A NamedCompound to add UndoEdit elements to.
     * @param changedEntries A Set of BibtexEntry objects to which all modified entries is added.
     * @return the thread performing the autosetting
     */
    public static Thread autoSetLinks(final Collection<BibtexEntry> entries, final NamedCompound ce,
                                      final Set<BibtexEntry> changedEntries) {

        final ExternalFileType[] types = Globals.prefs.getExternalFileTypeSelection();
        final JLabel label = new JLabel(Globals.lang("Searching for files"));
        Runnable r = new Runnable() {

            public void run() {
                boolean foundAny = false;
                ExternalFileType[] types = Globals.prefs.getExternalFileTypeSelection();
                ArrayList<File> dirs = new ArrayList<File>();
                if (Globals.prefs.hasKey(GUIGlobals.FILE_FIELD + "Directory"))
                    dirs.add(new File(Globals.prefs.get(GUIGlobals.FILE_FIELD + "Directory")));
                Collection<String> extensions = new ArrayList<String>();
                for (int i = 0; i < types.length; i++) {
                    final ExternalFileType type = types[i];
                    extensions.add(type.getExtension());
                }
                // Run the search operation:
                Map<BibtexEntry, java.util.List<File>> result =
                        Util.findAssociatedFiles(entries, extensions, dirs);

                // Iterate over the entries:
                for (Iterator<BibtexEntry> i=result.keySet().iterator(); i.hasNext();) {
                    BibtexEntry anEntry = i.next();
                    FileListTableModel tableModel = new FileListTableModel();
                    Object oldVal = anEntry.getField(GUIGlobals.FILE_FIELD);
                    if (oldVal != null)
                        tableModel.setContent((String)oldVal);
                    List<File> files = result.get(anEntry);
                    for (File f : files) {
			f = relativizePath(f, dirs);
                        boolean alreadyHas = false;
			//System.out.println("File: "+f.getPath());
                        for (int j = 0; j < tableModel.getRowCount(); j++) {
                            FileListEntry existingEntry = tableModel.getEntry(j);
			    //System.out.println("Comp: "+existingEntry.getLink());
			    if (new File(existingEntry.getLink()).equals(f)) {
                                alreadyHas = true;
                                break;
                            }
                        }
                        if (!alreadyHas) {
                            int index = f.getPath().lastIndexOf('.');
                            if ((index >= 0) && (index < f.getPath().length()-1)) {
                                ExternalFileType type = Globals.prefs.getExternalFileTypeByExt
                                    (f.getPath().substring(index+1));
                                FileListEntry flEntry = new FileListEntry(f.getName(), f.getPath(), type);
                                tableModel.addEntry(tableModel.getRowCount(), flEntry);
                            } else {
                                FileListEntry flEntry = new FileListEntry(f.getName(), f.getPath(),
                                        new UnknownExternalFileType(""));
                                tableModel.addEntry(tableModel.getRowCount(), flEntry);
                            }
                            String newVal = tableModel.getStringRepresentation();
                            if (newVal.length() == 0)
                                newVal = null;
                            UndoableFieldChange change = new UndoableFieldChange(anEntry,
                                    GUIGlobals.FILE_FIELD, oldVal, newVal);
                            ce.addEdit(change);
                            anEntry.setField(GUIGlobals.FILE_FIELD, newVal);
                            changedEntries.add(anEntry);
                        }
                    }
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
        return t;
    }


    /**
     * Automatically add links for this entry to the table model given as an argument, based on
     * the globally stored list of external file types. The entry itself is not modified. The entry's
     * bibtex key must have been set.
     * The operation is done in a new thread, which is returned for the caller to wait for
     * if needed.
     *
     * @param entry The BibtexEntry to find links for.
     * @param tableModel The table model to insert links into. Already existing links are not duplicated or removed.
     * @param metaData The MetaData providing the relevant file directory, if any.
     * @param callback An ActionListener that is notified (on the event dispatch thread) when the search is
     *  finished. The ActionEvent has id=0 if no new links were added, and id=1 if one or more links were added.
     *  This parameter can be null, which means that no callback will be notified.
     * @param diag An instantiated modal JDialog which will be used to display the progress of the autosetting.
     *      This parameter can be null, which means that no progress update will be shown.
     * @return the thread performing the autosetting
     */
    public static Thread autoSetLinks(final BibtexEntry entry, final FileListTableModel tableModel,
                                      final MetaData metaData, final ActionListener callback,
                                      final JDialog diag) {

        final Collection<BibtexEntry> entries = new ArrayList<BibtexEntry>();
        entries.add(entry);
        final ExternalFileType[] types = Globals.prefs.getExternalFileTypeSelection();
        final JProgressBar prog = new JProgressBar(JProgressBar.HORIZONTAL, types.length-1);
        prog.setIndeterminate(true);
        prog.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        final JLabel label = new JLabel(Globals.lang("Searching for files"));
        if (diag != null) {
            diag.setTitle(Globals.lang("Autosetting links"));
            diag.getContentPane().add(prog, BorderLayout.CENTER);
            diag.getContentPane().add(label, BorderLayout.SOUTH);

            diag.pack();
            diag.setLocationRelativeTo(diag.getParent());
        }
        Runnable r = new Runnable() {

            public void run() {
                boolean foundAny = false;
                ExternalFileType[] types = Globals.prefs.getExternalFileTypeSelection();
                ArrayList<File> dirs = new ArrayList<File>();
                if (metaData.getFileDirectory(GUIGlobals.FILE_FIELD) != null)
                    dirs.add(new File(metaData.getFileDirectory(GUIGlobals.FILE_FIELD)));
                Collection<String> extensions = new ArrayList<String>();
                for (int i = 0; i < types.length; i++) {
                    final ExternalFileType type = types[i];
                    extensions.add(type.getExtension());
                }
                // Run the search operation:
                Map<BibtexEntry, java.util.List<File>> result =
                        Util.findAssociatedFiles(entries, extensions, dirs);

                // Iterate over the entries:
                for (Iterator<BibtexEntry> i=result.keySet().iterator(); i.hasNext();) {
                    BibtexEntry anEntry = i.next();
                    List<File> files = result.get(anEntry);
                    for (File f : files) {
			        f = relativizePath(f, dirs);
                        boolean alreadyHas = false;
                        for (int j = 0; j < tableModel.getRowCount(); j++) {
                            FileListEntry existingEntry = tableModel.getEntry(j);
                            if (new File(existingEntry.getLink()).equals(f)) {
                                alreadyHas = true;
                                break;
                            }
                        }
                        if (!alreadyHas) {
                            int index = f.getPath().lastIndexOf('.');
                            if ((index >= 0) && (index < f.getPath().length()-1)) {
                                ExternalFileType type = Globals.prefs.getExternalFileTypeByExt
                                    (f.getPath().substring(index+1));
                                FileListEntry flEntry = new FileListEntry(f.getName(), f.getPath(), type);
                                tableModel.addEntry(tableModel.getRowCount(), flEntry);
                                foundAny = true;
                            } else {
                                FileListEntry flEntry = new FileListEntry(f.getName(), f.getPath(),
                                        new UnknownExternalFileType(""));
                                tableModel.addEntry(tableModel.getRowCount(), flEntry);
                                foundAny = true;
                            }
                        }
                    }
                }
                final int id = foundAny ? 1 : 0;
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (diag != null)
                            diag.dispose();
                        if (callback != null)
                            callback.actionPerformed(new ActionEvent(this, id, ""));
                    }
                });

            }
        };
        Thread t = new Thread(r);
        t.start();
        if (diag != null) {
            diag.setVisible(true);
        }
        return t;
    }

    /**
     * If the file is below one of the directories in a list, return a File specifying
     * a path relative to that directory.
     */
    public static File relativizePath(File f, ArrayList<File> dirs) {
	String pth = f.getPath();
	for (File dir : dirs) {
	    if (pth.startsWith(dir.getPath())) {
		String subs = pth.substring(dir.getPath().length());
		if ((subs.length() > 0) && ((subs.charAt(0) == '/') || (subs.charAt(0) == '\\')))
		    subs = subs.substring(1);
	    return new File(subs);	    
	    }
	}
	return f;
    }


    /**
     * Run a file download operation.
     */
    private void downloadFile() {
        String bibtexKey = entryEditor.getEntry().getCiteKey();
        if (bibtexKey == null) {
            int answer = JOptionPane.showConfirmDialog(frame,
                    Globals.lang("This entry has no BibTeX key. Generate key now?"),
                    Globals.lang("Download file"), JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (answer == JOptionPane.OK_OPTION) {
                ActionListener l = entryEditor.generateKeyAction;
                l.actionPerformed(null);
                bibtexKey = entryEditor.getEntry().getCiteKey();
            }
        }
        DownloadExternalFile def = new DownloadExternalFile(frame,
                frame.basePanel().metaData(), bibtexKey);
        try {
            def.download(this);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This is the callback method that the DownloadExternalFile class uses to report the result
     * of a download operation. This call may never come, if the user cancelled the operation.
     * @param file The FileListEntry linking to the resulting local file.
     */
    public void downloadComplete(FileListEntry file) {
        tableModel.addEntry(tableModel.getRowCount(), file);
        entryEditor.updateField(this);
    }

    class TableClickListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if ((e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 2)) {
                int row = rowAtPoint(e.getPoint());
                if (row >= 0) {
                    FileListEntry entry = tableModel.getEntry(row);
                    editListEntry(entry);
                }
            }
        }
    }


}
