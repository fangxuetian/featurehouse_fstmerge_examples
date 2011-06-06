
package com.lowagie.text.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;


class PdfCopyFieldsImp extends PdfWriter {

    private static final PdfName iTextTag = new PdfName("_iTextTag_");
    private static final Integer zero = new Integer(0);
    ArrayList readers = new ArrayList();
    HashMap readers2intrefs = new HashMap();
    HashMap pages2intrefs = new HashMap();
    HashMap visited = new HashMap();
    ArrayList fields = new ArrayList();
    RandomAccessFileOrArray file;
    HashMap fieldTree = new HashMap();
    ArrayList pageRefs = new ArrayList();
    ArrayList pageDics = new ArrayList();
    PdfDictionary resources = new PdfDictionary();
    PdfDictionary form;
    boolean closing = false;
    Document nd;
    private HashMap tabOrder;
    private ArrayList calculationOrder = new ArrayList();
    private ArrayList calculationOrderRefs;
    private boolean hasSignature;
    
    PdfCopyFieldsImp(OutputStream os) throws DocumentException {
        this(os, '\0');
    }
    
    PdfCopyFieldsImp(OutputStream os, char pdfVersion) throws DocumentException {
        super(new PdfDocument(), os);
        pdf.addWriter(this);
        if (pdfVersion != 0)
            super.setPdfVersion(pdfVersion);
        nd = new Document();
        nd.addDocListener(pdf);
    }
    
    void addDocument(PdfReader reader, List pagesToKeep) throws DocumentException {
        if (!readers2intrefs.containsKey(reader) && reader.isTampered())
            throw new DocumentException("The document was reused.");
        reader = new PdfReader(reader);        
        reader.selectPages(pagesToKeep);
        if (reader.getNumberOfPages() == 0)
            return;
        reader.setTampered(false);
        addDocument(reader);
    }
    
    void addDocument(PdfReader reader) throws DocumentException {
        if (!reader.isOpenedWithFullPermissions())
            throw new IllegalArgumentException("PdfReader not opened with owner password");
        openDoc();
        if (readers2intrefs.containsKey(reader)) {
            reader = new PdfReader(reader);
        }
        else {
            if (reader.isTampered())
                throw new DocumentException("The document was reused.");
            reader.consolidateNamedDestinations();
            reader.setTampered(true);
        }
        reader.shuffleSubsetNames();
        readers2intrefs.put(reader, new IntHashtable());
        readers.add(reader);
        int len = reader.getNumberOfPages();
        IntHashtable refs = new IntHashtable();
        for (int p = 1; p <= len; ++p) {
            refs.put(reader.getPageOrigRef(p).getNumber(), 1);
            reader.releasePage(p);
        }
        pages2intrefs.put(reader, refs);
        visited.put(reader, new IntHashtable());
        fields.add(reader.getAcroFields());
        updateCalculationOrder(reader);
    }
    
    private static String getCOName(PdfReader reader, PRIndirectReference ref) {
        String name = "";
        while (ref != null) {
            PdfObject obj = PdfReader.getPdfObject(ref);
            if (obj == null || obj.type() != PdfObject.DICTIONARY)
                break;
            PdfDictionary dic = (PdfDictionary)obj;
            PdfString t = (PdfString)PdfReader.getPdfObject(dic.get(PdfName.T));
            if (t != null) {
                name = t.toUnicodeString()+ "." + name;
            }
            ref = (PRIndirectReference)dic.get(PdfName.PARENT);
        }
        if (name.endsWith("."))
            name = name.substring(0, name.length() - 1);
        return name;
    }
    
    private void updateCalculationOrder(PdfReader reader) {
        PdfDictionary catalog = reader.getCatalog();
        PdfDictionary acro = (PdfDictionary)PdfReader.getPdfObject(catalog.get(PdfName.ACROFORM));
        if (acro == null)
            return;
        PdfArray co = (PdfArray)PdfReader.getPdfObject(acro.get(PdfName.CO));
        if (co == null || co.size() == 0)
            return;
        AcroFields af = reader.getAcroFields();
        ArrayList coa = co.getArrayList();
        for (int k = 0; k < coa.size(); ++k) {
            PdfObject obj = (PdfObject)coa.get(k);
            if (obj == null || !obj.isIndirect())
                continue;
            String name = getCOName(reader, (PRIndirectReference)obj);
            if (af.getFieldItem(name) == null)
                continue;
            name = "." + name;
            if (calculationOrder.contains(name))
                continue;
            calculationOrder.add(name);
        }
    }
    
