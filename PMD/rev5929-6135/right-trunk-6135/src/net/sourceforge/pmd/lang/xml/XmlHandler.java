
package net.sourceforge.pmd.lang.xml;

import java.io.Writer;

import net.sourceforge.pmd.lang.LanguageVersionHandler;
import net.sourceforge.pmd.lang.Parser;
import net.sourceforge.pmd.lang.VisitorStarter;
import net.sourceforge.pmd.lang.XPathHandler;
import net.sourceforge.pmd.lang.rule.RuleViolationFactory;
import net.sourceforge.pmd.lang.xml.rule.XmlRuleViolationFactory;

import org.jaxen.Navigator;
import org.jaxen.dom.DocumentNavigator;


public class XmlHandler implements LanguageVersionHandler {

    public XPathHandler getXPathHandler() {
	return new XPathHandler() {
	    public void initialize() {
	    }

	    public Navigator getNavigator() {
		return new DocumentNavigator();
	    }
	};
    }

    public RuleViolationFactory getRuleViolationFactory() {
	return XmlRuleViolationFactory.INSTANCE;
    }

    public Parser getParser() {
	return new XmlParser();
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
	
	return VisitorStarter.DUMMY;
    }
}
