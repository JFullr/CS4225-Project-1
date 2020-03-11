import project.client.GameClientManager;
import project.client.NetworkData;
import project.client.NetworkGameState;

/**
 * The Class XClient.
 *
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class XClient {

	private GameClientManager client;

	/**
	 * Instantiates a new x client.
	 */
	public XClient() {

		this.client = new GameClientManager();
		this.client.start();

	}

	/**
	 * Connect.
	 */
	public static void main(String[] args) {
		HangmanServer server = new HangmanServer(4225);
		
		server.start();
	}

}
