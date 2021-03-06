

package net.sf.jabref.msbib;
import net.sf.jabref.*;
import java.util.*;
import java.io.InputStream;
import java.io.IOException;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class MSBibDatabase {
	protected Set entries;
	
	public MSBibDatabase() {
		
		entries = new HashSet();
	}
	
	public MSBibDatabase(InputStream stream) throws IOException {
		importEntries(stream);
    }

	public MSBibDatabase(BibtexDatabase bibtex) {
		Set keySet = bibtex.getKeySet();
        addEntries(bibtex, keySet);
    }

    public MSBibDatabase(BibtexDatabase bibtex, Set keySet) {
        if (keySet == null)
            keySet = bibtex.getKeySet();
        addEntries(bibtex, keySet);
    }

    public List importEntries(InputStream stream) throws IOException {
    	entries = new HashSet();	
    	ArrayList bibitems = new ArrayList();
    	Document docin = null;
    	try {
    	DocumentBuilder dbuild = DocumentBuilderFactory.
    								newInstance().
    								newDocumentBuilder();
   		docin = dbuild.parse(stream);
    	} catch (Exception e) {
	   		System.out.println("Exception caught..." + e);
	   		e.printStackTrace();
    	}
   		String bcol = "b:";
   		NodeList rootLst = docin.getElementsByTagName("b:Sources");
   		if(rootLst.getLength()==0) {   			
   			rootLst = docin.getElementsByTagName("Sources");
   			bcol = "";
   		}
   		if(rootLst.getLength()==0)
   			return bibitems;



   		NodeList sourceList = ((Element)(rootLst.item(0))).getElementsByTagName(bcol+"Source");
   		for(int i=0; i<sourceList.getLength(); i++) {
   			MSBibEntry entry = new MSBibEntry((Element)sourceList.item(i),bcol);
   			entries.add(entry);
   			bibitems.add(entry.getBibtexRepresentation());   			
   		}
   		
   		return bibitems;
    }

    private void addEntries(BibtexDatabase database, Set keySet) {
        entries = new HashSet();
        for(Iterator iter = keySet.iterator(); iter.hasNext(); ) {
			BibtexEntry entry = database.getEntryById((String)iter.next());
			MSBibEntry newMods = new MSBibEntry(entry);
			entries.add(newMods);
		}
	}
	public Document getDOMrepresentation() {
		Document result = null;
	   	try {
	   		DocumentBuilder dbuild = DocumentBuilderFactory.
														newInstance().
														newDocumentBuilder();
	   		result = dbuild.newDocument();
	   		Element msbibCollection = result.createElement("b:Sources");
	   		msbibCollection.setAttribute("SelectedStyle","");
	   		msbibCollection.setAttribute("xmlns", "http://schemas.openxmlformats.org/officeDocument/2006/bibliography");
	   		msbibCollection.setAttribute("xmlns:b", "http://schemas.openxmlformats.org/officeDocument/2006/bibliography");	   			   		 
	   		
	   		for(Iterator iter = entries.iterator(); iter.hasNext(); ) {
	   			MSBibEntry entry = (MSBibEntry) iter.next();
	   			Node node = entry.getDOMrepresentation(result);
	   			msbibCollection.appendChild(node);
	   		}
	   		
	   		result.appendChild(msbibCollection);	   		
	   	}
	   	catch (Exception e)
		{
	   		System.out.println("Exception caught..." + e);
	   		e.printStackTrace();
		}
	   	return result;
	   }
}
