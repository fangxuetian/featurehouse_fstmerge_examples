
package net.sourceforge.pmd.lang.java.rule.controversial;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.pmd.PropertyDescriptor;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.dfa.DataFlowNode;
import net.sourceforge.pmd.lang.dfa.VariableAccess;
import net.sourceforge.pmd.lang.dfa.pathfinder.CurrentPath;
import net.sourceforge.pmd.lang.dfa.pathfinder.DAAPathFinder;
import net.sourceforge.pmd.lang.dfa.pathfinder.Executable;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.rule.properties.IntegerProperty;


public class DataflowAnomalyAnalysisRule extends AbstractJavaRule implements Executable {
    private RuleContext rc;
    private List<DaaRuleViolation> daaRuleViolations;
    private int maxRuleViolations;
    private int currentRuleViolationCount;

    private static final PropertyDescriptor MAX_PATH_DESCRIPTOR = new IntegerProperty(
            "maxpaths", "Maximum number of paths per method", 100, 8000, 5000, 1.0f
            );

    private static final PropertyDescriptor MAX_VIOLATIONS_DESCRIPTOR = new IntegerProperty(
            "maxviolations", "Maximum number of anomalys per class", 1, 2000, 1000, 2.0f
            );

    private static final Map<String, PropertyDescriptor> PROPERTY_DESCRIPTORS_BY_NAME = asFixedMap(
            new PropertyDescriptor[] { MAX_PATH_DESCRIPTOR, MAX_VIOLATIONS_DESCRIPTOR});

    protected Map<String, PropertyDescriptor> propertiesByName() {
        return PROPERTY_DESCRIPTORS_BY_NAME;
    }

    private static class Usage {
        public int accessType;
        public DataFlowNode node;

        public Usage(int accessType, DataFlowNode node) {
            this.accessType = accessType;
            this.node = node;
        }

        public String toString() {
            return "accessType = " + accessType + ", line = " + node.getLine();
        }
    }

    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        this.maxRuleViolations = getIntProperty(MAX_VIOLATIONS_DESCRIPTOR);
        this.currentRuleViolationCount = 0;
        return super.visit(node, data);
    }

    public Object visit(ASTMethodDeclaration methodDeclaration, Object data) {
        this.rc = (RuleContext) data;
        this.daaRuleViolations = new ArrayList<DaaRuleViolation>();

        final DataFlowNode node = methodDeclaration.getDataFlowNode().getFlow().get(0);

        final DAAPathFinder pathFinder = new DAAPathFinder(node, this, getIntProperty(MAX_PATH_DESCRIPTOR));
        pathFinder.run();

        super.visit(methodDeclaration, data);
        return data;
    }

    public void execute(CurrentPath path) {
        if (maxNumberOfViolationsReached()) {
            
            return;
        }

        final Map<String, Usage> hash = new HashMap<String, Usage>();

        final Iterator<DataFlowNode> pathIterator = path.iterator();
        while (pathIterator.hasNext()) {
            
            DataFlowNode inode = pathIterator.next();
            if (inode.getVariableAccess() != null) {
                
                for (int g = 0; g < inode.getVariableAccess().size(); g++) {
                    final VariableAccess va = inode.getVariableAccess().get(g);

                    
                    final Usage lastUsage = hash.get(va.getVariableName());
                    if (lastUsage != null) {
                        
                        checkVariableAccess(inode, va, lastUsage);
                    }

                    final Usage newUsage = new Usage(va.getAccessType(), inode);
                    
                    hash.put(va.getVariableName(), newUsage);
                }
            }
        }
    }

    private void checkVariableAccess(DataFlowNode inode, VariableAccess va, final Usage u) {
        
        final int startLine = u.node.getLine();
        final int endLine = inode.getLine();

        final Node lastNode = inode.getNode();
        final Node firstNode = u.node.getNode();

        if (va.accessTypeMatches(u.accessType) && va.isDefinition() ) { 
            addDaaViolation(rc, lastNode, "DD", va.getVariableName(), startLine, endLine);
        } else if (u.accessType == VariableAccess.UNDEFINITION && va.isReference()) { 
            addDaaViolation(rc, lastNode, "UR", va.getVariableName(), startLine, endLine);
        } else if (u.accessType == VariableAccess.DEFINITION && va.isUndefinition()) { 
            addDaaViolation(rc, firstNode, "DU", va.getVariableName(), startLine, endLine);
        }
    }

    
    private final void addDaaViolation(Object data, Node node, String type, String var, int startLine, int endLine) {
        if (!maxNumberOfViolationsReached()
                && !violationAlreadyExists(type, var, startLine, endLine)
                && node != null) {
            final RuleContext ctx = (RuleContext) data;
            String msg = type;
            if (getMessage() != null) {
                msg = MessageFormat.format(getMessage(), type, var, startLine, endLine);
            }
            final DaaRuleViolation violation = new DaaRuleViolation(this, ctx, node, type, msg, var, startLine, endLine);
            ctx.getReport().addRuleViolation(violation);
            this.daaRuleViolations.add(violation);
            this.currentRuleViolationCount++;
      }
    }

    
    private boolean maxNumberOfViolationsReached() {
        return this.currentRuleViolationCount >= this.maxRuleViolations;
    }

    
    private boolean violationAlreadyExists(String type, String var, int startLine, int endLine) {
        for(DaaRuleViolation violation: this.daaRuleViolations) {
            if ((violation.getBeginLine() == startLine)
                    && (violation.getEndLine() == endLine)
                    && violation.getType().equals(type)
                    && violation.getVariableName().equals(var)) {
                return true;
            }
        }
        return false;
    }
}
