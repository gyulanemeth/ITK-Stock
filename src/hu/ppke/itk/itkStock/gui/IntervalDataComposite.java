package hu.ppke.itk.itkStock.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

import hu.ppke.itk.itkStock.server.db.historicData.StockData;
import hu.ppke.itk.itkStock.server.db.historicData.StockDate;
import hu.ppke.itk.itkStock.server.db.historicData.StockTime;
import hu.ppke.itk.itkStock.server.db.historicData.Transaction;
import hu.ppke.itk.itkStock.server.id.StockId;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

public class IntervalDataComposite extends Composite{

	private static SortedMap<StockDate, SortedMap<StockTime, Transaction>> fetch;
    private JFreeChart localJFreeChart;
	
	public IntervalDataComposite(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		this.setLayout(new org.eclipse.swt.layout.GridLayout(1, false));
		Composite comp = new Composite(this, SWT.NONE);
		Composite comp1 = new Composite(this, SWT.EMBEDDED | SWT.NO_BACKGROUND);
		comp.setLayout(new org.eclipse.swt.layout.GridLayout(6, false));
		final Frame frame = SWT_AWT.new_Frame(comp1);
		frame.setPreferredSize(new Dimension(500, 600));
		final Combo combo = new Combo(comp, SWT.DROP_DOWN);
		
		 for(String s : StockId.getStocks()){
			  combo.add(s);
		  }
		 
		Label from = new Label(comp, SWT.None);
		from.setText("From: ");
		final DateTime calendarFrom = new DateTime (comp, SWT.DROP_DOWN);
		
		Label to = new Label(comp, SWT.None);
		to.setText("To: ");
		final DateTime calendarTo = new DateTime(comp, SWT.DROP_DOWN);
		
		Button requestButton = new Button(comp, SWT.None);
		requestButton.setText("Request");
		requestButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				
				try {
					Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> fetchData = StockData.fetchData(combo.getText(), 
									new StockDate(calendarFrom.getYear(), calendarFrom.getMonth(), calendarFrom.getDay()), 
									new StockDate(calendarTo.getYear(), calendarTo.getMonth(), calendarTo.getDay()));
					fetch = (SortedMap<StockDate, SortedMap<StockTime, Transaction>>) fetchData.values();
					ChartPanel localChartPanel = new ChartPanel(localJFreeChart, true, true, true, false, true);
				    localChartPanel.setPreferredSize(new Dimension(500, 270));
				    frame.add(localChartPanel);
				   
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		});
		
		Button requestButton2 = new Button(comp1, SWT.None);
		requestButton2.setText("Request");
		requestButton2.pack();
		combo.pack();
		from.pack();
		to.pack();
		requestButton.pack();
		calendarFrom.pack();
		calendarTo.pack();
		comp.pack();		
		comp1.pack();
	}
	
	private static JFreeChart createChart(String stockName)
	  {
	    XYDataset localXYDataset = createPriceDataset();
	    JFreeChart localJFreeChart = ChartFactory.createTimeSeriesChart(stockName, "Date", "Price", localXYDataset, true, true, false);
	    XYPlot localXYPlot = (XYPlot)localJFreeChart.getPlot();
	    NumberAxis localNumberAxis1 = (NumberAxis)localXYPlot.getRangeAxis();
	    localNumberAxis1.setLowerMargin(0.4D);
	    DecimalFormat localDecimalFormat = new DecimalFormat("00.00");
	    localNumberAxis1.setNumberFormatOverride(localDecimalFormat);
	    XYItemRenderer localXYItemRenderer = localXYPlot.getRenderer();
	    localXYItemRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")));
	    NumberAxis localNumberAxis2 = new NumberAxis("Volume");
	    localNumberAxis2.setUpperMargin(1.0D);
	    localXYPlot.setRangeAxis(1, localNumberAxis2);
	    localXYPlot.setDataset(1, createVolumeDataset());
	    localXYPlot.setRangeAxis(1, localNumberAxis2);
	    localXYPlot.mapDatasetToRangeAxis(1, 1);
	    XYBarRenderer localXYBarRenderer = new XYBarRenderer(0.2D);
	    localXYBarRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0,000.00")));
	    localXYPlot.setRenderer(1, localXYBarRenderer);
	    ChartUtilities.applyCurrentTheme(localJFreeChart);
	    localXYBarRenderer.setBarPainter(new StandardXYBarPainter());
	    localXYBarRenderer.setShadowVisible(false);
	    return localJFreeChart;
	    
	  }

	private static XYDataset createPriceDataset()
	  {
	    TimeSeries localTimeSeries = new TimeSeries("Price");
	    Iterator iterator = fetch.keySet().iterator();
	    Iterator dateIterator;
	    double value =0;
	    int numberOfValues=0;
	    while(iterator.hasNext()){
	    	SortedMap<StockTime, Transaction> dateStock = fetch.get(iterator.next());
	    	dateIterator = dateStock.keySet().iterator();
	    	StockDate date = (StockDate) iterator.next();
	    	while(dateIterator.hasNext()){
	    		StockTime time = (StockTime) dateIterator.next();
	    		Transaction price = dateStock.get(dateIterator);
	    		value+=price.getPrice();
	    		numberOfValues++;
	    				}
	    	localTimeSeries.add(new Day(date.getDay(), date.getMonth(), date.getYear()),value/numberOfValues);
	    }
	    return new TimeSeriesCollection(localTimeSeries);
	  }
	
	private static IntervalXYDataset createVolumeDataset()
	  {
	    TimeSeries localTimeSeries = new TimeSeries("Volume");
	    Iterator iterator = fetch.keySet().iterator();
	    Iterator dateIterator;
	    double volume =0;
	    while(iterator.hasNext()){
	    	SortedMap<StockTime, Transaction> dateStock = fetch.get(iterator.next());
	    	dateIterator = dateStock.keySet().iterator();
	    	StockDate date = (StockDate) iterator.next();
	    	while(dateIterator.hasNext()){
	    		StockTime time = (StockTime) dateIterator.next();
	    		Transaction price = dateStock.get(dateIterator);
	    		volume+=price.getVolume();
	    				}
	    	localTimeSeries.add(new Day(date.getDay(), date.getMonth(), date.getYear()),volume);
	    }
	    return new TimeSeriesCollection(localTimeSeries);
	 
	  }

}
