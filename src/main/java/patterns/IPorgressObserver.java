package patterns;

import java.util.Map;

import filesystem.XFile;

/**
 * @author Selami Observer pattern implementation.Observer class obeserves two
 *         things. First observes thread id to manage sub process second
 *         observes work progress state
 */
public interface IPorgressObserver extends IObserver {

	

	public void addThreadId(long val);

	public void removeThreadId(long val);
	
}
