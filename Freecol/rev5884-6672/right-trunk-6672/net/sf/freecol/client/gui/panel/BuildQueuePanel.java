

package net.sf.freecol.client.gui.panel;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;

import net.sf.freecol.client.gui.i18n.Messages;
import net.sf.freecol.client.gui.Canvas;
import net.sf.freecol.client.gui.plaf.FreeColComboBoxRenderer;

import net.sf.freecol.FreeCol;
import net.sf.freecol.common.model.Ability;
import net.sf.freecol.common.model.AbstractGoods;
import net.sf.freecol.common.model.BuildableType;
import net.sf.freecol.common.model.Building;
import net.sf.freecol.common.model.BuildingType;
import net.sf.freecol.common.model.Colony;
import net.sf.freecol.common.model.FeatureContainer;
import net.sf.freecol.common.model.FreeColGameObjectType;
import net.sf.freecol.common.model.StringTemplate;
import net.sf.freecol.common.model.UnitType;
import net.sf.freecol.common.resources.ResourceManager;
import net.sf.freecol.common.util.Utils;

import net.miginfocom.swing.MigLayout;


public class BuildQueuePanel extends FreeColPanel implements ActionListener, ItemListener {

    private static Logger logger = Logger.getLogger(BuildQueuePanel.class.getName());

    private static final String BUY = "buy";

    private final BuildQueueTransferHandler buildQueueHandler = new BuildQueueTransferHandler();

    private ListCellRenderer cellRenderer;
    private static JCheckBox compact = new JCheckBox();
    private static JCheckBox showAll = new JCheckBox();
    private JList buildQueueList;
    private JList unitList;
    private JList buildingList;
    private JButton buyBuilding;
    private Colony colony;
    private int unitCount;

    private FeatureContainer featureContainer = new FeatureContainer();

    private Map<BuildableType, String> lockReasons = new HashMap<BuildableType, String>();
    private Set<BuildableType> unbuildableTypes = new HashSet<BuildableType>();

    
    private static final List<UnitType> buildableUnits = new ArrayList<UnitType>();

    static {
        for (UnitType unitType : FreeCol.getSpecification().getUnitTypeList()) {
            if (!unitType.getGoodsRequired().isEmpty()) {
                
                buildableUnits.add(unitType);
            }
        }
    }

