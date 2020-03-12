package project;

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
		
		AurtdrsGameServer server = new AurtdrsGameServer(GAME_PORT);
		server.start();
		
		/*/
		
		AurtdrsGame game = new AurtdrsGame();
		game.start();
		
		//*/
	}

}
