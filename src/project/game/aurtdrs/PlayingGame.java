package project.game.aurtdrs;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

import project.client.NetworkData;

/**
 * The Class PlayingGame.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class PlayingGame implements AurtdrsProcess {
	
	private static final Image ENGINE = new ImageIcon("res/engine.png").getImage();
	private static final Image TRAILER = new ImageIcon("res/trailer.png").getImage();
	private static final Image TRAILER_LEFT = new ImageIcon("res/trailer_left.png").getImage();
	private static final Image TRAILER_RIGHT = new ImageIcon("res/trailer_right.png").getImage();
	
	private int totalPlayers;
	private RoadTrain[] otherPlayers;
	private RoadTrain client;
	
	public PlayingGame() {
		
		this.otherPlayers = null;
		this.client = null;
		this.totalPlayers = 0;
		
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
		
		for(RoadTrain train : this.otherPlayers) {
			if(train == null) {
				//client(s) disconnected
				this.otherPlayers = null;
				this.client = null;
				return;
			}
		}
		
	}

	public void processState(NetworkData data) {
		if (data == null) {
			return;
		}
		
		if(this.client == null) {
			this.otherPlayers = ((RoadTrain[])data.getData()[0]);
			this.totalPlayers = this.otherPlayers.length;
			this.client = this.makeClient();
		}
		
	}
	
	private RoadTrain makeClient() {
		
		RoadTrain train = new RoadTrain();
		train.acceleration = 0;
		train.cars = new Point[] {new Point(0,0),new Point(0,1),new Point(0,2),new Point(0,3)};
		
		return train;
		
	}
	
	private static class RoadTrain {
		
		public double acceleration;
		public Point[] cars;
		
	}
	
}
