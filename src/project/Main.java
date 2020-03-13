package project;

import project.aurtdrs.AurtdrsGameSystem;
import project.aurtdrs.server.AurtdrsGameServerSystem;

/**
 * The Class Main.
 *
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class Main {

	private static final int GAME_PORT = 4225;

	/**
	 * The main method begins a new game.
	 *
	 * @param args not used
	 * @throws Exception server error
	 */
	public static void main(String[] args) throws Exception {

		/*
		if (args != null && args.length > 0 && args[0].toLowerCase().startsWith("-server")) {

			AurtdrsGameServer server = new AurtdrsGameServer(GAME_PORT);
			server.start();

		} else {

			AurtdrsGame game = new AurtdrsGame();
			game.start();

		}
		//*/
		
		/*
		
		AurtdrsGameServerSystem server = new AurtdrsGameServerSystem(GAME_PORT);
		server.start();
		
		/*/
		
		AurtdrsGameSystem game = new AurtdrsGameSystem();
		game.start();
		
		//*/
	}
	
	/**
	 * Server main.
	 *
	 * @param args the args
	 * @throws Exception the exception
	 */
	public static void serverMain(String[] args) throws Exception {
		AurtdrsGameServerSystem server = new AurtdrsGameServerSystem(GAME_PORT);
		server.start();
	}
	
	/**
	 * Client main.
	 *
	 * @param args the args
	 * @throws Exception the exception
	 */
	public static void clientMain(String[] args) throws Exception {
		AurtdrsGameSystem game = new AurtdrsGameSystem();
		game.start();
	}

}
