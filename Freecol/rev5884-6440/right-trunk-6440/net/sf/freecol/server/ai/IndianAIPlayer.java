

package net.sf.freecol.server.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;


import net.sf.freecol.FreeCol;
import net.sf.freecol.common.model.Colony;
import net.sf.freecol.common.model.Goods;
import net.sf.freecol.common.model.IndianSettlement;
import net.sf.freecol.common.model.Location;
import net.sf.freecol.common.model.Map;
import net.sf.freecol.common.model.PathNode;
import net.sf.freecol.common.model.Player;
import net.sf.freecol.common.model.Settlement;
import net.sf.freecol.common.model.Tension;
import net.sf.freecol.common.model.Tile;
import net.sf.freecol.common.model.Unit;
import net.sf.freecol.common.model.Unit.UnitState;
import net.sf.freecol.common.model.Map.Position;
import net.sf.freecol.common.networking.NetworkConstants;
import net.sf.freecol.server.ai.mission.IndianBringGiftMission;
import net.sf.freecol.server.ai.mission.IndianDemandMission;
import net.sf.freecol.server.ai.mission.UnitSeekAndDestroyMission;
import net.sf.freecol.server.ai.mission.UnitWanderHostileMission;



public class IndianAIPlayer extends NewAIPlayer {

    private static final Logger logger = Logger.getLogger(IndianAIPlayer.class.getName());

    private static final int MAX_DISTANCE_TO_BRING_GIFT = 5;

    private static final int MAX_NUMBER_OF_GIFTS_BEING_DELIVERED = 1;

    private static final int MAX_DISTANCE_TO_MAKE_DEMANDS = 5;

