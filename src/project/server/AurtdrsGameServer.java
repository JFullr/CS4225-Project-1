package project.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import project.client.NetworkData;
import project.client.NetworkGameState;
import utils.network.Client;

/**
 * The Class AurtdrsGameServer represents a server that hosts a game of
 * "Ultimate Australian Road Train Drag Racing Simulator".
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020.
 */
public class AurtdrsGameServer {

	private static int POOL_SIZE = 2;
	
	private GameServer server;
	
	private HashMap<Integer,ArrayList<Client>> currentGames;
	private HashMap<Client,Integer> clientGame;
	
	
	/**
	 * Instantiates a new aurtdrs game server.
	 *
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public AurtdrsGameServer(int port) throws IOException {
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
	
	public int getLobbySize(Client client) {
		return this.currentGames.get(this.clientGame.get(client)).size();
	}
	
	private void connectionProcess(Client client) {
		try {
			//this.clientGame
			client.sendData(new NetworkData(NetworkGameState.LOBBY,this.getLobbySize(client)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
