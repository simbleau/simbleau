/**
 * 
 */
package edu.cs495.engine.gfx.obj;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import edu.cs495.engine.developer.DeveloperLog;


/** An Image graphics object
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class Image implements Renderable {

	/** The width of the image */
	protected int width;
	/** The height of the object */
	protected int height;
	/** The pixel data of this image. int elements are in ARGB format */
	protected int[] clampedPixels;
	
	/** Initializes an image based on passed values
	 * 
	 * @param width - width of the image
	 * @param height - height of the image
	 * @param clampedPixels - the pixels of the new image
	 */
	public Image(int width, int height, int[] clampedPixels) {
		this.width = width;
		this.height = height;
		this.clampedPixels = clampedPixels;
	}
	
	/** Creates an Image by loading in pixel data from a file path
	 * 
	 * @param path - the path to the image
	 */
	public Image(String path) {
		BufferedImage image = null;
		
		try {
			DeveloperLog.printLog("Loading " + path + "...");
			image = ImageIO.read(Image.class.getResourceAsStream(path));
			this.width = image.getWidth();
			this.height = image.getHeight();
			this.clampedPixels = image.getRGB(
					0, //StartX
					0, //StartY
					this.width, //Width
					this.height, //Height
					null, //RGB Array
					0, //Offset
					this.width); //Scan Size	
			image.flush();
		} catch (IOException e) {
			DeveloperLog.printLog(e.getStackTrace().toString());
			this.width = 0;
			this.height = 0;
			this.clampedPixels = null;
			return;
		}
		
		/* TODO Check if image is null and do give feedback to the user if it
		* doesn't exist
		* Currently, I'd prefer this to crash the program. */
	}

	/** Return the width of the image
	 * 
	 * @return the width 
	 */
	public int getWidth() {
		return width;
	}

	/** Return half of the width
	 * 
	 *  @return the half width of the image (for centering calculations) 
	 */
	public int getHalfWidth() {
		return width / 2;
	}

	/** Sets a new image width
	 * 
	 * @param width - the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/** Return the height of the image
	 * 
	 * @return the height of the image 
	 */
	public int getHeight() {
		return height;
	}

	/** Return half of the height
	 * 
	 *  @return the half height of the image (for centering calculation 
	 */
	public int getHalfHeight() {
		return height / 2;
	}
	
	/** Sets a new image height
	 * 
	 * @param height - the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/** Return the pixel data of this image
	 * 
	 * @return the pixel data array of this image 
	 */
	public int[] getPixels() {
		return clampedPixels;
	}

	/** Writes over the current pixel data with new pixel data
	 * 
	 * @param pixelData - the new pixel data for this image
	 */
	public void setPixelData(int[] pixelData) {
		this.clampedPixels = pixelData;
	}

	
}
