package project.game.aurtdrs;

import java.awt.Graphics;
import java.util.HashMap;

import audio.VorbisPlayer;
import project.client.NetworkData;
import project.client.NetworkGameState;

public class AurtdrsEngine implements AurtdrsProcess {

	private Lobby lobby;
	private PlayingGame game;
	private Synchronize synch;
	private GameOver gameOver;
	private Disconnected disconnected;

	private AurtdrsProcess currentProcess;
	private NetworkGameState currentState;
	private HashMap<AurtdrsProcess, String> musicMap;
	private HashMap<AurtdrsProcess, Float> musicVolumeMap;
	private HashMap<NetworkGameState, AurtdrsProcess> processMap;

	private VorbisPlayer musicPlayer;

	public AurtdrsEngine() {
		this.lobby = new Lobby();
		this.game = new PlayingGame();
		this.synch = new Synchronize();
		this.gameOver = new GameOver();
		this.disconnected = new Disconnected();

		this.musicMap = new HashMap<AurtdrsProcess, String>();
		this.musicMap.put(this.lobby, "res/lobby (2).ogg");
		this.musicMap.put(this.game, null);
		this.musicMap.put(this.synch, null);
		this.musicMap.put(this.gameOver, null);
		this.musicMap.put(this.disconnected, "res/DISCONNECT A(tm).ogg");
		
		
		this.musicVolumeMap = new HashMap<AurtdrsProcess, Float>();
		this.musicVolumeMap.put(this.lobby, .5f);
		this.musicVolumeMap.put(this.game, null);
		this.musicVolumeMap.put(this.synch, null);
		this.musicVolumeMap.put(this.gameOver, null);
		this.musicVolumeMap.put(this.disconnected, .5f);

		this.currentProcess = null;

		this.processMap = new HashMap<NetworkGameState, AurtdrsProcess>();
		this.processMap.put(NetworkGameState.LOBBY, this.lobby);
		this.processMap.put(NetworkGameState.IN_GAME, this.game);
		this.processMap.put(NetworkGameState.SYNCHRONIZING, this.synch);
		this.processMap.put(NetworkGameState.MATCH_OVER, this.gameOver);
		this.processMap.put(NetworkGameState.DISCONNECTED, this.disconnected);

	}

	public void setState(NetworkGameState state) {
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
			//e.printStackTrace();
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
			this.setState(data.getState());
			this.currentProcess.processState(data);
		}
	}

}
