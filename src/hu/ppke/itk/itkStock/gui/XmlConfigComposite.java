package hu.ppke.itk.itkStock.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;


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
		Locale l = new Locale("hu", "HU"); 
		actualLang="hu_HU";
		
		Label label_title = new Label(this, SWT.CENTER);
		label_title.setSize(430, 15);
		label_title.setLocation(10, 10);
		label_title.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("XmlConfigComposite.label_title.text")); //$NON-NLS-1$ //$NON-NLS-2$
		label_title.pack();
		
		Group groupConnection = new Group(this, SWT.NONE);
		groupConnection.setBounds(10, 31, 430, 96);
		
		Label host_address = new Label(groupConnection, SWT.NONE);
		host_address.setBounds(10, 24, 112, 15);
		host_address.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("XmlConfigComposite.host_address.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		textInput_hostAddress = new Text(groupConnection, SWT.BORDER);
		textInput_hostAddress.setBounds(128, 21, 176, 21);
		
		Label port = new Label(groupConnection, SWT.NONE);
		port.setBounds(10, 59, 112, 15);
		port.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("XmlConfigComposite.port.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		textInput_hostPort = new Text(groupConnection, SWT.BORDER);
		textInput_hostPort.setBounds(128, 56, 76, 21);
		
		Group groupLanguage = new Group(this, SWT.NONE);
		groupLanguage.setBounds(10, 133, 430, 101);
		
		Label label_lang = new Label(groupLanguage, SWT.NONE);
		label_lang.setBounds(10, 30, 160, 15);
		label_lang.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("XmlConfigComposite.label_lang.text"));
		
		final Combo languagesDropDownList = new Combo(groupLanguage, SWT.DROP_DOWN);
		languagesDropDownList.setLocation(10, 50);
		/*
		for (int i = 0; i < 4; i++) {
			languagesDropDownList.add("item"+i);
		}*/
		
		languagesDropDownList.pack();
		
		for(String s : listLangs()){
			languagesDropDownList.add(s);
		}
	    
		Button btnSave = new Button(this, SWT.NONE);
		btnSave.setBounds(200, 246, 50, 25);
		btnSave.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("XmlConfigComposite.btnSave.text"));
		
		info = new Label(this, SWT.NONE);
		info.setBounds(126, 277, 200, 15);
		
		btnSave.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if(isNumeric(textInput_hostPort.getText()) && textInput_hostAddress.getText() != null && textInput_hostPort.getText() != null){
				updateXML(textInput_hostAddress.getText(), 
						  textInput_hostPort.getText(), 
						  languagesDropDownList.getText());
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
			//jelenleg is ugyanaz a nyelv vagyon belőve, nincs szükség  módosításra
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
		if(actualLang.equals(lang)){
			//do nothing
		} else{
			Element langElement = rootElement.getChild("localization").getChild("language");
			langElement.setText(lang);
		}
		
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
	/**
	 * 
	 * @return (LinkedList) language files
	 */
	public LinkedList<String> listLangs(){
		
		String path = ".\\src\\hu\\ppke\\itk\\itkStock\\gui";
		
		String files;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles(); 
		
		LinkedList<String> langFiles = new LinkedList<String>();
		LinkedList<String> temp = new LinkedList<String>();
		
		for (int i = 0; i < listOfFiles.length; i++){
			
			if (listOfFiles[i].isFile()){
				files = listOfFiles[i].getName();
				if (files.endsWith(".properties")){
		          langFiles.add(files);
		        }
		    }
		}
		
		for (String s : langFiles) {
			temp.add(s.substring(0, s.indexOf('.')));
		}
		
		langFiles.clear();
		
		for (String s : temp) {
			langFiles.add(s.substring(s.length()-5));
		}
		
		langFiles.add(langFiles.indexOf("sages")+1, "default");
		langFiles.remove("sages");
		return langFiles;
	}
	
	/**
	 * Public Getter - Gets the actual port number
	 * @return (String) actualPort
	 */
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
	
	
}

