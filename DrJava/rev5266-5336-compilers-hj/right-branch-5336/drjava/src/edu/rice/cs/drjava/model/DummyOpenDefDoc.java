
package edu.rice.cs.drjava.model;

import java.util.ArrayList;
import java.util.List;

import java.io.*;

import java.awt.print.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;

import edu.rice.cs.drjava.model.debug.Breakpoint;
import edu.rice.cs.drjava.model.definitions.*;
import edu.rice.cs.drjava.model.definitions.reducedmodel.*;
import edu.rice.cs.drjava.model.FinalizationListener;
import edu.rice.cs.drjava.model.definitions.ClassNameNotFoundException;
import edu.rice.cs.drjava.model.definitions.indent.Indenter;


import edu.rice.cs.util.OperationCanceledException;
import edu.rice.cs.util.docnavigation.*;
import edu.rice.cs.util.text.SwingDocument;

public class DummyOpenDefDoc implements OpenDefinitionsDocument { 
  



  
  public int compareTo(OpenDefinitionsDocument d) {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public boolean modifiedOnDisk() { throw new UnsupportedOperationException("Dummy method"); }
  
  public void addBrowserRegion(BrowserDocumentRegion r) {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public void removeBrowserRegion(BrowserDocumentRegion r) {
    throw new UnsupportedOperationException("Dummy method");
  }
  public boolean saveFile(FileSaveSelector com) throws IOException {
    throw new UnsupportedOperationException("Dummy method");
  }
  



  
  public boolean canAbandonFile() { throw new UnsupportedOperationException("Dummy method"); }
  
  public boolean quitFile() { throw new UnsupportedOperationException("Dummy method"); }
  
  public void setCurrentLocation(int location) { throw new UnsupportedOperationException("Dummy method"); }
  
  public DefinitionsDocument getDocument() { throw new UnsupportedOperationException("Dummy method"); }
  
  public boolean isModifiedSinceSave() { throw new UnsupportedOperationException("Dummy method"); }
  

  
  public int balanceForward() { throw new UnsupportedOperationException("Dummy method"); }
  
  public int balanceBackward() { throw new UnsupportedOperationException("Dummy method"); }
  
  public File getFile() throws FileMovedException { throw new UnsupportedOperationException("Dummy method"); }
  
  public File getRawFile() { return null; }
  
  public File getParentDirectory() { throw new UnsupportedOperationException("Dummy method"); }
  
  public boolean fileExists() { throw new UnsupportedOperationException("Dummy method"); }
  
  public boolean verifyExists() { throw new UnsupportedOperationException("Dummy method"); }
  
  public void cleanUpPrintJob() { throw new UnsupportedOperationException("Dummy method"); }
  
  public String getFirstTopLevelClassName() throws ClassNameNotFoundException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public void startCompile() throws IOException { throw new UnsupportedOperationException("Dummy method"); }
  
  public void runMain(String className) throws IOException { throw new UnsupportedOperationException("Dummy method"); }

  public void runApplet(String className) throws IOException { throw new UnsupportedOperationException("Dummy method"); }

  public void runSmart(String className) throws IOException { throw new UnsupportedOperationException("Dummy method"); }
  
  public boolean revertIfModifiedOnDisk() throws IOException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public Pageable getPageable() throws IllegalStateException {
    throw new UnsupportedOperationException("Dummy method");
  }

  public int gotoLine(int line) {
    throw new UnsupportedOperationException("Dummy method");
  }
 
  public void print() throws PrinterException, BadLocationException, FileMovedException {
    throw new UnsupportedOperationException("Dummy method");
  }
  public void removeFromDebugger() { throw new UnsupportedOperationException("Dummy method"); }

  public RegionManager<Breakpoint> getBreakpointManager() { throw new UnsupportedOperationException("Dummy method"); }

  public RegionManager<MovingDocumentRegion> getBookmarkManager() { throw new UnsupportedOperationException("Dummy method"); }












  
  public void clearBrowserRegions() { throw new UnsupportedOperationException("Dummy method"); }
  
  public boolean isReady() { throw new UnsupportedOperationException("Dummy method"); }

  public boolean isUntitled() { throw new UnsupportedOperationException("Dummy method"); }
  
  public boolean isSourceFile() { throw new UnsupportedOperationException("Dummy method"); }
  
  public boolean isEmpty() { throw new UnsupportedOperationException("Dummy method"); }
  
  public File getSourceRoot() throws InvalidPackageException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public String getFileName() { throw new UnsupportedOperationException("Dummy method"); }
  public String getCanonicalPath() { throw new UnsupportedOperationException("Dummy method"); }
  public String getCompletePath() { throw new UnsupportedOperationException("Dummy method"); }
  public String getName() { return getFileName(); }
 
  public void startJUnit() throws ClassNotFoundException, IOException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public void generateJavadoc(FileSaveSelector saver) throws IOException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public String getPackageName() { throw new UnsupportedOperationException("Dummy method"); }
  public void setPackage(String s) { throw new UnsupportedOperationException("Dummy method"); }
  public String getPackageNameFromDocument() { throw new UnsupportedOperationException("Dummy method"); }
  
  public String getEnclosingClassName(int pos, boolean qual) throws BadLocationException, ClassNameNotFoundException {
    throw new UnsupportedOperationException("Dummy method");
  }

  public boolean saveFileAs(FileSaveSelector com) throws IOException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public void preparePrintJob() throws BadLocationException, FileMovedException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public void revertFile() throws IOException { throw new UnsupportedOperationException("Dummy method"); }
  public boolean checkIfClassFileInSync() { throw new UnsupportedOperationException("Dummy method"); }
  public void documentSaved() { throw new UnsupportedOperationException("Dummy method"); }
  public void documentModified() { throw new UnsupportedOperationException("Dummy method"); }
  public void documentReset() { throw new UnsupportedOperationException("Dummy method"); }
  public int getCurrentLocation() { throw new UnsupportedOperationException("Dummy method"); }
  public INavigatorItem getIDoc() { throw new UnsupportedOperationException("Dummy method"); }
  public void resetModification() { throw new UnsupportedOperationException("Dummy method"); }
  public long getTimestamp() { throw new UnsupportedOperationException("Dummy method"); }
  public void updateModifiedSinceSave() { throw new UnsupportedOperationException("Dummy method"); }
  public void setFile(File file) { throw new UnsupportedOperationException("Dummy method"); }
  public void close() { } 
  public boolean inProjectPath() { return false; }
  public boolean inNewProjectPath(File f) { throw new UnsupportedOperationException("Dummy method"); }
  public boolean inProject() { return false; }
  public boolean isAuxiliaryFile() { return false; }
  public int _getLineStartPos(int pos) { throw new UnsupportedOperationException("Dummy method"); }
  public int _getLineEndPos(int pos) { throw new UnsupportedOperationException("Dummy method"); }
  
  
  
  protected SwingDocument _defDoc = new SwingDocument();
  public void addDocumentListener(DocumentListener listener) { _defDoc.addDocumentListener(listener); }
  
  public void addUndoableEditListener(UndoableEditListener listener) {
    _defDoc.addUndoableEditListener(listener);
  }
  
  public Position createPosition(int offs) throws BadLocationException {
    return _defDoc.createPosition(offs);
  }
  
  public Position createUnwrappedPosition(int offs) throws BadLocationException {
    return _defDoc.createUnwrappedPosition(offs);
  }
  
  public Element getDefaultRootElement() { return _defDoc.getDefaultRootElement(); }
  
  
  public Position getStartPosition() { 
    throw new UnsupportedOperationException("DummyOpenDefDoc does not support getStartPosition()"); 
  }
  public Position getEndPosition() { 
    throw new UnsupportedOperationException("ConcreteOpenDefDoc does not support getEndPosition()"); 
  }
  
  public int getLength() { return _defDoc.getLength(); }
  public Object getProperty(Object key) { return _defDoc.getProperty(key); }
  public Element[] getRootElements() { return _defDoc.getRootElements(); }

  
  public String getText(int offset, int length) throws BadLocationException {
    return _defDoc.getText(offset, length);
  }
  
  public void getText(int offset, int length, Segment txt) throws BadLocationException {
    _defDoc.getText(offset, length, txt);
  }
  
  public void insertString(int offset, String str, AttributeSet set) throws BadLocationException {
    _defDoc.insertString(offset, str, set);
  }
  
  public void append(String str, AttributeSet set) { _defDoc.append(str, set); }
  public void append(String str, Style style) { _defDoc.append(str, style); }
  public void append(String str) { _defDoc.append(str); }
  public void putProperty(Object key, Object value) { _defDoc.putProperty(key, value); }
  public void remove(int offs, int len) throws BadLocationException { _defDoc.remove(offs, len); }
  public void removeDocumentListener(DocumentListener listener) { _defDoc.removeDocumentListener(listener); }
  
  public void removeUndoableEditListener(UndoableEditListener listener) {
    _defDoc.removeUndoableEditListener(listener);
  }
  
  public void render(Runnable r) { _defDoc.render(r); }
  
  
  
  
  public CompoundUndoManager getUndoManager() { throw new UnsupportedOperationException("Dummy method"); }
  public int commentLines(int selStart, int selEnd) { throw new UnsupportedOperationException("Dummy method"); }
  public int uncommentLines(int selStart, int selEnd) { throw new UnsupportedOperationException("Dummy method"); }
  public void indentLines(int selStart, int selEnd) { throw new UnsupportedOperationException("Dummy method"); }
  public int getCurrentLine() { throw new UnsupportedOperationException("Dummy method"); }
  public int getCurrentCol() { throw new UnsupportedOperationException("Dummy method"); }
  public boolean getClassFileInSync() { throw new UnsupportedOperationException("Dummy method"); }
  public void setClassFileInSync(boolean val) { throw new UnsupportedOperationException("Dummy method"); }
 
  public int getIntelligentBeginLinePos(int currPos) throws BadLocationException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public int _getOffset(int lineNum) { throw new UnsupportedOperationException("Dummy method"); }
  
  public String getQualifiedClassName() throws ClassNameNotFoundException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public String getQualifiedClassName(int pos) throws ClassNameNotFoundException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public String getLexiName() { throw new UnsupportedOperationException("Dummy method"); }
  public ReducedModelState getStateAtCurrent() { throw new UnsupportedOperationException("Dummy method"); }
  public void resetUndoManager() { throw new UnsupportedOperationException("Dummy method"); }
  public File getCachedClassFile() { throw new UnsupportedOperationException("Dummy method"); }
  public void setCachedClassFile(File f) { throw new UnsupportedOperationException("Dummy method"); }
  
  public DocumentListener[] getDocumentListeners() { throw new UnsupportedOperationException("Dummy method"); }
  public UndoableEditListener[] getUndoableEditListeners() { throw new UnsupportedOperationException("Dummy method"); }
  
  public void addFinalizationListener(FinalizationListener<DefinitionsDocument> fl) {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public List<FinalizationListener<DefinitionsDocument>> getFinalizationListeners() {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public boolean undoManagerCanUndo() { throw new UnsupportedOperationException("Dummy method"); }
  public boolean undoManagerCanRedo() { throw new UnsupportedOperationException("Dummy method"); }
  
  
  
  public Font getFont(AttributeSet attr) { throw new UnsupportedOperationException("Dummy method"); }
  public Color getBackground(AttributeSet attr) { throw new UnsupportedOperationException("Dummy method"); }
  public Color getForeground(AttributeSet attr) { throw new UnsupportedOperationException("Dummy method"); }
  public Element getCharacterElement(int pos) { throw new UnsupportedOperationException("Dummy method"); }
  public Element getParagraphElement(int pos) { throw new UnsupportedOperationException("Dummy method"); }
  public Style getLogicalStyle(int p) { throw new UnsupportedOperationException("Dummy method"); }
  public void setLogicalStyle(int pos, Style s) { throw new UnsupportedOperationException("Dummy method"); }
  
  public void setCharacterAttributes(int offset, int length, AttributeSet s, boolean replace) {
    throw new UnsupportedOperationException("Dummy method");
  }    
  
  public void setParagraphAttributes(int offset, int length, AttributeSet s, boolean replace) {
    throw new UnsupportedOperationException("Dummy method");
  }    
  
  public Style getStyle(String nm) { throw new UnsupportedOperationException("Dummy method"); }
  public void removeStyle(String nm) { throw new UnsupportedOperationException("Dummy method"); }
  public Style addStyle(String nm, Style parent) { throw new UnsupportedOperationException("Dummy method"); }
  
  
  




  
  public int findPrevEnclosingBrace(int pos, char opening, char closing) throws BadLocationException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public int findNextEnclosingBrace(int pos, char opening, char closing) throws BadLocationException {
    throw new UnsupportedOperationException("Dummy method");
  }
  



  
  public int getFirstNonWSCharPos(int pos) throws BadLocationException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public int getFirstNonWSCharPos(int pos, boolean acceptComments) throws BadLocationException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public int getFirstNonWSCharPos (int pos, char[] whitespace, boolean acceptComments) throws BadLocationException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public int _getLineFirstCharPos(int pos) throws BadLocationException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public int findCharOnLine(int pos, char findChar) { throw new UnsupportedOperationException("Dummy method"); }
  
  public int _getIndentOfCurrStmt(int pos) throws BadLocationException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public int _getIndentOfCurrStmt(int pos, char[] delims) throws BadLocationException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public int _getIndentOfCurrStmt(int pos, char[] delims, char[] whitespace) throws BadLocationException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public void indentLines(int selStart, int selEnd, Indenter.IndentReason reason, ProgressMonitor pm) throws OperationCanceledException {
    throw new UnsupportedOperationException("Dummy method");
  }
  



  



  
  public int findPrevDelimiter(int pos, char[] delims) throws BadLocationException {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public int findPrevDelimiter(int pos, char[] delims, boolean skipParenPhrases) throws BadLocationException {
    throw new UnsupportedOperationException("Dummy method");
  }
  



  public void move(int dist) { throw new UnsupportedOperationException("Dummy method"); }

  public ArrayList<HighlightStatus> getHighlightStatus(int start, int end) {
    throw new UnsupportedOperationException("Dummy method");
  }
  
  public void setIndent(int indent) { throw new UnsupportedOperationException("Dummy method"); }
  public int getIndent() { throw new UnsupportedOperationException("Dummy method"); }
  public int getInitialVerticalScroll() { throw new UnsupportedOperationException("Dummy method"); }
  public int getInitialHorizontalScroll() { throw new UnsupportedOperationException("Dummy method"); }
  public int getInitialSelectionStart() { throw new UnsupportedOperationException("Dummy method"); }
  public int getInitialSelectionEnd() { throw new UnsupportedOperationException("Dummy method"); }
  public String getText() { throw new UnsupportedOperationException("Dummy method"); }
  public void clear() { throw new UnsupportedOperationException("Dummy method"); }
  
  public ReducedModelControl getReduced() { throw new UnsupportedOperationException("Dummy method"); }
 
  
  public int getNumberOfLines() { return 0; }
  
  
  public boolean isShadowed(int pos) { return false; }
  
  public int getLineOfOffset(int offset) { throw new UnsupportedOperationException("Dummy method"); }
  public int getOffsetOfLine(int line) { throw new UnsupportedOperationException("Dummy method"); } 
  
  public int getCaretPosition() { throw new UnsupportedOperationException("Dummy method"); }

  public boolean containsClassOrInterfaceOrEnum() throws BadLocationException { throw new UnsupportedOperationException("Dummy method"); }
}
