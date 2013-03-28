
package net.sourceforge.pmd.lang.java;

import java.util.LinkedList;

import net.sourceforge.pmd.lang.DataFlowHandler;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.dfa.DataFlowNode;
import net.sourceforge.pmd.lang.java.ast.ASTLabeledStatement;
import net.sourceforge.pmd.lang.java.dfa.JavaDataFlowNode;

public class JavaDataFlowHandler implements DataFlowHandler {
    public DataFlowNode createDataFlowNode(LinkedList<DataFlowNode> dataFlow, Node node) {
	return new JavaDataFlowNode(dataFlow, node);
    }

    public Class<ASTLabeledStatement> getLabelStatementNodeClass() {
	return ASTLabeledStatement.class;
    }
}
