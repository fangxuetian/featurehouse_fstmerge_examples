

package com.lowagie.text.html.simpleparser;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;

import com.lowagie.text.html.HtmlTags;
import com.lowagie.text.html.Markup;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocListener;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ElementTags;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.FontFactoryImp;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.TextElementArray;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.xml.simpleparser.SimpleXMLDocHandler;
import com.lowagie.text.xml.simpleparser.SimpleXMLParser;

public class HTMLWorker implements SimpleXMLDocHandler, DocListener {

    protected ArrayList objectList;

    protected DocListener document;

    private Paragraph currentParagraph;

    private ChainedProperties cprops = new ChainedProperties();

    private Stack stack = new Stack();

    private boolean pendingTR = false;

    private boolean pendingTD = false;

    private boolean pendingLI = false;

    private StyleSheet style = new StyleSheet();

    private boolean isPRE = false;

    private Stack tableState = new Stack();

    private boolean skipText = false;

    private HashMap interfaceProps;

    private FactoryProperties factoryProperties = new FactoryProperties();

    
    public HTMLWorker(DocListener document) {
        this.document = document;
    }

    public void setStyleSheet(StyleSheet style) {
        this.style = style;
    }

    public StyleSheet getStyleSheet() {
        return style;
    }

    public void setInterfaceProps(HashMap interfaceProps) {
        this.interfaceProps = interfaceProps;
        FontFactoryImp ff = null;
        if (interfaceProps != null)
            ff = (FontFactoryImp) interfaceProps.get("font_factory");
        if (ff != null)
            factoryProperties.setFontImp(ff);
    }

    public HashMap getInterfaceProps() {
        return interfaceProps;
    }

    public void parse(Reader reader) throws IOException {
        SimpleXMLParser.parse(this, null, reader, true);
    }

    public static ArrayList parseToList(Reader reader, StyleSheet style)
            throws IOException {
        return parseToList(reader, style, null);
    }

    public static ArrayList parseToList(Reader reader, StyleSheet style,
            HashMap interfaceProps) throws IOException {
        HTMLWorker worker = new HTMLWorker(null);
        if (style != null)
            worker.style = style;
        worker.document = worker;
        worker.setInterfaceProps(interfaceProps);
        worker.objectList = new ArrayList();
        worker.parse(reader);
        return worker.objectList;
    }

