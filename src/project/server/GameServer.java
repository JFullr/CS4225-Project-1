package project.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import project.client.NetworkData;
import utils.network.Client;
import utils.network.Server;

public class GameServer {
	
	private static final int TIMEOUT_SECONDS = 5;
	private static final int HEARTBEAT_TIMOUT_MILLIS = 5000;
	
	private Server server;
	private HashSet<Client> clients;
	
	private volatile boolean running;
	
	public GameServer(int port) throws IOException {
		
		this.server = new Server(port);
		this.clients = new HashSet<Client>();
		this.running = false;
		
	}
	
	public void start() {
		
		synchronized(this) {
			
			this.running = true;
			
			this.server.start((client)->{
				synchronized(this) {
					this.clients.add(client);
				}
			});
			new Thread(()->{
				while(true) {
					
					try {
						Thread.sleep(HEARTBEAT_TIMOUT_MILLIS);
					} catch (InterruptedException e) {}
					
					this.sendHeartbeats();
				}
			}).start();
			
		}
		
	}
	
	public void end() {
		this.server.end();
		this.running = false;
	}
	
	
	public void sendHeartbeats() {
		
		while(this.running) {
		
			ArrayList<Client> toDelete = new ArrayList<Client>();
			
			for(Client cli : this.clients) {
				try {
					cli.sendData(NetworkData.HEART_BEAT);
				} catch (IOException e) {
					toDelete.add(cli);
				}
			}
			
			synchronized(this) {
				for(Client cli : toDelete) {
					
					while(!cli.close());
					
					this.clients.remove(cli);
				}
			}
		
		}
		
	}
	
}
