package project.server;

import java.io.IOException;

public class AurtdrsGameServer {
	
	private GameServerManager server;

	public AurtdrsGameServer(int port) throws IOException {
		this.server = new GameServerManager(port);
	}
	
	public void start() {
		this.server.start();
	}
	
	public void end() {
		this.server.end();
	}
	
}
