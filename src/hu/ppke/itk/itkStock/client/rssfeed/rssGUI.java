package hu.ppke.itk.itkStock.client.rssfeed;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class rssGUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					rssGUI window = new rssGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public rssGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JScrollPane scrollPanefeeds = new JScrollPane();
		scrollPanefeeds.setPreferredSize(new Dimension(100, 100));
		frame.getContentPane().add(scrollPanefeeds, BorderLayout.CENTER);
		
		final JTextArea textArea = new JTextArea();
		scrollPanefeeds.setViewportView(textArea);
		
		JScrollPane scrollPanelist = new JScrollPane();
		scrollPanelist.setPreferredSize(new Dimension(100, 100));
		frame.getContentPane().add(scrollPanelist, BorderLayout.WEST);
		
		final String[] data = {"Yahoo", "Arts", "Money", "World"};
		final String[] rssdata = {"http://news.yahoo.com/rss/",
								"http://feeds.reuters.com/news/artsculture",
								"http://feeds.reuters.com/news/wealth",
								"http://feeds.reuters.com/Reuters/worldNews"};
		final JList list = new JList(data);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				rssRead(rssdata[list.getSelectedIndex()],textArea);
			}
		});
		scrollPanelist.setViewportView(list);
		
	}

	protected void rssRead(String u, final JTextArea textArea) {
		try {
			final URL url = new URL(u);
			
			new Thread(){
				public void run()
				{
					textArea.setText("Loading...");
					try
					{
					XmlReader reader = new XmlReader(url);
					SyndFeed feed = new SyndFeedInput().build(reader);
					String tmp="";
					for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
				        SyndEntry entry = (SyndEntry) i.next();
				        tmp+=entry.getTitle();
				        tmp+="\n";
				    }
					textArea.setText(tmp);
					}
					catch(Exception e){}
				}
			}.start();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
