

package net.sf.freecol.server.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import net.sf.freecol.FreeCol;
import net.sf.freecol.common.Specification;
import net.sf.freecol.common.model.AbstractUnit;
import net.sf.freecol.common.model.BuildableType;
import net.sf.freecol.common.model.Building;
import net.sf.freecol.common.model.BuildingType;
import net.sf.freecol.common.model.Colony;
import net.sf.freecol.common.model.ColonyTile;
import net.sf.freecol.common.model.CombatModel;
import net.sf.freecol.common.model.EquipmentType;
import net.sf.freecol.common.model.Europe;
import net.sf.freecol.common.model.ExportData;
import net.sf.freecol.common.model.Game;
import net.sf.freecol.common.model.GameOptions;
import net.sf.freecol.common.model.Goods;
import net.sf.freecol.common.model.GoodsType;
import net.sf.freecol.common.model.HighScore;
import net.sf.freecol.common.model.HistoryEvent;
import net.sf.freecol.common.model.IndianSettlement;
import net.sf.freecol.common.model.Location;
import net.sf.freecol.common.model.Map;
import net.sf.freecol.common.model.Player;
import net.sf.freecol.common.model.Region;
import net.sf.freecol.common.model.Settlement;
import net.sf.freecol.common.model.Tension;
import net.sf.freecol.common.model.Tile;
import net.sf.freecol.common.model.TileImprovement;
import net.sf.freecol.common.model.TileImprovementType;
import net.sf.freecol.common.model.TileItemContainer;
import net.sf.freecol.common.model.TradeRoute;
import net.sf.freecol.common.model.Unit;
import net.sf.freecol.common.model.UnitType;
import net.sf.freecol.common.model.UnitTypeChange;
import net.sf.freecol.common.model.WorkLocation;
import net.sf.freecol.common.model.CombatModel.CombatResult;
import net.sf.freecol.common.model.CombatModel.CombatResultType;
import net.sf.freecol.common.model.LostCityRumour.RumourType;
import net.sf.freecol.common.model.Map.Direction;
import net.sf.freecol.common.model.Map.Position;
import net.sf.freecol.common.model.Player.Stance;
import net.sf.freecol.common.model.Unit.Role;
import net.sf.freecol.common.model.Unit.UnitState;
import net.sf.freecol.common.model.UnitTypeChange.ChangeType;
import net.sf.freecol.common.networking.BuildColonyMessage;
import net.sf.freecol.common.networking.BuyMessage;
import net.sf.freecol.common.networking.BuyPropositionMessage;
import net.sf.freecol.common.networking.CashInTreasureTrainMessage;
import net.sf.freecol.common.networking.Connection;
import net.sf.freecol.common.networking.ClaimLandMessage;
import net.sf.freecol.common.networking.CloseTransactionMessage;
import net.sf.freecol.common.networking.DebugForeignColonyMessage;
import net.sf.freecol.common.networking.DeclareIndependenceMessage;
import net.sf.freecol.common.networking.DeliverGiftMessage;
import net.sf.freecol.common.networking.DiplomacyMessage;
import net.sf.freecol.common.networking.DisembarkMessage;
import net.sf.freecol.common.networking.EmigrateUnitMessage;
import net.sf.freecol.common.networking.GetTransactionMessage;
import net.sf.freecol.common.networking.GiveIndependenceMessage;
import net.sf.freecol.common.networking.GoodsForSaleMessage;
import net.sf.freecol.common.networking.JoinColonyMessage;
import net.sf.freecol.common.networking.Message;
import net.sf.freecol.common.networking.NetworkConstants;
import net.sf.freecol.common.networking.NoRouteToServerException;
import net.sf.freecol.common.networking.RenameMessage;
import net.sf.freecol.common.networking.SellMessage;
import net.sf.freecol.common.networking.SellPropositionMessage;
import net.sf.freecol.common.networking.SetDestinationMessage;
import net.sf.freecol.common.networking.SpySettlementMessage;
import net.sf.freecol.common.networking.StatisticsMessage;
import net.sf.freecol.common.networking.UpdateCurrentStopMessage;
import net.sf.freecol.common.option.BooleanOption;
import net.sf.freecol.common.util.RandomChoice;
import net.sf.freecol.server.FreeColServer;
import net.sf.freecol.server.model.ServerPlayer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public final class InGameInputHandler extends InputHandler implements NetworkConstants {

    private static Logger logger = Logger.getLogger(InGameInputHandler.class.getName());

    
    public InGameInputHandler(final FreeColServer freeColServer) {
        super(freeColServer);
        
        register("createUnit", new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return createUnit(connection, element);
            }
        });
        register("createBuilding", new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return createBuilding(connection, element);
            }
        });
        register("getRandom", new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return getRandom(connection, element);
            }
        });
        register("getVacantEntryLocation", new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return getVacantEntryLocation(connection, element);
            }
        });
        register(SetDestinationMessage.getXMLElementTagName(), new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return new SetDestinationMessage(getGame(), element).handle(freeColServer, connection);
            }
        });
        register("move", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return move(connection, element);
            }
        });
        register("askSkill", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return askSkill(connection, element);
            }
        });
        register("attack", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return attack(connection, element);
            }
        });
        register("embark", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return embark(connection, element);
            }
        });
        register("boardShip", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return boardShip(connection, element);
            }
        });
        register("learnSkillAtSettlement", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return learnSkillAtSettlement(connection, element);
            }
        });
        register("scoutIndianSettlement", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return scoutIndianSettlement(connection, element);
            }
        });
        register("missionaryAtSettlement", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return missionaryAtSettlement(connection, element);
            }
        });
        register("inciteAtSettlement", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return inciteAtSettlement(connection, element);
            }
        });
        register("armedUnitDemandTribute", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return armedUnitDemandTribute(connection, element);
            }
        });
        register(DisembarkMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new DisembarkMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register("loadCargo", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return loadCargo(connection, element);
            }
        });
        register("unloadCargo", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return unloadCargo(connection, element);
            }
        });
        register("buyGoods", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return buyGoods(connection, element);
            }
        });
        register("sellGoods", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return sellGoods(connection, element);
            }
        });
        register("moveToEurope", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return moveToEurope(connection, element);
            }
        });
        register("moveToAmerica", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return moveToAmerica(connection, element);
            }
        });
        register(BuildColonyMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new BuildColonyMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register(JoinColonyMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new JoinColonyMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register("recruitUnitInEurope", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return recruitUnitInEurope(connection, element);
            }
        });
        register(EmigrateUnitMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new EmigrateUnitMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register("trainUnitInEurope", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return trainUnitInEurope(connection, element);
            }
        });
        register("equipUnit", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return equipUnit(connection, element);
            }
        });
        register("work", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return work(connection, element);
            }
        });
        register("changeWorkType", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return changeWorkType(connection, element);
            }
        });
        register("workImprovement", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return workImprovement(connection, element);
            }
        });
        register("setBuildQueue", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return setBuildQueue(connection, element);
            }
        });
        register("changeState", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return changeState(connection, element);
            }
        });
        register("putOutsideColony", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return putOutsideColony(connection, element);
            }
        });
        register("clearSpeciality", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return clearSpeciality(connection, element);
            }
        });
        register("setNewLandName", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return setNewLandName(connection, element);
            }
        });
        register("endTurn", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return endTurn(connection, element);
            }
        });
        register("disbandUnit", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return disbandUnit(connection, element);
            }
        });
        register(CashInTreasureTrainMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new CashInTreasureTrainMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register(GetTransactionMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new GetTransactionMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register(CloseTransactionMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new CloseTransactionMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register(GoodsForSaleMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new GoodsForSaleMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register(BuyPropositionMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new BuyPropositionMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register(SellPropositionMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new SellPropositionMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register(BuyMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new BuyMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register(SellMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new SellMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register(DeliverGiftMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new DeliverGiftMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register("indianDemand", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return indianDemand(connection, element);
            }
        });
        register(ClaimLandMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new ClaimLandMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register("payForBuilding", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return payForBuilding(connection, element);
            }
        });
        register("payArrears", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return payArrears(connection, element);
            }
        });
        register("setGoodsLevels", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return setGoodsLevels(connection, element);
            }
        });
        register(DeclareIndependenceMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new DeclareIndependenceMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register(GiveIndependenceMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new GiveIndependenceMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register("foreignAffairs", new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return foreignAffairs(connection, element);
            }
        });
        register("highScores", new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return highScores(connection, element);
            }
        });
        register("getREFUnits", new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return getREFUnits(connection, element);
            }
        });
        register(RenameMessage.getXMLElementTagName(), new CurrentPlayerNetworkRequestHandler() {
            @Override
            public Element handle(Player player, Connection connection, Element element) {
                return new RenameMessage(getGame(), element).handle(freeColServer, player, connection);
            }
        });
        register("getNewTradeRoute", new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return getNewTradeRoute(connection, element);
            }
        });
        register("updateTradeRoute", new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return updateTradeRoute(connection, element);
            }
        });
        register("setTradeRoutes", new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return setTradeRoutes(connection, element);
            }
        });
        register("assignTradeRoute", new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return assignTradeRoute(connection, element);
            }
        });
        register(UpdateCurrentStopMessage.getXMLElementTagName(), new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return new UpdateCurrentStopMessage(getGame(), element).handle(freeColServer, connection);
            }
        });
        register(DiplomacyMessage.getXMLElementTagName(), new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return new DiplomacyMessage(getGame(), element).handle(freeColServer, connection);
            }
        });
        register("selectFromFountainYouth", new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return selectFromFountainYouth(connection, element);
            }
        });
        register(SpySettlementMessage.getXMLElementTagName(), new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return new SpySettlementMessage(getGame(), element).handle(freeColServer, connection);
            }
        });
        register(DebugForeignColonyMessage.getXMLElementTagName(), new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return new DebugForeignColonyMessage(getGame(), element).handle(freeColServer, connection);
            }
        });
        register("abandonColony", new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return abandonColony(connection, element);
            }
        });
        register("continuePlaying", new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return continuePlaying(connection, element);
            }
        });
        register("assignTeacher", new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return assignTeacher(connection, element);
            }
        });
        register(StatisticsMessage.getXMLElementTagName(), new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return getServerStatistics(connection, element);
            }
        });
        register("retire", new NetworkRequestHandler() {
            public Element handle(Connection connection, Element element) {
                return retire(connection, element);
            }
        });
    }


    
    
    
    private List<ServerPlayer> getOtherPlayers(ServerPlayer serverPlayer) {
        List<ServerPlayer> result = new ArrayList<ServerPlayer>();
        for (Player otherPlayer : getGame().getPlayers()) {
            ServerPlayer enemyPlayer = (ServerPlayer) otherPlayer;
            if (!enemyPlayer.equals(serverPlayer)
                && enemyPlayer.isConnected()) {
                result.add(enemyPlayer);
            }
        }
        return result;
    }

    
    private void sendRemoveUnitToAll(Unit unit, ServerPlayer serverPlayer) {
        Element remove = Message.createNewRootElement("remove");
        unit.addToRemoveElement(remove);
        for (ServerPlayer enemyPlayer : getOtherPlayers(serverPlayer)) {
            if (unit.isVisibleTo(enemyPlayer)) {
                try {
                    enemyPlayer.getConnection().sendAndWait(remove);
                } catch (IOException e) {
                    logger.warning(e.getMessage());
                }
            }
        }
    }

    
    private void sendUpdatedTileToAll(Tile newTile, ServerPlayer serverPlayer) {
        for (ServerPlayer enemyPlayer : getOtherPlayers(serverPlayer)) {
            if (enemyPlayer.canSee(newTile)) {
                Element update = Message.createNewRootElement("update");
                Document doc = update.getOwnerDocument();
                update.appendChild(newTile.toXMLElement(enemyPlayer, doc));
                try {
                    enemyPlayer.getConnection().sendAndWait(update);
                } catch (IOException e) {
                    logger.warning(e.getMessage());
                }
            }
        }
    }

    
    private Element setNewLandName(Connection connection, Element element) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        player.setNewLandName(element.getAttribute("newLandName"));
        player.getHistory().add(new HistoryEvent(getGame().getTurn().getNumber(),
                                                 HistoryEvent.Type.DISCOVER_NEW_WORLD,
                                                 "%name%", player.getNewLandName()));
        
        return null;
    }

    
    private Element createUnit(Connection connection, Element element) {
        logger.info("Receiving \"createUnit\"-request.");
        String taskID = element.getAttribute("taskID");
        Location location = (Location) getGame().getFreeColGameObject(element.getAttribute("location"));
        Player owner = (Player) getGame().getFreeColGameObject(element.getAttribute("owner"));
        UnitType type = FreeCol.getSpecification().getUnitType(element.getAttribute("type"));
        if (location == null) {
            throw new NullPointerException();
        }
        if (owner == null) {
            throw new NullPointerException();
        }
        Unit unit = getFreeColServer().getModelController()
                .createUnit(taskID, location, owner, type, false, connection);
        Element reply = Message.createNewRootElement("createUnitConfirmed");
        reply.appendChild(unit.toXMLElement(owner, reply.getOwnerDocument()));
        return reply;
    }

    
    private Element createBuilding(Connection connection, Element element) {
        logger.info("Receiving \"createBuilding\"-request.");
        String taskID = element.getAttribute("taskID");
        Colony colony = (Colony) getGame().getFreeColGameObject(element.getAttribute("colony"));
        BuildingType type = FreeCol.getSpecification().getBuildingType(element.getAttribute("type"));
        if (colony == null) {
            throw new NullPointerException();
        }
        Building building = getFreeColServer().getModelController()
                .createBuilding(taskID, colony, type, false, connection);
        Element reply = Message.createNewRootElement("createBuildingConfirmed");
        reply.appendChild(building.toXMLElement(colony.getOwner(), reply.getOwnerDocument()));
        return reply;
    }

    
    private Element getRandom(Connection connection, Element element) {
        
        String taskID = element.getAttribute("taskID");
        int n = Integer.parseInt(element.getAttribute("n"));
        int result = getFreeColServer().getModelController().getRandom(taskID, n);
        Element reply = Message.createNewRootElement("getRandomConfirmed");
        reply.setAttribute("result", Integer.toString(result));
        
        return reply;
    }

    
    private Element getVacantEntryLocation(Connection connection, Element element) {
        Unit unit = (Unit) getGame().getFreeColGameObject(element.getAttribute("unit"));
        Player owner = unit.getOwner();
        ServerPlayer askingPlayer = getFreeColServer().getPlayer(connection);
        Location entryLocation = unit.getEntryLocation();
        if (owner != askingPlayer) {
            
            if (entryLocation == null) {
                throw new IllegalStateException("Unit " + unit.getId() + " with owner " + owner
                                                + " not owned by " + askingPlayer
                                                + ", refusing to get vacant location!");
            } else {
                logger.warning("Unit " + unit.getId() + " with owner " + owner
                               + " not owned by " + askingPlayer
                               + ", entry location is " + entryLocation.getId());
            }
        } else {
            entryLocation = getFreeColServer().getModelController().setToVacantEntryLocation(unit);
        }
        Element reply = Message.createNewRootElement("getVacantEntryLocationConfirmed");
        reply.setAttribute("location", entryLocation.getId());
        return reply;
    }

    
    private Element getNewTradeRoute(Connection connection, Element element) {
        Player player = getFreeColServer().getPlayer(connection);
        TradeRoute tradeRoute = getFreeColServer().getModelController().getNewTradeRoute(player);
        Element reply = Message.createNewRootElement("getNewTradeRouteConfirmed");
        reply.appendChild(tradeRoute.toXMLElement(player, reply.getOwnerDocument()));
        return reply;
    }

    
    private Element updateTradeRoute(Connection connection, Element element) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Element childElement = (Element) element.getChildNodes().item(0);
        TradeRoute clientTradeRoute = new TradeRoute(null, childElement);
        TradeRoute serverTradeRoute = (TradeRoute) getGame().getFreeColGameObject(clientTradeRoute.getId());
        if (serverTradeRoute == null) {
            throw new IllegalArgumentException("Could not find 'TradeRoute' with specified ID: "
                    + clientTradeRoute.getId());
        }
        if (serverTradeRoute.getOwner() != player) {
            throw new IllegalStateException("Not your trade route!");
        }
        serverTradeRoute.updateFrom(clientTradeRoute);
        return null;
    }

    
    private Element setTradeRoutes(Connection connection, Element element) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        ArrayList<TradeRoute> routes = new ArrayList<TradeRoute>();
        
        NodeList childElements = element.getChildNodes();
        for(int i = 0; i < childElements.getLength(); i++) {
            Element childElement = (Element) childElements.item(i);
            String id = childElement.getAttribute("id");
            TradeRoute serverTradeRoute = (TradeRoute) getGame().getFreeColGameObject(id);
            if (serverTradeRoute == null) {
                throw new IllegalArgumentException("Could not find 'TradeRoute' with specified ID: " + id);
            }
            if (serverTradeRoute.getOwner() != player) {
                throw new IllegalStateException("Not your trade route!");
            }
            routes.add(serverTradeRoute);
        }
        player.setTradeRoutes(routes);
        return null;
    }

    
    private Element assignTradeRoute(Connection connection, Element element) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(element.getAttribute("unit"));

        if (unit == null) {
            throw new IllegalArgumentException("Could not find 'Unit' with specified ID: "
                    + element.getAttribute("unit"));
        } else if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }

        String tradeRouteString = element.getAttribute("tradeRoute");

        if (tradeRouteString == null || tradeRouteString == "") {
            unit.setTradeRoute(null);
        } else {
            TradeRoute tradeRoute = (TradeRoute) getGame().getFreeColGameObject(tradeRouteString);

            if (tradeRoute == null) {
                throw new IllegalArgumentException("Could not find 'TradeRoute' with specified ID: "
                                                   + element.getAttribute("tradeRoute"));
            }
            if (tradeRoute.getOwner() != player) {
                throw new IllegalStateException("Not your trade route!");
            }
            unit.setTradeRoute(tradeRoute);
        }
        return null;
    }

    
    private Element abandonColony(Connection connection, Element abandonElement) {
        FreeColServer freeColServer = getFreeColServer();
        ServerPlayer player = freeColServer.getPlayer(connection);
        
        Colony colony = (Colony) getGame().getFreeColGameObject(abandonElement.getAttribute("colony"));
        
        if (colony == null) {
            throw new IllegalArgumentException("Could not find 'Colony' with specified ID: "
                    + abandonElement.getAttribute("colony"));
        }
        if (colony.getOwner() != player) {
            throw new IllegalStateException("Not your colony!");
        }

        colony.getOwner().getHistory()
            .add(new HistoryEvent(colony.getGame().getTurn().getNumber(),
                                  HistoryEvent.Type.ABANDON_COLONY,
                                  "%colony%", colony.getName()));

        Tile tile = colony.getTile();
        
        colony.dispose();
        sendUpdatedTileToAll(tile, player);
        return null;
    }

    
    private Element move(Connection connection, Element moveElement) {
        FreeColServer freeColServer = getFreeColServer();
        ServerPlayer player = freeColServer.getPlayer(connection);
        String unitID = moveElement.getAttribute("unit");
        Unit unit = (Unit) getGame().getFreeColGameObject(unitID);
        Direction direction = Enum.valueOf(Direction.class, moveElement.getAttribute("direction"));
        if (unit == null) {
            throw new IllegalArgumentException("Could not find 'Unit' with specified ID: " + unitID);
        }
        if (unit.getTile() == null) {
            throw new IllegalArgumentException("'Unit' not on map: ID: " + unitID + " ("
                    + unit.getName() + ")");
        }
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        Tile newTile = getGame().getMap().getNeighbourOrNull(direction, unit.getTile());

        for (ServerPlayer enemyPlayer : getOtherPlayers(player)) {
            try {
                if (unit.isVisibleTo(enemyPlayer)) { 
                    
                    Element opponentMoveElement = Message.createNewRootElement("opponentMove");
                    opponentMoveElement.setAttribute("fromTile", unit.getTile().getId());
                    opponentMoveElement.setAttribute("direction", direction.toString());
                    opponentMoveElement.setAttribute("unit", unit.getId());
                    enemyPlayer.getConnection().sendAndWait(opponentMoveElement);
                } else if (enemyPlayer.canSee(newTile)
                           && newTile.getSettlement() == null) {
                    
                    Element opponentMoveElement = Message.createNewRootElement("opponentMove");
                    opponentMoveElement.setAttribute("direction", direction.toString());
                    opponentMoveElement.setAttribute("toTile", newTile.getId());
                    opponentMoveElement.appendChild(unit.toXMLElement(enemyPlayer, opponentMoveElement
                            .getOwnerDocument(), false, false));
                    if (unit.isOnCarrier() && !((Unit) unit.getLocation()).isVisibleTo(enemyPlayer)) {
                        Unit location = (Unit) unit.getLocation();
                        opponentMoveElement.setAttribute("inUnit", location.getId());
                        opponentMoveElement.appendChild(location.toXMLElement(enemyPlayer, opponentMoveElement
                                .getOwnerDocument(), false, false));
                    }
                    enemyPlayer.getConnection().sendAndWait(opponentMoveElement);
                }
            } catch (IOException e) {
                logger.warning("Could not send message to: " + enemyPlayer.getName() + " with connection "
                        + enemyPlayer.getConnection());
            }
        }
        
        unit.move(direction);

        Element reply = Message.createNewRootElement("update");
        CombatModel combatModel = unit.getGame().getCombatModel();        
        
        if (unit.isNaval() && unit.getMovesLeft() > 0) {
            Iterator<Position> tileIterator = getGame().getMap().getAdjacentIterator(unit.getTile().getPosition());
            float attackPower = 0;
            Unit attacker = null;
            
            while (tileIterator.hasNext()) {
                Tile tile = getGame().getMap().getTile(tileIterator.next());
                Colony colony = tile.getColony();
                
                if (colony != null) {
                    
                } else if (!tile.isLand() && tile.getFirstUnit() != null) {
                    Player enemy = tile.getFirstUnit().getOwner();
                    if (player == enemy) { 
                        continue;
                    }
                    
                    for (Unit enemyUnit : tile.getUnitList()) {
                        if (enemyUnit.isOffensiveUnit() && (player.getStance(enemy) == Stance.WAR
                                || enemyUnit.hasAbility("model.ability.piracy")
                                || unit.hasAbility("model.ability.piracy"))) {
                            attackPower += combatModel.getOffencePower(enemyUnit, unit);
                            if (attacker == null) {
                                attacker = enemyUnit;
                            }
                        }
                    }
                }
            }
            
            if (attackPower > 0) {
                
                
                assert attacker != null;
                float defencePower = combatModel.getDefencePower(attacker, unit);
                float totalProbability = attackPower + defencePower;
                int r = getPseudoRandom().nextInt(Math.round(totalProbability) + 1);
                if (r < attackPower) {
                    int diff = Math.max(0, Math.round(attackPower - defencePower));
                    int moves = Math.min(9, 3 + diff / 3);
                    unit.setMovesLeft(unit.getMovesLeft() - moves);
                    reply.setAttribute("movesSlowed", Integer.toString(moves));
                    reply.setAttribute("slowedBy", attacker.getId());
                }
            }
        }
        

        if (player.isEuropean()) {
            Region region = newTile.getDiscoverableRegion();
            if (region != null &&
                (region.isPacific() ||
                 getGame().getGameOptions().getBoolean(GameOptions.EXPLORATION_POINTS))) {
                String name;
                if (region.isPacific()) {
                    name = region.getDisplayName();
                } else {
                    name = moveElement.getAttribute("regionName");
                    if (name == null || "".equals(name)) {
                        name = player.getDefaultRegionName(region.getType());
                    }
                }
                region.discover(player, getGame().getTurn(), name);
                reply.appendChild(region.toXMLElement(player, reply.getOwnerDocument()));

                Element updateElement = Message.createNewRootElement("update");
                updateElement.appendChild(region.toXMLElement(player, updateElement.getOwnerDocument()));
                freeColServer.getServer().sendToAll(updateElement, player.getConnection());
            }
        }


        if (newTile.hasLostCityRumour() && player.isEuropean()) {
            exploreLostCityRumour(unit, player);
        }
        Document doc = reply.getOwnerDocument();
        reply.appendChild(newTile.toXMLElement(player, doc, false, false));
        for (Tile t : getGame().getMap().getSurroundingTiles(unit.getTile(), unit.getLineOfSight())) {
            reply.appendChild(t.toXMLElement(player, doc, false, false));
        }
        return reply;
    }

    
    private void exploreLostCityRumour(Unit unit, ServerPlayer player) {

        Tile tile = unit.getTile();        

        Specification specification = FreeCol.getSpecification();
        List<UnitType> learntUnitTypes = unit.getType().getUnitTypesLearntInLostCity();
        List<UnitType> newUnitTypes = specification.getUnitTypesWithAbility("model.ability.foundInLostCity");
        List<UnitType> treasureUnitTypes = specification.getUnitTypesWithAbility("model.ability.carryTreasure");

        int level = Specification.getSpecification().getRangeOption("model.option.difficulty").getValue();

        RumourType rumour = tile.getLostCityRumour().getType();

        if (rumour == null) {

            
        
            
            
            
            
            final int BAD_EVENT_PERCENTAGE[]  = { 11, 17, 23, 30, 37};
            final int GOOD_EVENT_PERCENTAGE[] = { 75, 62, 48, 33, 17};
            
        
            
            
            
            final int BAD_EVENT_MOD[]  = {-6, -7, -7, -8, -9};
            final int GOOD_EVENT_MOD[] = {14, 15, 16, 18, 20};

            
            boolean isExpertScout = unit.hasAbility("model.ability.expertScout") && 
                unit.hasAbility("model.ability.scoutIndianSettlement");
            boolean hasDeSoto = player.hasAbility("model.ability.rumoursAlwaysPositive");

            
            int percentNeutral = 0;
            int percentBad;
            int percentGood;
        
            if (hasDeSoto) {
                percentBad     = 0;
                percentGood    = 100;
            } else {
                
                percentBad  = BAD_EVENT_PERCENTAGE[level];
                percentGood = GOOD_EVENT_PERCENTAGE[level];
        
                
                if (isExpertScout) {
                    percentBad += BAD_EVENT_MOD[level];
                    percentGood += GOOD_EVENT_MOD[level];
                }

                
                
                if (percentBad+percentGood<100) {
                    percentNeutral = 100-percentBad-percentGood;
                }
            }

            
            
            int eventNothing = 100;

            
            int eventVanish = 100;
            int eventBurialGround = 0;
            
            if (tile.getOwner() != null && !tile.getOwner().isEuropean()) {
                eventVanish = 75;
                eventBurialGround = 25;
            }

            
            int eventLearn    = 30;
            int eventTrinkets = 30;
            int eventColonist = 20;
            
            if (learntUnitTypes.isEmpty()) {
                eventLearn    =  0;
                eventTrinkets = 50;
                eventColonist = 30;
            }

            
            
            
            int eventDorado   = 13;
            int eventFountain =  7;

            
            
            
            eventNothing      *= percentNeutral;
            eventVanish       *= percentBad;
            eventBurialGround *= percentBad;
            eventLearn        *= percentGood;
            eventTrinkets     *= percentGood;
            eventColonist     *= percentGood;
            eventDorado       *= percentGood;
            eventFountain     *= percentGood;

            
            List<RandomChoice<RumourType>> choices = new ArrayList<RandomChoice<RumourType>>();
            if (eventNothing>0) {
                choices.add(new RandomChoice<RumourType>(RumourType.NOTHING, eventNothing));
            }
            if (eventVanish>0) {
                choices.add(new RandomChoice<RumourType>(RumourType.EXPEDITION_VANISHES, eventVanish));
            }
            if (eventBurialGround>0) {
                choices.add(new RandomChoice<RumourType>(RumourType.BURIAL_GROUND, eventBurialGround));
            }
            if (eventLearn>0) {
                choices.add(new RandomChoice<RumourType>(RumourType.LEARN, eventLearn));
            }
            if (eventTrinkets>0) {
                choices.add(new RandomChoice<RumourType>(RumourType.TRIBAL_CHIEF, eventTrinkets));
            }
            if (eventColonist>0) {
                choices.add(new RandomChoice<RumourType>(RumourType.COLONIST, eventColonist));
            }
            if (eventFountain>0) {
                choices.add(new RandomChoice<RumourType>(RumourType.FOUNTAIN_OF_YOUTH, eventFountain));
            }
            if (eventDorado>0) {
                choices.add(new RandomChoice<RumourType>(RumourType.TREASURE, eventDorado));
            }
            rumour = RandomChoice.getWeightedRandom(getPseudoRandom(), choices);
        } else {
            if (rumour == RumourType.LEARN && learntUnitTypes.isEmpty()) {
                rumour = RumourType.NOTHING;
            }
        }

        Element rumourElement = Message.createNewRootElement("lostCityRumour");
        rumourElement.setAttribute("type", rumour.toString());
        rumourElement.setAttribute("unit", unit.getId());
        Unit newUnit;
        int random;
        int dx = 10 - level;
        switch (rumour) {
        case BURIAL_GROUND:
            Player indianPlayer = tile.getOwner();
            indianPlayer.modifyTension(player, Tension.Level.HATEFUL.getLimit());
            break;
        case EXPEDITION_VANISHES:
            unit.dispose();
            break;
        case NOTHING:
            break;
        case LEARN:
            random = getPseudoRandom().nextInt(learntUnitTypes.size());
            unit.setType(learntUnitTypes.get(random));
            rumourElement.setAttribute("unitType", learntUnitTypes.get(random).getId());
            break;
        case TRIBAL_CHIEF:
            int amount = getPseudoRandom().nextInt(dx * 10) + dx * 5;
            player.modifyGold(amount);
            rumourElement.setAttribute("amount", Integer.toString(amount));
            break;
        case COLONIST:
            random = getPseudoRandom().nextInt(newUnitTypes.size());
            newUnit = new Unit(getGame(), tile, player, newUnitTypes.get(random), UnitState.ACTIVE);
            rumourElement.appendChild(newUnit.toXMLElement(player, rumourElement.getOwnerDocument()));
            break;
        case TREASURE:
            int treasure = getPseudoRandom().nextInt(dx * 600) + dx * 300;
            random = getPseudoRandom().nextInt(treasureUnitTypes.size());
            newUnit = new Unit(getGame(), tile, player, treasureUnitTypes.get(random), UnitState.ACTIVE);
            newUnit.setTreasureAmount(treasure);
            rumourElement.setAttribute("amount", Integer.toString(treasure));
            rumourElement.appendChild(newUnit.toXMLElement(player, rumourElement.getOwnerDocument()));
            player.getHistory().add(new HistoryEvent(getGame().getTurn().getNumber(),
                                                     HistoryEvent.Type.CITY_OF_GOLD,
                                                     "%treasure%", String.valueOf(treasure)));
            break;
        case FOUNTAIN_OF_YOUTH:
            if (player.getEurope() != null) {
                if (player.hasAbility("model.ability.selectRecruit")) {
                    player.setRemainingEmigrants(dx);
                    rumourElement.setAttribute("emigrants", Integer.toString(dx));
                } else {
                    for (int k = 0; k < dx; k++) {
                        newUnit = new Unit(getGame(), player.getEurope(), player,
                                           player.generateRecruitable(player.getId() + "fountain." + Integer.toString(k)),
                                           UnitState.ACTIVE);
                        rumourElement.appendChild(newUnit.toXMLElement(player, rumourElement.getOwnerDocument()));
                    }
                }
            }
            break;
        default:
            throw new IllegalStateException("No such rumour.");
        }
        
        try {
            player.getConnection().sendAndWait(rumourElement);
        } catch (IOException e) {
            logger.warning("Could not send rumour message to: " + player.getName() + " with connection "
                    + player.getConnection());
        }
        tile.removeLostCityRumour();
        
        for (ServerPlayer updatePlayer : getOtherPlayers(player)) {
            if (updatePlayer.canSee(tile)) {
                try {
                    Element rumourUpdate = Message.createNewRootElement("update");
                    rumourUpdate.appendChild(tile.toXMLElement(updatePlayer, rumourUpdate.getOwnerDocument()));
                    updatePlayer.getConnection().sendAndWait(rumourUpdate);
                } catch (IOException e) {
                    logger.warning("Could not send update message to: " + updatePlayer.getName() + " with connection "
                                   + updatePlayer.getConnection());
                }
            }
        }
    }
    
    
    private Element selectFromFountainYouth(Connection connection, Element element) {
        FreeColServer freeColServer = getFreeColServer();
        ServerPlayer player = freeColServer.getPlayer(connection);
        int remaining = player.getRemainingEmigrants();
        if (remaining == 0) {
            throw new IllegalStateException("There is no remaining emigrants for this player.");
        }
        player.setRemainingEmigrants(remaining-1);
        
        Europe europe = player.getEurope();
        int slot = Integer.parseInt(element.getAttribute("slot"));
        UnitType recruitable = europe.getRecruitable(slot);
        UnitType newRecruitable = player.generateRecruitable(player.getId() + "slot." + Integer.toString(slot));
        europe.setRecruitable(slot, newRecruitable);
        
        Unit unit = new Unit(getGame(), europe, player, recruitable, UnitState.ACTIVE);
        Element reply = Message.createNewRootElement("selectFromFountainYouthConfirmed");
        reply.setAttribute("newRecruitable", newRecruitable.getId());
        reply.appendChild(unit.toXMLElement(player, reply.getOwnerDocument()));
        return reply;
    }

    
    private Element askSkill(Connection connection, Element element) {
        FreeColServer freeColServer = getFreeColServer();
        Map map = getGame().getMap();
        ServerPlayer player = freeColServer.getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(element.getAttribute("unit"));
        Direction direction = Enum.valueOf(Direction.class, element.getAttribute("direction"));
        if (unit == null) {
            throw new IllegalArgumentException("Could not find 'Unit' with specified ID: "
                    + element.getAttribute("unit"));
        }
        if (unit.getMovesLeft() == 0) {
            throw new IllegalArgumentException("Unit has no moves left.");
        }
        if (unit.getTile() == null) {
            throw new IllegalArgumentException("'Unit' not on map: ID: " + element.getAttribute("unit"));
        }
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        IndianSettlement settlement = (IndianSettlement) map.getNeighbourOrNull(direction, unit.getTile())
                .getSettlement();
        
        unit.setMovesLeft(0);
        Element reply = Message.createNewRootElement("provideSkill");
        if (settlement.getLearnableSkill() != null) {
            reply.setAttribute("skill", settlement.getLearnableSkill().getId());
        }
        
        settlement.getTile().updateIndianSettlementSkill(player);
        return reply;
    }

    
    private Element attack(Connection connection, Element attackElement) {
        FreeColServer freeColServer = getFreeColServer();
        ServerPlayer player = freeColServer.getPlayer(connection);
        
        String unitID = attackElement.getAttribute("unit");
        Unit unit = (Unit) getGame().getFreeColGameObject(unitID);
        Direction direction = Enum.valueOf(Direction.class, attackElement.getAttribute("direction"));
        
        if (unit == null) {
            throw new IllegalArgumentException("Could not find 'Unit' with specified ID: " + unitID);
        }
        if (unit.getTile() == null) {
            throw new IllegalArgumentException("'Unit' is not on the map: " + unit.toString());
        }
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        Tile newTile = getGame().getMap().getNeighbourOrNull(direction, unit.getTile());
        if (newTile == null) {
            throw new IllegalArgumentException("Could not find tile in direction " + direction + " from unit with ID "
                    + unitID);
        }
        CombatResult result;
        int plunderGold = -1;
        Unit defender = newTile.getDefendingUnit(unit);
        Player defendingPlayer = null;
        if (defender == null) {
            if (newTile.getSettlement() != null) {
                defendingPlayer = newTile.getSettlement().getOwner();
                result = new CombatResult(CombatResultType.DONE_SETTLEMENT, 0);
            } else {
                throw new IllegalStateException("Nothing to attack in direction " + direction + " from unit with ID "
                        + unitID);
            }
        } else {
            defendingPlayer = defender.getOwner();
            result = unit.getGame().getCombatModel().generateAttackResult(unit, defender); 
        }
        if (result.type == CombatResultType.DONE_SETTLEMENT) {
            Settlement s = newTile.getSettlement();
            if (s instanceof Colony) {
                
                plunderGold = (s.getOwner().getGold()*s.getUnitCount())/s.getOwner().getColoniesPopulation();
            } else {
                
                plunderGold = s.getOwner().getGold() / 10;
            }
        }
        
        
        Location repairLocation = null;
        
        Player loserOwner = null;
        switch (result.type) {
        case WIN:
            if (defender.isNaval()) {
                loserOwner = defendingPlayer;
                repairLocation = loserOwner.getRepairLocation(defender);
            }
            break;
        case DONE_SETTLEMENT:
            for (Unit victim : newTile.getUnitList()) {
                if (victim.isNaval()) {
                    loserOwner = victim.getOwner();
                    repairLocation = loserOwner.getRepairLocation(victim);
                    break;
                }
            }
            break;
        case LOSS:
            if (unit.isNaval()) {
                loserOwner = player;
                repairLocation = loserOwner.getRepairLocation(unit);
            }
            break;
        case EVADES:
        case GREAT_LOSS:
        case GREAT_WIN:
            
            break;
        }
        
        
        
        for (ServerPlayer enemyPlayer : getOtherPlayers(player)) {
            Element opponentAttackElement = Message.createNewRootElement("opponentAttack");
            if (unit.isVisibleTo(enemyPlayer) || defender.isVisibleTo(enemyPlayer)) {
                opponentAttackElement.setAttribute("direction", direction.toString());
                opponentAttackElement.setAttribute("result", result.type.toString());
                opponentAttackElement.setAttribute("damage", String.valueOf(result.damage));
                opponentAttackElement.setAttribute("plunderGold", Integer.toString(plunderGold));
                opponentAttackElement.setAttribute("unit", unit.getId());
                opponentAttackElement.setAttribute("defender", defender.getId());
                
                if (defender.getOwner() == enemyPlayer) {
                	
                	if(repairLocation != null && loserOwner == defender.getOwner()){
                		opponentAttackElement.setAttribute("repairIn", repairLocation.getId());
                	}
                    
                	opponentAttackElement.setAttribute("update", "unit");
                	
                    opponentAttackElement.appendChild(unit.toXMLElement(enemyPlayer,
                            opponentAttackElement.getOwnerDocument(),false,false));
                } else if (!defender.isVisibleTo(enemyPlayer)) {
                    opponentAttackElement.setAttribute("update", "defender");
                    
                    opponentAttackElement.setAttribute("defenderTile", defender.getTile().getId());
                    if (!enemyPlayer.canSee(defender.getTile())) {
                        enemyPlayer.setExplored(defender.getTile());
                        opponentAttackElement.appendChild(defender.getTile()
                            .toXMLElement(enemyPlayer, opponentAttackElement.getOwnerDocument()));
                    }
                	
                	
                	
                    opponentAttackElement.appendChild(defender.toXMLElement(enemyPlayer,
                            opponentAttackElement.getOwnerDocument(),false,false));
                } else if (!unit.isVisibleTo(enemyPlayer)) {
                	
                	
                	
                    opponentAttackElement.setAttribute("update", "unit");
                    Element unitElm = unit.toXMLElement(enemyPlayer,opponentAttackElement.getOwnerDocument(),false,false);
                    opponentAttackElement.appendChild(unitElm);
                }
                try {
                    enemyPlayer.getConnection().sendAndWait(opponentAttackElement);
                } catch (IOException e) {
                    logger.warning("Could not send message to: " + enemyPlayer.getName()
                                   + " with connection " + enemyPlayer.getConnection());
                }
            }
        }
        
        Element reply = Message.createNewRootElement("attackResult");
        reply.setAttribute("result", result.type.toString());
        reply.setAttribute("damage", String.valueOf(result.damage));
        reply.setAttribute("plunderGold", Integer.toString(plunderGold));
        
        
        if(repairLocation != null && player == loserOwner){
        	reply.setAttribute("repairIn", repairLocation.getId());
        }
        
        if (result.type == CombatResultType.DONE_SETTLEMENT && newTile.getColony() != null) {
            
            reply.appendChild(newTile.toXMLElement(newTile.getColony().getOwner(), reply.getOwnerDocument()));
            reply.appendChild(defender.toXMLElement(newTile.getColony().getOwner(), reply.getOwnerDocument()));
        } else {
        	
        	
        	
            reply.appendChild(defender.toXMLElement(player, reply.getOwnerDocument(), false, false));
        }
        
        
        
        boolean isIndianCapitalBurned=false;
        if(result.type == CombatResultType.DONE_SETTLEMENT && 
        		newTile.getSettlement() instanceof IndianSettlement &&
        		((IndianSettlement) newTile.getSettlement()).isCapital()) {
        	isIndianCapitalBurned = true;
        	reply.setAttribute("indianCapitalBurned", Boolean.toString(isIndianCapitalBurned));
        }
        
        int oldUnits = unit.getTile().getUnitCount();
        
        
        unit.getGame().getCombatModel().attack(unit, defender, result, plunderGold, repairLocation);
        if(isIndianCapitalBurned){
        	defendingPlayer.surrenderTo(player);
        }
        
        if (result.type.compareTo(CombatResultType.WIN) >= 0 
            && unit.getTile() != newTile
            && oldUnits < unit.getTile().getUnitCount()) {
            
            
            
            Unit lastUnit = unit.getTile().getLastUnit();
            if (!lastUnit.getOwner().isEuropean()) {
                Element convertElement = reply.getOwnerDocument().createElement("convert");
                convertElement.appendChild(lastUnit.toXMLElement(unit.getOwner(), reply.getOwnerDocument()));
                reply.appendChild(convertElement);
            }
        }
        
        if (result.type.compareTo(CombatResultType.EVADES) >= 0 && unit.getTile().equals(newTile)) {
            
            Element update = reply.getOwnerDocument().createElement("update");
            int lineOfSight = unit.getLineOfSight();
            if (result.type == CombatResultType.DONE_SETTLEMENT && newTile.getSettlement() != null) {
                lineOfSight = Math.max(lineOfSight, newTile.getSettlement().getLineOfSight());
            }
            List<Tile> surroundingTiles = getGame().getMap().getSurroundingTiles(unit.getTile(), lineOfSight);
            for (int i = 0; i < surroundingTiles.size(); i++) {
                Tile t = surroundingTiles.get(i);
                update.appendChild(t.toXMLElement(player, update.getOwnerDocument()));
            }
            update.appendChild(unit.getTile().toXMLElement(player, update.getOwnerDocument()));
            reply.appendChild(update);
        }
        return reply;
    }

    
    private Element embark(Connection connection, Element embarkElement) {
        FreeColServer freeColServer = getFreeColServer();
        ServerPlayer player = freeColServer.getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(embarkElement.getAttribute("unit"));
        Direction direction = Enum.valueOf(Direction.class, embarkElement.getAttribute("direction"));
        Unit destinationUnit = (Unit) getGame().getFreeColGameObject(embarkElement.getAttribute("embarkOnto"));
        if (unit == null || destinationUnit == null
                || getGame().getMap().getNeighbourOrNull(direction, unit.getTile()) != destinationUnit.getTile()) {
            throw new IllegalArgumentException("Invalid data format in client message.");
        }
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        Tile oldTile = unit.getTile();
        unit.embark(destinationUnit);
        sendRemoveUnitToAll(unit, player);
        return null;
    }

    
    private Element boardShip(Connection connection, Element boardShipElement) {
        FreeColServer freeColServer = getFreeColServer();
        ServerPlayer player = freeColServer.getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(boardShipElement.getAttribute("unit"));
        Unit carrier = (Unit) getGame().getFreeColGameObject(boardShipElement.getAttribute("carrier"));
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        Tile oldTile = unit.getTile();
        boolean tellEnemyPlayers = true;
        if (oldTile == null || oldTile.getSettlement() != null) {
            tellEnemyPlayers = false;
        }
        if (unit.isNaval()) {
            logger.warning("Tried to load a ship onto another carrier.");
            return null;
        }
        unit.boardShip(carrier);
        if (tellEnemyPlayers) {
            sendRemoveUnitToAll(unit, player);
        }
        
        sendUpdatedTileToAll(unit.getTile(), player);
        return null;
    }

    
    private Element learnSkillAtSettlement(Connection connection, Element element) {
        FreeColServer freeColServer = getFreeColServer();
        Map map = getGame().getMap();
        ServerPlayer player = freeColServer.getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(element.getAttribute("unit"));
        Direction direction = Enum.valueOf(Direction.class, element.getAttribute("direction"));
        boolean cancelAction = false;
        if (element.getAttribute("action").equals("cancel")) {
            cancelAction = true;
        }
        if (unit.getTile() == null) {
            throw new IllegalArgumentException("'Unit' not on map: ID: " + element.getAttribute("unit"));
        }
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        Tile tile = map.getNeighbourOrNull(direction, unit.getTile());
        IndianSettlement settlement = (IndianSettlement) tile.getSettlement();
        if (settlement == null) {
            throw new IllegalStateException("No settlement to learn skill from.");
        }
        if (!unit.getType().canBeUpgraded(settlement.getLearnableSkill(), ChangeType.NATIVES)) {
            throw new IllegalStateException("Unit can't learn that skill from settlement!");
        }
        
        Element reply = Message.createNewRootElement("learnSkillResult");
        if (!cancelAction) {
            Tension tension = settlement.getAlarm(player);
            if (tension == null) {
                tension = new Tension(0);
            }
            switch (tension.getLevel()) {
            case HATEFUL:
                reply.setAttribute("result", "die");
                unit.dispose();
                break;
            case ANGRY:
                reply.setAttribute("result", "leave");
                break;
            default:
                unit.learnFromIndianSettlement(settlement);
                
                settlement.getTile().updateIndianSettlementSkill(player);
                reply.setAttribute("result", "success");
            }
        } else {
            reply.setAttribute("result", "cancelled");
        }
        return reply;
    }

    
    private Element scoutIndianSettlement(Connection connection, Element element) {
        FreeColServer freeColServer = getFreeColServer();
        Map map = getGame().getMap();
        ServerPlayer player = freeColServer.getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(element.getAttribute("unit"));
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        Direction direction = Enum.valueOf(Direction.class, element.getAttribute("direction"));
        String action = element.getAttribute("action");
        IndianSettlement settlement = (IndianSettlement) map.getNeighbourOrNull(direction, unit.getTile()).getSettlement();
        Element reply = Message.createNewRootElement("scoutIndianSettlementResult");
        if (action.equals("basic")) {
            unit.contactAdjacent(settlement.getTile());
            unit.setMovesLeft(0);
            
            UnitType skill = settlement.getLearnableSkill();
            if (skill != null) {
                reply.setAttribute("skill", skill.getId());
            }
            settlement.updateWantedGoods();
            GoodsType[] wantedGoods = settlement.getWantedGoods();
            reply.setAttribute("highlyWantedGoods", wantedGoods[0].getId());
            reply.setAttribute("wantedGoods1", wantedGoods[1].getId());
            reply.setAttribute("wantedGoods2", wantedGoods[2].getId());
            reply.setAttribute("numberOfCamps", String.valueOf(settlement.getOwner().getSettlements().size()));
            for (Tile tile : getGame().getMap().getSurroundingTiles(settlement.getTile(), unit.getLineOfSight())) {
                reply.appendChild(tile.toXMLElement(player, reply.getOwnerDocument()));
            }
            
            settlement.getTile().updateIndianSettlementInformation(player);
        } else if (action.equals("cancel")) {
            return null;
        } else if (action.equals("attack")) {
            
            
            
            unit.setMovesLeft(1);
            return null;
        } else if (settlement.getAlarm(player) != null &&
                   settlement.getAlarm(player).getLevel() == Tension.Level.HATEFUL) {
            reply.setAttribute("result", "die");
            unit.dispose();
        } else if (action.equals("speak")) {
            unit.contactAdjacent(settlement.getTile());
            if (!settlement.hasBeenVisited()) {
                if (settlement.getLearnableSkill() != null
                    && settlement.getLearnableSkill().hasAbility("model.ability.expertScout")
                    && !unit.hasAbility("model.ability.expertScout")) {
                    unit.setType(settlement.getLearnableSkill());
                    reply.setAttribute("result", "expert");
                    Element update = reply.getOwnerDocument().createElement("update");
                    update.appendChild(unit.toXMLElement(player, update.getOwnerDocument(), false, false));
                    reply.appendChild(update);
                } else if (getPseudoRandom().nextInt(9) < 3) {
                    reply.setAttribute("result", "tales");
                    Element update = reply.getOwnerDocument().createElement("update");
                    Position center = new Position(settlement.getTile().getX(), settlement.getTile().getY());
                    Iterator<Position> circleIterator = map.getCircleIterator(center, true, 6);
                    while (circleIterator.hasNext()) {
                        Position position = circleIterator.next();
                        if ((!position.equals(center))
                                && (map.getTile(position).isLand() || map.getTile(position).isCoast())) {
                            Tile t = map.getTile(position);
                            player.setExplored(t);
                            update.appendChild(t.toXMLElement(player, update.getOwnerDocument(), false, false));
                        }
                    }
                    reply.appendChild(update);
                } else {
                    int beadsGold = (getPseudoRandom().nextInt(400) * settlement.getBonusMultiplier()) + 50;
                    if (unit.hasAbility("model.ability.expertScout")) {
                        beadsGold = (beadsGold * 11) / 10;
                    }
                    reply.setAttribute("result", "beads");
                    reply.setAttribute("amount", Integer.toString(beadsGold));
                    player.modifyGold(beadsGold);
                }
                settlement.setVisited(player);
            } else {
                reply.setAttribute("result", "nothing");
            }
        } else if (action.equals("tribute")) {
            unit.contactAdjacent(settlement.getTile());
            demandTribute(settlement, player, reply);
        }
        return reply;
    }

    
    private Element armedUnitDemandTribute(Connection connection, Element element) {
        FreeColServer freeColServer = getFreeColServer();
        ServerPlayer player = freeColServer.getPlayer(connection);
        
        Unit unit = (Unit) getGame().getFreeColGameObject(element.getAttribute("unit"));
        Direction direction = Enum.valueOf(Direction.class, element.getAttribute("direction"));
        
        if (unit == null) {
            throw new IllegalArgumentException("Could not find 'Unit' with specified ID: "
                    + element.getAttribute("unit"));
        }
        if (unit.getTile() == null) {
            throw new IllegalArgumentException("'Unit' is not on the map: " + unit.toString());
        }
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        Tile newTile = getGame().getMap().getNeighbourOrNull(direction, unit.getTile());
        if (newTile == null) {
            throw new IllegalArgumentException("Could not find tile in direction " + direction + " from unit with ID "
                    + element.getAttribute("unit"));
        }
        unit.setMovesLeft(0);
        IndianSettlement settlement = (IndianSettlement) newTile.getSettlement();
        Element reply = Message.createNewRootElement("armedUnitDemandTributeResult");
        demandTribute(settlement, player, reply);
        return reply;
    }


    
    private void demandTribute(IndianSettlement settlement, Player player, Element reply) {
        int gold = settlement.getTribute(player);
        if (gold > 0) {
            reply.setAttribute("result", "agree");
            reply.setAttribute("amount", String.valueOf(gold));
            player.modifyGold(gold);
        } else {
            reply.setAttribute("result", "disagree");
        }
    }


    
    private Element missionaryAtSettlement(Connection connection, Element element) {
        FreeColServer freeColServer = getFreeColServer();
        InGameController inGameController = freeColServer.getInGameController();
        Map map = getGame().getMap();
        ServerPlayer player = freeColServer.getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(element.getAttribute("unit"));
        Direction direction = Enum.valueOf(Direction.class, element.getAttribute("direction"));
        String action = element.getAttribute("action");
        IndianSettlement settlement = (IndianSettlement) map.getNeighbourOrNull(direction, unit.getTile())
                .getSettlement();
        Tension tension = settlement.getAlarm(unit.getOwner());
        unit.setMovesLeft(0);
        if (action.equals("cancel")) {
            return null;
        } else if (tension == null) {
            return null;
        } else if (action.equals("establish")) {
            sendRemoveUnitToAll(unit, player);
            
            boolean success = inGameController.createMission(settlement,unit);
            
            Element reply = Message.createNewRootElement("missionaryReply");
            reply.setAttribute("success", String.valueOf(success));
            reply.setAttribute("tension", tension.getLevel().toString());
            return reply;
        } else if (action.equals("heresy")) {
            Element reply = Message.createNewRootElement("missionaryReply");
            sendRemoveUnitToAll(unit, player);
            double random = Math.random() * settlement.getMissionary().getOwner().getImmigration() /
                (unit.getOwner().getImmigration() + 1);
            if (settlement.getMissionary().hasAbility("model.ability.expertMissionary")) {
                random += 0.2;
            }
            if (unit.hasAbility("model.ability.expertMissionary")) {
                random -= 0.2;
            }
            if (random < 0.5) {
            	boolean success = inGameController.createMission(settlement,unit);
            	reply.setAttribute("success", String.valueOf(success));
            	reply.setAttribute("tension", tension.getLevel().toString());    
            } else {
                reply.setAttribute("success", "false");
                unit.dispose();
            }
            return reply;
        } else if (action.equals("incite")) {
            Element reply = Message.createNewRootElement("missionaryReply");
            Player enemy = (Player) getGame().getFreeColGameObject(element.getAttribute("incite"));
            reply.setAttribute("amount", String.valueOf(Game.getInciteAmount(player, enemy, settlement.getOwner())));
            
            
            unit.setLocation(settlement);
            return reply;
        } else {
            return null;
        }
    }

    
    private Element inciteAtSettlement(Connection connection, Element element) {
        FreeColServer freeColServer = getFreeColServer();
        Map map = getGame().getMap();
        ServerPlayer player = freeColServer.getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(element.getAttribute("unit"));
        Direction direction = Enum.valueOf(Direction.class, element.getAttribute("direction"));
        String confirmed = element.getAttribute("confirmed");
        IndianSettlement settlement = (IndianSettlement) unit.getTile().getSettlement();
        
        unit.setLocation(map.getNeighbourOrNull(direction.getReverseDirection(), unit.getTile()));
        if (confirmed.equals("true")) {
            Player enemy = (Player) getGame().getFreeColGameObject(element.getAttribute("enemy"));
            int amount = Game.getInciteAmount(player, enemy, settlement.getOwner());
            if (player.getGold() < amount) {
                throw new IllegalStateException("Not enough gold to incite indians!");
            } else {
                player.modifyGold(-amount);
            }
            
            
            settlement.getOwner().changeRelationWithPlayer(enemy, Stance.WAR);
            
            settlement.modifyAlarm(enemy, 1000); 
            enemy.modifyTension(settlement.getOwner(), 500);
            enemy.modifyTension(player, 250);
        }
        
        return null;
    }

    
    private Element loadCargo(Connection connection, Element loadCargoElement) {
        Unit carrier = (Unit) getGame().getFreeColGameObject(loadCargoElement.getAttribute("carrier"));
        Goods goods = new Goods(getGame(), (Element) loadCargoElement.getChildNodes().item(0));
        goods.loadOnto(carrier);
        return null;
    }

    
    private Element unloadCargo(Connection connection, Element unloadCargoElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Goods goods = new Goods(getGame(), (Element) unloadCargoElement.getChildNodes().item(0));
        if (goods.getLocation() instanceof Unit && ((Unit) goods.getLocation()).getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        if (goods.getLocation() instanceof Unit && ((Unit) goods.getLocation()).getColony() != null) {
            goods.unload();
        } else {
            goods.setLocation(null);
        }
        return null;
    }

    
    private Element buyGoods(Connection connection, Element buyGoodsElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Unit carrier = (Unit) getGame().getFreeColGameObject(buyGoodsElement.getAttribute("carrier"));
        GoodsType type = FreeCol.getSpecification().getGoodsType(buyGoodsElement.getAttribute("type"));
        int amount = Integer.parseInt(buyGoodsElement.getAttribute("amount"));
        if (carrier.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        if (carrier.getOwner() != player) {
            throw new IllegalStateException();
        }
        carrier.buyGoods(type, amount);
       
        Element marketElement = Message.createNewRootElement("marketElement");
        marketElement.setAttribute("type", type.getId());
        marketElement.setAttribute("amount", String.valueOf(-amount/4));
        getFreeColServer().getServer().sendToAll(marketElement, player.getConnection());
        return null;
    }

    
    private Element sellGoods(Connection connection, Element sellGoodsElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Goods goods = new Goods(getGame(), (Element) sellGoodsElement.getChildNodes().item(0));
        if (goods.getLocation() instanceof Unit && ((Unit) goods.getLocation()).getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        player.getMarket().sell(goods, player);

        Element marketElement = Message.createNewRootElement("marketElement");
        marketElement.setAttribute("type", goods.getType().getId());
        marketElement.setAttribute("amount", String.valueOf(goods.getAmount()/4));
        getFreeColServer().getServer().sendToAll(marketElement, player.getConnection());
        return null;
    }

    
    private Element moveToEurope(Connection connection, Element moveToEuropeElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(moveToEuropeElement.getAttribute("unit"));
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        
        sendRemoveUnitToAll(unit, player);
        
        Tile oldTile = unit.getTile();
        unit.moveToEurope();
        return null;
    }

    
    private Element moveToAmerica(Connection connection, Element moveToAmericaElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(moveToAmericaElement.getAttribute("unit"));
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        unit.moveToAmerica();
        return null;
    }

    
    private Element recruitUnitInEurope(Connection connection, Element recruitUnitInEuropeElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Europe europe = player.getEurope();
        int slot = Integer.parseInt(recruitUnitInEuropeElement.getAttribute("slot"));
        UnitType recruitable = europe.getRecruitable(slot);
        UnitType newRecruitable = player.generateRecruitable(player.getId() + "slot." + Integer.toString(slot));
        Unit unit = new Unit(getGame(), europe, player, recruitable, UnitState.ACTIVE, recruitable.getDefaultEquipment());
        Element reply = Message.createNewRootElement("recruitUnitInEuropeConfirmed");
        reply.setAttribute("newRecruitable", newRecruitable.getId());
        reply.appendChild(unit.toXMLElement(player, reply.getOwnerDocument()));
        europe.recruit(slot, unit, newRecruitable);
        return reply;
    }

    
    private Element trainUnitInEurope(Connection connection, Element trainUnitInEuropeElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Europe europe = player.getEurope();
        String unitId = trainUnitInEuropeElement.getAttribute("unitType");
        UnitType unitType = FreeCol.getSpecification().getUnitType(unitId);
        Unit unit = new Unit(getGame(), europe, player, unitType, UnitState.ACTIVE, unitType.getDefaultEquipment());
        Element reply = Message.createNewRootElement("trainUnitInEuropeConfirmed");
        reply.appendChild(unit.toXMLElement(player, reply.getOwnerDocument()));
        europe.train(unit);
        return reply;
    }

    
    private Element equipUnit(Connection connection, Element workElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(workElement.getAttribute("unit"));
        String typeString = workElement.getAttribute("type");
        EquipmentType type = FreeCol.getSpecification().getEquipmentType(typeString);
        int amount = Integer.parseInt(workElement.getAttribute("amount"));
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }

        if (amount > 0) {
            unit.equipWith(type, amount);
        } else {
            unit.removeEquipment(type, -amount);
        }

        if (unit.getLocation() instanceof Tile) {
            sendUpdatedTileToAll(unit.getTile(), player);
        }
        return null;
    }

    
    private Element work(Connection connection, Element workElement) {
        ServerPlayer serverPlayer = getFreeColServer().getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(workElement.getAttribute("unit"));
        WorkLocation workLocation = (WorkLocation) getGame().getFreeColGameObject(workElement.getAttribute("workLocation"));
        if (unit.getOwner() != serverPlayer) {
            throw new IllegalStateException("Not your unit!");
        }
        if (workLocation == null) {
            throw new NullPointerException();
        }
        if (!workLocation.canAdd(unit)) {
            throw new IllegalStateException("Can not add " + unit.getName() + "(" + unit.getId()
                                            + ") to " + workLocation.toString() + "(" 
                                            + workLocation.getId() + ")");
        }
        if (workLocation instanceof ColonyTile) {
            Tile tile = ((ColonyTile) workLocation).getWorkTile();
            Colony colony = workLocation.getColony();
            if (tile.getOwningSettlement() != colony) {
                
                serverPlayer.claimLand(tile, colony, 0);
            }
        }

        Location oldLocation = unit.getLocation();
        unit.work(workLocation);
        
        sendUpdatedTileToAll(unit.getTile(), serverPlayer);
        
        if (oldLocation instanceof ColonyTile) {
            sendUpdatedTileToAll(((ColonyTile) oldLocation).getWorkTile(), serverPlayer);
        }
        
        if (workLocation instanceof ColonyTile) {
            sendUpdatedTileToAll(((ColonyTile) workLocation).getWorkTile(), serverPlayer);
        }
        return null;
    }

    
    private Element changeWorkType(Connection connection, Element workElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(workElement.getAttribute("unit"));
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }

        String workTypeString = workElement.getAttribute("workType");
        if (workTypeString != null) {
            GoodsType workType = FreeCol.getSpecification().getGoodsType(workTypeString);
            
            unit.setWorkType(workType);

        }
        return null;

    }

    
    private Element workImprovement(Connection connection, Element workElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(workElement.getAttribute("unit"));
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        Tile tile = unit.getTile();

        String improvementTypeString = workElement.getAttribute("improvementType");
        if (improvementTypeString != null) {
            Element reply = Message.createNewRootElement("workImprovementConfirmed");

            if (tile.getTileItemContainer() == null) {
                tile.setTileItemContainer(new TileItemContainer(tile.getGame(), tile));
                reply.appendChild(tile.getTileItemContainer().toXMLElement(player, reply.getOwnerDocument()));
            }

            TileImprovementType type = FreeCol.getSpecification().getTileImprovementType(improvementTypeString);
            TileImprovement improvement = unit.getTile().findTileImprovementType(type);
            if (improvement == null) {
                
                improvement = new TileImprovement(getGame(), unit.getTile(), type);
                unit.getTile().add(improvement);
            }
            reply.appendChild(improvement.toXMLElement(player, reply.getOwnerDocument()));
            unit.work(improvement);
            return reply;
        } else {
            return null;
        }

    }

    
    private Element assignTeacher(Connection connection, Element workElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Unit student = (Unit) getGame().getFreeColGameObject(workElement.getAttribute("student"));
        Unit teacher = (Unit) getGame().getFreeColGameObject(workElement.getAttribute("teacher"));

        if (!student.canBeStudent(teacher)) {
            throw new IllegalStateException("Unit can not be student!");
        }
        if (!teacher.getColony().canTrain(teacher)) {
            throw new IllegalStateException("Unit can not be teacher!");
        }
        if (student.getOwner() != player) {
            throw new IllegalStateException("Student is not your unit!");
        }
        if (teacher.getOwner() != player) {
            throw new IllegalStateException("Teacher is not your unit!");
        }
        if (student.getColony() != teacher.getColony()) {
            throw new IllegalStateException("Student and teacher are not in the same colony!");
        }
        if (!(student.getLocation() instanceof WorkLocation)) {
            throw new IllegalStateException("Student is not in a WorkLocation!");
        }
        
        if (student.getTeacher() != null) {
            student.getTeacher().setStudent(null);
        }
        student.setTeacher(teacher);
        if (teacher.getStudent() != null) {
            teacher.getStudent().setTeacher(null);
        }
        teacher.setStudent(student);
        return null;
    }

    
    private Element setBuildQueue(Connection connection, Element setBuildQueueElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Colony colony = (Colony) getGame().getFreeColGameObject(setBuildQueueElement.getAttribute("colony"));
        if (colony.getOwner() != player) {
            throw new IllegalStateException("Not your colony!");
        }
        List<BuildableType> buildQueue = new ArrayList<BuildableType>();
        int size = Integer.parseInt(setBuildQueueElement.getAttribute("size"));
        for (int x = 0; x < size; x++) {
            String typeId = setBuildQueueElement.getAttribute("x" + Integer.toString(x));
            buildQueue.add((BuildableType) Specification.getSpecification().getType(typeId));
        }

        colony.setBuildQueue(buildQueue);
        
        sendUpdatedTileToAll(colony.getTile(), player);
        return null;
    }

    
    private Element changeState(Connection connection, Element changeStateElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObjectSafely(changeStateElement.getAttribute("unit"));
        if (unit == null) {
            throw new IllegalArgumentException("Could not find 'Unit' with specified ID: "
                    + changeStateElement.getAttribute("unit"));
        }
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        UnitState state = Enum.valueOf(UnitState.class, changeStateElement.getAttribute("state"));
        Tile oldTile = unit.getTile();
        if (unit.checkSetState(state)) {
            unit.setState(state);
        } else {
            logger.warning("Can't set state " + state + " for unit " + unit + " with current state " + unit.getState()
                    + " and " + unit.getMovesLeft() + " moves left belonging to " + player
                    + ". Possible cheating attempt (or bug)?");
        }
        
        sendUpdatedTileToAll(oldTile, player);
        return null;
    }

    
    private Element putOutsideColony(Connection connection, Element putOutsideColonyElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(putOutsideColonyElement.getAttribute("unit"));
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        Location oldLocation = unit.getLocation();
        unit.putOutsideColony();
        
        
        Element updateElement = Message.createNewRootElement("update");
        updateElement.appendChild(unit.getTile().toXMLElement(player, updateElement.getOwnerDocument()));
        if (oldLocation instanceof Building) {
            updateElement.appendChild(((Building) oldLocation)
                                      .toXMLElement(player, updateElement.getOwnerDocument()));
        } else if (oldLocation instanceof ColonyTile) {
            updateElement.appendChild(((ColonyTile) oldLocation)
                                      .toXMLElement(player, updateElement.getOwnerDocument()));
        }
        return updateElement;
    }

    
    private Element payForBuilding(Connection connection, Element payForBuildingElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Colony colony = (Colony) getGame().getFreeColGameObject(payForBuildingElement.getAttribute("colony"));
        if (colony.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        colony.payForBuilding();
        return null;
    }

    
    private Element payArrears(Connection connection, Element payArrearsElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        GoodsType goodsType = FreeCol.getSpecification().getGoodsType(payArrearsElement.getAttribute("goodsType"));
        int arrears = player.getArrears(goodsType);
        if (player.getGold() < arrears) {
            throw new IllegalStateException("Not enough gold to pay tax arrears!");
        } else {
            player.modifyGold(-arrears);
            player.resetArrears(goodsType);
        }
        return null;
    }

    
    private Element setGoodsLevels(Connection connection, Element setGoodsLevelsElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Colony colony = (Colony) getGame().getFreeColGameObject(setGoodsLevelsElement.getAttribute("colony"));
        if (colony == null) {
            throw new IllegalArgumentException("Found no colony with ID " + setGoodsLevelsElement.getAttribute("colony"));
        } else if (colony.getOwner() != player) {
            throw new IllegalStateException("Not your colony!");
            
        }
        ExportData exportData = new ExportData();
        exportData.readFromXMLElement((Element) setGoodsLevelsElement.getChildNodes().item(0));
        colony.setExportData(exportData);
        return null;
    }

    
    private Element clearSpeciality(Connection connection, Element clearSpecialityElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(clearSpecialityElement.getAttribute("unit"));
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        unit.clearSpeciality();
        if (unit.getLocation() instanceof Tile) {
            sendUpdatedTileToAll(unit.getTile(), player);
        }
        return null;
    }

    
    private Element endTurn(Connection connection, Element element) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        getFreeColServer().getInGameController().endTurn(player);
        return null;
    }

    
    private Element disbandUnit(Connection connection, Element element) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(element.getAttribute("unit"));
        if (unit == null) {
            throw new IllegalArgumentException("Could not find 'Unit' with specified ID: "
                    + element.getAttribute("unit"));
        }
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        Tile oldTile = unit.getTile();
        unit.dispose();
        sendUpdatedTileToAll(oldTile, player);
        return null;
    }

    
    private Element foreignAffairs(Connection connection, Element element) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Element reply = Message.createNewRootElement("foreignAffairsReport");
        Iterator<Player> enemyPlayerIterator = getGame().getPlayerIterator();
        while (enemyPlayerIterator.hasNext()) {
            ServerPlayer enemyPlayer = (ServerPlayer) enemyPlayerIterator.next();
            if (enemyPlayer.getConnection() == null || enemyPlayer.isIndian()
                || enemyPlayer.isDead()) {
                continue;
            }
            Element enemyElement = reply.getOwnerDocument().createElement("opponent");
            enemyElement.setAttribute("player", enemyPlayer.getId());
            int numberOfColonies = enemyPlayer.getSettlements().size();
            int numberOfUnits = 0;
            int militaryStrength = 0;
            int navalStrength = 0;
            Iterator<Unit> unitIterator = enemyPlayer.getUnitIterator();
            while (unitIterator.hasNext()) {
                Unit unit = unitIterator.next();
                numberOfUnits++;
                if (unit.isNaval()) {
                    navalStrength += unit.getGame().getCombatModel().getOffencePower(unit, null);
                } else {
                    militaryStrength += unit.getGame().getCombatModel().getOffencePower(unit, null);
                }
            }
            Stance stance = enemyPlayer.getStance(player);
            if (stance == Stance.UNCONTACTED) {
                stance = Stance.PEACE;
            }
            enemyElement.setAttribute("numberOfColonies", String.valueOf(numberOfColonies));
            enemyElement.setAttribute("numberOfUnits", String.valueOf(numberOfUnits));
            enemyElement.setAttribute("militaryStrength", String.valueOf(militaryStrength));
            enemyElement.setAttribute("navalStrength", String.valueOf(navalStrength));
            enemyElement.setAttribute("stance", String.valueOf(stance));
            enemyElement.setAttribute("gold", String.valueOf(enemyPlayer.getGold()));
            if (player.equals(enemyPlayer) ||
                player.hasAbility("model.ability.betterForeignAffairsReport")) {
                enemyElement.setAttribute("SoL", String.valueOf(enemyPlayer.getSoL()));
                enemyElement.setAttribute("foundingFathers", String.valueOf(enemyPlayer.getFatherCount()));
                enemyElement.setAttribute("tax", String.valueOf(enemyPlayer.getTax()));
            }
            reply.appendChild(enemyElement);
        }
        return reply;
    }


    
    private Element highScores(Connection connection, Element element) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Element reply = Message.createNewRootElement("highScoresReport");
        for (HighScore score : getFreeColServer().getHighScores()) {
            reply.appendChild(score.toXMLElement(player, reply.getOwnerDocument()));
        }
        return reply;
    }


    
    private Element retire(Connection connection, Element element) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Element reply = Message.createNewRootElement("confirmRetire");
        boolean highScore = getFreeColServer().newHighScore(player);
        if (highScore) {
            try {
                getFreeColServer().saveHighScores();
                reply.setAttribute("highScore", "true");
            } catch (Exception e) {
                logger.warning(e.toString());
                reply.setAttribute("highScore", "false");
            }
        } else {
            reply.setAttribute("highScore", "false");
        }
        return reply;
    }


    
    private Element getREFUnits(Connection connection, Element element) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        List<AbstractUnit> units = new ArrayList<AbstractUnit>();
        UnitType defaultType = FreeCol.getSpecification().getUnitType("model.unit.freeColonist");
        if (player.getMonarch() == null) {
            ServerPlayer enemyPlayer = (ServerPlayer) player.getREFPlayer();
            java.util.Map<UnitType, EnumMap<Role, Integer>> unitHash =
                new HashMap<UnitType, EnumMap<Role, Integer>>();
            for (Unit unit : enemyPlayer.getUnits()) {
                if (unit.isOffensiveUnit()) {
                    UnitType unitType = defaultType;
                    if (unit.getType().getOffence() > 0 ||
                        unit.hasAbility("model.ability.expertSoldier")) {
                        unitType = unit.getType();
                    }
                    EnumMap<Role, Integer> roleMap = unitHash.get(unitType);
                    if (roleMap == null) {
                        roleMap = new EnumMap<Role, Integer>(Role.class);
                    }
                    Role role = unit.getRole();
                    Integer count = roleMap.get(role);
                    if (count == null) {
                        roleMap.put(role, new Integer(1));
                    } else {
                        roleMap.put(role, new Integer(count.intValue() + 1));
                    }
                    unitHash.put(unitType, roleMap);
                }
            }
            for (java.util.Map.Entry<UnitType, EnumMap<Role, Integer>> typeEntry : unitHash.entrySet()) {
                for (java.util.Map.Entry<Role, Integer> roleEntry : typeEntry.getValue().entrySet()) {
                    units.add(new AbstractUnit(typeEntry.getKey(), roleEntry.getKey(), roleEntry.getValue()));
                }
            }
        } else {
            units = player.getMonarch().getREF();
        }

        Element reply = Message.createNewRootElement("REFUnits");
        for (AbstractUnit unit : units) {
            reply.appendChild(unit.toXMLElement(player,reply.getOwnerDocument()));
        }
        return reply;
    }

    
    private Element indianDemand(Connection connection, Element element) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        Unit unit = (Unit) getGame().getFreeColGameObject(element.getAttribute("unit"));
        Colony colony = (Colony) getGame().getFreeColGameObject(element.getAttribute("colony"));
        if (unit == null) {
            throw new IllegalArgumentException("Could not find 'Unit' with specified ID: "
                    + element.getAttribute("unit"));
        }
        if (unit.getMovesLeft() <= 0) {
            throw new IllegalStateException("No moves left!");
        }
        if (colony == null) {
            throw new IllegalArgumentException("Could not find 'Colony' with specified ID: "
                    + element.getAttribute("colony"));
        }
        if (unit.getOwner() != player) {
            throw new IllegalStateException("Not your unit!");
        }
        if (unit.getTile().getDistanceTo(colony.getTile()) > 1) {
            throw new IllegalStateException("Not adjacent to colony!");
        }
        ServerPlayer receiver = (ServerPlayer) colony.getOwner();
        if (receiver.isConnected()) {
            int gold = 0;
            Goods goods = null;
            Element goodsElement = Message.getChildElement(element, Goods.getXMLElementTagName());
            if (goodsElement == null) {
                gold = Integer.parseInt(element.getAttribute("gold"));
            } else {
                goods = new Goods(getGame(), goodsElement);
            }
            try {
                Element reply = receiver.getConnection().ask(element);
                boolean accepted = Boolean.valueOf(reply.getAttribute("accepted")).booleanValue();
                if (accepted) {
                    if (goods == null) {
                        receiver.modifyGold(-gold);
                    } else {
                        colony.getGoodsContainer().removeGoods(goods);
                    }
                }
                return reply;
            } catch (IOException e) {
                logger.warning("Could not send \"demand\"-message!");
            }
        }
        return null;
    }

    
    private Element continuePlaying(Connection connection, Element element) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        if (!getFreeColServer().isSingleplayer()) {
            throw new IllegalStateException("Can't continue playing in multiplayer!");
        }
        if (player != getFreeColServer().getInGameController().checkForWinner()) {
            throw new IllegalStateException("Can't continue playing! Player "
                    + player.getName() + " hasn't won the game");
        }
        GameOptions go = getGame().getGameOptions();
        ((BooleanOption) go.getObject(GameOptions.VICTORY_DEFEAT_REF)).setValue(false);
        ((BooleanOption) go.getObject(GameOptions.VICTORY_DEFEAT_EUROPEANS)).setValue(false);
        ((BooleanOption) go.getObject(GameOptions.VICTORY_DEFEAT_HUMANS)).setValue(false);
        
        
        final ServerPlayer currentPlayer = (ServerPlayer) getFreeColServer().getGame().getCurrentPlayer();
        getFreeColServer().getInGameController().endTurn(currentPlayer);
        return null;
    }

    
    @Override
    protected Element logout(Connection connection, Element logoutElement) {
        ServerPlayer player = getFreeColServer().getPlayer(connection);
        logger.info("Logout by: " + connection + ((player != null) ? " (" + player.getName() + ") " : ""));
        if (player == null) {
            return null;
        }
        
        
        
        
        
        
        
        player.setConnected(false);
        if (getFreeColServer().getGame().getCurrentPlayer() == player
                && !getFreeColServer().isSingleplayer()) {
            getFreeColServer().getInGameController().endTurn(player);
        }
        try {
            getFreeColServer().updateMetaServer();
        } catch (NoRouteToServerException e) {}
        
        return null;
    }

    

    private Element getServerStatistics(Connection connection, Element request) {
        StatisticsMessage m = new StatisticsMessage(getGame(), getFreeColServer().getAIMain());
        Element reply = m.toXMLElement();
        return reply;
    }
}
