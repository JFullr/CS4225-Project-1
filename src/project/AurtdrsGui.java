package project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;

import project.client.GameClientManager;
import project.client.NetworkData;
import project.client.NetworkState;
import project.game.aurtdrs.AurtdrsEngine;
import project.game.aurtdrs.Lobby;

/**
 * The Class Gui.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class AurtdrsGui {

	private static final String WINDOW_TITLE = "Ultimate Australian Road Train Drag Racing Simulator";

	private int width;
	private int height;

	private JFrame window;
	private volatile boolean running;
	private Image imageBuffer;
	
	private AurtdrsEngine game;
	private GameClientManager network;

	/**
	 * Instantiates a new gui.
	 * @param network 
	 */
	public AurtdrsGui(AurtdrsEngine game, GameClientManager network) {

		this.width = 800;
		this.height = 600;
		
		this.game = game;
		this.network = network;

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
		
		this.addKeyListener();
		
		if (network.isConnected()) {
			this.drawButtons();
			this.drawUserNameTextBox();
		}

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
		
		/*
		this.game.setState(NetworkState.DISCONNECTED);
		/*/
		
		//TODO game test
		this.game.setState(NetworkState.IN_GAME);
		
		//*/

		while (this.running) {
			try {

				Graphics graphics = this.imageBuffer.getGraphics();
				graphics.setColor(Color.WHITE);
				graphics.fillRect(0, 0, this.width, this.height);

				/*
				if(this.network != null) {
					NetworkData data = this.network.getData();
					while(data != null) {
						this.game.processState(data);
						data = this.network.getData();
					}
				}
				//*/
				this.game.tick();
				this.game.render(graphics, this.width, this.height);
				
				graphics.setColor(Color.GREEN);
				graphics.setFont(Lobby.DISPLAY);
				graphics.drawString("ESC = Quit", 5, 30);

				graphics = this.window.getContentPane().getGraphics();
				graphics.drawImage(this.imageBuffer, 0, 0, null);
//				this.drawButtons();
//				this.drawUserNameTextBox();
				
				
				graphics.dispose();

				Thread.sleep(20);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	private void drawUserNameTextBox() {
		this.window.add(new JTextField());
	}

	private void drawButtons() {
		JButton quitButton = new JButton("Quit");
		this.window.add(quitButton);
		
	}

	private void addKeyListener() {
		this.window.addKeyListener(new AurtdrsKey());
	}

}
