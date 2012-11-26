package hu.ppke.itk.itkStock.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import java.util.ResourceBundle;

public class LogInComposite extends Composite{

	 private Label label1,label2;
	 private Text username;
	 private Text password;
	 private Text text;
	 private boolean islog=false;
	
	public LogInComposite(Composite parent,int style) {
		
		super(parent, style);
		// TODO Auto-generated constructor stub
		//this.setLayout(new GridLayout(2, false));
		this.addKeyListener(new MyKeyListener());
		    this.pack();
		    setLayout(new FormLayout());
		    Composite composite = new Composite(this, SWT.NONE);
		    composite.setLayoutData(new FormData());
		    GridLayout gl_composite = new GridLayout(2, false);
		    composite.setLayout(gl_composite);
		    label1=new Label(composite, SWT.NONE);
		    label1.setBounds(5, 8, 64, 15);
		    label1.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("LogInComposite.label1.text")); //$NON-NLS-1$ //$NON-NLS-2$
		    label1.pack();
		    
		    username = new Text(composite, SWT.SINGLE | SWT.BORDER);
		    username.setBounds(74, 5, 76, 21);
		    username.setText("");
		    username.setTextLimit(30);
		    username.setFocus();
		    
		    	username.addKeyListener(new MyKeyListener());
		    label2=new Label(composite, SWT.NONE);
		    label2.setBounds(5, 34, 56, 15);
		    label2.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("LogInComposite.label2.text")); //$NON-NLS-1$ //$NON-NLS-2$
		    label2.pack();
		    password = new Text(composite, SWT.SINGLE | SWT.BORDER);
		    password.setBounds(74, 31, 76, 21);
		    System.out.println(password.getEchoChar());
		    password.setEchoChar('*');
		    password.setTextLimit(30);
		    password.addKeyListener(new MyKeyListener());
		    Button button=new Button(composite,SWT.PUSH);
		    button.setBounds(5, 57, 50, 25);
		    button.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("LogInComposite.button.text")); //$NON-NLS-1$ //$NON-NLS-2$
		    new Label(composite, SWT.NONE);
		    
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
	}
	

	private class MyKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
			if(arg0.keyCode==13){
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
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
		}
	}
}

