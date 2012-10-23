package hu.ppke.itk.itkStock.server.db.user;

import hu.ppke.itk.itkStock.server.db.dbAccess.AbstractManager;
import hu.ppke.itk.itkStock.server.db.dbAccess.BusinessObjectException;
import hu.ppke.itk.itkStock.server.db.dbAccess.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Ez a 'Manager' osztaly vegzi el a 'User (extends BusinesObject)' objektumok kezeleset. 'dbConnector' tagvaltozojan keresztul kapcsolodik az adatbazishoz.
 * Tartalmazza az osszes felhasznalok kezelesehez szukseges eljarast, 'PreparedStatement'-ek kent.
 * 'Manager' osztalykent funkcional.
 *
 * @see AbstractManager
 * @see DatabaseConnector
 * @see PreparedStatement
 *
 */

public class UserManager extends AbstractManager<User>
{
	private PreparedStatement addUser = null;
	private PreparedStatement removeUser = null;
	private PreparedStatement updateUser = null;
	private PreparedStatement getUserByName = null;
	private PreparedStatement getUserById = null;
	private PreparedStatement setPassword = null;
	private PreparedStatement setEmail = null;
	private PreparedStatement setMoney = null;
	private PreparedStatement getMoney = null;
	private PreparedStatement setUsername = null;
	private PreparedStatement authenticateUser = null;
	private PreparedStatement checkUserExistenceByName = null;
	private PreparedStatement checkUserExistenceById = null;
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
		this.getUserByName = this.dbConnector.prepareStatement("SELECT id, username, email, password, is_admin FROM users WHERE username = ?");
		this.getUserById = this.dbConnector.prepareStatement("SELECT id, username, email, password, is_admin FROM users WHERE id = ?");
		this.setPassword = this.dbConnector.prepareStatement("UPDATE users SET password = ? WHERE username = ?");
		this.setEmail = this.dbConnector.prepareStatement("UPDATE users SET email = ? WHERE username = ?");
		this.setUsername = this.dbConnector.prepareStatement("UPDATE users SET username = ? WHERE username = ?");
		this.authenticateUser = this.dbConnector.prepareStatement("SELECT IF( ( SELECT COUNT( * ) FROM users WHERE username = ? AND PASSWORD = ? ) = 0, FALSE, TRUE )");
		this.checkUserExistenceByName = this.dbConnector.prepareStatement("SELECT IF( ( SELECT COUNT( * ) FROM users WHERE username = ? ) = 0, FALSE, TRUE )");
		this.checkUserExistenceById = this.dbConnector.prepareStatement("SELECT IF( ( SELECT COUNT( * ) FROM users WHERE id = ? ) = 0, FALSE, TRUE )");
		this.promoteAdmin = this.dbConnector.prepareStatement("UPDATE users SET is_admin = 1 WHERE username = ?");
		this.demoteAdmin = this.dbConnector.prepareStatement("UPDATE users SET is_admin = 0 WHERE username = ?");
		this.setMoney = this.dbConnector.prepareStatement("UPDATE users SET money = ? WHERE username = ?");
		this.getMoney = this.dbConnector.prepareStatement("SELECT money FROM users WHERE username = ?");
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

	public void setMoney(String username, double money) throws SQLException
	{
		this.setMoney.setDouble(1, money);
		this.setMoney.setString(2, username);
		this.setMoney.executeUpdate();
	}

	public double getMoney(String username) throws SQLException
	{
		this.getMoney.setString(1, username);
		this._resultSet = this.getMoney.executeQuery();
		
		if ( this.resultSet.next() )
			return this._resultSet.getDouble(1);

		return 0.0D;
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
		this.resultSet = this.authenticateUser.executeQuery();
		this.resultSet.first();

		return this.resultSet.getBoolean(1);
	}

	public boolean checkUserExistenceByName(String username) throws SQLException
	{
		this.checkUserExistenceByName.setString(1, username);
		this.resultSet = this.checkUserExistenceByName.executeQuery();
		this.resultSet.first();

		return this.resultSet.getBoolean(1);
	}

	public boolean checkUserExistenceById(int id) throws SQLException
	{
		this.checkUserExistenceById.setInt(1, id);
		this.resultSet = this.checkUserExistenceById.executeQuery();
		this.resultSet.first();

		return this.resultSet.getBoolean(1);
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
	public void update(User bo) throws SQLException
	{
		this.updateUser.setString(1, bo.getUsername());
		this.updateUser.setString(2, bo.getEmail());
		this.updateUser.setString(3, bo.getPassword());
		this.updateUser.setInt(4, bo.isAdmin() ? 1 : 0);
		this.updateUser.executeUpdate();
	}

	@Override
	public User get(int id) throws SQLException, BusinessObjectException
	{
		if ( !this.checkUserExistenceById(id) )
			throw new BusinessObjectException("User with id = " + id + " does not exist.");

		this.getUserById.setInt(1, id);
		this.resultSet = this.getUserById.executeQuery();

		if ( !this.resultSet.first() )
			return null;

		User tempuser = new User(this, id);

		tempuser.setData
					( this.resultSet.getString(2)
					, this.resultSet.getString(3)
					, this.resultSet.getString(4)
					, this.resultSet.getBoolean(5) );

		return tempuser;
	}

	public User get(String username) throws SQLException, BusinessObjectException
	{
		if ( !this.checkUserExistenceByName(username) )
			throw new BusinessObjectException("User with username = " + username + " does not exist.");

		this.getUserByName.setString(1, username);
		this.resultSet = this.getUserByName.executeQuery();

		if ( !this.resultSet.first() )
			return null;

		User tempuser = new User(this, username);

		tempuser.setData
					( this.resultSet.getString(2)
					, this.resultSet.getString(3)
					, this.resultSet.getString(4)
					, this.resultSet.getBoolean(5) );

		tempuser.setId(this.resultSet.getInt(1));

		return tempuser;
	}

	@Override
	public void create(User bo) throws SQLException, BusinessObjectException
	{
		if ( bo.getId() != 0 && this.checkUserExistenceById(bo.getId()) )
			throw new BusinessObjectException("User with id = " + bo.getId() + " already exists.");

		if ( bo.getUsername() != null && this.checkUserExistenceByName(bo.getUsername()) )
			throw new BusinessObjectException("User with username = " + bo.getUsername() + " already exists.");

		this.addUser( bo.getUsername(), bo.getEmail(), bo.getPassword() );
		bo.get();
	}
}
