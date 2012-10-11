package hu.ppke.itk.itkStock.nio.core;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractWorker implements Runnable {
	private List<ServerDataEvent> queue = new LinkedList<ServerDataEvent>();

	public abstract byte[] response(byte[] data, int count);

	public void processData(NioServer server, SocketChannel socket, byte[] data, int count) {
		byte[] resp = response(data, count);
		synchronized (queue) {
			queue.add(new ServerDataEvent(server, socket, resp));
			queue.notify();
		}
	}

	public void run() {
		ServerDataEvent dataEvent;

		while (true) {
			// Wait for data to become available
			synchronized (queue) {
				while (queue.isEmpty()) {
					try {
						queue.wait();
					} catch (InterruptedException e) {
					}
				}
				dataEvent = (ServerDataEvent) queue.remove(0);
			}

			// Return to sender
			dataEvent.server.send(dataEvent.socket, dataEvent.data);
		}
	}
}