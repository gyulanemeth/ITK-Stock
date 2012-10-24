package hu.ppke.itk.itkStock.nio.protocol;

import java.sql.SQLException;

import hu.ppke.itk.itkStock.client.watcherClient.ClientsideWatcher;
import hu.ppke.itk.itkStock.client.watcherClient.SerializationTools;
import hu.ppke.itk.itkStock.server.db.stockWatcher.WatcherManager;

public class ResponseWatcherUnregisterCommand extends
		AbstractProtocolCommandWorker {
	
	private WatcherManager wm;

	public ResponseWatcherUnregisterCommand(WatcherManager wm) {
		this.wm = wm;
	}

	@Override
	public ProtocolMessage response(ProtocolMessage msg) {
		byte[] bytes = msg.toByteArray();
		ClientsideWatcher cw = (ClientsideWatcher) SerializationTools.bytesToObject(bytes);
		
		try {
			if(!wm.getWatcherByUserIdStockType(cw.getUserId(), cw.getPaperName(), cw.getBoundType())) {
				wm.removeWatcherByUserIdStockType(cw.getUserId(), cw.getPaperName(), cw.getBoundType());
				return new ProtocolMessage(ProtocolTools.unregisterWatcherResponse, "successful".getBytes());
			} else
				return new ProtocolMessage(ProtocolTools.unregisterWatcherResponse, "failed - not exists".getBytes());
		} catch (SQLException e) {
			return new ProtocolMessage(ProtocolTools.unregisterWatcherResponse, "failed - sql error".getBytes());
		}
	}

}
