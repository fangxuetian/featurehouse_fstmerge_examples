package net.sf.jabref.external;

import net.sf.jabref.Globals;
import net.sf.jabref.GUIGlobals;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

import com.jgoodies.forms.builder.ButtonBarBuilder;


public class IconSelection extends JDialog {

    JList icons;
    List<String> iconKeys;
    DefaultListModel listModel;
    JButton ok = new JButton(Globals.lang("Ok")),
        cancel = new JButton(Globals.lang("Cancel"));
    private boolean okPressed = false;
    private int selected = -1;
    private JDialog parent;

    public IconSelection(JDialog parent, String initialSelection) {
        super(parent, Globals.lang("Select icon"), true);
        this.parent = parent;
        init(initialSelection);
    }

    public void setVisible(boolean visible) {
        if (visible) {
            okPressed = false;
            selected = -1;
        }
        super.setVisible(visible);
    }

    
    public boolean isOkPressed() {
        return okPressed;
    }

    public String getSelectedIconKey() {
        if (selected >= 0)
            return iconKeys.get(selected);
        else
            return null;
    }

    private void init(String initialSelection) {
        int initSelIndex = -1;
        iconKeys = new ArrayList<String>();
        Map icns = GUIGlobals.getAllIcons();
        HashSet<ImageIcon> iconSet = new LinkedHashSet<ImageIcon>();
        for (Iterator i=icns.keySet().iterator(); i.hasNext();) {
            String key = (String)i.next();
            ImageIcon icon = GUIGlobals.getImage(key);
            if (!iconSet.contains(icon)) {
                iconKeys.add(key);
                if (key.equals(initialSelection))
                    initSelIndex = iconKeys.size()-1;
            }
            iconSet.add(icon);

        }

        listModel = new DefaultListModel();
        icons = new JList(listModel);
        for (Iterator<ImageIcon> iterator = iconSet.iterator(); iterator.hasNext();) {
            listModel.addElement(new JLabel(iterator.next()));
        }
        class MyRenderer implements ListCellRenderer {
            JLabel comp = new JLabel();
            public MyRenderer() {
                comp.setOpaque(true);
                comp.setIconTextGap(0);
                comp.setHorizontalAlignment(JLabel.CENTER);
            }

            public Component getListCellRendererComponent(JList list, Object value, int i,
                                                          boolean isSelected, 
                                                          boolean hasFocus) {
                comp.setText(null);
                comp.setIcon(((JLabel)value).getIcon());
                if (isSelected) {
                    comp.setBackground(list.getSelectionBackground());
                    comp.setForeground(list.getSelectionForeground());
                    comp.setBorder(BorderFactory.createEtchedBorder());
                } else {
                    comp.setBackground(list.getBackground());
                    comp.setForeground(list.getForeground());
                    comp.setBorder(null);
                }

                return comp;
            }
        }

        if (initSelIndex >= 0)
            icons.setSelectedIndex(initSelIndex);
        icons.setCellRenderer(new MyRenderer());
        icons.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        icons.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        ButtonBarBuilder bb = new ButtonBarBuilder();
        bb.addGlue();
        bb.addGridded(ok);
        bb.addGridded(cancel);
        bb.addGlue();
        bb.getPanel().setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                okPressed = true;
                if (icons.getSelectedValue() != null)
                    selected = icons.getSelectedIndex(); 
                dispose();
            }
        });
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                okPressed = false;
                dispose();
            }
        });
        icons.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {

                }
            }
        });

        getContentPane().add(new JScrollPane(icons), BorderLayout.CENTER);
        getContentPane().add(bb.getPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }
}
