package project.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import utils.network.Client;

/**
 * The Class AurtdrsGameServer represents a server that hosts a game of
 * "Ultimate Australian Road Train Drag Racing Simulator".
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020.
 */
public class AurtdrsGameServer {

	private GameServer server;
	
	private HashMap<Integer,ArrayList<Client>> currentGames;
	
	/**
	 * Instantiates a new aurtdrs game server.
	 *
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public AurtdrsGameServer(int port) throws IOException {
		this.server = new GameServer(port);
	}

	/**
	 * Starts the session on the server for "Ultimate Australian Road Train Drag
	 * Racing Simulator".
	 */
	public void start() {
		this.server.start();
	}

	/**
	 * Ends the session on the server for "Ultimate Australian Road Train Drag
	 * Racing Simulator".
	 */
	public void end() {
		this.server.end();
	}

}
