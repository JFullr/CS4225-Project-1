package project;

import java.io.IOException;

import project.server.AurtdrsGameServer;

/**
 * The Class Main.
 *
 * @author csuser
 */
public class Main {

	private static final int GAME_PORT = 4225;

	/**
	 * The main method begins a new game.
	 *
	 * @param args not used
	 * @throws IOException server error
	 */
	public static void main(String[] args) throws IOException {
		
		if(args != null && args.length > 0 && args[0].toLowerCase().startsWith("-server")) {
			
			AurtdrsGameServer server = new AurtdrsGameServer(GAME_PORT);
			server.start();
			
		}else {

			AurtdrsGame game = new AurtdrsGame();
			game.start();
		
		}

	}

	

}
