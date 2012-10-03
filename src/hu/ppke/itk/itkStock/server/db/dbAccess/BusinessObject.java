package hu.ppke.itk.itkStock.server.db.dbAccess;

import java.sql.SQLException;

/**
 * Ez az osztaly az alaposztalya az osszes adatbazisbol lekerdezheto beepitett objektumnak.
 * Minden peldanya egy aktiv objektum, mely a neki megfelelo 'Manager' osztalyon keresztul kapcsolodik az adatbazishoz,
 * es tartja magat szinkronban vele.
 * Minden 'BusinessObject'-et elsosorban az 'id' (azonosito szam) mezo azonosit.
 * @see AbstractManager
 * @see DatabaseConnector
 * @see #id
 */

public abstract class BusinessObject
{
	protected AbstractManager<? extends BusinessObject> manager = null;
	protected boolean identified = false;
	protected int id;
	
	public BusinessObject( AbstractManager<? extends BusinessObject> manager, int id )
	{
		this.manager = manager;
		this.id = id;
	}
	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.identified = false;
		this.id = id;
	}
	
	public boolean isSynchronizedWithDatabase()
	{
		return this.identified;
	}
	
	/**
	 * Lekerdezi az adatbazisbol az aktualis objektumot a rendelkezesre allo azonositok alapjan. 
	 */
	public abstract boolean get() throws SQLException, BusinessObjectException;
	/**
	 * "Szinkronizalja" az adatbazissal az aktualis objektumot. Feltolti az adatokat. 
	 */
	public abstract void update() throws SQLException, BusinessObjectException;
	/**
	 * Uj bejegyzest hoz letre az adatbazisban az aktualis objektumban talalhato adatokkal.
	 */
	public abstract void create() throws SQLException, BusinessObjectException;
	

}
