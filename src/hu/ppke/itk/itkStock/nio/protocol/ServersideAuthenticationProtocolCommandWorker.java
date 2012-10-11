// TUTORIAL HOW TO KAPA

package hu.ppke.itk.itkStock.nio.protocol;

import java.io.UnsupportedEncodingException;


public class ServersideAuthenticationProtocolCommandWorker extends AbstractProtocolCommandWorker {

	@Override
	public synchronized ProtocolMessage response(ProtocolMessage msg) {
		try {
			System.out.println("Server got: "+msg.toString());
			ProtocolMessage rsp = new ProtocolMessage();
			rsp.command = (short) (msg.command + 1);
			String s = new String(msg.data, "ASCII");
			String username = s.split(" ")[1];
			// LOGIN()...
			rsp.data = ("You logged in as: " + username).getBytes();
			System.out.println("Server sent: "+rsp.toString());
			return rsp;
		} catch (UnsupportedEncodingException e) {
			// error while logging in
			e.printStackTrace();
			return new ProtocolMessage(ProtocolTools.serverToClientError, null);
		}
	}

}
