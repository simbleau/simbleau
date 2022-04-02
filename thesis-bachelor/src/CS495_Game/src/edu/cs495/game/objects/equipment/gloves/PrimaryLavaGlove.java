/**
 * 
 */
package edu.cs495.game.objects.equipment.gloves;

import java.awt.Point;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.gfx.obj.filters.ColorReplacementFilter;
import edu.cs495.engine.gfx.obj.filters.ColorSwapFilter;
import edu.cs495.game.objects.player.Player;
import edu.cs495.game.objects.projectiles.AbstractBlast;
import edu.cs495.game.objects.projectiles.LavaBlast;

/** A primary lava glove (middle tier)
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class PrimaryLavaGlove extends AbstractGlove{
	
	/** The type of the glove */
	private static final ArmorType GLOVE_TYPE = ArmorType.P_GLOVE;
	
	/** The mode of glove */
	private static final GloveMode GLOVE_MODE = GloveMode.SEMI;
	
	/** The countdown between shooting */
	private static final float COOLDOWN = 0.5f;
	
	/** The base damage */
	private static final int BASE_DAMAGE = 15;
	
	/** The damage reduction */
	private static final int DAMAGE_REDUCTION = 2;
	
	/** The path of the glove image file */
	private static final String IMAGE_PATH = "/equipment/gloves/left_glove.png";
	
	/** The path of the glove icon image file */
	private static final String ICON_PATH = "/equipment/gloves/icons/primary_glove.png";

	/** The lava color filter */
	protected ColorReplacementFilter lavaColor;
	
	/** The shades changes per second */
	private static final int SPS = 5;
	
	/** The total amount of color changes */
	protected static final int SHADES_OF_RED = 5;
	
	/** The frame of animation */
	protected int frame;
	
	/** Initializing a fire glove
	 * 
	 * @see AbstractGlove
	 */
	public PrimaryLavaGlove() {
		super("Primary Lava Glove", 
				GLOVE_TYPE, 
				GLOVE_MODE, 
				COOLDOWN, 
				BASE_DAMAGE, 
				DAMAGE_REDUCTION, 
				IMAGE_PATH, 
				ICON_PATH);
		
		this.lavaColor = new ColorReplacementFilter();
		this.frame = 0;
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
		ColorSwapFilter colorReplacementFilter = new ColorSwapFilter();
		colorReplacementFilter.addColorSwap(0xff00ff, 0xff4f00);
		
		//Build new icon/sprites
		this.icon.addFilter(colorReplacementFilter);
		this.icon.bake(colorReplacementFilter);
		
		//Set rotation filter up
		lavaColor.setImage(filterImage);
		lavaColor.setOverlayColor(0xff0000);
		spriteSheet.addFilter(lavaColor);
		spriteSheet.bakeAll(lavaColor);
	}




	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.equipment.gloves.AbstractGlove#tick(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void tick(GameDriver gameDriver) {
		super.tick(gameDriver);
		
		frame = (int) ((SPS * gameDriver.getGameTime() / GameDriver.NANO_SECOND)) % SHADES_OF_RED;
		
		int red = 0xff0000;
		double greenPower = Math.cos(((float) frame / (float) SHADES_OF_RED) * 2 * Math.PI);
		int greenHue = (0xc0 / 2) + (int) ((greenPower * 0x70) / 2);
		
		lavaColor.setOverlayColor(red | greenHue << 8);
		spriteSheet.bakeAll(lavaColor);
	}


	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.equipment.gloves.AbstractGlove#shoot(java.awt.Point, double)
	 */
	@Override
	public void shoot(GameDriver gameDriver, Player sender, Point origin, double theta) {
		if (timer < 0) {
			AbstractBlast blast = new LavaBlast(origin, theta, sender);
			blast.init(gameDriver);
			gameDriver.getLevel().addGameObject(blast);	
			timer = cooldown;
		}
	}
	
	

}
