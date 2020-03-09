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
	private static final int NUMBEROFPLAYERS = 2;

	private static final int NUMBEROFNONDIAGONALS = 4;
	private static final int DIAGONALSIZE = 3;

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
	 * Places Player's picked Coodinate into TicTacToe board if it is a valid move.
	 * 
	 * @param playerName the name of the player inputting the coordinate.
	 * @param coordinate the coordinate the player has selected.
	 */
	public void inputCoordinate(String playerName, String coordinate) {
		String winningPlayer = null;

		if (this.isValidCoordinate(coordinate) && this.isValidPlayerName(playerName)) {
			this.coordinateMap.put(coordinate, playerName);
		}

		if (!this.nonDiagonal.contains(coordinate)) {
			winningPlayer = this.isDiagonalWin(playerName);
		}

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
		String winningPlayer = playerName;

		ArrayList<String> leftDiagonal = this.assembleLeftDiagonal();
		ArrayList<String> rightDiagonal = this.assembleRightDiagonal();

		for (String current : leftDiagonal) {
			if (!current.equals(playerName)) {
				winningPlayer = null;
			}
		}
		for (String current : rightDiagonal) {
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

}
