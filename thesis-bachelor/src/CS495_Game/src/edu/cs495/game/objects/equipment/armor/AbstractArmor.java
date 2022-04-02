/**
 * 
 */
package edu.cs495.game.objects.equipment.armor;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.obj.FilteredSpriteSheet;
import edu.cs495.engine.gfx.obj.Font;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.gfx.obj.SpriteSheet;
import edu.cs495.game.objects.equipment.AbstractItem;
import edu.cs495.game.objects.player.Player;

/** A piece of armor for a player
 * @author Spencer Imbleau
 */
public abstract class AbstractArmor extends AbstractItem {
	
	/** Enumeration of types of armor */
	public static enum ArmorType {
		HELMET,
		CHEST,
		LEG,
		SHOES,
		P_GLOVE,
		S_GLOVE;
	}
	
	/**The type of armor this is */
	protected ArmorType type;
	
	/** The offset to the player's X position */
	protected int offsetX;
	
	/** The offset to the player's Y position */
	protected int offsetY;
	
	/** The amount of damage this reduces */
	protected int damageReduction;
	
	/** A damage reduction image to display */
	protected Image damageReductionImage;
	
	/** The path of the sprite sheet */
	protected String assetPath;
	
	/** The armor sprite sheet */
	protected FilteredSpriteSheet spriteSheet;
	
	
	/** Initialize an abstract armor
	 * 
	 * @param name - the name of the armor 
	 * @param armorType - the type of the armor
	 * @param damageReduction - the amount of damage reduction
	 * @param spriteSheetPath - the path of the sprite sheet
	 * @param tileWidth - the width of the armor
	 * @param tileHeight - the height of the armor
	 * @param iconPath - the path of the icon
	 * @param offX - the x offset to the player's position
	 * @param offY - the y offset to the player's position
	 */
	public AbstractArmor(
			String name,
			ArmorType armorType,
			int damageReduction,
			String spriteSheetPath,
			int tileWidth,
			int tileHeight,
			String iconPath,
			int offX,
			int offY) {

		super(name, "-" + damageReduction, 0, 0, iconPath, "Equip > " + name, "Unequip > " + name);
		
		this.type = armorType;
		this.damageReduction = damageReduction;
		
		//Sprite sheet
		this.assetPath = spriteSheetPath;
		this.width = tileWidth;
		this.height = tileHeight;
		
		//Set offsets
		this.offsetX = offX;
		this.offsetY = offY;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.equipment.AbstractItem#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		super.init(gameDriver);
		this.damageReductionImage = Font.SMALL.getStringImage("-" + damageReduction, 0xff00ffff);
		
		//SpriteSheet
		FilteredSpriteSheet spriteSheet = 
				new FilteredSpriteSheet(assetPath, width, height);
		this.spriteSheet = spriteSheet;
	}

	/** Returns the offset to the player's X position for proper clipping 
	 * 
	 * @return the x offset to the player's position
	 */
	public int getPosXOffset() {
		return offsetX;
	}
	
	/** Returns the offset to the player's Y position for proper clipping 
	 * 
	 * @return the y offset to the player's position
	 */
	public int getPosYOffset() {
		return offsetY;
	}
	
	/** Return the sprite sheet of this armor piece 
	 * 
	 * @return the item's sprite sheet 
	 */
	public SpriteSheet getSpriteSheet() {
		return spriteSheet;
	}
	
	/** Return the damage reduction text image
	 * 
	 * @return the damage reduction text image
	 */
	public Image getDamageReductionTextImage() {
		return this.damageReductionImage;
	}
	
	/** Return the amount of damage reduction given by this armor
	 * 
	 *  @return the item's damage reduction 
	 */
	public int getDamageReduction() {
		return damageReduction;
	}
	
	/** Returns the type of this armor
	 * 
	 * @return the type of armor this is
	 */
	public ArmorType getType() {
		return this.type;
	}

	
	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.equipment.AbstractItem#use(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void use(GameDriver gameDriver, Player player) {
		player.getGearManager().equip(this);
	}
	
	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.equipment.AbstractItem#use(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void alt(GameDriver gameDriver, Player player) {
		switch (type) {
		case HELMET:
			player.getGearManager().unequipHelmet();
			break;
		case CHEST:
			player.getGearManager().unequipChest();
			break;
		case LEG:
			player.getGearManager().unequipLegs();
			break;
		case SHOES:
			player.getGearManager().unequipShoes();
			break;
		case P_GLOVE:
			player.getGearManager().unequipPrimaryGlove();
			break;
		case S_GLOVE:
			player.getGearManager().unequipSecondaryGlove();
			break;
		default:
			break;
		}
	}



}
