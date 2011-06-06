

package org.gjt.sp.jedit.browser;


import java.awt.*;
import java.awt.font.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import org.gjt.sp.jedit.io.VFSFile;
import org.gjt.sp.jedit.*;



public class FileCellRenderer extends DefaultTableCellRenderer
{
	public static Icon fileIcon = GUIUtilities.loadIcon("File.png");
	public static Icon openFileIcon = GUIUtilities.loadIcon("OpenFile.png");
	public static Icon dirIcon = GUIUtilities.loadIcon("Folder.png");
	public static Icon openDirIcon = GUIUtilities.loadIcon("OpenFolder.png");
	public static Icon filesystemIcon = GUIUtilities.loadIcon("DriveSmall.png");
	public static Icon loadingIcon = GUIUtilities.loadIcon("ReloadSmall.png");

	
	public FileCellRenderer()
	{
		plainFont = UIManager.getFont("Tree.font");
		if(plainFont == null)
			plainFont = jEdit.getFontProperty("metal.secondary.font");
		boldFont = plainFont.deriveFont(Font.BOLD);
	} 

	
	public Component getTableCellRendererComponent(JTable table,
		Object value, boolean isSelected, boolean hasFocus, 
		int row, int column)
	{
		super.getTableCellRendererComponent(table,value,isSelected,
			hasFocus,row,column);

		if(value instanceof VFSDirectoryEntryTableModel.Entry)
		{
			VFSDirectoryEntryTableModel.Entry entry =
				(VFSDirectoryEntryTableModel.Entry)value;
			VFSFile file = entry.dirEntry;

			setFont(file.getType() == VFSFile.FILE
				? plainFont : boldFont);

			this.isSelected = isSelected;
			this.file = file;

			if(column == 0)
			{
				
				
				
				String path;
				if(file.getSymlinkPath() == null)
					path = file.getPath();
				else
					path = file.getSymlinkPath();
				openBuffer = jEdit._getBuffer(path) != null;

				setIcon(showIcons
					? getIconForFile(file,entry.expanded,
					openBuffer) : null);
				setText(file.getName());

				int state;
				if(file.getType() == VFSFile.FILE)
					state = ExpansionToggleBorder.STATE_NONE;
				else if(entry.expanded)
					state = ExpansionToggleBorder.STATE_EXPANDED;
				else
					state = ExpansionToggleBorder.STATE_COLLAPSED;

				setBorder(new ExpansionToggleBorder(
					state,entry.level));
			}
			else
			{
				VFSDirectoryEntryTableModel model = (VFSDirectoryEntryTableModel)table.getModel();
				String extAttr = model.getExtendedAttribute(column);

				openBuffer = false;
				setIcon(null);
				setText(file.getExtendedAttribute(extAttr));
				setBorder(new EmptyBorder(1,1,1,1));
			}
		}

		return this;
	} 

	
	public void paintComponent(Graphics g)
	{
		if(!isSelected)
		{
			Color color = file.getColor();

			setForeground(color == null
				? UIManager.getColor("Tree.foreground")
				: color);
		}

		super.paintComponent(g);

		if(openBuffer)
		{
			Font font = getFont();

			FontMetrics fm = getFontMetrics(font);
			int x, y;
			if(getIcon() == null)
			{
				x = 0;
				y = fm.getAscent() + 2;
			}
			else
			{
				x = getIcon().getIconWidth() + getIconTextGap();
				y = Math.max(fm.getAscent() + 2,16);
			}

			Insets border = getBorder().getBorderInsets(this);
			x += border.left;

			g.setColor(getForeground());
			g.drawLine(x,y,x + fm.stringWidth(getText()),y);
		}
	} 

	
	
	public static Icon getIconForFile(VFSFile file,
		boolean expanded)
	{
		return getIconForFile(file,expanded,
			jEdit._getBuffer(file.getSymlinkPath()) != null);
	} 

	
	public static Icon getIconForFile(VFSFile file,
		boolean expanded, boolean openBuffer)
	{
		if (defaultIcons)
			return file.getDefaultIcon(expanded, openBuffer);
		return file.getIcon(expanded, openBuffer);
	} 

	
	Font plainFont;
	Font boldFont;
	boolean showIcons;
	private static boolean defaultIcons = true;

	
	void propertiesChanged()
	{
		showIcons = jEdit.getBooleanProperty("vfs.browser.showIcons");
		defaultIcons = jEdit.getBooleanProperty("vfs.browser.useDefaultIcons");
	} 

	
	int getEntryWidth(VFSDirectoryEntryTableModel.Entry entry,
		Font font, FontRenderContext fontRenderContext)
	{
		String name = entry.dirEntry.getName();
		int width = (int)font.getStringBounds(name,fontRenderContext)
			.getWidth();
		width += ExpansionToggleBorder.ICON_WIDTH
			+ entry.level * ExpansionToggleBorder.LEVEL_WIDTH
			+ 3;
		if(showIcons)
		{
			width += fileIcon.getIconWidth();
			width += getIconTextGap();
		}
		return width;
	} 

	

	
	private boolean openBuffer;
	private boolean isSelected;
	private VFSFile file;
	

	
	static class ExpansionToggleBorder implements Border
	{
		static final Icon COLLAPSED_ICON;
		static final Icon EXPANDED_ICON;
		static final int ICON_WIDTH;

		static final int LEVEL_WIDTH = 15;

		static final int STATE_NONE = 0;
		static final int STATE_COLLAPSED = 1;
		static final int STATE_EXPANDED = 2;

		
		ExpansionToggleBorder(int state, int level)
		{
			this.state = state;
			this.level = level;
		} 

		
		public void paintBorder(Component c, Graphics g,
			int x, int y, int width, int height)
		{
			switch(state)
			{
			case STATE_COLLAPSED:
				COLLAPSED_ICON.paintIcon(c,g,
					x + level * LEVEL_WIDTH + 2,
					y + (height - COLLAPSED_ICON.getIconHeight()) / 2);
				break;
			case STATE_EXPANDED:
				EXPANDED_ICON.paintIcon(c,g,
					x + level * LEVEL_WIDTH + 2,
					y + 2 + (height - EXPANDED_ICON.getIconHeight()) / 2);
				break;
			}
		} 

		
		public Insets getBorderInsets(Component c)
		{
			return new Insets(1,level * LEVEL_WIDTH
				+ ICON_WIDTH + 4,1,1);
		} 

		
		public boolean isBorderOpaque()
		{
			return false;
		} 

		
		public static boolean isExpansionToggle(int level, int x)
		{
			return (x >= level * LEVEL_WIDTH)
				&& (x <= level * LEVEL_WIDTH + ICON_WIDTH);
		} 

		
		private int state;
		private int level;

		static
		{
			COLLAPSED_ICON = GUIUtilities.loadIcon("arrow1.png");
			EXPANDED_ICON = GUIUtilities.loadIcon("arrow2.png");
			ICON_WIDTH = Math.max(COLLAPSED_ICON.getIconWidth(),
				EXPANDED_ICON.getIconWidth());
		} 
	} 
}
