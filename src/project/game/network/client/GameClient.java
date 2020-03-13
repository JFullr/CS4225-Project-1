package project.game.network.client;

import java.io.IOException;

import project.game.network.NetworkData;
import utils.network.Client;

/**
 * Defines a client that will send and receive data for the given game.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class GameClient {

	private Client client;

	/**
	 * Instantiates a new GameClient with an address and port.
	 * 
	 * @param address the address of the GameClient
	 * @param port    the port the GameClient connects to
	 */
	public GameClient(String address, int port) {

		this.client = new Client(address, port);

	}

	/**
	 * Detects if the client can connect to a server.
	 * 
	 * @return false if fails to connect, true if connects
	 */
	public boolean connectBlocking() {

		try {
			this.client.connect(NetworkData.HEART_BEAT);
		} catch (IOException e) {
			return false;
		}
		return true;

	}

	/**
	 * Detects if the GameClient can send data to a server.
	 * 
	 * @return false if fails to send, true if sends
	 */
	public boolean sendHeartbeat() {
		try {
			this.client.sendData(NetworkData.HEART_BEAT);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * Sends NetworkData from GameClient to server
	 * 
	 * @param data NetworkData to be sent
	 * @return true if data was sent, false otherwise.
	 */
	public boolean sendData(NetworkData data) {
		try {
			this.client.sendData(data);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * Reads blocking NetworkData
	 * 
	 * @return NetworkData
	 * @throws Exception error on request or cast
	 */
	public NetworkData readRequest() throws Exception {
		return (NetworkData) this.client.readBlocking();
	}
	
	/**
	 * Attempts to close the GameClient.
	 * 
	 * @return true is client has closed, false if hasn't
	 */
	public boolean close() {
		return this.client.close();
	}

}
