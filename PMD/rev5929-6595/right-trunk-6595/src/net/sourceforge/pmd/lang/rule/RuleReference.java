package net.sourceforge.pmd.lang.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pmd.PropertyDescriptor;
import net.sourceforge.pmd.RulePriority;
import net.sourceforge.pmd.RuleSetReference;
import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.util.StringUtil;


public class RuleReference extends AbstractDelegateRule {
	private Language language;
	private LanguageVersion minimumLanguageVersion;
	private LanguageVersion maximumLanguageVersion;
	private Boolean deprecated;
	private String name;
	private List<PropertyDescriptor<?>> propertyDescriptors;
	private Map<PropertyDescriptor<?>, Object> propertyValues;
	private String message;
	private String description;
	private List<String> examples;
	private String externalInfoUrl;
	private RulePriority priority;
	private RuleSetReference ruleSetReference;

	public Language getOverriddenLanguage() {
		return language;
	}

	@Override
	public void setLanguage(Language language) {
		
		if (!isSame(language, super.getLanguage()) || this.language != null) {
			this.language = language;
			super.setLanguage(language);
		}
	}

	public LanguageVersion getOverriddenMinimumLanguageVersion() {
		return minimumLanguageVersion;
	}

	@Override
	public void setMinimumLanguageVersion(LanguageVersion minimumLanguageVersion) {
		
		if (!isSame(minimumLanguageVersion, super.getMinimumLanguageVersion()) || this.minimumLanguageVersion != null) {
			this.minimumLanguageVersion = minimumLanguageVersion;
			super.setMinimumLanguageVersion(minimumLanguageVersion);
		}
	}

	public LanguageVersion getOverriddenMaximumLanguageVersion() {
		return maximumLanguageVersion;
	}

	@Override
	public void setMaximumLanguageVersion(LanguageVersion maximumLanguageVersion) {
		
		if (!isSame(maximumLanguageVersion, super.getMaximumLanguageVersion()) || this.maximumLanguageVersion != null) {
			this.maximumLanguageVersion = maximumLanguageVersion;
			super.setMaximumLanguageVersion(maximumLanguageVersion);
		}
	}

	public Boolean isOverriddenDeprecated() {
		return deprecated;
	}

	@Override
	public boolean isDeprecated() {
		return deprecated != null && deprecated.booleanValue();
	}

	@Override
	public void setDeprecated(boolean deprecated) {
		
		
		this.deprecated = deprecated ? deprecated : null;
	}

	public String getOverriddenName() {
		return name;
	}

	@Override
	public void setName(String name) {
		
		if (!isSame(name, super.getName()) || this.name != null) {
			this.name = name;
			super.setName(name);
		}
	}

	public String getOverriddenMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		
		if (!isSame(message, super.getMessage()) || this.message != null) {
			this.message = message;
			super.setMessage(message);
		}
	}

	public String getOverriddenDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		
		if (!isSame(description, super.getDescription()) || this.description != null) {
			this.description = description;
			super.setDescription(description);
		}
	}

	public List<String> getOverriddenExamples() {
		return examples;
	}

	@Override
	public void addExample(String example) {
		
		
		
		
		
		
		
		

		
		if (!contains(super.getExamples(), example)) {
			if (this.examples == null) {
				this.examples = new ArrayList<String>(1);
			}
			
			this.examples.clear();
			this.examples.add(example);
			super.addExample(example);
		}
	}

	public String getOverriddenExternalInfoUrl() {
		return externalInfoUrl;
	}

	@Override
	public void setExternalInfoUrl(String externalInfoUrl) {
		
		if (!isSame(externalInfoUrl, super.getExternalInfoUrl()) || this.externalInfoUrl != null) {
			this.externalInfoUrl = externalInfoUrl;
			super.setExternalInfoUrl(externalInfoUrl);
		}
	}

	public RulePriority getOverriddenPriority() {
		return priority;
	}

	@Override
	public void setPriority(RulePriority priority) {
		
		if (priority != super.getPriority() || this.priority != null) {
			this.priority = priority;
			super.setPriority(priority);
		}
	}
	
    public List<PropertyDescriptor<?>> getOverriddenPropertyDescriptors() {
	return propertyDescriptors;
    }

    @Override
    public void definePropertyDescriptor(PropertyDescriptor<?> propertyDescriptor) throws IllegalArgumentException {
	
	
	
	super.definePropertyDescriptor(propertyDescriptor);
	if (this.propertyDescriptors == null) {
	    this.propertyDescriptors = new ArrayList<PropertyDescriptor<?>>();
	}
	this.propertyDescriptors.add(propertyDescriptor);
    }

    public Map<PropertyDescriptor<?>, Object> getOverriddenPropertiesByPropertyDescriptor() {
	return propertyValues;
    }

    @Override
    public <T> void setProperty(PropertyDescriptor<T> propertyDescriptor, T value) {
	
	if (!isSame(super.getProperty(propertyDescriptor), value)) {
	    if (this.propertyValues == null) {
		this.propertyValues = new HashMap<PropertyDescriptor<?>, Object>();
	    }
	    this.propertyValues.put(propertyDescriptor, value);
	    super.setProperty(propertyDescriptor, value);
	}
    }

	public RuleSetReference getRuleSetReference() {
		return ruleSetReference;
	}

	public void setRuleSetReference(RuleSetReference ruleSetReference) {
		this.ruleSetReference = ruleSetReference;
	}

	private static boolean isSame(String s1, String s2) {
		return StringUtil.isSame(s1, s2, true, false, true);
	}

	@SuppressWarnings("PMD.CompareObjectsWithEquals")
	private static boolean isSame(Object o1, Object o2) {
	    	if (o1 instanceof Object[] && o2 instanceof Object[]) {
	    	    return isSame((Object[])o1, (Object[])o2);
	    	}
		return o1 == o2 || (o1 != null && o2 != null && o1.equals(o2));
	}
	private static boolean isSame(Object[] a1, Object[] a2) {
		return a1 == a2 || (a1 != null && a2 != null && Arrays.equals(a1, a2));
	}

	private static boolean contains(Collection<String> collection, String s1) {
		for (String s2 : collection) {
			if (isSame(s1, s2)) {
				return true;
			}
		}
		return false;
	}
}
