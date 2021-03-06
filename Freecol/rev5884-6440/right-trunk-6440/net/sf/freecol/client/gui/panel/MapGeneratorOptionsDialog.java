

package net.sf.freecol.client.gui.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.freecol.FreeCol;
import net.sf.freecol.client.gui.Canvas;
import net.sf.freecol.client.gui.i18n.Messages;
import net.sf.freecol.client.gui.option.BooleanOptionUI;
import net.sf.freecol.client.gui.option.FileOptionUI;
import net.sf.freecol.client.gui.option.OptionMapUI;
import net.sf.freecol.server.generator.MapGeneratorOptions;


public final class MapGeneratorOptionsDialog extends FreeColDialog<Boolean> implements ActionListener {

    private static final Logger logger = Logger.getLogger(MapGeneratorOptionsDialog.class.getName());

    private static final int OK = 0, CANCEL = 1, RESET = 2;

    private JButton ok, cancel;

    private JPanel buttons = new JPanel(new FlowLayout());

    private JLabel header;

    private OptionMapUI ui;


    
    public MapGeneratorOptionsDialog(Canvas parent) {
        super(parent);
        setLayout(new BorderLayout());

        ok = new JButton(Messages.message("ok"));
        ok.setActionCommand(String.valueOf(OK));
        ok.addActionListener(this);
        ok.setMnemonic('O');
        buttons.add(ok);

        JButton reset = new JButton(Messages.message("reset"));
        reset.setActionCommand(String.valueOf(RESET));
        reset.addActionListener(this);
        reset.setMnemonic('R');
        buttons.add(reset);
        
        cancel = new JButton(Messages.message("cancel"));
        cancel.setActionCommand(String.valueOf(CANCEL));
        cancel.addActionListener(this);
        cancel.setMnemonic('C');
        buttons.add(cancel);

        FreeColPanel.enterPressesWhenFocused(ok);
        setCancelComponent(cancel);
        
        setSize(750, 500);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(750, 500);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }
    
    public void initialize(boolean editable, MapGeneratorOptions mgo) {
        removeAll();

        
        header = getDefaultHeader(mgo.getName());
        add(header, BorderLayout.NORTH);

        
        JPanel uiPanel = new JPanel(new BorderLayout());
        uiPanel.setOpaque(false);
        ui = new OptionMapUI(mgo, editable);
        uiPanel.add(ui, BorderLayout.CENTER);
        uiPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(uiPanel, BorderLayout.CENTER);

        
        add(buttons, BorderLayout.SOUTH);

        ok.setEnabled(editable);
        
        
        JPanel shortcutsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        shortcutsPanel.setOpaque(false);
        shortcutsPanel.add(new JLabel(Messages.message("shortcuts")));
        File mapDirectory = new File(FreeCol.getDataDirectory(), "maps");
        if (mapDirectory.isDirectory()) {
            for (final File file : mapDirectory.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        return file.isFile() && file.getName().endsWith(".fsg");
                    }
                })) {
                JButton mapButton = new JButton();
                String mapName = file.getName().substring(0, file.getName().lastIndexOf('.'));
                String imageName = mapName + ".png";
                File imageFile = new File(mapDirectory, imageName);
                if (imageFile.exists() && imageFile.isFile()) {
                    try {
                        Image mapImage = ImageIO.read(imageFile);
                        ImageIcon mapIcon = new ImageIcon(mapImage);
                        mapButton.setIcon(mapIcon);
                        mapButton.setToolTipText(mapName);
                    } catch(IOException e) {
                        logger.warning("Unable to load map image " + imageName);
                    }
                } else {
                    mapButton.setText(mapName);
                }
                mapButton.setBorderPainted(false);
                mapButton.setRolloverEnabled(true);
                final OptionMapUI theUI = ui;
                mapButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            theUI.reset();
                            FileOptionUI fou = (FileOptionUI) theUI.getOptionUI(MapGeneratorOptions.IMPORT_FILE);
                            fou.setValue(file);
                    
                            ((BooleanOptionUI) theUI.getOptionUI(MapGeneratorOptions.IMPORT_RUMOURS)).setValue(false);
                            ((BooleanOptionUI) theUI.getOptionUI(MapGeneratorOptions.IMPORT_TERRAIN)).setValue(true);
                            ((BooleanOptionUI) theUI.getOptionUI(MapGeneratorOptions.IMPORT_BONUSES)).setValue(false);
                        }
                    });
                shortcutsPanel.add(mapButton);
            }
        }
        uiPanel.add(shortcutsPanel, BorderLayout.NORTH);
    }

    public void requestFocus() {
        if (ok.isEnabled()) {
            ok.requestFocus();
        } else {
            cancel.requestFocus();
        }
    }

    
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        try {
            switch (Integer.valueOf(command).intValue()) {
            case OK:
                ui.unregister();
                ui.updateOption();
                getCanvas().remove(this);
                if (!getClient().isMapEditor()) {
                    getClient().getPreGameController().sendMapGeneratorOptions();
                    getClient().getCanvas().getStartGamePanel().updateMapGeneratorOptions();
                }
                setResponse(new Boolean(true));
                break;
            case CANCEL:
                ui.rollback();
                ui.unregister();
                getCanvas().remove(this);
                setResponse(new Boolean(false));
                break;
            case RESET:
                ui.reset();
                break;
            default:
                logger.warning("Invalid ActionCommand: invalid number.");
            }
        } catch (NumberFormatException e) {
            logger.warning("Invalid Actioncommand: not a number.");
        }
    }
}
