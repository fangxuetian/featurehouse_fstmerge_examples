package net.sourceforge.squirrel_sql.plugins.syntax.netbeans;

import java.awt.Event;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.event.SessionAdapter;
import net.sourceforge.squirrel_sql.client.session.event.SessionEvent;
import net.sourceforge.squirrel_sql.client.session.parser.ParserEventsAdapter;
import net.sourceforge.squirrel_sql.client.session.parser.IParserEventsProcessor;
import net.sourceforge.squirrel_sql.client.session.parser.kernel.ErrorInfo;
import net.sourceforge.squirrel_sql.fw.id.IIdentifier;
import net.sourceforge.squirrel_sql.plugins.syntax.KeyManager;
import net.sourceforge.squirrel_sql.plugins.syntax.SyntaxPreferences;
import net.sourceforge.squirrel_sql.plugins.syntax.SyntaxPugin;

import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.BaseSettingsInitializer;
import org.netbeans.editor.Settings;


public class NetbeansSQLEditorPane extends JEditorPane
{
   
	private static final long serialVersionUID = -7433339152923153176L;
	private boolean _parsingInitialized;
   private ISession _session;
   private ErrorInfo[] _currentErrorInfos = new ErrorInfo[0];
   private SyntaxPreferences _prefs;
   private SyntaxFactory _syntaxFactory;
   private SyntaxPugin _plugin;

   private IIdentifier _sqlEntryPanelIdentifier;
	private SessionAdapter _sessionListener;
   private NetbeansPropertiesWrapper _propertiesWrapper;

   public NetbeansSQLEditorPane(ISession session, SyntaxPreferences prefs, SyntaxFactory syntaxFactory, SyntaxPugin plugin, IIdentifier sqlEntryPanelIdentifier, NetbeansPropertiesWrapper propertiesWrapper)
	{
		_session = session;

		_prefs = prefs;
		_syntaxFactory = syntaxFactory;
		_plugin = plugin;
		_sqlEntryPanelIdentifier = sqlEntryPanelIdentifier;
      _propertiesWrapper = propertiesWrapper;

		_syntaxFactory.putEditorPane(_session, this);

		Settings.removeInitializer(BaseSettingsInitializer.NAME);
		Settings.addInitializer(new BaseSettingsInitializer(), Settings.CORE_LEVEL);
		
		
		
		
		
		
		
		
		
		Settings.removeInitializer(SQLSettingsInitializer.NAME);

		Font font = _session.getProperties().getFontInfo().createFont();
		Settings.addInitializer(new SQLSettingsInitializer(SQLKit.class, _prefs, font, _plugin));


		
		
		setEditorKit(new SQLKit(syntaxFactory));
		
		

		modifyKeyStrokes();

		Document doc = getDocument();
      _syntaxFactory.putDocument(_session, _propertiesWrapper, doc);

		_sessionListener = new SessionAdapter()
		{
			public void sessionClosed(SessionEvent evt)
			{
				dispose(evt);
			}
		};
		_session.getApplication().getSessionManager().addSessionListener(_sessionListener);


		setToolTipText("Just to make getToolTiptext() to be called");

		new KeyManager(this);
	}


