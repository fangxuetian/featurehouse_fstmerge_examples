

package net.sf.freecol.client.gui.panel;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import net.sf.freecol.FreeCol;
import net.sf.freecol.client.gui.Canvas;
import net.sf.freecol.client.gui.i18n.Messages;
import net.sf.freecol.common.model.BuildableType;
import net.sf.freecol.common.model.Building;
import net.sf.freecol.common.model.Colony;
import net.sf.freecol.common.model.Goods;
import net.sf.freecol.common.model.GoodsType;
import net.sf.freecol.common.model.Player;
import net.sf.freecol.common.model.Unit;

import net.miginfocom.swing.MigLayout;


public final class ReportColonyPanel extends ReportPanel {

    private List<Colony> colonies;

    private final int ROWS_PER_COLONY = 4;

    
    public ReportColonyPanel(Canvas parent) {

        super(parent, Messages.message("menuBar.report.colony"));
        Player player = getMyPlayer();
        colonies = player.getColonies();

        
        Collections.sort(colonies, getClient().getClientOptions().getColonyComparator());

        reportPanel.setLayout(new MigLayout("wrap 12, fillx", "", ""));

        for (Colony colony : colonies) {

            
            JButton button = getLinkButton(colony.getName(), null, colony.getId());
            button.addActionListener(this);
            reportPanel.add(button, "newline 20, span, split 2");
            reportPanel.add(new JSeparator(JSeparator.HORIZONTAL), "growx");

            
            List<Unit> unitList = colony.getUnitList();
            Collections.sort(unitList, getUnitTypeComparator());
            for (Unit unit : unitList) {
                UnitLabel unitLabel = new UnitLabel(unit, getCanvas(), true, true);
                reportPanel.add(unitLabel);
            }
            unitList = colony.getTile().getUnitList();
            Collections.sort(unitList, getUnitTypeComparator());
            for (Unit unit : unitList) {
                UnitLabel unitLabel = new UnitLabel(unit, getCanvas(), true, true);
                reportPanel.add(unitLabel);
            }
            reportPanel.add(new JLabel(), "newline, span");

            
            int netFood = colony.getFoodProduction() - colony.getFoodConsumption();
            if (netFood != 0) {
                ProductionLabel productionLabel = new ProductionLabel(Goods.FOOD, netFood, getCanvas());
                productionLabel.setStockNumber(colony.getFoodCount());
                reportPanel.add(productionLabel, "span 2");
            }
            for (GoodsType goodsType : FreeCol.getSpecification().getGoodsTypeList()) {
                if (goodsType.isFoodType()) {
                    continue;
                }
                int newValue = colony.getProductionNetOf(goodsType);
                int stockValue = colony.getGoodsCount(goodsType);
                if (newValue != 0 || stockValue > 0) {
                    Building building = colony.getBuildingForProducing(goodsType);
                    ProductionLabel productionLabel = new ProductionLabel(goodsType, newValue, getCanvas());
                    if (building != null) {
                        productionLabel.setMaximumProduction(building.getMaximumProduction());
                    }
                    if (goodsType == Goods.HORSES) {
                        productionLabel.setMaxGoodsIcons(1);
                    }
                    productionLabel.setStockNumber(stockValue);   
                    reportPanel.add(productionLabel, "span 2");
                }
            }
            reportPanel.add(new JLabel(), "newline, span");

            for (Building building : colony.getBuildings()) {
                reportPanel.add(new JLabel(building.getName()), "span 3");
            }

            
            BuildableType currentType = colony.getCurrentlyBuilding();
            JLabel buildableLabel = new JLabel();
            if (currentType == null) {
                buildableLabel.setText(Messages.message("nothing"));
                buildableLabel.setForeground(Color.RED);
            } else {
                buildableLabel.setText(currentType.getName());
                buildableLabel.setForeground(Color.GRAY);
            }
            reportPanel.add(buildableLabel, "span 3");

        }

    }
}
