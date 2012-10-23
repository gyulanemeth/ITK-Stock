package hu.ppke.itk.itkStock.client.watcherClient;

import hu.ppke.itk.itkStock.nio.core.NioClient;
import hu.ppke.itk.itkStock.nio.core.RspHandler;
import hu.ppke.itk.itkStock.nio.protocol.NotifyWatcherClientCommand;
import hu.ppke.itk.itkStock.nio.protocol.RegisterWatcherCommand;
import hu.ppke.itk.itkStock.nio.protocol.UnregisterWatcherCommand;
import hu.ppke.itk.itkStock.server.db.stockWatcher.Watcher;
import hu.ppke.itk.itkStock.server.db.stockWatcher.Watcher.BoundTypes;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

/**
 * This class is representing a user who is working width {@link Watcher}s.
 */
public class WatcherClient extends Observable implements Runnable {

	private static final int serverPort = 9090;
	private static InetAddress serverArrd;
	private int userId;
	private int passw;

	private NioClient client;
	private RspHandler handler;

	private ArrayList<ClientsideWatcher> watchers = new ArrayList<ClientsideWatcher>();
	private HashMap<String, Double> lastPrices = new HashMap<String, Double>();

	/**
	 * Constructs a new object of this class width the given user id and
	 * password.
	 * 
	 * @param userId
	 *            the given user id.
	 * @param passw
	 *            the given password.
	 * @throws IOException
	 *             whenever some sort of I/O error occurs.
	 */
	public WatcherClient(int userId, String passw) throws IOException {
		this.userId = userId;
		this.passw = passw.hashCode();

		serverArrd = InetAddress.getByName("localhost");

		client = new NioClient(serverArrd, serverPort);
		Thread t = new Thread(client);
		t.setDaemon(true);
		t.start();

		handler = new RspHandler();
		handler.addProtocolCommandWorker((short) 201,
				new RegisterWatcherCommand());
		handler.addProtocolCommandWorker((short) 203,
				new UnregisterWatcherCommand());
		handler.addProtocolCommandWorker((short) 206,
				new NotifyWatcherClientCommand());
	}

	/**
	 * Registers a new {@link Watcher} for a given stock.
	 * 
	 * @param paperName
	 *            the given stock's name.
	 * @param boundValue
	 *            the given bound value.
	 * @param boundType
	 *            the type of the bound.
	 * @return <b>true</b> if the operation is successful, <b>false</b> otherwise.
	 * 
	 * @see BoundTypes
	 */
	public boolean addWatcher(String paperName, double boundValue, int boundType) {

		ClientsideWatcher cw = new ClientsideWatcher(paperName, boundValue,
				boundType, userId);
		byte[] bytes = SerializationTools.objectToBytes(cw);

		try {
			client.send(bytes, handler);
			watchers.add(cw);
		} catch (IOException e) {
			return false;
		}

		handler.waitForResponse();

		// TODO: hogyan kapom meg a választ?
		return true;
	}

	/**
	 * Removes the given watcher, so that is no more observing the stock's price
	 * change.
	 * 
	 * @param cw
	 *            the watcher which should be removed.
	 * @return <b>true</b> if the operation is successful, <b>false</b> otherwise.
	 */
	public boolean removeWatcher(ClientsideWatcher cw) {

		byte[] bytes = SerializationTools.objectToBytes(cw);

		try {
			client.send(bytes, handler);
			watchers.remove(cw);
		} catch (IOException e) {
			return false;
		}
		handler.waitForResponse();

		// TODO: hogyan kapom meg a választ?
		return true;
	}

	@Override
	public void run() {
		// TODO: nio szerver hogyan tudja értesíteni a klienst?

		// ha a szerver szól
		// lastPrices.put(paperName, price);
		notifyObservers();

	}

	/**
	 * Retrieves the list of the active watchers.
	 * 
	 * @return the list of the watchers.
	 */
	public ArrayList<ClientsideWatcher> getWatchers() {
		return watchers;
	}

	/**
	 * Retrieves the last prices which were sent by the server.
	 * 
	 * @return the list of prices.
	 */
	public HashMap<String, Double> getLastPrices() {
		return lastPrices;
	}

}
