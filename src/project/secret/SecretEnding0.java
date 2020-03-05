package project.secret;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;

public class SecretEnding0 {
	
	public static void render(Graphics g, int frameWidth, int frameHeight) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, frameWidth, frameHeight);
		g.setColor(Color.GREEN);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
		g.drawString("Hello World", 30, 30);
		g.drawString("this is what happens when you burn the \"Land down under\"", 30, 80);

		ImageIcon halo = new ImageIcon("BS-883200.jpg");
		g.drawImage(halo.getImage(), (frameWidth / 2) - 225, 100, 450, 450, null);
	}
	
}
