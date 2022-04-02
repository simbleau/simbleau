package edu.cs495.game.objects.player;


/** Enumerates offsets for rendering */
public enum PlayerOffsets {
	
	/** The amount of pixels the health bar is above the player */
	HEALTH_BAR(-3),
	
	/** The amount of pixels the dialogue shows above the player */
	DIALOGUE(-5),
	
	/** The amount of pixels the left eye is offset to Offset X */
	LEFT_EYE_X(12),
	
	/** The amount of pixels the right eye is offset to Offset Y */
	EYES_Y(5),
	
	/** The amount of pixels the right eye is offset to Offset X */
	RIGHT_EYE_X(18),
	
	/** The amount of pixels the shirt is offset to the Offset X */
	DEFAULT_SHIRT_X(7),
	
	/** The amount of pixels the shirt is offset to the Offset Y */
	DEFAULT_SHIRT_Y(14),
	
	/** The amount of pixels the pants is offset to the Position Y */
	DEFAULT_PANTS(-27),
	
	/** The amount of pixels the shadow is offset to the Position Y */
	SHADOW(-9),
	
	/** The amount of pixels the left arm is offset to the Position X */
	LEFT_ARM_X(-26),
	
	/** The amount of pixels the left arm is offset to the Position X */
	RIGHT_ARM_X(-12),
	
	/** The amount of pixels the arms are offset to the Offset Y */
	ARMS_Y(-2),
	
	/** The amount of pixels below the player the username is rendered */
	USERNAME(1),

	/** The starting X for the hitbox */
	HITBOX_X(6),
	
	/** The starting Y for the hitbox */
	HITBOX_Y(14),
	
	/** The center of the player's head (crit box) at X position */
	CENTER_HEAD_X(16),
	
	/** The center of the player's head (crit box) at Y position */
	CENTER_HEAD_Y(PlayerSizes.HEAD_RADIUS.size);
	
	
	final int offset;

	/**
	 * Initialize the enumerated value with an offset
	 * 
	 * @param offset : the offset value of the enumeration
	 */
	PlayerOffsets(int offset) {
		this.offset = offset;
	}
}