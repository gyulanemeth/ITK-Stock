// TUTORIAL HOW TO KAPA

package hu.ppke.itk.itkStock.nio.protocol;


public class ClientsideAuthenticationProtocolCommandWorker extends AbstractProtocolCommandWorker {

	@Override
	public synchronized ProtocolMessage response(ProtocolMessage msg) {
		System.out.println("Client got: " + msg.toString());

		ProtocolMessage rsp = new ProtocolMessage(ProtocolTools.serverToClientError, "thanks".getBytes());

		System.out.println("Client sent: " + rsp.toString());
		return rsp;
	}

}
