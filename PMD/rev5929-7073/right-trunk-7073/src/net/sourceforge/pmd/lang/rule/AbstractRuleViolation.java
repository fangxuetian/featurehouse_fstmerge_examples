
package net.sourceforge.pmd.lang.rule;

import java.util.regex.Pattern;

import net.sourceforge.pmd.PropertyDescriptor;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.lang.ast.Node;

public abstract class AbstractRuleViolation implements RuleViolation {

    protected Rule rule;
    protected String description;
    protected boolean suppressed;
    protected String filename;

    protected int beginLine;
    protected int beginColumn;

    protected int endLine;
    protected int endColumn;

    protected String packageName;
    protected String className;
    protected String methodName;
    protected String variableName;

    
    public AbstractRuleViolation(Rule rule, RuleContext ctx, Node node, String message) {
	this.rule = rule;
	this.description = message;
	this.filename = ctx.getSourceCodeFilename();
	if (this.filename == null) {
	    this.filename = "";
	}
	if (node != null) {
	    this.beginLine = node.getBeginLine();
	    this.beginColumn = node.getBeginColumn();
	    this.endLine = node.getEndLine();
	    this.endColumn = node.getEndColumn();
	}
	this.packageName = "";
	this.className = "";
	this.methodName = "";
	this.variableName = "";

	
	if (node != null && rule != null) {
	    
	    String regex = rule.getProperty(Rule.VIOLATION_SUPPRESS_REGEX_DESCRIPTOR);
	    if (regex != null && description != null) {
		if (Pattern.matches(regex, description)) {
		    suppressed = true;
		}
	    }

	    
	    if (!suppressed) {
		String xpath = rule.getProperty(Rule.VIOLATION_SUPPRESS_XPATH_DESCRIPTOR);
		if (xpath != null) {
		    suppressed = node.hasDescendantMatchingXPath(xpath);
		}
	    }
	}
    }

    protected String expandVariables(String message) {
	if (message.indexOf("${") >= 0) {
	    StringBuilder buf = new StringBuilder(message);
	    int startIndex = -1;
	    while ((startIndex = buf.indexOf("${", startIndex + 1)) >= 0) {
		final int endIndex = buf.indexOf("}", startIndex);
		if (endIndex >= 0) {
		    final String name = buf.substring(startIndex + 2, endIndex);
		    if (isVariable(name)) {
			buf.replace(startIndex, endIndex + 1, getVariableValue(name));
		    }
		}
	    }
	    return buf.toString();
	} else {
	    return message;
	}
    }

    protected boolean isVariable(String name) {
	return "variableName".equals(name) || "methodName".equals(name) || "className".equals(name)
		|| "packageName".equals(name) || rule.getPropertyDescriptor(name) != null;
    }

    protected String getVariableValue(String name) {
	if ("variableName".equals(name)) {
	    return variableName;
	} else if ("methodName".equals(name)) {
	    return methodName;
	} else if ("className".equals(name)) {
	    return className;
	} else if ("packageName".equals(name)) {
	    return packageName;
	} else {
	    final PropertyDescriptor<?> propertyDescriptor = rule.getPropertyDescriptor(name);
	    return String.valueOf(rule.getProperty(propertyDescriptor));
	}
    }

    public Rule getRule() {
	return rule;
    }

    public String getDescription() {
	return expandVariables(description);
    }

    public boolean isSuppressed() {
	return this.suppressed;
    }

    public String getFilename() {
	return filename;
    }

    public int getBeginLine() {
	return beginLine;
    }

    public int getBeginColumn() {
	return beginColumn;
    }

    public int getEndLine() {
	return endLine;
    }

    public int getEndColumn() {
	return endColumn;
    }

    public String getPackageName() {
	return packageName;
    }

    public String getClassName() {
	return className;
    }

    public String getMethodName() {
	return methodName;
    }

    public String getVariableName() {
	return variableName;
    }

    @Override
    public String toString() {
	return getFilename() + ':' + getRule() + ':' + getDescription() + ':' + beginLine;
    }
}
