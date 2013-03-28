package net.sourceforge.squirrel_sql.client.session.action;

import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.action.SquirrelAction;
import net.sourceforge.squirrel_sql.client.session.ISQLPanelAPI;

import java.awt.event.ActionEvent;


public class CloseAllSQLResultTabsButCurrentAction extends SquirrelAction
											implements ISQLPanelAction
{

	
	private ISQLPanelAPI _panel;


	
	public CloseAllSQLResultTabsButCurrentAction(IApplication app)
	{
		super(app);
	}

	public void setSQLPanel(ISQLPanelAPI panel)
	{
		_panel = panel;
      setEnabled(null != _panel);
	}

	
	public synchronized void actionPerformed(ActionEvent evt)
	{
		if (_panel != null)
		{
         _panel.closeAllButCurrentResultTabs();
		}
	}
}
