package net.sourceforge.pmd.lang.rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
	private Properties properties;
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

	
	public Properties getOverriddenProperties() {
		return properties;
	}

	
	@Override
	public void addProperty(String name, String property) {
		
		if (!super.hasProperty(name) || !isSame(property, super.getStringProperty(name))) {
			if (this.properties == null) {
				this.properties = new Properties();
			}
			this.properties.put(name, property);
			super.addProperty(name, property);
		}
	}

	
	@Override
	public void addProperties(Properties properties) {
		
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			addProperty((String) entry.getKey(), (String) entry.getValue());
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
		return o1 == o2 || (o1 != null && o2 != null && o1.equals(o2));
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
