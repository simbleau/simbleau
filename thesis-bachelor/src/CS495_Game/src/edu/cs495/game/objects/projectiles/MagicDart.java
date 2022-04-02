/**
 * 
 */
package edu.cs495.game.objects.projectiles;

import java.awt.Point;

import edu.cs495.game.objects.player.Player;

/**
 * @author Spencer
 *
 */
public class MagicDart extends AbstractDart{

	/** The sprite sheet for the blast */
	private static final String SHEET_PATH = "/equipment/spells/magic_dart.png";
	
	/** The width of a blast tile in the sprite sheet */
	private static final int TILE_WIDTH = 16;
	
	/** The height of a blast tile in the sprite sheet */
	private static final int TILE_HEIGHT = 16;
	
	/** The bloom color for this spell */
	private static final int BLOOM_RGB = 0xffcfea;
	
	/** The starting time to live for the projectile (in seconds)  */
	private static final float INITIAL_TTL = 0.5f;	
	
	/** Initialize the dart with a speed
	 * 
	 * @param theta - the angle to travel
	 * @param speed - the speed of this projectile
	 * @param origin - the origin of this blast
	 */
	public MagicDart(Point origin, double theta, int speed, Player sender) {
		super(
				SHEET_PATH, 
				TILE_WIDTH, 
				TILE_HEIGHT, 
				BLOOM_RGB, 
				theta, speed, 
				INITIAL_TTL, origin, sender);
	}
	
	/** Initialize the dart with a speed
	 * 
	 * @param theta - the angle to travel
	 * @param origin - the origin of this blast
	 */
	public MagicDart(Point origin, double theta, Player sender) {
		super(
				SHEET_PATH, 
				TILE_WIDTH, 
				TILE_HEIGHT, 
				BLOOM_RGB, 
				theta, AbstractDart.DEFAULT_SPEED, 
				INITIAL_TTL, origin, sender);
	}

	
}