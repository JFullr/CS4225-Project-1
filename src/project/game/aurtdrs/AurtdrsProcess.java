package project.game.aurtdrs;

import java.awt.Graphics;

/**
 * The Interface AurtdrsProcess.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public interface AurtdrsProcess {

	/**
	 * Tick.
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
