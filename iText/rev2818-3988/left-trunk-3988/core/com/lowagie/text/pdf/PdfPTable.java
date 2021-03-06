

package com.lowagie.text.pdf;

import java.util.ArrayList;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ElementListener;
import com.lowagie.text.Image;
import com.lowagie.text.LargeElement;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.events.PdfPTableEventForwarder;



public class PdfPTable implements LargeElement{
    
        
    public static final int BASECANVAS = 0;
    
        
    public static final int BACKGROUNDCANVAS = 1;
    
        
    public static final int LINECANVAS = 2;
    
        
    public static final int TEXTCANVAS = 3;
    
    protected ArrayList rows = new ArrayList();
    protected float totalHeight = 0;
    protected PdfPCell currentRow[];
    protected int currentRowIdx = 0;
    protected PdfPCell defaultCell = new PdfPCell((Phrase)null);
    protected float totalWidth = 0;
    protected float relativeWidths[];
    protected float absoluteWidths[];
    protected PdfPTableEvent tableEvent;
    
    
    protected int headerRows;
    
    
    protected float widthPercentage = 80;
    
    
    private int horizontalAlignment = Element.ALIGN_CENTER;
    
    
    private boolean skipFirstHeader = false;
    
    private boolean skipLastFooter = false;

    protected boolean isColspan = false;
    
    protected int runDirection = PdfWriter.RUN_DIRECTION_DEFAULT;

    
    private boolean lockedWidth = false;
    
    
    private boolean splitRows = true;
    
    
    protected float spacingBefore;
    
    
    protected float spacingAfter;
    
    
    private boolean extendLastRow;
    
    
    private boolean headersInEvent;
    
    
    private boolean splitLate = true;
    
    
    private boolean keepTogether;
    
    
    protected boolean complete = true;
    
    
    private int footerRows;
    
    
    protected boolean rowCompleted = true;
    
