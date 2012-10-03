package hu.ppke.itk.itkStock.server.db.user;

import hu.ppke.itk.itkStock.server.db.dbAccess.BusinessObject;
import hu.ppke.itk.itkStock.server.db.dbAccess.BusinessObjectException;

import java.sql.SQLException;

/**
 * Ez az osztaly reprezentalja a felhasznalokat ('User'). Egy 'BusinessObject', tehat alapesetben egyes peldanyai 
 * osszekottetesben vannak az adatbazissal. Konstruktoraban meg kell adnunk azt a 'UserManager' osztaly-peldanyt, mely az adatbazissal az osszekottetest biztositja.
 * 
 * @see UserManager
 * 
 */

public class User extends BusinessObject
{
	private String username;
	private String email;
	private String password;
	private boolean admin;
	
	private boolean changed = false;

	public User( UserManager userManager, String username ) throws BusinessObjectException
	{
		super(userManager,0);
		this.setData(username, "", "", false);
	}
	
	public User( UserManager userManager, int id ) throws BusinessObjectException
	{
		super(userManager,id);
		this.setData("", "", "", false);
	}
	
	public User( UserManager userManager ) throws BusinessObjectException
	{
		super(userManager, 0);
		this.setData("", "", "", false);
	}
	
	public void setData( String username, String email, String password, boolean is_admin ) throws BusinessObjectException
	{
		this.setUsername(username);
		this.setEmail(email);
		this.setPassword(password);
		this.setAdmin(is_admin);
		this.changed = true;
	}
	
	@Override
	public boolean get() throws SQLException, BusinessObjectException
	{
		User temp = null;
		
		if ( this.id != 0 )
			temp = (User) this.manager.get(this.id);
		else if ( this.username != null )
			temp = (User) ( (UserManager) this.manager ).get(this.username);
		else
			throw new BusinessObjectException("Neither user id, nor username specified.");
		
		if ( temp == null )
			return false;
		
		
		this.id = temp.id;
		this.username = temp.username;
		this.email = temp.email;
		this.password = temp.password;
		this.admin = temp.admin;
		
		this.identified = true;
		this.changed = false;
		
		return true;
	}

	@Override
	public void update() throws SQLException, BusinessObjectException
	{
		if ( !this.identified )
			throw new BusinessObjectException("Must identify BusinessObject before updating in database.");
		
		if ( this.changed )
			((UserManager)this.manager).update(this);
	}
	
	@Override
	public void create() throws SQLException, BusinessObjectException
	{
		if ( this.identified )
			throw new BusinessObjectException("Identified object should not be created.");
		
		((UserManager)this.manager).create(this);
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
		this.changed = true;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
		this.changed = true;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
		this.changed = true;
	}

	public boolean isAdmin()
	{
		return admin;
	}

	public void setAdmin(boolean admin)
	{
		this.admin = admin;
		this.changed = true;
	}
	
}
