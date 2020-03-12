package project.game.aurtdrs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import project.client.NetworkData;

/**
 * The Class Disconnected.
 *
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class Disconnected implements AurtdrsProcess {

	private static double oneRadian = Math.toRadians(1);

	private double degree;
	private int pulse;

	/**
	 * Instantiates a new disconnected.
	 */
	public Disconnected() {

		this.degree = 0;
		this.pulse = 0;

	}

	/**
	 * Tick.
	 */
	public void tick() {

		this.pulse++;
		this.pulse = this.pulse & 0xFF;
		this.degree += oneRadian * 2;

	}

	/**
	 * Render.
	 *
	 * @param graphics the graphics
	 * @param frameWidth the frame width
	 * @param frameHeight the frame height
	 */
	public void render(Graphics graphics, int frameWidth, int frameHeight) {

		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, frameWidth, frameHeight);

		graphics.setColor(Color.GREEN);
		graphics.setFont(new Font(Font.DIALOG, Font.PLAIN, 18 + this.pulse / 32));

		int disconnectX = (int) (frameWidth / 5 * 2 - Math.cos(this.degree) * (frameWidth / 4));
		graphics.drawString("Disconnected [F]", disconnectX, frameHeight / 2);

		int connectX = (int) (frameWidth / 5 * 2 + Math.cos(this.degree) * (frameWidth / 4));
		int connectY = (int) (frameHeight / 2 + Math.sin(this.degree) * (frameHeight / 3));
		graphics.drawString("...Connecting...", connectX, connectY);

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