package net.sourceforge.squirrel_sql.client.gui.desktopcontainer;

import net.sourceforge.squirrel_sql.client.gui.desktopcontainer.docktabdesktop.DockHandle;
import net.sourceforge.squirrel_sql.client.gui.desktopcontainer.docktabdesktop.DockHandleListener;
import net.sourceforge.squirrel_sql.client.gui.desktopcontainer.docktabdesktop.DockHandleEvent;
import net.sourceforge.squirrel_sql.client.gui.desktopcontainer.docktabdesktop.AutoHideManager;
import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.session.event.ISessionListener;
import net.sourceforge.squirrel_sql.client.session.event.SessionEvent;
import net.sourceforge.squirrel_sql.client.session.event.SessionAdapter;
import net.sourceforge.squirrel_sql.fw.id.IIdentifier;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import java.beans.VetoableChangeListener;
import java.beans.PropertyVetoException;

public class DockDelegate implements IDockDelegate
{
   private JPanel _contentPane = new JPanel();
   private IApplication _app;
   private String _title;
   private DockWidget _dockWidget;
   private WidgetEventCaster _eventCaster = new WidgetEventCaster();
   private DockHandle _dockHandle;


   public DockDelegate(IApplication app, String title, DockWidget dockWidget)
   {
      _app = app;
      _title = title;
      _dockWidget = dockWidget;
   }

   public void addDockWidgetListener(WidgetListener widgetListener)
   {
      _eventCaster.addDockWidgetListener(widgetListener);
   }

   public void removeDockWidgetListener(WidgetListener widgetListener)
   {
      _eventCaster.removeDockWidgetListener(widgetListener);
   }


   public boolean isVisible()
   {
      return true;  
   }

   public void _moveToFront()
   {
      _dockHandle.openDock();
   }

   public void setDefaultCloseOperation(int operation)
   {
      
   }

   public Container getContentPane()
   {
      return _contentPane;
   }


   public String getTitle()
   {
      return _title;
   }


   public void _setTitle(String title)
   {
      
   }

   public void _setVisible(boolean aFlag)
   {
      
   }

   public Container getAwtContainer()
   {
      return null;  
   }

   public void setContentPane(JPanel contentPane)
   {
      
   }

   public void showOk(String msg)
   {
      
   }

   public Dimension getSize()
   {
      return null;  
   }

   public void setSize(Dimension size)
   {
      
   }

   public void _addNotify()
   {
      
   }
   

   public void addFocusListener(FocusListener focusListener)
   {
      
   }

   public void removeFocusListener(FocusListener focusListener)
   {
      
   }

   public void addVetoableChangeListener(VetoableChangeListener vetoableChangeListener)
   {
      
   }

   public void removeVetoableChangeListener(VetoableChangeListener vetoableChangeListener)
   {
      
   }


   public void setBounds(Rectangle rectangle)
   {
      
   }

   public void setSelected(boolean b) throws PropertyVetoException
   {
      if(b)
      {
         _dockHandle.openDock();
      }
      else
      {
         _dockHandle.closeDock();
      }
   }

   public void setLayer(Integer layer)
   {
   }

   public void pack()
   {
   }

   public void makeToolWindow(boolean isToolWindow)
   {
   }

   public void _dispose()
   {
   }

   public void _updateUI()
   {
   }

   public void centerWithinDesktop()
   {
   }

   public JInternalFrame getInternalFrame()
   {
      return null;
   }


   public void setDockHandle(DockHandle dockHandle)
   {
      _dockHandle = dockHandle;

      _app.getSessionManager().addSessionListener(new SessionAdapter()
      {
         public void sessionConnected(SessionEvent evt)
         {
            _dockHandle.mayAutoHide();
         }
      });


      _dockHandle.addDockHandleListener(new DockHandleListener()
      {
         public void dockClosing(DockHandleEvent e)
         {
            _eventCaster.fireWidgetClosing(new WidgetEvent(e, _dockWidget));
         }

         public void dockOpened(DockHandleEvent e)
         {
            _eventCaster.fireWidgetOpened(new WidgetEvent(e, _dockWidget));
         }
      });
   }


   public void putClientProperty(Object key, Object prop)
   {
      _contentPane.putClientProperty(key, prop);
   }

   public Object getClientProperty(Object key)
   {
      return _contentPane.getClientProperty(key);
   }

   public void fireWidgetClosing()
   {
      ActionEvent ae = new ActionEvent(_dockWidget, ActionEvent.ACTION_PERFORMED, "fireWidgetClosing");
      _eventCaster.fireWidgetClosing(new WidgetEvent(new DockHandleEvent(ae), _dockWidget));
   }

   public void fireWidgetClosed()
   {
      ActionEvent ae = new ActionEvent(_dockWidget, ActionEvent.ACTION_PERFORMED, "fireWidgetClosed");
      _eventCaster.fireWidgetClosed(new WidgetEvent(new DockHandleEvent(ae), _dockWidget));
   }

   public void validate()
   {
      _contentPane.validate();
   }

   public void setFrameIcon(Icon icon)
   {
      
   }

   public void toFront()
   {
   }

   public void requestFocus()
   {
      
   }

   public void setMaximum(boolean b)
   {
      
   }

   public void setBorder(Border border)
   {
      
   }

   public void setPreferredSize(Dimension dimension)
   {
      
   }

   public boolean isToolWindow()
   {
      return false;  
   }

   public boolean isClosed()
   {
      return _dockHandle.isClosed();
   }

   public boolean isIcon()
   {
      return false;
   }
}
