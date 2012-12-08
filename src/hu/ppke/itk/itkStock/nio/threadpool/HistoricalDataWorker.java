package hu.ppke.itk.itkStock.nio.threadpool;

import java.nio.channels.SelectionKey;

/**
 * @author Andras Baranko
 * A simple static class, to service the historic data requests.
 */
public class HistoricalDataWorker {
	public static void writeDataToSocket(SelectionKey key, byte[] data) {
		ThreadPool pool = ThreadPool.getInstance();
		WorkerThread worker = pool.getWorker();
		worker.serviceChannel(key, data);
	}
}
