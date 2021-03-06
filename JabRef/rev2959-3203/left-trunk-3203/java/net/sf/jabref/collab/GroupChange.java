package net.sf.jabref.collab;

import javax.swing.JComponent;
import javax.swing.JLabel;

import net.sf.jabref.BasePanel;
import net.sf.jabref.Globals;
import net.sf.jabref.BibtexDatabase;
import net.sf.jabref.groups.AllEntriesGroup;
import net.sf.jabref.groups.GroupTreeNode;
import net.sf.jabref.groups.UndoableModifySubtree;
import net.sf.jabref.undo.NamedCompound;

public class GroupChange extends Change {
    private final GroupTreeNode m_changedGroups;
    private GroupTreeNode tmpGroupRoot;

    public GroupChange(GroupTreeNode changedGroups, GroupTreeNode tmpGroupRoot) {
        super(changedGroups != null ? 
                "Modified groups tree"
                : "Removed all groups"); 
        m_changedGroups = changedGroups;
        this.tmpGroupRoot = tmpGroupRoot;
    }

    public boolean makeChange(BasePanel panel, BibtexDatabase secondary, NamedCompound undoEdit) {
        final GroupTreeNode root = panel.getGroupSelector().getGroupTreeRoot();
        final UndoableModifySubtree undo = new UndoableModifySubtree(
                panel.getGroupSelector(), root, Globals.lang("Modified groups")); 
        root.removeAllChildren();
        if (m_changedGroups == null) {
            
            root.setGroup(new AllEntriesGroup());
        } else {
            
            root.setGroup(m_changedGroups.getGroup());
            for (int i = 0; i < m_changedGroups.getChildCount(); ++i)        
                root.add(((GroupTreeNode) m_changedGroups.getChildAt(i)).deepCopy());
            
            
            
            root.refreshGroupsForNewDatabase(panel.database());
        }
        panel.getGroupSelector().revalidateGroups();
        undoEdit.addEdit(undo);
        
        
        GroupTreeNode copied = m_changedGroups.deepCopy();
        tmpGroupRoot.removeAllChildren();
        tmpGroupRoot.setGroup(copied.getGroup());
        for (int i = 0; i < copied.getChildCount(); ++i)
            tmpGroupRoot.add(((GroupTreeNode) copied.getChildAt(i)).deepCopy());
        tmpGroupRoot.refreshGroupsForNewDatabase(secondary);
        return true;
    }

    JComponent description() {
        return new JLabel("<html>" + name + "." + (m_changedGroups != null ? " " 
                + "Accepting the change replaces the complete " +
                "groups tree with the externally modified groups tree." : "") 
                + "</html>"); 
        
    }
}
