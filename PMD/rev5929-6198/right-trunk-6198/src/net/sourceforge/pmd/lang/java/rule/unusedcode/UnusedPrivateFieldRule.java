
package net.sourceforge.pmd.lang.java.rule.unusedcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBody;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBodyDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTPrimarySuffix;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.java.symboltable.NameOccurrence;
import net.sourceforge.pmd.lang.java.symboltable.VariableNameDeclaration;

public class UnusedPrivateFieldRule extends AbstractJavaRule {

    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        Map<VariableNameDeclaration, List<NameOccurrence>> vars = node.getScope().getVariableDeclarations();
        for (Map.Entry<VariableNameDeclaration, List<NameOccurrence>> entry: vars.entrySet()) {
            VariableNameDeclaration decl = entry.getKey();
            if (!decl.getAccessNodeParent().isPrivate() || isOK(decl.getImage())) {
                continue;
            }
            if (!actuallyUsed(entry.getValue())) {
            	if (!usedInOuterClass(node, decl)) {
            		addViolation(data, decl.getNode(), decl.getImage());
            	}
            }
        }
        return super.visit(node, data);
    }

    
	private boolean usedInOuterClass(ASTClassOrInterfaceDeclaration node,
			VariableNameDeclaration decl) {
		List<ASTClassOrInterfaceDeclaration> outerClasses = node.getParentsOfType(ASTClassOrInterfaceDeclaration.class);
		for (ASTClassOrInterfaceDeclaration outerClass : outerClasses) {
			ASTClassOrInterfaceBody classOrInterfaceBody = outerClass.getFirstChildOfType(ASTClassOrInterfaceBody.class);
			
			List<ASTClassOrInterfaceBodyDeclaration> classOrInterfaceBodyDeclarations = new ArrayList<ASTClassOrInterfaceBodyDeclaration>();
			classOrInterfaceBody.findChildrenOfType(ASTClassOrInterfaceBodyDeclaration.class, classOrInterfaceBodyDeclarations, false);
			
			for (ASTClassOrInterfaceBodyDeclaration classOrInterfaceBodyDeclaration : classOrInterfaceBodyDeclarations) {
				for (int i = 0; i < classOrInterfaceBodyDeclaration.jjtGetNumChildren(); i++) {
					if (classOrInterfaceBodyDeclaration.jjtGetChild(i) instanceof ASTClassOrInterfaceDeclaration) {
						continue;	
					}
					
					List<ASTPrimarySuffix> primarySuffixes = classOrInterfaceBodyDeclaration.findChildrenOfType(ASTPrimarySuffix.class);
					for (ASTPrimarySuffix primarySuffix : primarySuffixes) {
						if (decl.getImage().equals(primarySuffix.getImage())) {
							return true; 
						}
					}
					
					List<ASTPrimaryPrefix> primaryPrefixes = classOrInterfaceBodyDeclaration.findChildrenOfType(ASTPrimaryPrefix.class);
					for (ASTPrimaryPrefix primaryPrefix : primaryPrefixes) {
						ASTName name = primaryPrefix.getFirstChildOfType(ASTName.class);
						
						if (name != null && name.getImage().endsWith(decl.getImage())) {
							return true; 
						}
					}
				}
			}
			
		}
		
		return false;
	}

    private boolean actuallyUsed(List<NameOccurrence> usages) {
        for (NameOccurrence nameOccurrence: usages) {
            if (!nameOccurrence.isOnLeftHandSide()) {
                return true;
            }
        }
        
        return false;
    }

    private boolean isOK(String image) {
        return image.equals("serialVersionUID") || image.equals("serialPersistentFields") || image.equals("IDENT");
    }
}
