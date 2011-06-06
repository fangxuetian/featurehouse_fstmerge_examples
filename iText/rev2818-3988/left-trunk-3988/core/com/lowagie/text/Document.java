

package com.lowagie.text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;



public class Document implements DocListener {
    
    
    
    private static final String ITEXT = "iText";
    
    private static final String RELEASE = "5.0.0_SNAPSHOT";
    
    private static final String ITEXT_VERSION = ITEXT + " " + RELEASE + " by 1T3XT";
    
    
    public static boolean compress = true; 
    
    
    public static boolean plainRandomAccess = false; 
 
    
    public static float wmfFontCorrection = 0.86f;
    
    
    private ArrayList listeners = new ArrayList();
    
    
    protected boolean open;
    
    
    protected boolean close;
    
    
    
    
    protected Rectangle pageSize;
    
    
    protected float marginLeft = 0;
    
    
    protected float marginRight = 0;
    
    
    protected float marginTop = 0;
    
    
    protected float marginBottom = 0;
    
    
    protected boolean marginMirroring = false;
    
    
    protected boolean marginMirroringTopBottom = false;
    
    
    protected String javaScript_onLoad = null;

    
    protected String javaScript_onUnLoad = null;

    
    protected String htmlStyleClass = null;

    
    
    
    protected int pageN = 0;
    
    
    protected HeaderFooter header = null;
    
    
    protected HeaderFooter footer = null;
    
    
    protected int chapternumber = 0;
    
    
    
    
    
    public Document() {
        this(PageSize.A4);
    }
    
    
    
    public Document(Rectangle pageSize) {
        this(pageSize, 36, 36, 36, 36);
    }
    
    
    
    public Document(Rectangle pageSize, float marginLeft, float marginRight,
            float marginTop, float marginBottom) {
        this.pageSize = pageSize;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
    }
    
    
    
    
    
    public void addDocListener(DocListener listener) {
        listeners.add(listener);
    }
    
    
    
    public void removeDocListener(DocListener listener) {
        listeners.remove(listener);
    }
    
    
    
    
    
