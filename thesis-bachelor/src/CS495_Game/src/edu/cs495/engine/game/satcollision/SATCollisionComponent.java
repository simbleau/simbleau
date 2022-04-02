/**
 * 
 */
package edu.cs495.engine.game.satcollision;

import java.util.LinkedList;
import java.util.List;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.game.AbstractComponent;
import edu.cs495.engine.game.AbstractPhysicalGameObject;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Box;
import edu.cs495.engine.gfx.obj.Line;
import edu.cs495.game.objects.projectiles.AbstractProjectile;

/**
 * An SAT collision component adds collision detection.
 * 
 * @version March 2019
 * @author Spencer Imbleau
 */
public class SATCollisionComponent extends AbstractComponent {

	/** The area of collision for collision probing */
	protected List<Collidable> collidableAreas;

	/** The parent of these physics */
	protected AbstractPhysicalGameObject parent;
	
	/** Collision depth */
	protected int extrusion;
	
	/** The minimum offset to the parent's offX which should be searched for collision */
	protected int xMin;
	
	/** The maximum offset to the parent's offX which should be searched for collision */
	protected int xMax;
	
	/** The minimum offset to the parent's offY which should be searched for collision */
	protected int yMin;
	
	/** The maximum offset to the parent's offY which should be searched for collision */
	protected int yMax;
	
	/** The offset to the position which starts the floor of this object */
	protected int yFloor;

	/** Initialize the SATCollisionComponent component
	 * 
	 * @param parent - the parent object to this component
	 */
	public SATCollisionComponent(AbstractPhysicalGameObject parent, int extrusion) {
		super(parent);
		this.parent = parent;
		this.extrusion = extrusion;
		this.yFloor = 0;
		this.collidableAreas = new LinkedList<>();
	}

	@Override
	public void init(GameDriver gameDriver) {
		this.collidableAreas.clear();
		updateCollisionBounds();
	}

