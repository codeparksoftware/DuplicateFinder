package patterns;

/*
 * @author selami
 *Observer pattern's equivalent  .Observable class notify two things.
 *First notify current thread id to manage sub process (start,stop,pause,resume ops.)
 *second notify work progress state  as percent value
 *
 */
public interface IPorgressObservable extends IObservable {

	public void addThreadId(long val);

	public void removeThreadId(long val);

}
