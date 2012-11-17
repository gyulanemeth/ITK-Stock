package hu.ppke.itk.itkStock.server_user_handling;

import java.sql.SQLException;

import hu.ppke.itk.itkStock.nio.protocol.ProtocolMessage;
import hu.ppke.itk.itkStock.server.db.dbAccess.BusinessObjectException;
import hu.ppke.itk.itkStock.server.db.dbAccess.DatabaseConnector;
import hu.ppke.itk.itkStock.server.db.user.User;
import hu.ppke.itk.itkStock.server.db.user.UserManager;

public class user_handling {
    private ProtocolMessage rsp;
    public static DatabaseConnector dc= new DatabaseConnector();
    public static UserManager userM ;
	private static User user;
	public static  ProtocolMessage check_NameAndPw(String username, String password, ProtocolMessage rsp) {
		boolean bool=false;
		try {
			dc.initConnection();
			userM=new UserManager(dc);
			bool=userM.authenticateUser(username, password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(bool){
		rsp.data = ("Username and Password is correct! ").getBytes();
		}else{
		rsp.data=("Username or Password isn't correct!").getBytes();
		}
		return rsp;
		
	}

//	public static ProtocolMessage add_User(String username, String password,
//			String email) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public static ProtocolMessage create_new(String username, String password,String email,ProtocolMessage rsp) {
		try {
			dc.initConnection();
			userM=new UserManager(dc);
			user=new User(userM);
			user.setData(username, email, password, false);
			userM.create(user);	
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (BusinessObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rsp.data = ("successfull ").getBytes();
		return rsp;
	}

	public static ProtocolMessage set_money(String username, String money,ProtocolMessage rsp) {
		// TODO Auto-generated method stub
		try {
			dc.initConnection();
			userM=new UserManager(dc);
			userM.setMoney(username, Double.valueOf(money.trim()).doubleValue());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rsp.data = ("successfull ").getBytes();
		return rsp;
	}

	public static ProtocolMessage get_money(String username,ProtocolMessage rsp) {
		// TODO Auto-generated method stub
		double money=-1;
		try {
			dc.initConnection();
			userM=new UserManager(dc);
			money=userM.getMoney(username);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rsp.data = ("\n\nUsername: "+username+"\nMoney: "+Double.toString(money)+" \n").getBytes() ;
		return rsp;
	}

	public static ProtocolMessage change_UserName(String username, String newusername,ProtocolMessage rsp) {
		// TODO Auto-generated method stub
		try {
			dc.initConnection();
			userM=new UserManager(dc);
			userM.setUsername(username, newusername);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rsp.data = ("successfull ").getBytes();
		return rsp;
	}

	public static ProtocolMessage change_Email(String username, String email,ProtocolMessage rsp) {
		// TODO Auto-generated method stub
		try {
			dc.initConnection();
			userM=new UserManager(dc);
			userM.setEmail(username, email);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rsp.data = ("successfull ").getBytes();
		return rsp;
	}

	public static ProtocolMessage change_Password(String username, String password,ProtocolMessage rsp) {
		// TODO Auto-generated method stub
		try {
			dc.initConnection();
			userM=new UserManager(dc);
			userM.setPassword(username, password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rsp.data = ("successfull ").getBytes();
		return rsp;
	}

	public static ProtocolMessage remove_User(String username,ProtocolMessage rsp) {
		// TODO Auto-generated method stub
		try {
			dc.initConnection();
			userM=new UserManager(dc);
			userM.removeUser(username);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rsp.data = ("successfull ").getBytes();
		return rsp;
	}

	public static ProtocolMessage update_User(String username, String password,
			String email, String is_admin, ProtocolMessage rsp) {
		// TODO Auto-generated method stub
		Boolean bool_is_admin=Boolean.parseBoolean(is_admin);
		try {
			dc.initConnection();
			userM=new UserManager(dc);
			user= new User(userM);
			user.setData(username, email, password, bool_is_admin);
			userM.update(user);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BusinessObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rsp.data = ("successfull ").getBytes();
		return rsp;
	}

	public static ProtocolMessage check_UserExistence(String username,
			ProtocolMessage rsp) {
		// TODO Auto-generated method stub
		boolean bool=false;
		try {
			dc.initConnection();
			userM=new UserManager(dc);
			bool=userM.checkUserExistenceByName(username);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(bool){
		rsp.data = ("User is exist").getBytes();
		}
		else
		{
		rsp.data = ("User isn't exist ").getBytes();

		}
		return rsp;
	}

	public static ProtocolMessage promote_Admin(String username,
			ProtocolMessage rsp) {
	
		try {
			dc.initConnection();
			userM=new UserManager(dc);
			userM.promoteAdmin(username);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rsp.data = ("successfull ").getBytes();

		
		return rsp;
	
	}

	public static ProtocolMessage demote_Admin(String username,
			ProtocolMessage rsp) {
		
		// TODO Auto-generated method stub
		
		try {
			dc.initConnection();
			userM=new UserManager(dc);
			userM.demoteAdmin(username);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rsp.data = ("successfull ").getBytes();

		
		return rsp;	}

	
	
	
	
}
