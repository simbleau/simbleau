package edu.cs495.game.objects.player;

/** Enumerates player sizes */
public enum PlayerSizes {
	
		/** The amount of padding between a username icon and the username */
		USERNAME_ICON_PADDING(1),
		
		/** The width of the player sprite */
		WIDTH(32),
		
		/** The half width of a player */
		HALF_WIDTH(WIDTH.size / 2),
		
		/** The height of the player sprite */
		HEIGHT(64),
		
		/** The magnitude of extrusion to the player's hitbox */
		DEPTH(12),
		
		/** The radius of the player's head hitbox */
		HEAD_RADIUS(7),
		
		/** The width of the hitbox */
		HITBOX_WIDTH(20),
		
		/** The height of the hitbox */
		HITBOX_HEIGHT(50),
		
		/** The width of the player's health bar */
		HEALTH_BAR_WIDTH(WIDTH.size),
		
		/** The height of the player's health bar */
		HEALTH_BAR_HEIGHT(2);

		/** The value of the size */
		final int size;

		/**
		 * Initialize the enumerated value with a size
		 * 
		 * @param size : the size value of the enumeration
		 */
		PlayerSizes(int size) {
			this.size = size;
		}
}