    public void endDocument() {
        try {
            for (int k = 0; k < stack.size(); ++k)
                document.add((Element) stack.elementAt(k));
            if (currentParagraph != null)
                document.add(currentParagraph);
            currentParagraph = null;
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public void startDocument() {
        HashMap h = new HashMap();
        style.applyStyle("body", h);
        cprops.addToChain("body", h);
    }

    public void startElement(String tag, HashMap h) {
        if (!tagsSupported.containsKey(tag))
            return;
        try {
            style.applyStyle(tag, h);
            String follow = (String) FactoryProperties.followTags.get(tag);
            if (follow != null) {
                HashMap prop = new HashMap();
                prop.put(follow, null);
                cprops.addToChain(follow, prop);
                return;
            }
            FactoryProperties.insertStyle(h, cprops);
            if (tag.equals(HtmlTags.ANCHOR)) {
                cprops.addToChain(tag, h);
                if (currentParagraph == null) {
                    currentParagraph = new Paragraph();
                }
                stack.push(currentParagraph);
                currentParagraph = new Paragraph();
                return;
            }
            if (tag.equals(HtmlTags.NEWLINE)) {
                if (currentParagraph == null) {
                    currentParagraph = new Paragraph();
                }
                currentParagraph.add(factoryProperties
                        .createChunk("\n", cprops));
                return;
            }
            if (tag.equals(HtmlTags.CHUNK) || tag.equals(HtmlTags.SPAN)) {
                cprops.addToChain(tag, h);
                return;
            }
            if (tag.equals(HtmlTags.IMAGE)) {
                String src = (String) h.get(ElementTags.SRC);
                if (src == null)
                    return;
                cprops.addToChain(tag, h);
                Image img = null;
                if (interfaceProps != null) {
                    ImageProvider ip = (ImageProvider) interfaceProps
                            .get("img_provider");
                    if (ip != null)
                        img = ip.getImage(src, h, cprops, document);
                    if (img == null) {
                        HashMap images = (HashMap) interfaceProps
                                .get("img_static");
                        if (images != null) {
                            Image tim = (Image) images.get(src);
                            if (tim != null)
                                img = Image.getInstance(tim);
                        } else {
                            if (!src.startsWith("http")) { 
                                String baseurl = (String) interfaceProps
                                        .get("img_baseurl");
                                if (baseurl != null) {
                                    src = baseurl + src;
                                    img = Image.getInstance(src);
                                }
                            }
                        }
                    }
                }
                if (img == null) {
                    if (!src.startsWith("http")) {
                        String path = cprops.getProperty("image_path");
                        if (path == null)
                            path = "";
                        src = new File(path, src).getPath();
                    }
                    img = Image.getInstance(src);
                }
                String align = (String) h.get("align");
                String width = (String) h.get("width");
                String height = (String) h.get("height");
                String before = cprops.getProperty("before");
                String after = cprops.getProperty("after");
                if (before != null)
                    img.setSpacingBefore(Float.parseFloat(before));
                if (after != null)
                    img.setSpacingAfter(Float.parseFloat(after));
                float actualFontSize = Markup.parseLength(cprops
                        .getProperty(ElementTags.SIZE),
                        Markup.DEFAULT_FONT_SIZE);
                if (actualFontSize <= 0f)
                    actualFontSize = Markup.DEFAULT_FONT_SIZE;
                float widthInPoints = Markup.parseLength(width, actualFontSize);
                float heightInPoints = Markup.parseLength(height,
                        actualFontSize);
                if (widthInPoints > 0 && heightInPoints > 0) {
                    img.scaleAbsolute(widthInPoints, heightInPoints);
                } else if (widthInPoints > 0) {
                    heightInPoints = img.getHeight() * widthInPoints
                            / img.getWidth();
                    img.scaleAbsolute(widthInPoints, heightInPoints);
                } else if (heightInPoints > 0) {
                    widthInPoints = img.getWidth() * heightInPoints
                            / img.getHeight();
                    img.scaleAbsolute(widthInPoints, heightInPoints);
                }
                img.setWidthPercentage(0);
                if (align != null) {
                    endElement("p");
                    int ralign = Image.MIDDLE;
                    if (align.equalsIgnoreCase("left"))
                        ralign = Image.LEFT;
                    else if (align.equalsIgnoreCase("right"))
                        ralign = Image.RIGHT;
                    img.setAlignment(ralign);
                    Img i = null;
                    boolean skip = false;
                    if (interfaceProps != null) {
                        i = (Img) interfaceProps.get("img_interface");
                        if (i != null)
                            skip = i.process(img, h, cprops, document);
                    }
                    if (!skip)
                        document.add(img);
                    cprops.removeChain(tag);
                } else {
                    cprops.removeChain(tag);
                    if (currentParagraph == null) {
                        currentParagraph = FactoryProperties
                                .createParagraph(cprops);
                    }
                    currentParagraph.add(new Chunk(img, 0, 0));
                }
                return;
            }
            endElement("p");
            if (tag.equals("h1") || tag.equals("h2") || tag.equals("h3")
                    || tag.equals("h4") || tag.equals("h5") || tag.equals("h6")) {
                if (!h.containsKey(ElementTags.SIZE)) {
                    int v = 7 - Integer.parseInt(tag.substring(1));
                    h.put(ElementTags.SIZE, Integer.toString(v));
                }
                cprops.addToChain(tag, h);
                return;
            }
            if (tag.equals(HtmlTags.UNORDEREDLIST)) {
                if (pendingLI)
                    endElement(HtmlTags.LISTITEM);
                skipText = true;
                cprops.addToChain(tag, h);
                com.lowagie.text.List list = new com.lowagie.text.List(false,
                        10);
                list.setListSymbol("\u");
                stack.push(list);
                return;
            }
            if (tag.equals(HtmlTags.ORDEREDLIST)) {
                if (pendingLI)
                    endElement(HtmlTags.LISTITEM);
                skipText = true;
                cprops.addToChain(tag, h);
                com.lowagie.text.List list = new com.lowagie.text.List(true, 10);
                stack.push(list);
                return;
            }
            if (tag.equals(HtmlTags.LISTITEM)) {
                if (pendingLI)
                    endElement(HtmlTags.LISTITEM);
                skipText = false;
                pendingLI = true;
                cprops.addToChain(tag, h);
                ListItem item = FactoryProperties.createListItem(cprops);
                stack.push(item);
                return;
            }
            if (tag.equals(HtmlTags.DIV) || tag.equals(HtmlTags.BODY)) {
                cprops.addToChain(tag, h);
                return;
            }
            if (tag.equals(HtmlTags.PRE)) {
                if (!h.containsKey(ElementTags.FACE)) {
                    h.put(ElementTags.FACE, "Courier");
                }
                cprops.addToChain(tag, h);
                isPRE = true;
                return;
            }
            if (tag.equals("p")) {
                cprops.addToChain(tag, h);
                currentParagraph = FactoryProperties.createParagraph(h);
                return;
            }
            if (tag.equals("tr")) {
                if (pendingTR)
                    endElement("tr");
                skipText = true;
                pendingTR = true;
                cprops.addToChain("tr", h);
                return;
            }
            if (tag.equals("td") || tag.equals("th")) {
                if (pendingTD)
                    endElement(tag);
                skipText = false;
                pendingTD = true;
                cprops.addToChain("td", h);
                stack.push(new IncCell(tag, cprops));
                return;
            }
            if (tag.equals("table")) {
                cprops.addToChain("table", h);
                IncTable table = new IncTable(h);
                stack.push(table);
                tableState.push(new boolean[] { pendingTR, pendingTD });
                pendingTR = pendingTD = false;
                skipText = true;
                return;
            }
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public void endElement(String tag) {
        if (!tagsSupported.containsKey(tag))
            return;
        try {
            String follow = (String) FactoryProperties.followTags.get(tag);
            if (follow != null) {
                cprops.removeChain(follow);
                return;
            }
            if (tag.equals("font") || tag.equals("span")) {
                cprops.removeChain(tag);
                return;
            }
            if (tag.equals("a")) {
                if (currentParagraph == null) {
                    currentParagraph = new Paragraph();
                }
                boolean skip = false;
                if (interfaceProps != null) {
                    ALink i = (ALink) interfaceProps.get("alink_interface");
                    if (i != null)
                        skip = i.process(currentParagraph, cprops);
                }
                if (!skip) {
                    String href = cprops.getProperty("href");
                    if (href != null) {
                        ArrayList chunks = currentParagraph.getChunks();
                        int size = chunks.size();
                        for (int k = 0; k < size; ++k) {
                            Chunk ck = (Chunk) chunks.get(k);
                            ck.setAnchor(href);
                        }
                    }
                }
                Paragraph tmp = (Paragraph) stack.pop();
                Phrase tmp2 = new Phrase();
                tmp2.add(currentParagraph);
                tmp.add(tmp2);
                currentParagraph = tmp;
                cprops.removeChain("a");
                return;
            }
            if (tag.equals("br")) {
                return;
            }
            if (currentParagraph != null) {
                if (stack.empty())
                    document.add(currentParagraph);
                else {
                    Object obj = stack.pop();
                    if (obj instanceof TextElementArray) {
                        TextElementArray current = (TextElementArray) obj;
                        current.add(currentParagraph);
                    }
                    stack.push(obj);
                }
            }
            currentParagraph = null;
            if (tag.equals(HtmlTags.UNORDEREDLIST)
                    || tag.equals(HtmlTags.ORDEREDLIST)) {
                if (pendingLI)
                    endElement(HtmlTags.LISTITEM);
                skipText = false;
                cprops.removeChain(tag);
                if (stack.empty())
                    return;
                Object obj = stack.pop();
                if (!(obj instanceof com.lowagie.text.List)) {
                    stack.push(obj);
                    return;
                }
                if (stack.empty())
                    document.add((Element) obj);
                else
                    ((TextElementArray) stack.peek()).add(obj);
                return;
            }
            if (tag.equals(HtmlTags.LISTITEM)) {
                pendingLI = false;
                skipText = true;
                cprops.removeChain(tag);
                if (stack.empty())
                    return;
                Object obj = stack.pop();
                if (!(obj instanceof ListItem)) {
                    stack.push(obj);
                    return;
                }
                if (stack.empty()) {
                    document.add((Element) obj);
                    return;
                }
                Object list = stack.pop();
                if (!(list instanceof com.lowagie.text.List)) {
                    stack.push(list);
                    return;
                }
                ListItem item = (ListItem) obj;
                ((com.lowagie.text.List) list).add(item);
                ArrayList cks = item.getChunks();
                if (!cks.isEmpty())
                    item.getListSymbol()
                            .setFont(((Chunk) cks.get(0)).getFont());
                stack.push(list);
                return;
            }
            if (tag.equals("div") || tag.equals("body")) {
                cprops.removeChain(tag);
                return;
            }
            if (tag.equals(HtmlTags.PRE)) {
                cprops.removeChain(tag);
                isPRE = false;
                return;
            }
            if (tag.equals("p")) {
                cprops.removeChain(tag);
                return;
            }
            if (tag.equals("h1") || tag.equals("h2") || tag.equals("h3")
                    || tag.equals("h4") || tag.equals("h5") || tag.equals("h6")) {
                cprops.removeChain(tag);
                return;
            }
            if (tag.equals("table")) {
                if (pendingTR)
                    endElement("tr");
                cprops.removeChain("table");
                IncTable table = (IncTable) stack.pop();
                PdfPTable tb = table.buildTable();
                tb.setSplitRows(true);
                if (stack.empty())
                    document.add(tb);
                else
                    ((TextElementArray) stack.peek()).add(tb);
                boolean state[] = (boolean[]) tableState.pop();
                pendingTR = state[0];
                pendingTD = state[1];
                skipText = false;
                return;
            }
            if (tag.equals("tr")) {
                if (pendingTD)
                    endElement("td");
                pendingTR = false;
                cprops.removeChain("tr");
                ArrayList cells = new ArrayList();
                IncTable table = null;
                while (true) {
                    Object obj = stack.pop();
                    if (obj instanceof IncCell) {
                        cells.add(((IncCell) obj).getCell());
                    }
                    if (obj instanceof IncTable) {
                        table = (IncTable) obj;
                        break;
                    }
                }
                table.addCols(cells);
                table.endRow();
                stack.push(table);
                skipText = true;
                return;
            }
            if (tag.equals("td") || tag.equals("th")) {
                pendingTD = false;
                cprops.removeChain("td");
                skipText = true;
                return;
            }
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public void text(String str) {
        if (skipText)
            return;
        String content = str;
        if (isPRE) {
            if (currentParagraph == null) {
                currentParagraph = FactoryProperties.createParagraph(cprops);
            }
            Chunk chunk = factoryProperties.createChunk(content, cprops);
            currentParagraph.add(chunk);
            return;
        }
        if (content.trim().length() == 0 && content.indexOf(' ') < 0) {
            return;
        }

        StringBuffer buf = new StringBuffer();
        int len = content.length();
        char character;
        boolean newline = false;
        for (int i = 0; i < len; i++) {
            switch (character = content.charAt(i)) {
            case ' ':
                if (!newline) {
                    buf.append(character);
                }
                break;
            case '\n':
                if (i > 0) {
                    newline = true;
                    buf.append(' ');
                }
                break;
            case '\r':
                break;
            case '\t':
                break;
            default:
                newline = false;
                buf.append(character);
            }
        }
        if (currentParagraph == null) {
            currentParagraph = FactoryProperties.createParagraph(cprops);
        }
        Chunk chunk = factoryProperties.createChunk(buf.toString(), cprops);
        currentParagraph.add(chunk);
    }

    public boolean add(Element element) throws DocumentException {
        objectList.add(element);
        return true;
    }

    public void clearTextWrap() throws DocumentException {
    }

    public void close() {
    }

    public boolean newPage() {
        return true;
    }

    public void open() {
    }

    public void resetFooter() {
    }

    public void resetHeader() {
    }

    public void resetPageCount() {
    }

    public void setFooter(HeaderFooter footer) {
    }

    public void setHeader(HeaderFooter header) {
    }

    public boolean setMarginMirroring(boolean marginMirroring) {
        return true;
    }

    public boolean setMargins(float marginLeft, float marginRight,
            float marginTop, float marginBottom) {
        return true;
    }

    public void setPageCount(int pageN) {
    }

    public boolean setPageSize(Rectangle pageSize) {
        return true;
    }

    public static final String tagsSupportedString = "ol ul li a pre font span br p div body table td th tr i b u sub sup em strong s strike"
            + " h1 h2 h3 h4 h5 h6 img";

    public static final HashMap tagsSupported = new HashMap();

    static {
        StringTokenizer tok = new StringTokenizer(tagsSupportedString);
        while (tok.hasMoreTokens())
            tagsSupported.put(tok.nextToken(), null);
    }

    
    private static float lengthParse(String txt, int c) {
        if (txt == null)
            return -1;
        if (txt.endsWith("%")) {
            float vf = Float.parseFloat(txt.substring(0, txt.length() - 1));
            return vf;
        }
        if (txt.endsWith("px")) {
            float vf = Float.parseFloat(txt.substring(0, txt.length() - 2));
            return vf;
        }
        int v = Integer.parseInt(txt);
        return (float) v / c * 100f;
    }
}
