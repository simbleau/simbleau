/**
 * 
 */
package edu.cs495.game.objects.equipment.armor.helmets;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.obj.filters.ColorSwapFilter;
import edu.cs495.game.objects.equipment.armor.AbstractArmor;

/** A pointy magician's hat
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public abstract class PointyHat extends AbstractArmor {

	/** The type the armor */
	private static final ArmorType ARMOR_TYPE = ArmorType.HELMET;

	/** The icon path of the armor */
	private static final String ICON_PATH = 
			"/equipment/armor/icons/pointy_hat.png";
	
	/** The sheet path for the armor */
	private static final String SHEET_PATH = 
			"/equipment/armor/pointy_hat.png";
	
	/** The horizontal size of the visible armor */
	private static final int TILE_WIDTH = 14;
	
	/** The vertical size of the visible armor */
	private static final int TILE_HEIGHT = 13; 
	
	/** The offset to the player's X position if worn */
	private static final int OFFSET_X = -7;
	
	/** The offset to the player's Y position if worn */
	private static final int OFFSET_Y = -72;

	/** The damage reduction this armor offers */
	private static final int DAMAGE_REDUCTION = 1;
	
	/** The primary color of the robe bottom */
	private int color1;

	/** The accent color of the robe bottom */
	private int color2;
	
	/** Properly initialize the Mod Crown */
	public PointyHat(String name, int rgb1, int rgb2) {
		super(name,
				ARMOR_TYPE,
				DAMAGE_REDUCTION, 
				SHEET_PATH, 
				TILE_WIDTH,
				TILE_HEIGHT,
				ICON_PATH, 
				OFFSET_X, 
				OFFSET_Y);
		
		this.color1 = rgb1;
		this.color2 = rgb2;
	}

	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractGameObject#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		super.init(gameDriver);
		
		//Color Filter
		ColorSwapFilter colorReplacementFilter = new ColorSwapFilter();
		colorReplacementFilter.addColorSwap(0xff00ff, color1);
		colorReplacementFilter.addColorSwap(0x00ff00, color2);
		
		//Build new icon/sprites
		this.icon.clean();
		this.spriteSheet.cleanAll();
		
		//Init Icon
		this.icon.addFilter(colorReplacementFilter);
		this.spriteSheet.addFilter(colorReplacementFilter);
		
		//Build new icon/sprites
		this.icon.build();
		this.spriteSheet.buildAll();
	}
}