package net.sourceforge.pmd.lang.rule;

import java.text.MessageFormat;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.util.StringUtil;

public abstract class AbstractRuleViolationFactory implements RuleViolationFactory {

    private static final Object[] NO_ARGS = new Object[0];

    public void addViolation(RuleContext ruleContext, Rule rule, Node node, String message, Object[] args) {
	final String formattedMessage;
	if (message != null) {
	    
	    final String escapedMessage = StringUtil.replaceString(message, "${", "$'{'");
	    formattedMessage = MessageFormat.format(escapedMessage, args != null ? args : NO_ARGS);
	} else {
	    formattedMessage = message;
	}
	ruleContext.getReport().addRuleViolation(createRuleViolation(rule, ruleContext, node, formattedMessage));
    }

    protected abstract RuleViolation createRuleViolation(Rule rule, RuleContext ruleContext, Node node, String message);
}
