package jTable;

import java.io.File;

import common.StringUtils;

public class FileModel {
	public FileModel(File f) {

		setFileName(f.getName());
		setType(StringUtils.getFileExtension(f));
		setSize(f.length());
		setPath(f.getAbsolutePath());
	}

	private static final int NAME_INDEX = 1;
	private static final int CHECK_INDEX = 2;
	private static final int SIZE_INDEX = 3;
	private static final int FILETYPE_INDEX = 4;
	private static final int PATH_INDEX = 5;

	private String fileName;
	private long Size;
	private String type;
	private String path;
	private boolean check;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getSize() {
		return Size;
	}

	private void setSize(long size) {
		Size = size;
	}

	public String getType() {
		return type;
	}

	private void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isCellEditable(int row, int column) {
		// all cells false
		return false;
	}

	public Object GetValue(int colIndex) {
		switch (colIndex) {
		case NAME_INDEX:
			return getFileName();
		case CHECK_INDEX:
			return isCheck();
		case SIZE_INDEX:
			return getSize();
		case FILETYPE_INDEX:
			return getType();
		case PATH_INDEX:
			return getPath();
		default:
			return new Object();
		}
	}

	public void setValue(Object value, int colIndex) {
		switch (colIndex) {
		case NAME_INDEX:
			setFileName((String) value);
		case CHECK_INDEX:
			setCheck((boolean) value);
		case SIZE_INDEX:
			setSize((long) value);
		case FILETYPE_INDEX:
			setType((String) value);
		case PATH_INDEX:
			setPath(path);
		default:
			System.out.println("No such column   in here!...");
		}
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

}
