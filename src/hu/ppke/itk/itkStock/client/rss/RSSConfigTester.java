package hu.ppke.itk.itkStock.client.rss;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class RSSConfigTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<String> list = RSSConfigManager.defaultManager().getFeeds();
		
		System.out.println("Saved feeds:");
		for (String string : list) {
			
			System.out.println(string);
		}
		
		System.out.println();
		
		String exampleFeed = "ExampleFeed";
		
//		This does not work!
//		list.add(exampleFeed);
//		list.remove(exampleFeed);
		
//		Use this instace of list manipulation
		RSSConfigManager.defaultManager().addFeed(exampleFeed);
		RSSConfigManager.defaultManager().removeFeed(exampleFeed);
		
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));
		
		new RSSConfigManagerGUI(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		
		shell.open();
		
		while (!shell.isDisposed())
		{
			if(!display.readAndDispatch()){
				display.sleep();
			}
		}
		display.dispose();
	}

}
