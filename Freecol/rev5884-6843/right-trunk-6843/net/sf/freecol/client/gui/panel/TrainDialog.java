

package net.sf.freecol.client.gui.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import net.sf.freecol.FreeCol;
import net.sf.freecol.client.gui.Canvas;
import net.sf.freecol.client.gui.i18n.Messages;

import net.sf.freecol.common.model.Europe;
import net.sf.freecol.common.model.Player;
import net.sf.freecol.common.model.Unit.Role;
import net.sf.freecol.common.model.UnitType;

import net.miginfocom.swing.MigLayout;




public final class TrainDialog extends FreeColDialog<Integer> implements ActionListener {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(TrainDialog.class.getName());

    private static final String TRAIN_DONE = "DONE";

    private final JButton done = new JButton(Messages.message("trainDialog.done"));

    private final JLabel question;

    private final List<UnitType> trainableUnits = new ArrayList<UnitType>();

    private final Comparator<UnitType> unitPriceComparator;

    
    public TrainDialog(Canvas parent, EuropePanel.EuropeAction europeAction) {

        super(parent);

        final Europe europe = getMyPlayer().getEurope();
        unitPriceComparator = new Comparator<UnitType>() {
            public int compare(final UnitType type1, final UnitType type2) {
                return (europe.getUnitPrice(type1) - 
                        europe.getUnitPrice(type2));
            }
        };

        switch(europeAction) {
        case TRAIN:
            trainableUnits.addAll(FreeCol.getSpecification().getUnitTypesTrainedInEurope());
            question = new JLabel(Messages.message("trainDialog.clickOn"));
            setLayout(new MigLayout("wrap 3", "[sg]", ""));
            break;
        case PURCHASE:
        default:
            trainableUnits.addAll(FreeCol.getSpecification().getUnitTypesPurchasedInEurope());
            question  = new JLabel(Messages.message("purchaseDialog.clickOn"));
            setLayout(new MigLayout("wrap 2", "[sg]", ""));
        }

        done.setActionCommand(String.valueOf(TRAIN_DONE));
        done.addActionListener(this);
        enterPressesWhenFocused(done);

    }

    
    public void initialize() {

        removeAll();
        add(question, "span, wrap 20");

        final Player player = getMyPlayer();
        final Europe europe = player.getEurope();

        
        Collections.sort(trainableUnits, unitPriceComparator);

        for (UnitType unitType : trainableUnits) {
            int price = europe.getUnitPrice(unitType);
            JButton newButton = new JButton();
            newButton.setLayout(new MigLayout("wrap 2", "[60]", "[30][30]"));

            ImageIcon unitIcon = getLibrary().getUnitImageIcon(unitType, Role.DEFAULT,
                                                               (price > player.getGold()), 0.66);
            JLabel unitName = localizedLabel(unitType.getNameKey());
            JLabel unitPrice = new JLabel(Messages.message("goldAmount", "%amount%", 
                                                           String.valueOf(price)));
            if (price > player.getGold()) {
                unitName.setEnabled(false);
                unitPrice.setEnabled(false);
                newButton.setEnabled(false);
            }
            newButton.add(new JLabel(unitIcon), "span 1 2");
            newButton.add(unitName);
            newButton.add(unitPrice);
            newButton.setActionCommand(unitType.getId());
            newButton.addActionListener(this);
            enterPressesWhenFocused(newButton);
            add(newButton, "grow");
        }
        add(done, "newline 20, span, tag ok");
        setSize(getPreferredSize());
        revalidate();
    }

    public void requestFocus() {
        done.requestFocus();
    }

    
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (TRAIN_DONE.equals(command)) {
            setResponse(new Integer(-1));
        } else {
            UnitType unitType = FreeCol.getSpecification().getUnitType(command);
            getController().trainUnitInEurope(unitType);
            initialize();
        }
    }
}
