package patterns;

import java.util.Map;

import filesystem.XFile;

public interface IObserver {
	public void update(long val);
	public void finished(Map<Long, XFile> list,boolean interrupted);

}
