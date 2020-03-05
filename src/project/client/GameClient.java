package project.client;

import java.io.IOException;

import utils.network.Client;

public class GameClient {
	
	private static final int TIMEOUT_SECONDS = 5;
	private static final Object initData = new Object();
	
	private Client client;
	
	public GameClient(String address, int port) {
		
		this.client = new Client(address, port, TIMEOUT_SECONDS);
		
	}
	
	public boolean connectBlocking() {
		
		try {
			this.client.connect(initData);
		} catch (IOException e) {
			return false;
		}
		return true;
		
	}
	
	public boolean sendHeartbeat() {
		try {
			this.client.sendData(initData);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public boolean sendData(NetworkData data) {
		try {
			this.client.sendData(data);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public NetworkData readRequest() {
		try {
			return (NetworkData)this.client.readBlocking();
		} catch (IOException | ClassNotFoundException e) {
			return null;
		}
	}
	
	public boolean close() {
		return this.client.close();
	}
	
}
