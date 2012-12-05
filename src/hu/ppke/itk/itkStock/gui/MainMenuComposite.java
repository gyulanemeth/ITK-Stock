package hu.ppke.itk.itkStock.gui;


import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class MainMenuComposite extends Composite{

	public MainMenuComposite(Composite parent, int style) {
		super(parent, style); 
		DateTime calendar = new DateTime (this, SWT.CALENDAR);
		  calendar.addSelectionListener (new SelectionAdapter () {
		    public void widgetSelected (SelectionEvent e) {
		      System.out.println ("calendar date changed");
		    }
		  });
		  calendar.setBounds(10, 10, 300, 300);
		  calendar.pack();
		 
		  
		// TODO Auto-generated constructor stub
	}

	
}
