package project.game.aurtdrs;

import java.awt.Graphics;

public interface AurtdrsProcess {
	
	void tick();
	
	void render(Graphics g, int frameWidth, int frameHeight);
	
}
