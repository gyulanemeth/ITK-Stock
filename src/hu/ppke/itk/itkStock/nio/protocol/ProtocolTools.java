package hu.ppke.itk.itkStock.nio.protocol;

public class ProtocolTools {
	// public static final byte[] serverToClientError = new byte[] { 0, 0 };//
	// even
	public static final short serverToClientError = 0;// even
	// public static final byte[] clientToServerError = new byte[] { 0, 1 };//
	// odd
	public static final short clientToServerError = 1;// odd

	public static byte[] shortToBytes(short s) {
		return java.nio.ByteBuffer.allocate(2).putShort(s).array();
	}

	public static short bytesToShort(byte[] data) {
		return java.nio.ByteBuffer.wrap(data).getShort();
	}
}
