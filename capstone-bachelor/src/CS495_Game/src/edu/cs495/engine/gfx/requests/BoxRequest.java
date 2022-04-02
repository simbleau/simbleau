/**
 * 
 */
package edu.cs495.engine.gfx.requests;

import edu.cs495.engine.gfx.obj.Box;

/** This is an {@link AbstractRequest} which will encapsulate a {@link Box}
 * and where to draw it. This is sent to the renderer for processing.
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class BoxRequest extends AbstractRequest {
	
	/** The box being rendered */
	public final Box box;
	/** The x coordinate this object is being rendered at */
	public final int offX;
	/** The y coordinate this object is being rendered at */
	public final int offY;
	
	/** Initialize Box Request
	 * 
	 * @param box - the box to render
	 * @param offX - The x coordinate to render the box at
	 * @param offY - The y coordinate to render the box at
	 */
	public BoxRequest(Box box, int offX, int offY) {
		super(offY + box.getHeight());
		this.box = box;
		this.offX = offX;
		this.offY = offY;
	}
	
	/** Initialize Box Request with a special render order
	 * 
	 * @param box - the box to render
	 * @param renderOrder - a specific render ordering for this request
	 * @param offX - The x coordinate to render the box at
	 * @param offY - The y coordinate to render the box at
	 */
	public BoxRequest(Box box, int renderOrder, int offX, int offY) {
		super(renderOrder);
		this.box = box;
		this.offX = offX;
		this.offY = offY;
	}
	
}