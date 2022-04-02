package edu.cs495.engine.gfx.requests;

import edu.cs495.engine.gfx.obj.Box;

public class BoxOverlayRequest extends AbstractRequest {
	
	/** The box being requested */
	public final Box BOX;
	/** The x coordinate, relative to the screen, for rendering */
	public final int X;
	/** The y coordinate, relative to the screen, for rendering */
	public final int Y;
	
	/** Initializes an overlay box request 
	 * 
	 * @param box - the box to overlay
	 * (note: Only applies when comparing overlay requests, as overlays always
	 * render ontop of all game rendering requests by default)
	 * @param x - the X coordinate this is rendered at on the screen
	 * @param y - the Y coordinate this is rendered at on the screen */
	public BoxOverlayRequest(Box box, int renderOrder, int x, int y) {
		super(renderOrder);
		this.BOX = box;
		this.X = x;
		this.Y = y;
	}

}
