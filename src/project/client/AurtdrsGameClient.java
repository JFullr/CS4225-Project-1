package project.client;

/**
 * The Class AurtdrsGameClient.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class AurtdrsGameClient {
	
	private static final int HEARTBEAT_TIMOUT_MILLIS = 5000;
	
	private GameClientManager client;
	private boolean running;
	
	/**
	 * Instantiates a new aurtdrs game client.
	 */
	public AurtdrsGameClient() {
		this.client = new GameClientManager();
		this.running = false;
	}
	
	/**
	 * Instantiates a new aurtdrs game client.
	 *
	 * @param address the address
	 * @param port the port
	 */
	public AurtdrsGameClient(String address, int port) {
		this.client = new GameClientManager(address, port);
		this.running = false;
	}
	
	/**
	 * Start.
	 */
	public synchronized void start() {
		
		this.client.start();
		
	}
	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public synchronized NetworkData getData() {
		return this.client.getData();
	}

}
