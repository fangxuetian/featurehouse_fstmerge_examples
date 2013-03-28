
package test.net.sourceforge.pmd.rules.useless;

import org.junit.Before;

import test.net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class UselessRulesTest extends SimpleAggregatorTst {

    @Before
    public void setUp() {
        addRule("useless", "UnnecessaryConversionTemporary");
        addRule("useless", "UnnecessaryReturn");
        addRule("useless", "UnnecessaryFinalModifier");
        addRule("useless", "UselessOverridingMethod");
        addRule("useless", "UselessOperationOnImmutable");
        addRule("useless", "UnusedNullCheckInEquals");
        addRule("useless", "UselessParentheses");	
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(UselessRulesTest.class);
    }
}
