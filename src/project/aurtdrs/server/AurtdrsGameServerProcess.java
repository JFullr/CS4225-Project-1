package project.aurtdrs.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import project.aurtdrs.process.AurtdrsRoadTrain;
import project.game.network.NetworkData;
import project.game.network.NetworkState;
import project.game.network.server.GameServerManager;
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
	 * Enumeration for the major states of the server.
	 */
	private enum ServerState {

		WAITING_FOR_CLIENTS, 
		PROCESSING_GAME, 
		RESULTS_SCREEN

	}

	private ServerState state;
	private GameServerManager server;

	private ArrayList<Client> clients;
	private HashMap<Client, String> userNames;

	/*
	 * private boolean readyToPlay; private boolean playing; private boolean
	 * matchOver;
	 */

	/**
	 * Instantiates a new aurtdrs game server process.
	 *
	 * @param server
	 *            the server
	 * @param clients
	 *            the clients
	 */
	public AurtdrsGameServerProcess(GameServerManager server, ArrayList<Client> clients) {

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

			case WAITING_FOR_CLIENTS:
	
				this.syncAndLobby();
	
				break;
			case PROCESSING_GAME:
	
				this.inGame();
	
				break;
			case RESULTS_SCREEN:
	
				//
	
				break;
			default:
				break;

		}

	}

	private void syncAndLobby() {

		for (Client client : this.clients) {

			NetworkData theData = this.server.getData(client);

			if (theData != null) {

				this.syncAndLobbyActOnData(client, theData);

			}

		}

	}
	
	private void syncAndLobbyActOnData(Client client, NetworkData theData) {
		if (theData.getState() == NetworkState.SYNCHRONIZING) {
			
			this.nameDiscrimination();
			
		}
		///TODO add in constant knowing of how large the player pool is for connection alerts to other clients
		/*
		else if (theData.getState() == NetworkState.LOBBY) {
			this.sendDataToAll(new NetworkData(NetworkState.SYNCHRONIZING, this.clients.size()));
		}
		*/
	}

	private void kickPlayers() {

		int count = 0;
		for (Client client : this.clients) {
			count++;
			if (count > MAX_CLIENTS) {

				this.sendDataToAll(new NetworkData(NetworkState.DISCONNECTED, "Game Room Full / Game In Progress -- Please Try Again"));
				client.close();

			}
		}
	}

	private void nameDiscrimination() {

		for (Client client : this.clients) {

			NetworkData theData = this.server.getData(client);
			Object[] theObjects = theData.getData();
			String nameToCheck = String.valueOf(theObjects[0]);
			if (nameToCheck == null) {
				this.nameRejected();
				return;
			}

			for (Client nameKey : this.userNames.keySet()) {
				if (this.userNames.get(nameKey).equals(nameToCheck)) {
					this.nameRejected();
				}
			}

			this.nameSuccess(client);

		}
	}

	private void nameRejected() {
		String outputMessage = "Name not unique, please try a different name";

		this.sendDataToAll(new NetworkData(NetworkState.SYNCHRONIZING, outputMessage));
	}

	private void nameSuccess(Client client) {
System.out.println("NAME TO LOBBY");
		this.sendData(client, new NetworkData(NetworkState.LOBBY));
	}

	private void inGame() {

		NetworkData data = this.collectData();
		this.race(data);

	}

	private void lobbyProcess() {

		// TODO maybe add lobby count polling to client, code immediately after is the
		// lobby count.


		this.sendDataToAll(new NetworkData(NetworkState.LOBBY, this.clients.size()));

		if (this.clients.size() == MAX_CLIENTS) {
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
	 * @param aggregate
	 *            the aggregate
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
	 * @param train
	 *            the train
	 * @return true, if successful
	 */
	private boolean endCondition(AurtdrsRoadTrain train) {

		if (train.getDistance() > GOAL) {
			return true;
		}

		return false;
	}
	
	private void sendData(Client client, NetworkData data) {

		this.server.sendData(client, data);
		
	}
	
	private void sendDataToAll(NetworkData data) {

		for (Client client : this.clients) {
			this.server.sendData(client, data);
		}

	}

}