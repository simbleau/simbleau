package edu.cs495.game.objects.player.menus;

/** Enumerates identifiers for which interface may be open */
public enum MenuTypes {
	/** No active menu */
	NONE,
	
	/** The Escape AbstractMenu (Contains logout/settings) */
	ESCAPE,
	
	/** Inventory AbstractMenu (Shows the player's inventory) */
	INVENTORY,
	
	/** Gear AbstractMenu (Shows the player's equipped items) */
	GEAR,
	
	/** Statistics AbstractMenu (Shows the player's statistics) */
	STATS;
	
}