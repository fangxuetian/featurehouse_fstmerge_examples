package net.sourceforge.pmd.util.viewer.gui;

import net.sourceforge.pmd.ast.SimpleNode;
import net.sourceforge.pmd.util.viewer.model.ViewerModel;
import net.sourceforge.pmd.util.viewer.model.ViewerModelEvent;
import net.sourceforge.pmd.util.viewer.model.ViewerModelListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.util.Vector;


public class EvaluationResultsPanel extends JPanel implements ViewerModelListener {
    private ViewerModel model;
    private JList list;

    
    public EvaluationResultsPanel(ViewerModel model) {
        super(new BorderLayout());

        this.model = model;

        init();
    }

    private void init() {
        model.addViewerModelListener(this);

        list = new JList();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (list.getSelectedValue() != null) {
                    model.selectNode((SimpleNode) list.getSelectedValue(), EvaluationResultsPanel.this);
                }
            }
        });

        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    
    public void viewerModelChanged(ViewerModelEvent e) {
        switch (e.getReason()) {
            case ViewerModelEvent.PATH_EXPRESSION_EVALUATED:

                if (e.getSource() != this) {
                    list.setListData(new Vector(model.getLastEvaluationResults()));
                }

                break;

            case ViewerModelEvent.CODE_RECOMPILED:
                list.setListData(new Vector(0));

                break;
        }
    }
}
