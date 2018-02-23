package jTable;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class HeaderRenderer extends JCheckBox implements TableCellRenderer {
	private final JLabel label = new JLabel("Check All");
	private int targetColumnIndex;

	public HeaderRenderer(JTableHeader header, int index) {
		super((String) null);
		this.targetColumnIndex = index;
		setOpaque(false);
		setFont(header.getFont());

		header.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTableHeader header = (JTableHeader) e.getSource();
				JTable table = header.getTable();
				TableColumnModel columnModel = table.getColumnModel();
				int vci = columnModel.getColumnIndexAtX(e.getX());
				int mci = table.convertColumnIndexToModel(vci);
				if (mci == targetColumnIndex) {
					TableColumn column = columnModel.getColumn(vci);
					Object v = column.getHeaderValue();
					boolean b = Status.DESELECTED.equals(v) ? true : false;
					TableModel m = table.getModel();
					for (int i = 0; i < m.getRowCount(); i++)
						m.setValueAt(b, i, mci);
					column.setHeaderValue(b ? Status.SELECTED : Status.DESELECTED);
					// header.repaint();
				}
			}
		});
	}

	@Override
	public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isS, boolean hasF, int row,
			int col) {
		TableCellRenderer r = tbl.getTableHeader().getDefaultRenderer();
		JLabel l = (JLabel) r.getTableCellRendererComponent(tbl, val, isS, hasF, row, col);
		if (targetColumnIndex == tbl.convertColumnIndexToModel(col)) {
			if (val instanceof Status) {
				switch ((Status) val) {
				case SELECTED:
					setSelected(true);
					setEnabled(true);
					break;
				case DESELECTED:
					setSelected(false);
					setEnabled(true);
					break;
				case INDETERMINATE:
					setSelected(true);
					setEnabled(false);
					break;
				}
			} else {
				setSelected(true);
				setEnabled(false);
			}
			label.setIcon(new ComponentIcon(this));
			l.setIcon(new ComponentIcon(label));
			l.setText(null); // XXX: Nimbus???
		}
		return l;
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
