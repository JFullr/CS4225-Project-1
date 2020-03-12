package project.game.aurtdrs;

import java.awt.Graphics;

import project.client.NetworkData;

/**
 * The Class Synchronize.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class Synchronize implements AurtdrsProcess {
	
	public Synchronize() {
		
	}
	
	public void tick() {
		
	}
	
	/**
	 * Render.
	 *
	 * @param graphics the graphics
	 * @param frameWidth the frame width
	 * @param frameHeight the frame height
	 */
	public void render(Graphics graphics, int frameWidth, int frameHeight) {
		
	}

	public void processState(NetworkData data) {
		if (data == null) {
			return;
		}
		
	}

	public void resetState() {
		
	}
	
}
