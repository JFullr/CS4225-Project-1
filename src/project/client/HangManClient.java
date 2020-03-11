package project.client;

/**
 * The Class HangManClient.
 *
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class HangManClient {

	private static final int HEARTBEAT_TIMOUT_MILLIS = 5000;

	private GameClientManager client;
	private boolean running;
	private HangManClientManager hangManMangager;

	/**
	 * Instantiates a new hang man client.
	 */
	public HangManClient() {
		this.client = new GameClientManager();
		this.running = false;
		this.hangManMangager = new HangManClientManager(this.client.toString());
	}

	/**
	 * Instantiates a new hang man client.
	 *
	 * @param address the address
	 * @param port    the port
	 */
	public HangManClient(String address, int port) {
		this.client = new GameClientManager(address, port);
		this.running = false;
	}

	/**
	 * Start the hang man client.
	 */
	public synchronized void start() {

		this.client.start();
		this.hangManMangager.setGameData(this.client.getData().getData());

	}

	/**
	 * Send data.
	 */
	public void sendData() {
		NetworkData gameData = new NetworkData(NetworkGameState.IN_GAME, this.hangManMangager.getGameData());
		this.client.sendData(gameData);
	}
	
	

	/**
	 * Gets the game data for hang man.
	 *
	 * @return the data
	 */
	public synchronized NetworkData getData() {
		return this.client.getData();
	}

}