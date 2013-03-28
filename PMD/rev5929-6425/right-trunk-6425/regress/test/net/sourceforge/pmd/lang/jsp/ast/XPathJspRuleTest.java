package test.net.sourceforge.pmd.lang.jsp.ast;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSets;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.rule.XPathRule;

import org.junit.Test;

import test.net.sourceforge.pmd.testframework.RuleTst;

public class XPathJspRuleTest extends RuleTst {

    
    @Test
    public void testExpressionMatching() throws Throwable {
        Rule rule = new XPathRule();
        rule.addProperty("xpath", XPATH_EXPRESSION);
        rule.setMessage("Test");
        RuleSet rules = new RuleSet();
        rules.addRule(rule);
        rules.setLanguage(Language.JSP);

        RuleContext ctx = new RuleContext();
        Report report = new Report();
        ctx.setReport(report);
        ctx.setSourceCodeFilename("n/a");
        ctx.setLanguageVersion(LanguageVersion.JSP);

        PMD p = new PMD();

        p.processFile(new StringReader(MATCH), new RuleSets(rules), ctx);

        assertEquals("One violation expected!", 1, report.size());

        RuleViolation rv = (RuleViolation) report.iterator().next();
        assertEquals(1, rv.getBeginLine());
    }

    private static final String MATCH
            = "<html><hr/></html>";

    private static final String XPATH_EXPRESSION
            = "//Element [@Name='hr']";

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(XPathJspRuleTest.class);
    }
}
