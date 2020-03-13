package project.aurtdrs.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import project.game.network.server.GameServerManager;
import utils.network.Client;

/**
 * The Class AurtdrsGameServer.
 *
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class AurtdrsGameServerSystem {

	private GameServerManager server;
	private HashMap<Integer, AurtdrsGameServerProcess> gameProcesses;

	/**
	 * Instantiates a new aurtdrs game server.
	 *
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public AurtdrsGameServerSystem(int port) throws IOException {
		this.server = new GameServerManager(port);

		this.gameProcesses = new HashMap<Integer, AurtdrsGameServerProcess>();

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

	/**
	 * Sets the game pools.
	 */
	private void setGamePools() {
		
		int map = 0;
		for (ArrayList<Client> pool : this.server.getGamePools()) {

			if (!this.gameProcesses.keySet().contains(map)) {
				this.gameProcesses.put(map, new AurtdrsGameServerProcess(this.server, pool));
			}
			
			map++;

		}

	}

	/**
	 * Processes the game states in the game pool.
	 */
	public void processGames() {

		int map = 0;
		int size = this.server.getGamePools().size();
		for (int i = 0; i < size; i++) {

			this.gameProcesses.get(map).processGame();

			map++;
		}

	}

}
