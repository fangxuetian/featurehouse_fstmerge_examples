

package net.sf.freecol.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import net.sf.freecol.FreeCol;
import net.sf.freecol.client.FreeColClient;
import net.sf.freecol.client.gui.action.ChangeWindowedModeAction;
import net.sf.freecol.client.gui.action.DetermineHighSeasAction;
import net.sf.freecol.client.gui.action.DisplayGridAction;
import net.sf.freecol.client.gui.action.DisplayTileTextAction;
import net.sf.freecol.client.gui.action.DisplayTileTextAction.DisplayText;
import net.sf.freecol.client.gui.action.MapControlsAction;
import net.sf.freecol.client.gui.action.NewAction;
import net.sf.freecol.client.gui.action.NewEmptyMapAction;
import net.sf.freecol.client.gui.action.OpenAction;
import net.sf.freecol.client.gui.action.PreferencesAction;
import net.sf.freecol.client.gui.action.SaveAction;
import net.sf.freecol.client.gui.action.SaveAndQuitAction;
import net.sf.freecol.client.gui.action.ScaleMapAction;
import net.sf.freecol.client.gui.action.ShowMainAction;
import net.sf.freecol.client.gui.action.ZoomInAction;
import net.sf.freecol.client.gui.action.ZoomOutAction;
import net.sf.freecol.client.gui.i18n.Messages;
import net.sf.freecol.client.gui.menu.DebugMenu;
import net.sf.freecol.server.generator.MapGeneratorOptions;


public class MapEditorMenuBar extends FreeColMenuBar {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(MapEditorMenuBar.class.getName());

    
    public MapEditorMenuBar(final FreeColClient freeColClient) {
        super(freeColClient);

        reset();
    }


    
    public void reset() {
        removeAll();

        buildGameMenu();
        buildViewMenu();
        buildToolsMenu();
        buildColopediaMenu();

        
        if (FreeCol.isInDebugMode()) {
            add(new DebugMenu(freeColClient));
        }

        update();
    }

    private void buildGameMenu() {
        
        JMenu menu = new JMenu(Messages.message("menuBar.game"));
        menu.setOpaque(false);
        menu.setMnemonic(KeyEvent.VK_G);

        menu.add(getMenuItem(NewAction.id));
        menu.add(getMenuItem(NewEmptyMapAction.id));

        menu.addSeparator();

        menu.add(getMenuItem(OpenAction.id));
        menu.add(getMenuItem(SaveAction.id));
        JMenuItem playItem = new JMenuItem(Messages.message("startGame"));
        playItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    File saveGameFile = new File(FreeCol.getAutosaveDirectory(), "tempMap.fsg");
                    MapGeneratorOptions options = freeColClient.getPreGameController().getMapGeneratorOptions();
                    if (options == null) {
                        options = new MapGeneratorOptions();
                    }
                    options.setFile(MapGeneratorOptions.IMPORT_FILE, saveGameFile);                        
                    freeColClient.getMapEditorController().saveGame(saveGameFile);
                    freeColClient.getPreGameController().sendMapGeneratorOptions();
                    freeColClient.getCanvas().newGame();
                }
            });
        menu.add(playItem);
        menu.addSeparator();

        menu.add(getMenuItem(PreferencesAction.id));

        menu.addSeparator();

        menu.add(getMenuItem(ShowMainAction.id));
        menu.add(getMenuItem(SaveAndQuitAction.id));

        add(menu);
    }

    private void buildViewMenu() {
        

        JMenu menu = new JMenu(Messages.message("menuBar.view"));
        menu.setOpaque(false);
        menu.setMnemonic(KeyEvent.VK_V);

        menu.add(getCheckBoxMenuItem(MapControlsAction.id));
        menu.add(getCheckBoxMenuItem(DisplayGridAction.id));
        menu.add(getCheckBoxMenuItem(ChangeWindowedModeAction.id));

        menu.addSeparator();
        ButtonGroup tileTextGroup = new ButtonGroup();
        for (DisplayText type : DisplayText.values()) {
            menu.add(getRadioButtonMenuItem(DisplayTileTextAction.id + type, tileTextGroup));
        }

        menu.addSeparator();
        menu.add(getMenuItem(ZoomInAction.id));
        menu.add(getMenuItem(ZoomOutAction.id));

        add(menu);
    }

    private void buildToolsMenu() {
        

        JMenu menu = new JMenu(Messages.message("menuBar.tools"));
        menu.setOpaque(false);
        menu.setMnemonic(KeyEvent.VK_T);

        menu.add(getMenuItem(ScaleMapAction.id));
        menu.add(getMenuItem(DetermineHighSeasAction.id));

        add(menu);
    }

}
