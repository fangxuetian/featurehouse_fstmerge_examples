


package net.sf.freecol.client.control;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import net.sf.freecol.FreeCol;
import net.sf.freecol.client.ClientOptions;
import net.sf.freecol.client.FreeColClient;
import net.sf.freecol.client.gui.Canvas;
import net.sf.freecol.client.gui.i18n.Messages;
import net.sf.freecol.client.gui.panel.LoadingSavegameDialog;
import net.sf.freecol.client.networking.Client;
import net.sf.freecol.common.FreeColException;
import net.sf.freecol.common.ServerInfo;
import net.sf.freecol.common.io.FreeColSavegameFile;
import net.sf.freecol.common.model.DifficultyLevel;
import net.sf.freecol.common.model.Game;
import net.sf.freecol.common.model.GameOptions;
import net.sf.freecol.common.model.NationOptions;
import net.sf.freecol.common.model.Player;
import net.sf.freecol.common.model.Specification;
import net.sf.freecol.common.networking.Connection;
import net.sf.freecol.common.networking.Message;
import net.sf.freecol.common.networking.NoRouteToServerException;
import net.sf.freecol.common.resources.ResourceManager;
import net.sf.freecol.common.util.XMLStream;
import net.sf.freecol.server.FreeColServer;
import net.sf.freecol.server.generator.MapGeneratorOptions;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



public final class ConnectController {

    private static final Logger logger = Logger.getLogger(ConnectController.class.getName());

    private final FreeColClient freeColClient;

    
    public ConnectController(FreeColClient freeColClient) {
        this.freeColClient = freeColClient;
    }

    
    
