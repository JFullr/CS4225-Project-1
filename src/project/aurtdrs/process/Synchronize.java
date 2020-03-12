package project.aurtdrs.process;

import java.awt.Graphics;

import javax.swing.JFrame;
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
	private JFrame gui;

	/**
	 * Instantiates a new synchronize.
	 * 
	 * @param modalGui the gui to show the dialogs on
	 */
	public Synchronize(JFrame modalGui) {

		this.enteringName = false;
		this.gui = modalGui;
		
	}

	/**
	 * Tick.
	 */
	public void tick() {
		
		
		if (!this.enteringName) {
			System.out.println("OPENING");
			this.enteringName = true;
			String nameInput = JOptionPane.showInputDialog(this.gui, "Enter Name");
			
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
