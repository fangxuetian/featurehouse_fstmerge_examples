package net.sf.jabref.external;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;

import net.sf.jabref.GUIGlobals;
import net.sf.jabref.Globals;
import net.sf.jabref.JabRefFrame;
import net.sf.jabref.MnemonicAwareAction;


public class PushToApplicationButton implements ActionListener {

    public static List<PushToApplication> applications;

    private JabRefFrame frame;
    private List<PushToApplication> pushActions;
    private JPanel comp;
    private JButton pushButton, menuButton;
    private int selected = 0;
    private JPopupMenu popup = null;
    private HashMap<PushToApplication, PushToApplicationAction> actions = new HashMap<PushToApplication, PushToApplicationAction>();
    private final Dimension buttonDim = new Dimension(23, 23);
    private static final URL ARROW_ICON = GUIGlobals.class.getResource("/images/secondary_sorted_reverse.png");
    private MenuAction mAction = new MenuAction();

    
    static {
      applications = new ArrayList<PushToApplication>();
      applications.add(new PushToLyx());
      applications.add(new PushToEmacs());
      applications.add(new PushToWinEdt());
      applications.add(new PushToLatexEditor());
      applications.add(new PushToVim());
    }


    public PushToApplicationButton(JabRefFrame frame, List<PushToApplication> pushActions) {
        this.frame = frame;
        this.pushActions = pushActions;
        init();
    }

    private void init() {
        comp = new JPanel();
        comp.setLayout(new BorderLayout());

        menuButton = new JButton(new ImageIcon(ARROW_ICON));
        menuButton.setMargin(new Insets(0,0,0,0));
        menuButton.setPreferredSize(new Dimension(menuButton.getIcon().getIconWidth(),
                menuButton.getIcon().getIconHeight()));
        menuButton.addActionListener(new MenuButtonActionListener());
        menuButton.setToolTipText(Globals.lang("Select external application"));
        pushButton = new JButton();
        if (Globals.prefs.hasKey("pushToApplication")) {
            String appSelected = Globals.prefs.get("pushToApplication");
            for (int i=0; i<pushActions.size(); i++) {
                PushToApplication toApp = (PushToApplication)pushActions.get(i);
                if (toApp.getName().equals(appSelected)) {
                    selected = i;
                    break;
                }
            }
        }

        setSelected(selected);
        pushButton.addActionListener(this);

        comp.add(pushButton, BorderLayout.CENTER);
        comp.add(menuButton, BorderLayout.EAST);
        comp.setBorder(BorderFactory.createLineBorder(Color.gray));
        comp.setMaximumSize(comp.getPreferredSize());


    }

    
    private void buildPopupMenu() {
        popup = new JPopupMenu();
        int j=0;
        for (PushToApplication application : pushActions){
            JMenuItem item = new JMenuItem(application.getApplicationName(),
                    application.getIcon());
            item.addActionListener(new PopupItemActionListener(j));
            popup.add(item);
            j++;
        }
    }

    
    private void setSelected(int i) {
        this.selected = i;
        PushToApplication toApp = (PushToApplication)pushActions.get(i);
        pushButton.setIcon(toApp.getIcon());
        pushButton.setToolTipText(toApp.getTooltip());
        pushButton.setPreferredSize(buttonDim);
        Globals.prefs.put("pushToApplication", toApp.getName());
        mAction.setTitle(toApp.getApplicationName());
    }

    
    public Component getComponent() {
        return comp;
    }

    public Action getMenuAction() {
        return mAction;
    }

    public void actionPerformed(ActionEvent e) {
        PushToApplication toApp = (PushToApplication)pushActions.get(selected);

        
        PushToApplicationAction action = actions.get(toApp);
        if (action == null) {
            action = new PushToApplicationAction(frame, toApp);
            actions.put(toApp, action);
        }
        action.actionPerformed(new ActionEvent(toApp, 0, "push"));
    }

    class PopupItemActionListener implements ActionListener {
        private int index;
        public PopupItemActionListener(int index) {
            this.index = index;
        }

        public void actionPerformed(ActionEvent e) {
            
            setSelected(index);
            
            PushToApplicationButton.this.actionPerformed(null);
            
            
            pushButton.requestFocus();
        }
    }


    class MenuButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            
            if (popup == null)
                buildPopupMenu();
            popup.show(comp, 0, menuButton.getHeight());
        }
    }

    class MenuAction extends MnemonicAwareAction {

        public MenuAction() {
            putValue(ACCELERATOR_KEY, Globals.prefs.getKey("Push to application"));
        }

        public void setTitle(String appName) {
            putValue(NAME, Globals.lang("Push entries to external application (%0)",
                    appName));
        }

        public void actionPerformed(ActionEvent e) {
            PushToApplicationButton.this.actionPerformed(null);
        }
    }

}
