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
import java.util.ResourceBundle;
import org.eclipse.swt.widgets.Label;

public class MainGui {

	private static LogInComposite logIn;
	private static IntervalDataCandleComposite candleComposite;
	private static IntervalDataComposite configComposite;
	private static IntervalDataTableComposite dataTableComposite;
	protected static MainMenuComposite mainMenuComposite;
	private static SettingsComposite settingsComposite;
	private static XmlConfigComposite xmlConfigComposite;
	private static ClientSettingsComposite accountComposite;
	private int pageNumber = -1;
	private static Composite page1 = null;
	static StackLayout layout=null;
	 static Shell shell;
	 static Display display;
	 static Composite contentPanel;
	 boolean logedIn=false;
	 private static Menu menu;
	 public static final int width = 800;
	 public static final int height = 600;
	 static Menu menu_2;
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
	    contentPanel.setBounds(0, 0, width, height);
	    //contentPanel.setLayout(new FillLayout());
	    layout = new StackLayout();
	    contentPanel.setLayout(layout);
	  
		
		logIn = new LogInComposite(contentPanel,SWT.NONE);
		logIn.setBounds(300, 200, 400, 100);
		//logIn.setLayout(new FillLayout());
		
		new Label(logIn, SWT.NONE);
		
		candleComposite = new IntervalDataCandleComposite(contentPanel, SWT.NONE);
		
		configComposite = new IntervalDataComposite(contentPanel, SWT.NONE);
		
		dataTableComposite = new IntervalDataTableComposite(contentPanel, SWT.NONE);
		
		mainMenuComposite = new MainMenuComposite(contentPanel, SWT.NONE);
		
		settingsComposite = new SettingsComposite(contentPanel, SWT.NONE);
		
		xmlConfigComposite = new XmlConfigComposite(contentPanel, SWT.NONE);
		
		accountComposite = new ClientSettingsComposite(contentPanel, SWT.NONE);
		 menu = new Menu(shell, SWT.BAR);
			shell.setMenuBar(menu);
			
			MenuItem fileMenu = new MenuItem(menu, SWT.CASCADE);
			fileMenu.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("MainGui.fileMenu.text")); //$NON-NLS-1$ //$NON-NLS-2$
			
			Menu menu_1 = new Menu(fileMenu);
			fileMenu.setMenu(menu_1);
			
			MenuItem logInmenu = new MenuItem(menu_1, SWT.NONE);
			logInmenu.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("MainGui.logInmenu.text")); //$NON-NLS-1$ //$NON-NLS-2$
			logInmenu.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event arg0) {
					// TODO Auto-generated method stub
					layout.topControl = logIn;
					logIn.setBounds(300, 200, 400, 100);
					contentPanel.layout();
				}
			});
			
			MenuItem exitItem = new MenuItem(menu_1, SWT.NONE);
			exitItem.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("MainGui.exitItem.text")); //$NON-NLS-1$ //$NON-NLS-2$
			exitItem.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event arg0) {
					// TODO Auto-generated method stub
					shell.close();
				}
			});
			MenuItem mntmView = new MenuItem(menu, SWT.CASCADE);
			mntmView.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("MainGui.mntmView.text")); //$NON-NLS-1$ //$NON-NLS-2$
			
			menu_2 = new Menu(mntmView);
			mntmView.setMenu(menu_2);
			
			MenuItem mntmXmlConfigGrafikus = new MenuItem(menu_2, SWT.NONE);
			mntmXmlConfigGrafikus.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("MainGui.mntmXmlConfigGrafikus.text")); //$NON-NLS-1$ //$NON-NLS-2$
			mntmXmlConfigGrafikus.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event arg0) {
					// TODO Auto-generated method stub
					layout.topControl = xmlConfigComposite;
					contentPanel.layout();
				}
			});
		
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
		 
			
		
		
		MenuItem config = new MenuItem(menu_2, SWT.NONE);
		config.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("MainGui.config.text")); //$NON-NLS-1$ //$NON-NLS-2$
		config.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				layout.topControl = candleComposite;
				contentPanel.layout();
			}
		});

		
		MenuItem tableView = new MenuItem(menu_2, SWT.NONE);
		tableView.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("MainGui.tableView.text")); //$NON-NLS-1$ //$NON-NLS-2$
		tableView.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				layout.topControl = dataTableComposite;
				contentPanel.layout();
			}
		});
		
		MenuItem mntmIntervalData = new MenuItem(menu_2, SWT.NONE);
		mntmIntervalData.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("MainGui.mntmIntervalData.text")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmIntervalData.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				layout.topControl = configComposite;
				contentPanel.layout();
			}
		});
		
		
		
		MenuItem mntmSettings = new MenuItem(menu, SWT.CASCADE);
		mntmSettings.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("MainGui.mntmSettings.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		Menu menu_3 = new Menu(mntmSettings);
		mntmSettings.setMenu(menu_3);
		
		MenuItem mntmAccount = new MenuItem(menu_3, SWT.NONE);
		mntmAccount.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("MainGui.mntmAccount.text")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmAccount.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				layout.topControl = accountComposite;
				contentPanel.layout();
			}
		});
		
		MenuItem mntmProperties = new MenuItem(menu_3, SWT.NONE);
		mntmProperties.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("MainGui.mntmProperties.text")); //$NON-NLS-1$ //$NON-NLS-2$
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
