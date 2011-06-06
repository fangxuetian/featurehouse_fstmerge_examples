

package org.gjt.sp.jedit.io;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;



public class CharsetEncoding implements Encoding
{
	
	public CharsetEncoding(String name)
	{
		body = Charset.forName(name);
	} 

	
	public Reader getTextReader(InputStream in) throws IOException
	{
		
		
		
		
		return new InputStreamReader(in, body.newDecoder());
	}

	public Writer getTextWriter(OutputStream out) throws IOException
	{
		
		
		return new OutputStreamWriter(out, body.newEncoder());
	}

	public Reader getPermissiveTextReader(InputStream in) throws IOException
	{
		
		
		CharsetDecoder permissive = body.newDecoder();
		permissive.onMalformedInput(CodingErrorAction.REPLACE);
		permissive.onUnmappableCharacter(CodingErrorAction.REPLACE);
		return new InputStreamReader(in, permissive);
	}
	

	
	private final Charset body;
	
}
