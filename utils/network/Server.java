package utils.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The Class Server represents the server side of the client-server connection.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class Server {

	private final Object requestLock = new Object();
	private ServerSocket server;
	private AtomicBoolean running;
	private Queue<Client> defaultRequests;
	private Thread serverThread;

	/**
	 * Instantiates a new server.
	 *
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Server(int port) throws IOException {

		this.server = new ServerSocket(port);

		this.serverThread = null;
		this.defaultRequests = new ArrayDeque<Client>();
		this.running = new AtomicBoolean(false);

	}

	/**
	 * Checks if the server is started.
	 *
	 * @return true, if is started
	 */
	public boolean isStarted() {
		return this.running.get();
	}

	/**
	 * Terminates the server.
	 */
	public void end() {
		if (this.serverThread != null) {
			synchronized (this.requestLock) {

				this.serverThread.interrupt();
				this.serverThread = null;
				this.running.set(false);
				this.defaultRequests.clear();

			}
		}
	}

	/**
	 * Gets the next request from the client.
	 *
	 * @return the next request
	 */
	public Client getNextRequest() {
		synchronized (this.requestLock) {
			if (this.defaultRequests.isEmpty()) {
				return null;
			}
			return this.defaultRequests.remove();
		}
	}

	/**
	 * Waits until the next request from the client.
	 *
	 * @return the client
	 */
	public Client waitNextRequest() {
		synchronized (this.requestLock) {
			if (this.defaultRequests.isEmpty()) {
				try {
					this.requestLock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return this.defaultRequests.remove();
		}
	}

	/**
	 * Start.
	 */
	public void start() {
		this.start(null);
	}

	/**
	 * Start.
	 *
	 * @param immediateHandler the immediate handler
	 * @return true, if successful
	 */
	public boolean start(NetworkHandler immediateHandler) {

		synchronized (this.requestLock) {

			if (this.serverThread != null) {
				return false;
			}

			this.serverThread = this.makeServerThread(immediateHandler);

			this.serverThread.start();
		}

		return this.serverThread != null;

	}

	private Thread makeServerThread(NetworkHandler immediateHandler) {

		return new Thread(() -> {
			this.running.set(true);
			while (this.running.get()) {

				try {

					Socket request = this.server.accept();
					this.handleIncoming(immediateHandler, request);

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

	}

	private void handleIncoming(NetworkHandler immediateHandler, Socket request) {

		Client client = null;
		try {
			client = new Client(request);
		} catch (IOException e1) {
			return;
		}

		final Client shim = client;

		new Thread(() -> {

			if (immediateHandler != null) {

				immediateHandler.request(shim);

			} else {

				synchronized (this.requestLock) {
					this.defaultRequests.add(shim);
					this.requestLock.notifyAll();
				}

			}

		}).start();
	}

}
