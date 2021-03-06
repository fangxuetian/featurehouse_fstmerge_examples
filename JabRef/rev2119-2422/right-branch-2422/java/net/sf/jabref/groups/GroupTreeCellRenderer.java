

package net.sf.jabref.groups;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;

import net.sf.jabref.*;


public class GroupTreeCellRenderer extends DefaultTreeCellRenderer {
    
    protected Object highlight1Cell = null;
    protected Object[] highlight2Cells = null;
    protected Object[] highlight3Cells = null;
    protected Object highlightBorderCell = null;

    public static ImageIcon
      groupRefiningIcon = GUIGlobals.getImage("groupRefining"),
      groupIncludingIcon = GUIGlobals.getImage("groupIncluding"),
      groupRegularIcon = null;


    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {
        if (value == highlight1Cell)
            selected = true; 
        Component c = super.getTreeCellRendererComponent(tree, value, selected,
                expanded, leaf, row, hasFocus);
        
        
        if (!(value instanceof GroupTreeNode))
            return c;
        AbstractGroup group = ((GroupTreeNode) value).getGroup();
        if (group == null || !(c instanceof JLabel))
            return c; 
        JLabel label = (JLabel) c;
        if (highlightBorderCell != null && highlightBorderCell == value)
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        else
            label.setBorder(BorderFactory.createEmptyBorder());
        boolean italics = Globals.prefs.getBoolean("groupShowDynamic")
        && group.isDynamic();
        boolean red = false;
        if (highlight2Cells != null) {
            for (int i = 0; i < highlight2Cells.length; ++i) {
                if (highlight2Cells[i] == value) {
                    
                    red = true;
                    break;
                }
            }
        }
        boolean underline = false;
        if (highlight3Cells != null) {
            for (int i = 0; i < highlight3Cells.length; ++i) {
                if (highlight3Cells[i] == value) {
                    underline = true;
                    break;
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        if (red)
            sb.append("<font color=\"#FF0000\">");
        if (underline)
            sb.append("<u>");
        if (italics)
            sb.append("<i>");
        sb.append(Util.quoteForHTML(group.getName()));
        if (italics)
            sb.append("</i>");
        if (underline)
            sb.append("</u>");
        if (red)
            sb.append("</font>");
        sb.append("</html>");
        final String text = sb.toString();
        if (!label.getText().equals(text))
            label.setText(text);
        label.setToolTipText("<html>" + group.getShortDescription() + "</html>");
        if (Globals.prefs.getBoolean("groupShowIcons")) {
            switch (group.getHierarchicalContext()) {
            case AbstractGroup.REFINING:
                if (label.getIcon() != groupRefiningIcon)
                    label.setIcon(groupRefiningIcon);
                break;
            case AbstractGroup.INCLUDING:
                if (label.getIcon() != groupIncludingIcon)
                    label.setIcon(groupIncludingIcon);
                break;
            default:
                if (label.getIcon() != groupRegularIcon)
                    label.setIcon(groupRegularIcon);
                break;
            }
        } else {
            label.setIcon(null);
        }
        return c;
    }

    
    void setHighlight1Cell(Object cell) {
        this.highlight1Cell = cell;
    }

    
    void setHighlight2Cells(Object[] cells) {
        this.highlight2Cells = cells;
    }

    
    void setHighlight3Cells(Object[] cells) {
        this.highlight3Cells = cells;
    }

    
    void setHighlightBorderCell(Object highlightBorderCell) {
        this.highlightBorderCell = highlightBorderCell;
    }
}
