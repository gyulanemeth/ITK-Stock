package hu.ppke.itk.itkStock.client;

import java.io.IOException;

import hu.ppke.itk.itkStock.client.watcherClient.WatcherClient;
import hu.ppke.itk.itkStock.server.db.stockWatcher.Watcher.BoundTypes;

public class ClientTestMain {
	public static void main(String[] args) {
		//we gonna have clients as well! ;)
		
		try {
			WatcherClient wc = new WatcherClient(0, "passw");
			wc.addWatcher("MOL", 100000, BoundTypes.UPPER_BOUND);
			
			System.out.println(wc.getWatchers().get(0));
		} catch (IOException e) {
		}
	}
}
