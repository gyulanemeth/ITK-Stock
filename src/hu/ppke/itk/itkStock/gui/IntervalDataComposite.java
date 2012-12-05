package hu.ppke.itk.itkStock.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;

import javax.swing.BoxLayout;

import hu.ppke.itk.itkStock.server.db.historicData.StockData;
import hu.ppke.itk.itkStock.server.db.historicData.StockDate;
import hu.ppke.itk.itkStock.server.db.historicData.StockTime;
import hu.ppke.itk.itkStock.server.db.historicData.Transaction;
import hu.ppke.itk.itkStock.server.id.StockId;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
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
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.ApplicationFrame;
import org.eclipse.swt.layout.GridData;
import java.util.ResourceBundle;

public class IntervalDataComposite extends Composite{

	private static Map<StockDate, Map<StockTime, Transaction>> fetch;
    private JFreeChart localJFreeChart;
    private ChartComposite comp1;
	public IntervalDataComposite(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		this.setLayout(new org.eclipse.swt.layout.GridLayout(1, false));
		Composite comp = new Composite(this, SWT.NONE);
		comp1 = new ChartComposite(getComposite(), SWT.NONE);
		GridData gd_comp1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_comp1.heightHint = 254;
		gd_comp1.widthHint = 366;
		comp1.setLayoutData(gd_comp1);
		comp.setLayout(new org.eclipse.swt.layout.GridLayout(6, false));
		final Combo combo = new Combo(comp, SWT.DROP_DOWN);
		combo.add("Random adatok");
		for(String s : StockId.getStocks()){
			  combo.add(s);
		 }
		 
		Label from = new Label(comp, SWT.None);
		from.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("IntervalDataComposite.from.text")); //$NON-NLS-1$ //$NON-NLS-2$
		final DateTime calendarFrom = new DateTime (comp, SWT.DROP_DOWN);
		
		Label to = new Label(comp, SWT.None);
		to.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("IntervalDataComposite.to.text")); //$NON-NLS-1$ //$NON-NLS-2$
		final DateTime calendarTo = new DateTime(comp, SWT.DROP_DOWN);
		
		Button requestButton = new Button(comp, SWT.None);
		requestButton.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("IntervalDataComposite.requestButton.text")); //$NON-NLS-1$ //$NON-NLS-2$
		requestButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				
			
			
			
				/*try {
					Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> fetchData = StockData.fetchData(combo.getText(), 
									new StockDate(calendarFrom.getYear(), calendarFrom.getMonth(), calendarFrom.getDay()), 
									new StockDate(calendarTo.getYear(), calendarTo.getMonth(), calendarTo.getDay()));
					fetch = (SortedMap<StockDate, SortedMap<StockTime, Transaction>>) fetchData.values();	   
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	*/
				fetch= new HashMap<>();
				for(int i=0;i<100;i++){
					Random random = new Random();
					Map<StockTime, Transaction> adat = new HashMap<StockTime, Transaction>();
					Transaction trans = new Transaction(random.nextDouble(), random.nextInt(500));
					adat.put(new StockTime(random.nextInt(23)+1, random.nextInt(58)+1, random.nextInt(99)+1), trans);
					fetch.put(new StockDate(2012, random.nextInt(11)+1, random.nextInt(26)+1), adat);
				}

				localJFreeChart = createChart(combo.getText());
				comp1.setChart(localJFreeChart);
				comp1.forceRedraw();

			}
		});
		
		combo.pack();
		from.pack();
		to.pack();
		requestButton.pack();
		calendarFrom.pack();
		calendarTo.pack();
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
	    Iterator<StockDate> iterator = fetch.keySet().iterator();
	    Iterator<StockTime> dateIterator;
	    while(iterator.hasNext()){
	    	Map<StockTime, Transaction> dateStock = fetch.get(iterator.next());
	    	dateIterator = dateStock.keySet().iterator();
	    	StockDate date = iterator.next();
	    	while(dateIterator.hasNext()){
	    		StockTime time = dateIterator.next();
	    		Transaction price = dateStock.get(time);
	    		localTimeSeries.add(new Second(time.getSecond(), time.getMinute(), time.getHour(), date.getDay(), date.getMonth(), date.getYear()),price.getPrice());
	    				}
	    	
	    	
	    }
	    return new TimeSeriesCollection(localTimeSeries);
	  }
	
	private static IntervalXYDataset createVolumeDataset()
	  {
	    TimeSeries localTimeSeries = new TimeSeries("Volume");
	    Iterator<StockDate> iterator = fetch.keySet().iterator();
	    Iterator<StockTime> dateIterator;
	    while(iterator.hasNext()){
	    	Map<StockTime, Transaction> dateStock = fetch.get(iterator.next());
	    	dateIterator = dateStock.keySet().iterator();
	    	StockDate date = iterator.next();
	    	while(dateIterator.hasNext()){
	    		StockTime time = dateIterator.next();
	    		Transaction price = dateStock.get(time);
	    		localTimeSeries.add(new Second(time.getSecond(), time.getMinute(), time.getHour(), date.getDay(), date.getMonth(), date.getYear()),price.getVolume());
	    				}
	    }
	    return new TimeSeriesCollection(localTimeSeries);
	 
	  }
	
	public Composite getComposite(){
		return this;
	}

}
