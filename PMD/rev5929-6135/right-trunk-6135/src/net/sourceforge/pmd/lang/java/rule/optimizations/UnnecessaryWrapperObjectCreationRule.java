package net.sourceforge.pmd.lang.java.rule.optimizations;

import java.util.Set;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTPrimarySuffix;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.util.CollectionUtil;

public class UnnecessaryWrapperObjectCreationRule extends AbstractJavaRule {

    private static final Set<String> prefixSet = CollectionUtil.asSet(new String[] {
        "Byte.valueOf",
        "Short.valueOf",
        "Integer.valueOf",
        "Long.valueOf",
        "Float.valueOf",
        "Double.valueOf",
        "Character.valueOf"
    });

    private static final Set<String> suffixSet = CollectionUtil.asSet(new String[] {
        "byteValue",
        "shortValue",
        "intValue",
        "longValue",
        "floatValue",
        "doubleValue",
        "charValue"
    });

    public Object visit(ASTPrimaryPrefix node, Object data) {
        if (node.jjtGetNumChildren() == 0 || !node.jjtGetChild(0).getClass().equals(ASTName.class)) {
            return super.visit(node, data);
        }

        String image = ((ASTName) node.jjtGetChild(0)).getImage();
        if (image.startsWith("java.lang.")) {
            image = image.substring(10);
        }

        boolean checkBoolean = ((RuleContext) data).getLanguageVersion().compareTo(LanguageVersion.JAVA_15) >= 0;

        if (prefixSet.contains(image)||(checkBoolean && "Boolean.valueOf".equals(image))) {
            ASTPrimaryExpression parent = (ASTPrimaryExpression) node.jjtGetParent();
            if (parent.jjtGetNumChildren() >= 3) {
                Node n = parent.jjtGetChild(2);
                if (n instanceof ASTPrimarySuffix) {
                    ASTPrimarySuffix suffix = (ASTPrimarySuffix) n;
                    image = suffix.getImage();

                    if (suffixSet.contains(image)||(checkBoolean && "booleanValue".equals(image))) {
                        super.addViolation(data, node);
                        return data;
                    }
                }
            }
        }
        return super.visit(node, data);
    }

}
