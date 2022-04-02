package edu.cs495.game.objects.player;

/** Enumerates sprite indices for which direction the player faces */
public enum PlayerDirections {
	
	/** Forward facing identifier */
	FORWARD(0),
	
	/** Backward facing identifier */
	BACKWARD(1);

	/** The value which corresponds to the row of a sprite sheet
	 * to render forward or backward clothing/arms/etc.
	 */
	final int spriteIndex;

	/**
	 * Initialize the enumerated value with a directional identifier
	 * 
	 * @param side : the value of the enumeration
	 */
	PlayerDirections(int spriteIndex) {
		this.spriteIndex = spriteIndex;
	}
}
