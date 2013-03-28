
package test.net.sourceforge.pmd.lang.jsp.rule.basicjsf;

import org.junit.Before;

import test.net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class BasicJsfRulesTest extends SimpleAggregatorTst {

    private static final String RULESET = "jsp-basic-jsf";

    @Before
    public void setUp() {
	addRule(RULESET, "DontNestJsfInJstlIteration");
    }

    public static junit.framework.Test suite() {
	return new junit.framework.JUnit4TestAdapter(BasicJsfRulesTest.class);
    }
}
