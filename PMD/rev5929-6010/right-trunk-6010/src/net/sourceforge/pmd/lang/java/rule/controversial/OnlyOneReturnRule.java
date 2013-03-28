
package net.sourceforge.pmd.lang.java.rule.controversial;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTReturnStatement;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.ast.Node;

public class OnlyOneReturnRule extends AbstractJavaRule {

    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isInterface()) {
            return data;
        }
        return super.visit(node, data);
    }

    public Object visit(ASTMethodDeclaration node, Object data) {
        if (node.isAbstract()) {
            return data;
        }

        List<ASTReturnStatement> returnNodes = new ArrayList<ASTReturnStatement>();
        node.findChildrenOfType(ASTReturnStatement.class, returnNodes, false);
        if (returnNodes.size() > 1) {
            for (Iterator<ASTReturnStatement> i = returnNodes.iterator(); i.hasNext();) {
        	Node problem = i.next();
                
                if (!i.hasNext()) {
                    continue;
                }
                addViolation(data, problem);
            }
        }
        return data;
    }

}
