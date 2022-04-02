package edu.cs495.game.objects.scenery.estate;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.game.objects.scenery.AbstractScenery;

/** A window, type 1
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class WindowType1 extends AbstractScenery{
	
	private int renderOrder;
	
	/** The file path for the scenery image */
	private static final String IMAGE_PATH = "/map/estate/window1.png";

	/** Initialize the scenery
	 * 
	 * @param imagePath - the path of the image
	 * @param posX - X world coordinate
	 * @param posY = Y world coordinate
	 */
	public WindowType1(float posX, float posY) {
		super(IMAGE_PATH, posX, posY);
		this.renderOrder = (int) posY + 19; //Window is 19 px above brick walls 
	}

	/** Render the window at a special render order on parallel with the wall 
	 * it is attached to
	 *
	 * @see {@link renderer#render()}
	 */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		renderer.draw(image, renderOrder, offX, offY);
	}
	
}