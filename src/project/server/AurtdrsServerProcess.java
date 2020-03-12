package project.server;

import java.io.IOException;
import java.util.ArrayList;

import project.client.NetworkData;
import project.client.NetworkState;
import project.game.aurtdrs.AurtdrsRoadTrain;
import utils.network.Client;

public class AurtdrsServerProcess {
	
	private static final int TIMEOUT_MILLIS = 50;
	
	private static final double GOAL = 7.7E7;
	
	private GameServerManager server;
	private Iterable<Client> clients;
	
	public AurtdrsServerProcess(GameServerManager server, Iterable<Client> clients) {
		
		this.clients = clients;
		this.server = server;
		
	}
	
	public void processGame() {
		
		NetworkData data = this.collectData();
		this.race(data);
		
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
