package net.sourceforge.squirrel_sql.plugins.codecompletion;

import net.sourceforge.squirrel_sql.client.plugin.IPlugin;
import net.sourceforge.squirrel_sql.client.plugin.PluginResources;

public final class Resources extends PluginResources
{
	Resources(IPlugin plugin)
	{
		super(Resources.class.getName(), plugin);
	}

	interface IKeys
	{
	}
}
