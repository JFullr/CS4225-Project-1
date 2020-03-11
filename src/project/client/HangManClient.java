package project.client;

/**
 * The Class HangManClient.
 *
 * @author Tim
 */
public class HangManClient {

	private static final int HEARTBEAT_TIMOUT_MILLIS = 5000;

	private GameClientManager client;
	private boolean running;
	private HangManManagerClient hangManMangager;

	/**
	 * Instantiates a new hang man client.
	 */
	public HangManClient() {
		this.client = new GameClientManager();
		this.running = false;
		this.hangManMangager = new HangManManagerClient(this.client.toString());
	}

	/**
	 * Instantiates a new hang man client.
	 *
	 * @param address the address
	 * @param port the port
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