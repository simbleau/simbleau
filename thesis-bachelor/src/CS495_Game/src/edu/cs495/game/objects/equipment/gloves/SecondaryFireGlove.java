/**
 * 
 */
package edu.cs495.game.objects.equipment.gloves;

import java.awt.Point;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.gfx.obj.filters.ColorSwapFilter;
import edu.cs495.game.objects.player.Player;
import edu.cs495.game.objects.projectiles.AbstractParticle;
import edu.cs495.game.objects.projectiles.Fireball;

/** A secondary fire glove (low tier)
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class SecondaryFireGlove extends AbstractGlove{

	/** The type of the glove */
	private static final ArmorType GLOVE_TYPE = ArmorType.S_GLOVE;
	
	/** The mode of glove */
	private static final GloveMode GLOVE_MODE = GloveMode.BURST;
	
	/** The countdown between shooting */
	private static final float COOLDOWN = 0.5f;
	
	/** The base damage */
	private static final int BASE_DAMAGE = 5;
	
	/** The damage reduction */
	private static final int DAMAGE_REDUCTION = 1;
	
	/** The path of the glove image file */
	private static final String IMAGE_PATH = "/equipment/gloves/right_twotoned_glove.png";
	
	/** The path of the glove icon image file */
	private static final String ICON_PATH = "/equipment/gloves/icons/secondary_twotoned_glove.png";
	
	/** Initializing a fire glove
	 * 
	 * @see AbstractGlove
	 */
	public SecondaryFireGlove() {
		super("Secondary Fire Glove", 
				GLOVE_TYPE, 
				GLOVE_MODE, 
				COOLDOWN, 
				BASE_DAMAGE, 
				DAMAGE_REDUCTION, 
				IMAGE_PATH, 
				ICON_PATH);
	}




	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.equipment.gloves.AbstractGlove#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		super.init(gameDriver);
		
		//Color Filter
		ColorSwapFilter colorReplacementFilter = new ColorSwapFilter();
		colorReplacementFilter.addColorSwap(0xff00ff, 0xff2020);
		colorReplacementFilter.addColorSwap(0x00ff00, 0x2f1010);
		
		//Build new icon/sprites
		this.icon.addFilter(colorReplacementFilter);
		this.icon.clean();
		this.icon.build();
		
		//Dirty solution
		Image filterImage = new Image(spriteSheet.getTileHeight(), spriteSheet.getTileWidth(), spriteSheet.get(0, 0));
		
		//Set rotation filter up
		ColorSwapFilter gloveColorFilter = new ColorSwapFilter();	
		gloveColorFilter.setImage(filterImage);
		gloveColorFilter.addColorSwap(0xff00ff, 0xff2020);
		gloveColorFilter.addColorSwap(0x00ff00, 0x2f1010);
		
		
		spriteSheet.cleanAll();
		spriteSheet.addFilter(gloveColorFilter);
		spriteSheet.buildAll();
	}




	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.equipment.gloves.AbstractGlove#shoot(java.awt.Point, double)
	 */
	@Override
	public void shoot(GameDriver gameDriver, Player sender, Point origin, double theta) {
		if (timer < 0) {
			AbstractParticle ember = new Fireball(origin, theta, sender);
			ember.init(gameDriver);
			gameDriver.getLevel().addGameObject(ember);	
			timer = cooldown;
		}
	}
}
	
	