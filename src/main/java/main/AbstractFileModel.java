package main;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

public class AbstractFileModel extends AbstractTableModel {

	private String[] columnNames;
	protected Vector dataVector;

	public AbstractFileModel(String[] columns) {
		columnNames = columns;
		dataVector = new Vector<>();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		// Only the third column
		if (column == 2)
			return true;
		return false;
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return dataVector.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		FileModel fm = (FileModel) dataVector.get(row);
		return fm.GetValue(column);

	}

	public FileModel getDataRow(int row) {
		FileModel fm = (FileModel) dataVector.get(row);

		return fm;
	}

	@Override
	public Class<?> getColumnClass(int column) {
		if (column == 0)
			return ImageIcon.class;
		if (dataVector.size() > 0)
			return getValueAt(0, column).getClass();

		return Object.class;
	}
	 


	public void setValueAt(Object value, int row, int column) {
		FileModel fm = (FileModel) dataVector.get(row);
		fm.setValue(value, column);
		fireTableCellUpdated(row, column);
	}

	public void RemoveAll() {

		int i = dataVector.size();
		dataVector.clear();
		fireTableRowsDeleted(0, i);
	}

	public void addEmptyRow() {
		// dataVector.add(new FileModel());
		// fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
	}

	public void addRow(FileModel f) {

		dataVector.add(f);

		fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
		fireTableDataChanged();
	}

	public void DeleteRow(int rowIndex) {

		dataVector.removeElementAt(rowIndex);
		fireTableRowsDeleted(rowIndex, rowIndex);
		fireTableDataChanged();
	}

}