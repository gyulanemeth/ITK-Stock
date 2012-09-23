package hu.ppke.itk.itkStock.dbaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager
{
	private DatabaseConnector dbConnector;
	private volatile ResultSet _resultSet = null;
	
	private PreparedStatement addUser = null;
	private PreparedStatement removeUser = null;
	private PreparedStatement setPassword = null;
	private PreparedStatement setEmail = null;
	private PreparedStatement setUsername = null;
	private PreparedStatement authenticateUser = null;
	
	public UserManager( DatabaseConnector dbConnector ) throws SQLException
	{
		this.dbConnector = dbConnector;
		
		if ( this.dbConnector == null || !this.dbConnector.isInitialized() )
			throw new SQLException("DatabaseConnector is not initialized.");
		
		this.addUser = this.dbConnector.prepareStatement("INSERT INTO users ( username, email, password ) VALUES ( ?, ?, ? )");
		this.removeUser = this.dbConnector.prepareStatement("DELETE FROM users WHERE username = ?");
		this.setPassword = this.dbConnector.prepareStatement("UPDATE users SET password = ? WHERE username = ?");
		this.setEmail = this.dbConnector.prepareStatement("UPDATE users SET email = ? WHERE username = ?");
		this.setUsername = this.dbConnector.prepareStatement("UPDATE users SET username = ? WHERE username = ?");
		this.authenticateUser = this.dbConnector.prepareStatement("SELECT IF( ( SELECT COUNT( * ) FROM users WHERE username = ? AND PASSWORD = ? ) = 0, FALSE, TRUE )");
		
	}
	
	public void addUser(String username, String email, String password) throws SQLException
	{
		this.addUser.setString(1, username);
		this.addUser.setString(2, email);
		this.addUser.setString(3, password);
		this.addUser.executeUpdate();
	}
	
	public void removeUser(String username) throws SQLException
	{
		this.removeUser.setString(1, username);
		this.removeUser.executeUpdate();
	}
	
	public void setPassword(String username, String password) throws SQLException
	{
		this.setPassword.setString(1, password);
		this.setPassword.setString(2, username);
		this.setPassword.executeUpdate();
	}
	
	public void setEmail(String username, String email) throws SQLException
	{
		this.setEmail.setString(1, email);
		this.setEmail.setString(2, username);
		this.setEmail.executeUpdate();
	}
	
	public void setUsername(String oldusername, String newusername) throws SQLException
	{
		this.setUsername.setString(1, newusername);
		this.setUsername.setString(2, oldusername);
		this.setUsername.executeUpdate();
	}
	
	public boolean authenticateUser(String username, String password) throws SQLException
	{
		this.authenticateUser.setString(1, username);
		this.authenticateUser.setString(2, password);
		this._resultSet = this.authenticateUser.executeQuery();
		this._resultSet.first();
		
		return this._resultSet.getBoolean(1); 
	}

}
