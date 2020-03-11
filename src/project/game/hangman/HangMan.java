package project.game.hangman;

import java.util.ArrayList;

/**
 * The Class HangMan.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class HangMan {

	private static final String NULL_GAME_WORD = "the game word cannot be null.";
	private static final String BLANK_GAME_WORD = "the game word cannot be blank.";
	private static final int BODY_PART_LIMIT = 4;

	private String dashRep;
	private String word;
	private int bodyParts;
	private ArrayList<String> guessedChars;

	/**
	 * Instantiates a new game of hang man.
	 *
	 * @param gameWord the game word
	 */
	public HangMan(String gameWord) {
		if (gameWord == null) {
			throw new IllegalArgumentException(NULL_GAME_WORD);
		}
		if (gameWord.isBlank()) {
			throw new IllegalArgumentException(BLANK_GAME_WORD);
		}
		this.word = gameWord;
		this.bodyParts = 0;
		this.dashRep = this.word.replaceAll(".", "_");
		this.guessedChars = new ArrayList<String>();
	}

	/**
	 * Checks if the guess is valid.
	 *
	 * @param guess the guess
	 * @return true, iff valid
	 */
	public boolean guessIsValid(String guess) {
		boolean isValid = false;
		if (this.word.contains(guess) && guess.length() == 1 && !this.guessedChars.contains(guess)) {
			this.guessedChars.add(guess);
			this.updateDashes(guess);
			isValid = true;
		} else if (guess.equalsIgnoreCase(this.word)) {
			this.dashRep = this.word;
		} else {
			this.bodyParts++;
		}
		return isValid;
	}

	private void updateDashes(String guess) {
		String[] dashArray = this.dashRep.split("");
		for (int i = 0; i < this.word.length(); i++) {
			if (this.word.substring(i, i + 1).equalsIgnoreCase(guess)) {
				dashArray[i] = guess;
			}
		}
		this.dashRep = String.join("", dashArray);

	}

	/**
	 * Gets the body parts.
	 *
	 * @return the body parts
	 */
	public int getBodyParts() {
		return this.bodyParts;
	}

	/**
	 * Gets the dash rep.
	 *
	 * @return the dash rep
	 */
	public String getDashRep() {
		return this.dashRep;
	}
	
	/**
	 * Checks if the game is over.
	 *
	 * @return true, iff is game over
	 */
	public boolean isGameOver() {
		return this.bodyParts >= BODY_PART_LIMIT || this.dashRep.equalsIgnoreCase(this.word);
	}
}
