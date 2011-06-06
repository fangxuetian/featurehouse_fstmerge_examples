

package com.lowagie.text.rtf.table;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.lowagie.text.Cell;
import com.lowagie.text.Element;
import com.lowagie.text.Row;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPRow;
import com.lowagie.text.rtf.RtfElement;
import com.lowagie.text.rtf.document.RtfDocument;



public class RtfRow extends RtfElement {

    
    private static final byte[] ROW_BEGIN = "\\trowd".getBytes();
    
    private static final byte[] ROW_WIDTH_STYLE = "\\trftsWidth3".getBytes();
    
    private static final byte[] ROW_WIDTH = "\\trwWidth".getBytes();
    
    private static final byte[] ROW_KEEP_TOGETHER = "\\trkeep".getBytes();
    
    private static final byte[] ROW_HEADER_ROW = "\\trhdr".getBytes();
    
    private static final byte[] ROW_ALIGN_LEFT = "\\trql".getBytes();
    
    private static final byte[] ROW_ALIGN_RIGHT = "\\trqr".getBytes();
    
    private static final byte[] ROW_ALIGN_CENTER = "\\trqc".getBytes();
    
    private static final byte[] ROW_ALIGN_JUSTIFIED = "\\trqj".getBytes();
    
    private static final byte[] ROW_GRAPH = "\\trgaph10".getBytes();
    
    private static final byte[] ROW_CELL_SPACING_LEFT = "\\trspdl".getBytes();
    
    private static final byte[] ROW_CELL_SPACING_TOP = "\\trspdt".getBytes();
    
    private static final byte[] ROW_CELL_SPACING_RIGHT = "\\trspdr".getBytes();
    
    private static final byte[] ROW_CELL_SPACING_BOTTOM = "\\trspdb".getBytes();
    
    private static final byte[] ROW_CELL_SPACING_LEFT_STYLE = "\\trspdfl3".getBytes();
    
    private static final byte[] ROW_CELL_SPACING_TOP_STYLE = "\\trspdft3".getBytes();
    
    private static final byte[] ROW_CELL_SPACING_RIGHT_STYLE = "\\trspdfr3".getBytes();
    
    private static final byte[] ROW_CELL_SPACING_BOTTOM_STYLE = "\\trspdfb3".getBytes();
    
    private static final byte[] ROW_CELL_PADDING_LEFT = "\\trpaddl".getBytes();
    
    private static final byte[] ROW_CELL_PADDING_RIGHT = "\\trpaddr".getBytes();
    
    private static final byte[] ROW_CELL_PADDING_LEFT_STYLE = "\\trpaddfl3".getBytes();
    
    private static final byte[] ROW_CELL_PADDING_RIGHT_STYLE = "\\trpaddfr3".getBytes();
    
    private static final byte[] ROW_END = "\\row".getBytes();

    
    private RtfTable parentTable = null;
    
    private ArrayList cells = null;
    
    private int width = 0;
    
    private int rowNumber = 0;
    
    
    protected RtfRow(RtfDocument doc, RtfTable rtfTable, Row row, int rowNumber) {
        super(doc);
        this.parentTable = rtfTable;
        this.rowNumber = rowNumber;
        importRow(row);
    }

    
    protected RtfRow(RtfDocument doc, RtfTable rtfTable, PdfPRow row, int rowNumber) {
        super(doc);
        this.parentTable = rtfTable;
        this.rowNumber = rowNumber;
        importRow(row);
    }
    

    
    private void importRow(Row row) {
        this.cells = new ArrayList();
        this.width = this.document.getDocumentHeader().getPageSetting().getPageWidth() - this.document.getDocumentHeader().getPageSetting().getMarginLeft() - this.document.getDocumentHeader().getPageSetting().getMarginRight();
        this.width = (int) (this.width * this.parentTable.getTableWidthPercent() / 100);
        
        int cellRight = 0;
        int cellWidth = 0;
        for(int i = 0; i < row.getColumns(); i++) {
            cellWidth = (int) (this.width * this.parentTable.getProportionalWidths()[i] / 100);
            cellRight = cellRight + cellWidth;
            
            Cell cell = (Cell) row.getCell(i);
            RtfCell rtfCell = new RtfCell(this.document, this, cell);
            rtfCell.setCellRight(cellRight);
            rtfCell.setCellWidth(cellWidth);
            this.cells.add(rtfCell);
        }
    }
    
    private void importRow(PdfPRow row) {
        this.cells = new ArrayList();
        this.width = this.document.getDocumentHeader().getPageSetting().getPageWidth() - this.document.getDocumentHeader().getPageSetting().getMarginLeft() - this.document.getDocumentHeader().getPageSetting().getMarginRight();
        this.width = (int) (this.width * this.parentTable.getTableWidthPercent() / 100);
        
        int cellRight = 0;
        int cellWidth = 0;
        PdfPCell[] cells = row.getCells();
        for(int i = 0; i < cells.length; i++) {
            cellWidth = (int) (this.width * this.parentTable.getProportionalWidths()[i] / 100);
            cellRight = cellRight + cellWidth;
            
            PdfPCell cell = (PdfPCell) cells[i];
            RtfCell rtfCell = new RtfCell(this.document, this, cell);
            rtfCell.setCellRight(cellRight);
            rtfCell.setCellWidth(cellWidth);
            this.cells.add(rtfCell);
        }
    }
    
