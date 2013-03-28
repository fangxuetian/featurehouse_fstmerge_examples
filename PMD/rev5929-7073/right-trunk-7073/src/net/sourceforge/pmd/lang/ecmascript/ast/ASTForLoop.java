
package net.sourceforge.pmd.lang.ecmascript.ast;

import org.mozilla.javascript.ast.ForLoop;

public class ASTForLoop extends AbstractEcmascriptNode<ForLoop> {
    public ASTForLoop(ForLoop forLoop) {
	super(forLoop);
    }

    
    public Object jjtAccept(EcmascriptParserVisitor visitor, Object data) {
	return visitor.visit(this, data);
    }

    public EcmascriptNode getInitializer() {
	return (EcmascriptNode) jjtGetChild(0);
    }

    public EcmascriptNode getCondition() {
	return (EcmascriptNode) jjtGetChild(1);
    }

    public EcmascriptNode getIncrement() {
	return (EcmascriptNode) jjtGetChild(2);
    }

    public EcmascriptNode getBody() {
	return (EcmascriptNode) jjtGetChild(3);
    }
}
