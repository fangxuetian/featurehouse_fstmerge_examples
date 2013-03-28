
package test.net.sourceforge.pmd.lang.xml.rule.basic;

import org.junit.Before;

import test.net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class BasicRulesTest extends SimpleAggregatorTst {

    @Before
    public void setUp() {
        addRule("xml-basic", "MistypedCDATASection");
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(BasicRulesTest.class);
    }
}
