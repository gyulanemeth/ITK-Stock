package hu.ppke.itk.itkStock.nio.threadpool;

import hu.ppke.itk.itkStock.server.db.historicData.StockData;
import hu.ppke.itk.itkStock.server.db.historicData.StockDate;
import hu.ppke.itk.itkStock.server.db.historicData.StockTime;
import hu.ppke.itk.itkStock.server.db.historicData.Transaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.Map;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * * A worker thread class which can drain channels and echo-back * the input.
 * Each instance is constructed with a reference to * the owning thread pool
 * object. When started, the thread loops * forever waiting to be awakened to
 * service the channel associated * with a SelectionKey object. * The worker is
 * tasked by calling its serviceChannel( ) method * with a SelectionKey object.
 * The serviceChannel( ) method stores * the key reference in the thread object
 * then calls notify( ) * to wake it up. When the channel has been drained, the
 * worker * thread returns itself to its parent pool.
 */
public class WorkerThread extends Thread {
	private SelectionKey key;
	private ByteBuffer data;

	// Loop forever waiting for work to do
	public synchronized void run() {
		System.out.println(this.getName() + " is ready");
		while (true) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace(); // Clear interrupt status
				WorkerThread.interrupted();
			}
			try {
				writeChannel();
			} catch (Exception e) {
				System.out.println("Sending historic data to user failed.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * * Called to initiate a unit of work by this worker thread * on the
	 * provided SelectionKey object. This method is * synchronized, as is the
	 * run( ) method, so only one key * can be serviced at a given time. *
	 * Before waking the worker thread, and before returning * to the main
	 * selection loop, this key's interest set is * updated to remove OP_READ.
	 * This will cause the selector * to ignore read-readiness for this channel
	 * while the * worker thread is servicing it.
	 */
	public synchronized void serviceChannel(SelectionKey k, byte[] requestedData) {
		key = k;
		try {
			data = request(requestedData);
		} catch (SQLException e) {
			e.printStackTrace();
			data = ByteBuffer.wrap("There where database error at the request".getBytes());
		}
		key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE));
		this.notify();
	}

	private synchronized ByteBuffer request(byte[] r) throws SQLException {
		Pattern datePatt = Pattern.compile("T\\((.+)\\)S\\((.+)\\)E\\((.+)\\)");
		Matcher m = datePatt.matcher(r.toString());
		Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> result = null;
		if (m.matches()) {
			String[] tickers = m.group(1).split(",");
			int year = Integer.parseInt(m.group(2).split("-")[0]);
			int month = Integer.parseInt(m.group(2).split("-")[1]);
			int day = Integer.parseInt(m.group(2).split("-")[2]);
			StockDate start = new StockDate(year, month, day);
			StockDate end = null;
			String[] temp = m.group(3).split("-");
			if (temp.length > 1) {
				year = Integer.parseInt(temp[0]);
				month = Integer.parseInt(temp[1]);
				day = Integer.parseInt(temp[2]);
				end = new StockDate(year, month, day);
			}
			if (tickers[0].isEmpty()) {
				if (end == null)
					result = StockData.fetchData(start);
				else
					result = StockData.fetchData(start, end);
			} else {
				if (end == null)
					result = StockData.fetchData(tickers,start);
				else
					result = StockData.fetchData(tickers, start, end);
			}
		}
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(byteOut);
			out.writeObject(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		r = byteOut.toByteArray();
		ByteBuffer buffer = ByteBuffer.allocate(r.length);
		buffer.put(r);
		return buffer;
	}

	void writeChannel() throws Exception {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		socketChannel.write(data);
		key.interestOps(SelectionKey.OP_READ);
	}
}