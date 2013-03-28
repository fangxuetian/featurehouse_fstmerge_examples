
package net.sourceforge.pmd.lang.ecmascript.ast;

import org.mozilla.javascript.ast.SwitchStatement;

public class ASTSwitchStatement extends AbstractEcmascriptNode<SwitchStatement> {
    public ASTSwitchStatement(SwitchStatement switchStatement) {
	super(switchStatement);
    }

    
    public Object jjtAccept(EcmascriptParserVisitor visitor, Object data) {
	return visitor.visit(this, data);
    }

    public EcmascriptNode getExpression() {
	return (EcmascriptNode) jjtGetChild(0);
    }

    public int getNumCases() {
	return node.getCases().size();
    }

    public ASTSwitchCase getSwitchCase(int index) {
	return (ASTSwitchCase) jjtGetChild(index + 1);
    }
}
