

package com.lowagie.text.html.simpleparser;

import java.awt.Color;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.ElementTags;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.FontFactoryImp;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.html.HtmlTags;
import com.lowagie.text.html.Markup;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.HyphenationAuto;
import com.lowagie.text.pdf.HyphenationEvent;


public class FactoryProperties {

    private FontFactoryImp fontImp = FontFactory.getFontImp();

    
    public FactoryProperties() {
    }

    public Chunk createChunk(String text, ChainedProperties props) {
        Font font = getFont(props);
        float size = font.getSize();
        size /= 2;
        Chunk ck = new Chunk(text, font);
        if (props.hasProperty("sub"))
            ck.setTextRise(-size);
        else if (props.hasProperty("sup"))
            ck.setTextRise(size);
        ck.setHyphenation(getHyphenation(props));
        return ck;
    }

    private static void setParagraphLeading(Paragraph p, String leading) {
        if (leading == null) {
            p.setLeading(0, 1.5f);
            return;
        }
        try {
            StringTokenizer tk = new StringTokenizer(leading, " ,");
            String v = tk.nextToken();
            float v1 = Float.parseFloat(v);
            if (!tk.hasMoreTokens()) {
                p.setLeading(v1, 0);
                return;
            }
            v = tk.nextToken();
            float v2 = Float.parseFloat(v);
            p.setLeading(v1, v2);
        } catch (Exception e) {
            p.setLeading(0, 1.5f);
        }
    }

    public static Paragraph createParagraph(HashMap<String, String> props) {
        Paragraph p = new Paragraph();
        String value = props.get("align");
        if (value != null) {
            if (value.equalsIgnoreCase("center"))
                p.setAlignment(Element.ALIGN_CENTER);
            else if (value.equalsIgnoreCase("right"))
                p.setAlignment(Element.ALIGN_RIGHT);
            else if (value.equalsIgnoreCase("justify"))
                p.setAlignment(Element.ALIGN_JUSTIFIED);
        }
        p.setHyphenation(getHyphenation(props));
        setParagraphLeading(p, props.get("leading"));
        return p;
    }

    public static void createParagraph(Paragraph p, ChainedProperties props) {
        String value = props.getProperty("align");
        if (value != null) {
            if (value.equalsIgnoreCase("center"))
                p.setAlignment(Element.ALIGN_CENTER);
            else if (value.equalsIgnoreCase("right"))
                p.setAlignment(Element.ALIGN_RIGHT);
            else if (value.equalsIgnoreCase("justify"))
                p.setAlignment(Element.ALIGN_JUSTIFIED);
        }
        p.setHyphenation(getHyphenation(props));
        setParagraphLeading(p, props.getProperty("leading"));
        value = props.getProperty("before");
        if (value != null) {
            try {
                p.setSpacingBefore(Float.parseFloat(value));
            } catch (Exception e) {
            }
        }
        value = props.getProperty("after");
        if (value != null) {
            try {
                p.setSpacingAfter(Float.parseFloat(value));
            } catch (Exception e) {
            }
        }
        value = props.getProperty("extraparaspace");
        if (value != null) {
            try {
                p.setExtraParagraphSpace(Float.parseFloat(value));
            } catch (Exception e) {
            }
        }
    }

    public static Paragraph createParagraph(ChainedProperties props) {
        Paragraph p = new Paragraph();
        createParagraph(p, props);
        return p;
    }

    public static ListItem createListItem(ChainedProperties props) {
        ListItem p = new ListItem();
        createParagraph(p, props);
        return p;
    }

