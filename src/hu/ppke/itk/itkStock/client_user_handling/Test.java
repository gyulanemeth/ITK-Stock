package hu.ppke.itk.itkStock.client_user_handling;

import hu.ppke.itk.itkStock.nio.core.AbstractWorker;
import hu.ppke.itk.itkStock.nio.core.NioClient;
import hu.ppke.itk.itkStock.nio.core.NioServer;
import hu.ppke.itk.itkStock.nio.core.RspHandler;
import hu.ppke.itk.itkStock.nio.protocol.ClientsideAuthenticationProtocolCommandWorker;
import hu.ppke.itk.itkStock.nio.protocol.ProtocolTools;
import hu.ppke.itk.itkStock.nio.protocol.ProtocolWorker;
import hu.ppke.itk.itkStock.nio.protocol.ServersideAuthenticationProtocolCommandWorker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;


public class Test {
		

	public static void main(String[] args) throws IOException {

		try {
			InetAddress serverArrd = InetAddress.getByName("localhost");
			int serverPort = 9090;
			int protocolNum=322;
			// create NIO server
			AbstractWorker protocolWorker = new ProtocolWorker();
			// add a new commandWorker to listen command 100, for example for auth.
			((ProtocolWorker) protocolWorker).addProtocolCommandWorker((short) 100,
					new ServersideAuthenticationProtocolCommandWorker());
			((ProtocolWorker) protocolWorker).addProtocolCommandWorker((short) protocolNum,
					new ServersideAuthenticationProtocolCommandWorker());
			new Thread(protocolWorker).start();
			new Thread(new NioServer(null, serverPort, protocolWorker)).start();

			// create NIO clients
			{
				NioClient client = new NioClient(serverArrd, serverPort);
				Thread t = new Thread(client);
				t.setDaemon(true);
				t.start();
				RspHandler handler = new RspHandler();
				// add new commandWorker to respond for command 101 (auth. response)
				handler.addProtocolCommandWorker((short)101, new ClientsideAuthenticationProtocolCommandWorker());
				handler.addProtocolCommandWorker((short) (protocolNum+1), new ClientsideAuthenticationProtocolCommandWorker());
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//				byteStream.write(ProtocolTools.shortToBytes((short) 100));
//				byteStream.write("username kapamester".getBytes());
//				client.send(byteStream.toByteArray(), handler);
				
//				handler.waitForResponse();
				
				user_handler hl = new user_handler(client,handler);
				//hl.add_User("Isti","isti1989","lorinczmail@gmail.com");
				//hl.set_Money("Isti",100.05);
				//hl.get_Money("Isti");
				//hl.change_UserName("Isti","Isti89");
				//hl.change_Email("Isti89", "isti17@freemail.hu");
				//hl.change_Password("Isti89", "titkos");
				//hl.check_NameAndPw("Isti89", "titkos");
				//hl.remove_User("Isti89");
				//hl.update_User("Lorincz", "password", "email", true);
				//hl.check_UserExistence("Lorincz");
				//hl.promote_Admin("Lorincz");
				hl.demote_Admin("Lorincz");

				handler.waitForResponse();
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("MAIN END");

	}

}
