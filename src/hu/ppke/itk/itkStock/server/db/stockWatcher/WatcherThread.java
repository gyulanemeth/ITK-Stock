package hu.ppke.itk.itkStock.server.db.stockWatcher;

import hu.ppke.itk.itkStock.server.db.dbAccess.DatabaseConnector;

/**
 * Konkr�tan az �rfolyamok figyel�s�t megval�s�t� oszt�ly. Egyel�re minden user
 * minden figyel�j�n v�gigmegy, �s ha t�ll�p�st tal�l, jelez.
 */
public class WatcherThread extends Thread {

	// 2.3 -ra v�r
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
