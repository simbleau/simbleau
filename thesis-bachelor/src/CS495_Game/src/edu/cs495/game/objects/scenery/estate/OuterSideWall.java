package edu.cs495.game.objects.scenery.estate;

import edu.cs495.game.objects.scenery.AbstractScenery;

/** An outer side wall
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class OuterSideWall extends AbstractScenery{
	
	/** The file path for the scenery image */
	private static final String IMAGE_PATH = "/map/estate/side_wall.png";
	
	

	/** Initialize the scenery
	 * 
	 * @param imagePath - the path of the image
	 * @param posX - X world coordinate
	 * @param posY = Y world coordinate
	 */
	public OuterSideWall(float posX, float posY) {
		super(IMAGE_PATH, posX, posY);
	}
	
}