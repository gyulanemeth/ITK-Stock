package hu.ppke.itk.itkStock.dbaccess;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractManager<T extends BusinessObject>
{
	protected DatabaseConnector dbConnector;
	protected volatile ResultSet _resultSet = null;
	
	public AbstractManager(DatabaseConnector dbConnector)
	{
		this.dbConnector = dbConnector;
	}
	
	public abstract void update(T businessObject) throws SQLException;
	public abstract T get(int id) throws SQLException;
}
