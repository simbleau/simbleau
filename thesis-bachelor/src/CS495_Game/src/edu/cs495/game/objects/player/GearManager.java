/**
 * 
 */
package edu.cs495.game.objects.player;

import java.util.LinkedList;
import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.game.objects.equipment.AbstractItem;
import edu.cs495.game.objects.equipment.armor.AbstractArmor;
import edu.cs495.game.objects.equipment.armor.AbstractArmor.ArmorType;
import edu.cs495.game.objects.equipment.armor.helmets.ModCrown;
import edu.cs495.game.objects.equipment.gloves.AbstractGlove;

/** The gear manager for a player
 * @author Spencer Imbleau
 */
public class GearManager {
	
	/** Game driver reference */
	private GameDriver gameDriver;
	
	/** A Moderator Crown is loaded here for reference if the player is a moderator */
	private AbstractArmor modCrown = null;
	
	/** The player */
	private Player player;
	
	/** The player's helmet */
	private AbstractArmor helmet = null;
	/** The player's chest armor */
	private AbstractArmor chest = null;
	/** The player's leg armor */
	private AbstractArmor legs = null;
	/** The player's shoes */
	private AbstractArmor shoes = null;
	/** The active primary glove */
	private AbstractGlove primaryGlove = null;
	/** The active secondary glove */
	private AbstractGlove secondaryGlove = null;

	
	/** The amount of items in the player's inventory */
	public static final int MAX_INVENTORY_SIZE = 12;
	/** The inventory */
	private LinkedList<AbstractItem> inventory;

	
	/** Initialize a gear manager for a player
	 * @param player : the object for the player
	 */
	public GearManager(GameDriver gameDriver, Player player) {
		this.gameDriver = gameDriver;
		this.player = player;
		this.inventory = new LinkedList<>();
		
		PlayerPrivileges privilege = player.getConfig().getPrivilege();
		if (privilege == PlayerPrivileges.ADMIN ||
				privilege == PlayerPrivileges.MODERATOR) {
			equipModCrown();
		}

	}

	//============================
	//Returning
	
	
	/** @return the player's helmet */
	public AbstractArmor getHelmet() {
		return helmet;
	}
	
	/** @return the player's chest armor */
	public AbstractArmor getChestArmor() {
		return chest;
	}

	/** @return the player's leg armor */
	public AbstractArmor getLegArmor() {
		return legs;
	}

	/** @return the player's shoes */
	public AbstractArmor getShoes() {
		return shoes;
	}

	/** @return the equipped primary glove */
	public AbstractGlove getPrimaryGlove() {
		return this.primaryGlove;
	}
	
	/** @return the equipped secondary glove */
	public AbstractGlove getSecondaryGlove() {
		return this.secondaryGlove;
	}
	
	//============================
	//Armor Checking
	
	/** @return true if the player has a helmet, false otherwise */
	public boolean hasHelmet() {
		return (helmet != null);		
	}

	/** @return true if the player has chest armor, false otherwise */
	public boolean hasChestArmor() {
		return (chest != null);
	}
	
	/** @return true if the player has leg armor, false otherwise */
	public boolean hasLegArmor() {
		return (legs != null);
	}
	
	/** @return true if the player has shoes, false otherwise */
	public boolean hasShoes() {
		return (shoes != null);
	}
	
	/** @return true if the player has a primary glove, false otherwise */
	public boolean hasPrimaryGlove() {
		return (primaryGlove != null);
	}
	
	/** @return true if the player has a secondary, false otherwise */
	public boolean hasSecondaryGlove() {
		return (secondaryGlove != null);
	}
	
	//============================
	//Equipping
	
	/** Set's a player's armor
	 * 
	 * @param armor - a piece of armor
	 * @return true if the armor was equipped, false otherwise
	 */
	public boolean equip(AbstractArmor armor) {
		
		switch (armor.getType()) {
		case HELMET:
			return equipHelmet(armor);
		case CHEST:
			return equipChest(armor);
		case LEG:
			return equipLegs(armor);
		case SHOES:
			return equipShoes(armor);
		case P_GLOVE:
			return equipPrimaryGlove((AbstractGlove) armor);
		case S_GLOVE:
			return equipSecondaryGlove((AbstractGlove) armor);
		default:
			return false;
		}
		
	}
	
	/** Set's a player's weapon
	 * 
	 * @param armor - a piece of armor
	 * @return true if the armor was equipped, false otherwise
	 */
	public boolean equip(AbstractGlove glove) {
		
		if (glove.getType() == ArmorType.P_GLOVE) {
			return equipPrimaryGlove(glove);
		} else if (glove.getType() == ArmorType.S_GLOVE) {
			return equipSecondaryGlove(glove);
		} else {
			return false;
		}
		
	}
	

	/** Sets the player's helmet to the mod crown */
	public void equipModCrown() {
		if (modCrown == null) {
			modCrown = new ModCrown(player.getConfig().getPrivilege());
			modCrown.init(gameDriver);
		}
		
		if (hasHelmet()) {
			return;
		} else {
			DeveloperLog.printLog("Mod Crown equipped");
			this.helmet = modCrown;	
		}
	}
	
