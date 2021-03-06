package net.sf.jabref.plugin;

import net.sf.jabref.JabRefFrame;
import net.sf.jabref.SidePaneComponent;
import net.sf.jabref.SidePaneManager;

import javax.swing.*;


public interface SidePanePlugin {

    public void init(JabRefFrame frame, SidePaneManager manager);
    
    public SidePaneComponent getSidePaneComponent();

    public JMenuItem getMenuItem();

    public String getShortcutKey();
}
