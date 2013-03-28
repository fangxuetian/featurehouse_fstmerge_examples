
package net.sourceforge.pmd.lang.xpath;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pmd.lang.ast.xpath.Attribute;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.SimpleFunctionContext;
import org.jaxen.XPathFunctionContext;


public class MatchesFunction implements Function {

    public static void registerSelfInSimpleContext() {
        
        ((SimpleFunctionContext) XPathFunctionContext.getInstance()).registerFunction(null, "matches", new MatchesFunction());
    }

    public Object call(Context context, List args) throws FunctionCallException {
        if (args.isEmpty()) {
            return Boolean.FALSE;
        }
        List attributes = (List) args.get(0);
        Attribute attr = (Attribute) attributes.get(0);

        for(int i = 1; i < args.size(); i++) {
            Pattern check = Pattern.compile((String) args.get(i));
            Matcher matcher = check.matcher(attr.getValue());
            if (matcher.find()) {
                return context.getNodeSet();
            }
        }
        return Boolean.FALSE;
    }
}
