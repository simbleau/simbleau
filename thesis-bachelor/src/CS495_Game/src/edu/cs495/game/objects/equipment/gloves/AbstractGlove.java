/**
 * 
 */
package edu.cs495.game.objects.equipment.gloves;

import java.awt.Point;
import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.gfx.obj.filters.RotationFilter;
import edu.cs495.game.objects.equipment.armor.AbstractArmor;
import edu.cs495.game.objects.player.Player;

/** An abstract weapon for a player
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public abstract class AbstractGlove extends AbstractArmor {
	
	/** Enumeration of casting modes */
	public enum GloveMode {
		/** Automatic fire mode */
		AUTOMATIC,
		/* Burst fire mode */
		BURST,
		/** Semi automatic fire mode */
		SEMI;
	}
	
	/** The rest position when initialized */
	protected double initialTheta;
	
	/** The base damage of one projectile */
	protected int baseDamage;
	
	/** The mode of the spell being cast */
	protected GloveMode mode;
	
	/** The initial cooldown for the glove */
	protected final float initialCooldown;
	
	/** The current cooldown of the glove */
	protected float cooldown;
	
	/** The timer which tracks cooldown */
	protected float timer;
	
	/** Kind of like animation frames but for rate of fires */
	protected int sequence;
	
	/** The rotation filter for the gloves */
	protected RotationFilter rotationFilter;


	/** Initialize a glove
	 * 
	 * @param name - the name of the glove
	 * @param projectile - the launched item
	 * @param gloveType - the type of the glove
	 * @param gloveMode - the firing mode of the glove
	 * @param cooldown - the cooldown of the glove
	 * @param baseDamage - the damage per projectile
	 * @param damageReduction - the defence that is offered by the glove
	 * @param imagePath - the path of the glove image file
	 * @param iconPath - the icon path
	 * @param offX - the x offset to the player's position
	 * @param offY - the y offset to the player's position
	 */
	public AbstractGlove(String name, ArmorType armorType, GloveMode gloveMode,
			float cooldown, int baseDamage, int damageReduction, String imagePath,  String iconPath) {
		super
				(name, 
				armorType, 
				damageReduction, 
				imagePath, 
				38, 38, 
				iconPath, 
				0, 0);
		this.mode = gloveMode;
		this.baseDamage = baseDamage;
		this.initialCooldown = cooldown;
		this.cooldown = cooldown;
		this.timer = cooldown;
		this.sequence = 0;
	}
	
	

	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.equipment.armor.AbstractArmor#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		super.init(gameDriver);
		
		//Dirty solution
		Image filterImage = new Image(spriteSheet.getTileHeight(), spriteSheet.getTileWidth(), spriteSheet.get(0, 0));
		
		RotationFilter gloveRotationFilter = new RotationFilter(
				filterImage, 
				0, 
				new Point(filterImage.getHalfWidth(), filterImage.getHalfHeight()));
		this.rotationFilter = gloveRotationFilter;	
		
		//Allow immediate shooting of the glove
		this.timer = 0;
		
		spriteSheet.addFilter(gloveRotationFilter);
		spriteSheet.cleanAll();
		spriteSheet.buildAll();
	}
	
	/** Returns the angle of the gloves
	 * 
	 * @return the angle in radians of the gloves
	 */
	public double getTheta() {
		return this.rotationFilter.getTheta();
	}

	/** Sets the new angle of the gloves
	 * 
	 * @param theta - angle for the gloves
	 */
	public void setTheta(double theta) {
		this.rotationFilter.setTheta(theta);
		this.spriteSheet.cleanAll();
		this.spriteSheet.buildAll();
	}

	/** Get the mode the glove is in
	 * 
	 * @return the glove mode
	 * @see {@link GloveMode}
	 */
	public GloveMode getMode() {
		return this.mode;
	}

	/** A tick event which controls fire rate for the glove
	 * 
	 * @param gameDriver - the game driver for the glove
	 */
	public void tick(GameDriver gameDriver) {		
			timer -= GameDriver.UPDATE_DT;
			if (timer < 0) {
				switch (mode) {
				case AUTOMATIC:
				case SEMI:
					this.cooldown = initialCooldown;
					break;
				case BURST:
					switch (sequence) {
					case 0:
						cooldown = initialCooldown / 5f;
						sequence = 1;
						break;
					case 1: 
						cooldown = initialCooldown / 5f;
						sequence = 2;
						break;
					case 2:
						cooldown = initialCooldown;
						sequence = 0;
						break;
					}
				}
			}
		}

	/** Shoot a projectile from the origin with this glove
	 * 
	 * @param gameDriver - the game driver for this glove
	 * @param origin - the starting point of the projectile
	 * @param theta - the angle to shoot
	 */
	public abstract void shoot(GameDriver gameDriver, Player sender, Point origin, double theta);



	/** Returns whether the glove is ready to shoot
	 * 
	 * @return true if the glove's cooldown is <= 0, false otherwise
	 */
	public boolean canShoot() {
		return (this.timer <= 0);
	}
}
