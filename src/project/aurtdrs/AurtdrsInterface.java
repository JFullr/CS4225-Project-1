package project.aurtdrs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;

import project.aurtdrs.process.AurtdrsEngine;
import project.aurtdrs.process.Lobby;
import project.game.network.NetworkData;
import project.game.network.NetworkState;
import project.game.network.client.GameClientManager;

/**
 * The Class Gui.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class AurtdrsInterface {

	private static final String WINDOW_TITLE = "Ultimate Australian Road Train Drag Racing Simulator";
	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 600;

	private int width;
	private int height;

	private JFrame window;
	private volatile boolean running;
	private Image imageBuffer;

	private AurtdrsEngine game;
	private GameClientManager client;

	/**
	 * Instantiates a new interface.
	 *
	 */
	public AurtdrsInterface() {

		this.width = WINDOW_WIDTH;
		this.height = WINDOW_HEIGHT;

		this.client = new GameClientManager();
		this.game = new AurtdrsEngine(this.window);

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
		this.window.setLocationRelativeTo(null);
		this.window.setVisible(true);
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.addKeyListener();

		new Thread(() -> {
			this.graphics();
		}).start();

		this.client.start();
	}

	/**
	 * Terminates the GUI
	 */
	public void end() {
		this.client.end();
		this.running = false;
		this.window.dispose();
	}

	private void graphics() {

		this.running = true;
		this.imageBuffer = this.window.createVolatileImage(this.width, this.height);
		if (this.imageBuffer == null) {
			return;
		}

		this.processLoop();

	}

	private void processLoop() {

		while (this.running) {
			try {
				Graphics graphics = this.imageBuffer.getGraphics();

				this.handleQuit();
				this.handleNetworkData();
				this.drawToBuffer(graphics);

				graphics = this.window.getContentPane().getGraphics();
				graphics.drawImage(this.imageBuffer, 0, 0, null);
				graphics.dispose();

				Thread.sleep(20);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void handleNetworkData() {

		if (this.client != null) {
			NetworkData data = this.client.getData();
			while (data != null) {
				NetworkData propagate = this.game.processState(data);
				data = this.client.getData();
				if (propagate != null) {
					this.client.sendData(propagate);
				}
			}
			if (!this.client.isConnected()) {
				this.game.setState(NetworkState.DISCONNECTED);
			}
		}

	}

	private void drawToBuffer(Graphics graphics) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, this.width, this.height);

		this.game.tick();
		this.game.render(graphics, this.width, this.height);
		this.drawESCQuit(graphics);
	}

	private void drawESCQuit(Graphics graphics) {
		graphics.setColor(Color.GREEN);
		graphics.setFont(Lobby.DISPLAY);
		graphics.drawString("ESC = Quit", 5, 30);
	}

	private void handleQuit() {
		if (AurtdrsKey.isQuit()) {
			this.client.sendData(new NetworkData(NetworkState.PLAYER_QUIT));
			System.exit(0);
		}
	}

	private void addKeyListener() {
		this.window.addKeyListener(new AurtdrsKey());
	}

}
