

package net.sf.freecol.client.gui.action;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.KeyStroke;

import net.sf.freecol.client.FreeColClient;
import net.sf.freecol.client.gui.ImageLibrary;
import net.sf.freecol.common.model.Unit.UnitState;


public class SkipUnitAction extends MapboardAction {
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(SkipUnitAction.class.getName());




    public static final String id = "skipUnitAction";


    
    SkipUnitAction(FreeColClient freeColClient) {
        super(freeColClient, "unit.state.1", null, KeyStroke.getKeyStroke(' ', 0));
        putValue(BUTTON_IMAGE, freeColClient.getImageLibrary().getUnitButtonImageIcon(ImageLibrary.UNIT_BUTTON_DONE, 0));
        putValue(BUTTON_ROLLOVER_IMAGE, freeColClient.getImageLibrary().getUnitButtonImageIcon(
                ImageLibrary.UNIT_BUTTON_DONE, 1));
        putValue(BUTTON_PRESSED_IMAGE, freeColClient.getImageLibrary().getUnitButtonImageIcon(
                ImageLibrary.UNIT_BUTTON_DONE, 2));
        putValue(BUTTON_DISABLED_IMAGE, freeColClient.getImageLibrary().getUnitButtonImageIcon(
                ImageLibrary.UNIT_BUTTON_DONE, 3));
    }

    
    protected boolean shouldBeEnabled() {
        return super.shouldBeEnabled() && getFreeColClient().getGUI().getActiveUnit() != null;
    }

    
    public String getId() {
        return id;
    }

    
    public void actionPerformed(ActionEvent e) {
        
        getFreeColClient().getInGameController().changeState(getFreeColClient().getGUI().getActiveUnit(), 
                                                             UnitState.SKIPPED);
    }
}
