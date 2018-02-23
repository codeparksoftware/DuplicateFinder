package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import Gradient.GradientPanel;
import jTable.AbstractFileModel;
import jTable.Status;

public class frmMain extends JFrame {
	private JTable table;

	public static final String[] columnNames = { "File", "Select All", "Size", "Type", "Path" };
	protected transient HeaderCheckBoxHandler handler;
	protected final int CHECKBOX_COLUMN = 1;

	public frmMain() {
		URL iconUrl = this.getClass().getResource("/images/duplicate_main_50.png");
		Toolkit tk = this.getToolkit();
		ImageIcon bannericon = new ImageIcon(tk.getImage(iconUrl));
		URL icoForm = this.getClass().getResource("/images/clean16.png");
		// Form
		setIconImage(tk.getImage(icoForm));
		this.setTitle(".::Duplicate Finder&Cleaner::. ");
		setPreferredSize(new Dimension(1024, 768));
		// Form

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		GradientPanel panel_1 = new GradientPanel();
		panel_1.setColorStart(Color.WHITE);
		panel_1.setColorEnd(Color.ORANGE);

		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.setSize(panel.getWidth(), 50);
		JLabel label = new JLabel();
		panel_1.add(label, BorderLayout.WEST);
		label.setIcon(bannericon);

		JLabel lblNewLabel = new JLabel("       Find & Clean Your Duplicate Files!");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		panel_1.add(lblNewLabel, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));

