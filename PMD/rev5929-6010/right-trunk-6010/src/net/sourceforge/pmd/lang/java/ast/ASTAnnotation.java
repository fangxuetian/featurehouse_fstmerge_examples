

package net.sourceforge.pmd.lang.java.ast;

import net.sourceforge.pmd.Rule;

import java.util.Arrays;
import java.util.List;

public class ASTAnnotation extends AbstractJavaNode {

    private static List unusedRules = Arrays.asList(new String[]{"UnusedPrivateField","UnusedLocalVariable","UnusedPrivateMethod","UnusedFormalParameter"});

    public ASTAnnotation(int id) {
        super(id);
    }

    public ASTAnnotation(JavaParser p, int id) {
        super(p, id);
    }

    public boolean suppresses(Rule rule) {
        final String ruleAnno = "\"PMD." + rule.getName() + "\"";

        if (jjtGetChild(0) instanceof ASTSingleMemberAnnotation) {
            ASTSingleMemberAnnotation n = (ASTSingleMemberAnnotation) jjtGetChild(0);

            if (n.jjtGetChild(0) instanceof ASTName) {
                ASTName annName = ((ASTName) n.jjtGetChild(0));

                if (annName.getImage().equals("SuppressWarnings")) {
                    List<ASTLiteral> nodes = n.findChildrenOfType(ASTLiteral.class);
                    for (ASTLiteral element: nodes) {
                        if (element.hasImageEqualTo("\"PMD\"")
                                || element.hasImageEqualTo(ruleAnno)
                                
                                
                                || (element.hasImageEqualTo("\"unused\"") && unusedRules.contains(rule.getName()))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
