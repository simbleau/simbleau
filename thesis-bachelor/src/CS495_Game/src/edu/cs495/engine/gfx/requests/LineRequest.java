package edu.cs495.engine.gfx.requests;

import edu.cs495.engine.gfx.obj.Line;

/** This is an {@link AbstractRequest} which will encapsulate a {@link Line}
 * and where to draw it. This is sent to the renderer for processing.
 * 
 * @version March 2019
 * @author Spencer Imbleau
 */
public class LineRequest extends AbstractRequest {
	
	/** The box being rendered */
	public final Line line;
	/** The x coordinate this object is being rendered at */
	public final int offX;
	/** The y coordinate this object is being rendered at */
	public final int offY;
	
	/** Initialize Line Request
	 * 
	 * @param line - the line to render
	 * @param offX - The x coordinate to render the line at
	 * @param offY - The y coordinate to render the line at
	 */
	public LineRequest(Line line, int offX, int offY) {
		super(offY + line.getDy());
		this.line = line;
		this.offX = offX;
		this.offY = offY;
	}
	
	/** Initialize Box Request with a special render order
	 * 
	 * @param line - the line to render
	 * @param renderOrder - a specific render ordering for this request
	 * @param offX - The x coordinate to render the box at
	 * @param offY - The y coordinate to render the box at
	 */
	public LineRequest(Line line, int renderOrder, int offX, int offY) {
		super(renderOrder);
		this.line = line;
		this.offX = offX;
		this.offY = offY;
	}
	
}
