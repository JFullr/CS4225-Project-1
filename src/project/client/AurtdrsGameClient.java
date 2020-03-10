package project.client;

public class AurtdrsGameClient {
	
	private static final int HEARTBEAT_TIMOUT_MILLIS = 5000;
	
	private GameClientManager client;
	private boolean running;
	
	public AurtdrsGameClient() {
		this.client = new GameClientManager();
		this.running = false;
	}
	
	public AurtdrsGameClient(String address, int port) {
		this.client = new GameClientManager(address, port);
		this.running = false;
	}
	
	public synchronized void start() {
		
		this.client.start();
		
	}

}
