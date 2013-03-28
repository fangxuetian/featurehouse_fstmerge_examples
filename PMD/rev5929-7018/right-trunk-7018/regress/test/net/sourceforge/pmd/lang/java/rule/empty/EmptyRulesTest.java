
package test.net.sourceforge.pmd.lang.java.rule.empty;

import org.junit.Before;

import test.net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class EmptyRulesTest extends SimpleAggregatorTst {
    
    private static final String RULESET = "java-empty";

    @Before
    public void setUp() {
        addRule(RULESET, "EmptyCatchBlock");
        addRule(RULESET, "EmptyFinallyBlock");
        addRule(RULESET, "EmptyIfStmt");
        addRule(RULESET, "EmptyInitializer");
        addRule(RULESET, "EmptyStatementBlock");
        addRule(RULESET, "EmptyStatementNotInLoop");
        addRule(RULESET, "EmptyStaticInitializer");
        addRule(RULESET, "EmptySwitchStatements");
        addRule(RULESET, "EmptySynchronizedBlock");
        addRule(RULESET, "EmptyTryBlock");
        addRule(RULESET, "EmptyWhileStmt");
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(EmptyRulesTest.class);
    }
}
