package hu.ppke.itk.itkStock.nio.protocol;

import java.util.Map;
import java.util.SortedMap;

import hu.ppke.itk.itkStock.client.historicData.HistoricData;
import hu.ppke.itk.itkStock.client.watcherClient.SerializationTools;
import hu.ppke.itk.itkStock.server.db.historicData.StockDate;
import hu.ppke.itk.itkStock.server.db.historicData.StockTime;
import hu.ppke.itk.itkStock.server.db.historicData.Transaction;

public class RecieveHistoricData extends AbstractProtocolCommandWorker {
	HistoricData historic;
	public RecieveHistoricData(HistoricData h) {
		historic = h;
	}
	@Override
	public ProtocolMessage response(ProtocolMessage msg) {
		byte[] data = msg.data;
		Object o = SerializationTools.bytesToObject(data);
		@SuppressWarnings("unchecked")
		Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> historicDatas = (Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>>) o;
		historic.addToArchive(historicDatas);
		return null;
	}

}
