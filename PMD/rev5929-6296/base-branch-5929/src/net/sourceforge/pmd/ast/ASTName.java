

package net.sourceforge.pmd.ast;

import net.sourceforge.pmd.symboltable.NameDeclaration;

public class ASTName extends SimpleJavaTypeNode {
    public ASTName(int id) {
        super(id);
    }

    public ASTName(JavaParser p, int id) {
        super(p, id);
    }

    private NameDeclaration nd;

    public void setNameDeclaration(NameDeclaration nd) {
        this.nd = nd;
    }

    public NameDeclaration getNameDeclaration() {
        return this.nd;
    }


    
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
