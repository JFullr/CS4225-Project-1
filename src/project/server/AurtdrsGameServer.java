package project.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import utils.network.Client;

public class AurtdrsGameServer {
	
	private GameServerManager server;
	
	private HashMap<ArrayList<Client>, AurtdrsGameServerProcess> gameProcesses;

	public AurtdrsGameServer(int port) throws IOException {
		this.server = new GameServerManager(port);
		
		this.gameProcesses = new HashMap<ArrayList<Client>, AurtdrsGameServerProcess>();
		
	}
	
	public void start() {
		this.server.start();
		this.gameProcess();
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
			
			if(!this.gameProcesses.keySet().contains(pool)) {
				this.gameProcesses.put(pool, new AurtdrsGameServerProcess(this.server, pool));
			}
			
		}
		
	}
	
	public void processGames() {
		
		for(ArrayList<Client> pool : this.server.getGamePools()) {
			
			this.gameProcesses.get(pool).processGame();
			
			
		}
		
	}
	
	
}
