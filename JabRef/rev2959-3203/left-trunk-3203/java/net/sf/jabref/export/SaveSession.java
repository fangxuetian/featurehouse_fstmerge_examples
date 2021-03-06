package net.sf.jabref.export;

import net.sf.jabref.Globals;
import net.sf.jabref.Util;
import net.sf.jabref.GUIGlobals;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.nio.charset.UnsupportedCharsetException;


public class SaveSession {

    public static final String LOCKFILE_SUFFIX = ".lock";
    
    public static final long LOCKFILE_CRITICAL_AGE = 60000;

    private static final String TEMP_PREFIX = "jabref";
    private static final String TEMP_SUFFIX = "save.bib";

    File file, tmp, backupFile;
    String encoding;
    boolean backup, useLockFile;
    VerifyingWriter writer;

    public SaveSession(File file, String encoding, boolean backup) throws IOException,
        UnsupportedCharsetException {
        this.file = file;
        tmp = File.createTempFile(TEMP_PREFIX, TEMP_SUFFIX);
        useLockFile = Globals.prefs.getBoolean("useLockFiles");
        this.backup = backup;
        this.encoding = encoding;
        writer = new VerifyingWriter(new FileOutputStream(tmp), encoding);
    }

    public VerifyingWriter getWriter() {
        return writer;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setUseBackup(boolean useBackup) {
        this.backup = useBackup;
    }

    public void commit() throws SaveException {
        if (file == null)
            return;
        if (file.exists() && backup) {
            String name = file.getName();
            String path = file.getParent();
            File backupFile = new File(path, name + GUIGlobals.backupExt);
            try {
                Util.copyFile(file, backupFile, true);
            } catch (IOException ex) {
                ex.printStackTrace();
                throw SaveException.BACKUP_CREATION;
                
            }
        }
        try {
            if (useLockFile) {
                try {
                    if (createLockFile()) {
                        
                        if (!Util.waitForFileLock(file, 10))
                            throw SaveException.FILE_LOCKED;

                    }
                } catch (IOException ex) {
                    System.err.println("Error when creating lock file");
                    ex.printStackTrace();
                }
            }

            Util.copyFile(tmp, file, true);
        } catch (IOException ex2) {
            
            
            
            
            throw new SaveException(Globals.lang("Save failed while committing changes")+": "+ex2.getMessage());
        } finally {
            if (useLockFile) {
                try {
                    deleteLockFile();
                } catch (IOException ex) {
                    System.err.println("Error when deleting lock file");
                    ex.printStackTrace();
                }
            }
        }

        tmp.delete();
    }

    public void cancel() throws IOException {
        tmp.delete();
    }


    
    private boolean createLockFile() throws IOException {
        File lock = new File(file.getPath()+LOCKFILE_SUFFIX);
        if (lock.exists()) {
            return true;
        }
        FileOutputStream out = new FileOutputStream(lock);
        out.write(0);
        try {
            out.close();
        } catch (IOException ex) {
            System.err.println("Error when creating lock file");
            ex.printStackTrace();
        }
        lock.deleteOnExit();
        return false;
    }

    
    private boolean deleteLockFile() throws IOException {
        File lock = new File(file.getPath()+LOCKFILE_SUFFIX);
        if (!lock.exists()) {
            return false;
        }
        lock.delete();
        return true;
    }

    public File getTemporaryFile() {
        return tmp;
    }
}
