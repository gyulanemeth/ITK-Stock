// TUTORIAL HOW TO KAPA

package hu.ppke.itk.itkStock.nio.protocol;

import hu.ppke.itk.itkStock.server_user_handling.user_handling;

import java.io.UnsupportedEncodingException;


public class ServersideAuthenticationProtocolCommandWorker extends AbstractProtocolCommandWorker {
	
	@Override
	public synchronized ProtocolMessage response(ProtocolMessage msg) {
		try {
			System.out.println("Server got: "+msg.toString());
			ProtocolMessage rsp = new ProtocolMessage();
			
			
			if(msg.command==100){
			rsp.command = (short) (msg.command + 1);
			String s = new String(msg.data, "ASCII");
			String username = s.split(" ")[1];
			// LOGIN()...
			rsp.data = ("You logged in as: " + username).getBytes();
			System.out.println("Server sent: "+rsp.toString());
			}
			
			
			//user name and password is correct?
			if(msg.command==300){
				rsp.command = (short) (msg.command + 1);
				String s2 = new String(msg.data, "ASCII");
				String[] s_array;
				s_array=s2.split(" ",0);
				rsp=user_handling.check_NameAndPw(s_array[0], s_array[1],rsp);						
			}	
			
			
			if(msg.command==302){
				rsp.command = (short) (msg.command + 1);
				String s2 = new String(msg.data, "ASCII");
				 String[] s_array;
				 s_array=s2.split(" ",0);
				 rsp=user_handling.create_new(s_array[0],s_array[1],s_array[2],rsp);
			}
			
			if(msg.command==304){
				rsp.command= (short) (msg.command + 1);
				String s2 = new String(msg.data,"ASCII");
				String[] s_array;
				s_array=s2.split(" ",0);
				rsp=user_handling.set_money(s_array[0], s_array[1],rsp);
				
				
			}
			
			if(msg.command==306){
				rsp.command= (short) (msg.command + 1);
				String s2 = new String(msg.data,"ASCII");
				String[] s_array;
				s_array=s2.split(" ",0);
				rsp=user_handling.get_money(s_array[0],rsp);
				
				
			}
			
			if(msg.command==308){
				rsp.command= (short) (msg.command + 1);
				String s2 = new String(msg.data,"ASCII");
				String[] s_array;
				s_array=s2.split(" ",0);
				rsp=user_handling.change_UserName(s_array[0],s_array[1],rsp);
				
				
			}
			
			if(msg.command==310){
				rsp.command= (short) (msg.command + 1);
				String s2 = new String(msg.data,"ASCII");
				String[] s_array;
				s_array=s2.split(" ",0);
				rsp=user_handling.change_Email(s_array[0],s_array[1],rsp);
				
				
			}
			
			if(msg.command==312){
				rsp.command= (short) (msg.command + 1);
				String s2 = new String(msg.data,"ASCII");
				String[] s_array;
				s_array=s2.split(" ",0);
				rsp=user_handling.change_Password(s_array[0],s_array[1],rsp);
				
				
			}
			
			if(msg.command==314){
				rsp.command= (short) (msg.command + 1);
				String s2 = new String(msg.data,"ASCII");
				String[] s_array;
				s_array=s2.split(" ",0);
				rsp=user_handling.remove_User(s_array[0],rsp);
				
				
			}
			if(msg.command==316){
				rsp.command= (short) (msg.command + 1);
				String s2 = new String(msg.data,"ASCII");
				String[] s_array;
				s_array=s2.split(" ",0);
				rsp=user_handling.update_User(s_array[0],s_array[1],s_array[2],s_array[3],rsp);
				
				
			}
			if(msg.command==318){
				rsp.command= (short) (msg.command + 1);
				String s2 = new String(msg.data,"ASCII");
				String[] s_array;
				s_array=s2.split(" ",0);
				rsp=user_handling.check_UserExistence(s_array[0],rsp);
				
				
			}
			if(msg.command==320){
				rsp.command= (short) (msg.command + 1);
				String s2 = new String(msg.data,"ASCII");
				String[] s_array;
				s_array=s2.split(" ",0);
				rsp=user_handling.promote_Admin(s_array[0],rsp);
				
				
			}
			if(msg.command==322){
				rsp.command= (short) (msg.command + 1);
				String s2 = new String(msg.data,"ASCII");
				String[] s_array;
				s_array=s2.split(" ",0);
				rsp=user_handling.demote_Admin(s_array[0],rsp);
				
				
			}

			
			return rsp;
		} catch (UnsupportedEncodingException e) {
			// error while logging in
			e.printStackTrace();
			return new ProtocolMessage(ProtocolTools.serverToClientError, null);
		}
	}

}
