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
 * @author Andras A simple Thread class to service on the thread pool request.
 */
/**
 * @author Andras
 * 
 */
public class WorkerThread extends Thread {
	private SelectionKey key;
	private ByteBuffer data;

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
	 * This method is called from the static HistoricDataWorker. At every pull
	 * request, this method sets the serviceable selection key, and generates
	 * the response asked inside the requested data byte array. Finally, it
	 * notifyes the run loop, that we got data it can service.
	 * 
	 * @param k
	 *            - the key to service
	 * @param requestedData
	 *            - The requested data, in its specific form.
	 */
	public synchronized void serviceChannel(SelectionKey k, byte[] requestedData) {
		key = k;
		try {
			data = request(requestedData);
		} catch (SQLException e) {
			e.printStackTrace();
			data = ByteBuffer.wrap("There where database error at the request"
					.getBytes());
		}
		key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE));
		this.notify();
	}

	/**
	 * @param r
	 *            The data in byte[], in the form of T(optional ticker(s,
	 *            separeted my commas))S(Y-m-d of the given day, in case of the
	 *            next parameter is not empty, this means the first day)E(Y-m-d
	 *            or a simple -)
	 *            Examples: 
	 *            T(OTP,EGIS)S(2012-01-01)E(2012-10-10)
	 *            			OTP and EGIS datas between 2012-01-01 and 2012-10-10
	 *            T()S(2010-01-25)E(-)
	 *            			all data from 2010-01-25
	 * @return The serialized data.
	 * @throws SQLException
	 */
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
					result = StockData.fetchData(tickers, start);
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