package hu.ppke.itk.itkStock.SaveDailyDatas;

import java.util.List;

public interface StockDataObserver {
	public void notify(List<String> updatedStocks);
}
