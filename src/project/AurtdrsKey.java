package project;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AurtdrsKey implements KeyListener {
	
	public static volatile boolean SPACE;
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			SPACE = true;
		}
		e.consume();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			SPACE = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			SPACE = true;
		}
	}

}
