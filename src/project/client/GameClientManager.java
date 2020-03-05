package project.client;

public class GameClientManager {
	
	private static final int ATTEMPT_TIMEOUT_MILLIS = 10000;
	
	private static final int HEARBEAT_TIMEOUT_MILLIS = 5000;
	private static final String ADDRESS = "127.0.0.1";
	private static final int PORT = 4225;
	
	private GameClient client;
	
	private volatile boolean running;
	private boolean attempt;
	
	public GameClientManager() {
		
		this.client = new GameClient(ADDRESS,PORT);
		this.attempt = false;
		
	}
	
	public void end() {
		this.attempt = false;
		this.running = false;
	}
	
	public boolean start() {
		
		synchronized(this) {
			
			this.makeAutoAttemptThread();
		
			if(this.running) {
				return true;
			}
			
			if(!this.client.connectBlocking()) {
				return false;
			}
			
			this.running = true;
			
			this.makeNetworkThreads();
			
			return true;
		
		}
		
	}
	
	public boolean isRunning() {
		return this.running;
	}
	
	private void networkProcess() {
		
		
		
	}
	
	private void makeAutoAttemptThread() {
		
		if(this.attempt) {
			return;
		}
		synchronized(this) {
			
			if(this.attempt) {
				return;
			}
			this.attempt = true;
			
			new Thread(()->{
				
				while(this.attempt) {
					
					try {
						Thread.sleep(ATTEMPT_TIMEOUT_MILLIS);
					} catch (InterruptedException e) {}
					
					if(!this.isRunning()) {
						this.start();
					}
				}
				
			}).start();
			
		}
	}
	
	private void makeNetworkThreads() {
		new Thread(()->{
			
			while(this.running) {
				
				try {
					Thread.sleep(HEARBEAT_TIMEOUT_MILLIS);
				} catch (InterruptedException e) {}
				
				this.client.sendHeartbeat();
			}
			
			while(!this.client.close()) {
				this.client.close();
			}
			
		}).start();
		
		new Thread(()->{
			
			while(this.running) {
				
				this.networkProcess();
				
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {}
				
			}
			
			while(!this.client.close()) {
				this.client.close();
			}
			
		}).start();
	}
	
}
