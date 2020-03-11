package project.game.aurtdrs.network;

import project.client.GameClientManager;
import project.client.NetworkData;

public class AurtdrsGameClient {
	
	private GameClientManager client;
	
	public AurtdrsGameClient() {
		this.client = new GameClientManager();
	}
	
	public AurtdrsGameClient(String address, int port) {
		this.client = new GameClientManager(address, port);
	}
	
	public synchronized void start() {
		
		this.client.start();
		
	}
	
	public synchronized NetworkData getData() {
		return this.client.getData();
	}

}
