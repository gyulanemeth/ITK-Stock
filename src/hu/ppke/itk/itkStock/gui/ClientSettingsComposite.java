package hu.ppke.itk.itkStock.gui;

import java.util.ResourceBundle;

import hu.ppke.itk.itkStock.client.ClientSettings;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class ClientSettingsComposite extends Composite{

	public ClientSettingsComposite(Composite parent, int style) {
		super(parent, style);
		
		Composite comp = new Composite(this, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));
		comp.setBounds(50,50, 100, 100);
		Label portText = new Label(comp, SWT.NONE);
		portText.setText("Port");
		final Text portGetText = new Text(comp, SWT.BORDER);
		portGetText.setText("");
		//portGetText.setText(""+ClientSettings.INSTANCE.getPort());
		Label hostText = new Label(comp, SWT.NONE);
		hostText.setText("Host");
		final Text hostGetText = new Text(comp, SWT.BORDER);
		hostGetText.setText("");
		//hostGetText.setText(ClientSettings.INSTANCE.getHostname());
		Button saveButton = new Button(comp, SWT.NONE);
		saveButton.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("ClientSettingsComposite.saveButton.text")); //$NON-NLS-1$ //$NON-NLS-2$
		saveButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				if(portGetText.getText() != ""){
					ClientSettings.INSTANCE.setPort(Integer.parseInt(portGetText.getText()));
				}
				if(hostGetText.getText() != ""){
					ClientSettings.INSTANCE.setHostname(hostGetText.getText());
				}
			}
		});
		saveButton.pack();
		portText.pack();
		portGetText.pack();
		hostGetText.pack();
		hostText.pack();
		comp.pack();
		
		// TODO Auto-generated constructor stub
	}

}
