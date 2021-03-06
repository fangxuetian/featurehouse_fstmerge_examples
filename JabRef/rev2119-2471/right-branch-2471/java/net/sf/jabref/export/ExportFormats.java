package net.sf.jabref.export;

import net.sf.jabref.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.util.*;
import java.awt.event.ActionEvent;
import java.io.File;


public class ExportFormats {

	private static Map<String,ExportFormat> exportFormats = new TreeMap<String,ExportFormat>();

    public static void initAllExports() {
        exportFormats.clear();

        
        putFormat(new ExportFormat(
                Globals.lang("HTML"), "html", "html", null, ".html"));
        putFormat(new ExportFormat(
                Globals.lang("Simple HTML"), "simplehtml", "simplehtml", null, ".html"));
        putFormat(new ExportFormat(Globals.lang("Docbook"), "docbook", "docbook", null, ".xml"));
        putFormat(new ExportFormat(Globals.lang("BibTeXML"), "bibtexml", "bibtexml", null, ".xml"));
        putFormat(new ModsExportFormat());
        putFormat(new ExportFormat(Globals.lang("HTML table"),
                "tablerefs", "tablerefs", "tablerefs", ".html"));
        putFormat(new ExportFormat(Globals.lang("HTML table (with Abstract & BibTeX)"),
                "tablerefsabsbib", "tablerefsabsbib", "tablerefsabsbib", ".html"));
        putFormat(new ExportFormat(Globals.lang("Harvard RTF"), "harvard", "harvard",
                "harvard", ".rtf"));
        putFormat(new ExportFormat(Globals.lang("MIS Quarterly"), "misq", "misq",
                "misq", ".rtf"));
        putFormat(new ExportFormat(Globals.lang("Endnote"), "endnote", "EndNote",
                "endnote", ".txt"));
        putFormat(new OpenOfficeDocumentCreator());
        putFormat(new OpenDocumentSpreadsheetCreator());
        putFormat(new MSBibExportFormat());
    
        
        TreeMap customExports = Globals.prefs.customExports.getCustomExportFormats();
        for (Iterator i=customExports.keySet().iterator(); i.hasNext();) {
            putFormat((ExportFormat)customExports.get(i.next()));
        }
    }

	
	public static String getConsoleExportList(int maxLineLength, int firstLineSubtr,
		String linePrefix) {
		StringBuffer sb = new StringBuffer();
		int lastBreak = -firstLineSubtr;

		for (Iterator i = exportFormats.keySet().iterator(); i.hasNext();) {
			String name = (String) i.next();
			if (sb.length() + 2 + name.length() - lastBreak > maxLineLength) {
				sb.append(",\n");
				lastBreak = sb.length();
				sb.append(linePrefix);
			} else if (sb.length() > 0)
				sb.append(", ");
			sb.append(name);
		}

		return sb.toString();
	}

    
    public static Map getExportFormats() {
        
        return Collections.unmodifiableMap(exportFormats);
    } 

    
	public static ExportFormat getExportFormat(String consoleName) {
		return (ExportFormat) exportFormats.get(consoleName);
	}

	
	public static AbstractAction getExportAction(JabRefFrame frame, boolean selectedOnly) {

		class ExportAction extends MnemonicAwareAction {

			private static final long serialVersionUID = 639463604530580554L;

			private JabRefFrame frame;

			private boolean selectedOnly;

			public ExportAction(JabRefFrame frame, boolean selectedOnly) {
				this.frame = frame;
				this.selectedOnly = selectedOnly;
				putValue(NAME, selectedOnly ? "Export selected entries" : "Export");
			}

			public void actionPerformed(ActionEvent e) {
				ExportFormats.initAllExports();
				JFileChooser fc = ExportFormats.createExportFileChooser(
                    Globals.prefs.get("exportWorkingDirectory"));
				fc.showSaveDialog(frame);
				File file = fc.getSelectedFile();
				if (file == null)
					return;
				FileFilter ff = fc.getFileFilter();
				if (ff instanceof ExportFileFilter) {

                    ExportFileFilter eff = (ExportFileFilter) ff;
                    String path = file.getPath();
                    if (!path.endsWith(eff.getExportFormat().getExtension()))
                        path = path + eff.getExportFormat().getExtension();
                    file = new File(path);
                    if (file.exists()) {
                        
                        if (JOptionPane.showConfirmDialog(frame, "'" + file.getName() + "' "
                            + Globals.lang("exists. Overwrite file?"), Globals.lang("Export"),
                            JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
                            return;
                    }
                    final ExportFormat format = eff.getExportFormat();
                    Set<String> entryIds = null;
                    if (selectedOnly) {
                        BibtexEntry[] selected = frame.basePanel().getSelectedEntries();
                        entryIds = new HashSet<String>();
                        for (int i = 0; i < selected.length; i++) {
                            BibtexEntry bibtexEntry = selected[i];
                            entryIds.add(bibtexEntry.getId());
                        }
                    }

                    
                    
                    
                    Globals.prefs.fileDirForDatabase = frame.basePanel().metaData()
                            .getFileDirectory(GUIGlobals.FILE_FIELD);                    

                    
                    
                    Globals.prefs.put("lastUsedExport", format.getConsoleName());
                    Globals.prefs.put("exportWorkingDirectory", file.getParent());
                    final File finFile = file;
                    final Set<String> finEntryIDs = entryIds;
                    AbstractWorker exportWorker = new AbstractWorker() {
                        String errorMessage = null;
                        public void run() {
                            try {
                                format.performExport(frame.basePanel().database(), finFile.getPath(), frame
                                    .basePanel().getEncoding(), finEntryIDs);
                            } catch (Exception ex) {
                                
                                errorMessage = ex.getMessage();
                            }
                        }

                        public void update() {
                            
                            if (errorMessage == null) {
                                frame.output(Globals.lang("%0 export successful", format.getDisplayName()));
                            }
                            
                            else {
                                frame.output(Globals.lang("Could not save file")
                                        + " - " + errorMessage);
                                
                                JOptionPane.showMessageDialog(frame, Globals.lang("Could not save file")
                                    + ".\n" + errorMessage, Globals.lang("Save database"),
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    };

                    
                    (exportWorker.getWorker()).run();
                    
                    exportWorker.update();
                }
			}
		}

		return new ExportAction(frame, selectedOnly);
	}

    
    public static JFileChooser createExportFileChooser(String currentDir) {
		String lastUsedFormat = Globals.prefs.get("lastUsedExport");
		FileFilter defaultFilter = null;
		JFileChooser fc = new JFileChooser(currentDir);
		TreeSet<FileFilter> filters = new TreeSet<FileFilter>();
		for (Iterator i = exportFormats.keySet().iterator(); i.hasNext();) {
			String formatName = (String) i.next();
			ExportFormat format = (ExportFormat) exportFormats.get(formatName);
			filters.add(format.getFileFilter());
			if (formatName.equals(lastUsedFormat))
				defaultFilter = format.getFileFilter();
		}
		for (Iterator i = filters.iterator(); i.hasNext();) {
			fc.addChoosableFileFilter((ExportFileFilter) i.next());
		}
		fc.setAcceptAllFileFilterUsed(false);
		if (defaultFilter != null)
			fc.setFileFilter(defaultFilter);
		return fc;
	}

	private static void putFormat(ExportFormat format) {
		exportFormats.put(format.getConsoleName(), format);
	}

}