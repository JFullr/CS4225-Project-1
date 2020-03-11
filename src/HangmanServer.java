import java.io.IOException;
import java.util.ArrayList;

import project.client.NetworkData;
import project.client.NetworkGameState;
import utils.network.Client;

/**
 * The Class HangmanServer.
 *
 * @author csuser
 */
public class HangmanServer {

	private static int limit = 6;

	private GameServer server;
	private ArrayList<Player> clients;

	/**
	 * Instantiates a new hangman server.
	 *
	 * @param port the port
	 */
	public HangmanServer(int port) {
		try {
			this.server = new GameServer(port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.clients = new ArrayList<Player>();

	}

	/**
	 * Start.
	 */
	public void start() {
		this.server.start((client) -> {

			if (this.clients.size() >= limit) {
				try {
					client.sendData(new NetworkData(NetworkGameState.DISCONNECTED));
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
	}

	/**
	 * Send client disconnect.
	 *
	 * @param data the data
	 */
	public void sendClientDisconnect(NetworkData data) {
		this.server.getData(client);
	}

	/**
	 * Send client connect.
	 *
	 * @param data the data
	 */
	public void sendClientConnect(NetworkData data) {

	}

	/**
	 * Send client answer.
	 *
	 * @param data the data
	 */
	public void sendClientAnswer(NetworkData data) {

	}

	/**
	 * The Class Player.
	 */
	private class Player {
		
		private String name;
		private Client client;

	}

}
