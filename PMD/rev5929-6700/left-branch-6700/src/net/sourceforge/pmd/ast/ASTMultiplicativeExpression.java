

package net.sourceforge.pmd.ast;

public class ASTMultiplicativeExpression extends SimpleJavaTypeNode {
    public ASTMultiplicativeExpression(int id) {
        super(id);
    }

    public ASTMultiplicativeExpression(JavaParser p, int id) {
        super(p, id);
    }


    
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
