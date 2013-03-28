
package net.sourceforge.pmd.lang.java.rule.strings;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.pmd.PropertyDescriptor;
import net.sourceforge.pmd.lang.java.ast.ASTAdditiveExpression;
import net.sourceforge.pmd.lang.java.ast.ASTArgumentList;
import net.sourceforge.pmd.lang.java.ast.ASTDoStatement;
import net.sourceforge.pmd.lang.java.ast.ASTForStatement;
import net.sourceforge.pmd.lang.java.ast.ASTIfStatement;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimarySuffix;
import net.sourceforge.pmd.lang.java.ast.ASTSwitchLabel;
import net.sourceforge.pmd.lang.java.ast.ASTSwitchStatement;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.ASTWhileStatement;
import net.sourceforge.pmd.lang.java.ast.TypeNode;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.properties.IntegerProperty;
import net.sourceforge.pmd.symboltable.NameOccurrence;
import net.sourceforge.pmd.symboltable.VariableNameDeclaration;
import net.sourceforge.pmd.typeresolution.TypeHelper;


public class ConsecutiveLiteralAppendsRule extends AbstractJavaRule {

    private final static Set<Class> blockParents;

    static {
        blockParents = new HashSet<Class>();
        blockParents.add(ASTForStatement.class);
        blockParents.add(ASTWhileStatement.class);
        blockParents.add(ASTDoStatement.class);
        blockParents.add(ASTIfStatement.class);
        blockParents.add(ASTSwitchStatement.class);
        blockParents.add(ASTMethodDeclaration.class);
    }
    
    private static final PropertyDescriptor thresholdDescriptor = new IntegerProperty(
    		"threshold", 
    		"?",
    		1,
    		1.0f
    		);
    
    private static final Map<String, PropertyDescriptor> propertyDescriptorsByName = asFixedMap(thresholdDescriptor);
 

    private int threshold = 1;

    public Object visit(ASTVariableDeclaratorId node, Object data) {

        if (!isStringBuffer(node)) {
            return data;
        }
        threshold = getIntProperty(thresholdDescriptor);

        int concurrentCount = checkConstructor(node, data);
        Node lastBlock = getFirstParentBlock(node);
        Node currentBlock = lastBlock;
        Map<VariableNameDeclaration, List<NameOccurrence>> decls = node.getScope().getVariableDeclarations();
        Node rootNode = null;
        
        if (concurrentCount == 1) {
            rootNode = node;
        }
        for (Map.Entry<VariableNameDeclaration, List<NameOccurrence>> entry: decls.entrySet()) {
            List<NameOccurrence> decl = entry.getValue();
            for (NameOccurrence no: decl) {
        	Node n = no.getLocation();

                currentBlock = getFirstParentBlock(n);

                if (!InefficientStringBufferingRule.isInStringBufferOperation(n, 3,"append")) {
                    if (!no.isPartOfQualifiedName()) {
                        checkForViolation(rootNode, data, concurrentCount);
                        concurrentCount = 0;
                    }
                    continue;
                }
                ASTPrimaryExpression s = n.getFirstParentOfType(ASTPrimaryExpression.class);
                int numChildren = s.jjtGetNumChildren();
                for (int jx = 0; jx < numChildren; jx++) {
                    Node sn = s.jjtGetChild(jx);
                    if (!(sn instanceof ASTPrimarySuffix)
                            || sn.getImage() != null) {
                        continue;
                    }

                    
                    if ((currentBlock != null && lastBlock != null && !currentBlock
                            .equals(lastBlock))
                            || (currentBlock == null ^ lastBlock == null)) {
                        checkForViolation(rootNode, data, concurrentCount);
                        concurrentCount = 0;
                    }

                    
                    
                    if (concurrentCount == 0) {
                        rootNode = sn;
                    }
                    if (isAdditive(sn)) {
                        concurrentCount = processAdditive(data,
                                concurrentCount, sn, rootNode);
                        if (concurrentCount != 0) {
                            rootNode = sn;
                        }
                    } else if (!isAppendingStringLiteral(sn)) {
                        checkForViolation(rootNode, data, concurrentCount);
                        concurrentCount = 0;
                    } else {
                        concurrentCount++;
                    }
                    lastBlock = currentBlock;
                }
            }
        }
        checkForViolation(rootNode, data, concurrentCount);
        return data;
    }

    
    private int checkConstructor(ASTVariableDeclaratorId node, Object data) {
        Node parent = node.jjtGetParent();
        if (parent.jjtGetNumChildren() >= 2) {
            ASTArgumentList list = parent
                    .jjtGetChild(1).getFirstChildOfType(ASTArgumentList.class);
            if (list != null) {
                ASTLiteral literal = list.getFirstChildOfType(ASTLiteral.class);
                if (!isAdditive(list) && literal != null
                        && literal.isStringLiteral()) {
                    return 1;
                } 
                return processAdditive(data, 0, list, node);
            }
        }
        return 0;
    }

