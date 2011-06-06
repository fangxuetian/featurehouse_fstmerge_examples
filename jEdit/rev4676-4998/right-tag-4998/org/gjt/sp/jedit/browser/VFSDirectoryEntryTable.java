

package org.gjt.sp.jedit.browser;


import javax.swing.table.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.gjt.sp.jedit.io.VFS;
import org.gjt.sp.jedit.MiscUtilities;
import org.gjt.sp.util.Log;



public class VFSDirectoryEntryTable extends JTable
{
	
	public VFSDirectoryEntryTable(BrowserView browserView)
	{
		super(new VFSDirectoryEntryTableModel());
		this.browserView = browserView;
		setShowGrid(false);

		setIntercellSpacing(new Dimension(0,0));

		

		setDefaultRenderer(VFSDirectoryEntryTableModel.Entry.class,
			renderer = new FileCellRenderer());

		JTableHeader header = getTableHeader();
		header.setReorderingAllowed(false);

		setRowSelectionAllowed(true);
		
		

		setAutoResizeMode(AUTO_RESIZE_OFF);
	} 

	
	public void doTypeSelect(String str, boolean ignoreCase)
	{
		if(str.length() == 0)
			clearSelection();
		else if(getSelectedRow() == -1)
			doTypeSelect(str,0,getRowCount(),ignoreCase);
		else
		{
			int start = getSelectionModel().getMaxSelectionIndex();
			boolean retVal = doTypeSelect(str,start,getRowCount(),
				ignoreCase);

			if(!retVal)
			{
				
				
				doTypeSelect(str,0,start,ignoreCase);
			}
		}
	} 

	
	public VFS.DirectoryEntry[] getSelectedFiles()
	{
		VFSDirectoryEntryTableModel model
			= (VFSDirectoryEntryTableModel)getModel();

		LinkedList returnValue = new LinkedList();
		int[] selectedRows = getSelectedRows();
		for(int i = 0; i < selectedRows.length; i++)
		{
			returnValue.add(model.files[selectedRows[i]].dirEntry);
		}
		return (VFS.DirectoryEntry[])returnValue.toArray(new
		VFS.DirectoryEntry[returnValue.size()]);
	} 

	
	public void getExpandedDirectories(Set set)
	{
		VFSDirectoryEntryTableModel model
			= (VFSDirectoryEntryTableModel)getModel();

		if(model.files != null)
		{
			for(int i = 0; i < model.files.length; i++)
			{
				if(model.files[i].expanded)
					set.add(model.files[i].dirEntry.path);
			}
		}
	} 

	
	public void toggleExpanded(int row)
	{
		VFSDirectoryEntryTableModel model
		= (VFSDirectoryEntryTableModel)getModel();

		VFSDirectoryEntryTableModel.Entry entry = model.files[row];
		if(entry.dirEntry.type == VFS.DirectoryEntry.FILE)
			return;

		if(entry.expanded)
			model.collapse(row);
		else
			browserView.loadDirectory(entry,entry.dirEntry.path);
	} 

	
	public void setDirectory(Object node, ArrayList list, Set tmpExpanded)
	{
		timer.stop();
		typeSelectBuffer.setLength(0);

		VFSDirectoryEntryTableModel model = ((VFSDirectoryEntryTableModel)getModel());
		int startIndex;
		if(node == null)
		{
			startIndex = 0;
			model.setRoot(list);
		}
		else
		{
			startIndex =
				model.expand(
				(VFSDirectoryEntryTableModel.Entry)node,
				list);
			startIndex++;
		}

		for(int i = 0; i < list.size(); i++)
		{
			VFSDirectoryEntryTableModel.Entry e
				= model.files[startIndex + i];
			String path = e.dirEntry.path;
			if(tmpExpanded.contains(path))
			{
				browserView.loadDirectory(e,path);
				tmpExpanded.remove(path);
			}
		}

		resizeColumnsAppropriately();
	} 

	
	public void maybeReloadDirectory(String path)
	{
		VFSDirectoryEntryTableModel model
		= (VFSDirectoryEntryTableModel)getModel();

		for(int i = 0; i < model.files.length; i++)
		{
			VFSDirectoryEntryTableModel.Entry e = model.files[i];
			if(!e.expanded || e.dirEntry.type == VFS.DirectoryEntry.FILE)
				continue;

			if(path.equals(e.dirEntry.path))
				browserView.loadDirectory(e,path);
		}
	} 

	
	public void propertiesChanged()
	{
		renderer.propertiesChanged();

		VFS.DirectoryEntry template = new VFS.DirectoryEntry(
			"foo","foo","foo",VFS.DirectoryEntry.FILE,0L,false);
		setRowHeight(renderer.getTableCellRendererComponent(
			this,new VFSDirectoryEntryTableModel.Entry(template,0),
			false,false,0,1).getPreferredSize().height);
		Dimension prefSize = getPreferredSize();
		setPreferredScrollableViewportSize(new Dimension(prefSize.width,
			getRowHeight() * 12));
	} 

	
	public void scrollRectToVisible(Rectangle rect)
	{
		
		rect.width = 0;
		super.scrollRectToVisible(rect);
	} 

	
	public void processKeyEvent(KeyEvent evt)
	{
		if(evt.getID() == KeyEvent.KEY_PRESSED)
		{
			VFSDirectoryEntryTableModel model =
				(VFSDirectoryEntryTableModel)getModel();
			int row = getSelectedRow();

			switch(evt.getKeyCode())
			{
			case KeyEvent.VK_LEFT:
				evt.consume();
				if(row != -1)
				{
					if(model.files[row].expanded)
					{
						model.collapse(row);
						break;
					}

					for(int i = row - 1; i >= 0; i--)
					{
						if(model.files[i].expanded)
						{
							setSelectedRow(i);
							break;
						}
					}
				}

				String dir = browserView.getBrowser()
					.getDirectory();
				dir = MiscUtilities.getParentOfPath(dir);
				browserView.getBrowser().setDirectory(dir);
				break;
			case KeyEvent.VK_RIGHT:
				if(row != -1)
				{
					if(!model.files[row].expanded)
						toggleExpanded(row);
				}
				evt.consume();
				break;
			case KeyEvent.VK_DOWN:
				
				if(row == -1 && getModel().getRowCount() != 0)
				{
					setSelectedRow(0);
					evt.consume();
				}
				break;
			case KeyEvent.VK_ENTER:
				browserView.getBrowser().filesActivated(
					(evt.isShiftDown()
					? VFSBrowser.M_OPEN_NEW_VIEW
					: VFSBrowser.M_OPEN),false);
				evt.consume();
				break;
			}
		}
		else if(evt.getID() == KeyEvent.KEY_TYPED)
		{
			if(evt.isControlDown() || evt.isAltDown()
				|| evt.isMetaDown())
			{
				return;
			}

			
			if(evt.isShiftDown() && evt.getKeyChar() == '\n')
				return;

			VFSBrowser browser = browserView.getBrowser();

			switch(evt.getKeyChar())
			{
			case '~':
				if(browser.getMode() == VFSBrowser.BROWSER)
					browser.setDirectory(System.getProperty(
						"user.home"));
				break;
			case '/':
				if(browser.getMode() == VFSBrowser.BROWSER)
					browser.rootDirectory();
				break;
			case '-':
				if(browser.getMode() == VFSBrowser.BROWSER)
				{
					browser.setDirectory(
						browser.getView().getBuffer()
						.getDirectory());
				}
				break;
			default:
				typeSelectBuffer.append(evt.getKeyChar());
				doTypeSelect(typeSelectBuffer.toString(),true);

				timer.stop();
				timer.setInitialDelay(750);
				timer.setRepeats(false);
				timer.start();
				return;
			}
		}

		if(!evt.isConsumed())
			super.processKeyEvent(evt);
	} 

	
	public void setSelectedRow(int row)
	{
		getSelectionModel().setSelectionInterval(row,row);
		scrollRectToVisible(getCellRect(row,0,true));
	} 

	
	private BrowserView browserView;
	private FileCellRenderer renderer;
	private StringBuffer typeSelectBuffer = new StringBuffer();
	private Timer timer = new Timer(0,new ClearTypeSelect());

	
	private boolean doTypeSelect(String str, int start, int end,
		boolean ignoreCase)
	{
		for(int i = start; i < end; i++)
		{
			VFSDirectoryEntryTableModel.Entry entry =
				(VFSDirectoryEntryTableModel.Entry)getValueAt(i,1);
			String matchAgainst = (MiscUtilities.isAbsolutePath(str)
				? entry.dirEntry.path : entry.dirEntry.name);
			if(matchAgainst.regionMatches(ignoreCase,
				0,str,0,str.length()))
			{
				setSelectedRow(i);
				return true;
			}
		}

		return false;
	} 

	
	private void resizeColumnsAppropriately()
	{
		VFSDirectoryEntryTableModel model
		= (VFSDirectoryEntryTableModel)getModel();

		FontMetrics fm = getFontMetrics(getFont());
		int[] widths = new int[model.getColumnCount()];
		for(int i = 0; i < widths.length; i++)
		{
			String columnName = model.getColumnName(i);
			if(columnName != null)
				widths[i] = fm.stringWidth(columnName);
		}

		for(int i = 0; i < model.files.length; i++)
		{
			for(int j = 0; j < widths.length; j++)
			{
				int width = renderer.getTableCellRendererComponent(
					this,model.files[i],false,false,i,j)
					.getPreferredSize().width;
				widths[j] = Math.max(widths[j],width);
			}
		}

		for(int i = 0; i < widths.length; i++)
		{
			int width = widths[i];
			if(i != 0 && i != widths.length - 1 && width != 0)
				width += 10;
			getColumnModel().getColumn(i).setPreferredWidth(width);
		}

		doLayout();
	} 

	

	
	class ClearTypeSelect implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			typeSelectBuffer.setLength(0);
		}
	} 
}
