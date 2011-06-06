

package com.lowagie.text.rtf.list;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.lowagie.text.ListItem;
import com.lowagie.text.rtf.RtfBasicElement;
import com.lowagie.text.rtf.document.RtfDocument;
import com.lowagie.text.rtf.style.RtfParagraphStyle;
import com.lowagie.text.rtf.text.RtfChunk;
import com.lowagie.text.rtf.text.RtfParagraph;



public class RtfListItem extends RtfParagraph {

    
    private RtfList parentList = null;
    
    private boolean containsInnerList = false;
    
    
    public RtfListItem(RtfDocument doc, ListItem listItem) {
        super(doc, listItem);
    }
    
    
    public byte[] write() 
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            writeContent(result);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }
        
    public void writeContent(final OutputStream result) throws IOException
    {
        if(this.paragraphStyle.getSpacingBefore() > 0) {
            result.write(RtfParagraphStyle.SPACING_BEFORE);
            result.write(intToByteArray(paragraphStyle.getSpacingBefore()));
        }
        if(this.paragraphStyle.getSpacingAfter() > 0) {
            result.write(RtfParagraphStyle.SPACING_AFTER);
            result.write(intToByteArray(this.paragraphStyle.getSpacingAfter()));
        }
        for(RtfBasicElement rtfElement: chunks) {
            if(rtfElement instanceof RtfChunk) {
                ((RtfChunk) rtfElement).setSoftLineBreaks(true);
            } else if(rtfElement instanceof RtfList) {
                result.write(RtfParagraph.PARAGRAPH);
                this.containsInnerList = true;
            }
            
            rtfElement.writeContent(result);
            if(rtfElement instanceof RtfList) {
                result.write(this.parentList.writeListBeginning());
                result.write("\\tab".getBytes());
            }
        }
    }        

    
    public byte[] writeDefinition() {
        for(int i = 0; i < chunks.size(); i++) {
            RtfBasicElement rtfElement = chunks.get(i);
            if(rtfElement instanceof RtfList) {
                return ((RtfList) rtfElement).writeDefinition();
            }
        }
        return new byte[0];
    }
    
    public boolean writeDefinition(OutputStream out) throws IOException
    {
        for(RtfBasicElement rtfElement: chunks) {
            if(rtfElement instanceof RtfList) {
                RtfList rl = (RtfList)rtfElement;
                rl.writeDefinition(out);
                return(true);
            }
        }
        return(false);        
    }
    
    
    public void inheritListSettings(int listNumber, int listLevel) {
        for(RtfBasicElement rtfElement: chunks) {
            if(rtfElement instanceof RtfList) {
                ((RtfList) rtfElement).setListNumber(listNumber);
                ((RtfList) rtfElement).setListLevel(listLevel);
                ((RtfList) rtfElement).setParent(this.parentList);
            }
        }
    }
        
    
    protected void correctIndentation() {
        for(RtfBasicElement rtfElement: chunks) {
            if(rtfElement instanceof RtfList) {
                ((RtfList) rtfElement).correctIndentation();
            }
        }
    }
    
    
    public void setParent(RtfList parentList) {
        this.parentList = parentList;
    }

    
    public boolean isContainsInnerList() {
        return this.containsInnerList;
    }
}
