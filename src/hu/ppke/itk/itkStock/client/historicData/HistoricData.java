package hu.ppke.itk.itkStock.client.historicData;

import hu.ppke.itk.itkStock.client.watcherClient.SerializationTools;
import hu.ppke.itk.itkStock.nio.protocol.ProtocolTools;
import hu.ppke.itk.itkStock.nio.protocol.RecieveHistoricData;
import hu.ppke.itk.itkStock.nio.core.NioClient;
import hu.ppke.itk.itkStock.nio.core.RspHandler;
import hu.ppke.itk.itkStock.server.db.historicData.StockData;
import hu.ppke.itk.itkStock.server.db.historicData.StockDate;
import hu.ppke.itk.itkStock.server.db.historicData.StockTime;
import hu.ppke.itk.itkStock.server.db.historicData.Transaction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class HistoricData {
	Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> memory;
	NioClient client;

	/**
	 * There are 4 necessary step for using this class. First, we need the
	 * Mapped stocks from the memory, as the first parameter of this class, and
	 * secondly the client. For using this class we need to call the search
	 * method, if it result with a null value, then we should call the server
	 * thorugh requestFromServer.
	 * 
	 * @param m
	 *            a map in the structure of the server.StockData.fecth
	 * @param c
	 *            the NioClient
	 */
	HistoricData(
			Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> m,
			NioClient c) {
		memory = m;
		client = c;
	}

	/**
	 * First it checks the memory, and after the archive file
	 * 
	 * @param tickers
	 *            The ticker name of the stocks.
	 * @param from
	 *            The start date of the request is StockDate format.
	 * @param to
	 *            The end date
	 * @return the necessary historical data, if it is null, than we should
	 *         request it from the server by {@link requestFromServer}
	 * @throws ParseException
	 *             thrown if the date format is not yyyy-MM-dd
	 */
	public Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> search(
			String[] tickers, StockDate from, StockData to)
			throws ParseException {
		Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> result = new HashMap<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>>();
		result = searchIn(memory, tickers, from, to);
		if (!result.isEmpty()) {
			return result;
		}
		try {
			result = searchIn(getArchive("archive"), tickers, from, to);
		} catch (IOException e) {
			// nem nagy kaland, úgyis lekérjük akkor a szervertõl
		}
		if (!result.isEmpty()) {
			return result;
		}
		return null;
	}

	/**
	 * request the historical data form the server, and add a responsehandler
	 * for the incoming datas, which will write the datas to the archive
	 * 
	 * @param tickers
	 *            The ticker name of the stocks.
	 * @param from
	 *            The start date of the request is StockDate format.
	 * @param to
	 *            The end date
	 * @throws IOException
	 */
	public void requestFromServer(String[] tickers, StockDate from, StockData to)
			throws IOException {
		StringBuilder msg = new StringBuilder();
		msg.append((short) 301);
		msg.append("T(");
		for (String t : tickers) {
			msg.append(t);
		}
		msg.append(")S(").append(from.toString()).append(")E(");
		if (to == null) {
			msg.append("-)");
		} else {
			msg.append(to.toString()).append(")");
		}
		RspHandler rsp = new RspHandler();
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		RecieveHistoricData reciever = new RecieveHistoricData(this);
		rsp.addProtocolCommandWorker(ProtocolTools.getHistoricalData, reciever);
		try {
			byteStream.write(ProtocolTools
					.shortToBytes(ProtocolTools.requestForHistoricalData));
			byteStream.write(msg.toString().getBytes());
			client.send(byteStream.toByteArray(), rsp);
		} catch (IOException e) {
		}
		rsp.waitForResponse();
	}

	/**
	 * This method looks up the map, and returns with just with the requested
	 * data.
	 * 
	 * @param in
	 *            The map where we should process.
	 * @param tickers
	 *            The ticker name of the stocks.
	 * @param from
	 *            The start date of the request is StockDate format.
	 * @param to
	 *            The end date
	 * @return Map with just the requested datas.
	 * @throws ParseException
	 *             Thrown, if the time format is incorrect.
	 */
	private Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> searchIn(
			Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> in,
			String[] tickers, StockDate from, StockData to)
			throws ParseException {
		Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> result = new HashMap<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>>();
		if (tickers == null || tickers.length < 1) {
			for (Map.Entry<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> entry : in
					.entrySet()) {
				String ticker = entry.getKey();
				if (in.containsKey(ticker)) {
					SortedMap<StockDate, SortedMap<StockTime, Transaction>> depth1 = in
							.get(ticker);

					SortedMap<StockDate, SortedMap<StockTime, Transaction>> treeMap = new TreeMap<StockDate, SortedMap<StockTime, Transaction>>();
					if (to == null) {
						if (depth1.containsKey(from)) {
							treeMap.put(from, depth1.get(from));
						}
					} else {
						GregorianCalendar gcal = new GregorianCalendar();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date start = sdf.parse(from.toString());
						Date end = sdf.parse(to.toString());
						gcal.setTime(start);
						while (gcal.getTime().before(end)) {
							StockDate date = new StockDate((short) gcal
									.getTime().getYear(), (byte) gcal.getTime()
									.getMonth(), (byte) gcal.getTime().getDay());
							if (depth1.containsKey(date)) {
								treeMap.put(date, depth1.get(date));
							}
							gcal.add(Calendar.DAY_OF_WEEK, 1);
						}
					}
				}
			}
		} else {
			for (String ticker : tickers) {
				if (in.containsKey(ticker)) {
					SortedMap<StockDate, SortedMap<StockTime, Transaction>> depth1 = in
							.get(ticker);

					SortedMap<StockDate, SortedMap<StockTime, Transaction>> treeMap = new TreeMap<StockDate, SortedMap<StockTime, Transaction>>();
					if (to == null) {
						if (depth1.containsKey(from)) {
							treeMap.put(from, depth1.get(from));
						}
					} else {
						GregorianCalendar gcal = new GregorianCalendar();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date start = sdf.parse(from.toString());
						Date end = sdf.parse(to.toString());
						gcal.setTime(start);
						while (gcal.getTime().before(end)) {
							StockDate date = new StockDate((short) gcal
									.getTime().getYear(), (byte) gcal.getTime()
									.getMonth(), (byte) gcal.getTime().getDay());
							if (depth1.containsKey(date)) {
								treeMap.put(date, depth1.get(date));
							}
							gcal.add(Calendar.DAY_OF_WEEK, 1);
						}
					}
				}
			}
		}
		return result;

	}

	/**
	 * @param filename
	 *            String. The name of the file, where the historic datas archive
	 *            are in a bytearray;
	 * @return the map as in the StockData class
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> getArchive(
			String filename) throws IOException {
		FileInputStream stream = null;
		byte[] bytearray = null;
		try {
			File file = new File(filename);
			stream = new FileInputStream(file);
			bytearray = new byte[(int) file.length()];
			stream.read(bytearray);
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		Object o = SerializationTools.bytesToObject(bytearray);

		return (Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>>) o;
	}

	/**
	 * First, it puts all the datas[] to one big map, and the after a standard
	 * serialization it writes everything to the "filename" file;
	 * 
	 * @param datas
	 *            array of the maps, same as in the StockData class.
	 * @param filename
	 *            String. The filename, where it should archive the datas.
	 * @throws IOException
	 */
	private void makeArchive(
			ArrayList<Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>>> datas,
			String filename) throws IOException {
		Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> archive = new HashMap<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>>();
		for (Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> map : datas) {
			archive.putAll(map);
		}
		byte[] bytearray = SerializationTools.objectToBytes(archive);
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(filename);
			stream.write(bytearray);
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	public void addToArchive(
			Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> data) {
		ArrayList<Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>>> list = new ArrayList<Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>>>();
		list.add(memory);
		try {
			list.add(getArchive("archive"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		list.add(data);
		try {
			makeArchive(list, "archive");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
