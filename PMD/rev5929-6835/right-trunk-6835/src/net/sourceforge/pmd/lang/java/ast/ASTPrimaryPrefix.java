

package net.sourceforge.pmd.lang.java.ast;

public class ASTPrimaryPrefix extends AbstractJavaTypeNode {
    public ASTPrimaryPrefix(int id) {
        super(id);
    }

    public ASTPrimaryPrefix(JavaParser p, int id) {
        super(p, id);
    }

    private boolean usesThisModifier;
    private boolean usesSuperModifier;

    public void setUsesThisModifier() {
        usesThisModifier = true;
    }

    public boolean usesThisModifier() {
        return this.usesThisModifier;
    }

    public void setUsesSuperModifier() {
        usesSuperModifier = true;
    }

    public boolean usesSuperModifier() {
        return this.usesSuperModifier;
    }

    
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
