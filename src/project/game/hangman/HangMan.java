package project.game.hangman;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import utils.FileUtils;

/**
 * The Class HangMan.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class HangMan {

	private static final int MAX_PLAYER_COUNT = 4;
	private static final int BODY_PART_LIMIT = 6;
	
	private String[] wordLibrary;
	private String[] dashRep;
	private String[] gameData;
	
	private Queue<Player> players;
	private Player currentTurn;
	private int bodyParts;

	/**
	 * Instantiates a new game of hang man.
	 *
	 * @param players the players
	 */
	public HangMan(Player... players) {
		try {
			this.players = new LinkedList<Player>();
			if (players.length > MAX_PLAYER_COUNT) {
				this.maxOutPlayers(players);
			} else {
				this.players.addAll(Arrays.asList(players));
			}
			this.wordLibrary = FileUtils.readLines("dictionary.txt");
			this.currentTurn = this.players.remove();
			this.bodyParts = 0;
		} catch (IOException e) {
			System.err.println("Error: caused by: " + e.getMessage());
		}
	}

	private void maxOutPlayers(Player... players) {
		for (int i = 0; i < MAX_PLAYER_COUNT; i++) {
			this.players.add(players[i]);
		}
	}
}
