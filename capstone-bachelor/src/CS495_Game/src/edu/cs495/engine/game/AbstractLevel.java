/**
 * 
 */
package edu.cs495.engine.game;

import java.util.ArrayList;
import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Image;

/** An abstract game level object models a level of a game. This holds several 
 * attributes such as the background of the level, objects in the level, and
 * more used for updating and rendering.
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public abstract class AbstractLevel {
	
	/** The map image */
	protected Image map;
	
	/** The width of the level */
	protected int width;

	/** The height of the level */
	protected int height;
	
	/** Determines if the level supports lighting */
	protected boolean lighting = false;
	
	/** Boundaries for the map, if the map has it */
	protected MapBoundaries boundaries;
	
	/** Contains the game objects in this level
	 * 
	 * @see AbstractGameObject
	 */
	protected ArrayList<AbstractGameObject> levelObjects = new ArrayList<>();
	
	/** Contains the physical game objects in this level
	 * 
	 * @see AbstractGameObject
	 */
	protected ArrayList<AbstractPhysicalGameObject> levelPhysicalObjects = new ArrayList<>();
	
	/** Initializes this game level
	 * 
	 * @param gameDriver - a reference to the Game Driver
	 */
	public abstract void init(GameDriver gameDriver);
	
	/** Updates all game level objects
	 *  
	 * @param gameDriver - the driver for the game 
	 */
	public void update(GameDriver gameDriver){
		//Thread-safe updating
		for (int i = 0; i < levelObjects.size(); i++) {
			//Update everything
			AbstractGameObject gameObject = levelObjects.get(i);
			gameObject.update(gameDriver);
			
			//Check for dead objects
			if (gameObject.isDead()) {
				removeGameObject(gameObject);
				i--;
			}
		}
	}

	
	/** Render all game objects in this level
	 * 
	 * @param gameDriver - the game driver for this level
	 * @param renderer - the renderer for this level
	 */
	public void render(GameDriver gameDriver, Renderer renderer) {
		for (int i = 0; i < levelObjects.size(); i++) {
			levelObjects.get(i).render(gameDriver, renderer);
		}
	}
	
	/** Adds a game object to the {@link #levelObjects}
	 * 
	 * @param gameObject - an {@link AbstractGameObject} to add
	 */
	public void addGameObject(AbstractGameObject gameObject) {
		levelObjects.add(gameObject);
		if (gameObject instanceof AbstractPhysicalGameObject) {
			levelPhysicalObjects.add((AbstractPhysicalGameObject) gameObject);
		}
	}
	
	/** Removes a game object from the {@link #levelObjects}
	 * 
	 * @param tag - the tag of the {@link AbstractGameObject} to remove
	 */
	public void removeGameObject(String tag) {
		for (int i = 0; i < levelObjects.size(); i++) {
			AbstractGameObject gameObject = levelObjects.get(i);
			if (gameObject.getTag().equals(tag)) {
				gameObject.kill();
				break;
			}
		}
	}
	
	
	/** Removes a referenced game object from the {@link #levelObjects}
	 * 
	 * @param object - The {@link AbstractGameObject} to remove
	 */
	public void removeGameObject(AbstractGameObject object) {
		levelObjects.remove(object);
		
		if (object instanceof AbstractPhysicalGameObject) {
			levelPhysicalObjects.remove((AbstractPhysicalGameObject) object);
		}
	}
	
	/** Returns the objects on the level
	 * 
	 * @return the game objects in the level
	 */
	public ArrayList<AbstractGameObject> getObjects() {
		return this.levelObjects;
	}
	
	/** Returns the physical objects on the level for quicker traversing during collision
	 * 
	 * @return the physical game objects in the level
	 */
	public ArrayList<AbstractPhysicalGameObject> getPhysicalObjects() {
		return this.levelPhysicalObjects;
	}
	
	/** Returns a game object in the current level, if it exists, or null
	 * 
	 * @param tag - the tag of the game object (CaSe SenSiTiVe)
	 * @return a game object with the corresponding unique tag, or null
	 */
	public AbstractGameObject getObject(String tag) {
		for (AbstractGameObject gameObject : levelObjects) {
			if (gameObject.getTag().equals(tag)) {
				return gameObject;
			}
		}
		return null;
	}


	/** Returns if this level supports dynamic lighting (mainly for performance)
	 * 
	 *  @return true if the level has dynamic lighting, false otherwise
	 */
	public boolean hasLighting() {
		return lighting;
	}
	
	/** Toggles the lighting for the level */
	public void toggleLighting() {
		lighting = !lighting;
		DeveloperLog.printLog(this.getClass().getSimpleName() 
				+ ": lighting set to " + lighting);
	}
	
	/** Returns the width of the game level
	 * 
	 *  @return the width of the level
	 */
	public int getWidth() {
		return width;
	}

	/** Returns the height of the level
	 * 
	 *  @return the height of the level 
	 */
	public int getHeight() {
		return height;
	}
	
	/** Return whether the map has boundaries or not
	 * 
	 * @return true if {@link #boundaries} is not null, false otherwise
	 */
	public boolean hasBoundaries() {
		return (this.boundaries != null);
	}
	
	/** Get the map boundaries
	 * 
	 * @return the map boundaries (could be null)
	 */
	public MapBoundaries getBoundaries() {
		return this.boundaries;
	}

	/** Get the map itself
	 * 
	 * @return the map image
	 */
	public Image getMap() {
		return this.map;
	}

	/** Returns the spawn point X on this level
	 * 
	 * @return the spawn point X coordinate
	 */
	public abstract float getSpawnX();

	
	/** Returns the spawn point Y on this level
	 * 
	 * @return the spawn point Y coordinate
	 */
	public abstract float getSpawnY();

}
