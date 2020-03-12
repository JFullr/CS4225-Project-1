package project.aurtdrs.client;

import project.game.network.NetworkData;
import project.game.network.client.GameClientManager;

/**
 * The Class AurtdrsGameClient.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class AurtdrsGameClient {
	
	private GameClientManager client;
	
	/**
	 * Instantiates a new aurtdrs game client.
	 */
	public AurtdrsGameClient() {
		this.client = new GameClientManager();
	}
	
	/**
	 * Instantiates a new aurtdrs game client.
	 *
	 * @param address the address
	 * @param port the port
	 */
	public AurtdrsGameClient(String address, int port) {
		this.client = new GameClientManager(address, port);
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
