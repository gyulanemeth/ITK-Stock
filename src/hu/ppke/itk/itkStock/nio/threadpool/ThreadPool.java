package hu.ppke.itk.itkStock.nio.threadpool;

import java.util.LinkedList;
import java.util.List;

/**
 * A very simple SINGLETON thread pool class. The pool size is set at construction
 * time and remains fixed. Threads are cycled * through a FIFO idle queue.
 * The taken_processors value is necessary for the maximum performance.
 */
public class ThreadPool {
	private final int TAKEN_PROCESSORS = 0;
	List<WorkerThread> idle = new LinkedList<WorkerThread>();

	private ThreadPool() {
		int poolSize = Runtime.getRuntime().availableProcessors()
				- TAKEN_PROCESSORS;
		for (int i = 0; i < poolSize; i++) {
			WorkerThread thread = new WorkerThread();
			thread.setName("Worker" + (i + 1));
			thread.start();
			idle.add(thread);
		}
	}

	private static class SingletonHolder {
		public static final ThreadPool INSTANCE = new ThreadPool();
	}

	public static ThreadPool getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * * Find an idle worker thread, if any. If none of them are available, wait
	 * until any returns.
	 */
	public WorkerThread getWorker() {
		WorkerThread worker = null;
		synchronized (idle) {
			while (idle.size() < 0) {
				try {
					idle.wait();
				} catch (InterruptedException e) {
				}
			}
			worker = (WorkerThread) idle.remove(0);
		}
		return (worker);
	}

	/** * Called by the worker thread to return itself to the * idle pool. */
	void returnWorker(WorkerThread worker) {
		synchronized (idle) {
			idle.add(worker);
			idle.notify();
		}
	}
}