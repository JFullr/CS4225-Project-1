package project.game;

/**
 * Coordinates use of TicTacToe class to User Interface.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class TicTacToeManager {

	private TicTacToe aTicTacToe;

	/**
	 * Instantiates a TicTacToeManager with an unused TicTacToe class.
	 */
	public TicTacToeManager() {
		this.aTicTacToe = new TicTacToe();
	}

	public String inputCoordinateWithPlayer(String playerName, String coordinate) {
		return this.aTicTacToe.inputCoordinate(playerName, coordinate);
	}

}
