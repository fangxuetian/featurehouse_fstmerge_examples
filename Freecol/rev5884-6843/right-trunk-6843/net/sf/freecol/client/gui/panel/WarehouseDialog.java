

package net.sf.freecol.client.gui.panel;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.sf.freecol.FreeCol;
import net.sf.freecol.client.gui.Canvas;
import net.sf.freecol.client.gui.i18n.Messages;
import net.sf.freecol.common.model.Colony;
import net.sf.freecol.common.model.ExportData;
import net.sf.freecol.common.model.Goods;
import net.sf.freecol.common.model.GoodsType;

import net.miginfocom.swing.MigLayout;


public final class WarehouseDialog extends FreeColDialog<Boolean> implements ActionListener {

    private static final Logger logger = Logger.getLogger(WarehouseDialog.class.getName());

    private static final int OK = 0, CANCEL = 1;

    private final JButton ok = new JButton(Messages.message("ok"));

    private final JButton cancel = new JButton(Messages.message("cancel"));

    private final JPanel warehouseDialog;


    
    public WarehouseDialog(Canvas parent, Colony colony) {
        super(parent);

        warehouseDialog = new JPanel(new GridLayout(0, 4, margin, margin));
        warehouseDialog.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(warehouseDialog,
                                                 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement( 16 );
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        ok.setActionCommand(String.valueOf(OK));
        ok.addActionListener(this);
        enterPressesWhenFocused(ok);

        cancel.setActionCommand(String.valueOf(CANCEL));
        cancel.addActionListener(this);
        enterPressesWhenFocused(cancel);
        setCancelComponent(cancel);

        setLayout(new MigLayout("fill, wrap 1", "", ""));

        add(getDefaultHeader(Messages.message("warehouseDialog.name")), "align center");
        add(scrollPane, "grow");
        add(ok, "newline 20, split 2, tag ok");
        add(cancel, "tag cancel");

        for (GoodsType goodsType : FreeCol.getSpecification().getGoodsTypeList()) {
            if (goodsType.isStorable()) {
                warehouseDialog.add(new WarehouseGoodsPanel(colony, goodsType));
            }
        }

        setSize(getPreferredSize());
    }

    public void requestFocus() {
        ok.requestFocus();
    }

    
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        try {
            switch (Integer.valueOf(command).intValue()) {
            case OK:
                setResponse(new Boolean(true));
                for (Component c : warehouseDialog.getComponents()) {
                    if (c instanceof WarehouseGoodsPanel) {
                        ((WarehouseGoodsPanel) c).saveSettings();
                    }
                }
                break;
            case CANCEL:
                getCanvas().remove(this);
                setResponse(new Boolean(false));
                break;
            default:
                logger.warning("Invalid ActionCommand: invalid number.");
            }
        } catch (NumberFormatException e) {
            logger.warning("Invalid Actioncommand: not a number.");
        }
    }


    public class WarehouseGoodsPanel extends JPanel {

        private final Colony colony;

        private final GoodsType goodsType;

        private final JCheckBox export;

        private final JSpinner lowLevel;

        private final JSpinner highLevel;

        private final JSpinner exportLevel;


        public WarehouseGoodsPanel(Colony colony, GoodsType goodsType) {

            this.colony = colony;
            this.goodsType = goodsType;

            setLayout(new MigLayout("wrap 2", "", ""));
            setOpaque(false);
            String goodsName = Messages.message(goodsType.getNameKey());
            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(goodsName),
                    BorderFactory.createEmptyBorder(6, 6, 6, 6)));

            ExportData exportData = colony.getExportData(goodsType);

            
            Goods goods = new Goods(colony.getGame(), colony, goodsType, 
                                    colony.getGoodsContainer().getGoodsCount(goodsType));
            GoodsLabel goodsLabel = new GoodsLabel(goods, getCanvas());
            goodsLabel.setHorizontalAlignment(JLabel.LEADING);
            add(goodsLabel, "span 1 2");

            
            SpinnerNumberModel lowLevelModel = new SpinnerNumberModel(exportData.getLowLevel(), 0, 100, 1);
            lowLevel = new JSpinner(lowLevelModel);
            lowLevel.setToolTipText(Messages.message("warehouseDialog.lowLevel.shortDescription"));
            add(lowLevel);

            
            SpinnerNumberModel highLevelModel = new SpinnerNumberModel(exportData.getHighLevel(), 0, 100, 1);
            highLevel = new JSpinner(highLevelModel);
            highLevel.setToolTipText(Messages.message("warehouseDialog.highLevel.shortDescription"));
            add(highLevel);

            
            export = new JCheckBox(Messages.message("warehouseDialog.export"), exportData.isExported());
            export.setToolTipText(Messages.message("warehouseDialog.export.shortDescription"));
            if (!colony.hasAbility("model.ability.export")) {
                export.setEnabled(false);
            }
            add(export);

            
            SpinnerNumberModel exportLevelModel = new SpinnerNumberModel(exportData.getExportLevel(), 0, colony
                    .getWarehouseCapacity(), 1);
            exportLevel = new JSpinner(exportLevelModel);
            exportLevel.setToolTipText(Messages.message("warehouseDialog.exportLevel.shortDescription"));
            add(exportLevel);

            setSize(getPreferredSize());
        }

        public void saveSettings() {
            int lowLevelValue = ((SpinnerNumberModel) lowLevel.getModel()).getNumber().intValue();
            int highLevelValue = ((SpinnerNumberModel) highLevel.getModel()).getNumber().intValue();
            int exportLevelValue = ((SpinnerNumberModel) exportLevel.getModel()).getNumber().intValue();
            ExportData exportData = colony.getExportData(goodsType);
            boolean changed = (export.isSelected() != exportData.isExported())
                || (lowLevelValue != exportData.getLowLevel())
                || (highLevelValue != exportData.getHighLevel())
                || (exportLevelValue != exportData.getExportLevel());
            
            exportData.setExported(export.isSelected());
            exportData.setLowLevel(lowLevelValue);
            exportData.setHighLevel(highLevelValue);
            exportData.setExportLevel(exportLevelValue);
            if (changed) {
                getController().setGoodsLevels(colony, goodsType);
            }
        }

    }
}
