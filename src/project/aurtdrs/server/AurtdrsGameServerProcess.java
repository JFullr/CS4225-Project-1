package project.aurtdrs.server;

import java.util.ArrayList;
import java.util.HashMap;

import project.aurtdrs.process.AurtdrsRoadTrain;
import project.aurtdrs.process.AurtdrsRoadTrainTransmission;
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

	private static final int MAX_CLIENTS = 3;
	private static final int TIMEOUT_MILLIS = 50;
	private static final double GOAL = 7.7E7;

	/**
	 * Enumeration for the major states of the server.
	 */
	private enum ServerState {

		WAITING_FOR_CLIENTS, PROCESSING_GAME, RESULTS_SCREEN

	}

	private ServerState state;
	private GameServerManager server;

	private volatile ArrayList<Client> clients;

	private HashMap<Client, String> userNames;
	private HashMap<Client, NetworkState> userStates;

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
		this.userStates = new HashMap<Client, NetworkState>();

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

		this.lobbyProcess();

	}

	private void syncAndLobbyActOnData(Client client, NetworkData theData) {

		this.userStates.put(client, theData.getState());

		if (theData.getState() == NetworkState.SYNCHRONIZING) {

			this.nameDiscrimination(client, theData);

		}
	}

	private void nameDiscrimination(Client client, NetworkData theData) {

		Object[] theObjects = theData.getData();
		String nameToCheck = (String) theObjects[0];
		if (nameToCheck == null) {
			this.nameRejected(client);
			return;
		}

		for (String name : this.userNames.values()) {
			if (name.equals(nameToCheck)) {
				this.nameRejected(client);
				return;
			}
		}

		this.userStates.put(client, NetworkState.LOBBY);
		this.userNames.put(client, new String(nameToCheck));

		this.nameSuccess(client);

	}

	private void nameRejected(Client client) {

		String outputMessage = "Name not unique, please try a different name";
		this.sendData(client, new NetworkData(NetworkState.SYNCHRONIZING, outputMessage));

	}

	private void nameSuccess(Client client) {
		this.sendDataToAll(new NetworkData(NetworkState.PLAYER_QUIT, this.userNames.get(client) + " Has Joined"));
		this.sendData(client, new NetworkData(NetworkState.LOBBY, this.getValidClients().size()));
	}

	private void inGame() {
		try {
			Thread.sleep(TIMEOUT_MILLIS);
		} catch (InterruptedException e) {
		}

		NetworkData data = this.collectData();

		if (data.getState() == NetworkState.IN_GAME) {
			AurtdrsRoadTrainTransmission[] pack = (AurtdrsRoadTrainTransmission[]) data.getData()[0];
			int self = 0;
			for (Client client : this.clients) {

				AurtdrsRoadTrainTransmission tmp = pack[self];

				pack[self] = null;

				this.sendData(client, new NetworkData(NetworkState.IN_GAME, (Object) pack));

				pack[self] = tmp;

				self++;
			}
		} else {
			this.sendDataToAll(data);
			if (this.state == ServerState.WAITING_FOR_CLIENTS) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
				}
				this.removeAllClients();
			}
		}

	}

	private void lobbyProcess() {

		if (this.getValidClients().size() >= MAX_CLIENTS) {
			this.state = ServerState.PROCESSING_GAME;
			this.sendDataToAll(new NetworkData(NetworkState.IN_GAME, (Object) null));
		}

	}

	private ArrayList<Client> getValidClients() {
		ArrayList<Client> valid = new ArrayList<Client>();
		for (Client client : this.clients) {
			if (this.server.isClientConnected(client)) {
				if (this.userStates.get(client) == NetworkState.LOBBY) {
					valid.add(client);
				}
			}
		}
		return valid;
	}

	private NetworkData collectData() {

		ArrayList<AurtdrsRoadTrainTransmission> trains = new ArrayList<AurtdrsRoadTrainTransmission>();
		HashMap<AurtdrsRoadTrainTransmission, Client> winnerMap = new HashMap<AurtdrsRoadTrainTransmission, Client>();
		for (Client client : this.clients) {
			try {
				NetworkData gameData = this.server.getData(client);
				if (gameData == null) {
					trains.add(null);
				} else if (gameData.getState() == NetworkState.PLAYER_QUIT) {
					trains.add(null);
					this.sendDataToAll(
							new NetworkData(NetworkState.PLAYER_QUIT, this.userNames.get(client) + " Has Forfeited"));
				} else if (gameData.getState() != NetworkState.IN_GAME) {
					this.sendData(client,
							new NetworkData(NetworkState.DISCONNECTED, "Game In Progress Come back Later"));
					client.close();
				} else {
					this.handleInGameData(trains, winnerMap, client, gameData);
				}
			} catch (Exception e) {
				trains.add(null);
			}
		}

		return this.getGameBundle(trains, winnerMap);

	}

	private NetworkData getGameBundle(ArrayList<AurtdrsRoadTrainTransmission> trains,
			HashMap<AurtdrsRoadTrainTransmission, Client> winnerMap) {
		AurtdrsRoadTrainTransmission[] trainArr = new AurtdrsRoadTrainTransmission[trains.size()];
		trains.toArray(trainArr);

		for (AurtdrsRoadTrainTransmission train : trainArr) {
			if (train != null && this.endCondition(train.cast())) {
				String winner = this.userNames.get(winnerMap.get(train));

				this.state = ServerState.WAITING_FOR_CLIENTS;

				return new NetworkData(NetworkState.MATCH_OVER, (Object) winner);
			}
		}

		return new NetworkData(NetworkState.IN_GAME, (Object) trainArr);
	}

	private void removeAllClients() {
		for (Client client : this.clients) {
			client.close();
		}
	}

	private void handleInGameData(ArrayList<AurtdrsRoadTrainTransmission> trains,
			HashMap<AurtdrsRoadTrainTransmission, Client> winnerMap, Client client, NetworkData gameData) {
		if (gameData.getState() == NetworkState.IN_GAME) {
			AurtdrsRoadTrainTransmission trans = (AurtdrsRoadTrainTransmission) gameData.getData()[0];
			winnerMap.put(trans, client);
			trains.add(trans);
		} else {
			trains.add(null);
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
