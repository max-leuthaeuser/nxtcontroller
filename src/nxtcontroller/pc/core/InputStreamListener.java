package nxtcontroller.pc.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Object that permanently constructs new data sets from a given input stream.
 * 
 * <p>
 * It is implemented as an observable. Objects that implement the
 * {@link DataSetObserver} interface can register themselves to receive updates
 * upon incoming new data sets.
 * 
 * <p>
 * After you've created an instance of this class, register all your observers
 * and start a new thread using this instance:
 * 
 * <pre>
 * InputStreamListener li = new InputStreamListener(stream);
 * li.register(observer);
 * new Thread(li).start();
 * </pre>
 * 
 * @author Martin Morgenstern
 */
public class InputStreamListener implements Runnable {
	private final InputStream in;
	private Set<DataSetObserver> observers = new HashSet<DataSetObserver>();

	/**
	 * Constructs a new InputStreamListener using an existing InputStream
	 * object.
	 * 
	 * @param in
	 *            An input stream that is ready to use
	 * @throws NullPointerException
	 *             if in is null
	 */
	public InputStreamListener(final InputStream in) {
		if (in == null) {
			throw new NullPointerException();
		}

		this.in = in;
	}

	/**
	 * Register an observer object.
	 * 
	 * @param observer
	 */
	public void register(DataSetObserver observer) {
		observers.add(observer);
	}

	/**
	 * Notify all observers of new incoming data.
	 * 
	 * @param dataSet
	 */
	private void notifyObservers(DataSet dataSet) {
		for (DataSetObserver observer : observers) {
			observer.update(dataSet);
		}
	}

	/**
	 * Retrieves the next data set from the input stream.
	 * 
	 * @return next data set
	 */
	private DataSet getNextDataSet() {
		int left, front, right, angle;

		try {
			left = in.read();
			front = in.read();
			right = in.read();

			angle = in.read();
			angle |= in.read() << 8;
		} catch (IOException e) {
			// TODO properly handle this exception
			throw new RuntimeException();
		}

		return new DataSet(left, front, right, angle);
	}

	/**
	 * Constantly notify all observers of incoming data. WARNING: This method is
	 * supposed to run in a seperate thread.
	 */
	@Override
	public void run() {
		for (;;) {
			notifyObservers(getNextDataSet());
		}
	}
}
