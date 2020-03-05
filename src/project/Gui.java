package project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;

import project.secret.SecretEnding0;

/**
 * The Class Gui.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class Gui {
	
	private static final String WINDOW_TITLE = "Ultimate Australian Road Train Drag Racing Simulator";
	
	private int width;
	private int height;
	
	private JFrame window;
	private volatile boolean running;
	private Image imageBuffer;

	/**
	 * Instantiates a new gui.
	 */
	public Gui() {
		
		this.width = 800;
		this.height = 600;
		
	}
	
	public void start() {
		this.window = new JFrame(WINDOW_TITLE);
		this.window.getContentPane().setPreferredSize(new Dimension(width, height));
		this.window.pack();
		this.window.setResizable(false);
		this.window.pack();
		this.window.setVisible(true);
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		new Thread(() -> {
			this.graphics();
		}).start();
	}
	
	public void end() {
		this.running = false;
		this.window.dispose();
	}
	
	private void graphics() {

		running = true;
		imageBuffer = window.createVolatileImage(width, height);
		if (imageBuffer == null) {
			return;
		}

		while (running) {
			try {

				Graphics g = imageBuffer.getGraphics();
				g.setColor(Color.WHITE);
				
				SecretEnding0.render(g, width, height);
				
				g = window.getContentPane().getGraphics();
				g.drawImage(imageBuffer, 0, 0, null);
				g.dispose();

				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
