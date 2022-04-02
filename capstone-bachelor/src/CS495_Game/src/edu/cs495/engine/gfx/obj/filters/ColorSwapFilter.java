/**
 * 
 */
package edu.cs495.engine.gfx.obj.filters;

import java.util.HashMap;

import edu.cs495.engine.gfx.obj.Image;

/** A color replacement filter will change several colors to other colors.
 * 
 * @version February 2019
 * @author Spencer
 *
 */
public class ColorSwapFilter extends AbstractFilter{
	
	/** A list of RGB integer color swaps (Before, After) */
	protected HashMap<Integer, Integer> colorSwaps = new HashMap<>();
	
	/** Creates a color replacement filter with an image
	 * 
	 * @param image - The image to apply the filter to
	 */
	public ColorSwapFilter(Image image) {
		super(image);
	}
	
	/** Creates a color replacement filter with a null image */
	public ColorSwapFilter() {
		super();
	}

	/** Adds a color swap key,value pair which does not allow duplicates.
	 * Values passed must be RGB and not contain alpha. There is no parameter
	 * checking and values are assumed to be correct.
	 * 
	 * @param beforeRgb - an RGB color swap key
	 * @param afterRgb - an RGB color swap value
	 */
	public void addColorSwap(int beforeRgb, int afterRgb) {
		colorSwaps.put(beforeRgb, afterRgb);
	}
	 
	/** Removes a color swap key,value pair
	 * @param rgb - The RGB key to remove
	 */
	public void removeColorSwap(int rgb) {
		colorSwaps.remove(rgb);
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
				int pixelRGB = pixelColor & 0x00ffffff;
				
				if (colorSwaps.containsKey(pixelRGB)) {
						newPixels[x + yWidth] = 
								(pixelAlpha | colorSwaps.get(pixelRGB));
				} else {
					newPixels[x + yWidth] = image.getPixels()[x + yWidth];
				};
			}
		}
		
		return newPixels;
	}
}
