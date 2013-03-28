package net.sourceforge.pmd.ast;


public class SimpleJavaTypeNode extends SimpleJavaNode implements TypeNode {

	public SimpleJavaTypeNode(int i) {
		super(i);
	}

	public SimpleJavaTypeNode(JavaParser p, int i) {
		super(p, i);
	}

	private Class type;

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}
}
