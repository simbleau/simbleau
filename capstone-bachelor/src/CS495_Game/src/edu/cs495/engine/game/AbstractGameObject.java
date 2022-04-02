/**
 * 
 */
package edu.cs495.engine.game;

import java.util.ArrayList;
import java.util.List;
import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.Renderer;

/** An abstract game object features several attributes which are shared
 * across all game objects used for updating and rendering.
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public abstract class AbstractGameObject {
	
	/** A tag for the object, most helpful when searching for game objects */
	protected String tag;
	
	/** A list of components for this game object
	 * 
	 * @see {@link AbstractComponent} */
	protected List<AbstractComponent> components;
	
	/** The x coordinate to start rendering at*/
	protected int offX;
	/** The y coordinate to start rendering at  */
	protected int offY;
	/** The x coordinate of the object as it exists on the game's level */
	protected float posX;
	/** The y coordinate of the object as it exists on the game's level  */
	protected float posY;
	
	/** The width the game object occupies */
	protected int width;
	/** The height the game object occupies */
	protected int height;
	
	/** Determines if the object is dead. Dead objects are removed and cleaned up
	 * at the end of any game update. */
	protected boolean dead = false;

	/** Initialize an abstract game object
	 * 
	 * @param tag - a tag for the object
	 * @param posX - a starting position X for the object
	 * @param posY - a starting position X for the object
	 * @param width - the width of the object
	 * @param height - the height of the object
	 */
	public AbstractGameObject(String tag, float posX, float posY, int width, int height) {
		this.tag = tag;
		this.components = new ArrayList<>();
		this.width = width; 
		this.height = height;
		this.posX = posX;
		this.posY = posY;
		this.offX = ((int)posX) - (width / 2);
		this.offY = ((int) posY) - (height);
	}
	
	/** Initialization of the object 
	 * 
	 * @param gameDriver - the game driver for this object
	 */
	public abstract void init(GameDriver gameDriver);
	
	/** This update is called at every game update
	 * 
	 * @param gameDriver - the game driver for this object
	 */
	public void update(GameDriver gameDriver) {
		for (int i = 0; i < components.size(); i++) {
			components.get(i).update(gameDriver);
		}
	}
	
	/** This is the render procedure for this game object
	 * 
	 * @param gameDriver - the game driver for this object
	 * @param renderer - the renderer for this object
	 */
	public void render(GameDriver gameDriver, Renderer renderer) {
		for (int i = 0; i < components.size(); i++) {
			components.get(i).render(gameDriver, renderer);
		}
	}

	
	/** Get the {@link AbstractComponent}s which affect this game object
	 * 
	 * @return a list of {@link AbstractComponent}s which affect this game object
	 */
	public List<AbstractComponent> getComponents() {
		return components;
	}

	
	/** Get the tag of this game object
	 * 
	 * @return the tag of this game object 
	 */
	public String getTag() {
		return tag;
	}
	
	/** Returns the x position of this object 
	 * 
	 * @return the position x value
	 */
	public float getPosX() {
		return posX;
	}

	/** Returns the Y position of this object 
	 * 
	 * @return the position y value
	 */
	public float getPosY() {
		return posY;
	}
	

	/** Return the X offset for this object
	 * 
	 * @return the offset X for rendering 
	 */
	public int getOffX() {
		return offX;
	}

	/** Returns the Y offset for this object
	 * 
	 *  @return the offset Y for rendering
	 */
	public int getOffY() {
		return offY;
	}

	/** Return the width of this object
	 * 
	 * @return the width of this object
	 */
	public int getWidth() {
		return width;
	}

	/** Returns the height of this object
	 * 
	 * @return the height of this object
	 */
	public int getHeight() {
		return height;
	}
	
	/** Return if this game object is dead or not. Dead objects are removed from
	 * the level at the end of every game tick.
	 * 
	 * @return true if the object is dead (needs cleanup), false otherwise 
	 * @see {@link #dead}
	  */
	public boolean isDead() {
		return dead;
	}
	
	/** Sets the dead variable to true 
	 * 
	 * @see {@link dead} 
	 */
	public void kill() {
		dead = true;
	}
}
