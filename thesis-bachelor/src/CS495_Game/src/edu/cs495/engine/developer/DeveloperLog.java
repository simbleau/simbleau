/**
 * 
 */
package edu.cs495.engine.developer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import edu.cs495.game.objects.player.chatbox.ChatProvider;


/** The developer log static object called for developer book-keeping
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class DeveloperLog extends ChatProvider {
	
	/** A list of debug reports */
	private static ArrayList<String> logs = new ArrayList<>();
	
	/** Whether or not to run the debug update in the game loop */
	private static boolean debugging = false;
	
	/** Constructor (Private) : This class contains only static methods/fields.
	 * Do not try to initialize this. */
	private DeveloperLog() {} //Private: Do not initialize this class.
	
	/** Print a message to stdout
	 * 
	 * @param log - a debug message
	 */
	public static void print(String log) {
		System.out.println(log);
		
	}
	
	
	/** Print an error message to stderr
	 * 
	 * @param error - a debug message
	 */
	public static void printErr(String error) {
		System.err.println(error);
	}
	
	
	/** Record a message containing debug information
	 * 
	 * @param log - a debug message
	 */
	public static void log(String log) {
		logs.add(log);
	}
	
	/** Print a message to stdout and log it
	 * 
	 * @param log - a debug message
	 */
	public static void printLog(String log) {
		print(log);
		log(log);
	}
	
	/** Print an error message to stderr and log it
	 * 
	 * @param error - a debug message
	 */
	public static void errLog(String error) {
		printErr(error);
		log(error);
	}
	
	/** Clears the list of debug messages */
	public static void clearLog() {
		logs = new ArrayList<>();
	}
	
	/** Saves the debug messages thus accumulated to the default folder
	 * as a file named 'logs-{0}', where {0} is the current date. */
	public static void saveLogs() {
		Date today = new Date();
		
		try (PrintWriter out = new PrintWriter("logs-" + today.toString())) {
		    out.println(logs);
		} catch (FileNotFoundException e) {
			print("Error: Failed to save logs." + System.lineSeparator() 
			+ "Message: " + e.getMessage());
		}
	}
	
	/** Toggle the debugging boolean */
	public static void toggleDebug() {
		debugging = !debugging;
		printLog("Debug set: " + debugging);
	}
	
	/** Returns whether or not we are debugging
	 * 
	 *  @return true if {@link #debugging} is true, false otherwise
	 */
	public static boolean isDebugging() {
		return debugging;
	}

}
