

package net.sourceforge.pmd.lang.java.ast;

public class ASTEmptyStatement extends AbstractJavaNode {
    public ASTEmptyStatement(int id) {
        super(id);
    }

    public ASTEmptyStatement(JavaParser p, int id) {
        super(p, id);
    }


    
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
