package hu.ppke.itk.itkStock.dbaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Ez az osztaly valositja meg az adatbazis kapcsolatot.
 * A konstruktor meghivasa nem elegendo a kapcsolat kiepitesehez.
 * Szukseges az 'initConnection()' tagfuggveny meghivasa.
 * @see #initConnection()
 * A kapcsolat lezarasahoz a 'closeConnection()' fuggvenyt hivjuk meg.
 * Ilyenkor az adatbazis csatlakozas lezarasa mellett felszabadulnak a 'PreparedStatement'-jeink is.
 * @see #closeConnection()
 * @see #_connection
 * @see #preparedStatements
 */

public class DatabaseConnector
{
	private static final int defaultPortnumber = 8889;
	private static final String defaultUsername = "root";
	private static final String defaultPassword = "root";
	private static final String defaultServerAddr = "localhost";
	private static final String defaultDatabase = "test";
	
	private int portnumber = defaultPortnumber;
	private String username = new String(defaultUsername);
	private String password = new String(defaultPassword);
	private String serverAddr = new String(defaultServerAddr);
	private String database = new String(defaultDatabase);
	
	private ArrayList<PreparedStatement> preparedStatements = new ArrayList<PreparedStatement>();
	
	private Connection _connection = null;
	
	private boolean initialized = false;
	
	private String url;
	
	public DatabaseConnector()
	{
		this.buildUrl();
	}
	
	public DatabaseConnector
				( int portnumber
				, String username
				, String password
				, String serverAddr
				, String database )
	{
		this.portnumber = portnumber;
		this.username = new String(username);
		this.password = new String(password);
		this.serverAddr = new String(serverAddr);
		this.database = new String(database);
		
		this.buildUrl();
	}
	
	private void buildUrl()
	{
		this.url = "jdbc:mysql://" + this.serverAddr + ":" + this.portnumber + "/" + this.database;
	}
	
	public synchronized void initConnection() throws SQLException, ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver");
		this._connection = DriverManager.getConnection(this.url,this.username,this.password);
		this.initialized = true;
	}
	
	public synchronized void closeConnection() throws SQLException
	{
		if ( !this.initialized )
			return;
		
		for ( PreparedStatement ps : this.preparedStatements )
		{
			if ( ps != null )
				ps.close();
		}
		
		if ( this._connection != null )
			this._connection.close();
	}
	
	public PreparedStatement prepareStatement( String sqlString ) throws SQLException
	{
		if ( this.initialized )
		{
			PreparedStatement temp =  this._connection.prepareStatement( sqlString );
			this.preparedStatements.add(temp);
			return temp;
		}
		
		return null;
	}
	
	protected void finalize() throws Throwable
	{
		this.closeConnection();
		super.finalize();
	}
	
	public boolean isInitialized()
	{
		return this.initialized;
	}
}
