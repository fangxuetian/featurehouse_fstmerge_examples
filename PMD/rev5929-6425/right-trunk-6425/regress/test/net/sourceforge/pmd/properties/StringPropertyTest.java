package test.net.sourceforge.pmd.properties;

import net.sourceforge.pmd.PropertyDescriptor;
import net.sourceforge.pmd.lang.rule.properties.StringProperty;


public class StringPropertyTest extends AbstractPropertyDescriptorTester {

	private static final int maxStringLength = 52;
	private static final char delimiter = '|';
	private static final char[] charSet = filter(allChars.toCharArray(), delimiter);
	
	public StringPropertyTest() {
		super();
	}
	
	
	protected Object createValue(int count) {

		if (count == 1) return newString();
		
		String[] values = new String[count];
		for (int i=0; i<count; i++) values[i] = (String)createValue(1);
		return values;
	}

	
	protected Object createBadValue(int count) {

		if (count == 1) return null;
		
		Object[] values = new Object[count];
		for (int i=0; i<count; i++) values[i] = createBadValue(1);
		return values;
	}
	
	
	private String newString() {
		
		int strLength = randomInt(0, maxStringLength);
		
		char[] chars = new char[strLength];
		for (int i=0; i<chars.length; i++) chars[i] = randomCharIn(charSet);
		return new String(chars);
	}
	
	
	private char randomCharIn(char[] chars) {
		return randomChar(chars);
	}
	
	
	protected PropertyDescriptor createProperty(boolean multiValue) {
		return multiValue ?
			new StringProperty("testString", "Test string property", new String[] {"hello", "world"}, 1.0f, delimiter) :
			new StringProperty("testString", "Test string property", "brian", 1.0f);			
		}

	
	protected PropertyDescriptor createBadProperty(boolean multiValue) {
		return multiValue ?
			new StringProperty("testString", "Test string property", new String[] {"hello", "world", "a"+delimiter+"b"}, 1.0f, delimiter) :
			new StringProperty("", "Test string property", "brian", 1.0f);			
		}

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(StringPropertyTest.class);
    }
}
