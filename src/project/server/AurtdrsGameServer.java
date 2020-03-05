package project.server;

import java.io.IOException;

public class AurtdrsGameServer {
	
	private GameServer server;
	
	public AurtdrsGameServer(int port) throws IOException {
		this.server = new GameServer(port);
	}
	
	public void start() {
		this.server.start();
	}
	
	public void end() {
		this.server.end();
	}
	
}
