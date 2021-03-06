
package genj.common;

import genj.gedcom.Context;
import genj.gedcom.Entity;
import genj.gedcom.Gedcom;
import genj.gedcom.Property;
import genj.gedcom.PropertyDate;
import genj.gedcom.PropertyName;
import genj.gedcom.TagPath;
import genj.io.BasicTransferable;
import genj.util.WordBuffer;
import genj.util.swing.Action2;
import genj.util.swing.HeadlessLabel;
import genj.util.swing.LinkWidget;
import genj.util.swing.SortableTableModel;
import genj.util.swing.SortableTableModel.Directive;
import genj.view.ContextProvider;
import genj.view.SelectionSink;
import genj.view.ViewContext;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;


public class PropertyTableWidget extends JPanel  {
  
  private final static Logger LOG = Logger.getLogger("genj.common");
  
  private JPanel panelShortcuts;
  private Table table;
  private boolean ignoreSelection  = false;
  private int visibleRowCount = -1;
  private TransferHandler transferer;
  
  
  public PropertyTableWidget() {
    this(null);
  }
  
  
  public PropertyTableWidget(PropertyTableModel propertyModel) {
    
    
    panelShortcuts = new JPanel();
    panelShortcuts.setMinimumSize(new Dimension());
    panelShortcuts.setLayout(new BoxLayout(panelShortcuts, BoxLayout.Y_AXIS));
    
    
    table = new Table();
    setModel(propertyModel);
    setRowSelection(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    setColSelection(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    
    setLayout(new BorderLayout());
    add(BorderLayout.CENTER, new JScrollPane(table));
    add(BorderLayout.EAST, panelShortcuts);
    
    
  }
 
  
  public void setColSelection(int set) {
    table.setColSelection(set);
  }
  
  
  public void setRowSelection(int set) {
    table.setRowSelection(set);
  }
  
  
  public TableModel getTableModel() {
    return table.getModel();
  }
  
  
  public void setModel(PropertyTableModel set) {
    table.setPropertyTableModel(set);
  }
  
  public void setVisibleRowCount(int rows) {
    visibleRowCount   = rows;
    revalidate();
    repaint();
  }
  
  
  public PropertyTableModel getModel() {
    return table.getPropertyTableModel();
  }
  
  
  public void setAutoResize(boolean on) {
    table.setAutoResizeMode(on ? JTable.AUTO_RESIZE_ALL_COLUMNS : JTable.AUTO_RESIZE_OFF);
  }
  
  public int[] getSelectedRows() {
    return table.getSelectedRows();
  }
  
  public Property getSelectedRow() {
    int i = table.getSelectedRow();
    return i<0 ? null : table.getRowRoot(i);
  }
  
  
  public void select(Context context) {
    
    if (ignoreSelection)
      return;
    
    if (context.getGedcom()!=getModel().getGedcom())
      throw new IllegalArgumentException("select on wrong gedcom");
    
    
    try {
      ignoreSelection = true;
      
      
      List<? extends Property> props = context.getProperties();
      
      
      if (props.isEmpty()) {
        List<Property> ps = new ArrayList<Property>(context.getProperties());
        for (Entity ent : context.getEntities())
          if (!ps.contains(ent))
            ps.add(ent);
        props = ps;
      }
        









      
      ListSelectionModel rows = table.getSelectionModel();
      ListSelectionModel cols = table.getColumnModel().getSelectionModel();
      table.clearSelection();
      
      Point cell = new Point();
      for (Property prop : props) {
  
        cell = table.getCell(prop);
        if (cell.y<0)
          continue;

        
        rows.addSelectionInterval(cell.y,cell.y);
        if (cell.x>=0)
          cols.addSelectionInterval(cell.x,cell.x);
      }
      
      
      if (cell.y>=0) {
        Rectangle visible = table.getVisibleRect();
        Rectangle scrollto = table.getCellRect(cell.y,cell.x,true);
        if (cell.x<0) scrollto.x = visible.x;
        table.scrollRectToVisible(scrollto);
      }

    } finally {
      ignoreSelection = false;
    }
    
  }

  
  public void addListSelectionListener(ListSelectionListener listener) {
    table.getSelectionModel().addListSelectionListener(listener);
  }
  
  
  public void removeListSelectionListener(ListSelectionListener listener) {
    table.getSelectionModel().removeListSelectionListener(listener);
  }
  
  
  public String getColumnLayout() {
    
    
    
    
    SortableTableModel model = (SortableTableModel)table.getModel();
    TableColumnModel columns = table.getColumnModel();
    List<Directive> directives = model.getDirectives();

    WordBuffer result = new WordBuffer(",");
    result.append(columns.getColumnCount());
    
    for (int c=0; c<columns.getColumnCount(); c++) 
      result.append(columns.getColumn(c).getWidth());
    
    for (int d=0;d<directives.size();d++) {
      SortableTableModel.Directive dir = (SortableTableModel.Directive)directives.get(d);
      result.append(dir.getColumn());
      result.append(dir.getDirection());
    }
    
    return result.toString();
  }
  
  
  public void setColumnLayout(String layout) {
    
    SortableTableModel model = (SortableTableModel)table.getModel();
    TableColumnModel columns = table.getColumnModel();

    try {
      StringTokenizer tokens = new StringTokenizer(layout, ",");
      int n = Integer.parseInt(tokens.nextToken());
  
      for (int i=0;i<n&&i<columns.getColumnCount();i++) {
        TableColumn col = columns.getColumn(i);
        int w = Integer.parseInt(tokens.nextToken());
        col.setWidth(w);
        col.setPreferredWidth(w);
      }
      
      model.cancelSorting();
      while (tokens.hasMoreTokens()) {
        int c = Integer.parseInt(tokens.nextToken());
        int d = Integer.parseInt(tokens.nextToken());
        if (c<columns.getColumnCount())
          model.setSortingStatus(c, d);
      }

    } catch (Throwable t) {
      
    }
  }
  
  
  public int[] getColumnDirections() {
    SortableTableModel model = (SortableTableModel)table.getModel();
    int[] result = new int[model.getColumnCount()];
    for (int i = 0; i < result.length; i++) {
      result[i] = model.getSortingStatus(i);
    }
    return result;
  }

  
  public void setColumnDirections(int[] set) {
    SortableTableModel model = (SortableTableModel)table.getModel();
    for (int i = 0; i<set.length && i<model.getColumnCount(); i++) {
      model.setSortingStatus(i, set[i]);
    }
  }
  
  
  private class Table extends JTable implements ContextProvider {
    
    private PropertyTableModel propertyModel;
    private SortableTableModel sortableModel = new SortableTableModel();
    private int defaultRowHeight;
    
    
    Table() {

      setPropertyTableModel(null);
      setDefaultRenderer(Object.class, new Renderer());
      getTableHeader().setReorderingAllowed(false);
      
      getColumnModel().getSelectionModel().addListSelectionListener(this);
      
      

      Renderer r = new Renderer();
      r.setFont(getFont());
      defaultRowHeight = r.getPreferredSize().height;

      
      setModel(sortableModel);
      sortableModel.setTableHeader(getTableHeader());
      
      
      setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
      setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
      
      
      addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
          
          int row = rowAtPoint(e.getPoint());
          int col = columnAtPoint(e.getPoint());
          if (row<0||col<0)
            clearSelection();
          else {
            if (!isCellSelected(row, col)) {
              getSelectionModel().setSelectionInterval(row, row);
              getColumnModel().getSelectionModel().setSelectionInterval(col, col);
            }
          }
        }
      });
      
      
      sortableModel.addTableModelListener(new TableModelListener() {
        public void tableChanged(TableModelEvent e) {
          if (e.getLastRow()==Integer.MAX_VALUE) createShortcuts();
        }
      });
      panelShortcuts.addComponentListener(new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
          createShortcuts();
        }
      });
      
      
    }
    
    @Override
    public TransferHandler getTransferHandler() {
      if (transferer==null)
        transferer = new Transferer();
      return transferer;
    }
    
    @Override
    public int[] getSelectedRows() {
      int[] rows = super.getSelectedRows();
      for (int r=0;r<rows.length;r++)
        rows[r] = sortableModel.modelIndex(rows[r]);
      return rows;
    }
    
    void setRowSelection(int set) {
      getSelectionModel().setSelectionMode(set);
    }
    
    void setColSelection(int set) {
      getColumnModel().setColumnSelectionAllowed(set>=0);
      if (set>=0)
        getColumnModel().getSelectionModel().setSelectionMode(set);
    }
    
    
    Action2 createShortcut(String txt, final int y) {
      return new Action2(txt.toUpperCase()) {
        public void actionPerformed(ActionEvent event) {
          int x = 0;
          try { x = ((JViewport)table.getParent()).getViewPosition().x; } catch (Throwable t) {};
          table.scrollRectToVisible(new Rectangle(x, y, 1, getParent().getHeight()));
        }
      };
    }
    
    
    void createShortcuts(int col, JComponent container) {

      if (propertyModel==null||container.getHeight()==0)
        return;
      
      TableModel model = getModel();
      Collator collator = propertyModel.getGedcom().getCollator();

      
      List<Action2> actions = new ArrayList<Action2>(26);
      
      String cursor = "";
      for (int r=0;r<model.getRowCount();r++) {
        Property prop = (Property)model.getValueAt(r, col);
        if (prop instanceof PropertyDate)
          break;
        if (prop==null)
          continue;
        
        String value = prop instanceof PropertyName ? ((PropertyName)prop).getLastName().trim() : prop.getDisplayValue().trim(); 
        if (value.length()==0)
          continue;
        value = value.substring(0,1).toLowerCase();
        
        if (collator.compare(cursor, value)>=0)
          continue;
        cursor = value;

        
        Action2 action = createShortcut(value, table.getCellRect(r, col, true).y);
        actions.add(action);
        
        
        InputMap imap = container.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap amap = container.getActionMap();
        imap.put(KeyStroke.getKeyStroke(value.charAt(0)), action);
        amap.put(action, action);
      }
      
      
      if (actions.isEmpty())
        return;
      
      LinkWidget sample = new LinkWidget("Sample", null);
      int h = sample.getPreferredSize().height;
      int n = Math.min(actions.size(), (container.getHeight() - h) / h);
      for (int i=0;i<n;i++) {
        LinkWidget link = new LinkWidget(actions.get(i*actions.size()/n));
        link.setAlignmentX(0.5F);
        container.add(link);
      }
      
      if (n<actions.size()) {
        LinkWidget link = new LinkWidget(actions.get(actions.size()-1));
        link.setAlignmentX(0.5F);
        container.add(link);
      }
      
      
    }
    
    
    void createShortcuts() {
      
      
      panelShortcuts.removeAll();
      panelShortcuts.getInputMap(WHEN_IN_FOCUSED_WINDOW).clear();
      panelShortcuts.getActionMap().clear();
      panelShortcuts.revalidate();
      panelShortcuts.repaint();
      
      
      if (!sortableModel.isSorting())
        return;
      
      SortableTableModel.Directive directive = (SortableTableModel.Directive)sortableModel.getDirectives().get(0);
      if (directive.getDirection()<=0)
        return;
      
      createShortcuts(directive.getColumn(), panelShortcuts);

      
    }
    
    
    Point getCell(Property property) {
      
      Point p = new Point(-1,-1);
      
      if (propertyModel==null)
        return p;
      
      SortableTableModel model = (SortableTableModel)getModel();
      for (int i=0;i<model.getRowCount();i++) {
        
        int r = model.modelIndex(i);

        for (int j=0; j<model.getColumnCount(); j++) {
          if (model.getValueAt(r, j)==property)
            return new Point(j,r);
        }

      }
      
      return p;
    }

    Property getRowRoot(int index) {
      return propertyModel.getRowRoot(sortableModel.modelIndex(index));
    }

    
    void setPropertyTableModel(PropertyTableModel propertyModel) {
      
      this.propertyModel = propertyModel;
      
      sortableModel.setTableModel(new Model(propertyModel));
      
    }
    
    
    PropertyTableModel getPropertyTableModel() {
      return propertyModel;
    }

    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
      
      
      List<? extends Property> before = getContext().getProperties();
      
      
      super.changeSelection(rowIndex, columnIndex, toggle, extend);
      
      
      if (ignoreSelection)
        return;

      List<Property> properties = new ArrayList<Property>();
      ListSelectionModel rows = getSelectionModel();
      ListSelectionModel cols  = getColumnModel().getSelectionModel();
      
      for (int r=rows.getMinSelectionIndex() ; r<=rows.getMaxSelectionIndex() ; r++) {
        for (int c=cols.getMinSelectionIndex(); c<=cols.getMaxSelectionIndex(); c++) {
          
          if (!rows.isSelectedIndex(r)||!cols.isSelectedIndex(c))
            continue;
          
          SortableTableModel model = (SortableTableModel)getModel();
          if (r<0||r>=model.getRowCount()||c<0||c>=model.getColumnCount())
            continue;
          Property prop = (Property)getValueAt(r,c);
          if (prop==null)
            prop = propertyModel.getRowRoot(model.modelIndex(r));
          
          if (before.contains(prop)) 
            properties.add(prop);
          else
            properties.add(0, prop);
        }
      }
      
      
      if (!properties.isEmpty()) {
        ignoreSelection = true;
        SelectionSink.Dispatcher.fireSelection(PropertyTableWidget.this, new Context(properties.get(0).getGedcom(), new ArrayList<Entity>(), properties), false);	
        ignoreSelection = false;
      }
      
      
    }
    
     
    public Dimension getPreferredScrollableViewportSize() {
      Dimension d = super.getPreferredScrollableViewportSize();
      if (visibleRowCount>0) {
        d.height = 0; 
        for(int row=0; row<visibleRowCount; row++) {
          if (row<getModel().getRowCount())
            d.height += getRowHeight(row); 
          else
            d.height += defaultRowHeight; 
        }
      }
      return d;
    }
    
    
    public ViewContext getContext() {
      
      
      Gedcom ged = propertyModel.getGedcom();
      if (ged==null)
        return null;
      SortableTableModel model = (SortableTableModel)getModel();
      
      
      List<Property> properties = new ArrayList<Property>();
      int[] rows = super.getSelectedRows();
      if (rows.length>0) {
        int[] cols = getSelectedColumns();

        
        for (int r=0;r<rows.length;r++) {
          
          
          boolean rowRepresented = false;
          for (int c=0;c<cols.length;c++) {
            
            Property p = (Property)getValueAt(rows[r], cols[c]);
            if (p!=null) {
              properties.add(p);
              rowRepresented = true;
            }
            
          }
          
          
          if (!rowRepresented)
            properties.add(propertyModel.getRowRoot(model.modelIndex(rows[r])));
          
          
        }
      }
      
      
      return new ViewContext(ged, new ArrayList<Entity>(), properties);
    }
    
    
    private class Model extends AbstractTableModel implements PropertyTableModelListener, SortableTableModel.RowComparator {
      
      
      private PropertyTableModel model;
      
      
      private Property cells[][];
      
      
      private Model(PropertyTableModel set) {
        
        model = set;
        
      }
      
      private Gedcom getGedcom() {
        return model!=null ? model.getGedcom() : null;
      }
      
      public void handleRowsAdded(PropertyTableModel model, int rowStart, int rowEnd) {
        
        
        int 
          rows = model.getNumRows(), 
          cols = model.getNumCols();
        cells = new Property[rows][cols];
        
        
        fireTableRowsInserted(rowStart, rowEnd);
      }
      
      public void handleRowsDeleted(PropertyTableModel model, int rowStart, int rowEnd) {
        
        int 
          rows = model.getNumRows(), 
          cols = model.getNumCols();
        cells = new Property[rows][cols];
        
        
        fireTableRowsDeleted(rowStart, rowEnd);
      }
      
      public void handleRowsChanged(PropertyTableModel model, int rowStart, int rowEnd, int col) {
        
        for (int i=rowStart; i<=rowEnd; i++) 
          cells[i][col] = null;
        
        fireTableChanged(new TableModelEvent(this, rowStart, rowEnd, col));
      }
      
      
      public void addTableModelListener(TableModelListener l) {
        super.addTableModelListener(l);
        
        if (model!=null&&getListeners(TableModelListener.class).length==1)
          model.addListener(this);
      }
      
      
      public void removeTableModelListener(TableModelListener l) {
        super.removeTableModelListener(l);
        
        if (model!=null&&getListeners(TableModelListener.class).length==0)
          model.removeListener(this);
      }
      
      @Override
      public int compare(Object valueA, Object valueB, int col) {
        if (propertyModel instanceof AbstractPropertyTableModel)
          return ((AbstractPropertyTableModel)propertyModel).compare((Property)valueA,(Property)valueB, col);
        return AbstractPropertyTableModel.defaultCompare((Property)valueA,(Property)valueB, col);
      }
      
      
      public String getColumnName(int col) {
        return model!=null ? model.getColName(col) : "";
      }
      
      
      public int getColumnCount() {
        return model!=null ? model.getNumCols() : 0;
      }
      
      
      public int getRowCount() {
        return model!=null ? model.getNumRows() : 0;
      }
      
      
      private TagPath getPath(int col) {
        return model!=null ? model.getColPath(col) : null;
      }
      
      
      private Context getContextAt(int row, int col) {
        
        if (model==null)
          return null;
        
        Property prop = getPropertyAt(row, col);
        if (prop!=null)
          return new Context(prop);
        
        
        Property root = model.getRowRoot(row);
        if (root!=null)
          return new Context(root.getEntity());

        
        return new Context(model.getGedcom());
      }
      
      
      private Property getPropertyAt(int row, int col) {
        
        
        if (cells==null) 
          cells = new Property[model.getNumRows()][model.getNumCols()];
        
        Property prop = cells[row][col];
        if (prop==null) {
          prop = model.getRowRoot(row).getProperty(model.getColPath(col));
          cells[row][col] = prop;
        }
        return prop;
      }
      
      
      public Object getValueAt(int row, int col) {
        return getPropertyAt(row, col);
      }
      
      
      private Property getProperty(int row) {
        return model.getRowRoot(row);
      }
      
    } 
    
    
    private class Renderer extends HeadlessLabel implements TableCellRenderer {
      
      Renderer() {
        setPadding(2);
      }
      
      
      public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focs, int row, int col) {

        setFont(table.getFont());
        if (getRowHeight()!=getPreferredSize().height)
          setRowHeight(getPreferredSize().height);
        
        
        if (propertyModel instanceof AbstractPropertyTableModel) {
          AbstractPropertyTableModel m = (AbstractPropertyTableModel)propertyModel;
          setText(m.getCellValue((Property)value, row, col));
          setHorizontalAlignment(m.getCellAlignment((Property)value, row, col));
        } else {
          setText(AbstractPropertyTableModel.getDefaultCellValue((Property)value, row, col));
          setHorizontalAlignment(AbstractPropertyTableModel.getDefaultCellAlignment((Property)value, row, col));
        }
        
        
        if (selected) {
          setBackground(table.getSelectionBackground());
          setForeground(table.getSelectionForeground());
          setOpaque(true);
        } else {
          setForeground(table.getForeground());
          setOpaque(false);
        }
        
        return this;
      }
      
    } 
    
    private class Transferer extends TransferHandler {

      
      protected Transferable createTransferable(JComponent c) {
        
        
        if (c!=Table.this) 
          return null;
        
        
        int[] cols = table.getSelectedColumns();
        int[] rows = table.getSelectedRows();

        if (rows == null || cols == null || rows.length == 0 || cols.length == 0) 
          return null;

        StringBuffer plainBuf = new StringBuffer();
        StringBuffer htmlBuf = new StringBuffer();

        htmlBuf.append("<html>\n<body>\n<table>\n");

        for (int row = 0; row < rows.length; row++) {
          htmlBuf.append("<tr>\n");
          for (int col = 0; col < cols.length; col++) {
            Property obj = (Property)table.getValueAt(rows[row], cols[col]);
            String val = ((obj == null) ? "" : obj.getDisplayValue());
            plainBuf.append(val + "\t");
            htmlBuf.append("  <td>" + val + "</td>\n");
          }
          
          plainBuf.deleteCharAt(plainBuf.length() - 1).append("\n");
          htmlBuf.append("</tr>\n");
        }

        
        plainBuf.deleteCharAt(plainBuf.length() - 1);
        htmlBuf.append("</table>\n</body>\n</html>");

        return new BasicTransferable(plainBuf.toString(), htmlBuf.toString());
      }

      public int getSourceActions(JComponent c) {
        return c==Table.this ? COPY : NONE;
      }
    }
  } 

  
  
  



































































  
  
} 
