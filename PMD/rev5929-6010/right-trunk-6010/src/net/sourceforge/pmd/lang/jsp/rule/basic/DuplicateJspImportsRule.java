package net.sourceforge.pmd.lang.jsp.rule.basic;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.java.rule.ImportWrapper;
import net.sourceforge.pmd.lang.jsp.ast.ASTJspDirectiveAttribute;
import net.sourceforge.pmd.lang.jsp.rule.AbstractJspRule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class DuplicateJspImportsRule extends AbstractJspRule {

    private Set<ImportWrapper> imports = new HashSet<ImportWrapper>();

    public void apply(List nodes, RuleContext ctx) {
        
        imports.clear();
        super.apply(nodes, ctx);
    }

    public Object visit(ASTJspDirectiveAttribute node, Object data) {

        if (!"import".equals(node.getName())) {
            return super.visit(node, data);
        }
        String values = node.getValue();
        StringTokenizer st = new StringTokenizer(values, ",");
        int count = st.countTokens();
        for (int ix = 0; ix < count; ix++) {
            String token = st.nextToken();
            ImportWrapper wrapper = new ImportWrapper(token, token, node);
            if (imports.contains(wrapper)) {
                addViolation(data, node, node.getImage());
            } else {
                imports.add(wrapper);
            }
        }
        return super.visit(node, data);
    }

}
