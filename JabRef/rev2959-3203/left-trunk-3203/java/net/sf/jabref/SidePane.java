
package net.sf.jabref;

import java.awt.*;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class SidePane extends JPanel {

	final Dimension PREFERRED_SIZE = new Dimension(GUIGlobals.SPLIT_PANE_DIVIDER_LOCATION, 100);

	GridBagLayout gridBagLayout = new GridBagLayout();

	GridBagConstraints constraint = new GridBagConstraints();

	JPanel mainPanel = new JPanel();

	public SidePane() {

		
		

		setLayout(new BorderLayout());
		mainPanel.setLayout(gridBagLayout);

		
		constraint.anchor = GridBagConstraints.NORTH;
		constraint.fill = GridBagConstraints.BOTH;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(1, 1, 1, 1);
		constraint.gridheight = 1;
		constraint.weightx = 1;

		
		JScrollPane sp = new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setBorder(null);
		
		
		
		super.add(sp);
	}

	public void setComponents(Collection<SidePaneComponent> comps) {
		mainPanel.removeAll();

		constraint.weighty = 0;
		for (Component c : comps){
			gridBagLayout.setConstraints(c, constraint);
			mainPanel.add(c);
		}
		constraint.weighty = 1;
		Component bx = Box.createVerticalGlue();
		gridBagLayout.setConstraints(bx, constraint);
		mainPanel.add(bx);

		revalidate();
		repaint();
	}

	public void remove(Component c) {
		mainPanel.remove(c);
	}

	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	public Dimension getPreferredSize() {
		return PREFERRED_SIZE;
	}
}
