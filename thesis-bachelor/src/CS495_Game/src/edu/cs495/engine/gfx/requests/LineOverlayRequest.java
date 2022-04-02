package edu.cs495.engine.gfx.requests;

import edu.cs495.engine.gfx.obj.Line;

public class LineOverlayRequest extends AbstractRequest {
	
	/** The box being requested */
	public final Line line;
	/** The x coordinate, relative to the screen, for rendering */
	public final int x;
	/** The y coordinate, relative to the screen, for rendering */
	public final int y;
	
	/** Initializes an overlay box request 
	 * 
	 * @param line - the line to overlay
	 * @param renderOrder - the order this line is rendered in comparison to other overlays
	 * @param x - the X coordinate this is rendered at on the screen
	 * @param y - the Y coordinate this is rendered at on the screen */
	public LineOverlayRequest(Line line, int renderOrder, int x, int y) {
		super(renderOrder);
		this.line = line;
		this.x = x;
		this.y = y;
	}

}
