package net.sourceforge.pmd.util.viewer.gui;


import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.util.viewer.model.ViewerModel;
import net.sourceforge.pmd.util.viewer.model.ViewerModelEvent;
import net.sourceforge.pmd.util.viewer.model.ViewerModelListener;
import net.sourceforge.pmd.util.viewer.util.NLS;




public class SourceCodePanel extends JPanel implements ViewerModelListener {
	
    private ViewerModel model;
    private JTextArea sourceCodeArea;

    private static final Color highlightColor = new Color(79, 237, 111);
    
    public SourceCodePanel(ViewerModel model) {
        this.model = model;
        init();
    }

    private void init() {
        model.addViewerModelListener(this);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), NLS.nls("SOURCE.PANEL.TITLE")));
        setLayout(new BorderLayout());
        sourceCodeArea = new JTextArea();
        add(new JScrollPane(sourceCodeArea), BorderLayout.CENTER);
    }

    
    public String getSourceCode() {
        return sourceCodeArea.getText();
    }

    
    public void viewerModelChanged(ViewerModelEvent e) {
        if (e.getReason() == ViewerModelEvent.NODE_SELECTED) {
            final Node node = (Node) e.getParameter();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        sourceCodeArea.getHighlighter().removeAllHighlights();
                        if (node == null) {
                            return;
                        }
                        int startOffset =
                                sourceCodeArea.getLineStartOffset(node.getBeginLine() - 1) +
                                node.getBeginColumn() - 1;
                        int end =
                                sourceCodeArea.getLineStartOffset(node.getEndLine() - 1) +
                                node.getEndColumn();
                        sourceCodeArea.getHighlighter().addHighlight(startOffset, end,
                                new DefaultHighlighter.DefaultHighlightPainter(highlightColor));
                        sourceCodeArea.moveCaretPosition(startOffset);
                    } catch (BadLocationException exc) {
                        throw new IllegalStateException(exc.getMessage());
                    }
                }
            });
        }
    }
}

