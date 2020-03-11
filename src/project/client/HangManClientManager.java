package project.client;

import project.game.hangman.HangMan;
import project.game.hangman.Player;

/**
 * The Class HangManManagerClient.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class HangManClientManager {

	private static final String BLANK_CLIENT_NAME = "the client name cannot be blank.";
	private static final String NULL_CLIENT_NAME = "the client name cannot be null.";
	private static final String MISSING_GAME_DATA = "some of the required game data is missing.";
	private static final String NULL_GUESS = "the guess cannot be null.";
	private static final String BLANK_GUESS = "the guess cannot be blank.";

	private HangMan hangMan;
	private Player currentTurn;
	private String clientName;

	/**
	 * Instantiates a new hang man manager client.
	 * 
	 * @precondition clientName != null && !clientName.Blank()
	 * @postcondition this.clientName == clientName
	 *
	 * @param clientName the client name
	 */
	public HangManClientManager(String clientName) {
		if (clientName == null) {
			throw new IllegalArgumentException(NULL_CLIENT_NAME);
		}
		if (clientName.isBlank()) {
			throw new IllegalArgumentException(BLANK_CLIENT_NAME);
		}
		
		this.currentTurn = null;
		this.hangMan = null;
		this.clientName = clientName;
	}

	/**
	 * Submits a guess to the hang man server.
	 *
	 * @param guess the guess
	 */
	public void submitGuess(String guess) {
		if (guess == null) {
			throw new IllegalArgumentException(NULL_GUESS);
		}
		if (guess.isBlank()) {
			throw new IllegalArgumentException(BLANK_GUESS);
		}

		if (this.currentTurn.getName().equals(this.clientName)) {
			if (!this.hangMan.guessIsValid(guess)) {
				this.currentTurn.decrementGuesses();
			} else {
				this.currentTurn.updateScore();
			}
		}
	}

	/**
	 * Gets the game data.
	 *
	 * @return the game data
	 */
	public Object[] getGameData() {
		Object[] gameData = { this.currentTurn, this.hangMan };
		return gameData;
	}

	/**
	 * Sets the game data.
	 * 
	 * @precondition gameData != null && gameData.length == 2
	 * @postcondition this.currentTurn = gameData[0] && this.hangMan = gameData[1]
	 *
	 * @param gameData the new game data
	 */
	public void setGameData(Object[] gameData) {
		if (gameData.length != 2) {
			throw new IllegalArgumentException(MISSING_GAME_DATA);
		}

		this.currentTurn = (Player) gameData[0];
		this.hangMan = (HangMan) gameData[1];
	}
}
