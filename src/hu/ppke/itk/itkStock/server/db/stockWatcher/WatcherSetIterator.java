package hu.ppke.itk.itkStock.server.db.stockWatcher;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Ezzel az iter�torral Watcher tupleket tartalmaz� ResultSeten lehet v�gigmenni.
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
