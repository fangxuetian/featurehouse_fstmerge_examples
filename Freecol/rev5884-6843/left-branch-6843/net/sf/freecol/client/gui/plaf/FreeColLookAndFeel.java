

package net.sf.freecol.client.gui.plaf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import net.sf.freecol.FreeCol;
import net.sf.freecol.client.gui.FAFile;
import net.sf.freecol.client.gui.i18n.Messages;
import net.sf.freecol.common.FreeColException;
import net.sf.freecol.common.resources.ResourceManager;


public class FreeColLookAndFeel extends MetalLookAndFeel {
    private static final Logger logger = Logger.getLogger(FreeColLookAndFeel.class.getName());
    
    
    private final static Class<FreeCol> resourceLocator = net.sf.freecol.FreeCol.class;
    private File dataDirectory;
    
    private final Dimension windowSize;


    
    public FreeColLookAndFeel(Dimension windowSize) throws FreeColException {
        this("", windowSize);
    }
    

   
    public FreeColLookAndFeel(String dataFolder, Dimension windowSize) throws FreeColException {
        super();
        
        this.windowSize = windowSize;
        
        if(dataFolder.equals("")) { 
            dataDirectory = new File("data");

            if(!dataDirectory.exists() || !dataDirectory.isDirectory()) {
                dataDirectory = null;                                
            }
        } else {
            dataDirectory = new File(dataFolder);

            if(!dataDirectory.exists() || !dataDirectory.isDirectory()) {
                throw new FreeColException("Data directory not found in: " + dataDirectory.getName());
            }
        }
        
        setCurrentTheme(new DefaultMetalTheme() {
            protected ColorUIResource getPrimary1() {
                return new ColorUIResource(ResourceManager.getColor("lookAndFeel.primary1.color"));
            }

            protected ColorUIResource getPrimary2() {
                return new ColorUIResource(ResourceManager.getColor("lookAndFeel.backgroundSelect.color"));
            }

            protected ColorUIResource getPrimary3() {
                return new ColorUIResource(ResourceManager.getColor("lookAndFeel.primary3.color"));
            }

            protected ColorUIResource getSecondary1() {
                return new ColorUIResource(ResourceManager.getColor("lookAndFeel.secondary1.color"));
            }

            protected ColorUIResource getSecondary2() {
                return new ColorUIResource(ResourceManager.getColor("lookAndFeel.disabled.color"));
            }

            protected ColorUIResource getSecondary3() {
                return new ColorUIResource(ResourceManager.getColor("lookAndFeel.background.color"));
            }
        });
    }


    
    
   
    public UIDefaults getDefaults() {
        UIDefaults u = super.getDefaults();

        try {
            u.put("CheckBoxUI", "net.sf.freecol.client.gui.plaf.FreeColCheckBoxUI");
            u.put("net.sf.freecol.client.gui.plaf.FreeColCheckBoxUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColCheckBoxUI"));
            u.put("ComboBoxUI", "net.sf.freecol.client.gui.plaf.FreeColComboBoxUI");
            u.put("net.sf.freecol.client.gui.plaf.FreeColComboBoxUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColComboBoxUI"));
            u.put("RadioButtonUI", "net.sf.freecol.client.gui.plaf.FreeColRadioButtonUI");
            u.put("net.sf.freecol.client.gui.plaf.FreeColRadioButtonUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColRadioButtonUI"));
            u.put("ButtonUI", "net.sf.freecol.client.gui.plaf.FreeColButtonUI");
            u.put("net.sf.freecol.client.gui.plaf.FreeColButtonUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColButtonUI"));
            u.put("TextFieldUI", "net.sf.freecol.client.gui.plaf.FreeColTextFieldUI");
            u.put("net.sf.freecol.client.gui.plaf.FreeColTextFieldUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColTextFieldUI"));
            u.put("TextAreaUI", "net.sf.freecol.client.gui.plaf.FreeColTextAreaUI");
            u.put("net.sf.freecol.client.gui.plaf.FreeColTextAreaUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColTextAreaUI"));
            u.put("PanelUI", "net.sf.freecol.client.gui.plaf.FreeColPanelUI");
            u.put("net.sf.freecol.client.gui.plaf.FreeColPanelUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColPanelUI"));
            u.put("MenuBarUI", "net.sf.freecol.client.gui.plaf.FreeColMenuBarUI");
            u.put("net.sf.freecol.client.gui.plaf.FreeColMenuBarUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColMenuBarUI"));
            u.put("PopupMenuUI", "net.sf.freecol.client.gui.plaf.FreeColPopupMenuUI");
            u.put("net.sf.freecol.client.gui.plaf.FreeColPopupMenuUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColPopupMenuUI"));
            u.put("LabelUI", "net.sf.freecol.client.gui.plaf.FreeColLabelUI");
            u.put("net.sf.freecol.client.gui.plaf.FreeColLabelUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColLabelUI"));
            u.put("MenuItemUI", "net.sf.freecol.client.gui.plaf.FreeColMenuItemUI");
            u.put("net.sf.freecol.client.gui.plaf.FreeColMenuItemUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColMenuItemUI"));
            u.put("ListUI", "net.sf.freecol.client.gui.plaf.FreeColListUI");
            u.put("net.sf.freecol.client.gui.plaf.FreeColListUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColListUI"));
            u.put("TableUI", "net.sf.freecol.client.gui.plaf.FreeColTableUI");
            u.put("net.sf.freecol.client.gui.plaf.FreeColTableUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColTableUI"));
            u.put("TableHeaderUI", "net.sf.freecol.client.gui.plaf.FreeColTableHeaderUI");
            u.put("net.sf.freecol.client.gui.plaf.FreeColTableHeaderUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColTableHeaderUI"));
            u.put("ScrollPaneUI", "net.sf.freecol.client.gui.plaf.FreeColScrollPaneUI");
            u.put("net.sf.freecol.client.gui.plaf.FreeColScrollPaneUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColScrollPaneUI"));
            u.put("ToolTipUI", "net.sf.freecol.client.gui.plaf.FreeColToolTipUI");
            u.put("net.sf.freecol.client.gui.plaf.FreeColToolTipUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColToolTipUI"));            
            
            
            

            
            u.put("net.sf.freecol.client.gui.plaf.FreeColBrightPanelUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColBrightPanelUI"));
            u.put("InPortPanelUI", "net.sf.freecol.client.gui.plaf.FreeColBrightPanelUI");            
            u.put("CargoPanelUI", "net.sf.freecol.client.gui.plaf.FreeColBrightPanelUI");
            u.put("BuildingsPanelUI", "net.sf.freecol.client.gui.plaf.FreeColPanelUI");
            u.put("OutsideColonyPanelUI", "net.sf.freecol.client.gui.plaf.FreeColBrightPanelUI");
            u.put("InPortPanelUI", "net.sf.freecol.client.gui.plaf.FreeColBrightPanelUI");
            u.put("WarehousePanelUI", "net.sf.freecol.client.gui.plaf.FreeColBrightPanelUI");

            
            u.put("net.sf.freecol.client.gui.plaf.FreeColTransparentPanelUI", Class.forName("net.sf.freecol.client.gui.plaf.FreeColTransparentPanelUI"));
            u.put("MarketPanelUI", "net.sf.freecol.client.gui.plaf.FreeColTransparentPanelUI");
            u.put("EuropeCargoPanelUI", "net.sf.freecol.client.gui.plaf.FreeColTransparentPanelUI");
            u.put("ToAmericaPanelUI", "net.sf.freecol.client.gui.plaf.FreeColTransparentPanelUI");
            u.put("ToEuropePanelUI", "net.sf.freecol.client.gui.plaf.FreeColTransparentPanelUI");
            u.put("EuropeInPortPanelUI", "net.sf.freecol.client.gui.plaf.FreeColTransparentPanelUI");
            u.put("DocksPanelUI", "net.sf.freecol.client.gui.plaf.FreeColTransparentPanelUI");
            
            
            InputStream faStream = null;            
            File f = new File(dataDirectory, "fonts" + System.getProperty("file.separator") + "signature.faf");
            if (f.exists() && f.isFile()) {
                try {
                    faStream = new FileInputStream(f.toString());
                } catch (FileNotFoundException e) {} 
            } else {
                URL url = resourceLocator.getResource("data/fonts/signature.faf");
                if (url != null) {
                    try {
                        faStream = url.openStream();
                    } catch (IOException e) {} 
                }
            }
            try {
                if (faStream != null) {
                    u.put("Declaration.signature.font", new FAFile(faStream));
                }
            } catch (IOException e) {
                logger.warning("Could not load the Font Animation File for the signature.");
            }
            
            
            String [][] resources = {                
            };

            
            MediaTracker mt = new MediaTracker(new Component() {});
            
            for (int i=0; i<resources.length; i++) {
                Image image = null;                
                File file = new File(dataDirectory, "images" + System.getProperty("file.separator") + "ui" + System.getProperty("file.separator") + resources[i][1]);
                
                if (file.exists() && file.isFile()) {
                    image = Toolkit.getDefaultToolkit().getImage(file.toString());    
                } else {
                    URL url = resourceLocator.getResource("data/images/ui/"+  resources[i][1]);
                    if (url != null) {
                        image = Toolkit.getDefaultToolkit().getImage(url);
                    }
                }

                if (image == null) {
                    logger.warning("Could not find image: " + resources[i][1]);
                } else {
                    mt.addImage(image, 0);
                    u.put(resources[i][0], image);
                }
            }
            
            
            
                
            try {
                mt.waitForID(0, 30000); 
            } catch (InterruptedException e) {
                logger.warning("Interrupted while loading resources!");
            }
            
            
            resources = new String[][] {                
                {"HeaderFont", Messages.message("HeaderFont")},
            };                  

            for (int i=0; i<resources.length; i++) {
                InputStream fontStream = null; 
                
                File file = new File(dataDirectory, "fonts" + System.getProperty("file.separator") + resources[i][1]);
                if (file.exists() && file.isFile()) {
                    try {
                        fontStream = new FileInputStream(file.toString());
                    } catch (FileNotFoundException e) {} 
                } else {
                    URL url = resourceLocator.getResource("data/fonts/" + resources[i][1]);
                    if (url != null) {
                        try {
                            fontStream = url.openStream();
                        } catch (IOException e) {} 
                    }
                }    

                if (fontStream != null) {  
                    try {
                        u.put(resources[i][0], Font.createFont(Font.TRUETYPE_FONT, fontStream));
                    } catch (FontFormatException e) {
                        logger.warning("Could not load font: " + resources[i][1] + " because it has the wrong format.");
                        u.put(resources[i][0], new Font("SansSerif", Font.PLAIN, 1));
                    } catch (IOException ie) {
                        logger.warning("Could not load font: " + resources[i][1] + " because of an IO problem.");
                        u.put(resources[i][0], new Font("SansSerif", Font.PLAIN, 1));                
                    }                                
                } else {           
                    logger.warning("Could not find font: " + resources[i][1]);
                    u.put(resources[i][0], new Font("SansSerif", Font.PLAIN, 1));
                }
            }          
            
            
            
            Image im = (Image) u.get("cursor.go.image");
            if (im != null) {
                u.put("cursor.go", Toolkit.getDefaultToolkit().createCustomCursor(im, new Point(im.getWidth(null)/2, im.getHeight(null)/2), "go"));
            } else {
                u.put("cursor.go", Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Failed to load look and feel!", e);
            System.exit(-1);
        }

        return u;
    }
    
    
    
    public String getDescription() {
        return "The default Look and Feel for FreeCol";
    }


    
    public String getName() {
        return "FreeCol Look and Feel";
    }
}
