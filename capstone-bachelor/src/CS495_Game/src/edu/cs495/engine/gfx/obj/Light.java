package edu.cs495.engine.gfx.obj;

/** A light graphics object which holds information about a light
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class Light implements Renderable {
	/** The radius of the light */
	public final int radius;
	
	/** The diameter of the light */
	public final int diameter;
	
	/** The color of the light in RGB format (no alpha!) */
	public final int rgb;
	
	/** The graphical ARGB lighting map for the light object */
	public final int[] lightMap;

	/** Initializes the light with information
	 * 
	 * @param radius - the radius of the light
	 * @param rgb - The color of the light in RGB format
	 * @param lightPower - Power of the light (0 <= lightPower <= 1)
	 */
	public Light(int radius, int rgb, float lightPower) {
		this.radius = radius;
		this.diameter = radius * 2;
		this.rgb = rgb;
		this.lightMap = new int[diameter * diameter];
		
		for (int y = 0; y < diameter; y++) {
			double y2 = (y - radius) * (y - radius);
			for (int x = 0; x < diameter; x++) {
				double x2 = (x - radius) * (x - radius);
				double distance = Math.sqrt(x2 + y2); //Pythagorean theorem
				
				if (distance < radius) {
					double rawPower = lightPower * (1 - distance / radius);
					
					//(0.0 <= rawPower <= 1.0)
					double power = Math.max(0, Math.min(rawPower, 1)); 
					
					int newRed = (int) (((rgb >> 16) & 0xff) * power) << 16;
					int newGreen = (int) (((rgb >> 8) & 0xff) * power) << 8;
					int newBlue = (int) ((rgb & 0xff) * power);
					lightMap[x + y * diameter] = newRed | newGreen | newBlue;
				} else {
					lightMap[x + y * diameter] = 0;
				}
			}
		}
	}

	/** Get the light value at a certain point in the light map
	 * 
	 * @param x - X coordinate of the light map
	 * @param y - Y coordinate of the light map
	 * @return the pixel value at the (x, y) coordiante given
	 */
	public int getLightValue(int x, int y) {
		if (x < 0 || x >= diameter || y < 0 || y >= diameter) {
			return 0;
		}
		return lightMap[x + y * diameter];
	}
}
