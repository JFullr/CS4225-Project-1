package project;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Class Main.
 *
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class Main {

	/**
	 * The main method begins a new game.
	 *
	 * @param args not used
	 * @throws IOException server error
	 */
	public static void main(String[] args) throws IOException {

		/*
		 * if (args != null && args.length > 0 &&
		 * args[0].toLowerCase().startsWith("-server")) {
		 * 
		 * AurtdrsGameServer server = new AurtdrsGameServer(GAME_PORT); server.start();
		 * 
		 * } else {
		 * 
		 * AurtdrsGame game = new AurtdrsGame(); game.start();
		 * 
		 * } //
		 */

		/*
		 * 
		 * AurtdrsGameServer server = new AurtdrsGameServer(GAME_PORT); server.start();
		 * 
		 * /
		 */

		// */
	}

	/**
	 * Starts the jfx.
	 * 
	 * @param window The xml Window it will open.
	 * @throws Exception If it can't find the scene
	 */
	public void start(Stage window) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("project/client/HangManGUI.fxml"));
		Scene scene = new Scene(root, 200, 200);
		window.setScene(scene);
		window.show();

	}
}
