

package net.sourceforge.pmd.renderers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class XSLTRenderer extends XMLRenderer {

	private Transformer transformer;
	private String xsltFilename = "/etc/pmd-nicerhtml.xsl";
	private Writer outputWriter;

	public XSLTRenderer() {

	}
	
	public XSLTRenderer(String xsltFilename) {
		File file = new File(xsltFilename);
		if ( xsltFilename != null && file.exists() && file.canRead() ) {
			this.xsltFilename = xsltFilename;
		}
	}

	@Override
	public void start() throws IOException {
		
		this.outputWriter = getWriter();
		
		Writer w = new StringWriter();
		setWriter(w);
		
		
		InputStream xslt = null;
		File file = new File(this.xsltFilename);
		if ( file.exists() && file.canRead() ) {
			xslt = new FileInputStream(file);
		}
		else {
			xslt = this.getClass().getResourceAsStream(xsltFilename);
		}
		if ( xslt == null ) {
			throw new FileNotFoundException("Can't file XSLT sheet :" + xsltFilename);
		}
		this.prepareTransformer(xslt);
		
		super.start();
	}

	
	private void prepareTransformer(InputStream xslt) {
		if ( xslt != null ) {
			try {
		    
		    TransformerFactory factory = TransformerFactory.newInstance();
		    StreamSource src = new StreamSource(xslt);
		    
		    this.transformer = factory.newTransformer(src);
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void end() throws IOException {
		
		super.end();
		
		Writer writer = super.getWriter();
		if ( writer instanceof StringWriter ) {
			StringWriter w = (StringWriter)writer;
			StringBuffer buffer = w.getBuffer();
			
			InputStream xml =  new ByteArrayInputStream(buffer.toString().getBytes(this.encoding));
			Document doc = this.getDocument(xml);
			this.transform(doc);
		}
		else {
			
			new RuntimeException("Wrong writer").printStackTrace();
		}

	}

	private void transform(Document doc) {
		DOMSource source = new DOMSource(doc);
		this.setWriter(new StringWriter());
		StreamResult result = new StreamResult(this.outputWriter);
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	private Document getDocument(InputStream xml) {
		try {
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return parser.parse(xml);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
