package net.sourceforge.squirrel_sql.client.session.action;


import net.sourceforge.squirrel_sql.client.action.SquirrelAction;
import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.session.ISQLPanelAPI;

import java.awt.event.ActionEvent;


public class FileCloseAction extends SquirrelAction  implements ISQLPanelAction
{
   private ISQLPanelAPI _panel;

   public FileCloseAction(IApplication app)
   {
      super(app);
   }

   public void actionPerformed(ActionEvent e)
   {
      _panel.fileClose();
   }

   public void setSQLPanel(ISQLPanelAPI panel)
   {
      _panel = panel;
      setEnabled(null != _panel);
   }
}