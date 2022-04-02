/**
 * 
 */
package edu.cs495.game.objects.scenery;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Image;

/** Abstract scenery item - does not move. Has no components.
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public abstract class AbstractScenery extends AbstractGameObject{
	/** The path of the image */
	protected String imagePath;
	
	/** The image of the object */
	protected Image image;
	
	/** Initialize the object
	 *
	 * @param posX - X coordinate
	 * @param posY - Y coordinate
	 */
	public AbstractScenery(String imagePath, float posX, float posY) {
		super("(Scenery)", posX, posY, 0, 0);
		this.imagePath = imagePath;
	}
	
	
	/** Initialize the object */
	@Override
	public void init(GameDriver gameDriver) {
		this.image = new Image(imagePath);
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.offX = (int) posX - image.getHalfWidth();
		this.offY = (int) posY - image.getHeight();
	}

	/** Update does nothing, it's static scenery
	 * Nevertheless, it's nice to still have the update method incase
	 * you want to override the update for a scenery object :)
	 * 
	 * @see {@link AbstractGameObject#init(GameDriver)}
	 */
	@Override
	public void update(GameDriver gameDriver) {
		super.update(gameDriver);
	} //Do nothing


	/** Renders the scenery
	 * 
	 * @see {@link AbstractGameObject#init(GameDriver)}
	 */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		super.render(gameDriver, renderer);
		
		renderer.draw(image, offX, offY);
	}

}
