

package net.sourceforge.pmd.lang.java.ast;

public class ASTBreakStatement extends AbstractJavaNode {
    public ASTBreakStatement(int id) {
        super(id);
    }

    public ASTBreakStatement(JavaParser p, int id) {
        super(p, id);
    }


    
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
