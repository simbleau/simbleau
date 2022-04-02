/**
 * 
 */
package edu.cs495.engine.gfx.obj;

/** A graphics object which contains information describing an ellipse
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class Ellipse implements Renderable  {
	
	/** The width of the ellipse */
	private int width;
	/** The height of the ellipse */
	private int height;
	/** The color of the ellipse, in ARGB format */
	private int argb;
	/** True if the ellipse is full, false if wire-frame */
	private boolean full;

	/** Initializes an ellipse graphics object
	 * 
	 * @param width - the width of the ellipse
	 * @param height - the height of the ellipse
	 * @param argb - the color, in ARGB format, of the ellipse
	 * @param full - true if full, false if wire-frame
	 */
	public Ellipse(int width, int height, int argb, boolean full) {
		this.width = width;
		this.height = height;
		this.argb = argb;
		this.full = full;
	}

	
	/** Return the width of the ellipse
	 * 
	 * @return the width of the ellipse 
	 */
	public int getWidth() {
		return width;
	}

	/** Sets a new width for the ellipse
	 * 
	 * @param width - the width to set the ellipse
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/** Return the height of the ellipse
	 * 
	 *  @return the height of the ellipse 
	 */
	public int getHeight() {
		return height;
	}

	/** Sets a new height for the ellipse
	 * 
	 * @param height - the height to set the ellipse
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/** Return the ARGB color of the ellipse
	 * 
	 * @return the argb color of the ellipse 
	 */
	public int getArgb() {
		return argb;
	}

	/** Sets the color of the ellipse
	 * 
	 * @param argb - the argb color to set the ellipse
	 */
	public void setArgb(int argb) {
		this.argb = argb;
	}

	/** Return if the ellipse is full
	 * 
	 *  @return true if the ellipse is full, false if hollow
	 */
	public boolean isFull() {
		return full;
	}

	/** Sets the new state of fullness for the ellipse
	 * 
	 * @param full - true if you want the ellipse to be full, false for hollow
	 */
	public void setFull(boolean full) {
		this.full = full;
	}
}
