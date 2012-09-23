package hu.ppke.itk.itkStock.server;

import java.sql.SQLException;

import hu.ppke.itk.itkStock.dbaccess.DatabaseConnector;
import hu.ppke.itk.itkStock.dbaccess.UserManager;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		DatabaseConnector dbc = new DatabaseConnector();
		UserManager umg = null;
		
		try
		{
			dbc.initConnection();
			umg = new UserManager(dbc);
			umg.addUser("troll", "lol@asd.hu", "blabla");
			dbc.closeConnection();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ITK-Stock server");
		System.out.println("test");
	}

}