		setJTable(panel_2);
		this.pack();
		this.show();
	}

	private void setJTable(JPanel panel_2) {
		table = new JTable(new AbstractFileModel(columnNames)) {

			@Override
			public void updateUI() {
				// [JDK-6788475] Changing to Nimbus LAF and back doesn't reset look and feel of
				// JTable completely - Java Bug System
				// https://bugs.openjdk.java.net/browse/JDK-6788475
				// XXX: set dummy ColorUIResource
				setSelectionForeground(new ColorUIResource(Color.RED));
				setSelectionBackground(new ColorUIResource(Color.RED));
				getTableHeader().removeMouseListener(handler);
				TableModel m = getModel();
				if (Objects.nonNull(m)) {
					m.removeTableModelListener(handler);
				}
				super.updateUI();

				m = getModel();
				for (int i = 0; i < m.getColumnCount(); i++) {
					TableCellRenderer r = getDefaultRenderer(m.getColumnClass(i));
					if (r instanceof Component) {
						SwingUtilities.updateComponentTreeUI((Component) r);
					}
				}
				TableColumn column = getColumnModel().getColumn(CHECKBOX_COLUMN);
				column.setHeaderRenderer(new HeaderRenderer());
				column.setHeaderValue(Status.INDETERMINATE);

				handler = new HeaderCheckBoxHandler(this, CHECKBOX_COLUMN);
				m.addTableModelListener(handler);
				getTableHeader().addMouseListener(handler);
			}

			@Override
			public Component prepareEditor(TableCellEditor editor, int row, int column) {
				Component c = super.prepareEditor(editor, row, column);
				if (c instanceof JCheckBox) {
					JCheckBox b = (JCheckBox) c;
					b.setBackground(getSelectionBackground());
					b.setBorderPainted(true);
				}
				return c;
			}

		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		table.setFillsViewportHeight(true);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());

		// TableCellRenderer renderer = new EvenOddRenderer();
		// table.setDefaultRenderer(Object.class, renderer);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		table.setFont(new Font(table.getFont().getName(), Font.PLAIN, 11));
		// table.getColumnModel().getColumn(0).setMaxWidth(150);
		table.getColumnModel().getColumn(0).setPreferredWidth((int) this.getWidth() / 4);
		table.getColumnModel().getColumn(1).setMaxWidth(80);
		table.getColumnModel().getColumn(2).setMaxWidth(50);
		table.getColumnModel().getColumn(3).setMaxWidth(50);
		table.getColumnModel().getColumn(4).setPreferredWidth((int) this.getWidth() / 2);

		/*
		 * table.getColumnModel().getColumn(4).setPreferredWidth((int) this.getWidth() /
		 * 5); table.getColumnModel().getColumn(5).setPreferredWidth((int)
		 * this.getWidth() / 5);
		 * table.getColumnModel().getColumn(6).setPreferredWidth((int) this.getWidth() /
		 * 5); table.getColumnModel().getColumn(7).setPreferredWidth((int)
		 * this.getWidth() / 15);
		 */

		JScrollPane js = new JScrollPane(table);

		JPopupMenu popupMenu = new JPopupMenu();

		addPopup(table, popupMenu);
		/*
		 * JMenuItem itmAddFile = new JMenuItem(ADD_FILE);
		 * itmAddFile.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent arg0) { addFile(); } });
		 * popupMenu.add(itmAddFile);
		 * 
		 * JMenuItem itmNewMenuItem = new JMenuItem("Add Folder");
		 * itmNewMenuItem.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { addDirectory(); } });
		 * popupMenu.add(itmNewMenuItem); popupMenu.addSeparator(); JMenuItem
		 * itmRemoveSelected = new JMenuItem("Remove Selected");
		 * itmRemoveSelected.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { removeSelected();
		 * 
		 * } }); popupMenu.add(itmRemoveSelected);
		 * 
		 * JMenuItem itmRemoveAll = new JMenuItem("Remove All");
		 * itmRemoveAll.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { clearList(); } });
		 * popupMenu.add(itmRemoveAll);
		 */
		table.addMouseListener(new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					JTable source = (JTable) e.getSource();
					int row = source.rowAtPoint(e.getPoint());
					int column = source.columnAtPoint(e.getPoint());
					if (!source.isRowSelected(row))
						source.changeSelection(row, column, false, false);
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		panel_2.add(js);

	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	class HeaderRenderer implements TableCellRenderer {
		private final JCheckBox check = new JCheckBox("");
		private final JLabel label = new JLabel("Select All");

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (value instanceof Status) {
				switch ((Status) value) {
				case SELECTED:
					check.setSelected(true);
					check.setEnabled(true);
					break;
				case DESELECTED:
					check.setSelected(false);
					check.setEnabled(true);
					break;
				case INDETERMINATE:
					check.setSelected(true);
					check.setEnabled(false);
					break;
				default:
					throw new AssertionError("Unknown Status");
				}
			} else {
				check.setSelected(true);
				check.setEnabled(false);
			}
			check.setOpaque(false);
			check.setFont(table.getFont());
			TableCellRenderer r = table.getTableHeader().getDefaultRenderer();
			JLabel l = (JLabel) r.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			label.setIcon(new ComponentIcon(check));
			l.setIcon(new ComponentIcon(label));
			l.setText(null); 
		
			return l;
		}
	}

	class HeaderCheckBoxHandler extends MouseAdapter implements TableModelListener {
		private final JTable table;
		private final int targetColumnIndex;

		protected HeaderCheckBoxHandler(JTable table, int index) {
			super();
			this.table = table;
			this.targetColumnIndex = index;
		}

		@Override
		public void tableChanged(TableModelEvent e) {
			if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == targetColumnIndex) {
				int vci = table.convertColumnIndexToView(targetColumnIndex);
				TableColumn column = table.getColumnModel().getColumn(vci);
				Object status = column.getHeaderValue();
				TableModel m = table.getModel();
				if (m instanceof DefaultTableModel && fireUpdateEvent((DefaultTableModel) m, column, status)) {
					JTableHeader h = table.getTableHeader();
					h.repaint(h.getHeaderRect(vci));
				}
			}
		}

		@SuppressWarnings("PMD.ReplaceVectorWithList")
		private boolean fireUpdateEvent(DefaultTableModel m, TableColumn column, Object status) {
			if (Status.INDETERMINATE.equals(status)) {
				List<Boolean> l = ((Vector<?>) m.getDataVector()).stream()
						.map(v -> (Boolean) ((Vector<?>) v).get(targetColumnIndex)).distinct()
						.collect(Collectors.toList());
				boolean isOnlyOneSelected = l.size() == 1;
				if (isOnlyOneSelected) {
					column.setHeaderValue(l.get(0) ? Status.SELECTED : Status.DESELECTED);
					return true;
				} else {
					return false;
				}
			} else {
				column.setHeaderValue(Status.INDETERMINATE);
				return true;
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			JTableHeader header = (JTableHeader) e.getComponent();
			JTable table = header.getTable();
			TableColumnModel columnModel = table.getColumnModel();
			TableModel m = table.getModel();
			int vci = columnModel.getColumnIndexAtX(e.getX());
			int mci = table.convertColumnIndexToModel(vci);
			if (mci == targetColumnIndex && m.getRowCount() > 0) {
				TableColumn column = columnModel.getColumn(vci);
				Object v = column.getHeaderValue();
				boolean b = Status.DESELECTED.equals(v);
				for (int i = 0; i < m.getRowCount(); i++) {
					m.setValueAt(b, i, mci);
				}
				column.setHeaderValue(b ? Status.SELECTED : Status.DESELECTED);
				// header.repaint();
			}
		}
	}

	class ComponentIcon implements Icon {
		private final JComponent cmp;

		protected ComponentIcon(JComponent cmp) {
			this.cmp = cmp;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			SwingUtilities.paintComponent(g, cmp, c.getParent(), x, y, getIconWidth(), getIconHeight());
		}

		@Override
		public int getIconWidth() {
			return cmp.getPreferredSize().width;
		}

		@Override
		public int getIconHeight() {
			return cmp.getPreferredSize().height;
		}
	}

}
