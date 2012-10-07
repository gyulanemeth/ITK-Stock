package hu.ppke.itk.itkStock.SaveDailyDatas;

import hu.ppke.itk.itkStock.dbaccess.DatabaseConnector;
import hu.ppke.itk.itkStock.stockdata.StockData;
import hu.ppke.itk.itkStock.stockdata.StockDate;
import hu.ppke.itk.itkStock.stockdata.StockTime;
import hu.ppke.itk.itkStock.stockdata.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.sun.tools.javac.util.Pair;

/**
 * Kötés manager osztály Ez felel a kötések adatbázisban rögzítésélrt és a Cache
 * kezeléséért is. Innen történő lekérdezés először megnézi, hogy a memóriából
 * oda tudja-e adni a kért adatot és csak akkor fordul az adatbázishoz, ha ez
 * nem lehetséges.
 * 
 * @author ki-csen
 * 
 */
public class StockDataManager {
	protected Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> Cache = new HashMap<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>>();
	protected Pair<Date, Date> s_interval; // / Cachelt intervallum
	protected StockDataSaver Sdatasaver = null; // /dbManager
	protected List<StockDataObserver> observers = new ArrayList<StockDataObserver>();
	protected List<StockDataRecord> datalist = new ArrayList<StockDataRecord>(); // /átmeneti
																					// tár
																					// a
																					// beolvasáshoz

	/**
	 * StockDate+StockTime To Date
	 * 
	 * @param date
	 *            StockDate
	 * @param time
	 *            StockTime
	 * @return date + time [Date]
	 */
	@SuppressWarnings("deprecation")
	public static Date StockDatetimeToDate(StockDate date, StockTime time) {
		// Depricated de nem tudok vele mit tenni. Nem én implementáltam a
		// használt típusokat.
		return new Date(date.getYear(), date.getMonth(), date.getDay(),
				time.getHour(), time.getMinute(), time.getSecond());
	}

	/**
	 * @param dbc
	 *            dbConnector ezt kívülről kapja, hogy a közöset használja. Nem
	 *            figyel a bezárásra. Idő elötti bezárása —> SQLExc
	 * @throws SQLException
	 */
	public StockDataManager(DatabaseConnector dbc) throws SQLException {
		super();
		Sdatasaver = new StockDataSaver(dbc);
	}

	/**
	 * Beolvasott adatok intervallumának megállapítása, adatok rögzítése és
	 * Cachelése Átmeneti tár törlése
	 * 
	 * @return siker
	 * @throws ImplementationLogicException
	 */
	synchronized public boolean comit() throws ImplementationLogicException {
		List<String> updatedStock = new ArrayList<String>();
		Date lastdate = StockDatetimeToDate(datalist.get(0).getDate(), datalist
				.get(0).getTime());
		Date firstdate = null;
		StockDataRecord prev = null;
		Collections.sort(datalist);
		for (StockDataRecord record : datalist) {
			if (!add(record) && !record.equals(prev)) {
				firstdate = StockDatetimeToDate(record.getDate(),
						record.getTime());
				System.out.println("Braked: " + record.getPapername() + ", "
						+ record.getTime().toString() + " :"
						+ record.equals(prev));
				break;
			} else {
				if (!updatedStock.contains(record.getPapername())) {
					updatedStock.add(record.getPapername());
				}
				System.out.println("Pushed: " + record.getPapername() + ", "
						+ record.getTime().toString());
			}
			prev = record;
		}
		refresh_intervals(firstdate, lastdate);
		notifyUpdatedStock(updatedStock);
		this.datalist = new ArrayList<StockDataRecord>();
		return true;
	}

	/**
	 * Beolvasott kötés rögzítése az átmeneti tárban
	 * 
	 * @param papername
	 * @param date
	 * @param time
	 * @param transaction
	 */
	synchronized public void add(String papername, StockDate date,
			StockTime time, Transaction transaction) {
		datalist.add(new StockDataRecord(Sdatasaver, 0, papername, date, time,
				transaction));
	}

