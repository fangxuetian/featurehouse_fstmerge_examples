

package net.sf.freecol.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.freecol.common.Specification;
import net.sf.freecol.common.io.Mods;
import net.sf.freecol.common.io.FreeColModFile.ModInfo;
import net.sf.freecol.common.model.Colony;
import net.sf.freecol.common.model.Europe;
import net.sf.freecol.common.model.FreeColGameObject;
import net.sf.freecol.common.model.ModelMessage;
import net.sf.freecol.common.model.Player;
import net.sf.freecol.common.model.Unit;
import net.sf.freecol.common.option.AudioMixerOption;
import net.sf.freecol.common.option.BooleanOption;
import net.sf.freecol.common.option.ListOption;
import net.sf.freecol.common.option.ListOptionSelector;
import net.sf.freecol.common.option.OptionGroup;
import net.sf.freecol.common.option.OptionMap;
import net.sf.freecol.common.option.PercentageOption;
import net.sf.freecol.common.option.SelectOption;

import org.w3c.dom.Element;


public class ClientOptions extends OptionMap {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(ClientOptions.class.getName());

    
    public static final String LANGUAGE = "model.option.languageOption";

    
    public static final String JUMP_TO_ACTIVE_UNIT = "model.option.jumpToActiveUnit";

    
    public static final String ALWAYS_CENTER = "model.option.alwaysCenter";

    
    public static final String MIN_NUMBER_FOR_DISPLAYING_GOODS = "model.option.guiMinNumberToDisplayGoods";

    
    public static final String MIN_NUMBER_FOR_DISPLAYING_GOODS_COUNT = "model.option.guiMinNumberToDisplayGoodsCount";

    
    public static final String MAX_NUMBER_OF_GOODS_IMAGES = "model.option.guiMaxNumberOfGoodsImages";

    
    public static final String DISPLAY_COMPASS_ROSE = "model.option.displayCompassRose";

    
    public static final String DISPLAY_MAP_CONTROLS = "model.option.displayMapControls";

    
    public static final String DISPLAY_GRID = "model.option.displayGrid";

    
    public static final String UNIT_LAST_MOVE_DELAY = "model.option.unitLastMoveDelay";
    
    
    public static final String DISPLAY_BORDERS = "model.option.displayBorders";

    
    public static final String DISPLAY_TILE_TEXT = "model.option.displayTileText";

    public static final int DISPLAY_TILE_TEXT_EMPTY = 0, DISPLAY_TILE_TEXT_NAMES = 1, DISPLAY_TILE_TEXT_OWNERS = 2, DISPLAY_TILE_TEXT_REGIONS = 3;

    
    public static final String MOVE_ANIMATION_SPEED = "model.option.moveAnimationSpeed";

    
    public static final String ENEMY_MOVE_ANIMATION_SPEED = "model.option.enemyMoveAnimationSpeed";

    
    public static final String MESSAGES_GROUP_BY = "model.option.guiMessagesGroupBy";

    public static final int MESSAGES_GROUP_BY_NOTHING = 0;

    public static final int MESSAGES_GROUP_BY_TYPE = 1;

    public static final int MESSAGES_GROUP_BY_SOURCE = 2;

    public static final String AUDIO_MIXER = "audioMixer";

    public static final String MUSIC_VOLUME = "musicVolume";

    public static final String SFX_VOLUME = "sfxVolume";

    
    public static final String SHOW_SONS_OF_LIBERTY = "model.option.guiShowSonsOfLiberty";

    public static final String SHOW_WARNING = "model.option.guiShowWarning";

    public static final String SHOW_GOVERNMENT_EFFICIENCY = "model.option.guiShowGovernmentEfficiency";

    public static final String SHOW_WAREHOUSE_CAPACITY = "model.option.guiShowWarehouseCapacity";

    public static final String SHOW_UNIT_IMPROVED = "model.option.guiShowUnitImproved";

    public static final String SHOW_UNIT_DEMOTED = "model.option.guiShowUnitDemoted";