    public BuildQueuePanel(Colony colony, Canvas parent) {

        super(parent, new MigLayout("wrap 3", "[260:][260:][260:]", "[][][300:400:][]"));
        this.colony = colony;
        this.unitCount = colony.getUnitCount();

        DefaultListModel current = new DefaultListModel();
        for (BuildableType type : colony.getBuildQueue()) {
            current.addElement(type);
            featureContainer.add(type.getFeatureContainer());
        }

        cellRenderer = getCellRenderer();

        compact.setText(Messages.message("colonyPanel.compactView"));
        compact.addItemListener(this);

        showAll.setText(Messages.message("colonyPanel.showAll"));
        showAll.addItemListener(this);

        buildQueueList = new JList(current);
        buildQueueList.setTransferHandler(buildQueueHandler);
        buildQueueList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        buildQueueList.setDragEnabled(true);
        buildQueueList.setCellRenderer(cellRenderer);
        buildQueueList.addMouseListener(new BuildQueueMouseAdapter(false));

        Action deleteAction = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    DefaultListModel model = (DefaultListModel) buildQueueList.getModel();
                    for (Object type : buildQueueList.getSelectedValues()) {
                        model.removeElement(type);
                    }
                    updateAllLists();
                }
            };

        buildQueueList.getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "delete");
        buildQueueList.getActionMap().put("delete", deleteAction);

        Action addAction = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    DefaultListModel model = (DefaultListModel) buildQueueList.getModel();
                    for (Object type : ((JList) e.getSource()).getSelectedValues()) {
                        model.addElement(type);
                    }
                    updateAllLists();
                }
            };

        BuildQueueMouseAdapter adapter = new BuildQueueMouseAdapter(true);
        DefaultListModel units = new DefaultListModel();
        unitList = new JList(units);
        unitList.setTransferHandler(buildQueueHandler);
        unitList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        unitList.setDragEnabled(true);
        unitList.setCellRenderer(cellRenderer);
        unitList.addMouseListener(adapter);

        unitList.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "add");
        unitList.getActionMap().put("add", addAction);

        DefaultListModel buildings = new DefaultListModel();
        buildingList = new JList(buildings);
        buildingList.setTransferHandler(buildQueueHandler);
        buildingList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        buildingList.setDragEnabled(true);
        buildingList.setCellRenderer(cellRenderer);
        buildingList.addMouseListener(adapter);

        buildingList.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "add");
        buildingList.getActionMap().put("add", addAction);

        JLabel headLine = new JLabel(Messages.message("colonyPanel.buildQueue"));
        headLine.setFont(bigHeaderFont);

        buyBuilding = new JButton(Messages.message("colonyPanel.buyBuilding"));
        buyBuilding.setActionCommand(BUY);
        buyBuilding.addActionListener(this);

        updateAllLists();

        add(headLine, "span 3, align center, wrap 40");
        add(new JLabel(Messages.message("menuBar.colopedia.unit")), "align center");
        add(new JLabel(Messages.message("colonyPanel.buildQueue")), "align center");
        add(new JLabel(Messages.message("menuBar.colopedia.building")), "align center");
        add(new JScrollPane(unitList), "grow");
        add(new JScrollPane(buildQueueList), "grow");
        add(new JScrollPane(buildingList), "grow, wrap 20");
        add(buyBuilding, "span, split 4");
        add(compact);
        add(showAll);
        add(okButton, "tag ok");
    }

    private void updateUnitList() {
        DefaultListModel units = (DefaultListModel) unitList.getModel();
        units.clear();
        loop: for (UnitType unitType : buildableUnits) {
            
            List<String> lockReason = new ArrayList<String>();
            if (unbuildableTypes.contains(unitType)) {
                continue;
            }

            if (unitType.getPopulationRequired() > unitCount) {
                lockReason.add(Messages.message("colonyPanel.populationTooSmall", "%number%",
                                                Integer.toString(unitType.getPopulationRequired())));
            }

            if (!(colony.getFeatureContainer()
                  .hasAbility("model.ability.build", unitType, getGame().getTurn())
                  || featureContainer.hasAbility("model.ability.build", unitType))) {
                boolean builderFound = false;
                for (Ability ability : FreeCol.getSpecification().getAbilities("model.ability.build")) {
                    if (ability.appliesTo(unitType)
                        && ability.getValue()
                        && ability.getSource() != null
                        && !unbuildableTypes.contains(ability.getSource())) {
                        builderFound = true;
                        lockReason.add(Messages.message(ability.getSource().getNameKey()));
                        break;
                    }
                }
                if (!builderFound) {
                    unbuildableTypes.add(unitType);
                    continue;
                }
            }

            Map<String, Boolean> requiredAbilities = unitType.getAbilitiesRequired();
            for (Entry<String, Boolean> entry : requiredAbilities.entrySet()) {
                if (colony.hasAbility(entry.getKey()) != entry.getValue()
                    && featureContainer.hasAbility(entry.getKey()) != entry.getValue()) {
                    List<FreeColGameObjectType> sources = FreeCol.getSpecification()
                        .getTypesProviding(entry.getKey(), entry.getValue());
                    if (sources.isEmpty()) {
                        
                        unbuildableTypes.add(unitType);
                        continue loop;
                    } else {
                        lockReason.add(Messages.message(sources.get(0).getNameKey()));
                    }
                }
            }
            if (lockReason.isEmpty()) {
                lockReasons.put(unitType, null);
            } else {
                lockReasons.put(unitType, Messages.message("colonyPanel.requires", "%string%",
                                                           Utils.join("/", lockReason)));
            }
            if (lockReason.isEmpty() || showAll.isSelected()) {
                units.addElement(unitType);
            }
        }
    }

    private void updateBuildingList() {
        DefaultListModel buildings = (DefaultListModel) buildingList.getModel();
        DefaultListModel current = (DefaultListModel) buildQueueList.getModel();
        buildings.clear();
        loop: for (BuildingType buildingType : FreeCol.getSpecification().getBuildingTypeList()) {
            
            List<String> lockReason = new ArrayList<String>();
            Building colonyBuilding = colony.getBuilding(buildingType);
            if (current.contains(buildingType) || hasBuildingType(buildingType)) {
                
                continue;
            } else if (unbuildableTypes.contains(buildingType)) {
                continue;
            } else if (buildingType.getGoodsRequired().isEmpty()) {
                
                continue;
            } else if (unbuildableTypes.contains(buildingType.getUpgradesFrom())) {
                
                unbuildableTypes.add(buildingType);
                continue;
            }

            if (buildingType.getPopulationRequired() > unitCount) {
                lockReason.add(Messages.message("colonyPanel.populationTooSmall", "%number%",
                                                Integer.toString(buildingType.getPopulationRequired())));
            }

            Map<String, Boolean> requiredAbilities = buildingType.getAbilitiesRequired();
            for (Entry<String, Boolean> entry : requiredAbilities.entrySet()) {
                if (colony.hasAbility(entry.getKey()) != entry.getValue()
                    && featureContainer.hasAbility(entry.getKey()) != entry.getValue()) {
                    List<FreeColGameObjectType> sources = FreeCol.getSpecification()
                        .getTypesProviding(entry.getKey(), entry.getValue());
                    if (sources.isEmpty()) {
                        
                        unbuildableTypes.add(buildingType);
                        continue loop;
                    } else {
                        lockReason.add(Messages.message(sources.get(0).getNameKey()));
                    }
                }
            }

            if (buildingType.getUpgradesFrom() != null
                && !current.contains(buildingType.getUpgradesFrom())) {
                if (colonyBuilding == null
                    || colonyBuilding.getType() != buildingType.getUpgradesFrom()) {
                    lockReason.add(Messages.message(buildingType.getUpgradesFrom().getNameKey()));
                }
            }
            if (lockReason.isEmpty()) {
                lockReasons.put(buildingType, null);
            } else {
                lockReasons.put(buildingType, Messages.message("colonyPanel.requires", "%string%",
                                                               Utils.join("/", lockReason)));
            }
            if (lockReason.isEmpty() || showAll.isSelected()) {
                buildings.addElement(buildingType);
            }
        }
    }

    private void updateAllLists() {
        DefaultListModel current = (DefaultListModel) buildQueueList.getModel();
        featureContainer = new FeatureContainer();
        for (Object type: current.toArray()) {
            if (getMinimumIndex((BuildableType) type) >= 0) {
                featureContainer.add(((BuildableType) type).getFeatureContainer());
            } else {
                current.removeElement(type);
            }
        }
        
        
        
        updateBuildingList();
        updateUnitList();
        updateBuyBuildingButton();
    }

    private void updateBuyBuildingButton() {
        DefaultListModel current = (DefaultListModel) buildQueueList.getModel();
        if (current.getSize() == 0) {
            buyBuilding.setEnabled(false);
        } else {
            buyBuilding.setEnabled(colony.canPayToFinishBuilding((BuildableType) current.getElementAt(0)));
        }
    }

    private boolean hasBuildingType(BuildingType buildingType) {
        if (colony.getBuilding(buildingType) == null) {
            return false;
        } else if (colony.getBuilding(buildingType).getType() == buildingType) {
            return true;
        } else if (buildingType.getUpgradesTo() != null) {
            return hasBuildingType(buildingType.getUpgradesTo());
        } else {
            return false;
        }
    }

    private List<BuildableType> getBuildableTypes(JList list) {
        List<BuildableType> result = new ArrayList<BuildableType>();
        if (list != null) {
            ListModel model = list.getModel();
            for (int index = 0; index < model.getSize(); index++) {
                Object object = model.getElementAt(index);
                if (object instanceof BuildableType) {
                    result.add((BuildableType) object);
                }
            }
        }
        return result;
    }

    private List<BuildableType> getBuildableTypes(Object[] objects) {
        List<BuildableType> result = new ArrayList<BuildableType>();
        if (objects != null) {
            for (Object object : objects) {
                if (object instanceof BuildableType) {
                    result.add((BuildableType) object);
                }
            }
        }
        return result;
    }

    private int getMinimumIndex(BuildableType buildableType) {
        ListModel buildQueue = buildQueueList.getModel();
        if (buildableType instanceof UnitType) {
            if (colony.canBuild(buildableType)) {
                return 0;
            } else {
                for (int index = 0; index < buildQueue.getSize(); index++) {
                    if (((BuildableType) buildQueue.getElementAt(index))
                        .hasAbility("model.ability.build", buildableType)) {
                        return index + 1;
                    }
                }
            }
        } else if (buildableType instanceof BuildingType) {
            BuildingType upgradesFrom = ((BuildingType) buildableType).getUpgradesFrom();
            if (upgradesFrom == null) {
                return 0;
            } else {
                Building building = colony.getBuilding((BuildingType) buildableType);
                BuildingType buildingType = (building == null) ? null : building.getType();
                if (buildingType == upgradesFrom) {
                    return 0;
                } else {
                    for (int index = 0; index < buildQueue.getSize(); index++) {
                        if (upgradesFrom.equals(buildQueue.getElementAt(index))) {
                            return index + 1;
                        }
                    }
                }
            }
        }
        return -1;
    }


    
    public void actionPerformed(ActionEvent event) {
        if (colony.getOwner() == getMyPlayer()) {
            String command = event.getActionCommand();
            List<BuildableType> buildables = getBuildableTypes(buildQueueList);
            if (!buildables.isEmpty() && lockReasons.get(buildables.get(0)) != null) {
                getCanvas().showInformationMessage(StringTemplate.template("colonyPanel.unbuildable")
                                                   .addName("%colony%", colony.getName())
                                                   .add("%object%", buildables.get(0).getNameKey()),
                                                   buildables.get(0));
                return;
            }
            getController().setBuildQueue(colony, buildables);
            if (OK.equals(command)) {
                
            } else if (BUY.equals(command)) {
                getController().payForBuilding(colony);
                getCanvas().updateGoldLabel();
            } else {
                logger.warning("Unsupported command " + command);
            }
        }
        getCanvas().remove(this);
    }

    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == compact) {
            updateDetailView();
        } else if (event.getSource() == showAll) {
            updateAllLists();
        }
    }

    private void updateDetailView() {
        cellRenderer = getCellRenderer();
        buildQueueList.setCellRenderer(cellRenderer);
        buildingList.setCellRenderer(cellRenderer);
        unitList.setCellRenderer(cellRenderer);
    }

    private ListCellRenderer getCellRenderer() {
        if (compact.isSelected()) {
            if (cellRenderer == null || cellRenderer instanceof DefaultBuildQueueCellRenderer) {
                return new SimpleBuildQueueCellRenderer();
            }
        } else if (cellRenderer == null || cellRenderer instanceof SimpleBuildQueueCellRenderer) {
            return new DefaultBuildQueueCellRenderer();
        }
                
        
        return cellRenderer;
    }

    
    public class BuildQueueTransferHandler extends TransferHandler {

        private final DataFlavor buildQueueFlavor = new DataFlavor(List.class, "BuildingQueueFlavor");

        JList source = null;
        int[] indices = null;
        int targetIndex = -1;  
        int numberOfItems = 0;  

        
        public boolean importData(JComponent comp, Transferable data) {

            if (!canImport(comp, data.getTransferDataFlavors())) {
                return false;
            }

            JList target = null;
            List<BuildableType> buildQueue = new ArrayList<BuildableType>();
            DefaultListModel targetModel;

            try {
                target = (JList) comp;
                targetModel = (DefaultListModel) target.getModel();
                Object transferData = data.getTransferData(buildQueueFlavor);
                if (transferData instanceof List) {
                    for (Object object : (List) transferData) {
                        if (object instanceof BuildableType) {
                            if ((object instanceof BuildingType
                                 && target == unitList)
                                || (object instanceof UnitType
                                    && target == buildingList)) {
                                return false;
                            }
                            buildQueue.add((BuildableType) object);
                        }
                    }
                }
            } catch (Exception e) {
                logger.warning(e.toString());
                return false;
            }

            for (BuildableType type : buildQueue) {
                if (getMinimumIndex(type) < 0) {
                    return false;
                }
            }

            int preferredIndex = target.getSelectedIndex();

            if (source.equals(target)) {
                if (target == buildQueueList) {
                    
                    if (indices != null &&
                        preferredIndex >= indices[0] - 1 &&
                        preferredIndex <= indices[indices.length - 1]) {
                        indices = null;
                        return true;
                    }
                    numberOfItems = buildQueue.size();
                } else {
                    return false;
                }
            }

            int maxIndex = targetModel.size();
            if (preferredIndex < 0 || preferredIndex > maxIndex) {
                preferredIndex = maxIndex;
            }
            targetIndex = preferredIndex;

            for (int index = 0; index < buildQueue.size(); index++) {
                int minimumIndex = getMinimumIndex(buildQueue.get(index));
                if (minimumIndex < targetIndex + index) {
                    minimumIndex = targetIndex + index;
                }
                targetModel.insertElementAt(buildQueue.get(index), minimumIndex);
            }

            return true;
        }

        
        protected void exportDone(JComponent source, Transferable data, int action) {

            if ((action == MOVE) && (indices != null)) {
                DefaultListModel model = (DefaultListModel) ((JList) source).getModel();

                
                if (numberOfItems > 0) {
                    for (int i = 0; i < indices.length; i++) {
                        if (indices[i] > targetIndex) {
                            indices[i] += numberOfItems;
                        }
                    }
                }
                
                for (int i = indices.length -1; i >= 0; i--) {
                    model.remove(indices[i]);
                }
            }
            
            indices = null;
            targetIndex = -1;
            numberOfItems = 0;
            updateAllLists();
        }

        
        public boolean canImport(JComponent comp, DataFlavor[] flavors) {
            if (flavors == null) {
                return false;
            } else {
                for (DataFlavor flavor : flavors) {
                    if (flavor.equals(buildQueueFlavor)) {
                        return true;
                    }
                }
                return false;
            }
        }

        
        protected Transferable createTransferable(JComponent comp) {
            if (comp instanceof JList) {
                source = (JList) comp;
                indices = source.getSelectedIndices();
                List<BuildableType> buildQueue = getBuildableTypes(source.getSelectedValues());
                return new BuildQueueTransferable(buildQueue);
            } else {
                return null;
            }
        }

        
        public int getSourceActions(JComponent comp) {
            if (comp == unitList) {
                return COPY;
            } else {
                return MOVE;
            }
        }

        
        public class BuildQueueTransferable implements Transferable {
            private List<BuildableType> buildQueue;
            private final DataFlavor[] supportedFlavors = new DataFlavor[] {
                buildQueueFlavor
            };

            
            public BuildQueueTransferable(List<BuildableType> buildQueue) {
                this.buildQueue = buildQueue;
            }

            
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                if (isDataFlavorSupported(flavor)) {
                    return buildQueue;
                } else {
                    throw new UnsupportedFlavorException(flavor);
                }
            }

            
            public List<BuildableType> getBuildQueue() {
                return buildQueue;
            }

            
            public DataFlavor[] getTransferDataFlavors() {
                return supportedFlavors;
            }

            
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                for (DataFlavor myFlavor : supportedFlavors) {
                    if (myFlavor.equals(flavor)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    
    class SelectedPanel extends JPanel {

        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            Composite oldComposite = g2d.getComposite();
            Color oldColor = g2d.getColor();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setComposite(oldComposite);
            g2d.setColor(oldColor);

            super.paintComponent(g);
        }
    }

    class SimpleBuildQueueCellRenderer extends FreeColComboBoxRenderer {

        public void setLabelValues(JLabel c, Object value) {
            c.setText(Messages.message(((BuildableType) value).getNameKey()));
        }

    }

    class DefaultBuildQueueCellRenderer implements ListCellRenderer {

        JPanel itemPanel = new JPanel();
        JPanel selectedPanel = new SelectedPanel();
        JLabel imageLabel = new JLabel(new ImageIcon());
        JLabel nameLabel = new JLabel();

        private JLabel lockLabel =
            new JLabel(new ImageIcon(ResourceManager.getImage("lock.image", 0.5)));

        private Dimension buildingDimension = new Dimension(-1, 48);

        public DefaultBuildQueueCellRenderer() {
            itemPanel.setOpaque(false);
            itemPanel.setLayout(new MigLayout());
            selectedPanel.setOpaque(false);
            selectedPanel.setLayout(new MigLayout());
        }

        public Component getListCellRendererComponent(JList list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {
            BuildableType item = (BuildableType) value;
            JPanel panel = isSelected ? selectedPanel : itemPanel;
            panel.removeAll();

            ((ImageIcon) imageLabel.getIcon()).setImage(ResourceManager.getImage(item.getId() + ".image",
                                                                                 buildingDimension));

            nameLabel.setText(Messages.message(item.getNameKey()));
            panel.setToolTipText(lockReasons.get(item));
            panel.add(imageLabel, "span 1 2");
            if (lockReasons.get(item) == null) {
                panel.add(nameLabel, "wrap");
            } else {
                panel.add(nameLabel, "split 2");
                panel.add(lockLabel, "wrap");
            }

            List<AbstractGoods> goodsRequired = item.getGoodsRequired();
            int size = goodsRequired.size();
            for (int i = 0; i < size; i++) {
                AbstractGoods goods = goodsRequired.get(i);
                ImageIcon icon = new ImageIcon(ResourceManager.getImage(goods.getType().getId() + ".image", 0.66));
                JLabel goodsLabel =
                    new JLabel(Integer.toString(goods.getAmount()), icon, SwingConstants.CENTER);
                if (i == 0 && size > 1) {
                    panel.add(goodsLabel, "split " + size);
                } else {
                    panel.add(goodsLabel);
                }
            }
            return panel;
        }
    }

    class BuildQueueMouseAdapter extends MouseAdapter {

        private boolean add = true;

        public BuildQueueMouseAdapter(boolean add) {
            this.add = add;
        }

        public void mousePressed(MouseEvent e) {
            JList source = (JList) e.getSource();
            if ((e.getButton() == MouseEvent.BUTTON3 || e.isPopupTrigger())) {
                int index = source.locationToIndex(e.getPoint());
                BuildableType type = (BuildableType) source.getModel().getElementAt(index);
                if (type instanceof BuildingType) {
                    getCanvas().showPanel(new ColopediaPanel(getCanvas(),
                                                             ColopediaPanel.PanelType.BUILDINGS, type));
                } else if (type instanceof UnitType) {
                    getCanvas().showPanel(new ColopediaPanel(getCanvas(),
                                                             ColopediaPanel.PanelType.UNITS, type));
                }
            } else if ((e.getClickCount() > 1 && !e.isConsumed())) {
                DefaultListModel model = (DefaultListModel) buildQueueList.getModel();
                if (source.getSelectedIndex() == -1) {
                    source.setSelectedIndex(source.locationToIndex(e.getPoint()));
                }
                for (Object type : source.getSelectedValues()) {
                    if (add) {
                        model.addElement(type);
                    } else {
                        model.removeElement(type);
                    }
                }
                updateAllLists();
            }
        }
    }

}

