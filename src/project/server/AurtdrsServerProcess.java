package project.server;

import java.io.IOException;

import project.client.NetworkData;
import project.client.NetworkState;
import utils.network.Client;

public class AurtdrsServerProcess {
	
	private static final int TIMEOUT_MILLIS = 50;
	
	private long goal = 32000;
	
	private GameServerManager server;
	private Iterable<Client> clients;
	
	public AurtdrsServerProcess(GameServerManager server, Iterable<Client> clients) {
		
		this.clients = clients;
		this.server = server;
		
	}
	
	public void processGame() {
		
		try {
			
			for(Client client : this.clients) {
				this.server.getData(client);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void collectData() {
		
	}
	
	private void race() {
		
		for(Client client : this.clients) {
			try {
				client.sendData(new NetworkData(NetworkState.IN_GAME, null, null));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	private void sendData(NetworkData data) {
		
		for(Client client : this.clients) {
			try {
				client.sendData(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
