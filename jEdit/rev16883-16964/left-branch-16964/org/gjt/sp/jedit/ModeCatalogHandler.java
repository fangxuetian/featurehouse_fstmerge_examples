

package org.gjt.sp.jedit;


import org.gjt.sp.jedit.syntax.ModeProvider;
import org.gjt.sp.util.Log;
import org.gjt.sp.util.XMLUtilities;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;



class ModeCatalogHandler extends DefaultHandler
{
	
	ModeCatalogHandler(String directory, boolean resource)
	{
		this.directory = directory;
		this.resource = resource;
	} 

	
	public InputSource resolveEntity(String publicId, String systemId)
	{
		return XMLUtilities.findEntity(systemId, "catalog.dtd", getClass());
	} 

	
	public void startElement(String uri, String localName,
							 String qName, Attributes attrs)
	{
		if (qName.equals("MODE"))
		{
			String modeName = attrs.getValue("NAME");

			String file = attrs.getValue("FILE");
			if(file == null)
			{
				Log.log(Log.ERROR,this,directory + "catalog:"
					+ " mode " + modeName + " doesn't have"
					+ " a FILE attribute");
			}

			String filenameGlob = attrs.getValue("FILE_NAME_GLOB");
			String firstlineGlob = attrs.getValue("FIRST_LINE_GLOB");


			Mode mode = ModeProvider.instance.getMode(modeName);
			if(mode == null)
			{
				mode = instantiateMode(modeName);
				ModeProvider.instance.addMode(mode);
			}

			Object path;
			if(resource)
				path = jEdit.class.getResource(directory + file);
			else
				path = MiscUtilities.constructPath(directory,file);
			mode.setProperty("file",path);

			if(filenameGlob != null)
				mode.setProperty("filenameGlob",filenameGlob);
			else
				mode.unsetProperty("filenameGlob");

			if(firstlineGlob != null)
				mode.setProperty("firstlineGlob",firstlineGlob);
			else
				mode.unsetProperty("firstlineGlob");

			mode.init();
		}
	} 

	protected Mode instantiateMode(String modeName)
	{
		return new Mode(modeName);
	}

	private String directory;
	private boolean resource;

}

