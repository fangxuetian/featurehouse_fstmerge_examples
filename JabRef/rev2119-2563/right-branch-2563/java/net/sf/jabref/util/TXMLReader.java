









package net.sf.jabref.util ;

import java.io.* ;
import javax.xml.parsers.* ;

import org.w3c.dom.* ;
import org.xml.sax.* ;
import net.sf.jabref.*;

public class TXMLReader
{
  private Document config ; 
  private DocumentBuilderFactory factory ;
  private DocumentBuilder builder ;

  private boolean ready = false ;

  public TXMLReader(String resPath)
  {
    factory = DocumentBuilderFactory.newInstance() ;
    try
    {
      builder = factory.newDocumentBuilder() ;

      InputStream stream = null ;
      if (resPath != null)
      {
        stream = TXMLReader.class.getResourceAsStream( resPath ) ;
      }
      
      if (stream == null)
      {
        try
        {
          stream = new FileInputStream( "src" +resPath ) ;
        }
        catch (Exception e)
        {

        }
      }

      if (stream != null)
      {
        config = builder.parse( stream ) ;
        ready = true ;
      }
    }

    catch ( SAXException sxe )
    {
      sxe.printStackTrace() ;
    }
    catch ( ParserConfigurationException pce )
    {
      pce.printStackTrace() ;
    }
    catch ( IOException ioe )
    {
      ioe.printStackTrace() ;
    }
    catch (Exception oe)
    {
      oe.printStackTrace();
    }
  }

  

  public boolean isReady()
  {
    return ready ;
  }


  public NodeList getNodes( String name )
  {
    return config.getElementsByTagName( name ) ;
  }

  

  private Element getFirstElement( Element element, String name )
  {
    NodeList nl = element.getElementsByTagName( name ) ;
    if ( nl.getLength() < 1 )
    {
      throw new RuntimeException(
          "Element: " + element + " does not contain: " + name ) ;
    }
    return ( Element ) nl.item( 0 ) ;
  }

  
  public String getSimpleElementText( Element node, String name )
  {
    Element namedElement = getFirstElement( node, name ) ;
    return getSimpleElementText( namedElement ) ;
  }

  
  public String getSimpleElementText( Element node )
  {
    StringBuffer sb = new StringBuffer() ;
    NodeList children = node.getChildNodes() ;
    for ( int i = 0 ; i < children.getLength() ; i++ )
    {
      Node child = children.item( i ) ;
      if ( child instanceof Text )
      {
        sb.append( child.getNodeValue().trim() ) ;
      }
    }
    return sb.toString() ;
  }

  
  
  
  public int readIntegerAttribute( Element node, String attrName, int defaultValue )
  {
    int back = defaultValue ;
    if ( node != null )
    {
      String data = node.getAttribute( attrName ) ;
      if ( data != null )
      {
        if ( data.length() > 0 )
        {
          try
          {
            back = Integer.parseInt( data ) ;
          }
          catch (Exception e) {}
        }
      }
    }
    return back ;
  }

  public String readStringAttribute( Element node, String attrName, String defaultValue )
  {
    if ( node != null )
    {
      String data = node.getAttribute( attrName ) ;
      if ( data != null )
      {
        if ( data.length() > 0 )
        {
          return data ;
        }
      }
    }
    return defaultValue ;
  }

  public double readDoubleAttribute( Element node, String attrName, double defaultValue )
  {
    if ( node != null )
    {
      String data = node.getAttribute( attrName ) ;
      if ( data != null )
      {
        if ( data.length() > 0 )
        {
          return Double.parseDouble( data ) ;
        }
      }
    }
    return defaultValue ;
  }

}
