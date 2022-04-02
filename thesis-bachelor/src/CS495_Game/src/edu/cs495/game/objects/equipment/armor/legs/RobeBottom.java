package edu.cs495.game.objects.equipment.armor.legs;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.obj.filters.ColorSwapFilter;
import edu.cs495.game.objects.equipment.armor.AbstractArmor;

/**
 * @author Spencer
 *
 */
public abstract class RobeBottom extends AbstractArmor{
	/** The type the armor */
	private static final ArmorType ARMOR_TYPE = ArmorType.LEG;

	/** The icon path of the armor */
	private static final String ICON_PATH = 
			"/equipment/armor/icons/robe_bottom.png";
	
	/** The sheet path for the armor */
	private static final String SHEET_PATH = 
			"/equipment/armor/robe_bottom.png";
	
	/** The horizontal size of the visible armor */
	private static final int TILE_WIDTH = 32;
	
	/** The vertical size of the visible armor */
	private static final int TILE_HEIGHT = 22; 
	
	/** The offset to the player's X position if worn */
	private static final int OFFSET_X = -16;
	
	/** The offset to the player's Y position if worn */
	private static final int OFFSET_Y = -27;
	
	/** The amount of damage reduction by a standard robe bottom */
	private static final int ROBE_BOTTOM_DAMAGE_REDUCTION = 2;
	
	/** The primary color of the robe bottom */
	private int color1;

	/** The accent color of the robe bottom */
	private int color2;
	
	/** Initialize a robe bottom
	 * 
	 * @param name - the name of the robe bottom
	 * @param color1 - the primary color of the robe bottom
	 * @param color2 - the secondary color of the robe bottom
	 */
	public RobeBottom(String name, int color1, int color2) {
		super (name,
				ARMOR_TYPE,
				ROBE_BOTTOM_DAMAGE_REDUCTION,
				SHEET_PATH,
				TILE_WIDTH,
				TILE_HEIGHT,
				ICON_PATH,
				OFFSET_X,
				OFFSET_Y);
		
		this.color1 = color1;
		this.color2 = color2;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.equipment.armor.AbstractArmor#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		super.init(gameDriver);
		
		//Color Filter
		ColorSwapFilter colorReplacementFilter = new ColorSwapFilter();
		colorReplacementFilter.addColorSwap(0xff00ff, color1);
		colorReplacementFilter.addColorSwap(0x00ff00, color2);
		
		spriteSheet.addFilter(colorReplacementFilter);
		spriteSheet.buildAll();
		
		icon.addFilter(colorReplacementFilter);
		icon.build();
	}
	

}
