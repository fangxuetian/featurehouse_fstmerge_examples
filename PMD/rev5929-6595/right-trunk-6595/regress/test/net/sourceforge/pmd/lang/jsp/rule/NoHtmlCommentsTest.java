package test.net.sourceforge.pmd.lang.jsp.rule;

import org.junit.Before;

import test.net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class NoHtmlCommentsTest extends SimpleAggregatorTst {

    @Before
    public void setUp() {
        addRule("jsp", "NoHtmlComments");
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(NoHtmlCommentsTest.class);
    }
}
