package hu.ppke.itk.itkStock.nio.protocol;

import java.io.UnsupportedEncodingException;

public class ProtocolMessage {
	public short command;// stores a 2 byte unsigned big-endian command-id
	// in a twos complement int!!!
	public byte[] data;// raw data; can contain any byte array

	public ProtocolMessage() {
	}

	public ProtocolMessage(short command, byte[] data) {
		this.command = command;
		this.data = data;
		if (this.data == null)
			this.data = new byte[0];
	}

	public static ProtocolMessage parseMessage(byte[] data) {
		// at least a command-id must be sent
		if (data.length < 2)
			return null;
		ProtocolMessage m = new ProtocolMessage();
		byte[] id = new byte[2];
		System.arraycopy(data, 0, id, 0, 2);
		m.command = ProtocolTools.bytesToShort(id);
		m.data = new byte[data.length - 2];
		System.arraycopy(data, 2, m.data, 0, data.length - 2);
		return m;
	}

	public byte[] toByteArray() {
		byte[] b = new byte[data.length + 2];
		byte[] c = ProtocolTools.shortToBytes(this.command);
		System.arraycopy(c, 0, b, 0, 2);
		System.arraycopy(this.data, 0, b, 2, data.length);
		return b;
	}

	public String toString() {
		try {
			return String.valueOf(this.command) + " " + new String(this.data, "ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

}
