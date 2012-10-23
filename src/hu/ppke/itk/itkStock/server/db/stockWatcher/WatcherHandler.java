package hu.ppke.itk.itkStock.server.db.stockWatcher;

import java.sql.SQLException;
import java.util.List;

import hu.ppke.itk.itkStock.SaveDailyDatas.StockDataObserver;
import hu.ppke.itk.itkStock.nio.core.AbstractWorker;
import hu.ppke.itk.itkStock.nio.core.NioServer;
import hu.ppke.itk.itkStock.nio.protocol.ProtocolWorker;
import hu.ppke.itk.itkStock.nio.protocol.ServersideAuthenticationProtocolCommandWorker;
import hu.ppke.itk.itkStock.server.db.dbAccess.DatabaseConnector;

/**
 * Actually this class is handling the observing of stocks. For now, it iterates
 * over every watcher at stated intervals, and warns the user if the price
 * stepped over the bound.
 */
public class WatcherHandler implements StockDataObserver {

	DatabaseConnector dc = new DatabaseConnector();
	WatcherManager wm;
	
	AbstractWorker protocolWorker = new ProtocolWorker();
	int serverPort = 9090;
	
	public WatcherHandler() throws ClassNotFoundException, SQLException {
		dc.initConnection();
		wm = new WatcherManager(dc);
	}

	public void init() {

		try {
			dc.initConnection();
			wm = new WatcherManager(dc);
			wm.clear();
			
			((ProtocolWorker) protocolWorker).addProtocolCommandWorker((short) 201,
					new ServersideAuthenticationProtocolCommandWorker());
			((ProtocolWorker) protocolWorker).addProtocolCommandWorker((short) 203,
					new ServersideAuthenticationProtocolCommandWorker());
			new Thread(protocolWorker).start();
			new Thread(new NioServer(null, serverPort, protocolWorker)).start();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void notify(List<String> updatedStocks) {
		// az aktuális árfolyamokat tartalmazó cache még nincs implementálva,
		// ezért sok kódot még ki kellett kommentelnem
		
		
//		try {
//			while(/* ide kell vmi feltétel, amíg az id osztály frissíti az árfolyamokat*/)
//				getClass().wait(); // itt megvárjuk, amíg a másik osztály be nem
								// fejezi az aktuális árfolyamok cachelését
//		} catch (InterruptedException e) {
//		}

		for (String stock : updatedStocks) {
			
			// HashMap<string, double> prices = StockId.getPrices();
			int stockPrice = 0; // = prices.get(stock);

			try {

				WatcherSetIterator watchersByStock = wm
						.getWatchersByStock(stock);

				while (watchersByStock.hasNext()) {
					Watcher next = watchersByStock.next();
					int boundType = next.getBoundType();
					double boundValue = next.getBoundValue();
					int userId = next.getUserId();

					if (boundType * stockPrice < boundValue * boundType)
						notifyUser(userId, stockPrice);
				}

			} catch (SQLException e) {
			}

		}
	}

	private void notifyUser(int userId, int stockPrice) {
		// TODO Auto-generated method stub

	}

}
