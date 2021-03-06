
package net.sf.jabref.groups;

import javax.swing.undo.AbstractUndoableEdit;

import net.sf.jabref.Globals;

class UndoableResetGroups extends AbstractUndoableEdit {
    
    private final GroupTreeNode m_groupsBackup;
    
    private final GroupTreeNode m_groupsRootHandle;
    private final GroupSelector m_groupSelector;
    private boolean m_revalidate = true;

    public UndoableResetGroups(GroupSelector groupSelector,
            GroupTreeNode groupsRoot) {
        this.m_groupsBackup = groupsRoot.deepCopy();
        this.m_groupsRootHandle = groupsRoot;
        this.m_groupSelector = groupSelector;
    }

    public String getUndoPresentationName() {
        return Globals.lang("Undo") + ": " 
            + Globals.lang("clear all groups");
    }

    public String getRedoPresentationName() {
        return Globals.lang("Redo") + ": " 
            + Globals.lang("clear all groups");
    }

    public void undo() {
        super.undo();
        
        m_groupsRootHandle.removeAllChildren();
        m_groupsRootHandle.setGroup(m_groupsBackup.getGroup().deepCopy());
        for (int i = 0; i < m_groupsBackup.getChildCount(); ++i)
            m_groupsRootHandle.add(((GroupTreeNode) m_groupsBackup
                    .getChildAt(i)).deepCopy());
        if (m_revalidate)
            m_groupSelector.revalidateGroups();
    }

    public void redo() {
        super.redo();
        m_groupsRootHandle.removeAllChildren();
        m_groupsRootHandle.setGroup(new AllEntriesGroup());
        if (m_revalidate)
            m_groupSelector.revalidateGroups();
    }

    
    public void setRevalidate(boolean revalidate) {
        m_revalidate = revalidate;
    }
}
