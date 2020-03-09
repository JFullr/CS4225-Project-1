package project.secret;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;

/**
 * The Class SecretEnding0 is a secret ending possible to do in the game
 * "Ultimate Australian Road Train Drag Racing Simulator".
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020.
 */
public class SecretEnding0 {

	/**
	 * Renders the secret ending of the game "Ultimate Australian Road Train Drag
	 * Racing Simulator".
	 *
	 * @param graphics    the graphics
	 * @param frameWidth  the frame width
	 * @param frameHeight the frame height
	 */
	public static void render(Graphics graphics, int frameWidth, int frameHeight) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, frameWidth, frameHeight);
		graphics.setColor(Color.GREEN);

		graphics.setFont(new Font("TimesRoman", Font.PLAIN, 32));
		graphics.drawString("Hello World", 30, 30);
		graphics.drawString("this is what happens when you burn the \"Land down under\"", 30, 80);

		ImageIcon halo = new ImageIcon("BS-883200.jpg");
		graphics.drawImage(halo.getImage(), (frameWidth / 2) - 225, 100, 450, 450, null);
	}

}
