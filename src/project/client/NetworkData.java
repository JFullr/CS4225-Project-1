package project.client;

/**
 * The Class NetworkData encapsulates the data to be sent amongst the server and
 * the clients for "Ultimate Australian Road Train Drag Racing Simulator".
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020.
 */
public class NetworkData {

	public static final transient NetworkData HEART_BEAT = new NetworkData(NetworkGameState.HEART_BEAT);

	private NetworkGameState state;
	private Object[] data;

	/**
	 * Instantiates a new network data object with the given network game state and
	 * how ever many objects represented as data.
	 *
	 * @param state the network's game state
	 * @param data  the data to persist and distribute
	 */
	public NetworkData(NetworkGameState state, Object... data) {
		this.state = state;
		this.data = data;
	}

	/**
	 * Gets the current network state of the game.
	 *
	 * @return the current network state of the game
	 */
	public NetworkGameState getState() {
		return this.state;
	}

	/**
	 * Gets the data of the current network state.
	 *
	 * @return the data of the current network state
	 */
	public Object[] getData() {
		return this.data;
	}

}
