package net.sf.jabref.imports;

import net.sf.jabref.imports.ParserResult;
import net.sf.jabref.imports.OpenDatabaseAction;
import net.sf.jabref.*;
import net.sf.jabref.groups.GroupTreeNode;
import net.sf.jabref.groups.AllEntriesGroup;
import net.sf.jabref.groups.ExplicitGroup;
import net.sf.jabref.groups.AbstractGroup;
import net.sf.jabref.undo.NamedCompound;
import net.sf.jabref.undo.UndoableInsertEntry;
import net.sf.jabref.undo.UndoableInsertString;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.List;


public class AppendDatabaseAction extends BaseAction {
    private JabRefFrame frame;
    private BasePanel panel;
    private List filesToOpen = new ArrayList();

    public AppendDatabaseAction(JabRefFrame frame, BasePanel panel) {
        this.frame = frame;
        this.panel = panel;
    }

    public void action() {

        filesToOpen.clear();
        final MergeDialog md = new MergeDialog(frame, Globals.lang("Append database"), true);
        Util.placeDialog(md, panel);
        md.setVisible(true);
        if (md.isOkPressed()) {
            String[] chosen = Globals.getMultipleFiles(frame, new File(Globals.prefs.get("workingDirectory")),
                    null, false);
          
          
          if(chosen == null)
            return;
          for (int i=0; i<chosen.length; i++)
            filesToOpen.add(new File(chosen[i]));

            
            
            (new Thread() {
                public void run() {
                    openIt(md.importEntries(), md.importStrings(),
                            md.importGroups(), md.importSelectorWords());
                }
            }).start();
            
        }

      }

    void openIt(boolean importEntries, boolean importStrings,
                boolean importGroups, boolean importSelectorWords) {
        if (filesToOpen.size() == 0)
            return;
        for (Iterator i = filesToOpen.iterator(); i.hasNext();) {
            File file = (File)i.next();
            try {
                Globals.prefs.put("workingDirectory", file.getPath());
                
                String encoding = Globals.prefs.get("defaultEncoding");
                ParserResult pr = OpenDatabaseAction.loadDatabase(file, encoding);
                mergeFromBibtex(frame, panel, pr, importEntries, importStrings,
                        importGroups, importSelectorWords);
                panel.output(Globals.lang("Imported from database") + " '" + file.getPath() + "'");
            } catch (Throwable ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog
                        (panel, ex.getMessage(),
                                "Open database", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void mergeFromBibtex(JabRefFrame frame, BasePanel panel, ParserResult pr,
                                boolean importEntries, boolean importStrings,
                                boolean importGroups, boolean importSelectorWords)
              throws KeyCollisionException {

          BibtexDatabase fromDatabase = pr.getDatabase();
          ArrayList appendedEntries = new ArrayList();
          ArrayList originalEntries = new ArrayList();
          BibtexDatabase database = panel.database();
          BibtexEntry originalEntry;
          NamedCompound ce = new NamedCompound(Globals.lang("Append database"));
          MetaData meta = new MetaData(pr.getMetaData(), pr.getDatabase());

          if (importEntries) { 
              boolean overwriteOwner = Globals.prefs.getBoolean("overwriteOwner");
              boolean overwriteTimeStamp = Globals.prefs.getBoolean("overwriteTimeStamp");
              Iterator i = fromDatabase.getKeySet().iterator();
              while (i.hasNext()) {
                  originalEntry = fromDatabase.getEntryById((String) i.next());
                  BibtexEntry be = (BibtexEntry) (originalEntry.clone());
                  be.setId(Util.createNeutralId());
                  Util.setAutomaticFields(be, overwriteOwner, overwriteTimeStamp);
                  database.insertEntry(be);
                  appendedEntries.add(be);
                  originalEntries.add(originalEntry);
                  ce.addEdit(new UndoableInsertEntry(database, be, panel));
              }
          }

          if (importStrings) {
              BibtexString bs;
              int pos = 0;
              Iterator i = fromDatabase.getStringKeySet().iterator();
              for (; i.hasNext();) {
                  bs = (BibtexString) (fromDatabase.getString(i.next()).clone());
                  if (!database.hasStringLabel(bs.getName())) {
                      
                      database.addString(bs);
                      ce.addEdit(new UndoableInsertString(panel, database, bs));
                  }
              }
          }

          if (importGroups) {
              GroupTreeNode newGroups = meta.getGroups();
              if (newGroups != null) {

                  
                  if (newGroups.getGroup() instanceof AllEntriesGroup) {
                      
                      ExplicitGroup group = new ExplicitGroup("Imported",
                              AbstractGroup.INDEPENDENT); 
                      newGroups.setGroup(group);
                      for (int i = 0; i < appendedEntries.size(); ++i)
                          group.addEntry((BibtexEntry) appendedEntries.get(i));
                  }

                  
                  
                  
                  frame.groupSelector.addGroups(newGroups, ce);
                  
                  
                  
                  GroupTreeNode node;
                  ExplicitGroup group;
                  BibtexEntry entry;
                  for (Enumeration e = newGroups.preorderEnumeration(); e.hasMoreElements();) {
                      node = (GroupTreeNode) e.nextElement();
                      if (!(node.getGroup() instanceof ExplicitGroup))
                          continue;
                      group = (ExplicitGroup) node.getGroup();
                      for (int i = 0; i < originalEntries.size(); ++i) {
                          entry = (BibtexEntry) originalEntries.get(i);
                          if (group.contains(entry)) {
                              group.removeEntry(entry);
                              group.addEntry((BibtexEntry) appendedEntries.get(i));
                          }
                      }
                  }
                  frame.groupSelector.revalidateGroups();
              }
          }

          if (importSelectorWords) {
              Iterator i = meta.iterator();
              while (i.hasNext()) {
                  String s = (String) i.next();
                  if (s.startsWith(Globals.SELECTOR_META_PREFIX)) {
                      panel.metaData().putData(s, meta.getData(s));
                  }
              }
          }

          ce.end();
          panel.undoManager.addEdit(ce);
          panel.markBaseChanged();
      }


}