    void propagate(PdfObject obj, PdfIndirectReference refo, boolean restricted) throws IOException {
        if (obj == null)
            return;


        if (obj instanceof PdfIndirectReference)
            return;
        switch (obj.type()) {
            case PdfObject.DICTIONARY:
            case PdfObject.STREAM: {
                PdfDictionary dic = (PdfDictionary)obj;
                for (Iterator it = dic.getKeys().iterator(); it.hasNext();) {
                    PdfName key = (PdfName)it.next();
                    if (restricted && (key.equals(PdfName.PARENT) || key.equals(PdfName.KIDS)))
                        continue;
                    PdfObject ob = dic.get(key);
                    if (ob != null && ob.isIndirect()) {
                        PRIndirectReference ind = (PRIndirectReference)ob;
                        if (!setVisited(ind) && !isPage(ind)) {
                            PdfIndirectReference ref = getNewReference(ind);
                            propagate(PdfReader.getPdfObjectRelease(ind), ref, restricted);
                        }
                    }
                    else
                        propagate(ob, null, restricted);
                }
                break;
            }
            case PdfObject.ARRAY: {
                ArrayList list = ((PdfArray)obj).getArrayList();
                
                for (Iterator it = list.iterator(); it.hasNext();) {
                    PdfObject ob = (PdfObject)it.next();
                    if (ob != null && ob.isIndirect()) {
                        PRIndirectReference ind = (PRIndirectReference)ob;
                        if (!isVisited(ind) && !isPage(ind)) {
                            PdfIndirectReference ref = getNewReference(ind);
                            propagate(PdfReader.getPdfObjectRelease(ind), ref, restricted);
                        }
                    }
                    else
                        propagate(ob, null, restricted);
                }
                break;
            }
            case PdfObject.INDIRECT: {
                throw new RuntimeException("Reference pointing to reference.");
            }
        }
    }
    
    private void adjustTabOrder(PdfArray annots, PdfIndirectReference ind, PdfNumber nn) {
        int v = nn.intValue();
        ArrayList t = (ArrayList)tabOrder.get(annots);
        if (t == null) {
            t = new ArrayList();
            int size = annots.size() - 1;
            for (int k = 0; k < size; ++k) {
                t.add(zero);
            }
            t.add(new Integer(v));
            tabOrder.put(annots, t);
            annots.add(ind);
        }
        else {
            int size = t.size() - 1;
            for (int k = size; k >= 0; --k) {
                if (((Integer)t.get(k)).intValue() <= v) {
                    t.add(k + 1, new Integer(v));
                    annots.getArrayList().add(k + 1, ind);
                    size = -2;
                    break;
                }
            }
            if (size != -2) {
                t.add(0, new Integer(v));
                annots.getArrayList().add(0, ind);
            }
        }
    }
    
