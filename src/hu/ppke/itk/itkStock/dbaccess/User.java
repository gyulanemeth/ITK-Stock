package hu.ppke.itk.itkStock.dbaccess;

import java.sql.SQLException;

public class User extends BusinessObject
{
	private int id;
	private String username;
	private String email;
	private String password;
	private boolean is_admin;

	public User( UserManager userManager, String username )
	{
		super( userManager);
		// TODO Auto-generated constructor stub
	}
	
	public User( UserManager userManager, int id )
	{
		super(userManager);
		// TODO Auto-generated constructor stub
	}
	
	public void setData( int id, String username, String email, String password, boolean is_admin )
	{
		this.setId(id);
		this.setUsername(username);
		this.setEmail(email);
		this.setPassword(password);
		this.setAdmin(is_admin);
	}
	
	@Override
	public void refresh() throws SQLException
	{
		User temp = (User) this.manager.get(this.id);
		this.username = temp.username;
		this.email = temp.email;
		this.password = temp.password;
		this.is_admin = temp.is_admin;
	}

	@Override
	public void commit() throws SQLException
	{
		this.manager.update(this);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
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

	public void setAdmin(boolean is_admin)
	{
		this.is_admin = is_admin;
	}
	
}
