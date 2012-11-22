package hu.ppke.itk.itkStock.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * A settings singleton for the client.<br>
 * If any setting changes, the settings file will be reparsed entirely.
 *
 */

public enum ClientSettings {
	INSTANCE;
	
	private int    port;
	private String hostname;
	
	/**
	 * Consturctor<br>
	 * Invokes load() method to get the settings from the 'ClientConfig.xml' file<br>
	 * If file not present, generates default values to default 'ServerConfig.xml' file
	 */
	private ClientSettings()
	{
		load();
	}
	
	/**
	 * This methods load the configuration file, if not present, generates one with default values.
	 */
	private void load()
	{
		try {			
			parseFile("ClientConfig.xml");			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("File not found!\nGenerating default values to \'ClientConfig.xml\'");
			generateDefualt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method parses XML config file with given nodes. Only the given nodes will be parsed, <br>
	 * they have to be in a specific order defined in the XML Scheme. Although it does not matter for <br>
	 * the parser.<br>
	 * 
	 * @param filename The name of the file to be parsed
	 * @throws JDOMException
	 * @throws IOException
	 */
	private void parseFile(String filename) throws JDOMException, IOException
	{
		SAXBuilder builder = new SAXBuilder(/*XMLReaders.XSDVALIDATING*/);
		Document doc = builder.build(filename);
		Element root = doc.getRootElement();

		port   = xmlGetPort(root);
		hostname = xmlGetHostname(root);
	}
	
	/**
	 * This method generates the default values for each option.<br>
	 * The default values will be parsed to an XML file, and reopened for reloading configuration.
	 */
	private void generateDefualt()
	{
		System.out.println("Generating default values...");
		
		Element root = makeRoot();
		
		//TODO generate default values to config file
		root.addContent(new Element("connection")
		     .addContent(new Element("port").setText("3306"))
		     .addContent(new Element("hostname").setText("localhost")));
		
		makeFile(root);
		
		System.out.println("Default configuration generated. Reloading...");
		
		try {
			parseFile("ClientConfig.xml");
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Makes a new configuration file. This method invokes when change has been made to any of the data
	 */
	private void reparseConfig()
	{
		//TODO remove this, following line debug only
		System.out.println("reparsing configuration file...");
		
		Element root = makeRoot();
		
		root.addContent(new Element("connection")
		    .addContent(new Element("port").setText(Integer.toString(port)))
		    .addContent(new Element("hostname").setText("localhost")));
		
		makeFile(root);
	}
	
	/**
	 * Makes the default root element for the configuration. This adds the namespace and the XML-Scheme as well.
	 * @return The root configuration element
	 */
	private Element makeRoot()
	{
		Element root = new Element("client_config", "xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute(new Attribute("noNamespaceSchemaLocation",
				"ClientConfig.xsd", Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance")));
		
		return root;
	}
	
	/**
	 * Makes the configuration XML file from root element
	 * @param root The root element of the document
	 */
	private void makeFile(Element root)
	{
		Document doc = new Document();
		doc.addContent(root);

		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		try {
			File f = new File("ClientConfig.xml");
			PrintWriter pw = new PrintWriter(f);
			out.output(doc, pw);
			out.output(doc, System.out); //TODO debug only line
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	//xml data-getter methods
	private int xmlGetPort(Element root)
	{
		return Integer.parseInt(root.getChild("connection")
				                    .getChild("port").getText());
	}
	private String xmlGetHostname(Element root)
	{
		return root.getChild("connection").getChildText("host");
	}

	//setter methods
	public void setPort(int dbPort) {
		this.port = dbPort;
		reparseConfig();
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
		reparseConfig();
	}
	
	//getter methods
	public int getPort() {
		return port;
	}
	public String getHostname() {
		return hostname;
	}
	
	//toString method, debug purpose only
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("==ClientConfig==\n");
		sb.append("port: ").append(Integer.toString(port));
		sb.append(", hostname: ").append(hostname);
		sb.append("\n================");
		return sb.toString();
	}

}
