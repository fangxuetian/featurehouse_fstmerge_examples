








package net.sf.jabref.wizard.integrity ;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.Globals;

public class IntegrityMessage implements Cloneable
{
  
  public static final int
      GENERIC_HINT             = 1,
      UPPER_AND_LOWER_HINT     = 10,
      FOUR_DIGITS_HINT         = 11

      ;

  
  public static final int
      GENERIC_WARNING                = 1001,
      NAME_START_WARNING             = 1010,
      NAME_END_WARNING               = 1011,
      NAME_SEMANTIC_WARNING          = 1012
      ;

  
  public static final int
      UNKNONW_FAILURE                    = 2001,
      UNEXPECTED_CLOSING_BRACE_FAILURE   = 2010
      ;

  public static int
      FULL_MODE    = 1,  
      SINLGE_MODE  = 2   
      ;

  private static int printMode = SINLGE_MODE ;

  private int type ;
  private BibtexEntry entry ;
  private String fieldName ;
  private Object additionalInfo ;
  private String msg ;
  private boolean fixed ; 

  public final synchronized static void setPrintMode(int newMode)
  {
    printMode = newMode ;
  }


  public IntegrityMessage(int pType, BibtexEntry pEntry, String pFieldName, Object pAdditionalInfo)
  {
    this.type = pType;
    this.entry = pEntry;
    this.fieldName = pFieldName;
    this.additionalInfo = pAdditionalInfo;
    fixed = false ;

    msg = getMessage() ;
  }

  public String getMessage()
  {
    String back = Globals.getIntegrityMessage("ITEXT_"+type) ;
    if ((back != null) && (fieldName != null))
    {
      back = back.replaceAll( "\\$FIELD", fieldName ) ;
    }
    return back ;
  }

  public String toString()
  {
    String back = msg ;
    if (printMode == FULL_MODE)
    {
      back = "[" + entry.getCiteKey() + "] " + msg ;
    }
    return back ;
  }

  public int getType()
  {
    return type;
  }

  public BibtexEntry getEntry()
  {
    return entry;
  }

  public String getFieldName()
  {
    return fieldName;
  }

  public Object getAdditionalInfo()
  {
    return additionalInfo;
  }

  public boolean getFixed()
  {
    return fixed;
  }

  public void setFixed(boolean pFixed)
  {
    this.fixed = pFixed;
  }
}