    public static final String SHOW_UNIT_ADDED = "model.option.guiShowUnitAdded";

    public static final String SHOW_UNIT_LOST = "model.option.guiShowUnitLost";

    public static final String SHOW_BUILDING_COMPLETED = "model.option.guiShowBuildingCompleted";

    public static final String SHOW_FOREIGN_DIPLOMACY = "model.option.guiShowForeignDiplomacy";

    public static final String SHOW_MARKET_PRICES = "model.option.guiShowMarketPrices";

    public static final String SHOW_MISSING_GOODS = "model.option.guiShowMissingGoods";

    public static final String SHOW_TUTORIAL = "model.option.guiShowTutorial";

    public static final String SHOW_COLONY_WARNINGS = "model.option.guiShowColonyWarnings";

    public static final String SHOW_PRECOMBAT = "model.option.guiShowPreCombat";

    
    public static final String SHOW_SAVEGAME_SETTINGS = "model.option.showSavegameSettings";

    
    public static final int SHOW_SAVEGAME_SETTINGS_NEVER = 0;

    
    public static final int SHOW_SAVEGAME_SETTINGS_MULTIPLAYER = 1;

    
    public static final int SHOW_SAVEGAME_SETTINGS_ALWAYS = 2;

    
    public static final String AUTOSAVE_PERIOD = "model.option.autosavePeriod";

    
    public static final String AUTOSAVE_GENERATIONS = "model.option.autosaveGenerations";

    
    public static final String DISPLAY_FOG_OF_WAR = "model.option.displayFogOfWar";

    
    public static final String MAP_SCROLL_ON_DRAG = "model.option.mapScrollOnDrag";

	
    public static final String AUTO_SCROLL = "model.option.autoScroll";

	
    
    public static final String AUTOLOAD_EMIGRANTS = "model.option.autoloadEmigrants";

    
    public static final String SMOOTH_MINIMAP_RENDERING = "model.option.smoothRendering";

    
    public static final String DEFAULT_MINIMAP_ZOOM = "model.option.defaultZoomLevel";

    
    public static final String MINIMAP_BACKGROUND_COLOR = "model.option.color.background";

    public static final String USER_MODS ="userMods";
    
    
    public static final String CUSTOM_STOCK = "model.option.customStock";

    
    public static final String LOW_LEVEL = "model.option.lowLevel";

    
    public static final String HIGH_LEVEL = "model.option.highLevel";

    
    public static final String COLONY_COMPARATOR = "model.option.colonyComparator";

    public static final int COLONY_COMPARATOR_NAME = 0, COLONY_COMPARATOR_AGE = 1, COLONY_COMPARATOR_POSITION = 2,
            COLONY_COMPARATOR_SIZE = 3, COLONY_COMPARATOR_SOL = 4;

    
    public static final String AUTO_END_TURN = "model.option.autoEndTurn";

    
    public static final String LABOUR_REPORT = "model.option.labourReport";
    public static final int LABOUR_REPORT_CLASSIC = 0;
    public static final int LABOUR_REPORT_COMPACT = 1;

    
    public static final String INDIAN_DEMAND_RESPONSE = "model.option.indianDemandResponse";
    public static final int INDIAN_DEMAND_RESPONSE_ASK = 0;
    public static final int INDIAN_DEMAND_RESPONSE_ACCEPT = 1;
    public static final int INDIAN_DEMAND_RESPONSE_REJECT = 2;

    
    public static final String UNLOAD_OVERFLOW_RESPONSE = "model.option.unloadOverflowResponse";
    public static final int UNLOAD_OVERFLOW_RESPONSE_ASK = 0;
    public static final int UNLOAD_OVERFLOW_RESPONSE_NEVER = 1;
    public static final int UNLOAD_OVERFLOW_RESPONSE_ALWAYS = 2;

    
    private static Comparator<Colony> colonyAgeComparator = new Comparator<Colony>() {
        
        public int compare(Colony s1, Colony s2) {
            return s1.getIntegerID().compareTo(s2.getIntegerID());
        }
    };

