package net.sf.jabref.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;

import net.sf.jabref.*;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;


public class DatabasePropertiesDialog extends JDialog {

    MetaData metaData;
    BasePanel panel = null;
    JComboBox encoding;
    JButton ok, cancel;
    JTextField fileDir = new JTextField(40),
            pdfDir = new JTextField(40), psDir = new JTextField(40);
    String oldFileVal="", oldPdfVal="", oldPsVal=""; 

    public DatabasePropertiesDialog(JFrame parent) {
        super(parent, Globals.lang("Database properties"), false);
        encoding = new JComboBox(Globals.ENCODINGS);
        ok = new JButton(Globals.lang("Ok"));
        cancel = new JButton(Globals.lang("Cancel"));
        init(parent);
    }

    public void setPanel(BasePanel panel) {
        this.panel = panel;
        this.metaData = panel.metaData();
    }

    public final void init(JFrame parent) {

        JButton browseFile = new JButton(Globals.lang("Browse"));
        JButton browsePdf = new JButton(Globals.lang("Browse"));
        JButton browsePs = new JButton(Globals.lang("Browse"));
        browseFile.addActionListener(new BrowseAction(parent, fileDir, true));
        browsePdf.addActionListener(new BrowseAction(parent, pdfDir, true));
        browsePs.addActionListener(new BrowseAction(parent, psDir, true));

        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("left:pref, 4dlu, left:pref, 4dlu, fill:pref", ""));
        builder.getPanel().setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        builder.append(Globals.lang("Database encoding"));
        builder.append(encoding);
        builder.nextLine();
        builder.appendSeparator(Globals.lang("Override default file directories"));
        builder.nextLine();
        builder.append(Globals.lang("File directory"));
        builder.append(fileDir);
        builder.append(browseFile);
        builder.nextLine();
        builder.append(Globals.lang("PDF directory"));
        builder.append(pdfDir);
        builder.append(browsePdf);
        builder.nextLine();
        builder.append(Globals.lang("PS directory"));
        builder.append(psDir);
        builder.append(browsePs);
        ButtonBarBuilder bb = new ButtonBarBuilder();
        bb.addGlue();
        bb.addGridded(ok);
        bb.addGridded(cancel);
        bb.addGlue();

        getContentPane().add(builder.getPanel(), BorderLayout.CENTER);
        getContentPane().add(bb.getPanel(), BorderLayout.SOUTH);
        pack();

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                storeSettings();
                dispose();
            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }

    public void setVisible(boolean visible) {
        if (visible)
            setValues();
        super.setVisible(visible);
    }

    public void setValues() {
        encoding.setSelectedItem(panel.getEncoding());

        Vector fileD = metaData.getData(GUIGlobals.FILE_FIELD+"Directory");
        if (fileD == null)
            fileDir.setText("");
        else {
            
            if (fileD.size() >= 1)
                fileDir.setText(((String)fileD.get(0)).trim());
        }

        Vector pdfD = metaData.getData("pdfDirectory");
        if (pdfD == null)
            pdfDir.setText("");
        else {
            
            if (pdfD.size() >= 1)
                pdfDir.setText(((String)pdfD.get(0)).trim());
        }

        Vector psD = metaData.getData("psDirectory");
        if (psD == null)
            psDir.setText("");
        else {
            
            if (psD.size() >= 1)
                psDir.setText(((String)psD.get(0)).trim());
        }

        
        oldFileVal = fileDir.getText();
        oldPdfVal = pdfDir.getText();
        oldPsVal = psDir.getText();
    }

    public void storeSettings() {
        String oldEncoding = panel.getEncoding();
        String newEncoding = (String)encoding.getSelectedItem();
        panel.setEncoding(newEncoding);

        Vector dir = new Vector(1);
        String text = fileDir.getText().trim();
        if (text.length() > 0) {
            dir.add(text);
            metaData.putData(GUIGlobals.FILE_FIELD+"Directory", dir);
        }
        else
            metaData.remove(GUIGlobals.FILE_FIELD+"Directory");

        dir = new Vector(1);
        text = pdfDir.getText().trim();
        if (text.length() > 0) {
            dir.add(text);
            metaData.putData("pdfDirectory", dir);
        }
        else
            metaData.remove("pdfDirectory");

        dir = new Vector(1);
        text = psDir.getText().trim();
        if (text.length() > 0) {
            dir.add(text);
            metaData.putData("psDirectory", dir);
        }
        else
            metaData.remove("psDirectory");

        
        boolean changed = !newEncoding.equals(oldEncoding)
            || !oldPdfVal.equals(pdfDir.getText())
            || !oldPsVal.equals(psDir.getText());
        
        
        if (changed)
            panel.markNonUndoableBaseChanged();
    }
}
