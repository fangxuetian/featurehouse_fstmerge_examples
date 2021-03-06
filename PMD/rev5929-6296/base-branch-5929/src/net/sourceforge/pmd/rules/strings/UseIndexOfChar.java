package net.sourceforge.pmd.rules.strings;

import net.sourceforge.pmd.rules.AbstractPoorMethodCall;


public class UseIndexOfChar extends AbstractPoorMethodCall {

    private static final String targetTypeName = "String";
    private static final String[] methodNames = new String[] { "indexOf", "lastIndexOf" };
    
    public UseIndexOfChar() {
        super();
    }

    
    protected String targetTypename() { 
        return targetTypeName;
    }

    
    protected String[] methodNames() {
        return methodNames;
    }

    
    protected boolean isViolationArgument(int argIndex, String arg) {
        
        return isSingleCharAsString(arg);
    }

}
