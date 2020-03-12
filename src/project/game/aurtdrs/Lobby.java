package project.game.aurtdrs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import project.client.NetworkData;
import project.client.NetworkState;

/**
 * The Class Lobby.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class Lobby implements AurtdrsProcess {
	
	public static final Font DISPLAY = new Font("TimesRoman", Font.PLAIN, 32);
	
	private int lobbyCount;

	/**
	 * Instantiates a new lobby.
	 */
	public Lobby() {
		
		this.lobbyCount = 0;
		
	}

	/**
	 * Tick.
	 */
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

		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, frameWidth, frameHeight);

		graphics.setColor(Color.GREEN);
		graphics.setFont(DISPLAY);
		graphics.drawString("Count: " + this.lobbyCount, 5, 30);

	}

	/**
	 * Gets the lobby count.
	 *
	 * @return the lobby count
	 */
	public int getLobbyCount() {
		return this.lobbyCount;
	}

	/**
	 * Sets the lobby count.
	 *
	 * @param lobbyCount the new lobby count
	 */
	public void setLobbyCount(int lobbyCount) {
		this.lobbyCount = lobbyCount;
	}
	
	public void processState(NetworkData data) {
		if (data == null) {
			return;
		}
		
		if(data.getState() != NetworkState.LOBBY) {
			return;
		}
		
		this.lobbyCount = (Integer)data.getData()[0];
		
	}

}
