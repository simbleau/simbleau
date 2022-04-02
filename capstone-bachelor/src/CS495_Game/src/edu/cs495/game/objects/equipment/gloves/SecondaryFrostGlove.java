/**
 * 
 */
package edu.cs495.game.objects.equipment.gloves;

import java.awt.Point;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.gfx.obj.filters.ColorSwapFilter;
import edu.cs495.game.objects.player.Player;
import edu.cs495.game.objects.projectiles.AbstractDart;
import edu.cs495.game.objects.projectiles.IceBlast;

/** A frosty glove
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class SecondaryFrostGlove extends AbstractGlove{
	
	/** The type of the glove */
	private static final ArmorType GLOVE_TYPE = ArmorType.S_GLOVE;
	
	/** The mode of glove */
	private static final GloveMode GLOVE_MODE = GloveMode.AUTOMATIC;
	
	/** The countdown between shooting */
	private static final float COOLDOWN = 0.3f;
	
	/** The base damage */
	private static final int BASE_DAMAGE = 2;
	
	/** The damage reduction */
	private static final int DAMAGE_REDUCTION = 1;
	
	/** The path of the glove image file */
	private static final String IMAGE_PATH = "/equipment/gloves/right_glove.png";
	
	/** The path of the glove icon image file */
	private static final String ICON_PATH = "/equipment/gloves/icons/frost_secondary_glove.png";

	/** The color replacement filter */
	protected ColorSwapFilter colorReplacementFilter;
	
	/** Initializing an ice glove
	 * 
	 * @see AbstractGlove
	 */
	public SecondaryFrostGlove() {
		super("Secondary Frost Glove", 
				GLOVE_TYPE, 
				GLOVE_MODE, 
				COOLDOWN, 
				BASE_DAMAGE, 
				DAMAGE_REDUCTION, 
				IMAGE_PATH, 
				ICON_PATH);
		
		this.colorReplacementFilter = new ColorSwapFilter();
	}




	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.equipment.gloves.AbstractGlove#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		super.init(gameDriver);
		
		//Dirty solution
		Image filterImage = new Image(spriteSheet.getTileHeight(), spriteSheet.getTileWidth(), spriteSheet.get(0, 0));
		
		//Color Filter
		colorReplacementFilter.setImage(filterImage);
		colorReplacementFilter.addColorSwap(0xff00ff, 0x0084ff);
		colorReplacementFilter.addColorSwap(0x00ff00, 0xbcfffd);
		
		//Build new icon/sprites
		this.icon.addFilter(colorReplacementFilter);
		this.icon.clean();
		this.icon.build();
		
		
		spriteSheet.cleanAll();
		spriteSheet.addFilter(colorReplacementFilter);
		spriteSheet.buildAll();
	}




	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.equipment.gloves.AbstractGlove#shoot(java.awt.Point, double)
	 */
	@Override
	public void shoot(GameDriver gameDriver, Player sender, Point origin, double theta) {
		if (timer < 0) {
			AbstractDart blast = new IceBlast(origin, theta, sender);
			blast.init(gameDriver);
			gameDriver.getLevel().addGameObject(blast);	
			timer = cooldown;
		}
	}
	
	

}
