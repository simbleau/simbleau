/**
 * 
 */
package edu.cs495.engine.gfx.obj.filters;

import edu.cs495.engine.gfx.obj.Image;

/** A color replacement filter will change one color to another.
 * 
 * @version February 2019
 * @author Spencer
 *
 */
public class ColorReplacementFilter extends AbstractFilter{
	
	/** The transparent alpha value */
	private static final int TRANSPARENT = 0x0;
	
	/** The overlayed color */
	protected int rgb;
	
	/** Creates a color replacement filter with an image
	 * 
	 * @param image - The image to apply the filter to
	 */
	public ColorReplacementFilter(Image image) {
		super(image);
		this.rgb = 0xffffff;
	}
	
	/** Creates a color replacement filter with a null image */
	public ColorReplacementFilter() {
		super();
		this.rgb = 0xffffff;
	}

	/** Adds a color swap key,value pair which does not allow duplicates.
	 * Values passed must be RGB and not contain alpha. There is no parameter
	 * checking and values are assumed to be correct.
	 * 
	 * @param beforeRgb - an RGB color swap key
	 * @param afterRgb - an RGB color swap value
	 */
	public void setOverlayColor(int rgb) {
		this.rgb = rgb;
	}


	/** Return the color of the overlay
	 * @return the color
	 */
	public int getOverlayColor() {
		return rgb;
	}

	/** Recolor the image using the listed {@link #colorSwaps}
	 * 
	 * @return the filtered pixel data
	 */
	@Override
	public int[] apply() {
		int[] newPixels = new int[image.getPixels().length];

		for (int y = 0; y < image.getHeight(); y++) {
			int yWidth = y * image.getWidth();
			for (int x = 0; x < image.getWidth(); x++) {
				
				int pixelColor = image.getPixels()[x + yWidth];
				int pixelAlpha = pixelColor & 0xff000000;
				
				if (pixelAlpha != TRANSPARENT) {
					newPixels[x + yWidth] = (pixelAlpha | rgb);
				}
				
			}
		}
		
		return newPixels;
	}
}
