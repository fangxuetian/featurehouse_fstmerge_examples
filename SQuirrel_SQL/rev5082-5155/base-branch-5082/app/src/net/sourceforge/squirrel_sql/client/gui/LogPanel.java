package net.sourceforge.squirrel_sql.client.gui;

import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.mainframe.action.ViewLogsCommand;
import net.sourceforge.squirrel_sql.client.resources.SquirrelResources;
import net.sourceforge.squirrel_sql.fw.gui.ErrorDialog;
import net.sourceforge.squirrel_sql.fw.util.log.ILoggerListener;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.util.Date;
import java.text.DateFormat;



public class LogPanel extends JPanel
{
    private static final long serialVersionUID = -2886311809367056785L;

    private static final StringManager s_stringMgr =
		StringManagerFactory.getStringManager(MemoryPanel.class);


	transient private SquirrelResources _resources;

	private static final int LOG_TYPE_INFO = 0;
	private static final int LOG_TYPE_WARN = 1;
	private static final int LOG_TYPE_ERROR = 2;


	private JButton _btnLastLog = new JButton();
	private JLabel _lblLogInfo = new JLabel();
	private JButton _btnViewLogs = new JButton();

	private Timer _displayLastLogTimer;
	private Timer _whiteIconTimer;

	private final Vector<LogData> _logsDuringDisplayDelay = new Vector<LogData>();
	transient private LogData _curlogToDisplay;
    transient private IApplication _app;


    transient private LogStatistics _statistics = new LogStatistics();

