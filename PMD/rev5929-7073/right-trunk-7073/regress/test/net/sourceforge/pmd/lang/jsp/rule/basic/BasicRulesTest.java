
package test.net.sourceforge.pmd.lang.jsp.rule.basic;

import org.junit.Before;

import test.net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class BasicRulesTest extends SimpleAggregatorTst {

    private static final String RULESET = "jsp-basic";

    @Before
    public void setUp() {
	addRule(RULESET, "DuplicateJspImports");
	addRule(RULESET, "IframeMissingSrcAttribute");
	addRule(RULESET, "JspEncoding");
	addRule(RULESET, "NoClassAttribute");
	addRule(RULESET, "NoHtmlComments");
	addRule(RULESET, "NoInlineScript");
	addRule(RULESET, "NoInlineStyleInformation");
	addRule(RULESET, "NoJspForward");
	addRule(RULESET, "NoLongScripts");
	addRule(RULESET, "NoScriptlets");
    }

    public static junit.framework.Test suite() {
	return new junit.framework.JUnit4TestAdapter(BasicRulesTest.class);
    }
}
