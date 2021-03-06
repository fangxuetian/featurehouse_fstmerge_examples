package net.sourceforge.pmd.ast;

import java.util.List;

import net.sourceforge.pmd.AbstractRuleChainVisitor;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.rules.XPathRule;

public class JavaRuleChainVisitor extends AbstractRuleChainVisitor {

	protected void indexNodes(List<CompilationUnit> astCompilationUnits, RuleContext ctx) {
		JavaParserVisitor javaParserVistor = new JavaParserVisitorAdapter() {
			
			
			public Object visit(SimpleJavaNode node, Object data) {
				indexNode(node);
				return super.visit(node, data);
			}
		};

		for (int i = 0; i < astCompilationUnits.size(); i++) {
			javaParserVistor.visit((ASTCompilationUnit)astCompilationUnits.get(i), ctx);
		}
	}

	protected void visit(Rule rule, SimpleNode node, RuleContext ctx) {
		
		if (rule instanceof XPathRule) {
			((XPathRule)rule).evaluate(node, ctx);
		} else {
			((SimpleJavaNode)node).jjtAccept((JavaParserVisitor)rule, ctx);
		}
	}
}