	/** Sets the player's helmet
	 * 
	 * @param helmet - the helmet to set
	 * @return true if the helmet was equipped, false otherwise
	 */
	private boolean equipHelmet(AbstractArmor helmet) {
		if (this.helmet instanceof ModCrown) {
			this.helmet = helmet;
			remove(helmet);
			return true;
		}
		
		//If we have a helmet, we need to unequip or swap it
		if (hasHelmet()) {
			//Swap or unequip
			if (isInventoryFull()) {
				return swapHelmet(helmet);
			} else {
				switch (player.getConfig().getPrivilege()) {
				case ADMIN:
				case MODERATOR:
					take(this.helmet);
					this.helmet = helmet;
					remove(helmet);
					return true;
					default:
						unequipHelmet();
						break;
				}
			}
		}
		
		//If we have no helmet, check if our new helmet is legit
		if (!hasHelmet()) {
			this.helmet = helmet;
			remove(helmet);
			return true;
		} else {
			helmet.drop(gameDriver.getLevel(), 
					this,
					(int) player.getPosX(), 
					(int) player.getPosY());
			return false;
		}
	}
	
	/** Sets the player's chest armor
	 * @param chest : the chest armor to set
	 */
	public boolean equipChest(AbstractArmor chest) {
		//If we have a chest, try to unequip it.
		if (hasChestArmor()) {
			if (isInventoryFull()) {
				swapChest(chest);
				return true;
			} else {
				unequipChest();
			}
		}
				
		//If it got removed, let's equip the new one
		if (!hasChestArmor()) {
			this.chest = chest;
			remove(chest);
			return true;
		} else {
			chest.drop(gameDriver.getLevel(), 
					this,
					(int) player.getPosX(), 
					(int) player.getPosY());
			return false;
		}
	}
	
	/** Sets the player's leg armor
	 * @param legs : the leg armor to set
	 */
	private boolean equipLegs(AbstractArmor legs) {
		if (hasLegArmor()) {
			if (isInventoryFull()) {
				swapLegs(legs);
				return true;
			} else {
				unequipLegs();
			}
		}
				
		//If it got removed, let's equip the new one
		if (!hasLegArmor()) {
			this.legs = legs;
			remove(legs);
			return true;
		} else {
			legs.drop(gameDriver.getLevel(), 
					this,
					(int) player.getPosX(), 
					(int) player.getPosY());
			return false;
		}
	}
	
	/** Sets the player's shoes
	 * @param shoes : the shoes to set
	 */
	private boolean equipShoes(AbstractArmor shoes) {
		if (hasShoes()) {
			if (isInventoryFull()) {
				swapShoes(shoes);
				return true;
			} else {
				unequipChest();
			}
		}
				
		//If it got removed, let's equip the new one
		if (!hasShoes()) {
			this.shoes = shoes;
			if (inventory.contains(shoes)) {
				remove(shoes);
			}
			return true;
		} else {
			shoes.drop(gameDriver.getLevel(), 
					this,
					(int) player.getPosX(), 
					(int) player.getPosY());
			return false;
		}
	}
	
	/** Equip a primary spell glove
	 * 
	 * @param glove - the glove to set
	 */
	public boolean equipPrimaryGlove(AbstractGlove glove) {
		glove.setTheta(player.getLeftArmTheta());
		
		if (hasPrimaryGlove()) {
			if (isInventoryFull()) {
				swapPrimaryGlove(glove);
				return true;
			} else {
				unequipPrimaryGlove();
			}
		}
				
		//If it got removed, let's equip the new one
		if (!hasPrimaryGlove()) {
			this.primaryGlove = glove;
			remove(glove);
			return true;
		} else {
			glove.drop(gameDriver.getLevel(), 
					this,
					(int) player.getPosX(), 
					(int) player.getPosY());
			return false;
		}
	}
	
	/** Equip a secondary spell glove
	 * @param glove - the glove to set
	 * 
	 */
	public boolean equipSecondaryGlove(AbstractGlove glove) {
		glove.setTheta(player.getRightArmTheta());
		
		if (hasSecondaryGlove()) {
			if (isInventoryFull()) {
				swapSecondaryGlove(glove);
				return true;
			} else {
				unequipSecondaryGlove();
			}
		}
				
		//If it got removed, let's equip the new one
		if (!hasSecondaryGlove()) {
			this.secondaryGlove = glove;
			remove(glove);
			return true;
		} else {
			glove.drop(gameDriver.getLevel(), 
					this,
					(int) player.getPosX(), 
					(int) player.getPosY());
			return false;
		}
	}
	
	//Swapping
	
