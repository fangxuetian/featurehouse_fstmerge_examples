package net.sourceforge.pmd.lang.jsp;

import java.io.Writer;

import net.sf.saxon.sxpath.IndependentContext;
import net.sourceforge.pmd.lang.DataFlowHandler;
import net.sourceforge.pmd.lang.LanguageVersionHandler;
import net.sourceforge.pmd.lang.Parser;
import net.sourceforge.pmd.lang.VisitorStarter;
import net.sourceforge.pmd.lang.XPathHandler;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.ast.xpath.AbstractASTXPathHandler;
import net.sourceforge.pmd.lang.jsp.ast.DumpFacade;
import net.sourceforge.pmd.lang.jsp.ast.JspNode;
import net.sourceforge.pmd.lang.jsp.rule.JspRuleViolationFactory;
import net.sourceforge.pmd.lang.rule.RuleViolationFactory;


public class JspHandler implements LanguageVersionHandler {

    public DataFlowHandler getDataFlowHandler() {
	return DataFlowHandler.DUMMY;
    }

    public XPathHandler getXPathHandler() {
	return new AbstractASTXPathHandler() {
	    public void initialize() {
	    }

	    public void initialize(IndependentContext context) {
	    }
	};
    }

    public RuleViolationFactory getRuleViolationFactory() {
	return JspRuleViolationFactory.INSTANCE;
    }

    public Parser getParser() {
	return new JspParser();
    }

    public VisitorStarter getDataFlowFacade() {
	return VisitorStarter.DUMMY;
    }

    public VisitorStarter getSymbolFacade() {
	return VisitorStarter.DUMMY;
    }

    public VisitorStarter getTypeResolutionFacade(ClassLoader classLoader) {
	return VisitorStarter.DUMMY;
    }

    public VisitorStarter getDumpFacade(final Writer writer, final String prefix, final boolean recurse) {
	return new VisitorStarter() {
	    public void start(Node rootNode) {
		new DumpFacade().initializeWith(writer, prefix, recurse, (JspNode) rootNode);
	    }
	};
    }
}
