package hu.ppke.itk.itkStock.SaveDailyDatas;

import hu.ppke.itk.itkStock.dbaccess.AbstractManager;
import hu.ppke.itk.itkStock.dbaccess.BusinessObject;
import hu.ppke.itk.itkStock.dbaccess.BusinessObjectException;
import hu.ppke.itk.itkStock.stockdata.StockDate;
import hu.ppke.itk.itkStock.stockdata.StockTime;
import hu.ppke.itk.itkStock.stockdata.Transaction;

import java.sql.SQLException;

/**
 * Kötést reprezentáló osztály
 * 
 * @author ki-csen
 * 
 */
public class StockDataRecord extends BusinessObject implements
		Comparable<StockDataRecord> {

	private String papername;
	private StockDate date;
	private StockTime time;
	private Transaction transaction;

	public StockDataRecord(StockDataSaver manager, int id, String papername,
			StockDate date, StockTime time, Transaction transaction) {
		super(manager, id);
		this.papername = papername;
		this.date = date;
		this.time = time;
		this.transaction = transaction;
	}

	/*
	 * Mivel a kötésnek nincs azonosítója így ennek a metódusnak nincs értelme
	 * (non-Javadoc)
	 * 
	 * @see hu.ppke.itk.itkStock.dbaccess.BusinessObject#get()
	 */
	@Override
	public boolean get() throws SQLException, BusinessObjectException {
		return false;
	}

	/*
	 * Mivel a kötés adatai nem változnak, így ezt a metódust nem kell meghívni.
	 * (non-Javadoc)
	 * 
	 * @see hu.ppke.itk.itkStock.dbaccess.BusinessObject#update()
	 */
	@Override
	public void update() throws SQLException, BusinessObjectException {
		throw new BusinessObjectException(
				"Invalid function. You can't update StockData.");
	}

	/*
	 * Kötést rögzíti az adatbázisban, ha az adatbázis azt még nem tartalmazza
	 * (non-Javadoc)
	 * 
	 * @see hu.ppke.itk.itkStock.dbaccess.BusinessObject#create()
	 */
	@Override
	public void create() throws SQLException, BusinessObjectException {
		if (this.identified)
			throw new BusinessObjectException(
					"Identified object should not be created.");
		// System.out.println(papername+" "+date.toString()+" "+time.toString()+" "+transaction.toString());
		((AbstractManager) this.manager).create(this);

	}

	/**
	 * @return Megtalálható-e már ez a kötés az adatbázisba?
	 * @throws SQLException
	 */
	public boolean Existence() throws SQLException {
		return ((StockDataSaver) this.manager).checkRecordExistence(papername,
				date.toString(), time.toString(), Double.toString(getClose()),
				Double.toString(getVolume()));
	}

	public String getPapername() {
		return papername;
	}

	public StockDate getDate() {
		return date;
	}

	public StockTime getTime() {
		return time;
	}

	public double getClose() {
		return this.transaction.getPrice();
	}

	public double getVolume() {
		return this.transaction.getVolume();
	}

	public Transaction getTransaction() {
		return transaction;
	}

	@Override
	public int compareTo(StockDataRecord arg0) {
		int compdate = this.date.compareTo(arg0.getDate());
		if (compdate != 0) {
			return -compdate;
		}

		return -this.time.compareTo(arg0.getTime());
	}

	@Override
	public boolean equals(Object aThat) {
		if (this == aThat)
			return true;
		if (!(aThat instanceof StockDataRecord))
			return false;
		StockDataRecord that = (StockDataRecord) aThat;
		return that.getPapername().contentEquals(papername)
				&& that.getDate().compareTo(date) == 0
				&& that.getTime().compareTo(time) == 0
				&& that.getClose() == this.transaction.getPrice()
				&& that.getVolume() == this.transaction.getVolume();
	}
}
