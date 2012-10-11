package hu.ppke.itk.itkStock.nio.core;

public class EchoWorker extends AbstractWorker {
	@Override
	public byte[] response(byte[] data, int count) {

		byte[] dataCopy = new byte[count];
		System.arraycopy(data, 0, dataCopy, 0, count);
		return dataCopy;
	}
}