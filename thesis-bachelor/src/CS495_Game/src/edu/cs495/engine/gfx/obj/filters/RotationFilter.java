/**
 * 
 */
package edu.cs495.engine.gfx.obj.filters;

import java.awt.Point;
import edu.cs495.engine.gfx.obj.Image;

/** A pixel rotation filter which can rotate an image around a point
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class RotationFilter extends AbstractFilter{
	
	/** An offset to convert negatives into positives for {@link #getAbsTheta()} */
	private static final double TWO_PI = Math.PI * 2;
	
	/** The angle of rotation in radians */
	private double theta;
	/** The X coordinate we are rotating around */
	private int anchorX;
	/** The Y coordinate we are rotating around */
	private int anchorY;
	
	/** Creates a rotation filter
	 * 
	 * @param image - The image which is being rotated
	 * @param degrees - The degrees to rotate the object 
	 * @param anchor - a point which is used to rotate the image about
	 */
	public RotationFilter(Image image, double theta, Point anchor) {
		super(image);
		setTheta(theta);
		setAnchor(anchor);
	}

	/** Initialize a blank rotation filter */
	public RotationFilter() {
		super();
		this.theta = Math.atan2(0, 0);
		this.anchorX = 0;
		this.anchorY = 0;
		
	}

	/** Rotate the pixels of a given image and return them
	 * Note: Image bounds are NOT modified, so if the image bounds cannot rotate
	 * the object fully within its own bounds, the filtered pixels will be
	 * clipped out of the image.
	 * 
	 * @param image - the Image to rotate
	 */
	@Override
	public int[] apply() {
		int filteredPixels[] = new int[image.getPixels().length];

		double sin = Math.sin(theta);
		double cos = Math.cos(theta);
		
		int width = image.getWidth();
		int height = image.getHeight();

		for (int y = 0; y < height; y++) {
			int yWidth = y * width;
			for (int x = 0; x < width; x++) {
				double a = x - anchorX;
				double b = y - anchorY;
				int xx = (int) (+a * cos - b * sin + anchorX);
				int yy = (int) (+a * sin + b * cos + anchorY);

				// Rotate pixels
				if (xx >= 0 && xx < width && yy >= 0 && yy < height) {
					filteredPixels[x + yWidth] = 
							image.getPixels()[xx + yy * width];
				}
			}
		}

		return filteredPixels;
	}
	
	
	/** Returns the angle theta of rotation (in radians) counter-clockwise
	 * 
	 * @return the rotation in degrees CCW
	 */
	public double getTheta() {
		return theta;
	}
	
	/** Returns theta + 2pi to always return a positive radian
	 * 
	 * @return the rotation in degrees CCW
	 */
	public double getAbsTheta() {
		return TWO_PI + theta;
	}

	/** Sets the rotation angle CCW to theta (in radians)
	 * 
	 * @param theta - the CCW rotation theta to set
	 */
	public void setTheta(double theta) {
		//Domain check
		if (theta > Math.PI) {
			this.theta = -Math.PI + (theta % Math.PI);
		} else if (theta < -Math.PI) {
			this.theta = Math.PI - (theta % Math.PI);
		} else {
			this.theta = theta;
		}
	}

	/** Sets a new anchor point to rotate the image around. If the point
	 * is out of bounds, it defaults to the center of the image.
	 * 
	 * @param anchor - a point which is used to rotate the image about
	 */
	public void setAnchor(Point anchor) {
		if (anchor.x < 0 || anchor.x > image.getWidth()) {
			anchorX = image.getHalfWidth();
			anchorY = image.getHalfHeight();
		} else {
			anchorX = anchor.x;
			anchorY = anchor.y;
		}
	}
	
}
