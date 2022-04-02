package edu.cs495.game.objects.player;


public enum PlayerColors {
	
	/** The color of health in the health bar */
	HEALTH(0xff00ff00),
	/** The color of damage in the health bar */
	DAMAGE(0xffff0000),
	
	/** The color of an administrator shadow (RGB) */
	ADMIN_SHADOW(0xe7cc36),
	
	/** The color of a moderator shadow (RGB) */
	MODERATOR_SHADOW(0xcac9c1),
	
	/** The color of a normal shadow (RGB) */
	SHADOW(0x232323),
	
	
	/** The color of the username under the player */
	USERNAME(0xffffffff);
	
	/** The value of the color */
	final int color;

	/**
	 * Initialize the enumerated value with a color
	 * 
	 * @param color - the color value of the enumeration
	 */
	PlayerColors(int color) {
		this.color = color;
	}
}
