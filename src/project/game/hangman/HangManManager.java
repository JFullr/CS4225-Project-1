package project.game.hangman;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import utils.FileUtils;

/**
 * The Class HangManManager.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020.
 */
public class HangManManager {
	private static final int MAX_PLAYER_COUNT = 4;

	private Queue<Player> players;
	private Player currentTurn;
	private String[] wordLibrary;
	private HangMan hangMan;

	/**
	 * Instantiates a new hang man game manager.
	 *
	 * @param players the players
	 */
	public HangManManager(Player... players) {
		this.hangMan = new HangMan(this.getGameWord());
		try {
			this.players = new LinkedList<Player>();
			if (players.length > MAX_PLAYER_COUNT) {
				this.maxOutPlayers(players);
			} else {
				this.players.addAll(Arrays.asList(players));
			}
			this.wordLibrary = FileUtils.readLines("dictionary.txt");
			this.currentTurn = this.players.remove();
		} catch (IOException e) {
			System.err.println("Error: caused by: " + e.getMessage());
		}
	}

	/**
	 * Submits a guess to the hang man game and updates the current player's game
	 * stats.
	 *
	 * @param guess the guess
	 */
	public void submitGuess(String guess) {
		if (!this.hangMan.guessIsValid(guess)) {
			this.currentTurn.decrementGuesses();
		} else {
			this.currentTurn.updateScore();
		}

		if (!this.guessesExeeded()) {
			this.players.add(currentTurn);
		}

		this.currentTurn = this.players.remove();
	}

	private boolean guessesExeeded() {
		return this.currentTurn.getGuesses() > 0;
	}

	private String getGameWord() {
		Random rand = new Random();

		int wordIndex = rand.nextInt(this.wordLibrary.length);

		return this.wordLibrary[wordIndex];
	}

	private void maxOutPlayers(Player... players) {
		for (int i = 0; i < MAX_PLAYER_COUNT; i++) {
			this.players.add(players[i]);
		}
	}
}
