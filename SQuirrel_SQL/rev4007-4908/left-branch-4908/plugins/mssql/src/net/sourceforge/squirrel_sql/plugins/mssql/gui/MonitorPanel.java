package net.sourceforge.squirrel_sql.plugins.mssql.gui;



import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sourceforge.squirrel_sql.client.session.properties.SessionProperties;
import net.sourceforge.squirrel_sql.fw.datasetviewer.BaseDataSetViewerDestination;
import net.sourceforge.squirrel_sql.fw.datasetviewer.DataSetException;
import net.sourceforge.squirrel_sql.fw.datasetviewer.IDataSetViewer;
import net.sourceforge.squirrel_sql.fw.datasetviewer.ResultSetDataSet;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;

public class MonitorPanel extends net.sourceforge.squirrel_sql.client.session.mainpanel.BaseMainPanelTab {

	private static final StringManager s_stringMgr =
		StringManagerFactory.getStringManager(MonitorPanel.class);

    
    private final static ILogger s_log = 
        LoggerController.createLogger(MonitorPanel.class);
    
	 private Connection _conn = null;

	 private Date _refreshDate;
	 private JPanel _mainPanel;

	 private JSlider _frequency = null;

	 private IDataSetViewer _whoViewer;
	 private CallableStatement _whoStmt = null;
	 private ResultSetDataSet _whoDataSet = null;

	 private IDataSetViewer _perfViewer;
	 private PreparedStatement _perfStmt = null;
	 private ResultSetDataSet _perfDataSet = null;

	 private CallableStatement _monitorStmt = null;

	 private Timer _refreshTimer = null;

	 private boolean _haveSession = false;

	 private boolean _inRefresh = false;

	 
	 public MonitorPanel() {
		  super();
	 }

	 public java.awt.Component getComponent() {
		  if (_mainPanel == null) {
				_mainPanel = buildMainPanel();

				
				_refreshTimer = new Timer(_frequency.getValue(),new ActionListener() {
					 public void actionPerformed(ActionEvent e) {
						  if (!_haveSession)
								return;
						  if (_inRefresh)
								return;
						  _inRefresh = true;
						  refreshData();
						  _inRefresh = false;
					 }
				});
		  }
		  return _mainPanel;
	 }

	 public String getHint() {
		 
		  return s_stringMgr.getString("mssql.activity");
	 }

	 public String getTitle() {
		  if (_refreshDate == null)
				
				return s_stringMgr.getString("mssql.monitor");
		  else {
				
				return s_stringMgr.getString("mssql.monitorAsOf", _refreshDate);
		  }
	 }

	 protected void refreshComponent() {
		  if (!_haveSession) {
				try {
					 _conn = this.getSession().getSQLConnection().getConnection();

					 _whoStmt = _conn.prepareCall("{ call sp_who }");
					 _whoDataSet = new ResultSetDataSet();

					 _perfStmt = _conn.prepareStatement("SELECT * FROM master.dbo.sysperfinfo");
					 _perfDataSet = new ResultSetDataSet();

					 _monitorStmt = _conn.prepareCall("{ call sp_monitor }");

					 _haveSession = true;
				}
				catch (java.sql.SQLException ex) {
					 s_log.error("Unexpected exception: "+ex.getMessage(), ex);
				}
		  }
		  refreshData();
	 }

	 private void refreshData() {
		  ResultSet rs;

		  try {
				_refreshDate = new Date();

				rs = _whoStmt.executeQuery();
				_whoDataSet.setResultSet(rs);
				_whoViewer.show(_whoDataSet);

				rs = _perfStmt.executeQuery();
				_perfDataSet.setResultSet(rs);
				_perfViewer.show(_perfDataSet);
		  }
		  catch (java.sql.SQLException ex) {
              s_log.error("Unexpected exception: "+ex.getMessage(), ex);
          }
		  catch (DataSetException dse) {
              s_log.error("Unexpected exception: "+dse.getMessage(), dse);
          }
	 }

	 private JPanel buildMainPanel() {
		  SessionProperties props = this.getSession().getProperties();

		  JPanel panel = new JPanel();
		  GridBagLayout gridBag = new GridBagLayout();
		  panel.setLayout(gridBag);

		  _frequency = new JSlider();
		  _frequency.setMinimum(0);
		  _frequency.setMaximum(20);
		  _frequency.setValue(0);         
		  _frequency.setMajorTickSpacing(2);
		  _frequency.setMinorTickSpacing(1);
		  _frequency.setPaintLabels(true);
		  _frequency.setPaintTicks(true);
		  _frequency.setSnapToTicks(true);
		  _frequency.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					 JSlider slider = (JSlider) e.getSource();
					 if (slider.getValue() == 0) {
						  
						  if (_refreshTimer.isRunning())
								_refreshTimer.stop();
					 }
					 else {
						  
						  _refreshTimer.setDelay(slider.getValue() * 1000);
						  if (!_refreshTimer.isRunning())
								_refreshTimer.start();
					 }
					 
					 slider.setToolTipText(s_stringMgr.getString("mssql.delay", Integer.valueOf(slider.getValue())));
				}
		  });
		  addComponentToGridBag(0,0,1,1,0.0,0.0,GridBagConstraints.BOTH,gridBag,_frequency,panel);


		  
		  JButton refreshButton = new JButton(s_stringMgr.getString("mssql.refreshNow"));
		  refreshButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					 if (!_haveSession)
						  return;
					 if (_inRefresh)
						  return;
					 _inRefresh = true;
					 refreshData();
					 _inRefresh = false;
				}
		  });
		  addComponentToGridBag(GridBagConstraints.RELATIVE,0,1,1,0.0,0.0,GridBagConstraints.NONE,gridBag,refreshButton,panel);

		  _whoViewer = BaseDataSetViewerDestination.getInstance(props.getReadOnlySQLResultsOutputClassName(), null);
		  JScrollPane whoScroll = new JScrollPane(_whoViewer.getComponent());
		  
		  whoScroll.setBorder(BorderFactory.createTitledBorder(s_stringMgr.getString("mssql.currentActivity")));
		  addComponentToGridBag(0,1,1,1,1.0,1.0,GridBagConstraints.BOTH,gridBag,whoScroll,panel);

		  _perfViewer = BaseDataSetViewerDestination.getInstance(props.getReadOnlySQLResultsOutputClassName(), null);
		  JScrollPane perfScroll = new JScrollPane(_perfViewer.getComponent());
		  
		  perfScroll.setBorder(BorderFactory.createTitledBorder(s_stringMgr.getString("mssql.performace")));
		  addComponentToGridBag(GridBagConstraints.RELATIVE,1,1,1,1.0,1.0,GridBagConstraints.BOTH,gridBag,perfScroll,panel);

		  return panel;
	 }

	 private void addComponentToGridBag(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int fill, GridBagLayout gridBag, java.awt.Component component, java.awt.Container container) {
		  GridBagConstraints c = new GridBagConstraints();
		  c.gridx = gridx;
		  c.gridy = gridy;
		  c.gridwidth = gridwidth;
		  c.gridheight = gridheight;
		  c.fill = fill;
		  c.weightx = weightx;
		  c.weighty = weighty;
		  gridBag.setConstraints(component,c);
		  container.add(component);
	 }
}
