package project.server;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

import project.client.NetworkData;
import project.client.NetworkGameState;
import utils.network.Client;
import utils.network.NetworkHandler;
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

	private static final int HEARTBEAT_TIMOUT_MILLIS = 2000;

	private Server server;
	private HashSet<Client> clients;
	private HashMap<Client, long[]> heartbeatPool;
	private HashMap<Client, Queue<NetworkData>> clientData;

	private final Object dataLock = new Object();

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
		this.heartbeatPool = new HashMap<Client, long[]>();
		this.clientData = new HashMap<Client, Queue<NetworkData>>();

	}

	/**
	 * Starts the server hosting the game.
	 *
	 * @param handler the handler
	 */
	public synchronized void start(NetworkHandler handler) {

		if (this.running) {
			return;
		}

		this.server.start((client) -> {

			this.handleServerStart(handler, client);
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

	private void handleServerStart(NetworkHandler handler, Client client) {
		synchronized (this) {
			this.clients.add(client);
			this.heartbeatPool.put(client, new long[2]);
			this.clientData.put(client, new ArrayDeque<NetworkData>());
		}
		try {
			client.sendData(new NetworkData(NetworkGameState.HEART_BEAT, this.clients.size()));
			if (handler != null) {
				handler.request(client);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (this.isClientConnected(client)) {

			try {

				NetworkData data = (NetworkData) client.readBlocking();
				if (data.getState() == NetworkGameState.HEART_BEAT) {
					this.applyHeartbeat(client);
				} else {
					synchronized (this.dataLock) {
						this.clientData.get(client).add(data);
					}
				}

			} catch (Exception e) {
				// e.printStackTrace();
				return;
			}

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
	 * Checks if is client connected.
	 *
	 * @param client the client
	 * @return true, if is client connected
	 */
	public synchronized boolean isClientConnected(Client client) {
		return this.clients.contains(client);
	}

	/**
	 * Apply heartbeat.
	 *
	 * @param client the client
	 */
	public synchronized void applyHeartbeat(Client client) {
		this.heartbeatPool.get(client)[0] = System.currentTimeMillis();
		this.heartbeatPool.get(client)[0] = 0;
	}

	/**
	 * Gets the data.
	 *
	 * @param client the client
	 * @return the data
	 */
	public NetworkData getData(Client client) {
		synchronized (this.dataLock) {
			if (this.clientData.get(client).size() < 1) {
				return null;
			}
			return this.clientData.get(client).remove();
		}
	}

	/**
	 * Sends the current heartbeats to each client.
	 */
	public synchronized void sendHeartbeats() {

		HashSet<Client> toDelete = new HashSet<Client>();

		this.tryToSendData(toDelete);

		long curTime = System.currentTimeMillis();
		for (Client client : this.clients) {
			if (this.heartbeatPool.get(client)[0] == 0) {

				this.heartbeatPool.get(client)[0] = curTime;

			} else {

				this.handleHeartBeatPool(toDelete, curTime, client);
			}
		}

		for (Client client : toDelete) {

			if (!client.close()) {
				System.err.println("Could not close client: " + client);
			}

			this.clients.remove(client);
			this.heartbeatPool.remove(client);
		}

	}

	private void handleHeartBeatPool(HashSet<Client> toDelete, long curTime, Client client) {
		long offset = curTime - this.heartbeatPool.get(client)[0];
		if (offset > HEARTBEAT_TIMOUT_MILLIS
				&& curTime - this.heartbeatPool.get(client)[0] > HEARTBEAT_TIMOUT_MILLIS * 2) {

			toDelete.add(client);

		} else if (offset > HEARTBEAT_TIMOUT_MILLIS && this.heartbeatPool.get(client)[0] == 0) {

			try {
				client.sendData(NetworkData.HEART_BEAT);
				this.heartbeatPool.get(client)[1] = System.currentTimeMillis();
			} catch (IOException e) {
				toDelete.add(client);
			}

		}
	}

	private void tryToSendData(HashSet<Client> toDelete) {
		for (Client client : this.clients) {
			try {
				client.sendData(new NetworkData(NetworkGameState.HEART_BEAT, this.clients.size()));
			} catch (IOException e) {
				toDelete.add(client);
			}
		}
	}

}
