
package test.net.sourceforge.pmd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.JUnit4TestAdapter;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RulePriority;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSetFactory;
import net.sourceforge.pmd.RuleSetNotFoundException;
import net.sourceforge.pmd.RuleSetWriter;
import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.java.rule.unusedcode.UnusedLocalVariableRule;
import net.sourceforge.pmd.lang.rule.RuleReference;
import net.sourceforge.pmd.lang.rule.XPathRule;
import net.sourceforge.pmd.util.ResourceLoader;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import test.net.sourceforge.pmd.testframework.TestDescriptor;

public class RuleSetFactoryTest {

    private boolean isJdk14;

    @Before
    public void setUp() {
	try {
	    Class.forName("java.lang.Appendable");
	} catch (Throwable t) {
	    isJdk14 = true;
	}
    }

    @Test
    public void testRuleSetFileName() throws RuleSetNotFoundException {
	RuleSet rs = loadRuleSet(EMPTY_RULESET);
	assertNull("RuleSet file name not expected", rs.getFileName());

	RuleSetFactory rsf = new RuleSetFactory();
	rs = rsf.createSingleRuleSet("rulesets/basic.xml");
	assertEquals("wrong RuleSet file name", rs.getFileName(), "rulesets/basic.xml");
    }

    @Test
    public void testNoRuleSetFileName() {
	RuleSet rs = loadRuleSet(EMPTY_RULESET);
	assertNull("RuleSet file name not expected", rs.getFileName());
    }

    @Test
    public void testRefs() throws Throwable {
	InputStream in = ResourceLoader
		.loadResourceAsStream("rulesets/favorites.xml", this.getClass().getClassLoader());
	if (in == null) {
	    throw new RuleSetNotFoundException(
		    "Can't find resource   Make sure the resource is a valid file or URL or is on the CLASSPATH.  Here's the current classpath: "
			    + System.getProperty("java.class.path"));
	}
	RuleSetFactory rsf = new RuleSetFactory();
	RuleSet rs = rsf.createSingleRuleSet("rulesets/favorites.xml");
	assertNotNull(rs.getRuleByName("WhileLoopsMustUseBraces"));
    }

    @Test(expected = RuleSetNotFoundException.class)
    public void testRuleSetNotFound() throws RuleSetNotFoundException {
	RuleSetFactory rsf = new RuleSetFactory();
	rsf.createSingleRuleSet("fooooo");
    }

    @Test
    public void testCreateEmptyRuleSet() {
	RuleSet rs = loadRuleSet(EMPTY_RULESET);
	assertEquals("test", rs.getName());
	assertEquals(0, rs.size());
    }

    @Test
    public void testSingleRule() {
	RuleSet rs = loadRuleSet(SINGLE_RULE);
	assertEquals(1, rs.size());
	Rule r = rs.getRules().iterator().next();
	assertEquals("MockRuleName", r.getName());
	assertEquals("net.sourceforge.pmd.lang.rule.MockRule", r.getRuleClass());
	assertEquals("avoid the mock rule", r.getMessage());
    }

    @Test
    public void testMultipleRules() {
	RuleSet rs = loadRuleSet(MULTIPLE_RULES);
	assertEquals(2, rs.size());
	Set<String> expected = new HashSet<String>();
	expected.add("MockRuleName1");
	expected.add("MockRuleName2");
	for (Iterator<Rule> i = rs.getRules().iterator(); i.hasNext();) {
	    assertTrue(expected.contains(i.next().getName()));
	}
    }

    @Test
    public void testSingleRuleWithPriority() {
	assertEquals(RulePriority.MEDIUM, loadFirstRule(PRIORITY).getPriority());
    }

    @Test
    public void testProps() {
	Rule r = loadFirstRule(PROPERTIES);
	assertTrue(r.hasProperty("foo"));
	assertEquals("bar", r.getStringProperty("foo"));
	assertEquals(2, r.getIntProperty("fooint"));
	assertTrue(r.hasProperty("fooBoolean"));
	assertTrue(r.getBooleanProperty("fooBoolean"));
	assertTrue(r.hasProperty("fooDouble"));
	assertEquals(1.0, r.getDoubleProperty("fooDouble"), 0.05);
	assertTrue(!r.hasProperty("BuggleFish"));
	assertTrue(r.getDescription().indexOf("testdesc2") != -1);
    }

    @Test
    public void testXPathPluginnameProperty() {
	Rule r = loadFirstRule(XPATH_PLUGINNAME);
	assertTrue(r.hasProperty("pluginname"));
    }

    @Test
    public void testXPath() {
	Rule r = loadFirstRule(XPATH);
	assertTrue(r.hasProperty("xpath"));
	assertTrue(r.getStringProperty("xpath").indexOf(" //Block ") != -1);
    }

    @Test
    public void testFacadesOffByDefault() {
	Rule r = loadFirstRule(XPATH);
	assertFalse(r.usesDFA());
    }

    @Test
    public void testDFAFlag() {
	assertTrue(loadFirstRule(DFA).usesDFA());
    }

    @Test
    public void testExternalReferenceOverride() {
	Rule r = loadFirstRule(REF_OVERRIDE);
	assertEquals("TestNameOverride", r.getName());
	assertEquals("Test message override", r.getMessage());
	assertEquals("Test description override", r.getDescription());
	assertEquals("Test example override", r.getExample());
	assertEquals("Test that both example are stored", 2, r.getExamples().size());
	assertEquals(RulePriority.MEDIUM, r.getPriority());
	assertTrue(r.hasProperty("test2"));
	assertEquals("override2", r.getStringProperty("test2"));
	assertTrue(r.hasProperty("test3"));
	assertEquals("override3", r.getStringProperty("test3"));
	assertTrue(r.hasProperty("test4"));
	assertEquals("new property", r.getStringProperty("test4"));
    }

