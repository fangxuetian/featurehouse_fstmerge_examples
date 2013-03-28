
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
        assertOutputContaining("Avoid really long methods");
        assertOutputContaining("Deeply nested if");
    }

    @Test
    public void testFormatterWithProperties() {
        executeTarget("testFormatterWithProperties");
        assertOutputContaining("Avoid really long methods");
        assertOutputContaining("Deeply nested if");
        assertOutputContaining("link_prefix");
        assertOutputContaining("line_prefix");
    }

    @Test
    public void testAbstractNames() {
        executeTarget("testAbstractNames");
        assertOutputContaining("Avoid really long methods");
        assertOutputContaining("Deeply nested if");
    }

    @Test
    public void testAbstractNamesInNestedRuleset() {
        executeTarget("testAbstractNamesInNestedRuleset");
        assertOutputContaining("Avoid really long methods");
        assertOutputContaining("Deeply nested if");
    }

    @Test
    public void testCommaInRulesetfiles() {
        executeTarget("testCommaInRulesetfiles");
        assertOutputContaining("Avoid really long methods");
        assertOutputContaining("Deeply nested if");
    }

    @Test
    public void testRelativeRulesets() {
        executeTarget("testRelativeRulesets");
        assertOutputContaining("Avoid really long methods");
        assertOutputContaining("Deeply nested if");
    }

    @Test
    public void testRelativeRulesetsInRulesetfiles() {
        executeTarget("testRelativeRulesetsInRulesetfiles");
        assertOutputContaining("Avoid really long methods");
        assertOutputContaining("Deeply nested if");
    }

    @Test
    public void testBasic() {
        executeTarget("testBasic");
    }

    @Test
    public void testInvalidLanguageVersion() {
        expectBuildExceptionContaining("testInvalidLanguageVersion", "Fail requested.", "The <version> element, if used, must be one of 'java 1.3', 'java 1.4', 'java 1.5', 'java 1.6', 'java 1.7'.");
    }
    
    @Test
    public void testExplicitRuleInRuleSet() {
        executeTarget("testExplicitRuleInRuleSet");
        assertOutputContaining("Avoid really long methods");
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(PMDTaskTest.class);
    }
}
