
package net.sf.jabref.export.layout;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import net.sf.jabref.BibtexDatabase;
import net.sf.jabref.BibtexEntry;
import net.sf.jabref.Globals;
import net.sf.jabref.NameFormatterTab;
import net.sf.jabref.Util;
import net.sf.jabref.export.layout.format.NameFormat;
import wsi.ra.tool.WSITools;
import wsi.ra.types.StringInt;


public class LayoutEntry {
	
	

	private LayoutFormatter[] option;

	private String text;

	private LayoutEntry[] layoutEntries;

	private int type;

	private String classPrefix;

	
	

	public LayoutEntry(StringInt si, String classPrefix_) throws Exception {
		type = si.i;
		classPrefix = classPrefix_;

		if (si.i == LayoutHelper.IS_LAYOUT_TEXT) {
			text = si.s;
		} else if (si.i == LayoutHelper.IS_SIMPLE_FIELD) {
			text = si.s.trim();
		} else if (si.i == LayoutHelper.IS_FIELD_START) {
		} else if (si.i == LayoutHelper.IS_FIELD_END) {
		} else if (si.i == LayoutHelper.IS_OPTION_FIELD) {
			Vector v = new Vector();
			WSITools.tokenize(v, si.s, "\n");

			if (v.size() == 1) {
				text = (String) v.get(0);
			} else {
				text = ((String) v.get(0)).trim();

				
				
				option = getOptionalLayout((String) v.get(1), classPrefix);
				
				
				
				
				
			}
		}

		
		
		
	}

	public LayoutEntry(Vector parsedEntries, String classPrefix_, int layoutType) throws Exception {
		classPrefix = classPrefix_;
		String blockStart = null;
		String blockEnd = null;
		StringInt si;
		Vector<StringInt> blockEntries = null;
		Vector<LayoutEntry> tmpEntries = new Vector<LayoutEntry>();
		LayoutEntry le;
		si = (StringInt) parsedEntries.get(0);
		blockStart = si.s;
		si = (StringInt) parsedEntries.get(parsedEntries.size() - 1);
		blockEnd = si.s;

		if (!blockStart.equals(blockEnd)) {
			System.err.println("Field start and end entry must be equal.");
		}

		type = layoutType;
		text = si.s;

		for (int i = 1; i < (parsedEntries.size() - 1); i++) {
			si = (StringInt) parsedEntries.get(i);

			
			if (si.i == LayoutHelper.IS_LAYOUT_TEXT) {
			} else if (si.i == LayoutHelper.IS_SIMPLE_FIELD) {
			} else if ((si.i == LayoutHelper.IS_FIELD_START)
				|| (si.i == LayoutHelper.IS_GROUP_START)) {
				blockEntries = new Vector<StringInt>();
				blockStart = si.s;
			} else if ((si.i == LayoutHelper.IS_FIELD_END) || (si.i == LayoutHelper.IS_GROUP_END)) {
				if (blockStart.equals(si.s)) {
					blockEntries.add(si);
					if (si.i == LayoutHelper.IS_GROUP_END)
						le = new LayoutEntry(blockEntries, classPrefix, LayoutHelper.IS_GROUP_START);
					else
						le = new LayoutEntry(blockEntries, classPrefix, LayoutHelper.IS_FIELD_START);
					tmpEntries.add(le);
					blockEntries = null;
				} else {
					System.out.println("Nested field entries are not implemented !!!");
				}
			} else if (si.i == LayoutHelper.IS_OPTION_FIELD) {
			}

			
			
			
			if (blockEntries == null) {
				
				tmpEntries.add(new LayoutEntry(si, classPrefix));
			} else {
				blockEntries.add(si);
			}
		}

		layoutEntries = new LayoutEntry[tmpEntries.size()];

		for (int i = 0; i < tmpEntries.size(); i++) {
			layoutEntries[i] = (LayoutEntry) tmpEntries.get(i);

			
		}
	}

