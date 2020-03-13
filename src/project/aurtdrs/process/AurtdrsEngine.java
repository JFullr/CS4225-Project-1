package project.aurtdrs.process;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashMap;

import javax.swing.JFrame;

import audio.VorbisPlayer;
import project.game.network.NetworkData;
import project.game.network.NetworkState;

/**
 * The Class AurtdrsEngine.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class AurtdrsEngine implements AurtdrsProcess {

	private Lobby lobby;
	private PlayingGame game;
	private Synchronize synch;
	private GameOver gameOver;
	private Disconnected disconnected;
	private AurtdrsProcess currentProcess;
	private NetworkState currentState;

	private HashMap<AurtdrsProcess, String> musicMap;
	private HashMap<AurtdrsProcess, Float> musicVolumeMap;
	private HashMap<NetworkState, AurtdrsProcess> processMap;

	private NetworkData networkData;
	private VorbisPlayer musicPlayer;

	private String notification;

	/**
	 * Instantiates a new aurtdrs engine.
	 * 
	 * @param display
	 *            the window this game is displayed on
	 */
	public AurtdrsEngine(JFrame display) {
		this.lobby = new Lobby();
		this.game = new PlayingGame();
		this.synch = new Synchronize(display);
		this.gameOver = new GameOver();
		this.disconnected = new Disconnected();

		this.musicMap = new HashMap<AurtdrsProcess, String>();
		this.musicMap.put(this.lobby, "res/lobby (2).ogg");
		this.musicMap.put(this.game, "res/gahmMusik (3).ogg");
		this.musicMap.put(this.synch, null);
		this.musicMap.put(this.gameOver, null);
		this.musicMap.put(this.disconnected, "res/DISCONNECT A(tm).ogg");

		this.musicVolumeMap = new HashMap<AurtdrsProcess, Float>();
		this.musicVolumeMap.put(this.lobby, .5f);
		this.musicVolumeMap.put(this.game, .65f);
		this.musicVolumeMap.put(this.synch, null);
		this.musicVolumeMap.put(this.gameOver, null);
		this.musicVolumeMap.put(this.disconnected, .5f);

		this.processMap = new HashMap<NetworkState, AurtdrsProcess>();
		this.processMap.put(NetworkState.LOBBY, this.lobby);
		this.processMap.put(NetworkState.IN_GAME, this.game);
		this.processMap.put(NetworkState.SYNCHRONIZING, this.synch);
		this.processMap.put(NetworkState.MATCH_OVER, this.gameOver);
		this.processMap.put(NetworkState.DISCONNECTED, this.disconnected);

		this.currentProcess = null;
		this.networkData = null;
		this.notification = null;
		this.setState(NetworkState.DISCONNECTED);

	}

	/**
	 * Sets the state.
	 *
	 * @param state
	 *            the new state
	 */
	public void setState(NetworkState state) {

		if (state == this.currentState) {
			return;
		}
		this.currentState = state;

		if (state == null) {
			this.currentProcess = this.lobby;
		} else {
			this.currentProcess = this.processMap.get(state);
		}
		
		this.currentProcess.resetState();

		if (this.musicPlayer != null) {
			this.musicPlayer.end();
		}

		this.musicPlayer = new VorbisPlayer(this.musicMap.get(this.currentProcess));
		try {
			this.musicPlayer.play();
			this.musicPlayer.setVolume(this.musicVolumeMap.get(this.currentProcess));
		} catch (Exception e) {
		}

	}

	/**
	 * Tick.
	 */
	public void tick() {

		if (this.currentProcess != null) {
			this.currentProcess.tick();
		}

	}

	/**
	 * Render.
	 *
	 * @param graphics
	 *            the graphics
	 * @param frameWidth
	 *            the frame width
	 * @param frameHeight
	 *            the frame height
	 */
	public void render(Graphics graphics, int frameWidth, int frameHeight) {

		if (this.currentProcess != null) {
			this.currentProcess.render(graphics, frameWidth, frameHeight);
		}

		this.renderNotification(graphics, frameWidth, frameHeight);

	}

	/**
	 * Processes the current game state.
	 *
	 * @param data
	 *            the data
	 * @return the network data
	 */
	public NetworkData processState(NetworkData data) {

		if (data == null) {
			return null;
		}

		if (data.getState() == NetworkState.PLAYER_QUIT) {

			this.notification = (String) data.getData()[0];

		} else {

			this.setState(data.getState());
			return this.currentProcess.processState(data);

		}

		return null;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public NetworkData getData() {
		return this.networkData;
	}

	/**
	 * Reset state.
	 */
	public void resetState() {
	}

	private void renderNotification(Graphics graphics, int frameWidth, int frameHeight) {

		graphics.setColor(Color.GREEN);

		graphics.setFont(new Font("Dialog", Font.PLAIN, 16));
		graphics.drawString("NOTIFICATIONS", 5, frameHeight - 150);
		graphics.setFont(new Font("Dialog", Font.PLAIN, 14));
		if (this.notification != null) {
			graphics.drawString(this.notification, 5, frameHeight - 135);
		}

	}

}
