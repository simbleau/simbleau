/**
 * 
 */
package edu.cs495.game.objects.equipment.gloves;

import java.awt.Point;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.gfx.obj.filters.ColorReplacementFilter;
import edu.cs495.game.objects.player.Player;
import edu.cs495.game.objects.projectiles.AbstractDart;
import edu.cs495.game.objects.projectiles.MagicDart;

/** A primary magic glove (high tier)
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class PrimaryMagicGlove extends AbstractGlove{
	
	/** The type of the glove */
	private static final ArmorType GLOVE_TYPE = ArmorType.P_GLOVE;
	
	/** The mode of glove */
	private static final GloveMode GLOVE_MODE = GloveMode.SEMI;
	
	/** The countdown between shooting */
	private static final float COOLDOWN = 0.3f;
	
	/** The base damage */
	private static final int BASE_DAMAGE = 20;
	
	/** The damage reduction */
	private static final int DAMAGE_REDUCTION = 3;
	
	/** The path of the glove image file */
	private static final String IMAGE_PATH = "/equipment/gloves/left_glove.png";
	
	/** The path of the glove icon image file */
	private static final String ICON_PATH = "/equipment/gloves/icons/magic_primary_glove.png";

	/** The color oscillation speed */
	private static final int COLOR_SPEED = 5;
	
	/** The lava color filter */
	protected ColorReplacementFilter magicColor;
	
	/** Decides color pattern */
	private short state;
	
	
	/** Initializing a fire glove
	 * 
	 * @see AbstractGlove
	 */
	public PrimaryMagicGlove() {
		super("Primary Magic Glove", 
				GLOVE_TYPE, 
				GLOVE_MODE, 
				COOLDOWN, 
				BASE_DAMAGE, 
				DAMAGE_REDUCTION, 
				IMAGE_PATH, 
				ICON_PATH);
		
		this.magicColor = new ColorReplacementFilter();
		this.state = 0;
	}




	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.equipment.gloves.AbstractGlove#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		super.init(gameDriver);
		
		//Dirty solution
		Image filterImage = new Image(spriteSheet.getTileHeight(), spriteSheet.getTileWidth(), spriteSheet.get(0, 0));
		
		//Set rotation filter up
		magicColor.setImage(filterImage);
		magicColor.setOverlayColor(0xff0000); //Init red
		state = 0;
		
		spriteSheet.cleanAll();
		spriteSheet.addFilter(magicColor);
		spriteSheet.buildAll();
	}

	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.equipment.gloves.AbstractGlove#tick(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void tick(GameDriver gameDriver) {
		super.tick(gameDriver);
		
		//Cycle color
		int current = magicColor.getOverlayColor();
		
		int r = (current & 0xff0000) >> 16;
		int g = (current & 0x00ff00) >> 8;
		int b = current & 0x0000ff;
		
		switch(state) {
		case 0:
		    g += COLOR_SPEED;
		    if(g == 255)
		        state = 1;
		    break;
		case 1:
		    r -= COLOR_SPEED;
		    if(r == 0)
		        state = 2;
		    break;
		case 2:
		    b += COLOR_SPEED;
		    if(b == 255)
		        state = 3;
		    break;
		case 3:
		    g -= COLOR_SPEED;
		    if(g == 0)
		        state = 4;
		    break;
		case 4:
		    r += COLOR_SPEED;
		    if(r == 255)
		        state = 5;
		    break;
		case 5:
		    b -= COLOR_SPEED;
		    if(b == 0)
		        state = 0;
		    break;
		}
		
		magicColor.setOverlayColor((r << 16) + (g << 8) + (b));
		
		//No need to clean and rebuild because in this special case it doesn't require
		spriteSheet.cleanAll();
		spriteSheet.buildAll();
	}


	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.equipment.gloves.AbstractGlove#shoot(java.awt.Point, double)
	 */
	@Override
	public void shoot(GameDriver gameDriver, Player sender, Point origin, double theta) {
		if (timer < 0) {
			AbstractDart dart = new MagicDart(origin, theta, sender);
			dart.init(gameDriver);
			gameDriver.getLevel().addGameObject(dart);	
			
			AbstractDart dart2 = new MagicDart(origin, (Math.PI * 2) +theta + (Math.PI / 8), sender);
			dart2.init(gameDriver);
			gameDriver.getLevel().addGameObject(dart2);	
			
			AbstractDart dart3 = new MagicDart(origin, (Math.PI * 2) + theta - (Math.PI / 8), sender);
			dart3.init(gameDriver);
			gameDriver.getLevel().addGameObject(dart3);	
			
			timer = cooldown;
		}
	}
	
}
