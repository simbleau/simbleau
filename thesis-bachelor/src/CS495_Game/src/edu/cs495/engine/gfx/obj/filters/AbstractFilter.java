/**
 * 
 */
package edu.cs495.engine.gfx.obj.filters;

import edu.cs495.engine.gfx.obj.Image;

/** An abstract image filter. Filters are applied to {@link Image}s which
 * will change the literal pixels of the image at runtime. If you desire to 
 * filter a sprite in a sprite sheet, you'll need to convert the specific sprite 
 * to the {@link Image} type and pass it to {@link #setImage(Image)}
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public abstract class AbstractFilter {
	
	/** A reference to the Image to apply the filter to */
	protected Image image;
	
	/** Initializes a filter object */
	public AbstractFilter() {
		this.image = null;
	}
	
	/** Initializes a filter object with a target image
	 * 
	 * @param image - The image to apply the filter to
	 */
	public AbstractFilter(Image image) {
		this.image = image;
	}
	

	/** Sets the image that receives filtering
	 * 
	 * @param image - the image to filter
	 */
	public void setImage(Image image) {
		this.image = image;
	}


	/** Apply a filter to the {@link #image}
	 * 
	 * @return a filtered array of pixel data
	 */
	public abstract int[] apply();
}

