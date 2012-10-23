package hu.ppke.itk.itkStock.server.db.stockWatcher;

import hu.ppke.itk.itkStock.SaveDailyDatas.StockDataManager;
import hu.ppke.itk.itkStock.SaveDailyDatas.StockDataObserver;
import hu.ppke.itk.itkStock.nio.core.AbstractWorker;
import hu.ppke.itk.itkStock.nio.core.NioServer;
import hu.ppke.itk.itkStock.nio.protocol.NotifyWatcherClientCommand;
import hu.ppke.itk.itkStock.nio.protocol.ProtocolWorker;
import hu.ppke.itk.itkStock.nio.protocol.RegisterWatcherCommand;
import hu.ppke.itk.itkStock.nio.protocol.UnregisterWatcherCommand;
import hu.ppke.itk.itkStock.server.db.dbAccess.DatabaseConnector;

import java.sql.SQLException;
import java.util.List;

/**
 * Actually this class is handling the observing of stocks. When the
 * StockDataManager notifies this object about a price change, this server
 * notifies all clients about that.
 * 
 * @see StockDataManager
 */
public class WatcherHandler implements StockDataObserver {

	DatabaseConnector dc = new DatabaseConnector();
	WatcherManager wm;

	AbstractWorker protocolWorker = new ProtocolWorker();
	int serverPort = 9090;

	/**
	 * Constructs a new {@code WatcherClient} object.
	 * 
	 * @throws ClassNotFoundException when the database initialization fails.
	 * @throws SQLException the database management initialization fails.
	 */
	public WatcherHandler() throws ClassNotFoundException, SQLException {
		dc.initConnection();
		wm = new WatcherManager(dc);
	}

	/**
	 * Initializes the server, starts the server thread, adds protocols.
	 */
	public void init() {

		try {
			dc.initConnection();
			wm = new WatcherManager(dc);
			wm.clear();

			((ProtocolWorker) protocolWorker).addProtocolCommandWorker(
					(short) 201, new RegisterWatcherCommand());
			((ProtocolWorker) protocolWorker).addProtocolCommandWorker(
					(short) 203, new UnregisterWatcherCommand());
			((ProtocolWorker) protocolWorker).addProtocolCommandWorker(
					(short) 206, new NotifyWatcherClientCommand());
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
		// / TODO: amikor elkészült, átírni Java kódra

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

	/**
	 * Notifies the client about the price change.
	 * 
	 * @param userId the user who should be informed.
	 * @param stockPrice the new price.
	 */
	private void notifyUser(int userId, int stockPrice) {
		// TODO: nio szerver hogyan értesíti a klienst?
	}
	
	/**
	 * Handles the client's register request.
	 * @throws SQLException whenever some sort of database error occurs. 
	 */
	private void respondToRegister(String paperName, int boundType, double boundValue, int userId) throws SQLException {
		// TODO: nio szerver hogyan válaszol a kliens kérésére?
		wm.addWatcher(userId, paperName, boundValue, boundType);
		
	}
	
	/**
	 * Handles the client's unregister request.
	 * @throws SQLException whenever some sort of database error occurs.
	 */
	private void respondToUnregister(int userId, String paperName, int boundType) throws SQLException {
		// TODO: nio szerver hogyan válaszol a kliens kérésére?
		wm.removeWatcherByUserIdStockType(userId, paperName, boundType);
	}

}
