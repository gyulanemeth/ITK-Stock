package hu.ppke.itk.itkStock.gui;

import hu.ppke.itk.itkStock.gui.chart.candlestick.CandleStickChart;
import hu.ppke.itk.itkStock.server.db.historicData.StockDate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;

public class IntervalDataCandleComposite extends Composite{

	Text ticker;
	Button button;
	DateTime fromdate;
	DateTime todate;
	Composite panel;
	JFreeChart chart;
	ChartComposite comp;
	int MODE = 0;
	
	public IntervalDataCandleComposite(Composite parent, int style) {
		super(parent, style);
		
		panel = new Composite(this, SWT.NONE);
		panel.setLayout(new GridLayout(2, true));
		comp = new ChartComposite(this, SWT.NONE);

		initWindow(panel);
		
		panel.pack();
	}
	
	private void initWindow(final Composite panel)
	{
		Label labelTitle = new Label(panel, SWT.NONE);
		labelTitle.setText("Ticker name");
		labelTitle.setLayoutData(new GridData(0));
		ticker = new Text(panel, SWT.BORDER);
		ticker.setLayoutData(new GridData(1));
		Label labelFrom = new Label(panel, SWT.NONE);
		labelFrom.setText("From:");
		labelFrom.setLayoutData(new GridData(2));
		fromdate = new DateTime(panel, SWT.BORDER);
		fromdate.setLayoutData(new GridData(3));		
		Label labelTo = new Label(panel, SWT.NONE);
		labelTo.setText("To:");
		labelTo.setLayoutData(new GridData(4));
		todate = new DateTime(panel, SWT.BORDER);
		todate.setLayoutData(new GridData(5));
		button = new Button(panel, SWT.NONE);
		button.setText("OK");
		button.setLayoutData(new GridData(6));
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e)
			{
				addCandlestickChart(ticker.getText().toUpperCase(), new StockDate(fromdate.getYear(), fromdate.getMonth()+1, fromdate.getDay()),
						new StockDate(todate.getYear(), todate.getMonth()+1, todate.getDay()));
				panel.setVisible(false);
			}
		});
		Label label = new Label(panel, SWT.NONE);
		label.setLayoutData(new GridData(7));
		Button modedays = new Button(panel, SWT.RADIO);
	    modedays.setText("Daily");
	    modedays.setLayoutData(new GridData(8));
	    Button modehours = new Button(panel, SWT.RADIO);
	    modehours.setText("Hourly");
	    modehours.setLayoutData(new GridData(9));
	    modehours.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e)
			{
				MODE = 11;
			}
		});
	    modedays.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e)
			{
				MODE = 10;
			}
		});
		
		//presets
		fromdate.setYear(2006);
		fromdate.setMonth(5);
		fromdate.setDay(6);
		ticker.setText("mol");
		todate.setYear(2006);
		todate.setMonth(5);
		todate.setDay(7);
		
		//enter key listener
		ticker.addKeyListener(listener);
		todate.addKeyListener(listener);
		fromdate.addKeyListener(listener);
	}
	
	private KeyListener listener = new KeyListener() {
		@Override
		public void keyReleased(KeyEvent arg0) {}
		
		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.keyCode==13)
			{
				addCandlestickChart(ticker.getText().toUpperCase(), new StockDate(fromdate.getYear(), fromdate.getMonth()+1, fromdate.getDay()),
						new StockDate(todate.getYear(), todate.getMonth()+1, todate.getDay()));
				panel.setVisible(false);
			}
		}
	};
	
	private void addCandlestickChart(String title, StockDate from, StockDate to)
	{
		CandleStickChart csc = new CandleStickChart(title, from, to, MODE);
		chart = csc.createChart();
		
	    comp.setChart(chart);
	    comp.pack();
	    comp.setSize(800, 550);
	}

}
