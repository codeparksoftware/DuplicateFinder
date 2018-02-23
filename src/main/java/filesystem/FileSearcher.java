package filesystem;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileSearcher {

	public static Map<Long, XFile> lst = new HashMap();

	public static void SearchFiles(String path) {

		File file = new File(path);
		File[] listOfFiles = file.listFiles();

		if (listOfFiles == null || listOfFiles.length == 0)
			return;
		for (File tmp : listOfFiles) {
			if (tmp.isDirectory()) {
				SearchFiles(tmp.getAbsolutePath());

			} else if (tmp.isFile()) {
				System.out.println(tmp.getAbsolutePath());
				XFile x = new XFile();
				x.count++;
				x.paths.add(tmp.getAbsolutePath());
				x.hash = XHash.Hash(tmp.getAbsolutePath());
				if (lst.containsKey(x.hash)) {
					XFile xf = lst.get(x.hash);
					xf.count++;
					xf.paths.add(tmp.getAbsolutePath());
				} else
					lst.put(x.hash, x);
			}

		}

	}

}
