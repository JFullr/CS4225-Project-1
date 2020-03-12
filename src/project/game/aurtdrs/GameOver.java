package project.game.aurtdrs;

import java.awt.Graphics;

import project.client.NetworkData;

/**
 * The Class GameOver represents what happens after the game as ended.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class GameOver implements AurtdrsProcess {
	
	public GameOver() {
		
	}
	
	public void tick() {
		
	}
	
	/**
	 * Render.
	 *
	 * @param grapics the grapics
	 * @param frameWidth the frame width
	 * @param frameHeight the frame height
	 */
	public void render(Graphics grapics, int frameWidth, int frameHeight) {
		
	}

	public void processState(NetworkData data) {
		if (data == null) {
			return;
		}
		
		
		
	}
	
	public void resetState() {
		
	}
	
}
