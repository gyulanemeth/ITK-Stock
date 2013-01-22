package hu.ppke.itk.itkStock.gui;

import hu.ppke.itk.itkStock.server.db.historicData.StockDate;
import hu.ppke.itk.itkStock.server.db.historicData.StockTime;
import hu.ppke.itk.itkStock.server.db.historicData.Transaction;
import hu.ppke.itk.itkStock.server.id.StockId;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class IntervalDataTableComposite extends Composite{
	private static Table table;
	private static Map<StockDate, Map<StockTime, Transaction>> data;

	public IntervalDataTableComposite(Composite parent, int style) {
		super(parent, style);
		
		this.setLayout(new GridLayout(1, false));
		Composite comp = new Composite(this, SWT.NONE);
		comp.setLayout(new GridLayout(6, false));
		
		
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
				System.out.println("combo: " + combo.getText());
				/*try {
					Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>> fetchData = StockData.fetchData(combo.getText(), 
									new StockDate(calendarFrom.getYear(), calendarFrom.getMonth(), calendarFrom.getDay()), 
									new StockDate(calendarTo.getYear(), calendarTo.getMonth(), calendarTo.getDay()));
					fetch = (SortedMap<StockDate, SortedMap<StockTime, Transaction>>) fetchData.values();	   
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	*/
				
				//Dummy data!
				data= new HashMap<>();
				for(int i=0;i<100;i++){
					Random random = new Random();
					Map<StockTime, Transaction> data2 = new HashMap<StockTime, Transaction>();
					Transaction trans = new Transaction(random.nextDouble(), random.nextInt(500));
					data2.put(new StockTime(random.nextInt(23)+1, random.nextInt(58)+1, random.nextInt(99)+1), trans);
					data.put(new StockDate(2012, random.nextInt(11)+1, random.nextInt(26)+1), data2);
				}
				
				 table.setLinesVisible(true);
				    table.setHeaderVisible(true);
				    GridData gdata = new GridData(SWT.FILL, SWT.FILL, true, true);
				    gdata.heightHint = 200;
				    table.setLayoutData(gdata);
				    String[] titles = {"Ticker", "Per", "Date", "Close", "Volume" };
				    
				    for (int i = 0; i < titles.length; i++) {
				    	TableColumn column = new TableColumn(table, SWT.NONE);
				        column.setText(titles[i]);
				        table.getColumn(i).pack();
				    }
				    
				
				    for (int i = 0; i < data.size(); i++){
				    	TableItem item = new TableItem(table, SWT.NONE);
				    	item.setText(0,combo.getText());
				    	item.setText(1,"Per"+i);
				    	item.setText(2,"Date");
				    	item.setText(3,"Close");
				    	item.setText(4,"Volume");
				    }

				

			}
		});
		
		table = new Table(comp, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table = new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1);
		gd_table.widthHint = 435;
		table.setLayoutData(gd_table);
		
	}

}
