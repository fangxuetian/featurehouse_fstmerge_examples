
package net.sourceforge.pmd.lang.java.rule;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;


public class AvoidNonConstructorMethodsWithClassName extends AbstractJavaRule {

    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isInterface()) {
            return data;
        }
        return super.visit(node, data);
    }

    public Object visit(ASTMethodDeclaration node, Object data) {
        String declaringType = getDeclaringType(node);
        if (node.getMethodName().equals(declaringType)) {
            addViolation(data, node, node.getMethodName());
        }
        return data;
    }

}
