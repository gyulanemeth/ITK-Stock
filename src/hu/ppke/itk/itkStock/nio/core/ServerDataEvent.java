package hu.ppke.itk.itkStock.nio.core;
import java.nio.channels.SocketChannel;

public class ServerDataEvent {
	public NioServer server;
	public SocketChannel socket;
	public byte[] data;

	public ServerDataEvent(NioServer server, SocketChannel socket, byte[] data) {
		this.server = server;
		this.socket = socket;
		this.data = data;
	}
}
