/**
 * 
 */
package edu.cs495.engine.gfx.requests;

import edu.cs495.engine.gfx.obj.Image;

/** This is an {@link AbstractRequest} which will encapsulate an {@link Image}
 * and where to draw it. This is displayed relative to the screen, not the
 * camera. Thus, an Overlay is a "HUD" element. i.e. screen element.
 * 
 * Overlay requests will be rendered ontop of all other requests.
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class OverlayRequest extends AbstractRequest{
	
	/** The image being overlayed */
	public final Image image;
	/** The x coordinate, relative to the screen, for rendering */
	public final int x;
	/** The y coordinate, relative to the screen, for rendering */
	public final int y;
	
	/** Initializes an overlay image request 
	 * 
	 * @param image - the image to overlay the screen
	 * @param renderOrder - the order of rendering for this request 
	 * (note: Only applies when comparing overlay requests, as overlays always
	 * render ontop of all game rendering requests by default)
	 * @param x - the X coordinate this is rendered at on the screen
	 * @param y - the Y coordinate this is rendered at on the screen */
	public OverlayRequest(Image image, int renderOrder, int x, int y) {
		super(renderOrder);
		this.image = image;
		this.x = x;
		this.y = y;
	}

	
}
