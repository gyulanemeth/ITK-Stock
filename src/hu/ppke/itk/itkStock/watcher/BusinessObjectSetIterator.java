package hu.ppke.itk.itkStock.watcher;

import hu.ppke.itk.itkStock.dbaccess.BusinessObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Ez egy wrapper class a java.sql.ResultSet-re. A kliensek könnyen végig tudnak iterálni egy
 * lekérdezés eredményén, és közvetlenül a BusinessObject-el tudnak foglalkozni.
 * 
 * @param <T> A saját BusinessObject leszármazottad, amire az iterátort használod.
 */
public abstract class BusinessObjectSetIterator<T extends BusinessObject> implements Iterator<BusinessObject> {

	protected ResultSet rs;

	protected BusinessObjectSetIterator(ResultSet rs) {
		this.rs = rs;
	}

	@Override
	public boolean hasNext() {
		try {
			return !rs.isAfterLast();
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public abstract T next();

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
