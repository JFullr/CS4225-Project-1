package project.game.hangman;

/**
 * The Class Player.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class Player {

	private int score;
	private int guesses;
	private String name;

	/**
	 * Instantiates a new player of the give name.
	 * 
	 * @precondition name != null && !name.isBlank()
	 *
	 * @param name the name of the player
	 */
	public Player(String name) {
		if (name == null) {
			throw new IllegalArgumentException("The player's name cannot be null.");
		}
		if (name.isEmpty()) {
			throw new IllegalArgumentException("The player's name cannot be blank.");
		}
		this.name = name;
		this.score = 0;
		this.guesses = 3;
	}

	/**
	 * Gets the player's current score.
	 *
	 * @return the player's current score
	 */
	public int getScore() {
		return this.score;
	}

	/**
	 * Gets the player's name.
	 *
	 * @return the name of the player
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Updates the player's score.
	 */
	public void updateScore() {
		this.score += 5;
	}

	/**
	 * Decrements the number of guesses the player has left.
	 */
	public void decrementGuesses() {
		this.guesses--;
	}

	/**
	 * Gets the number of guesses the player has left.
	 *
	 * @return the guesses
	 */
	public int getGuesses() {
		return this.guesses;
	}
}
