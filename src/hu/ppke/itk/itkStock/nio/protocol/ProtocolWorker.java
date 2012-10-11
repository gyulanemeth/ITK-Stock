package hu.ppke.itk.itkStock.nio.protocol;

import hu.ppke.itk.itkStock.nio.core.AbstractWorker;

import java.util.Hashtable;


public class ProtocolWorker extends AbstractWorker {
	private Hashtable<Short, AbstractProtocolCommandWorker> protocolCommandWorkers = new Hashtable<Short, AbstractProtocolCommandWorker>();

	public void addProtocolCommandWorker(Short id, AbstractProtocolCommandWorker w) {
		if (!protocolCommandWorkers.containsKey(id))
			protocolCommandWorkers.put(id, w);
	}

	public void removeProtocolCommandWorker(Short id) {
		protocolCommandWorkers.remove(id);
	}

	/**
	 * @param data
	 *            2 bytes of command-id and the rest is the corresponding data.
	 */
	@Override
	public byte[] response(byte[] data, int count) {
		ProtocolMessage m = ProtocolMessage.parseMessage(data);
		AbstractProtocolCommandWorker obj = protocolCommandWorkers.get(m.command);
		if (obj != null)
			return obj.response(m).toByteArray();
		else {
			System.err.println("Unknown command: " + m.command);
			return ("Unknown command: " + m.command).getBytes();
		}
	}

}
