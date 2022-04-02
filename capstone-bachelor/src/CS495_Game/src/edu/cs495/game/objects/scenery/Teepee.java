/**
 * 
 */
package edu.cs495.game.objects.scenery;


/** A teepee
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class Teepee extends AbstractScenery{
	
	/** The file path for the scenery image */
	private static final String IMAGE_PATH = "/map/common/teepee.png";

	/** Initialize the scenery
	 * 
	 * @param imagePath - the path of the image
	 * @param posX - X world coordinate
	 * @param posY = Y world coordinate
	 */
	public Teepee(float posX, float posY) {
		super(IMAGE_PATH, posX, posY);
	}
}