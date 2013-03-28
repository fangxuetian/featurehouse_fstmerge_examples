
package net.sourceforge.pmd.lang.java.rule.controversial;

import net.sourceforge.pmd.lang.java.ast.ASTAssignmentOperator;
import net.sourceforge.pmd.lang.java.ast.ASTConditionalExpression;
import net.sourceforge.pmd.lang.java.ast.ASTEqualityExpression;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTNullLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.java.symboltable.VariableNameDeclaration;




public class NullAssignmentRule extends AbstractJavaRule {

    @Override
    public Object visit(ASTNullLiteral node, Object data) {

        if (node.getNthParent(5) instanceof ASTStatementExpression) {
            ASTStatementExpression n = (ASTStatementExpression) node.getNthParent(5);

            if (isAssignmentToFinalField(n)) {
                return data;
            }

            if (n.jjtGetNumChildren() > 2 && n.jjtGetChild(1) instanceof ASTAssignmentOperator) {
                addViolation(data, node);
            }
        } else if (node.getNthParent(4) instanceof ASTConditionalExpression) {
            if (isBadTernary((ASTConditionalExpression)node.getNthParent(4))) {
                addViolation(data, node);
            }
        } else if (node.getNthParent(5) instanceof ASTConditionalExpression) {
            if (isBadTernary((ASTConditionalExpression)node.getNthParent(5))) {
                addViolation(data, node);
            }
        }

        return data;
    }

    private boolean isAssignmentToFinalField(ASTStatementExpression n) {
        ASTName name = n.getFirstDescendantOfType(ASTName.class);
        return name != null
                && name.getNameDeclaration() instanceof VariableNameDeclaration
                && ((VariableNameDeclaration) name.getNameDeclaration()).getAccessNodeParent().isFinal();
    }

    private boolean isBadTernary(ASTConditionalExpression n) {
        return n.isTernary() && !(n.jjtGetChild(0) instanceof ASTEqualityExpression);
    }
}
