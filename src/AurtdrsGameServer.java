
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import project.server.GameServerManager;
import utils.network.Client;

/**
 * The Class AurtdrsGameServer.
 *
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class AurtdrsGameServer {

	private GameServerManager server;
	private HashMap<ArrayList<Client>, AurtdrsServerProcess> gameProcesses;

	/**
	 * Instantiates a new aurtdrs game server.
	 *
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public AurtdrsGameServer(int port) throws IOException {
		this.server = new GameServerManager(port);
	}

	/**
	 * Start.
	 */
	public void start() {
		this.server.start();
		this.gameProcess();
	}

	/**
	 * End.
	 */
	public void end() {
		this.server.end();
	}

	/**
	 * Game process.
	 */
	public void gameProcess() {

		new Thread(() -> {

			while (true) {

				this.setGamePools();
				this.processGames();

				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
				}

			}

		}).start();

	}

	private void setGamePools() {

		for (ArrayList<Client> pool : this.server.getGamePools()) {

			if (!this.gameProcesses.keySet().contains(pool)) {
				this.gameProcesses.put(pool, new AurtdrsServerProcess(pool));
			}

		}

	}

	/**
	 * Process games.
	 */
	public void processGames() {

		for (ArrayList<Client> pool : this.server.getGamePools()) {

			this.gameProcesses.get(pool).processGame();

		}

	}

}