	/**
	 * Check every object O(n) for collision
	 * 
	 * @param gameDriver - the gameDriver which the objects we test for collision
	 *                   exist in
	 */
	@Override
	public void update(GameDriver gameDriver) {
		List<AbstractPhysicalGameObject> physicalObjects = gameDriver.getLevel().getPhysicalObjects();
		for (int i = 0; i < physicalObjects.size(); i++) {
			AbstractPhysicalGameObject physicalObject = physicalObjects.get(i);
			
			if (physicalObject == this.parent
					|| ((parent instanceof AbstractProjectile) && (physicalObject instanceof AbstractProjectile))) {
				continue;
			} else {
				if (this.parent.collidesWith(physicalObject)) {
					CollisionEvent event = this.parent.getCollisionEvent(physicalObject);
					this.parent.handleCollision(event);
				}
			}
		}
	}

	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		if (DeveloperLog.isDebugging()) {
		for (int i = 0; i < collidableAreas.size(); i++) {
			collidableAreas.get(i).render(gameDriver, renderer);
		}
		renderer.draw(new Line(0, extrusion * 2, 0xffff0000), Integer.MAX_VALUE, 
				(int) parent.getPosX(), (int) parent.getPosY() + yFloor - extrusion);
		renderer.draw(new Line(3, 0, 0xffff0000), Integer.MAX_VALUE, 
				(int) parent.getPosX() - 1, (int) parent.getPosY() + yFloor - extrusion);
		renderer.draw(new Line(3, 0, 0xffff0000), Integer.MAX_VALUE, 
				(int) parent.getPosX() - 1, (int) parent.getPosY() + yFloor + extrusion - 1);
		}
	}

	/**
	 * Add a {@link Collidable} area to the list of collision areas
	 * 
	 * @param area - a {@link Collidable} area
	 * @see Collidable
	 */
	public void addCollidableArea(Collidable area) {
		if (collidableAreas.size() == 0) {
			this.xMin = area.getXMin();
			this.xMax = area.getXMax();
			this.yMin = area.getYMin();
			this.yMax = area.getYMax();
			DeveloperLog.print(area.parent.getTag() + " > " + area.getClass().getSimpleName() + "Init: " + xMin + ", " + yMin + " - " + xMax + ", " + yMax);
		} else {
			if (xMin > area.getXMin()) {
				this.xMin = area.getXMin();
				DeveloperLog.print(area.parent.getTag() + " > " +  area.getClass().getSimpleName() + "XMinUpdate: " + xMin + ", " + yMin + " - " + xMax + ", " + yMax);
			} else if (xMax < area.getXMax()) {
				this.xMax = area.getXMax();
				DeveloperLog.print(area.parent.getTag() + " > " + area.getClass().getSimpleName() + "XMaxUpdate: " + xMin + ", " + yMin + " - " + xMax + ", " + yMax);
			}
			if (yMin > area.getYMin()) {
				this.yMin = area.getYMin();
				DeveloperLog.print(area.parent.getTag() + " > " + area.getClass().getSimpleName() + "YMinUpdate: " + xMin + ", " + yMin + " - " + xMax + ", " + yMax);
			} else if (yMax < area.getYMax()) {
				this.yMax = area.getYMax();
				DeveloperLog.print(area.parent.getTag() + " > " + area.getClass().getSimpleName() + "YMaxUpdate: " + xMin + ", " + yMin + " - " + xMax + ", " + yMax);
			}
		}
		
		this.collidableAreas.add(area);
	}

	/** Update the collision boundaries */
	protected void updateCollisionBounds() {
		if (hasCollidableAreas()) {
			this.xMin = collidableAreas.get(0).getXMin();
			this.xMax = collidableAreas.get(0).getXMax();
			this.yMin = collidableAreas.get(0).getYMin();
			this.yMin = collidableAreas.get(0).getYMax();
			for (int i = 1; i < collidableAreas.size(); i++) {
				Collidable area = collidableAreas.get(i);
				if (this.xMin > area.getXMin()) {
					this.xMin = area.getXMin();
				} else if (this.xMax < area.getXMax()) {
					this.xMax = area.getXMax();
				}
				if (this.yMin > area.getYMin()) {
					this.yMin = area.getYMin();
				} else if (this.yMax < area.getYMax()) {
					this.yMax = area.getYMax();
				}
			}	
		} else {
			this.xMin = 0;
			this.xMax = parent.getWidth();
			this.yMin = 0;
			this.yMax = parent.getHeight();
		}
	}
	
	/**
	 * Remove a {@link Collidable} area from the list of collision areas
	 * 
	 * @param area - a {@link Collidable} area
	 * @see Collidable
	 */
	public void removeCollidableArea(Collidable area) {
		this.collidableAreas.remove(area);
		updateCollisionBounds();
	}

	/**
	 * Clear the list of {@link Collidable} areas
	 * 
	 * @param area - a {@link Collidable} area
	 * @see Collidable
	 */
	public void clearCollidableAreas() {
		this.collidableAreas.clear();
		updateCollisionBounds();
	}

	/**
	 * Return whether this collision component has {@link Collidable} areas
	 * 
	 * @return true if this collision component has {@link Collidable} areas
	 * @see Collidable
	 */
	public boolean hasCollidableAreas() {
		return this.collidableAreas.size() != 0;
	}

	/**
	 * Determines if we have a collision with another object If the object has no
	 * physics component, then there is never a collision.
	 * 
	 * @param object - an object to test against this one
	 * @return true if there is a collision, false otherwise
	 */
	public boolean collidesWith(SATCollisionComponent physics) {

		// Get the delta Y between both object's posY
		int dy = ((int) parent.getPosY() + yFloor) - ((int) physics.parent.getPosY() + physics.yFloor);

		// Check that we are within a simulated depth on the Y axis
		if (dy <= extrusion && dy >= -extrusion) {
			
			// Get position Delta X
			int gap;
			if (parent.getOffX() + this.xMin > physics.parent.getOffX()
					+ physics.xMin) {
				gap = parent.getOffX() + this.xMin
						- (physics.parent.getOffX() + physics.xMax);
			} else {
				gap = physics.parent.getOffX() + physics.xMin
						- (parent.getOffX() + this.xMax);
			}
			
			
			// Check that we are strictly within X collision bounds
			if (gap <= 0) {
				DeveloperLog.print("Testing collision between " + parent.getClass().getSimpleName() + " and "
						+ physics.parent.getClass().getSimpleName() + " > x-intersection: " + gap + ", y-intrusion: " + dy);
				//Test all collisions
				for (int i = 0 ; i < collidableAreas.size(); i++) {
					Collidable collidableArea = collidableAreas.get(i);
					for (int j = 0; j < physics.collidableAreas.size(); j++) {
						Collidable collidableArea2 = physics.collidableAreas.get(j);
						if (collidableArea.collidesWith(collidableArea2)) {
							return true; // collision
						}
					}
				}
				return false;
			} else {
				return false; // Failed to be within x axis range
			}
		} else {
			return false; // Failed to be within y axis range
		}
	}

	/**
	 * Returns the collision event given by two colliding objects
	 * 
	 * @param object - the object which produces a collision event
	 * @return a collision event with the passed object
	 */
	public CollisionEvent getCollisionEvent(SATCollisionComponent physics) {
		List<CollisionEvent> collisionEvents = new LinkedList<>();
		for (int i = 0 ; i < collidableAreas.size(); i++) {
			Collidable collidableArea = collidableAreas.get(i);
			for (int j = 0; j < physics.collidableAreas.size(); j++) {
				Collidable collidableArea2 = physics.collidableAreas.get(j);
				if (collidableArea.collidesWith(collidableArea2)) {
					collisionEvents.add(collidableArea.getCollisionEvent(collidableArea2));
				}
			}
		}
		
		//TODO project all the force vectors onto eachother for a precise collision event
		double magnitude = 0;
		for (CollisionEvent collisionEvent : collisionEvents) {
			magnitude += collisionEvent.getPulseMagnitude();
		}
		
		return new CollisionEvent(physics.parent, 0, magnitude);
	}

	/** Get the magnitude of the hitbox on the y axis
	 * 
	 * @return the extrusion of the hitbox relative to the object's position
	 */
	public int getExtrusion() {
		return extrusion;
	}

	/** Set the magnitude of the hitbox on the y axis
	 * 
	 * @param extrusion - the extrusion of the hitbox relative to the object's position
	 */
	public void setExtrusion(int extrusion) {
		this.extrusion = extrusion;
	}
	
	public void setFloorOffset(int floorOffset) {
		this.yFloor = floorOffset;
	}
}
