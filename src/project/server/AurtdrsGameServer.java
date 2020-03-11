package project.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import utils.network.Client;

public class AurtdrsGameServer {
	
	private GameServerManager server;
	
	private HashMap<ArrayList<Client>, AurtdrsServerProcess> gameProcesses;

	public AurtdrsGameServer(int port) throws IOException {
		this.server = new GameServerManager(port);
	}
	
	public void start() {
		this.server.start();
	}
	
	public void end() {
		this.server.end();
	}
	
	public void gameProcess() {
		
		new Thread(()->{
			
			while(true) {
				
				this.setGamePools();
				this.processGames();
				
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
				}
				
			}
			
		}).start(); 
		
		
		
	}
	
	private void setGamePools() {
		
		for(ArrayList<Client> pool : this.server.getGamePools()) {
			//
		}
		
	}
	
	public void processGames() {
		
		
		
	}
	
	
}
