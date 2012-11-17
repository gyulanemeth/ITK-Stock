package hu.ppke.itk.itkStock.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class MainGui {

	private static LogInComposite logIn;
	static Composite page1 = null;
	 static int pageNum = -1;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Display display = new Display();
	    Shell shell = new Shell(display);
	    final Composite contentPanel = new Composite(shell, SWT.BORDER);
	    contentPanel.setBounds(100, 10, 800, 690);
	    //contentPanel.setLayout(new FillLayout());
	    final StackLayout layout = new StackLayout();
	    contentPanel.setLayout(layout);
	    
		logIn = new LogInComposite(contentPanel,SWT.NONE);
		logIn.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				pageNum = ++pageNum % 2;
		        layout.topControl = pageNum == 0 ? page1 :logIn;
		        contentPanel.layout();
			}
		});
		logIn.setLayout(new FillLayout());
		page1 = new Composite(contentPanel, SWT.NONE);
	    page1.setLayout(new FillLayout());
	    Button button = new Button(page1, SWT.NONE);
	    button.setText("Button on page 2");
	    button.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) {
	          pageNum = ++pageNum % 2;
	          layout.topControl = pageNum == 0 ? page1 : logIn;
	          contentPanel.layout();
	        }
	      });
	    button.pack();
		shell.pack();
		shell.setMaximized(true);
		 shell.open();
		    while (!shell.isDisposed()) {
		      if (!display.readAndDispatch())
		        display.sleep();
		    }
		    display.dispose();
		  
	}

}
