/**
 * 
 */
package edu.cs495.game.objects.equipment.armor.helmets;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.gfx.obj.filters.ColorSwapFilter;
import edu.cs495.game.objects.equipment.armor.AbstractArmor;
import edu.cs495.game.objects.player.Player;
import edu.cs495.game.objects.player.PlayerPrivileges;

/** A helmet, the Mod Crown.
 * @author Spencer Imbleau
 */
public class ModCrown extends AbstractArmor {
	
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
	private final int crownColor;
	/** The gem color of this crown */
	private final int gemColor;
	
	/** Properly initialize the Mod Crown */
	public ModCrown(PlayerPrivileges privilege) {
		super("Moderator Crown",
				ARMOR_TYPE,
				DAMAGE_REDUCTION, 
				SHEET_PATH, 
				TILE_WIDTH,
				TILE_HEIGHT,
				ICON_PATH, 
				OFFSET_X, 
				OFFSET_Y);
		
		if (privilege == PlayerPrivileges.ADMIN) {
			crownColor = 0xe7cc36;
			gemColor = 0xde1fde;
			this.name = "Admin Crown";
		} else if (privilege == PlayerPrivileges.MODERATOR) {
			crownColor = 0xcac9c1;
			gemColor = 0x252525;
		} else {
			//Normal 'awarded' mod crown
			crownColor = -1;
			gemColor = -1;
		}
		
	}

	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractGameObject#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		super.init(gameDriver);
		
		setUseText("Destroy > " + this.name);
		setAltText("Toggle Debugging");
		
		
		//Color Filter
		ColorSwapFilter colorReplacementFilter = new ColorSwapFilter();
		colorReplacementFilter.addColorSwap(0xff00ff, crownColor);
		colorReplacementFilter.addColorSwap(0xff0000, gemColor);
		
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

	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.equipment.armor.AbstractArmor#use(edu.cs495.engine.GameDriver, edu.cs495.game.objects.Player)
	 */
	@Override
	public void use(GameDriver gameDriver, Player player) {
		player.getGearManager().remove(this);
	}

	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.equipment.armor.AbstractArmor#alt(edu.cs495.engine.GameDriver, edu.cs495.game.objects.Player)
	 */
	@Override
	public void alt(GameDriver gameDriver, Player player) {
		DeveloperLog.toggleDebug();
		if (DeveloperLog.isDebugging()) {
			gameDriver.getRenderer().setMap(gameDriver.getLevel().getBoundaries().getImage());
		} else {
			gameDriver.getRenderer().setMap(gameDriver.getLevel().getMap());
		}
	}

	
	
}
