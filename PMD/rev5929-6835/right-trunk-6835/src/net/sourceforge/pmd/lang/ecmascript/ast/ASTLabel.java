package net.sourceforge.pmd.lang.ecmascript.ast;

import org.mozilla.javascript.ast.Label;

public class ASTLabel extends AbstractEcmascriptNode<Label> {
    public ASTLabel(Label label) {
	super(label);
	super.setImage(label.getName());
    }

    
    public Object jjtAccept(EcmascriptParserVisitor visitor, Object data) {
	return visitor.visit(this, data);
    }
}