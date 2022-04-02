/**
 * 
 */
package edu.cs495.engine.gfx.obj.filters;

import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.gfx.obj.Image;

/** An alpha filter will allow the adjustment of an Image's opacity at runtime
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class AlphaFilter extends AbstractFilter{
	
	/** The percentage which represents fully opaque */
	private static final int OPAQUE = 100;
	
	/** The percentage which represents fully opaque */
	private static final int TRANSPARENT = 0;
	
	/** A percentage of alpha transparency for the {@link AbstractFilter#image} */
	private int alphaPercent;
	
	/** Creates an alpha filter with a supplied image
	 * 
	 * @param image - The image to apply the filter to
	 */
	public AlphaFilter(Image image) {
		super(image);
	}
	
	/** Creates an alpha channel filter with no image */
	public AlphaFilter() {
		super();
	}

	/** Sets the alpha level of the image
	 * 
	 * @param alphaPercent - a new alpha value, as a percentage
	 */
	public void setAlphaPercent(int alphaPercent) {
		if (alphaPercent >= TRANSPARENT && alphaPercent <= OPAQUE) {
			this.alphaPercent = alphaPercent; 
		} else {
			DeveloperLog.errLog("Alpha percentage illegal: " 
					+ alphaPercent + "%");
		}
	}
	 
	/** Returns the alpha percentage for the filter
	 * 
	 * @return the alpha percent this filter is using
	 */
	public int getAlphaPercent() {
		return alphaPercent;
	}


	/** Apply a percentage of alpha to every pixel
	 * 
	 * @return the filtered pixel data
	 */
	@Override
	public int[] apply() {
		//Check if fully opqaue/transparent
		if (alphaPercent == OPAQUE) {
			return image.getPixels(); //Return the original image
		} else if (alphaPercent == TRANSPARENT) {
			return new int[image.getPixels().length]; //New array of 0s
		}
		
		//Apply filter
		int[] newPixels = new int[image.getPixels().length];

		for (int y = 0; y < image.getHeight(); y++) {
			int yWidth = y * image.getWidth();
			for (int x = 0; x < image.getWidth(); x++) {
				
				int pixelColor = image.getPixels()[x + yWidth];
				int pixelRGB = pixelColor & 0xffffff;
				
				int pixelAlpha = (pixelColor >> 24 ) & 0xff;
				int newAlpha = ((int)(pixelAlpha * (alphaPercent / 100f)) << 24);
				
				newPixels[x + yWidth] = 
							(newAlpha | pixelRGB);
			}
		}
		
		return newPixels;
	}
}
