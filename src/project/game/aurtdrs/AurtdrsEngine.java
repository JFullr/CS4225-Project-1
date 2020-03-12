package project.game.aurtdrs;

import java.awt.Graphics;
import java.util.HashMap;

import audio.VorbisPlayer;
import project.client.NetworkData;
import project.client.NetworkState;

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
	/**
	 * Instantiates a new aurtdrs engine.
	 */
	public AurtdrsEngine() {
		this.lobby = new Lobby();
		this.game = new PlayingGame();
		this.synch = new Synchronize();
		this.gameOver = new GameOver();
		this.disconnected = new Disconnected();

		this.musicMap = new HashMap<AurtdrsProcess, String>();
		this.musicMap.put(this.lobby, "res/lobby (2).ogg");
		this.musicMap.put(this.game, "gahmMusik (3).ogg");
		this.musicMap.put(this.synch, null);
		this.musicMap.put(this.gameOver, null);
		this.musicMap.put(this.disconnected, "res/DISCONNECT A(tm).ogg");
		
		
		this.musicVolumeMap = new HashMap<AurtdrsProcess, Float>();
		this.musicVolumeMap.put(this.lobby, .5f);
		this.musicVolumeMap.put(this.game, .7f);
		this.musicVolumeMap.put(this.synch, null);
		this.musicVolumeMap.put(this.gameOver, null);
		this.musicVolumeMap.put(this.disconnected, .5f);

		this.currentProcess = null;

		this.processMap = new HashMap<NetworkState, AurtdrsProcess>();
		this.processMap.put(NetworkState.LOBBY, this.lobby);
		this.processMap.put(NetworkState.IN_GAME, this.game);
		this.processMap.put(NetworkState.SYNCHRONIZING, this.synch);
		this.processMap.put(NetworkState.MATCH_OVER, this.gameOver);
		this.processMap.put(NetworkState.DISCONNECTED, this.disconnected);
		
		this.networkData = null;

	}
	
	/**
	 * Sets the state.
	 *
	 * @param state the new state
	 */
	public void setState(NetworkState state) {
		if (state == this.currentState) {
			return;
		}
		
		if (state == null) {
			this.currentProcess = this.lobby;
		} else {
			this.currentProcess = this.processMap.get(state);
		}

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

	public void tick() {

		if (this.currentProcess != null) {
			this.currentProcess.tick();
		}
		
		

	}

	public void render(Graphics g, int frameWidth, int frameHeight) {

		if (this.currentProcess != null) {
			this.currentProcess.render(g, frameWidth, frameHeight);
		}

	}

	public void processState(NetworkData data) {
		if (data != null) {
			if(data.getState() == NetworkState.HEART_BEAT) {
				
				return;
			}
			this.setState(data.getState());
			this.currentProcess.processState(data);
		}
	}
	
	public NetworkData getData() {
		return this.networkData;
	}

}
