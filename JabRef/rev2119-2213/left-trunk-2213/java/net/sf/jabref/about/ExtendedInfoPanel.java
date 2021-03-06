












package net.sf.jabref.about ;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import net.sf.jabref.GUIGlobals;
import net.sf.jabref.Globals;

public class ExtendedInfoPanel extends JPanel implements AnimationListener, ActionListener
{
  private JEditorPane textPane ;
  private JScrollPane scroller ;
  private CardLayout cards ;

  private JButton close ;
  private JButton license ;

  private AboutPanel about ;
  private JPanel infoPanel ;

  private boolean animationIsRunning = true ;

  private ActionListener mainListener ;

  public ExtendedInfoPanel(ActionListener mainFrame)
  {
    mainListener = mainFrame ;

    
    about = new AboutPanel();
    about.addAnimationListener(this);

    
    textPane = new JEditorPane() ;

    textPane.setEditable( false ) ;

    
    URL helpURL = getClass().getResource( GUIGlobals.getLocaleHelpPath() + GUIGlobals.aboutPage) ;
    
    if (helpURL == null)
    {
      helpURL = getClass().getResource( GUIGlobals.helpPre + GUIGlobals.aboutPage) ;
    }

    if ( helpURL != null )
    {
      try
      {
        textPane.setPage( helpURL ) ;
      }
      catch ( IOException e )
      {
        System.err.println( "Attempted to read a bad URL: " + helpURL ) ;
      }
    }
    else
    {
      System.err.println( "Couldn't find file: About.html" ) ;
    }

    scroller = new JScrollPane(textPane) ; 
    scroller.setPreferredSize( about.getSize());

    
    infoPanel = new JPanel() ;
    cards = new CardLayout() ;
    infoPanel.setLayout( cards);

    infoPanel.add(about, "ani") ;
    infoPanel.add(scroller, "inf") ;

     
    JPanel buttonPanel = new JPanel() ;
    buttonPanel.setBackground( Color.white);
    buttonPanel.setLayout( new GridLayout(1, 2, 10, 20) );
    buttonPanel.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED));

    buttonPanel.add( Box.createGlue() ) ;
    close = new JButton( Globals.lang("Skip") ) ;
    close.addActionListener( this ) ;
    close.setActionCommand( "close" ) ;
    close.setFocusable( false ) ;

    license = new JButton( Globals.lang("License") ) ;
    license.addActionListener( this ) ;
    license.setActionCommand( "license" ) ;
    license.setFocusable( false ) ;

    buttonPanel.add( close ) ;

    buttonPanel.add( Box.createGlue() ) ;


    
    this.setLayout( new BorderLayout(0, 0));

    this.add(infoPanel, BorderLayout.CENTER) ;
    this.add(buttonPanel, BorderLayout.SOUTH) ;


  }

  public void animationReady()
  {
    animationIsRunning = false ;
    cards.show(infoPanel, "inf");
    close.setText(Globals.lang("Close"));
  }

  public void actionPerformed( ActionEvent e )
  {
    String cmd = e.getActionCommand() ;
    if ( cmd.equals( "close" ) )
    {
      if (animationIsRunning)
      {
         about.skipAnimation(); 
      }
      else
      {
        setVisible( false ) ;
        mainListener.actionPerformed(e);
      }
    }
    else if ( cmd.equals( "license" ) )
    {

      mainListener.actionPerformed(e);
    }
  }

}
