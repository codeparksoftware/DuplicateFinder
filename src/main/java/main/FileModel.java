package main;

import java.awt.Color;

import javax.swing.ImageIcon;

import common.StringUtils;
import filesystem.XFile;

public class FileModel {
	public FileModel(XFile x, int index) {

		setFileName(x.name);
		setType(x.ext);
		setSize(x.size);
		setPath(x.paths.get(index));
		setHash(x.hash);
		setColor(x.color);
		setImg(x.img);
	}

	private static final short IMAGE_INDEX = 0;
	private static final int NAME_INDEX = 1;
	private static final int CHECK_INDEX = 2;
	private static final int SIZE_INDEX = 3;
	private static final int FILETYPE_INDEX = 4;
	private static final int PATH_INDEX = 5;
	private static final int HASH_INDEX = 6;
	private static final int COLOR_INDEX = 7;
	
	private ImageIcon img;
	private String fileName;
	private String Size;
	private String type;
	private String path;
	private boolean check;
	private long hash;
	private Color color;
	
	public ImageIcon getImg() {
		return img;
	}

	private void setImg(ImageIcon img) {
		this.img = img;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSize() {
		return Size;
	}

	private void setSize(long size) {

		Size = StringUtils.getFileLength(size);
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
		case HASH_INDEX:
			return getHash();
		case COLOR_INDEX:
			return getColor();
		case IMAGE_INDEX:
			return getImg();
		default:
			return new Object();
		}
	}

	public void setValue(Object value, int colIndex) {
		switch (colIndex) {
		case IMAGE_INDEX:
			setImg((ImageIcon) value);
			break;
		case NAME_INDEX:
			setFileName((String) value);
			break;
		case CHECK_INDEX:
			setCheck((boolean) value);
			break;
		case SIZE_INDEX:
			setSize((long) value);
			break;
		case FILETYPE_INDEX:
			setType((String) value);
			break;
		case PATH_INDEX:
			setPath(path);
			break;
		case HASH_INDEX:
			setHash((long) value);
			break;
		case COLOR_INDEX:
			setColor((Color) value);
			break;
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

	public long getHash() {
		return hash;
	}

	public void setHash(long hash) {
		this.hash = hash;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
