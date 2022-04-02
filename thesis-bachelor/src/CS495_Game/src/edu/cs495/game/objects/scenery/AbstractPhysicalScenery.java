/**
 * 
 */
package edu.cs495.game.objects.scenery;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.engine.game.AbstractPhysicalGameObject;
import edu.cs495.engine.game.satcollision.CollisionEvent;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Image;

/** Abstract scenery item with physics
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public abstract class AbstractPhysicalScenery extends AbstractPhysicalGameObject{
	
	/** The path of the image */
	protected String imagePath;
	
	/** The image of the object */
	protected Image image;

	/** Initialize the object
	 *
	 * @param posX - X coordinate
	 * @param posY - Y coordinate
	 */
	public AbstractPhysicalScenery(String imagePath, float posX, float posY, int depth) {
		super("(Scenery)", posX, posY, 0, 0, depth);
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
	
	

	@Override
	public void update(GameDriver gameDriver) {
		//Do nothing
	}


	/** Renders the scenery
	 * 
	 * @see {@link AbstractGameObject#init(GameDriver)}
	 */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		super.render(gameDriver, renderer);
		
		renderer.draw(image, offX, offY);
	}
	
	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractPhysicalGameObject#handleCollision(edu.cs495.engine.game.satcollision.CollisionEvent)
	 */
	@Override
	public void handleCollision(CollisionEvent collisionEvent) {
		//Do nothing
	}

}
