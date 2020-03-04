package utils.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The Class Client represents a client side of a client-server connection.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class Client {

	private String address;
	private int port;
	private int timeout;
	private Socket client;

	private ObjectInputStream input;
	private ObjectOutputStream output;

	/**
	 * Instantiates a new client.
	 *
	 * @param address the address
	 * @param port    the port
	 */
	public Client(String address, int port) {

		this.initClient(address, port, 0);

	}

	/**
	 * Instantiates a new client.
	 *
	 * @param address          the address
	 * @param port             the port
	 * @param timeoutInSeconds the timeout in seconds
	 */
	public Client(String address, int port, int timeoutInSeconds) {

		this.initClient(address, port, timeoutInSeconds);

	}
	
	private void initClient(String address, int port, int timeoutInSeconds) {
		this.input = null;
		this.output = null;
		this.address = address;
		this.port = port;
		this.timeout = timeoutInSeconds * 1000;
	}

	/**
	 * Instantiates a new client.
	 *
	 * @param initialized the initialized socket
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Client(Socket initialized) throws IOException {

		this.client = initialized;

		try {
			this.input = new ObjectInputStream(this.client.getInputStream());
		} catch (java.lang.NullPointerException e) {
			e.printStackTrace();
		}
		try {
			this.output = new ObjectOutputStream(this.client.getOutputStream());
		} catch (java.lang.NullPointerException e) {
		}

	}

	/**
	 * Connect.
	 *
	 * @param initData the initialization data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void connect(Object initData) throws IOException {

		this.client = new Socket(this.address, this.port);
		this.client.setSoTimeout(this.timeout);

		this.output = new ObjectOutputStream(this.client.getOutputStream());
		if (initData == null) {
			this.output.writeObject(0);
		} else {
			this.output.writeObject(initData);
		}
		this.output.flush();

		this.input = new ObjectInputStream(this.client.getInputStream());

	}

	/**
	 * Sends client data to the server.
	 *
	 * @param data the data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void sendData(Object data) throws IOException {

		if (this.output == null) {
			return;
		}

		this.output.writeObject(data);
		this.output.flush();

	}

	/**
	 * Reads the blocking.
	 *
	 * @return the object
	 * @throws ClassNotFoundException the class not found exception
	 * @throws IOException            Signals that an I/O exception has occurred.
	 */
	public Object readBlocking() throws ClassNotFoundException, IOException {

		if (this.input == null) {
			return null;
		}

		return this.input.readObject();

	}

}
