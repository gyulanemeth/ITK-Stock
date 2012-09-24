package hu.ppke.itk.itkStock.dbaccess;

import java.sql.SQLException;

public class User extends BusinessObject
{
	private int id;
	private String username;
	private String email;
	private String password;
	private boolean is_admin;

	public User( UserManager userManager, String username ) throws DatabaseException
	{
		super(userManager);
		this.setData(0, username, "", "", false);
	}
	
	public User( UserManager userManager, int id ) throws DatabaseException
	{
		super(userManager);
		this.setData(id, "", "", "", false);
	}
	
	public void setData( int id, String username, String email, String password, boolean is_admin ) throws DatabaseException
	{
		this.setId(id);
		this.setUsername(username);
		this.setEmail(email);
		this.setPassword(password);
		this.setAdmin(is_admin);
	}
	
	@Override
	public boolean get() throws DatabaseException, SQLException
	{
		User temp = null;
		
		if ( this.id != 0 )
			temp = (User) this.manager.get(this.id);
		else if ( this.username != null )
			temp = (User) ( (UserManager) this.manager ).get(this.username);
		else
			throw new DatabaseException("Neither user id, nor username specified.");
		
		if ( temp == null )
			return false;
		
		this.username = temp.username;
		this.email = temp.email;
		this.password = temp.password;
		this.is_admin = temp.is_admin;
		
		this.initilalized = true;
		
		return true;
	}

	@Override
	public void update() throws SQLException
	{
		this.manager.update(this);
	}
	
	@Override
	public void create() throws SQLException, DatabaseException
	{
		this.manager.create(this);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id) throws DatabaseException
	{
		if ( this.initilalized )
			throw new DatabaseException("Unable to change id after synchronization with the DB.");
		
		this.id = id;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean isAdmin()
	{
		return is_admin;
	}

	public void setAdmin(boolean admin)
	{
		this.is_admin = admin;
	}
	
}