    public Font getFont(ChainedProperties props) {
        String face = props.getProperty(ElementTags.FACE);
        if (face != null) {
            StringTokenizer tok = new StringTokenizer(face, ",");
            while (tok.hasMoreTokens()) {
                face = tok.nextToken().trim();
                if (face.startsWith("\""))
                    face = face.substring(1);
                if (face.endsWith("\""))
                    face = face.substring(0, face.length() - 1);
                if (fontImp.isRegistered(face))
                    break;
            }
        }
        int style = 0;
        if (props.hasProperty(HtmlTags.I))
            style |= Font.ITALIC;
        if (props.hasProperty(HtmlTags.B))
            style |= Font.BOLD;
        if (props.hasProperty(HtmlTags.U))
            style |= Font.UNDERLINE;
        if (props.hasProperty(HtmlTags.S))
            style |= Font.STRIKETHRU;
        String value = props.getProperty(ElementTags.SIZE);
        float size = 12;
        if (value != null)
            size = Float.parseFloat(value);
        Color color = Markup.decodeColor(props.getProperty("color"));
        String encoding = props.getProperty("encoding");
        if (encoding == null)
            encoding = BaseFont.WINANSI;
        return fontImp.getFont(face, encoding, true, size, style, color);
    }

    
    public static HyphenationEvent getHyphenation(ChainedProperties props) {
        return getHyphenation(props.getProperty("hyphenation"));
    }

    
    public static HyphenationEvent getHyphenation(HashMap<String, String> props) {
        return getHyphenation(props.get("hyphenation"));
    }

    
    public static HyphenationEvent getHyphenation(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
        String lang = s;
        String country = null;
        int leftMin = 2;
        int rightMin = 2;

        int pos = s.indexOf('_');
        if (pos == -1) {
            return new HyphenationAuto(lang, country, leftMin, rightMin);
        }
        lang = s.substring(0, pos);
        country = s.substring(pos + 1);
        pos = country.indexOf(',');
        if (pos == -1) {
            return new HyphenationAuto(lang, country, leftMin, rightMin);
        }
        s = country.substring(pos + 1);
        country = country.substring(0, pos);
        pos = s.indexOf(',');
        if (pos == -1) {
            leftMin = Integer.parseInt(s);
        } else {
            leftMin = Integer.parseInt(s.substring(0, pos));
            rightMin = Integer.parseInt(s.substring(pos + 1));
        }
        return new HyphenationAuto(lang, country, leftMin, rightMin);
    }

