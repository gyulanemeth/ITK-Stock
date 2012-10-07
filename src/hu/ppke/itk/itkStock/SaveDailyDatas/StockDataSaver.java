package hu.ppke.itk.itkStock.SaveDailyDatas;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import hu.ppke.itk.itkStock.server.db.dbAccess.AbstractManager;
import hu.ppke.itk.itkStock.server.db.dbAccess.BusinessObjectException;
import hu.ppke.itk.itkStock.server.db.dbAccess.DatabaseConnector;

/**
 * Kötés adabázisban történő rögzítéséért felelős Manager osztály
 * @author ki-csen
 *
 */
public class StockDataSaver extends AbstractManager<StockDataRecord> {
	private PreparedStatement addRecord = null;
	private PreparedStatement checkRecordExistence = null;

	public StockDataSaver(DatabaseConnector dbConnector) throws SQLException {
		super(dbConnector);

		if (this.dbConnector == null || !this.dbConnector.isInitialized())
			throw new SQLException("DatabaseConnector is not initialized.");

		this.addRecord = this.dbConnector
				.prepareStatement("INSERT INTO StockData ( papername, date, time, close, volume) VALUES ( ?, ?, ?, ?, ? )");
		this.checkRecordExistence = this.dbConnector
				.prepareStatement("SELECT IF( ( SELECT COUNT( * ) FROM StockData WHERE papername = ? AND date = ? AND time = ? AND close = ? AND volume = ? ) = 0, FALSE, TRUE )");
	}
	
	public void addRecord(String papername, String date, String time, String close,
			String volume) throws SQLException
	{
		this.addRecord.setString(1, papername);
		this.addRecord.setString(2, date);
		this.addRecord.setString(3, time);
		this.addRecord.setString(4, close);
		this.addRecord.setString(5, volume);
		this.addRecord.executeUpdate();
	}
	
	public boolean checkRecordExistence(String papername, String date, String time, String close,
			String volume) throws SQLException
	{
		this.checkRecordExistence.setString(1, papername);
		this.checkRecordExistence.setString(2, date);
		this.checkRecordExistence.setString(3, time);
		this.checkRecordExistence.setString(4, close);
		this.checkRecordExistence.setString(5, volume);
		this.resultSet = this.checkRecordExistence.executeQuery();
		this.resultSet.first();
		
		return this.resultSet.getBoolean(1); 
	}

	@Override
	public void update(StockDataRecord bo) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StockDataRecord get(int id) throws SQLException,
			BusinessObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(StockDataRecord bo) throws SQLException, BusinessObjectException {
		//System.out.println(bo.getPapername().toString()+";"+bo.getDate().toString()+";"+bo.getTime().toString()+";"+Double.toString(bo.getClose())+";"+Double.toString(bo.getVolume()));
		/*if (this.checkRecordExistence(bo.getPapername().toString(), bo.getDate().toString(), bo.getTime().toString(), Double.toString(bo.getClose()), Double.toString(bo.getVolume())) )
			throw new BusinessObjectException("This record is already exists.");*/
		this.addRecord( bo.getPapername().toString(), bo.getDate().toString(), bo.getTime().toString(), Double.toString(bo.getClose()), Double.toString(bo.getVolume()) );
		bo.get();
		
	}

}
