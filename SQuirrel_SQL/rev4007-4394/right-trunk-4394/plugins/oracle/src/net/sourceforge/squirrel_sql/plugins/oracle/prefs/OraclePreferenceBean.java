package net.sourceforge.squirrel_sql.plugins.oracle.prefs;


import java.io.Serializable;

import net.sourceforge.squirrel_sql.fw.preferences.IQueryTokenizerPreferenceBean;


public class OraclePreferenceBean implements Cloneable, 
                                             Serializable,
                                             IQueryTokenizerPreferenceBean 
{

    static final long serialVersionUID = 5818886723165356478L;

    static final String UNSUPPORTED = "Unsupported";

    
    private String _clientName;

    
    private String _clientVersion;

    private String statementSeparator = ";";

    private String procedureSeparator = "/";

    private String lineComment = "--";

    private boolean removeMultiLineComments = false;

    private boolean installCustomQueryTokenizer = true;

    private boolean excludeRecycleBinTables = true;
    
    private boolean showErrorOffset = true;
    
    public OraclePreferenceBean() {
        super();
    }

    
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex.getMessage()); 
        }
    }

    
    public String getClientName() {
        return _clientName;
    }

    
    public void setClientName(String value) {
        _clientName = value;
    }

    
    public String getClientVersion() {
        return _clientVersion;
    }

    
    public void setClientVersion(String value) {
        _clientVersion = value;
    }

    
    public void setStatementSeparator(String statementSeparator) {
        this.statementSeparator = statementSeparator;
    }

    
    public String getStatementSeparator() {
        return statementSeparator;
    }

    
    public void setProcedureSeparator(String procedureSeparator) {
        this.procedureSeparator = procedureSeparator;
    }

    
    public String getProcedureSeparator() {
        return procedureSeparator;
    }

    
    public void setLineComment(String lineComment) {
        this.lineComment = lineComment;
    }

    
    public String getLineComment() {
        return lineComment;
    }

    
    public void setRemoveMultiLineComments(boolean removeMultiLineComments) {
        this.removeMultiLineComments = removeMultiLineComments;
    }

    
    public boolean isRemoveMultiLineComments() {
        return removeMultiLineComments;
    }

    
    public void setInstallCustomQueryTokenizer(
            boolean installCustomQueryTokenizer) {
        this.installCustomQueryTokenizer = installCustomQueryTokenizer;
    }

    
    public boolean isInstallCustomQueryTokenizer() {
        return installCustomQueryTokenizer;
    }

    
    public void setExcludeRecycleBinTables(boolean excludeRecycleBinTables) {
        this.excludeRecycleBinTables = excludeRecycleBinTables;
    }

    
    public boolean isExcludeRecycleBinTables() {
        return excludeRecycleBinTables;
    }

   
   public boolean isShowErrorOffset() {
      return showErrorOffset;
   }

   
   public void setShowErrorOffset(boolean showErrorOffset) {
      this.showErrorOffset = showErrorOffset;
   }

}
