

package org.gjt.sp.jedit.gui;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.msg.RegisterChanged;
import org.gjt.sp.jedit.msg.PropertiesChanged;


public class RegisterViewer extends JPanel implements EBComponent, ActionListener
{
	
	public RegisterViewer(View view, String position)
	{
		super(new BorderLayout());
		this.view = view;
		Box toolBar = new Box(BoxLayout.X_AXIS);
		JLabel label = new JLabel(
			jEdit.getProperty("view-registers.title"));
		label.setBorder(new EmptyBorder(0,0,3,0));
		toolBar.add(label);
		
		toolBar.add(Box.createGlue());

		RolloverButton pasteRegister = new RolloverButton(
			GUIUtilities.loadIcon("Paste.png"));
		pasteRegister.setToolTipText(GUIUtilities.prettifyMenuLabel(
			jEdit.getProperty("paste-string-register.label")));
		pasteRegister.addActionListener(this);
		pasteRegister.setActionCommand("paste-string-register");
		toolBar.add(pasteRegister);

		RolloverButton clearRegister = new RolloverButton(
			GUIUtilities.loadIcon("Clear.png"));
		clearRegister.setToolTipText(GUIUtilities.prettifyMenuLabel(
			jEdit.getProperty("clear-string-register.label")));
		clearRegister.addActionListener(this);
		clearRegister.setActionCommand("clear-string-register");
		toolBar.add(clearRegister);

		
		add(BorderLayout.NORTH,toolBar);

		DefaultListModel registerModel = new DefaultListModel();
		registerList = new JList(registerModel);
		registerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		registerList.setCellRenderer(new Renderer());
		registerList.addListSelectionListener(new ListHandler());
		registerList.addMouseListener(new MouseHandler());

		contentTextArea = new JTextArea(10,20);
		contentTextArea.setEditable(true);
		documentHandler = new DocumentHandler();
		
		contentTextArea.addFocusListener(new FocusHandler());

		int orientation = JSplitPane.HORIZONTAL_SPLIT;
		if (position.equals(DockableWindowManager.LEFT) ||
			position.equals(DockableWindowManager.RIGHT))
			orientation = JSplitPane.VERTICAL_SPLIT;

		add(BorderLayout.CENTER,splitPane = new JSplitPane(orientation,
			jEdit.getBooleanProperty("appearance.continuousLayout"),
			new JScrollPane(registerList),
			new JScrollPane(contentTextArea)));

		refreshList();
	} 
	
	
	public void actionPerformed(ActionEvent evt)
	{
		String cmd = evt.getActionCommand();
		if (cmd.equals("paste-string-register"))
			insertRegister();
		else if (cmd.equals("clear-string-register"))
		{
			Object o = registerList.getSelectedValue();
			if (o == null)
				return;
			else if (o instanceof Character)
			{
				Registers.clearRegister(((Character)o).charValue());
				refreshList();
			}
		}
	} 
	
	
	public void handleMessage(EBMessage msg)
	{
		if (msg instanceof RegisterChanged)
		{
			if (((RegisterChanged)msg).getRegisterName() != '%')
				refreshList();
		}
		else if (msg instanceof PropertiesChanged)
		{
			GUIUtilities.initContinuousLayout(splitPane);
		}

	}

	
	public void addNotify()
	{
		super.addNotify();
		EditBus.addToBus(this);
	} 

	
	public void removeNotify()
	{
		super.removeNotify();
		EditBus.removeFromBus(this);
	} 

	

	
	private JList registerList;
	private JTextArea contentTextArea;
	private DocumentHandler documentHandler;
	private View view;
	private boolean editing;
	private JSplitPane splitPane;
	

	
	private void refreshList()
	{
		DefaultListModel registerModel = (DefaultListModel)registerList.getModel();
		Object o = registerList.getSelectedValue();
		int selected = -1;
		if (o != null && o instanceof Character)
			selected = ((Character)o).charValue();

		registerModel.removeAllElements();
		Registers.Register[] registers = Registers.getRegisters();

		int index = 0;
		for(int i = 0; i < registers.length; i++)
		{
			Registers.Register reg = registers[i];
			if(reg == null)
				continue;
			if (i == '%')
				continue;

			String value = reg.toString();
			if(value == null) 
				continue;
			if (i == selected)
				index = registerModel.size();
			registerModel.addElement(new Character((char)i));
		}

		if(registerModel.getSize() == 0)
		{
			registerModel.addElement(jEdit.getProperty("view-registers.none"));
			registerList.setEnabled(false);
		}
		else
			registerList.setEnabled(true);
		registerList.setSelectedIndex(index);
	} 

	
	private void updateSelected()
	{
		Object o = registerList.getSelectedValue();
		if (o == null)
			return;
		else if (o instanceof Character)
		{
			Registers.Register reg = Registers.getRegister(((Character)o).charValue());
			if (!editing)
			{
				contentTextArea.setText(reg.toString());
				contentTextArea.setEditable(true);
			}
		}
		else
		{
			if (!editing)
			{
				contentTextArea.setText("");
				contentTextArea.setEditable(false);
			}
		}
		if (!editing)
			contentTextArea.setCaretPosition(0);
	}

	
	private void insertRegister()
	{
		Object o = registerList.getSelectedValue();
		if (o == null || !(o instanceof Character))
			return;
		Registers.Register reg = Registers.getRegister(((Character)o).charValue());
		view.getTextArea().setSelectedText(reg.toString());
		view.getTextArea().requestFocus();
	} 

	

	

	
	static class Renderer extends DefaultListCellRenderer
	{
		public Component getListCellRendererComponent(
			JList list, Object value, int index,
			boolean isSelected, boolean cellHasFocus)
		{
			super.getListCellRendererComponent(list,value,
			index,isSelected,cellHasFocus);

			if(value instanceof Character)
			{
				char name = ((Character)value).charValue();

				String label;

				if(name == '\n')
					label = "\n";
				else if(name == '\t')
					label = "\t";
				else if(name == '$')
					label = jEdit.getProperty("view-registers.clipboard");
				else if(name == '%')
					label = jEdit.getProperty("view-registers.selection");
				else
					label = String.valueOf(name);
				String registerValue = Registers.getRegister(name).toString();
				if (registerValue.length() > 100)
					registerValue = registerValue.substring(0,100)+"...";
				registerValue = registerValue.replaceAll("\n"," ");
				registerValue = registerValue.replaceAll("\t"," ");
				setText(label+" : "+registerValue);
			}

			return this;
		}
	} 

	
	class ListHandler implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent evt)
		{
			Object value = registerList.getSelectedValue();
			if(!(value instanceof Character))
				return;

			char name = ((Character)value).charValue();

			Registers.Register reg = Registers.getRegister(name);
			if(reg == null)
				return;
			updateSelected();
		}
	} 

	
	class MouseHandler extends MouseAdapter
	{
		

		public void mouseClicked(MouseEvent evt)
		{
			if (evt.getClickCount() % 2 == 0)
				insertRegister();
		}
	} 

	
	class DocumentHandler implements DocumentListener
	{
		public void changedUpdate(DocumentEvent e)
		{
			updateRegisterSafely();
		}

		public void insertUpdate(DocumentEvent e)
		{
			updateRegisterSafely();
		}

		public void removeUpdate(DocumentEvent e)
		{
			updateRegisterSafely();
		}

		private void updateRegisterSafely()
		{
			try
			{
				editing = true;
				updateRegister();
			}
			finally
			{
				editing = false;
			}
		}
		
		private void updateRegister()
		{
			Object value = registerList.getSelectedValue();
			if(!(value instanceof Character))
				return;
			char name = ((Character)value).charValue();
			Registers.setRegister(name,contentTextArea.getText());
		}
	} 

	
	class FocusHandler implements FocusListener
	{
		public void focusGained(FocusEvent e)
		{
			contentTextArea.getDocument().addDocumentListener(documentHandler);
		}
		public void focusLost(FocusEvent e)
		{
			contentTextArea.getDocument().removeDocumentListener(documentHandler);
		}
	}

	
}
