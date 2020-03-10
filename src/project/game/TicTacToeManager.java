package project.game;

import project.client.GameClientManager;

/**
 * Coordinates use of TicTacToe class to User Interface.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class TicTacToeManager {

	private GameClientManager aGameClientManager;
	private TicTacToe aTicTacToe;

	/**
	 * Instantiates a TicTacToeManager with an unused TicTacToe class.
	 */
	public TicTacToeManager() {
		this.aTicTacToe = new TicTacToe();
	}

	/**
	 * Places Player's picked Coordinate into TicTacToe board if it is a valid move.
	 * 
	 * @param playerName the name of the player inputting the coordinate.
	 * @param coordinate the coordinate the player has selected.
	 * 
	 * @return The winning player's name. If no victor is decided, returns null.
	 *         Returns error message is the coordinate has already been selected.
	 */
	public String inputCoordinateWithPlayer(String playerName, String coordinate) {
		return this.aTicTacToe.inputCoordinate(playerName, coordinate);
	}

	/**
	 * Deletes the current TicTacToe game and replaces it with a new TicTacToe game.
	 */
	public void resetGame() {
		this.aTicTacToe = new TicTacToe();
	}

	/**
	 * Returns a string representation of the board.
	 * 
	 * @return a string representation of the board.
	 */
	public String getBoard() {
		return this.aTicTacToe.toString();
	}

	/**
	 * Returns the string ID of the first player.
	 * 
	 * @return the string ID of the first player.
	 */
	public String getPlayer1() {
		return this.aTicTacToe.getPlayer1();
	}

	/**
	 * Returns the string ID of the second player.
	 * 
	 * @return the string ID of the second player.
	 */
	public String getPlayer2() {
		return this.aTicTacToe.getPlayer2();
	}

}
