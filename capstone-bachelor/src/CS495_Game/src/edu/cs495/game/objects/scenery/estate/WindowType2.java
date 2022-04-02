package edu.cs495.game.objects.scenery.estate;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.game.objects.scenery.AbstractScenery;

/** A window, type 2
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class WindowType2 extends AbstractScenery{
	
	private int renderOrder;
	
	/** The file path for the scenery image */
	private static final String IMAGE_PATH = "/map/estate/window2.png";

	/** Initialize the scenery
	 * 
	 * @param imagePath - the path of the image
	 * @param posX - X world coordinate
	 * @param posY = Y world coordinate
	 */
	public WindowType2(float posX, float posY) {
		super(IMAGE_PATH, posX, posY);
		this.renderOrder = (int) posY + 19; //Window is 66 px from wall 
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