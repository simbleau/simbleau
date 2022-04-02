package edu.cs495.engine.game.satcollision;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.AbstractPhysicalGameObject;
import edu.cs495.engine.gfx.Renderer;

/** Classes which implement Collidable have collision logic
 * to determine whether a collision exists and in which way
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public abstract class Collidable {
	
	/** The parent object with this collision */
	protected AbstractPhysicalGameObject parent;
	
	/** The minimum x value to check for collision */
	protected int xMin;
	
	/** The maximum x value to check for collision */
	protected int xMax;
	
	/** The minimum y value to check for collision */
	protected int yMin;
	
	/** The maximum y value to check for collision */
	protected int yMax;
	
	/** Initialize a collidable collision area
	 * 
	 * @param parent - the parent of this collidable area
	 */
	public Collidable(AbstractPhysicalGameObject parent) {
		this.parent = parent;
	}
	
	/** Render the collidable area
	 * 
	 * @param gameDriver - the game driver
	 * @param renderer - the renderer 
	 */
	public abstract void render(GameDriver gameDriver, Renderer renderer);
	
	/** Returns whether this collidable object collides with another collidable object
	 * 
	 * @param object - an object which is collidable
	 * @return true if this collides with the object, false otherwise
	 */
	public abstract boolean collidesWith(Collidable object);
	
	/** Return the collision event between this object and another
	 * 
	 * @param parent - the object which tests collision
	 * @param object - an object which is colliding with parent
	 * @return a collision event report
	 * @see CollisionEvent
	 */
	public abstract CollisionEvent getCollisionEvent(Collidable object);
	
	/** Returns the local minimum X which should be tested for collision
	 *  
	 * @return the local minimum X which should be tested for collision
	 */
	public int getXMin() {
		return this.xMin;
	}
	
	/** Returns the local maximum X which should be tested for collision
	 *  
	 * @return the local maximum X which should be tested for collision
	 */
	public int getXMax(){
		return this.xMax;
	}
	
	/** Returns the local minimum Y which should be tested for collision
	 *  
	 * @return the local minimum Y which should be tested for collision
	 */
	public int getYMin(){
		return this.yMin;
	}
	
	/** Returns the local maximum Y which should be tested for collision
	 *  
	 * @return the local maximum Y which should be tested for collision
	 */
	public int getYMax() {
		return this.yMax;
	}
	
	/** Returns the parent of this collidable object
	 *
	 * @return the parent of this collidable object
	 */
	public AbstractPhysicalGameObject getParent() {
		return this.parent;
	}
}
