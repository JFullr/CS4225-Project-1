package project.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import project.game.hangman.Player;

/**
 * The Class HangManServer.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020.
 */
public class HangManServer {

	private GameServer server;
	private HangManServerManager hangManGame;
	private Map<String, String> players = new HashMap<String, String>();

	/**
	 * Instantiates a new hang man server.
	 *
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public HangManServer(int port) throws IOException {
		this.server = new GameServer(port);
		this.hangManGame = new HangManServerManager(this.getLobby());
	}

	private Player[] getLobby() {
		return null;
	}

	/**
	 * Starts the session on the server for "Hang Man".
	 */
	public void start() {
		this.server.start(null);
	}

	/**
	 * Ends the session on the server for "Hang Man".
	 */
	public void end() {
		this.server.end();
	}

}

