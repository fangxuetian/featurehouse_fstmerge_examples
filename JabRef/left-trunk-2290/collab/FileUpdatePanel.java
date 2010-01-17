package net.sf.jabref.collab;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.jabref.*;

public class FileUpdatePanel extends SidePaneComponent implements ActionListener {

    public static final String NAME = "fileUpdate";

	JButton test = new JButton(Globals.lang("Review changes"));

	BasePanel panel;

	JabRefFrame frame;

	SidePaneManager manager;

	JLabel message;

	ChangeScanner scanner;

	public FileUpdatePanel(JabRefFrame frame, BasePanel panel, SidePaneManager manager, File file,
		ChangeScanner scanner) {
		super(manager, GUIGlobals.getIconUrl("save"), Globals.lang("File changed"));
		this.panel = panel;
		this.frame = frame;
		this.manager = manager;
		this.scanner = scanner;

		JPanel main = new JPanel();
		main.setLayout(new BorderLayout());

		message = new JLabel("<html><center>"
			+ Globals.lang("The file<BR>'%0'<BR>has been modified<BR>externally!", file.getName())
			+ "</center></html>", JLabel.CENTER);

		main.add(message, BorderLayout.CENTER);
		main.add(test, BorderLayout.SOUTH);
		main.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

		add(main, BorderLayout.CENTER);
		test.addActionListener(this);
	}

	/**
	 * Unregister when this component closes. We need that to avoid showing
	 * two such external change warnings at the same time, only the latest one.
	 */
	public void componentClosing() {
	    manager.unregisterComponent(NAME);
	}

	/**
	 * actionPerformed
	 * 
	 * @param e
	 *            ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {
		manager.hideComponent(this);
		// ChangeScanner scanner = new ChangeScanner(frame, panel); //,
		// panel.database(), panel.metaData());
		// try {
		scanner.displayResult();
		// scanner.changeScan(panel.file());
		panel.setUpdatedExternally(false);
		// } catch (IOException ex) {
		// ex.printStackTrace();
		// }
	}
}
