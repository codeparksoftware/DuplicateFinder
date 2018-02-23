package filesystem;

import java.util.ArrayList;
import java.util.List;

public class XFile {

	public short count;
	public List<String> paths;
	public long hash;

	public XFile() {

		count = 0;
		paths = new ArrayList<String>();

	}

}
