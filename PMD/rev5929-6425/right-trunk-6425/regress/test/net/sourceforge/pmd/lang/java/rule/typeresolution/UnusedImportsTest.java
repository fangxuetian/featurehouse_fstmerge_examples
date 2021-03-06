
package test.net.sourceforge.pmd.lang.java.rule.typeresolution;

import org.junit.Before;

import test.net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class UnusedImportsTest extends SimpleAggregatorTst {

    @Before
    public void setUp() {
        addRule("typeresolution", "UnusedImports");
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(UnusedImportsTest.class);
    }
}
