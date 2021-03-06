

package net.sourceforge.pmd.lang.java.ast;

public class ASTMethodDeclaration extends AbstractJavaAccessNode {
    public ASTMethodDeclaration(int id) {
        super(id);
    }

    public ASTMethodDeclaration(JavaParser p, int id) {
        super(p, id);
    }

    
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    
    public String getMethodName() {
        ASTMethodDeclarator md = getFirstChildOfType(ASTMethodDeclarator.class);
        if (md != null)
            return md.getImage();
        return null;
    }

    public boolean isSyntacticallyPublic() {
        return super.isPublic();
    }

    public boolean isSyntacticallyAbstract() {
        return super.isAbstract();
    }

    public boolean isPublic() {
        if (isInterfaceMember()) {
            return true;
        }
        return super.isPublic();
    }

    public boolean isAbstract() {
        if (isInterfaceMember()) {
            return true;
        }
        return super.isAbstract();
    }

    public boolean isInterfaceMember() {
        ASTClassOrInterfaceDeclaration clz = getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
        return clz != null && clz.isInterface();
    }

    public boolean isVoid() {
        return getFirstChildOfType(ASTResultType.class).isVoid();
    }

    public ASTResultType getResultType() {
        return getFirstChildOfType(ASTResultType.class);
    }

    public ASTBlock getBlock() {
        
        if (this.jjtGetChild(2) instanceof ASTBlock) {
            return (ASTBlock) this.jjtGetChild(2);
        }
        if (jjtGetNumChildren() > 3) {
            if (this.jjtGetChild(3) instanceof ASTBlock) {
                return (ASTBlock) this.jjtGetChild(3);
            }
        }
        return null;
    }
}
