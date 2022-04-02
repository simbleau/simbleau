package edu.cs495.engine.game;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.satcollision.CollisionEvent;
import edu.cs495.engine.game.satcollision.SATCollisionComponent;
import edu.cs495.engine.gfx.Renderer;

public abstract class AbstractPhysicalGameObject extends AbstractGameObject {
	
	/** A physics component, if the object has physics */
	protected SATCollisionComponent physicsComponent;
	
	/** Creates an abstract game object with physics
	 * 
	 * @param tag - the tag of the object
	 * @param posX - the starting posX of the object
	 * @param posY - the starting posY of the object
	 * @param width - the width of the object
	 * @param height - the height of the object
	 * @see AbstractGameObject
	 */
	public AbstractPhysicalGameObject(String tag, float posX, float posY, int width, int height, int depth) {
		super(tag, posX, posY, width, height);
		this.physicsComponent = new SATCollisionComponent(this, (depth / 2));
	}
	
	/** Set the physics of our physical object
	 * Before invoking init, please set the {@link #physicsComponent}
	 * 
	 * @param gameDriver - the game driver for this object
	 */
	@Override
	public abstract void init(GameDriver gameDriver);

	/** All we do here is update our physical object
	 * 
	 * @param gameDriver - the game driver for this physical object
	 */
	@Override
	public void update(GameDriver gameDriver) {
		super.update(gameDriver);
		
		physicsComponent.update(gameDriver);
	}

	
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		super.render(gameDriver, renderer);
		
		physicsComponent.render(gameDriver, renderer);
	}
	
	/** Returns the physics component if it exists
	 * 
	 * @return the SATCollisionComponent, or null if it hasn't been set
	 */
	public SATCollisionComponent getPhysics() {
		return this.physicsComponent;
	}
	 
	/** Set the physics to be an SAT Collision component
	 * 
	 * @param physicsComponent - the new physics component
	 * @see SATCollisionComponent
	 */
	public void setPhysics(SATCollisionComponent physicsComponent) {
		this.physicsComponent = physicsComponent;
	}
	
	/** Determines if this physical object collides with another
	 * 
	 * @param physicalObject - an object to test collision with
	 * @return true if the objects, collide, false otherwise
	 */
	public boolean collidesWith(AbstractPhysicalGameObject physicalObject) {
		return physicsComponent.collidesWith(physicalObject.physicsComponent);
	}
	
	
	public CollisionEvent getCollisionEvent(AbstractPhysicalGameObject physicalObject) {
		return physicsComponent.getCollisionEvent(physicalObject.physicsComponent);
	}
	
	/** Abstract method - Invoked when the object receives a collision with another object 
	 * 
	 * @param collisionEvent - the event of collision
	 */
	public abstract void handleCollision(CollisionEvent collisionEvent);
}
