package net.sourceforge.squirrel_sql.fw.sql;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.squirrel_sql.fw.util.MyURLClassLoader;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;

public class SQLDriverClassLoader extends MyURLClassLoader
{
    
    
    private final static ILogger s_log =
                LoggerController.createLogger(SQLDriverClassLoader.class);    
    
	public SQLDriverClassLoader(ISQLDriver sqlDriver) throws MalformedURLException
	{
		super(createURLs(sqlDriver.getName(), sqlDriver.getJarFileNames()));
	}

	public SQLDriverClassLoader(URL[] urls)
	{
		super(urls);
	}

	public SQLDriverClassLoader(URL url)
	{
		super(url);
	}

	public Class<?>[] getDriverClasses(ILogger logger)
	{
		final Class<?>[] classes = getAssignableClasses(Driver.class, logger);
		final List<Class<?>> list = new ArrayList<Class<?>>();
		for (int i = 0; i < classes.length; ++i)
		{
			Class<?> clazz = classes[i];
			if (!Modifier.isAbstract(clazz.getModifiers()))
			{
				list.add(clazz);
			}
		}
		return list.toArray(new Class[list.size()]);
	}

	private static URL[] createURLs(String driverName, String[] fileNames)
		throws MalformedURLException
	{
		if (fileNames == null)
		{
			fileNames = new String[0];
		}
		URL[] urls = new URL[fileNames.length];
		for (int i = 0; i < fileNames.length; ++i)
		{
            File f = new File(fileNames[i]);
            if (!f.exists()) {
                s_log.info(
                    "For driver '"+driverName+"', the JVM says file doesn't exist: "+
                    fileNames[i]);
            }
            if (f.isDirectory()) {
                s_log.info(
                    "For driver '"+driverName+"', the JVM says the file is a directory: "+
                    fileNames[i]);
            }
            if (!f.canRead()) {
                s_log.info(
                    "For driver '"+driverName+"', the JVM says the file can't be read: "+
                    fileNames[i]);
            }
            urls[i] = f.toURL();
            
		}
		return urls;
	}
}
