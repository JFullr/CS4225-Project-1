package project.server;

import java.io.IOException;
import java.util.ArrayList;

import project.client.NetworkData;
import project.client.NetworkState;
import project.game.aurtdrs.AurtdrsRoadTrain;
import utils.network.Client;

public class AurtdrsGameServerProcess {
	
	private static final int MAX_CLIENTS = 2;
	
	private static final int TIMEOUT_MILLIS = 50;
	
	private static final double GOAL = 7.7E7;
	
	private GameServerManager server;
	private Iterable<Client> clients;
	private ArrayList<String> allNames;
	
	/*
	private boolean readyToPlay;
	private boolean playing;
	private boolean matchOver;
	*/
	
	private NetworkState state;
	
	public AurtdrsGameServerProcess(GameServerManager server, Iterable<Client> clients) {
		
		this.clients = clients;
		this.server = server;
		
		/*
		this.readyToPlay = false;
		this.playing = false;
		this.matchOver = true;
		*/
		
		this.state = NetworkState.SYNCHRONIZING;
		
	}
	
	public void processGame() {
		
		switch(this.state) {
			case LOBBY:
				this.lobbyProcess();
				break;
			case DISCONNECTED:
				break;
			case HEART_BEAT:
				break;
			case IN_GAME:
				this.inGame();
				break;
			case MATCH_OVER:
				break;
			case PLAYER_CONNECTED:
				break;
			case PLAYER_DISCONNECTED:
				break;
			case SYNCHRONIZING:
				this.nameDiscrimination();
				break;
			default:
				break;
		}
		
		this.kickPlayers();
		
	}
	
	private void kickPlayers() {
		
		int count = 0;
		for(Client client : this.clients){
			count++;
			if(count > MAX_CLIENTS) {
				this.sendData(new NetworkData(NetworkState.DISCONNECTED,"Game Room Full / Game In Progress -- Please Try Again"));
				client.close();
			}
		}
		
		
	}
	
	private void nameDiscrimination() {
		
	}
	
	private void inGame() {
		
		NetworkData data = this.collectData();
		this.race(data);
		
	}
	
	private void lobbyProcess() {
		
		//TODO maybe add lobby count polling
		
		int count = 0;
		for(Client cli : this.clients){
			count++;
		}
		
		this.sendData(new NetworkData(NetworkState.LOBBY, count));
		
		if(count == MAX_CLIENTS) {
			this.state = NetworkState.IN_GAME;
		}
		
	}
	
	private NetworkData collectData() {

		ArrayList<AurtdrsRoadTrain> trains = new ArrayList<AurtdrsRoadTrain>();

		for (Client client : this.clients) {
			try {
				NetworkData gameData = this.server.getData(client);
				if (gameData == null) {
					trains.add(null);
				} else {
					if (gameData.getState() == NetworkState.IN_GAME) {
						AurtdrsRoadTrain train = (AurtdrsRoadTrain)gameData.getData()[0];
						trains.add((AurtdrsRoadTrain) gameData.getData()[0]);
						if(this.endCondition(train)) {
							//TODO FIXME send winner data
							return new NetworkData(NetworkState.MATCH_OVER, (Object)null);
						}
					}else {
						trains.add(null);
					}
				}
			} catch (Exception e) {
				//e.printStackTrace();
				trains.add(null);
			}
		}

		AurtdrsRoadTrain[] trainArr = new AurtdrsRoadTrain[trains.size()];
		trains.toArray(trainArr);

		return new NetworkData(NetworkState.IN_GAME, (Object)trainArr);
		
	}
	
	private void race(NetworkData aggregate) {
		
		for(Client client : this.clients) {
			try {
				client.sendData(aggregate);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private boolean endCondition(AurtdrsRoadTrain train) {
		
		if(train.getDistance() > GOAL) {
			return true;
		}
		
		return false;
	}
	
	
	private void sendData(NetworkData data) {
		
		for(Client client : this.clients) {
			try {
				client.sendData(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
