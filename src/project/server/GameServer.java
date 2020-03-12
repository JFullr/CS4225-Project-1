package project.server;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

import project.client.NetworkData;
import project.client.NetworkState;
import utils.network.Client;
import utils.network.NetworkHandler;
import utils.network.Server;

/**
 * The Class GameServer represents the server that is currently hosting one or
 * more multi-player games. Each hosted game has a set number of player spots to
 * be filled. Once the lobby limit has been reached for a game, every player
 * attempting to join the lobby will be notified that the number of available
 * spots is zero and encourages either finding another game or wait until the
 * lobby has a cleared spot
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class GameServer {

	private static final int HEARTBEAT_TIMOUT_MILLIS = 2000;

	private Server server;
	private HashSet<Client> clients;
	private HashMap<Client,long[]> heartbeatPool;
	private HashMap<Client,Queue<NetworkData>> clientData;
	
	private volatile boolean running;

	/**
	 * Instantiates a new game server.
	 *
	 * @param port the port to connect to
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public GameServer(int port) throws IOException {

		this.server = new Server(port);
		this.clients = new HashSet<Client>();
		this.running = false;
		this.heartbeatPool = new HashMap<Client,long[]>();
		this.clientData = new HashMap<Client,Queue<NetworkData>>();

	}

	
	
	/**
	 * Starts the server hosting the game.
	 */
	public synchronized void start(NetworkHandler handler) {

		if (this.running) {
			return;
		}

		this.server.start((client) -> {
			
			synchronized (this) {
				this.clients.add(client);
				this.heartbeatPool.put(client, new long[2]);
				this.clientData.put(client, new ArrayDeque<NetworkData>());
			}
			try {
				client.sendData(new NetworkData(NetworkState.HEART_BEAT, this.clients.size()));
				System.out.println("Client Connected");
				if(handler != null) {
					handler.request(client);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			while(this.isClientConnected(client)) {
				
				try {
					
					NetworkData data = (NetworkData) client.readBlocking();
					if(data.getState() == NetworkState.HEART_BEAT) {
						this.applyHeartbeat(client);
					}else {
						if(data.getState() != NetworkState.HEART_BEAT) {
							synchronized(this.clientData) {
								this.clientData.get(client).add(data);
							}
						}
					}
					
				}catch(Exception e) {
					//e.printStackTrace();
					System.out.println("Client Disconnected");
					this.removeClient(client);
					
					return;
				}
				
			}
		});

		this.running = true;

		new Thread(() -> {
			while (true) {

				try {
					Thread.sleep(HEARTBEAT_TIMOUT_MILLIS);
				} catch (InterruptedException e) {
				}

				this.sendHeartbeats();
			}
		}).start();

	}

	/**
	 * Ends the current server session.
	 */
	public void end() {
		this.server.end();
		this.running = false;
	}
	
	
	public synchronized boolean isClientConnected(Client client) {
		return this.clients.contains(client);
	}
	
	public synchronized void applyHeartbeat(Client client) {
		this.heartbeatPool.get(client)[0] = System.currentTimeMillis();
		this.heartbeatPool.get(client)[0] = 0;
	}
	
	public NetworkData getData(Client client) {
		synchronized(clientData) {
			if(this.clientData.get(client).size() < 1) {
				return null;
			}
			return this.clientData.get(client).remove();
		}
	}

	/**
	 * Sends the current heartbeats to each client.
	 */
	public synchronized void sendHeartbeats() {
		
			HashSet<Client> toDelete = new HashSet<Client>();

			for (Client client : this.clients) {
				try {
					client.sendData(new NetworkData(NetworkState.HEART_BEAT, this.clients.size()));
				} catch (IOException e) {
					toDelete.add(client);
				}
			}
			
			long curTime = System.currentTimeMillis();
			for (Client client : this.clients) {
				if(this.heartbeatPool.get(client)[0]==0) {
					
					this.heartbeatPool.get(client)[0]=curTime;
					
				} else {
					
					long offset = curTime - this.heartbeatPool.get(client)[0] ;
					if( offset > HEARTBEAT_TIMOUT_MILLIS && curTime - this.heartbeatPool.get(client)[0] > HEARTBEAT_TIMOUT_MILLIS*2) {
						
						toDelete.add(client);
						
					} else if(offset > HEARTBEAT_TIMOUT_MILLIS && this.heartbeatPool.get(client)[0] == 0){
						
						try {
							client.sendData(NetworkData.HEART_BEAT);
							this.heartbeatPool.get(client)[1] = System.currentTimeMillis();
						} catch (IOException e) {
							toDelete.add(client);
							System.out.println("Client TimedOut");
						}
						
					}
				}
			}
			
			this.removeClients(toDelete);

	}
	
	
	private synchronized void removeClients(Iterable<Client> toDelete) {
		for (Client client : toDelete) {
			this.removeClient(client);
		}
	}
	
	private synchronized void removeClient(Client client) {
		
		if (!client.close()) {
			System.err.println("Could not close client: "+client);
		}

		this.clients.remove(client);
		this.heartbeatPool.remove(client);
		
	}

}
