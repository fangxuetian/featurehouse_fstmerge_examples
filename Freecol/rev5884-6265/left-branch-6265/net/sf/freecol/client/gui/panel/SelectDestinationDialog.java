

package net.sf.freecol.client.gui.panel;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.freecol.client.gui.Canvas;
import net.sf.freecol.client.gui.i18n.Messages;
import net.sf.freecol.client.gui.plaf.FreeColComboBoxRenderer;
import net.sf.freecol.common.model.Colony;
import net.sf.freecol.common.model.Europe;
import net.sf.freecol.common.model.IndianSettlement;
import net.sf.freecol.common.model.Location;
import net.sf.freecol.common.model.PathNode;
import net.sf.freecol.common.model.Player;
import net.sf.freecol.common.model.Settlement;
import net.sf.freecol.common.model.Unit;
import net.sf.freecol.common.model.pathfinding.GoalDecider;

import net.miginfocom.swing.MigLayout;



public final class SelectDestinationDialog extends FreeColDialog<Location> 
    implements ActionListener, ChangeListener, ItemListener {

    private static final Logger logger = Logger.getLogger(SelectDestinationDialog.class.getName());

    private static boolean showOnlyMyColonies = true;

    private static Comparator<Destination> destinationComparator = null;

    private final JCheckBox onlyMyColoniesBox;

    private final JComboBox comparatorBox;

    private final JList destinationList;

    private final List<Destination> destinations = new ArrayList<Destination>();


    
    public SelectDestinationDialog(Canvas parent, Unit unit) {
        super(parent);

        final Settlement inSettlement = (unit.getTile() != null) ? unit.getTile().getSettlement() : null;

        
        getGame().getMap().search(unit, new GoalDecider() {
                public PathNode getGoal() {
                    return null;
                }

                public boolean check(Unit u, PathNode p) {
                    Settlement settlement = p.getTile().getSettlement();
                    if (settlement != null && settlement != inSettlement) {
                        destinations.add(new Destination(settlement, p.getTurns()));
                    }
                    return false;
                }

                public boolean hasSubGoals() {
                    return false;
                }
            }, Integer.MAX_VALUE);


        if (destinationComparator == null) {
            destinationComparator = new DestinationComparator(getMyPlayer());
        }
        Collections.sort(destinations, destinationComparator);

        if (unit.isNaval() && unit.getOwner().canMoveToEurope()) {
            PathNode path = getGame().getMap().findPathToEurope(unit, unit.getTile());
            if (path != null) {
                destinations.add(0, new Destination(getMyPlayer().getEurope(),
                                                    path.getTotalTurns()));
            } else if (unit.getTile() != null
                       && (unit.getTile().canMoveToEurope()
                           || getGame().getMap().isAdjacentToMapEdge(unit.getTile()))) {
                destinations.add(0, new Destination(getMyPlayer().getEurope(), 0));
            }
        }

        MigLayout layout = new MigLayout("wrap 1, fill", "[align center]", "");
        setLayout(layout);

        JLabel header = new JLabel(Messages.message("selectDestination.text"));
        header.setFont(smallHeaderFont);
        add(header);

        DefaultListModel model = new DefaultListModel();
        destinationList = new JList(model);
        filterDestinations();

        destinationList.setCellRenderer(new LocationRenderer());
        destinationList.setFixedCellHeight(48);

        Action selectAction = new AbstractAction(Messages.message("ok")) {
                public void actionPerformed(ActionEvent e) {
                    Destination d = (Destination) destinationList.getSelectedValue();
                    if (d != null) {
                        setResponse((Location) d.location);
                    }
                    getCanvas().remove(SelectDestinationDialog.this);
                }
            };

        Action quitAction = new AbstractAction(Messages.message("selectDestination.cancel")) {
                public void actionPerformed(ActionEvent e) {
                    getCanvas().remove(SelectDestinationDialog.this);
                }
            };

        destinationList.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "select");
        destinationList.getActionMap().put("select", selectAction);
        destinationList.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "quit");
        destinationList.getActionMap().put("quit", quitAction);

        MouseListener mouseListener = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        Destination d = (Destination) destinationList.getSelectedValue();
                        if (d != null) {
                            setResponse((Location) d.location);
                        }
                        getCanvas().remove(SelectDestinationDialog.this);
                    }
                }
            };
        destinationList.addMouseListener(mouseListener);

        JScrollPane listScroller = new JScrollPane(destinationList);
        listScroller.setPreferredSize(new Dimension(250, 250));

        add(listScroller, "newline 30, growx, growy");

        onlyMyColoniesBox = new JCheckBox(Messages.message("selectDestination.onlyMyColonies"),
                                          showOnlyMyColonies);
        onlyMyColoniesBox.addChangeListener(this);
        add(onlyMyColoniesBox, "left");

        comparatorBox = new JComboBox(new String[] {
                Messages.message("selectDestination.sortByOwner"),
                Messages.message("selectDestination.sortByName"),
                Messages.message("selectDestination.sortByDistance")
            });
        comparatorBox.addItemListener(this);
        if (destinationComparator instanceof DestinationComparator) {
            comparatorBox.setSelectedIndex(0);
        } else if (destinationComparator instanceof NameComparator) {
            comparatorBox.setSelectedIndex(1);
        } else if (destinationComparator instanceof DistanceComparator) {
            comparatorBox.setSelectedIndex(2);
        }
        add(comparatorBox, "left");

        cancelButton.setAction(quitAction);
        okButton.setAction(selectAction);

        add(okButton, "newline 30, split 2, tag ok");
        add(cancelButton, "tag cancel");

        setSize(getPreferredSize());
    }

    @Override
    public void requestFocus() {
        destinationList.requestFocus();
    }

    public void stateChanged(ChangeEvent event) {
        showOnlyMyColonies = onlyMyColoniesBox.isSelected();
        filterDestinations();
    }

    public void itemStateChanged(ItemEvent event) {
        switch(comparatorBox.getSelectedIndex()) {
        case 0:
        default:
            destinationComparator = new DestinationComparator(getMyPlayer());
            break;
        case 1:
            destinationComparator = new NameComparator();
            break;
        case 2:
            destinationComparator = new DistanceComparator();
            break;
        }
        Collections.sort(destinations, destinationComparator);
        filterDestinations();
    }

    private void filterDestinations() {
        DefaultListModel model = (DefaultListModel) destinationList.getModel();
        Object selected = destinationList.getSelectedValue();
        model.clear();
        for (Destination d : destinations) {
            if (showOnlyMyColonies) {
                if (d.location instanceof Europe
                    || (d.location instanceof Colony
                        && ((Colony) d.location).getOwner() == getMyPlayer())) {
                    model.addElement(d);
                }
            } else {
                model.addElement(d);
            }
        }
        destinationList.setSelectedValue(selected, true);
        if (destinationList.getSelectedIndex() == -1) {
            destinationList.setSelectedIndex(0);
        }
    }

    public int compareNames(Location dest1, Location dest2) {
        String name1 = "";
        if (dest1 instanceof Settlement) {
            name1 = ((Settlement) dest1).getName();
        } else if (dest1 instanceof Europe) {
            return -1;
        }
        String name2 = "";
        if (dest2 instanceof Settlement) {
            name2 = ((Settlement) dest2).getName();
        } else if (dest2 instanceof Europe) {
            return 1;
        }
        return name1.compareTo(name2);
    }

    private class Destination {
        public Location location;
        public int turns;

        public Destination(Location location, int turns) {
            this.location = location;
            this.turns = turns;
        }
    }

    private class LocationRenderer extends FreeColComboBoxRenderer {

        @Override
        public void setLabelValues(JLabel label, Object value) {

            Destination d = (Destination) value;
            Location location = d.location;
            String name = "";
            if (location instanceof Europe) {
                Europe europe = (Europe) location;
                name = europe.getName();
                label.setIcon(new ImageIcon(getLibrary().getCoatOfArmsImage(europe.getOwner().getNation())
                                            .getScaledInstance(-1, 48, Image.SCALE_SMOOTH)));
            } else if (location instanceof Settlement) {
                Settlement settlement = (Settlement) location;
                name = settlement.getName();
                label.setIcon(new ImageIcon(getLibrary().getSettlementImage(settlement)
                                            .getScaledInstance(64, -1, Image.SCALE_SMOOTH)));
            }
            label.setText(Messages.message("selectDestination.destinationTurns",
                                           "%location%", name,
                                           "%turns%", String.valueOf(d.turns)));
        }
    }

    private class DestinationComparator implements Comparator<Destination> {

        private Player owner;

        public DestinationComparator(Player player) {
            this.owner = player;
        }

        public int compare(Destination choice1, Destination choice2) {
            Location dest1 = choice1.location;
            Location dest2 = choice2.location;

            int score1 = 100;
            if (dest1 instanceof Europe) {
                score1 = 10;
            } else if (dest1 instanceof Colony) {
                if (((Colony) dest1).getOwner() == owner) {
                    score1 = 20;
                } else {
                    score1 = 30;
                }
            } else if (dest1 instanceof IndianSettlement) {
                score1 = 40;
            }
            int score2 = 100;
            if (dest2 instanceof Europe) {
                score2 = 10;
            } else if (dest2 instanceof Colony) {
                if (((Colony) dest2).getOwner() == owner) {
                    score2 = 20;
                } else {
                    score2 = 30;
                }
            } else if (dest2 instanceof IndianSettlement) {
                score2 = 40;
            }

            if (score1 == score2) {
                return compareNames(dest1, dest2);
            } else {
                return score1 - score2;
            }
        }
    }

    private class NameComparator implements Comparator<Destination> {
        public int compare(Destination choice1, Destination choice2) {
            return compareNames(choice1.location, choice2.location);
        }
    }

    private class DistanceComparator implements Comparator<Destination> {
        public int compare(Destination choice1, Destination choice2) {
            int result = choice1.turns - choice2.turns;
            if (result == 0) {
                return compareNames(choice1.location, choice2.location);
            } else {
                return result;
            }
        }
    }

}