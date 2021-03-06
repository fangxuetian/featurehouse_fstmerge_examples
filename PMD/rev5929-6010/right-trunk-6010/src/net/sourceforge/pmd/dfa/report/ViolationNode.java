package net.sourceforge.pmd.dfa.report;

import net.sourceforge.pmd.RuleViolation;

public class ViolationNode extends AbstractReportNode {

    private RuleViolation ruleViolation;

    public ViolationNode(RuleViolation violation) {
        this.ruleViolation = violation;
    }

    public RuleViolation getRuleViolation() {
        return ruleViolation;
    }

    public boolean equalsNode(AbstractReportNode arg0) {
        if (!(arg0 instanceof ViolationNode)) {
            return false;
        }

        RuleViolation rv = ((ViolationNode) arg0).getRuleViolation();

        return rv.getFilename().equals(getRuleViolation().getFilename()) &&
        	rv.getBeginLine() == getRuleViolation().getBeginLine() &&
        	rv.getVariableName().equals(getRuleViolation().getVariableName());
    }

}