    public void startMultiplayerGame(boolean publicServer, String username, int port,
                                     NationOptions nationOptions, DifficultyLevel level) {

        freeColClient.setMapEditor(false);

        if (freeColClient.isLoggedIn()) {
            logout(true);
        }

        if (freeColClient.getFreeColServer() != null && 
            freeColClient.getFreeColServer().getServer().getPort() == port) {
            if (freeColClient.getCanvas().showConfirmDialog("stopServer.text",
                                                            "stopServer.yes",
                                                            "stopServer.no")) {
                freeColClient.getFreeColServer().getController().shutdown();
            } else {
                return;
            }
        }

        try {
            FreeColServer freeColServer = new FreeColServer(publicServer, false, port, null, nationOptions, level);
            freeColClient.setFreeColServer(freeColServer);
        } catch (NoRouteToServerException e) {
            freeColClient.getCanvas().errorMessage("server.noRouteToServer");
            return;
        } catch (IOException e) {
            freeColClient.getCanvas().errorMessage("server.couldNotStart");
            return;
        }

        joinMultiplayerGame(username, "localhost", port);
    }


    
    public void startSingleplayerGame(String username, NationOptions nationOptions,
                                      DifficultyLevel level) {

        freeColClient.setMapEditor(false);
        
        if (freeColClient.isLoggedIn()) {
            logout(true);
        }

        
        int port = FreeCol.getDefaultPort();

        if (freeColClient.getFreeColServer() != null
            && freeColClient.getFreeColServer().getServer().getPort() == port) {
            if (freeColClient.getCanvas().showConfirmDialog("stopServer.text",
                                                            "stopServer.yes",
                                                            "stopServer.no")) {
                freeColClient.getFreeColServer().getController().shutdown();
            } else {
                return;
            }
        }

        try {
            FreeColServer freeColServer = new FreeColServer(false, true, port, null, nationOptions, level);
            FreeCol.getSpecification().applyDifficultyLevel(level);
            freeColClient.setFreeColServer(freeColServer);
        } catch (NoRouteToServerException e) {
            logger.warning("Illegal state: An exception occured that can only appear in public multiplayer games.");
            return;
        } catch (IOException e) {
            freeColClient.getCanvas().errorMessage("server.couldNotStart");
            return;
        }

        freeColClient.setSingleplayer(true);

        if (login(username, "127.0.0.1", port)) {
            freeColClient.getPreGameController().setReady(true);
            freeColClient.getCanvas().showStartGamePanel(freeColClient.getGame(), freeColClient.getMyPlayer(),
                                                         true);
                                                         
        }
    }


    
    public void joinMultiplayerGame(String username, String host, int port) {
        final Canvas canvas = freeColClient.getCanvas();
        freeColClient.setMapEditor(false);
        
        if (freeColClient.isLoggedIn()) {
            logout(true);
        }

        List<String> vacantPlayers = getVacantPlayers(host, port);
        if (vacantPlayers != null) {
            String choice = canvas.showSimpleChoiceDialog(null,
                                                          "connectController.choicePlayer",
                                                          "cancel",
                                                          vacantPlayers);
            if (choice != null) {
                username = choice;
            } else {
                return;
            }
        }

        freeColClient.setSingleplayer(false);
        if (login(username, host, port) && !freeColClient.getGUI().isInGame()) {
            canvas.showStartGamePanel(freeColClient.getGame(), freeColClient.getMyPlayer(), false);
        }
    }


    
    public boolean login(String username, String host, int port) {
        Client client = freeColClient.getClient();
        Canvas canvas = freeColClient.getCanvas();
        NationOptions nationOptions = new NationOptions();

        freeColClient.setMapEditor(false);
        
        if (client != null) {
            client.disconnect();
        }

        try {
            client = new Client(host, port, freeColClient.getPreGameInputHandler());
        } catch (ConnectException e) {
            canvas.errorMessage("server.couldNotConnect");
            return false;
        } catch (IOException e) {
            canvas.errorMessage("server.couldNotConnect");
            return false;
        }

        freeColClient.setClient(client);

        Connection c = client.getConnection();
        XMLStreamReader in = null;
        try {
            XMLStreamWriter out = c.ask();
            out.writeStartElement("login");
            out.writeAttribute("username", username);
            out.writeAttribute("freeColVersion", FreeCol.getVersion());
            out.writeEndElement();
            in = c.getReply();
            if (in.getLocalName().equals("loginConfirmed")) {
                final String startGameStr = in.getAttributeValue(null, "startGame");
                boolean startGame = (startGameStr != null) && Boolean.valueOf(startGameStr).booleanValue();
                boolean singleplayer = Boolean.valueOf(in.getAttributeValue(null, "singleplayer")).booleanValue();
                boolean isCurrentPlayer = Boolean.valueOf(in.getAttributeValue(null, "isCurrentPlayer")).booleanValue();

                in.nextTag();
                Game game = new Game(freeColClient.getModelController(), in, username);
                
                
                
                
                Player thisPlayer = game.getPlayerByName(username);

                freeColClient.setGame(game);
                freeColClient.setMyPlayer(thisPlayer);

                final MapGeneratorOptions mgo;
                if (in.getLocalName().equals(MapGeneratorOptions.getXMLElementTagName())) {
                    mgo = new MapGeneratorOptions(in);
                } else {
                    mgo = new MapGeneratorOptions();
                }
                freeColClient.getPreGameController().setMapGeneratorOptions(mgo);
                
                c.endTransmission(in);
                
                
                if (startGame) {
                    freeColClient.setSingleplayer(singleplayer);
                    freeColClient.getPreGameController().startGame();

                    if (isCurrentPlayer) {
                        freeColClient.getInGameController().setCurrentPlayer(thisPlayer);
                    }
                }
            } else if (in.getLocalName().equals("error")) {
                canvas.errorMessage(in.getAttributeValue(null, "messageID"), in.getAttributeValue(null, "message"));

                c.endTransmission(in);
                return false;
            } else {
                logger.warning("Unkown message received: " + in.getLocalName());
                c.endTransmission(in);
                return false;
            }            
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.warning(sw.toString());
            canvas.errorMessage(null, "Could not send XML to the server.");
            try {
                c.endTransmission(in);
            } catch (IOException ie) {
                logger.warning("Exception while trying to end transmission: " + ie.toString());
            }
        }

        freeColClient.setLoggedIn(true);

        return true;
    }


    
    public void reconnect() {
        final String username = freeColClient.getMyPlayer().getName();
        final String host = freeColClient.getClient().getHost();
        final int port = freeColClient.getClient().getPort();
        
        freeColClient.getCanvas().removeInGameComponents();
        logout(true);
        login(username, host, port);
        freeColClient.getInGameController().nextModelMessage();
    }


    
    public void loadGame() {
        File file = freeColClient.getCanvas().showLoadDialog(FreeCol.getSaveDirectory());
        if (file != null) {
            
            loadGame(file);
        }
    }

    
    public void loadGame(File file) {
        final Canvas canvas = freeColClient.getCanvas();
        final File theFile = file;

        freeColClient.setMapEditor(false);
        
        class ErrorJob implements Runnable {
            private final  String  message;
            ErrorJob( String message ) {
                this.message = message;
            }
            public void run() {
                canvas.closeMenus();
                canvas.errorMessage( message );
            }
        }

        final boolean publicServer;
        final boolean singleplayer;
        final String name;
        final int port;
        XMLStream xs = null;
        try {
            
            final FreeColSavegameFile fis = new FreeColSavegameFile(theFile);
            xs = FreeColServer.createXMLStreamReader(fis);
            final XMLStreamReader in = xs.getXMLStreamReader();
            in.nextTag();
            final boolean defaultSingleplayer = Boolean.valueOf(in.getAttributeValue(null, "singleplayer")).booleanValue();
            final boolean defaultPublicServer;
            final String publicServerStr =  in.getAttributeValue(null, "publicServer");
            if (publicServerStr != null) {
                defaultPublicServer = Boolean.valueOf(publicServerStr).booleanValue();
            } else {
                defaultPublicServer = false;
            }
            xs.close();
            final int sgo = freeColClient.getClientOptions().getInteger(ClientOptions.SHOW_SAVEGAME_SETTINGS);
            if (sgo == ClientOptions.SHOW_SAVEGAME_SETTINGS_ALWAYS
                    || !defaultSingleplayer && sgo == ClientOptions.SHOW_SAVEGAME_SETTINGS_MULTIPLAYER) {
                if (canvas.showLoadingSavegameDialog(defaultPublicServer, defaultSingleplayer)) {
                    LoadingSavegameDialog lsd = canvas.getLoadingSavegameDialog();
                    publicServer = lsd.isPublic();
                    singleplayer = lsd.isSingleplayer();
                    name = lsd.getName();
                    port = lsd.getPort();
                } else {
                    return;
                }
            } else {
                publicServer = defaultPublicServer;
                singleplayer = defaultSingleplayer;
                name = null;
                port = FreeCol.getDefaultPort();
            }
        } catch (FileNotFoundException e) {
            SwingUtilities.invokeLater( new ErrorJob("fileNotFound") );
            return;
        } catch (IOException e) {
            SwingUtilities.invokeLater( new ErrorJob("server.couldNotStart") );
            return;
        } catch (NullPointerException e) {
            SwingUtilities.invokeLater( new ErrorJob("couldNotLoadGame") );
            return;
        } catch (XMLStreamException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.warning(sw.toString());                    
            SwingUtilities.invokeLater( new ErrorJob("server.couldNotStart") );
            return;
        } finally {
            xs.close();
        }
        
        if (freeColClient.getFreeColServer() != null && freeColClient.getFreeColServer().getServer().getPort() == port) {
            if (freeColClient.getCanvas().showConfirmDialog("stopServer.text", "stopServer.yes", "stopServer.no")) {
                freeColClient.getFreeColServer().getController().shutdown();
            } else {
                return;
            }
        }

        canvas.showStatusPanel(Messages.message("status.loadingGame"));
        
        Runnable loadGameJob = new Runnable() {
            public void run() {
                FreeColServer freeColServer = null;
                try {
                    final FreeColSavegameFile savegame = new FreeColSavegameFile(theFile);
                    freeColServer = new FreeColServer(savegame, publicServer, singleplayer, port, name);
                    freeColClient.setFreeColServer(freeColServer);
                    final String username = freeColServer.getOwner();
                    freeColClient.setSingleplayer(singleplayer);
                    SwingUtilities.invokeLater( new Runnable() {
                        public void run() {
                            ResourceManager.setScenarioMapping(savegame.getResourceMapping());
                            login(username, "127.0.0.1", FreeCol.getDefaultPort());
                            canvas.closeStatusPanel();
                        }
                    } );                    
                } catch (NoRouteToServerException e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            freeColClient.getCanvas().closeMainPanel();
                            freeColClient.getCanvas().showMainPanel();
                        }
                    });
                    SwingUtilities.invokeLater( new ErrorJob("server.noRouteToServer") );
                } catch (FileNotFoundException e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            freeColClient.getCanvas().closeMainPanel();
                            freeColClient.getCanvas().showMainPanel();
                        }
                    });
                    SwingUtilities.invokeLater( new ErrorJob("fileNotFound") );
                } catch (IOException e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            freeColClient.getCanvas().closeMainPanel();
                            freeColClient.getCanvas().showMainPanel();
                        }
                    });
                    SwingUtilities.invokeLater( new ErrorJob("server.couldNotStart") );
                } catch (FreeColException e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            freeColClient.getCanvas().closeMainPanel();
                            freeColClient.getCanvas().showMainPanel();
                        }
                    });
                    SwingUtilities.invokeLater( new ErrorJob(e.getMessage()) );                    
                }
            }
        };
        freeColClient.worker.schedule( loadGameJob );        
    }

    
    public void logout(boolean notifyServer) {
        if (notifyServer) {
            Element logoutMessage = Message.createNewRootElement("logout");
            logoutMessage.setAttribute("reason", "User has quit the client.");

            freeColClient.getClient().sendAndWait(logoutMessage);
        }

        try {
            freeColClient.getClient().getConnection().close();
        } catch (IOException e) {
            logger.warning("Could not close connection!");
        }

        ResourceManager.setScenarioMapping(null);
        ResourceManager.setCampaignMapping(null);
        
        if (!freeColClient.isHeadless()) {
            freeColClient.getGUI().setInGame(false);
        }
        freeColClient.setGame(null);
        freeColClient.setMyPlayer(null);
        freeColClient.setClient(null);

        freeColClient.setLoggedIn(false);        
    }


    
    public void quitGame(boolean bStopServer, boolean notifyServer) {
        final FreeColServer server = freeColClient.getFreeColServer();
        if (bStopServer && server != null) {            
            server.getController().shutdown();
            freeColClient.setFreeColServer(null);

            ResourceManager.setScenarioMapping(null);
            ResourceManager.setCampaignMapping(null);
            freeColClient.getGUI().setInGame(false);
            freeColClient.setGame(null);
            freeColClient.setMyPlayer(null);
            freeColClient.setIsRetired(false);
            freeColClient.setClient(null);                
            freeColClient.setLoggedIn(false);            
        } else if (freeColClient.isLoggedIn()) {
            logout(notifyServer);
        }          
    }


    
    public void quitGame(boolean bStopServer) {
        quitGame(bStopServer, true);
    }


    
    private List<String> getVacantPlayers(String host, int port) {
        Connection mc;
        try {
            mc = new Connection(host, port, null, FreeCol.CLIENT_THREAD);
        } catch (IOException e) {
            logger.warning("Could not connect to server.");
            return null;
        }

        ArrayList<String> items = new ArrayList<String>();
        Element element = Message.createNewRootElement("getVacantPlayers");
        try {
            Element reply = mc.ask(element);
            if (reply == null) {
                logger.warning("The server did not return a list.");
                return null;
            }
            if (!reply.getTagName().equals("vacantPlayers")) {
                logger.warning("The reply has an unknown type: " + reply.getTagName());
                return null;
            }
                        
            NodeList nl = reply.getChildNodes();
            for (int i=0; i<nl.getLength(); i++) {
                items.add(((Element) nl.item(i)).getAttribute("username"));
            }
        } catch (IOException e) {
            logger.warning("Could not send message to server.");
        } finally {
            try {
                mc.close();
            } catch (IOException e) {
                logger.warning("Could not close connection.");
            }
        }
                        
        return items;
    }


    
    public ArrayList<ServerInfo> getServerList() {
        Canvas canvas = freeColClient.getCanvas();

        Connection mc;
        try {
            mc = new Connection(FreeCol.META_SERVER_ADDRESS, FreeCol.META_SERVER_PORT, null, FreeCol.CLIENT_THREAD);
        } catch (IOException e) {
            logger.warning("Could not connect to meta-server.");
            canvas.errorMessage("metaServer.couldNotConnect");
            return null;
        }

        try {
            Element gslElement = Message.createNewRootElement("getServerList");
            Element reply = mc.ask(gslElement);
            if (reply == null) {
                logger.warning("The meta-server did not return a list.");
                canvas.errorMessage("metaServer.communicationError");
                return null;
            } else {
                ArrayList<ServerInfo> items = new ArrayList<ServerInfo>();
                NodeList nl = reply.getChildNodes();
                for (int i=0; i<nl.getLength(); i++) {
                    items.add(new ServerInfo((Element) nl.item(i)));
                }
                return items;
            }
        } catch (IOException e) {
            logger.warning("Network error while communicating with the meta-server.");
            canvas.errorMessage("metaServer.communicationError");
            return null;
        } finally {
            try {
                mc.close();
            } catch (IOException e) {
                logger.warning("Could not close connection to meta-server.");
                return null;
            }
        }
    }
}
