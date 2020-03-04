package utils;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * The Class ErrorHandler.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class ErrorHandler {

	private static final Object LOCK = new Object();
	private static Queue<String> errors = new ArrayDeque<String>();

	/**
	 * Adds the error.
	 *
	 * @param error the error
	 */
	public static void addError(Exception error) {
		ErrorHandler.addError(error.getMessage());
	}

	/**
	 * Adds the error.
	 *
	 * @param error the error
	 */
	public static void addError(String error) {

		synchronized (LOCK) {
			ErrorHandler.errors.add("ERR: " + error);
		}

	}

	/**
	 * Checks for next error.
	 *
	 * @return true, if successful
	 */
	public static boolean hasNextError() {

		synchronized (LOCK) {
			return !errors.isEmpty();
		}

	}

	/**
	 * Consume next error.
	 *
	 * @return the string
	 */
	public static String consumeNextError() {

		synchronized (LOCK) {
			return errors.remove();
		}

	}

}
