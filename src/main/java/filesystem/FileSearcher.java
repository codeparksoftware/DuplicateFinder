package filesystem;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import common.StringUtils;
import log.Level;
import log.Logger;
import patterns.IObserver;
import patterns.IPorgressObservable;
import patterns.IPorgressObserver;

public class FileSearcher implements IPorgressObservable, Runnable {

	private String searchPath;
	private IPorgressObserver observer;
	private Object MUTEX = new Object();
	private static final Logger logger = Logger.getLogger(FileSearcher.class.getName());
	private long countFile = 0;
	private boolean interrupted = false;

	public FileSearcher(String Initializepath) {
		searchPath = Initializepath;

	}

	private Map<Long, XFile> lst = new HashMap<Long, XFile>();

	public void SearchFiles(String path) {

		File file = new File(path);
		File[] listOfFiles = file.listFiles();

		if (listOfFiles == null || listOfFiles.length == 0)
			return;
		for (File tmp : listOfFiles) {
			if (Thread.currentThread().isInterrupted()) {
				interrupted = true;
				return;
			}
			if (tmp.isDirectory()) {
				SearchFiles(tmp.getAbsolutePath());

			} else if (tmp.isFile()) {

				XFile x = new XFile();
				x.name = tmp.getName();
				x.ext = StringUtils.getFileExtension(tmp);
				x.count++;
				x.paths.add(tmp.getAbsolutePath());
				x.hash = XHash.Hash(tmp.getAbsolutePath());
				x.size = tmp.length();
				if (lst.containsKey(x.hash) && lst.get(x.hash) != null && (lst.get(x.hash).size == x.size)) {
					XFile xf = lst.get(x.hash);
					xf.count++;
					xf.paths.add(tmp.getAbsolutePath());
					notifyObservers(++countFile);

				} else
					lst.put(x.hash, x);
			}

		}

	}

	@Override
	public void run() {

		StartSearch();

	}

	private void StartSearch() {

		addThreadId(Thread.currentThread().getId());
		SearchFiles(searchPath);
		removeThreadId(Thread.currentThread().getId());

		finished();
	}

	@Override
	public void notifyObservers(long val) {

		observer.update(val);
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			logger.log(Level.Error, e.getMessage());
		}

	}

	@Override
	public void add(IObserver observer) {
		if (observer == null)
			throw new NullPointerException("Null Observer");
		this.observer = (IPorgressObserver) observer;

	}

	@Override
	public void remove(IObserver observer) {
		observer = null;

	}

	@Override
	public void addThreadId(long val) {

		observer.addThreadId(val);
	}

	@Override
	public void removeThreadId(long val) {

		observer.removeThreadId(val);

	}

	@Override
	public void finished() {

		observer.finished(lst, interrupted);

	}

}
