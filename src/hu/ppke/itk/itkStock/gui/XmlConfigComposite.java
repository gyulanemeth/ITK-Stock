package hu.ppke.itk.itkStock.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class XmlConfigComposite extends Composite{
	
	//Configuring xml file's name:
	private String xfile= "ClientConfig.xml";
	private SAXBuilder builder;
	private Document document;
	private Element rootElement;
	
	private Text textInput_hostAddress;
	private Text textInput_hostPort;
	private Label info;
	
	private String actualPort;
	private String actualHost;
	private String actualLang;

	public XmlConfigComposite(Composite parent, int style) {
		super(parent, style);
		
		parseXML(xfile);
		
		Label label_title = new Label(this, SWT.CENTER);
		label_title.setSize(430, 15);
		label_title.setLocation(10, 10);
		label_title.setText("Connection and language settings");
		label_title.pack();
		
		Group groupConnection = new Group(this, SWT.NONE);
		groupConnection.setBounds(10, 31, 430, 96);
		
		Label host_address = new Label(groupConnection, SWT.NONE);
		host_address.setBounds(10, 24, 112, 15);
		host_address.setText("Host address:");
		
		textInput_hostAddress = new Text(groupConnection, SWT.BORDER);
		textInput_hostAddress.setBounds(128, 21, 176, 21);
		
		Label port = new Label(groupConnection, SWT.NONE);
		port.setBounds(10, 62, 112, 15);
		port.setText("Host port:");
		
		textInput_hostPort = new Text(groupConnection, SWT.BORDER);
		textInput_hostPort.setBounds(128, 56, 76, 21);
		
		Group groupLanguage = new Group(this, SWT.NONE);
		groupLanguage.setBounds(10, 133, 430, 101);
		
		Label label_lang = new Label(groupLanguage, SWT.NONE);
		label_lang.setBounds(10, 30, 156, 15);
		label_lang.setText("Set language and country:");
		
		ToolBar toolBar = new ToolBar(groupLanguage, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(10, 50, 142, 23);
		
		final ToolItem item = new ToolItem(toolBar, SWT.DROP_DOWN);
		item.setText("English - International");
		
		DropdownSelectionListener listenerOne = new DropdownSelectionListener(item);
	    listenerOne.add("English - United States");
	    listenerOne.add("Hungarian");
	    listenerOne.add("Slovakian");
	    item.addSelectionListener(listenerOne);
		
		
		toolBar.pack();
		
		Button btnSave = new Button(this, SWT.NONE);
		btnSave.setBounds(200, 246, 50, 25);
		btnSave.setText("Save");
		
		info = new Label(this, SWT.NONE);
		info.setBounds(126, 277, 200, 15);
		
		btnSave.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if(isNumeric(textInput_hostPort.getText()) && textInput_hostAddress.getText() != null && textInput_hostPort.getText() != null){
				updateXML(textInput_hostAddress.getText(), 
						  textInput_hostPort.getText(), 
						  item.getText());
				} else {
					if(!isNumeric(textInput_hostPort.getText())){
						info.setText("Port is not a number!");
					} else if(textInput_hostAddress.getText() != null && textInput_hostPort.getText() != null) {
						info.setText("Please fill out all the fields!");
					}
					
				}
			}
		});
		
		
	}
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Integer.parseInt(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}

	public void parseXML(String source){
		
		try {
			builder = new SAXBuilder();
			document = builder.build(xfile);
			rootElement = document.getRootElement();
			
			for(Element act : rootElement.getChildren()){
				if (act.getName().equals("connection")){
					//szupi
					setActualPort(act.getChild("port").getText());
					setActualHost(act.getChild("hostname").getText());
				} 
			}
			
		} catch (Exception e) {
			e.getStackTrace();
		}
		
	}
	
	public void updateXML(String host, String portNumber, String lang){
		//update host if needed
		if(getActualHost().equals(host)){
			//do nothing
			//jelenleg is ugyanaz a nyelv vagyon belõve, nincs szükség módosításra
		} else {
			Element hostElement = rootElement.getChild("connection").getChild("hostname");
			hostElement.setText(host);
			System.out.println(hostElement.getText());
		}
		
		//update port if needed
		if(getActualPort() == portNumber){
			//do nothing as not needed
		} else {
			Element portElement = rootElement.getChild("connection").getChild("port");
			portElement.setText(""+portNumber);
		}
		
		//update lang if needed
		//if(actualLang.equals(lang)){
			//do nothing
		//} else{
			//még nem felvett attribútúm!!!
			//Element langElement = rootElement.getChild("lang");
			//langElement.setText(lang);
		//}
		
		XMLOutputter xmlOutput = new XMLOutputter();
		 
		xmlOutput.setFormat(Format.getPrettyFormat());
		try {
			xmlOutput.output(document, new FileWriter(xfile));
			info.setText("Config Saved!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getActualPort() {
		return actualPort;
	}

	public void setActualPort(String actualPort) {
		this.actualPort = actualPort;
	}

	public String getActualHost() {
		return actualHost;
	}

	public void setActualHost(String actualHost) {
		this.actualHost = actualHost;
	}



	class DropdownSelectionListener extends SelectionAdapter {
		  private ToolItem dropdown;
		  private Menu menu;
		  
		  public DropdownSelectionListener(ToolItem dropdown) {
		    this.dropdown = dropdown;
		    menu = new Menu(dropdown.getParent().getShell());
		  }

		  public void add(String item) {
		    MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		    menuItem.setText(item);
		    menuItem.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		        MenuItem selected = (MenuItem) event.widget;
		        dropdown.setText(selected.getText());
		      }
		    });
		  }

		  public void widgetSelected(SelectionEvent event) {
		    if (event.detail == SWT.ARROW) {
		      ToolItem item = (ToolItem) event.widget;
		      Rectangle rect = item.getBounds();
		      Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
		      menu.setLocation(pt.x, pt.y + rect.height);
		      menu.setVisible(true);
		    } else {
		      System.out.println(dropdown.getText() + " Pressed");
		    }
		  }
		}
}

