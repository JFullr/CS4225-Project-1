import project.client.GameClientManager;
import project.client.NetworkData;
import project.client.NetworkGameState;

public class XClient {
	
	private GameClientManager client;
	
	public XClient() {
		
		client = new GameClientManager();
		client.start();
		
	}
	
	public connect() {

		client.sendData(new NetworkData(NetworkGameState.LOBBY,"connect"));
		
	}
	
	
	
}
