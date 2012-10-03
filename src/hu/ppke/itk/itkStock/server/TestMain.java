package hu.ppke.itk.itkStock.server;

import java.sql.SQLException;
import java.util.Random;

import hu.ppke.itk.itkStock.SaveDailyDatas.DailyDataListener;
import hu.ppke.itk.itkStock.SaveDailyDatas.StockDataManager;
import hu.ppke.itk.itkStock.server.db.dbAccess.BusinessObjectException;
import hu.ppke.itk.itkStock.server.db.dbAccess.DatabaseConnector;
import hu.ppke.itk.itkStock.server.db.user.User;
import hu.ppke.itk.itkStock.server.db.user.UserManager;

public class TestMain
{
	public static void main(String[] args)
	{
		DatabaseConnector dbc = new DatabaseConnector();
		UserManager umg = null;
		Random rgen = new Random();
		
		try
		{
			dbc.initConnection();
			umg = new UserManager(dbc);
			String username = "troll" + rgen.nextInt(9999);
			umg.addUser(username, username + "@troll.hu", "problem?");
			User testuser = new User(umg, username );
			testuser.get();
			System.out.println(testuser.getId());
			System.out.println(testuser.getUsername());
			System.out.println(testuser.getEmail());
			System.out.println(testuser.getPassword());
			System.out.println(testuser.isAdmin());
			
			//kikommenteltem, mert nem tudom, hogy hol van definialva a Pair.
//			///gubkr new
//			StockDataManager stockManager = new StockDataManager(dbc);
//			DailyDataListener listener = new DailyDataListener(stockManager);
//			listener.start();
//			///gubkr
			boolean interrupted = false;
			while(!interrupted){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					interrupted = true;
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			dbc.closeConnection();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (BusinessObjectException e)
		{
			e.printStackTrace();
		}
		System.out.println("ITK-Stock server");
	}
}
