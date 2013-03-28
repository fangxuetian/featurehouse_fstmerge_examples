package net.sourceforge.pmd.lang.dfa.pathfinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.pmd.lang.dfa.DataFlowNode;
import net.sourceforge.pmd.lang.dfa.NodeType;

public class CurrentPath {

    private List<DataFlowNode> list;

    public CurrentPath() {
        list = new ArrayList<DataFlowNode>();
    }

    public int getLength() {
        return list.size();
    }
    
    public Iterator<DataFlowNode> iterator() {
        return list.iterator();
    }

    public DataFlowNode getLast() {
        return list.get(list.size() - 1);
    }

    public void removeLast() {
	list.remove(list.size() - 1);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void addLast(DataFlowNode n) {
        list.add(n);
        
    }

    public boolean isDoBranchNode() {
        return this.getLast().isType(NodeType.DO_EXPR);
    }

    public boolean isFirstDoStatement() {
        return isFirstDoStatement(this.getLast());
    }

    public DataFlowNode getDoBranchNodeFromFirstDoStatement() {
	DataFlowNode inode = this.getLast();
        if (!isFirstDoStatement()) {
            return null;
        }
        for (DataFlowNode parent: inode.getParents()) {
            if (parent.isType(NodeType.DO_EXPR)) {
                return parent;
            }
        }
        return null;
    }

    public boolean isEndNode() {
        return this.getLast().getChildren().size() == 0;
        
    }

    public boolean isBranch() {
        return this.getLast().getChildren().size() > 1;
    }

    private boolean isFirstDoStatement(DataFlowNode inode) {
        int index = inode.getIndex() - 1;
        if (index < 0) {
            return false;
        }
        return inode.getFlow().get(index).isType(NodeType.DO_BEFORE_FIRST_STATEMENT);
    }
}

