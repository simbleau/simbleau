package edu.cs495.engine.game.satcollision;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.game.AbstractPhysicalGameObject;
import edu.cs495.engine.game.satcollision.SATVector.Vector;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Ellipse;
import edu.cs495.game.objects.player.menus.InventoryMenu;

/** This class models the data requires to make SAT physics calculations
 * for circles
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class SATCircle extends Collidable {
	
	/** The radius of this circle */
	protected int radius;
	
	/** The center x of this circle */
	protected int cx; 
	
	/** The center y of this circle */
	protected int cy;
	
	/** Initialize this SAT Circle collision area
	 * 
	 * @param radius - the radius for this circle
	 * @param cx - the center x
	 * @param cy - the center y
	 */
	public SATCircle(AbstractPhysicalGameObject parent, int radius, int cx, int cy) {
		super(parent);
		
		this.radius = radius;
		this.cx = cx;
		this.cy = cy;
		
		this.xMin = cx - radius;
		this.xMax = cx + radius;
		this.yMin = cy - radius;
		this.yMax = cy + radius;	
	}
	
	/** Render the collision area for debugging */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
			renderer.draw(new Ellipse(radius, radius, 0xcf00ffff, true), Integer.MAX_VALUE,
					parent.getOffX() + cx - (radius / 2), parent.getOffY() + cy);
	}
	
	/** Determine if this circle collides with the object passed
	 *
	 * @param object - a collidable object which could collide with this circle
	 * @returns true on collision, false otherwise
	 */
	@Override
	public boolean collidesWith(Collidable object) {
		if (object instanceof SATPolygon) {
			//Cast polygon
			SATPolygon polygon = (SATPolygon) object;
			
			//Center of circle
			int worldCx = parent.getOffX() + cx;
			int worldCy = parent.getOffY() + cy;
			
			int rayIntersections = 0;
			for (int i = 0; i < polygon.vectors.size(); i++) {
				SATVector vector = polygon.vectors.get(i);
			 
				//Get the displacement from each vertex to the center of the circle.
				//Vertex 1
				int v1X = polygon.parent.getOffX() + vector.edge.vertex1.x;
				int v1Y = polygon.parent.getOffY() + vector.edge.vertex1.y;
				Vector v1Displacement = new Vector(
						v1X,
						v1Y,
						worldCx, 
						worldCy
						);
				if (v1Displacement.magnitude < radius) {
					return true;
				}
				
				//Vertex 2
				int v2X = polygon.parent.getOffX() + vector.edge.vertex2.x;
				int v2Y = polygon.parent.getOffY() + vector.edge.vertex2.y;
				Vector v2Displacement = new Vector(
						v2X,
						v2Y,
						worldCx, 
						worldCy
						);
				if (v2Displacement.magnitude < radius) {
					return true;
				}
				
				//Calc ray intersections
				if (worldCx >= v1X && worldCx <= v2X && 
						(worldCy >= (v1Y + v2Y) / 2)) { 
					rayIntersections++;
				}
			}
			if (rayIntersections % 2 == 1) {
				return true;
			} else {
				return false;
			}
		} else if (object instanceof SATCircle) {
			//Cast circle
			SATCircle circle = (SATCircle) object;
			
			//Obtain distance between midpoints
			int dx = (parent.getOffX() + this.cx) - (object.getParent().getOffX() + circle.cx);
			int dy = (parent.getOffY() + this.cy) - (object.getParent().getOffY() + circle.cy); 
			double distance = Math.sqrt(dx * dx + dy * dy);
			
			//Check for collision by calculating their intersection
			if (distance - radius - circle.radius < 0) {
				return true;
			} else {
				return false;
			}
		} else {
			//Unrecognized collidable object
			DeveloperLog.errLog("Could not intepret the collision between " 
			+ this.getClass().getSimpleName() + " and " + object.getClass().getSimpleName());
			return false;
		}
	}


	/** Return the collision event between a parent with this collision area and another object
	 * 
	 * @param parent - the parent of this collision area
	 * @param object - the object to test
	 * @return a collision event between parent and object
	 * @see CollisionEvent
	 */
	@Override
	public CollisionEvent getCollisionEvent(Collidable object) {
		if (object instanceof SATPolygon) {
			return new CollisionEvent(object.parent, 0, 1);
		} else if (object instanceof SATCircle) {
			SATCircle circle = (SATCircle) object;
			int dx = cx - circle.cx;
			int dy = cy - circle.cy;
			double distance = Math.sqrt(dx * dx + dy * dy);
			
			double intersectionLength = distance - radius - circle.radius;
					
			CollisionEvent collision = new CollisionEvent(parent, 1, intersectionLength);
			return collision;
		} else {
			//Unrecognized collidable object
			DeveloperLog.errLog("Could not intepret the collision event between " 
			+ this.getClass().getSimpleName() + " and " + object.getClass().getSimpleName());
			return CollisionEvent.NO_COLLISION;
		}
	}

	
	/** Return this SAT circle's radius
	 * 
	 * @return the radius of the collision circle
	 */
	public int getRadius() {
		return radius;
	}

	/** Set the radius of the SATCircle and adjust the bounds of collision
	 * 
	 * @param radius - the radius of the new SAT collision zone
	 */
	public void setRadius(int radius) {
		this.radius = radius;
		this.yMin = cy - radius;
		this.yMax = cy + radius;
		this.xMin = cx - radius;
		this.xMax = cx + radius;
	}

	/** Return the center of the collidable area's x position
	 * 
	 * @return the center of the collidable area's x position
	 */
	public int getCx() {
		return cx;
	}
	
	/** Return the center of the collidable area's y position
	 * 
	 * @return the center of the collidable area's y position
	 */
	public int getCy() {
		return cy;
	}

	/** Set the new center of this collidable area's x position
	 * 
	 * @param cx - the new center x
	 */
	public void setCx(int cx) {
		this.cx = cx;
		if (cx - radius < xMin) {
			this.xMin = cx - radius;
		}
		if (cx + radius > xMax) {
			this.xMax = cx + radius;
		}
	}

	/** Set the new center of this collidable area's y position
	 * 
	 * @param cx - the new center y
	 */
	public void setCy(int cy) {
		this.cy = cy;
		if (cy - radius < yMin) {
			this.yMin = cy - radius;
		}
		if (cy + radius > yMax) {
			this.yMax = cy + radius;
		}
	}

}
