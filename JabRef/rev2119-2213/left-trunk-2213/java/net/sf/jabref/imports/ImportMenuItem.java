package net.sf.jabref.imports;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import net.sf.jabref.*;
import net.sf.jabref.gui.ImportInspectionDialog;
import net.sf.jabref.labelPattern.LabelPatternUtil;
import net.sf.jabref.undo.NamedCompound;
import net.sf.jabref.undo.UndoableInsertEntry;
import net.sf.jabref.undo.UndoableRemoveEntry;


public class ImportMenuItem extends JMenuItem implements ActionListener {

    JabRefFrame frame;
    boolean openInNew;
    MyWorker worker = null;
    ImportFormat importer;

    public ImportMenuItem(JabRefFrame frame, boolean openInNew) {
        this(frame, openInNew, null);
    }

    public ImportMenuItem(JabRefFrame frame, boolean openInNew, ImportFormat importer) {
        super(importer != null ? importer.getFormatName()
                : Globals.lang("Autodetect format"));
        this.importer = importer;
        this.frame = frame;
        this.openInNew = openInNew;
        addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        worker = new MyWorker();
        worker.init();
        worker.getWorker().run();
        worker.getCallBack().update();
    }
    
    
    public void automatedImport(String filenames[]) {
        
        MyWorker worker = new MyWorker();
        worker.fileOk = true;
        worker.filenames = filenames;
        
        worker.getWorker().run();
        worker.getCallBack().update();
    }
    

    class MyWorker extends AbstractWorker implements ImportInspectionDialog.CallBack {
        String[] filenames = null, formatName = null;
        ParserResult bibtexResult = null; 
        boolean fileOk = false;

        public void init() {
            filenames = Globals.getMultipleFiles(frame,
                    new File(Globals.prefs.get("workingDirectory")),
                    (importer != null ? importer.getExtensions() : null), true);

            
            if ((filenames != null) && (filenames.length > 0)) {
                frame.block();
                frame.output(Globals.lang("Starting import"));
                
                fileOk = true;
                
                Globals.prefs.put("workingDirectory", filenames[0]);
            }
        }

        public void run() {
            if (!fileOk)
                return;

            
            List imports = new ArrayList();
            for (int i = 0; i < filenames.length; i++) {
                String filename = filenames[i];
                if (importer != null)
                    
                    try {
                        imports.add(new Object[] { importer.getFormatName(),
                                Globals.importFormatReader.importFromFile(importer, filename)});
                    } catch (IOException e) {
                        
                    }
                else
                    
                    imports.add(Globals.importFormatReader.importUnknownFormat(filename));
            }

            
            
            bibtexResult = mergeImportResults(imports);
        }

