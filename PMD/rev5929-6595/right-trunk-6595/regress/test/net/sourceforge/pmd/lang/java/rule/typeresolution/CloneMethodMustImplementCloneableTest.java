
package test.net.sourceforge.pmd.lang.java.rule.typeresolution;

import org.junit.Before;

import test.net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class CloneMethodMustImplementCloneableTest extends SimpleAggregatorTst {

    @Before
	public void setUp() {
	
	}

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(CloneMethodMustImplementCloneableTest.class);
    }
}
