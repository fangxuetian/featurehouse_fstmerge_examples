

package net.sf.freecol.client.gui.panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.sf.freecol.client.gui.Canvas;
import net.sf.freecol.client.gui.i18n.Messages;



public final class RiverStylePanel extends FreeColDialog<Integer> {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(RiverStylePanel.class.getName());
    
    private static final int CANCEL = -1;
    private static final int DELETE = 0;
    
    public RiverStylePanel(Canvas parent) {
        super(parent);
        setLayout(new BorderLayout());
        
        JPanel stylesPanel = new JPanel(new GridLayout(9, 9));
        JButton deleteButton = new JButton(getLibrary().getScaledImageIcon(getLibrary().getMiscImageIcon(getLibrary().DELETE), 0.5f));
        deleteButton.setActionCommand(String.valueOf(DELETE));
        deleteButton.addActionListener(this);
        stylesPanel.add(deleteButton);
        for (int index = 1; index < getLibrary().RIVER_STYLES; index++) {
            JButton riverButton = new JButton(getLibrary().getScaledImageIcon(getLibrary().getRiverImage(index), 0.5f));
            riverButton.setActionCommand(String.valueOf(index));
            riverButton.addActionListener(this);
            stylesPanel.add(riverButton);
        }
        this.add(stylesPanel, BorderLayout.CENTER);
        JButton cancelButton = new JButton(Messages.message("cancel"));
        cancelButton.setActionCommand(String.valueOf(CANCEL));
        cancelButton.addActionListener(this);
        cancelButton.setMnemonic('C');
        this.add(cancelButton, BorderLayout.SOUTH);
        setSize(getPreferredSize());
    }


    
    public void actionPerformed(ActionEvent event) {
        int style = Integer.parseInt(event.getActionCommand());
        setResponse(new Integer(style));
    }

}
