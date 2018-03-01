package jTable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
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

public class XJTable extends JTable {

	protected transient HeaderCheckBoxHandler handler;

	private int CHECKBOX_COLUMN = -1;

	public void updateUI() {

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
		TableCellRenderer renderer = new EvenOddRenderer();
		setDefaultRenderer(Object.class, renderer);
		if (CHECKBOX_COLUMN >= 0 && getColumnModel() != null && getColumnModel().getColumnCount() > 0) {

			TableColumn column = getColumnModel().getColumn(CHECKBOX_COLUMN);
			column.setHeaderRenderer(new HeaderRenderer());
			column.setHeaderValue(Status.INDETERMINATE);

			handler = new HeaderCheckBoxHandler(this, CHECKBOX_COLUMN);
			m.addTableModelListener(handler);
			getTableHeader().addMouseListener(handler);
		}
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

	public int getCheckBOXCol() {
		return getCHECKBOX_COLUMN();
	}

	public void setCheckBOXCol(int checkBOXCol) {
		this.setCHECKBOX_COLUMN(checkBOXCol);
		if (checkBOXCol >= 0)
			updateUI();
	}

	private int getCHECKBOX_COLUMN() {
		return CHECKBOX_COLUMN;
	}

	private void setCHECKBOX_COLUMN(int cHECKBOX_COLUMN) {
		CHECKBOX_COLUMN = cHECKBOX_COLUMN;
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
					m.setValueAt(b, i, targetColumnIndex);
				}
				Map<Long, Integer> tmpList = new HashMap<Long, Integer>();

				if (b) {
					for (int i = 0; i < m.getRowCount(); i++) {
						long hash = (long) m.getValueAt(i, 6);
						tmpList.put(hash, i);
					}

					Object[] arr = tmpList.values().toArray();
					for (int i = 0; i < arr.length; i++) {
						m.setValueAt(false,(int) arr[i], targetColumnIndex);
					}

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

	enum Status {
		SELECTED, DESELECTED, INDETERMINATE
	}
}