    private static final int MAX_NUMBER_OF_DEMANDS = 1;


    
    public void startWorking() {
        logger.fine("Entering AI code for: " + getPlayer());
        sessionRegister.clear();
        clearAIUnits();
        determineStances();
        abortInvalidAndOneTimeMissions();
        secureSettlements();
        giveNormalMissions();
        bringGifts();
        demandTribute();
        doMissions();
        abortInvalidMissions();
        
        giveNormalMissions();
        doMissions();
        abortInvalidMissions();
        clearAIUnits();
    }

    
    private void giveNormalMissions() {
        logger.finest("Entering method giveNormalMissions");

        int numberOfUnits = FreeCol.getSpecification().numberOfUnitTypes();
        
        ArrayList<ArrayList<Wish>> workerWishes = new ArrayList<ArrayList<Wish>>(numberOfUnits);
        for (int i = 0; i < numberOfUnits; i++) {
            workerWishes.add(new ArrayList<Wish>());
        }

        Iterator<AIUnit> aiUnitsIterator = getAIUnitIterator();
        while (aiUnitsIterator.hasNext()) {
            AIUnit aiUnit = aiUnitsIterator.next();

            if (aiUnit.hasMission()) {
                continue;
            }

            Unit unit = aiUnit.getUnit();

            if (unit.isUninitialized()) {
                logger.warning("Trying to assign a mission to an uninitialized object: " + unit.getId());
                continue;
            }

            if (!aiUnit.hasMission()) {
                aiUnit.setMission(new UnitWanderHostileMission(getAIMain(), aiUnit));
            }
        }
    }

    
    private void secureSettlements() {
        logger.finest("Entering method secureSettlements");
        
        for (IndianSettlement is : getPlayer().getIndianSettlements()) {
            secureIndianSettlement(is);
        }
    }

    
    public void secureIndianSettlement(IndianSettlement is) {
        if (is.getOwner().isAtWar()) {
            Map map = getPlayer().getGame().getMap();
            if (is.getUnitCount() > 2) {
                int defenders = is.getTile().getUnitCount();
                int threat = 0;
                int worstThreat = 0;
                Location bestTarget = null;
                Iterator<Position> positionIterator = map.getCircleIterator(is.getTile().getPosition(), true, 2);
                while (positionIterator.hasNext()) {
                    Tile t = map.getTile(positionIterator.next());
                    
                    
                    if(!t.isLand()){
                        continue;
                    }
                    if (t.getFirstUnit() != null) {
                        Player enemy = t.getFirstUnit().getOwner();
                        if (enemy == getPlayer()) {
                            defenders++;
                        } else {
                            Tension tension = getPlayer().getTension(enemy);
                            if (tension != null) {
                                int value = tension.getValue();
                                if (value >= Tension.TENSION_ADD_MAJOR) {
                                    threat += 2;
                                    if (t.getUnitCount() * 2 > worstThreat) {
                                        if (t.getSettlement() != null) {
                                            bestTarget = t.getSettlement();
                                        } else {
                                            bestTarget = t.getFirstUnit();
                                        }
                                        worstThreat = t.getUnitCount() * 2;
                                    }
                                } else if (value >= Tension.TENSION_ADD_MINOR) {
                                    threat += 1;
                                    if (t.getUnitCount() > worstThreat) {
                                        if (t.getSettlement() != null) {
                                            bestTarget = t.getSettlement();
                                        } else {
                                            bestTarget = t.getFirstUnit();
                                        }
                                        worstThreat = t.getUnitCount();
                                    }
                                }
                            }
                        }
                    }
                }
                if (threat > defenders) {
                    Unit newDefender = is.getFirstUnit();
                    newDefender.setState(UnitState.ACTIVE);
                    newDefender.setLocation(is.getTile());
                    AIUnit newDefenderAI = (AIUnit) getAIMain().getAIObject(newDefender);
                    if (bestTarget != null) {
                        newDefenderAI.setMission(new UnitSeekAndDestroyMission(getAIMain(), newDefenderAI,
                                                                               bestTarget));
                    } else {
                        newDefenderAI.setMission(new UnitWanderHostileMission(getAIMain(), newDefenderAI));
                    }
                }
            }
        }
    }

    
    private void bringGifts() {
        logger.finest("Entering method bringGifts");
        if (!getPlayer().isIndian()) {
            return;
        }
        for (IndianSettlement indianSettlement : getPlayer().getIndianSettlements()) {
            
            if (getRandom().nextInt(10) != 1) {
                continue;
            }
            int alreadyAssignedUnits = 0;
            Iterator<Unit> ownedUnits = indianSettlement.getOwnedUnitsIterator();
            while (ownedUnits.hasNext()) {
                if (((AIUnit) getAIMain().getAIObject(ownedUnits.next())).getMission() instanceof IndianBringGiftMission) {
                    alreadyAssignedUnits++;
                }
            }
            if (alreadyAssignedUnits > MAX_NUMBER_OF_GIFTS_BEING_DELIVERED) {
                continue;
            }
            
            ArrayList<Colony> nearbyColonies = new ArrayList<Colony>();
            Iterator<Position> it = getGame().getMap().getCircleIterator(indianSettlement.getTile().getPosition(), true,
                                                                         MAX_DISTANCE_TO_BRING_GIFT);
            while (it.hasNext()) {
                Tile t = getGame().getMap().getTile(it.next());
                if (t.getColony() != null
                    && IndianBringGiftMission.isValidMission(getPlayer(), t.getColony().getOwner())) {
                    nearbyColonies.add(t.getColony());
                }
            }
            if (nearbyColonies.size() > 0) {
                Colony target = nearbyColonies.get(getRandom().nextInt(nearbyColonies.size()));
                Iterator<Unit> it2 = indianSettlement.getOwnedUnitsIterator();
                AIUnit chosenOne = null;
                while (it2.hasNext()) {
                    chosenOne = (AIUnit) getAIMain().getAIObject(it2.next());
                    if (!(chosenOne.getUnit().getLocation() instanceof Tile)) {
                        chosenOne = null;
                    } else if (chosenOne.getMission() == null
                               || chosenOne.getMission() instanceof UnitWanderHostileMission) {
                        break;
                    }
                }
                if (chosenOne != null) {
                    
                    PathNode pn = chosenOne.getUnit().findPath(indianSettlement.getTile(), target.getTile());
                    if (pn != null && pn.getTotalTurns() <= MAX_DISTANCE_TO_BRING_GIFT) {
                        chosenOne.setMission(new IndianBringGiftMission(getAIMain(), chosenOne, target));
                    }
                }
            }
        }
    }

    
    private void demandTribute() {
        logger.finest("Entering method demandTribute");
        if (!getPlayer().isIndian()) {
            return;
        }
        for (IndianSettlement indianSettlement : getPlayer().getIndianSettlements()) {
            
            if (getRandom().nextInt(10) != 1) {
                continue;
            }
            int alreadyAssignedUnits = 0;
            Iterator<Unit> ownedUnits = indianSettlement.getOwnedUnitsIterator();
            while (ownedUnits.hasNext()) {
                if (((AIUnit) getAIMain().getAIObject(ownedUnits.next())).getMission() instanceof IndianDemandMission) {
                    alreadyAssignedUnits++;
                }
            }
            if (alreadyAssignedUnits > MAX_NUMBER_OF_DEMANDS) {
                continue;
            }
            
            ArrayList<Colony> nearbyColonies = new ArrayList<Colony>();
            Iterator<Position> it = getGame().getMap().getCircleIterator(indianSettlement.getTile().getPosition(), true,
                                                                         MAX_DISTANCE_TO_MAKE_DEMANDS);
            while (it.hasNext()) {
                Tile t = getGame().getMap().getTile(it.next());
                if (t.getColony() != null) {
                    nearbyColonies.add(t. getColony());
                }
            }
            if (nearbyColonies.size() > 0) {
                int targetTension = Integer.MIN_VALUE;
                Colony target = null;
                for (int i = 0; i < nearbyColonies.size(); i++) {
                    Colony t = nearbyColonies.get(i);
                    Player to = t.getOwner();
                    if (getPlayer().getTension(to) == null ||
                        indianSettlement.getAlarm(to) == null) {
                        continue;
                    }
                    int tension = 1 + getPlayer().getTension(to).getValue() + indianSettlement.getAlarm(to).getValue();
                    tension = getRandom().nextInt(tension);
                    if (tension > targetTension) {
                        targetTension = tension;
                        target = t;
                    }
                }
                Iterator<Unit> it2 = indianSettlement.getOwnedUnitsIterator();
                AIUnit chosenOne = null;
                while (it2.hasNext()) {
                    chosenOne = (AIUnit) getAIMain().getAIObject(it2.next());
                    if (!(chosenOne.getUnit().getLocation() instanceof Tile)) {
                        chosenOne = null;
                    } else if (chosenOne.getMission() == null
                               || chosenOne.getMission() instanceof UnitWanderHostileMission) {
                        break;
                    }
                }
                if (chosenOne != null && target != null) {
                    
                    PathNode pn = chosenOne.getUnit().findPath(indianSettlement.getTile(), target.getTile());
                    if (pn != null && pn.getTotalTurns() <= MAX_DISTANCE_TO_MAKE_DEMANDS) {
                        
                        
                        Player tp = target.getOwner();
                        int tension = 1 + getPlayer().getTension(tp).getValue()
                            + indianSettlement.getAlarm(tp).getValue();
                        if (getRandom().nextInt(tension) > Tension.Level.HAPPY.getLimit()) {
                            chosenOne.setMission(new IndianDemandMission(getAIMain(), chosenOne, target));
                        }
                    }
                }
            }
        }
    }

    
    public int tradeProposition(Unit unit, Settlement settlement, Goods goods, int gold) {
        logger.finest("Entering method tradeProposition");
        String goldKey = "tradeGold#" + goods.getType().getIndex() + "#" + goods.getAmount() + "#" + unit.getId();
        String hagglingKey = "tradeHaggling#" + unit.getId();
        int price;
        if (sessionRegister.containsKey(goldKey)) {
            price = sessionRegister.get(goldKey).intValue();
            if (price <= 0) {
                return price;
            }
        } else {
            price = ((IndianSettlement) settlement).getPrice(goods) - getPlayer().getTension(unit.getOwner()).getValue();
            price = Math.min(price, getPlayer().getGold() / 2);
            if (price <= 0) {
                return 0;
            }
            sessionRegister.put(goldKey, new Integer(price));
        }
        if (gold < 0 || price == gold) {
            return price;
        } else if (gold > (getPlayer().getGold() * 3) / 4) {
            sessionRegister.put(goldKey, new Integer(-1));
            return NetworkConstants.NO_TRADE;
        } else if (gold > (price * 11) / 10) {
            logger.warning("Cheating attempt: haggling with a request too high");
            sessionRegister.put(goldKey, new Integer(-1));
            return NetworkConstants.NO_TRADE;
        } else {
            int haggling = 1;
            if (sessionRegister.containsKey(hagglingKey)) {
                haggling = sessionRegister.get(hagglingKey).intValue();
            }
            if (getRandom().nextInt(3 + haggling) <= 3) {
                sessionRegister.put(goldKey, new Integer(gold));
                sessionRegister.put(hagglingKey, new Integer(haggling + 1));
                return gold;
            } else {
                sessionRegister.put(goldKey, new Integer(-1));
                return NetworkConstants.NO_TRADE;
            }
        }
    }


}