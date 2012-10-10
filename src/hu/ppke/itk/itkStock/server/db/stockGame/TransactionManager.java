package hu.ppke.itk.itkStock.server.db.stockGame;

import hu.ppke.itk.itkStock.server.db.dbAccess.AbstractManager;
import hu.ppke.itk.itkStock.server.db.dbAccess.BusinessObjectException;
import hu.ppke.itk.itkStock.server.db.dbAccess.DatabaseConnector;
import hu.ppke.itk.itkStock.server.db.user.User;
import hu.ppke.itk.itkStock.server.db.user.UserManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionManager extends AbstractManager<Transaction> {
	
	private UserManager userManager ;
	//private StockManager stockManager ;
	
	private double fixTransactionCost ;
	private double percentTransactionCost ;
	private PreparedStatement addTransaction;
	
	/**
	 * Végrehajtunk egy tranzakciót
	 * @param userid
	 * @param paperName
	 * @param volume 		Mennyiség: pozitív - vétel, negatív - eladás
	 * @throws SQLException 
	 * @throws BusinessObjectException 
	 */
	public void makeTransaction(int userid, String paperName, int volume) throws SQLException, BusinessObjectException
	{
		// User adatok validálása
		if (!userManager.checkUserExistenceById(userid))
		{
			// Invalid userid
			throw new BusinessObjectException("Invalid user id") ;
		}

		// TODO: élő árfolyam lekérdezése (másik feladatra vár)
		// double price = getPrice(paperName) ;
		double price = 150 ;
		
		// Ennyivel fog változni a user számláján az összeg
		double sum = - volume * price ;
		
		// Jutalékok levonása
		sum -= fixTransactionCost + sum * percentTransactionCost ;

		// Adatbázisban a változtatások elkönyvelése
		
		// TODO: Adatbázis tranzakció indítása - ezeket nem választhatjuk külön, különben 
		// egy adatbázishiba esetén eltűnik/megjelenik a pénz
		// DatabaseConnector.startTransaction() ;
		
		// Tranzakció bejegyzése
		// TODO
		
		// Usertől pénzösszeg levonása
		User user = userManager.get(userid) ;
		// TODO: Pénzösszeg állítása
		// user.setMoney(user.getMoney()+sum) ;
		
		// TODO: Adatbázis tranzakció lezárása
		// DatabaseConnector.endTransaction() ;		
	}
	
	/**
	 * Adott mennyiségű értékpapír eladása
	 * 
	 * @param userid
	 * @param paperName
	 * @param volume
	 * @throws SQLException
	 * @throws BusinessObjectException
	 */
	public void sell(int userid, String paperName, int volume) throws SQLException, BusinessObjectException
	{
		makeTransaction(userid, paperName, -volume) ;
	}
	
	/**
	 * Adott mennyiségű értékpapír vásárlása
	 * 
	 * @param userid
	 * @param paperName
	 * @param volume
	 * @throws SQLException
	 * @throws BusinessObjectException
	 */
	public void buy(int userid, String paperName, int volume) throws SQLException, BusinessObjectException
	{
		makeTransaction(userid, paperName, volume) ;
	}
	
	/**
	 * A fix tranzakciós költség lekérdezése
	 * @return
	 */
	public double getFixTransactionCost() {
		return fixTransactionCost;
	}

	/**
	 * A fix tranzakciós költség beállítása
	 * @param fixTransactionCost
	 */
	public void setFixTransactionCost(double fixTransactionCost) {
		this.fixTransactionCost = fixTransactionCost;
	}

	/**
	 * A százalékos tranzakciós költség lekérdezése
	 * @return
	 */
	public double getPercentTransactionCost() {
		return percentTransactionCost;
	}

	/**
	 * A százalékos tranzakciós költség beállítása
	 * @param percentTransactionCost
	 */
	public void setPercentTransactionCost(double percentTransactionCost) {
		this.percentTransactionCost = percentTransactionCost;
	}

	/**
	 * A tranzakciómenedzserünk konstruktora, szüksége van a user managerre
	 * @param userManager
	 * @throws SQLException 
	 */
	public TransactionManager(DatabaseConnector dbConnector) throws SQLException
	{
		super(dbConnector) ;
		userManager = new UserManager(dbConnector) ;
		// stockManager = new StockManager(dbConnector) ;
		
		// TODO: Jutalékok adatbázisban tárolása
		fixTransactionCost = 10 ;
		percentTransactionCost = 0.01 ;

		addTransaction = this.dbConnector.prepareStatement("INSERT INTO StockHistory VALUES (?, ?, ?, ?, ?, ?);") ;
	}

	@Override
	public void update(Transaction bo) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Transaction get(int id) throws SQLException, BusinessObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(Transaction bo) throws SQLException,
			BusinessObjectException {
		// TODO Auto-generated method stub
		
	}
}
