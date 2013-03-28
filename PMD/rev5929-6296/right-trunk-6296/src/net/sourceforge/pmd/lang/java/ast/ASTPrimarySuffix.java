

package net.sourceforge.pmd.lang.java.ast;

public class ASTPrimarySuffix extends AbstractJavaNode {
    public ASTPrimarySuffix(int id) {
        super(id);
    }

    public ASTPrimarySuffix(JavaParser p, int id) {
        super(p, id);
    }

    private boolean isArguments;
    private boolean isArrayDereference;

    public void setIsArrayDereference() {
        isArrayDereference = true;
    }

    public boolean isArrayDereference() {
        return isArrayDereference;
    }

    public void setIsArguments() {
        this.isArguments = true;
    }

    public boolean isArguments() {
        return this.isArguments;
    }

    public int getArgumentCount() {
        if (!this.isArguments()) {
            throw new RuntimeException("ASTPrimarySuffix.getArgumentCount called, but this is not a method call");
        }
        return ((ASTArguments)jjtGetChild(jjtGetNumChildren()-1)).getArgumentCount();
    }

    
    @Override
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