	private void modifyKeyStrokes()
   {
      
      
      
      
      
      KeyStroke ctrlEnterStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, java.awt.event.InputEvent.CTRL_MASK);
      getKeymap().removeKeyStrokeBinding(ctrlEnterStroke);

      
      KeyStroke ctrlJStroke = KeyStroke.getKeyStroke(KeyEvent.VK_J, java.awt.event.InputEvent.CTRL_MASK);
      getKeymap().removeKeyStrokeBinding(ctrlJStroke);

      
      KeyStroke ctrlTStroke = KeyStroke.getKeyStroke(KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK);
      getKeymap().removeKeyStrokeBinding(ctrlTStroke);

      
      KeyStroke ctrlShiftFStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK | java.awt.event.InputEvent.SHIFT_MASK);
      getKeymap().removeKeyStrokeBinding(ctrlShiftFStroke);

      
      KeyStroke ctrlDStroke = KeyStroke.getKeyStroke(KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK);
      getKeymap().removeKeyStrokeBinding(ctrlDStroke);

      
      KeyStroke ctrlSubstractStroke = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, java.awt.event.InputEvent.CTRL_MASK);
      getKeymap().removeKeyStrokeBinding(ctrlSubstractStroke);

      
      KeyStroke ctrlShiftSubstractStroke = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, java.awt.event.InputEvent.CTRL_MASK | java.awt.event.InputEvent.SHIFT_MASK);
      getKeymap().removeKeyStrokeBinding(ctrlShiftSubstractStroke);

		
		KeyStroke ctrlShiftXStroke = KeyStroke.getKeyStroke(KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK | java.awt.event.InputEvent.SHIFT_MASK);
		getKeymap().removeKeyStrokeBinding(ctrlShiftXStroke);

		
		KeyStroke ctrlShiftCStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK | java.awt.event.InputEvent.SHIFT_MASK);
		getKeymap().removeKeyStrokeBinding(ctrlShiftCStroke);

		
      


      
      
      
      KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, Event.SHIFT_MASK);
      final Action origAction = getKeymap().getAction(ks);
      Action triggerParserAction = new AbstractAction()
      {
			private static final long serialVersionUID = 1158324060321498929L;

			public void actionPerformed(ActionEvent e)
         {
            
            
            origAction.actionPerformed(e);
            if(_session.getActiveSessionWindow().hasSQLPanelAPI())
            {
               IIdentifier entryPanelId = _session.getSQLPanelAPIOfActiveSessionWindow().getSQLEntryPanel().getIdentifier();
               triggerParser(entryPanelId);
            }
         }
      };
      
      

      getKeymap().addActionForKeyStroke(ks, triggerParserAction);
   }

   private void triggerParser(IIdentifier entryPanelId)
   {
      IParserEventsProcessor parserEventsProcessor = _propertiesWrapper.getParserEventsProcessor(entryPanelId, _session);

      if(null != parserEventsProcessor)
      {
         parserEventsProcessor.triggerParser();
      }
   }


   public void updateFromPreferences()
   {
      Settings.removeInitializer(BaseSettingsInitializer.NAME);
      Settings.addInitializer(new BaseSettingsInitializer(), Settings.CORE_LEVEL);
      
      
      
      
      
      
      
      
      
      Settings.removeInitializer(SQLSettingsInitializer.NAME);

      Font font = _session.getProperties().getFontInfo().createFont();
      Settings.addInitializer(new SQLSettingsInitializer(SQLKit.class, _prefs, font, _plugin));


      modifyKeyStrokes();

      Document doc = getDocument();
      _syntaxFactory.putDocument(_session, _propertiesWrapper, doc);

   }


   public String getToolTipText(MouseEvent event)
   {
      int pos = viewToModel(event.getPoint());

      initParsing();

      for (int i = 0; i < _currentErrorInfos.length; i++)
      {
         if(_currentErrorInfos[i].beginPos-1 <= pos && pos <= _currentErrorInfos[i].endPos)
         {
            return _currentErrorInfos[i].message;
         }
      }

      return null;
   }


   private void initParsing()
   {
      if(false == _parsingInitialized && null != _propertiesWrapper.getParserEventsProcessor(_sqlEntryPanelIdentifier, _session))
      {
         _parsingInitialized = true;
         _propertiesWrapper.getParserEventsProcessor(_sqlEntryPanelIdentifier, _session).addParserEventsListener(new ParserEventsAdapter()
         {
            public void errorsFound(ErrorInfo[] errorInfos)
            {
               onErrorsFound(errorInfos);
            }
         });
      }
   }

	private void onErrorsFound(ErrorInfo[] errorInfos)
   {
      _currentErrorInfos = errorInfos;
   }

   public String getText()
   {
      return super.getText().replaceAll("\r\n", "\n");
   }

   public void addUndoableEditListener(UndoableEditListener um) {
       getDocument().addUndoableEditListener(um);
   }
   
   public void setUndoManager(UndoManager manager)
   {
      getDocument().addUndoableEditListener(manager);
      getDocument().putProperty( BaseDocument.UNDO_MANAGER_PROP, manager );
   }

   public IIdentifier getSqlEntryPanelIdentifier()
   {
      return _sqlEntryPanelIdentifier;
   }



	private void dispose(SessionEvent evt)
	{
		if(evt.getSession().getIdentifier().equals(_session.getIdentifier()))
		{
			
			
			
			
			Settings.removeInitializer(SQLSettingsInitializer.NAME);

			
			_session.getApplication().getSessionManager().removeSessionListener(_sessionListener);

			
			
			
			
			
			_session = null;
		}
	}
}
