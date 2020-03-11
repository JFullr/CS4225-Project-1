package project.game.hangman;

/**
 * The Class HangMan.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class HangMan {

	private static final String NULL_GAME_WORD = "the game word cannot be null.";
	private static final String BLANK_GAME_WORD = "the game word cannot be blank.";
	private static final int BODY_PART_LIMIT = 6;

	private String[] dashRep;
	private String word;
	private int bodyParts;

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
		this.dashRep = this.getDashes();
	}

	private String[] getDashes() {
		String dashes = this.word.replaceAll(".", "_");
		return dashes.split("");
	}

}
