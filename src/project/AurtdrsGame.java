package project;

import project.client.GameClientManager;

public class AurtdrsGame {
	
	private Gui gui;
	private GameClientManager network;
	
	public AurtdrsGame() {
		this.gui = new Gui();
		this.network = new GameClientManager();
	}
	
	public void start() {
		this.gui.start();
		this.network.start();
	}
	
	public void end() {
		this.gui.end();
		this.network.end();
	}

}
