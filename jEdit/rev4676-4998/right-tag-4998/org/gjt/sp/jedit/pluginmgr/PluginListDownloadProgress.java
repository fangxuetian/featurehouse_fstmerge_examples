

package org.gjt.sp.jedit.pluginmgr;

import com.microstar.xml.XmlException;
import javax.swing.border.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import org.gjt.sp.jedit.options.GlobalOptions;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.Log;

class PluginListDownloadProgress extends JDialog
{
	PluginListDownloadProgress(PluginManager window)
	{
		super(window,
			jEdit.getProperty("plugin-list.progress.title"),true);

		this.window = window;

		JPanel content = new JPanel(new BorderLayout());
		content.setBorder(new EmptyBorder(12,12,12,12));
		setContentPane(content);

		JLabel caption = new JLabel(jEdit.getProperty("plugin-list.progress.caption"));
		caption.setBorder(new EmptyBorder(0,0,12,0));
		content.add(BorderLayout.NORTH,caption);

		Box box = new Box(BoxLayout.X_AXIS);
		box.add(Box.createGlue());
		JButton stop = new JButton(jEdit.getProperty("plugin-list.progress.stop"));
		stop.addActionListener(new ActionHandler());
		stop.setMaximumSize(stop.getPreferredSize());
		box.add(stop);
		box.add(Box.createGlue());
		content.add(BorderLayout.CENTER,box);

		addWindowListener(new WindowHandler());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setLocationRelativeTo(window);
		setResizable(false);
		show();
	}

	PluginList getPluginList()
	{
		return list;
	}

	
	private PluginManager window;
	private PluginList list;
	private DownloadThread thread;

	class DownloadThread extends Thread
	{
		public void run()
		{
			try
			{
				list = new PluginList();
				dispose();
			}
			catch(XmlException xe)
			{
				dispose();

				int line = xe.getLine();
				String path = jEdit.getProperty("plugin-manager.export-url");
				String message = xe.getMessage();
				Log.log(Log.ERROR,this,path + ":" + line
					+ ": " + message);
				String[] pp = { path, String.valueOf(line), message };
				GUIUtilities.error(window,"plugin-list.xmlerror",pp);
			}
			catch(Exception e)
			{
				dispose();

				Log.log(Log.ERROR,this,e);
				String[] pp = { e.toString() };

				String ok = jEdit.getProperty("common.ok");
				String proxyButton = jEdit.getProperty(
					"plugin-list.ioerror.proxy-servers");
				int retVal = JOptionPane.showOptionDialog(window,
					jEdit.getProperty("plugin-list.ioerror.message",pp),
					jEdit.getProperty("plugin-list.ioerror.title"),
					JOptionPane.YES_NO_OPTION,
					JOptionPane.ERROR_MESSAGE,
					null,
					new Object[] {
						proxyButton,
						ok
					},
					ok);

				if(retVal == 0)
					new GlobalOptions(window,"firewall");
			}
		}
	}

	class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			thread.stop();
			dispose();
		}
	}

	class WindowHandler extends WindowAdapter
	{
		boolean done;

		public void windowOpened(WindowEvent evt)
		{
			if(done)
				return;

			done = true;
			thread = new DownloadThread();
			thread.start();
		}

		public void windowClosing(WindowEvent evt)
		{
			thread.stop();
		}
	}
}
