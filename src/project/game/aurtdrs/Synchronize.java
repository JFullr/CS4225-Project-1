package project.game.aurtdrs;

import java.awt.Graphics;

import project.client.NetworkData;
import project.client.NetworkState;

/**
 * The Class Synchronize.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class Synchronize implements AurtdrsProcess {
	
	private String userName;
	
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

	public NetworkData processState(NetworkData data) {
		if (data == null) {
			return null;
		}
		
		if(data.getState() == NetworkState.SYNCHRONIZING) {
			if(this.userName != null) {
				return new NetworkData(NetworkState.SYNCHRONIZING, this.userName);
			}
		}
		
		return null;
		
	}

	public void resetState() {
		
	}
	
}
