/**
 * 
 */
package edu.cs495.engine.game;
import edu.cs495.engine.gfx.obj.Image;

/** A MapBoundaries will hold information about the map boundaries
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class MapBoundaries {
	
	/** The boundary map itself */
	private boolean[][] boundaryMap;
	
	/** The image of the boundary map */
	private Image boundaryMapImage;
	
	/** Initialize a Boundary Component by initializing our path and memory
	 * 
	 * @param boundaryMapPath - the path of the boundary map
	 */
	public MapBoundaries(String boundaryMapPath) {
		this.boundaryMapImage = new Image(boundaryMapPath);
		
		int[] pixelData = boundaryMapImage.getPixels();
		int mapWidth = boundaryMapImage.getWidth();
		int mapHeight = boundaryMapImage.getHeight();
		
		//Scan the image and load it into our boundary map
		this.boundaryMap = new boolean[mapWidth][mapHeight];
		for (int y = 0; y < mapHeight; y++) {
			int yStart = y * mapWidth;
			for (int x = 0; x < mapWidth; x++) {
				int xyColor = pixelData[x + yStart];
				if (xyColor == 0xff000000) { //Pitch black
					this.boundaryMap[x][y] = true; //Block this pixel 
				}
			}
		}
	}
	
	/** Return whether a pixel is a blocked map location
	 * 
	 * @param x - x coordinate of the map
	 * @param y - y coordinate of the map
	 * @return true if the area is blocked, false otherwise
	 */
	public boolean isBlocked(int x, int y) {
		if (x < 0 || x > boundaryMapImage.getWidth() 
				|| y < 0 || y > boundaryMapImage.getHeight()) {
			return true;
		}
		return (boundaryMap[x][y]);
	}
	
	/** Returns the image of the boundary map
	 * 
	 * @return the image of the boundary map
	 */
	public Image getImage() {
		return this.boundaryMapImage;
	}
}