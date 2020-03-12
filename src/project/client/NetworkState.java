package project.client;

import java.io.Serializable;

/**
 * The Enum NetworkGameState.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020.
 */
public enum NetworkState implements Serializable {

	HEART_BEAT, 
	DISCONNECTED, // Generic way of saying to user isn't connected. The genericness may provide
								// issues.

	PLAYER_DISCONNECTED, 
	PLAYER_CONNECTED,

	LOBBY, // Holds the player in connection until another player connects, which them goes
			// to IN_GAME. Joined by a lobbycount if we can be arsed.
	SYNCHRONIZING, // Initial state. Displays the username enter gui and will switch to LOBBY when
					// correct
					// username has been entered.
	IN_GAME, // Shoves user input into some kind of game logic and then tosses game logic
				// back at the player. When victory occurs, it moves to MATCH_OVER Most vague
				// aspect of system.
	MATCH_OVER // Gives the user an arbitrary score and disconnects them from lobby.

}
