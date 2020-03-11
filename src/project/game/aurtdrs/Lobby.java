package project.game.aurtdrs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import project.client.NetworkData;

public class Lobby implements AurtdrsProcess {
	
	public static final Font DISPLAY = new Font("TimesRoman", Font.PLAIN, 32);
	
	private int lobbyCount;
	
	public Lobby() {
		
		this.lobbyCount = 0;
		
	}
	

	public void tick() {
		
	}
	
	public void render(Graphics g, int frameWidth, int frameHeight) {
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, frameWidth, frameHeight);
		
		g.setColor(Color.GREEN);
		g.setFont(DISPLAY);
		g.drawString("Count: "+this.lobbyCount, 5, 30);
		
		
	}

	public int getLobbyCount() {
		return lobbyCount;
	}

	public void setLobbyCount(int lobbyCount) {
		this.lobbyCount = lobbyCount;
	}
	
	public void processState(NetworkData data) {
		if (data == null) {
			return;
		}
		
	}

	
}
