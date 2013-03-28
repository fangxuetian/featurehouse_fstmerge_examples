package test.net.sourceforge.pmd.properties;

import net.sourceforge.pmd.PropertyDescriptor;
import net.sourceforge.pmd.lang.rule.properties.BooleanMultiProperty;
import net.sourceforge.pmd.lang.rule.properties.BooleanProperty;

import org.junit.Test;


public class BooleanPropertyTest extends AbstractPropertyDescriptorTester {

	public BooleanPropertyTest() {
		super();
	}

	
	protected Object createValue(int valueCount) {
		
		if (valueCount == 1) return System.currentTimeMillis() % 1 > 0 ?
			Boolean.TRUE : Boolean.FALSE;
		
		Boolean[] values = new Boolean[valueCount];
		for (int i=0; i<values.length; i++) values[i] = (Boolean)createValue(1);
		return values;
	}

	 @Test
	public void testErrorForBad() {
		 
	}
	    
	 protected Object createBadValue(int count) {
	 	return null;
	}
		
	
	
	 protected PropertyDescriptor createProperty(boolean multiValue) {
		return multiValue ?
			new BooleanMultiProperty("testBoolean", "Test boolean property", new Boolean[] {false, true, true}, 1.0f) :
			new BooleanProperty("testBoolean", "Test boolean property", false, 1.0f);
	}

	
	 protected PropertyDescriptor createBadProperty(boolean multiValue) {
		return multiValue ?
			new BooleanMultiProperty("", "Test boolean property", new Boolean[] {false, true, true}, 1.0f) :
			new BooleanProperty("testBoolean", "", false, 1.0f);
	}
	
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(BooleanPropertyTest.class);
    }
}
