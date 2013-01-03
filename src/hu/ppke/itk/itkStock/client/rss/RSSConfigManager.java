package hu.ppke.itk.itkStock.client.rss;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class RSSConfigManager {
	
	//Singleton pattern 
	//Use this to access feeds!!
	
	private static RSSConfigManager singleton = null;
	
	public static RSSConfigManager defaultManager(){
		
		if (singleton == null) {
			
			singleton = new RSSConfigManager();
		}
		
		return singleton;
	}
	
	
// 	*************** Possible methods: ***************
	
//	public  List<String> getFeeds();  unmodifiable list!!
	
	
// !! ALL ADD OR REMOVE METHOD WILL SAVE THE STATE IMMEDIATLY !!
	
//	public void addFeed(String feed);
//	public void addFeeds(List<String> feedsToAdd);
//	public void removeFeed(String feed);
//	public void removeFeeds(List<String> feedsToRemove);
	
	
	
	
	// Instanse stuff
	
	private List<String> feeds;
	private Document configFile;
	
	
	// Default constructor, this will load all feeds from the cofnig file
	private RSSConfigManager(){
		
		feeds = new ArrayList<String>();
		
		String fileName = "ClientConfig.xml";
		SAXBuilder builder = new SAXBuilder(/*XMLReaders.XSDVALIDATING*/);
		
		try {
			
			configFile = builder.build(fileName);
			readRSSFeeds();
			
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Config file error!");
		}
	}
	
	
	// !! ALL ADD AND REMOVE METHOD WILL SAVE THE STATE IMMEDIATLY !!
	
	//Add a single feed
	public void addFeed(String feed){
		
		if (feeds.contains(feed)) {
			return;
		}
		
		feeds.add(feed);
		saveFeeds();
	}
	
	//Add feed(s) from arraylist
	public void addFeeds(List<String> feedsToAdd){
		
		for (String feed : feedsToAdd) {
			
			if (feeds.contains(feed)) {
				continue;
			}
			
			feeds.add(feed);
		}
		
		saveFeeds();
	}
	
	//Remove a single feed
	public void removeFeed(String feed){
		
		if (!feeds.contains(feed)) {
			return;
		}
		
		feeds.remove(feed);
		saveFeeds();
	}
	
	//Remove feed(s) from arraylist
	public void removeFeeds(List<String> feedsToRemove){
		
		for (String feed : feedsToRemove) {
			
			if (!feeds.contains(feed)) {
				continue;
			}
			
			feeds.remove(feed);
		}
		
		saveFeeds();
	}

	
	//Read feeds from the config file
	private void readRSSFeeds(){
		
		System.out.println("Loading feed(s)...");
		
		Element root = configFile.getRootElement();
			
			Element rssNode = root.getChild("rss");
			
			if (rssNode != null) {
				
				List<Element> feedList = rssNode.getChildren();
				
				for (Element element : feedList) {
					
					feeds.add(element.getText());
				}
			}
			
		System.out.println(feeds.size() + " feed(s) loaded.");
	}
	
	
	//Save the actual state to the config file
	private void saveFeeds(){
		
		System.out.println("Saving feed(s)...");
		
		Element root = configFile.getRootElement();
		Element newRoot = root.clone();
		newRoot.removeChild("rss");
		
		Element feedList = new Element("rss");
		for (String string : feeds) {
			
			feedList.addContent(new Element("feed").setText(string));
		}
		
		newRoot.addContent(feedList);
		
		Document doc = new Document();
		doc.addContent(newRoot);

		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		try {
			File f = new File("ClientConfig.xml");
			PrintWriter pw = new PrintWriter(f);
			out.output(doc, pw);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("Feed(s) saved.");
	}
	
	
	
	//Getters
	public  List<String> getFeeds() {
		
		return Collections.unmodifiableList(feeds);
	}
}
