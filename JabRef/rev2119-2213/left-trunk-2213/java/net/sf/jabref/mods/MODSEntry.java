package net.sf.jabref.mods;
import java.io.StringWriter;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.BibtexEntryType;
import net.sf.jabref.export.layout.LayoutFormatter;
import net.sf.jabref.export.layout.format.XMLChars;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class MODSEntry {
	protected String entryType = "mods"; 
	protected String id;
	protected List authors = null;
	protected List editors = null;
	
	protected String issuance = "monographic";
	protected PageNumbers pages = null;
	
	protected String publisher = null;
	protected String date = null;
	protected String place = null;
	
	protected String title = null;
	
	protected String type = "text";
	
	protected String number;
	protected String volume;
	protected String genre = null;
	protected Set handledExtensions;
	
	protected MODSEntry host;
	Map extensionFields;
	
	public static String BIBTEX = "bibtex_";
	
	private final boolean CHARFORMAT = false;
	
	public MODSEntry() {
		extensionFields = new HashMap();
		handledExtensions = new HashSet();
	
	}
	
	public MODSEntry(BibtexEntry bibtex) {
		this();
		handledExtensions.add(BIBTEX + "publisher");
		handledExtensions.add(BIBTEX + "title");
		handledExtensions.add(BIBTEX + "bibtexkey");
		handledExtensions.add(BIBTEX + "author");
		populateFromBibtex(bibtex);
	}
	
	protected void populateFromBibtex(BibtexEntry bibtex) {
		LayoutFormatter chars = new XMLChars();
		if (bibtex.getField("title") != null) {
			if(CHARFORMAT)
				title = chars.format(bibtex.getField("title").toString());
			else
				title = bibtex.getField("title").toString();
		}
		
		if (bibtex.getField("publisher") != null) {
			if(CHARFORMAT)
				publisher = chars.format(bibtex.getField("publisher").toString());
			else
				publisher = bibtex.getField("publisher").toString();
		}
			
		if (bibtex.getField("bibtexkey") != null)
			id = bibtex.getField("bibtexkey").toString();
		if (bibtex.getField("place") != null) {
			if(CHARFORMAT)
				place = chars.format(bibtex.getField("place").toString());
			else
				place = bibtex.getField("place").toString();
		}
			
		date = getDate(bibtex);	
		genre = getMODSgenre(bibtex);
		if (bibtex.getField("author") != null)
			authors = getAuthors(bibtex.getField("author").toString());
		if (bibtex.getType() == BibtexEntryType.ARTICLE || 
			bibtex.getType() == BibtexEntryType.INPROCEEDINGS)
		{
			host = new MODSEntry();
			host.entryType = "relatedItem";
			host.title = (String) bibtex.getField("booktitle");
			host.publisher = (String) bibtex.getField("publisher");
			host.number = (String) bibtex.getField("number");
			if (bibtex.getField("pages") != null)
				host.volume = (String) bibtex.getField("volume");
			host.issuance = "continuing";
			if (bibtex.getField("pages") != null)
				host.pages = new PageNumbers((String) bibtex.getField("pages"));
		}
		
		populateExtensionFields(bibtex);
		
	}
	
	protected void populateExtensionFields(BibtexEntry e) {
		Object fields [] = e.getAllFields();
		for(int i = 0; i < fields.length; i++) {
			String field = (String) fields[i];
			String value = (String) e.getField(field);
			field = BIBTEX + field;
			extensionFields.put(field, value);
		}
	}
	
	protected List getAuthors(String authors) {
		List result = new LinkedList();
		LayoutFormatter chars = new XMLChars();
		
		if (authors.indexOf(" and ") == -1) {
			if(CHARFORMAT)
				result.add(new PersonName(chars.format(authors)));
			else
				result.add(new PersonName(authors));
		}
        else
        {
            String[] names = authors.split(" and ");
            for (int i=0; i<names.length; i++) {
            	if(CHARFORMAT)
            		result.add(new PersonName(chars.format(names[i])));
            	else
            		result.add(new PersonName(names[i]));
            }
        }
		return result;
	}
	
	
	protected String getDate(BibtexEntry bibtex) {
		String result = "";
		if (bibtex.getField("year") != null)
			result += (bibtex.getField("year").toString());
		if (bibtex.getField("month") != null)
			result += "-" + bibtex.getField("month").toString();
		
		return result;
	}
	
	protected String getMODSgenre(BibtexEntry bibtex) {
		String bibtexType = bibtex.getType().getName();
		String result;
		if (bibtexType.equals("Mastersthesis"))
			result = "theses";
		else
			result = "conference publication";
		
		return bibtexType;		
	}
	
	public Document getDOMrepresentation() {
		Document result = null;
		try {
			DocumentBuilder d = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			
		
		}
		catch (Exception e) 
		{
			throw new Error(e);
		}
		return result;
	}
		
	
	public Element getDOMrepresentation(Document d) {
		Node result = null;
	   	try {
	   		Element mods = d.createElement(entryType);
	   		mods.setAttribute("version", "3.0");
	   		
	   		
	   		if(title != null) {
	   			Element titleInfo = d.createElement("titleInfo");
	   			Element mainTitle = d.createElement("title");
	   			mainTitle.appendChild(d.createTextNode(stripNonValidXMLCharacters(title)));
	   			titleInfo.appendChild(mainTitle);
		   		mods.appendChild(titleInfo);
	   		}
	   		if (authors != null) {
	   			for(Iterator iter = authors.iterator(); iter.hasNext();) {
	   				PersonName name = (PersonName) iter.next();
	   				Element modsName = d.createElement("name");
	   				modsName.setAttribute("type", "personal");
	   				if (name.getSurname() != null) {
	   					Element namePart = d.createElement("namePart");
	   					namePart.setAttribute("type", "family");
	   					namePart.appendChild(d.createTextNode(stripNonValidXMLCharacters(name.getSurname())));
	   					modsName.appendChild(namePart);
	   				}
	   				if (name.getGivenNames() != null) {
	   					Element namePart = d.createElement("namePart");
	   					namePart.setAttribute("type", "given");
	   					namePart.appendChild(d.createTextNode(stripNonValidXMLCharacters(name.getGivenNames())));
	   					modsName.appendChild(namePart);
	   				}
	   				Element role = d.createElement("role");
	   				Element roleTerm = d.createElement("roleTerm");
	   				roleTerm.setAttribute("type", "text");
	   				roleTerm.appendChild(d.createTextNode("author"));
	   				role.appendChild(roleTerm);
	   				modsName.appendChild(role);
	   				mods.appendChild(modsName);
	   			}
	   		}
	   		
	   		Element originInfo = d.createElement("originInfo");
	   		mods.appendChild(originInfo);
	   		if (this.publisher != null) {
	   			Element publisher = d.createElement("publisher");
				publisher.appendChild(d.createTextNode(stripNonValidXMLCharacters(this.publisher)));
	   			originInfo.appendChild(publisher);
	   		}
	   		if (date != null) {
	   			Element dateIssued = d.createElement("dateIssued");
	   			dateIssued.appendChild(d.createTextNode(stripNonValidXMLCharacters(date)));
	   			originInfo.appendChild(dateIssued);
	   		}
	   		Element issuance = d.createElement("issuance");
	   		issuance.appendChild(d.createTextNode(stripNonValidXMLCharacters(this.issuance)));
	   		originInfo.appendChild(issuance);
	   		
	   		if (id != null) {
	   			Element idref = d.createElement("identifier");
	   			idref.appendChild(d.createTextNode(stripNonValidXMLCharacters(id)));
	   			mods.appendChild(idref);
	   			mods.setAttribute("ID", id);
		   		
	   		}
	   		Element typeOfResource = d.createElement("typeOfResource");
	   		typeOfResource.appendChild(d.createTextNode(stripNonValidXMLCharacters(type)));
	   		mods.appendChild(typeOfResource);
	   		
	   		if (genre != null) {
	   			Element genreElement = d.createElement("genre");
	   			genreElement.setAttribute("authority", "marc");
	   			genreElement.appendChild(d.createTextNode(stripNonValidXMLCharacters(genre)));
	   			mods.appendChild(genreElement);
	   		}
	   		
	   		if (host != null) {
	   			Element relatedItem = host.getDOMrepresentation(d);	   			
	   			relatedItem.setAttribute("type","host");	   			
	   			mods.appendChild(relatedItem);
	   		}
	   		if (pages != null) {
	   			mods.appendChild(pages.getDOMrepresentation(d));
	   		}
	   		
	   		
	   		for(Iterator iter = extensionFields.entrySet().iterator(); iter.hasNext(); ) {
	   			Element extension = d.createElement("extension");
	   			Map.Entry theEntry = (Map.Entry) iter.next();
	   			String field = (String) theEntry.getKey();
	   			String value = (String) theEntry.getValue();
	   			if (handledExtensions.contains(field))
	   				continue;
	   			Element theData = d.createElement(field);
	   			theData.appendChild(d.createTextNode(stripNonValidXMLCharacters(value)));
	   			extension.appendChild(theData);
	   			mods.appendChild(extension);
	   		}
	   		return mods;
	   	}
	   	catch (Exception e)
		{
	   		System.out.println("Exception caught..." + e);
	   		e.printStackTrace();
	   		throw new Error(e);
		}
	   	
	   }
	
	
    public String stripNonValidXMLCharacters(String in) {
        StringBuffer out = new StringBuffer(); 
        char current; 

        if (in == null || ("".equals(in))) return ""; 
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i); 
            if ((current == 0x9) ||
                (current == 0xA) ||
                (current == 0xD) ||
                ((current >= 0x20) && (current <= 0xD7FF)) ||
                ((current >= 0xE000) && (current <= 0xFFFD)) ||
                ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();
    }

	
	public String toString() {
		StringWriter sresult = new StringWriter();
	   	try {
	      	 DOMSource source = new DOMSource(getDOMrepresentation());
	      	 StreamResult result = new StreamResult(sresult);
	      	 Transformer trans = TransformerFactory.newInstance().newTransformer();
	      	 trans.setOutputProperty(OutputKeys.INDENT, "yes");
	      	 trans.transform(source, result);
	      	}
	      	catch (Exception e) {
	      		throw new Error(e);
	      	}
	      return sresult.toString();
	}

}
