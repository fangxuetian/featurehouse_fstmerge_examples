package test.net.sourceforge.pmd.lang.java.rule.naming;

import org.junit.Before;

import test.net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class NamingRulesTest extends SimpleAggregatorTst {
    
    private static final String RULESET = "java-naming";

    @Before
    public void setUp() {
        addRule(RULESET, "AbstractNaming");
        addRule(RULESET, "AvoidDollarSigns");
        addRule(RULESET, "AvoidFieldNameMatchingMethodName");
        addRule(RULESET, "AvoidFieldNameMatchingTypeName");
        addRule(RULESET, "BooleanGetMethodName");
        addRule(RULESET, "ClassNamingConventions");
        addRule(RULESET, "LongVariable");
        addRule(RULESET, "MethodNamingConventions");
        addRule(RULESET, "MethodWithSameNameAsEnclosingClass");
        addRule(RULESET, "MisleadingVariableName");
        addRule(RULESET, "NoPackage");
        addRule(RULESET, "PackageCase");
        addRule(RULESET, "ShortMethodName");
        addRule(RULESET, "ShortClassName");
        addRule(RULESET, "ShortVariable");
        addRule(RULESET, "SuspiciousConstantFieldName");
        addRule(RULESET, "SuspiciousEqualsMethodName");
        addRule(RULESET, "SuspiciousHashcodeMethodName");
        addRule(RULESET, "VariableNamingConventions");
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(NamingRulesTest.class);
    }
}
