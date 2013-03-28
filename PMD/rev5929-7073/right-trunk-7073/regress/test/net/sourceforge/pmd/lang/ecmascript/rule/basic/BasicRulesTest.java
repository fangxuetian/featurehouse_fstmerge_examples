
package test.net.sourceforge.pmd.lang.ecmascript.rule.basic;

import org.junit.Before;

import test.net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class BasicRulesTest extends SimpleAggregatorTst {

    private static final String RULESET = "ecmascript-basic";

    @Before
    public void setUp() {
	addRule(RULESET, "AssignmentInOperand");
	addRule(RULESET, "ConsistentReturn");
	addRule(RULESET, "InnaccurateNumericLiteral");
	addRule(RULESET, "ScopeForInVariable");
	addRule(RULESET, "UnreachableCode");
    }

    public static junit.framework.Test suite() {
	return new junit.framework.JUnit4TestAdapter(BasicRulesTest.class);
    }
}
