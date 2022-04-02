/**
 * 
 */
package edu.cs495.game;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.AbstractGame;

/** Starting point of the game
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class Main {

	/** Starts the game
	 * @param args - The command line arguments
	 */
	public static void main(String[] args) {
		//Initialize the game
		AbstractGame game = new Game(System.nanoTime());
		
		GameDriver gameDriver = new GameDriver(game);
		gameDriver.setGameWidth(300);
		gameDriver.setGameHeight(250);
		gameDriver.setScale(2f);
		gameDriver.start();
	}

}
