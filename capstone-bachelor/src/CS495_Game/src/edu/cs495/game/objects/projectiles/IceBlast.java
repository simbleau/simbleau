/**
 * 
 */
package edu.cs495.game.objects.projectiles;

import java.awt.Point;

import edu.cs495.engine.GameDriver;
import edu.cs495.game.objects.player.Player;

/** An Ice Dart projectile
 * 
 * @author Spencer Imbleau
 * @version February 2019
 */
public class IceBlast extends AbstractDart {
	
	/** The sprite sheet for the blast */
	private static final String IMAGE_PATH = "/equipment/spells/ice_blast.png";
	
	/** The width of a blast tile in the sprite sheet */
	private static final int IMAGE_WIDTH = 16;
	
	/** The height of a blast tile in the sprite sheet */
	private static final int IMAGE_HEIGHT = 16;
	
	/** The bloom color for this spell */
	private static final int BLOOM_RGB = 0x4f4fff;
	
	/** The starting time to live for the projectile (in seconds)  */
	private static final float INITIAL_TTL = 2f;	
	
	/** Initialize the blast with a speed
	 * 
	 * @param theta - the angle to travel
	 * @param speed - the speed of this projectile
	 * @param origin - the origin of this blast
	 */
	public IceBlast( Point origin, double theta, int speed, Player sender) {
		super(
				IMAGE_PATH, 
				IMAGE_WIDTH, 
				IMAGE_HEIGHT, 
				BLOOM_RGB, 
				theta, speed, 
				INITIAL_TTL, origin, sender);
	}
	
	/** Initialize the blast with a speed
	 * 
	 * @param theta - the angle to travel
	 * @param origin - the origin of this blast
	 */
	public IceBlast(Point origin, double theta, Player sender) {
		super(
				IMAGE_PATH, 
				IMAGE_WIDTH, 
				IMAGE_HEIGHT, 
				BLOOM_RGB, 
				theta, AbstractDart.DEFAULT_SPEED, 
				INITIAL_TTL, origin, sender);
	}

	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.projectiles.AbstractBlast#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		// TODO Auto-generated method stub
		super.init(gameDriver);
		
		alphaFilter.setAlphaPercent(60); //It's ICE!
		
		dartSpriteSheet.cleanAll();
		dartSpriteSheet.buildAll();
	}

	
}