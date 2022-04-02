package edu.cs495.game.objects.equipment.armor.chests;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.obj.filters.ColorSwapFilter;
import edu.cs495.game.objects.equipment.armor.AbstractArmor;

/** A robe top
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public abstract class RobeTop extends AbstractArmor{
	/** The type the armor */
	private static final ArmorType ARMOR_TYPE = ArmorType.CHEST;

	/** The icon path of the armor */
	private static final String ICON_PATH = 
			"/equipment/armor/icons/robe_top.png";
	
	/** The sheet path for the armor */
	private static final String SHEET_PATH = 
			"/equipment/armor/robe_top.png";
	
	/** The horizontal size of the visible armor */
	private static final int TILE_WIDTH = 20;
	
	/** The vertical size of the visible armor */
	private static final int TILE_HEIGHT = 23; 
	
	/** The offset to the player's X position if worn */
	private static final int OFFSET_X = -10;
	
	/** The offset to the player's Y position if worn */
	private static final int OFFSET_Y = -50;
	
	/** The amount of damage reduction by a standard robe bottom */
	private static final int ROBE_TOP_DAMAGE_REDUCTION = 3;
	
	/** The primary color of the robe bottom */
	private int color1;

	/** The accent color of the robe bottom */
	private int color2;
	
	/** Initialize a robe bottom
	 * 
	 * @param name - the name of the robe bottom
	 * @param rgb1 - the primary color of the robe bottom
	 * @param rgb2 - the secondary color of the robe bottom
	 */
	public RobeTop(String name, int rgb1, int rgb2) {
		super (name,
				ARMOR_TYPE,
				ROBE_TOP_DAMAGE_REDUCTION,
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
	 * @see edu.cs495.game.objects.equipment.armor.AbstractArmor#init(edu.cs495.engine.GameDriver)
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