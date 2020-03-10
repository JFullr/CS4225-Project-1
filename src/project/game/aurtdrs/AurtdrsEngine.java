package project.game.aurtdrs;

import java.awt.Graphics;

import project.client.NetworkGameState;

public class AurtdrsEngine implements AurtdrsProcess {
	
	private Lobby lobby;
	private PlayingGame game;
	private Synchronize synch;
	private GameOver gameOver;
	
	private AurtdrsProcess currentState;
	private NetworkGameState state;
	
	public AurtdrsEngine() {
		this.lobby = new Lobby();
		this.game = new PlayingGame();
		this.synch = new Synchronize();
		this.gameOver = new GameOver();
		
		this.currentState = null;
		
	}
	
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
		}
	}
	
	public void tick() {
		
		///TODO heartbeat
		
		if(this.currentState != null) {
			this.currentState.tick();
		}
		
	}
	
	public void render(Graphics g, int frameWidth, int frameHeight) {
		
		if(this.currentState != null) {
			this.currentState.render(g, frameWidth, frameHeight);
		}
		
	}

}
