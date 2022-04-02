package edu.cs495.engine.game.satcollision;


/** This class models the information needed to interpret
 * a vector as part of a polygon used in SAT Collision.
 * 
 * Inner classes exist such as {@link Vertex}, {@link Axis}, and {@link Vector}.
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class SATVector {
	
	/** A vertex is a point in space with an x coordinate and y coordinate.
	 * It holds a point.
	 * 
	 * @author Spencer Imbleau
	 * @version March 2019
	 */
	static class Vertex {
		/** The x value of the origin of this vector */
		public final int x;
		/** The y value of the origin of this vector */
		public final int y;
		
		/** Initialize a vertex
		 * 
		 * @param x - x coordinate of the vertex
		 * @param y - y coordinate of the vertex
		 */
		public Vertex(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	/** A vector is a displacement with direction and magnitude over two points, from vertex1 to vertex2
	 * 
	 * @author Spencer Imbleau
	 * @version March 2019
	 */
	static class Vector { 
		
		/** The start vertex in this vector (Obey clockwise convention!) */
		public final Vertex vertex1;
		
		/** The end vertex in this vector (Obey clockwise convention!) */
		public final Vertex vertex2;
		
		/** The displacement in the x direction */
		public final int dx;
		/** The displacement in the y direction */
		public final int dy;

		/** The magnitude of this vector */
		public double magnitude;
		
		/** Initialize a vector
		 * 
		 * @param x1 - starting local x coordinate
		 * @param y1 - starting local y coordinate
		 * @param x2 - ending local x coordinate
		 * @param y2 - ending local y coordinate
		 */
		public Vector(int x1, int y1, int x2, int y2) {
			this.vertex1 = new Vertex(x1, y1);
			this.vertex2 = new Vertex(x2, y2);
			
			this.dx = vertex2.x - vertex1.x;
			this.dy = vertex2.y - vertex1.y;
			
			this.magnitude = Math.sqrt(dx * dx + dy * dy);
		}
		
		/** Returns the normal vector of the {@link #edge}
		 * 
		 * @return the normal vector of the {@link #edge}
		 */
		protected Vector getNormal() {
			return new Vector(vertex1.y, vertex2.x, vertex2.y, vertex1.x); //Yields <dy, -dx>
		}
		
		/** Returns the axis of the {@link #edge}
		 * 
		 * @return the unit vector (axis) of the {@link #edge} (magnitude = 1)
		 */
		protected SATAxis getAxis() {
			return new SATAxis((dx / magnitude), (dy / magnitude));
		}
		
		/** Return the dot product (scalar) of this vector and another
		 * 
		 * @param vector2 - another vector to dot product with this
		 * @return the dot product of this and vector2
		 */
		public int calcDotProduct(Vector vector2) {
			return (this.dx * vector2.dx + this.dy * vector2.dy);
		}
	}
	
	/** The parent object with this collision area */
	protected SATPolygon parentPolygon;
	
	/** The this SAT Vector's displacement vector */
	protected Vector edge;
	
	/** The normal of the {@link #edge} */
	protected Vector edgeNormal;
	
	/** The unit vector of the {@link #normal} */
	protected SATAxis collisionAxis;

	/** Initialize the SAT Vector
	 * 
	 * @param x1 - The x value of the origin of this vector 
	 * @param y1 - The y value of the origin of this vector 
	 * @param x2 - The x value of the end of this vector 
	 * @param y2 - The y value of the end of this vector 
	 */
	public SATVector(SATPolygon parentPolygon, int x1, int y1, int x2, int y2) {
		this.parentPolygon = parentPolygon;

		this.edge = new Vector(x1, y1, x2, y2);
		this.edgeNormal = edge.getNormal();
		this.collisionAxis = edgeNormal.getAxis(); 
	}

	/** Returns whether this SAT vector overlaps another
	 * 
	 * @param vector2 - the vector to test for overlap
	 * @return true if the vectors overlap on this vector's normal axis, false otherwise
	 */
	public boolean overlaps(SATPolygon polygon) {
		//Calculate min and maximum projections onto the collision axis
		SATProjection thisProjection = this.collisionAxis.getProjection(this);
		SATProjection polyProjection = this.collisionAxis.getProjection(polygon);
		
		//Calculate the gap between the vectors
		if (polyProjection.min > thisProjection.min) {
			if (polyProjection.min - thisProjection.max < 0) {
				return true;
			} else {
				return false;
			}
		} else {
			if (thisProjection.min - polyProjection.max < 0) {
				return true;
			} else {
				return false;
			}
		}
	}
}
