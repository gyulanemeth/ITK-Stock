package hu.ppke.itk.itkStock.server.db.stockWatcher;

import hu.ppke.itk.itkStock.server.db.dbAccess.BusinessObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Ez egy wrapper class a java.sql.ResultSet-re. A kliensek k�nnyen v�gig tudnak iter�lni egy
 * lek�rdez�s eredm�ny�n, �s k�zvetlen�l a BusinessObject-el tudnak foglalkozni.
 * 
 * @param <T> A saj�t BusinessObject lesz�rmazottad, amire az iter�tort haszn�lod.
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
