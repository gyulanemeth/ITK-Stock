package hu.ppke.itk.itkStock.dbaccess;

import java.sql.SQLException;

public abstract class BusinessObject
{
	protected AbstractManager manager = null;
	protected boolean initilalized = false;
	
	public BusinessObject( AbstractManager manager )
	{
		this.manager = manager;
	}
	
	public abstract boolean get() throws DatabaseException, SQLException;
	public abstract void update() throws SQLException;
	public abstract void create() throws SQLException, DatabaseException;

}
