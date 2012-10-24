package hu.ppke.itk.itkStock.nio.protocol;

import java.sql.SQLException;

import hu.ppke.itk.itkStock.client.watcherClient.ClientsideWatcher;
import hu.ppke.itk.itkStock.client.watcherClient.SerializationTools;
import hu.ppke.itk.itkStock.server.db.stockWatcher.WatcherManager;

public class ResponseWatcherRegisterCommand extends
		AbstractProtocolCommandWorker {
	
	private WatcherManager wm;
	
	public ResponseWatcherRegisterCommand(WatcherManager wm) {
		this.wm = wm;
	}

	@Override
	public ProtocolMessage response(ProtocolMessage msg) {
		
		byte[] bytes = msg.toByteArray();
		ClientsideWatcher cw = (ClientsideWatcher) SerializationTools.bytesToObject(bytes);
		
		try {
			if(!wm.getWatcherByUserIdStockType(cw.getUserId(), cw.getPaperName(), cw.getBoundType())) {
				// TODO: ellenőrizni, hogy megfelelőek-e a határok, az árakat tartalmazó map még nincs implementálva
				wm.addWatcher(cw.getUserId(), cw.getPaperName(), cw.getBoundValue(), cw.getBoundType());
				return new ProtocolMessage(ProtocolTools.registerWatcherResponse, "success".getBytes());
			} else
				return new ProtocolMessage(ProtocolTools.registerWatcherResponse, "failed - already exists".getBytes());
				
		} catch (SQLException e) {
			return new ProtocolMessage(ProtocolTools.registerWatcherResponse, "failed - sql error".getBytes());
		}
	}

}
