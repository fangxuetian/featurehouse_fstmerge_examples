package net.sf.jabref.external;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.sf.jabref.*;
import net.sf.jabref.gui.FileListEntry;
import net.sf.jabref.gui.FileListEntryEditor;
import net.sf.jabref.net.URLDownload;


public class DownloadExternalFile {
    private JabRefFrame frame;
    private JDialog dialog;
    private MetaData metaData;
    private String bibtexKey;
    private FileListEntryEditor editor;
    private boolean downloadFinished = false;

    public DownloadExternalFile(JabRefFrame frame, MetaData metaData, String bibtexKey) {

        this.frame = frame;
        this.metaData = metaData;
        this.bibtexKey = bibtexKey;
    }

    
    public void download(final DownloadCallback callback) throws IOException {

        final String res = JOptionPane.showInputDialog(frame,
                Globals.lang("Enter URL to download"));

        if (res == null || res.trim().length() == 0)
            return;

        
        final File tmp = File.createTempFile("jabref_download", "tmp");
        tmp.deleteOnExit();
        (new Thread() {
            public void run() {

                try {

                    URL url = new URL(res);
                    URLDownload udl = new URLDownload(frame, url, tmp);
                    try {
                        udl.download();
                    } catch (IOException e2) {
                        JOptionPane.showMessageDialog(frame, Globals.lang("Invalid URL: "
                                + e2.getMessage()), Globals.lang("Download file"),
                                JOptionPane.ERROR_MESSAGE);
                        Globals.logger("Error while downloading " + url.toString());
                        return;
                    }

                    
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            downloadFinished();
                        }
                    });


                } catch (MalformedURLException e1) {
                    JOptionPane.showMessageDialog(frame, Globals.lang("Invalid URL"), Globals
                            .lang("Download file"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();

        
        String suffix = getSuffix(res);
        String suggestedName = bibtexKey != null ? getSuggestedFileName(res, suffix) : "";
        final String directory = getFileDirectory(res);
        File file = new File(new File(directory), suggestedName);
        FileListEntry entry = new FileListEntry("", bibtexKey != null ? file.getPath() : "",
                Globals.prefs.getExternalFileTypeByExt(suffix));
        editor = new FileListEntryEditor(frame, entry, true, metaData);
        editor.getProgressBar().setIndeterminate(true);
        editor.setOkEnabled(false);
        editor.setExternalConfirm(new ConfirmCloseFileListEntryEditor() {
            public boolean confirmClose(FileListEntry entry) {
                File f = expandFilename(directory, entry.getLink());
                if (f.isDirectory()) {
                    JOptionPane.showMessageDialog(frame,
                            Globals.lang("Target file cannot be a directory."), Globals.lang("Download file"),
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (f.exists()) {
                    return JOptionPane.showConfirmDialog
                        (frame, "'"+f.getName()+"' "+Globals.lang("exists. Overwrite file?"),
                        Globals.lang("Download file"), JOptionPane.OK_CANCEL_OPTION)
                            == JOptionPane.OK_OPTION;
                } else
                    return true;
            }
        });
        editor.setVisible(true);
        
        if (editor.okPressed()) {
            String dirPrefix = directory+System.getProperty("file.separator");
            File toFile = expandFilename(directory, entry.getLink());
            try {
                boolean success = Util.copyFile(tmp, toFile, true);
                if (!success) {
                    
                    System.out.println("File already exists! DownloadExternalFile.download()");
                }

                
                
                if (entry.getLink().startsWith(directory) &&
                        (entry.getLink().length() > dirPrefix.length())) {
                    entry.setLink(entry.getLink().substring(dirPrefix.length()));
                }

                callback.downloadComplete(entry);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            tmp.delete();
        }
        else {
            
            if (downloadFinished)
                tmp.delete();
        }

    }

    
    private File expandFilename(String directory, String link) {
        File toFile = new File(link);
        
        String dirPrefix = directory+System.getProperty("file.separator");
        if (!toFile.isAbsolute()) {
            toFile = new File(dirPrefix+link);
        }
        return toFile;
    }

    
    public void downloadFinished() {
        downloadFinished = true;
        editor.getProgressBar().setVisible(false);
        editor.getProgressBarLabel().setVisible(false);
        editor.setOkEnabled(true);
        editor.getProgressBar().setValue(editor.getProgressBar().getMaximum());
    }

    public String getSuggestedFileName(String res, String suffix) {
        if (suffix == null) {
            System.out.println("Link has no obvious extension (DownloadExternalFile.download()");
        }

        String plannedName = bibtexKey + "." + suffix;

        
        if (Globals.ON_WIN) {
            plannedName = plannedName.replaceAll(
                    "\\?|\\*|\\<|\\>|\\||\\\"|\\:|\\.$|\\[|\\]", "");
        } else if (Globals.ON_MAC) {
            plannedName = plannedName.replaceAll(":", "");
        }

        return plannedName;
    }

    
    public String getSuffix(String link) {
        int index = link.lastIndexOf('.');
        if ((index <= 0) || (index == link.length() - 1)) 
            return null;
        return link.substring(index + 1);
    }

    public String getFileDirectory(String link) {
        
        return metaData.getFileDirectory(GUIGlobals.FILE_FIELD);
    }

    
    public interface DownloadCallback {
        public void downloadComplete(FileListEntry file);
    }
}
