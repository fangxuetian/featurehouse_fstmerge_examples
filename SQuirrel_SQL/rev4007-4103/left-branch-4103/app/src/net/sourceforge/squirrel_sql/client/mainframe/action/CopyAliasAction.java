package net.sourceforge.squirrel_sql.client.mainframe.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;

import net.sourceforge.squirrel_sql.fw.sql.ISQLAlias;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;

import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.action.SquirrelAction;
import net.sourceforge.squirrel_sql.client.gui.db.AliasesListInternalFrame;
import net.sourceforge.squirrel_sql.client.gui.db.IAliasesList;
import net.sourceforge.squirrel_sql.client.gui.db.SQLAlias;


public class CopyAliasAction extends SquirrelAction
{
   
   private static ILogger s_log =
      LoggerController.createLogger(CopyAliasAction.class);

    
    private static final StringManager s_stringMgr =
        StringManagerFactory.getStringManager(CopyAliasAction.class);

   
   private final IAliasesList _aliases;

   
   public CopyAliasAction(IApplication app, IAliasesList list)
   {
      super(app);
      if (list == null)
      {
         throw new IllegalArgumentException("Null AliasesList passed");
      }
      _aliases = list;
   }

   
   public void actionPerformed(ActionEvent evt)
   {
      IApplication app = getApplication();
      AliasesListInternalFrame tw = app.getWindowManager().getAliasesListInternalFrame();
      tw.moveToFront();
      try
      {
         tw.setSelected(true);
      }
      catch (PropertyVetoException ex)
      {
            
         s_log.error(s_stringMgr.getString("CopyAliasAction.error.selectingwindow"), ex);
      }
      SQLAlias alias = _aliases.getSelectedAlias();
      if (alias != null)
      {
         new CopyAliasCommand(getApplication(), alias).execute();
      }
   }
}
