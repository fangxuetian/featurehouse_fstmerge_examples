

package org.gjt.sp.jedit.bsh;

import java.util.Hashtable;


public class Capabilities 
{
	private static boolean accessibility = false;

	public static boolean haveSwing() {
		
		return classExists( "javax.swing.JButton" );
	}

	public static boolean canGenerateInterfaces() {
		
		return classExists( "java.lang.reflect.Proxy" );
	}

	
	public static boolean haveAccessibility() 
	{
		return accessibility;
	}

	public static void setAccessibility( boolean b ) 
		throws Unavailable
	{ 
		if ( b == false )
		{
			accessibility = false;
			return;
		}

		if ( !classExists( "java.lang.reflect.AccessibleObject" )
			|| !classExists("org.gjt.sp.jedit.bsh.reflect.ReflectManagerImpl")  
		)
			throw new Unavailable( "Accessibility unavailable" );

		
		try {
			String.class.getDeclaredMethods();
		} catch ( SecurityException e ) {
			throw new Unavailable("Accessibility unavailable: "+e);
		}

		accessibility = true; 
	}

	private static Hashtable classes = new Hashtable();
	
	public static boolean classExists( String name ) 
	{
		Object c = classes.get( name );

		if ( c == null ) {
			try {
				
				c = Class.forName( name );
			} catch ( ClassNotFoundException e ) { }

			if ( c != null )
				classes.put(c,"unused");
		}

		return c != null;
	}

	
	public static class Unavailable extends UtilEvalError
	{
		public Unavailable(String s ){ super(s); }
	}
}


