package edu.cs495.game.objects.scenery.estate;

import edu.cs495.engine.game.satcollision.SATPolygon;
import edu.cs495.game.objects.scenery.AbstractPhysicalScenery;

/** An inner side wall
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class InnerSideWall extends AbstractPhysicalScenery{
	
	/** The file path for the scenery image */
	private static final String IMAGE_PATH = "/map/estate/inner_side_wall.png";

	/** The depth of the wall */
	private static final int WALL_DEPTH = 194;
	
	private static final int FLOOR_OFFSET = -97;
	
	/** Initialize the scenery
	 * 
	 * @param imagePath - the path of the image
	 * @param posX - X world coordinate
	 * @param posY = Y world coordinate
	 */
	public InnerSideWall(float posX, float posY) {
		super(IMAGE_PATH, posX, posY, WALL_DEPTH);
		

		SATPolygon hitbox = new SATPolygon(this);
		hitbox.addVector(0, 0, 10, 0);
		hitbox.addVector(10, 0, 10, 194);
		hitbox.addVector(10, 194, 0, 194);
		hitbox.addVector(0, 194, 0, 0);
		
		this.physicsComponent.setFloorOffset(FLOOR_OFFSET);
		
		this.physicsComponent.addCollidableArea(hitbox);
		
	}
	
}