        public void update() {
            if (!fileOk)
                return;

            
            
            if (bibtexResult != null) {
                if (!openInNew) {
                    final BasePanel panel = (BasePanel) frame.getTabbedPane().getSelectedComponent();
                    BibtexDatabase toAddTo = panel.database();
                    
                    
                    
                    if (Globals.prefs.getBoolean("useImportInspectionDialog") &&
                            (Globals.prefs.getBoolean("useImportInspectionDialogForSingle")
                                    || (bibtexResult.getDatabase().getEntryCount() > 1))) {
                        ImportInspectionDialog diag = new ImportInspectionDialog(frame, panel,
                                BibtexFields.DEFAULT_INSPECTION_FIELDS,
                                Globals.lang("Import"), openInNew);
                        diag.addEntries(bibtexResult.getDatabase().getEntries());
                        diag.addCallBack(this);
                        diag.entryListComplete();
                        Util.placeDialog(diag, frame);
                        diag.setVisible(true);
                        diag.toFront();
                    } else {
                        boolean generateKeys = Globals.prefs.getBoolean("generateKeysAfterInspection");
                        NamedCompound ce = new NamedCompound(Globals.lang("Import entries"));
                        for (Iterator i = bibtexResult.getDatabase().getEntries().iterator();
                             i.hasNext();) {
                            BibtexEntry entry = (BibtexEntry) i.next();
                            try {
                                
                                boolean keepEntry = true;
                                BibtexEntry duplicate = Util.containsDuplicate(toAddTo, entry);
                                if (duplicate != null) {
                                    int answer = DuplicateResolverDialog.resolveDuplicateInImport
                                            (frame, duplicate, entry);
                                    
                                    if (answer == DuplicateResolverDialog.DO_NOT_IMPORT)
                                        keepEntry = false;
                                    if (answer == DuplicateResolverDialog.IMPORT_AND_DELETE_OLD) {
                                        
                                        toAddTo.removeEntry(duplicate.getId());
                                        ce.addEdit(new UndoableRemoveEntry(toAddTo, duplicate, panel));
                                    }
                                }
                                
                                if (keepEntry) {
                                    toAddTo.insertEntry(entry);
                                    
                                    if (generateKeys) {
                                        LabelPatternUtil.makeLabel(Globals.prefs.getKeyPattern(), toAddTo, entry);
                                        
                                    }
                                    
                                    Util.updateCompletersForEntry(panel.getAutoCompleters(), entry);

                                    ce.addEdit(new UndoableInsertEntry(toAddTo, entry, panel));
                                }
                            } catch (KeyCollisionException e) {
                                e.printStackTrace();
                            }
                        }
                        ce.end();
                        panel.undoManager.addEdit(ce);
                    }

                }

                else {
                    frame.addTab(bibtexResult.getDatabase(), bibtexResult.getFile(),
                            bibtexResult.getMetaData(), Globals.prefs.get("defaultEncoding"), true);
                    done(bibtexResult.getDatabase().getEntryCount());
                }


            } else {
                if (importer == null)
                    frame.output(Globals.lang("Could not find a suitable import format."));
                else
                    JOptionPane.showMessageDialog(frame, Globals.lang("No entries found. Please make sure you are "
								  +"using the correct import filter."), Globals.lang("Import failed"),
					      JOptionPane.ERROR_MESSAGE);
            }
            frame.unblock();
        }

        public void done(int entriesImported) {

            
            frame.output(Globals.lang("Imported entries") + ": " + entriesImported);

        }

        public void cancelled() {
            frame.output(Globals.lang("Import cancelled."));
        }


        
        
        
        public void stopFetching() {
            
        }
    }



    public static ParserResult mergeImportResults(List imports) {
        BibtexDatabase database = new BibtexDatabase();
        ParserResult directParserResult = null;
        boolean anythingUseful = false;

        for (Iterator iterator = imports.iterator(); iterator.hasNext();) {
            Object[] o = (Object[]) iterator.next();
            if (o[1] instanceof List) {
                List entries = (List) o[1];
                anythingUseful = anythingUseful | (entries.size() > 0);
                Util.setAutomaticFields(entries); 
                for (Iterator j = entries.iterator(); j.hasNext();) {
                    BibtexEntry entry = (BibtexEntry) j.next();
                    try {
                        entry.setId(Util.createNeutralId());
                        database.insertEntry(entry);
                    } catch (KeyCollisionException e) {
                        e.printStackTrace();
                    }
                }
            } else if (o[1] instanceof ParserResult) {
                
                ParserResult pr = (ParserResult) o[1];

                anythingUseful = anythingUseful
                        || ((pr.getDatabase().getEntryCount() > 0) || (pr.getDatabase().getStringCount() > 0));

                
                if (directParserResult == null) {
                    directParserResult = pr;
                }

                
                for (Iterator j = pr.getDatabase().getEntries().iterator(); j.hasNext();) {
                    BibtexEntry entry = (BibtexEntry) j.next();
                    try {
                        database.insertEntry(entry);
                    } catch (KeyCollisionException e) {
                        e.printStackTrace(); 
                    }
                }
                
                for (Iterator j = pr.getDatabase().getStringKeySet().iterator(); j.hasNext();) {
                    BibtexString bs = (BibtexString)
                            (pr.getDatabase().getString(j.next()).clone());

                    try {
                        database.addString(bs);
                    } catch (KeyCollisionException e) {
                        
                        
                    }
                }

            }
        }

        if (!anythingUseful)
            return null;

        if ((imports.size() == 1) && (directParserResult != null)) {
            return directParserResult;
        } else {

            ParserResult pr = new ParserResult(database, new HashMap(), new HashMap());
            return pr;

        }
    }

}
