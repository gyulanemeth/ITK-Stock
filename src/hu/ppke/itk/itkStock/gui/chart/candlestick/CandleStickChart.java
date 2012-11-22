package hu.ppke.itk.itkStock.gui.chart.candlestick;

import hu.ppke.itk.itkStock.server.db.historicData.StockData;
import hu.ppke.itk.itkStock.server.db.historicData.StockDate;
import hu.ppke.itk.itkStock.server.db.historicData.StockTime;
import hu.ppke.itk.itkStock.server.db.historicData.Transaction;

import java.awt.Dimension;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class CandleStickChart extends ApplicationFrame {

	private static final long serialVersionUID = 1L;
	private static final Calendar calendar = Calendar.getInstance(); //a calendar for the createDate method
	
	private Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction> > > db; //the result of the query in the init method
	private HashMap<Integer, HashMap<String, Double>> chartdata; //the dataset
	
	private String title; // = paper_name
	private StockDate stockDate; // the date we seek
	
	/**
	 * Consturcts a chart and then displays the result
	 * @param title paper_name
	 * @param stockDate the date
	 */
	public CandleStickChart(String title, StockDate stockDate)
	{
		super(title);
		this.title = title;
		this.stockDate = stockDate;
		
		init();
	}
	
	/**
	 * Init method<br>
	 * Gets the data from the database. A certain paper and a certain day is specified.
	 */
	private void init()
	{
		try {
			db = StockData.fetchData(title, stockDate);
			SortedMap<StockTime, Transaction> map = null;
			
			if (db.get(title)!=null) {
				map = db.get(title).get(stockDate);
//				System.out.println(map.toString());
			} else {
				System.err.println("Empty Dataset!");
				throw new Exception("Cannot make a chart out of an empty dataset");
			}
			
			if (map!=null) {
				parseData(map);
//				System.out.println(chartdata);
				createDataset(chartdata);
				
				JPanel jpanel = createDemoPanel();
				jpanel.setPreferredSize(new Dimension(500, 270));
				setContentPane(jpanel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parses the result of the query. This query is the result of the StockData.fetch() method
	 * The query only works for one day only, and one paper_name
	 * @param map
	 */
	private void parseData(SortedMap<StockTime, Transaction> map)
	{
		System.out.println(map);
		chartdata = new HashMap<Integer, HashMap<String, Double>>();
		for (Map.Entry<StockTime, Transaction> entry : map.entrySet())
		{
		    int hour = entry.getKey().getHour();
		    double price = entry.getValue().getPrice();
		    
		    if (chartdata.containsKey(hour)) {
		    	double lowprice = chartdata.get(hour).get("low");
		    	double highprice = chartdata.get(hour).get("high");
		    	price = entry.getValue().getPrice();
		    	double volume = chartdata.get(hour).get("volume") + entry.getValue().getVolume();
		    	
		    	if (price < lowprice) {
		    		chartdata.get(hour).put("low", price);
		    	}
		    	
		    	if (price > highprice) {
		    		chartdata.get(hour).put("high", price);
		    	}
		    	
		    	chartdata.get(hour).put("close", price);
		    	chartdata.get(hour).put("volume", volume);
		    } else {
		    	HashMap<String, Double> tmp = new HashMap<String, Double>();
		    	tmp.put("open", price);
		    	tmp.put("high", price);
		    	tmp.put("low", price);
		    	tmp.put("volume", 0.0);
		    	chartdata.put(hour, tmp);
		    }
		}
		System.out.println(chartdata);
	}
	
	/**
	 * Creates the chart from the dataset
	 * @return the chart
	 */
	public JFreeChart createChart()
	{
		JFreeChart jfreechart = ChartFactory.createCandlestickChart("Graph", "Time", "Value", createDataset(chartdata), true);
		XYPlot xyplot = (XYPlot)jfreechart.getPlot();
		xyplot.setDomainPannable(true);
		NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();
		numberaxis.setAutoRangeIncludesZero(false);
		numberaxis.setUpperMargin(0.0D);
		numberaxis.setLowerMargin(0.0D);
		return jfreechart;
	}
	
	/**
	 * A method which creates date for the dataset of the chart
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param min
	 * @return the date specified by year, month, day, hour, min
	 */
	public static Date createDate(int year, int month, int day, int hour, int min)
	{
		calendar.clear();
		calendar.set(year, month-1, day, hour, min);
		return calendar.getTime();
	}
	
	/**
	 * A method which creates date for tha dataset of the chart
	 * @param date
	 * @param hour
	 * @return the date specified by a StockDate and hour
	 */
	public static Date createDate(StockDate date, int hour)
	{
		calendar.clear();
		calendar.set(date.getYear(), date.getMonth(), date.getDay(), hour, 0);
		return calendar.getTime();
	}
	
	/**
	 * Generates the dataset for the CandlestickChart<br>
	 * The dataset containts arrays for the values needed to create a candlestick chart
	 * @param chartdata
	 * @return OHLCDataset containing the arrays
	 */
	private OHLCDataset createDataset(HashMap<Integer, HashMap<String, Double>> chartdata)
	{

		Date[]   date   = new Date[chartdata.size()];
		double[] high   = new double[chartdata.size()];
		double[] low    = new double[chartdata.size()];
		double[] open   = new double[chartdata.size()];
		double[] close  = new double[chartdata.size()];
		double[] volume = new double[chartdata.size()];
		StockDate sdate = new StockDate(2011, 02, 18);
		
		int i = 0;
		for (Map.Entry<Integer, HashMap<String, Double>> mapentry : chartdata.entrySet())
		{
			date[i]   = createDate(sdate, mapentry.getKey());
			high[i]   = mapentry.getValue().get("high");
			low[i]    = mapentry.getValue().get("low");
			open[i]   = mapentry.getValue().get("open");
			close[i]  = mapentry.getValue().get("close");
			volume[i] = mapentry.getValue().get("volume");
			i++;
		}

		return new DefaultHighLowDataset(title, date, high, low, open, close, volume);
	}
	
	/**
	 * Creates panel for chart
	 * @return panel
	 */
	public JPanel createDemoPanel()
	{
		JFreeChart jfreechart = createChart();
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		return chartpanel;
	}
	
	/**
	 * Test main
	 * @param args
	 */
	public static void main(String args[]) //testmain
	{
		CandleStickChart chart = new CandleStickChart("MTELEKOM", new StockDate(2010, 12, 30));
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
	
}
