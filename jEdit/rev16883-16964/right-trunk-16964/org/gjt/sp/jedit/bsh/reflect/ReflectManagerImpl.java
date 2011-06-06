

package org.gjt.sp.jedit.bsh.reflect;

import org.gjt.sp.jedit.bsh.ReflectManager;
import java.lang.reflect.AccessibleObject;


public class ReflectManagerImpl extends ReflectManager
{
	

	public boolean setAccessible( Object obj ) 
	{
		if ( obj instanceof AccessibleObject ) {
			((AccessibleObject)obj).setAccessible(true);
			return true;
		} else
			return false;
	}
}

