package edu.cs495.engine.gfx.obj;

/** An graphics object which contains information describing a line for rendering
 * 
 * @version March 2019
 * @author Spencer Imbleau
 */
public class Line implements Renderable, Overlayable {
	
	/** The delta y for this line  */
	private int dx;
	/** The delta y for this line */
	private int dy;
	/** The length of this line */
	private double length;
	
	/** The color of the box, in ARGB format */
	private int argb;
	
	/** Initializes a line graphics object
	 * 
	 * @param dx - the run of this line
	 * @param dy - the rise of this line
	 * @param argb - the color (ARGB format) of the line
	 */
	public Line(int dx, int dy, int argb) {
		this.dx = dx;
		this.dy = dy;
		this.length = Math.sqrt(dx * dx + dy * dy);
		this.argb = argb;
	}

	/** Return the run magnitude of the line
	 * 
	 * @return the run magnitude of the line
	 */
	public int getDx() {
		return this.dx;
	}

	/** Set the run magnitude of the line
	 * 
	 * @param dx - the new x length
	 */
	public void setDx(int dx) {
		this.dx= dx;
		this.length = Math.sqrt(dx * dx + dy * dy);
	}

	/** Return the rise magnitude of the line
	 * 
	 * @return the rise magnitude of the line
	 */
	public int getDy() {
		return this.dy;
	}

	/** Set the rise magnitude of the line
	 * 
	 * @param du - the new u length
	 */
	public void setDy(int dy) {
		this.dy = dy;
		this.length = Math.sqrt(dx * dx + dy * dy);
	}

	/** Return the argb color of the line
	 * 
	 * @return the argb color of the line
	 */
	public int getArgb() {
		return argb;
	}
	
	/** Set the color of this line
	 * 
	 * @param argb - an ARGB color code
	 */
	public void setArgb(int argb) {
		this.argb = argb;
	}

	/** Return the length of this line
	 * 
	 * @return the length of the line
	 */
	public double getLength() {
		return length;
	}
	
	

	
}
