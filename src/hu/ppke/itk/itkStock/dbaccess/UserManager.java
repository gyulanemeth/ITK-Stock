package hu.ppke.itk.itkStock.dbaccess;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserManager extends AbstractManager<User>
{	
	private PreparedStatement addUser = null;
	private PreparedStatement removeUser = null;
	private PreparedStatement updateUser = null;
	private PreparedStatement getUserByName = null;
	private PreparedStatement getUserById = null;
	private PreparedStatement setPassword = null;
	private PreparedStatement setEmail = null;
	private PreparedStatement setUsername = null;
	private PreparedStatement authenticateUser = null;
	private PreparedStatement promoteAdmin = null;
	private PreparedStatement demoteAdmin = null;
	
	public UserManager( DatabaseConnector dbConnector ) throws SQLException
	{
		super(dbConnector);
		
		if ( this.dbConnector == null || !this.dbConnector.isInitialized() )
			throw new SQLException("DatabaseConnector is not initialized.");
		
		this.addUser = this.dbConnector.prepareStatement("INSERT INTO users ( username, email, password ) VALUES ( ?, ?, ? )");
		this.removeUser = this.dbConnector.prepareStatement("DELETE FROM users WHERE username = ?");
		this.updateUser = this.dbConnector.prepareStatement("UPDATE users SET username = ?, email = ?, password = ?, is_admin = ? WHERE id = ?");
		this.getUserByName = this.dbConnector.prepareStatement("SELECT ( id, username, email, password, id_admin ) FROM users WHERE username = ?");
		this.getUserById = this.dbConnector.prepareStatement("SELECT ( id, username, email, password, id_admin ) FROM users WHERE id = ?");
		this.setPassword = this.dbConnector.prepareStatement("UPDATE users SET password = ? WHERE username = ?");
		this.setEmail = this.dbConnector.prepareStatement("UPDATE users SET email = ? WHERE username = ?");
		this.setUsername = this.dbConnector.prepareStatement("UPDATE users SET username = ? WHERE username = ?");
		this.authenticateUser = this.dbConnector.prepareStatement("SELECT IF( ( SELECT COUNT( * ) FROM users WHERE username = ? AND PASSWORD = ? ) = 0, FALSE, TRUE )");
		this.promoteAdmin = this.dbConnector.prepareStatement("UPDATE users SET is_admin = 1 WHERE username = ?");
		this.demoteAdmin = this.dbConnector.prepareStatement("UPDATE users SET is_admin = 0 WHERE username = ?");
		
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
	
	public void promoteAdmin(String username) throws SQLException
	{
		this.promoteAdmin.setString(1, username);
		this.promoteAdmin.executeUpdate();
	}
	
	public void demoteAdmin(String username) throws SQLException
	{
		this.demoteAdmin.setString(1, username);
		this.demoteAdmin.executeUpdate();
	}

	@Override
	public void update(User businessObject) throws SQLException
	{
		this.updateUser.setString(1, businessObject.getUsername());
		this.updateUser.setString(2, businessObject.getEmail());
		this.updateUser.setString(3, businessObject.getPassword());
		this.updateUser.setInt(4, businessObject.isAdmin() ? 1 : 0);
		this.updateUser.executeUpdate();
	}

	@Override
	public User get(int id) throws SQLException
	{
		this.getUserById.setInt(1, id);
		this._resultSet = this.getUserById.executeQuery();
		
		if ( !this._resultSet.first() )
			return null;
		
		User tempuser = new User(this, id);
		
		tempuser.setData
					( this._resultSet.getInt(1)
					, this._resultSet.getString(2)
					, this._resultSet.getString(3)
					, this._resultSet.getString(4)
					, this._resultSet.getBoolean(5) );
		
		return tempuser;
	}
	
	public User get(String username) throws SQLException
	{
		this.getUserByName.setString(1, username);
		this._resultSet = this.getUserById.executeQuery();
		
		if ( !this._resultSet.first() )
			return null;
		
		User tempuser = new User(this, username);
		
		tempuser.setData
					( this._resultSet.getInt(1)
					, this._resultSet.getString(2)
					, this._resultSet.getString(3)
					, this._resultSet.getString(4)
					, this._resultSet.getBoolean(5) );
		
		return tempuser;
	}

}
