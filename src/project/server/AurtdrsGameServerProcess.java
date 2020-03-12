package project.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import project.client.NetworkData;
import project.client.NetworkState;
import project.game.aurtdrs.AurtdrsRoadTrain;
import utils.network.Client;

/**
 * The Class AurtdrsGameServerProcess.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class AurtdrsGameServerProcess {

	private static final int MAX_CLIENTS = 2;
	private static final int TIMEOUT_MILLIS = 50;
	private static final double GOAL = 7.7E7;

	/**
	 * The Enum ServerState.
	 */
	private enum ServerState {

		WAITING_FOR_CLIENTS, PROCESSING_GAME, RESULTS_SCREEN

	}

	private ServerState state;
	private GameServerManager server;

	private Iterable<Client> clients;
	private HashMap<Client, String> userNames;

	/*
	 * private boolean readyToPlay; private boolean playing; private boolean
	 * matchOver;
	 */

	/**
	 * Instantiates a new aurtdrs game server process.
	 *
	 * @param server  the server
	 * @param clients the clients
	 */
	public AurtdrsGameServerProcess(GameServerManager server, Iterable<Client> clients) {

		this.clients = clients;
		this.server = server;

		this.userNames = new HashMap<Client, String>();

		/*
		 * this.readyToPlay = false; this.playing = false; this.matchOver = true;
		 */

		this.state = ServerState.WAITING_FOR_CLIENTS;

	}

	/**
	 * Processes the current game state.
	 */
	public void processGame() {

		switch (this.state) {
			default:
				break;

		}

	}

	private void kickPlayers() {

		int count = 0;
		for (Client client : this.clients) {
			count++;
			if (count > MAX_CLIENTS) {
				this.sendData(new NetworkData(NetworkState.DISCONNECTED,
						"Game Room Full / Game In Progress -- Please Try Again"));
				client.close();
			}
		}
	}

//	private void syncAndLobby() {
//
//		// send out lobby when available
//		// when lobby is full, goto game process -- NetworkState.IN_GAME
//
//		this.nameDiscrimination();
//		this.lobbyProcess();
//
//	}

	private void nameDiscrimination() {
		int count = 0;

		for (Client client : this.clients) {
			count++;

			NetworkData theData = this.server.getData(client);
			Object[] theObjects = theData.getData();
			String nameToCheck = String.valueOf(theObjects[1]);

			if (nameToCheck != null && !this.userNames.values().contains(nameToCheck) && count <= MAX_CLIENTS) {
				this.userNames.put(client, nameToCheck);
			} else {
				this.nameRejected();
			}
		}
	}

	private void nameRejected() {
		String outputMessage = "Name not unique, please try a different name";

		this.sendData(new NetworkData(NetworkState.SYNCHRONIZING, outputMessage));
	}

	private void inGame() {

		NetworkData data = this.collectData();
		this.race(data);

	}

	private void lobbyProcess() {

		// TODO maybe add lobby count polling to client, code immediately after is the
		// lobby count.

		int count = 0;
		for (Client cli : this.clients) {
			count++;
			cli.getClass(); // to make checkstyle shut up
		}

		this.sendData(new NetworkData(NetworkState.LOBBY, count));

		if (count == MAX_CLIENTS) {
			this.state = ServerState.PROCESSING_GAME;
		}

	}

	/**
	 * Collect data.
	 *
	 * @return the network data
	 */
	private NetworkData collectData() {

		ArrayList<AurtdrsRoadTrain> trains = new ArrayList<AurtdrsRoadTrain>();

		for (Client client : this.clients) {
			try {
				NetworkData gameData = this.server.getData(client);
				if (gameData == null) {
					trains.add(null);
				} else {
					if (gameData.getState() == NetworkState.IN_GAME) {
						AurtdrsRoadTrain train = (AurtdrsRoadTrain) gameData.getData()[0];
						trains.add((AurtdrsRoadTrain) gameData.getData()[0]);
						if (this.endCondition(train)) {
							// TODO FIXME send winner data
							return new NetworkData(NetworkState.MATCH_OVER, (Object) null);
						}
					} else {
						trains.add(null);
					}
				}
			} catch (Exception e) {
				// e.printStackTrace();
				trains.add(null);
			}
		}

		AurtdrsRoadTrain[] trainArr = new AurtdrsRoadTrain[trains.size()];
		trains.toArray(trainArr);

		return new NetworkData(NetworkState.IN_GAME, (Object) trainArr);

	}

	/**
	 * Race.
	 *
	 * @param aggregate the aggregate
	 */
	private void race(NetworkData aggregate) {

		for (Client client : this.clients) {
			try {
				client.sendData(aggregate);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * End condition.
	 *
	 * @param train the train
	 * @return true, if successful
	 */
	private boolean endCondition(AurtdrsRoadTrain train) {

		if (train.getDistance() > GOAL) {
			return true;
		}

		return false;
	}

	/**
	 * Send data.
	 *
	 * @param data the data
	 */
	private void sendData(NetworkData data) {

		for (Client client : this.clients) {
			try {
				client.sendData(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