	public String doLayout(BibtexEntry bibtex, BibtexDatabase database) {

		switch (type) {
		case LayoutHelper.IS_LAYOUT_TEXT:
			return text;
		case LayoutHelper.IS_SIMPLE_FIELD:
			return BibtexDatabase.getResolvedField(text, bibtex, database);
		case LayoutHelper.IS_FIELD_START:
		case LayoutHelper.IS_GROUP_START: {
			String field = BibtexDatabase.getResolvedField(text, bibtex, database);

			if ((field == null)
				|| ((type == LayoutHelper.IS_GROUP_START) && (field.equalsIgnoreCase(LayoutHelper
					.getCurrentGroup())))) {
				return null;
			} else {
				if (type == LayoutHelper.IS_GROUP_START) {
					LayoutHelper.setCurrentGroup(field);
				}
				StringBuffer sb = new StringBuffer(100);
				String fieldText;
				boolean previousSkipped = false;

				for (int i = 0; i < layoutEntries.length; i++) {
					fieldText = layoutEntries[i].doLayout(bibtex, database);

					if (fieldText == null) {
						if ((i + 1) < layoutEntries.length) {
							if (layoutEntries[i + 1].doLayout(bibtex, database).trim().length() == 0) {
								i++;
								previousSkipped = true;
								continue;
							}
						}
					} else {
						
						
						if (previousSkipped) {
							int eol = 0;

							while ((eol < fieldText.length())
								&& ((fieldText.charAt(eol) == '\n') || (fieldText.charAt(eol) == '\r'))) {
								eol++;
							}

							if (eol < fieldText.length()) {
								sb.append(fieldText.substring(eol));
							}
						} else {
							
							
							sb.append(fieldText);
						}
					}

					previousSkipped = false;
				}

				return sb.toString();
			}
		}
		case LayoutHelper.IS_FIELD_END:
		case LayoutHelper.IS_GROUP_END:
			return "";
		case LayoutHelper.IS_OPTION_FIELD: {
			String fieldEntry;

			if (text.equals("bibtextype")) {
				fieldEntry = bibtex.getType().getName();
			} else {
				
				
				String field = text.startsWith("\\") ? BibtexDatabase.getResolvedField(text.substring(1), bibtex, database)
					: BibtexDatabase.getText(text, database);
				
				if (field == null) {
					fieldEntry = "";
				} else {
					fieldEntry = field;
				}
			}

			
			if (option != null) {
				for (int i = 0; i < option.length; i++) {
					fieldEntry = option[i].format(fieldEntry);
				}
			}

			return fieldEntry;
		}
        case LayoutHelper.IS_ENCODING_NAME: {
            
            
            
            return BibtexDatabase.getResolvedField("encoding", bibtex, database);
        }
        default:
			return "";
		}
	}

	
	
	public String doLayout(BibtexDatabase database, String encoding) {
		if (type == LayoutHelper.IS_LAYOUT_TEXT) {
			return text;
		} else if (type == LayoutHelper.IS_SIMPLE_FIELD) {
			throw new UnsupportedOperationException(
				"bibtex entry fields not allowed in begin or end layout");
		} else if ((type == LayoutHelper.IS_FIELD_START) || (type == LayoutHelper.IS_GROUP_START)) {
			throw new UnsupportedOperationException(
				"field and group starts not allowed in begin or end layout");
		} else if ((type == LayoutHelper.IS_FIELD_END) || (type == LayoutHelper.IS_GROUP_END)) {
			throw new UnsupportedOperationException(
				"field and group ends not allowed in begin or end layout");
		} else if (type == LayoutHelper.IS_OPTION_FIELD) {
			String field = BibtexDatabase.getText(text, database);
			if (option != null) {
				for (int i = 0; i < option.length; i++) {
					field = option[i].format(field);
				}
			}

			return field;
		} else if (type == LayoutHelper.IS_ENCODING_NAME) {
            
            String commonName = Globals.ENCODING_NAMES_LOOKUP.get(encoding);
            return commonName != null ? commonName : encoding;
        }
		return "";
	}

	

	public static LayoutFormatter getLayoutFormatter(String className, String classPrefix)
		throws Exception {
		LayoutFormatter f = null;
        if (className.length() > 0) {
			try {
				try {
					f = (LayoutFormatter) Class.forName(classPrefix + className).newInstance();
				} catch (Throwable ex2) {
					f = (LayoutFormatter) Class.forName(className).newInstance();
				}
			} catch (ClassNotFoundException ex) {
				throw new Exception(Globals.lang("Formatter not found") + ": " + className);
			} catch (InstantiationException ex) {
				throw new Exception(className + " can not be instantiated.");
			} catch (IllegalAccessException ex) {
				throw new Exception(className + " can't be accessed.");
			}
		}
		return f;
	}

	
	public static LayoutFormatter[] getOptionalLayout(String formatterName, String classPrefix)
		throws Exception {

		ArrayList formatterStrings = Util.parseMethodsCalls(formatterName);

		ArrayList<LayoutFormatter> results = new ArrayList<LayoutFormatter>(formatterStrings.size());

		Map userNameFormatter = NameFormatterTab.getNameFormatters();

		for (int i = 0; i < formatterStrings.size(); i++) {

			String[] strings = (String[]) formatterStrings.get(i);

			String className = strings[0].trim();

            try {
				LayoutFormatter f = getLayoutFormatter(className, classPrefix);
                
                
                if (f instanceof ParamLayoutFormatter) {
                    if (strings.length >= 2)
                        ((ParamLayoutFormatter)f).setArgument(strings[1]);
                }
                results.add(f);
			} catch (Exception e) {

				String formatterParameter = (String) userNameFormatter.get(className);

				if (formatterParameter == null) {
					throw new Exception(Globals.lang("Formatter not found") + ": " + className);
				} else {
					NameFormat nf = new NameFormat();
					nf.setParameter(formatterParameter);
					results.add(nf);
				}
			}
		}

		return (LayoutFormatter[]) results.toArray(new LayoutFormatter[] {});
	}

}