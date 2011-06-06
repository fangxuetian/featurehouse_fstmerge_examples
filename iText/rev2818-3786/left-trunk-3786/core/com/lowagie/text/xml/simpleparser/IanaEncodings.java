
package com.lowagie.text.xml.simpleparser;

import java.util.HashMap;



public class IanaEncodings {

    
    private static final HashMap map = new HashMap();

    static {        
        
        map.put("BIG5", "Big5");
        map.put("CSBIG5", "Big5");
        map.put("CP037", "CP037");
        map.put("IBM037", "CP037");
        map.put("CSIBM037", "CP037");
        map.put("EBCDIC-CP-US", "CP037");
        map.put("EBCDIC-CP-CA", "CP037");
        map.put("EBCDIC-CP-NL", "CP037");
        map.put("EBCDIC-CP-WT", "CP037");
        map.put("IBM277", "CP277");
        map.put("CP277", "CP277");
        map.put("CSIBM277", "CP277");
        map.put("EBCDIC-CP-DK", "CP277");
        map.put("EBCDIC-CP-NO", "CP277");
        map.put("IBM278", "CP278");
        map.put("CP278", "CP278");
        map.put("CSIBM278", "CP278");
        map.put("EBCDIC-CP-FI", "CP278");
        map.put("EBCDIC-CP-SE", "CP278");
        map.put("IBM280", "CP280");
        map.put("CP280", "CP280");
        map.put("CSIBM280", "CP280");
        map.put("EBCDIC-CP-IT", "CP280");
        map.put("IBM284", "CP284");
        map.put("CP284", "CP284");
        map.put("CSIBM284", "CP284");
        map.put("EBCDIC-CP-ES", "CP284");
        map.put("EBCDIC-CP-GB", "CP285");
        map.put("IBM285", "CP285");
        map.put("CP285", "CP285");
        map.put("CSIBM285", "CP285");
        map.put("EBCDIC-CP-FR", "CP297");
        map.put("IBM297", "CP297");
        map.put("CP297", "CP297");
        map.put("CSIBM297", "CP297");
        map.put("EBCDIC-CP-AR1", "CP420");
        map.put("IBM420", "CP420");
        map.put("CP420", "CP420");
        map.put("CSIBM420", "CP420");
        map.put("EBCDIC-CP-HE", "CP424");
        map.put("IBM424", "CP424");
        map.put("CP424", "CP424");
        map.put("CSIBM424", "CP424");
        map.put("EBCDIC-CP-CH", "CP500");
        map.put("IBM500", "CP500");
        map.put("CP500", "CP500");
        map.put("CSIBM500", "CP500");
        map.put("EBCDIC-CP-CH", "CP500");
        map.put("EBCDIC-CP-BE", "CP500");
        map.put("IBM868", "CP868");
        map.put("CP868", "CP868");
        map.put("CSIBM868", "CP868");
        map.put("CP-AR", "CP868");
        map.put("IBM869", "CP869");
        map.put("CP869", "CP869");
        map.put("CSIBM869", "CP869");
        map.put("CP-GR", "CP869");
        map.put("IBM870", "CP870");
        map.put("CP870", "CP870");
        map.put("CSIBM870", "CP870");
        map.put("EBCDIC-CP-ROECE", "CP870");
        map.put("EBCDIC-CP-YU", "CP870");
        map.put("IBM871", "CP871");
        map.put("CP871", "CP871");
        map.put("CSIBM871", "CP871");
        map.put("EBCDIC-CP-IS", "CP871");
        map.put("IBM918", "CP918");
        map.put("CP918", "CP918");
        map.put("CSIBM918", "CP918");
        map.put("EBCDIC-CP-AR2", "CP918");
        map.put("EUC-JP", "EUCJIS");
        map.put("CSEUCPkdFmtJapanese", "EUCJIS");
        map.put("EUC-KR", "KSC5601");
        map.put("GB2312", "GB2312");
        map.put("CSGB2312", "GB2312");
        map.put("ISO-2022-JP", "JIS");
        map.put("CSISO2022JP", "JIS");
        map.put("ISO-2022-KR", "ISO2022KR");
        map.put("CSISO2022KR", "ISO2022KR");
        map.put("ISO-2022-CN", "ISO2022CN");
        
        map.put("X0201", "JIS0201");
        map.put("CSISO13JISC6220JP", "JIS0201");
        map.put("X0208", "JIS0208");
        map.put("ISO-IR-87", "JIS0208");
        map.put("X0208dbiJIS_X0208-1983", "JIS0208");
        map.put("CSISO87JISX0208", "JIS0208");
        map.put("X0212", "JIS0212");
        map.put("ISO-IR-159", "JIS0212");
        map.put("CSISO159JISX02121990", "JIS0212");
        map.put("SHIFT_JIS", "SJIS");
        map.put("CSSHIFT_JIS", "SJIS");
        map.put("MS_Kanji", "SJIS");
        
        
        map.put("WINDOWS-1250", "Cp1250");
        map.put("WINDOWS-1251", "Cp1251");
        map.put("WINDOWS-1252", "Cp1252");
        map.put("WINDOWS-1253", "Cp1253");
        map.put("WINDOWS-1254", "Cp1254");
        map.put("WINDOWS-1255", "Cp1255");
        map.put("WINDOWS-1256", "Cp1256");
        map.put("WINDOWS-1257", "Cp1257");
        map.put("WINDOWS-1258", "Cp1258");
        map.put("TIS-620", "TIS620");
        
        map.put("ISO-8859-1", "ISO8859_1");
        map.put("ISO-IR-100", "ISO8859_1");
        map.put("ISO_8859-1", "ISO8859_1");
        map.put("LATIN1", "ISO8859_1");
        map.put("CSISOLATIN1", "ISO8859_1");
        map.put("L1", "ISO8859_1");
        map.put("IBM819", "ISO8859_1");
        map.put("CP819", "ISO8859_1");
        
        map.put("ISO-8859-2", "ISO8859_2");
        map.put("ISO-IR-101", "ISO8859_2");
        map.put("ISO_8859-2", "ISO8859_2");
        map.put("LATIN2", "ISO8859_2");
        map.put("CSISOLATIN2", "ISO8859_2");
        map.put("L2", "ISO8859_2");
        
        map.put("ISO-8859-3", "ISO8859_3");
        map.put("ISO-IR-109", "ISO8859_3");
        map.put("ISO_8859-3", "ISO8859_3");
        map.put("LATIN3", "ISO8859_3");
        map.put("CSISOLATIN3", "ISO8859_3");
        map.put("L3", "ISO8859_3");
        
        map.put("ISO-8859-4", "ISO8859_4");
        map.put("ISO-IR-110", "ISO8859_4");
        map.put("ISO_8859-4", "ISO8859_4");
        map.put("LATIN4", "ISO8859_4");
        map.put("CSISOLATIN4", "ISO8859_4");
        map.put("L4", "ISO8859_4");
        
        map.put("ISO-8859-5", "ISO8859_5");
        map.put("ISO-IR-144", "ISO8859_5");
        map.put("ISO_8859-5", "ISO8859_5");
        map.put("CYRILLIC", "ISO8859_5");
        map.put("CSISOLATINCYRILLIC", "ISO8859_5");
        
        map.put("ISO-8859-6", "ISO8859_6");
        map.put("ISO-IR-127", "ISO8859_6");
        map.put("ISO_8859-6", "ISO8859_6");
        map.put("ECMA-114", "ISO8859_6");
        map.put("ASMO-708", "ISO8859_6");
        map.put("ARABIC", "ISO8859_6");
        map.put("CSISOLATINARABIC", "ISO8859_6");
        
        map.put("ISO-8859-7", "ISO8859_7");
        map.put("ISO-IR-126", "ISO8859_7");
        map.put("ISO_8859-7", "ISO8859_7");
        map.put("ELOT_928", "ISO8859_7");
        map.put("ECMA-118", "ISO8859_7");
        map.put("GREEK", "ISO8859_7");
        map.put("CSISOLATINGREEK", "ISO8859_7");
        map.put("GREEK8", "ISO8859_7");
        
        map.put("ISO-8859-8", "ISO8859_8");
        map.put("ISO-8859-8-I", "ISO8859_8"); 
        map.put("ISO-IR-138", "ISO8859_8");
        map.put("ISO_8859-8", "ISO8859_8");
        map.put("HEBREW", "ISO8859_8");
        map.put("CSISOLATINHEBREW", "ISO8859_8");
        
        map.put("ISO-8859-9", "ISO8859_9");
        map.put("ISO-IR-148", "ISO8859_9");
        map.put("ISO_8859-9", "ISO8859_9");
        map.put("LATIN5", "ISO8859_9");
        map.put("CSISOLATIN5", "ISO8859_9");
        map.put("L5", "ISO8859_9");
        
        map.put("KOI8-R", "KOI8_R");
        map.put("CSKOI8-R", "KOI8_R");
        map.put("US-ASCII", "ASCII");
        map.put("ISO-IR-6", "ASCII");
        map.put("ANSI_X3.4-1986", "ASCII");
        map.put("ISO_646.IRV:1991", "ASCII");
        map.put("ASCII", "ASCII");
        map.put("CSASCII", "ASCII");
        map.put("ISO646-US", "ASCII");
        map.put("US", "ASCII");
        map.put("IBM367", "ASCII");
        map.put("CP367", "ASCII");
        map.put("UTF-8", "UTF8");
        map.put("UTF-16", "Unicode");
        map.put("UTF-16BE", "UnicodeBig");
        map.put("UTF-16LE", "UnicodeLittle");
    }
    
        
    public static String getJavaEncoding(String iana) {
        String IANA = iana.toUpperCase();
        String jdec = (String)map.get(IANA);
        if (jdec == null)
            jdec = iana;
        return jdec;
    }
}
