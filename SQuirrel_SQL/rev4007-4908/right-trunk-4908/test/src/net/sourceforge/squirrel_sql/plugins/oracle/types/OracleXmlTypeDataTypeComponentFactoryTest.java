
package net.sourceforge.squirrel_sql.plugins.oracle.types;


import net.sourceforge.squirrel_sql.fw.datasetviewer.cellcomponent.AbstractDataTypeComponentFactoryTest;

import org.junit.Before;

public class OracleXmlTypeDataTypeComponentFactoryTest extends AbstractDataTypeComponentFactoryTest
{

	@Before
	public void setUp() throws Exception
	{
		classUnderTest = new OracleXmlTypeDataTypeComponentFactory();
	}

}
