package project.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import project.client.NetworkData;
import utils.network.Client;
import utils.network.Server;

/**
 * The Class GameServer represents the server that is currently hosting one or
 * more multi-player games. Each hosted game has a set number of player spots to
 * be filled. Once the lobby limit has been reached for a game, every player
 * attempting to join the lobby will be notified that the number of available
 * spots is zero and encourages either finding another game or wait until the
 * lobby has a cleared spot
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class GameServer {

	private static final int TIMEOUT_SECONDS = 5;
	private static final int HEARTBEAT_TIMOUT_MILLIS = 5000;

	private Server server;
	private HashSet<Client> clients;
	private volatile boolean running;

	/**
	 * Instantiates a new game server.
	 *
	 * @param port the port to connect to
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public GameServer(int port) throws IOException {

		this.server = new Server(port);
		this.clients = new HashSet<Client>();
		this.running = false;

	}

	/**
	 * Starts the server hosting the game.
	 */
	public void start() {

		synchronized (this) {

			if(this.running) {
				return;
			}

			this.server.start((client) -> {
				synchronized (this) {
					this.clients.add(client);
				}
			});
			
			this.running = true;
			
			new Thread(() -> {
				while (true) {

					try {
						Thread.sleep(HEARTBEAT_TIMOUT_MILLIS);
					} catch (InterruptedException e) {
					}

					this.sendHeartbeats();
				}
			}).start();

		}

	}

	/**
	 * Ends the current server session.
	 */
	public void end() {
		this.server.end();
		this.running = false;
	}

	/**
	 * Sends the current heartbeats to each client.
	 */
	public void sendHeartbeats() {

		while (this.running) {

			ArrayList<Client> toDelete = new ArrayList<Client>();

			for (Client cli : this.clients) {
				try {
					cli.sendData(NetworkData.HEART_BEAT);
				} catch (IOException e) {
					toDelete.add(cli);
				}
			}

			synchronized (this) {
				for (Client cli : toDelete) {

					while (!cli.close()) {
						System.out.println("what am I doing with my life?");
					}

					this.clients.remove(cli);
				}
			}

		}

	}

}
