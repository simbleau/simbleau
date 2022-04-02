/**
 * 
 */
package edu.cs495.engine.gfx.requests;

import edu.cs495.engine.gfx.obj.Light;

/** This is an {@link AbstractRequest} which will encapsulate a {@link Light}
 * and where to draw it. This is sent to the renderer for processing.
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class LightRequest extends AbstractRequest{

	/** The x coordinate the light is to be rendered at (center of the light) */
	public int offX;
	/** The y coordinate the light is to be rendered at (center of the light) */
	public int offY;
	
	/* The light object itself */
	public Light light;
	
	/** A light request which obeys normal render ordering procedure (y-depth)
	 * @param light : The light
	 * @param offX : The position x on the game canvas
	 * @param offY : The position y on the game canvas
	 */
	public LightRequest(Light light, int offX, int offY) {
		super(offY + light.diameter);
		this.light = light;
		this.offX = offX;
		this.offY = offY;
	}
	
	/** A light request with a specified render order
	 * @param light : The light
	 * @param renderOrder : The order this will be rendered in
	 * @param offX : The position x on the game canvas
	 * @param offY : The position y on the game canvas
	 */
	public LightRequest(Light light, int renderOrder, int offX, int offY) {
		super(renderOrder);
		this.light = light;
		this.offX = offX;
		this.offY = offY;
	}
	
}