	public LogPanel(IApplication app)
	{
		_app = app;
		_resources = _app.getResources();
		createGui();


		setIconForCurLogType();

		_whiteIconTimer = new Timer(5000, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				_btnLastLog.setIcon(_resources.getIcon(SquirrelResources.IImageNames.WHITE_GEM));
			}
		});

		_whiteIconTimer.setRepeats(false);

		int displayDelay = 200;
		_displayLastLogTimer = new Timer(displayDelay, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				updatePanel();
			}
		});

		_displayLastLogTimer.setRepeats(false);

		LoggerController.addLoggerListener(new ILoggerListener()
		{
			public void info(Class<?> source, Object message)
			{
				_statistics.setInfoCount(_statistics._infoCount + 1);
				addLog(LOG_TYPE_INFO, source.getName(), message, null);
			}

			public void info(Class<?> source, Object message, Throwable th)
			{
				_statistics.setInfoCount(_statistics._infoCount + 1);
				addLog(LOG_TYPE_INFO, source.getName(), message, th);
			}

			public void warn(Class<?> source, Object message)
			{
				_statistics.setWarnCount(_statistics._warnCount + 1);
				addLog(LOG_TYPE_WARN, source.getName(), message, null);
			}

			public void warn(Class<?> source, Object message, Throwable th)
			{
				_statistics.setWarnCount(_statistics._warnCount + 1);
				addLog(LOG_TYPE_WARN, source.getName(), message, th);
			}

			public void error(Class<?> source, Object message)
			{
				_statistics.setErrorCount(_statistics._errorCount + 1);
				addLog(LOG_TYPE_ERROR, source.getName(), message, message instanceof Throwable ? (Throwable)message:null);
			}

			public void error(Class<?> source, Object message, Throwable th)
			{
				_statistics.setErrorCount(_statistics._errorCount + 1);
				addLog(LOG_TYPE_ERROR, source.getName(), message, th);
			}
		});


		_btnLastLog.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				setIconForCurLogType();
			}

			public void mouseExited(MouseEvent e)
			{
				if(false == _whiteIconTimer.isRunning())
				{
					_btnLastLog.setIcon(_resources.getIcon(SquirrelResources.IImageNames.WHITE_GEM));
				}
			}
		});

		_btnLastLog.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				showLogInDialog();
			}
		});

		_btnViewLogs.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new ViewLogsCommand(_app).execute();
			}
		});

	}

	private void createGui()
	{
		setLayout(new BorderLayout(5,0));

		ImageIcon viewLogsIcon = _resources.getIcon(SquirrelResources.IImageNames.LOGS);
		_btnViewLogs.setIcon(viewLogsIcon);

		Dimension prefButtonSize = new Dimension(viewLogsIcon.getIconWidth(), viewLogsIcon.getIconHeight());
		_btnLastLog.setPreferredSize(prefButtonSize);
		_btnViewLogs.setPreferredSize(prefButtonSize);

		_btnLastLog.setBorder(null);
		_btnViewLogs.setBorder(null);

		JPanel pnlButtons = new JPanel(new GridLayout(1,2,3,0));
		pnlButtons.add(_btnLastLog);
		pnlButtons.add(_btnViewLogs);

		add(pnlButtons, BorderLayout.EAST);
		add(_lblLogInfo, BorderLayout.CENTER);

		
		_btnLastLog.setToolTipText(s_stringMgr.getString("LogPanel.viewLastLog"));

		
		_btnViewLogs.setToolTipText(s_stringMgr.getString("LogPanel.openLogs"));
	}


	private void showLogInDialog()
	{
		if(null != _curlogToDisplay)
		{
			Object[] params = new Object[]
				{
					_curlogToDisplay.source,
					_curlogToDisplay.logTime,
					_curlogToDisplay.message
				};

			
			String extMsg = s_stringMgr.getString("LogPanel.logMsg", params);
			ErrorDialog errorDialog = new ErrorDialog(_app.getMainFrame(), extMsg, _curlogToDisplay.throwable);

			String title;

			switch(_curlogToDisplay.logType)
			{
				case LOG_TYPE_INFO:
					
					title = s_stringMgr.getString("LogPanel.titleInfo");
					break;
				case LOG_TYPE_WARN:
					
					title = s_stringMgr.getString("LogPanel.titleWarn");
					break;
				case LOG_TYPE_ERROR:
					
					title = s_stringMgr.getString("LogPanel.titleError");
					break;
				default:
					
					title = s_stringMgr.getString("LogPanel.titleUnknown");
					break;
			}

			errorDialog.setTitle(title);
			errorDialog.setVisible(true);
		}
	}


	private void addLog(int logType, String source, Object message, Throwable t)
	{
		LogData log = new LogData();
		log.logType = logType;
		log.source = source;
		log.message = message;
		log.throwable = t;


		synchronized(_logsDuringDisplayDelay)
		{
			_logsDuringDisplayDelay.add(log);
		}

		_displayLastLogTimer.restart();
	}


	private void updatePanel()
	{
		LogData[] logs;
		synchronized(_logsDuringDisplayDelay)
		{
			logs = _logsDuringDisplayDelay.toArray(new LogData[_logsDuringDisplayDelay.size()]);
			_logsDuringDisplayDelay.clear();
		}

		_curlogToDisplay = null;
		for (int i = 0; i < logs.length; i++)
		{
			if(null == _curlogToDisplay)
			{
				_curlogToDisplay = logs[i];
			}
			else if(_curlogToDisplay.logType <= logs[i].logType)
			{
				_curlogToDisplay = logs[i];
			}
		}


		_lblLogInfo.setText(_statistics.toString());


		setIconForCurLogType();

		_whiteIconTimer.restart();
	}

	private void setIconForCurLogType()
	{
		if(null == _curlogToDisplay)
		{
			_btnLastLog.setIcon(_resources.getIcon(SquirrelResources.IImageNames.WHITE_GEM));
			return;
		}

		switch(_curlogToDisplay.logType)
		{
			case LOG_TYPE_INFO:
				_btnLastLog.setIcon(_resources.getIcon(SquirrelResources.IImageNames.GREEN_GEM));
				break;
			case LOG_TYPE_WARN:
				_btnLastLog.setIcon(_resources.getIcon(SquirrelResources.IImageNames.YELLOW_GEM));
				break;
			case LOG_TYPE_ERROR:
				_btnLastLog.setIcon(_resources.getIcon(SquirrelResources.IImageNames.RED_GEM));
				break;
		}
	}

	private static class LogData
	{
		int logType = -1;
		Object message = null;
		Throwable throwable = null;
		String source;
		String logTime;

		public LogData()
		{
			logTime = DateFormat.getInstance().format(new Date());
		}

	}

	private static class LogStatistics
	{
		private int _errorCount;
		private int _warnCount;
		private int _infoCount;

		private String _toString = "";

		public LogStatistics()
		{
			updateToString();
		}


		public String toString()
		{
			return _toString;
		}

		void setErrorCount(int errorCount)
		{
			this._errorCount = errorCount;
			updateToString();
		}

		private void updateToString()
		{
			Object[] params = new Integer[]
				{
					Integer.valueOf(_errorCount),
                    Integer.valueOf(_warnCount),
                    Integer.valueOf(_infoCount),
				};
			
			_toString = s_stringMgr.getString("LogPanel.logInfoLabel", params);
		}

		void setWarnCount(int warnCount)
		{
			this._warnCount = warnCount;
			updateToString();
		}

		void setInfoCount(int infoCount)
		{
			this._infoCount = infoCount;
			updateToString();
		}
	}


}