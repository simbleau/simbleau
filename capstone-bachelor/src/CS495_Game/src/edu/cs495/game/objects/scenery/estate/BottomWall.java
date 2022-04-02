package edu.cs495.game.objects.scenery.estate;

import edu.cs495.engine.game.satcollision.SATPolygon;
import edu.cs495.game.objects.scenery.AbstractPhysicalScenery;
import edu.cs495.game.objects.scenery.AbstractScenery;

/** A bottom wall
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class BottomWall extends AbstractPhysicalScenery{
	
	/** The file path for the scenery image */
	private static final String IMAGE_PATH = "/map/estate/bottom_wall.png";

	/** The depth of the wall */
	private static final int WALL_DEPTH = 4;
	
	/** Initialize the scenery
	 * 
	 * @param imagePath - the path of the image
	 * @param posX - X world coordinate
	 * @param posY = Y world coordinate
	 */
	public BottomWall(float posX, float posY) {
		super(IMAGE_PATH, posX, posY, WALL_DEPTH);
		
		SATPolygon hitbox = new SATPolygon(this);
		hitbox.addVector(0, 0, 77, 0);
		hitbox.addVector(77, 0, 77, 55);
		hitbox.addVector(77, 55, 0, 55);
		hitbox.addVector(0, 55, 0, 0);
		
		SATPolygon hitbox2 = new SATPolygon(this);
		hitbox2.addVector(111, 0, 209, 0);
		hitbox2.addVector(209, 0, 209, 55);
		hitbox2.addVector(209, 55, 111, 55);
		hitbox2.addVector(111, 55, 111, 0);
		
		SATPolygon hitbox3 = new SATPolygon(this);
		hitbox3.addVector(243, 0, 379, 0);
		hitbox3.addVector(379, 0, 379, 55);
		hitbox3.addVector(379, 55, 243, 55);
		hitbox3.addVector(243, 55, 243, 0);

		SATPolygon hitbox4 = new SATPolygon(this);
		hitbox4.addVector(413, 0, 532, 0);
		hitbox4.addVector(532, 0, 532, 55);
		hitbox4.addVector(532, 55, 413, 55);
		hitbox4.addVector(413, 55, 413, 0);
		
		SATPolygon hitbox5 = new SATPolygon(this);
		hitbox5.addVector(566, 0, 673, 0);
		hitbox5.addVector(673, 0, 673, 55);
		hitbox5.addVector(673, 55, 566, 55);
		hitbox5.addVector(566, 55, 566, 0);
		
		SATPolygon hitbox6 = new SATPolygon(this);
		hitbox6.addVector(707, 0, 783, 0);
		hitbox6.addVector(783, 0, 783, 55);
		hitbox6.addVector(783, 55, 707, 55);
		hitbox6.addVector(707, 55, 707, 0);
		
		this.physicsComponent.addCollidableArea(hitbox);
		this.physicsComponent.addCollidableArea(hitbox2);
		this.physicsComponent.addCollidableArea(hitbox3);
		this.physicsComponent.addCollidableArea(hitbox4);
		this.physicsComponent.addCollidableArea(hitbox5);
		this.physicsComponent.addCollidableArea(hitbox6);
	}
	
	
}

