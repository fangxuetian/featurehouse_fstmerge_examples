
package net.sourceforge.pmd.lang.java.rule.design;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jaxen.JaxenException;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.java.ast.ASTArgumentList;
import net.sourceforge.pmd.lang.java.ast.ASTCastExpression;
import net.sourceforge.pmd.lang.java.ast.ASTCatchStatement;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTThrowStatement;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.symboltable.NameOccurrence;
import net.sourceforge.pmd.symboltable.VariableNameDeclaration;


public class PreserveStackTraceRule extends AbstractJavaRule {

    private List<ASTName> nameNodes = new ArrayList<ASTName>();

    
    private static final String FIND_THROWABLE_INSTANCE = "//VariableDeclaratorId[" +
						    "(../descendant::VariableInitializer/Expression/PrimaryExpression/PrimaryPrefix/AllocationExpression/ClassOrInterfaceType" +
						    "[" +
						    			"contains(@Image,'Exception')" + 
						    			"and" +
						    			"(not (../Arguments/ArgumentList))" +
						    "]" +
						    ")]";

    private static final String ILLEGAL_STATE_EXCEPTION = "IllegalStateException";
    private static final String FILL_IN_STACKTRACE = ".fillInStackTrace";

    public Object visit(ASTCatchStatement catchStmt, Object data) {
        String target = catchStmt.jjtGetChild(0).jjtGetChild(1).getImage();
        
        gatherVariableWithExceptionRef(catchStmt,data);
        
        List<ASTThrowStatement> lstThrowStatements = catchStmt.findChildrenOfType(ASTThrowStatement.class);
        for (ASTThrowStatement throwStatement : lstThrowStatements) {
            Node n = throwStatement.jjtGetChild(0).jjtGetChild(0);
            if (n.getClass().equals(ASTCastExpression.class)) {
                ASTPrimaryExpression expr = (ASTPrimaryExpression) n.jjtGetChild(1);
                if (expr.jjtGetNumChildren() > 1 && expr.jjtGetChild(1).getClass().equals(ASTPrimaryPrefix.class)) {
                    RuleContext ctx = (RuleContext) data;
                    addViolation(ctx, throwStatement);
                }
                continue;
            }
            
            if ( ! isThrownExceptionOfType(throwStatement,ILLEGAL_STATE_EXCEPTION) ) {
	            
	            ASTArgumentList args = throwStatement.getFirstChildOfType(ASTArgumentList.class);

	            if (args != null) {
	                ck(data, target, throwStatement, args);
	            }
	            else {
	        	Node child = throwStatement.jjtGetChild(0);
	                while (child != null && child.jjtGetNumChildren() > 0
	                        && !child.getClass().equals(ASTName.class)) {
	                    child = child.jjtGetChild(0);
	                }
	                if (child != null){
	                    if( child.getClass().equals(ASTName.class) && (!target.equals(child.getImage()) && !child.hasImageEqualTo(target + FILL_IN_STACKTRACE))) {
	                        Map<VariableNameDeclaration, List<NameOccurrence>> vars = ((ASTName) child).getScope().getVariableDeclarations();
		                    for (VariableNameDeclaration decl: vars.keySet()) {
		                        args = decl.getNode().jjtGetParent()
		                                .getFirstChildOfType(ASTArgumentList.class);
		                        if (args != null) {
		                            ck(data, target, throwStatement, args);
		                        }
		                    }
	                    } else if(child.getClass().equals(ASTClassOrInterfaceType.class)){
	                       addViolation(data, throwStatement);
	                    }
	                }
	            }
            }

        }
        return super.visit(catchStmt, data);
    }

    
    private void gatherVariableWithExceptionRef(ASTCatchStatement catchStmt, Object data) {
    	try {
			List<Node> nodes = catchStmt.findChildNodesWithXPath(FIND_THROWABLE_INSTANCE);
			for ( Node node : nodes ) {
				List <Node> violations = catchStmt.findChildNodesWithXPath("//Expression/PrimaryExpression/PrimaryPrefix/Name[@Image = '" + node.getImage() + "']");
				if ( violations != null && violations.size() > 0 ) {
					
					
					if ( ! useInitCause((Node)violations.get(0),catchStmt) ) 
						addViolation((RuleContext) data, node);
				}
			}
		} catch (JaxenException e) {
			
			e.printStackTrace();
		}

	}

	private boolean useInitCause(Node node, ASTCatchStatement catchStmt) throws JaxenException {
		
		if ( node != null && node.getImage() != null )
		{
			List <Node> nodes = catchStmt.findChildNodesWithXPath("descendant::StatementExpression/PrimaryExpression/PrimaryPrefix/Name[@Image = '" + node.getImage() + ".initCause']");
			if ( nodes != null && nodes.size() > 0 )
			{
				return true;
			}
		}
		return false;
	}

	private boolean isThrownExceptionOfType(ASTThrowStatement throwStatement,String type) {
    	boolean status = false;
    	try {
			List<Node> results = throwStatement.findChildNodesWithXPath("Expression/PrimaryExpression/PrimaryPrefix/AllocationExpression/ClassOrInterfaceType[@Image = '" + type + "']");
			
			if ( results != null && results.size() == 1 ) {
				status = true;
			}
		} catch (JaxenException e) {
			
			e.printStackTrace();
		}
    	return status;
	}

	private void ck(Object data, String target, ASTThrowStatement throwStatement,
                    ASTArgumentList args) {
        boolean match = false;
        nameNodes.clear();
        args.findChildrenOfType(ASTName.class, nameNodes);
        for (ASTName nameNode : nameNodes) {
            if (target.equals(nameNode.getImage())) {
                match = true;
                break;
            }
        }
        if ( ! match) {
            RuleContext ctx = (RuleContext) data;
            addViolation(ctx, throwStatement);
        }
    }
}
