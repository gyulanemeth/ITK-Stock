package hu.ppke.itk.itkStock.gui.chart.candlestick;

import hu.ppke.itk.itkStock.server.db.historicData.StockData;
import hu.ppke.itk.itkStock.server.db.historicData.StockDate;
import hu.ppke.itk.itkStock.server.db.historicData.StockTime;
import hu.ppke.itk.itkStock.server.db.historicData.Transaction;

import java.awt.Dimension;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
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
	
	private String title; // = paper_name
	private StockDate fromdate; // the date we seek
	private OHLCDataset dataset;
	private int MODE;
	
	public static final int MODE_DAYS = 10;
	public static final int MODE_HOURS = 11;
	
	/**
	 * Consturcts a chart and then displays the result
	 * @param title paper_name
	 * @param stockDate the date
	 */
	public CandleStickChart(String title, StockDate fromdate, StockDate enddate, int MODE)
	{
		super(title);
		this.title = title;
		this.fromdate = fromdate;
		this.MODE = MODE;
		
		init(title, fromdate, enddate);
	}
	
	private void init(String title, StockDate from, StockDate to)
	{
		try {
			if (from.compareTo(to)==0)
				db = StockData.fetchData(title, from);
			else if (from.compareTo(to)<0)
				db = StockData.fetchData(title, from, to);
			else if (from.compareTo(to)>0) {
				System.err.println("Date interval is invalid, end date is lesser than from date\nOnly from date will be considered!");
				db = StockData.fetchData(title, from);
			}
			
			SortedMap<StockTime, Transaction> map = null;
			ArrayList<StockDate> dates = new ArrayList<StockDate>();
			
			if (db.get(title)!=null) {
				map = db.get(title).get(from);
				dates.add(from);
				
				if (MODE == MODE_DAYS)
				{
					dataset = createDatasetByDays(sumDay(parseDay(map), from));
					while(from.compareTo(to)<0)
					{
						StockDate t = next(from);
						map = db.get(title).get(t);
						dates.add(t);
						if (map!=null)
						{
							dataset = mergeDatasets(dataset, dates, 9, createDatasetByDays(sumDay(parseDay(map), t)), t, 9);
						}
						from = t;
					}
				}
				else if (MODE == MODE_HOURS)
				{
					dataset = createDataset(parseDay(map));
					while(from.compareTo(to)<0)
					{
						StockDate t = next(from);
						map = db.get(title).get(t);
						dates.add(t);
						if (map!=null)
						{
							dataset = mergeDatasets(dataset, dates, 9, createDataset(parseDay(map)), t, 9);
						}
						from = t;
					}
				}
				else {
					showMessage();
					throw new IllegalStateException("Invalid mode has been set!");
				}
				/*
				System.out.println(parseDay(map));
				System.out.println(sumDay(parseDay(map), from));
				dataset = createDataset(parseDay(map));
				dataset = createDatasetByDays(sumDay(parseDay(map), from));
				map = db.get(title).get(to);
				OHLCDataset temp = createDatasetByDays(sumDay(parseDay(map), to));
				dataset = mergeDatasets(dataset, from, 9, temp, to, 9);
				*/
			} else {
				System.err.println("Empty Dataset!\nCannot make a chart of an empty dataset");
				throw new IllegalArgumentException("No such ticker!");
			}
							
			JPanel jpanel = createDemoPanel();
			jpanel.setPreferredSize(new Dimension(500, 270));
			setContentPane(jpanel);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showMessage() {
		MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);
        
        messageBox.setText("IllegalStateException");
        messageBox.setMessage("Invalid mode has been set! Please set Hourly or Daily mode\nCTRL+N to start again.");
        messageBox.open();
	}

	private OHLCDataset mergeDatasets(OHLCDataset dataset1, ArrayList<StockDate> date1, int starthour1,
										OHLCDataset dataset2, StockDate date2, int starthour2)
	{		
		Date[]   date   = new Date  [dataset1.getItemCount(0) + dataset2.getItemCount(0)];
		double[] high   = new double[dataset1.getItemCount(0) + dataset2.getItemCount(0)];
		double[] low    = new double[dataset1.getItemCount(0) + dataset2.getItemCount(0)];
		double[] open   = new double[dataset1.getItemCount(0) + dataset2.getItemCount(0)];
		double[] close  = new double[dataset1.getItemCount(0) + dataset2.getItemCount(0)];
		double[] volume = new double[dataset1.getItemCount(0) + dataset2.getItemCount(0)];
		
		int j = 0;
		for (int i = 0; i<dataset1.getItemCount(0); i++)
		{
			if (j<date1.size()) {
				date[i] = createDate(date1.get(j), starthour1);
				if (MODE == MODE_HOURS && i%8 == 0 && i>0) {
					j++;
					starthour1 = 9;
				}
				else if (MODE == MODE_DAYS){
					j++;
					starthour1 = 9;
				}
			}
			else {
				date[i] = createDate(date1.get(0), starthour1);
			}
//			date[i] = createDate(date1.get(j), starthour1);
			starthour1++;
			open[i] = dataset1.getOpenValue(0, i);
			volume[i] = dataset1.getVolumeValue(0, i);
			high[i] = dataset1.getHighValue(0, i);
			low[i] = dataset1.getLowValue(0, i);
			close[i] = dataset1.getCloseValue(0, i);
		}
		
		for (int i = 0; i<dataset2.getItemCount(0); i++)
		{
			date[i+dataset1.getItemCount(0)] = createDate(date2, starthour2);
			starthour2++;
			open[i+dataset1.getItemCount(0)] = dataset2.getOpenValue(0, i);
			volume[i+dataset1.getItemCount(0)] = dataset2.getVolumeValue(0, i);
			high[i+dataset1.getItemCount(0)] = dataset2.getHighValue(0, i);
			low[i+dataset1.getItemCount(0)] = dataset2.getLowValue(0, i);
			close[i+dataset1.getItemCount(0)] = dataset2.getCloseValue(0, i);
		}
		
		return new DefaultHighLowDataset(title, date, high, low, open, close, volume);
	}
	
	//új függvény: adott nap parse-olása
	private SortedMap<Integer, HashMap<String, Double>> parseDay(SortedMap<StockTime, Transaction> map) throws Exception
	{
		if (map==null)
			throw new Exception("Empty Dataset!");
		
		SortedMap<Integer, HashMap<String, Double>> ret = new TreeMap<Integer, HashMap<String, Double>>();
		
		for (Entry<StockTime, Transaction> entry : map.entrySet())
		{
			int hour = entry.getKey().getHour();
		    double price = entry.getValue().getPrice();
		    
		    if (ret.containsKey(hour)) {
		    	double lowprice = ret.get(hour).get("low");
		    	double highprice = ret.get(hour).get("high");
		    	price = entry.getValue().getPrice();
		    	double volume = ret.get(hour).get("volume") + entry.getValue().getVolume();
		    	
		    	if (price < lowprice) {
		    		ret.get(hour).put("low", price);
		    	}
		    	
		    	if (price > highprice) {
		    		ret.get(hour).put("high", price);
		    	}
		    	
		    	ret.get(hour).put("close", price);
		    	ret.get(hour).put("volume", volume);
		    } else {
		    	HashMap<String, Double> tmp = new HashMap<String, Double>();
		    	tmp.put("open", price);
		    	tmp.put("high", price);
		    	tmp.put("low", price);
		    	tmp.put("volume", 0.0);
		    	ret.put(hour, tmp);
		    }
		}
		
		return ret;
	}
	
	//új függvény: nap összesítése
	private SortedMap<StockDate, HashMap<String, Double>> sumDay(SortedMap<Integer, HashMap<String, Double>> map, StockDate date)
	{
		SortedMap<StockDate, HashMap<String, Double>> ret = new TreeMap<StockDate, HashMap<String, Double>>();
		
		double open = -1;
		double close = -1;
		double volume = 0.0;
		double low;
		double high;
		
		Iterator<Entry<Integer, HashMap<String, Double>>> iterator = map.entrySet().iterator();
		HashMap<String, Double> value = iterator.next().getValue();
		open = value.get("open");
		close = value.get("close");
		low = value.get("low");
		high = value.get("high");
		volume += value.get("volume");
		
		for(Iterator<Entry<Integer, HashMap<String, Double>>> it = iterator; it.hasNext();) {
            value = it.next().getValue();
            close = value.get("close");
            volume += value.get("volume");
            if (value.get("low")<low) low = value.get("low");
            if (value.get("high")>high) high = value.get("high");
        }
		
		HashMap<String, Double> m = new HashMap<String, Double>();
		m.put("open", open);
		m.put("close", close);
		m.put("low", low);
		m.put("high", high);
		m.put("volume", volume);
		ret.put(fromdate, m);
		
		return ret;
	}
	
	/**
	 * Creates the chart from the dataset
	 * @return the chart
	 */
	public JFreeChart createChart()
	{
		JFreeChart jfreechart = ChartFactory.createCandlestickChart("Graph", "Time", "Value", dataset, true);
		XYPlot xyplot = (XYPlot)jfreechart.getPlot();
		xyplot.setDomainPannable(true);
		NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();
		numberaxis.setAutoRangeIncludesZero(false);
		numberaxis.setUpperMargin(0.0D);
		numberaxis.setLowerMargin(0.0D);
		return jfreechart;
	}
	
	private static StockDate next(StockDate today)
	{
		int year = today.getYear();
		int month = today.getMonth();
		int day = today.getDay();
		
		if (day==31)
		{
			if (month==12)
				year++;
			else
				month++;
		}
		else
			day++;
		
		return new StockDate(year, month, day);
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
	private OHLCDataset createDataset(SortedMap<Integer, HashMap<String, Double>> chartdata)
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
	
	private OHLCDataset createDatasetByDays(SortedMap<StockDate, HashMap<String, Double>> chartdata)
	{
		Date[]   date   = new Date[chartdata.size()];
		double[] high   = new double[chartdata.size()];
		double[] low    = new double[chartdata.size()];
		double[] open   = new double[chartdata.size()];
		double[] close  = new double[chartdata.size()];
		double[] volume = new double[chartdata.size()];
		
		int i = 0;
		for (Entry<StockDate, HashMap<String, Double>> mapentry : chartdata.entrySet())
		{
			date[i]   = createDate(mapentry.getKey(), 12);
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
//		CandleStickChart chart = new CandleStickChart("MOL", new StockDate(2006, 6, 6));
//		CandleStickChart chart = new CandleStickChart("MOL", new StockDate(2006, 6, 6), new StockDate(2006, 6, 6), MODE_HOURS);
		CandleStickChart chart = new CandleStickChart("MOL", new StockDate(2006, 6, 6), new StockDate(2006, 6, 9), MODE_HOURS);
//		CandleStickChart chart = new CandleStickChart("MOL", new StockDate(2006, 6, 6), new StockDate(2006, 6, 9), MODE_DAYS);
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
	
}
