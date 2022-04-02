/**
 * 
 */
package edu.cs495.engine.gfx.requests;

import edu.cs495.engine.gfx.obj.Image;

/** This is an {@link AbstractRequest} which will encapsulate an {@link Image}
 * and where to draw it. This is sent to the renderer for processing.
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class ImageRequest extends AbstractRequest {
	
	/** The image being rendered */
	public final Image image;
	/** The x coordinate this object is being rendered at */
	public final int offX;
	/** The y coordinate this object is being rendered at */
	public final int offY;
	
	/** Initialize Image Request
	 * @param image : The image to render
	 * @param offX : The x coordinate to render image at
	 * @param offY : The y coordinate to render image at
	 */
	public ImageRequest(Image image, int offX, int offY) {
		super(offY + image.getHeight());
		this.image = image;
		this.offX = offX;
		this.offY = offY;
	}

	/** Initialize Image Request with a specific render order
	 * @param image : The image to render
	 * @param renderOrder : a specific render ordering for this request
	 * @param offX : The x coordinate to render image at
	 * @param offY : The y coordinate to render image at
	 */
	public ImageRequest(Image image, int renderOrder, int offX, int offY) {
		super(renderOrder);
		this.image = image;
		this.offX = offX;
		this.offY = offY;
	}
	
}