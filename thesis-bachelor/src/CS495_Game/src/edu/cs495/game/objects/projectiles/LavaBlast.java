package edu.cs495.game.objects.projectiles;

import java.awt.Point;

import edu.cs495.game.objects.player.Player;

/** A lava blast spell
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class LavaBlast extends AbstractBlast {
	/** The sprite sheet for the blast */
	private static final String SPRITE_SHEET = "/equipment/spells/lava_blast.png";
	
	/** The width of a blast tile in the sprite sheet */
	private static final int TILE_WIDTH = 14;
	
	/** The height of a blast tile in the sprite sheet */
	private static final int TILE_HEIGHT = 14;
	
	/** The bloom color for this spell */
	private static final int BLOOM_RGB = 0xff2300;
	
	/** The starting time to live for the projectile (in seconds)  */
	private static final float INITIAL_TTL = 1.5f;
	
	/** Initialize the blast
	 * 
	 * @param theta - the angle to travel
	 * @param speed - the speed of this projectile
	 * @param origin - the origin of this blast
	 */
	public LavaBlast( Point origin, double theta, int speed, Player sender) {
		super(
				SPRITE_SHEET, 
				TILE_WIDTH, 
				TILE_HEIGHT, 
				BLOOM_RGB, 
				theta, speed, 
				INITIAL_TTL, origin, sender);
	}
	
	/** Initialize the blast
	 * 
	 * @param theta - the angle to travel
	 * @param speed - the speed of this projectile
	 * @param origin - the origin of this blast
	 */
	public LavaBlast( Point origin, double theta, Player sender) {
		super(
				SPRITE_SHEET, 
				TILE_WIDTH, 
				TILE_HEIGHT, 
				BLOOM_RGB, 
				theta, AbstractBlast.DEFAULT_SPEED, 
				INITIAL_TTL, origin, sender);
	}
}
