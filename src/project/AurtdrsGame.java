package project;

import audio.VorbisPlayer;
import project.client.GameClientManager;
import project.game.aurtdrs.AurtdrsEngine;

/**
 * The Class AurtdrsGame represents the game "Ultimate Australian Road Train Drag Racing Simulator".
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class AurtdrsGame {
	
	private AurtdrsGui gui;
	private GameClientManager network;
	private AurtdrsEngine game;
	
	/**
	 * Instantiates a new game of "Ultimate Australian Road Train Drag Racing Simulator".
	 */
	public AurtdrsGame() {
		
		this.initAudioInternalClassLoader();
		
		this.game = new AurtdrsEngine();
		this.network = new GameClientManager();
		this.gui = new AurtdrsGui(this.game, this.network);
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
	
	private void initAudioInternalClassLoader() {
		try {
			VorbisPlayer dummy = new VorbisPlayer("INIT_DUMMY");
			dummy.play();
		} catch (Exception e) {
		}
	}
	
}
