
package net.sourceforge.pmd.lang.ecmascript.rule;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ecmascript.ast.EcmascriptNode;
import net.sourceforge.pmd.lang.rule.AbstractRuleViolation;

public class EcmascriptRuleViolation extends AbstractRuleViolation {
    public EcmascriptRuleViolation(Rule rule, RuleContext ctx, EcmascriptNode node, String message) {
	super(rule, ctx, node, message);
    }
}
