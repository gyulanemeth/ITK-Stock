package hu.ppke.itk.itkStock.nio.test;

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

			// create NIO server
			AbstractWorker protocolWorker = new ProtocolWorker();
			// add a new commandWorker to listen command 100, for example for auth.
			((ProtocolWorker) protocolWorker).addProtocolCommandWorker((short) 100,
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

				ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
				byteStream.write(ProtocolTools.shortToBytes((short) 100));
				byteStream.write("username kapamester".getBytes());
				client.send(byteStream.toByteArray(), handler);
				
				handler.waitForResponse();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("MAIN END");

	}

}