    public static void insertStyle(HashMap<String, String> h) {
        String style = h.get("style");
        if (style == null)
            return;
        Properties prop = Markup.parseAttributes(style);
        for (Object element : prop.keySet()) {
            String key = (String) element;
            if (key.equals(Markup.CSS_KEY_FONTFAMILY)) {
                h.put("face", prop.getProperty(key));
            } else if (key.equals(Markup.CSS_KEY_FONTSIZE)) {
                h.put("size", Float.toString(Markup.parseLength(prop
                        .getProperty(key)))
                        + "px");
            } else if (key.equals(Markup.CSS_KEY_FONTSTYLE)) {
                String ss = prop.getProperty(key).trim().toLowerCase();
                if (ss.equals("italic") || ss.equals("oblique"))
                    h.put("i", null);
            } else if (key.equals(Markup.CSS_KEY_FONTWEIGHT)) {
                String ss = prop.getProperty(key).trim().toLowerCase();
                if (ss.equals("bold") || ss.equals("700") || ss.equals("800")
                        || ss.equals("900"))
                    h.put("b", null);
            } else if (key.equals(Markup.CSS_KEY_FONTWEIGHT)) {
                String ss = prop.getProperty(key).trim().toLowerCase();
                if (ss.equals("underline"))
                    h.put("u", null);
            } else if (key.equals(Markup.CSS_KEY_COLOR)) {
                Color c = Markup.decodeColor(prop.getProperty(key));
                if (c != null) {
                    int hh = c.getRGB();
                    String hs = Integer.toHexString(hh);
                    hs = "000000" + hs;
                    hs = "#" + hs.substring(hs.length() - 6);
                    h.put("color", hs);
                }
            } else if (key.equals(Markup.CSS_KEY_LINEHEIGHT)) {
                String ss = prop.getProperty(key).trim();
                float v = Markup.parseLength(prop.getProperty(key));
                if (ss.endsWith("%")) {
                    h.put("leading", "0," + v / 100);
                } else {
                    h.put("leading", v + ",0");
                }
            } else if (key.equals(Markup.CSS_KEY_TEXTALIGN)) {
                String ss = prop.getProperty(key).trim().toLowerCase();
                h.put("align", ss);
            }
        }
    }

    
    public static void insertStyle(HashMap<String, String> h, ChainedProperties cprops) {
        String style = h.get("style");
        if (style == null)
            return;
        Properties prop = Markup.parseAttributes(style);
        for (Object element : prop.keySet()) {
            String key = (String) element;
            if (key.equals(Markup.CSS_KEY_FONTFAMILY)) {
                h.put(ElementTags.FACE, prop.getProperty(key));
            } else if (key.equals(Markup.CSS_KEY_FONTSIZE)) {
                float actualFontSize = Markup.parseLength(cprops
                        .getProperty(ElementTags.SIZE),
                        Markup.DEFAULT_FONT_SIZE);
                if (actualFontSize <= 0f)
                    actualFontSize = Markup.DEFAULT_FONT_SIZE;
                h.put(ElementTags.SIZE, Float.toString(Markup.parseLength(prop
                        .getProperty(key), actualFontSize))
                        + "pt");
            } else if (key.equals(Markup.CSS_KEY_FONTSTYLE)) {
                String ss = prop.getProperty(key).trim().toLowerCase();
                if (ss.equals("italic") || ss.equals("oblique"))
                    h.put("i", null);
            } else if (key.equals(Markup.CSS_KEY_FONTWEIGHT)) {
                String ss = prop.getProperty(key).trim().toLowerCase();
                if (ss.equals("bold") || ss.equals("700") || ss.equals("800")
                        || ss.equals("900"))
                    h.put("b", null);
            } else if (key.equals(Markup.CSS_KEY_FONTWEIGHT)) {
                String ss = prop.getProperty(key).trim().toLowerCase();
                if (ss.equals("underline"))
                    h.put("u", null);
            } else if (key.equals(Markup.CSS_KEY_COLOR)) {
                Color c = Markup.decodeColor(prop.getProperty(key));
                if (c != null) {
                    int hh = c.getRGB();
                    String hs = Integer.toHexString(hh);
                    hs = "000000" + hs;
                    hs = "#" + hs.substring(hs.length() - 6);
                    h.put("color", hs);
                }
            } else if (key.equals(Markup.CSS_KEY_LINEHEIGHT)) {
                String ss = prop.getProperty(key).trim();
                float actualFontSize = Markup.parseLength(cprops
                        .getProperty(ElementTags.SIZE),
                        Markup.DEFAULT_FONT_SIZE);
                if (actualFontSize <= 0f)
                    actualFontSize = Markup.DEFAULT_FONT_SIZE;
                float v = Markup.parseLength(prop.getProperty(key),
                        actualFontSize);
                if (ss.endsWith("%")) {
                    h.put("leading", "0," + v / 100);
                } else {
                    h.put("leading", v + ",0");
                }
            } else if (key.equals(Markup.CSS_KEY_TEXTALIGN)) {
                String ss = prop.getProperty(key).trim().toLowerCase();
                h.put("align", ss);
            }
        }
    }

    public FontFactoryImp getFontImp() {
        return fontImp;
    }

    public void setFontImp(FontFactoryImp fontImp) {
        this.fontImp = fontImp;
    }

    public static HashMap<String, String> followTags = new HashMap<String, String>();
    static {
        followTags.put("i", "i");
        followTags.put("b", "b");
        followTags.put("u", "u");
        followTags.put("sub", "sub");
        followTags.put("sup", "sup");
        followTags.put("em", "i");
        followTags.put("strong", "b");
        followTags.put("s", "s");
        followTags.put("strike", "s");
    }
}
