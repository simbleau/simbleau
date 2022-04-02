/**
 * 
 */
package edu.cs495.engine.game;

import edu.cs495.engine.GameDriver;

/** An abstract camera used for rendering
 * 
 * @author Spencer Imbleau
 * @version February 2019
 */
public abstract class AbstractCamera {
	/** The displacement of the camera on the X axis */
	protected float camX;
	
	/** The displacement of the camera on the Y axis */
	protected float camY;
	
	/** Update the camera (called at game loop)
	 * 
	 * @param gameDriver - the game driver for this camera
	 * @see GameDriver
	 */
	public abstract void update(GameDriver gameDriver);
	
	/** Retrieve the Camera's X position
	 * 
	 * @return X position for this camera
	 */
	public float getCamX() {
		return this.camX;
	}

	/** Retrieve the Camera's X position
	 * 
	 * @return X position for this camera
	 */
	public float getCamY() {
		return this.camY;
	}
}
