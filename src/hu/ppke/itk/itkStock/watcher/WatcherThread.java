package hu.ppke.itk.itkStock.watcher;

import hu.ppke.itk.itkStock.dbaccess.DatabaseConnector;

/**
 * Konkrétan az árfolyamok figyelését megvalósító osztály. Egyelõre minden user
 * minden figyelõjén végigmegy, és ha túllépést talál, jelez.
 */
public class WatcherThread extends Thread {

	// 2.3 -ra vár
	// Collection stocksInMemory;
	// Collection UsersInMemory;
	DatabaseConnector dc = new DatabaseConnector();
	WatcherManager wm;

	// StockManager sm;

	@Override
	public void run() {

		try {
			dc.initConnection();
			wm = new WatcherManager(dc);
			// sm = new StockManager(dc);

			while (!interrupted()) {

//				for (User u : UsersInMemory) {
//					WatcherSetIterator wsi = wm.getWatchersByUser(u.username);
//
//					while (wsi.hasNext()) {
//						Watcher w = wsi.next();
//						Stock s = sm.getStockByName(w.getStockName());
//
//						if (w.getBoundType() == Watcher.BoundTypes.UPPER_BOUND) {
//							if (w.getBoundType() < s.getPrice())
//								notifyUser();
//						} else {
//							if (w.getBoundType() > s.getPrice())
//								notifyUser();
//						}
//					}
//				}

				this.wait();
			}

			dc.closeConnection();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
