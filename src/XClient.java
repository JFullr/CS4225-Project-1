import project.client.GameClientManager;
import project.client.NetworkData;
import project.client.NetworkGameState;

public class XClient {
	
	private GameClientManager client;
	
	public XClient() {
		
		client = new GameClientManager();
		client.start();
		
	}
	


	public static void main(String[] args) {
		HangmanServer server = new HangmanServer(4225);
		
		server.start();
		
		
	}
	
	
	
}
