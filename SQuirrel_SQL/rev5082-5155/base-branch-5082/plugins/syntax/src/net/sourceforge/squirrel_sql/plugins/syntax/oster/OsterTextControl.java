package net.sourceforge.squirrel_sql.plugins.syntax.oster;

import com.Ostermiller.Syntax.Lexer.Lexer;
import com.Ostermiller.Syntax.Lexer.SQLLexer;
import com.Ostermiller.Syntax.Lexer.Token;
import net.sourceforge.squirrel_sql.client.session.ExtendedColumnInfo;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.SQLTokenListener;
import net.sourceforge.squirrel_sql.client.session.schemainfo.SchemaInfo;
import net.sourceforge.squirrel_sql.client.session.parser.ParserEventsAdapter;
import net.sourceforge.squirrel_sql.client.session.parser.kernel.ErrorInfo;
import net.sourceforge.squirrel_sql.fw.gui.FontInfo;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;
import net.sourceforge.squirrel_sql.fw.id.IIdentifier;
import net.sourceforge.squirrel_sql.plugins.syntax.IConstants;
import net.sourceforge.squirrel_sql.plugins.syntax.SyntaxPreferences;
import net.sourceforge.squirrel_sql.plugins.syntax.SyntaxStyle;
import net.sourceforge.squirrel_sql.plugins.syntax.KeyManager;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class OsterTextControl extends JTextPane
{
    private static final long serialVersionUID = 1L;

    
	private static final ILogger s_log = LoggerController.createLogger(OsterTextControl.class);

	
	private final ISession _session;

	
	private Object doclock = new Object();

	
	private HighLightedDocument document;

	
	private DocumentReader documentReader;

	
	private Lexer syntaxLexer;

	
	private Colorer colorer;

	
	private Hashtable<String, SimpleAttributeSet> styles = 
	    new Hashtable<String, SimpleAttributeSet>();

	
	private final SyntaxPreferences _syntaxPrefs;

	private Vector<SQLTokenListener> _sqlTokenListeners = 
	    new Vector<SQLTokenListener>();
	private Vector<ErrorInfo> _currentErrorInfos = new Vector<ErrorInfo>();
   private Vector<ErrorInfo> _oldErrorInfos = new Vector<ErrorInfo>();


   OsterTextControl(ISession session, SyntaxPreferences prefs, final IIdentifier sqlEntryPanelIdentifier)
	{
		super();
		_session = session;
		_syntaxPrefs = prefs;

		document = new HighLightedDocument();
		setDocument(document);

		
		colorer = new Colorer();
		colorer.start();

		
		initStyles();

		
		documentReader = new DocumentReader(document);

		
		
		initDocument();

      setToolTipText("Just to make getToolTiptext() to be called");

		updateFromPreferences();

      SwingUtilities.invokeLater(new Runnable()
      {
         public void run()
         {
            initParser(_session, sqlEntryPanelIdentifier);
         }
      });

      new KeyManager(this);
	}

   private void initParser(ISession session, IIdentifier sqlEntryPanelIdentifier)
   {
      session.getParserEventsProcessor(sqlEntryPanelIdentifier).addParserEventsListener(new ParserEventsAdapter()
		{
			public void errorsFound(ErrorInfo[] errorInfos)
			{
				onErrorsFound(errorInfos);
			}
		});
   }

   private void onErrorsFound(ErrorInfo[] errorInfos)
	{
      boolean errorsChanged = false;
      if(_currentErrorInfos.size() == errorInfos.length)
      {
         for (int i = 0; i < errorInfos.length; i++)
         {
            if(false == errorInfos[i].equals(_currentErrorInfos.get(i)))
            {
               errorsChanged = true;
               break;
            }
         }
      }
      else
      {
         errorsChanged = true;
      }

      if(errorsChanged)
      {
         _oldErrorInfos.clear();
         _oldErrorInfos.addAll(_currentErrorInfos);

         _currentErrorInfos.clear();
         _currentErrorInfos.addAll(Arrays.asList(errorInfos));

         int heuristicDist = 20;

         for (int i = 0; i < errorInfos.length; i++)
         {
            int colBegin = Math.max(errorInfos[i].beginPos - heuristicDist, 0);
            int colLen = Math.min(errorInfos[i].endPos - errorInfos[i].beginPos + 2*heuristicDist, getDocument().getLength() - colBegin);
            color(colBegin, colLen, false, null);
         }

         for (int i = 0; i < _oldErrorInfos.size(); i++)
         {
            ErrorInfo errorInfo = _oldErrorInfos.elementAt(i);
            int colBegin = Math.max(errorInfo.beginPos - heuristicDist, 0);
            int colLen = Math.min(errorInfo.endPos - errorInfo.beginPos + 2*heuristicDist, getDocument().getLength() - colBegin);
            color(colBegin, colLen, false, null);
         }
      }
	}

   public void endColorerThread()
   {
      colorer.endThread();
   }

	
	public boolean getScrollableTracksViewportWidth()
	{
		final Component parent = getParent();
		final ComponentUI ui = getUI();

		if (parent != null)
		{
			return (ui.getPreferredSize(this).width <= parent.getSize().width);
		}
		return true;
	}

	void updateFromPreferences()
	{
		synchronized (doclock)
		{
			final FontInfo fi = _session.getProperties().getFontInfo();
			SyntaxStyle style;
			SimpleAttributeSet attribs;

			style = _syntaxPrefs.getColumnStyle();
			attribs = getMyStyle(IConstants.IStyleNames.COLUMN);
			applyStyle(attribs, style, fi);

			style = _syntaxPrefs.getCommentStyle();
			attribs = getMyStyle(IConstants.IStyleNames.COMMENT);
			applyStyle(attribs, style, fi);

			style = _syntaxPrefs.getDataTypeStyle();
			attribs = getMyStyle(IConstants.IStyleNames.DATA_TYPE);
			applyStyle(attribs, style, fi);

			style = _syntaxPrefs.getErrorStyle();
			attribs = getMyStyle(IConstants.IStyleNames.ERROR);
			applyStyle(attribs, style, fi);

			style = _syntaxPrefs.getFunctionStyle();
			attribs = getMyStyle(IConstants.IStyleNames.FUNCTION);
			applyStyle(attribs, style, fi);

			style = _syntaxPrefs.getIdentifierStyle();
			attribs = getMyStyle(IConstants.IStyleNames.IDENTIFIER);
			applyStyle(attribs, style, fi);

			style = _syntaxPrefs.getLiteralStyle();
			attribs = getMyStyle(IConstants.IStyleNames.LITERAL);
			applyStyle(attribs, style, fi);

			style = _syntaxPrefs.getOperatorStyle();
			attribs = getMyStyle(IConstants.IStyleNames.OPERATOR);
			applyStyle(attribs, style, fi);

			style = _syntaxPrefs.getReservedWordStyle();
			attribs = getMyStyle(IConstants.IStyleNames.RESERVED_WORD);
			applyStyle(attribs, style, fi);

			style = _syntaxPrefs.getSeparatorStyle();
			attribs = getMyStyle(IConstants.IStyleNames.SEPARATOR);
			applyStyle(attribs, style, fi);

			style = _syntaxPrefs.getTableStyle();
			attribs = getMyStyle(IConstants.IStyleNames.TABLE);
			applyStyle(attribs, style, fi);

			style = _syntaxPrefs.getWhiteSpaceStyle();
			attribs = getMyStyle(IConstants.IStyleNames.WHITESPACE);
			applyStyle(attribs, style, fi);

			colorAll();
		}
	}

	
	public void colorAll()
	{
		color(0, document.getLength(), false, null);
	}

	
	public void color(int position, int adjustment, boolean fireTableOrViewFoundEvent, String change)
	{
		colorer.color(position, adjustment, fireTableOrViewFoundEvent, change);
	}

	
	private SimpleAttributeSet getMyStyle(String styleName)
	{
		return styles.get(styleName);
	}

	private Token getNextToken() throws IOException
	{
      return syntaxLexer.getNextToken();
	}

	private void applyStyle(SimpleAttributeSet attribs, SyntaxStyle style,
								FontInfo fi)
	{
		StyleConstants.setFontFamily(attribs, fi.getFamily());
		StyleConstants.setFontSize(attribs, fi.getSize());
		StyleConstants.setBackground(attribs, new Color(style.getBackgroundRGB()));
		StyleConstants.setForeground(attribs, new Color(style.getTextRGB()));
		StyleConstants.setBold(attribs, style.isBold());
		StyleConstants.setItalic(attribs, style.isItalic());
	}

	
	private void initDocument()
	{
		syntaxLexer = new SQLLexer(documentReader);
		try
		{
			document.insertString(document.getLength(), "", getMyStyle("text"));
		}
		catch (BadLocationException ex)
		{
			s_log.error("Error setting initial document style", ex);
		}
	}

	
	private void initStyles()
	{
		final FontInfo fi = _session.getProperties().getFontInfo();

		SyntaxStyle style;
		SimpleAttributeSet attribs;

		
		attribs = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attribs, fi.getFamily());
		StyleConstants.setFontSize(attribs, fi.getSize());
		StyleConstants.setBackground(attribs, Color.white);
		StyleConstants.setForeground(attribs, Color.black);
		StyleConstants.setBold(attribs, false);
		StyleConstants.setItalic(attribs, false);
		styles.put("body", attribs);

		
		attribs = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attribs, fi.getFamily());
		StyleConstants.setFontSize(attribs, fi.getSize());
		StyleConstants.setBackground(attribs, Color.white);
		StyleConstants.setForeground(attribs, Color.blue);
		StyleConstants.setBold(attribs, true);
		StyleConstants.setItalic(attribs, false);
		styles.put("tag", attribs);

		
		attribs = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attribs, fi.getFamily());
		StyleConstants.setFontSize(attribs, fi.getSize());
		StyleConstants.setBackground(attribs, Color.white);
		StyleConstants.setForeground(attribs, Color.blue);
		StyleConstants.setBold(attribs, false);
		StyleConstants.setItalic(attribs, false);
		styles.put("endtag", attribs);

		
		attribs = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attribs, fi.getFamily());
		StyleConstants.setFontSize(attribs, fi.getSize());
		StyleConstants.setBackground(attribs, Color.white);
		StyleConstants.setForeground(attribs, Color.black);
		StyleConstants.setBold(attribs, false);
		StyleConstants.setItalic(attribs, false);
		styles.put("reference", attribs);

		
		attribs = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attribs, fi.getFamily());
		StyleConstants.setFontSize(attribs, fi.getSize());
		StyleConstants.setBackground(attribs, Color.white);
		StyleConstants.setForeground(attribs, new Color(0xB03060));
		StyleConstants.setBold(attribs, true);
		StyleConstants.setItalic(attribs, false);
		styles.put("name", attribs);

		
		attribs = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attribs, fi.getFamily());
		StyleConstants.setFontSize(attribs, fi.getSize());
		StyleConstants.setBackground(attribs, Color.white);
		StyleConstants.setForeground(attribs, new Color(0xB03060));
		StyleConstants.setBold(attribs, false);
		StyleConstants.setItalic(attribs, true);
		styles.put("value", attribs);

		
		attribs = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attribs, fi.getFamily());
		StyleConstants.setFontSize(attribs, fi.getSize());
		StyleConstants.setBackground(attribs, Color.white);
		StyleConstants.setForeground(attribs, Color.black);
		StyleConstants.setBold(attribs, true);
		StyleConstants.setItalic(attribs, false);
		styles.put("text", attribs);

		style = _syntaxPrefs.getColumnStyle();
		attribs = new SimpleAttributeSet();
		applyStyle(attribs, style, fi);
		styles.put(IConstants.IStyleNames.COLUMN, attribs);

		style = _syntaxPrefs.getCommentStyle();
		attribs = new SimpleAttributeSet();
		applyStyle(attribs, style, fi);
		styles.put(IConstants.IStyleNames.COMMENT, attribs);

		style = _syntaxPrefs.getDataTypeStyle();
		attribs = new SimpleAttributeSet();
		applyStyle(attribs, style, fi);
		styles.put(IConstants.IStyleNames.DATA_TYPE, attribs);

		style = _syntaxPrefs.getErrorStyle();
		attribs = new SimpleAttributeSet();
		applyStyle(attribs, style, fi);
		styles.put(IConstants.IStyleNames.ERROR, attribs);

		style = _syntaxPrefs.getFunctionStyle();
		attribs = new SimpleAttributeSet();
		applyStyle(attribs, style, fi);
		styles.put(IConstants.IStyleNames.FUNCTION, attribs);

		style = _syntaxPrefs.getIdentifierStyle();
		attribs = new SimpleAttributeSet();
		applyStyle(attribs, style, fi);
		styles.put(IConstants.IStyleNames.IDENTIFIER, attribs);

		style = _syntaxPrefs.getLiteralStyle();
		attribs = new SimpleAttributeSet();
		applyStyle(attribs, style, fi);
		styles.put(IConstants.IStyleNames.LITERAL, attribs);

		style = _syntaxPrefs.getOperatorStyle();
		attribs = new SimpleAttributeSet();
		applyStyle(attribs, style, fi);
		styles.put(IConstants.IStyleNames.OPERATOR, attribs);

		style = _syntaxPrefs.getReservedWordStyle();
		attribs = new SimpleAttributeSet();
		applyStyle(attribs, style, fi);
		styles.put(IConstants.IStyleNames.RESERVED_WORD, attribs);

		style = _syntaxPrefs.getSeparatorStyle();
		attribs = new SimpleAttributeSet();
		applyStyle(attribs, style, fi);
		styles.put(IConstants.IStyleNames.SEPARATOR, attribs);

		style = _syntaxPrefs.getTableStyle();
		attribs = new SimpleAttributeSet();
		applyStyle(attribs, style, fi);
		styles.put(IConstants.IStyleNames.TABLE, attribs);

		style = _syntaxPrefs.getWhiteSpaceStyle();
		attribs = new SimpleAttributeSet();
		applyStyle(attribs, style, fi);
		styles.put(IConstants.IStyleNames.WHITESPACE, attribs);

		
		attribs = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attribs, fi.getFamily());
		StyleConstants.setFontSize(attribs, fi.getSize());
		StyleConstants.setBackground(attribs, Color.white);
		StyleConstants.setForeground(attribs, new Color(0xA020F0).darker());
		StyleConstants.setBold(attribs, false);
		StyleConstants.setItalic(attribs, false);
		styles.put("preprocessor", attribs);

		
		attribs = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attribs, fi.getFamily());
		StyleConstants.setFontSize(attribs, fi.getSize());
		StyleConstants.setBackground(attribs, Color.white);
		StyleConstants.setForeground(attribs, Color.orange);
		StyleConstants.setBold(attribs, false);
		StyleConstants.setItalic(attribs, false);
		styles.put("unknown", attribs);
	}

	private class Colorer extends Thread
	{

      private boolean _endThread;
      private Vector<int[]> _currentLiteralAndCommentIntervals = 
          new Vector<int[]>();
      private Hashtable<String, String> _knownTables = 
          new Hashtable<String, String>();

      
		private class RecolorEvent
		{
			private int position;
			private int adjustment;
         private boolean fireTableOrViewFoundEvent;
         private String change;

         public RecolorEvent(int position, int adjustment, boolean fireTableOrViewFoundEvent, String change)
			{
				this.position = position;
				this.adjustment = adjustment;
            this.fireTableOrViewFoundEvent = fireTableOrViewFoundEvent;
            this.change = change;
			}
		}

		
		private volatile Vector<RecolorEvent> recolorEventQueue = 
		    new Vector<RecolorEvent>();


		

		private volatile boolean asleep = false;

		
		private Object lock = new Object();

		
		public void color(int position, int adjustment, boolean fireTableOrViewFoundEvent, String change)
		{
			synchronized (lock)
			{
				recolorEventQueue.add(new RecolorEvent(position, adjustment, fireTableOrViewFoundEvent, change));
				if (asleep)
				{
					this.interrupt();
				}
			}
		}

      public void endThread()
      {
         _endThread = true;
         if (asleep)
         {
            this.interrupt();
         }
      }


		
		public void run()
		{
			int colorStartPos = -1;
			int colorLen = 0;
         boolean fireTableOrViewFoundEvent = true;
			
			
			
			boolean tryAgain = false;
			for (;;)
			{ 
				synchronized (lock)
				{
					if (recolorEventQueue.size() > 0)
					{
						RecolorEvent re = recolorEventQueue.elementAt(0);
						recolorEventQueue.removeElementAt(0);

                  if(null != re.change)
                  {
                     
                     
                     
                     adjustIntervalsToAdjustment(re);
                  }

                  colorStartPos = getColorStartPos(re);
                  colorLen = getColorLen(colorStartPos, re);


                  if(null != re.change)
                  {
                     if(   -1 != re.change.indexOf('\'')
                        || -1 != re.change.indexOf('/')
                        || -1 != re.change.indexOf('*')
                        || -1 != re.change.indexOf('-')
                        || null != getInvolvedLiteralOrCommentInterval(re.position))
                     {
                        reinitLiteralAndCommentIntervals();

                        int colorStartPos2 = getColorStartPos(re);
                        int colorLen2 = getColorLen(colorStartPos, re);

                        int newStartPos = Math.min(colorStartPos, colorStartPos2);
                        int newEndPos = Math.max(colorStartPos2 + colorLen2, colorStartPos + colorLen);

                        colorStartPos = newStartPos;
                        colorLen = newEndPos - newStartPos;
                     }
                  }


                  fireTableOrViewFoundEvent = re.fireTableOrViewFoundEvent;
					}
					else
					{
						tryAgain = false;
						colorStartPos = -1;
						colorLen = 0;
                  fireTableOrViewFoundEvent = false;

					}
				}
				if (colorStartPos != -1)
				{
					try
					{
						final SchemaInfo si = _session.getSchemaInfo();
						Token t;
						synchronized (doclock)
						{
							
							
							
							
							
							
							
							syntaxLexer.reset(
								documentReader,
								0,
								colorStartPos,
								0);
							
							
							documentReader.seek(colorStartPos);
							
							
							
							
							t = getNextToken();
						}
                  SimpleAttributeSet errStyle = getMyStyle(IConstants.IStyleNames.ERROR);
                  ErrorInfo[] errInfoClone = _currentErrorInfos.toArray(new ErrorInfo[0]);
						while (t != null && t.getCharEnd() <= colorStartPos + colorLen + 1)
						{
							
							
							
							synchronized (doclock)
							{
								if (t.getCharEnd() <= document.getLength())
								{
									String type = t.getDescription();
									if (type.equals(IConstants.IStyleNames.IDENTIFIER))
									{
										final String data = t.getContents();
										if (si.isTable(data))
										{
											type = IConstants.IStyleNames.TABLE;
                                 if(fireTableOrViewFoundEvent)
                                 {
											   fireTableOrViewFound(t.getContents());
										   }

                                 String upperCaseTableName = data.toUpperCase();
                                 if(false == _knownTables.contains(upperCaseTableName))
                                 {
                                    _knownTables.put(upperCaseTableName, upperCaseTableName);
                                    recolorColumns(upperCaseTableName);
                                 }

										}
										else if (si.isColumn(data))
										{
											type = IConstants.IStyleNames.COLUMN;
										}
										else if (si.isDataType(data))
										{
											type = IConstants.IStyleNames.DATA_TYPE;
										}
										else if (si.isKeyword(data))
										{
											type = IConstants.IStyleNames.RESERVED_WORD;
										}
									}

                           int begin = t.getCharBegin();
                           int len = t.getCharEnd() - t.getCharBegin();

                           SimpleAttributeSet myStyle = null;
                           for (int i = 0; i < errInfoClone.length; i++)
                           {
                              if (    isBetween(errInfoClone[i].beginPos, errInfoClone[i].endPos, begin)
                                   && isBetween(errInfoClone[i].beginPos, errInfoClone[i].endPos, (begin + len - 1)) )
                              {
                                 myStyle = errStyle;
                              }
                           }

                           if(null == myStyle)
                           {
                              myStyle = getMyStyle(type);
                           }

                           setCharacterAttributes(
										begin,
										len,
										myStyle, true);
									
								}
							}
							synchronized (doclock)
							{
								t = getNextToken();
							}
						}

					}
					catch (IOException x)
					{
					}
					
					
					tryAgain = true;
				}

            if(_endThread)
            {
               break;
            }

				asleep = true;
				if (!tryAgain)
				{
					try
					{
						sleep(0xffffff);
					}
					catch (InterruptedException x)
					{
					}

				}
            if(_endThread)
            {
               break;
            }

				asleep = false;
			}
		}

      private void adjustIntervalsToAdjustment(RecolorEvent re)
      {
         for (int i = 0; i < _currentLiteralAndCommentIntervals.size(); i++)
         {
            int[] interval = _currentLiteralAndCommentIntervals.elementAt(i);

            if(re.position < interval[0])
            {
               interval[0] += re.adjustment;
            }
            if(re.position < interval[1])
            {
               interval[1] += re.adjustment;
            }
         }
      }

      private void recolorColumns(String tableName)
      {
         String text = getText().toUpperCase();

         ExtendedColumnInfo[] cols = _session.getSchemaInfo().getExtendedColumnInfos(tableName);

         for (int i = 0; i < cols.length; i++)
         {
            String upperCaseColName = cols[i].getColumnName().toUpperCase();

            int fromIndex = 0;
            for (;;)
            {
               fromIndex = text.indexOf(upperCaseColName, fromIndex);

               if(-1 == fromIndex)
               {
                  break;
               }

               color(fromIndex, upperCaseColName.length(), false, null);
               ++fromIndex;


            }
         }
      }

      private void reinitLiteralAndCommentIntervals()
      {
         try
         {
            _currentLiteralAndCommentIntervals.clear();

            Document doc = getDocument();
            int docLen = doc.getLength();

            int[] curInterval = null;
            boolean inMultiLineComment = false;
            boolean inSinglLineComment = false;
            boolean inLiteral = false;
            for(int i=0; i < docLen; ++i)
            {
               if(i < docLen + 1 && "/*".equals(doc.getText(i,2))
                  && false == inMultiLineComment && false == inSinglLineComment && false == inLiteral)
               {
                  curInterval = new int[2];
                  curInterval[0] = i;
                  curInterval[1] = docLen;
                  inMultiLineComment = true;
                  ++i;
                  continue;
               }

               if(i < docLen + 1 && "--".equals(doc.getText(i,2))
                  && false == inMultiLineComment && false == inSinglLineComment && false == inLiteral)
               {
                  curInterval = new int[2];
                  curInterval[0] = i;
                  curInterval[1] = docLen;
                  inSinglLineComment = true;
                  ++i;
                  continue;
               }

               if('\'' == doc.getText(i,1).charAt(0)
                  && false == inMultiLineComment && false == inSinglLineComment && false == inLiteral)
               {
                  curInterval = new int[2];
                  curInterval[0] = i;
                  curInterval[1] = docLen;
                  inLiteral = true;
                  continue;
               }



               if(i < docLen + 1 && "*/".equals(doc.getText(i,2)) && inMultiLineComment)
               {
                  curInterval[1] = i+1;
                  _currentLiteralAndCommentIntervals.add(curInterval);
                  curInterval = null;
                  inMultiLineComment = false;
                  ++i;
               }

               if('\n' == doc.getText(i,1).charAt(0) && inSinglLineComment)
               {
                  curInterval[1] = i;
                  _currentLiteralAndCommentIntervals.add(curInterval);
                  curInterval = null;
                  inSinglLineComment = false;
               }

               if('\'' == doc.getText(i,1).charAt(0) && inLiteral)
               {
                  if( i < docLen + 1 && '\'' == doc.getText(i+1,1).charAt(0))
                  {
                     ++i;
                  }
                  else
                  {
                     curInterval[1] = i;
                     _currentLiteralAndCommentIntervals.add(curInterval);
                     curInterval = null;
                     inLiteral = false;
                  }

               }
            }

            if(null != curInterval)
            {
               _currentLiteralAndCommentIntervals.add(curInterval);
            }
         }
         catch (BadLocationException e)
         {
            throw new RuntimeException(e);
         }
      }

      private int getColorLen(int begin, RecolorEvent re)
      {
         try
         {
            int reBegin = Math.min(re.position, re.position + re.adjustment);
            reBegin = Math.max(0, reBegin);

            int end = begin + (reBegin - begin) + Math.max(0, re.adjustment);

            int docLen = getDocument().getLength();

            if(end > docLen -1)
            {
               return docLen - begin;
            }

            for(;end < docLen -1; ++end)
            {
               if(Character.isWhitespace(getDocument().getText(end,1).charAt(0)))
               {
                  break;
               }
            }

            int[] interval = getInvolvedLiteralOrCommentInterval(end);

            if(null != interval)
            {
               return Math.max(end-begin, interval[1] - begin);
            }
            else
            {
               return end - begin;
            }
         }
         catch (BadLocationException e)
         {
            throw new RuntimeException(e);
         }
      }

      private int getColorStartPos(RecolorEvent re)
      {
         try
         {
            int startPos = Math.min(re.position, re.position + re.adjustment);

            if(0 > startPos)
            {
               return 0;
            }

            for (; startPos > 0; --startPos)
            {
               if(Character.isWhitespace(getDocument().getText(startPos-1,1).charAt(0)))
               {
                  break;
               }
            }

            int[] interval = getInvolvedLiteralOrCommentInterval(startPos);

            if(null != interval)
            {
               return Math.min(startPos, interval[0]);
            }
            else
            {
               return startPos;
            }



         }
         catch (BadLocationException e)
         {
            throw new RuntimeException(e);
         }
      }

      private int[] getInvolvedLiteralOrCommentInterval(int pos)
      {
         for (int i = 0; i < _currentLiteralAndCommentIntervals.size(); i++)
         {
            int[] interval = _currentLiteralAndCommentIntervals.elementAt(i);

            if(interval[0]-1 <= pos && pos <= interval[1]+1)
            {
               
               
               
               
               

               return interval;
            }
         }
         return null;
      }


      private boolean isBetween(int beg, int end, int p)
      {
         return beg <= p && p <= end;
      }


      private void setCharacterAttributes(final int offset, final int length, final AttributeSet s, final boolean replace)
      {
         SwingUtilities.invokeLater(new Runnable()
         {
            public void run()
            {
               
               
               
               document.setCharacterAttributes(offset, length, s, replace);
            }
         });
      }



	}

	public void addSQLTokenListener(SQLTokenListener l)
	{
		_sqlTokenListeners.add(l);
	}

	public void removeSQLTokenListener(SQLTokenListener l)
	{
		_sqlTokenListeners.remove(l);
	}

	private void fireTableOrViewFound(String name)
	{
		Vector<SQLTokenListener> buf;
		synchronized(_sqlTokenListeners)
		{
			buf = new Vector<SQLTokenListener>(_sqlTokenListeners);
		}

		for(int i=0; i < buf.size(); ++i)
		{
			buf.get(i).tableOrViewFound(name);
		}
	}

   public String getToolTipText(MouseEvent event)
   {
      int pos = viewToModel(event.getPoint());

      for (int i = 0; i < _currentErrorInfos.size(); i++)
      {
         ErrorInfo errInfo = _currentErrorInfos.elementAt(i);

         if(errInfo.beginPos-1 <= pos && pos <= errInfo.endPos)
         {
            return errInfo.message;
         }
      }

      return null;
   }


	
	private class HighLightedDocument extends DefaultStyledDocument
	{
        private static final long serialVersionUID = 1L;

        public HighLightedDocument()
		{
			super();
			putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
		}

		public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException
		{


				super.insertString(offs, str, a);
				color(offs, str.length(), true, str);
				documentReader.update(offs, str.length());

		}

		public void remove(int offs, int len) throws BadLocationException
		{


            String change = getText(offs, len);
				super.remove(offs, len);
				color(offs, -len, true, change);
				documentReader.update(offs, -len);

		}
   }

	class DocumentReader extends Reader
	{

		
		public void update(int position, int adjustment)
		{
			if (position < this.position)
			{
				if (this.position < position - adjustment)
				{
					this.position = position;
				}
				else
				{
					this.position += adjustment;
				}
			}
		}

		
		private long position = 0;

		
		private long mark = -1;

		
		private AbstractDocument document;

		
		public DocumentReader(AbstractDocument document)
		{
			this.document = document;
		}

		
		public void close()
		{
		}

		
		public void mark(int readAheadLimit)
		{
			mark = position;
		}

		
		public boolean markSupported()
		{
			return true;
		}

		
		public int read()
		{
			if (position < document.getLength())
			{
				try
				{
					char c = document.getText((int) position, 1).charAt(0);
					position++;
					return c;
				}
				catch (BadLocationException x)
				{
					return -1;
				}
			}
			else
			{
				return -1;
			}
		}

		
		public int read(char[] cbuf)
		{
			return read(cbuf, 0, cbuf.length);
		}

		
		public int read(char[] cbuf, int off, int len)
		{
			if (position < document.getLength())
			{
				int length = len;
				if (position + length >= document.getLength())
				{
					length = document.getLength() - (int) position;
				}
				if (off + length >= cbuf.length)
				{
					length = cbuf.length - off;
				}
				try
				{
					String s = document.getText((int) position, length);
					position += length;
					for (int i = 0; i < length; i++)
					{
                  
                  
                  
                  
						cbuf[off + i] = (char)((s.charAt(i)) % 256);
					}
					return length;
				}
				catch (BadLocationException x)
				{
					return -1;
				}
			}
			else
			{
				return -1;
			}
		}

		
		public boolean ready()
		{
			return true;
		}

		
		public void reset()
		{
			if (mark == -1)
			{
				position = 0;
			}
			else
			{
				position = mark;
			}
			mark = -1;
		}

		
		public long skip(long n)
		{
			if (position + n <= document.getLength())
			{
				position += n;
				return n;
			}
			else
			{
				long oldPos = position;
				position = document.getLength();
				return (document.getLength() - oldPos);
			}
		}

		
		public void seek(long n)
		{
			if (n <= document.getLength())
			{
				position = n;
			}
			else
			{
				position = document.getLength();
			}
		}
	}

}
