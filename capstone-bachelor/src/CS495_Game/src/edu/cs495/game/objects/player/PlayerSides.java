package edu.cs495.game.objects.player;

/** Enumerates identifiers for which side the player faces */
public enum PlayerSides {
	
	/** Center facing identifier */
	CENTER(0, Math.atan2(-90, 0)),
	/** Left facing identifier */
	LEFT(1, Math.atan2(-90, -20)),
	/** Right facing identifier */
	RIGHT(2, Math.atan2(-90, 20));

	final int spriteIndex;
	/** The angle in radians the player's arms are at rest */
	final double restAngle;

	/**
	 * Initialize the enumerated value with a directional identifier
	 * 
	 * @param side      - the value of the enumeration
	 * @param restAngle - an angle radians for which the player's arms rest
	 */
	PlayerSides(int spriteIndex, double restAngle) {
		this.spriteIndex = spriteIndex;
		this.restAngle = restAngle;
	}
}
