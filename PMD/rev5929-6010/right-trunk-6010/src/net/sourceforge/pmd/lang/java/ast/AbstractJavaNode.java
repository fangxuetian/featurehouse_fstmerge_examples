package net.sourceforge.pmd.lang.java.ast;

import net.sourceforge.pmd.lang.ast.AbstractNode;

public abstract class AbstractJavaNode extends AbstractNode implements JavaNode {

    protected JavaParser parser;

    public AbstractJavaNode(int id) {
        super(id);
    }

    public AbstractJavaNode(JavaParser parser, int id) {
        super(id);
        this.parser = parser;
    }

    public void jjtOpen() {
	if (beginLine == -1 && parser.token.next != null) {
	    beginLine = parser.token.next.beginLine;
	    beginColumn = parser.token.next.beginColumn;
	}
    }

    public void jjtClose() {
	if (beginLine == -1 && (children == null || children.length == 0)) {
	    beginColumn = parser.token.beginColumn;
	}
	if (beginLine == -1) {
	    beginLine = parser.token.beginLine;
	}
	endLine = parser.token.endLine;
	endColumn = parser.token.endColumn;
    }

    
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    
    public Object childrenAccept(JavaParserVisitor visitor, Object data) {
        if (children != null) {
            for (int i = 0; i < children.length; ++i) {
                ((JavaNode) children[i]).jjtAccept(visitor, data);
            }
        }
        return data;
    }
    
    public String toString() {
        return JavaParserTreeConstants.jjtNodeName[id];
    }
}
