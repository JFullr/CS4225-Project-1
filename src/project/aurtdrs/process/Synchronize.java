package project.aurtdrs.process;

import java.awt.Color;
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
	private JFrame gui;

	/**
	 * Instantiates a new synchronize.
	 * 
	 * @param modalGui
	 *            the gui to show the dialogs on
	 */
	public Synchronize(JFrame modalGui) {

		this.gui = modalGui;

	}

	/**
	 * Tick.
	 */
	public void tick() {

	}

	/**
	 * Render.
	 *
	 * @param graphics
	 *            the graphics
	 * @param frameWidth
	 *            the frame width
	 * @param frameHeight
	 *            the frame height
	 */
	public void render(Graphics graphics, int frameWidth, int frameHeight) {

		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, frameWidth, frameHeight);

	}
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * Process state.
	 *
	 * @param data
	 *            the data
	 * @return the network data
	 */
	public NetworkData processState(NetworkData data) {
		if (data == null) {
			return null;
		}

		if (data.getState() == NetworkState.SYNCHRONIZING) {
			String title = "Enter User Name";
			if (data.getData() != null && data.getData().length > 0) {
				title = (String) data.getData()[0];
			}
			do {
				this.userName = null;
				String nameInput = JOptionPane.showInputDialog(this.gui, "Enter Name", title);
				this.userName = nameInput;
			} while (this.userName == null || this.userName.isEmpty());
				
			return new NetworkData(NetworkState.SYNCHRONIZING, this.userName);
		}

		return null;

	}

	/**
	 * Reset state.
	 */
	public void resetState() {
	}

}
