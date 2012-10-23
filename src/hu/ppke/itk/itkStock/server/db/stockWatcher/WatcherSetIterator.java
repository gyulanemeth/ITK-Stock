package hu.ppke.itk.itkStock.server.db.stockWatcher;

import hu.ppke.itk.itkStock.server.db.dbAccess.BusinessObjectSetIterator;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This can iterate over a result set of watchers.
 */
public class WatcherSetIterator extends BusinessObjectSetIterator<Watcher> {

	protected WatcherSetIterator(ResultSet rs) {
		super(rs);
	}

	@Override
	public Watcher next() {
		try {
			rs.next();
			return new WatcherManager(rs).getWatcherFromSet();
		} catch (SQLException e) {
			return null;
		}
	}
}
