/**
 * 
 */
package edu.cs495.engine.gfx.requests;

import edu.cs495.engine.gfx.obj.Ellipse;

/** This is an {@link AbstractRequest} which will encapsulate an {@link Ellipse}
 * and where to draw it. This is sent to the renderer for processing.
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class EllipseRequest extends AbstractRequest {
	
	/** The ellipse being rendered */
	public final Ellipse ellipse;
	/** The x coordinate this object is being rendered at */
	public final int offX;
	/** The y coordinate this object is being rendered at */
	public final int offY;
	
	/** Initialize Ellipse Request
	 * @param ellipse - the ellipse to render
	 * @param offY - The x coordinate this object is being rendered at
	 * @param offY - The y coordinate this object is being rendered at
	 */
	public EllipseRequest(Ellipse ellipse, int offX, int offY) {
		super(offY + ellipse.getHeight());
		this.ellipse = ellipse;
		this.offX = offX;
		this.offY = offY;
	}
	
	/** Initialize Ellipse Request with a specific render order
	 * @param ellipse - the ellipse to render
	 * @param renderOrder : a specific render ordering for this request
	 * @param offY - The x coordinate this object is being rendered at
	 * @param offY - The y coordinate this object is being rendered at
	 */
	public EllipseRequest(Ellipse ellipse, int renderOrder, 
			int offX, int offY) {
		super(renderOrder);
		this.ellipse = ellipse;
		this.offX = offX;
		this.offY = offY;
	}
	
}