    private static Comparator<Colony> colonyNameComparator = new Comparator<Colony>() {
        public int compare(Colony s1, Colony s2) {
            return s1.getName().compareTo(s2.getName());
        }
    };

    private static Comparator<Colony> colonySizeComparator = new Comparator<Colony>() {
        
        public int compare(Colony s1, Colony s2) {
            int dsize = s2.getUnitCount() - s1.getUnitCount();
            if (dsize == 0) {
                return s2.getSoL() - s1.getSoL();
            } else {
                return dsize;
            }
        }
    };

    private static Comparator<Colony> colonySoLComparator = new Comparator<Colony>() {
        
        public int compare(Colony s1, Colony s2) {
            int dsol = s2.getSoL() - s1.getSoL();
            if (dsol == 0) {
                return s2.getUnitCount() - s1.getUnitCount();
            } else {
                return dsol;
            }
        }
    };

    private static Comparator<Colony> colonyPositionComparator = new Comparator<Colony>() {
        
        public int compare(Colony s1, Colony s2) {
            int dy = s1.getTile().getY() - s2.getTile().getY();
            if (dy == 0) {
                return s1.getTile().getX() - s2.getTile().getX();
            } else {
                return dy;
            }
        }
    };

    private Comparator<ModelMessage> messageSourceComparator = new Comparator<ModelMessage>() {
        
        public int compare(ModelMessage message1, ModelMessage message2) {
            Object source1 = message1.getSource();
            Object source2 = message2.getSource();
            if (source1 == source2) {
                return messageTypeComparator.compare(message1, message2);
            }
            int base = getClassIndex(source1) - getClassIndex(source2);
            if (base == 0) {
                if (source1 instanceof Colony) {
                    return getColonyComparator().compare((Colony) source1, (Colony) source2);
                }
            }
            return base;
        }

        private int getClassIndex(Object object) {
            if (object instanceof Player) {
                return 10;
            } else if (object instanceof Colony) {
                return 20;
            } else if (object instanceof Europe) {
                return 30;
            } else if (object instanceof Unit) {
                return 40;
            } else if (object instanceof FreeColGameObject) {
                return 50;
            } else {
                return 1000;
            }
        }

    };

