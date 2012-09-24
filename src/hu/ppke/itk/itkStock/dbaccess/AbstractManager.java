package hu.ppke.itk.itkStock.dbaccess;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Ez az osztaly az ose az osszes 'BusinessObject'-et kezelo 'Manager' osztalynak.
 * Feladata osszekapcsolni a 'BusinessObject' peldanyokat az adatbazissal, megvalositani az SQL eljarasokat, melyekkel lekerdezhetjuk, modosithatjuk oket.
 * @see BusinessObject
 * @see PreparedStatement
 */

public abstract class AbstractManager<T extends BusinessObject>
{
	protected DatabaseConnector dbConnector;
	protected volatile ResultSet _resultSet = null;
	
	public AbstractManager(DatabaseConnector dbConnector)
	{
		this.dbConnector = dbConnector;
	}
	
	/**
	 * Frissiti az adatbazis-beli adatokat a bo (BusinessObject) objektumban talalt informaciok alapjan.
	 * 
	 * @param bo	Az objektum, mely adatait "szinkronizaljuk" az adatbazissal.
	 */
	public abstract void update(T bo) throws SQLException;
	/**
	 * Letrehozza az adatbazis-beli adatok alapjan az aktualis 'BusinesObject'-et.
	 * 
	 * @param id	Az azonosito, mely alapjan elerhetjuk az adatbazisban a meglevo objektumot. Bizonoyos esetekben letrehozhatunk hasonlo nevu fuggvenyt
	 * 				leszarmazott osztalyokba mas elvart parameterrel.
	 */
	public abstract T get(int id) throws SQLException, BusinessObjectException;
	/**
	 * A parameterkent kapott 'BusinessObject'-ben talalt adatok alapjan letrehoz az adatbazisban egy uj bejegyzest. Ha letezik ilyen adat, akkor kivetelt dob.
	 * 
	 * @param bo	A letrehozando 'BusinessObject'.
	 */
	public abstract void create(T bo) throws SQLException, BusinessObjectException;
}
