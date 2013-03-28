package net.sourceforge.pmd.lang.rule.properties.factories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.pmd.PropertyDescriptorFields;
import net.sourceforge.pmd.PropertyDescriptor;
import net.sourceforge.pmd.PropertyDescriptorFactory;
import net.sourceforge.pmd.util.CollectionUtil;
import net.sourceforge.pmd.util.StringUtil;


public class BasicPropertyDescriptorFactory<T> implements PropertyDescriptorFactory, PropertyDescriptorFields {

	private final Class<?> valueType;
	private final Map<String, Boolean> fieldTypesByKey;

	protected static final Map<String, Boolean> coreFieldTypesByKey = CollectionUtil.mapFrom(
			new String[]  { nameKey, 		descKey, 	defaultValueKey}, 
			new Boolean[] { Boolean.TRUE,  Boolean.TRUE, Boolean.TRUE}
			);
	
	public BasicPropertyDescriptorFactory(Class<?> theValueType) {
		valueType = theValueType;
		fieldTypesByKey = Collections.unmodifiableMap(coreFieldTypesByKey);
	}
	









	
	public BasicPropertyDescriptorFactory(Class<?> theValueType, Map<String, Boolean> additionalFieldTypesByKey) {
		
		valueType = theValueType;
		Map<String, Boolean> temp = new HashMap<String, Boolean>(coreFieldTypesByKey.size() + additionalFieldTypesByKey.size());
		temp.putAll(coreFieldTypesByKey);
		temp.putAll(additionalFieldTypesByKey);
		
		fieldTypesByKey = Collections.unmodifiableMap(temp);
	}
	
	public Class<?> valueType() {
		return valueType;
	}
	
	@Override
	public PropertyDescriptor<?> createWith(Map<String, String> valuesById) {
		throw new RuntimeException("Unimplemented createWith() method in subclass");
	}

	@Override
	public Map<String, Boolean> expectedFields() {
		return fieldTypesByKey;
	}

	protected String nameIn(Map<String, String> valuesById) {
		return valuesById.get(nameKey);
	}
	
	protected String descriptionIn(Map<String, String> valuesById) {
		return valuesById.get(descKey);
	}
	
	protected String defaultValueIn(Map<String, String> valuesById) {
		return valuesById.get(defaultValueKey);
	}
	
	protected String numericDefaultValueIn(Map<String, String> valuesById) {
		String number = defaultValueIn(valuesById);
		return StringUtil.isEmpty(number) ? "0" : number;	
	}
	
	protected static String minValueIn(Map<String, String> valuesById) {
		return valuesById.get(minKey);
	}
	
	protected static String maxValueIn(Map<String, String> valuesById) {
		return valuesById.get(maxKey);
	}















	
	protected static Integer[] integersIn(String numberString) {
		String[] values = numberString.split(",");	
		List<Integer> ints = new ArrayList<Integer>(values.length);
		for (String value : values) {
			try {
				Integer newInt = Integer.parseInt(value);
				ints.add(newInt);
			} catch (Exception ex) {
				
			}
		}
		return ints.toArray(new Integer[ints.size()]);
	}
	
	protected static Long[] longsIn(String numberString) {
		String[] values = numberString.split(",");	
		List<Long> longs = new ArrayList<Long>(values.length);
		for (String value : values) {
			try {
				Long newLong = Long.parseLong(value);
				longs.add(newLong);
			} catch (Exception ex) {
				
			}
		}
		return longs.toArray(new Long[longs.size()]);
	}
	
	protected static Float[] floatsIn(String numberString) {
		String[] values = numberString.split(",");	
		List<Float> floats = new ArrayList<Float>(values.length);
		for (String value : values) {
			try {
				Float newFloat = Float.parseFloat(value);
				floats.add(newFloat);
			} catch (Exception ex) {
				
			}
		}
		return floats.toArray(new Float[floats.size()]);
	}
	
	protected static Double[] doublesIn(String numberString) {
		String[] values = numberString.split(",");	
		List<Double> doubles = new ArrayList<Double>(values.length);
		for (String value : values) {
			try {
				Double newDouble = Double.parseDouble(value);
				doubles.add(newDouble);
			} catch (Exception ex) {
				
			}
		}
		return doubles.toArray(new Double[doubles.size()]);
	}
	
	protected static String[] labelsIn(Map<String, String> valuesById) {
		return null;	
	}
	
	protected static Object[] choicesIn(Map<String, String> valuesById) {
		return null;	
	}
	
	protected static int indexIn(Map<String, String> valuesById) {
		return 0;	
	}
	
	protected static int[] indiciesIn(Map<String, String> valuesById) {
		return null;	
	}	

	protected static char delimiterIn(Map<String, String> valuesById) {
		String characterStr = valuesById.get(delimiterKey).trim();
		return characterStr.charAt(0);
	}
	
	protected static String[] minMaxFrom(Map<String, String> valuesById) {
		String min = minValueIn(valuesById);
		String max = maxValueIn(valuesById);
		if (StringUtil.isEmpty(min) || StringUtil.isEmpty(max)) {
			throw new RuntimeException("min and max values must be specified");
		}
		return new String[] { min, max };
	}
		
	protected static String[] legalPackageNamesIn(Map<String, String> valuesById) {
		String names = valuesById.get(legalPackagesKey);
		if (StringUtil.isEmpty(names)) return null;
		return StringUtil.substringsOf(names, '|');	
	}
	
	public static Map<String, Boolean> expectedFieldTypesWith(String[] otherKeys, Boolean[] otherValues) {
		Map<String, Boolean> largerMap = new HashMap<String, Boolean>(otherKeys.length + coreFieldTypesByKey.size());
		largerMap.putAll(coreFieldTypesByKey);
		for (int i=0; i<otherKeys.length; i++) largerMap.put(otherKeys[i], otherValues[i]);
		return largerMap;
	}
	






}
