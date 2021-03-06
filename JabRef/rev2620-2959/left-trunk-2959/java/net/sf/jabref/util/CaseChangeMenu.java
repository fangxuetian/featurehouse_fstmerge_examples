package net.sf.jabref.util;



import net.sf.jabref.Globals;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CaseChangeMenu extends JMenu implements ActionListener{
    static CaseChanger cc = new CaseChanger();
    JMenuItem changeCaseItems[];
    private JTextComponent parent;

    public CaseChangeMenu(JTextComponent opener){
        
        super(Globals.lang("Change case"));
        parent = opener;
        int m = CaseChanger.getNumModes();
        changeCaseItems = new JMenuItem[m];
        for (int i=0;i<m;i++){
            changeCaseItems[i]=new JMenuItem(Globals.lang(CaseChanger.getModeName(i)));
            changeCaseItems[i].addActionListener(this);
            this.add(changeCaseItems[i]);
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = (e.getSource());
        for(int i=0, m=CaseChanger.getNumModes(); i<m; i++){
            if(source == changeCaseItems[i]){
                caseChange(i);
                break;
            }
        }
    }

    private void caseChange(int mode){
        parent.setText(CaseChanger.changeCase(parent.getText(), mode));
    }
}