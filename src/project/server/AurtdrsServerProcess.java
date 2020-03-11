package project.server;

import java.io.IOException;

import project.client.NetworkData;
import project.client.NetworkGameState;
import utils.network.Client;

public class AurtdrsServerProcess {
	
	private long goal = 32000;
	
	private Iterable<Client> clients;
	
	public AurtdrsServerProcess(Iterable<Client> clients) {
		
		this.clients = clients;
		
	}
	
	public void processGame() {
		
		
		
	}
	
	private void race() {
		
		for(Client client : this.clients) {
			try {
				client.sendData(new NetworkData(NetworkGameState.IN_GAME, null, null));
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
