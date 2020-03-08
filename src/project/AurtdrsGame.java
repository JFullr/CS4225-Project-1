package project;

import project.client.GameClientManager;

/**
 * The Class AurtdrsGame represents the game "Ultimate Australian Road Train Drag Racing Simulator".
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class AurtdrsGame {
	
	private Gui gui;
	private GameClientManager network;
	
	/**
	 * Instantiates a new game of "Ultimate Australian Road Train Drag Racing Simulator".
	 */
	public AurtdrsGame() {
		this.gui = new Gui();
		this.network = new GameClientManager();
	}
	
	/**
	 * Starts the game.
	 */
	public void start() {
		this.gui.start();
		this.network.start();
	}
	
	/**
	 * Ends the game.
	 */
	public void end() {
		this.gui.end();
		this.network.end();
	}

}
