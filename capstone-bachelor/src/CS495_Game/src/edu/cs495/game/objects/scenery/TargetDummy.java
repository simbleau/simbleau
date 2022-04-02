package edu.cs495.game.objects.scenery;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.game.satcollision.CollisionEvent;
import edu.cs495.engine.game.satcollision.SATCircle;
import edu.cs495.engine.gfx.Renderer;

/** A target dummy is a target which informs the player of their DPS
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class TargetDummy extends AbstractPhysicalScenery {
	
	/** The file path for the scenery image */
	private static final String IMAGE_PATH = "/map/common/target.png";
	
	/** The depth of the object's hitbox on the y axis */
	private static final int OBJECT_DEPTH = 4;
	
	/** The hitbox radius */
	private static final int COLLISION_CIRCLE_RADIUS = 10;
	
	/** The outer hitbox of the target dummy */
	private final SATCircle targetCircle;
	
	/** Initialize a target dummy 
	 * 
	 * @param posX - world x coordinate for placement
	 * @param posY - world y coordinate for placement
	 */
	public TargetDummy(float posX, float posY) {
		super(IMAGE_PATH, posX, posY, OBJECT_DEPTH);
		this.targetCircle = 
				new SATCircle(this, COLLISION_CIRCLE_RADIUS, COLLISION_CIRCLE_RADIUS, COLLISION_CIRCLE_RADIUS);
		
		this.physicsComponent.addCollidableArea(targetCircle);
	}


	@Override
	public void handleCollision(CollisionEvent collisionEvent) {
		DeveloperLog.print("TARGET DUMMY HIT");
	}
	
	


	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.scenery.AbstractPhysicalScenery#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		// TODO Auto-generated method stub
		super.init(gameDriver);
		
		this.offY -= 32;
	}


	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.scenery.AbstractPhysicalScenery#render(edu.cs495.engine.GameDriver, edu.cs495.engine.gfx.Renderer)
	 */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		physicsComponent.render(gameDriver, renderer);
		
		renderer.draw(image, (int) posY, offX, offY);
	}
	
	

}
