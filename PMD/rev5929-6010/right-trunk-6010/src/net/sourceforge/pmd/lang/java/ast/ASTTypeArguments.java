

package net.sourceforge.pmd.lang.java.ast;

public class ASTTypeArguments extends AbstractJavaNode {
    public ASTTypeArguments(int id) {
        super(id);
    }

    public ASTTypeArguments(JavaParser p, int id) {
        super(p, id);
    }


    
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
