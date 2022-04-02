/**
 * 
 */
package edu.cs495.engine.gfx.obj;

/** An graphics object which contains information describing a box for rendering
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class Box implements Renderable, Overlayable {
	
	/** The width of the box */
	private int width;
	/** The height of the box */
	private int height;
	/** The color of the box, in ARGB format */
	private int argb;
	/** The state of fullness of the box (full == true, hollow == false) */
	private boolean full;
	
	/** Initializes a box graphics object
	 * 
	 * @param width - width of the box
	 * @param height - height of the box
	 * @param argb - the color (ARGB format) of the box
	 * @param full - true if full, false if hollow
	 */
	public Box(int width, int height, int argb, boolean full) {
		this.width = width;
		this.height = height;
		this.argb = argb;
		this.full = full;
	}
	
	/** Returns the box as an Image type. This is NOT recommended for hollow
	 * boxes as it's horribly inefficient. For hollow boxes use 
	 * {@link BoxRequest} for efficiency.
	 * 
	 * @return An image containing the box's pixels
	 * @see BoxRequest
	 */
	public Image getImageFormat() {
		int[] newPixels = new int[width * height];	
		
		if (full) {
			//Full rectangle
			for (int y = 0; y < height; y++) {
				int yWidth = y * width;
				for (int x = 0; x < width; x++) {
					newPixels[x + yWidth] =  argb;
				}
			}
		} else {
			//Hollow rectangle
			int xOffset = width - 1;
			for (int y = 0; y < height; y++) {
				int yStart = y * width;
				newPixels[yStart] = argb;
				newPixels[yStart + xOffset] = argb;
			}
			int yFloor = (Math.max(0, height - 1)) * width;
			for (int x = 0; x < width; x++) {
				newPixels[x] = argb;
				newPixels[yFloor + x] = argb;
			}
		}
			
		return new Image(width, height, newPixels);
	}

	/** Return the width of the box
	 * 
	 * @return the width of the box 
	 */
	public int getWidth() {
		return width;
	}

	/** Sets a new width for the box
	 * 
	 * @param width - the width to set the box
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/** Return the height of the box
	 * 
	 *  @return the height of the box 
	 */
	public int getHeight() {
		return height;
	}

	/** Sets a new height for the box
	 * 
	 * @param height - the height to set the box
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/** Return the ARGB color of the box
	 * 
	 * @return the argb color of the box 
	 */
	public int getArgb() {
		return argb;
	}

	/** Sets the color of the box
	 * 
	 * @param argb - the argb color to set the box
	 */
	public void setArgb(int argb) {
		this.argb = argb;
	}

	/** Return if the box is full
	 * 
	 *  @return true if the box is full, false if hollow
	 */
	public boolean isFull() {
		return full;
	}

	/** Sets the new state of fullness for the box
	 * 
	 * @param full - true if you want the box to be full, false for hollow
	 */
	public void setFull(boolean full) {
		this.full = full;
	}

}
