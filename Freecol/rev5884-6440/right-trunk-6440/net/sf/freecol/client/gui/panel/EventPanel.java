

package net.sf.freecol.client.gui.panel;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;
import net.sf.freecol.client.gui.Canvas;
import net.sf.freecol.client.gui.Canvas.EventType;
import net.sf.freecol.client.gui.i18n.Messages;
import net.sf.freecol.common.resources.ResourceManager;



public final class EventPanel extends FreeColDialog<Boolean> {

    private static final Logger logger = Logger.getLogger(EventPanel.class.getName());

    private static final int OK = 0;
    
    private JLabel header;

    private JLabel imageLabel;

    
    public EventPanel(Canvas parent, EventType type) {

        super(parent);

        setLayout(new MigLayout("", "", ""));

        header = new JLabel();
        header.setFont(mediumHeaderFont);

        imageLabel = new JLabel();

        switch(type) {
        case FIRST_LANDING:
            imageLabel.setIcon(new ImageIcon(ResourceManager.getImage("EventImage.firstLanding")));
            header.setText(Messages.message("event.firstLanding", "%name%", getMyPlayer().getSafeNewLandName()));
            break;
        case MEETING_EUROPEANS:
            imageLabel.setIcon(new ImageIcon(ResourceManager.getImage("EventImage.meetingEuropeans")));
            header.setText(Messages.message("event.meetingEuropeans"));
            break;
        case MEETING_NATIVES:
            imageLabel.setIcon(new ImageIcon(ResourceManager.getImage("EventImage.meetingNatives")));
            header.setText(Messages.message("event.meetingNatives"));
            break;
        case MEETING_AZTEC:
            imageLabel.setIcon(new ImageIcon(ResourceManager.getImage("EventImage.meetingAztec")));
            header.setText(Messages.message("event.meetingAztec"));
            break;
        case MEETING_INCA:
            imageLabel.setIcon(new ImageIcon(ResourceManager.getImage("EventImage.meetingInca")));
            header.setText(Messages.message("event.meetingInca"));
            break;
        case DISCOVER_PACIFIC:
            imageLabel.setIcon(new ImageIcon(ResourceManager.getImage("EventImage.discoverPacific")));
            header.setText(Messages.message("model.region.pacific.discover"));
            break;
        default:
            setResponse(Boolean.FALSE);
        }

        add(header, "align center, wrap 20");
        add(imageLabel);
        add(okButton, "newline 20, tag ok");
        okButton.setActionCommand(String.valueOf(OK));
        okButton.addActionListener(this);

        setSize(getPreferredSize());
        enterPressesWhenFocused(okButton);
    }
    
    public void requestFocus() {
        okButton.requestFocus();
    }

    
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        try {
            switch (Integer.valueOf(command).intValue()) {
            case OK:
                setResponse(new Boolean(true));
                break;
            default:
                logger.warning("Invalid Actioncommand: invalid number.");
            }
        } catch (NumberFormatException e) {
            logger.warning("Invalid Actioncommand: not a number.");
        }
    }
}
