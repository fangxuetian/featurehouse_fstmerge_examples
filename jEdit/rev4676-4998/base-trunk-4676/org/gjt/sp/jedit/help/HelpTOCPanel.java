

package org.gjt.sp.jedit.help;


import com.microstar.xml.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.gjt.sp.jedit.browser.FileCellRenderer; 
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.Log;


class HelpTOCPanel extends JPanel
{
	
	public HelpTOCPanel(HelpViewer helpViewer)
	{
		super(new BorderLayout());

		this.helpViewer = helpViewer;
		nodes = new Hashtable();

		createTOC();

		toc = new TOCTree(tocModel);

		
		if(!OperatingSystem.isMacOSLF())
			toc.putClientProperty("JTree.lineStyle", "Angled");

		toc.setCellRenderer(new TOCCellRenderer());
		toc.setEditable(false);
		toc.setRootVisible(false);
		toc.setShowsRootHandles(true);

		for(int i = 0; i <tocRoot.getChildCount(); i++)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)
				tocRoot.getChildAt(i);
			toc.expandPath(new TreePath(node.getPath()));
		}

		add(BorderLayout.CENTER,new JScrollPane(toc));
	} 

	
	public void selectNode(String shortURL)
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)nodes.get(shortURL);

		if(node == null)
			return;

		TreePath path = new TreePath(tocModel.getPathToRoot(node));
		toc.expandPath(path);
		toc.setSelectionPath(path);
		toc.scrollPathToVisible(path);
	} 

	
	private HelpViewer helpViewer;
	private DefaultTreeModel tocModel;
	private DefaultMutableTreeNode tocRoot;
	private JTree toc;
	private Hashtable nodes;

	
	private DefaultMutableTreeNode createNode(String href, String title)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(
			new HelpNode(href,title),true);
		nodes.put(href,node);
		return node;
	} 

	
	private void createTOC()
	{
		tocRoot = new DefaultMutableTreeNode();

		tocRoot.add(createNode("welcome.html",
			jEdit.getProperty("helpviewer.toc.welcome")));

		tocRoot.add(createNode("README.txt",
			jEdit.getProperty("helpviewer.toc.readme")));
		tocRoot.add(createNode("CHANGES.txt",
			jEdit.getProperty("helpviewer.toc.changes")));
		tocRoot.add(createNode("TODO.txt",
			jEdit.getProperty("helpviewer.toc.todo")));
		tocRoot.add(createNode("COPYING.txt",
			jEdit.getProperty("helpviewer.toc.copying")));
		tocRoot.add(createNode("COPYING.DOC.txt",
			jEdit.getProperty("helpviewer.toc.copying-doc")));
		tocRoot.add(createNode("Apache.LICENSE.txt",
			jEdit.getProperty("helpviewer.toc.copying-apache")));

		loadTOC(tocRoot,"news42/toc.xml");
		loadTOC(tocRoot,"users-guide/toc.xml");
		loadTOC(tocRoot,"FAQ/toc.xml");
		loadTOC(tocRoot,"api/toc.xml");

		DefaultMutableTreeNode pluginTree = new DefaultMutableTreeNode(
			jEdit.getProperty("helpviewer.toc.plugins"),true);

		EditPlugin[] plugins = jEdit.getPlugins();
		for(int i = 0; i < plugins.length; i++)
		{
			EditPlugin plugin = plugins[i];
			PluginJAR jar = plugin.getPluginJAR();
			if(jar == null)
				continue;

			String name = plugin.getClassName();

			String docs = jEdit.getProperty("plugin." + name + ".docs");
			String label = jEdit.getProperty("plugin." + name + ".name");
			if(docs != null)
			{
				if(label != null && docs != null)
				{
					URL url = jar.getClassLoader()
						.getResource(docs);
					if(url != null)
					{
						pluginTree.add(createNode(
							url.toString(),label));
					}
				}
			}
		}

		if(pluginTree.getChildCount() != 0)
			tocRoot.add(pluginTree);
		else
		{
			
			pluginTree = null;
		}

		tocModel = new DefaultTreeModel(tocRoot);
	} 

	
	private void loadTOC(DefaultMutableTreeNode root, String path)
	{
		TOCHandler h = new TOCHandler(root,MiscUtilities.getParentOfPath(path));
		XmlParser parser = new XmlParser();
		parser.setHandler(h);

		try
		{
			parser.parse(null, null, new InputStreamReader(
				new URL(helpViewer.getBaseURL()
				+ '/' + path).openStream()));
		}
		catch(XmlException xe)
		{
			int line = xe.getLine();
			String message = xe.getMessage();
			Log.log(Log.ERROR,this,path + ':' + line
				+ ": " + message);
		}
		catch(Exception e)
		{
			Log.log(Log.NOTICE,this,e);
		}
	} 

	

	
	static class HelpNode
	{
		String href, title;

		
		HelpNode(String href, String title)
		{
			this.href = href;
			this.title = title;
		} 

		
		public String toString()
		{
			return title;
		} 
	} 

	
	class TOCHandler extends HandlerBase
	{
		String dir;

		
		TOCHandler(DefaultMutableTreeNode root, String dir)
		{
			nodes = new Stack();
			node = root;
			this.dir = dir;
		} 

		
		public void attribute(String aname, String value, boolean isSpecified)
		{
			if(aname.equals("HREF"))
				href = value;
		} 

		
		public void charData(char[] c, int off, int len)
		{
			if(tag.equals("TITLE"))
			{
				StringBuffer buf = new StringBuffer();
				for(int i = 0; i < len; i++)
				{
					char ch = c[off + i];
					if(ch == ' ' || !Character.isWhitespace(ch))
						buf.append(ch);
				}
				title = buf.toString();
			}
		} 

		
		public void startElement(String name)
		{
			tag = name;
		} 

		
		public void endElement(String name)
		{
			if(name == null)
				return;

			if(name.equals("TITLE"))
			{
				DefaultMutableTreeNode newNode = createNode(
					dir + href,title);
				node.add(newNode);
				nodes.push(node);
				node = newNode;
			}
			else if(name.equals("ENTRY"))
				node = (DefaultMutableTreeNode)nodes.pop();
		} 

		
		private String tag;
		private String title;
		private String href;
		private DefaultMutableTreeNode node;
		private Stack nodes;
		
	} 

	
	class TOCTree extends JTree
	{
		
		TOCTree(TreeModel model)
		{
			super(model);
			ToolTipManager.sharedInstance().registerComponent(this);
		} 

		
		public final String getToolTipText(MouseEvent evt)
		{
			TreePath path = getPathForLocation(evt.getX(), evt.getY());
			if(path != null)
			{
				Rectangle cellRect = getPathBounds(path);
				if(cellRect != null && !cellRectIsVisible(cellRect))
					return path.getLastPathComponent().toString();
			}
			return null;
		} 

		
		 

		
		protected void processMouseEvent(MouseEvent evt)
		{
			

			switch(evt.getID())
			{
			
			case MouseEvent.MOUSE_CLICKED:
				TreePath path = getPathForLocation(evt.getX(),evt.getY());
				if(path != null)
				{
					if(!isPathSelected(path))
						setSelectionPath(path);

					Object obj = ((DefaultMutableTreeNode)
						path.getLastPathComponent())
						.getUserObject();
					if(!(obj instanceof HelpNode))
					{
						this.expandPath(path);
						return;
					}

					HelpNode node = (HelpNode)obj;

					helpViewer.gotoURL(node.href,true);
				}

				super.processMouseEvent(evt);
				break;
			default:
				super.processMouseEvent(evt);
				break;
			}
		} 

		
		private int toolTipInitialDelay = -1;
		private int toolTipReshowDelay = -1;

		
		private boolean cellRectIsVisible(Rectangle cellRect)
		{
			Rectangle vr = TOCTree.this.getVisibleRect();
			return vr.contains(cellRect.x,cellRect.y) &&
				vr.contains(cellRect.x + cellRect.width,
				cellRect.y + cellRect.height);
		} 

		
	} 

	
	class TOCCellRenderer extends DefaultTreeCellRenderer
	{
		EmptyBorder border = new EmptyBorder(1,0,1,1);

		public Component getTreeCellRendererComponent(JTree tree,
			Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean focus)
		{
			super.getTreeCellRendererComponent(tree,value,sel,
				expanded,leaf,row,focus);
			setIcon(leaf ? FileCellRenderer.fileIcon
				: (expanded ? FileCellRenderer.openDirIcon
				: FileCellRenderer.dirIcon));
			setBorder(border);

			return this;
		}
	} 
}
