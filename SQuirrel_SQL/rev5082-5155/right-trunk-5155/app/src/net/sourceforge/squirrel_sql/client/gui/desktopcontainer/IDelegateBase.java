package net.sourceforge.squirrel_sql.client.gui.desktopcontainer;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusListener;
import java.beans.VetoableChangeListener;
import java.beans.PropertyVetoException;

public interface IDelegateBase
{
   boolean isVisible();

   
   void _moveToFront();

   void setDefaultCloseOperation(int operation);

   Container getContentPane();

   void pack();

   String getTitle();

   void makeToolWindow(boolean isToolWindow);

   void _dispose();

   void _setTitle(String title);

   void _updateUI();

   void _setVisible(boolean aFlag);

   void _addNotify();

   void centerWithinDesktop();

   Container getAwtContainer();

   void setContentPane(JPanel contentPane);

   void showOk(String msg);

   Dimension getSize();

   void setSize(Dimension size);

   void addFocusListener(FocusListener focusListener);
   void removeFocusListener(FocusListener focusListener);

   void addVetoableChangeListener(VetoableChangeListener vetoableChangeListener);
   void removeVetoableChangeListener(VetoableChangeListener vetoableChangeListener);

   
   JInternalFrame getInternalFrame();

   void setBounds(Rectangle rectangle);

   void setSelected(boolean b)
      throws PropertyVetoException;

   void setLayer(Integer layer);

   void putClientProperty(Object key, Object prop);

   Object getClientProperty(Object key);

   void fireWidgetClosing();

   void fireWidgetClosed();

   void validate();

   void setFrameIcon(Icon icon);

   void toFront();

   void requestFocus();

   void setMaximum(boolean b)
      throws PropertyVetoException;


   void setBorder(Border border);

   void setPreferredSize(Dimension dimension);

   boolean isToolWindow();

   boolean isClosed();

   boolean isIcon();

}
