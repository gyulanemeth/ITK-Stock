package hu.ppke.itk.itkStock.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class LogInComposite extends Composite{

	 private Label label1,label2;
	 private Text username;
	 private Text password;
	 private Text text;
	 private boolean islog=false;
	
	public LogInComposite(Composite parent,int style) {
		
		super(parent, style);
		// TODO Auto-generated constructor stub
		this.setLayout(new GridLayout(2, false));
		label1=new Label(this, SWT.NONE);
		label1.setText("User Name: ");
	    
	    username = new Text(this, SWT.SINGLE | SWT.BORDER);
	    username.setText("");
	    username.setTextLimit(30);
	    label2=new Label(this, SWT.NONE);
		label2.setText("Password: ");
	    
	    password = new Text(this, SWT.SINGLE | SWT.BORDER);
	    System.out.println(password.getEchoChar());
	    password.setEchoChar('*');
	    password.setTextLimit(30);
	    Button button=new Button(this,SWT.PUSH);
		button.setText("Submit");
		button.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event event) {
				  String selected=username.getText();
		          String selected1=password.getText();
				  
			if(selected==""){ 
				MessageBox messageBox = new MessageBox(getShell(), SWT.OK |SWT.ICON_WARNING |SWT.CANCEL);
					messageBox.setMessage("Enter the User Name");
					 messageBox.open();
			}
			if(selected1==""){
		             MessageBox messageBox = new MessageBox(getShell(), SWT.OK |SWT.ICON_WARNING |SWT.CANCEL);
					messageBox.setMessage("Enter the Password");
					 messageBox.open();
					 }
			if(selected!="" && selected1 != "")
			{
			MessageBox messageBox = new MessageBox(getShell(), SWT.OK | SWT.CANCEL);
		        messageBox.setText("Login Form");
				messageBox.setMessage("Welcome:" + username.getText());
		        messageBox.open();
		       MainGui.initializeMenu();
		       MainGui.layout.topControl = MainGui.mainMenuComposite;
		       getParent().layout();
				}
				}
			});
		
		    username.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		    password.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		    label1.pack();
		    label2.pack();
		    this.pack();
	}

}
