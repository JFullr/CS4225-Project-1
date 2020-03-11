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
		
		this.clients = new ArrayList<Player>();
		
	}
	
	public void start() {
		this.server.start((client)->{

			synchronized(this) {
				if(this.clients.size() >= limit) {
					
					try {
						client.sendData(new NetworkData(NetworkGameState.DISCONNECTED));
						client.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}else {
					
					NetworkData data = this.server.getData(client);
					
					while(data == null || data.getState() == NetworkGameState.HEART_BEAT);
					
					if(data.getState() == NetworkGameState.LOBBY) {
						
						String name = (String) data.getData()[0];
						this.clients.add(new Player(name,client));
						
					}
					
				}
				
				
			}
			
		});
	}
	
	public void sendClientConnect(NetworkData data) {
		
	}
	
	public void sendClientAnswer(NetworkData data) {
		
	}
	
	
	private class Player {
		
		private String name;
		private Client client;
		
		public Player(String name, Client client) {
			this.name = name;
			this.client = client;
		}
		
	}

}