	/**
	 * Kőtás rögzítése az adatbázisban és a Cacheben
	 * 
	 * @param record
	 *            Kötés
	 * @return siker
	 * @throws ImplementationLogicException
	 */
	protected boolean add(StockDataRecord record)
			throws ImplementationLogicException {
		try {
			if (isInCache(record)) {
				return false;
			}

			record.create();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		pushToCache(record.getPapername(), record.getDate(), record.getTime(),
				record.getTransaction());
		return true;
	}

	/**
	 * Cachelt kötések intervallumának frissítése
	 * 
	 * @param start
	 *            hozzáadott intervallum kezdete
	 * @param stop
	 *            hozzáadott intervallum vége
	 */
	protected void refresh_intervals(Date start, Date stop) {
		if (this.s_interval == null) {
			this.s_interval = new Pair<Date, Date>(start, stop);
		} else if (this.s_interval.snd.equals(start)) {
			Date fstart = this.s_interval.fst;
			this.s_interval = new Pair<Date, Date>(fstart, stop);
		} else {
			// /Nincs implementálva az összes eset
			this.s_interval = new Pair<Date, Date>(start, stop);
		}
	}

	/**
	 * Kötés hozzáadása a Cachehez
	 * 
	 * @param papername
	 * @param date
	 * @param time
	 * @param transaction
	 * @return
	 */
	protected boolean pushToCache(String papername, StockDate date,
			StockTime time, Transaction transaction) {
		if (Cache.containsKey(papername)) {
			if (Cache.get(papername).containsKey(date)) {
				if (!Cache.get(papername).get(date).containsKey(time)) {
					Cache.get(papername).get(date).put(time, transaction);
				} else {
					Transaction prev = Cache.get(papername).get(date).get(time);
					Cache.get(papername).get(date)
							.put(time, Transaction.merge(prev, transaction));
				}
			} else {
				TreeMap<StockTime, Transaction> timetree = new TreeMap<StockTime, Transaction>();
				timetree.put(time, transaction);
				Cache.get(papername).put(date, timetree);
			}
		} else {
			TreeMap<StockTime, Transaction> timetree = new TreeMap<StockTime, Transaction>();
			timetree.put(time, transaction);
			TreeMap<StockDate, SortedMap<StockTime, Transaction>> tree = new TreeMap<StockDate, SortedMap<StockTime, Transaction>>();
			Cache.put(papername, tree);
		}
		return true;
	}

	/**
	 * Benne van ez a kötés a Cacheben?
	 * 
	 * @param papername
	 * @param date
	 * @param time
	 * @param transaction
	 * @return
	 * @throws SQLException
	 */
	protected boolean isInCache(StockDataRecord record) throws SQLException {
		if (Cache.containsKey(record.getPapername())) {
			if (Cache.get(record.getPapername()).containsKey(record.getDate())) {
				if (Cache.get(record.getPapername()).get(record.getDate())
						.containsKey(record.getTime())) {
					if (record.Existence()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Árfolyamfigyelő értesítése
	 * 
	 * @param updatedStocks
	 *            Megváltozott kötések nevei
	 */
	protected void notifyUpdatedStock(List<String> updatedStocks) {
		for (StockDataObserver observer : this.observers) {
			observer.notify(updatedStocks);
		}
	}

	/**
	 * Megfigyelő hozzáadása
	 * 
	 * @param observer
	 */
	public void addObserver(StockDataObserver observer) {
		this.observers.add(observer);
	}

	/**
	 * get daily transaction data from the server about a certain stock.
	 * 
	 * @param ticker
	 *            Name of the stock.
	 * @param date
	 *            The date on which the transactions happened. Only the
	 *            year-month-day bit of the StockDate object is taken into
	 *            account.
	 * @return A Map holding the transactions for which transaction->date = date
	 *         and transaction->stock = ticker
	 */
	public Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> getData(
			String ticker, StockDate date) throws SQLException {
		return StockData.fetchData(ticker, date);
	}

	/**
	 * get transaction data from the server about a certain interval, about a
	 * certain ticker.
	 * 
	 * @param ticker
	 *            Names of the stock.
	 * @param from
	 *            The start date of the interval, inclusive. Only the
	 *            year-month-day bit of the StockDate object is taken into
	 *            account.
	 * @param to
	 *            The end date of the interval, inclusive. Only the
	 *            year-month-day bit of the StockDate object is taken into
	 *            account.
	 * @return A Map holding the transactions for which: from <=
	 *         transaction->datettime <= to and transaction->ticker = ticker.
	 */
	public Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> getData(
			String ticker, StockDate from, StockDate to) throws SQLException {
		return StockData.fetchData(ticker, from, to);
	}

	/**
	 * get daily transaction data from the server about a certain stock.
	 * 
	 * @param tickers
	 *            Names of the stocks.
	 * @param from
	 *            The start date of the interval, inclusive. Only the
	 *            year-month-day bit of the StockDate object is taken into
	 *            account.
	 * @param to
	 *            The end date of the interval, inclusive. Only the
	 *            year-month-day bit of the StockDate object is taken into
	 *            account.
	 * @return A Map holding the transactions for which transaction->date = date
	 *         and transaction->stock in tickers
	 */
	public Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> getData(
			String[] tickers, StockDate date) throws SQLException {
		return StockData.fetchData(tickers, date);
	}

	/**
	 * get transaction data from the server about a certain interval, about a
	 * set of tickers.
	 * 
	 * @param tickers
	 *            Names of the stocks.
	 * @param from
	 *            The start date of the interval, inclusive. Only the
	 *            year-month-day bit of the StockDate object is taken into
	 *            account.
	 * @param to
	 *            The end date of the interval, inclusive. Only the
	 *            year-month-day bit of the StockDate object is taken into
	 *            account.
	 * @return A Map holding the transactions for which: from <=
	 *         transaction->datettime <= to and transaction->ticker in tickers.
	 */
	public Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> getData(
			String[] tickers, StockDate from, StockDate to) throws SQLException {
		return StockData.fetchData(tickers, from, to);
	}

	/**
	 * get all transaction data about a specific day.
	 * 
	 * @param date
	 *            The date. Only the year-month-day bit of the StockDate object
	 *            is taken into account.
	 * @return A Map holding the transactions for which: transaction->date =
	 *         date.
	 */
	public Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> getData(
			StockDate date) throws SQLException {
		return StockData.fetchData(date);
	}

	/**
	 * get all transaction data about a specific interval
	 * 
	 * @param from
	 *            The start date of the interval, inclusive. Only the
	 *            year-month-day bit of the StockDate object is taken into
	 *            account.
	 * @param to
	 *            The end date of the interval, inclusive. Only the
	 *            year-month-day bit of the StockDate object is taken into
	 *            account.
	 * @return A Map holding the transactions for which: from <=
	 *         transaction->datettime <= to.
	 */
	public Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> getData(
			StockDate from, StockDate to) throws SQLException {
		return StockData.fetchData(from, to);
	}


}
