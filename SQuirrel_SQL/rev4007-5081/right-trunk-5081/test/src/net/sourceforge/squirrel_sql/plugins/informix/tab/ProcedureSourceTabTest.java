
package net.sourceforge.squirrel_sql.plugins.informix.tab;


import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.AbstractSourceTabTest;

import org.junit.Before;

public class ProcedureSourceTabTest extends AbstractSourceTabTest
{

	@Before
	public void setUp() throws Exception
	{
		classUnderTest = new ProcedureSourceTab(HINT);
	}

}
