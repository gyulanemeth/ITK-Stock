package hu.ppke.itk.itkStock.gui;

import java.io.ObjectInputStream.GetField;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class MainGui {

	private static LogInComposite logIn;
	private static IntervalDataCandleComposite candleComposite;
	private static IntervalDataComposite configComposite;
	private static IntervalDataTableComposite dataTableComposite;
	protected static MainMenuComposite mainMenuComposite;
	private static SettingsComposite settingsComposite;
	private static XmlConfigComposite xmlConfigComposite;
	private static AccountComposite accountComposite;
	private int pageNumber = -1;
	private static Composite page1 = null;
	static StackLayout layout=null;
	 static Shell shell;
	 static Display display;
	 static Composite contentPanel;
	 boolean logedIn=false;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		display = new Display();
	    shell = new Shell(display, SWT.DIALOG_TRIM);
	    shell.addListener (SWT.Close, new Listener () {

	    	@Override
	    	public void handleEvent(Event arg0) {
	    		// TODO Auto-generated method stub
	    		int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
	    		MessageBox messageBox = new MessageBox (shell, style);
	    		messageBox.setText ("Exit");
	    		messageBox.setMessage ("Close the program?");
	    		arg0.doit = messageBox.open () == SWT.YES;
	    	
	    	}
	    });
	    contentPanel = new Composite(shell, SWT.BORDER);
	    contentPanel.setBounds(0, 0, 800, 600);
	    //contentPanel.setLayout(new FillLayout());
	    layout = new StackLayout();
	    contentPanel.setLayout(layout);
	  
		
		logIn = new LogInComposite(contentPanel,SWT.NONE);
		//logIn.setLayout(new FillLayout());
		logIn.setBounds(200, 200, 400, 100);
		
		candleComposite = new IntervalDataCandleComposite(contentPanel, SWT.NONE);
		
		configComposite = new IntervalDataComposite(contentPanel, SWT.NONE);
		
		dataTableComposite = new IntervalDataTableComposite(contentPanel, SWT.NONE);
		
		mainMenuComposite = new MainMenuComposite(contentPanel, SWT.NONE);
		
		settingsComposite = new SettingsComposite(contentPanel, SWT.NONE);
		
		xmlConfigComposite = new XmlConfigComposite(contentPanel, SWT.NONE);
		
		accountComposite = new AccountComposite(contentPanel, SWT.NONE);
		
		shell.pack();
		//shell.setSize(800, 600);
		
		 shell.open();
		    while (!shell.isDisposed()) {
		      if (!display.readAndDispatch())
		        display.sleep();
		    }
		    display.dispose();
		  
	}
	
	public static void initializeMenu(){
		  Menu menu = new Menu(shell, SWT.BAR);
			shell.setMenuBar(menu);
			
			MenuItem fileMenu = new MenuItem(menu, SWT.CASCADE);
			fileMenu.setText("File");
			
			Menu menu_1 = new Menu(fileMenu);
			fileMenu.setMenu(menu_1);
			
			MenuItem exitItem = new MenuItem(menu_1, SWT.NONE);
			exitItem.setText("Exit");
			exitItem.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event arg0) {
					// TODO Auto-generated method stub
					shell.close();
				}
			});
			
			MenuItem mntmView = new MenuItem(menu, SWT.CASCADE);
			mntmView.setText("View");
			
			Menu menu_2 = new Menu(mntmView);
			mntmView.setMenu(menu_2);
			
			MenuItem config = new MenuItem(menu_2, SWT.NONE);
			config.setText("Interval Data Candle ");
			config.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event arg0) {
					// TODO Auto-generated method stub
					layout.topControl = candleComposite;
					contentPanel.layout();
				}
			});

			
			MenuItem tableView = new MenuItem(menu_2, SWT.NONE);
			tableView.setText("Interval Data Table");
			tableView.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event arg0) {
					// TODO Auto-generated method stub
					layout.topControl = dataTableComposite;
					contentPanel.layout();
				}
			});
			
			MenuItem mntmIntervalData = new MenuItem(menu_2, SWT.NONE);
			mntmIntervalData.setText("Interval Data Config");
			mntmIntervalData.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event arg0) {
					// TODO Auto-generated method stub
					layout.topControl = configComposite;
					contentPanel.layout();
				}
			});
			
			MenuItem mntmXmlConfigGrafikus = new MenuItem(menu_2, SWT.NONE);
			mntmXmlConfigGrafikus.setText("Xml Config");
			mntmXmlConfigGrafikus.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event arg0) {
					// TODO Auto-generated method stub
					layout.topControl = xmlConfigComposite;
					contentPanel.layout();
				}
			});
			
			MenuItem mntmSettings = new MenuItem(menu, SWT.CASCADE);
			mntmSettings.setText("Settings");
			
			Menu menu_3 = new Menu(mntmSettings);
			mntmSettings.setMenu(menu_3);
			
			MenuItem mntmAccount = new MenuItem(menu_3, SWT.NONE);
			mntmAccount.setText("Account");
			mntmAccount.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event arg0) {
					// TODO Auto-generated method stub
					layout.topControl = accountComposite;
					contentPanel.layout();
				}
			});
			
			MenuItem mntmProperties = new MenuItem(menu_3, SWT.NONE);
			mntmProperties.setText("Preferences");
			mntmProperties.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event arg0) {
					// TODO Auto-generated method stub
					layout.topControl = settingsComposite;
					contentPanel.layout();
				}
			});
	}

}