    private Comparator<ModelMessage> messageTypeComparator = new Comparator<ModelMessage>() {
        
        public int compare(ModelMessage message1, ModelMessage message2) {
            int dtype = message1.getType().ordinal() - message2.getType().ordinal();
            if (dtype == 0 && message1.getSource() != message2.getSource()) {
                return messageSourceComparator.compare(message1, message2);
            } else {
                return dtype;
            }
        }
    };


    
    public ClientOptions() {
        super(getXMLElementTagName());
    }

    
    public ClientOptions(Element element) {
        super(element, getXMLElementTagName());
    }

    
    protected void addDefaultOptions() {
        Specification spec = Specification.getSpecification();
        
        OptionGroup guiGroup = spec.getOptionGroup("clientOptions.gui");
        guiGroup.add(spec.getOptionGroup("clientOptions.minimap"));
        add(guiGroup);
        
        add(spec.getOptionGroup("clientOptions.messages"));

        OptionGroup audioGroup = new OptionGroup("clientOptions.audio");
        new AudioMixerOption(AUDIO_MIXER, audioGroup);
        new PercentageOption(MUSIC_VOLUME, audioGroup, 100).setPreviewEnabled(true);
        new PercentageOption(SFX_VOLUME, audioGroup, 100).setPreviewEnabled(true);
        add(audioGroup);

        add(spec.getOptionGroup("clientOptions.savegames"));
        add(spec.getOptionGroup("clientOptions.warehouse"));
        add(spec.getOptionGroup("clientOptions.other"));
        
        final OptionGroup modsGroup = new OptionGroup("clientOptions.mods");
        final ListOptionSelector<ModInfo> selector = new ListOptionSelector<ModInfo>() {
            private Map<String, ModInfo> mods = null; 
            private void init() {
                if (mods == null) {
                    final List<ModInfo> modInfos = Mods.getModInfos();
                    mods = new HashMap<String, ModInfo>();
                    for (ModInfo mi : modInfos) {
                        mods.put(mi.getId(), mi);
                    }
                }
            }
            public String getId(ModInfo t) {
                return t.getId();
            }
            public ModInfo getObject(String id) {
                init();
                return mods.get(id);
            }
            public List<ModInfo> getOptions() {
                init();
                return new ArrayList<ModInfo>(mods.values());
            }
            public String toString(ModInfo t) {
                return t.getName();
            }
        };
        new ListOption<ModInfo>(selector, USER_MODS, modsGroup);
        add(modsGroup);
    }

    
    public int getDisplayTileText() {
        return getInteger(DISPLAY_TILE_TEXT);
    }

    
    public Comparator<Colony> getColonyComparator() {
        return getColonyComparator(getInteger(COLONY_COMPARATOR));
    }

    
    public static Comparator<Colony> getColonyComparator(int type) {
        switch (type) {
        case COLONY_COMPARATOR_AGE:
            return colonyAgeComparator;
        case COLONY_COMPARATOR_POSITION:
            return colonyPositionComparator;
        case COLONY_COMPARATOR_SIZE:
            return colonySizeComparator;
        case COLONY_COMPARATOR_SOL:
            return colonySoLComparator;
        case COLONY_COMPARATOR_NAME:
            return colonyNameComparator;
        default:
            throw new IllegalStateException("Unknown comparator");
        }
    }

    
    public Comparator<ModelMessage> getModelMessageComparator() {
        switch (getInteger(MESSAGES_GROUP_BY)) {
        case MESSAGES_GROUP_BY_SOURCE:
            return messageSourceComparator;
        case MESSAGES_GROUP_BY_TYPE:
            return messageTypeComparator;
        default:
            return null;
        }
    }

    
    public BooleanOption getBooleanOption(ModelMessage message) {
        switch (message.getType()) {
        case WARNING:
            return (BooleanOption) getObject(ClientOptions.SHOW_WARNING);
        case SONS_OF_LIBERTY:
            return (BooleanOption) getObject(ClientOptions.SHOW_SONS_OF_LIBERTY);
        case GOVERNMENT_EFFICIENCY:
            return (BooleanOption) getObject(ClientOptions.SHOW_GOVERNMENT_EFFICIENCY);
        case WAREHOUSE_CAPACITY:
            return (BooleanOption) getObject(ClientOptions.SHOW_WAREHOUSE_CAPACITY);
        case UNIT_IMPROVED:
            return (BooleanOption) getObject(ClientOptions.SHOW_UNIT_IMPROVED);
        case UNIT_DEMOTED:
            return (BooleanOption) getObject(ClientOptions.SHOW_UNIT_DEMOTED);
        case UNIT_LOST:
            return (BooleanOption) getObject(ClientOptions.SHOW_UNIT_LOST);
        case UNIT_ADDED:
            return (BooleanOption) getObject(ClientOptions.SHOW_UNIT_ADDED);
        case BUILDING_COMPLETED:
            return (BooleanOption) getObject(ClientOptions.SHOW_BUILDING_COMPLETED);
        case FOREIGN_DIPLOMACY:
            return (BooleanOption) getObject(ClientOptions.SHOW_FOREIGN_DIPLOMACY);
        case MARKET_PRICES:
            return (BooleanOption) getObject(ClientOptions.SHOW_MARKET_PRICES);
        case MISSING_GOODS:
            return (BooleanOption) getObject(ClientOptions.SHOW_MISSING_GOODS);
        case DEFAULT:
        default:
            return null;
        }
    }

    protected boolean isCorrectTagName(String tagName) {
        return getXMLElementTagName().equals(tagName);
    }

    
    public static String getXMLElementTagName() {
        return "clientOptions";
    }

}
