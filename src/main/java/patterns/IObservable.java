package patterns;

public interface IObservable {
	public void notifyObservers(long val);

	public void add(IObserver observer);

	public void remove(IObserver observer);

	public void finished();

}
