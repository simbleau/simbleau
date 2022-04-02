package edu.cs495.game.objects.scenery.estate;

import edu.cs495.engine.game.satcollision.SATPolygon;
import edu.cs495.game.objects.scenery.AbstractPhysicalScenery;

/** An inner side wall
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class TopRightWall extends AbstractPhysicalScenery{
	
	/** The file path for the scenery image */
	private static final String IMAGE_PATH = "/map/estate/top_right_wall.png";
	
	/** The depth of the wall */
	private static final int WALL_DEPTH = 4;
	
	/** Initialize the scenery
	 * 
	 * @param imagePath - the path of the image
	 * @param posX - X world coordinate
	 * @param posY = Y world coordinate
	 */
	public TopRightWall(float posX, float posY) {
		super(IMAGE_PATH, posX, posY, WALL_DEPTH);
		
		SATPolygon hitbox = new SATPolygon(this);
		hitbox.addVector(0, 0, 76, 0);
		hitbox.addVector(76, 0, 76, 55);
		hitbox.addVector(76, 55, 0, 55);
		hitbox.addVector(0, 55, 0, 0);
		
		SATPolygon hitbox2 = new SATPolygon(this);
		hitbox2.addVector(110, 0, 186, 0);
		hitbox2.addVector(186, 0, 186, 55);
		hitbox2.addVector(186, 55, 110, 55);
		hitbox2.addVector(110, 55, 110, 0);
		
		this.physicsComponent.addCollidableArea(hitbox);
		this.physicsComponent.addCollidableArea(hitbox2);
	}
	
}