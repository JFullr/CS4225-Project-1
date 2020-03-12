package project;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The Class AurtdrsKey is an intermediate class for handle specific key strokes
 * during the game.
 *
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class AurtdrsKey implements KeyListener {

	private static volatile boolean space;
	private static volatile boolean quit;

	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			setSpace(true);
		}
		if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			quit = true;
		}
		event.consume();
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			setSpace(false);
		}
		if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			quit = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			setSpace(true);
		}
		if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			quit = true;
		}
	}

	/**
	 * Checks if is space.
	 *
	 * @return the space
	 */
	public static boolean isSpace() {
		return space;
	}

	/**
	 * Sets the space.
	 *
	 * @param space the space to set
	 */
	public static void setSpace(boolean space) {
		AurtdrsKey.space = space;
	}

	/**
	 * Checks if is quit.
	 *
	 * @return the quit
	 */
	public static boolean isQuit() {
		return quit;
	}

	/**
	 * Sets the quit.
	 *
	 * @param quit the quit to set
	 */
	public static void setQuit(boolean quit) {
		AurtdrsKey.quit = quit;
	}

}
