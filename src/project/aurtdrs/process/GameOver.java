package project.aurtdrs.process;

import java.awt.Color;
import java.awt.Graphics;

import project.game.network.NetworkData;
import project.game.network.NetworkState;

/**
 * The Class GameOver represents what happens after the game as ended.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class GameOver implements AurtdrsProcess {

	private String winnerName;

	/**
	 * Instantiates a new game over.
	 */
	public GameOver() {
		this.winnerName = "";
	}

	/**
	 * Tick.
	 */
	public void tick() {

	}

	/**
	 * Render.
	 *
	 * @param graphics     the graphics
	 * @param frameWidth  the frame width
	 * @param frameHeight the frame height
	 */
	public void render(Graphics graphics, int frameWidth, int frameHeight) {

		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, frameWidth, frameHeight);

		graphics.setColor(Color.GREEN);
		graphics.setFont(Lobby.DISPLAY);
		graphics.drawString("Winner:" + this.winnerName, frameWidth / 3, frameHeight / 2);
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

		if (data.getState() == NetworkState.MATCH_OVER) {
			this.winnerName = (String) data.getData()[0];
		}

		return null;
	}

	/**
	 * Reset state.
	 */
	public void resetState() {

	}

}
