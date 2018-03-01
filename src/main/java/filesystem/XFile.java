package filesystem;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class XFile {

	public short count;
	public List<String> paths;
	public long hash;
	public long size;
	public String name = "";
	public String ext = "";
	public Color color;
	public ImageIcon img;

	public XFile() {

		count = 0;
		paths = new ArrayList<String>();

	}

}
