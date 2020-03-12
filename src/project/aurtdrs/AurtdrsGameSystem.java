package project.aurtdrs;

import audio.VorbisPlayer;
import project.aurtdrs.process.AurtdrsEngine;
import project.game.network.client.GameClientManager;

/**
 * The Class AurtdrsGame represents the game "Ultimate Australian Road Train
 * Drag Racing Simulator".
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class AurtdrsGameSystem {

	private AurtdrsInterface gui;

	/**
	 * Instantiates a new game of "Ultimate Australian Road Train Drag Racing
	 * Simulator".
	 */
	public AurtdrsGameSystem() {

		this.initAudioInternalClassLoader();

		this.gui = new AurtdrsInterface();

	}

	/**
	 * Starts the game.
	 */
	public void start() {
		this.gui.start();
	}

	/**
	 * Ends the game.
	 */
	public void end() {
		this.gui.end();
	}

	private void initAudioInternalClassLoader() {
		try {
			VorbisPlayer dummy = new VorbisPlayer("INIT_DUMMY");
			dummy.play();
		} catch (Exception e) {
		}
	}

}