    private int processAdditive(Object data, int concurrentCount,
	    Node sn, Node rootNode) {
        ASTAdditiveExpression additive = sn.getFirstChildOfType(ASTAdditiveExpression.class);
        if (additive == null) {
            return 0;
        }
        int count = concurrentCount;
        boolean found = false;
        for (int ix = 0; ix < additive.jjtGetNumChildren(); ix++) {
            Node childNode = additive.jjtGetChild(ix);
            if (childNode.jjtGetNumChildren() != 1
                    || childNode.findChildrenOfType(ASTName.class).size() != 0) {
                if (!found) {
                    checkForViolation(rootNode, data, count);
                    found = true;
                }
                count = 0;
            } else {
                count++;
            }
        }

        
        
        if (!found) {
            count = 1;
        }

        return count;
    }

    
    private boolean isAdditive(Node n) {
        List lstAdditive = n.findChildrenOfType(ASTAdditiveExpression.class);
        if (lstAdditive.isEmpty()) {
            return false;
        }
        
        
        
        for (int ix = 0; ix < lstAdditive.size(); ix++) {
            ASTAdditiveExpression expr = (ASTAdditiveExpression) lstAdditive.get(ix);
            if (expr.getParentsOfType(ASTArgumentList.class).size() != 1) {
                return false;
            }
        }
        return true;
    }

    
    private Node getFirstParentBlock(Node node) {
        Node parentNode = node.jjtGetParent();

        Node lastNode = node;
        while (parentNode != null
                && !blockParents.contains(parentNode.getClass())) {
            lastNode = parentNode;
            parentNode = parentNode.jjtGetParent();
        }
        if (parentNode != null
                && parentNode.getClass().equals(ASTIfStatement.class)) {
            parentNode = lastNode;
        } else if (parentNode != null
                && parentNode.getClass().equals(ASTSwitchStatement.class)) {
            parentNode = getSwitchParent(parentNode, lastNode);
        }
        return parentNode;
    }

    
    private Node getSwitchParent(Node parentNode, Node lastNode) {
        int allChildren = parentNode.jjtGetNumChildren();
        ASTSwitchLabel label = null;
        for (int ix = 0; ix < allChildren; ix++) {
            Node n = parentNode.jjtGetChild(ix);
            if (n.getClass().equals(ASTSwitchLabel.class)) {
                label = (ASTSwitchLabel) n;
            } else if (n.equals(lastNode)) {
                parentNode = label;
                break;
            }
        }
        return parentNode;
    }

    
    private void checkForViolation(Node node, Object data,
                                   int concurrentCount) {
        if (concurrentCount > threshold) {
            String[] param = {String.valueOf(concurrentCount)};
            addViolation(data, node, param);
        }
    }

    private boolean isAppendingStringLiteral(Node node) {
	Node n = node;
        while (n.jjtGetNumChildren() != 0
                && !n.getClass().equals(ASTLiteral.class)) {
            n = n.jjtGetChild(0);
        }
        return n.getClass().equals(ASTLiteral.class);
    }

    private static boolean isStringBuffer(ASTVariableDeclaratorId node) {

        if (node.getType() != null) {
            return node.getType().equals(StringBuffer.class);
        }
        Node nn = node.getTypeNameNode();
        if (nn.jjtGetNumChildren() == 0) {
            return false;
        }
        return TypeHelper.isA((TypeNode)nn.jjtGetChild(0), StringBuffer.class);
    }

    protected Map<String, PropertyDescriptor> propertiesByName() {
    	return propertyDescriptorsByName;
    }
}