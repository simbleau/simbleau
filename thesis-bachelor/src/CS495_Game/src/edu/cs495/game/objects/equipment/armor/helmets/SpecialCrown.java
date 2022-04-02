/**
 * 
 */
package edu.cs495.game.objects.equipment.armor.helmets;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.obj.filters.ColorSwapFilter;
import edu.cs495.game.objects.equipment.armor.AbstractArmor;

/** A special crown
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class SpecialCrown extends AbstractArmor {
	
	/** The type the armor */
	private static final ArmorType ARMOR_TYPE = ArmorType.HELMET;

	/** The icon path of the armor */
	private static final String ICON_PATH = 
			"/equipment/armor/icons/crown.png";
	
	/** The sheet path for the armor */
	private static final String SHEET_PATH = 
			"/equipment/armor/crown.png";
	
	/** The horizontal size of the visible armor */
	private static final int TILE_WIDTH = 12;
	
	/** The vertical size of the visible armor */
	private static final int TILE_HEIGHT = 6; 
	
	/** The offset to the player's X position if worn */
	private static final int OFFSET_X = -6;
	
	/** The offset to the player's Y position if worn */
	private static final int OFFSET_Y = -66;

	/** The damage reduction this armor offers */
	private static final int DAMAGE_REDUCTION = 0;
	
	/** The color of this crown */
	private final int CROWN_COLOR = 0x4d380e;
	
	/** The gem color of this crown */
	private final int GEM_COLOR = 0xd6d6d6;
	
	/** Properly initialize the Mod Crown */
	public SpecialCrown() {
		super("Special Crown",
				ARMOR_TYPE,
				DAMAGE_REDUCTION, 
				SHEET_PATH, 
				TILE_WIDTH,
				TILE_HEIGHT,
				ICON_PATH, 
				OFFSET_X, 
				OFFSET_Y);
	}

	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractGameObject#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		super.init(gameDriver);
		
		//Color Filter
		ColorSwapFilter colorReplacementFilter = new ColorSwapFilter();
		colorReplacementFilter.addColorSwap(0xff00ff, CROWN_COLOR);
		colorReplacementFilter.addColorSwap(0xff0000, GEM_COLOR);
		
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
