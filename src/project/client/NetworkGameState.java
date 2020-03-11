package project.client;

import java.io.Serializable;

/**
 * The Enum NetworkGameState.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020.
 */
public enum NetworkGameState implements Serializable {

	HEART_BEAT,
	DISCONNECTED,
	
	LOBBY, 
	SYNCHRONIZING, 
	IN_GAME,
	MATCH_OVER

}
