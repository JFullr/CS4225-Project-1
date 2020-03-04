package project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * The Class Main.
 *
 * @author csuser
 */
public class Main {

	private static int width = 800;
	private static int height = 600;
	
	private static JFrame j;
	private static boolean running;
	private static Image i;

	/**
	 * The main method begins a new game.
	 *
	 * @param args not used
	 */
	public static void main(String[] args) {

		j = new JFrame("Ultimate Aussue Road Snake Simulator");
		j.getContentPane().setPreferredSize(new Dimension(width, height));
		j.pack();
		j.setResizable(false);
		j.pack();
		j.setVisible(true);
		j.setDefaultCloseOperation(3);

		new Thread(() -> {
			graphics();
		}).start();

	}

	private static void graphics() {

		running = true;
		i = j.createVolatileImage(width, height);
		if (i == null) {
			return;
		}

		while (running) {
			try {

				Graphics g = i.getGraphics();
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, width, height);
				g.setColor(Color.GREEN);

				g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
				g.drawString("Hello World", 30, 30);
				g.drawString("this is what happens when you burn the \"Land down under\"", 30, 80);

				ImageIcon halo = new ImageIcon("BS-883200.jpg");
				g.drawImage(halo.getImage(), (width / 2) - 225, 100, 450, 450, null);
				
				g = j.getContentPane().getGraphics();
				g.drawImage(i, 0, 0, null);
				g.dispose();

				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