    protected void handleCellSpanning() {
        RtfCell deletedCell = new RtfCell(true);
        for(int i = 0; i < this.cells.size(); i++) {
            RtfCell rtfCell = (RtfCell) this.cells.get(i);
            if(rtfCell.getColspan() > 1) {
                int cSpan = rtfCell.getColspan();
                for(int j = i + 1; j < i + cSpan; j++) {
                    if(j < this.cells.size()) {
                        RtfCell rtfCellMerge = (RtfCell) this.cells.get(j);
                        rtfCell.setCellRight(rtfCell.getCellRight() + rtfCellMerge.getCellWidth());
                        rtfCell.setCellWidth(rtfCell.getCellWidth() + rtfCellMerge.getCellWidth());
                        this.cells.set(j, deletedCell);
                    }
                }
            }
            if(rtfCell.getRowspan() > 1) {
                ArrayList rows = this.parentTable.getRows();
                for(int j = 1; j < rtfCell.getRowspan(); j++) {
                    RtfRow mergeRow = (RtfRow) rows.get(this.rowNumber + j);
                    if(this.rowNumber + j < rows.size()) {
                        RtfCell rtfCellMerge = (RtfCell) mergeRow.getCells().get(i);
                        rtfCellMerge.setCellMergeChild(rtfCell);
                    }
                    if(rtfCell.getColspan() > 1) {
                        int cSpan = rtfCell.getColspan();
                        for(int k = i + 1; k < i + cSpan; k++) {
                            if(k < mergeRow.getCells().size()) {
                                mergeRow.getCells().set(k, deletedCell);
                            }
                        }
                    }
                }
            }
        }
    }

    
    protected void cleanRow() {
        int i = 0;
        while(i < this.cells.size()) {
            if(((RtfCell) this.cells.get(i)).isDeleted()) {
                this.cells.remove(i);
            } else {
                i++;
            }
        }
    }
    
    
    private void writeRowDefinition(final OutputStream result) throws IOException {
        result.write(ROW_BEGIN);
        this.document.outputDebugLinebreak(result);
        result.write(ROW_WIDTH_STYLE);
        result.write(ROW_WIDTH);
        result.write(intToByteArray(this.width));
        if(this.parentTable.getCellsFitToPage()) {
            result.write(ROW_KEEP_TOGETHER);
        }
        if(this.rowNumber <= this.parentTable.getHeaderRows()) {
            result.write(ROW_HEADER_ROW);
        }
        switch (this.parentTable.getAlignment()) {
            case Element.ALIGN_LEFT:
                result.write(ROW_ALIGN_LEFT);
                break;
            case Element.ALIGN_RIGHT:
                result.write(ROW_ALIGN_RIGHT);
                break;
            case Element.ALIGN_CENTER:
                result.write(ROW_ALIGN_CENTER);
                break;
            case Element.ALIGN_JUSTIFIED:
            case Element.ALIGN_JUSTIFIED_ALL:
                result.write(ROW_ALIGN_JUSTIFIED);
                break;
        }
        result.write(ROW_GRAPH);
        RtfBorderGroup borders =this.parentTable.getBorders();
        if(borders != null) {
            borders.writeContent(result);
        }
        
        if(this.parentTable.getCellSpacing() > 0) {
            result.write(ROW_CELL_SPACING_LEFT);
            result.write(intToByteArray((int) (this.parentTable.getCellSpacing() / 2)));
            result.write(ROW_CELL_SPACING_LEFT_STYLE);
            result.write(ROW_CELL_SPACING_TOP);
            result.write(intToByteArray((int) (this.parentTable.getCellSpacing() / 2)));
            result.write(ROW_CELL_SPACING_TOP_STYLE);
            result.write(ROW_CELL_SPACING_RIGHT);
            result.write(intToByteArray((int) (this.parentTable.getCellSpacing() / 2)));
            result.write(ROW_CELL_SPACING_RIGHT_STYLE);
            result.write(ROW_CELL_SPACING_BOTTOM);
            result.write(intToByteArray((int) (this.parentTable.getCellSpacing() / 2)));
            result.write(ROW_CELL_SPACING_BOTTOM_STYLE);
        }
        
        result.write(ROW_CELL_PADDING_LEFT);
        result.write(intToByteArray((int) (this.parentTable.getCellPadding() / 2)));
        result.write(ROW_CELL_PADDING_RIGHT);
        result.write(intToByteArray((int) (this.parentTable.getCellPadding() / 2)));
        result.write(ROW_CELL_PADDING_LEFT_STYLE);
        result.write(ROW_CELL_PADDING_RIGHT_STYLE);
        
        this.document.outputDebugLinebreak(result);
        
        for(int i = 0; i < this.cells.size(); i++) {
            RtfCell rtfCell = (RtfCell) this.cells.get(i);
            rtfCell.writeDefinition(result);
        }        
    }
    
        
    public void writeContent(final OutputStream result) throws IOException
    {
        writeRowDefinition(result);
        
        for(int i = 0; i < this.cells.size(); i++) {
            RtfCell rtfCell = (RtfCell) this.cells.get(i);
            rtfCell.writeContent(result);
        }

        result.write(DELIMITER);

        if(this.document.getDocumentSettings().isOutputTableRowDefinitionAfter()) {
            writeRowDefinition(result);
        }

        result.write(ROW_END);
        this.document.outputDebugLinebreak(result);
    }        
    
    
    protected RtfTable getParentTable() {
        return this.parentTable;
    }
    
    
    protected ArrayList getCells() {
        return this.cells;
    }
}
