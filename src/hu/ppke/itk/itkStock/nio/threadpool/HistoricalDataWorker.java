package hu.ppke.itk.itkStock.nio.threadpool;

import java.nio.channels.SelectionKey;

public class HistoricalDataWorker {
	public static void writeDataToSocket(SelectionKey key, byte[] data) {
		ThreadPool pool = ThreadPool.getInstance();
		WorkerThread worker = pool.getWorker();
		worker.serviceChannel(key, data);
	}
}
