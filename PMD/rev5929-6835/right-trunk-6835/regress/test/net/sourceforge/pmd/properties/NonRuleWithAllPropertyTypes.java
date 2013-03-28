package test.net.sourceforge.pmd.properties;

import java.lang.reflect.Method;

import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.rule.properties.BooleanMultiProperty;
import net.sourceforge.pmd.lang.rule.properties.BooleanProperty;
import net.sourceforge.pmd.lang.rule.properties.CharacterMultiProperty;
import net.sourceforge.pmd.lang.rule.properties.CharacterProperty;
import net.sourceforge.pmd.lang.rule.properties.EnumeratedMultiProperty;
import net.sourceforge.pmd.lang.rule.properties.EnumeratedProperty;
import net.sourceforge.pmd.lang.rule.properties.FloatMultiProperty;
import net.sourceforge.pmd.lang.rule.properties.FloatProperty;
import net.sourceforge.pmd.lang.rule.properties.IntegerMultiProperty;
import net.sourceforge.pmd.lang.rule.properties.IntegerProperty;
import net.sourceforge.pmd.lang.rule.properties.LongMultiProperty;
import net.sourceforge.pmd.lang.rule.properties.LongProperty;
import net.sourceforge.pmd.lang.rule.properties.MethodMultiProperty;
import net.sourceforge.pmd.lang.rule.properties.MethodProperty;
import net.sourceforge.pmd.lang.rule.properties.StringMultiProperty;
import net.sourceforge.pmd.lang.rule.properties.StringProperty;
import net.sourceforge.pmd.lang.rule.properties.TypeMultiProperty;
import net.sourceforge.pmd.lang.rule.properties.TypeProperty;
import net.sourceforge.pmd.util.ClassUtil;


public class NonRuleWithAllPropertyTypes extends AbstractJavaRule {

    private static final Method stringLength = ClassUtil.methodFor(String.class, "length", ClassUtil.EMPTY_CLASS_ARRAY);
    private static final Method stringToLowerCase = ClassUtil.methodFor(String.class, "toLowerCase", ClassUtil.EMPTY_CLASS_ARRAY);
     
	
	public static final StringProperty singleStr	  = new StringProperty("singleStr", "String value", "hello world" , 3.0f);
	public static final StringMultiProperty multiStr  = new StringMultiProperty("multiStr", "Multiple string values", new String[] {"hello", "world"}, 5.0f, '|');
	
	public static final IntegerProperty singleInt	  = new IntegerProperty("singleInt", "Single integer value", 1, 10, 8 , 3.0f);
	public static final IntegerMultiProperty multiInt = new IntegerMultiProperty("multiInt", "Multiple integer values", 0, 10, new Integer[] {1,2,3,4}, 5.0f);
 
	public static final LongProperty singleLong       = new LongProperty("singleLong", "Single long value", 1l, 10l, 8l , 3.0f);
    public static final LongMultiProperty multiLong   = new LongMultiProperty("multiLong", "Multiple long values", 0l, 10l, new Long[] {1l,2l,3l,4l}, 5.0f);
	
	public static final BooleanProperty singleBool	   = new BooleanProperty("singleBool", "Single boolean value", true, 6.0f);
	public static final BooleanMultiProperty multiBool = new BooleanMultiProperty("multiBool", "Multiple boolean values", new Boolean[] { true, false}, 5.0f);
	
	public static final CharacterProperty singleChar	 = new CharacterProperty("singleChar", "Single character", 'a', 5.0f);
	public static final CharacterMultiProperty multiChar = new CharacterMultiProperty("multiChar", "Multiple characters", new Character[] {'a', 'e', 'i', 'o', 'u'}, 6.0f, '|');
	
	public static final FloatProperty singleFloat	     = new FloatProperty("singleFloat", "Single float value", 9f, 10f, .9f, 5.0f);
	public static final FloatMultiProperty multiFloat    = new FloatMultiProperty("multiFloat", "Multiple float values", 0f, 5f, new Float[] {1f,2f,3f}, 6.0f);
	
	public static final TypeProperty singleType	         = new TypeProperty("singleType", "Single type", String.class, new String[] { "java.lang" }, 5.0f);
	public static final TypeMultiProperty multiType	     = new TypeMultiProperty("multiType", "Multiple types", new Class[] {Integer.class, Object.class}, new String[] { "java.lang" }, 6.0f);

    public static final MethodProperty singleMethod      = new MethodProperty("singleMethod", "Single method", stringLength, new String[] { "java.lang" }, 5.0f);
    public static final MethodMultiProperty multiMethod  = new MethodMultiProperty("multiMethod", "Multiple methods", new Method[] {stringLength, stringToLowerCase}, new String[] { "java.lang" }, 6.0f);

	public static final EnumeratedProperty<Class> enumType			 = new EnumeratedProperty<Class>("enumType", "Enumerated choices", new String[] {"String", "Object"}, new Class[] {String.class, Object.class}, 1, 5.0f);
	public static final EnumeratedMultiProperty<Class> multiEnumType = new EnumeratedMultiProperty<Class>("multiEnumType", "Multiple enumerated choices", new String[] {"String", "Object"}, new Class[] {String.class, Object.class}, new int[] {0,1}, 5.0f);
	
	
	public NonRuleWithAllPropertyTypes() {
		super();
		definePropertyDescriptor(singleStr);
		definePropertyDescriptor(multiStr);
		definePropertyDescriptor(singleInt);
		definePropertyDescriptor(multiInt);
	    definePropertyDescriptor(singleLong);
	    definePropertyDescriptor(multiLong);
		definePropertyDescriptor(singleBool);
		definePropertyDescriptor(multiBool);
		definePropertyDescriptor(singleChar);
		definePropertyDescriptor(multiChar);
		definePropertyDescriptor(singleFloat);
		definePropertyDescriptor(multiFloat);
		definePropertyDescriptor(singleType);
		definePropertyDescriptor(multiType);
		definePropertyDescriptor(enumType);
		definePropertyDescriptor(singleMethod);
        definePropertyDescriptor(multiMethod);
		definePropertyDescriptor(multiEnumType);
	}
}