    protected PdfArray branchForm(HashMap level, PdfIndirectReference parent, String fname) throws IOException {
        PdfArray arr = new PdfArray();
        for (Iterator it = level.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            String name = (String) entry.getKey();
            Object obj = entry.getValue();
            PdfIndirectReference ind = getPdfIndirectReference();
            PdfDictionary dic = new PdfDictionary();
            if (parent != null)
                dic.put(PdfName.PARENT, parent);
            dic.put(PdfName.T, new PdfString(name, PdfObject.TEXT_UNICODE));
            String fname2 = fname + "." + name;
            int coidx = calculationOrder.indexOf(fname2);
            if (coidx >= 0)
                calculationOrderRefs.set(coidx, ind);
            if (obj instanceof HashMap) {
                dic.put(PdfName.KIDS, branchForm((HashMap)obj, ind, fname2));
                arr.add(ind);
                addToBody(dic, ind);
            }
            else {
                ArrayList list = (ArrayList)obj;
                dic.mergeDifferent((PdfDictionary)list.get(0));
                if (list.size() == 3) {
                    dic.mergeDifferent((PdfDictionary)list.get(2));
                    int page = ((Integer)list.get(1)).intValue();
                    PdfDictionary pageDic = (PdfDictionary)pageDics.get(page - 1);
                    PdfArray annots = (PdfArray)PdfReader.getPdfObject(pageDic.get(PdfName.ANNOTS));
                    if (annots == null) {
                        annots = new PdfArray();
                        pageDic.put(PdfName.ANNOTS, annots);
                    }
                    PdfNumber nn = (PdfNumber)dic.get(iTextTag);
                    dic.remove(iTextTag);
                    adjustTabOrder(annots, ind, nn);
                }
                else {
                    PdfArray kids = new PdfArray();
                    for (int k = 1; k < list.size(); k += 2) {
                        int page = ((Integer)list.get(k)).intValue();
                        PdfDictionary pageDic = (PdfDictionary)pageDics.get(page - 1);
                        PdfArray annots = (PdfArray)PdfReader.getPdfObject(pageDic.get(PdfName.ANNOTS));
                        if (annots == null) {
                            annots = new PdfArray();
                            pageDic.put(PdfName.ANNOTS, annots);
                        }
                        PdfDictionary widget = new PdfDictionary();
                        widget.merge((PdfDictionary)list.get(k + 1));
                        widget.put(PdfName.PARENT, ind);
                        PdfNumber nn = (PdfNumber)widget.get(iTextTag);
                        widget.remove(iTextTag);
                        PdfIndirectReference wref = addToBody(widget).getIndirectReference();
                        adjustTabOrder(annots, wref, nn);
                        kids.add(wref);
                        propagate(widget, null, false);
                    }
                    dic.put(PdfName.KIDS, kids);
                }
                arr.add(ind);
                addToBody(dic, ind);
                propagate(dic, null, false);
            }
        }
        return arr;
    }
    
    protected void createAcroForms() throws IOException {
        if (fieldTree.isEmpty())
            return;
        form = new PdfDictionary();
        form.put(PdfName.DR, resources);
        propagate(resources, null, false);
        form.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
        tabOrder = new HashMap();
        calculationOrderRefs = new ArrayList(calculationOrder);
        form.put(PdfName.FIELDS, branchForm(fieldTree, null, ""));
        if (hasSignature)
            form.put(PdfName.SIGFLAGS, new PdfNumber(3));
        PdfArray co = new PdfArray();
        for (int k = 0; k < calculationOrderRefs.size(); ++k) {
            Object obj = calculationOrderRefs.get(k);
            if (obj instanceof PdfIndirectReference)
                co.add((PdfIndirectReference)obj);
        }
        if (co.size() > 0)
            form.put(PdfName.CO, co);
    }
    
