package hu.ppke.itk.itkStock.nio.protocol;


public abstract class AbstractProtocolCommandWorker {
	/**
	 * 
	 * @param data Contains only the data (without the two-byte command-id).
	 * @param count Length of data.
	 * @return Response byte array.
	 */
	public abstract ProtocolMessage response(ProtocolMessage msg);
}
