package project.game.aurtdrs;

import java.awt.Graphics;

import project.client.NetworkGameState;

/**
 * The Class AurtdrsEngine.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class AurtdrsEngine implements AurtdrsProcess {
	
	private Lobby lobby;
	private PlayingGame game;
	private Synchronize synch;
	private GameOver gameOver;
	private AurtdrsProcess currentState;
	private NetworkGameState state;
	
	/**
	 * Instantiates a new aurtdrs engine.
	 */
	public AurtdrsEngine() {
		this.lobby = new Lobby();
		this.game = new PlayingGame();
		this.synch = new Synchronize();
		this.gameOver = new GameOver();
		
		this.currentState = null;
		
	}
	
	/**
	 * Sets the state.
	 *
	 * @param state the new state
	 */
	public void setState(NetworkGameState state) {
		switch(state) {
			case HEART_BEAT:
				
				break;
			case DISCONNECTED:
				
				break;
			
			case LOBBY:
				this.currentState = this.lobby;
				break;
			case SYNCHRONIZING:
				this.currentState = this.synch;
				break;
			case IN_GAME:
				this.currentState = this.game;
				break;
			case MATCH_OVER:
				this.currentState = this.gameOver;
				break;
			default:
				break;
		}
	}
	
	/**
	 * Tick.
	 */
	public void tick() {
		
		///TODO heartbeat
		
		if (this.currentState != null) {
			this.currentState.tick();
		}
		
	}
	
	/**
	 * Render.
	 *
	 * @param graphics the grapics
	 * @param frameWidth the frame width
	 * @param frameHeight the frame height
	 */
	public void render(Graphics graphics, int frameWidth, int frameHeight) {
		
		if (this.currentState != null) {
			this.currentState.render(graphics, frameWidth, frameHeight);
		}
		
	}

}
