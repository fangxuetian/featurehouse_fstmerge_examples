
package test.net.sourceforge.pmd.ant;

import org.apache.tools.ant.BuildFileTest;
import org.junit.Test;

import test.net.sourceforge.pmd.testframework.TestDescriptor;

public class PMDTaskTest extends BuildFileTest {

    @Override
    public void setUp() {
        
        configureProject("regress/test/net/sourceforge/pmd/ant/xml/pmdtasktest.xml");
    }

    @Test
    public void testNoFormattersValidation() {
        
        
        if (TestDescriptor.inRegressionTestMode()) {
            return;
        }
        expectBuildExceptionContaining("testNoFormattersValidation", "Valid Error Message", "<??>");
    }

    @Test
    public void testFormatterWithNoToFileAttribute() {
        expectBuildExceptionContaining("testFormatterWithNoToFileAttribute", "Valid Error Message", "toFile or toConsole needs to be specified in Formatter");
    }

    @Test
    public void testNoRuleSets() {
        expectBuildExceptionContaining("testNoRuleSets", "Valid Error Message", "No rulesets specified");
    }

    @Test
    public void testNestedRuleset() {
        executeTarget("testNestedRuleset");
        assertOutputContaining("Position literals first in String comparisons");
        assertOutputContaining("Too many fields");
    }

    @Test
    public void testFormatterWithProperties() {
        executeTarget("testFormatterWithProperties");
        assertOutputContaining("Position literals first in String comparisons");
        assertOutputContaining("Too many fields");
        assertOutputContaining("link_prefix");
        assertOutputContaining("line_prefix");
    }

    @Test
    public void testAbstractNames() {
        executeTarget("testAbstractNames");
        assertOutputContaining("Position literals first in String comparisons");
        assertOutputContaining("Too many fields");
    }

    @Test
    public void testAbstractNamesInNestedRuleset() {
        executeTarget("testAbstractNamesInNestedRuleset");
        assertOutputContaining("Position literals first in String comparisons");
        assertOutputContaining("Too many fields");
    }

    @Test
    public void testCommaInRulesetfiles() {
        executeTarget("testCommaInRulesetfiles");
        assertOutputContaining("Position literals first in String comparisons");
        assertOutputContaining("Too many fields");
    }

    @Test
    public void testRelativeRulesets() {
        executeTarget("testRelativeRulesets");
        assertOutputContaining("Position literals first in String comparisons");
        assertOutputContaining("Too many fields");
    }

    @Test
    public void testRelativeRulesetsInRulesetfiles() {
        executeTarget("testRelativeRulesetsInRulesetfiles");
        assertOutputContaining("Position literals first in String comparisons");
        assertOutputContaining("Too many fields");
    }

    @Test
    public void testBasic() {
        executeTarget("testBasic");
    }

    @Test
    public void testInvalidJDK() {
        expectBuildExceptionContaining("testInvalidJDK", "Fail requested.", "The targetjdk attribute, if used, must be one of ");
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(PMDTaskTest.class);
    }
}
