

package org.gjt.sp.jedit.pluginmgr;


import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.zip.GZIPInputStream;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.gjt.sp.util.Log;
import org.gjt.sp.util.StandardUtilities;
import org.gjt.sp.util.WorkRequest;
import org.gjt.sp.util.IOUtilities;
import org.gjt.sp.jedit.*;



class PluginList
{
	
	public static final int GZIP_MAGIC_1 = 0x1f;
	public static final int GZIP_MAGIC_2 = 0x8b;

	final List<Plugin> plugins = new ArrayList<Plugin>();
	final Map<String, Plugin> pluginHash = new HashMap<String, Plugin>();
	final List<PluginSet> pluginSets = new ArrayList<PluginSet>();

	
	private final String id;

	
	PluginList(WorkRequest workRequest) throws Exception
	{
		String path = jEdit.getProperty("plugin-manager.export-url");
		id = jEdit.getProperty("plugin-manager.mirror.id");
		if (!id.equals(MirrorList.Mirror.NONE))
			path += "?mirror="+id;
		PluginListHandler handler = new PluginListHandler(this,path);
		XMLReader parser = XMLReaderFactory.createXMLReader();
		InputStream in = null;
		InputStream inputStream = null;
		try
		{
			inputStream = new URL(path).openStream();
			workRequest.setStatus(jEdit.getProperty("plugin-manager.list-download"));
			in = new BufferedInputStream(inputStream);
			if(in.markSupported())
			{
				in.mark(2);
				int b1 = in.read();
				int b2 = in.read();
				in.reset();

				if(b1 == GZIP_MAGIC_1 && b2 == GZIP_MAGIC_2)
					in = new GZIPInputStream(in);
			}

			InputSource isrc = new InputSource(new InputStreamReader(in,"UTF8"));
			isrc.setSystemId("jedit.jar");
			parser.setContentHandler(handler);
			parser.setDTDHandler(handler);
			parser.setEntityResolver(handler);
			parser.setErrorHandler(handler);
			parser.parse(isrc);
		}
		finally
		{
			IOUtilities.closeQuietly(in);
			IOUtilities.closeQuietly(inputStream);
		}
	} 

	
	void addPlugin(Plugin plugin)
	{
		plugin.checkIfInstalled();
		plugins.add(plugin);
		pluginHash.put(plugin.name,plugin);
	} 

	
	void addPluginSet(PluginSet set)
	{
		pluginSets.add(set);
	} 

	
	void finished()
	{
		
		
		for(int i = 0; i < plugins.size(); i++)
		{
			Plugin plugin = plugins.get(i);
			for(int j = 0; j < plugin.branches.size(); j++)
			{
				Branch branch = plugin.branches.get(j);
				for(int k = 0; k < branch.deps.size(); k++)
				{
					Dependency dep = branch.deps.get(k);
					if(dep.what.equals("plugin"))
						dep.plugin = pluginHash.get(dep.pluginName);
				}
			}
		}
	} 

	
	void dump()
	{
		for(int i = 0; i < plugins.size(); i++)
		{
			System.err.println(plugins.get(i));
			System.err.println();
		}
	} 

	
	
	String getMirrorId()
	{
		return id;
	} 

	
	static class PluginSet
	{
		String name;
		String description;
		final List<String> plugins = new ArrayList<String>();

		public String toString()
		{
			return plugins.toString();
		}
	} 

	
	public static class Plugin
	{
		String jar;
		String name;
		String description;
		String author;
		final List<Branch> branches = new ArrayList<Branch>();
		
		

		void checkIfInstalled()
		{
			
		}

		String getInstalledVersion()
		{
			PluginJAR[] jars = jEdit.getPluginJARs();
			for(int i = 0; i < jars.length; i++)
			{
				String path = jars[i].getPath();

				if(MiscUtilities.getFileName(path).equals(jar))
				{
					EditPlugin plugin = jars[i].getPlugin();
					if(plugin != null)
					{
						return jEdit.getProperty(
							"plugin." + plugin.getClassName()
							+ ".version");
					}
					else
						return null;
				}
			}

			return null;
		}

		String getInstalledPath()
		{
			PluginJAR[] jars = jEdit.getPluginJARs();
			for(int i = 0; i < jars.length; i++)
			{
				String path = jars[i].getPath();

				if(MiscUtilities.getFileName(path).equals(jar))
					return path;
			}

			return null;
		}

		
		Branch getCompatibleBranch()
		{
			for(int i = 0; i < branches.size(); i++)
			{
				Branch branch = branches.get(i);
				if(branch.canSatisfyDependencies())
					return branch;
			}

			return null;
		}

