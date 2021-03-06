package net.sf.jabref.export.layout.format;

import net.sf.jabref.export.layout.LayoutFormatter;


public class DOICheck implements LayoutFormatter {

	public String format(String fieldText) {
		
		if (fieldText == null){
			return null;
		}
		
		fieldText = fieldText.trim();
		
		if (fieldText.length() == 0){
			return fieldText;
		}
		
		if (fieldText.startsWith("10")){
			return "http://dx.doi.org/" + fieldText;
		}
		
		return fieldText;
	}
}
