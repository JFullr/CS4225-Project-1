package project.game.aurtdrs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import project.client.NetworkData;

public class Disconnected implements AurtdrsProcess {
	
	private static double ONE_RADIAN = Math.toRadians(1);
	
	private double degree;
	private int pulse;
	
	public Disconnected() {
		
		this.degree = 0;
		this.pulse = 0;
		
	}
	
	public void tick() {
		
		this.pulse++;
		this.pulse = this.pulse&0xFF;
		this.degree+=ONE_RADIAN*2;
		
	}
	
	public void render(Graphics g, int frameWidth, int frameHeight) {
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, frameWidth, frameHeight);
		
		g.setColor(Color.GREEN);
		g.setFont(new Font(Font.DIALOG, Font.PLAIN,18+this.pulse/32));
		
		int disconnectX = (int)(frameWidth/5*2 - Math.cos(this.degree)*(frameWidth/4));
		g.drawString("Disconnected [F]", disconnectX, frameHeight/2);
		
		int connectX = (int)(frameWidth/5*2 + Math.cos(this.degree)*(frameWidth/4));
		int connectY = (int)(frameHeight/2 + Math.sin(this.degree)*(frameHeight/3));
		g.drawString("...Connecting...", connectX, connectY);
		
	}

	public void processState(NetworkData data) {
		if (data == null) {
			return;
		}
		
	}
	
	public void resetState() {
		
	}
	
}