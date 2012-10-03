package hu.ppke.itk.itkStock.SaveDailyDatas;

import hu.ppke.itk.itkStock.server.db.dbAccess.DatabaseConnector;
import hu.ppke.itk.itkStock.server.db.historicData.StockDate;
import hu.ppke.itk.itkStock.server.db.historicData.StockTime;
import hu.ppke.itk.itkStock.server.db.historicData.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.sun.tools.javac.util.Pair;

/**
 * Kötés manager osztály
 * Ez felel a kötések adatbázisban rögzítésélrt és a Catche kezeléséért is.
 * Innen történő lekérdezés először megnézi, hogy a memóriából oda tudja-e adni a kért adatot és csak akkor fordul az adatbázishoz, ha ez nem lehetséges.
 * @author ki-csen
 *
 */
public class StockDataManager {
	protected Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction> > > Catche = new HashMap<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>>();
	protected Pair<Date, Date> s_interval; /// Catchelt intervallum
	protected StockDataSaver Sdatasaver = null; ///dbManager
	protected List<StockDataRecord> datalist = new ArrayList<StockDataRecord>(); ///átmeneti tár a beolvasáshoz
	
	/**
	 * StockDate+StockTime To Date
	 * @param date StockDate
	 * @param time StockTime
	 * @return date + time [Date]
	 */
	@SuppressWarnings("deprecation")
	public static Date StockDatetimeToDate(StockDate date, StockTime time){
		//Depricated de nem tudok vele mit tenni. Nem én implementáltam a használt típusokat.
		return new Date(date.getYear(), date.getMonth(), date.getDay(), time.getHour(), time.getMinute(), time.getSecond());
	}
	
	/**
	 * @param dbc dbConnector ezt kívülről kapja, hogy a közöset használja. Nem figyel a bezárásra. Idő elötti bezárása —> SQLExc
	 * @throws SQLException
	 */
	public StockDataManager(DatabaseConnector dbc) throws SQLException {
		super();
		Sdatasaver = new StockDataSaver(dbc);
	}
	
	/**
	 * Beolvasott adatok intervallumának megállapítása, adatok rögzítése és Catchelése
	 * Átmeneti tár törlése
	 * @return siker
	 * @throws ImplementationLogicException
	 */
	synchronized public boolean comit() throws ImplementationLogicException{
		List<String> updatedStock = new ArrayList<String>();
		Date lastdate = StockDatetimeToDate(datalist.get(0).getDate(),datalist.get(0).getTime());
		Date firstdate = null;
		for (StockDataRecord record : datalist) {
			if(!add(record)){
				firstdate = StockDatetimeToDate(record.getDate(),record.getTime());
				break;
			}else{
				if(!updatedStock.contains(record.getPapername())){
					updatedStock.add(record.getPapername());
				}
			}
		}
		refresh_intervals(firstdate,lastdate);
		notifyUpdatedStock(updatedStock);
		this.datalist = new ArrayList<StockDataRecord>();
		return true;
	}
	
	/**
	 * Beolvasott kötés rögzítése az átmeneti tárban
	 * @param papername
	 * @param date
	 * @param time
	 * @param transaction
	 */
	synchronized public void add(String papername, StockDate date, StockTime time, Transaction transaction){
		datalist.add(new StockDataRecord(Sdatasaver, 0, papername, date, time, transaction));
	}
	

	/**
	 * Kőtás rögzítése az adatbázisban és a Catcheben
	 * @param record Kötés
	 * @return siker
	 * @throws ImplementationLogicException
	 */
	protected boolean add(StockDataRecord record) throws ImplementationLogicException{
		if(isInCatche(record.getPapername(), record.getDate(), record.getTime(), record.getTransaction())){
			return false;
		}
		
		try {
			record.create();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		if (!pushToCatche(record.getPapername(), record.getDate(), record.getTime(), record.getTransaction())){
			throw new ImplementationLogicException("Same transaction in same time!");
		}
		
		return true;
	}
	
	/**
	 * Catchelt kötések intervallumának frissítése
	 * @param start hozzáadott intervallum kezdete
	 * @param stop hozzáadott intervallum vége
	 */
	protected void refresh_intervals(Date start, Date stop){
		if(this.s_interval == null){
			this.s_interval = new Pair<Date, Date>(start, stop);
		}else if(this.s_interval.snd.equals(start)){
			Date fstart = this.s_interval.fst;
			this.s_interval = new Pair<Date, Date>(fstart, stop);
		}else{
			///Nincs implementálva az összes eset
			this.s_interval=new Pair<Date, Date>(start, stop);
		}
	}
	
	/**
	 * Kötés hozzáadása a Catchehez
	 * @param papername
	 * @param date
	 * @param time
	 * @param transaction
	 * @return
	 */
	protected boolean pushToCatche(String papername, StockDate date, StockTime time, Transaction transaction) {
		if(Catche.containsKey(papername)){
			if(Catche.get(papername).containsKey(date)){
				if(!Catche.get(papername).get(date).containsKey(time)){
					/// Feltételezzük, hogy egy időben nincs két azonos kötés - Ez biztosan rosz feltételezés!!!!!!
					Catche.get(papername).get(date).put(time, transaction);
				}else{
					return false;
				}
			}else{
				TreeMap<StockTime, Transaction> timetree = new TreeMap<StockTime, Transaction>();
				timetree.put(time, transaction);
				Catche.get(papername).put(date, timetree);
			}
		}else{
			TreeMap<StockTime, Transaction> timetree = new TreeMap<StockTime, Transaction>();
			timetree.put(time, transaction);
			TreeMap<StockDate, SortedMap<StockTime, Transaction> > tree = new TreeMap<StockDate, SortedMap<StockTime, Transaction> >();
			Catche.put(papername, tree);
		}
		return true;
	}
	
	
	/**
	 * Benne van ez a kötés a Catcheben?
	 * @param papername
	 * @param date
	 * @param time
	 * @param transaction
	 * @return
	 */
	protected boolean isInCatche(String papername, StockDate date, StockTime time, Transaction transaction) {
		if(Catche.containsKey(papername)){
			if(Catche.get(papername).containsKey(date)){
				if(Catche.get(papername).get(date).containsKey(time)){
					if(Catche.get(papername).get(date).get(time).equals(transaction)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**Árfolyamfigyelő értesítése
	 * @param updatedStocks Megváltozott kötések nevei
	 */
	protected void  notifyUpdatedStock(List<String> updatedStocks){
		//Nincs még implementálva
	}

}
