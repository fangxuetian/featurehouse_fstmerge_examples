package net.sourceforge.pmd.util.viewer.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.util.viewer.gui.menu.ASTNodePopupMenu;
import net.sourceforge.pmd.util.viewer.model.ASTModel;
import net.sourceforge.pmd.util.viewer.model.ViewerModel;
import net.sourceforge.pmd.util.viewer.model.ViewerModelEvent;
import net.sourceforge.pmd.util.viewer.model.ViewerModelListener;
import net.sourceforge.pmd.util.viewer.util.NLS;



public class ASTPanel extends JPanel implements ViewerModelListener, TreeSelectionListener {
    private ViewerModel model;
    private JTree tree;

    
    public ASTPanel(ViewerModel model) {
        this.model = model;
        init();
    }

    private void init() {
        model.addViewerModelListener(this);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), NLS.nls("AST.PANEL.TITLE")));
        setLayout(new BorderLayout());
        tree = new JTree((TreeNode) null);
        tree.addTreeSelectionListener(this);
        tree.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    TreePath path = tree.getClosestPathForLocation(e.getX(), e.getY());
                    tree.setSelectionPath(path);
                    JPopupMenu menu = new ASTNodePopupMenu(model, (Node) path.getLastPathComponent());
                    menu.show(tree, e.getX(), e.getY());
                }
            }
        });

        add(new JScrollPane(tree), BorderLayout.CENTER);
    }

    
    public void viewerModelChanged(ViewerModelEvent e) {
        switch (e.getReason()) {
            case ViewerModelEvent.CODE_RECOMPILED:
                tree.setModel(new ASTModel(model.getRootNode()));
                break;
            case ViewerModelEvent.NODE_SELECTED:
                if (e.getSource() != this) {
                    LinkedList<Node> list = new LinkedList<Node>();
                    for (Node n = (Node) e.getParameter(); n != null; n = n.jjtGetParent()) {
                        list.addFirst(n);
                    }
                    TreePath path = new TreePath(list.toArray());
                    tree.setSelectionPath(path);
                    tree.scrollPathToVisible(path);
                }
                break;
            default:
        	
        	break;
        }
    }

    
    public void valueChanged(TreeSelectionEvent e) {
        model.selectNode((Node) e.getNewLeadSelectionPath().getLastPathComponent(), this);
    }
}