    public void close() {
        if (closing) {
            super.close();
            return;
        }
        closing = true;
        try {
            closeIt();
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
    
    protected void closeIt() throws IOException {
        for (int k = 0; k < readers.size(); ++k) {
            ((PdfReader)readers.get(k)).removeFields();
        }
        for (int r = 0; r < readers.size(); ++r) {
            PdfReader reader = (PdfReader)readers.get(r);
            for (int page = 1; page <= reader.getNumberOfPages(); ++page) {
                pageRefs.add(getNewReference(reader.getPageOrigRef(page)));
                pageDics.add(reader.getPageN(page));
            }
        }
        mergeFields();
        createAcroForms();
        for (int r = 0; r < readers.size(); ++r) {
                PdfReader reader = (PdfReader)readers.get(r);
                for (int page = 1; page <= reader.getNumberOfPages(); ++page) {
                    PdfDictionary dic = reader.getPageN(page);
                    PdfIndirectReference pageRef = getNewReference(reader.getPageOrigRef(page));
                    PdfIndirectReference parent = root.addPageRef(pageRef);
                    dic.put(PdfName.PARENT, parent);
                    propagate(dic, pageRef, false);
                }
        }
        for (Iterator it = readers2intrefs.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            PdfReader reader = (PdfReader) entry.getKey();
            try {
                file = reader.getSafeFile();
                file.reOpen();
                IntHashtable t = (IntHashtable) entry.getValue();
                int keys[] = t.toOrderedKeys();
                for (int k = 0; k < keys.length; ++k) {
                    PRIndirectReference ref = new PRIndirectReference(reader, keys[k]);
                    addToBody(PdfReader.getPdfObjectRelease(ref), t.get(keys[k]));
                }
            }
            finally {
                try {
                    file.close();
                    reader.close();
                }
                catch (Exception e) {
                    
                }
            }
        }
        pdf.close();
    }
    
    void addPageOffsetToField(HashMap fd, int pageOffset) {
        if (pageOffset == 0)
            return;
        for (Iterator it = fd.values().iterator(); it.hasNext();) {
            ArrayList page = ((AcroFields.Item)it.next()).page;
            for (int k = 0; k < page.size(); ++k)
                page.set(k, new Integer(((Integer)page.get(k)).intValue() + pageOffset));
        }
    }

    void createWidgets(ArrayList list, AcroFields.Item item) {
        for (int k = 0; k < item.merged.size(); ++k) {
            list.add(item.page.get(k));
            PdfDictionary merged = (PdfDictionary)item.merged.get(k);
            PdfObject dr = merged.get(PdfName.DR);
            if (dr != null)
                PdfFormField.mergeResources(resources, (PdfDictionary)PdfReader.getPdfObject(dr));
            PdfDictionary widget = new PdfDictionary();
            for (Iterator it = merged.getKeys().iterator(); it.hasNext();) {
                PdfName key = (PdfName)it.next();
                if (widgetKeys.containsKey(key))
                    widget.put(key, merged.get(key));
            }
            widget.put(iTextTag, new PdfNumber(((Integer)item.tabOrder.get(k)).intValue() + 1));
            list.add(widget);
        }
    }
    
    void mergeField(String name, AcroFields.Item item) {
        HashMap map = fieldTree;
        StringTokenizer tk = new StringTokenizer(name, ".");
        if (!tk.hasMoreTokens())
            return;
        while (true) {
            String s = tk.nextToken();
            Object obj = map.get(s);
            if (tk.hasMoreTokens()) {
                if (obj == null) {
                    obj = new HashMap();
                    map.put(s, obj);
                    map = (HashMap)obj;
                    continue;
                }
                else if (obj instanceof HashMap)
                    map = (HashMap)obj;
                else
                    return;
            }
            else {
                if (obj instanceof HashMap)
                    return;
                PdfDictionary merged = (PdfDictionary)item.merged.get(0);
                if (obj == null) {
                    PdfDictionary field = new PdfDictionary();
                    if (PdfName.SIG.equals(merged.get(PdfName.FT)))
                        hasSignature = true;
                    for (Iterator it = merged.getKeys().iterator(); it.hasNext();) {
                        PdfName key = (PdfName)it.next();
                        if (fieldKeys.containsKey(key))
                            field.put(key, merged.get(key));
                    }
                    ArrayList list = new ArrayList();
                    list.add(field);
                    createWidgets(list, item);
                    map.put(s, list);
                }
                else {
                    ArrayList list = (ArrayList)obj;
                    PdfDictionary field = (PdfDictionary)list.get(0);
                    PdfName type1 = (PdfName)field.get(PdfName.FT);
                    PdfName type2 = (PdfName)merged.get(PdfName.FT);
                    if (type1 == null || !type1.equals(type2))
                        return;
                    int flag1 = 0;
                    PdfObject f1 = field.get(PdfName.FF);
                    if (f1 != null && f1.isNumber())
                        flag1 = ((PdfNumber)f1).intValue();
                    int flag2 = 0;
                    PdfObject f2 = merged.get(PdfName.FF);
                    if (f2 != null && f2.isNumber())
                        flag2 = ((PdfNumber)f2).intValue();
                    if (type1.equals(PdfName.BTN)) {
                        if (((flag1 ^ flag2) & PdfFormField.FF_PUSHBUTTON) != 0)
                            return;
                        if ((flag1 & PdfFormField.FF_PUSHBUTTON) == 0 && ((flag1 ^ flag2) & PdfFormField.FF_RADIO) != 0)
                            return;
                    }
                    else if (type1.equals(PdfName.CH)) {
                        if (((flag1 ^ flag2) & PdfFormField.FF_COMBO) != 0)
                            return;
                    }
                    createWidgets(list, item);
                }
                return;
            }
        }
    }
    
    void mergeWithMaster(HashMap fd) {
        for (Iterator it = fd.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            String name = (String) entry.getKey();
            mergeField(name, (AcroFields.Item) entry.getValue());
        }
    }
    
    void mergeFields() {
        int pageOffset = 0;
        for (int k = 0; k < fields.size(); ++k) {
            HashMap fd = ((AcroFields)fields.get(k)).getFields();
            addPageOffsetToField(fd, pageOffset);
            mergeWithMaster(fd);
            pageOffset += ((PdfReader)readers.get(k)).getNumberOfPages();
        }
    }

    public PdfIndirectReference getPageReference(int page) {
        return (PdfIndirectReference)pageRefs.get(page - 1);
    }
    
    protected PdfDictionary getCatalog(PdfIndirectReference rootObj) {
        try {
            PdfDictionary cat = pdf.getCatalog(rootObj);
            if (form != null) {
                PdfIndirectReference ref = addToBody(form).getIndirectReference();
                cat.put(PdfName.ACROFORM, ref);
            }
            return cat;
        }
        catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    protected PdfIndirectReference getNewReference(PRIndirectReference ref) {
        return new PdfIndirectReference(0, getNewObjectNumber(ref.getReader(), ref.getNumber(), 0));
    }
    
    protected int getNewObjectNumber(PdfReader reader, int number, int generation) {
        IntHashtable refs = (IntHashtable)readers2intrefs.get(reader);
        int n = refs.get(number);
        if (n == 0) {
            n = getIndirectReferenceNumber();
            refs.put(number, n);
        }
        return n;
    }
    
    protected boolean isVisited(PdfReader reader, int number, int generation) {
        IntHashtable refs = (IntHashtable)readers2intrefs.get(reader);
        return refs.containsKey(number);
    }
    
    protected boolean isVisited(PRIndirectReference ref) {
        IntHashtable refs = (IntHashtable)visited.get(ref.getReader());
        return refs.containsKey(ref.getNumber());
    }
    
    protected boolean setVisited(PRIndirectReference ref) {
        IntHashtable refs = (IntHashtable)visited.get(ref.getReader());
        return (refs.put(ref.getNumber(), 1) != 0);
    }
    
    protected boolean isPage(PRIndirectReference ref) {
        IntHashtable refs = (IntHashtable)pages2intrefs.get(ref.getReader());
        return refs.containsKey(ref.getNumber());
    }

    RandomAccessFileOrArray getReaderFile(PdfReader reader) {
            return file;
    }

    public void openDoc() {
        if (!nd.isOpen())
            nd.open();
    }    
    
    protected static final HashMap widgetKeys = new HashMap();
    protected static final HashMap fieldKeys = new HashMap();
    static {
        Integer one = new Integer(1);
        widgetKeys.put(PdfName.SUBTYPE, one);
        widgetKeys.put(PdfName.CONTENTS, one);
        widgetKeys.put(PdfName.RECT, one);
        widgetKeys.put(PdfName.NM, one);
        widgetKeys.put(PdfName.M, one);
        widgetKeys.put(PdfName.F, one);
        widgetKeys.put(PdfName.BS, one);
        widgetKeys.put(PdfName.BORDER, one);
        widgetKeys.put(PdfName.AP, one);
        widgetKeys.put(PdfName.AS, one);
        widgetKeys.put(PdfName.C, one);
        widgetKeys.put(PdfName.A, one);
        widgetKeys.put(PdfName.STRUCTPARENT, one);
        widgetKeys.put(PdfName.OC, one);
        widgetKeys.put(PdfName.H, one);
        widgetKeys.put(PdfName.MK, one);
        widgetKeys.put(PdfName.DA, one);
        widgetKeys.put(PdfName.Q, one);
        fieldKeys.put(PdfName.AA, one);
        fieldKeys.put(PdfName.FT, one);
        fieldKeys.put(PdfName.TU, one);
        fieldKeys.put(PdfName.TM, one);
        fieldKeys.put(PdfName.FF, one);
        fieldKeys.put(PdfName.V, one);
        fieldKeys.put(PdfName.DV, one);
        fieldKeys.put(PdfName.DS, one);
        fieldKeys.put(PdfName.RV, one);
        fieldKeys.put(PdfName.OPT, one);
        fieldKeys.put(PdfName.MAXLEN, one);
        fieldKeys.put(PdfName.TI, one);
        fieldKeys.put(PdfName.I, one);
        fieldKeys.put(PdfName.LOCK, one);
        fieldKeys.put(PdfName.SV, one);
    }
}
