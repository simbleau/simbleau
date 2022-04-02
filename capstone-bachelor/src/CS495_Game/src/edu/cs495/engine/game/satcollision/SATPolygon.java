package edu.cs495.engine.game.satcollision;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.game.AbstractPhysicalGameObject;
import edu.cs495.engine.game.satcollision.SATVector.Vector;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Box;
import edu.cs495.engine.gfx.obj.Line;

/** A polygon defined by a list of vectors. Polygons should be
 * added clockwise (as is convention). Otherwise, calculations will be 
 * incorrect because we evaluate left-normals for SAT Collision.
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class SATPolygon extends Collidable {
	
	/** The midpoint x value of the polygon */
	protected int xMid;
	
	/** The midpoint y value of the polygon */
	protected int yMid; 

	/** A list of vectors which encompass this polygon collision area */
	protected List<SATVector> vectors;
	
	/** Initialize a new SATPolygon
	 * 
	 * @param parent - the physical game object which has this collision polygon
	 */
	public SATPolygon(AbstractPhysicalGameObject parent) {
		super(parent);		
		this.xMid = 0;
		this.yMid = 0;
		this.vectors = new LinkedList<>();
	}
	
	/** Render the collision area for debugging */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
			for (int i = 0; i < vectors.size(); i++) {
				SATVector vector = vectors.get(i);
				Line line = new Line(vector.edge.dx, vector.edge.dy, 0xcf00ffff);
				renderer.draw(line, Integer.MAX_VALUE, 
						parent.getOffX() + vector.edge.vertex1.x, 
						parent.getOffY() + vector.edge.vertex1.y);
				Line normal = new Line((int) (5 * vector.edgeNormal.dx / vector.edgeNormal.magnitude), (int) (5 * vector.edgeNormal.dy / vector.edgeNormal.magnitude), 0xcf00ff00);
				renderer.draw(normal, Integer.MAX_VALUE, 
						parent.getOffX() + vector.edge.vertex1.x + (vector.edge.dx / 2), 
						parent.getOffY() + vector.edge.vertex1.y + (vector.edge.dy / 2));
			}
			renderer.draw(new Box(3,3, 0xff00ffff, false), Integer.MAX_VALUE, parent.getOffX() + (int) xMid - 1, parent.getOffY() + (int) yMid - 1);
	}
	
	/** Check if this SAT polygon collides with another shape
	 * 
	 * @returns true if there is collision, false otherwise
	 */
	@Override
	public boolean collidesWith(Collidable object) {
		if (object instanceof SATPolygon) {
			//Cast polygon
			SATPolygon polygon = (SATPolygon) object;
			
			//Loop through our polygon's vectors and check for collision
			for (int i = 0; i < this.vectors.size(); i++) {
				SATVector vector = this.vectors.get(i);
				
				//If the polygon doesn't overlap the vector's collision axis, there is no collision
				if (!vector.overlaps(polygon)) {
					return false;
				}
			}
			
			return true;
		} else if (object instanceof SATCircle) {
			//Cast circle
			SATCircle circle = (SATCircle) object;
			
			//Circle cx, cy
			int worldCx = circle.parent.getOffX() + circle.cx;
			int worldCy = circle.parent.getOffY() + circle.cy;
			
			int rayIntersections = 0;
			for (int i = 0; i < vectors.size(); i++) {
				SATVector vector = vectors.get(i);
			
				//Get the displacement from each vertex to the center of the circle.
				//Vertex 1
				int v1X = parent.getOffX() + vector.edge.vertex1.x;
				int v1Y = parent.getOffY() + vector.edge.vertex1.y;
				Vector v1Displacement = new Vector(
						v1X,
						v1Y,
						worldCx, 
						worldCy
						);
				if (v1Displacement.magnitude < circle.radius) {
					return true;
				}
				
				//Vertex 2
				int v2X = parent.getOffX() + vector.edge.vertex2.x;
				int v2Y = parent.getOffY() + vector.edge.vertex2.y;
				Vector v2Displacement = new Vector(
						v2X,
						v2Y,
						worldCx, 
						worldCy
						);
				if (v2Displacement.magnitude < circle.radius) {
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
		} else {
			//Unrecognized collidable object
			DeveloperLog.errLog("Could not intepret the collision between " 
			+ this.getClass().getSimpleName() + " and " + object.getClass().getSimpleName());
			return false;
		}
	}
	
	/** Return the collision event between the parent object and another collidable object
	 * 
	 * @return a collision event between parent and object
	 */
	@Override
	public CollisionEvent getCollisionEvent(Collidable object) {
		if (object instanceof SATPolygon) {
			//Collision logic between SATPolygon and SATPolygon
			SATPolygon polygon = (SATPolygon) object;
			
			boolean collided = true;
			for (int i = 0; i < this.vectors.size(); i++) {
				SATVector vector = this.vectors.get(i);
				if (!vector.overlaps(polygon)) {
					collided = false;
					break;
				}
			}
			
			if (collided) {
				return new CollisionEvent(object.parent, 0, 1);
			} else {
				return CollisionEvent.NO_COLLISION;
			}
		} else if (object instanceof SATCircle) {
			return new CollisionEvent(object.parent, 0, 1);
		} else {
			//Unrecognized collidable object
			DeveloperLog.errLog("Could not intepret the collision between " 
			+ this.getClass().getSimpleName() + " and " + object.getClass().getSimpleName());
			return CollisionEvent.NO_COLLISION;
		}
	}
	
	/** Add a vector to this polygon (vectors should be added CW)
	 * 
	 * @param x1 - start X
	 * @param y1 - start Y
	 * @param x2 - end X
	 * @param y2 - end Y
	 */
	public void addVector(int x1, int y1, int x2, int y2) {
		if (vectors.size() == 0) {
			this.xMin = Math.min(x1, x2);
			this.xMax = Math.max(x1, x2);
			this.yMin = Math.min(y1, y2);
			this.yMax = Math.max(y1, y2);
			this.xMid = (xMin + xMax) / 2;
			this.yMid = (yMin + yMax) / 2;
		} else {
			//x1
			if (x1 < xMin) {
				xMin = x1;
				this.xMid = (xMin + xMax) / 2;
			} else if (x1 > xMax) {
				xMax = x1;
				this.xMid = (xMin + xMax) / 2;
			}
			//y1
			if (y1 < yMin) {
				yMin = y1;
				this.yMid = (yMin + yMax) / 2;
			} else if (y1 > yMax) {
				yMax = y1;
				this.yMid = (yMin + yMax) / 2;
			}
			//x2
			if (x2 < xMin) {
				xMin = x2;
				this.xMid = (xMin + xMax) / 2;
			} else if (x2 > xMax) {
				xMax = x2;
				this.xMid = (xMin + xMax) / 2;
			}
			//y2
			if (y2 < yMin) {
				yMin = y2;
				this.yMid = (yMin + yMax) / 2;
			} else if (y2 > yMax) {
				yMax = y2;
				this.yMid = (yMin + yMax) / 2;
			}
		}
		
		vectors.add(new SATVector(this, x1, y1, x2, y2));
	}
	
}
