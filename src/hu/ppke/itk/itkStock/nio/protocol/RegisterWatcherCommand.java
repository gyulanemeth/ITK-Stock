package hu.ppke.itk.itkStock.nio.protocol;

/**
 * Command for adding new watcher to a given stock by the client.
 * 
 * @see UnregisterWatcherCommand
 */
public class RegisterWatcherCommand extends AbstractProtocolCommandWorker {

	private String serverMessage = "";
	
	public String getServerMessage() {
		return serverMessage;
	}

	@Override
	public ProtocolMessage response(ProtocolMessage msg) {

		serverMessage = msg.toString();
		return new ProtocolMessage(ProtocolTools.registerWatcher,
				"ok".getBytes());
	}

}
