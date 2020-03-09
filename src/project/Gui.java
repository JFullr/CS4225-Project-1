package project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;

import project.client.NetworkGameState;
import project.game.aurtdrs.AurtdrsEngine;

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
	
	private AurtdrsEngine game;

	/**
	 * Instantiates a new gui.
	 */
	public Gui() {

		this.width = 800;
		this.height = 600;
		
		this.game = new AurtdrsEngine();

	}

	/**
	 * Entrypoint of the GUI
	 */
	public void start() {
		this.window = new JFrame(WINDOW_TITLE);
		this.window.getContentPane().setPreferredSize(new Dimension(this.width, this.height));
		this.window.pack();
		this.window.setResizable(false);
		this.window.pack();
		this.window.setVisible(true);
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		new Thread(() -> {
			this.graphics();
		}).start();
	}

	/**
	 * Terminates the GUI
	 */
	public void end() {
		this.running = false;
		this.window.dispose();
	}

	private void graphics() {

		this.running = true;
		this.imageBuffer = this.window.createVolatileImage(this.width, this.height);
		if (this.imageBuffer == null) {
			return;
		}
		
		this.game.setState(NetworkGameState.LOBBY);

		while (this.running) {
			try {

				Graphics g = this.imageBuffer.getGraphics();
				g.setColor(Color.WHITE);

				this.game.render(g, this.width, this.height);
				//SecretEnding0.render(e, this.width, this.height);

				g = this.window.getContentPane().getGraphics();
				g.drawImage(this.imageBuffer, 0, 0, null);
				g.dispose();

				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
