import project.client.GameClientManager;
import project.client.NetworkData;
import project.client.NetworkGameState;

/**
 * The Class HangmanClient.
 *
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class HangmanClient {

	private static final String DISCONNECT = "QUIT";
	private static final String CONNECT = "CONNECT";
	private static final String ANSWER = "SUCCESS";

	private GameClientManager client;
	private String username;

	/**
	 * Instantiates a new hangman client.
	 *
	 * @param address the address
	 * @param port    the port
	 */
	public HangmanClient(String address, int port) {
		this.client = new GameClientManager();
		this.username = null;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {

	}

	/**
	 * Game.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	public void game() throws InterruptedException {

		NetworkData data = null;
		do {

			do {
				Thread.sleep(10);
			} while (!this.client.isConnected());

			this.sendClientConnect();

			data = this.client.getData();
			while (data == null || data.getState() == NetworkGameState.HEART_BEAT) {
				System.out.print("");
			}

		} while (data == null || data.getState() == NetworkGameState.DISCONNECTED);

	}

	/**
	 * Send client disconnect.
	 */
	public void sendClientDisconnect() {
		
		this.client.sendData(new NetworkData(NetworkGameState.MATCH_OVER, DISCONNECT));
	}

	/**
	 * Send client connect.
	 */
	public void sendClientConnect() {

		this.client.sendData(new NetworkData(NetworkGameState.LOBBY, CONNECT, this.username));

	}

	/**
	 * Send client answer.
	 *
	 * @param letter the letter
	 */
	public void sendClientAnswer(String letter) {

		this.client.sendData(new NetworkData(NetworkGameState.IN_GAME, CONNECT, letter));

	}

}
