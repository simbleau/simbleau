/**
 * 
 */
package edu.cs495.engine.game.cameras;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.AbstractCamera;

/** A Stationary Camera which stares at one point
 * 
 * @author Spencer Imbleau
 * @version February 2019
 */
public class StationaryCamera extends AbstractCamera {

	/** Initialize this camera to look at (0, 0) */
	public StationaryCamera() {
		this(0, 0);
	}
	
	/** Initialize this camera to a point
	 * 
	 * @param camX - X coordinate
	 * @param camY - Y coordinate
	 */
	public StationaryCamera(float camX, float camY) {
		this.camX = camX;
		this.camY = camY;
	}
	
	/** Does nothing. Camera does not move. */
	@Override
	public void update(GameDriver gameDriver) {}

	/** Change the camera point to a new point
	 * 
	 * @param offX - an X value for the new point
	 * @param offY - a Y value for the new point
	 */
	public void changePoint(float offX, float offY) {
		this.camX = offX;
		this.camY = offY;
	}

}
