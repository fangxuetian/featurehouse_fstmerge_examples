
package net.sf.jabref.groups;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.event.CaretListener;

import net.sf.jabref.*;
import net.sf.jabref.undo.NamedCompound;


class AutoGroupDialog extends JDialog implements CaretListener {
    JTextField remove = new JTextField(60), field = new JTextField(60),
            deliminator = new JTextField(60);
    JLabel nf = new JLabel(Globals.lang("Field to group by") + ":"),
            nr = new JLabel(Globals.lang("Characters to ignore") + ":");
    JCheckBox nd = new JCheckBox(Globals.lang(
    		"Use the following delimiter character(s)")
            + ":"); 
    JButton ok = new JButton(Globals.lang("Ok")), cancel = new JButton(Globals
            .lang("Cancel"));
    JPanel main = new JPanel(), opt = new JPanel();
    private boolean ok_pressed = false;
    private GroupTreeNode m_groupsRoot;
    private JabRefFrame frame;
    private BasePanel panel;
    private GroupSelector gs;
    private String oldRemove, oldField;
    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints con = new GridBagConstraints();

    
    public AutoGroupDialog(JabRefFrame jabrefFrame, BasePanel basePanel,
            GroupSelector groupSelector, GroupTreeNode groupsRoot,
            String defaultField, String defaultRemove, String defaultDeliminator) {
        super(jabrefFrame, Globals.lang("Automatically create groups"), true);
        frame = jabrefFrame;
        gs = groupSelector;
        panel = basePanel;
        m_groupsRoot = groupsRoot;
        field.setText(defaultField);
        remove.setText(defaultRemove);
        deliminator.setText(defaultDeliminator);
        nd.setSelected(true);
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ok_pressed = true;
                dispose();

                GroupTreeNode autoGroupsRoot = new GroupTreeNode(
                        new ExplicitGroup(Globals.lang("Automatically created groups"),
                        		AbstractGroup.INCLUDING));
                HashSet hs = null;
                if (nd.isSelected()) {
                    hs = Util
                            .findDeliminatedWordsInField(panel.getDatabase(),
                                    field().toLowerCase().trim(), deliminator
                                            .getText());
                } else {
                    hs = Util.findAllWordsInField(panel.getDatabase(), field()
                            .toLowerCase().trim(), remove());
                }
                Iterator i = hs.iterator();
                while (i.hasNext()) {
                    String keyword = i.next().toString();
                    KeywordGroup group = new KeywordGroup(keyword, field(),
                            keyword, false, false, AbstractGroup.INDEPENDENT);
                    autoGroupsRoot.add(new GroupTreeNode(group));
                }

                m_groupsRoot.add(autoGroupsRoot);
                NamedCompound ce = new NamedCompound(Globals
                        .lang("Autogenerate groups"));
                UndoableAddOrRemoveGroup undo = new UndoableAddOrRemoveGroup(
                        gs, m_groupsRoot, autoGroupsRoot,
                        UndoableAddOrRemoveGroup.ADD_NODE);
                undo.setRevalidate(true);
                ce.addEdit(undo);

                panel.markBaseChanged(); 
                gs.revalidateGroups();
                frame.output(Globals.lang("Created groups."));
                ce.end();
                panel.undoManager.addEdit(ce);
            }
        };
        remove.addActionListener(okListener);
        field.addActionListener(okListener);
        field.addCaretListener(this);
        AbstractAction cancelAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };
        cancel.addActionListener(cancelAction);
        ok.addActionListener(okListener);
        
        ActionMap am = main.getActionMap();
        InputMap im = main.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(frame.prefs().getKey("Close dialog"), "close");
        am.put("close", cancelAction);
        
        main.setLayout(gbl);
        opt.setLayout(gbl);
        main.setBorder(BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(), Globals.lang("Group properties")));
        
        con.weightx = 0;
        con.gridwidth = 1;
        con.insets = new Insets(3, 5, 3, 5);
        con.anchor = GridBagConstraints.EAST;
        con.fill = GridBagConstraints.NONE;
        con.gridx = 0;
        con.gridy = 0;
        gbl.setConstraints(nf, con);
        main.add(nf);
        con.gridy = 1;
        gbl.setConstraints(nr, con);
        main.add(nr);
        con.gridy = 2;
        gbl.setConstraints(nd, con);
        main.add(nd);
        con.weightx = 1;
        con.anchor = GridBagConstraints.WEST;
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridy = 0;
        con.gridx = 1;
        gbl.setConstraints(field, con);
        main.add(field);
        con.gridy = 1;
        gbl.setConstraints(remove, con);
        main.add(remove);
        con.gridy = 2;
        gbl.setConstraints(deliminator, con);
        main.add(deliminator);
        
        con.gridx = GridBagConstraints.RELATIVE;
        con.gridy = GridBagConstraints.RELATIVE;
        con.weightx = 1;
        con.gridwidth = 1;
        con.anchor = GridBagConstraints.EAST;
        con.fill = GridBagConstraints.NONE;
        gbl.setConstraints(ok, con);
        opt.add(ok);
        con.anchor = GridBagConstraints.WEST;
        con.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(cancel, con);
        opt.add(cancel);
        getContentPane().add(main, BorderLayout.CENTER);
        getContentPane().add(opt, BorderLayout.SOUTH);
        
        updateComponents();
        setSize(400, 200);
        Util.placeDialog(this, frame);
    }

    public boolean okPressed() {
        return ok_pressed;
    }

    public String oldField() {
        return oldField;
    }

    public String oldRemove() {
        return oldRemove;
    }

    public String field() {
        return field.getText();
    }

    public String remove() {
        return remove.getText();
    }

    public void caretUpdate(CaretEvent e) {
        updateComponents();
    }
    
    protected void updateComponents() {
        String groupField = field.getText().trim();
        ok.setEnabled(groupField.matches("\\w+"));
    }
}
