package hu.ppke.itk.itkStock.client_user_handling;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import hu.ppke.itk.itkStock.nio.core.NioClient;
import hu.ppke.itk.itkStock.nio.core.RspHandler;
import hu.ppke.itk.itkStock.nio.protocol.ProtocolTools;

public class user_handler {
	private NioClient client;
	private RspHandler handler;
	
	
	/**
	 * @param handler 
	 * @param name: temporary variable for name
	 * @param password: temporary variable for password
	 * 
	 * @return  if user name is valid and password is correct return will be true
	 */
	public user_handler(NioClient client, RspHandler handler){
		//this.user=user;
		this.client=client;
		this.handler=handler;
	}
	
	
	public void check_NameAndPw(String name,String password){

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		
		try {
			byteStream.write(ProtocolTools.shortToBytes((short) 300));
			byteStream.write(name.getBytes());
			byteStream.write(" ".getBytes());
			byteStream.write(password.getBytes());
			byteStream.write(" ".getBytes());
			client.send(byteStream.toByteArray(), handler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
		
	}
	
	/**
	 * 
	 * @param name this is the user name
	 * @param password this is the password
	 * @param email this is the email
	 * @return if the name is not exist and we inserted the new user into the database, then the return will be true
	 */
	public void create_New(String name,String password,String email){
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			byteStream.write(ProtocolTools.shortToBytes((short) 302));
			byteStream.write(name.getBytes());
			byteStream.write(" ".getBytes());
			byteStream.write(password.getBytes());
			byteStream.write(" ".getBytes());
			byteStream.write(email.getBytes());
			byteStream.write(" ".getBytes());
			client.send(byteStream.toByteArray(), handler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param money
	 * @return
	 */
	public void set_Money(String username,double money){

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			String sdouble=Double.toString(money);
			byteStream.write(ProtocolTools.shortToBytes((short) 304));
			byteStream.write(username.getBytes());
			byteStream.write(" ".getBytes());
			byteStream.write(sdouble.getBytes());
			byteStream.write(" ".getBytes());
			client.send(byteStream.toByteArray(), handler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * 
	 * @param money
	 * @return
	 */
	public void get_Money(String username){

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			byteStream.write(ProtocolTools.shortToBytes((short) 306));
			byteStream.write(username.getBytes());
			byteStream.write(" ".getBytes());

			client.send(byteStream.toByteArray(), handler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
	}
	
	
	
	/**
	 * 
	 * @param newName
	 */
	public void change_UserName(String username,String newName){

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			byteStream.write(ProtocolTools.shortToBytes((short) 308));
			byteStream.write(username.getBytes());
			byteStream.write(" ".getBytes());
			byteStream.write(newName.getBytes());
			byteStream.write(" ".getBytes());

			client.send(byteStream.toByteArray(), handler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param newEmail
	 * @param oldEmail
	 */
	public void change_Email(String username,String newEmail){
		
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			byteStream.write(ProtocolTools.shortToBytes((short) 310));
			byteStream.write(username.getBytes());
			byteStream.write(" ".getBytes());
			byteStream.write(newEmail.getBytes());
			byteStream.write(" ".getBytes());

			client.send(byteStream.toByteArray(), handler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * 
	 * @param oldPassword
	 * @param newPassword
	 */
	public void change_Password(String username,String newPassword){

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			byteStream.write(ProtocolTools.shortToBytes((short) 312));
			byteStream.write(username.getBytes());
			byteStream.write(" ".getBytes());
			byteStream.write(newPassword.getBytes());
			byteStream.write(" ".getBytes());

			client.send(byteStream.toByteArray(), handler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public void remove_User(String username){

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			byteStream.write(ProtocolTools.shortToBytes((short) 314));
			byteStream.write(username.getBytes());
			byteStream.write(" ".getBytes());

			client.send(byteStream.toByteArray(), handler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void update_User(String username,String password,String email,boolean admin){

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			byteStream.write(ProtocolTools.shortToBytes((short) 316));
			byteStream.write(username.getBytes());
			byteStream.write(" ".getBytes());
			byteStream.write(password.getBytes());
			byteStream.write(" ".getBytes());
			byteStream.write(email.getBytes());
			byteStream.write(" ".getBytes());
			byteStream.write(Boolean.toString(admin).getBytes());
			byteStream.write(" ".getBytes());

			client.send(byteStream.toByteArray(), handler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void check_UserExistence(String username){

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			byteStream.write(ProtocolTools.shortToBytes((short) 318));
			byteStream.write(username.getBytes());
			byteStream.write(" ".getBytes());

			client.send(byteStream.toByteArray(), handler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void promote_Admin(String username){

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			byteStream.write(ProtocolTools.shortToBytes((short) 320));
			byteStream.write(username.getBytes());
			byteStream.write(" ".getBytes());

			client.send(byteStream.toByteArray(), handler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void demote_Admin(String username){

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			byteStream.write(ProtocolTools.shortToBytes((short) 322));
			byteStream.write(username.getBytes());
			byteStream.write(" ".getBytes());

			client.send(byteStream.toByteArray(), handler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
}
