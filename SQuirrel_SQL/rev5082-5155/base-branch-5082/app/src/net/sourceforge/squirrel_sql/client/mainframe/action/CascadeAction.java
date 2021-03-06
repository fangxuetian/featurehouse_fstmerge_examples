package net.sourceforge.squirrel_sql.client.mainframe.action;

import java.awt.event.ActionEvent;

import net.sourceforge.squirrel_sql.fw.gui.CursorChanger;
import net.sourceforge.squirrel_sql.client.mainframe.action.CascadeInternalFramesAction;

import net.sourceforge.squirrel_sql.client.IApplication;

public class CascadeAction extends CascadeInternalFramesAction
{
	
	private IApplication _app;

	
	public CascadeAction(IApplication app)
	{
		super(app);
		_app = app;
		app.getResources().setupAction(
			this,
			_app.getSquirrelPreferences().getShowColoriconsInToolbar());
	}

	public void actionPerformed(ActionEvent evt)
	{
		CursorChanger cursorChg = new CursorChanger(_app.getMainFrame());
		cursorChg.show();
		try
		{
			super.actionPerformed(evt);
		}
		finally
		{
			cursorChg.restore();
		}
	}
}
