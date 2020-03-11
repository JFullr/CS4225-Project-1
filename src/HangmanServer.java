import java.io.IOException;
import java.util.ArrayList;

import project.client.NetworkData;
import project.client.NetworkGameState;
import utils.network.Client;

public class HangmanServer {
	
	private static int limit = 6;
	
	private GameServer server;
	
	private ArrayList<Player> clients;
	
	public HangmanServer(int port) {
		try {
			this.server = new GameServer(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.clients = new ArrayList<Client>();
		
	}
	
	public void start() {
		this.server.start((client)->{

			if(this.clients.size() >= limit) {
				try {
					client.sendData(new NetworkData(NetworkGameState.DISCONNECTED));
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		});
	}
	
	public sendClientDisconnect(NetworkData data) {
		this.server.getData(client)
	}
	
	public void sendClientConnect(NetworkData data) {
		
	}
	
	public void sendClientAnswer(NetworkData data) {
		
	}
	
	
	private class Player{
		
		String name;
		Client client;
		
	}

}
