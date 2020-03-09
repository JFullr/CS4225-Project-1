package project.game;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An xy-coordinate based model of TicTacToe.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class TicTacToe {

	private static final String PLAYER1 = "X";
	private static final String PLAYER2 = "O";
	private static final String EMPTYSPACE = "_";
	private static final String CAT = "CAT";
	private static final int NUMBEROFPLAYERS = 2;

	private static final String R1C1 = "A1";
	private static final String R1C2 = "A2";
	private static final String R1C3 = "A3";

	private static final String R2C1 = "B1";
	private static final String R2C2 = "B2";
	private static final String R2C3 = "B3";

	private static final String R3C1 = "C1";
	private static final String R3C2 = "C2";
	private static final String R3C3 = "C3";

	private HashMap<String, String> coordinateMap = new HashMap<String, String>();
	private String[] playerNames;
	private ArrayList<String> nonDiagonal;

	/**
	 * Instantiates an empty TicTacToe board.
	 */
	public TicTacToe() {

		this.playerNames = new String[NUMBEROFPLAYERS];
		this.playerNames[0] = PLAYER1;
		this.playerNames[1] = PLAYER2;

		this.nonDiagonal = new ArrayList<String>();
		this.nonDiagonal.add(R1C2);
		this.nonDiagonal.add(R2C1);
		this.nonDiagonal.add(R2C3);
		this.nonDiagonal.add(R3C2);

		this.coordinateMap.put(R1C1, EMPTYSPACE);
		this.coordinateMap.put(R1C2, EMPTYSPACE);
		this.coordinateMap.put(R1C3, EMPTYSPACE);

		this.coordinateMap.put(R2C1, EMPTYSPACE);
		this.coordinateMap.put(R2C2, EMPTYSPACE);
		this.coordinateMap.put(R2C3, EMPTYSPACE);

		this.coordinateMap.put(R3C1, EMPTYSPACE);
		this.coordinateMap.put(R3C2, EMPTYSPACE);
		this.coordinateMap.put(R3C3, EMPTYSPACE);
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
	public String inputCoordinate(String playerName, String coordinate) {

		if (playerName == null) {
			throw new IllegalArgumentException("Player name cannot be null.");
		}

		if (coordinate == null) {
			throw new IllegalArgumentException("Coordinate input cannot be null.");
		}

		String winningPlayer = null;

		if (this.coordinateMap.get(coordinate).equals(playerName)) {
			return "Coordinate choice invalid, please select a new coordinate.";
		}

		if (this.isValidCoordinate(coordinate) && this.isValidPlayerName(playerName)) {
			this.coordinateMap.put(coordinate, playerName);
		}

		if (!this.nonDiagonal.contains(coordinate)) {
			winningPlayer = this.isDiagonalWin(playerName);
		}

		if (winningPlayer != null && this.isValidPlayerName(playerName)) {
			winningPlayer = this.isHorizontalWin(playerName);
		}

		if (winningPlayer != null && this.isValidPlayerName(playerName)) {
			winningPlayer = this.isVerticalWin(playerName);
		}

		if (winningPlayer == null && this.checkForCat()) {
			winningPlayer = CAT;
		}

		return winningPlayer;
	}

	/**
	 * Returns the String object representing Player 1.
	 * 
	 * @return the String object representing Player 1.
	 */
	public String getPlayer1() {
		return PLAYER1;
	}

	/**
	 * Returns the String object representing Player 2.
	 * 
	 * @return the String object representing Player 2.
	 */
	public String getPlayer2() {
		return PLAYER2;
	}

	private boolean isValidCoordinate(String coordinate) {
		boolean answer = false;

		if (this.coordinateMap.containsKey(coordinate)) {
			answer = true;
		}
		return answer;
	}

	private boolean isValidPlayerName(String playerName) {
		boolean answer = false;

		for (int i = 0; i < NUMBEROFPLAYERS; i++) {
			if (this.playerNames[i].equals(playerName)) {
				answer = true;
			}
		}
		return answer;
	}

	private String isDiagonalWin(String playerName) {
		String winningPlayer = null;

		ArrayList<String> leftDiagonal = this.assembleLeftDiagonal();
		ArrayList<String> rightDiagonal = this.assembleRightDiagonal();

		String leftCheck = this.didTheyWin(playerName, leftDiagonal);
		String rightCheck = this.didTheyWin(playerName, rightDiagonal);

		if (leftCheck != null || rightCheck != null) {
			winningPlayer = playerName;
		}

		return winningPlayer;
	}

	private String isHorizontalWin(String playerName) {
		String winningPlayer = null;

		ArrayList<String> topHorizontal = this.assembleTopHorizontal();
		ArrayList<String> middleHorizontal = this.assembleMiddleHorizontal();
		ArrayList<String> bottomHorizontal = this.assembleBottomHorizontal();

		String topCheck = this.didTheyWin(playerName, topHorizontal);
		String middleCheck = this.didTheyWin(playerName, middleHorizontal);
		String bottomCheck = this.didTheyWin(playerName, bottomHorizontal);

		if (topCheck != null || middleCheck != null || bottomCheck != null) {
			winningPlayer = playerName;
		}

		return winningPlayer;
	}

	private String isVerticalWin(String playerName) {
		String winningPlayer = null;

		ArrayList<String> leftVertical = this.assembleLeftVertical();
		ArrayList<String> middleVertical = this.assembleMiddleVertical();
		ArrayList<String> rightVertical = this.assembleRightVertical();

		String leftCheck = this.didTheyWin(playerName, leftVertical);
		String middleCheck = this.didTheyWin(playerName, middleVertical);
		String rightCheck = this.didTheyWin(playerName, rightVertical);

		if (leftCheck != null || middleCheck != null || rightCheck != null) {
			winningPlayer = playerName;
		}

		return winningPlayer;
	}

	private String didTheyWin(String playerName, ArrayList<String> data) {
		String winningPlayer = playerName;
		for (String current : data) {
			if (!current.equals(playerName)) {
				winningPlayer = null;
			}
		}

		return winningPlayer;
	}

	private ArrayList<String> assembleLeftDiagonal() {
		ArrayList<String> leftDiagonal = new ArrayList<String>();

		leftDiagonal.add(this.coordinateMap.get(R1C1));
		leftDiagonal.add(this.coordinateMap.get(R2C2));
		leftDiagonal.add(this.coordinateMap.get(R3C3));

		return leftDiagonal;
	}

	private ArrayList<String> assembleRightDiagonal() {
		ArrayList<String> rightDiagonal = new ArrayList<String>();

		rightDiagonal.add(this.coordinateMap.get(R1C3));
		rightDiagonal.add(this.coordinateMap.get(R2C2));
		rightDiagonal.add(this.coordinateMap.get(R3C1));

		return rightDiagonal;
	}

	private ArrayList<String> assembleTopHorizontal() {
		ArrayList<String> horizontal = new ArrayList<String>();

		horizontal.add(this.coordinateMap.get(R1C1));
		horizontal.add(this.coordinateMap.get(R1C2));
		horizontal.add(this.coordinateMap.get(R1C3));

		return horizontal;
	}

	private ArrayList<String> assembleMiddleHorizontal() {
		ArrayList<String> horizontal = new ArrayList<String>();

		horizontal.add(this.coordinateMap.get(R2C1));
		horizontal.add(this.coordinateMap.get(R2C2));
		horizontal.add(this.coordinateMap.get(R2C3));

		return horizontal;
	}

	private ArrayList<String> assembleBottomHorizontal() {
		ArrayList<String> horizontal = new ArrayList<String>();

		horizontal.add(this.coordinateMap.get(R3C1));
		horizontal.add(this.coordinateMap.get(R3C2));
		horizontal.add(this.coordinateMap.get(R3C3));

		return horizontal;
	}

	private ArrayList<String> assembleLeftVertical() {
		ArrayList<String> vertical = new ArrayList<String>();

		vertical.add(this.coordinateMap.get(R1C1));
		vertical.add(this.coordinateMap.get(R2C1));
		vertical.add(this.coordinateMap.get(R3C1));

		return vertical;
	}

	private ArrayList<String> assembleMiddleVertical() {
		ArrayList<String> vertical = new ArrayList<String>();

		vertical.add(this.coordinateMap.get(R1C2));
		vertical.add(this.coordinateMap.get(R2C2));
		vertical.add(this.coordinateMap.get(R3C2));

		return vertical;
	}

	private ArrayList<String> assembleRightVertical() {
		ArrayList<String> vertical = new ArrayList<String>();

		vertical.add(this.coordinateMap.get(R1C3));
		vertical.add(this.coordinateMap.get(R2C3));
		vertical.add(this.coordinateMap.get(R3C3));

		return vertical;
	}

	private boolean checkForCat() {
		boolean isCat = false;

		if (!this.coordinateMap.containsValue(EMPTYSPACE)) {
			isCat = true;
		}

		return isCat;
	}

	/**
	 * Returns a String representation of the TicTacToe class.
	 * 
	 * @return a String representation of the TicTacToe class.
	 */
	public String toString() {
		String outputOfTicTacToeBoard = "";

		ArrayList<String> topHorizontal = this.assembleTopHorizontal();
		ArrayList<String> middleHorizontal = this.assembleMiddleHorizontal();
		ArrayList<String> bottomHorizontal = this.assembleBottomHorizontal();

		outputOfTicTacToeBoard += this.toStringAssistance(topHorizontal);
		outputOfTicTacToeBoard += this.toStringAssistance(middleHorizontal);
		outputOfTicTacToeBoard += this.toStringAssistance(bottomHorizontal);

		return outputOfTicTacToeBoard;
	}

	private String toStringAssistance(ArrayList<String> row) {
		String output = "";

		for (String current : row) {
			output += current;
		}
		output += System.lineSeparator();

		return output;
	}

}
