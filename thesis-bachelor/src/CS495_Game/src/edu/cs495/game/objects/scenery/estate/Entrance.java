package edu.cs495.game.objects.scenery.estate;

import edu.cs495.engine.game.satcollision.SATPolygon;
import edu.cs495.game.objects.scenery.AbstractPhysicalScenery;

/** The main entrance
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class Entrance extends AbstractPhysicalScenery{
	
	/** The file path for the scenery image */
	private static final String IMAGE_PATH = "/map/estate/entrance.png";

	/** The depth of the wall */
	private static final int WALL_DEPTH = 4;
	
	/** Initialize the scenery
	 * 
	 * @param imagePath - the path of the image
	 * @param posX - X world coordinate
	 * @param posY = Y world coordinate
	 */
	public Entrance(float posX, float posY) {
		super(IMAGE_PATH, posX, posY, WALL_DEPTH);
		
		SATPolygon hitbox = new SATPolygon(this);
		hitbox.addVector(0, 0, 23, 0);
		hitbox.addVector(23, 0, 23, 72);
		hitbox.addVector(23, 72, 0, 72);
		hitbox.addVector(0, 72, 0, 0);
		
		SATPolygon hitbox2 = new SATPolygon(this);
		hitbox2.addVector(57, 0, 156, 0);
		hitbox2.addVector(156, 0, 156, 72);
		hitbox2.addVector(156, 72, 57, 72);
		hitbox2.addVector(57, 72, 57, 0);
		
		SATPolygon hitbox3 = new SATPolygon(this);
		hitbox3.addVector(255, 0, 346, 0);
		hitbox3.addVector(346, 0, 346, 72);
		hitbox3.addVector(346, 72, 255, 72);
		hitbox3.addVector(255, 72, 255, 0);

		SATPolygon hitbox4 = new SATPolygon(this);
		hitbox4.addVector(380, 0, 411, 0);
		hitbox4.addVector(411, 0, 411, 72);
		hitbox4.addVector(411, 72, 380, 72);
		hitbox4.addVector(380, 72, 380, 0);
		
		this.physicsComponent.addCollidableArea(hitbox);
		this.physicsComponent.addCollidableArea(hitbox2);
		this.physicsComponent.addCollidableArea(hitbox3);
		this.physicsComponent.addCollidableArea(hitbox4);
	}
	
	
}

