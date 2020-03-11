/**
 * 
 */
package project.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import project.game.TicTacToeManager;

/**
 * Manages the TicTacToeGameServer
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class TicTacToeGameServer {

	private TicTacToeManager aTicTacToeManager;

	private GameServer server;
	private Map<String, String> players = new HashMap<String, String>();

	/**
	 * Instantiates a new tic tac toe server.
	 *
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public TicTacToeGameServer(int port) throws IOException {
		this.server = new GameServer(port);
		this.aTicTacToeManager = new TicTacToeManager();
	}

	/**
	 * Starts the session on the server for "Tic Tac Toe".
	 */
	public void start() {
		this.server.start(null);
	}

	/**
	 * Ends the session on the server for "Tic Tac Toe".
	 */
	public void end() {
		this.server.end();
	}

}
