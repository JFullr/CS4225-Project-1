package project.game.aurtdrs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

import project.AurtdrsKey;
import project.client.NetworkData;

/**
 * The Class PlayingGame.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class PlayingGame implements AurtdrsProcess {
	
	private static final int MAX_SHIFT = 250;
	private static final int PERFECT_SHIFT = 232;
	
	private static final String URL_RESOURCE_BASE = "res/";
	private static final Image ENGINE = new ImageIcon((URL_RESOURCE_BASE+"engine.png")).getImage();
	private static final Image TRAILER = new ImageIcon(URL_RESOURCE_BASE+"trailer.png").getImage();
	private static final Image TRAILER_LEFT = new ImageIcon(URL_RESOURCE_BASE+"trailer_left.png").getImage();
	private static final Image TRAILER_RIGHT = new ImageIcon(URL_RESOURCE_BASE+"trailer_right.png").getImage();
	
	private static final Image BACKGROUND = new ImageIcon(URL_RESOURCE_BASE+"background.png").getImage();
	private static final Image CACTUS = new ImageIcon(URL_RESOURCE_BASE+"cactus.png").getImage();
	
	private AurtdrsRoadTrain[] otherPlayers;
	private AurtdrsRoadTrain client;
	
	private int animation;
	
	private int shift;
	private boolean shiftDirection;
	
	public PlayingGame() {
		
		this.otherPlayers = null;
		this.client = null;
		this.animation = 0;
		this.shift = 0;
		this.shiftDirection = true;
		
	}
	
	public void tick() {
		
		this.animation+=3;
		
		if(this.animation < 0) {
			this.animation = 0;
		}
		
		if(this.shiftDirection) {
			
			this.shift+=4;
			
			if(this.shift >= MAX_SHIFT) {
				this.shiftDirection = false;
			}
			
		}else {
			
			this.shift-=4;
			
			if(this.shift <= 0) {
				this.shiftDirection = true;
			}
			
		}
		
		/*if(this.shift == PERFECT_SHIFT) {
			this.shift = 0;
		}*/
		
		if(AurtdrsKey.SPACE) {
			AurtdrsKey.SPACE = false;
			
			this.client.incrementAcceleration( (1.0-(Math.abs(this.shift-PERFECT_SHIFT)/MAX_SHIFT))*10);
			
			this.shift = 0;
			
		}
		
		this.client.incrementValues();
		
	}
	
	/**
	 * Render.
	 *
	 * @param graphics the graphics
	 * @param frameWidth the frame width
	 * @param frameHeight the frame height
	 */
	public void render(Graphics graphics, int frameWidth, int frameHeight) {
		
		this.renderDefault(graphics, frameWidth, frameHeight);
		this.renderClient(graphics, frameWidth, frameHeight);
		
		this.renderOtherRoads(graphics, frameWidth, frameHeight);
		
		this.renderShift(graphics, frameWidth, frameHeight);
		
	}

	public void processState(NetworkData data) {
		if (data == null) {
			return;
		}
		
		if(this.client == null) {
			this.otherPlayers = ((AurtdrsRoadTrain[])data.getData()[0]);
			this.client = new AurtdrsRoadTrain();
		}
		
	}

	public void resetState() {
		this.otherPlayers = new AurtdrsRoadTrain[8];
		for(int i = 0; i < this.otherPlayers.length; i++) {
			this.otherPlayers[i] = new AurtdrsRoadTrain();
		}
		this.client = new AurtdrsRoadTrain();
		this.animation = 0;
		this.shift = 0;
		this.shiftDirection = true;
	}
	
	private void renderShift(Graphics graphics, int frameWidth, int frameHeight) {
		graphics.setColor(Color.DARK_GRAY);
		
		graphics.fillRect(frameWidth-120, 10, 110, frameHeight-20);
		
		graphics.setColor(Color.GREEN);
		graphics.fillRect(frameWidth-120, frameHeight-10 - shift*(frameHeight-40)/MAX_SHIFT, 110, shift*(frameHeight-40)/MAX_SHIFT);
		
		graphics.setColor(Color.RED);
		graphics.fillRect(frameWidth-120, 70, 70, 20);
		
	}
	
	private void renderDefault(Graphics graphics, int frameWidth, int frameHeight) {
		
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, frameWidth, frameHeight);
		graphics.drawImage(BACKGROUND, 0, 0, frameWidth, frameHeight, null);
		
		int roadX = frameWidth/2 - TRAILER_LEFT.getWidth(null)/2;
		int roadWidth = TRAILER_LEFT.getWidth(null);
		
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRect(roadX, 0, roadWidth, frameHeight);
		
	}
	
	private void renderClient(Graphics graphics, int frameWidth, int frameHeight) {
		
		int roadX = frameWidth/2 - TRAILER_LEFT.getWidth(null)/2;
		int trainX = roadX+TRAILER_LEFT.getWidth(null)/4;
		int trainYHead = frameHeight/4;
		this.renderTrain(graphics, frameWidth, frameHeight,this.client, trainX, trainYHead);
		
	}
	
	private void renderTrain(Graphics graphics, int frameWidth, int frameHeight, AurtdrsRoadTrain train, int trainX, int trainYHead) {
		
		int oscillate = this.animation & 0x1;
		int part = -1;
		for(Point car : train.getRelativePositions()) {
			if(part == -1) {
				graphics.drawImage(ENGINE, trainX, trainYHead + oscillate, null);
			}else {
				oscillate = (oscillate+1) & 0x1;
				int carYpos = trainYHead + ENGINE.getHeight(null) + TRAILER.getHeight(null)*part + oscillate;
				graphics.drawImage(TRAILER, trainX, carYpos, null);
			}
			
			part++;
			
		}
		
	}
	
	private void renderOtherRoads(Graphics graphics, int frameWidth, int frameHeight) {
		
		int left = 0;
		int right = 0;
		int size = TRAILER_LEFT.getWidth(null)*3/2;
		int side = 1;
		for(AurtdrsRoadTrain train : this.otherPlayers) {
			
			side^=1;
			left+=side^1;
			right+=side;
			
			if(train == null) {
				
				graphics.setColor(Color.RED);
				
			} else {
				graphics.setColor(Color.LIGHT_GRAY);
				
			}
			
			int roadX = frameWidth/2 - TRAILER_LEFT.getWidth(null)/2;
			roadX += side == 1 ? right*size : -left*size;
			int roadWidth = TRAILER_LEFT.getWidth(null);
			
			
			graphics.fillRect(roadX, 0, roadWidth, frameHeight);
			
			if(train != null) {
				int trainX = roadX+TRAILER_LEFT.getWidth(null)/4;
				int trainYHead = frameHeight/4;
				
				int offset = (int) (this.client.getDistance() - train.getDistance());
				trainYHead+= offset;
			
				this.renderTrain(graphics, frameWidth, frameHeight,train, trainX, trainYHead);
			}
			
		}
	}
	
}
