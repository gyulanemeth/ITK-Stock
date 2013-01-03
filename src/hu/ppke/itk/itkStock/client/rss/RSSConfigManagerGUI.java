package hu.ppke.itk.itkStock.client.rss;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;


public class RSSConfigManagerGUI extends Composite {
	
	private String dialogCloseResult;

	public RSSConfigManagerGUI(Composite parent, int style) {
		super(parent, style);
		
		buildWindow();
	}

	private void buildWindow(){
		
		// Create the layout
		
	    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
	    gridData.horizontalSpan= 2;
	    this.setLayoutData(gridData);
	    this.setLayout(new GridLayout(2, false));
	    
	    // Create table with saved feeds (shows the saved feeds)
		
		final Table table = new Table(this, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
	    table.setLinesVisible(true);
	    table.setHeaderVisible(true);
	    GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
	    data.heightHint = 200;
	    table.setLayoutData(data);

	    
	     TableColumn column = new TableColumn(table, SWT.FILL);
	     column.setText("Feed");
	     column.pack();
	    
	    List<String> feedList = RSSConfigManager.defaultManager().getFeeds();
	    
	    for (String string : feedList) {
	    	TableItem item = new TableItem(table, SWT.NONE);
		      item.setText (0,string);
		}
	    
		table.getColumn(0).pack();
		
		
		//Buttons staff
	    //Create the buttons layout
		
	    Composite buttonsPlace = new Composite(this, SWT.NONE);
	    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
	    buttonsPlace.setLayoutData(gridData);
	    buttonsPlace.setLayout(new GridLayout(1, false));
	    
	    //Create add button (Add a new feed to the table and also to the config file
	    
	    Button addButton = new Button(buttonsPlace, SWT.NONE);
	    addButton.setText("Add");
	    addButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				showAddDialog();
				
				if (dialogCloseResult != null) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, dialogCloseResult);
					
					RSSConfigManager.defaultManager().addFeed(dialogCloseResult);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    //Create remove button (Remove all selected feeds (rows) from the table
	    //and also delete from the config file
	    
	    Button removeButton = new Button(buttonsPlace, SWT.NONE);
	    removeButton.setText("Remove");
	    removeButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				TableItem selectedItems[] = table.getSelection();
				
				ArrayList<String> list = new ArrayList<String>();
				
				for (TableItem item : selectedItems) {
					
					list.add(item.getText());
					table.remove(table.indexOf(item));
				}
				
				if (list.size() > 0) {
					
					RSSConfigManager.defaultManager().removeFeeds(list);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
	    
	    
	    //Separator
	    new Label(buttonsPlace, SWT.SEPARATOR | SWT.HORIZONTAL);
	    
	    
	    // Create select all button (Select all row of the table)
	    
	    Button selectAllButton = new Button(buttonsPlace, SWT.NONE);
	    selectAllButton.setText("Select all");
	    selectAllButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				table.selectAll();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
	    
	    
	    //Create deselect all button (deselect all selected row from the table)
	    
	    Button deselectAll = new Button(buttonsPlace, SWT.NONE);
	    deselectAll.setText("Deselect all");
	    deselectAll.addSelectionListener(new SelectionListener() {
			
	    	@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				table.deselectAll();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
	}
	
	
	// Show a dialog when the user wants to add new feed. 
	//If she/he wants to add it, it will add to the table and also save
	//to the config file.
	
	public String showAddDialog() {
		
	    Shell parent = (Shell) getParent();
	    final Shell shell =
	      new Shell(parent, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
	    shell.setText("Add new feed");

	    shell.setLayout(new GridLayout(2, true));

	    Label label = new Label(shell, SWT.NULL);
	    label.setText("Please enter a valid rss feed URL:");

	    final Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);

	    final Button buttonOK = new Button(shell, SWT.PUSH);
	    buttonOK.setText("Ok");
	    buttonOK.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
	    Button buttonCancel = new Button(shell, SWT.PUSH);
	    buttonCancel.setText("Cancel");

	    text.addListener(SWT.Modify, new Listener() {

		@Override
		public void handleEvent(Event arg0) {

			 try {
		          dialogCloseResult = text.getText();
		          buttonOK.setEnabled(true);
		        } catch (Exception e) {
		          buttonOK.setEnabled(false);
		        }
		}
	    });

	    buttonOK.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event event) {
	        shell.dispose();
	      }
	    });

	    buttonCancel.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event event) {
	        dialogCloseResult = null;
	        shell.dispose();
	      }
	    });
	    
	    shell.addListener(SWT.Traverse, new Listener() {
	      public void handleEvent(Event event) {
	        if(event.detail == SWT.TRAVERSE_ESCAPE)
	          event.doit = false;
	      }
	    });

	    text.setText("");
	    shell.pack();
	    shell.open();

	    Display display = parent.getDisplay();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }

	    return dialogCloseResult;
	  }
}
