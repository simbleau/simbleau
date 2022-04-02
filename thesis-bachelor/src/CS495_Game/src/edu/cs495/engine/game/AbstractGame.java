/**
 * 
 */
package edu.cs495.engine.game;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.gfx.Renderer;

/** An abstract game handler. Each game contains a level, camera, and 
 * abstract methods for initialization, updating, and rendering.
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public abstract class AbstractGame {
	
	/** The current level being updated and rendered at game tick 
	 * 
	 * @see AbstractLevel
	 */
	protected AbstractLevel level;
	
	/** The current active camera 
	 *
	 * @see AbstractCamera
	 */
	protected AbstractCamera camera;

	/** The current game time */
	protected long time;
	
	
	
	/** Initialization of the game
	 * 
	 * @param gameDriver - the driver for the game 
	 */
	public void init(GameDriver gameDriver) {
		level.init(gameDriver);
	}
	
	/** Implementation of the game tick is here
	 * 
	 * @param gameDriver - the driver for the game 
	 */
	public void update(GameDriver gameDriver) {
		level.update(gameDriver);
	}
	
	/** Rendering game assets is implemented here
	 * 
	 * @param gameDriver - the driver for the game
	 * @param renderer - the renderer for the game
	 */
	public void render(GameDriver gameDriver, Renderer renderer) {
		level.render(gameDriver, renderer);
	}
	
	/** Returns the current level of the game 
	 * 
	 * @return the current level of the game
	 */
	public AbstractLevel getLevel() {
		return level;
	}

	/** Sets the current level of the game
	 * 
	 * @param level - The new level to set
	 */
	public void setLevel(AbstractLevel level) {
		this.level = level;
		DeveloperLog.printLog("Level set: " + level.getClass().getSimpleName());
	}
	
	public Long getTime() {
		return this.time;
	}
	
	/** Returns the camera for the game
	 * 
	 * @return the current camera object 
	 */
	public AbstractCamera getCamera() {
		return camera;
	}

	/** Sets the current camera for the game
	 * 
	 * @param camera - The new camera to set 
	 */
	public void setCamera(AbstractCamera camera) {
		this.camera = camera;
		DeveloperLog.printLog("Camera set: " + camera.getClass().getSimpleName());
	}

	/** Increment the time of the game
	 * 
	 * @param deltaTime - the time to add
	 */
	public void incrementTime(long deltaTime) {
		this.time += deltaTime;
	}
	
}
