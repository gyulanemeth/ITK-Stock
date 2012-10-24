package hu.ppke.itk.itkStock.client.watcherClient;

import hu.ppke.itk.itkStock.nio.core.NioClient;
import hu.ppke.itk.itkStock.nio.core.RspHandler;
import hu.ppke.itk.itkStock.nio.protocol.ProtocolTools;
import hu.ppke.itk.itkStock.nio.protocol.RegisterWatcherCommand;
import hu.ppke.itk.itkStock.nio.protocol.UnregisterWatcherCommand;
import hu.ppke.itk.itkStock.server.db.stockWatcher.Watcher;
import hu.ppke.itk.itkStock.server.db.stockWatcher.Watcher.BoundTypes;

import java.io.ByteArrayOutputStream;
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

	RegisterWatcherCommand rwc = new RegisterWatcherCommand();
	UnregisterWatcherCommand uwc = new UnregisterWatcherCommand();

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

		handler.addProtocolCommandWorker(ProtocolTools.registerWatcher, rwc);
		handler.addProtocolCommandWorker(ProtocolTools.unregisterWatcher, uwc);
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
	 * @return the response message sent by the server.
	 * 
	 * @see BoundTypes
	 */
	public String addWatcher(String paperName, double boundValue, int boundType) {

		ClientsideWatcher cw = new ClientsideWatcher(paperName, boundValue,
				boundType, userId);
		byte[] bytes = SerializationTools.objectToBytes(cw);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		

		try {
			byteStream.write(ProtocolTools.shortToBytes(ProtocolTools.registerWatcher));
			byteStream.write(bytes);
			client.send(bytes, handler);
			
		} catch (IOException e) {
			return null;
		}

		handler.waitForResponse();
		
		if(rwc.getServerMessage().contains("success"))
			watchers.add(cw);
		
		return rwc.getServerMessage();
	}

	/**
	 * 
	 * Removes the given watcher, so that is no more observing the stock's price
	 * change.
	 * 
	 * @param cw the watcher which should be removed.
	 * @return the response message sent by the server.
	 */
	public String removeWatcher(ClientsideWatcher cw) {

		byte[] bytes = SerializationTools.objectToBytes(cw);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

		try {
			byteStream.write(ProtocolTools.shortToBytes(ProtocolTools.unregisterWatcher));
			byteStream.write(bytes);
			client.send(bytes, handler);
			
		} catch (IOException e) {
			return null;
		}
		handler.waitForResponse();
		
		if(uwc.getServerMessage().contains("success"))
			watchers.remove(cw);

		return uwc.getServerMessage();
	}

	@Override
	public void run() {
		// TODO: nio szerver hogyan tudja értesíteni a klienst?
		// TODO: ha minden igaz a nio szerverben ez még nincsen implementálva :S

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
