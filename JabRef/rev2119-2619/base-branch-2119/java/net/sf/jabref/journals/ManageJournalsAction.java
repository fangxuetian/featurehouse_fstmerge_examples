package net.sf.jabref.journals;

import net.sf.jabref.JabRefFrame;
import net.sf.jabref.Globals;
import net.sf.jabref.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class ManageJournalsAction extends AbstractAction {

    JabRefFrame frame;

    public ManageJournalsAction(JabRefFrame frame) {
        super(Globals.lang("Manage journal abbreviations"));
        this.frame = frame;
    }
    public void actionPerformed(ActionEvent actionEvent) {
        ManageJournalsPanel panel = new ManageJournalsPanel(frame);
        Util.placeDialog(panel.getDialog(), frame);
        panel.setValues();
        panel.getDialog().setVisible(true);
    }
}