    protected PdfPTable() {
    }
    
        
    public PdfPTable(float relativeWidths[]) {
        if (relativeWidths == null)
            throw new NullPointerException("The widths array in PdfPTable constructor can not be null.");
        if (relativeWidths.length == 0)
            throw new IllegalArgumentException("The widths array in PdfPTable constructor can not have zero length.");
        this.relativeWidths = new float[relativeWidths.length];
        System.arraycopy(relativeWidths, 0, this.relativeWidths, 0, relativeWidths.length);
        absoluteWidths = new float[relativeWidths.length];
        calculateWidths();
        currentRow = new PdfPCell[absoluteWidths.length];
        keepTogether = false;
    }
    
        
    public PdfPTable(int numColumns) {
        if (numColumns <= 0)
            throw new IllegalArgumentException("The number of columns in PdfPTable constructor must be greater than zero.");
        relativeWidths = new float[numColumns];
        for (int k = 0; k < numColumns; ++k)
            relativeWidths[k] = 1;
        absoluteWidths = new float[relativeWidths.length];
        calculateWidths();
        currentRow = new PdfPCell[absoluteWidths.length];
        keepTogether = false;
    }
    
        
    public PdfPTable(PdfPTable table) {
        copyFormat(table);
        for (int k = 0; k < currentRow.length; ++k) {
            if (table.currentRow[k] == null)
                break;
            currentRow[k] = new PdfPCell(table.currentRow[k]);
        }
        for (int k = 0; k < table.rows.size(); ++k) {
            PdfPRow row = (PdfPRow)(table.rows.get(k));
            if (row != null)
                row = new PdfPRow(row);
            rows.add(row);
        }
    }
    
    
    public static PdfPTable shallowCopy(PdfPTable table) {
        PdfPTable nt = new PdfPTable();
        nt.copyFormat(table);
        return nt;
    }

    
    protected void copyFormat(PdfPTable sourceTable) {
        relativeWidths = new float[sourceTable.getNumberOfColumns()];
        absoluteWidths = new float[sourceTable.getNumberOfColumns()];
        System.arraycopy(sourceTable.relativeWidths, 0, relativeWidths, 0, getNumberOfColumns());
        System.arraycopy(sourceTable.absoluteWidths, 0, absoluteWidths, 0, getNumberOfColumns());
        totalWidth = sourceTable.totalWidth;
        totalHeight = sourceTable.totalHeight;
        currentRowIdx = 0;
        tableEvent = sourceTable.tableEvent;
        runDirection = sourceTable.runDirection;
        defaultCell = new PdfPCell(sourceTable.defaultCell);
        currentRow = new PdfPCell[sourceTable.currentRow.length];
        isColspan = sourceTable.isColspan;
        splitRows = sourceTable.splitRows;
        spacingAfter = sourceTable.spacingAfter;
        spacingBefore = sourceTable.spacingBefore;
        headerRows = sourceTable.headerRows;
        footerRows = sourceTable.footerRows;
        lockedWidth = sourceTable.lockedWidth;
        extendLastRow = sourceTable.extendLastRow;
        headersInEvent = sourceTable.headersInEvent;
        widthPercentage = sourceTable.widthPercentage;
        splitLate = sourceTable.splitLate;
        skipFirstHeader = sourceTable.skipFirstHeader;
        skipLastFooter = sourceTable.skipLastFooter;
        horizontalAlignment = sourceTable.horizontalAlignment;
        keepTogether = sourceTable.keepTogether;
        complete = sourceTable.complete;
    }

        
    public void setWidths(float relativeWidths[]) throws DocumentException {
        if (relativeWidths.length != getNumberOfColumns())
            throw new DocumentException("Wrong number of columns.");
        this.relativeWidths = new float[relativeWidths.length];
        System.arraycopy(relativeWidths, 0, this.relativeWidths, 0, relativeWidths.length);
        absoluteWidths = new float[relativeWidths.length];
        totalHeight = 0;
        calculateWidths();
        calculateHeights(true);
    }

        
    public void setWidths(int relativeWidths[]) throws DocumentException {
        float tb[] = new float[relativeWidths.length];
        for (int k = 0; k < relativeWidths.length; ++k)
            tb[k] = relativeWidths[k];
        setWidths(tb);
    }

    
    protected void calculateWidths() {
        if (totalWidth <= 0)
            return;
        float total = 0;
        int numCols = getNumberOfColumns();
        for (int k = 0; k < numCols; ++k)
            total += relativeWidths[k];
        for (int k = 0; k < numCols; ++k)
            absoluteWidths[k] = totalWidth * relativeWidths[k] / total;
    }
    
        
    public void setTotalWidth(float totalWidth) {
        if (this.totalWidth == totalWidth)
            return;
        this.totalWidth = totalWidth;
        totalHeight = 0;
        calculateWidths();
        calculateHeights(true);
    }

        
    public void setTotalWidth(float columnWidth[]) throws DocumentException {
        if (columnWidth.length != getNumberOfColumns())
            throw new DocumentException("Wrong number of columns.");
        totalWidth = 0;
        for (int k = 0; k < columnWidth.length; ++k)
            totalWidth += columnWidth[k];
        setWidths(columnWidth);
    }

        
    public void setWidthPercentage(float columnWidth[], Rectangle pageSize) throws DocumentException {
        if (columnWidth.length != getNumberOfColumns())
            throw new IllegalArgumentException("Wrong number of columns.");
        float totalWidth = 0;
        for (int k = 0; k < columnWidth.length; ++k)
            totalWidth += columnWidth[k];
        widthPercentage = totalWidth / (pageSize.getRight() - pageSize.getLeft()) * 100f;
        setWidths(columnWidth);
    }

        
    public float getTotalWidth() {
        return totalWidth;
    }
    
    
    public float calculateHeights(boolean firsttime) {
        if (totalWidth <= 0)
            return 0;
        totalHeight = 0;
        for (int k = 0; k < rows.size(); ++k) {
            PdfPRow row = (PdfPRow)rows.get(k);
            if (row != null) {
                if (firsttime)
                    row.setWidths(absoluteWidths);
                totalHeight += row.getMaxHeights();
            }
        }
        return totalHeight;
    }
    
    
    public void calculateHeightsFast() {
        calculateHeights(false);
    }
    
        
    public PdfPCell getDefaultCell() {
        return defaultCell;
    }
    
        
    public void addCell(PdfPCell cell) {
        rowCompleted = false;
        PdfPCell ncell = new PdfPCell(cell);
        
        int colspan = ncell.getColspan();
        colspan = Math.max(colspan, 1);
        colspan = Math.min(colspan, currentRow.length - currentRowIdx);
        ncell.setColspan(colspan);

        if (colspan != 1)
            isColspan = true;
        int rdir = ncell.getRunDirection();
        if (rdir == PdfWriter.RUN_DIRECTION_DEFAULT)
            ncell.setRunDirection(runDirection);
        
        skipColsWithRowspanAbove();
        
        boolean cellAdded = false;
        if (currentRowIdx < currentRow.length) {  
            currentRow[currentRowIdx] = ncell;
            currentRowIdx += colspan;
            cellAdded = true;
        }

        skipColsWithRowspanAbove();
        
        if (currentRowIdx >= currentRow.length) {
            int numCols = getNumberOfColumns();
            if (runDirection == PdfWriter.RUN_DIRECTION_RTL) {
                PdfPCell rtlRow[] = new PdfPCell[numCols];
                int rev = currentRow.length;
                for (int k = 0; k < currentRow.length; ++k) {
                    PdfPCell rcell = currentRow[k];
                    int cspan = rcell.getColspan();
                    rev -= cspan;
                    rtlRow[rev] = rcell;
                    k += cspan - 1;
                }
                currentRow = rtlRow;
            }
            PdfPRow row = new PdfPRow(currentRow);
            if (totalWidth > 0) {
                row.setWidths(absoluteWidths);
                totalHeight += row.getMaxHeights();
            }
            rows.add(row);
            currentRow = new PdfPCell[numCols];
            currentRowIdx = 0;
            rowCompleted = true;
        }
        
        if (!cellAdded) {
            currentRow[currentRowIdx] = ncell;
            currentRowIdx += colspan;
        }
    }
    
    
    private void skipColsWithRowspanAbove() {
        int direction = 1;
        if (runDirection == PdfWriter.RUN_DIRECTION_RTL)
            direction = -1;
        while (rowSpanAbove(rows.size(), currentRowIdx))
            currentRowIdx += direction;
    }
    
    
    boolean rowSpanAbove(int currRow, int currCol) {
        
        if ((currCol >= getNumberOfColumns()) 
                || (currCol < 0) 
                || (currRow == 0))
            return false;
        
        int row = currRow - 1;
        PdfPRow aboveRow = (PdfPRow)rows.get(row);
        if (aboveRow == null)
            return false;
        PdfPCell aboveCell = (PdfPCell)aboveRow.getCells()[currCol];
        while ((aboveCell == null) && (row > 0)) {
            aboveRow  = (PdfPRow)rows.get(--row);
            aboveCell = (PdfPCell)aboveRow.getCells()[currCol];
        }
        
        int distance = currRow - row;

        if (aboveCell == null) {
            int col = currCol - 1;
            aboveCell = (PdfPCell)aboveRow.getCells()[col];
            while ((aboveCell == null) && (row > 0))
                aboveCell = (PdfPCell)aboveRow.getCells()[--col];
            return aboveCell != null && aboveCell.getRowspan() > distance;
        }
        
        if ((aboveCell.getRowspan() == 1) && (distance > 1)) {
            int col = currCol - 1;
            aboveRow = (PdfPRow)rows.get(row + 1);
            distance--;
            aboveCell = (PdfPCell)aboveRow.getCells()[col];
            while ((aboveCell == null) && (col > 0))
                aboveCell = (PdfPCell)aboveRow.getCells()[--col];
        }
        
        return aboveCell != null && aboveCell.getRowspan() > distance;
    }
    
    
        
