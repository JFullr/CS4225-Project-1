package project.aurtdrs.process;

import java.awt.Graphics;

import javax.swing.JOptionPane;

import project.game.network.NetworkData;
import project.game.network.NetworkState;

/**
 * The Class Synchronize.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class Synchronize implements AurtdrsProcess {

	private String userName;
	private boolean enteringName;

	/**
	 * Instantiates a new synchronize.
	 */
	public Synchronize() {

		this.enteringName = false;
		
	}

	/**
	 * Tick.
	 */
	public void tick() {
		
		
		if(!this.enteringName) {
		
			this.enteringName = true;
			String nameInput = JOptionPane.showInputDialog(null, "Enter Name");
			
		}

	}

	/**
	 * Render.
	 *
	 * @param graphics    the graphics
	 * @param frameWidth  the frame width
	 * @param frameHeight the frame height
	 */
	public void render(Graphics graphics, int frameWidth, int frameHeight) {

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

		if (data.getState() == NetworkState.SYNCHRONIZING) {
			if (this.userName != null) {
				return new NetworkData(NetworkState.SYNCHRONIZING, this.userName);
			}
		}

		return null;

	}

	/**
	 * Reset state.
	 */
	public void resetState() {

	}

}