		boolean canBeInstalled()
		{
			Branch branch = getCompatibleBranch();
			return branch != null && !branch.obsolete
				&& branch.canSatisfyDependencies();
		}

		void install(Roster roster, String installDirectory, boolean downloadSource)
		{
			String installed = getInstalledPath();

			Branch branch = getCompatibleBranch();
			if(branch.obsolete)
			{
				if(installed != null)
					roster.addRemove(installed);
				return;
			}

			
			

			if(installed != null)
			{
				installDirectory = MiscUtilities.getParentOfPath(
					installed);
			}

			roster.addInstall(
				installed,
				downloadSource ? branch.downloadSource : branch.download,
				installDirectory,
				downloadSource ? branch.downloadSourceSize : branch.downloadSize);

		}

		public String toString()
		{
			return name;
		}
	} 

	
	static class Branch
	{
		String version;
		String date;
		int downloadSize;
		String download;
		int downloadSourceSize;
		String downloadSource;
		boolean obsolete;
		final List<Dependency> deps = new ArrayList<Dependency>();

		boolean canSatisfyDependencies()
		{
			for(int i = 0; i < deps.size(); i++)
			{
				Dependency dep = deps.get(i);
				if(!dep.canSatisfy())
					return false;
			}

			return true;
		}

		void satisfyDependencies(Roster roster, String installDirectory,
			boolean downloadSource)
		{
			for(int i = 0; i < deps.size(); i++)
			{
				Dependency dep = deps.get(i);
				dep.satisfy(roster,installDirectory,downloadSource);
			}
		}

		public String toString()
		{
			return "[version=" + version + ",download=" + download
				+ ",obsolete=" + obsolete + ",deps=" + deps + ']';
		}
	} 

	
	static class Dependency
	{
		final String what;
		final String from;
		final String to;
		
		final String pluginName;
		Plugin plugin;

		Dependency(String what, String from, String to, String pluginName)
		{
			this.what = what;
			this.from = from;
			this.to = to;
			this.pluginName = pluginName;
		}

		boolean isSatisfied()
		{
			if(what.equals("plugin"))
			{
				for(int i = 0; i < plugin.branches.size(); i++)
				{
					String installedVersion = plugin.getInstalledVersion();
					if(installedVersion != null
						&&
					(from == null || StandardUtilities.compareStrings(
						installedVersion,from,false) >= 0)
						&&
						(to == null || StandardUtilities.compareStrings(
						      installedVersion,to,false) <= 0))
					{
						return true;
					}
				}

				return false;
			}
			else if(what.equals("jdk"))
			{
				String javaVersion = System.getProperty("java.version").substring(0,3);

				if((from == null || StandardUtilities.compareStrings(
					javaVersion,from,false) >= 0)
					&&
					(to == null || StandardUtilities.compareStrings(
						     javaVersion,to,false) <= 0))
					return true;
				else
					return false;
			}
			else if(what.equals("jedit"))
			{
				String build = jEdit.getBuild();

				if((from == null || StandardUtilities.compareStrings(
					build,from,false) >= 0)
					&&
					(to == null || StandardUtilities.compareStrings(
						     build,to,false) <= 0))
					return true;
				else
					return false;
			}
			else
			{
				Log.log(Log.ERROR,this,"Invalid dependency: " + what);
				return false;
			}
		}

		boolean canSatisfy()
		{
			if(isSatisfied())
				return true;
			if (what.equals("plugin"))
				return plugin.canBeInstalled();
			return false;
		}

		void satisfy(Roster roster, String installDirectory,
			boolean downloadSource)
		{
			if(what.equals("plugin"))
			{
				String installedVersion = plugin.getInstalledVersion();
				for(int i = 0; i < plugin.branches.size(); i++)
				{
					Branch branch = plugin.branches.get(i);
					if((installedVersion == null
						||
					StandardUtilities.compareStrings(
						installedVersion,branch.version,false) < 0)
						&&
					(from == null || StandardUtilities.compareStrings(
						branch.version,from,false) >= 0)
						&&
						(to == null || StandardUtilities.compareStrings(
						      branch.version,to,false) <= 0))
					{
						plugin.install(roster,installDirectory,
							downloadSource);
						return;
					}
				}
			}
		}

		public String toString()
		{
			return "[what=" + what + ",from=" + from
				+ ",to=" + to + ",plugin=" + plugin + ']';
		}
	} 
}
