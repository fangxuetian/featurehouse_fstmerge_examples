
package net.sourceforge.pmd.lang.ecmascript.rule;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.ecmascript.ast.EcmascriptNode;
import net.sourceforge.pmd.lang.rule.AbstractRuleViolationFactory;

public final class EcmascriptRuleViolationFactory extends AbstractRuleViolationFactory {

    public static final EcmascriptRuleViolationFactory INSTANCE = new EcmascriptRuleViolationFactory();

    private EcmascriptRuleViolationFactory() {
    }

    @Override
    protected RuleViolation createRuleViolation(Rule rule, RuleContext ruleContext, Node node, String message) {
	return new EcmascriptRuleViolation(rule, ruleContext, (EcmascriptNode) node, message);
    }
}
