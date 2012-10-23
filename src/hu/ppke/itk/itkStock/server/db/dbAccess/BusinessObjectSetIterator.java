package hu.ppke.itk.itkStock.server.db.dbAccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * This is a wrapper class for the java.sql.ResultSet class. The clients can
 * iterate over the set, and get directly a BusinessObjectSet object.
 * 
 * @param <T>
 *            A class extending the BusinessObject class, for you are using this
 *            iterator.
 */
public abstract class BusinessObjectSetIterator<T extends BusinessObject>
		implements Iterator<BusinessObject> {

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
