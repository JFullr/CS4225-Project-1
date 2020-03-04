package utils.network;

/**
 * The Interface NetworkHandler.
 *
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public interface NetworkHandler {

	/**
	 * Interfaces request for the client to the server.
	 *
	 * @param request the request of the client
	 */
	void request(Client request);

}
