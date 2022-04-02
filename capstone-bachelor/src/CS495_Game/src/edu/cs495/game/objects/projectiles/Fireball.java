/**
 * 
 */
package edu.cs495.game.objects.projectiles;

import java.awt.Point;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.obj.filters.ColorReplacementFilter;
import edu.cs495.game.objects.player.Player;

/** A fireball projectile
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class Fireball extends AbstractParticle{

	/** The sprite sheet for the particle */
	private static final int PARTICLE_RADIUS = 4;
	
	/** The starting color for this ember */
	private static final int START_RGB = 0xffff0000;
	
	/** The starting time to live for the projectile (in seconds)  */
	private static final float INITIAL_TTL = 1f;	
	
	/** Slow factor - this projectile loses 0.1% speed every gametime, or 6.0% speed every second (with 60 UPS) */
	private static final float SLOW_FACTOR = (.999f);
	
	/** After the TTL of this projectile has been exhausted, this projectile loses 0.25 */
	private static final float DEAD_SLOW_FACTOR = (.985f);
	
	/** the color of the particle filter */
	protected ColorReplacementFilter particleColorFilter;
	
	/** Initialize the particle with a speed
	 * 
	 * @param theta - the angle to travel
	 * @param speed - the speed of this projectile
	 * @param origin - the origin of this blast
	 */
	public Fireball( Point origin, double theta, int speed, Player sender) {
		super(	PARTICLE_RADIUS, 
				START_RGB,
				theta, speed, 
				INITIAL_TTL, origin, sender);
		this.particleColorFilter = new ColorReplacementFilter();
	}
	
	/** Initialize the particle with a default speed
	 * 
	 * @param theta - the angle to travel
	 * @param origin - the origin of this blast
	 */
	public Fireball(Point origin, double theta, Player sender) {
		super(	PARTICLE_RADIUS, 
				START_RGB,
				theta, AbstractParticle.DEFAULT_SPEED, 
				INITIAL_TTL, origin, sender);
		this.particleColorFilter = new ColorReplacementFilter();
	}

	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.projectiles.AbstractParticle#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		super.init(gameDriver);
		
		//Particle color filter
		this.particleColorFilter.setImage(particleImage);
		this.particleColorFilter.setOverlayColor(argb & 0x00ffffff);
		this.particleImage.addFilter(this.particleColorFilter);
		this.particleImage.bake(particleColorFilter);
	}

	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.projectiles.AbstractParticle#update(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void update(GameDriver gameDriver) {
		super.update(gameDriver);
		
		if (ttl <= 0) {
			//Fade to ashy white
			particleColorFilter.setOverlayColor(Math.min(0xffffff, particleColorFilter.getOverlayColor() + 0x050505));
			//Slow down fast
			xInc *= DEAD_SLOW_FACTOR;
			//Add back the offset that was lost (at a slower pace)
			posY -= yInc * (.80f);
			yInc -= GameDriver.UPDATE_DT; //Throttle speed
			
			//The most disgustingly clever solution possible. 40 here acts as a speed to ascend 
			offY -= (int) (yInc * ttl * 40f);
		} else { 
			//Fade to black (charring)
			particleColorFilter.setOverlayColor(Math.max(0x000000, particleColorFilter.getOverlayColor() - 0x060000));
		}

		//Bake our particle color
		this.particleImage.bake(particleColorFilter);
		
		// slow down 
		xInc *= SLOW_FACTOR;
	}


	
	
}
