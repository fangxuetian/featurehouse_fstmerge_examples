package net.sf.jabref.collab;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import net.sf.jabref.*;
import net.sf.jabref.groups.GroupTreeNode;
import net.sf.jabref.imports.OpenDatabaseAction;
import net.sf.jabref.imports.ParserResult;


public class ChangeScanner extends Thread {

    final double MATCH_THRESHOLD = 0.4;
    final String[] sortBy = new String[] {"year", "author", "title" };
    File f;
    BibtexDatabase inMem;
    MetaData mdInMem;
    BasePanel panel;
    JabRefFrame frame;

    
    
    DefaultMutableTreeNode changes = new DefaultMutableTreeNode(Globals.lang("External changes"));

    

    public ChangeScanner(JabRefFrame frame, BasePanel bp) { 
        panel = bp;
        this.frame = frame;
        this.inMem = bp.database();
        this.mdInMem = bp.metaData();
        
        setPriority(Thread.MIN_PRIORITY);

    }

    public void changeScan(File f) {
        this.f = f;
        start();
    }

    public void run() {
        try {
            

            
            File tempFile = Globals.fileUpdateMonitor.getTempFile(panel.fileMonitorHandle());
            ParserResult pr = OpenDatabaseAction.loadDatabase(tempFile,
            Globals.prefs.get("defaultEncoding"));
            BibtexDatabase inTemp = pr.getDatabase();
            MetaData mdInTemp = new MetaData(pr.getMetaData(),inTemp);
            
            pr = OpenDatabaseAction.loadDatabase(f, Globals.prefs.get("defaultEncoding"));
            BibtexDatabase onDisk = pr.getDatabase();
            MetaData mdOnDisk = new MetaData(pr.getMetaData(),onDisk);

            
            EntryComparator comp = new EntryComparator(false, true, sortBy[2]);
            comp = new EntryComparator(false, true, sortBy[1], comp);
            comp = new EntryComparator(false, true, sortBy[0], comp);
            EntrySorter sInTemp = inTemp.getSorter(comp);
            comp = new EntryComparator(false, true, sortBy[2]);
            comp = new EntryComparator(false, true, sortBy[1], comp);
            comp = new EntryComparator(false, true, sortBy[0], comp);
            EntrySorter sOnDisk = onDisk.getSorter(comp);
            comp = new EntryComparator(false, true, sortBy[2]);
            comp = new EntryComparator(false, true, sortBy[1], comp);
            comp = new EntryComparator(false, true, sortBy[0], comp);
            EntrySorter sInMem = inMem.getSorter(comp);

            

            scanPreamble(inMem, inTemp, onDisk);
            scanStrings(inMem, inTemp, onDisk);


            scanEntries(sInMem, sInTemp, sOnDisk);
            
            scanGroups(mdInMem, mdInTemp, mdOnDisk);


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean changesFound() {
        return changes.getChildCount() > 0;
    }

    public void displayResult() {
        if (changes.getChildCount() > 0) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ChangeDisplayDialog dial = new ChangeDisplayDialog(frame, panel, changes);
                    Util.placeDialog(dial, frame);
                    dial.setVisible(true); 
                }
            });

        } else {
            JOptionPane.showMessageDialog(frame, Globals.lang("No actual changes found."),
            Globals.lang("External changes"), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void scanEntries(EntrySorter mem, EntrySorter tmp, EntrySorter disk) {

        
        
        
        int piv1 = 0, piv2 = 0;

        
        
        HashSet<String> used = new HashSet<String>(disk.getEntryCount());
        HashSet<Integer> notMatched = new HashSet<Integer>(tmp.getEntryCount());

        
        
        
        mainLoop: for (piv1=0; piv1<tmp.getEntryCount(); piv1++) {

            
            double comp = -1;
            
            
            if (!used.contains(""+piv2) && (piv2<disk.getEntryCount())) {
                comp = DuplicateCheck.compareEntriesStrictly(tmp.getEntryAt(piv1), disk.getEntryAt(piv2));
            }
            if (comp > 1) {
                used.add(""+piv2);
                piv2++;
                continue mainLoop;
            }

            
            if (piv2 < disk.getEntryCount()-1) {
                for (int i = piv2+1; i < disk.getEntryCount(); i++) {
                    if (!used.contains(""+i))
                        comp = DuplicateCheck.compareEntriesStrictly(tmp.getEntryAt(piv1), disk.getEntryAt(i));
                    else
                        comp = -1;

                    if (comp > 1) {
                        used.add("" + i);
                        continue mainLoop;
                    }
                }
            }

            
            notMatched.add(new Integer(piv1));
        }


        
        
        if (notMatched.size() > 0) {

            for (Iterator<Integer> it=notMatched.iterator(); it.hasNext();) {

                Integer integ = it.next();
                piv1 = integ.intValue();


                
                
                int bestMatchI = -1;
                double bestMatch = 0;
                double comp = -1;

                if (piv2 < disk.getEntryCount()-1) {
                    for (int i = piv2; i < disk.getEntryCount(); i++) {
                        if (!used.contains(""+i)) {
                            comp = DuplicateCheck.compareEntriesStrictly(tmp.getEntryAt(piv1),
                            disk.getEntryAt(i));
                        }
                        else
                            comp = -1;

                        if (comp > bestMatch) {
                            bestMatch = comp;
                            bestMatchI = i;
                        }
                    }
                }

                if (bestMatch > MATCH_THRESHOLD) {
                    used.add(""+bestMatchI);
                    it.remove();

                    EntryChange ec = new EntryChange(bestFit(tmp, mem, piv1), tmp.getEntryAt(piv1),
                    disk.getEntryAt(bestMatchI));
                    changes.add(ec);

                    
                    
                    
                    
                    
                    

                    
                    

                }
                else {
                    EntryDeleteChange ec = new EntryDeleteChange(bestFit(tmp, mem, piv1), tmp.getEntryAt(piv1));
                    changes.add(ec);
          

                }

            }

        }

        
        
        if (used.size() < disk.getEntryCount()) {
            for (int i=0; i<disk.getEntryCount(); i++) {
                if (!used.contains(""+i)) {

                    
                    boolean hasAlready = false;
                    for (int j = 0; j < mem.getEntryCount(); j++) {
                        if (DuplicateCheck.compareEntriesStrictly(mem.getEntryAt(j),
                            disk.getEntryAt(i)) >= 1) {
                            hasAlready = true;
                            break;
                        }
                    }
                    if (!hasAlready) {
                        EntryAddChange ec = new EntryAddChange(disk.getEntryAt(i));
                        changes.add(ec);
                    }
          
                }
            }
            
        }
    }

    
    private BibtexEntry bestFit(EntrySorter old, EntrySorter neu, int index) {
        double comp = -1;
        int found = 0;
        loop: for (int i=0; i<neu.getEntryCount(); i++) {
            double res = DuplicateCheck.compareEntriesStrictly(old.getEntryAt(index),
            neu.getEntryAt(i));
            if (res > comp) {
                comp = res;
                found = i;
            }
            if (comp > 1)
                break loop;
        }
        return neu.getEntryAt(found);
    }

    private void scanPreamble(BibtexDatabase inMem, BibtexDatabase onTmp, BibtexDatabase onDisk) {
        String mem = inMem.getPreamble(),
        tmp = onTmp.getPreamble(),
        disk = onDisk.getPreamble();
        if (tmp != null) {
            if ((disk == null) || !tmp.equals(disk))
                changes.add(new PreambleChange(tmp, mem, disk));
        }
        else if ((disk != null) && !disk.equals("")) {
            changes.add(new PreambleChange(tmp, mem, disk));
        }
    }

    private void scanStrings(BibtexDatabase inMem, BibtexDatabase onTmp, BibtexDatabase onDisk) {
        int nTmp = onTmp.getStringCount(),
        nDisk = onDisk.getStringCount();
        if ((nTmp == 0) && (nDisk == 0))
            return;

        HashSet<Object> used = new HashSet<Object>();
        HashSet<Object> usedInMem = new HashSet<Object>();
        HashSet<String> notMatched = new HashSet<String>(onTmp.getStringCount());

        
        
        mainLoop: for (String key : onTmp.getStringKeySet()){
            BibtexString tmp = onTmp.getString(key);

            
            for (String diskId : onDisk.getStringKeySet()){
                if (!used.contains(diskId)) {
                    BibtexString disk = onDisk.getString(diskId);
                    if (disk.getName().equals(tmp.getName())) {
                        
                        if ((tmp.getContent() != null) && !tmp.getContent().equals(disk.getContent())) {
                            
                            BibtexString mem = findString(inMem, tmp.getName(), usedInMem);
                            if (mem != null)
                                changes.add(new StringChange(mem, tmp.getName(),
                                mem.getContent(),
                                tmp.getContent(), disk.getContent()));
                            else
                                changes.add(new StringChange(null, tmp.getName(), null, tmp.getContent(), disk.getContent()));
                        }
                        used.add(diskId);
                        
                        
                        continue mainLoop;
                    }

                }
            }
            
            notMatched.add(tmp.getId());
        }

        
        if (notMatched.size() > 0) {
            for (Iterator<String> i = notMatched.iterator(); i.hasNext();){
                BibtexString tmp = onTmp.getString(i.next());

                
                
                for (String diskId : onDisk.getStringKeySet()){

                	if (!used.contains(diskId)) {
                        BibtexString disk = onDisk.getString(diskId);

                        if (disk.getContent().equals(tmp.getContent())) {
                            
                            

                            
                            BibtexString bsMem = null;
                            
                            for (String memId : inMem.getStringKeySet()){
                                BibtexString bsMem_cand = inMem.getString(memId);
                                if (bsMem_cand.getContent().equals(disk.getContent()) &&
                                !usedInMem.contains(memId)) {
                                    usedInMem.add(memId);
                                    bsMem = bsMem_cand;
                                    break;
                                }
                            }

                            changes.add(new StringNameChange(bsMem, bsMem.getName(),
                            tmp.getName(), disk.getName(),
                            tmp.getContent()));
                            i.remove();
                            used.add(diskId);

                        }
                    }
                }
            }
        }

        if (notMatched.size() > 0) {
            
            for (Iterator<String> i = notMatched.iterator(); i.hasNext(); ) {
                String nmId = i.next();
                BibtexString tmp = onTmp.getString(nmId);
                BibtexString mem = findString(inMem, tmp.getName(), usedInMem);
                if (mem != null) { 
                    changes.add(new StringRemoveChange(tmp, mem));
                }
            }
        }


        
        
        for (Iterator<String> i=onDisk.getStringKeySet().iterator(); i.hasNext();) {
            String diskId = i.next();
            if (!used.contains(diskId)) {
                BibtexString disk = onDisk.getString(diskId);
                
                used.add(diskId);
                changes.add(new StringAddChange(disk));
            }
        }
    }

    private BibtexString findString(BibtexDatabase base, String name, HashSet<Object> used) {
        if (!base.hasStringLabel(name))
            return null;
        for (Iterator<String> i=base.getStringKeySet().iterator(); i.hasNext();) {
            String key = i.next();
            BibtexString bs = base.getString(key);
            if (bs.getName().equals(name) && !used.contains(key)) {
                used.add(key);
                return bs;
            }
        }
        return null;
    }

    
    public void scanGroups(MetaData inMem, MetaData onTmp, MetaData onDisk) {
        final GroupTreeNode groupsTmp = onTmp.getGroups();
        final GroupTreeNode groupsDisk = onDisk.getGroups();
        if (groupsTmp == null && groupsDisk == null)
            return;
        if ((groupsTmp != null && groupsDisk == null)
                || (groupsTmp == null && groupsDisk != null)) {
            changes.add(new GroupChange(groupsDisk));
            return;
        }
        if (groupsTmp.equals(groupsDisk))
            return;
        changes.add(new GroupChange(groupsDisk));
        return;

















































    }

}
