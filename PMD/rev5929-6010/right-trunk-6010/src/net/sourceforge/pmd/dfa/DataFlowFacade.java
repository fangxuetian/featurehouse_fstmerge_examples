
package net.sourceforge.pmd.dfa;

import net.sourceforge.pmd.dfa.variableaccess.VariableAccessVisitor;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.JavaParserVisitorAdapter;


public class DataFlowFacade extends JavaParserVisitorAdapter {

    private StatementAndBraceFinder sbf;
    private VariableAccessVisitor vav;

    public void initializeWith(ASTCompilationUnit node) {
        sbf = new StatementAndBraceFinder();
        vav = new VariableAccessVisitor();
        node.jjtAccept(this, null);
    }

    public Object visit(ASTMethodDeclaration node, Object data) {
        sbf.buildDataFlowFor(node);
        vav.compute(node);
        return data;
    }

    public Object visit(ASTConstructorDeclaration node, Object data) {
        sbf.buildDataFlowFor(node);
        vav.compute(node);
        return data;
    }
}
