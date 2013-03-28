
package net.sourceforge.pmd.lang.java.rule.strings;

import net.sourceforge.pmd.lang.java.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class AppendCharacterWithCharRule extends AbstractJavaRule {

    private static final Pattern REGEX = Pattern.compile("\"[\\\\]?[\\s\\S]\"");

    public Object visit(ASTLiteral node, Object data) {
        ASTBlockStatement bs = node.getFirstParentOfType(ASTBlockStatement.class);
        if (bs == null) {
            return data;
        }

        String str = node.getImage();
        if (str == null || str.length() < 3 || str.length() > 4) {
            return data;
        }

        Matcher matcher = REGEX.matcher(str);
        if (matcher.find()) {
            if (!InefficientStringBufferingRule.isInStringBufferOperation(node, 8, "append")) {
                return data;
            }
            addViolation(data, node);
        }
        return data;
    }
}
