package edu.cs495.game;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.game.objects.player.LocalPlayer;

/** A game window listener listens for window events
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class GameWindowListener implements WindowListener {

	/** The game linked to the window */
	Game game;
	
	/** Initialize the game window listener
	 * 
	 * @param game - the game which is being handled
	 */
	public GameWindowListener(Game game) {
		this.game = game;
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		// Nothing
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		//Nothing
	}
 
	@Override
	public void windowClosing(WindowEvent arg0) {
		//Disconnect from the game if the player is online
		LocalPlayer localPlayer = game.getPlayer();
		if (localPlayer.isOnline()) {
			localPlayer.getNetwork().killNetwork();
		} else {
			DeveloperLog.printLog("Exiting, not online...");
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// Nothing
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// Nothing
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// Nothing
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// Nothing
	}
}
