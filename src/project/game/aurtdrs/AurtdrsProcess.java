package project.game.aurtdrs;

import java.awt.Graphics;

import project.client.NetworkData;

public interface AurtdrsProcess {
	
	void processState(NetworkData data);
	
	void tick();
	
	void render(Graphics g, int frameWidth, int frameHeight);
	
}
