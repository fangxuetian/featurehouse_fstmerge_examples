
package net.sourceforge.pmd.lang.java.rule.strings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.pmd.lang.java.ast.ASTAdditiveExpression;
import net.sourceforge.pmd.lang.java.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTIfStatement;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTMultiplicativeExpression;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTPrimarySuffix;
import net.sourceforge.pmd.lang.java.ast.ASTSwitchLabel;
import net.sourceforge.pmd.lang.java.ast.ASTSwitchStatement;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.java.symboltable.NameOccurrence;
import net.sourceforge.pmd.lang.java.typeresolution.TypeHelper;
import net.sourceforge.pmd.lang.ast.Node;


public class InsufficientStringBufferDeclarationRule extends AbstractJavaRule {

    private final static Set<Class<? extends Node>> blockParents;

    static {
        blockParents = new HashSet<Class<? extends Node>>();
        blockParents.add(ASTIfStatement.class);
        blockParents.add(ASTSwitchStatement.class);
    }

    public Object visit(ASTVariableDeclaratorId node, Object data) {
        if (!TypeHelper.isA(node.getNameDeclaration(), StringBuffer.class)) {
            return data;
        }
        Node rootNode = node;
        int anticipatedLength = 0;
        int constructorLength = 16;

        constructorLength = getConstructorLength(node, constructorLength);
        anticipatedLength = getInitialLength(node);
        List<NameOccurrence> usage = node.getUsages();
        Map<Node, Map<Node, Integer>> blocks = new HashMap<Node, Map<Node, Integer>>();
        for (int ix = 0; ix < usage.size(); ix++) {
            NameOccurrence no = usage.get(ix);
            Node n = no.getLocation();
            if (!InefficientStringBufferingRule.isInStringBufferOperation(n, 3, "append")) {

                if (!no.isOnLeftHandSide() && !InefficientStringBufferingRule.isInStringBufferOperation(n, 3, "setLength")) {
                    continue;
                }
                if (constructorLength != -1 && anticipatedLength > constructorLength) {
                    anticipatedLength += processBlocks(blocks);
                    String[] param = { String.valueOf(constructorLength), String.valueOf(anticipatedLength) };
                    addViolation(data, rootNode, param);
                }
                constructorLength = getConstructorLength(n, constructorLength);
                rootNode = n;
                anticipatedLength = getInitialLength(node);
            }
            ASTPrimaryExpression s = n.getFirstParentOfType(ASTPrimaryExpression.class);
            int numChildren = s.jjtGetNumChildren();
            for (int jx = 0; jx < numChildren; jx++) {
        	Node sn = s.jjtGetChild(jx);
                if (!(sn instanceof ASTPrimarySuffix) || sn.getImage() != null) {
                    continue;
                }
                int thisSize = 0;
                Node block = getFirstParentBlock(sn);
                if (isAdditive(sn)) {
                    thisSize = processAdditive(sn);
                } else {
                    thisSize = processNode(sn);
                }
                if (block != null) {
                    storeBlockStatistics(blocks, thisSize, block);
                } else {
                    anticipatedLength += thisSize;
                }
            }
        }
        anticipatedLength += processBlocks(blocks);
        if (constructorLength != -1 && anticipatedLength > constructorLength) {
            String[] param = { String.valueOf(constructorLength), String.valueOf(anticipatedLength) };
            addViolation(data, rootNode, param);
        }
        return data;
    }

    
    private void storeBlockStatistics(Map<Node, Map<Node, Integer>> blocks, int thisSize, Node block) {
        Node statement = block.jjtGetParent();
        if (ASTIfStatement.class.equals(block.jjtGetParent().getClass())) {
            
            
            Node possibleStatement = statement.getFirstParentOfType(ASTIfStatement.class);
            while(possibleStatement != null && possibleStatement.getClass().equals(ASTIfStatement.class)) {
                statement = possibleStatement;
                possibleStatement = possibleStatement.getFirstParentOfType(ASTIfStatement.class);
            }
        }
        Map<Node, Integer> thisBranch = blocks.get(statement);
        if (thisBranch == null) {
            thisBranch = new HashMap<Node, Integer>();
            blocks.put(statement, thisBranch);
        }
        Integer x = thisBranch.get(block);
        if (x != null) {
            thisSize += x;
        }
        thisBranch.put(statement, thisSize);
    }

    private int processBlocks(Map<Node, Map<Node, Integer>> blocks) {
        int anticipatedLength = 0;
        int ifLength = 0;
        for (Map.Entry<Node, Map<Node, Integer>> entry: blocks.entrySet()) {
            ifLength = 0;
            for (Map.Entry<Node, Integer> entry2: entry.getValue().entrySet()) {
                Integer value = entry2.getValue();
                ifLength = Math.max(ifLength, value.intValue());
            }
            anticipatedLength += ifLength;
        }
        return anticipatedLength;
    }

