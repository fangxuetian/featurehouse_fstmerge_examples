









package net.sf.jabref ;

import java.awt.* ;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.net.*;
import java.awt.datatransfer.*;
import net.sf.jabref.util.*;

public class FieldTextMenu implements MouseListener
{
  private FieldEditor myFieldName ;
  private JPopupMenu inputMenu = new JPopupMenu() ;
  private CopyAction copyAct = new CopyAction() ;
  private PasteAction pasteAct = new PasteAction() ;

  public FieldTextMenu(FieldEditor fieldComponent)
  {
    myFieldName = fieldComponent ;

    
    inputMenu.add( pasteAct ) ;
    inputMenu.add( copyAct ) ;
    inputMenu.addSeparator();
    inputMenu.add(new ReplaceAction());
    if (myFieldName.getTextComponent() instanceof JTextComponent)
        inputMenu.add(new CaseChangeMenu((JTextComponent) myFieldName.getTextComponent()));
  }

  public void mouseClicked(MouseEvent e)
  {
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseExited(MouseEvent e)
  {
  }

  public void mousePressed(MouseEvent e)
  {
    maybeShowPopup( e ) ;
  }

  public void mouseReleased(MouseEvent e)
  {
    maybeShowPopup( e ) ;
  }

  private void maybeShowPopup( MouseEvent e )
  {
    if ( e.isPopupTrigger() )
    {
      if (myFieldName != null)
      {
          myFieldName.requestFocus();

        
        String txt = myFieldName.getSelectedText() ;
        boolean cStat = false ;
        if (txt != null)
          if (txt.length() > 0)
            cStat = true ;

        copyAct.setEnabled(cStat);
        inputMenu.show( e.getComponent(), e.getX(), e.getY() ) ;
      }
    }
  }



  abstract class BasicAction extends AbstractAction
  {
    public BasicAction(String text, String description, URL icon)
    {
      super(Globals.lang(text), new ImageIcon(icon));
      putValue(SHORT_DESCRIPTION, Globals.lang(description));
    }

    public BasicAction(String text, String description, URL icon, KeyStroke key)
    {
      super(Globals.lang(text), new ImageIcon(icon));
      putValue(ACCELERATOR_KEY, key);
      putValue(SHORT_DESCRIPTION, Globals.lang(description));
    }

    public BasicAction(String text)
    {
      super(Globals.lang(text));
    }

    public BasicAction(String text, KeyStroke key)
    {
      super(Globals.lang(text));
      putValue(ACCELERATOR_KEY, key);
    }

    public abstract void actionPerformed(ActionEvent e) ;
  }

  


  class PasteAction extends BasicAction
  {
    public PasteAction()
    {
      super("Paste from clipboard", "Paste from clipboard",
              GUIGlobals.getIconUrl("paste"));
    }

    public void actionPerformed(ActionEvent e)
    {
      Clipboard systemClip = Toolkit.getDefaultToolkit().getSystemClipboard();

      try
      {


        String data = ClipBoardManager.clipBoard.getClipboardContents() ;
        if (data != null)
          if (data.length() > 0)
            if (myFieldName != null)
              myFieldName.paste(data);
      }
      catch (Exception ex) {}
    }
  }

  class CopyAction extends BasicAction
  {
    public CopyAction()
    {
      super("Copy to clipboard", "Copy to clipboard", GUIGlobals.getIconUrl("copy"));
    }

    public void actionPerformed(ActionEvent e)
    {
      try
      {


        if (myFieldName != null)
        {
          String data = myFieldName.getSelectedText() ;
          if (data != null)
            if (data.length() > 0)
              ClipBoardManager.clipBoard.setClipboardContents(data);
        }
      }
      catch (Exception ex) {}
    }
  }

  class ReplaceAction extends BasicAction{
    public ReplaceAction(){
        super("Replace comma by and where appropriate");
    }
    public void actionPerformed(ActionEvent evt){
        if (myFieldName.getText().equals("")){
            return;
        }
        
        String input = myFieldName.getText();
        
        myFieldName.setText(generalFixAuthor(input));
    }
  }

 public static String generalFixAuthor(String in){
        String author;
        String[] authors = in.split("( |,)and ",-1);
        for (int i = 0; i < authors.length; i++){
            authors[i].trim();
        }
        
        author = authors[authors.length-1];
        boolean lnfn = (author.indexOf(",") > 0);
        StringBuffer sb = new StringBuffer();
        
        if(lnfn){
            String[] parts;
            for (int i = 0; i < authors.length; i++){
                parts = authors[i].split(",",-1);
                if(parts.length == 2){
                    parts[0] = parts[0].trim().replaceAll(" ","~");
                    parts[1] = parts[1].trim().replaceAll(" ","~");
                    sb.append(parts[1]+" "+ parts[0]);
                } else {
                    sb.append(authors[i]);
                }
                if(i < authors.length -1){
                    sb.append(" and ");
                }
            }
        } else {
            for (int i = 0; i < authors.length; i++){
                String[] iAuthors = authors[i].split(",");
                String[] ijparts;
                for (int j=0; j<iAuthors.length; j++){
                    iAuthors[j] = iAuthors[j].trim();
                    ijparts = iAuthors[j].split(" ",-1);
                    for (int k=0; k<ijparts.length; k++){
                        sb.append(ijparts[k]);
                        if(k < ijparts.length-2){
                            sb.append('~');
                        } else if (k == ijparts.length-2){
                            sb.append(' ');
                        }
                    }
                    if (j < iAuthors.length -1 || i < authors.length -1){
                        sb.append(" and ");
                    }
                } 
            } 
        }
        return sb.toString();
    }

}
