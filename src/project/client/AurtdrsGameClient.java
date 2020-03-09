package project.client;

public class AurtdrsGameClient {
	
	private static final int HEARTBEAT_TIMOUT_MILLIS = 5000;
	
	private GameClient client;
	private boolean running;
	
	public AurtdrsGameClient(String address, int port) {
		this.client = new GameClient(null, 0);
		this.running = false;
	}
	
	public synchronized void start() {
		
		if(this.running) {
			return;
		}
		
		this.running = true;
		
		new Thread(()->{
			while(!this.client.connectBlocking()) {
				//
			}
		}).start();
		
		new Thread(()->{
			while (true) {
				try {
					Thread.sleep(HEARTBEAT_TIMOUT_MILLIS);
				} catch (InterruptedException e) {
				}
				this.client.sendHeartbeat();
			}
		}).start();
		
	}

}
