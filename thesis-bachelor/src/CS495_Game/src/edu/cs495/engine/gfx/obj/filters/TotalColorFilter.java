package edu.cs495.engine.gfx.obj.filters;

import edu.cs495.engine.gfx.obj.Image;

/** A color replacement filter will make the entire image a new color
 * 
 * @version February 2019
 * @author Spencer Imbleau
 *
 */
public class TotalColorFilter extends AbstractFilter{
	
	private int newColor;
	
	/** Creates a color replacement filter with an image
	 * 
	 * @param image - The image to apply the filter to
	 */
	public TotalColorFilter(Image image) {
		super(image);
	}
	
	/** Creates a color replacement filter with a null image */
	public TotalColorFilter() {
		super();
	}

	/** Get the color for replacement
	 * 
	 * @returns the RGB color for color replacement
	 */
	public int getColor() {
		return this.newColor;
	}

	/** Choose the new color for replacement
	 * 
	 * @param rgb - the new RGB color for color replacement
	 */
	public void setColor(int rgb) {
		this.newColor = rgb;
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
				
				newPixels[x + yWidth] = (pixelAlpha | newColor);
			}
		}
		
		return newPixels;
	}
}