    private int processAdditive(Node sn) {
        ASTAdditiveExpression additive = sn.getFirstChildOfType(ASTAdditiveExpression.class);
        if (additive == null) {
            return 0;
        }
        int anticipatedLength = 0;
        for (int ix = 0; ix < additive.jjtGetNumChildren(); ix++) {
            Node childNode = additive.jjtGetChild(ix);
            ASTLiteral literal = childNode.getFirstChildOfType(ASTLiteral.class);
            if (literal != null && literal.getImage() != null) {
                anticipatedLength += literal.getImage().length() - 2;
            }
        }

        return anticipatedLength;
    }

    private static final boolean isLiteral(String str) {
        if (str.length() == 0) {
            return false;
        }
        char c = str.charAt(0);
        return (c == '"' || c == '\'');
    }

    private int processNode(Node sn) {
        int anticipatedLength = 0;
        ASTPrimaryPrefix xn = sn.getFirstChildOfType(ASTPrimaryPrefix.class);
        if (xn.jjtGetNumChildren() != 0 && xn.jjtGetChild(0).getClass().equals(ASTLiteral.class)) {
            String str = xn.jjtGetChild(0).getImage();
            if (str != null) {
	            if(isLiteral(str)){
	                anticipatedLength += str.length() - 2;
	            } else if(str.startsWith("0x")){
	                anticipatedLength += 1;
	            } else {
	                anticipatedLength += str.length();
	            }
            }
        }
        return anticipatedLength;
    }

    private int getConstructorLength(Node node, int constructorLength) {
        int iConstructorLength = constructorLength;
        Node block = node.getFirstParentOfType(ASTBlockStatement.class);
        List<ASTLiteral> literal;

        if (block == null) {
            block = node.getFirstParentOfType(ASTFieldDeclaration.class);
        }
        if (block == null) {
            block = node.getFirstParentOfType(ASTFormalParameter.class);
            if (block != null) {
                iConstructorLength = -1;
            }
        }
        
        
        ASTAdditiveExpression exp = block.getFirstChildOfType(ASTAdditiveExpression.class);
        if(exp != null){
            return 16;
        }
        ASTMultiplicativeExpression mult = block.getFirstChildOfType(ASTMultiplicativeExpression.class);
        if(mult != null){
            return 16;
        }
        
        literal = block.findChildrenOfType(ASTLiteral.class);
        if (literal.isEmpty()) {
            List<ASTName> name = block.findChildrenOfType(ASTName.class);
            if (!name.isEmpty()) {
                iConstructorLength = -1;
            }
        } else if (literal.size() == 1) {
            String str = literal.get(0).getImage();
            if (str == null) {
                iConstructorLength = 0;
            } else if (isLiteral(str)) {
                
                
                
                iConstructorLength = 14 + str.length(); 
            } else {
                iConstructorLength = Integer.parseInt(str);
            }
        } else {
            iConstructorLength = -1;
        }
        
        if(iConstructorLength == 0){
            iConstructorLength = 16;
        }

        return iConstructorLength;
    }


    private int getInitialLength(Node node) {
	Node block = node.getFirstParentOfType(ASTBlockStatement.class);

        if (block == null) {
            block = node.getFirstParentOfType(ASTFieldDeclaration.class);
            if (block == null) {
                block = node.getFirstParentOfType(ASTFormalParameter.class);
            }
        }
        List<ASTLiteral> literal = block.findChildrenOfType(ASTLiteral.class);
        if (literal.size() == 1) {
            String str = literal.get(0).getImage();
            if (str != null && isLiteral(str)) {
                return str.length() - 2; 
            }
        }
        
        return 0;
    }

    private boolean isAdditive(Node n) {
        return n.findChildrenOfType(ASTAdditiveExpression.class).size() >= 1;
    }

    
    private Node getFirstParentBlock(Node node) {
        Node parentNode = node.jjtGetParent();

        Node lastNode = node;
        while (parentNode != null && !blockParents.contains(parentNode.getClass())) {
            lastNode = parentNode;
            parentNode = parentNode.jjtGetParent();
        }
        if (parentNode != null && ASTIfStatement.class.equals(parentNode.getClass())) {
            parentNode = lastNode;
        } else if (parentNode != null && parentNode.getClass().equals(ASTSwitchStatement.class)) {
            parentNode = getSwitchParent(parentNode, lastNode);
        }
        return parentNode;
    }

    
    private static Node getSwitchParent(Node parentNode, Node lastNode) {
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

}