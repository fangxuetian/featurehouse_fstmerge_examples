package net.sourceforge.squirrel_sql.client.gui.db.aliasproperties;

import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.gui.db.SQLAlias;
import net.sourceforge.squirrel_sql.fw.gui.MultipleLineLabel;
import net.sourceforge.squirrel_sql.fw.id.IIdentifier;
import net.sourceforge.squirrel_sql.fw.sql.ISQLAlias;
import net.sourceforge.squirrel_sql.fw.sql.SQLDriverPropertyCollection;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;


public class DriverPropertiesController implements IAliasPropertiesPanelController
{
   
   private static final StringManager s_stringMgr =
      StringManagerFactory.getStringManager(DriverPropertiesController.class);

   private DriverPropertiesPanel _propsPnl;

   
   private SQLDriverPropertyCollection _driverPropInfo;
   private ISQLAlias _alias;
   private IApplication _app;
   String _errMsg;
   private Color _origTblColor;

   public DriverPropertiesController(SQLAlias alias, IApplication app)
   {
      _alias = alias;
      _app = app;
      String aliasUrl = alias.getUrl();

      IIdentifier driverIdentifier = alias.getDriverIdentifier();
      if (driverIdentifier == null)
      {
         
         _errMsg = s_stringMgr.getString("DriverPropertiesController.noDriverSelected");
         _app.getMessageHandler().showErrorMessage(_errMsg);
         return;
      }
      final Driver jdbcDriver = app.getSQLDriverManager().getJDBCDriver(driverIdentifier);
      if (jdbcDriver == null)
      {
         
         _errMsg = s_stringMgr.getString("DriverPropertiesController.loadingDriverFailed", app.getDataCache().getDriver(driverIdentifier).getName());
         _app.getMessageHandler().showErrorMessage(_errMsg);
         return;
      } else {
          try {
              if (!jdbcDriver.acceptsURL( aliasUrl )) {
                  String driverName = 
                      app.getDataCache().getDriver(driverIdentifier).getName();
                  
                  
                  _errMsg = 
                      s_stringMgr.getString(
                              "DriverPropertiesController.invalidUrl", 
                              new String[] {driverName, aliasUrl});
                  _app.getMessageHandler().showErrorMessage(_errMsg);
                  return;
              }
          } catch (Exception e) {
              
              _errMsg = s_stringMgr.getString("DriverPropertiesController.loadingDriverFailed", app.getDataCache().getDriver(driverIdentifier).getName());
              _app.getMessageHandler().showErrorMessage(_errMsg);
              return;
          }
      }

      DriverPropertyInfo[] infoAr;
      try
      {
         infoAr = jdbcDriver.getPropertyInfo(alias.getUrl(), new Properties());
      }
      catch (SQLException e)
      {
         
         _errMsg = s_stringMgr.getString("DriverPropertiesController.gettingDriverPropetiesFailed");
         _app.getMessageHandler().showErrorMessage(_errMsg);
         return;
      }

      SQLDriverPropertyCollection driverPropertiesClone = alias.getDriverPropertiesClone();
      driverPropertiesClone.applyDriverPropertynfo(infoAr);
      _propsPnl = new DriverPropertiesPanel(driverPropertiesClone);

      _propsPnl.chkUseDriverProperties.setSelected(alias.getUseDriverProperties());
      updateTableEnabled();

      _propsPnl.chkUseDriverProperties.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            updateTableEnabled();
         }
      });


   }

   private void updateTableEnabled()
   {
      if(null == _origTblColor)
      {
         _origTblColor = _propsPnl.tbl.getForeground();
      }

      _propsPnl.tbl.setEnabled(_propsPnl.chkUseDriverProperties.isSelected());

      if(_propsPnl.chkUseDriverProperties.isSelected())
      {
         _propsPnl.tbl.setForeground(_origTblColor);
      }
      else
      {
         _propsPnl.tbl.setForeground(Color.lightGray);
      }
   }

   
   public SQLDriverPropertyCollection getSQLDriverPropertyCollection()
   {
      return _driverPropInfo;
   }

   public Component getPanelComponent()
   {
      if(null == _propsPnl)
      {
         return new MultipleLineLabel(_errMsg);
      }
      else
      {
         return _propsPnl;
      }
   }


   public void applyChanges()
   {
      if (null != _propsPnl)
      {
         _alias.setDriverProperties(_propsPnl.getSQLDriverProperties());
         _alias.setUseDriverProperties(_propsPnl.chkUseDriverProperties.isSelected());
      }
   }


   public String getTitle()
   {
      
      return s_stringMgr.getString("DriverPropertiesController.title");
   }

   public String getHint()
   {
      
      return s_stringMgr.getString("DriverPropertiesController.title");
   }

}
