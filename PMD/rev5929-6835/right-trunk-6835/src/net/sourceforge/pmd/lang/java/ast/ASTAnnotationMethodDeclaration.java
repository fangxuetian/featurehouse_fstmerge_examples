

package net.sourceforge.pmd.lang.java.ast;

public class ASTAnnotationMethodDeclaration extends AbstractJavaAccessNode {
  public ASTAnnotationMethodDeclaration(int id) {
    super(id);
  }

  public ASTAnnotationMethodDeclaration(JavaParser p, int id) {
    super(p, id);
  }


  
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}

