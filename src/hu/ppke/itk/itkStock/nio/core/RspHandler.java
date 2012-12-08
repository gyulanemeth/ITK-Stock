package hu.ppke.itk.itkStock.nio.core;

import hu.ppke.itk.itkStock.nio.protocol.AbstractProtocolCommandWorker;
import hu.ppke.itk.itkStock.nio.protocol.ProtocolMessage;

import java.util.Hashtable;


public class RspHandler {
	private Hashtable<Short, AbstractProtocolCommandWorker> protocolCommandWorkers = new Hashtable<Short, AbstractProtocolCommandWorker>();

	public void addProtocolCommandWorker(Short id, AbstractProtocolCommandWorker w) {
		if (!protocolCommandWorkers.containsKey(id))
			protocolCommandWorkers.put(id, w);
	}

	public void removeProtocolCommandWorker(Short id) {
		protocolCommandWorkers.remove(id);
	}

	private byte[] rsp = null;
	

	public synchronized boolean handleResponse(byte[] rsp) {
		this.setRsp(rsp);
		//this.rsp[2] = 65;
		ProtocolMessage m = ProtocolMessage.parseMessage(this.rsp);
		//System.out.println("ASD: "+new String(new byte[]{this.rsp[3]}));
		AbstractProtocolCommandWorker obj = protocolCommandWorkers.get(m.command);
		if (obj != null)
			this.setRsp(obj.response(m).toByteArray());
		else {
			System.err.println("RspHandler: Unknown command: " + m.command);
			this.setRsp(("RspHandler: Unknown command: " + m.command).getBytes());
		}
		//System.out.println("RESPONSE: " + new String(new byte[]{this.rsp[8]}));
		
		
		this.notify();
		return true;
	}

	public synchronized void waitForResponse() {
		while (this.rsp == null) {
			try {
				this.wait();
			} catch (InterruptedException e) {
			}
		}

		//System.out.println("RESPONSE: " + new String(new byte[]{this.rsp[8]}));
	}
	public void setRsp(byte[] rsp) {
		this.rsp = rsp;
	}
}