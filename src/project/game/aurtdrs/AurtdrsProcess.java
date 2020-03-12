package project.game.aurtdrs;

import java.awt.Graphics;

import project.client.NetworkData;
	
/**
 * The Interface AurtdrsProcess.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public interface AurtdrsProcess {
	
	/**
	 * Resets the current state
	 */
	void resetState();
	
	/**
	 * Process state data.
	 *
	 * @param data the data
	 */
	NetworkData processState(NetworkData data);

	/**
	 * The computation process.
	 */
	void tick();

	/**
	 * Render.
	 *
	 * @param graphics    the graphics
	 * @param frameWidth  the frame width
	 * @param frameHeight the frame height
	 */
	void render(Graphics graphics, int frameWidth, int frameHeight);

}
