package project.game;

import java.util.Scanner;

/**
 * Creates Text User Interface for TicTacToe game.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class TicTacToeTUI {
	private TicTacToeManager aTicTacToeManager;

	/**
	 * Instantiates a new tic tac toe TUI.
	 */
	public TicTacToeTUI() {
		this.aTicTacToeManager = new TicTacToeManager();
	}

	/**
	 * Run.
	 */
	public void run() {
		Scanner consoleInput = new Scanner(System.in);

		System.out.println("Player 1 found, beginning TicTacToe");
		System.out.println(this.aTicTacToeManager.getBoard());

		System.out.println("Rows correlate to capital A-C. Columns to 1-3");
		System.out.println("Ex: A3");
		System.out.println("Please select a row/column");
		// String userInput = consoleInput.nextLine();

		consoleInput.close();

		// String userResult =
		// this.aTicTacToeManager.inputCoordinateWithPlayer(playerName, userInput);

	}
}
