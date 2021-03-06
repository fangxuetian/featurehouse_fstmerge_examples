
package net.sourceforge.squirrel_sql.plugins.sessionscript;

import net.sourceforge.squirrel_sql.plugins.AbstractPluginTest;
import net.sourceforge.squirrel_sql.plugins.DatabaseProductVersionData;
import net.sourceforge.squirrel_sql.plugins.sqlscript.SQLScriptPlugin;

import org.junit.After;
import org.junit.Before;


public class SQLScriptPluginTest extends AbstractPluginTest implements DatabaseProductVersionData
{	
	@Before
	public void setUp() throws Exception
	{
		classUnderTest = new SQLScriptPlugin();
	}

	@After
	public void tearDown() throws Exception
	{
		classUnderTest = null;
	}		

}