    @Test
    public void testOverrideMessage() {
	Rule r = loadFirstRule(REF_OVERRIDE_ORIGINAL_NAME);
	assertEquals("TestMessageOverride", r.getMessage());
    }

    @Test
    public void testOverrideMessageOneElem() {
	Rule r = loadFirstRule(REF_OVERRIDE_ORIGINAL_NAME_ONE_ELEM);
	assertEquals("TestMessageOverride", r.getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectExternalRef() throws IllegalArgumentException {
	loadFirstRule(REF_MISPELLED_XREF);
    }

    @Test
    public void testSetPriority() {
	RuleSetFactory rsf = new RuleSetFactory();
	rsf.setMinimumPriority(RulePriority.MEDIUM_HIGH);
	assertEquals(0, rsf.createRuleSet(new ByteArrayInputStream(SINGLE_RULE.getBytes())).size());
	rsf.setMinimumPriority(RulePriority.MEDIUM_LOW);
	assertEquals(1, rsf.createRuleSet(new ByteArrayInputStream(SINGLE_RULE.getBytes())).size());
    }

    @Test
    public void testLanguage() {
	Rule r = loadFirstRule(LANGUAGE);
	assertEquals(Language.JAVA, r.getLanguage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectLanguage() {
	loadFirstRule(INCORRECT_LANGUAGE);
    }

    @Test
    public void testMinimumLanugageVersion() {
	Rule r = loadFirstRule(MINIMUM_LANGUAGE_VERSION);
	assertEquals(LanguageVersion.JAVA_14, r.getMinimumLanguageVersion());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectMinimumLanugageVersion() {
	loadFirstRule(INCORRECT_MINIMUM_LANGUAGE_VERSION);
    }

    @Test
    public void testMaximumLanugageVersion() {
	Rule r = loadFirstRule(MAXIMUM_LANGUAGE_VERSION);
	assertEquals(LanguageVersion.JAVA_17, r.getMaximumLanguageVersion());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectMaximumLanugageVersion() {
	loadFirstRule(INCORRECT_MAXIMUM_LANGUAGE_VERSION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvertedMinimumMaximumLanugageVersions() {
	loadFirstRule(INVERTED_MINIMUM_MAXIMUM_LANGUAGE_VERSIONS);
    }

    @Test
    public void testDirectDeprecatedRule() {
	Rule r = loadFirstRule(DIRECT_DEPRECATED_RULE);
	assertNotNull("Direct Deprecated Rule", r);
    }

    @Test
    public void testReferenceToDeprecatedRule() {
	Rule r = loadFirstRule(REFERENCE_TO_DEPRECATED_RULE);
	assertNotNull("Reference to Deprecated Rule", r);
	assertTrue("Rule Reference", r instanceof RuleReference);
	assertFalse("Not deprecated", r.isDeprecated());
	assertTrue("Original Rule Deprecated", ((RuleReference)r).getRule().isDeprecated());
	assertEquals("Rule name", r.getName(), DEPRECATED_RULE_NAME);
    }

    @Test
    public void testRuleSetReferenceWithDeprecatedRule() {
	RuleSet ruleSet = loadRuleSet(REFERENCE_TO_RULESET_WITH_DEPRECATED_RULE);
	assertNotNull("RuleSet", ruleSet);
	assertFalse("RuleSet empty", ruleSet.getRules().isEmpty());
	
	Rule r = ruleSet.getRuleByName(DEPRECATED_RULE_NAME);
	assertNull("Deprecated Rule Reference", r);
	for (Rule rule: ruleSet.getRules()) {
	    assertFalse("Rule not deprecated", rule.isDeprecated());
	}
    }

    @Test
    public void testIncludeExcludePatterns() {
	RuleSet ruleSet = loadRuleSet(INCLUDE_EXCLUDE_RULESET);

	assertNotNull("Include patterns", ruleSet.getIncludePatterns());
	assertEquals("Include patterns size", 2, ruleSet.getIncludePatterns().size());
	assertEquals("Include pattern #1", "include1", ruleSet.getIncludePatterns().get(0));
	assertEquals("Include pattern #2", "include2", ruleSet.getIncludePatterns().get(1));

	assertNotNull("Exclude patterns", ruleSet.getExcludePatterns());
	assertEquals("Exclude patterns size", 3, ruleSet.getExcludePatterns().size());
	assertEquals("Exclude pattern #1", "exclude1", ruleSet.getExcludePatterns().get(0));
	assertEquals("Exclude pattern #2", "exclude2", ruleSet.getExcludePatterns().get(1));
	assertEquals("Exclude pattern #3", "exclude3", ruleSet.getExcludePatterns().get(2));
    }

    @Test
    public void testAllPMDBuiltInRulesMeetConventions() throws IOException, RuleSetNotFoundException,
	    ParserConfigurationException, SAXException {
	int invalidSinceAttributes = 0;
	int invalidExternalInfoURL = 0;
	int invalidClassName = 0;
	int invalidRegexSuppress = 0;
	int invalidXPathSuppress = 0;
	String messages = "";
	
	List<String> ruleSetFileNames = getRuleSetFileNames();
	for (String fileName : ruleSetFileNames) {
	    RuleSet ruleSet = loadRuleSetByFileName(fileName);
	    for (Rule rule : ruleSet.getRules()) {
		Language language = ruleSet.getLanguage();
		if (language == null) {
		    language = Language.JAVA;
		}
		String group = fileName.substring(fileName.indexOf('/') + 1);
		group = group.substring(0, group.indexOf(".xml"));
		if (group.indexOf('-') >= 0) {
		    group = group.substring(0, group.indexOf('-'));
		}

		
		if (rule.getSince() == null) {
		    invalidSinceAttributes++;
		    messages += "Rule " + fileName + "/" + rule.getName() + " is missing 'since' attribute" + PMD.EOL;
		}
		
		if (rule.getExternalInfoUrl() == null || "".equalsIgnoreCase(rule.getExternalInfoUrl())) {
		    invalidExternalInfoURL++;
		    messages += "Rule " + fileName + "/" + rule.getName() + " is missing 'externalInfoURL' attribute"
			    + PMD.EOL;
		} else {
		    String expectedExternalInfoURL = "http://pmd.sourceforge.net/rules/"
			    + fileName.replaceAll("rulesets/", "").replaceAll(".xml", "") + ".html#" + rule.getName();
		    if (!expectedExternalInfoURL.equals(rule.getExternalInfoUrl())) {
			invalidExternalInfoURL++;
			messages += "Rule " + fileName + "/" + rule.getName()
				+ " seems to have an invalid 'externalInfoURL' value (" + rule.getExternalInfoUrl()
				+ "), it should be:" + expectedExternalInfoURL + PMD.EOL;
		    }
		}
		
		String expectedClassName = "net.sourceforge.pmd.lang." + language.getTerseName() + ".rule." + group
			+ "." + rule.getName() + "Rule";
		if (!rule.getRuleClass().equals(expectedClassName)
			&& !rule.getRuleClass().equals(XPathRule.class.getName())) {
		    invalidClassName++;
		    messages += "Rule " + fileName + "/" + rule.getName() + " seems to have an invalid 'class' value ("
			    + rule.getRuleClass() + "), it should be:" + expectedClassName + PMD.EOL;
		}
		
		if (rule.hasProperty(Rule.VIOLATION_SUPPRESS_REGEX_PROPERTY)) {
		    invalidRegexSuppress++;
		    messages += "Rule " + fileName + "/" + rule.getName() + " should not have '"
			    + Rule.VIOLATION_SUPPRESS_REGEX_PROPERTY
			    + "', this is intended for end user customization only." + PMD.EOL;
		}
		
		if (rule.hasProperty(Rule.VIOLATION_SUPPRESS_XPATH_PROPERTY)) {
		    invalidXPathSuppress++;
		    messages += "Rule " + fileName + "/" + rule.getName() + " should not have '"
			    + Rule.VIOLATION_SUPPRESS_XPATH_PROPERTY
			    + "', this is intended for end user customization only." + PMD.EOL;
		}
	    }
	}
	
	if (invalidSinceAttributes > 0 || invalidExternalInfoURL > 0 || invalidClassName > 0
		|| invalidRegexSuppress > 0 || invalidXPathSuppress > 0) {
	    fail("All built-in PMD rules need 'since' attribute (" + invalidSinceAttributes
		    + " are missing), a proper ExternalURLInfo (" + invalidExternalInfoURL
		    + " are invalid), a class name meeting conventions (" + invalidClassName + " are invalid), no '"
		    + Rule.VIOLATION_SUPPRESS_REGEX_PROPERTY + "' property (" + invalidRegexSuppress
		    + " are invalid), and no '" + Rule.VIOLATION_SUPPRESS_XPATH_PROPERTY + "' property ("
		    + invalidXPathSuppress + " are invalid)" + PMD.EOL + messages);
	}
    }

    @Test
    public void testXmlSchema() throws IOException, RuleSetNotFoundException, ParserConfigurationException,
	    SAXException {
	if (isJdk14) {
	    
	    return;
	}

	boolean allValid = true;
	List<String> ruleSetFileNames = getRuleSetFileNames();
	for (String fileName : ruleSetFileNames) {
	    boolean valid = validateAgainstSchema(fileName);
	    allValid = allValid && valid;
	}
	assertTrue("All XML must parse without producing validation messages.", allValid);
    }

    @Test
    public void testDtd() throws IOException, RuleSetNotFoundException, ParserConfigurationException, SAXException {
	boolean allValid = true;
	List<String> ruleSetFileNames = getRuleSetFileNames();
	for (String fileName : ruleSetFileNames) {
	    boolean valid = validateAgainstDtd(fileName);
	    allValid = allValid && valid;
	}
	assertTrue("All XML must parse without producing validation messages.", allValid);
    }

    @Test
    public void testReadWriteRoundTrip() throws IOException, RuleSetNotFoundException, ParserConfigurationException,
	    SAXException {

	List<String> ruleSetFileNames = getRuleSetFileNames();
	for (String fileName : ruleSetFileNames) {
	    testRuleSet(fileName);
	}
    }

    @Test
    public void testWindowsJdk14Bug() throws IOException, RuleSetNotFoundException, ParserConfigurationException,
	    SAXException {

	if (TestDescriptor.inRegressionTestMode()) {
	    
	    return;
	}
	
	testRuleSet("regress/test/net/sourceforge/pmd/xml/j2ee.xml");
    }

    public void testRuleSet(String fileName) throws IOException, RuleSetNotFoundException,
	    ParserConfigurationException, SAXException {

	
	String xml1 = readFullyToString(ResourceLoader.loadResourceAsStream(fileName));

	
	RuleSet ruleSet1 = loadRuleSetByFileName(fileName);

	
	ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
	RuleSetWriter writer1 = new RuleSetWriter(outputStream1);
	writer1.write(ruleSet1);
	writer1.close();
	String xml2 = new String(outputStream1.toByteArray());

	
	RuleSetFactory ruleSetFactory = new RuleSetFactory();
	RuleSet ruleSet2 = ruleSetFactory.createRuleSet(new ByteArrayInputStream(outputStream1.toByteArray()));

	

	
	ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
	RuleSetWriter writer2 = new RuleSetWriter(outputStream2);
	writer2.write(ruleSet2);
	writer2.close();
	String xml3 = new String(outputStream2.toByteArray());

	
	
	

	
	RuleSet ruleSet3 = ruleSetFactory.createRuleSet(new ByteArrayInputStream(outputStream2.toByteArray()));

	
	if (!isJdk14) {
	    assertTrue("1st roundtrip RuleSet XML is not valid against Schema",
		    validateAgainstSchema(new ByteArrayInputStream(xml2.getBytes())));
	    assertTrue("2nd roundtrip RuleSet XML is not valid against Schema",
		    validateAgainstSchema(new ByteArrayInputStream(xml3.getBytes())));
	}
	assertTrue("1st roundtrip RuleSet XML is not valid against DTD", validateAgainstDtd(new ByteArrayInputStream(
		xml2.getBytes())));
	assertTrue("2nd roundtrip RuleSet XML is not valid against DTD", validateAgainstDtd(new ByteArrayInputStream(
		xml3.getBytes())));

	
	assertEqualsRuleSet("Original RuleSet and 1st roundtrip Ruleset not the same", ruleSet1, ruleSet2);
	assertEqualsRuleSet("1st roundtrip Ruleset and 2nd roundtrip RuleSet not the same", ruleSet2, ruleSet3);

	
	assertEquals("1st roundtrip RuleSet XML and 2nd roundtrip RuleSet XML", xml2, xml3);
    }

    private void assertEqualsRuleSet(String message, RuleSet ruleSet1, RuleSet ruleSet2) {
	assertEquals(message + ", RuleSet name", ruleSet1.getName(), ruleSet2.getName());
	assertEquals(message + ", RuleSet description", ruleSet1.getDescription(), ruleSet2.getDescription());
	assertEquals(message + ", RuleSet language", ruleSet1.getLanguage(), ruleSet2.getLanguage());
	assertEquals(message + ", RuleSet exclude patterns", ruleSet1.getExcludePatterns(), ruleSet2
		.getExcludePatterns());
	assertEquals(message + ", RuleSet include patterns", ruleSet1.getIncludePatterns(), ruleSet2
		.getIncludePatterns());
	assertEquals(message + ", RuleSet rule count", ruleSet1.getRules().size(), ruleSet2.getRules().size());

	for (int i = 0; i < ruleSet1.getRules().size(); i++) {
	    Rule rule1 = ((List<Rule>) ruleSet1.getRules()).get(i);
	    Rule rule2 = ((List<Rule>) ruleSet2.getRules()).get(i);

	    assertFalse(message + ", Different RuleReference",
		    ((rule1 instanceof RuleReference) && !(rule2 instanceof RuleReference))
			    || (!(rule1 instanceof RuleReference) && (rule2 instanceof RuleReference)));

	    if (rule1 instanceof RuleReference) {
		RuleReference ruleReference1 = (RuleReference) rule1;
		RuleReference ruleReference2 = (RuleReference) rule2;
		assertEquals(message + ", RuleReference overridden language", ruleReference1.getOverriddenLanguage(),
			ruleReference2.getOverriddenLanguage());
		assertEquals(message + ", RuleReference overridden minimum language version", ruleReference1.getOverriddenMinimumLanguageVersion(),
			ruleReference2.getOverriddenMinimumLanguageVersion());
		assertEquals(message + ", RuleReference overridden maximum language version", ruleReference1.getOverriddenMaximumLanguageVersion(),
			ruleReference2.getOverriddenMaximumLanguageVersion());
		assertEquals(message + ", RuleReference overridden deprecated", ruleReference1.isOverriddenDeprecated(),
			ruleReference2.isOverriddenDeprecated());
		assertEquals(message + ", RuleReference overridden name", ruleReference1.getOverriddenName(),
			ruleReference2.getOverriddenName());
		assertEquals(message + ", RuleReference overridden description", ruleReference1
			.getOverriddenDescription(), ruleReference2.getOverriddenDescription());
		assertEquals(message + ", RuleReference overridden message", ruleReference1.getOverriddenMessage(),
			ruleReference2.getOverriddenMessage());
		assertEquals(message + ", RuleReference overridden external info url", ruleReference1
			.getOverriddenExternalInfoUrl(), ruleReference2.getOverriddenExternalInfoUrl());
		assertEquals(message + ", RuleReference overridden priority", ruleReference1.getOverriddenPriority(),
			ruleReference2.getOverriddenPriority());
		assertEquals(message + ", RuleReference overridden examples", ruleReference1.getOverriddenExamples(),
			ruleReference2.getOverriddenExamples());
		assertEquals(message + ", RuleReference overridden properties", ruleReference1
			.getOverriddenProperties(), ruleReference2.getOverriddenProperties());
	    }

	    assertEquals(message + ", Rule name", rule1.getName(), rule2.getName());
	    assertEquals(message + ", Rule class", rule1.getRuleClass(), rule2.getRuleClass());
	    assertEquals(message + ", Rule description " + rule1.getName(), rule1.getDescription(), rule2
		    .getDescription());
	    assertEquals(message + ", Rule message", rule1.getMessage(), rule2.getMessage());
	    assertEquals(message + ", Rule external info url", rule1.getExternalInfoUrl(), rule2.getExternalInfoUrl());
	    assertEquals(message + ", Rule priority", rule1.getPriority(), rule2.getPriority());
	    assertEquals(message + ", Rule examples", rule1.getExamples(), rule2.getExamples());
	    for (Object key : rule1.getProperties().keySet()) {
		assertEquals(message + ", Rule property " + key, rule1.getProperties().get(key), rule2.getProperties()
			.get(key));
	    }
	    assertEquals(message + ", Rule properties", rule1.getProperties(), rule2.getProperties());
	}
    }

    private boolean validateAgainstSchema(String fileName) throws IOException, RuleSetNotFoundException,
	    ParserConfigurationException, SAXException {
	InputStream inputStream = loadResourceAsStream(fileName);
	boolean valid = validateAgainstSchema(inputStream);
	if (!valid) {
	    System.err.println("Validation against XML Schema failed for: " + fileName);
	}
	return valid;
    }

    private boolean validateAgainstSchema(InputStream inputStream) throws IOException, RuleSetNotFoundException,
	    ParserConfigurationException, SAXException {
	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	saxParserFactory.setValidating(true);
	saxParserFactory.setNamespaceAware(true);

	
	
	saxParserFactory.setFeature("http://xml.org/sax/features/validation", true);
	saxParserFactory.setFeature("http://apache.org/xml/features/validation/schema", true);
	saxParserFactory.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);

	SAXParser saxParser = saxParserFactory.newSAXParser();
	ValidateDefaultHandler validateDefaultHandler = new ValidateDefaultHandler("etc/ruleset_xml_schema.xsd");
	saxParser.parse(inputStream, validateDefaultHandler);
	inputStream.close();
	return validateDefaultHandler.isValid();
    }

    private boolean validateAgainstDtd(String fileName) throws IOException, RuleSetNotFoundException,
	    ParserConfigurationException, SAXException {
	InputStream inputStream = loadResourceAsStream(fileName);
	boolean valid = validateAgainstDtd(inputStream);
	if (!valid) {
	    System.err.println("Validation against DTD failed for: " + fileName);
	}
	return valid;
    }

    private boolean validateAgainstDtd(InputStream inputStream) throws IOException, RuleSetNotFoundException,
	    ParserConfigurationException, SAXException {
	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	saxParserFactory.setValidating(true);
	saxParserFactory.setNamespaceAware(true);

	
	String file = readFullyToString(inputStream);

	
	file = file.replaceAll("<\\?xml [ a-zA-Z0-9=\".-]*\\?>", "");
	file = file.replaceAll("xmlns=\"http://pmd.sf.net/ruleset/1.0.0\"", "");
	file = file.replaceAll("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
	file = file.replaceAll(
		"xsi:schemaLocation=\"http://pmd.sf.net/ruleset/1.0.0 http://pmd.sf.net/ruleset_xml_schema.xsd\"", "");
	file = file.replaceAll("xsi:noNamespaceSchemaLocation=\"http://pmd.sf.net/ruleset_xml_schema.xsd\"", "");

	file = "<?xml version=\"1.0\"?>" + PMD.EOL + "<!DOCTYPE ruleset SYSTEM \"file://"
		+ System.getProperty("user.dir") + "/etc/ruleset.dtd\">" + PMD.EOL + file;

	inputStream = new ByteArrayInputStream(file.getBytes());

	SAXParser saxParser = saxParserFactory.newSAXParser();
	ValidateDefaultHandler validateDefaultHandler = new ValidateDefaultHandler("etc/ruleset.dtd");
	saxParser.parse(inputStream, validateDefaultHandler);
	inputStream.close();
	return validateDefaultHandler.isValid();
    }

    private String readFullyToString(InputStream inputStream) throws IOException {
	StringBuffer buf = new StringBuffer(64 * 1024);
	BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	String line;
	while ((line = reader.readLine()) != null) {
	    buf.append(line);
	    buf.append(PMD.EOL);
	}
	reader.close();
	return buf.toString();
    }

    
    private List<String> getRuleSetFileNames() throws IOException, RuleSetNotFoundException {
	Properties properties = new Properties();
	properties.load(ResourceLoader.loadResourceAsStream("rulesets/rulesets.properties"));
	String fileNames = properties.getProperty("rulesets.testnames");
	StringTokenizer st = new StringTokenizer(fileNames, ",");
	List<String> ruleSetFileNames = new ArrayList<String>();
	while (st.hasMoreTokens()) {
	    ruleSetFileNames.add(st.nextToken());
	}
	return ruleSetFileNames;
    }

    private class ValidateDefaultHandler extends DefaultHandler {
	private final String validateDocument;
	private boolean valid = true;

	public ValidateDefaultHandler(String validateDocument) {
	    this.validateDocument = validateDocument;
	}

	public boolean isValid() {
	    return valid;
	}

	public void error(SAXParseException e) throws SAXException {
	    log("Error", e);
	}

	public void fatalError(SAXParseException e) throws SAXException {
	    log("FatalError", e);
	}

	public void warning(SAXParseException e) throws SAXException {
	    log("Warning", e);
	}

	private void log(String prefix, SAXParseException e) {
	    String message = prefix + " at (" + e.getLineNumber() + ", " + e.getColumnNumber() + "): " + e.getMessage();
	    System.err.println(message);
	    valid = false;
	}

	public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
	    if ("http://pmd.sf.net/ruleset_xml_schema.xsd".equals(systemId) || systemId.endsWith("ruleset.dtd")) {
		try {
		    InputStream inputStream = loadResourceAsStream(validateDocument);
		    return new InputSource(inputStream);
		} catch (RuleSetNotFoundException e) {
		    System.err.println(e.getMessage());
		    throw new IOException(e.getMessage());
		}
	    } else {
		throw new IllegalArgumentException("No clue how to handle: publicId=" + publicId + ", systemId="
			+ systemId);
	    }
	}
    }

    private InputStream loadResourceAsStream(String resource) throws RuleSetNotFoundException {
	InputStream inputStream = ResourceLoader.loadResourceAsStream(resource, this.getClass().getClassLoader());
	if (inputStream == null) {
	    throw new RuleSetNotFoundException(
		    "Can't find resource "
			    + resource
			    + "  Make sure the resource is a valid file or URL or is on the CLASSPATH.  Here's the current classpath: "
			    + System.getProperty("java.class.path"));
	}
	return inputStream;
    }

    private static final String REF_OVERRIDE_ORIGINAL_NAME = "<?xml version=\"1.0\"?>" + PMD.EOL
	    + "<ruleset name=\"test\">" + PMD.EOL + " <description>testdesc</description>" + PMD.EOL + " <rule "
	    + PMD.EOL + "  ref=\"rulesets/unusedcode.xml/UnusedLocalVariable\" message=\"TestMessageOverride\"> "
	    + PMD.EOL + " </rule>" + PMD.EOL + "</ruleset>";

    private static final String REF_MISPELLED_XREF = "<?xml version=\"1.0\"?>" + PMD.EOL + "<ruleset name=\"test\">"
	    + PMD.EOL + " <description>testdesc</description>" + PMD.EOL + " <rule " + PMD.EOL
	    + "  ref=\"rulesets/unusedcode.xml/FooUnusedLocalVariable\"> " + PMD.EOL + " </rule>" + PMD.EOL
	    + "</ruleset>";

    private static final String REF_OVERRIDE_ORIGINAL_NAME_ONE_ELEM = "<?xml version=\"1.0\"?>" + PMD.EOL
	    + "<ruleset name=\"test\">" + PMD.EOL + " <description>testdesc</description>" + PMD.EOL
	    + " <rule ref=\"rulesets/unusedcode.xml/UnusedLocalVariable\" message=\"TestMessageOverride\"/> " + PMD.EOL
	    + "</ruleset>";

    private static final String REF_OVERRIDE = "<?xml version=\"1.0\"?>" + PMD.EOL + "<ruleset name=\"test\">"
	    + PMD.EOL + " <description>testdesc</description>" + PMD.EOL + " <rule " + PMD.EOL
	    + "  ref=\"rulesets/unusedcode.xml/UnusedLocalVariable\" " + PMD.EOL + "  name=\"TestNameOverride\" "
	    + PMD.EOL + "  message=\"Test message override\"> " + PMD.EOL
	    + "  <description>Test description override</description>" + PMD.EOL
	    + "  <example>Test example override</example>" + PMD.EOL + "  <priority>3</priority>" + PMD.EOL
	    + "  <properties>" + PMD.EOL + "   <property name=\"test2\" value=\"override2\"/>" + PMD.EOL
	    + "   <property name=\"test3\"><value>override3</value></property>" + PMD.EOL
	    + "   <property name=\"test4\" value=\"new property\"/>" + PMD.EOL + "  </properties>" + PMD.EOL
	    + " </rule>" + PMD.EOL + "</ruleset>";

    private static final String EMPTY_RULESET = "<?xml version=\"1.0\"?>" + PMD.EOL + "<ruleset name=\"test\">"
	    + PMD.EOL + "<description>testdesc</description>" + PMD.EOL + "</ruleset>";

    private static final String SINGLE_RULE = "<?xml version=\"1.0\"?>" + PMD.EOL + "<ruleset name=\"test\">" + PMD.EOL
	    + "<description>testdesc</description>" + PMD.EOL + "<rule " + PMD.EOL + "name=\"MockRuleName\" " + PMD.EOL
	    + "message=\"avoid the mock rule\" " + PMD.EOL + "class=\"net.sourceforge.pmd.lang.rule.MockRule\">"
	    + "<priority>3</priority>" + PMD.EOL + "</rule></ruleset>";

    private static final String MULTIPLE_RULES = "<?xml version=\"1.0\"?>" + PMD.EOL + "<ruleset name=\"test\">"
	    + PMD.EOL + "<description>testdesc</description>" + PMD.EOL + "<rule name=\"MockRuleName1\" " + PMD.EOL
	    + "message=\"avoid the mock rule\" " + PMD.EOL + "class=\"net.sourceforge.pmd.lang.rule.MockRule\">"
	    + PMD.EOL + "</rule>" + PMD.EOL + "<rule name=\"MockRuleName2\" " + PMD.EOL
	    + "message=\"avoid the mock rule\" " + PMD.EOL + "class=\"net.sourceforge.pmd.lang.rule.MockRule\">"
	    + PMD.EOL + "</rule></ruleset>";

    private static final String PROPERTIES = "<?xml version=\"1.0\"?>" + PMD.EOL + "<ruleset name=\"test\">" + PMD.EOL
	    + "<description>testdesc</description>" + PMD.EOL + "<rule name=\"MockRuleName\" " + PMD.EOL
	    + "message=\"avoid the mock rule\" " + PMD.EOL + "class=\"net.sourceforge.pmd.lang.rule.MockRule\">"
	    + PMD.EOL + "<description>testdesc2</description>" + PMD.EOL + "<properties>" + PMD.EOL
	    + "<property name=\"fooBoolean\" value=\"true\"/>" + PMD.EOL
	    + "<property name=\"fooDouble\" value=\"1.0\" />" + PMD.EOL + "<property name=\"foo\" value=\"bar\"/>"
	    + PMD.EOL + "<property name=\"fooint\" value=\"2\"/>" + PMD.EOL + "</properties>" + PMD.EOL
	    + "</rule></ruleset>";

    private static final String XPATH = "<?xml version=\"1.0\"?>" + PMD.EOL + "<ruleset name=\"test\">" + PMD.EOL
	    + "<description>testdesc</description>" + PMD.EOL + "<priority>3</priority>" + PMD.EOL
	    + "<rule name=\"MockRuleName\" " + PMD.EOL + "message=\"avoid the mock rule\" " + PMD.EOL
	    + "class=\"net.sourceforge.pmd.lang.rule.MockRule\">" + PMD.EOL + "<description>testdesc2</description>"
	    + PMD.EOL + "<properties>" + PMD.EOL + "<property name=\"xpath\">" + PMD.EOL + "<value>" + PMD.EOL
	    + "<![CDATA[ //Block ]]>" + PMD.EOL + "</value>" + PMD.EOL + "</property>" + PMD.EOL + "</properties>"
	    + PMD.EOL + "</rule></ruleset>";

    private static final String XPATH_PLUGINNAME = "<?xml version=\"1.0\"?>" + PMD.EOL + "<ruleset name=\"test\">"
	    + PMD.EOL + "<description>testdesc</description>" + PMD.EOL + "<priority>3</priority>" + PMD.EOL
	    + "<rule name=\"MockRuleName\" " + PMD.EOL + "message=\"avoid the mock rule\" " + PMD.EOL
	    + "class=\"net.sourceforge.pmd.lang.rule.MockRule\">" + PMD.EOL + "<description>testdesc2</description>"
	    + PMD.EOL + "<properties>" + PMD.EOL + "<property name=\"xpath\" pluginname=\"true\">" + PMD.EOL
	    + "<value>" + PMD.EOL + "<![CDATA[ //Block ]]>" + PMD.EOL + "</value>" + PMD.EOL + "</property>" + PMD.EOL
	    + "</properties>" + PMD.EOL + "</rule></ruleset>";

    private static final String PRIORITY = "<?xml version=\"1.0\"?>" + PMD.EOL + "<ruleset name=\"test\">" + PMD.EOL
	    + "<description>testdesc</description>" + PMD.EOL + "<rule " + PMD.EOL + "name=\"MockRuleName\" " + PMD.EOL
	    + "message=\"avoid the mock rule\" " + PMD.EOL + "class=\"net.sourceforge.pmd.lang.rule.MockRule\">"
	    + "<priority>3</priority>" + PMD.EOL + "</rule></ruleset>";

    private static final String LANGUAGE = "<?xml version=\"1.0\"?>" + PMD.EOL + "<ruleset name=\"test\">" + PMD.EOL
	    + "<description>testdesc</description>" + PMD.EOL + "<rule " + PMD.EOL + "name=\"MockRuleName\" " + PMD.EOL
	    + "message=\"avoid the mock rule\" " + PMD.EOL
	    + "class=\"net.sourceforge.pmd.lang.rule.MockRule\" language=\"java\">" + PMD.EOL + "</rule></ruleset>";

    private static final String INCORRECT_LANGUAGE = "<?xml version=\"1.0\"?>" + PMD.EOL + "<ruleset name=\"test\">"
	    + PMD.EOL + "<description>testdesc</description>" + PMD.EOL + "<rule " + PMD.EOL + "name=\"MockRuleName\" "
	    + PMD.EOL + "message=\"avoid the mock rule\" " + PMD.EOL
	    + "class=\"net.sourceforge.pmd.lang.rule.MockRule\"" + PMD.EOL + " language=\"bogus\">" + PMD.EOL
	    + "</rule></ruleset>";

    private static final String MINIMUM_LANGUAGE_VERSION = "<?xml version=\"1.0\"?>" + PMD.EOL
	    + "<ruleset name=\"test\">" + PMD.EOL + "<description>testdesc</description>" + PMD.EOL + "<rule "
	    + PMD.EOL + "name=\"MockRuleName\" " + PMD.EOL + "message=\"avoid the mock rule\" " + PMD.EOL
	    + "class=\"net.sourceforge.pmd.lang.rule.MockRule\"" + PMD.EOL + " language=\"java\"" + PMD.EOL
	    + " minimumLanguageVersion=\"1.4\">" + PMD.EOL + "</rule></ruleset>";

    private static final String INCORRECT_MINIMUM_LANGUAGE_VERSION = "<?xml version=\"1.0\"?>" + PMD.EOL
	    + "<ruleset name=\"test\">" + PMD.EOL + "<description>testdesc</description>" + PMD.EOL + "<rule "
	    + PMD.EOL + "name=\"MockRuleName\" " + PMD.EOL + "message=\"avoid the mock rule\" " + PMD.EOL
	    + "class=\"net.sourceforge.pmd.lang.rule.MockRule\"" + PMD.EOL + " language=\"java\"" + PMD.EOL
	    + " minimumLanguageVersion=\"bogus\">" + PMD.EOL + "</rule></ruleset>";

    private static final String MAXIMUM_LANGUAGE_VERSION = "<?xml version=\"1.0\"?>" + PMD.EOL
	    + "<ruleset name=\"test\">" + PMD.EOL + "<description>testdesc</description>" + PMD.EOL + "<rule "
	    + PMD.EOL + "name=\"MockRuleName\" " + PMD.EOL + "message=\"avoid the mock rule\" " + PMD.EOL
	    + "class=\"net.sourceforge.pmd.lang.rule.MockRule\"" + PMD.EOL + " language=\"java\"" + PMD.EOL
	    + " maximumLanguageVersion=\"1.7\">" + PMD.EOL + "</rule></ruleset>";

    private static final String INCORRECT_MAXIMUM_LANGUAGE_VERSION = "<?xml version=\"1.0\"?>" + PMD.EOL
	    + "<ruleset name=\"test\">" + PMD.EOL + "<description>testdesc</description>" + PMD.EOL + "<rule "
	    + PMD.EOL + "name=\"MockRuleName\" " + PMD.EOL + "message=\"avoid the mock rule\" " + PMD.EOL
	    + "class=\"net.sourceforge.pmd.lang.rule.MockRule\"" + PMD.EOL + " language=\"java\"" + PMD.EOL
	    + " maximumLanguageVersion=\"bogus\">" + PMD.EOL + "</rule></ruleset>";

    private static final String INVERTED_MINIMUM_MAXIMUM_LANGUAGE_VERSIONS = "<?xml version=\"1.0\"?>" + PMD.EOL
	    + "<ruleset name=\"test\">" + PMD.EOL + "<description>testdesc</description>" + PMD.EOL + "<rule "
	    + PMD.EOL + "name=\"MockRuleName\" " + PMD.EOL + "message=\"avoid the mock rule\" " + PMD.EOL
	    + "class=\"net.sourceforge.pmd.lang.rule.MockRule\" " + PMD.EOL + "language=\"java\"" + PMD.EOL
	    + " minimumLanguageVersion=\"1.7\"" + PMD.EOL + "maximumLanguageVersion=\"1.4\">" + PMD.EOL
	    + "</rule></ruleset>";

    private static final String DIRECT_DEPRECATED_RULE = "<?xml version=\"1.0\"?>" + PMD.EOL
	    + "<ruleset name=\"test\">" + PMD.EOL + "<description>testdesc</description>" + PMD.EOL
	    + "<rule "
	    + PMD.EOL + "name=\"MockRuleName\" " + PMD.EOL + "message=\"avoid the mock rule\" " + PMD.EOL
	    + "class=\"net.sourceforge.pmd.lang.rule.MockRule\" deprecated=\"true\">" + PMD.EOL
	    + "</rule></ruleset>";

    
    private static final String DEPRECATED_RULE_RULESET_NAME = "rulesets/basic.xml";

    
    private static final String DEPRECATED_RULE_NAME = "EmptyCatchBlock";

    private static final String REFERENCE_TO_DEPRECATED_RULE = "<?xml version=\"1.0\"?>" + PMD.EOL
	    + "<ruleset name=\"test\">" + PMD.EOL + "<description>testdesc</description>" + PMD.EOL + "<rule "
	    + PMD.EOL + "ref=\"" + DEPRECATED_RULE_RULESET_NAME + "/" + DEPRECATED_RULE_NAME + "\">" + PMD.EOL
	    + "</rule></ruleset>";

    private static final String REFERENCE_TO_RULESET_WITH_DEPRECATED_RULE = "<?xml version=\"1.0\"?>" + PMD.EOL
	    + "<ruleset name=\"test\">" + PMD.EOL + "<description>testdesc</description>" + PMD.EOL + "<rule "
	    + PMD.EOL + "ref=\"" + DEPRECATED_RULE_RULESET_NAME + "\">" + PMD.EOL + "</rule></ruleset>";

    private static final String DFA = "<?xml version=\"1.0\"?>" + PMD.EOL + "<ruleset name=\"test\">" + PMD.EOL
	    + "<description>testdesc</description>" + PMD.EOL + "<rule " + PMD.EOL + "name=\"MockRuleName\" " + PMD.EOL
	    + "message=\"avoid the mock rule\" " + PMD.EOL + "dfa=\"true\" " + PMD.EOL
	    + "class=\"net.sourceforge.pmd.lang.rule.MockRule\">" + "<priority>3</priority>" + PMD.EOL
	    + "</rule></ruleset>";

    private static final String INCLUDE_EXCLUDE_RULESET = "<?xml version=\"1.0\"?>" + PMD.EOL
	    + "<ruleset name=\"test\">" + PMD.EOL + "<description>testdesc</description>" + PMD.EOL
	    + "<include-pattern>include1</include-pattern>" + PMD.EOL + "<include-pattern>include2</include-pattern>"
	    + PMD.EOL + "<exclude-pattern>exclude1</exclude-pattern>" + PMD.EOL
	    + "<exclude-pattern>exclude2</exclude-pattern>" + PMD.EOL + "<exclude-pattern>exclude3</exclude-pattern>"
	    + PMD.EOL + "</ruleset>";

    private static final String EXTERNAL_REFERENCE_RULE_SET = "<?xml version=\"1.0\"?>" + PMD.EOL
	    + "<ruleset name=\"test\">" + PMD.EOL + "<description>testdesc</description>" + PMD.EOL
	    + "<rule ref=\"rulesets/unusedcode.xml/UnusedLocalVariable\"/>" + PMD.EOL + "</ruleset>";

    private Rule loadFirstRule(String ruleSetXml) {
	RuleSet rs = loadRuleSet(ruleSetXml);
	return rs.getRules().iterator().next();
    }

    private RuleSet loadRuleSetByFileName(String ruleSetFileName) throws RuleSetNotFoundException {
	RuleSetFactory rsf = new RuleSetFactory();
	return rsf.createSingleRuleSet(ruleSetFileName);
    }

    private RuleSet loadRuleSet(String ruleSetXml) {
	RuleSetFactory rsf = new RuleSetFactory();
	return rsf.createRuleSet(new ByteArrayInputStream(ruleSetXml.getBytes()));
    }

    @Test
    public void testExternalReferences() {
	RuleSet rs = loadRuleSet(EXTERNAL_REFERENCE_RULE_SET);
	assertEquals(1, rs.size());
	assertEquals(UnusedLocalVariableRule.class.getName(), rs.getRuleByName("UnusedLocalVariable").getRuleClass());
    }

    public static junit.framework.Test suite() {
	return new JUnit4TestAdapter(RuleSetFactoryTest.class);
    }
}
