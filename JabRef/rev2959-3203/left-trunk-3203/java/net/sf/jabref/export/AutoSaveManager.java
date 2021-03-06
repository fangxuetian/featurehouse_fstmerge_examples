package net.sf.jabref.export;

import net.sf.jabref.JabRefFrame;
import net.sf.jabref.BasePanel;
import net.sf.jabref.Globals;

import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import java.util.ArrayList;
import java.io.File;


public class AutoSaveManager {
    private JabRefFrame frame;
    private Timer t = null;
    private TimerTask task = null;

    public AutoSaveManager(JabRefFrame frame) {

        this.frame = frame;
    }

    public void startAutoSaveTimer() {
        task = new AutoSaveTask();
        t = new Timer();
        long interval = (long)(60000*Globals.prefs.getInt("autoSaveInterval"));
        t.scheduleAtFixedRate(task, interval, interval);
    }

    public void stopAutoSaveTimer() {
        t.cancel();
    }

    class AutoSaveTask extends TimerTask {
        public void run() {
            
            

            List<BasePanel> panels = new ArrayList<BasePanel>();
            for (int i=0; i<frame.baseCount(); i++)
                panels.add(frame.baseAt(i));

            int i=0;
            for (BasePanel panel : panels) {
                if (panel.isBaseChanged()) {
                    if (panel.getFile() != null) {
                        autoSave(panel);
                    }
                }
                else {
                }
                i++;
            }
        }
    }

    
    public static File getAutoSaveFile(File f) {
        String n = f.getName();
        return new File(f.getParentFile(), ".$"+n+"$");
    }

    
    public static boolean autoSave(BasePanel panel) {
        File backupFile = getAutoSaveFile(panel.getFile());
        try {
            SaveSession ss = FileActions.saveDatabase(panel.database(), panel.metaData(),
                    backupFile, Globals.prefs,
                    false, false, panel.getEncoding(), true);
            ss.commit();
        } catch (SaveException e) {
            e.printStackTrace();
            return false;
        } catch (Throwable ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    
    public static boolean deleteAutoSaveFile(BasePanel panel) {
        if (panel.getFile() == null)
            return true;
        File backupFile = getAutoSaveFile(panel.getFile());
        if (backupFile.exists()) {
            return backupFile.delete();
        }
        else return true;
    }

    
    public void clearAutoSaves() {
        List<BasePanel> panels = new ArrayList<BasePanel>();
        for (int i=0; i<frame.baseCount(); i++)
            panels.add(frame.baseAt(i));
        for (BasePanel panel : panels) {
            deleteAutoSaveFile(panel);
        }
    }

    
    public static boolean newerAutoSaveExists(File f) {
        File asFile = getAutoSaveFile(f);
        return asFile.exists() && (asFile.lastModified() > f.lastModified());
    }
}
