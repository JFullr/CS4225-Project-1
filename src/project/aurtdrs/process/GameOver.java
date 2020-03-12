package project.aurtdrs.process;

import java.awt.Graphics;

import project.game.network.NetworkData;

/**
 * The Class GameOver represents what happens after the game as ended.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class GameOver implements AurtdrsProcess {

	/**
	 * Instantiates a new game over.
	 */
	public GameOver() {

	}

	/**
	 * Tick.
	 */
	public void tick() {

	}

	/**
	 * Render.
	 *
	 * @param grapics     the grapics
	 * @param frameWidth  the frame width
	 * @param frameHeight the frame height
	 */
	public void render(Graphics grapics, int frameWidth, int frameHeight) {

	}

	/**
	 * Process state.
	 *
	 * @param data the data
	 * @return the network data
	 */
	public NetworkData processState(NetworkData data) {
		if (data == null) {
			return null;
		}
		
		
		return null;
	}

	/**
	 * Reset state.
	 */
	public void resetState() {

	}

}
