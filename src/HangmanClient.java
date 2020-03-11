import project.client.GameClientManager;
import project.client.NetworkData;
import project.client.NetworkGameState;

public class HangmanClient {
	
	private static final String DISCONNECT = "QUIT";
	private static final String CONNECT = "CONNECT";
	private static final String ANSWER = "SUCCESS";

	private GameClientManager client;
	private String username;
	
	public HangmanClient(String address, int port) {
		this.client = new GameClientManager();
		this.username = null;
	}
	
	public void setName(String name) {
		
	}
	
	public void game() throws InterruptedException {
		
		NetworkData data = null;
		do {
			
			
			do{
				Thread.sleep(10);
			}while(! this.client.isConnected());
			
			this.sendClientConnect();
			
			data = this.client.getData();
			while(data == null || data.getState() == NetworkGameState.HEART_BEAT);
		
		} while(data == null || data.getState() == NetworkGameState.DISCONNECTED);
		
	}
	
	public void sendClientDisconnect() {
		
		this.client.sendData(new NetworkData(NetworkGameState.LOBBY, DISCONNECT));
		
	}
	
	public void sendClientConnect() {
		
		this.client.sendData(new NetworkData(NetworkGameState.LOBBY, CONNECT, this.username));
		
	}
	
	public void sendClientAnswer(String letter) {
		
		this.client.sendData(new NetworkData(NetworkGameState.IN_GAME, CONNECT, letter));
		
	}
	
}