	/** Swap helmets with an item which exists in the inventory
	 *
	 * @param newHelmet - the helmet to swap
	 */
	private boolean swapHelmet(AbstractArmor newHelmet) {	
		AbstractArmor current = this.helmet;
		this.helmet = newHelmet;
		inventory.set(getIndexOf(newHelmet), current);
		return true;
	}
	/** Swap chest armor with an item which exists in the inventory
	 *
	 * @param newChest - the chest to swap
	 */
	private void swapChest(AbstractArmor newChest) {
		AbstractArmor current = this.chest;
		this.chest = newChest;
		inventory.set(getIndexOf(newChest), current);
	}
	/** Swap legs armor with an item which exists in the inventory
	 *
	 * @param newlegs - the legs to swap
	 */
	private void swapLegs(AbstractArmor newLegs) {
		AbstractArmor current = this.legs;
		this.legs = newLegs;
		inventory.set(getIndexOf(newLegs), current);
	}
	/** Swap shoes armor with an item which exists in the inventory
	 *
	 * @param newshoes - the shoes to swap
	 */
	private void swapShoes(AbstractArmor newShoes) {
		AbstractArmor current = this.shoes;
		this.shoes = newShoes;
		inventory.set(getIndexOf(newShoes), current);
	}
	/** Swap primary glove with a glove which exists in the inventory
	 *
	 * @param newGlove - a new glove to equip
	 */
	private void swapPrimaryGlove(AbstractGlove newGlove) {
		AbstractGlove current = this.primaryGlove;
		this.primaryGlove = newGlove;
		inventory.set(getIndexOf(newGlove), current);
	}
	/** Swap secondary glove with a glove which exists in the inventory
	 *
	 * @param newGlove - a new glove to equip
	 */
	private void swapSecondaryGlove(AbstractGlove newGlove) {
		AbstractGlove current = this.secondaryGlove;
		this.secondaryGlove = newGlove;
		inventory.set(getIndexOf(newGlove), current);
	}
	
	
	/** Unequip the helmet */
	public boolean unequipHelmet() {
		if (take(helmet)) {
			this.helmet = null;
			if (player.getConfig().getPrivilege() == PlayerPrivileges.ADMIN ||
					player.getConfig().getPrivilege() == PlayerPrivileges.MODERATOR) {
				equipModCrown();
			}
			return true;
		} else {
			return false;
		}
	}
	
	/** Unequip the chest armor */
	public boolean unequipChest() {
		if (take(chest)) {
			this.chest = null;
			return true;
		} else {
			return false;
		}
	}
	
	/** Unequip the leg armor */
	public boolean unequipLegs() {
		if (take(legs)) {
			this.legs = null;
			return true;
		} else {
			return false;
		}
	}
	
	/** Unequip the shoes */
	public boolean unequipShoes() {
		if (take(shoes)) {
			this.shoes = null;
			return true;
		} else {
			return false;
		}
	}
	
	/** Unequip the primary glove */
	public boolean unequipPrimaryGlove() {
		if (take(primaryGlove)) {
			this.primaryGlove = null;
			return true;
		} else {
			return false;
		}
	}

	/** Unequip the primary glove */
	public boolean unequipSecondaryGlove() {
		if (take(secondaryGlove)) {
			this.secondaryGlove = null;
			return true;
		} else {
			return false;
		}
	}
	
	//============================
	//Functions
	
	/** Return whether the inventory is empty
	 *  
	 * @return true if the inventory is empty, false otherwise 
	 */
	public boolean isInventoryEmpty() {
		return (getAvailableSpace() == MAX_INVENTORY_SIZE);
	}
	
	/** @return true if the inventory is full, false otherwise */
	public boolean isInventoryFull() {
		return (getAvailableSpace() <= 0);
	}
	
	/** Return the inventory's current size
	 * @return the inventory's current size
	 */
	public int getItemCount() {
		return inventory.size();
	}
	
	/** Returns the amount of available inventory spaces
	 * @return the amount of available inventory spaces
	 */
	public int getAvailableSpace() {
		return MAX_INVENTORY_SIZE - inventory.size();
	}
	
	/** Returns whether a slot is null
	 * @param slot : the index in the inventory to grab
	 * @return true if the slot is not null, false if it is
	 */
	public boolean itemExistsAt(int slot) {
		if (inventory.size() > slot)
			return true;
		else
			return false;
	}
	
	
	/** Returns the iventory item at a given index
	 * @param slot : the index in the inventory to grab
	 * @return The item in the inventory (or null)
	 */
	public AbstractItem getInventoryItem(int slot) {
		if (inventory.size() > slot)
			return inventory.get(slot);
		return null;
	}
	
	/** Returns if an item is in the inventory
	 * @param item : the item to check the inventory for
	 * @return
	 */
	public boolean inventoryContains(AbstractItem item) {
		return inventory.contains(item);
	}
	
	/** Returns the item slot number
	 * @param item : the item to return the slot number for
	 * @return the item slot number
	 */
	public int getIndexOf(AbstractItem item) {
		return inventory.indexOf(item);
	}
	
	/** Places an item in the nearest inventory slot or drops it if there's no space
	 * 
	 * @param item - The item to place in the inventory
	 * @return true if the item is in the inventory, false otherwise
	 */
	public boolean take(AbstractItem item) {
		if (!isInventoryFull()) {
			inventory.add(item);
			return true;
		} else {
			return false;
		}
	}


	/** Remove an item from the inventory
	 * @return true if the item was removed, false otherwise */
	public boolean remove(AbstractItem item) {
		return inventory.removeFirstOccurrence(item);
	}
	
}
