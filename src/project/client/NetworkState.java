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
	DISCONNECTED,
	
	PLAYER_DISCONNECTED,
	PLAYER_CONNECTED,
	
	LOBBY, 
	SYNCHRONIZING, 
	IN_GAME,
	MATCH_OVER

}
