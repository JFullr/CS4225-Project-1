package project.client;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Manages a GameClient's interactions through the network.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class GameClientManager {

	private static final int ATTEMPT_TIMEOUT_MILLIS = 3000;

	private static final int HEARBEAT_TIMEOUT_MILLIS = 5000;
	private static final String DEFAULT_ADDRESS = "127.0.0.1";
	private static final int DEFAULT_PORT = 4225;

	private GameClient client;

	private volatile boolean running;
	private boolean attempt;

	private volatile boolean connected;

	private Queue<NetworkData> readData;

	/**
	 * Instantiates a new GameClientManager with a GameClient.
	 */
	public GameClientManager() {

		this.client = new GameClient(DEFAULT_ADDRESS, DEFAULT_PORT);
		this.attempt = false;
		this.readData = new ArrayDeque<NetworkData>();
		this.readData.add(NetworkData.DISCONNECTED);

	}

	/**
	 * Instantiates a new game client manager.
	 *
	 * @param address the address
	 * @param port the port
	 */
	public GameClientManager(String address, int port) {

		this.client = new GameClient(address, port);
		this.attempt = false;
		this.readData = new ArrayDeque<NetworkData>();
		this.readData.add(NetworkData.DISCONNECTED);

	}

	/**
	 * Stops the GameClientManager
	 */
	public void end() {
		this.attempt = false;
		this.running = false;
		this.connected = false;
	}

	/**
	 * Entry-point of the GameClientManager/Begins the GameClientManager
	 * 
	 * @return false if GameClientManager fails to connect.
	 */
	public boolean start() {

		this.makeAutoAttemptThread();
		this.attempt = true;

		if (this.running) {
			return true;
		}

		if (!this.client.connectBlocking()) {
			return false;
		}
		
		this.running = true;
		this.connected = true;

		this.makeNetworkThreads();

		return true;

	}

	/**
	 * Returns whether or not the GameClientManager is running.
	 * 
	 * @return true if running, false if not running
	 */
	public boolean isRunning() {
		return this.running;
	}

	/**
	 * Checks if is connected.
	 *
	 * @return true, if is connected
	 */
	public boolean isConnected() {
		return this.connected;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public synchronized NetworkData getData() {
		if (this.readData.size() < 1) {
			return null;
		}
		return this.readData.remove();
	}

	private void makeAutoAttemptThread() {

		if (this.attempt) {
			return;
		}
		synchronized (this) {

			if (this.attempt) {
				return;
			}
			this.attempt = true;

			new Thread(() -> {

				while (this.attempt) {

					try {
						Thread.sleep(ATTEMPT_TIMEOUT_MILLIS);
					} catch (InterruptedException e) {
					}

					if (!this.isRunning()) {
						this.start();
					}
				}

			}).start();

		}
	}

	private void makeNetworkThreads() {
		new Thread(() -> {

			while (this.running) {
				try {
					Thread.sleep(HEARBEAT_TIMEOUT_MILLIS);
				} catch (InterruptedException e) {
				}

				this.client.sendHeartbeat();
			}
			while (!this.client.close()) {
				this.client.close();
			}
		}).start();

		new Thread(() -> {

			while (this.running) {

				this.networkProcess();

				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
				}

			}

			while (!this.client.close()) {
				this.client.close();
			}

		}).start();
	}

	private synchronized void networkProcess() {
		NetworkData data = this.client.readRequest();
		if (data != null) {
			this.readData.add(data);
		}
	}

}