    public void addCell(String text) {
        addCell(new Phrase(text));
    }
    
        
    public void addCell(PdfPTable table) {
        defaultCell.setTable(table);
        addCell(defaultCell);
        defaultCell.setTable(null);
    }
    
        
    public void addCell(Image image) {
        defaultCell.setImage(image);
        addCell(defaultCell);
        defaultCell.setImage(null);
    }
    
        
    public void addCell(Phrase phrase) {
        defaultCell.setPhrase(phrase);
        addCell(defaultCell);
        defaultCell.setPhrase(null);
    }
    
        
    public float writeSelectedRows(int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte[] canvases) {
        return writeSelectedRows(0, -1, rowStart, rowEnd, xPos, yPos, canvases);
    }
    
        
    public float writeSelectedRows(int colStart, int colEnd, int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte[] canvases) {
        if (totalWidth <= 0)
            throw new RuntimeException("The table width must be greater than zero.");
        
        int totalRows = rows.size();
        if (rowStart < 0)
            rowStart = 0;
        if (rowEnd < 0)
            rowEnd = totalRows;
        else
            rowEnd = Math.min(rowEnd, totalRows);
        if (rowStart >= rowEnd)
            return yPos;
        
        int totalCols = getNumberOfColumns();
        if (colStart < 0)
            colStart = 0;
        else
            colStart = Math.min(colStart, totalCols);
        if (colEnd < 0)
            colEnd = totalCols;
        else
            colEnd = Math.min(colEnd, totalCols);
        
        float yPosStart = yPos;
        for (int k = rowStart; k < rowEnd; ++k) {
            PdfPRow row = (PdfPRow)rows.get(k);
            if (row != null) {
                row.writeCells(colStart, colEnd, xPos, yPos, canvases);
                yPos -= row.getMaxHeights();
            }
        }
        
        if (tableEvent != null && colStart == 0 && colEnd == totalCols) {
            float heights[] = new float[rowEnd - rowStart + 1];
            heights[0] = yPosStart;
            for (int k = rowStart; k < rowEnd; ++k) {
                PdfPRow row = (PdfPRow)rows.get(k);
                float hr = 0;
                if (row != null)
                    hr = row.getMaxHeights();
                heights[k - rowStart + 1] = heights[k - rowStart] - hr;
            }
            tableEvent.tableLayout(this, getEventWidths(xPos, rowStart, rowEnd, headersInEvent), heights, headersInEvent ? headerRows : 0, rowStart, canvases);
        }
        
        return yPos;
    }
    
        
    public float writeSelectedRows(int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas) {
        return writeSelectedRows(0, -1, rowStart, rowEnd, xPos, yPos, canvas);
    }
    
        
    public float writeSelectedRows(int colStart, int colEnd, int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas) {
        int totalCols = getNumberOfColumns();
        if (colStart < 0)
            colStart = 0;
        else
            colStart = Math.min(colStart, totalCols);
        
        if (colEnd < 0)
            colEnd = totalCols;
        else
            colEnd = Math.min(colEnd, totalCols);
        
        boolean clip = (colStart != 0 || colEnd != totalCols);
        
        if (clip) {
            float w = 0;
            for (int k = colStart; k < colEnd; ++k)
                w += absoluteWidths[k];
            canvas.saveState();
            float lx = (colStart == 0) ? 10000 : 0;
            float rx = (colEnd == totalCols) ? 10000 : 0;
            canvas.rectangle(xPos - lx, -10000, w + lx + rx, PdfPRow.RIGHT_LIMIT);
            canvas.clip();
            canvas.newPath();
        }
        
        PdfContentByte[] canvases = beginWritingRows(canvas);
        float y = writeSelectedRows(colStart, colEnd, rowStart, rowEnd, xPos, yPos, canvases);
        endWritingRows(canvases);
        
        if (clip)
            canvas.restoreState();
        
        return y;
    }
    
        
    public static PdfContentByte[] beginWritingRows(PdfContentByte canvas) {
        return new PdfContentByte[]{
            canvas,
            canvas.getDuplicate(),
            canvas.getDuplicate(),
            canvas.getDuplicate(),
        };
    }
    
        
    public static void endWritingRows(PdfContentByte[] canvases) {
        PdfContentByte canvas = canvases[BASECANVAS];
        canvas.saveState();
        canvas.add(canvases[BACKGROUNDCANVAS]);
        canvas.restoreState();
        canvas.saveState();
        canvas.setLineCap(2);
        canvas.resetRGBColorStroke();
        canvas.add(canvases[LINECANVAS]);
        canvas.restoreState();
        canvas.add(canvases[TEXTCANVAS]);
    }
    
        
    public int size() {
        return rows.size();
    }
    
        
    public float getTotalHeight() {
        return totalHeight;
    }
    
        
    public float getRowHeight(int idx) {
        if (totalWidth <= 0 || idx < 0 || idx >= rows.size())
            return 0;
        PdfPRow row = (PdfPRow)rows.get(idx);
        if (row == null)
            return 0;
        return row.getMaxHeights();
    }
    
        
    public float getRowspanHeight(int rowIndex, int cellIndex) {
        if (totalWidth <= 0 || rowIndex < 0 || rowIndex >= rows.size())
            return 0;
        PdfPRow row = (PdfPRow)rows.get(rowIndex);
        if (row == null || cellIndex >= row.getCells().length)
            return 0;
        PdfPCell cell = row.getCells()[cellIndex];
        if (cell == null)
            return 0;
        float rowspanHeight = 0;
        for (int j = 0; j < cell.getRowspan(); j++) {
            rowspanHeight += getRowHeight(rowIndex + j);
        }
        return rowspanHeight;
    }
    
        
    public float getHeaderHeight() {
        float total = 0;
        int size = Math.min(rows.size(), headerRows);
        for (int k = 0; k < size; ++k) {
            PdfPRow row = (PdfPRow)rows.get(k);
            if (row != null)
                total += row.getMaxHeights();
        }
        return total;
    }
    
        
    public float getFooterHeight() {
        float total = 0;
        int start = Math.max(0, headerRows - footerRows);
        int size = Math.min(rows.size(), headerRows);
        for (int k = start; k < size; ++k) {
            PdfPRow row = (PdfPRow)rows.get(k);
            if (row != null)
                total += row.getMaxHeights();
        }
        return total;
    }
    
        
    public boolean deleteRow(int rowNumber) {
        if (rowNumber < 0 || rowNumber >= rows.size())
            return false;
        if (totalWidth > 0) {
            PdfPRow row = (PdfPRow)rows.get(rowNumber);
            if (row != null)
                totalHeight -= row.getMaxHeights();
        }
        rows.remove(rowNumber);
        if (rowNumber < headerRows) {
            --headerRows;
            if (rowNumber >= (headerRows - footerRows))
                --footerRows;
        }
        return true;
    }
    
        
    public boolean deleteLastRow() {
        return deleteRow(rows.size() - 1);
    }
    
    
    public void deleteBodyRows() {
        ArrayList rows2 = new ArrayList();
        for (int k = 0; k < headerRows; ++k)
            rows2.add(rows.get(k));
        rows = rows2;
        totalHeight = 0;
        if (totalWidth > 0)
            totalHeight = getHeaderHeight();
    }
    
    
    public int getNumberOfColumns() {
        return relativeWidths.length;
    }

    
    public int getHeaderRows() {
        return headerRows;
    }
    
    
    public void setHeaderRows(int headerRows) {
        if (headerRows < 0)
            headerRows = 0;
        this.headerRows = headerRows;
    }
    
    
    public ArrayList getChunks() {
        return new ArrayList();
    }
    
    
    public int type() {
        return Element.PTABLE;
    }
    
    
    public boolean isContent() {
        return true;
    }

    
    public boolean isNestable() {
        return true;
    }
    
    
    public boolean process(ElementListener listener) {
        try {
            return listener.add(this);
        }
        catch(DocumentException de) {
            return false;
        }
    }
    
    
    public float getWidthPercentage() {
        return widthPercentage;
    }
    
    
    public void setWidthPercentage(float widthPercentage) {
        this.widthPercentage = widthPercentage;
    }
    
    
    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }
    
    
    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }
    
    
    public PdfPRow getRow(int idx) {
        return (PdfPRow)rows.get(idx);
    }

    
    public ArrayList getRows() {
        return rows;
    }
    
    
    public ArrayList getRows(int start, int end) {
        ArrayList list = new ArrayList();
        if (start < 0 || end > size()) {
            return list;
        }
        PdfPRow firstRow = adjustCellsInRow(start, end);
        int colIndex = 0;
        PdfPCell cell;
        while (colIndex < getNumberOfColumns()) {
            int rowIndex = start;
            while (rowSpanAbove(rowIndex--, colIndex)) {
                PdfPRow row = getRow(rowIndex);
                if (row != null) {
                    PdfPCell replaceCell = row.getCells()[colIndex];
                    if (replaceCell != null) {
                        firstRow.getCells()[colIndex] = new PdfPCell(replaceCell);
                        float extra = 0;
                        int stop = Math.min(rowIndex + replaceCell.getRowspan(), end);
                        for (int j = start + 1; j < stop; j++) {
                            extra += getRowHeight(j);
                        }
                        firstRow.setExtraHeight(colIndex, extra);
                        float diff = getRowspanHeight(rowIndex, colIndex)
                            - getRowHeight(start) - extra;
                        firstRow.getCells()[colIndex].consumeHeight(diff);
                    }
                }
            }
            cell = firstRow.getCells()[colIndex];
            if (cell == null)
                colIndex++;
            else
                colIndex += cell.getColspan();
        }
        list.add(firstRow);
        for (int i = start + 1; i < end; i++) {
            list.add(adjustCellsInRow(i, end));
        }
        return list;
    }
    
    
    protected PdfPRow adjustCellsInRow(int start, int end) {
        PdfPRow row = new PdfPRow(getRow(start));
        row.initExtraHeights();
        PdfPCell cell;
        PdfPCell[] cells = row.getCells();
        for (int i = 0; i < cells.length; i++) {
            cell = cells[i];
            if (cell == null || cell.getRowspan() == 1)
                continue;
            int stop = Math.min(end, start + cell.getRowspan());
            float extra = 0;
            for (int k = start + 1; k < stop; k++) {
                extra += getRowHeight(k);
            }
            row.setExtraHeight(i, extra);
        }
        return row;
    }

        
    public void setTableEvent(PdfPTableEvent event) {
        if (event == null)
            this.tableEvent = null;
        else if (this.tableEvent == null)
            this.tableEvent = event;
        else if (this.tableEvent instanceof PdfPTableEventForwarder)
            ((PdfPTableEventForwarder)this.tableEvent).addTableEvent(event);
        else {
            PdfPTableEventForwarder forward = new PdfPTableEventForwarder();
            forward.addTableEvent(this.tableEvent);
            forward.addTableEvent(event);
            this.tableEvent = forward;
        }
    }
    
        
    public PdfPTableEvent getTableEvent() {
        return tableEvent;
    }
    
        
    public float[] getAbsoluteWidths() {
        return absoluteWidths;
    }
    
    float [][] getEventWidths(float xPos, int firstRow, int lastRow, boolean includeHeaders) {
        if (includeHeaders) {
            firstRow = Math.max(firstRow, headerRows);
            lastRow = Math.max(lastRow, headerRows);
        }
        float widths[][] = new float[(includeHeaders ? headerRows : 0) + lastRow - firstRow][];
        if (isColspan) {
            int n = 0;
            if (includeHeaders) {
                for (int k = 0; k < headerRows; ++k) {
                    PdfPRow row = (PdfPRow)rows.get(k);
                    if (row == null)
                        ++n;
                    else
                        widths[n++] = row.getEventWidth(xPos);
                }
            }
            for (; firstRow < lastRow; ++firstRow) {
                    PdfPRow row = (PdfPRow)rows.get(firstRow);
                    if (row == null)
                        ++n;
                    else
                        widths[n++] = row.getEventWidth(xPos);
            }
        }
        else {
            int numCols = getNumberOfColumns();
            float width[] = new float[numCols + 1];
            width[0] = xPos;
            for (int k = 0; k < numCols; ++k)
                width[k + 1] = width[k] + absoluteWidths[k];
            for (int k = 0; k < widths.length; ++k)
                widths[k] = width;
        }
        return widths;
    }


    
    public boolean isSkipFirstHeader() {
        return skipFirstHeader;
    }


    
    public boolean isSkipLastFooter() {
        return skipLastFooter;
    }
    
    
    public void setSkipFirstHeader(boolean skipFirstHeader) {
        this.skipFirstHeader = skipFirstHeader;
    }
    
    
    public void setSkipLastFooter(boolean skipLastFooter) {
        this.skipLastFooter = skipLastFooter;
    }

    
    public void setRunDirection(int runDirection) {
        switch (runDirection) {
            case PdfWriter.RUN_DIRECTION_DEFAULT:
            case PdfWriter.RUN_DIRECTION_NO_BIDI:
            case PdfWriter.RUN_DIRECTION_LTR:
            case PdfWriter.RUN_DIRECTION_RTL:
                this.runDirection = runDirection;
                break;
            default:
                throw new RuntimeException("Invalid run direction: " + runDirection);
        }
    }
    
    
    public int getRunDirection() {
        return runDirection;
    }
    
    
    public boolean isLockedWidth() {
        return this.lockedWidth;
    }
    
    
    public void setLockedWidth(boolean lockedWidth) {
        this.lockedWidth = lockedWidth;
    }
    
    
    public boolean isSplitRows() {
        return this.splitRows;
    }
    
    
    public void setSplitRows(boolean splitRows) {
        this.splitRows = splitRows;
    }
    
    
    public void setSpacingBefore(float spacing) {
        this.spacingBefore = spacing;
    }
    
    
    public void setSpacingAfter(float spacing) {
        this.spacingAfter = spacing;
    }    

    
    public float spacingBefore() {
        return spacingBefore;
    }
    
    
    public float spacingAfter() {
        return spacingAfter;
    }    
    
    
    public boolean isExtendLastRow() {
        return extendLastRow;
    }
    
    
    public void setExtendLastRow(boolean extendLastRow) {
        this.extendLastRow = extendLastRow;
    }
    
    
    public boolean isHeadersInEvent() {
        return headersInEvent;
    }
    
    
    public void setHeadersInEvent(boolean headersInEvent) {
        this.headersInEvent = headersInEvent;
    }
    
    
    public boolean isSplitLate() {
        return splitLate;
    }
    
    
    public void setSplitLate(boolean splitLate) {
        this.splitLate = splitLate;
    }
    
    
    public void setKeepTogether(boolean keepTogether) {
        this.keepTogether = keepTogether;
    }
    
    
    public boolean getKeepTogether() {
        return keepTogether;
    }
    
    
    public int getFooterRows() {
        return this.footerRows;
    }
    
    
    public void setFooterRows(int footerRows) {
        if (footerRows < 0)
            footerRows = 0;
        this.footerRows = footerRows;
    }
    
    
    public void completeRow() {
        while (!rowCompleted) {
            addCell(defaultCell);
        }
    }
    
    
    public void flushContent() {
        deleteBodyRows();
        setSkipFirstHeader(true);
    }

    
    public boolean isComplete() {
        return complete;
    }

    
    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}