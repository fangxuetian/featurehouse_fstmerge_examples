

package com.lowagie.text.rtf.document;

import java.io.IOException;
import java.io.OutputStream;

import com.lowagie.text.DocWriter;
import com.lowagie.text.rtf.RtfElement;



public class RtfProtectionSetting extends RtfElement {
    
    private static final byte[] FORMPROT = DocWriter.getISOBytes("\\formprot");
    
    private static final byte[] REVPROT = DocWriter.getISOBytes("\\revprot");
    
    private static final byte[] ANNOTPROT = DocWriter.getISOBytes("\\annotprot");
    
    private static final byte[] READPROT = DocWriter.getISOBytes("\\readprot");
    
    
    private static final byte[] PROTLEVEL = DocWriter.getISOBytes("\\protlevel");
    
    private static final byte[] ENFORCEPROT = DocWriter.getISOBytes("\\enforceprot");
    
    
    private static final byte[] READONLYRECOMMENDED = DocWriter.getISOBytes("\\readonlyrecommended");

    
    public RtfProtectionSetting(RtfDocument doc) {
        super(doc);
    }

        
    public void writeContent(final OutputStream result) throws IOException
    {
    }
    
        
    public void writeDefinition(final OutputStream result) throws IOException
    {
        if(document.getDocumentSettings().isDocumentProtected()) {
            switch(document.getDocumentSettings().getProtectionLevelRaw()) {
            case RtfProtection.LEVEL_FORMPROT:
                result.write(FORMPROT);
                break;
            case RtfProtection.LEVEL_ANNOTPROT:
                result.write(ANNOTPROT);
                break;
            case RtfProtection.LEVEL_REVPROT:
                result.write(REVPROT);
                break;
            case RtfProtection.LEVEL_READPROT:
                result.write(ANNOTPROT);
                result.write(READPROT);
                break;
            }
            result.write(ENFORCEPROT);    
            result.write((byte)'1');
            result.write(PROTLEVEL);
            result.write(document.getDocumentSettings().getProtectionLevelBytes());
        }
        
        if(document.getDocumentSettings().getReadOnlyRecommended()) {
            result.write(READONLYRECOMMENDED);
            result.write(DELIMITER);
        }
    }
}
