package project.server;

import java.io.IOException;

/**
 * The Class HangManServer.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020.
 */
public class HangManServer {

	private GameServer server;

	/**
	 * Instantiates a new hang man server.
	 *
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public HangManServer(int port) throws IOException {
		this.server = new GameServer(port);
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

