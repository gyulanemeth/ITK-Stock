package hu.ppke.itk.itkStock.dbaccess;

import java.sql.SQLException;

public abstract class BusinessObject
{
	protected AbstractManager manager = null;
	
	public BusinessObject( AbstractManager manager )
	{
		this.manager = manager;
	}
	
	public abstract void refresh() throws SQLException;
	public abstract void commit() throws SQLException;

}
