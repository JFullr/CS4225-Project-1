package project.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import project.client.NetworkData;
import project.client.NetworkState;
import utils.network.Client;

/**
 * The Class AurtdrsGameServer represents a server that hosts a game of
 * "Ultimate Australian Road Train Drag Racing Simulator".
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020.
 */
public class GameServerManager {

	private static final int GAME_SIZE = 2;
	private static final int MAX_GAMES = 10;
	
	private GameServer server;
	
	private HashMap<Integer,ArrayList<Client>> currentGames;
	private HashMap<Client,Integer> clientGame;
	
	
	/**
	 * Instantiates a new aurtdrs game server.
	 *
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public GameServerManager(int port) throws IOException {
		this.server = new GameServer(port);
		
		this.currentGames = new HashMap<Integer,ArrayList<Client>>();
		this.clientGame = new HashMap<Client, Integer>();
	}

	/**
	 * Starts the session on the server for "Ultimate Australian Road Train Drag
	 * Racing Simulator".
	 */
	public void start() {
		this.server.start((client)->{
			this.connectionProcess(client);
		});
	}

	/**
	 * Ends the session on the server for "Ultimate Australian Road Train Drag
	 * Racing Simulator".
	 */
	public void end() {
		this.server.end();
	}
	
	public NetworkData getData(Client client) {
		return this.server.getData(client);
	}
	
	public ArrayList<ArrayList<Client>> getGamePools(){
		
		ArrayList<ArrayList<Client>> all = new ArrayList<ArrayList<Client>>();
		all.addAll(this.currentGames.values());
		return all;
		
	}
	
	private void connectionProcess(Client client) {
		try {
			this.assignToGame(client);
			client.sendData(new NetworkData(NetworkState.LOBBY,this.getLobbySize(client)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int getLobbySize(Client client) {
		return this.currentGames.get(this.clientGame.get(client)).size();
	}
	
	private synchronized void assignToGame(Client client) {
		
		for(int i = 0; i < Integer.MAX_VALUE && i < this.currentGames.size(); i++) {
			
			for(Client cli : this.currentGames.get(i)) {
				if(!this.server.isClientConnected(cli)) {
					this.currentGames.get(i).remove(cli);
					break;
				}
			}
			
			if(this.currentGames.get(i).size() < GAME_SIZE) {
				
				this.currentGames.get(i).add(client);
				this.clientGame.put(client, i);
				return;
			}
			
		}
		
		int pos = this.currentGames.size();
		
		if(pos >= MAX_GAMES) {
			//TODO disconnect
			return;
		}
		
		this.clientGame.put(client, pos);
		this.currentGames.put(pos, new ArrayList<Client>());
		this.currentGames.get(pos).add(client);
		
	}

}
