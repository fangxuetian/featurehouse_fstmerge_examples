
package net.sourceforge.squirrel_sql.plugins.oracle.expander;


import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.AbstractINodeExpanderTest;


import org.junit.Before;

public class SchemaExpanderTest extends AbstractINodeExpanderTest
{

	@Before
	public void setUp() throws Exception
	{
		classUnderTest = new SchemaExpander();
	}

}
