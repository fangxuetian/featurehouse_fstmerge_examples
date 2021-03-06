

package net.sf.freecol.client.gui.panel;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.sf.freecol.client.gui.Canvas;
import net.sf.freecol.client.gui.i18n.Messages;
import net.sf.freecol.common.model.GoodsType;
import net.sf.freecol.common.model.IndianNationType;
import net.sf.freecol.common.model.IndianSettlement;
import net.sf.freecol.common.model.Player;
import net.sf.freecol.common.model.Tension;
import net.sf.freecol.common.model.UnitType;

import net.miginfocom.swing.MigLayout;


public final class ReportIndianPanel extends ReportPanel {

    
    public ReportIndianPanel(Canvas parent) {
        super(parent, Messages.message("menuBar.report.indian"));
        Player player = getMyPlayer();
        reportPanel.setLayout(new MigLayout("wrap 1, fillx"));
        for (Player opponent : getGame().getPlayers()) {
            if (opponent.isIndian() && !opponent.isDead() && player.hasContacted(opponent)) {
                reportPanel.add(buildIndianAdvisorPanel(player, opponent));
            }
        }
        scrollPane.getViewport().setOpaque(false);
        reportPanel.setOpaque(false);
        reportPanel.doLayout();
    }

    
    private JPanel buildIndianAdvisorPanel(Player player, Player opponent) {

        JPanel result = new JPanel(new MigLayout("wrap 4, fillx", "[]20px[]", ""));
        result.setOpaque(false);

        result.add(localizedLabel("report.indian.nameOfTribe"));
        result.add(localizedLabel(opponent.getNationName()), "span");
        result.add(localizedLabel("report.indian.chieftain"));
        result.add(new JLabel(Messages.message(opponent.getName())), "span");
        result.add(localizedLabel("report.indian.typeOfSettlements"));
        result.add(localizedLabel(((IndianNationType) opponent.getNationType()).getSettlementTypeKey()), "span");
        result.add(localizedLabel("report.indian.numberOfSettlements"));
        result.add(new JLabel(String.valueOf(opponent.getSettlements().size())), "span");
        result.add(new JLabel(Messages.message("report.indian.tension")+":"));
        result.add(localizedLabel(opponent.getTension(player).toString()), "span");

        result.add(new JSeparator(JSeparator.HORIZONTAL), "span, growx");

        boolean anyKnownSettlements = false;
        if(opponent.getIndianSettlements().size() > 0) {
                anyKnownSettlements = true;
        }
        
        if(anyKnownSettlements) {
            result.add(localizedLabel("Settlement"), "newline 10");
            result.add(localizedLabel("report.indian.tension"));
            result.add(localizedLabel("report.indian.skillTaught"));
            result.add(localizedLabel("report.indian.tradeInterests"));

            
            for (IndianSettlement settlement : opponent.getIndianSettlements()) {
                boolean known = settlement.getTile().isExplored();
                boolean visited = settlement.hasBeenVisited(player);
                String locationName = Messages.message(settlement.getNameFor(player));
                if (known) {
                    locationName += ((settlement.isCapital()) ? "*" : "")
                        + ((settlement.getMissionary() != null) ? "+" : "")
                        + " (" + settlement.getTile().getX()
                        + ", " + settlement.getTile().getY() + ")";
                }
                result.add(new JLabel(locationName), "newline 15");

                Tension tension = settlement.getAlarm(player);
                String tensionString
                    = (!player.hasContacted(opponent)) ? "notContacted"
                    : (known && tension != null) ? tension.toString()
                    : "indianSettlement.tensionUnknown";
                result.add(localizedLabel(tensionString));

                JLabel skillLabel = new JLabel();
                UnitType skillType = settlement.getLearnableSkill();
                String skillString;
                if (visited) {
                    if (skillType == null) {
                        skillString = "indianSettlement.skillNone";
                    } else {
                        skillString = skillType.getNameKey();
                        ImageIcon skillImage = getLibrary().getUnitImageIcon(skillType, 0.66);
                        skillLabel.setIcon(skillImage);
                    }
                } else {
                    skillString = "indianSettlement.skillUnknown";
                }
                skillLabel.setText(Messages.message(skillString));
                result.add(skillLabel);

                GoodsType[] wantedGoods = settlement.getWantedGoods();
                if (visited && wantedGoods[0] != null) {
                    JLabel goodsLabel = localizedLabel(wantedGoods[0].getNameKey());
                    goodsLabel.setIcon(new ImageIcon(getLibrary().getGoodsImage(wantedGoods[0], 0.66)));
                    String split = "split " + String.valueOf(wantedGoods.length);
                    result.add(goodsLabel, split);
                    for (int i = 1; i < wantedGoods.length; i++) {
                        if (wantedGoods[i] != null) {
                            goodsLabel = localizedLabel(wantedGoods[i].getNameKey());
                            goodsLabel.setIcon(getLibrary().getScaledGoodsImageIcon(wantedGoods[i], 0.5));
                            result.add(goodsLabel);
                        }
                    }
                } else {
                    result.add(localizedLabel("indianSettlement.wantedGoodsUnknown"));
                }
            }
        } else {
            result.add(localizedLabel("report.indian.noKnownSettlements"));
        }
        result.add(new JSeparator(JSeparator.HORIZONTAL), "newline 10, span, growx");
        return result;
    }
}
