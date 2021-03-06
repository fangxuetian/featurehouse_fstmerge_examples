
package net.sourceforge.pmd.lang.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.pmd.PropertyDescriptor;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RulePriority;
import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.ast.Node;




public abstract class AbstractRule implements Rule {

    
    private static final boolean IN_OLD_PROPERTY_MODE = true;

    private Language language;
    private LanguageVersion minimumLanguageVersion;
    private LanguageVersion maximumLanguageVersion;
    private boolean deprecated;
    private String name = getClass().getName();
    private String since;
    private String ruleClass = getClass().getName();
    private String ruleSetName;
    private String message;
    private String description;
    private List<String> examples = new ArrayList<String>();
    private String externalInfoUrl;
    private RulePriority priority = RulePriority.LOW;
    private Properties properties = new Properties();
    private boolean usesDFA;
    private boolean usesTypeResolution;
    private List<String> ruleChainVisits = new ArrayList<String>();

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
	if (this.language != null && this instanceof ImmutableLanguage && !this.language.equals(language)) {
	    throw new UnsupportedOperationException("The Language for Rule class " + this.getClass().getName()
		    + " is immutable and cannot be changed.");
	}
        this.language = language;
    }

    public LanguageVersion getMinimumLanguageVersion() {
        return minimumLanguageVersion;
    }

    public void setMinimumLanguageVersion(LanguageVersion minimumLanguageVersion) {
        this.minimumLanguageVersion = minimumLanguageVersion;
    }

    public LanguageVersion getMaximumLanguageVersion() {
        return maximumLanguageVersion;
    }

    public void setMaximumLanguageVersion(LanguageVersion maximumLanguageVersion) {
        this.maximumLanguageVersion = maximumLanguageVersion;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getSince() {
	return since;
    }

    public void setSince(String since) {
	this.since = since;
    }

    public String getRuleClass() {
	return ruleClass;
    }

    public void setRuleClass(String ruleClass) {
	this.ruleClass = ruleClass;
    }

    public String getRuleSetName() {
	return ruleSetName;
    }

    public void setRuleSetName(String ruleSetName) {
	this.ruleSetName = ruleSetName;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public List<String> getExamples() {
	
	return examples;
    }

    
    public String getExample() {
	if (examples.isEmpty()) {
	    return null;
	} else {
	    
	    return examples.get(examples.size() - 1);
	}
    }

    public void addExample(String example) {
	examples.add(example);
    }

    public String getExternalInfoUrl() {
	return externalInfoUrl;
    }

    public void setExternalInfoUrl(String externalInfoUrl) {
	this.externalInfoUrl = externalInfoUrl;
    }

    public RulePriority getPriority() {
	return priority;
    }

    public void setPriority(RulePriority priority) {
	this.priority = priority;
    }

    
    @Deprecated
    public Properties getProperties() {
	
	return properties;
    }

    
    @Deprecated
    public void addProperty(String name, String value) {
	getProperties().setProperty(name, value);
    }

    
    @Deprecated
    public void addProperties(Properties properties) {
	getProperties().putAll(properties);
    }

    
    @Deprecated
    public boolean hasProperty(String name) {
	return IN_OLD_PROPERTY_MODE ? 
	getProperties().containsKey(name)
		: propertiesByName().containsKey(name);
    }

    
    @Deprecated
    public boolean getBooleanProperty(String name) {
	return Boolean.parseBoolean(getProperties().getProperty(name));
    }

    public boolean getBooleanProperty(PropertyDescriptor descriptor) {

	return ((Boolean) getProperty(descriptor)).booleanValue();
    }

    
    public boolean[] getBooleanProperties(PropertyDescriptor descriptor) {
	Boolean[] values = (Boolean[]) getProperties(descriptor);
	boolean[] bools = new boolean[values.length];
	for (int i = 0; i < bools.length; i++) {
	    bools[i] = values[i].booleanValue();
	}
	return bools;
    }

    
    @Deprecated
    public int getIntProperty(String name) {
	return Integer.parseInt(getProperties().getProperty(name));
    }

    public int getIntProperty(PropertyDescriptor descriptor) {

	return ((Number) getProperty(descriptor)).intValue();
    }

    
    public int[] getIntProperties(PropertyDescriptor descriptor) {
	Number[] values = (Number[]) getProperties(descriptor);
	int[] ints = new int[values.length];
	for (int i = 0; i < ints.length; i++) {
	    ints[i] = values[i].intValue();
	}
	return ints;
    }

    
    @Deprecated
    public double getDoubleProperty(String name) {
	return Double.parseDouble(getProperties().getProperty(name));
    }

    public double getDoubleProperty(PropertyDescriptor descriptor) {
	return ((Number) getProperty(descriptor)).doubleValue();
    }

    
    public double[] getDoubleProperties(PropertyDescriptor descriptor) {
	Number[] values = (Number[]) getProperties(descriptor);
	double[] doubles = new double[values.length];
	for (int i = 0; i < doubles.length; i++) {
	    doubles[i] = values[i].doubleValue();
	}
	return doubles;
    }

    
    @Deprecated
    public String getStringProperty(String name) {
	return getProperties().getProperty(name);
    }

    public String getStringProperty(PropertyDescriptor descriptor) {
	return (String) getProperty(descriptor);
    }

    public String[] getStringProperties(PropertyDescriptor descriptor) {
	return (String[]) getProperties(descriptor);
    }

    public char getCharacterProperty(PropertyDescriptor descriptor) {
	return ((Character) getProperty(descriptor)).charValue();
    }

    public Class<?>[] getTypeProperties(PropertyDescriptor descriptor) {
	return (Class[]) getProperties(descriptor);
    }

    public Class<?> getTypeProperty(PropertyDescriptor descriptor) {
	return (Class<?>) getProperty(descriptor);
    }

    private Object getProperty(PropertyDescriptor descriptor) {
	if (descriptor.maxValueCount() > 1) {
	    propertyGetError(descriptor, true);
	}
	String rawValue = getProperties().getProperty(descriptor.name());
	return rawValue == null || rawValue.length() == 0 ? descriptor.defaultValue() : descriptor.valueFrom(rawValue);
    }

    public void setProperty(PropertyDescriptor descriptor, Object value) {
	if (descriptor.maxValueCount() > 1) {
	    propertySetError(descriptor, true);
	}
	getProperties().setProperty(descriptor.name(), descriptor.asDelimitedString(value));
    }

    private Object[] getProperties(PropertyDescriptor descriptor) {
	if (descriptor.maxValueCount() == 1) {
	    propertyGetError(descriptor, false);
	}
	String rawValue = getProperties().getProperty(descriptor.name());
	return rawValue == null || rawValue.length() == 0 ? (Object[]) descriptor.defaultValue()
		: (Object[]) descriptor.valueFrom(rawValue);
    }

    public void setProperties(PropertyDescriptor descriptor, Object[] values) {
	if (descriptor.maxValueCount() == 1) {
	    propertySetError(descriptor, false);
	}
	getProperties().setProperty(descriptor.name(), descriptor.asDelimitedString(values));
    }

    
    protected Map<String, PropertyDescriptor> propertiesByName() {
	return Collections.emptyMap();
    }

    public PropertyDescriptor propertyDescriptorFor(String name) {
	PropertyDescriptor descriptor = propertiesByName().get(name);
	if (descriptor == null) {
	    throw new IllegalArgumentException("Unknown property: " + name);
	}
	return descriptor;
    }

    private void propertyGetError(PropertyDescriptor descriptor, boolean requestedSingleValue) {

	if (requestedSingleValue) {
	    throw new RuntimeException("Cannot retrieve a single value from a multi-value property field");
	}
	throw new RuntimeException("Cannot retrieve multiple values from a single-value property field");
    }

    private void propertySetError(PropertyDescriptor descriptor, boolean setSingleValue) {

	if (setSingleValue) {
	    throw new RuntimeException("Cannot set a single value within a multi-value property field");
	}
	throw new RuntimeException("Cannot set multiple values within a single-value property field");
    }

    public void setUsesDFA() {
	this.usesDFA = true;
    }

    public boolean usesDFA() {
	return this.usesDFA;
    }

    public void setUsesTypeResolution() {
	this.usesTypeResolution = true;
    }

    public boolean usesTypeResolution() {
	return this.usesTypeResolution;
    }

    public boolean usesRuleChain() {
	return !getRuleChainVisits().isEmpty();
    }

    public List<String> getRuleChainVisits() {
	return ruleChainVisits;
    }

    public void addRuleChainVisit(Class<? extends Node> nodeClass) {
	if (!nodeClass.getSimpleName().startsWith("AST")) {
	    throw new IllegalArgumentException("Node class does not start with 'AST' prefix: " + nodeClass);
	}
	addRuleChainVisit(nodeClass.getSimpleName().substring("AST".length()));
    }

    public void addRuleChainVisit(String astNodeName) {
	if (!ruleChainVisits.contains(astNodeName)) {
	    ruleChainVisits.add(astNodeName);
	}
    }

    public void start(RuleContext ctx) {
	
    }

    public void end(RuleContext ctx) {
	
    }

    
    public final void addViolation(Object data, Node node) {
	RuleContext ruleContext = (RuleContext) data;
	ruleContext.getLanguageVersion().getLanguageVersionHandler().getRuleViolationFactory().addViolation(
		ruleContext, this, node);
    }

    
    public final void addViolation(Object data, Node node, String arg) {
	RuleContext ruleContext = (RuleContext) data;
	ruleContext.getLanguageVersion().getLanguageVersionHandler().getRuleViolationFactory().addViolation(
		ruleContext, this, node, arg);
    }

    
    public final void addViolation(Object data, Node node, Object[] args) {
	RuleContext ruleContext = (RuleContext) data;
	ruleContext.getLanguageVersion().getLanguageVersionHandler().getRuleViolationFactory().addViolation(
		ruleContext, this, node, args);
    }

    
    public final void addViolationWithMessage(Object data, Node node, String message) {
	RuleContext ruleContext = (RuleContext) data;
	ruleContext.getLanguageVersion().getLanguageVersionHandler().getRuleViolationFactory().addViolationWithMessage(
		ruleContext, this, node, message);
    }

    
    public final void addViolationWithMessage(Object data, Node node, String message, Object[] args) {
	RuleContext ruleContext = (RuleContext) data;
	ruleContext.getLanguageVersion().getLanguageVersionHandler().getRuleViolationFactory().addViolationWithMessage(
		ruleContext, this, node, message, args);
    }

    
    @Override
    public boolean equals(Object o) {
	if (o == null) {
	    return false; 
	}

	if (this == o) {
	    return true; 
	}

	boolean equality = this.getClass().getName().equals(o.getClass().getName());

	if (equality) {
	    Rule that = (Rule) o;
	    equality = this.getName().equals(that.getName()) && this.getPriority().equals(that.getPriority())
		    && this.getProperties().equals(that.getProperties());
	}

	return equality;
    }

    
    @Override
    public int hashCode() {
	return this.getClass().getName().hashCode() + (this.getName() != null ? this.getName().hashCode() : 0)
		+ this.getPriority().hashCode() + (this.getProperties() != null ? this.getProperties().hashCode() : 0);
    }

    public static Map<String, PropertyDescriptor> asFixedMap(PropertyDescriptor[] descriptors) {
	Map<String, PropertyDescriptor> descriptorsByName = new HashMap<String, PropertyDescriptor>(descriptors.length);
	for (PropertyDescriptor descriptor : descriptors) {
	    descriptorsByName.put(descriptor.name(), descriptor);
	}
	return Collections.unmodifiableMap(descriptorsByName);
    }

    public static Map<String, PropertyDescriptor> asFixedMap(PropertyDescriptor descriptor) {
	return asFixedMap(new PropertyDescriptor[] { descriptor });
    }
}