    public boolean add(Element element) throws DocumentException {
        if (close) {
            throw new DocumentException(
                "The document has been closed. You can't add any Elements.");
        }
        if (!open && element.isContent()) {
            throw new DocumentException(
                "The document is not open yet; you can only add Meta information.");
        }
        boolean success = false;
        DocListener listener;
        if (element instanceof ChapterAutoNumber) {
            chapternumber = ((ChapterAutoNumber)element).setAutomaticNumber(chapternumber);
        }
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            success |= listener.add(element);
        }
        if (element instanceof LargeElement) {
            LargeElement e = (LargeElement)element;
            if (!e.isComplete())
                e.flushContent();
        }
        return success;
    }
    
    
    
    public void open() {
        if (!close) {
            open = true;
        }
        DocListener listener;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.setPageSize(pageSize);
            listener.setMargins(marginLeft, marginRight, marginTop,
                    marginBottom);
            listener.open();
        }
    }
    
    
    
    public boolean setPageSize(Rectangle pageSize) {
        this.pageSize = pageSize;
        DocListener listener;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.setPageSize(pageSize);
        }
        return true;
    }
    
    
    
    public boolean setMargins(float marginLeft, float marginRight,
            float marginTop, float marginBottom) {
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
        DocListener listener;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.setMargins(marginLeft, marginRight, marginTop,
                    marginBottom);
        }
        return true;
    }
    
    
    
    public boolean newPage() {
        if (!open || close) {
            return false;
        }
        DocListener listener;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.newPage();
        }
        return true;
    }
    
    
    
    public void setHeader(HeaderFooter header) {
        this.header = header;
        DocListener listener;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.setHeader(header);
        }
    }
    
    
    
    public void resetHeader() {
        this.header = null;
        DocListener listener;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.resetHeader();
        }
    }
    
    
    
    public void setFooter(HeaderFooter footer) {
        this.footer = footer;
        DocListener listener;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.setFooter(footer);
        }
    }
    
    
    
    public void resetFooter() {
        this.footer = null;
        DocListener listener;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.resetFooter();
        }
    }
    
    
    
    public void resetPageCount() {
        pageN = 0;
        DocListener listener;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.resetPageCount();
        }
    }
    
    
    
    public void setPageCount(int pageN) {
        this.pageN = pageN;
        DocListener listener;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.setPageCount(pageN);
        }
    }
    
    
    
    public int getPageNumber() {
        return this.pageN;
    }
    
    
    
    public void close() {
        if (!close) {
            open = false;
            close = true;
        }
        DocListener listener;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.close();
        }
    }
    
    
    
    
    
    public boolean addHeader(String name, String content) {
        try {
            return add(new Header(name, content));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
    
    
    public boolean addTitle(String title) {
        try {
            return add(new Meta(Element.TITLE, title));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
    
    
    public boolean addSubject(String subject) {
        try {
            return add(new Meta(Element.SUBJECT, subject));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
    
    
    public boolean addKeywords(String keywords) {
        try {
            return add(new Meta(Element.KEYWORDS, keywords));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
    
    
    public boolean addAuthor(String author) {
        try {
            return add(new Meta(Element.AUTHOR, author));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
    
    
    public boolean addCreator(String creator) {
        try {
            return add(new Meta(Element.CREATOR, creator));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
    
    
    public boolean addProducer() {
        try {
            return add(new Meta(Element.PRODUCER, "iText by lowagie.com"));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
    
    
    public boolean addCreationDate() {
        try {
            
            final SimpleDateFormat sdf = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss zzz yyyy");
            return add(new Meta(Element.CREATIONDATE, sdf.format(new Date())));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
    
    
    
    
    public float leftMargin() {
        return marginLeft;
    }
    
    
    
    public float rightMargin() {
        return marginRight;
    }
    
    
    
    public float topMargin() {
        return marginTop;
    }
    
    
    
    public float bottomMargin() {
        return marginBottom;
    }
    
    
    
    public float left() {
        return pageSize.getLeft(marginLeft);
    }
    
    
    
    public float right() {
        return pageSize.getRight(marginRight);
    }
    
    
    
    public float top() {
        return pageSize.getTop(marginTop);
    }
    
    
    
    public float bottom() {
        return pageSize.getBottom(marginBottom);
    }
    
    
    
    public float left(float margin) {
        return pageSize.getLeft(marginLeft + margin);
    }
    
    
    
    public float right(float margin) {
        return pageSize.getRight(marginRight + margin);
    }
    
    
    
    public float top(float margin) {
        return pageSize.getTop(marginTop + margin);
    }
    
    
    
    public float bottom(float margin) {
        return pageSize.getBottom(marginBottom + margin);
    }
    
    
    
    public Rectangle getPageSize() {
        return this.pageSize;
    }
    
        
    public boolean isOpen() {
        return open;
    }
    
        
    public static final String getProduct() {
        return ITEXT;
    }
    
        
    public static final String getRelease() {
        return RELEASE;
    }
    
        
    public static final String getVersion() {
        return ITEXT_VERSION;
    }

    
    
    public void setJavaScript_onLoad(String code) {
        this.javaScript_onLoad = code;
    }

    

    public String getJavaScript_onLoad() {
        return this.javaScript_onLoad;
    }

    
    
    public void setJavaScript_onUnLoad(String code) {
        this.javaScript_onUnLoad = code;
    }

    

    public String getJavaScript_onUnLoad() {
        return this.javaScript_onUnLoad;
    }

    
    
    public void setHtmlStyleClass(String htmlStyleClass) {
        this.htmlStyleClass = htmlStyleClass;
    }

    
    
    public String getHtmlStyleClass() {
        return this.htmlStyleClass;
    }
    
        
    public boolean setMarginMirroring(boolean marginMirroring) {
        this.marginMirroring = marginMirroring;
        DocListener listener;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.setMarginMirroring(marginMirroring);
        }
        return true;
    }
    
        
    public boolean setMarginMirroringTopBottom(boolean marginMirroringTopBottom) {
        this.marginMirroringTopBottom = marginMirroringTopBottom;
        DocListener listener;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.setMarginMirroringTopBottom(marginMirroringTopBottom);
        }
        return true;
    }
    
        
    public boolean isMarginMirroring() {
        return marginMirroring;
    }
}
