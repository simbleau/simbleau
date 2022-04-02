/**
 * 
 */
package edu.cs495.engine.game.satcollision;


/** Axis represents a unit vector involved in SAT collision
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class SATAxis {
	/** The x displacement */
	public double dx;
	
	/** The y displacement */
	public double dy;
	
	/** Return the scalar projection of a point onto this axis
	 * 
	 * @param x - the x coordinate
	 * @param y - the y coordinate
	 * @return the scalar projecton of a point onto this axis
	 */
	public double getScalarProjection(int x, int y) {
		// This returns the scalar projection of a vertex onto this axis
		// Since an axis is a unit vector, there is no need to divide by the magnitude
		return (this.dx * x) + (this.dy * y);
	}
	
	
	/** Initialize an axis with magnitude = 1
	 * 
	 * @param dx - the x coordinate displacement
	 * @param dy - the y coordinate displacement
	 */
	public SATAxis(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	/** Return the {@link SATProjection} received from projecting a line from two points onto this axis
	 * 
	 * @param x1 - start x
	 * @param y1 - start y
	 * @param x2 - end x
	 * @param y2 - end y
	 * @return the {@link SATProjection} received from projecting a line from two points onto this axis
	 */
	public SATProjection getProjection(int x1, int y1, int x2, int y2) { 
		double v1Projection = getScalarProjection(x1, y1);
		double v2Projection = getScalarProjection(x2, y2);
		 
		if (v1Projection < v2Projection) {
			return new SATProjection(v1Projection, v2Projection);
		} else {
			return new SATProjection(v2Projection, v1Projection);
		}
	}
	
	/** Return the {@link SATProjection} received from projecting a {@link SATVector} onto this axis
	 * 
	 * @param vector - the SATVector to project onto this axis
	 * @return the {@link SATProjection} received from projecting a {@link SATVector} onto this axis
	 */
	public SATProjection getProjection(SATVector vector) {
		return getProjection(
				vector.parentPolygon.parent.getOffX() + vector.edge.vertex1.x,
				vector.parentPolygon.parent.getOffY() + vector.edge.vertex1.y,
				vector.parentPolygon.parent.getOffX() + vector.edge.vertex2.x,
				vector.parentPolygon.parent.getOffY() + vector.edge.vertex2.y);
	}
	
	/** Return the {@link SATProjection} received from projecting a polygon onto this axis
	 * 
	 * @param polygon - the polygon to project onto this axis
	 * @return the {@link SATProjection} received from projecting a vector onto this axis
	 */
	public SATProjection getProjection (SATPolygon polygon) {
		double minProjection = 0;
		double maxProjection = 0;
		for (int i = 0; i < polygon.vectors.size(); i++) {
			SATVector vector = polygon.vectors.get(i);
			double v1Projection = getScalarProjection(
					polygon.parent.getOffX() + vector.edge.vertex1.x,
					polygon.parent.getOffY() + vector.edge.vertex1.y);
			double v2Projection = getScalarProjection(
					polygon.parent.getOffX() + vector.edge.vertex2.x,
					polygon.parent.getOffY() + vector.edge.vertex2.y);

			if (i == 0) {
				//Assign min/max
				if (v1Projection < v2Projection) {
					minProjection = v1Projection;
					maxProjection = v2Projection;
				} else {
					minProjection = v2Projection;
					maxProjection = v1Projection;
				}
			} else {
				//Update min/max
				if (v1Projection < minProjection) {
					minProjection = v1Projection;
				} else if (v1Projection > maxProjection) {
					maxProjection = v1Projection;
				}
				if (v2Projection < minProjection) {
					minProjection = v2Projection;
				} else if (v2Projection > maxProjection) {
					maxProjection = v2Projection;
				}
			}
		}
		
		return new SATProjection(minProjection, maxProjection);
	}

}
