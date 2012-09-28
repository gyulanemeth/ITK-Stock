package hu.ppke.itk.itkStock.watcher;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Ezzel az iterátorral Watcher tupleket tartalmazó ResultSeten lehet végigmenni.
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
