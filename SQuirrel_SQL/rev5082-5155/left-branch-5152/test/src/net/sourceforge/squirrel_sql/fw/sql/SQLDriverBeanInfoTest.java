
package net.sourceforge.squirrel_sql.fw.sql;


import net.sourceforge.squirrel_sql.fw.AbstractPropertyBeanInfoTest;

import org.junit.Before;

public class SQLDriverBeanInfoTest extends AbstractPropertyBeanInfoTest
{

	@Before
	public void setUp() throws Exception
	{
		classUnderTest = new SQLDriverPropertyBeanInfo();
	}

}
